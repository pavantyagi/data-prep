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

package org.talend.dataprep.transformation.pipeline;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.talend.dataprep.api.preparation.Step;
import org.talend.dataprep.transformation.pipeline.builder.NodeBuilder;
import org.talend.dataprep.transformation.pipeline.node.*;

import java.util.Arrays;

public class StepNodeTransformerTest {

    private static final Step STEP = new Step(null, null, "");

    private static final Step ROOT = new Step(null, null, "");

    @Test
    public void shouldNotCreateStepNode() throws Exception {
        // given
        Node node = NodeBuilder.from(new TestNode()).build();

        // when
        final Node processed = StepNodeTransformer.transform(node, emptyList());

        // then
        assertEquals(SourceNode.class, processed.getClass());
        assertEquals(TestNode.class, processed.getLink().getTarget().getClass());
    }

    @Test
    public void shouldCreateStepNode() throws Exception {
        // given
        Node node = NodeBuilder.from(new CompileNode(null, null))
                .to(new ActionNode(null, null))
                .build();

        // when
        final Node processed = StepNodeTransformer.transform(node, Arrays.asList(ROOT, STEP));

        // then
        final Class[] expectedClasses = { SourceNode.class, StepNode.class };
        final NodeClassVisitor visitor = new NodeClassVisitor();
        processed.accept(visitor);
        assertThat(visitor.traversedClasses, hasItems(expectedClasses));

    }

    @Test
    public void shouldCreateStepNodeWhenSurrounded() throws Exception {
        // given
        Node node = NodeBuilder.from(new TestNode()) //
                .to(new CompileNode(null, null)) //
                .to(new ActionNode(null, null)) //
                .to(new BasicNode()) //
                .build();

        // when
        final Node processed = StepNodeTransformer.transform(node, Arrays.asList(ROOT, STEP));

        // then
        final Class[] expectedClasses = { SourceNode.class, TestNode.class, StepNode.class, BasicNode.class };
        final NodeClassVisitor visitor = new NodeClassVisitor();
        processed.accept(visitor);
        assertThat(visitor.traversedClasses, hasItems(expectedClasses));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailCreateStepNode() throws Exception {
        // given
        Node node = NodeBuilder.from(new CompileNode(null, null))
                .to(new ActionNode(null, null))
                .build();

        // then
        StepNodeTransformer.transform(node, emptyList());
    }

    @Test
    public void shouldCreateStepNodeWithTooManySteps() throws Exception {
        // given
        Node node = NodeBuilder.from(new CompileNode(null, null))
                .to(new ActionNode(null, null))
                .build();

        // when
        final Node processed = StepNodeTransformer.transform(node, Arrays.asList(ROOT, STEP, STEP));

        // then
        final Class[] expectedClasses = { SourceNode.class, StepNode.class };
        final NodeClassVisitor visitor = new NodeClassVisitor();
        processed.accept(visitor);
        assertThat(visitor.traversedClasses, hasItems(expectedClasses));
    }


}
