import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT, DATE_TIME_SECOND_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISAInvoice } from 'app/shared/model/sa-invoice.model';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import { ISaBill } from 'app/shared/model/sa-bill.model';

type EntityResponseType = HttpResponse<ISAInvoice>;
type EntityArrayResponseType = HttpResponse<ISAInvoice[]>;

@Injectable({ providedIn: 'root' })
export class SAInvoiceService {
    private resourceUrl = SERVER_API_URL + 'api/sa-invoices';

    constructor(private http: HttpClient) {}

    create(sAInvoice: ISAInvoice): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sAInvoice);
        if (copy.saBill) {
            copy.saBill = this.convertDateFromClientSABill(copy.saBill);
        }
        if (copy.rsInwardOutward) {
            copy.rsInwardOutward = this.convertDateFromClientObject(copy.rsInwardOutward);
        }
        if (copy.mcReceipt) {
            copy.mcReceipt = this.convertDateFromClientObject(copy.mcReceipt);
        }
        if (copy.mbDeposit) {
            copy.mbDeposit = this.convertDateFromClientObject(copy.mbDeposit);
        }
        return this.http
            .post<ISAInvoice>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(sAInvoice: ISAInvoice): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sAInvoice);
        if (copy.saBill) {
            copy.saBill = this.convertDateFromClientSABill(copy.saBill);
        }
        if (copy.rsInwardOutward) {
            copy.rsInwardOutward = this.convertDateFromClientObject(copy.rsInwardOutward);
        }
        if (copy.mcReceipt) {
            copy.mcReceipt = this.convertDateFromClientObject(copy.mcReceipt);
        }
        if (copy.mbDeposit) {
            copy.mbDeposit = this.convertDateFromClientObject(copy.mbDeposit);
        }
        return this.http
            .put<ISAInvoice>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: any): Observable<EntityResponseType> {
        return this.http
            .get<ISAInvoice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISAInvoice[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: any): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClientSABill(saBill: ISaBill): ISaBill {
        const copy: ISaBill = Object.assign({}, saBill, {
            invoiceDate: saBill.invoiceDate != null && saBill.invoiceDate.isValid() ? saBill.invoiceDate.format(DATE_TIME_FORMAT) : null,
            refDateTime:
                saBill.refDateTime != null && saBill.refDateTime.isValid() ? saBill.refDateTime.format(DATE_TIME_SECOND_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromClientObject(object: any): any {
        const copy: any = Object.assign({}, object, {
            date: object.date != null && object.date.isValid() ? object.date.format(DATE_FORMAT) : null,
            postedDate: object.postedDate != null && object.postedDate.isValid() ? object.postedDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromClient(sAInvoice: ISAInvoice): ISAInvoice {
        const copy: ISAInvoice = Object.assign({}, sAInvoice, {
            date: sAInvoice.date != null && sAInvoice.date.isValid() ? sAInvoice.date.format(DATE_FORMAT) : null,
            postedDate: sAInvoice.postedDate != null && sAInvoice.postedDate.isValid() ? sAInvoice.postedDate.format(DATE_FORMAT) : null,
            invoiceDate:
                sAInvoice.invoiceDate != null && sAInvoice.invoiceDate.isValid() ? sAInvoice.invoiceDate.format(DATE_FORMAT) : null,
            DueDate: sAInvoice.dueDate != null && sAInvoice.dueDate.isValid() ? sAInvoice.dueDate.format(DATE_FORMAT) : null,
            listDate: sAInvoice.listDate != null && sAInvoice.listDate.isValid() ? sAInvoice.listDate.format(DATE_FORMAT) : null,
            dateSendMail:
                sAInvoice.dateSendMail != null && sAInvoice.dateSendMail.isValid() ? sAInvoice.dateSendMail.format(DATE_FORMAT) : null,
            matchDate: sAInvoice.matchDate != null && sAInvoice.matchDate.isValid() ? sAInvoice.matchDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        res.body.invoiceDate = res.body.invoiceDate != null ? moment(res.body.invoiceDate) : null;
        res.body.dueDate = res.body.dueDate != null ? moment(res.body.dueDate) : null;
        res.body.listDate = res.body.listDate != null ? moment(res.body.listDate) : null;
        res.body.dateSendMail = res.body.dateSendMail != null ? moment(res.body.dateSendMail) : null;
        res.body.matchDate = res.body.matchDate != null ? moment(res.body.matchDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((sAInvoice: ISAInvoice) => {
            sAInvoice.date = sAInvoice.date != null ? moment(sAInvoice.date) : null;
            sAInvoice.postedDate = sAInvoice.postedDate != null ? moment(sAInvoice.postedDate) : null;
            sAInvoice.invoiceDate = sAInvoice.invoiceDate != null ? moment(sAInvoice.invoiceDate) : null;
            sAInvoice.dueDate = sAInvoice.dueDate != null ? moment(sAInvoice.dueDate) : null;
            sAInvoice.listDate = sAInvoice.listDate != null ? moment(sAInvoice.listDate) : null;
            sAInvoice.dateSendMail = sAInvoice.dateSendMail != null ? moment(sAInvoice.dateSendMail) : null;
            sAInvoice.matchDate = sAInvoice.matchDate != null ? moment(sAInvoice.matchDate) : null;
        });
        return res;
    }

    findByRowNum(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISAInvoice>(`${this.resourceUrl}/index`, { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    searchSAInvoice(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISAInvoice[]>(SERVER_API_URL + 'api/sa-invoice-objects-search', {
            params: options,
            observe: 'response'
        });
    }

    searchSAInvoicePopup(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISAInvoice[]>(SERVER_API_URL + 'api/sa-invoices/popup', {
            params: options,
            observe: 'response'
        });
    }

    getRefVouchersBySAInvoiceID(id: string): Observable<HttpResponse<any[]>> {
        return this.http.get<any[]>(`${this.resourceUrl}/ref-voucher/${id}`, { observe: 'response' });
    }

    // Add by Hautv
    getRefVouchersByMCReceiptID(id: string): Observable<EntityResponseType> {
        return this.http.get<any>(`${this.resourceUrl}/ref-voucher-by-mcreceipt-id/${id}`, { observe: 'response' });
    }

    // Add by namnh
    getRefVouchersByMBDepositID(id: string): Observable<EntityResponseType> {
        return this.http.get<any>(`${this.resourceUrl}/ref-voucher-by-mb-deposit-id/${id}`, { observe: 'response' });
    }

    getSaInvoiceDetail(req?: any) {
        return this.http.post<any>(`${this.resourceUrl}/detail`, req, { observe: 'response' });
    }

    export(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceUrl + '/export/pdf', { params: options, observe: 'response', headers, responseType: 'blob' });
    }

    exportExcel(req?: any): Observable<any> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceUrl + '/export/excel', { params: options, observe: 'response', headers, responseType: 'blob' });
    }

    private convertDateArrayFromServer2(res) {
        res.body.forEach(sAInvoiceDetail => {
            sAInvoiceDetail.expiryDate = sAInvoiceDetail.expiryDate != null ? moment(sAInvoiceDetail.expiryDate) : null;
        });
        return res;
    }

    checkRelateVoucher(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/check-relate-voucher`, { params: options, observe: 'response' });
    }

    getSAInvoiceBySABillID(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(`${this.resourceUrl}/get-sainvoiceid`, { params: options, observe: 'response' });
    }

    multiDelete(obj: ISAInvoice[]): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceUrl}/multi-delete-sa-invoices`, obj, { observe: 'response' });
    }

    multiUnrecord(obj: ISAInvoice[]): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceUrl}/multi-unrecord-sa-invoices`, obj, { observe: 'response' });
    }

    isBanHangChuaThuTien(req?: any): Observable<HttpResponse<boolean>> {
        const options = createRequestOption(req);
        return this.http.get<boolean>(`${this.resourceUrl}/isBanHangChuaThuTien`, { params: options, observe: 'response' });
    }
}
