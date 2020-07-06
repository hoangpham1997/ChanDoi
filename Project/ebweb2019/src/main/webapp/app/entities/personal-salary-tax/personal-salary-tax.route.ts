import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PersonalSalaryTax } from 'app/shared/model/personal-salary-tax.model';
import { PersonalSalaryTaxService } from './personal-salary-tax.service';
import { PersonalSalaryTaxComponent } from './personal-salary-tax.component';
import { PersonalSalaryTaxDetailComponent } from './personal-salary-tax-detail.component';
import { PersonalSalaryTaxUpdateComponent } from './personal-salary-tax-update.component';
import { PersonalSalaryTaxDeletePopupComponent } from './personal-salary-tax-delete-dialog.component';
import { IPersonalSalaryTax } from 'app/shared/model/personal-salary-tax.model';

@Injectable({ providedIn: 'root' })
export class PersonalSalaryTaxResolve implements Resolve<IPersonalSalaryTax> {
    constructor(private service: PersonalSalaryTaxService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((personalSalaryTax: HttpResponse<PersonalSalaryTax>) => personalSalaryTax.body));
        }
        return of(new PersonalSalaryTax());
    }
}

export const personalSalaryTaxRoute: Routes = [
    {
        path: 'personal-salary-tax',
        component: PersonalSalaryTaxComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.personalSalaryTax.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'personal-salary-tax/:id/view',
        component: PersonalSalaryTaxDetailComponent,
        resolve: {
            personalSalaryTax: PersonalSalaryTaxResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.personalSalaryTax.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'personal-salary-tax/new',
        component: PersonalSalaryTaxUpdateComponent,
        resolve: {
            personalSalaryTax: PersonalSalaryTaxResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.personalSalaryTax.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'personal-salary-tax/:id/edit',
        component: PersonalSalaryTaxUpdateComponent,
        resolve: {
            personalSalaryTax: PersonalSalaryTaxResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.personalSalaryTax.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const personalSalaryTaxPopupRoute: Routes = [
    {
        path: 'personal-salary-tax/:id/delete',
        component: PersonalSalaryTaxDeletePopupComponent,
        resolve: {
            personalSalaryTax: PersonalSalaryTaxResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.personalSalaryTax.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
