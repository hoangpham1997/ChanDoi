import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { IBank } from 'app/shared/model/bank.model';

type EntityResponseType = HttpResponse<IAutoPrinciple>;
type EntityArrayResponseType = HttpResponse<IAutoPrinciple[]>;

@Injectable({ providedIn: 'root' })
export class AutoPrincipleService {
    private resourceUrl = SERVER_API_URL + 'api/auto-principles';

    constructor(private http: HttpClient) {}

    create(autoPrinciple: IAutoPrinciple): Observable<EntityResponseType> {
        return this.http.post<IAutoPrinciple>(this.resourceUrl, autoPrinciple, { observe: 'response' });
    }

    update(autoPrinciple: IAutoPrinciple): Observable<EntityResponseType> {
        return this.http.put<IAutoPrinciple>(this.resourceUrl, autoPrinciple, { observe: 'response' });
    }

    find(id: any): Observable<EntityResponseType> {
        return this.http.get<IAutoPrinciple>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAutoPrinciple[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    getAllAutoPrinciples(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAutoPrinciple[]>(this.resourceUrl + '/getAllAutoPrinciples', { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getAutoPrinciples(): Observable<EntityArrayResponseType> {
        return this.http.get<IAutoPrinciple[]>(this.resourceUrl + '/find-all-auto-principle-active-companyid', { observe: 'response' });
    }

    getAutoPrinciplesByCompanyID(): Observable<EntityArrayResponseType> {
        return this.http.get<IAutoPrinciple[]>(this.resourceUrl + '/find-all-auto-principle-by-companyid', { observe: 'response' });
    }

    getByTypeAndCompanyID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAutoPrinciple[]>(this.resourceUrl + '/type-and-companyid', { params: options, observe: 'response' });
    }

    pageableAutoPrinciple(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAutoPrinciple[]>(this.resourceUrl + '/pageable-all-auto-principle', { params: options, observe: 'response' });
    }
}
