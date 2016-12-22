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

package org.talend.dataprep.dataset.configuration;

import static org.talend.dataprep.conversions.BeanConversionService.RegistrationBuilder.fromBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.talend.dataprep.api.dataset.DataSetMetadata;
import org.talend.dataprep.api.user.UserData;
import org.talend.dataprep.conversions.BeanConversionService;
import org.talend.dataprep.dataset.service.messages.UserDataSetMetadata;
import org.talend.dataprep.security.Security;
import org.talend.dataprep.user.store.UserDataRepository;

@Configuration
public class DataSetConversions {

    @Autowired
    private Security security;

    @Autowired
    private UserDataRepository userDataRepository;

    @Bean
    public DataSetConversionsInitialization dataSetConversionsInitialization() {
        return new DataSetConversionsInitialization();
    }

    private class DataSetConversionsInitialization implements BeanPostProcessor {

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) {
            if (bean instanceof BeanConversionService) {
                final BeanConversionService conversionService = (BeanConversionService) bean;
                conversionService.register(fromBean(DataSetMetadata.class) //
                        .toBeans(UserDataSetMetadata.class) //
                        .using(UserDataSetMetadata.class, (metadata, userMetadata) -> {
                            String userId = security.getUserId();
                            final UserData userData = userDataRepository.get(userId);
                            if (userData != null) {
                                userMetadata.setFavorite(userData.getFavoritesDatasets().contains(metadata.getId()));
                            }
                            return userMetadata;
                        }) //
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
