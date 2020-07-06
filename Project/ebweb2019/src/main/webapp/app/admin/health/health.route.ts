import { Route } from '@angular/router';

import { EbHealthCheckComponent } from './health.component';

export const healthRoute: Route = {
    path: 'eb-health',
    component: EbHealthCheckComponent,
    data: {
        pageTitle: 'health.title'
    }
};
