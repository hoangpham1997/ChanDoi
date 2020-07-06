import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IGenCode } from 'app/shared/model/gen-code.model';
import { ISystemOption } from 'app/shared/model/system-option.model';

type EntityResponseType = HttpResponse<IGenCode>;
type EntityArrayResponseType = HttpResponse<IGenCode[]>;

@Injectable({ providedIn: 'root' })
export class GenCodeService {
    private resourceUrl = SERVER_API_URL + 'api/gen-codes';

    constructor(private http: HttpClient) {}

    create(genCode: IGenCode): Observable<EntityResponseType> {
        return this.http.post<IGenCode>(this.resourceUrl, genCode, { observe: 'response' });
    }

    update(genCode: IGenCode): Observable<EntityResponseType> {
        return this.http.put<IGenCode>(this.resourceUrl, genCode, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IGenCode>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IGenCode[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getGenCodes(): Observable<EntityArrayResponseType> {
        return this.http.get<IGenCode[]>(this.resourceUrl + '/find-all-gen-codes-companyid', { observe: 'response' });
    }
}
