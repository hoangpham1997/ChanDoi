import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IConnectEInvoice } from 'app/shared/model/hoa-don-dien-tu/connect';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { CloseBook } from 'app/tonghop/khoa-so-ky-ke-toan/close-book-request.model';

type EntityResponseType = HttpResponse<ViewVoucherNo>;
type EntityArrayResponseType = HttpResponse<ViewVoucherNo[]>;

@Injectable({ providedIn: 'root' })
export class XuLyChungTuService {
    private resourceUrl = SERVER_API_URL + 'api/view-voucher-no';

    constructor(private http: HttpClient) {}

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<ViewVoucherNo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ViewVoucherNo[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    connect(connectEInvoice: IConnectEInvoice): Observable<any> {
        return this.http.post<IConnectEInvoice>(this.resourceUrl + '/connect', connectEInvoice, { observe: 'response' });
    }

    getAllVoucherNotRecorded(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ViewVoucherNo[]>(this.resourceUrl + '/get-all-voucher-not-recorded', {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    closeBook(closeBook: CloseBook): Observable<any> {
        const copy = this.convertDateFromClient(closeBook);
        return this.http.post<any>(this.resourceUrl + '/close-book', copy, { observe: 'response' });
    }

    // Tìm kiếm hóa đơn
    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<object[]>(SERVER_API_URL + 'api/e-invoices/search', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(closeBook: CloseBook): CloseBook {
        if (closeBook.listChangePostedDateDiff) {
            const copyChangePostedDateDiffs = [];
            closeBook.listChangePostedDateDiff.forEach(n => {
                const copyChangePostedDateDiff = Object.assign({}, n, {
                    date: n.date != null && n.date.isValid() ? n.date.format(DATE_FORMAT) : null,
                    postedDate: n.postedDate != null && n.postedDate.isValid() ? n.postedDate.format(DATE_FORMAT) : null,
                    postedDateChange:
                        n.postedDateChange != null && n.postedDateChange.isValid() ? n.postedDateChange.format(DATE_FORMAT) : null
                });
                copyChangePostedDateDiffs.push(copyChangePostedDateDiff);
            });
            closeBook.listChangePostedDateDiff = copyChangePostedDateDiffs;
        }
        if (closeBook.listDataChangeDiff) {
            const copylistDataChangeDiffs = [];
            closeBook.listDataChangeDiff.forEach(n => {
                const copylistDataChangeDiff = Object.assign({}, n, {
                    date: n.date != null && n.date.isValid() ? n.date.format(DATE_FORMAT) : null,
                    postedDate: n.postedDate != null && n.postedDate.isValid() ? n.postedDate.format(DATE_FORMAT) : null,
                    postedDateChange:
                        n.postedDateChange != null && n.postedDateChange.isValid() ? n.postedDateChange.format(DATE_FORMAT) : null
                });
                copylistDataChangeDiffs.push(copylistDataChangeDiff);
            });
            closeBook.listDataChangeDiff = copylistDataChangeDiffs;
        }
        return closeBook;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((xuLyChungTuModel: ViewVoucherNo) => {
            xuLyChungTuModel.date = xuLyChungTuModel.date != null ? moment(xuLyChungTuModel.date) : null;
            xuLyChungTuModel.postedDate = xuLyChungTuModel.postedDate != null ? moment(xuLyChungTuModel.postedDate) : null;
        });
        return res;
    }
}
