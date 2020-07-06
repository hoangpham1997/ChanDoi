import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ContextMenuComponent } from 'app/contextmenu/contextmenu.component';
import { CommonModule } from '@angular/common';

@NgModule({
    imports: [CommonModule],
    declarations: [ContextMenuComponent],
    entryComponents: [ContextMenuComponent],
    exports: [ContextMenuComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebContextMenuModule {}
