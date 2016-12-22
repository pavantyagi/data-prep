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

package org.talend.dataprep.preparation.configuration;

import static org.talend.dataprep.conversions.BeanConversionService.RegistrationBuilder.fromBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.talend.dataprep.api.preparation.Preparation;
import org.talend.dataprep.api.preparation.PreparationMessage;
import org.talend.dataprep.conversions.BeanConversionService;
import org.talend.dataprep.preparation.conversion.PreparationMessageConverter;
import org.talend.dataprep.preparation.conversion.SimpleUserPreparationConverter;
import org.talend.dataprep.preparation.service.UserPreparation;

@Configuration
public class PreparationConversions {

    @Autowired
    private PreparationMessageConverter preparationMessageConverter;

    @Autowired
    private SimpleUserPreparationConverter userPreparationConverter;


    @Bean
    public PreparationConversionsInitialization preparationConversionsInitialization() {
        return new PreparationConversionsInitialization();
    }

    private class PreparationConversionsInitialization implements BeanPostProcessor {

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) {
            if (bean instanceof BeanConversionService) {
                final BeanConversionService conversionService = (BeanConversionService) bean;
                conversionService //
                        .register(fromBean(Preparation.class) //
                        .toBeans(PreparationMessage.class, UserPreparation.class) //
                                .using(PreparationMessage.class, preparationMessageConverter::toPreparationMessage) //
                                .using(UserPreparation.class, userPreparationConverter::toUserPreparation) //
                        .build()
                );
                return conversionService;
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) {
            return bean;
        }

    }
}
