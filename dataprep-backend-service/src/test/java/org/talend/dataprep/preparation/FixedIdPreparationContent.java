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

package org.talend.dataprep.preparation;

import org.talend.dataprep.api.preparation.PreparationActions;

public class FixedIdPreparationContent extends PreparationActions {

    private final String fixedId;

    public FixedIdPreparationContent(String fixedId) {
        this.fixedId = fixedId;
    }

    @Override
    public String id() {
        return fixedId;
    }

    @Override
    public String getId() {
        return fixedId;
    }
}
