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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.talend.dataprep.api.dataset.DataSetMetadata;
import org.talend.dataprep.api.share.Owner;
import org.talend.dataprep.api.user.UserData;
import org.talend.dataprep.dataset.service.UserDataSetMetadata;
import org.talend.dataprep.security.Security;
import org.talend.dataprep.user.store.UserDataRepository;

/**
 * Default implementation of the {@link UserDataSetMetadataConverter}
 */
@Component
public class SimpleUserDataSetMetadataConverter implements UserDataSetMetadataConverter {

    /** DataPrep security. */
    @Autowired
    private Security security;

    /** Where to get user data. */
    @Autowired
    private UserDataRepository userDataRepository;

    @Override
    public UserDataSetMetadata toUserDataSetMetadata(DataSetMetadata metadata, UserDataSetMetadata userMetadata) {
        String userId = security.getUserId();

        // update the dataset favorites
        final UserData userData = userDataRepository.get(userId);
        if (userData != null) {
            userMetadata.setFavorite(userData.getFavoritesDatasets().contains(metadata.getId()));
        }

        // and the owner
        userMetadata.setOwner(new Owner(userId, security.getUserDisplayName(), ""));

        return userMetadata;
    }

}
