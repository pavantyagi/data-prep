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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.talend.dataprep.preparation.service.EntityBuilder.*;
import static org.talend.dataprep.preparation.service.PreparationControllerTestClient.appendStepsToPrep;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.talend.dataprep.api.preparation.Preparation;
import org.talend.dataprep.api.preparation.PreparationMessage;
import org.talend.dataprep.preparation.BasePreparationTest;

/**
 * Unit test for the org.talend.dataprep.preparation.conversion.SimplePreparationMessageConverter class.
 *
 * @see SimplePreparationMessageConverter
 */
public class SimplePreparationMessageConverterTest extends BasePreparationTest {

    @Autowired
    private PreparationMessageConverter converter;

    @Test
    public void shouldConvertToPreparationMessage() throws Exception {
        // given
        final String prepId = createPreparationFromService("#DS1234", "super prep !", 3);
        appendStepsToPrep(prepId, step(diff("0003"), action("copy", paramsColAction("0001", "lastname"))));
        appendStepsToPrep(prepId, step(diff("0004"), action("copy", paramsColAction("0001", "lastname"))));
        appendStepsToPrep(prepId, step(null, action("uppercase", paramsColAction("0003", "lastname_copy"))));

        // when
        final Preparation preparation = repository.get(prepId, Preparation.class);
        final PreparationMessage actual = converter.toPreparationMessage(preparation, new PreparationMessage());

        // then
        assertNotNull(actual.getDiff());
        assertEquals(3, actual.getDiff().size());
        assertNotNull(actual.getSteps());
        assertEquals(4, actual.getSteps().size()); // root step + 3

    }
}
