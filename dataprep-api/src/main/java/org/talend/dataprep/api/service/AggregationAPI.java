package org.talend.dataprep.api.service;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.talend.dataprep.api.service.command.aggregation.Aggregate;
import org.talend.dataprep.exception.TDPException;
import org.talend.dataprep.exception.error.CommonErrorCodes;
import org.talend.dataprep.transformation.aggregation.api.AggregationParameters;

import com.netflix.hystrix.HystrixCommand;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * High level Aggregation API.
 */
@RestController
@Api(value = "api", basePath = "/api", description = "Aggregation API")
public class AggregationAPI extends APIService {

    /**
     * Compute an aggregation according to the given parameters.
     *
     * @param input The aggredation parameters.
     * @param response The HTTP response.
     */
    @RequestMapping(value = "/api/aggregate", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Compute aggregation", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, notes = "Compute aggregation according to the given parameters")
    public void compute(@RequestBody
    final AggregationParameters input, final HttpServletResponse response) {

        LOG.debug("Aggregation computation requested (pool: {} )...", getConnectionManager().getTotalStats());

        // validate input parameters
        if (StringUtils.isEmpty(input.getDatasetId()) && StringUtils.isEmpty(input.getPreparationId())) {
            throw new TDPException(CommonErrorCodes.BAD_AGGREGATION_PARAMETERS);
        }

        // get the command and execute it
        HttpClient client = getClient();
        HystrixCommand<InputStream> command = getCommand(Aggregate.class, client, input);

        // copy the content to the http response
        try (InputStream result = command.execute()) {
            response.setHeader("Content-Type", APPLICATION_JSON_VALUE); //$NON-NLS-1$
            OutputStream output = response.getOutputStream();
            IOUtils.copyLarge(result, output);
            output.flush();
            LOG.debug("Aggregation done (pool: {} )...", getConnectionManager().getTotalStats());
        } catch (IOException e) {
            throw new TDPException(CommonErrorCodes.UNEXPECTED_EXCEPTION, e);
        }

    }

}
