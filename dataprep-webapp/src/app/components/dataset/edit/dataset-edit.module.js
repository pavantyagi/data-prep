/*  ============================================================================

 Copyright (C) 2006-2016 Talend Inc. - www.talend.com

 This source code is available under agreement available at
 https://github.com/Talend/data-prep/blob/master/LICENSE

 You should have received a copy of the agreement
 along with this program; if not, write to Talend SA
 9 rue Pages 92150 Suresnes, France

 ============================================================================*/

import angular from 'angular';


import DatasetEditComponent from './dataset-edit.component';
import DatasetEditTcompComponent from './tcomp/dataset-edit-tcomp.component';

import SERVICES_IMPORT_MODULE from '../../../services/import/import-module';

const MODULE_NAME = 'data-prep.dataset-edit';

angular.module(MODULE_NAME, [
	SERVICES_IMPORT_MODULE,
])
	.component('datasetEditor', DatasetEditComponent)
	.component('tcompDatasetEditor', DatasetEditTcompComponent);

export default MODULE_NAME;
