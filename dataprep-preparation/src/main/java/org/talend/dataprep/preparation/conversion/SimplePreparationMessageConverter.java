// ============================================================================
//
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

package org.talend.dataprep.preparation.conversion;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.talend.dataprep.api.action.ActionDefinition;
import org.talend.dataprep.api.action.ActionDefinition.Behavior;
import org.talend.dataprep.api.preparation.*;
import org.talend.dataprep.preparation.store.PreparationRepository;
import org.talend.dataprep.transformation.pipeline.ActionRegistry;

/**
 * Default implementation of the PreparationMessageConverter.
 */
@Component
public class SimplePreparationMessageConverter implements PreparationMessageConverter {

    /** This class' logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SimplePreparationMessageConverter.class);

    /** The preparation repository. */
    @Autowired
    private PreparationRepository preparationRepository;

    /** Preparation utilities. */
    @Autowired
    private PreparationUtils preparationUtils;

    /** Registry that knows all about actions. */
    @Autowired
    private ActionRegistry actionRegistry;

    /** The root step. */
    @Resource(name = "rootStep")
    private Step rootStep;

    @Override
    public PreparationMessage toPreparationMessage(Preparation source, PreparationMessage target) {

        final List<Step> steps = preparationUtils.listSteps(source.getHeadId(), preparationRepository);
        target.setSteps(steps);

        // Steps diff metadata
        final List<StepDiff> diffs = steps.stream().filter(isNotRootStep).map(Step::getDiff).collect(toList());
        target.setDiff(diffs);

        // Actions
        final Step head = preparationRepository.get(source.getHeadId(), Step.class);
        if (head != null && head.getContent() != null) {
            // Get preparation actions
            PreparationActions prepActions = preparationRepository.get(head.getContent().id(), PreparationActions.class);
            target.setActions(prepActions.getActions());
            List<Action> actions = prepActions.getActions();

            // Allow distributed run
            boolean allowDistributedRun = true;
            for (Action action : actions) {
                final ActionDefinition actionDefinition = actionRegistry.get(action.getName());
                if (actionDefinition.getBehavior().contains(Behavior.FORBID_DISTRIBUTED)) {
                    allowDistributedRun = false;
                    break;
                }
            }
            target.setAllowDistributedRun(allowDistributedRun);

            // Actions metadata
            if (actionRegistry == null) {
                LOGGER.debug("No action metadata available, unable to serialize action metadata for preparation {}.",
                        source.id());
            } else {
                List<ActionDefinition> actionDefinitions = actions.stream() //
                        .map(a -> actionRegistry.get(a.getName())) //
                        .collect(Collectors.toList());
                target.setMetadata(actionDefinitions);
            }
        }
        return target;
    }

    /**
     * Return the predicate that tells if a given step is not the root step.
     */
    private final Predicate<Step> isNotRootStep = step -> !rootStep.id().equals(step.id());
}
