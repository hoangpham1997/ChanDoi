import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IGOtherVoucherDetailExpenseAllocation } from 'app/shared/model/g-other-voucher-detail-expense-allocation.model';

type EntityResponseType = HttpResponse<IGOtherVoucherDetailExpenseAllocation>;
type EntityArrayResponseType = HttpResponse<IGOtherVoucherDetailExpenseAllocation[]>;

@Injectable({ providedIn: 'root' })
export class GOtherVoucherDetailExpenseAllocationService {
    private resourceUrl = SERVER_API_URL + 'api/g-other-voucher-detail-expense-allocations';

    constructor(private http: HttpClient) {}

    create(gOtherVoucherDetailExpenseAllocation: IGOtherVoucherDetailExpenseAllocation): Observable<EntityResponseType> {
        return this.http.post<IGOtherVoucherDetailExpenseAllocation>(this.resourceUrl, gOtherVoucherDetailExpenseAllocation, {
            observe: 'response'
        });
    }

    update(gOtherVoucherDetailExpenseAllocation: IGOtherVoucherDetailExpenseAllocation): Observable<EntityResponseType> {
        return this.http.put<IGOtherVoucherDetailExpenseAllocation>(this.resourceUrl, gOtherVoucherDetailExpenseAllocation, {
            observe: 'response'
        });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IGOtherVoucherDetailExpenseAllocation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IGOtherVoucherDetailExpenseAllocation[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
