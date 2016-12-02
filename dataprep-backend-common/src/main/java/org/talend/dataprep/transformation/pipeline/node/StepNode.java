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

package org.talend.dataprep.transformation.pipeline.node;

import java.util.Optional;

import org.talend.dataprep.api.dataset.RowMetadata;
import org.talend.dataprep.api.dataset.row.DataSetRow;
import org.talend.dataprep.api.preparation.Step;
import org.talend.dataprep.transformation.pipeline.Node;
import org.talend.dataprep.transformation.pipeline.Visitor;

/**
 * <p>
 * This node is dedicated to execution when a preparation is available. This node is used to group together nodes that correspond
 * to a step.
 * </p>
 * <p>
 * This allows code to reuse row metadata contained in step instead of provided one.
 * </p>
 *
 * @see org.talend.dataprep.transformation.pipeline.StepNodeTransformer
 */
public class StepNode extends BasicNode {

    private final Step step;

    private final Node entryNode;

    public StepNode(Step step, Node entryNode) {
        this.step = step;
        this.entryNode = entryNode;
    }

    public Step getStep() {
        return step;
    }

    @Override
    public void receive(DataSetRow row, RowMetadata metadata) {
        Optional<RowMetadata> stepMetadata = Optional.ofNullable(step.getRowMetadata());
        final RowMetadata rowMetadata = stepMetadata.isPresent() ? stepMetadata.get() : metadata;
        if (!stepMetadata.isPresent()) {
            step.setRowMetadata(metadata);
        }
        entryNode.exec().receive(row, rowMetadata);
        super.receive(row, rowMetadata);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitStepNode(this);
    }

    @Override
    public Node copyShallow() {
        return new StepNode(step, entryNode);
    }
}
