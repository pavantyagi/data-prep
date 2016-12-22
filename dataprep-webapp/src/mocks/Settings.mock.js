/*  ============================================================================

 Copyright (C) 2006-2016 Talend Inc. - www.talend.com

 This source code is available under agreement available at
 https://github.com/Talend/data-prep/blob/master/LICENSE

 You should have received a copy of the agreement
 along with this program; if not, write to Talend SA
 9 rue Pages 92150 Suresnes, France

 ============================================================================*/

const settingsMock = {
	views: {
		appheaderbar: {
			app: 'Data Preparation',
			brandLink: {
				title: 'Talend Data Preparation',
				onClick: 'menu:home',
			},
			search: {
				onToggle: 'search:toggle',
				onBlur: 'search:toggle',
				onChange: '"search:all',
				onSelect: {
					preparation: '"menu:playground:preparation',
					dataset: '"menu:playground:dataset',
					folder: '"menu:folders',
					documentation: 'external:documentation',
				},
			},
			actions: ['onboarding:preparation', 'modal:feedback', 'external:help'],
			userMenuActions: {
				id: 'user-menu',
				name: 'Mike Tuchen',
				icon: 'icon-profile',
				menu: ['user:logout'],
			},
		},
		breadcrumb: {
			maxItems: 5,
			onItemClick: 'menu:folders',
		},
		sidepanel: {
			onToggleDock: 'sidepanel:toggle',
			actions: ['menu:preparations', 'menu:datasets'],
		},
		'listview:folders': {
			list: {
				titleProps: {
					onClick: 'menu:folders',
				},
			},
		},
		'listview:preparations': {
			didMountActionCreator: 'preparations:fetch',
			list: {
				columns: [
					{ key: 'name', label: 'Name' },
					{ key: 'author', label: 'Author' },
					{ key: 'creationDate', label: 'Created' },
					{ key: 'lastModificationDate', label: 'Last change' },
					{ key: 'dataset', label: 'Dataset' },
					{ key: 'nbLines', label: 'Nb lines' },
					{ key: 'nbSteps', label: 'Nb steps' },
				],
				items: [],
				itemProps: {
					classNameKey: 'className',
				},
				titleProps: {
					displayModeKey: 'displayMode',
					iconKey: 'icon',
					key: 'name',
					onClick: 'menu:playground:preparation',
					onEditCancel: 'inventory:cancel-edit',
					onEditSubmit: 'preparation:submit-edit',
				},
			},
			toolbar: {
				sortOptions: [
					{ id: 'name', name: 'Name' },
					{ id: 'date', name: 'Creation Date' },
				],
				actions: {
					left: ['preparation:create', 'preparation:create:folder'],
				},
				onSelectDisplayMode: 'preparation:display-mode',
				onSelectSortBy: 'preparation:sort',
				searchLabel: 'Find a preparation',
			},
		},
		'listview:datasets': {
			didMountActionCreator: 'datasets:fetch',
			list: {
				columns: [
					{ key: 'name', label: 'Name' },
					{ key: 'author', label: 'Author' },
					{ key: 'creationDate', label: 'Created' },
					{ key: 'nbLines', label: 'Lines' },
				],
				items: [],
				itemProps: {
					classNameKey: 'className',
				},
				titleProps: {
					displayModeKey: 'displayMode',
					iconKey: 'icon',
					key: 'name',
					onClick: 'menu:playground:dataset',
					onEditCancel: 'inventory:cancel-edit',
					onEditSubmit: 'dataset:submit-edit',
				},
			},
			toolbar: {
				sortOptions: [
					{ id: 'name', name: 'Name' },
					{ id: 'date', name: 'Creation Date' },
				],
				actions: [],
				onSelectDisplayMode: 'dataset:display-mode',
				onSelectSortBy: 'dataset:sort',
				searchLabel: 'Find a dataset',
			},
		},
	},
	actions: {
		'menu:preparations': {
			id: 'menu:preparations',
			name: 'Preparations',
			icon: 'talend-dataprep',
			type: '@@router/GO',
			payload: {
				method: 'go',
				args: ['reactHome.preparations'],
			},
		},
		'menu:datasets': {
			id: 'menu:datasets',
			name: 'Datasets',
			icon: 'talend-datastore',
			type: '@@router/GO',
			payload: {
				method: 'go',
				args: ['reactHome.datasets'],
			},
		},
		'menu:folders': {
			id: 'menu:folders',
			name: 'Folders',
			icon: 'talend-folder',
			type: '@@router/GO_FOLDER',
			payload: {
				method: 'go',
				args: ['reactHome.preparations'],
			},
		},
		'menu:playground:preparation': {
			id: 'menu:playground:preparation',
			name: 'Open Preparation',
			icon: 'talend-dataprep',
			type: '@@router/GO_PREPARATION',
			payload: {
				method: 'go',
				args: ['playground.preparation'],
			},
		},
		'menu:playground:dataset': {
			id: 'menu:playground:dataset',
			name: 'Open Dataset',
			icon: 'talend-dataprep',
			type: '@@router/GO_DATASET',
			payload: {
				method: 'go',
				args: [
					'playground.dataset',
				],
			},
		},
		'sidepanel:toggle': {
			id: 'sidepanel:toggle',
			name: 'Click here to toggle the side panel',
			icon: '',
			type: '@@sidepanel/TOGGLE',
			payload: {
				method: 'toggleHomeSidepanel',
				args: [],
			},
		},
		'onboarding:preparation': {
			id: 'onboarding:preparation',
			name: 'Click here to discover the application',
			icon: 'talend-board',
			type: '@@onboarding/START_TOUR',
			payload: {
				method: 'startTour',
				args: [
					'preparation',
				],
			},
		},
		'modal:feedback': {
			id: 'modal:feedback',
			name: 'Send feedback to Talend',
			icon: 'talend-bubbles',
			type: '@@modal/SHOW',
			payload: {
				method: 'showFeedback',
			},
		},
		'external:help': {
			id: 'external:help',
			name: 'Open Online Help',
			icon: 'talend-question-circle',
			type: '@@external/OPEN_WINDOW',
			payload: {
				method: 'open',
				args: [
					'https://help.talend.com/pages/viewpage.action?pageId=266307043&utm_medium=dpdesktop&utm_source=header',
				],
			},
		},
		'user:logout': {
			id: 'user:logout',
			name: 'Logout',
			icon: 'icon-logout',
			type: '@@user/logout',
			payload: {
				method: 'logout',
			},
		},
		'dataset:display-mode': {
			id: 'dataset:display-mode',
			name: 'Change dataset display mode',
			icon: '',
			type: '@@inventory/DISPLAY_MODE',
			payload: {
				method: 'setDatasetsDisplayMode',
				args: [],
			},
		},
		'dataset:sort': {
			id: 'dataset:sort',
			name: 'Change dataset sort',
			icon: '',
			type: '@@dataset/SORT',
			payload: {
				method: 'setDatasetsSortFromIds',
				args: [],
			},
		},
		'dataset:submit-edit': {
			id: 'dataset:submit-edit',
			name: 'Submit name edition',
			icon: 'talend-check',
			type: '@@dataset/SUBMIT_EDIT',
		},
		'dataset:remove': {
			id: 'dataset:remove',
			name: 'Remove dataset',
			icon: 'talend-trash',
			type: '@@dataset/REMOVE',
			payload: {
				method: 'remove',
				args: [],
			},
		},
		'dataset:clone': {
			id: 'dataset:clone',
			name: 'Copy dataset',
			icon: 'talend-files-o',
			type: '@@dataset/CLONE',
			payload: {
				method: 'clone',
				args: [],
			},
		},
		'dataset:favourite': {
			id: 'dataset:favourite',
			name: 'Add dataset in your favourite list',
			icon: 'talend-star',
			type: '@@dataset/FAVOURITE',
			payload: {
				method: 'toggleFavorite',
				args: [],
			},
		},
		'dataset:update': {
			id: 'dataset:update',
			name: 'Update dataset ',
			icon: 'talend-file-move',
			type: '@@dataset/UPDATE',
			payload: {
				method: '',
				args: [],
			},
		},
		'datasets:fetch': {
			id: 'datasets:fetch',
			name: 'Fetch all datasets',
			icon: 'talend-dataprep',
			type: '@@dataset/DATASET_FETCH',
			payload: {
				method: 'init',
				args: [],
			},
		},
		'preparation:display-mode': {
			id: 'preparation:display-mode',
			name: 'Change preparation display mode',
			icon: '',
			type: '@@inventory/DISPLAY_MODE',
			payload: {
				method: 'setPreparationsDisplayMode',
				args: [],
			},
		},
		'preparation:sort': {
			id: 'preparation:sort',
			name: 'Change preparation sort',
			icon: '',
			type: '@@preparation/SORT',
			payload: {
				method: 'setPreparationsSortFromIds',
				args: [],
			},
		},
		'preparation:create': {
			id: 'preparation:create',
			name: 'Create preparation',
			icon: 'talend-plus',
			type: '@@preparation/CREATE',
			payload: {
				method: 'togglePreparationCreator',
				args: [],
			},
		},
		'preparation:create:folder': {
			id: 'preparation:create:folder',
			name: 'Create folder',
			icon: 'talend-folder',
			type: '@@preparation/CREATE',
			payload: {
				method: 'toggleFolderCreator',
				args: [],
			},
		},
		'preparations:fetch': {
			id: 'preparations:fetch',
			name: 'Fetch preparations from current folder',
			icon: 'talend-dataprep',
			type: '@@preparation/FETCH',
			payload: {
				method: 'init',
				args: [],
			},
		},
		'preparation:copy-move': {
			id: 'preparation:copy-move',
			name: 'Copy/Move preparation',
			icon: 'talend-copy_dataset',
			type: '@@preparation/COPY_MOVE',
			payload: {
				method: 'copyMove',
				args: [],
			},
		},
		'inventory:edit': {
			id: 'inventory:edit',
			name: 'Edit name',
			icon: 'talend-pencil',
			type: '@@inventory/EDIT',
			payload: {
				method: 'enableInventoryEdit',
				args: [],
			},
		},
		'inventory:cancel-edit': {
			id: 'inventory:cancel-edit',
			name: 'Cancel name edition',
			icon: 'talend-crossbig',
			type: '@@inventory/CANCEL_EDIT',
			payload: {
				method: 'disableInventoryEdit',
				args: [],
			},
		},
		'preparation:submit-edit': {
			id: 'preparation:submit-edit',
			name: 'Submit name edition',
			icon: 'talend-check',
			type: '@@preparation/VALIDATE_EDIT',
			payload: {
				method: '',
				args: [],
			},
		},
		'preparation:remove': {
			id: 'preparation:remove',
			name: 'Remove preparation',
			icon: 'talend-delete',
			type: '@@preparation/REMOVE',
			payload: {
				method: 'remove',
				args: [],
			},
		},
		'preparation:remove:folder': {
			id: 'preparation:remove:folder',
			name: 'Remove folder',
			icon: 'talend-trash',
			type: '@@preparation/REMOVE_FOLDER',
			payload: {
				method: 'removeFolder',
				args: [],
			},
		},
		'search:toggle': {
			id: 'search:toggle',
			icon: 'talend-search',
			type: '@@search/TOGGLE',
		},
		'search:all': {
			id: 'search:all',
			type: '@@search/ALL',
		},
		'external:documentation': {
			id: 'external:documentation',
			type: '@@external/OPEN_WINDOW',
			icon: 'talend-question-circle',
			name: 'Documentation',
			payload: {
				method: 'open',
			},
		},
	},
};

export default settingsMock;
