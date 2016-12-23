/*  ============================================================================

 Copyright (C) 2006-2016 Talend Inc. - www.talend.com

 This source code is available under agreement available at
 https://github.com/Talend/data-prep/blob/master/LICENSE

 You should have received a copy of the agreement
 along with this program; if not, write to Talend SA
 9 rue Pages 92150 Suresnes, France

 ============================================================================*/

describe('Import', function () {

	beforeEach(angular.mock.module('data-prep.services.state'));

	describe('state service', function () {
		it('should set import types', inject(function (importState, ImportStateService) {
			// given
			const imports = [{ name: 'import 1' }, { name: 'import 2' }];

			// when
			ImportStateService.setImportTypes(imports);

			// then
			expect(importState.importTypes).toBe(imports);
		}));

		it('should set import visible for an item', inject(function (importState, ImportStateService) {
			// given
			const item = { id: 'id' };
			expect(importState.visible).toBeFalsy();
			expect(importState.importItem).toBeNull();

			// when
			ImportStateService.setVisible(item);

			// then
			expect(importState.visible).toBeTruthy();
			expect(importState.importItem).toBe(item);
		}));
	});
});
