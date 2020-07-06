import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICPResult } from 'app/shared/model/cp-result.model';
import { ICPUncompleteDetails } from 'app/shared/model/cp-uncomplete-details.model';

type EntityResponseType = HttpResponse<ICPResult>;
type EntityArrayResponseType = HttpResponse<ICPResult[]>;

@Injectable({ providedIn: 'root' })
export class CPResultService {
    private resourceUrl = SERVER_API_URL + 'api/cp-results';

    constructor(private http: HttpClient) {}

    create(cPResult: ICPResult): Observable<EntityResponseType> {
        return this.http.post<ICPResult>(this.resourceUrl, cPResult, { observe: 'response' });
    }

    update(cPResult: ICPResult): Observable<EntityResponseType> {
        return this.http.put<ICPResult>(this.resourceUrl, cPResult, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<ICPResult>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICPResult[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    calculateCost(req: any): Observable<EntityArrayResponseType> {
        return this.http.post<ICPResult[]>(`${this.resourceUrl}/calculateCost`, req, { observe: 'response' });
    }
}
