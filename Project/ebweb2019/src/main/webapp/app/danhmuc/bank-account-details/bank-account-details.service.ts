import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IUnit } from 'app/shared/model/unit.model';
import { IRepository } from 'app/shared/model/repository.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';

type EntityResponseType = HttpResponse<IBankAccountDetails>;
type EntityArrayResponseType = HttpResponse<IBankAccountDetails[]>;

@Injectable({ providedIn: 'root' })
export class BankAccountDetailsService {
    private resourceUrl = SERVER_API_URL + 'api/bank-account-details';

    constructor(private http: HttpClient) {}

    create(bankAccountDetails: IBankAccountDetails): Observable<EntityResponseType> {
        return this.http.post<IBankAccountDetails>(this.resourceUrl, bankAccountDetails, { observe: 'response' });
    }

    update(bankAccountDetails: IBankAccountDetails): Observable<EntityResponseType> {
        return this.http.put<IBankAccountDetails>(this.resourceUrl, bankAccountDetails, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IBankAccountDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findIdPopup(id: string): Observable<EntityArrayResponseType> {
        return this.http.get<IBankAccountDetails[]>(this.resourceUrl + '/find-bank-account-details-id', {
            observe: 'response'
        });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBankAccountDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    getAllBankAccountDetails(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBankAccountDetails[]>(this.resourceUrl + '/getAllBankAccountDetails', {
            params: options,
            observe: 'response'
        });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getBankAccountDetails(): Observable<EntityArrayResponseType> {
        return this.http.get<IBankAccountDetails[]>(this.resourceUrl + '/find-all-bank-account-details-active-companyid', {
            observe: 'response'
        });
    }

    getBankAccountDetailsNotCreditCard(): Observable<EntityArrayResponseType> {
        return this.http.get<IBankAccountDetails[]>(this.resourceUrl + '/find-all-bank-account-details-active-companyid-not-credit-card', {
            observe: 'response'
        });
    }

    getBankAccountDetailsByCompanyID(): Observable<EntityArrayResponseType> {
        return this.http.get<IBankAccountDetails[]>(this.resourceUrl + '/find-all-bank-account-details-by-companyid', {
            observe: 'response'
        });
    }

    getBankAccountDetailsForAccType(): Observable<EntityArrayResponseType> {
        return this.http.get<IBankAccountDetails[]>(this.resourceUrl + '/find-all-for-acc-type', {
            observe: 'response'
        });
    }

    getBankAccountDetailsCustom(): Observable<EntityArrayResponseType> {
        return this.http.get<IBankAccountDetails[]>(this.resourceUrl + '/find-all-bank-account-details-active-companyid-custom', {
            observe: 'response'
        });
    }

    allBankAccountDetails(): Observable<EntityArrayResponseType> {
        return this.http.get<IBankAccountDetails[]>(this.resourceUrl + '/find-all-bank-account-details', {
            observe: 'response'
        });
    }
    pageableBankAccountDetails(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBankAccountDetails[]>(this.resourceUrl + '/pageable-all-bank-account-details', {
            params: options,
            observe: 'response'
        });
    }

    deleteByListIDUnit(rq: any[]): Observable<HttpResponse<HandlingResult>> {
        return this.http.post<any>(`${this.resourceUrl}/delete-list-bank-account-details`, rq, { observe: 'response' });
    }

    getBankAccountDetailsByOrgID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBankAccountDetails[]>(this.resourceUrl + '/find-all-bank-account-details-active-by-orgid', {
            params: options,
            observe: 'response'
        });
    }
}
