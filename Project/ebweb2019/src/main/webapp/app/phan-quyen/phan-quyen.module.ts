import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { EbwebEbGroupModule } from 'app/phan-quyen/eb-group/eb-group.module';
import { EbwebPermissionUserModule } from 'app/phan-quyen/permission-user/permission-user.module';
import { EbwebUserManagementModule } from 'app/phan-quyen/user-management/user-management.module';
import { EbwebRolePermissionModule } from 'app/phan-quyen/role-permission/role-permission.module';

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        EbwebEbGroupModule,
        EbwebPermissionUserModule,
        EbwebUserManagementModule,
        EbwebRolePermissionModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebPhanQuyenModule {}
