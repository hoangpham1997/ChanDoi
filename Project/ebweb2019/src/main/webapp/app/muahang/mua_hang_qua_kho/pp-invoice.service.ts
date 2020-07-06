import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPPInvoice } from 'app/shared/model/pp-invoice.model';
import { IPPDiscountReturn, PPDiscountReturn } from 'app/shared/model/pp-discount-return.model';
import { IPPInvoiceDetails } from 'app/shared/model/pp-invoice-details.model';
import { Pporder } from 'app/shared/model/pporder.model';
import { IRefVoucher } from 'app/shared/model/ref-voucher.model';
import { IPPInvoiceDetailCost } from 'app/shared/model/pp-invoice-detail-cost.model';
import { IViewSAOrderDTO } from 'app/shared/model/view-sa-order.model';
import { IPPInvoiceDTO } from 'app/shared/modal/pp-invoice/pp-invoice-dto.model';
import { ISaReturn } from 'app/shared/model/sa-return.model';

type EntityResponseType = HttpResponse<IPPInvoice>;
type EntityResponseTypeAny = HttpResponse<any>;
type EntityArrayResponseType = HttpResponse<IPPInvoice[]>;
type EntityArrayResponseDetailType = HttpResponse<IPPInvoiceDetails[]>;

@Injectable({ providedIn: 'root' })
export class PPInvoiceService {
    private resourceUrl = SERVER_API_URL + 'api/pp-invoices';
    private searchSnapShot: any;

    constructor(private http: HttpClient) {}

    setSearchSnapShot(searchSnapShotEnt) {
        this.searchSnapShot = searchSnapShotEnt;
    }

    getSearchSnapShot() {
        return this.searchSnapShot ? this.searchSnapShot : {};
    }

