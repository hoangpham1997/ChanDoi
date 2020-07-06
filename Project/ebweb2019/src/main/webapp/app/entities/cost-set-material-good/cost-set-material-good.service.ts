import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICostSetMaterialGood } from 'app/shared/model/cost-set-material-good.model';
import { ICostSet } from 'app/shared/model/cost-set.model';

type EntityResponseType = HttpResponse<ICostSetMaterialGood>;
type EntityArrayResponseType = HttpResponse<ICostSetMaterialGood[]>;

@Injectable({ providedIn: 'root' })
export class CostSetMaterialGoodService {
    private resourceUrl = SERVER_API_URL + 'api/cost-set-material-goods';

    constructor(private http: HttpClient) {}

    create(costSetMaterialGood: ICostSetMaterialGood): Observable<EntityResponseType> {
        return this.http.post<ICostSetMaterialGood>(this.resourceUrl, costSetMaterialGood, { observe: 'response' });
    }

    update(costSetMaterialGood: ICostSetMaterialGood): Observable<EntityResponseType> {
        return this.http.put<ICostSetMaterialGood>(this.resourceUrl, costSetMaterialGood, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICostSetMaterialGood>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICostSetMaterialGood[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getAllByCompanyID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/get-all-by-company-id', { params: options, observe: 'response' });
    }

    getAllForReport(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/get-all-for-report', { params: options, observe: 'response' });
    }
}
