//  ============================================================================
//
//  Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
//  This source code is available under agreement available at
//  https://github.com/Talend/data-prep/blob/master/LICENSE
//
//  You should have received a copy of the agreement
//  along with this program; if not, write to Talend SA
//  9 rue Pages 92150 Suresnes, France
//
//  ============================================================================

package org.talend.dataprep.transformation.service;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.talend.dataprep.test.SameJSONFile.sameJSONAsFile;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Integration tests on column actions.
 */
public class ActionTests extends TransformationServiceBaseTests {

    @Test
    public void noColumnActions() throws Exception {
        // when
        final String response = given() //
                .contentType(JSON) //
                .body("") //
                .when() //
                .post("/actions/column") //
                .asString();

        // then
        assertThat(response, sameJSONAsFile(ActionTests.class.getResourceAsStream("all_actions.json")).allowingExtraUnexpectedFields());
    }

    @Test
    public void columnActions() throws Exception {
        // given
        final String columnMetadata = IOUtils.toString(ActionTests.class.getResourceAsStream("../suggestions/string_column.json"));

        // when
        final String response = given() //
                .contentType(JSON) //
                .body(columnMetadata) //
                .when() //
                .post("/actions/column") //
                .asString();

        // then
        assertThat(response, sameJSONAsFile(ActionTests.class.getResourceAsStream("all_actions_string.json")).allowingExtraUnexpectedFields());
    }

    @Test
    public void should_get_all_lines_actions() throws Exception {
        // when
        final String response = given() //
                .when() //
                .get("/actions/line") //
                .asString();

        // then
        assertThat(response, sameJSONAsFile(ActionTests.class.getResourceAsStream("all_line_scope_actions.json")));
    }


    @Test
    public void should_get_all_dataset_actions() throws Exception {
        // when
        final String response = given() //
                .when() //
                .get("/actions/dataset") //
                .asString();

        // then
        assertThat(response, sameJSONAsFile(ActionTests.class.getResourceAsStream("all_dataset_scope_actions.json")));
    }

}
