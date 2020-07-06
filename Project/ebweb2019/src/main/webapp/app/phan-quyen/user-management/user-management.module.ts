import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { EbwebSharedModule } from 'app/shared';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UserMgmtComponent } from 'app/phan-quyen/user-management/user-management.component';
import { UserMgmtUpdateComponent } from 'app/phan-quyen/user-management/user-management-update.component';
import { UserMgmtDeleteDialogComponent } from 'app/phan-quyen/user-management/user-management-delete-dialog.component';
import { userMgmtRoute } from 'app/phan-quyen/user-management/user-management.route';
import { UserMgmtDetailComponent } from 'app/phan-quyen/user-management/user-management-detail.component';

const ENTITY_STATES = [...userMgmtRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [UserMgmtComponent, UserMgmtUpdateComponent, UserMgmtDetailComponent, UserMgmtDeleteDialogComponent],
    entryComponents: [UserMgmtComponent, UserMgmtUpdateComponent, UserMgmtDeleteDialogComponent, UserMgmtDetailComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebUserManagementModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
