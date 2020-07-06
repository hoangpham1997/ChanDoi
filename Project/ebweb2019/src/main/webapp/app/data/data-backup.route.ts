import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { DataBackupComponent } from 'app/data/data-backup.component';

export const dataBackupRoute: Routes = [
    {
        path: '',
        component: DataBackupComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.dataBackup.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
