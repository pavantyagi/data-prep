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

package org.talend.dataprep.api.service.api;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.talend.dataprep.api.dataset.DataSetMetadata;
import org.talend.dataprep.api.preparation.Preparation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Simple bean used to display a dataset metadata and a summary of its related preparations.
 */
@JsonIgnoreProperties({ "lifecycle", "nbLinesHeader", "nbLinesFooter" })
public class EnrichedDataSetMetadata extends DataSetMetadata {

    /** For the Serialization interface. */
    private static final long serialVersionUID = 1;

    /** List of preparations based on this dataset. */
    private List<PreparationSummary> preparations;

    /**
     * Constructor.
     *
     * @param source the original dataset metadata.
     * @param relatedPreparations the relatedPreparations based on this dataset.
     */
    public EnrichedDataSetMetadata(DataSetMetadata source, List<Preparation> relatedPreparations) {
        super(source.getId(), source.getName(), source.getAuthor(), source.getCreationDate(), source.getLastModificationDate(),
                null, null);
        this.setContent(source.getContent());
        this.setDraft(source.isDraft());
        this.setEncoding(source.getEncoding());
        this.setFavorite(source.isFavorite());
        this.setLocation(source.getLocation());
        this.setOwner(source.getOwner());
        this.setRoles(source.getRoles());
        this.setSharedByMe(source.isSharedByMe());
        this.setSharedDataSet(source.isSharedDataSet());
        this.setSheetName(source.getSheetName());
        this.setTag(source.getTag());

        this.setSchemaParserResult(null); // not interested
        this.getContent().getParameters().clear(); // not interested

        if (relatedPreparations != null) {
            this.preparations = relatedPreparations.stream().map(PreparationSummary::new).collect(Collectors.toList());
        } else {
            this.preparations = Collections.emptyList();
        }
    }

    /**
     * @return the Preparations
     */
    public List<PreparationSummary> getPreparations() {
        return preparations;
    }

    /**
     * @param preparations the preparations to set.
     */
    public void setPreparations(List<PreparationSummary> preparations) {
        this.preparations = preparations;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "EnrichedDataSetMetadata{" + //
                "id=" + this.getId() + //
                ", name=" + this.getName() + //
                ", preparations=" + preparations //
                + "} " + super.toString();
    }

    /**
     * Bean used to model a preparation based on this dataset.
     */
    public static class PreparationSummary implements Serializable {

        /** For the Serialization interface. */
        private static final long serialVersionUID = 1;

        /** The preparation id. */
        private String id;

        /** The preparation name. */
        private String name;

        /** The number of steps within this preparation. */
        private int nbSteps;

        /** The preparation last modification date. */
        private long lastModificationDate;

        /**
         * Constructor.
         * 
         * @param source the preparation source.
         */
        public PreparationSummary(Preparation source) {
            this.id = source.getId();
            this.name = source.getName();
            this.lastModificationDate = source.getLastModificationDate();
            this.nbSteps = source.getSteps().size();
        }

        /**
         * @return the Id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id the id to set.
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return the Name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the NbSteps
         */
        public int getNbSteps() {
            return nbSteps;
        }

        /**
         * @param nbSteps the nbSteps to set.
         */
        public void setNbSteps(int nbSteps) {
            this.nbSteps = nbSteps;
        }

        /**
         * @return the LastModificationDate
         */
        public long getLastModificationDate() {
            return lastModificationDate;
        }

        /**
         * @param lastModificationDate the lastModificationDate to set.
         */
        public void setLastModificationDate(long lastModificationDate) {
            this.lastModificationDate = lastModificationDate;
        }

        /**
         * @see Object#toString()
         */
        @Override
        public String toString() {
            return "PreparationSummary{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", nbSteps=" + nbSteps
                    + ", lastModificationDate=" + lastModificationDate + '}';
        }

        /**
         * @see Object#equals(Object)
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PreparationSummary that = (PreparationSummary) o;
            return nbSteps == that.nbSteps && lastModificationDate == that.lastModificationDate && Objects.equals(id, that.id)
                    && Objects.equals(name, that.name);
        }

        /**
         * @see Object#hashCode()
         */
        @Override
        public int hashCode() {
            return Objects.hash(id, name, nbSteps, lastModificationDate);
        }
    }
}
