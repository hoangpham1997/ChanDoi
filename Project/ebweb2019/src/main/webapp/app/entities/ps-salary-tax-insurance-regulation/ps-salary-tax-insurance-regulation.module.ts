import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    PSSalaryTaxInsuranceRegulationComponent,
    PSSalaryTaxInsuranceRegulationDetailComponent,
    PSSalaryTaxInsuranceRegulationUpdateComponent,
    PSSalaryTaxInsuranceRegulationDeletePopupComponent,
    PSSalaryTaxInsuranceRegulationDeleteDialogComponent,
    pSSalaryTaxInsuranceRegulationRoute,
    pSSalaryTaxInsuranceRegulationPopupRoute
} from './';

const ENTITY_STATES = [...pSSalaryTaxInsuranceRegulationRoute, ...pSSalaryTaxInsuranceRegulationPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PSSalaryTaxInsuranceRegulationComponent,
        PSSalaryTaxInsuranceRegulationDetailComponent,
        PSSalaryTaxInsuranceRegulationUpdateComponent,
        PSSalaryTaxInsuranceRegulationDeleteDialogComponent,
        PSSalaryTaxInsuranceRegulationDeletePopupComponent
    ],
    entryComponents: [
        PSSalaryTaxInsuranceRegulationComponent,
        PSSalaryTaxInsuranceRegulationUpdateComponent,
        PSSalaryTaxInsuranceRegulationDeleteDialogComponent,
        PSSalaryTaxInsuranceRegulationDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebPSSalaryTaxInsuranceRegulationModule {}
