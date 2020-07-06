import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMaterialGoodsSpecialTaxGroup } from 'app/shared/model/material-goods-special-tax-group.model';
import { IBank } from 'app/shared/model/bank.model';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';

type EntityResponseType = HttpResponse<IMaterialGoodsSpecialTaxGroup>;
type EntityArrayResponseType = HttpResponse<IMaterialGoodsSpecialTaxGroup[]>;

@Injectable({ providedIn: 'root' })
export class MaterialGoodsSpecialTaxGroupService {
    private resourceUrl = SERVER_API_URL + 'api/material-goods-special-tax-groups';

    constructor(private http: HttpClient) {}

    create(materialGoodsSpecialTaxGroup: IMaterialGoodsSpecialTaxGroup): Observable<EntityResponseType> {
        return this.http.post<IMaterialGoodsSpecialTaxGroup>(this.resourceUrl, materialGoodsSpecialTaxGroup, { observe: 'response' });
    }

    update(materialGoodsSpecialTaxGroup: IMaterialGoodsSpecialTaxGroup): Observable<EntityResponseType> {
        return this.http.put<IMaterialGoodsSpecialTaxGroup>(this.resourceUrl, materialGoodsSpecialTaxGroup, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IMaterialGoodsSpecialTaxGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoodsSpecialTaxGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getMaterialGoodsSpecialTaxGroupByCompanyID(): Observable<EntityArrayResponseType> {
        return this.http.get<IMaterialGoodsSpecialTaxGroup[]>(
            this.resourceUrl + '/find-all-material-goods-special-tax-group-by-companyid',
            { observe: 'response' }
        );
    }

    getMaterialGoodsSpecialTaxGroupByActiveExceptID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoodsSpecialTaxGroup[]>(
            this.resourceUrl + '/find-all-material-goods-special-tax-group-active-company-id-except-id',
            {
                params: options,
                observe: 'response'
            }
        );
    }

    getOneMaterialGoodsSpecialTaxGroupByActiveExceptID(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoodsSpecialTaxGroup>(
            this.resourceUrl + '/find-one-material-goods-special-tax-group-active-company-id-except-id',
            {
                params: options,
                observe: 'response'
            }
        );
    }

    pageableMaterialGoodsSpecialTaxGroup(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoodsSpecialTaxGroup[]>(this.resourceUrl + '/pageable-all-material-goods-special-tax-group', {
            params: options,
            observe: 'response'
        });
    }

    getMaterialGoodsSpecialTaxGroup(): Observable<EntityArrayResponseType> {
        return this.http.get<IMaterialGoodsSpecialTaxGroup[]>(
            this.resourceUrl + '/find-all-material-goods-special-tax-group-by-company-id',
            {
                observe: 'response'
            }
        );
    }

    getMaterialGoodsSpecialTaxGroupID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoodsSpecialTaxGroup[]>(this.resourceUrl + '/find-all-material-goods-special-tax-group-company-id', {
            params: options,
            observe: 'response'
        });
    }

    getMaterialGoodsSpecialTaxGroupActive(): Observable<EntityArrayResponseType> {
        return this.http.get<IMaterialGoodsSpecialTaxGroup[]>(
            this.resourceUrl + '/find-all-material-goods-special-tax-group-active-companyid',
            {
                observe: 'response'
            }
        );
    }

    getMaterialGoodsSpecialTaxGroupsOne(id: any): Observable<EntityArrayResponseType> {
        return this.http.get<IStatisticsCode[]>(`${this.resourceUrl + '/get-material-goods-special-tax-groups'}/${id}`, {
            observe: 'response'
        });
    }

    getActiveMaterialGoodsSpecialTaxGroup(): Observable<EntityArrayResponseType> {
        return this.http.get<IMaterialGoodsSpecialTaxGroup[]>(this.resourceUrl + '/get-all-active', { observe: 'response' });
    }

    getMaterialGoodsSpecialTaxGroups(): Observable<EntityArrayResponseType> {
        return this.http.get<IMaterialGoodsSpecialTaxGroup[]>(this.resourceUrl + '/find-all-material-goods-special-tax-group-companyid', {
            observe: 'response'
        });
    }

    getMaterialGoodsSpecialTaxGroup1(id: any): Observable<EntityArrayResponseType> {
        return this.http.get<IMaterialGoodsSpecialTaxGroup[]>(`${this.resourceUrl + '/getCbxStatisticsCodes'}/${id}`, {
            observe: 'response'
        });
    }

    getAll(req?: any): Observable<EntityArrayResponseType> {
        const option = createRequestOption(req);
        return this.http.get<IMaterialGoodsSpecialTaxGroup[]>(this.resourceUrl, { params: option, observe: 'response' });
    }
}
