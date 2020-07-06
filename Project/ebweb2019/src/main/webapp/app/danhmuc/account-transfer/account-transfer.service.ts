import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAccountTransfer } from 'app/shared/model/account-transfer.model';
import { IAccountList } from 'app/shared/model/account-list.model';

type EntityResponseType = HttpResponse<IAccountTransfer>;
type EntityArrayResponseType = HttpResponse<IAccountTransfer[]>;

@Injectable({ providedIn: 'root' })
export class AccountTransferService {
    private resourceUrl = SERVER_API_URL + 'api/account-transfers';

    constructor(private http: HttpClient) {}

    create(accountTransfer: IAccountTransfer): Observable<EntityResponseType> {
        return this.http.post<IAccountTransfer>(this.resourceUrl, accountTransfer, { observe: 'response' });
    }

    update(accountTransfer: IAccountTransfer): Observable<EntityResponseType> {
        return this.http.put<IAccountTransfer>(this.resourceUrl, accountTransfer, { observe: 'response' });
    }

    find(id: any): Observable<EntityResponseType> {
        return this.http.get<IAccountTransfer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountTransfer[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getAccountTransfers(): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountTransfer[]>(this.resourceUrl + '/find-all-account-transfers-companyid', { observe: 'response' });
    }
}
