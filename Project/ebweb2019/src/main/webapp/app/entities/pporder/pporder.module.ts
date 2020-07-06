import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { PporderComponent, PporderDetailComponent, pporderRoute, PporderUpdateComponent } from './';

const ENTITY_STATES = [...pporderRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [PporderComponent, PporderDetailComponent, PporderUpdateComponent],
    entryComponents: [PporderComponent, PporderUpdateComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebPporderModule {}
