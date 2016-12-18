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

package org.talend.dataprep.api.preparation;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.codec.digest.DigestUtils;
import org.talend.dataprep.api.dataset.RowMetadata;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents one step of a {@link Preparation}.
 */
public class Step extends Identifiable implements Serializable {

    public static final Step ROOT_STEP = new Step() {

        @Override
        public String id() {
            return "f6e172c33bdacbc69bca9d32b2bd78174712a171";
        }

        @Override
        public String getId() {
            return "f6e172c33bdacbc69bca9d32b2bd78174712a171";
        }

        @Override
        public void setId(String id) {
        }
    };

    private static final PreparationActions ROOT_ACTIONS = new PreparationActions() {

        @Override
        public String id() {
            return "cdcd5c9a3a475f2298b5ee3f4258f8207ba10879";
        }

        @Override
        public String getId() {
            return "cdcd5c9a3a475f2298b5ee3f4258f8207ba10879";
        }

        @Override
        public void setId(String id) {
        }
    };

    /** Serialization UID. */
    private static final long serialVersionUID = 1L;

    private Step parent = ROOT_STEP;

    private PreparationActions preparationActions = ROOT_ACTIONS;

    /** The app version. */
    @JsonProperty("app-version")
    private String appVersion;

    private StepDiff diff;

    private RowMetadata rowMetadata;

    private Step() {
    }

    public Step(final Step parent, final PreparationActions content, final String appVersion) {
        this(parent, content, appVersion, null);
    }

    public Step(final Step parent, final PreparationActions content, final String appVersion, final StepDiff diff) {
        this.parent = parent;
        this.preparationActions = content;
        this.appVersion = appVersion;
        this.diff = diff;
    }

    public Step getParent() {
        return parent;
    }

    public void setParent(Step parent) {
        this.parent = parent;
    }

    public StepDiff getDiff() {
        return diff;
    }

    public void setDiff(StepDiff diff) {
        this.diff = diff;
    }

    /**
     * @return the AppVersion
     */
    public String getAppVersion() {
        return appVersion;
    }

    @Override
    public String id() {
        return getId();
    }

    @Override
    public String getId() {
        return DigestUtils.sha1Hex(
                (parent == null ? "null" : parent.getId()) + (preparationActions == null ? "null" : preparationActions.getId()));
    }

    @Override
    public void setId(String id) {
        // No op
    }

    public PreparationActions getContent() {
        return preparationActions;
    }

    public void setContent(PreparationActions preparationActions) {
        this.preparationActions = preparationActions;
    }

    @Override
    public String toString() {
        return "Step{" + //
                "parentId='" + parent + '\'' + //
                ", contentId='" + preparationActions + '\'' + //
                ", appVersion='" + appVersion + '\'' + //
                ", diff=" + diff + //
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Step step = (Step) o;
        return Objects.equals(parent, step.parent) && Objects.equals(preparationActions, step.preparationActions)
                && Objects.equals(appVersion, step.appVersion) && Objects.equals(diff, step.diff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, preparationActions, appVersion, diff);
    }

    /**
     * @return The row metadata linked to this step. Might be <code>null</code> to indicate no row metadata is present.
     */
    public RowMetadata getRowMetadata() {
        return rowMetadata;
    }

    /**
     * Set the row metadata for this step.
     * @param rowMetadata The row metadata to set for this step.
     */
    public void setRowMetadata(RowMetadata rowMetadata) {
        this.rowMetadata = rowMetadata;
    }
}
