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

package org.talend.dataprep.transformation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.talend.dataprep.api.preparation.PreparationMessage;
import org.talend.dataprep.api.service.command.preparation.PreparationUpdate;

/**
 * This service provides operation to update a preparation in preparation service. This is useful when transformation
 * service wants to update step's metadata once a transformation is over.
 */
@Service
public class PreparationUpdater {

    @Autowired
    ApplicationContext context;

    /**
     * Updates a preparation in preparation service with provided argument.
     *
     * @param preparation The new version of the preparation, the {@link PreparationMessage#id()} will be used to find the
     * preparation to update.
     */
    public void update(PreparationMessage preparation) {
        final PreparationUpdate update = context.getBean(PreparationUpdate.class, preparation.id(), preparation);
        update.execute();
    }
}
