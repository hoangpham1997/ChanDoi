import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { EbwebSharedModule } from 'app/shared';
/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */

import {
    adminState,
    AuditsComponent,
    LogsComponent,
    EbMetricsMonitoringModalComponent,
    EbMetricsMonitoringComponent,
    EbHealthModalComponent,
    EbHealthCheckComponent,
    EbConfigurationComponent,
    EbDocsComponent,
    UserMgmtAdminComponent,
    UserMgmtDetailAdminComponent,
    UserMgmtUpdateAdminComponent,
    UserMgmtDeleteDialogAdminComponent,
    OrganizationUnitDeletePopupAdminComponent,
    EbPackageComponent,
    EbPackageDetailComponent,
    EbPackageUpdateComponent,
    EbPackageDeletePopupComponent,
    EbPackageDeleteDialogComponent
} from './';
import { HomeAdminComponent } from 'app/admin/home/home.component';
import { SidebarAdminComponent } from 'app/admin/sidebar/sidebar.component';
import { NavbarAdminComponent } from 'app/admin/navbar/navbar.component';
import { OrganizationUnitAdminComponent } from 'app/admin/organization-unit/organization-unit.component';
import { OrganizationUnitUpdateAdminComponent } from 'app/admin/organization-unit/organization-unit-update.component';
import { OrganizationUnitDeleteDialogAdminComponent } from 'app/admin/organization-unit/organization-unit-delete-dialog.component';

@NgModule({
    imports: [
        EbwebSharedModule,
        RouterModule.forChild(adminState)
        /* jhipster-needle-add-admin-module - JHipster will add admin modules here */
    ],
    declarations: [
        AuditsComponent,
        UserMgmtAdminComponent,
        UserMgmtDetailAdminComponent,
        UserMgmtUpdateAdminComponent,
        UserMgmtDeleteDialogAdminComponent,
        LogsComponent,
        EbConfigurationComponent,
        EbHealthCheckComponent,
        EbHealthModalComponent,
        EbDocsComponent,
        EbMetricsMonitoringComponent,
        EbMetricsMonitoringModalComponent,
        HomeAdminComponent,
        SidebarAdminComponent,
        NavbarAdminComponent,
        OrganizationUnitAdminComponent,
        OrganizationUnitUpdateAdminComponent,
        OrganizationUnitDeleteDialogAdminComponent,
        OrganizationUnitDeletePopupAdminComponent,
        EbPackageComponent,
        EbPackageDetailComponent,
        EbPackageUpdateComponent,
        EbPackageDeletePopupComponent,
        EbPackageDeleteDialogComponent
    ],
    entryComponents: [
        EbPackageDeleteDialogComponent,
        UserMgmtDeleteDialogAdminComponent,
        EbHealthModalComponent,
        EbMetricsMonitoringModalComponent,
        OrganizationUnitDeleteDialogAdminComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebAdminModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
