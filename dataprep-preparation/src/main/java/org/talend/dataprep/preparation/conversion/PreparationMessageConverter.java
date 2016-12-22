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

import org.talend.dataprep.api.preparation.Preparation;
import org.talend.dataprep.api.preparation.PreparationMessage;

/**
 * Interface defined to convert Preparation to PreparationMessage.
 */
public interface PreparationMessageConverter {

    /**
     * Return the PreparationMessage from the given Preparation.
     *
     * @param source the original preparation.
     * @param target the target PreparationMessage.
     * @return the PreparationMessage from the given Preparation.
     */
    PreparationMessage toPreparationMessage(Preparation source, PreparationMessage target);

}
