package org.talend.dataprep.transformation.api.action.metadata.line;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.talend.dataprep.transformation.api.action.metadata.category.ActionCategory.DATA_CLEANSING;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.talend.dataprep.api.dataset.DataSetRow;
import org.talend.dataprep.transformation.api.action.ActionTestWorkbench;
import org.talend.dataprep.transformation.api.action.metadata.common.ImplicitParameters;

public class MakeLineHeaderTest {

    private MakeLineHeader action;

    @Before
    public void init() throws IOException {
        action = new MakeLineHeader();
    }

    @Test
    public void should_be_in_data_cleansing_category() {
        //when
        final String name = action.getCategory();

        //then
        assertThat(name, is(DATA_CLEANSING.getDisplayName()));
    }

    @Test
    public void should_return_name() {
        //when
        final String name = action.getName();

        //then
        assertThat(name, is("make_line_header"));
    }

    @Test
    public void should_delete_line_with_provided_row_id() {
        //given
        Long rowId = 120L;

        // row 1
        Map<String, String> rowContent = new HashMap<>();
        rowContent.put("0000", "David");
        rowContent.put("0001", "Bowie");
        final DataSetRow row1 = new DataSetRow(rowContent);
        row1.setTdpId(rowId);

        // row 2
        rowContent = new HashMap<>();
        rowContent.put("0000", "John");
        rowContent.put("0001", "Lennon");
        final DataSetRow row2 = new DataSetRow(rowContent);
        row2.setTdpId(rowId++);

        final Map<String, String> parameters = new HashMap<>();
        parameters.put(ImplicitParameters.SCOPE.getKey().toLowerCase(), "line");
        parameters.put("row_id", row2.getTdpId().toString());

        assertThat(row1.isDeleted(), is(false));
        assertThat(row2.isDeleted(), is(false));

        //when
        ActionTestWorkbench.test(row2, action.create(parameters).getRowAction());

        //then
        assertThat(row1.isDeleted(), is(false));
        assertThat(row2.isDeleted(), is(true));

        assertEquals("John", row2.getRowMetadata().getById("0000").getName());
        assertEquals("Lennon", row2.getRowMetadata().getById("0001").getName());
    }

}