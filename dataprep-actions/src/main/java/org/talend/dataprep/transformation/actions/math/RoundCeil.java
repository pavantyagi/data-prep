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
package org.talend.dataprep.transformation.actions.math;

import org.talend.dataprep.transformation.actions.common.AbstractActionMetadata;
import org.talend.dataprep.transformation.actions.common.ActionMetadata;
import org.talend.dataprep.transformation.actions.common.DataprepAction;

import java.math.RoundingMode;

/**
 * Returns the smallest (closest to negative infinity) value that is greater than or equal to the value and is equal to
 * a mathematical integer.
 * 
 * @see RoundingMode#CEILING
 */
@DataprepAction(AbstractActionMetadata.ACTION_BEAN_PREFIX + RoundCeil.ACTION_NAME)
public class RoundCeil extends AbstractRound {

    /** The action name. */
    public static final String ACTION_NAME = "ceil"; //$NON-NLS-1$

    /**
     * @see ActionMetadata#getName()
     */
    @Override
    public String getName() {
        return ACTION_NAME;
    }

    @Override
    protected RoundingMode getRoundingMode() {
        return RoundingMode.CEILING;
    }
}