/*  ============================================================================

 Copyright (C) 2006-2016 Talend Inc. - www.talend.com

 This source code is available under agreement available at
 https://github.com/Talend/data-prep/blob/master/LICENSE

 You should have received a copy of the agreement
 along with this program; if not, write to Talend SA
 9 rue Pages 92150 Suresnes, France

 ============================================================================*/

const NAV_ITEM = 'navItem';
const DROPDOWN = 'dropdown';

export default class AppHeaderBarCtrl {
	constructor($element, $translate, appSettings, SettingsActionsService) {
		'ngInject';

		this.$element = $element;
		this.$translate = $translate;
		this.appSettings = appSettings;
		this.settingsActionsService = SettingsActionsService;
	}

	$onInit() {
		this.initApp();
		this.adaptBrandLink();
		this.adaptContent();
	}

	$postLink() {
		this.$element[0].addEventListener('click', (e) => {
			// block the native click action to avoid home redirection on empty href
			e.preventDefault();
		});
	}

	$onChanges(changes) {
		if (this.content) {
			const updatedContent = this.content.slice();
			const searchConfiguration = updatedContent[1].search;
			if (changes.searching) {
				const searching = changes.searching.currentValue;
				searchConfiguration.searching = searching;
			}
			else if (changes.searchToggle) {
				const searchToggle = changes.searchToggle.currentValue;
				if (searchToggle) {
					searchConfiguration.onToggle = this.searchOnToggle;
					searchConfiguration.value = '';
					searchConfiguration.items = null;
				}
				else {
					delete searchConfiguration.onToggle;
				}
			}
			else if (changes.searchInput) {
				const searchInput = changes.searchInput.currentValue;
				searchConfiguration.value = searchInput;
				if (!searchInput) {
					searchConfiguration.items = null;
				}
			}
			else if (changes.searchResults) {
				const searchResults = changes.searchResults.currentValue;
				this.adaptedSearchResults = this._adaptSearchResults(searchResults);
				searchConfiguration.items = this.adaptedSearchResults;
			}
			this.content = updatedContent;
		}
	}

	initApp() {
		this.app = this.appSettings.views.appheaderbar.app;
	}

	adaptBrandLink() {
		const settingsBrandLink = this.appSettings.views.appheaderbar.brandLink;
		const clickAction = this.appSettings.actions[settingsBrandLink.onClick];
		this.brandLink = {
			...settingsBrandLink,
			onClick: this.settingsActionsService.createDispatcher(clickAction),
		};
	}

	adaptSearch() {
		const searchSettings = this.appSettings.views.appheaderbar.search;

		// onToggle
		const onToggleAction = this.appSettings.actions[searchSettings.onToggle];
		this.searchOnToggle = onToggleAction && this.settingsActionsService.createDispatcher(onToggleAction);

		// onBlur
		const onBlurAction = this.appSettings.actions[searchSettings.onBlur];
		const onBlurActionDispatcher = onBlurAction && this.settingsActionsService.createDispatcher(onBlurAction);
		this.searchOnBlur = (event) => {
			if (onBlurActionDispatcher && !this.searchInput) {
				onBlurActionDispatcher(event);
			}
		};

		// onChange
		const onChangeAction = this.appSettings.actions[searchSettings.onChange];
		const onChangeActionDispatcher = onChangeAction && this.settingsActionsService.createDispatcher(onChangeAction);
		this.searchOnChange = (event) => {
			const searchInput = event.target && event.target.value;
			if (onChangeActionDispatcher) {
				return onChangeActionDispatcher(event, { searchInput });
			}
		};

		// onSelect
		this.searchAvailableInventoryTypes = [];
		const onSelectActionBy = searchSettings.onSelect;
		const onSelectDispatcherByType = [];
		Object.keys(onSelectActionBy).forEach((type) => {
			const onSelectAction = this.appSettings.actions[onSelectActionBy[type]];
			if (onSelectAction) {
				this.searchAvailableInventoryTypes.push({
					title: type,
					iconName: onSelectAction.icon,
					iconTitle: onSelectAction.name,
				});
				onSelectDispatcherByType[type] = this.settingsActionsService.createDispatcher(onSelectAction);
			}
		});
		this.searchOnSelect = (event, { sectionIndex, itemIndex }) => {
			const selectedCategory = this.adaptedSearchResults[sectionIndex];
			const selectedItem = selectedCategory && selectedCategory.suggestions[itemIndex];
			const onSelectDispatcher = onSelectDispatcherByType[selectedItem.inventoryType];
			this.searchOnToggle();
			if (onSelectDispatcher) {
				return onSelectDispatcher(event, selectedItem);
			}
		};

		return {
			...searchSettings,
			icon: onToggleAction && {
				name: onToggleAction.icon,
				title: onToggleAction.name,
				bsStyle: 'link',
			},
			onToggle: this.searchOnToggle,
			onBlur: this.searchOnBlur,
			onChange: this.searchOnChange,
			onSelect: this.searchOnSelect,
		};
	}

	_adaptSearchResults(searchResults) {
		return this.searchAvailableInventoryTypes
			.filter(inventoryType => searchResults.some(result => result.inventoryType === inventoryType.title))
			.map((inventoryType) => {
				const suggestions = searchResults.filter(result => result.inventoryType === inventoryType.title);
				return {
					title: inventoryType.title,
					icon: {
						name: inventoryType.iconName,
						title: inventoryType.title,
					},
					suggestions: suggestions.map((result) => {
						return {
							...result,
							title: result.name,
							description: result.description,
						};
					}),
				};
			});
	}

	adaptContent() {
		const search = this.appSettings.views.appheaderbar.search ?
			this.adaptSearch() :
			null;
		const navItems = this.appSettings.views.appheaderbar.actions ?
			this.adaptActions() :
			[];
		const userMenu = this.appSettings.views.appheaderbar.userMenuActions ?
			this.adaptUserMenu() :
			[];

		this.content = [
			{
				navs: [{
					nav: { pullRight: true },
					navItems: navItems.concat(userMenu),
				}],
			},
			{
				search,
			},
		];
	}

	adaptActions() {
		return this.appSettings
			.views
			.appheaderbar
			.actions
			.map(actionName => this.appSettings.actions[actionName])
			.map(action => ({
				type: NAV_ITEM,
				item: {
					id: action.id,
					name: this.$translate.instant(action.name),
					icon: action.icon,
					onClick: this.settingsActionsService.createDispatcher(action),
				},
			}));
	}

	adaptUserMenu() {
		const { id, name, icon, menu } = this.appSettings
			.views
			.appheaderbar
			.userMenuActions;

		return {
			type: DROPDOWN,
			item: {
				dropdown: {
					id,
					icon,
					title: name,
				},
				items: menu
					.map(actionName => this.appSettings.actions[actionName])
					.map(action => ({
						id: action.id,
						icon: action.icon,
						name: action.name,
						onClick: this.settingsActionsService.createDispatcher(action),
					})),
			},
		};
	}
}
