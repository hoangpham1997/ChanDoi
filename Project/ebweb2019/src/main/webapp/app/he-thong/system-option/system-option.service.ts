import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISystemOption } from 'app/shared/model/system-option.model';
import { Moment } from 'moment';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IAccountDefault } from 'app/shared/model/account-default.model';
import { IGenCode } from 'app/shared/model/gen-code.model';

type EntityResponseString = HttpResponse<string>;
type EntityResponseType = HttpResponse<ISystemOption>;
type EntityArrayResponseType = HttpResponse<ISystemOption[]>;

@Injectable({ providedIn: 'root' })
export class SystemOptionService {
    private resourceUrl = SERVER_API_URL + 'api/system-options';

    constructor(private http: HttpClient) {}

    create(systemOption: ISystemOption): Observable<EntityResponseType> {
        return this.http.post<ISystemOption>(this.resourceUrl, systemOption, { observe: 'response' });
    }

    update(systemOption: ISystemOption): Observable<EntityResponseType> {
        return this.http.put<ISystemOption>(this.resourceUrl, systemOption, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISystemOption>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISystemOption[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    findOneByUser(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISystemOption>(this.resourceUrl + '/findOne', { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    updatePostedDate(req?: string): Observable<EntityResponseString> {
        return this.http.put<string>(`${this.resourceUrl}/update-posted-date`, req, { observe: 'response' });
    }

    getSystemOptions(): Observable<EntityArrayResponseType> {
        return this.http.get<ISystemOption[]>(this.resourceUrl + '/find-all-system-options-companyid', { observe: 'response' });
    }

    save(req?: any): Observable<EntityResponseString> {
        return this.http.put<string>(`${this.resourceUrl}/save-all`, req, { observe: 'response' });
    }

    getSystemOptionsByCompanyID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISystemOption[]>(this.resourceUrl + '/find-system-options-by-companyid', {
            params: options,
            observe: 'response'
        });
    }
}
