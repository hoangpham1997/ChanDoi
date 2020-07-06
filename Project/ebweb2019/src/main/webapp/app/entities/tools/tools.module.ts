import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    ToolsComponent,
    ToolsDetailComponent,
    ToolsUpdateComponent,
    ToolsDeletePopupComponent,
    ToolsDeleteDialogComponent,
    toolsRoute,
    toolsPopupRoute
} from './';

const ENTITY_STATES = [...toolsRoute, ...toolsPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [ToolsComponent, ToolsDetailComponent, ToolsUpdateComponent, ToolsDeleteDialogComponent, ToolsDeletePopupComponent],
    entryComponents: [ToolsComponent, ToolsUpdateComponent, ToolsDeleteDialogComponent, ToolsDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebToolsModule {}
