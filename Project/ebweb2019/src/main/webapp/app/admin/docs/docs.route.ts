import { Route } from '@angular/router';

import { EbDocsComponent } from './docs.component';

export const docsRoute: Route = {
    path: 'docs',
    component: EbDocsComponent,
    data: {
        pageTitle: 'global.menu.admin.apidocs'
    }
};
