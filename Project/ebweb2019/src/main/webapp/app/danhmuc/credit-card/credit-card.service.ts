import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICreditCard } from 'app/shared/model/credit-card.model';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import { map } from 'rxjs/operators';
import { IMaterialQuantum } from 'app/shared/model/material-quantum.model';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { IBank } from 'app/shared/model/bank.model';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';

type EntityResponseType = HttpResponse<ICreditCard>;
type EntityArrayResponseType = HttpResponse<ICreditCard[]>;

@Injectable({ providedIn: 'root' })
export class CreditCardService {
    private resourceUrl = SERVER_API_URL + 'api/credit-cards';

    constructor(private http: HttpClient) {}

    create(creditCard: ICreditCard): Observable<EntityResponseType> {
        return this.http.post<ICreditCard>(this.resourceUrl, creditCard, { observe: 'response' });
    }

    update(creditCard: ICreditCard): Observable<EntityResponseType> {
        return this.http.put<ICreditCard>(this.resourceUrl, creditCard, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<ICreditCard>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICreditCard[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    getAllCreditCards(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICreditCard[]>(this.resourceUrl + '/getAllCreditCards', { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findByCreditCardNumber(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICreditCard>(this.resourceUrl + '/findByCreditCardNumber', { params: options, observe: 'response' });
    }

    getCreditCardsByCompanyID(): Observable<EntityArrayResponseType> {
        return this.http.get<ICreditCard[]>(this.resourceUrl + '/find-all-credit-cards-by-companyid', { observe: 'response' });
    }

    getCreditCardsActiveByCompanyID(): Observable<EntityArrayResponseType> {
        return this.http.get<ICreditCard[]>(this.resourceUrl + '/find-all-credit-cards-active-by-companyid', { observe: 'response' });
    }

    pageableCreditCard(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICreditCard[]>(this.resourceUrl + '/pageable-all-credit-card', { params: options, observe: 'response' });
    }

    pageableCreditCard1(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICreditCard[]>(this.resourceUrl + '/pageable-all-credit-cards', { params: options, observe: 'response' });
    }

    deleteCreditCardID(rq: any[]): Observable<HttpResponse<HandlingResult>> {
        return this.http.post<any>(`${this.resourceUrl}/delete-credit-card`, rq, { observe: 'response' });
    }
}
