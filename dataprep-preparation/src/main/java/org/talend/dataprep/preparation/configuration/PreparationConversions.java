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

import static java.util.stream.Collectors.toList;
import static org.talend.dataprep.conversions.BeanConversionService.RegistrationBuilder.fromBean;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.talend.dataprep.api.action.ActionDefinition;
import org.talend.dataprep.api.preparation.*;
import org.talend.dataprep.api.share.Owner;
import org.talend.dataprep.conversions.BeanConversionService;
import org.talend.dataprep.preparation.service.UserPreparation;
import org.talend.dataprep.preparation.store.PreparationRepository;
import org.talend.dataprep.security.Security;
import org.talend.dataprep.transformation.pipeline.ActionRegistry;

@Configuration
public class PreparationConversions {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreparationConversions.class);

    /** The root step. */
    @Resource(name = "rootStep")
    private Step rootStep;

    @Autowired
    private PreparationRepository preparationRepository;

    @Autowired
    private ActionRegistry actionRegistry;

    @Autowired
    private PreparationUtils preparationUtils;

    @Autowired
    private Security security;

    private final Predicate<Step> isNotRootStep = step -> !rootStep.id().equals(step.id());

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
                        .using(PreparationMessage.class, (preparation, preparationMessage) -> {
                            final List<Step> steps = preparationUtils.listSteps(preparation.getHeadId(), preparationRepository);
                            preparationMessage.setSteps(steps);

                            // Steps diff metadata
                            final List<StepDiff> diffs = steps.stream().filter(isNotRootStep).map(Step::getDiff).collect(toList());
                            preparationMessage.setDiff(diffs);

                            // Actions
                            final Step head = preparationRepository.get(preparation.getHeadId(), Step.class);
                            if (head != null && head.getContent() != null) {
                                // Get preparation actions
                                PreparationActions prepActions = preparationRepository.get(head.getContent().id(), PreparationActions.class);
                                preparationMessage.setActions(prepActions.getActions());
                                List<Action> actions = prepActions.getActions();

                                // Allow distributed run
                                boolean allowDistributedRun = true;
                                for (Action action : actions) {
                                    final ActionDefinition actionDefinition = actionRegistry.get(action.getName());
                                    if (actionDefinition.getBehavior().contains(ActionDefinition.Behavior.FORBID_DISTRIBUTED)) {
                                        allowDistributedRun = false;
                                        break;
                                    }
                                }
                                preparationMessage.setAllowDistributedRun(allowDistributedRun);

                                // Actions metadata
                                if (actionRegistry == null) {
                                    LOGGER.debug("No action metadata available, unable to serialize action metadata for preparation {}.",
                                            preparation.id());
                                } else {
                                    List<ActionDefinition> actionDefinitions = actions.stream() //
                                            .map(a -> actionRegistry.get(a.getName())) //
                                            .collect(Collectors.toList());
                                    preparationMessage.setMetadata(actionDefinitions);
                                }
                            }
                            return preparationMessage;
                        }) //
                        .using(UserPreparation.class, (preparation, userPreparation) -> {
                            Owner owner = new Owner(security.getUserId(), security.getUserId(), null);
                            userPreparation.setOwner(owner);
                            return userPreparation;
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
