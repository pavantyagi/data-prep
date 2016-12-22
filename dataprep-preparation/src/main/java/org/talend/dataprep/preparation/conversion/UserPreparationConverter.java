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
import org.talend.dataprep.preparation.service.UserPreparation;

/**
 * Interface defined to convert Preparation to UserPreparation.
 */
public interface UserPreparationConverter {

    /**
     * Return the UserPreparation from the given Preparation.
     *
     * @param source the original preparation.
     * @param target the target UserPreparation.
     * @return the UserPreparation from the given Preparation.
     */
    UserPreparation toUserPreparation(Preparation source, UserPreparation target);

}
