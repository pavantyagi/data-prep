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

package org.talend.dataprep.preparation.store;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.*;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.talend.dataprep.api.preparation.Action;
import org.talend.dataprep.api.preparation.Preparation;
import org.talend.dataprep.api.preparation.PreparationActions;

public abstract class PreparationRepositoryTest {

    protected abstract Preparation getPreparation(String preparationName);

    protected abstract PreparationRepository getRepository();

    @Test
    public void shouldListOnlyWantedClass() {

        List<Integer> ids = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

        // store preparations
        final List<Preparation> preparations = ids.stream() //
                .map(i -> getPreparation(String.valueOf(i))) //
                .collect(Collectors.toList());

        preparations.stream().forEach(prep -> getRepository().add(prep));

        // list all preparations
        final Collection<Preparation> actual = getRepository().listAll(Preparation.class);

        assertEquals(ids.size(), actual.size());
        preparations.stream().forEach(actual::contains);
    }

    @Test
    public void shouldListOnlyPreparationsForDatasets() {
        List<Integer> ids = Arrays.asList(1, 2, 3);

        // store preparations
        final List<Preparation> preparations = ids.stream() //
                .map(i -> getPreparation(String.valueOf(i))) //
                .collect(Collectors.toList());

        preparations.stream().forEach(prep -> getRepository().add(prep));

        // get preparation by name
        final Preparation expected = preparations.get(1);
        final Collection<Preparation> actual = getRepository().getByDataSet(expected.getDataSetId());

        assertEquals(1, actual.size());
        assertTrue(actual.contains(expected));

    }

    @Test
    public void findOneByDataset_should_return_a_preparation_that_use_dataset() {
        // given
        final String datasetId = "789b61f3128a9bc24a684";
        final Preparation prep1 = new Preparation();
        prep1.setDataSetId("other_dataset");
        final Preparation prep2 = new Preparation();
        prep2.setDataSetId(datasetId);

        getRepository().add(prep1);
        getRepository().add(prep2);

        // when
        final Preparation result = getRepository().findOneByDataset(datasetId);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getDataSetId(), is(datasetId));
    }

    @Test
    public void findOneByDataset_should_return_null_when_no_preparation_use_dataset() {
        // given
        final String datasetId = "789b61f3128a9bc24a684";
        final Preparation prep1 = new Preparation();
        prep1.setDataSetId("other_dataset");
        final Preparation prep2 = new Preparation();
        prep2.setDataSetId("other_dataset");

        getRepository().add(prep1);
        getRepository().add(prep2);

        // when
        final Preparation result = getRepository().findOneByDataset(datasetId);

        // then
        assertThat(result, nullValue());
    }

    @Test
    public void findOneStepActionByDataset_should_return_a_lookup_action_that_use_dataset() {
        // given
        final String datasetId = "789b61f3128a9bc24a684";
        final Map<String, String> parametersOnDataset = new HashMap<>();
        parametersOnDataset.put("lookup_ds_id", datasetId);
        final Map<String, String> parametersWithoutDataset = new HashMap<>();
        parametersWithoutDataset.put("other", "other");

        final List<Action> action1 = new ArrayList<>(1);
        action1.add(Action.Builder.builder().withParameters(parametersWithoutDataset).build());
        final List<Action> action2 = new ArrayList<>(1);
        action2.add(Action.Builder.builder().withParameters(parametersOnDataset).build());

        final PreparationActions prepAction1 = new PreparationActions().append(action1);
        final PreparationActions prepAction2 = new PreparationActions().append(action2);

        getRepository().add(prepAction1);
        getRepository().add(prepAction2);

        // when
        final PreparationActions result = getRepository().findOneStepActionByDataset(datasetId);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getActions().get(0).getParameters().get("lookup_ds_id"), is(datasetId));
    }

    @Test
    public void findOneStepActionByDataset_should_return_null_when_no_action_use_dataset() {
        // given
        final String datasetId = "789b61f3128a9bc24a684";
        final Map<String, String> parametersOnOtherDataset = new HashMap<>();
        parametersOnOtherDataset.put("lookup_ds_id", "other");
        final Map<String, String> parametersWithoutDataset = new HashMap<>();
        parametersWithoutDataset.put("other", "other");

        final List<Action> action1 = new ArrayList<>(1);
        action1.add(Action.Builder.builder().withParameters(parametersWithoutDataset).build());
        final List<Action> action2 = new ArrayList<>(1);
        action2.add(Action.Builder.builder().withParameters(parametersOnOtherDataset).build());

        final PreparationActions prepAction1 = new PreparationActions().append(action1);
        final PreparationActions prepAction2 = new PreparationActions().append(action2);

        getRepository().add(prepAction1);
        getRepository().add(prepAction2);

        // when
        final PreparationActions result = getRepository().findOneStepActionByDataset(datasetId);

        // then
        assertThat(result, nullValue());
    }
}
