import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPaymentClause } from 'app/shared/model/payment-clause.model';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { ISAInvoice } from 'app/shared/model/sa-invoice.model';

type EntityResponseType = HttpResponse<IPaymentClause>;
type EntityArrayResponseType = HttpResponse<IPaymentClause[]>;

@Injectable({ providedIn: 'root' })
export class PaymentClauseService {
    private resourceUrl = SERVER_API_URL + 'api/payment-clauses';

    constructor(private http: HttpClient) {}

    create(paymentClause: IPaymentClause): Observable<EntityResponseType> {
        return this.http.post<IPaymentClause>(this.resourceUrl, paymentClause, { observe: 'response' });
    }

    update(paymentClause: IPaymentClause): Observable<EntityResponseType> {
        return this.http.put<IPaymentClause>(this.resourceUrl, paymentClause, { observe: 'response' });
    }

    find(id: any): Observable<EntityResponseType> {
        return this.http.get<IPaymentClause>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPaymentClause[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    getAllPaymentClauses(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPaymentClause[]>(this.resourceUrl + '/getAllPaymentClauses', {
            params: options,
            observe: 'response'
        });
    }
    // getAllByCompanyID(req?: any): Observable<EntityArrayResponseType> {
    //     const options = createRequestOption(req);
    //     return this.http.get<IPaymentClause[]>(`${this.resourceUrl}/get-all-by-company-id`, { params: options, observe: 'response' });
    // }
    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    multiDelete(obj: IPaymentClause[]): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceUrl}/multi-delete-payment-clause`, obj, { observe: 'response' });
    }
}
