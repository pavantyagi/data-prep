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

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.talend.dataprep.api.dataset.DataSetMetadata;
import org.talend.dataprep.api.share.Owner;
import org.talend.dataprep.api.user.UserData;
import org.talend.dataprep.dataset.service.UserDataSetMetadata;
import org.talend.dataprep.security.Security;
import org.talend.dataprep.user.store.UserDataRepository;

/**
 * Unit test for the org.talend.dataprep.dataset.conversion.SimpleUserDataSetMetadataConverter class.
 *
 * @see SimpleUserDataSetMetadataConverter
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleUserDataSetMetadataConverterTest {

    @Mock
    private Security security;

    @Mock
    private UserDataRepository<UserData> repository;

    @InjectMocks
    private SimpleUserDataSetMetadataConverter converter;

    @Test
    public void shouldConvert() throws Exception {
        // given
        String datasetId = "1";

        String userId = "toto";
        String userDisplayName = "to to";
        when(security.getUserId()).thenReturn(userId);
        when(security.getUserDisplayName()).thenReturn(userDisplayName);

        UserData userData = new UserData(userId, "1.0.0");
        userData.addFavoriteDataset(datasetId);
        when(repository.get(userId)).thenReturn(userData);

        // when
        DataSetMetadata source = new DataSetMetadata();
        source.setId(datasetId);
        final UserDataSetMetadata actual = converter.toUserDataSetMetadata(source, new UserDataSetMetadata());

        // then
        final Owner owner = actual.getOwner();
        assertNotNull(owner);
        assertEquals(userId, owner.getId());
        assertTrue(actual.isFavorite());
    }
}
