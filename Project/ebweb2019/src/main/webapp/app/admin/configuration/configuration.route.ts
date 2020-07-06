import { Route } from '@angular/router';

import { EbConfigurationComponent } from './configuration.component';

export const configurationRoute: Route = {
    path: 'eb-configuration',
    component: EbConfigurationComponent,
    data: {
        pageTitle: 'configuration.title'
    }
};
