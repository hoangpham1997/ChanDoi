import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    CareerGroupComponent,
    CareerGroupDetailComponent,
    CareerGroupUpdateComponent,
    CareerGroupDeletePopupComponent,
    CareerGroupDeleteDialogComponent,
    careerGroupRoute,
    careerGroupPopupRoute
} from './';

const ENTITY_STATES = [...careerGroupRoute, ...careerGroupPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CareerGroupComponent,
        CareerGroupDetailComponent,
        CareerGroupUpdateComponent,
        CareerGroupDeleteDialogComponent,
        CareerGroupDeletePopupComponent
    ],
    entryComponents: [CareerGroupComponent, CareerGroupUpdateComponent, CareerGroupDeleteDialogComponent, CareerGroupDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebCareerGroupModule {}
