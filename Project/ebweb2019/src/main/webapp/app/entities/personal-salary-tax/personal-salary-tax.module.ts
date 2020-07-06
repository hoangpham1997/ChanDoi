import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    PersonalSalaryTaxComponent,
    PersonalSalaryTaxDetailComponent,
    PersonalSalaryTaxUpdateComponent,
    PersonalSalaryTaxDeletePopupComponent,
    PersonalSalaryTaxDeleteDialogComponent,
    personalSalaryTaxRoute,
    personalSalaryTaxPopupRoute
} from './';

const ENTITY_STATES = [...personalSalaryTaxRoute, ...personalSalaryTaxPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PersonalSalaryTaxComponent,
        PersonalSalaryTaxDetailComponent,
        PersonalSalaryTaxUpdateComponent,
        PersonalSalaryTaxDeleteDialogComponent,
        PersonalSalaryTaxDeletePopupComponent
    ],
    entryComponents: [
        PersonalSalaryTaxComponent,
        PersonalSalaryTaxUpdateComponent,
        PersonalSalaryTaxDeleteDialogComponent,
        PersonalSalaryTaxDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebPersonalSalaryTaxModule {}
