/*  ============================================================================

 Copyright (C) 2006-2016 Talend Inc. - www.talend.com

 This source code is available under agreement available at
 https://github.com/Talend/data-prep/blob/master/LICENSE

 You should have received a copy of the agreement
 along with this program; if not, write to Talend SA
 9 rue Pages 92150 Suresnes, France

 ============================================================================*/

const HomeComponent = {
	template: `
		<layout>
			<ui-view name="home-content"></ui-view>
		</layout>

		<dataset-xls-preview></dataset-xls-preview>
		<folder-creator></folder-creator>
		<preparation-copy-move></preparation-copy-move>
		<preparation-creator></preparation-creator>
		<insertion-home></insertion-home>
		<about></about>
	`,
};

export default HomeComponent;
