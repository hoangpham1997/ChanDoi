import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { RolePermissionComponent } from 'app/phan-quyen/role-permission/role-permission.component';
import { rolePermissionRoute } from 'app/phan-quyen/role-permission/role-permission.route';

const ENTITY_STATES = [...rolePermissionRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [RolePermissionComponent],
    entryComponents: [RolePermissionComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebRolePermissionModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
