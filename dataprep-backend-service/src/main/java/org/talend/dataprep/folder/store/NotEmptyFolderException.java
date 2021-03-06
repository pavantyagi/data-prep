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

package org.talend.dataprep.folder.store;

/**
 * This exception happen whern trying to delete a non empty folder
 * TODO: this should be a TDPException as it is a business exception.
 */
public class NotEmptyFolderException extends Exception {

    public NotEmptyFolderException(String message) {
        super(message);
    }
}
