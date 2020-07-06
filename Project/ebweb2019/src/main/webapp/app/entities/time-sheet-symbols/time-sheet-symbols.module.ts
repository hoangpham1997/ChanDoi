import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    TimeSheetSymbolsComponent,
    TimeSheetSymbolsDetailComponent,
    TimeSheetSymbolsUpdateComponent,
    TimeSheetSymbolsDeletePopupComponent,
    TimeSheetSymbolsDeleteDialogComponent,
    timeSheetSymbolsRoute,
    timeSheetSymbolsPopupRoute
} from './';

const ENTITY_STATES = [...timeSheetSymbolsRoute, ...timeSheetSymbolsPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TimeSheetSymbolsComponent,
        TimeSheetSymbolsDetailComponent,
        TimeSheetSymbolsUpdateComponent,
        TimeSheetSymbolsDeleteDialogComponent,
        TimeSheetSymbolsDeletePopupComponent
    ],
    entryComponents: [
        TimeSheetSymbolsComponent,
        TimeSheetSymbolsUpdateComponent,
        TimeSheetSymbolsDeleteDialogComponent,
        TimeSheetSymbolsDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebTimeSheetSymbolsModule {}
