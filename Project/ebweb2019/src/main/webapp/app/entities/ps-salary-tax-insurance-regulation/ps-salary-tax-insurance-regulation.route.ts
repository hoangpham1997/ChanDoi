import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PSSalaryTaxInsuranceRegulation } from 'app/shared/model/ps-salary-tax-insurance-regulation.model';
import { PSSalaryTaxInsuranceRegulationService } from './ps-salary-tax-insurance-regulation.service';
import { PSSalaryTaxInsuranceRegulationComponent } from './ps-salary-tax-insurance-regulation.component';
import { PSSalaryTaxInsuranceRegulationDetailComponent } from './ps-salary-tax-insurance-regulation-detail.component';
import { PSSalaryTaxInsuranceRegulationUpdateComponent } from './ps-salary-tax-insurance-regulation-update.component';
import { PSSalaryTaxInsuranceRegulationDeletePopupComponent } from './ps-salary-tax-insurance-regulation-delete-dialog.component';
import { IPSSalaryTaxInsuranceRegulation } from 'app/shared/model/ps-salary-tax-insurance-regulation.model';

@Injectable({ providedIn: 'root' })
export class PSSalaryTaxInsuranceRegulationResolve implements Resolve<IPSSalaryTaxInsuranceRegulation> {
    constructor(private service: PSSalaryTaxInsuranceRegulationService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(
                    map(
                        (pSSalaryTaxInsuranceRegulation: HttpResponse<PSSalaryTaxInsuranceRegulation>) =>
                            pSSalaryTaxInsuranceRegulation.body
                    )
                );
        }
        return of(new PSSalaryTaxInsuranceRegulation());
    }
}

export const pSSalaryTaxInsuranceRegulationRoute: Routes = [
    {
        path: 'ps-salary-tax-insurance-regulation',
        component: PSSalaryTaxInsuranceRegulationComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.pSSalaryTaxInsuranceRegulation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'ps-salary-tax-insurance-regulation/:id/view',
        component: PSSalaryTaxInsuranceRegulationDetailComponent,
        resolve: {
            pSSalaryTaxInsuranceRegulation: PSSalaryTaxInsuranceRegulationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pSSalaryTaxInsuranceRegulation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'ps-salary-tax-insurance-regulation/new',
        component: PSSalaryTaxInsuranceRegulationUpdateComponent,
        resolve: {
            pSSalaryTaxInsuranceRegulation: PSSalaryTaxInsuranceRegulationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pSSalaryTaxInsuranceRegulation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'ps-salary-tax-insurance-regulation/:id/edit',
        component: PSSalaryTaxInsuranceRegulationUpdateComponent,
        resolve: {
            pSSalaryTaxInsuranceRegulation: PSSalaryTaxInsuranceRegulationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pSSalaryTaxInsuranceRegulation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pSSalaryTaxInsuranceRegulationPopupRoute: Routes = [
    {
        path: 'ps-salary-tax-insurance-regulation/:id/delete',
        component: PSSalaryTaxInsuranceRegulationDeletePopupComponent,
        resolve: {
            pSSalaryTaxInsuranceRegulation: PSSalaryTaxInsuranceRegulationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pSSalaryTaxInsuranceRegulation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
