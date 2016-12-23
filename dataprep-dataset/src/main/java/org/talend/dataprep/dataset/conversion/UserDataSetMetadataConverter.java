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

package org.talend.dataprep.dataset.conversion;

import org.talend.dataprep.api.dataset.DataSetMetadata;
import org.talend.dataprep.dataset.service.UserDataSetMetadata;

/**
 * Interface used by {@link org.talend.dataprep.dataset.configuration.DataSetConversions}.
 */
public interface UserDataSetMetadataConverter {

    /**
     * @param source the DataSetMetadata to convert.
     * @param target the result.
     * @return the updated user dataset metadata.
     */
    UserDataSetMetadata toUserDataSetMetadata(DataSetMetadata source, UserDataSetMetadata target);

}
