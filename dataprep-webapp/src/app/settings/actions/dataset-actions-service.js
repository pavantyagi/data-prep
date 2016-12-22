/*  ============================================================================

 Copyright (C) 2006-2016 Talend Inc. - www.talend.com

 This source code is available under agreement available at
 https://github.com/Talend/data-prep/blob/master/LICENSE

 You should have received a copy of the agreement
 along with this program; if not, write to Talend SA
 9 rue Pages 92150 Suresnes, France

 ============================================================================*/

export default class DatasetActionsService {
	constructor($document, $stateParams, state, DatasetService,
				MessageService, StateService, StorageService,
				TalendConfirmService) {
		'ngInject';
		this.$document = $document;
		this.$stateParams = $stateParams;
		this.state = state;
		this.DatasetService = DatasetService;
		this.MessageService = MessageService;
		this.StateService = StateService;
		this.StorageService = StorageService;
		this.TalendConfirmService = TalendConfirmService;

		this.renamingList = [];
	}

	dispatch(action) {
		switch (action.type) {
		case '@@dataset/SORT': {
			const oldSort = this.state.inventory.datasetsSort;
			const oldOrder = this.state.inventory.datasetsOrder;

			const { sortBy, sortDesc } = action.payload;
			const sortOrder = sortDesc ? 'desc' : 'asc';

			this.StateService.setDatasetsSortFromIds(sortBy, sortOrder);

			this.DatasetService.refreshDatasets()
				.then(() => this.StorageService.setDatasetsSort(sortBy))
				.then(() => this.StorageService.setDatasetsOrder(sortOrder))
				.catch(() => {
					this.StateService.setDatasetsSortFromIds(oldSort.id, oldOrder.id);
				});
			break;
		}
		case '@@dataset/DATASET_FETCH':
			this.StateService.setPreviousRoute('nav.index.datasets');
			this.StateService.setFetchingInventoryDatasets(true);
			this.DatasetService.init().then(() => {
				this.StateService.setFetchingInventoryDatasets(false);
			});
			break;
		case '@@dataset/SUBMIT_EDIT': {
			const newName = action.payload.value;
			const cleanName = newName && newName.trim();
			const dataset = action.payload.model;

			this.StateService.disableInventoryEdit(dataset);
			if (cleanName && cleanName !== dataset.name) {
				if (this.renamingList.indexOf(dataset) > -1) {
					this.MessageService.warning(
						'DATASET_CURRENTLY_RENAMING_TITLE',
						'DATASET_CURRENTLY_RENAMING'
					);
					return;
				}

				if (this.DatasetService.getDatasetByName(cleanName)) {
					this.MessageService.error(
						'DATASET_NAME_ALREADY_USED_TITLE',
						'DATASET_NAME_ALREADY_USED'
					);
					return;
				}

				this.renamingList.push(dataset);

				return this.DatasetService.rename(dataset.model, cleanName)
					.then(() => {
						this.MessageService.success(
							'DATASET_RENAME_SUCCESS_TITLE',
							'DATASET_RENAME_SUCCESS'
						);
					})
					.finally(() => {
						const index = this.renamingList.indexOf(dataset);
						this.renamingList.splice(index, 1);
					});
			}
			break;
		}
		case '@@dataset/REMOVE': {
			const dataset = action.payload.model;
			this.TalendConfirmService
				.confirm(
					{ disableEnter: true },
					['DELETE_PERMANENTLY', 'NO_UNDONE_CONFIRM'],
					{ type: 'dataset', name: dataset.name }
				)
				.then(() => this.DatasetService.delete(dataset))
				.then(() => this.MessageService.success(
					'REMOVE_SUCCESS_TITLE',
					'REMOVE_SUCCESS',
					{ type: 'dataset', name: dataset.name }
				));
			break;
		}
		case '@@dataset/CLONE': {
			const dataset = action.payload.model;
			this.DatasetService.clone(dataset)
				.then(() => this.MessageService.success('COPY_SUCCESS_TITLE', 'COPY_SUCCESS'));
			break;
		}
		case '@@dataset/FAVOURITE': {
			this.DatasetService[action.payload.method](action.payload.model);
			break;
		}
		case '@@dataset/UPDATE': {
			const { payload } = action;
			const modelType = payload.model && payload.model.type;
			if (modelType && modelType.indexOf('tcomp') > -1) {
				console.log(payload.model);
				this.StateService[payload.method](payload.model);
			}
			else {
				this.$document[0].getElementById('inputUpdateDataset').click();
				this.StateService.setDatasetToUpdate(payload.model);
			}
			break;
		}
		}
	}
}
