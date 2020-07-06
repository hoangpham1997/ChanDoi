import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { PermissionUserComponent } from 'app/phan-quyen/permission-user/permission-user.component';
import { permissionUserRoute } from 'app/phan-quyen/permission-user/permission-user.route';
import { EbGroupByOrgDeleteDialogComponent } from 'app/phan-quyen/permission-user/eb-group-delete-dialog.component';

const ENTITY_STATES = [...permissionUserRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [PermissionUserComponent, EbGroupByOrgDeleteDialogComponent],
    entryComponents: [PermissionUserComponent, EbGroupByOrgDeleteDialogComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebPermissionUserModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
