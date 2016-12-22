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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.talend.dataprep.api.preparation.Preparation;
import org.talend.dataprep.api.share.Owner;
import org.talend.dataprep.preparation.service.UserPreparation;
import org.talend.dataprep.security.Security;

/**
 * Default implementation of the UserPreparationConverter.
 */
@Component
public class SimpleUserPreparationConverter implements UserPreparationConverter {

    @Autowired
    private Security security;

    @Override
    public UserPreparation toUserPreparation(Preparation source, UserPreparation target) {
        Owner owner = new Owner(security.getUserId(), security.getUserDisplayName(), null);
        target.setOwner(owner);
        return target;
    }
}
