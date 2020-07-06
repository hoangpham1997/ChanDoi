import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';

type EntityResponseType = HttpResponse<IAccountingObjectBankAccount>;
type EntityArrayResponseType = HttpResponse<IAccountingObjectBankAccount[]>;

@Injectable({ providedIn: 'root' })
export class AccountingObjectBankAccountService {
    private resourceUrl = SERVER_API_URL + 'api/accounting-object-bank-accounts';

    constructor(private http: HttpClient) {}

    create(accountingObjectBankAccount: IAccountingObjectBankAccount): Observable<EntityResponseType> {
        return this.http.post<IAccountingObjectBankAccount>(this.resourceUrl, accountingObjectBankAccount, { observe: 'response' });
    }

    update(accountingObjectBankAccount: IAccountingObjectBankAccount): Observable<EntityResponseType> {
        return this.http.put<IAccountingObjectBankAccount>(this.resourceUrl, accountingObjectBankAccount, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<IAccountingObjectBankAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountingObjectBankAccount[]>(this.resourceUrl, { params: options, observe: 'response' });
    }
    getByAccountingObjectById(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/by-id', { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
    // addbymran
    createAll(accountingObjectBankAccounts: IAccountingObjectBankAccount[]): Observable<EntityArrayResponseType> {
        return this.http.post<IAccountingObjectBankAccount[]>(`${this.resourceUrl}/create-all`, accountingObjectBankAccounts, {
            observe: 'response'
        });
    }

    findDTO(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountingObjectBankAccount[]>(`${this.resourceUrl}/dto`, { params: options, observe: 'response' });
    }
}
