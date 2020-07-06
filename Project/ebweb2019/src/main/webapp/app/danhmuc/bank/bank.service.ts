import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBank } from 'app/shared/model/bank.model';
import { IUnit } from 'app/shared/model/unit.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';

type EntityResponseType = HttpResponse<IBank>;
type EntityArrayResponseType = HttpResponse<IBank[]>;

@Injectable({ providedIn: 'root' })
export class BankService {
    private resourceUrl = SERVER_API_URL + 'api/banks';

    constructor(private http: HttpClient) {}

    create(bank: IBank): Observable<EntityResponseType> {
        return this.http.post<IBank>(this.resourceUrl, bank, { observe: 'response' });
    }

    update(bank: IBank): Observable<EntityResponseType> {
        return this.http.put<IBank>(this.resourceUrl, bank, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IBank>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findIdPopup(id: string): Observable<EntityResponseType> {
        return this.http.get<IBank>(`${this.resourceUrl + '/find-bank-id'}/${id}`, {
            observe: 'response'
        });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBank[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    getAllBanks(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBank[]>(this.resourceUrl + '/getAllBanks', { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    deleteByListID(rq: any[]): Observable<HttpResponse<HandlingResult>> {
        return this.http.post<any>(`${this.resourceUrl}/delete-list`, rq, { observe: 'response' });
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBank[]>(`${this.resourceUrl}/search-all`, { params: options, observe: 'response' });
    }

    searchAllBank(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBank[]>(`${this.resourceUrl}/bank-search-all`, {
            params: options,
            observe: 'response'
        });
    }

    getBanks(): Observable<EntityArrayResponseType> {
        return this.http.get<IBank[]>(this.resourceUrl + '/find-all-banks-active-companyid', { observe: 'response' });
    }

    getBanksByCompanyID(): Observable<EntityArrayResponseType> {
        return this.http.get<IBank[]>(this.resourceUrl + '/find-all-banks-by-companyid', { observe: 'response' });
    }

    pageableBank(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBank[]>(this.resourceUrl + '/pageable-all-bank', { params: options, observe: 'response' });
    }
}
