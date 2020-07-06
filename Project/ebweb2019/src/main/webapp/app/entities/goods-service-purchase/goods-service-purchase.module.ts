import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    GoodsServicePurchaseComponent,
    GoodsServicePurchaseDetailComponent,
    GoodsServicePurchaseUpdateComponent,
    GoodsServicePurchaseDeletePopupComponent,
    GoodsServicePurchaseDeleteDialogComponent,
    goodsServicePurchaseRoute,
    goodsServicePurchasePopupRoute
} from './';

const ENTITY_STATES = [...goodsServicePurchaseRoute, ...goodsServicePurchasePopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        GoodsServicePurchaseComponent,
        GoodsServicePurchaseDetailComponent,
        GoodsServicePurchaseUpdateComponent,
        GoodsServicePurchaseDeleteDialogComponent,
        GoodsServicePurchaseDeletePopupComponent
    ],
    entryComponents: [
        GoodsServicePurchaseComponent,
        GoodsServicePurchaseUpdateComponent,
        GoodsServicePurchaseDeleteDialogComponent,
        GoodsServicePurchaseDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebGoodsServicePurchaseModule {}
