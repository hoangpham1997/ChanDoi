import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IUnit } from 'app/shared/model/unit.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IBank } from 'app/shared/model/bank.model';
import { ICPAllocationRate } from 'app/shared/model/cp-allocation-rate.model';
import { ICPAllocationQuantum } from 'app/shared/model/cp-allocation-quantum.model';

type EntityResponseType = HttpResponse<ICostSet>;
type EntityArrayResponseType = HttpResponse<ICostSet[]>;

@Injectable({ providedIn: 'root' })
export class CostSetService {
    private resourceUrl = SERVER_API_URL + 'api/cost-sets';

    constructor(private http: HttpClient) {}

    create(costSet: ICostSet): Observable<EntityResponseType> {
        return this.http.post<ICostSet>(this.resourceUrl, costSet, { observe: 'response' });
    }

    update(costSet: ICostSet): Observable<EntityResponseType> {
        return this.http.put<ICostSet>(this.resourceUrl, costSet, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICostSet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICostSet[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    getAllCostSets(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICostSet[]>(this.resourceUrl + '/find-all-cost-sets-by-company-id', { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICostSet[]>(`${this.resourceUrl}/search-all`, { params: options, observe: 'response' });
    }

    searchAllActive(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICostSet[]>(`${this.resourceUrl}/search-all-active`, { params: options, observe: 'response' });
    }

    getCostSets(): Observable<EntityArrayResponseType> {
        return this.http.get<ICostSet[]>(this.resourceUrl + '/find-all-cost-sets-active-companyid', { observe: 'response' });
    }
    getCostSetsByType(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICostSet[]>(this.resourceUrl + '/find-cost-sets-by-type', {
            params: options,
            observe: 'response'
        });
    }

    getCostSetsByCompanyID(): Observable<EntityArrayResponseType> {
        return this.http.get<ICostSet[]>(this.resourceUrl + '/find-all-cost-sets-by-companyid', { observe: 'response' });
    }

    findByListID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICostSet[]>(this.resourceUrl + '/find-by-list-id-cost-set', {
            params: options,
            observe: 'response'
        });
    }

    getCostSetsByOrg(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICostSet[]>(this.resourceUrl + '/find-all-cost-sets-by-org', { params: options, observe: 'response' });
    }

    findRevenueByCostSetID(costSetDTO: any): Observable<HttpResponse<any[]>> {
        return this.http.post<any[]>(`${this.resourceUrl}/find-revenue-by-costset-id`, costSetDTO, { observe: 'response' });
    }

    getCostSetList(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICostSet[]>(`${this.resourceUrl}/find-all-cost-set-dependent`, { params: options, observe: 'response' });
    }
}
