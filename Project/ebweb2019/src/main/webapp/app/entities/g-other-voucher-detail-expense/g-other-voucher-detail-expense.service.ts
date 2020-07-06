import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IGOtherVoucherDetailExpense } from 'app/shared/model/g-other-voucher-detail-expense.model';

type EntityResponseType = HttpResponse<IGOtherVoucherDetailExpense>;
type EntityArrayResponseType = HttpResponse<IGOtherVoucherDetailExpense[]>;

@Injectable({ providedIn: 'root' })
export class GOtherVoucherDetailExpenseService {
    private resourceUrl = SERVER_API_URL + 'api/g-other-voucher-detail-expenses';

    constructor(private http: HttpClient) {}

    create(gOtherVoucherDetailExpense: IGOtherVoucherDetailExpense): Observable<EntityResponseType> {
        return this.http.post<IGOtherVoucherDetailExpense>(this.resourceUrl, gOtherVoucherDetailExpense, { observe: 'response' });
    }

    update(gOtherVoucherDetailExpense: IGOtherVoucherDetailExpense): Observable<EntityResponseType> {
        return this.http.put<IGOtherVoucherDetailExpense>(this.resourceUrl, gOtherVoucherDetailExpense, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IGOtherVoucherDetailExpense>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IGOtherVoucherDetailExpense[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
