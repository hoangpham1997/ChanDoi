import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';
import { IConnectEInvoice } from 'app/shared/model/hoa-don-dien-tu/connect';
import { RequestResetNoDtoModel } from 'app/tien-ich/danh-lai-so-chung-tu/reques-reset-no-dto.model';

type EntityResponseType = HttpResponse<IViewVoucher>;
type EntityArrayResponseType = HttpResponse<IViewVoucher[]>;

@Injectable({ providedIn: 'root' })
export class ViewVoucherService {
    private resourceUrl = SERVER_API_URL + 'api/view-vouchers';
    private resourceUrlUtilities = SERVER_API_URL + 'api/utilities';

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

    getViewVoucherToModal(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/get-view-voucher-to-modal', { params: options, observe: 'response' });
    }

    checkViaStockPPInvoice(req: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<any>(SERVER_API_URL + 'api/pp-invoice/check-via-stock', {
            params: options,
            observe: 'response'
        });
    }

    getQuantityExistsTest(req?: any): Observable<HttpResponse<any[]>> {
        // const options = createRequestOption(req);
        return this.http.post<any[]>(`api/material-goods/get-all-by-quantity-exists`, req, { observe: 'response' });
    }
    getMaterialGoodAndRepository(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.post<any>(`api/material-goods/material-good-and-repository`, req, { observe: 'response' });
    }

    getVoucherByTypeGroup(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http
            .get<any[]>(this.resourceUrlUtilities + '/reset-no/get-vouchers', { params: options, observe: 'response' })
            .pipe(map((res: any) => this.convertDateFromServer(res)));
    }

    searchVoucher(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http
            .get<any[]>(this.resourceUrlUtilities + '/search-voucher', { params: options, observe: 'response' })
            .pipe(map((res: any) => this.convertDateFromServer(res)));
    }

    resetNo(requesResetNoDtoModel: RequestResetNoDtoModel): Observable<any> {
        return this.http.post<IConnectEInvoice>(this.resourceUrlUtilities + '/reset-no', requesResetNoDtoModel, { observe: 'response' });
    }
}
