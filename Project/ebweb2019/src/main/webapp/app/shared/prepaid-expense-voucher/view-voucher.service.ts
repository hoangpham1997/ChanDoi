import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';

type EntityResponseType = HttpResponse<IViewVoucher>;
type EntityArrayResponseType = HttpResponse<IViewVoucher[]>;

@Injectable({ providedIn: 'root' })
export class PrepaidExpenseVoucherService {
    private resourceUrl = SERVER_API_URL + 'api/view-vouchers';

    constructor(private http: HttpClient) {}

    create(viewVoucher: IViewVoucher): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(viewVoucher);
        return this.http
            .post<IViewVoucher>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(viewVoucher: IViewVoucher): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(viewVoucher);
        return this.http
            .put<IViewVoucher>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IViewVoucher>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(viewVoucher: IViewVoucher): IViewVoucher {
        const copy: IViewVoucher = Object.assign({}, viewVoucher, {
            date: viewVoucher.date != null && viewVoucher.date.isValid() ? viewVoucher.date.format(DATE_FORMAT) : null,
            postedDate:
                viewVoucher.postedDate != null && viewVoucher.postedDate.isValid() ? viewVoucher.postedDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((viewVoucher: IViewVoucher) => {
            viewVoucher.date = viewVoucher.date != null ? moment(viewVoucher.date) : null;
            viewVoucher.postedDate = viewVoucher.postedDate != null ? moment(viewVoucher.postedDate) : null;
        });
        return res;
    }
}
