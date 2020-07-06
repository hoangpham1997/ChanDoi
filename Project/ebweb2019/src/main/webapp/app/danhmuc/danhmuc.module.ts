import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { EbwebAccountListModule } from 'app/danhmuc/account-list/account-list.module';
import { EbwebAutoPrincipleModule } from 'app/danhmuc/auto-principle/auto-principle.module';
import { EbwebUnitModule } from 'app/danhmuc/unit/unit.module';
import { EbwebAccountDefaultModule } from 'app/danhmuc/account-default/account-default.module';
import { EbwebCostSetModule } from 'app/danhmuc/doi-tuong-tap-hop-chi-phi/cost-set.module';

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        EbwebAccountListModule,
        EbwebAutoPrincipleModule,
        EbwebUnitModule,
        EbwebAccountDefaultModule,
        EbwebCostSetModule

        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebDanhMucModule {}
