import { Route } from '@angular/router';

import { EbMetricsMonitoringComponent } from './metrics.component';

export const metricsRoute: Route = {
    path: 'eb-metrics',
    component: EbMetricsMonitoringComponent,
    data: {
        pageTitle: 'metrics.title'
    }
};
