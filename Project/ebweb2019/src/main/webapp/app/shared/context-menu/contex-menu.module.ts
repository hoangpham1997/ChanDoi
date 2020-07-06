import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EbContextMenuComponent } from 'app/shared/context-menu/context-menu.component';

@NgModule({
    imports: [CommonModule],
    declarations: [EbContextMenuComponent],
    entryComponents: [EbContextMenuComponent],
    exports: [EbContextMenuComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbContextMenuModule {}
