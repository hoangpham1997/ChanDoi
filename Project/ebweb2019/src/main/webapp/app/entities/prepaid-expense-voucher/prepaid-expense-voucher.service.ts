import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPrepaidExpenseVoucher } from 'app/shared/model/prepaid-expense-voucher.model';

type EntityResponseType = HttpResponse<IPrepaidExpenseVoucher>;
type EntityArrayResponseType = HttpResponse<IPrepaidExpenseVoucher[]>;

@Injectable({ providedIn: 'root' })
export class PrepaidExpenseVoucherService {
    private resourceUrl = SERVER_API_URL + 'api/prepaid-expense-vouchers';

    constructor(private http: HttpClient) {}

    create(prepaidExpenseVoucher: IPrepaidExpenseVoucher): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(prepaidExpenseVoucher);
        return this.http
            .post<IPrepaidExpenseVoucher>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(prepaidExpenseVoucher: IPrepaidExpenseVoucher): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(prepaidExpenseVoucher);
        return this.http
            .put<IPrepaidExpenseVoucher>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPrepaidExpenseVoucher>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPrepaidExpenseVoucher[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(prepaidExpenseVoucher: IPrepaidExpenseVoucher): IPrepaidExpenseVoucher {
        const copy: IPrepaidExpenseVoucher = Object.assign({}, prepaidExpenseVoucher, {
            date:
                prepaidExpenseVoucher.date != null && prepaidExpenseVoucher.date.isValid()
                    ? prepaidExpenseVoucher.date.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((prepaidExpenseVoucher: IPrepaidExpenseVoucher) => {
            prepaidExpenseVoucher.date = prepaidExpenseVoucher.date != null ? moment(prepaidExpenseVoucher.date) : null;
        });
        return res;
    }
}
