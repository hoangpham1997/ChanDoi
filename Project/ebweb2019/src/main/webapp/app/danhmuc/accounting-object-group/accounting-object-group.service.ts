import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAccountingObjectGroup } from 'app/shared/model/accounting-object-group.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { map } from 'rxjs/operators';
import { IUnit } from 'app/shared/model/unit.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';

type EntityResponseType = HttpResponse<IAccountingObjectGroup>;
type EntityArrayResponseType = HttpResponse<IAccountingObjectGroup[]>;

@Injectable({ providedIn: 'root' })
export class AccountingObjectGroupService {
    private resourceUrl = SERVER_API_URL + 'api/accounting-object-groups';

    constructor(private http: HttpClient) {}

    create(accountingObjectGroup: IAccountingObjectGroup): Observable<EntityResponseType> {
        return this.http.post<IAccountingObjectGroup>(this.resourceUrl, accountingObjectGroup, { observe: 'response' });
    }

    update(accountingObjectGroup: IAccountingObjectGroup): Observable<EntityResponseType> {
        return this.http.put<IAccountingObjectGroup>(this.resourceUrl, accountingObjectGroup, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<IAccountingObjectGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountingObjectGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountingObjectGroup[]>(`${this.resourceUrl}/search-all`, { params: options, observe: 'response' });
    }

    getAllAccountingObjectGroup(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountingObjectGroup[]>(this.resourceUrl + '/find-all-accounting-object-group-by-companyid', {
            params: options,
            observe: 'response'
        });
    }

    getLoadAllAccountingObjectGroup(): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountingObjectGroup[]>(this.resourceUrl + '/load-all-accounting-object-group-by-companyid', {
            observe: 'response'
        });
    }
    getCbxAccountingObjectGroup(id: any): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountingObjectGroup[]>(`${this.resourceUrl + '/getCbxAccountingObjectGroup'}/${id}`, {
            observe: 'response'
        });
    }
    getAllAccountingObjectGroupSimilarBranch(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountingObjectGroup[]>(
            this.resourceUrl + '/find-all-accounting-object-group-by-company-id-similar-branch',
            {
                params: options,
                observe: 'response'
            }
        );
    }
}
