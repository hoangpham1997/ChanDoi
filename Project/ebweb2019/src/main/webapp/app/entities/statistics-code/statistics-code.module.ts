import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    StatisticsCodeComponent,
    StatisticsCodeDetailComponent,
    StatisticsCodeUpdateComponent,
    StatisticsCodeDeletePopupComponent,
    StatisticsCodeDeleteDialogComponent,
    statisticsCodeRoute,
    statisticsCodePopupRoute
} from './';

const ENTITY_STATES = [...statisticsCodeRoute, ...statisticsCodePopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        StatisticsCodeComponent,
        StatisticsCodeDetailComponent,
        StatisticsCodeUpdateComponent,
        StatisticsCodeDeleteDialogComponent,
        StatisticsCodeDeletePopupComponent
    ],
    entryComponents: [
        StatisticsCodeComponent,
        StatisticsCodeUpdateComponent,
        StatisticsCodeDeleteDialogComponent,
        StatisticsCodeDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebStatisticsCodeModule {}
