import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    RepositoryLedgerComponent,
    RepositoryLedgerDetailComponent,
    RepositoryLedgerUpdateComponent,
    RepositoryLedgerDeletePopupComponent,
    RepositoryLedgerDeleteDialogComponent,
    repositoryLedgerRoute,
    repositoryLedgerPopupRoute
} from './';

const ENTITY_STATES = [...repositoryLedgerRoute, ...repositoryLedgerPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RepositoryLedgerComponent,
        RepositoryLedgerDetailComponent,
        RepositoryLedgerUpdateComponent,
        RepositoryLedgerDeleteDialogComponent,
        RepositoryLedgerDeletePopupComponent
    ],
    entryComponents: [
        RepositoryLedgerComponent,
        RepositoryLedgerUpdateComponent,
        RepositoryLedgerDeleteDialogComponent,
        RepositoryLedgerDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebRepositoryLedgerModule {}
