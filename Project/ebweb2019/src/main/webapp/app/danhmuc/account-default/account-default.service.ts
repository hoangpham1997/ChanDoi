import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAccountDefault } from 'app/shared/model/account-default.model';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IBank } from 'app/shared/model/bank.model';
import { IAccountDefaultCategory } from 'app/shared/model/account-default-category.model';
import { ICPProductQuantum } from 'app/shared/model/cp-product-quantum.model';

type EntityResponseType = HttpResponse<IAccountDefault>;
type EntityArrayResponseType = HttpResponse<IAccountDefault[]>;

@Injectable({ providedIn: 'root' })
export class AccountDefaultService {
    private resourceUrl = SERVER_API_URL + 'api/account-defaults';

    constructor(private http: HttpClient) {}

    create(accountDefault: IAccountDefault): Observable<EntityResponseType> {
        return this.http.post<IAccountDefault>(this.resourceUrl, accountDefault, { observe: 'response' });
    }

    update(accountDefault: IAccountDefault): Observable<EntityResponseType> {
        return this.http.put<IAccountDefault>(this.resourceUrl, accountDefault, { observe: 'response' });
    }

    find(id: any): Observable<EntityResponseType> {
        return this.http.get<IAccountDefault>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountDefault[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    getAllAccountDefaults(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountDefault[]>(this.resourceUrl + '/getAllAccountDefaults', { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getAllAccountListByAccountType(req?: any): Observable<HttpResponse<IAccountList[]>> {
        const options = createRequestOption(req);
        return this.http.get<IAccountList[]>(this.resourceUrl + '/getAllAccountDefaults', { params: options, observe: 'response' });
    }

    getAccountDefaultsByCompanyID(): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountDefaultCategory[]>(this.resourceUrl + '/find-all-account-defaults-by-companyid', {
            observe: 'response'
        });
    }

    getAccountDefaultByTypeID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountDefault[]>(this.resourceUrl + '/find-by-type-id', {
            params: options,
            observe: 'response'
        });
    }

    save(accountDefaults: IAccountDefault[]): Observable<EntityArrayResponseType> {
        return this.http.put<IAccountDefault[]>(`${this.resourceUrl}/save-all`, accountDefaults, { observe: 'response' });
    }
}