    create(pPInvoice: IPPInvoice): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(pPInvoice);
        return this.http
            .post<IPPInvoice>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    createPPInvoice(type: '/rsi-save' | '/save', ref?: any): Observable<any> {
        const copy = this.convertDateFromClient(ref);
        return this.http
            .post<IPPInvoice>(this.resourceUrl + type, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(pPInvoice: IPPInvoice): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(pPInvoice);
        return this.http
            .put<IPPInvoice>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPPInvoice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPPInvoice[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(pPInvoice: IPPInvoice): IPPInvoice {
        const copy: IPPInvoice = Object.assign({}, pPInvoice, {
            date: pPInvoice.date != null && pPInvoice.date.isValid() ? pPInvoice.date.format(DATE_FORMAT) : null,
            postedDate: pPInvoice.postedDate != null && pPInvoice.postedDate.isValid() ? pPInvoice.postedDate.format(DATE_FORMAT) : null,
            dueDate: pPInvoice.dueDate != null && pPInvoice.dueDate.isValid() ? pPInvoice.dueDate.format(DATE_FORMAT) : null,
            matchDate: pPInvoice.matchDate != null && pPInvoice.matchDate.isValid() ? pPInvoice.matchDate.format(DATE_FORMAT) : null,
            issueDate: pPInvoice.issueDate != null && pPInvoice.issueDate.isValid() ? pPInvoice.issueDate.format(DATE_FORMAT) : null
        });

        if (copy.ppInvoiceDetails && copy.ppInvoiceDetails.length > 0) {
            for (let i = 0; i < copy.ppInvoiceDetails.length; i++) {
                copy.ppInvoiceDetails[i] = Object.assign({}, copy.ppInvoiceDetails[i], {
                    expiryDate:
                        copy.ppInvoiceDetails[i].expiryDate != null && copy.ppInvoiceDetails[i].expiryDate.isValid()
                            ? copy.ppInvoiceDetails[i].expiryDate.format(DATE_FORMAT)
                            : null,
                    invoiceDate:
                        copy.ppInvoiceDetails[i].invoiceDate != null && copy.ppInvoiceDetails[i].invoiceDate.isValid()
                            ? copy.ppInvoiceDetails[i].invoiceDate.format(DATE_FORMAT)
                            : null
                });
            }
        }

        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        res.body.dueDate = res.body.dueDate != null ? moment(res.body.dueDate) : null;
        res.body.matchDate = res.body.matchDate != null ? moment(res.body.matchDate) : null;
        res.body.issueDate = res.body.issueDate != null ? moment(res.body.issueDate) : null;
        if (res.body.ppInvoiceDetails && res.body.ppInvoiceDetails.length > 0) {
            for (let i = 0; i < res.body.ppInvoiceDetails.length; i++) {
                res.body.ppInvoiceDetails[i].expiryDate =
                    res.body.ppInvoiceDetails[i].expiryDate != null ? moment(res.body.ppInvoiceDetails[i].expiryDate) : null;
                res.body.ppInvoiceDetails[i].invoiceDate =
                    res.body.ppInvoiceDetails[i].invoiceDate != null ? moment(res.body.ppInvoiceDetails[i].invoiceDate) : null;
            }
        }
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((pPInvoice: IPPInvoice) => {
            pPInvoice.date = pPInvoice.date != null ? moment(pPInvoice.date) : null;
            pPInvoice.postedDate = pPInvoice.postedDate != null ? moment(pPInvoice.postedDate) : null;
            pPInvoice.dueDate = pPInvoice.dueDate != null ? moment(pPInvoice.dueDate) : null;
            pPInvoice.matchDate = pPInvoice.matchDate != null ? moment(pPInvoice.matchDate) : null;
            pPInvoice.issueDate = pPInvoice.issueDate != null ? moment(pPInvoice.issueDate) : null;
        });
        return res;
    }

    searchPPInvoice(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<PPDiscountReturn[]>(SERVER_API_URL + 'api/pp-invoices/search', {
            params: options,
            observe: 'response'
        });
    }

    findById(req: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http
            .get<any>(SERVER_API_URL + 'api/pp-invoices/get-by-id', { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    // không lấy detail
    findPPInvoiceById(req: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http
            .get<any>(SERVER_API_URL + 'api/pp-invoices/get-pp-invoice-by-id', { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findDetailById(req: any): Observable<EntityArrayResponseDetailType> {
        const options = createRequestOption(req);
        return this.http.get<any>(SERVER_API_URL + 'api/pp-invoices/get-detail-by-ppinvoice-id', {
            params: options,
            observe: 'response'
        });
    }

    findDetailByPaymentVoucherID(req: any): Observable<EntityArrayResponseDetailType> {
        const options = createRequestOption(req);
        return this.http.get<any>(SERVER_API_URL + 'api/pp-invoices/get-detail-by-paymentVoucher-id', {
            params: options,
            observe: 'response'
        });
    }

    findPPInvoiceByPaymentVoucherId(req: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http
            .get<any>(SERVER_API_URL + 'api/pp-invoices/findPPInvoiceByPaymentVoucherId', {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: any) => this.convertDateFromServer(res)));
    }

    private convertDateArrayDetailFromServer(res: EntityArrayResponseDetailType): EntityArrayResponseDetailType {
        res.body.forEach((pPInvoiceDetails: IPPInvoiceDetails) => {
            pPInvoiceDetails.expiryDate = pPInvoiceDetails.expiryDate != null ? moment(pPInvoiceDetails.expiryDate) : null;
            pPInvoiceDetails.invoiceDate = pPInvoiceDetails.invoiceDate != null ? moment(pPInvoiceDetails.invoiceDate) : null;
        });
        return res;
    }

    findByRowNum(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPPInvoice>(`${this.resourceUrl}/index`, { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findRefVoucherByRefId(req: any): Observable<HttpResponse<IRefVoucher[]>> {
        return this.http.get<IRefVoucher[]>(SERVER_API_URL + 'api/view-vouchers/find-by-ref-id-ppinvoice', {
            params: req,
            observe: 'response'
        });
    }

    getPPInvoiceDetailCost(req: any): Observable<HttpResponse<IPPInvoiceDetailCost[]>> {
        return this.http.get<IPPInvoiceDetailCost[]>(SERVER_API_URL + 'api/pp-invoice-detail-cost/get-by-ppinvoiceid', {
            params: req,
            observe: 'response'
        });
    }

    getPPInvoiceDetailCostByPaymentVoucherID(req: any): Observable<HttpResponse<IPPInvoiceDetailCost[]>> {
        return this.http.get<IPPInvoiceDetailCost[]>(SERVER_API_URL + 'api/pp-invoice-detail-cost/get-by-paymentvoucherid', {
            params: req,
            observe: 'response'
        });
    }

    deleteById(req: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<any>(SERVER_API_URL + 'api/pp-invoices/delete-by-id', {
            params: options,
            observe: 'response'
        });
    }

    getPPOrderNo(req: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<any>(SERVER_API_URL + 'api/pp-invoice-detail/get-pporderno-by-id', {
            params: options,
            observe: 'response'
        });
    }

    export(type: 'excel' | 'pdf', req?: any): Observable<HttpResponse<any>> {
        req.fromDate = req.fromDate && req.fromDate.isValid() ? req.fromDate.format(DATE_FORMAT) : '';
        req.toDate = req.toDate && req.toDate.isValid() ? req.toDate.format(DATE_FORMAT) : '';
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(`${this.resourceUrl}/export/${type}`, { params: options, observe: 'response', headers, responseType: 'blob' });
    }

    getViewPPInvoiceDTO(req?: any): Observable<HttpResponse<IPPInvoiceDTO[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<IPPInvoiceDTO[]>(`${this.resourceUrl}/ViewPPInvoiceDTO`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: HttpResponse<IPPInvoiceDTO[]>) => this.convertDateArrayFromServer_View(res)));
    }

    private convertDateArrayFromServer_View(res: HttpResponse<IPPInvoiceDTO[]>): HttpResponse<IPPInvoiceDTO[]> {
        res.body.forEach((sAOrder: IPPInvoiceDTO) => {
            sAOrder.date = sAOrder.date != null ? moment(sAOrder.date) : null;
        });
        return res;
    }

    checkUnRecord(req: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<any>(SERVER_API_URL + 'api/pp-invoice/check-un-record', {
            params: options,
            observe: 'response'
        });
    }

    checkPayVendor(req: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<any>(SERVER_API_URL + 'api/pp-invoice/check-play-vendor', {
            params: options,
            observe: 'response'
        });
    }

    getCustomerReport(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/pdf');
        return this.http.get(SERVER_API_URL + 'api/report/pdf', {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    multiDelete(obj: any): Observable<HttpResponse<any>> {
        return this.http.post<any>(SERVER_API_URL + 'api/pp-invoices/multi-delete', obj, { observe: 'response' });
    }

    multiUnrecord(obj: any): Observable<HttpResponse<any>> {
        return this.http.post<any>(SERVER_API_URL + 'api/pp-invoices/multi-unrecord', obj, { observe: 'response' });
    }
}
