import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    SaleDiscountPolicyComponent,
    SaleDiscountPolicyDetailComponent,
    SaleDiscountPolicyUpdateComponent,
    SaleDiscountPolicyDeletePopupComponent,
    SaleDiscountPolicyDeleteDialogComponent,
    saleDiscountPolicyRoute,
    saleDiscountPolicyPopupRoute
} from './';

const ENTITY_STATES = [...saleDiscountPolicyRoute, ...saleDiscountPolicyPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SaleDiscountPolicyComponent,
        SaleDiscountPolicyDetailComponent,
        SaleDiscountPolicyUpdateComponent,
        SaleDiscountPolicyDeleteDialogComponent,
        SaleDiscountPolicyDeletePopupComponent
    ],
    entryComponents: [
        SaleDiscountPolicyComponent,
        SaleDiscountPolicyUpdateComponent,
        SaleDiscountPolicyDeleteDialogComponent,
        SaleDiscountPolicyDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebSaleDiscountPolicyModule {}
