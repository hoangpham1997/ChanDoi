import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IUnit } from 'app/shared/model/unit.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';

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
        return this.http.get<ICostSet[]>(this.resourceUrl + '/getAllCostSets', { params: options, observe: 'response' });
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
        return this.http.get<ICostSet[]>(`${this.resourceUrl}/search-all-active`, {
            params: options,
            observe: 'response'
        });
    }

    getCostSets(): Observable<EntityArrayResponseType> {
        return this.http.get<ICostSet[]>(this.resourceUrl + '/find-all-cost-sets-active-companyid', { observe: 'response' });
    }

    getCostSetsByCompanyId(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICostSet[]>(`${this.resourceUrl}/find-all-cost-set-by-companyid`, {
            params: options,
            observe: 'response'
        });
    }

    multiDelete(obj: any): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceUrl}/multi-delete-cost-set`, obj, { observe: 'response' });
    }
}
