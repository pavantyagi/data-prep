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

package org.talend.dataprep.transformation.format;

import java.util.Collections;

import org.springframework.stereotype.Component;
import org.talend.dataprep.format.export.ExportFormat;

/**
 * Json format type.
 */
@Component("format#" + JsonFormat.JSON)
public class JsonFormat extends ExportFormat {

    /** Json format type name. */
    public static final String JSON = "JSON";

    /**
     * Default constructor.
     */
    public JsonFormat() {
        super(JSON, "application/json", ".json", false, false, Collections.emptyList());
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
