package org.talend.dataprep.api.service.api;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.hibernate.validator.HibernateValidator;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Unit test for the format parameters.
 * 
 * @see ExportParameters
 *
 */
public class ExportParametersTest {

    /** The validator factory. */
    private LocalValidatorFactoryBean validator;

    /**
     * Default constructor that initialize the validator factory.
     */
    public ExportParametersTest() {
        validator = new LocalValidatorFactoryBean();
        validator.setProviderClass(HibernateValidator.class);
        validator.afterPropertiesSet();
    }

    @Test
    public void noDataSetIdNorPreparationId() {
        // given parameters without preparation not dataset id
        ExportParameters params = new ExportParameters();

        // when
        Set<ConstraintViolation<ExportParameters>> constraintViolations = validator.validate(params);

        // then 2 violations (dataset & preparation id are missing & column id is empty)
        Assert.assertEquals(2, constraintViolations.size());
    }

    @Test
    public void noDataSetId() {
        // given parameters without preparation not dataset id
        ExportParameters params = new ExportParameters();
        params.setPreparationId("prep#98435");

        // when
        Set<ConstraintViolation<ExportParameters>> constraintViolations = validator.validate(params);

        // then 1 violation (exportType is empty)
        Assert.assertEquals(1, constraintViolations.size());
    }

    @Test
    public void noPreparationId() {
        // given parameters without preparation not dataset id
        ExportParameters params = new ExportParameters();
        params.setDatasetId("dataset#19843");

        // when
        Set<ConstraintViolation<ExportParameters>> constraintViolations = validator.validate(params);

        // then 1 violation (exportType is empty)
        Assert.assertEquals(1, constraintViolations.size());
    }

    @Test
    public void validInput() {
        // given parameters without preparation not dataset id
        ExportParameters params = new ExportParameters();
        params.setDatasetId("dataset#19843");
        params.setExportType("TOTO");

        // when
        Set<ConstraintViolation<ExportParameters>> constraintViolations = validator.validate(params);

        // then no violation
        Assert.assertTrue(constraintViolations.isEmpty());
    }
}