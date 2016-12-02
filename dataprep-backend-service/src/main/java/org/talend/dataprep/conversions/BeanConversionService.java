// ============================================================================
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// https://github.com/Talend/data-prep/blob/master/LICENSE
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================

package org.talend.dataprep.conversions;

import static java.util.stream.Stream.of;

import java.util.*;
import java.util.function.BiFunction;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * This service provides methods to convert beans to other beans (DTOs, transient beans...). This service helps code to
 * separate between core business code and representations for various use cases.
 */
@Service
public class BeanConversionService implements ConversionService {

    private final Map<Class<?>, Registration> registrations = new HashMap<>();

    public void register(Registration registration) {
        registrations.put(registration.modelClass, registration);
    }

    public boolean has(Class<?> modelClass) {
        return registrations.containsKey(modelClass);
    }

    public void clear() {
        registrations.clear();
    }

    @Override
    public boolean canConvert(TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor1) {
        return canConvert(typeDescriptor.getType(), typeDescriptor1.getType());
    }

    @Override
    public boolean canConvert(Class<?> aClass, Class<?> aClass1) {
        return ObjectUtils.nullSafeEquals(aClass, aClass1) || has(aClass) && of(registrations.get(aClass))
                .anyMatch(registration -> of(registration.convertedClasses).anyMatch(aClass1::equals));
    }

    /**
     * Similar {@link #convert(Object, Class)} but allow user to specify a constant conversion that overrides previously defined conversions.
     * @param source The bean to convert.
     * @param aClass The target class for conversion.
     * @param onTheFlyConvert The function to apply on the transformed bean.
     * @param <U> The source type.
     * @param <T> The target type.
     * @return The converted bean (typed as <code>T</code>).
     */
    public <U, T> T convert(U source, Class<T> aClass, BiFunction<U, T, T> onTheFlyConvert) {
        try {
            T converted = aClass.newInstance();
            BeanUtils.copyProperties(source, converted);
            return onTheFlyConvert.apply(source, converted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T convert(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            T converted = targetClass.newInstance();
            BeanUtils.copyProperties(source, converted);
            final Class<?> sourceClass = source.getClass();
            Registration<T> registration = registrations.get(sourceClass);

            if (registration != null) {
                List<BiFunction<Object, Object, Object>> customs = new ArrayList<>();
                Class currentClass = targetClass;
                while (currentClass != null) {
                    final BiFunction<Object, Object, Object> custom = registration.customs.get(currentClass);
                    if (custom != null) {
                        customs.add(custom);
                    }
                    currentClass = currentClass.getSuperclass();
                }

                T result = converted;
                for (BiFunction<Object, Object, Object> current : customs) {
                    result = (T) current.apply(source, converted);
                }
                return result;
            } else {
                return converted;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object convert(Object o, TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor1) {
        return convert(o, typeDescriptor1.getObjectType());
    }

    private static class Registration<T> {

        private final Class<T> modelClass;

        private final Class<?>[] convertedClasses;

        private final Map<Class<?>, BiFunction<Object, Object, Object>> customs;

        private Registration(Class<T> modelClass, Class<?>[] convertedClasses,
                Map<Class<?>, BiFunction<Object, Object, Object>> customs) {
            this.modelClass = modelClass;
            this.convertedClasses = convertedClasses;
            this.customs = customs;
        }
    }

    public static class RegistrationBuilder<T> {

        private final List<Class<?>> destinations = new ArrayList<>();

        private final Class<T> source;

        private final Map<Class<?>, BiFunction<Object, Object, Object>> customs = new HashMap<>();

        private RegistrationBuilder(Class<T> source) {
            this.source = source;
        }

        public static <T> RegistrationBuilder<T> fromBean(Class<T> source) {
            return new RegistrationBuilder<>(source);
        }

        public RegistrationBuilder<T> toBeans(Class<?>... destinations) {
            Collections.addAll(this.destinations, destinations);
            return this;
        }

        public <U> RegistrationBuilder<T> using(Class<U> destination, BiFunction<T, U, U> custom) {
            customs.put(destination, (BiFunction<Object, Object, Object>) custom);
            return this;
        }

        public Registration<T> build() {
            return new Registration<>(source, destinations.toArray(new Class<?>[destinations.size()]), customs);
        }

    }

}
