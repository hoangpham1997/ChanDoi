import { NgModule } from '@angular/core';

import { EbwebSharedLibsModule, FindLanguageFromKeyPipe, EbAlertComponent, EbAlertErrorComponent } from './';

@NgModule({
    imports: [EbwebSharedLibsModule],
    declarations: [FindLanguageFromKeyPipe, EbAlertComponent, EbAlertErrorComponent],
    exports: [EbwebSharedLibsModule, FindLanguageFromKeyPipe, EbAlertComponent, EbAlertErrorComponent]
})
export class EbwebSharedCommonModule {}
