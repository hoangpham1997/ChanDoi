import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { catchError, map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IEInvoice } from 'app/shared/model/hoa-don-dien-tu/e-invoice.model';
import { IMCPayment } from 'app/shared/model/mc-payment.model';
import { IResponeSds } from 'app/shared/model/hoa-don-dien-tu/respone-sds';
import { ISupplier } from 'app/shared/model/hoa-don-dien-tu/supplier';
import { IConnectEInvoice } from 'app/shared/model/hoa-don-dien-tu/connect';
import { ISAOrder } from 'app/shared/model/sa-order.model';
import { IRequestSDS } from 'app/shared/model/hoa-don-dien-tu/request-sds';

type EntityResponseType = HttpResponse<IEInvoice>;
type EntityArrayResponseType = HttpResponse<IEInvoice[]>;

@Injectable({ providedIn: 'root' })
export class EInvoiceService {
    private resourceUrl = SERVER_API_URL + 'api/e-invoices';

    constructor(private http: HttpClient) {}

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IEInvoice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IEInvoice[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getEInvoiceWaitSign(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IEInvoice[]>(this.resourceUrl + '/wait-sign', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getEInvoiceCanceled(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IEInvoice[]>(this.resourceUrl + '/canceled', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getEInvoiceForConvert(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IEInvoice[]>(this.resourceUrl + '/convert', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getEInvoiceAdjusted(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IEInvoice[]>(this.resourceUrl + '/adjusted', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getEInvoiceReplaced(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IEInvoice[]>(this.resourceUrl + '/replaced', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getSuppliers(): Observable<HttpResponse<ISupplier[]>> {
        return this.http.get<ISupplier[]>(this.resourceUrl + '/suppliers', { observe: 'response' });
    }

    getConnect(): Observable<HttpResponse<IConnectEInvoice>> {
        return this.http.get<IConnectEInvoice>(this.resourceUrl + '/connect', { observe: 'response' });
    }

    connect(connectEInvoice: IConnectEInvoice): Observable<any> {
        return this.http.post<IConnectEInvoice>(this.resourceUrl + '/connect', connectEInvoice, { observe: 'response' });
    }

    // Phát hành hóa đơn
    publishInvoice(requestEInvoice) {
        return this.http.post<IResponeSds>(this.resourceUrl + '/publish-invoice', requestEInvoice, { observe: 'response' });
    }

    // Tạo hóa đơn chờ ký
    createInvoiceWaitSign(requestEInvoice) {
        return this.http.post<IResponeSds>(this.resourceUrl + '/create-invoice-wait-sign', requestEInvoice, { observe: 'response' });
    }

    // Hủy hóa đơn
    cancelInvoice(requestEInvoice) {
        return this.http.post<IResponeSds>(this.resourceUrl + '/cancel', requestEInvoice, { observe: 'response' });
    }

    // Chuyển đổi chứng minh nguồn gốc
    convertedOrigin(requestEInvoice) {
        return this.http.post<IResponeSds>(this.resourceUrl + '/converted-origin', requestEInvoice, { observe: 'response' });
    }

    // Gửi hóa đơn cho khách hàng
    sendMail(requestEInvoice) {
        return this.http.post<IResponeSds>(this.resourceUrl + '/send-mail', requestEInvoice, { observe: 'response' });
    }

    // Chuyển đổi lưu trữ
    convertedStorage(requestEInvoice): Observable<HttpResponse<any>> {
        const options = createRequestOption(requestEInvoice);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/pdf');
        return this.http.get(this.resourceUrl + '/converted-storage', {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    getDigestData(req: IRequestSDS) {
        return this.http.post<IResponeSds>(this.resourceUrl + '/get-digest-data', req, { observe: 'response' });
    }

    publishInvoiceWithCert(req: IRequestSDS) {
        return this.http.post<IResponeSds>(this.resourceUrl + '/sign-with-digest-data', req, { observe: 'response' });
    }

    // Tìm kiếm hóa đơn
    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<object[]>(SERVER_API_URL + 'api/e-invoices/search', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getInformationVoucherByID(req) {
        const options = createRequestOption(req);
        return this.http.get<any>(this.resourceUrl + '/get-information-voucher-by-id', { params: options, observe: 'response' });
    }

    viewInvoicePdf(requestEInvoice): Observable<HttpResponse<any>> {
        const options = createRequestOption(requestEInvoice);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/pdf');
        return this.http
            .get(this.resourceUrl + '/get-e-invoice-view', {
                params: options,
                observe: 'response',
                headers,
                responseType: 'blob'
            })
            .pipe(catchError(this.parseErrorBlob));
    }

    parseErrorBlob(err: HttpErrorResponse): Observable<any> {
        const reader: FileReader = new FileReader();

        const obs = Observable.create((observer: any) => {
            reader.onloadend = () => {
                observer.error(JSON.parse(reader.result));
                observer.complete();
            };
        });
        reader.readAsText(err.error);
        return obs;
    }

    viewInvoiceConvertedOrigin(requestEInvoice): Observable<HttpResponse<any>> {
        const options = createRequestOption(requestEInvoice);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/pdf');
        return this.http.get(this.resourceUrl + '/get-view-converted-origin', {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    private convertDateFromClient(sAOrder: IEInvoice): IEInvoice {
        const copy: IEInvoice = Object.assign({}, sAOrder, {
            invoiceDate: sAOrder.invoiceDate != null && sAOrder.invoiceDate.isValid() ? sAOrder.invoiceDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.invoiceDate = res.body.invoiceDate != null ? moment(res.body.invoiceDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((eIncoie: IEInvoice) => {
            eIncoie.invoiceDate = eIncoie.invoiceDate != null ? moment(eIncoie.invoiceDate) : null;
        });
        return res;
    }

    // MIV
    // load dữ liệu MIV
    loadDataMIV(requestEInvoice) {
        return this.http.post<IResponeSds>(this.resourceUrl + '/load-data-miv', requestEInvoice, { observe: 'response' });
    }

    // MIV
    // load dữ liệu MIV
    loadDataTokenMIV(requestEInvoice) {
        return this.http.post<IResponeSds>(this.resourceUrl + '/load-token-miv', requestEInvoice, { observe: 'response' });
    }
}
