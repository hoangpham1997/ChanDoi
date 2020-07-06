import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISAOrder } from 'app/shared/model/sa-order.model';
import { IViewSAQuoteDTO } from 'app/shared/model/view-sa-quote.model';
import { IViewSAOrderDTO } from 'app/shared/model/view-sa-order.model';
import { SaReturnDetails } from 'app/shared/model/sa-return-details.model';
import { SAOrderDetails } from 'app/shared/model/sa-order-details.model';
import { IRequestSDS } from 'app/shared/model/hoa-don-dien-tu/request-sds';
import { IResponeSds } from 'app/shared/model/hoa-don-dien-tu/respone-sds';
import { RequestRecordListDtoModel } from 'app/shared/model/request-record-list-dto.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';

type EntityResponseType = HttpResponse<ISAOrder>;
type EntityArrayResponseType = HttpResponse<ISAOrder[]>;

@Injectable({ providedIn: 'root' })
export class SAOrderService {
    private resourceUrl = SERVER_API_URL + 'api/s-a-orders';
    private resourceUrlDTO = SERVER_API_URL + 'api/s-a-ordersDTO';
    private _searchVoucher: any = {};

    constructor(private http: HttpClient) {}

    create(sAOrder: ISAOrder): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sAOrder);
        return this.http
            .post<ISAOrder>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateFromServerDTO(res)));
    }

    update(sAOrder: ISAOrder): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sAOrder);
        return this.http
            .put<ISAOrder>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateFromServerDTO(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<ISAOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISAOrder[]>(this.resourceUrlDTO, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    // add  by Hautv
    deleteByListID(rq: any[]): Observable<HttpResponse<HandlingResult>> {
        return this.http.post<any>(`${this.resourceUrl}/delete-list`, rq, { observe: 'response' });
    }

    private convertDateFromClient(sAOrder: ISAOrder): ISAOrder {
        const copy: ISAOrder = Object.assign({}, sAOrder, {
            date: sAOrder.date != null && sAOrder.date.isValid() ? sAOrder.date.format(DATE_FORMAT) : null,
            deliverDate: sAOrder.deliverDate != null && sAOrder.deliverDate.isValid() ? sAOrder.deliverDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.deliverDate = res.body.deliverDate != null ? moment(res.body.deliverDate) : null;
        return res;
    }

    private convertDateFromServerDTO(res: any): EntityResponseType {
        res.body.sAOrder.date = res.body.sAOrder.date != null ? moment(res.body.sAOrder.date) : null;
        res.body.sAOrder.deliverDate = res.body.sAOrder.deliverDate != null ? moment(res.body.sAOrder.deliverDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((sAOrder: ISAOrder) => {
            sAOrder.date = sAOrder.date != null ? moment(sAOrder.date) : null;
            sAOrder.deliverDate = sAOrder.deliverDate != null ? moment(sAOrder.deliverDate) : null;
        });
        return res;
    }

    getViewSAOrderDTO(req?: any): Observable<HttpResponse<IViewSAOrderDTO[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<IViewSAOrderDTO[]>(`${this.resourceUrl}/ViewSAOrderDTO`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: HttpResponse<IViewSAOrderDTO[]>) => this.convertDateArrayFromServer_View(res)));
    }

    private convertDateArrayFromServer_View(res: HttpResponse<IViewSAOrderDTO[]>): HttpResponse<IViewSAOrderDTO[]> {
        res.body.forEach((sAOrder: IViewSAOrderDTO) => {
            sAOrder.date = sAOrder.date != null ? moment(sAOrder.date) : null;
        });
        return res;
    }

    export(): Observable<HttpResponse<any>> {
        if (this._searchVoucher) {
            this._searchVoucher.page = this._searchVoucher.page ? this._searchVoucher.page - 1 : 0;
        } else {
            this._searchVoucher = { page: 0 };
        }
        this._searchVoucher.fromInvoiceDate = this._searchVoucher.fromInvoiceDate ? this._searchVoucher.fromInvoiceDate : '';
        this._searchVoucher.toInvoiceDate = this._searchVoucher.toInvoiceDate ? this._searchVoucher.toInvoiceDate : '';
        const options = createRequestOption(this._searchVoucher);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceUrl + '/export/pdf', {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    findAllDetailsById(req: any): Observable<HttpResponse<SAOrderDetails[]>> {
        const options = createRequestOption(req);
        return this.http.get<SAOrderDetails[]>(`${this.resourceUrl}/details`, { params: options, observe: 'response' });
    }

    checkRelateVoucher(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/check-relate-voucher`, { params: options, observe: 'response' });
    }

    deleteRef(id: string) {
        return this.http
            .post<any>(this.resourceUrl + '/delete-ref/' + id, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServerForMultiAction(res)));
    }

    private convertDateArrayFromServerForMultiAction(res: any): any {
        res.body.listFail.forEach((xuLyChungTuModel: ViewVoucherNo) => {
            xuLyChungTuModel.date = xuLyChungTuModel.date != null ? moment(xuLyChungTuModel.date) : null;
            xuLyChungTuModel.postedDate = xuLyChungTuModel.postedDate != null ? moment(xuLyChungTuModel.postedDate) : null;
        });
        return res;
    }
}
