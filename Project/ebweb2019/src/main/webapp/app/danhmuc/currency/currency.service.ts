import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICurrency } from 'app/shared/model/currency.model';
import { ICreditCard } from 'app/shared/model/credit-card.model';
import { IBank } from 'app/shared/model/bank.model';

type EntityResponseType = HttpResponse<ICurrency>;
type EntityArrayResponseType = HttpResponse<ICurrency[]>;

@Injectable({ providedIn: 'root' })
export class CurrencyService {
    private resourceUrl = SERVER_API_URL + 'api/currencies';

    constructor(private http: HttpClient) {}

    create(currency: ICurrency): Observable<EntityResponseType> {
        return this.http.post<ICurrency>(this.resourceUrl, currency, { observe: 'response' });
    }

    update(currency: ICurrency): Observable<EntityResponseType> {
        return this.http.put<ICurrency>(this.resourceUrl, currency, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICurrency>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICurrency[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    getCurrency(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICurrency[]>(SERVER_API_URL + 'api/get-currency', { params: options, observe: 'response' });
    }

    getAllCurrencies(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICurrency[]>(this.resourceUrl + '/getAllCurrencies', {
            params: options,
            observe: 'response'
        });
    }

    getAllActiveCurrencies(): Observable<EntityArrayResponseType> {
        return this.http.get<ICurrency[]>(this.resourceUrl + '/is-active', { observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findAllActive(): Observable<EntityArrayResponseType> {
        return this.http.get<ICurrency[]>(`${this.resourceUrl}/active`, { observe: 'response' });
    }

    findAllByCompanyID(): Observable<EntityArrayResponseType> {
        return this.http.get<ICurrency[]>(`${this.resourceUrl}/find-all-by-company-id`, { observe: 'response' });
    }

    findActiveDefault(): Observable<EntityResponseType> {
        return this.http.get<ICurrency>(`${this.resourceUrl}/active/default`, { observe: 'response' });
    }

    findAllByCompanyIDIsNull(): Observable<EntityArrayResponseType> {
        return this.http.get<ICurrency[]>(`${this.resourceUrl}/find-all-by-company-id-null`, { observe: 'response' });
    }

    pageableCurrency(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICurrency[]>(this.resourceUrl + '/pageable-all-currency', { params: options, observe: 'response' });
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICurrency[]>(`${this.resourceUrl}/search-all-currency`, {
            params: options,
            observe: 'response'
        });
    }

    findCurrencyByCompanyID(req?: any): Observable<EntityArrayResponseType> {
        return this.http.get<ICurrency[]>(`${this.resourceUrl}/find-currency-by-company-id`, {
            observe: 'response'
        });
    }
}
