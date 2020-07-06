import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPporderdetail, PPOrderDetail } from 'app/shared/model/pporderdetail.model';

type EntityResponseType = HttpResponse<IPporderdetail>;
type EntityArrayResponseType = HttpResponse<IPporderdetail[]>;

@Injectable({ providedIn: 'root' })
export class PporderdetailService {
    private resourceUrl = SERVER_API_URL + 'api/pporderdetails';

    constructor(private http: HttpClient) {}

    create(pporderdetail: IPporderdetail): Observable<EntityResponseType> {
        return this.http.post<IPporderdetail>(this.resourceUrl, pporderdetail, { observe: 'response' });
    }

    update(pporderdetail: IPporderdetail): Observable<EntityResponseType> {
        return this.http.put<IPporderdetail>(this.resourceUrl, pporderdetail, { observe: 'response' });
    }

    find(id: string): Observable<HttpResponse<PPOrderDetail>> {
        return this.http.get<PPOrderDetail>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPporderdetail[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    queryNoPage(): Observable<EntityArrayResponseType> {
        return this.http.get<IPporderdetail[]>(this.resourceUrl + '/no-page', { observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
