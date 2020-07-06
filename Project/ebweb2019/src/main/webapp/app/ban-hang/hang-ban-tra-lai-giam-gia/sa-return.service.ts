import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT, DATE_TIME_SECOND_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISaReturn } from 'app/shared/model/sa-return.model';
import { Irecord } from 'app/shared/model/record';
import { ISAInvoice } from 'app/shared/model/sa-invoice.model';

type EntityResponseType = HttpResponse<ISaReturn>;
type EntityArrayResponseType = HttpResponse<ISaReturn[]>;

@Injectable({ providedIn: 'root' })
export class SaReturnService {
    private resourceUrl = SERVER_API_URL + 'api/sa-returns';

    private _searchVoucher: any = {};

    get searchVoucher(): any {
        return this._searchVoucher;
    }

    set searchVoucher(value: any) {
        this._searchVoucher = value;
    }

    get moveVoucher(): any {
        return this._searchVoucher.rowIndex;
    }

    set moveVoucher(value: any) {
        this._searchVoucher.rowIndex += value;
    }

    get total(): any {
        return this._searchVoucher.total;
    }

    set total(value: any) {
        this._searchVoucher.total = value;
    }

    constructor(private http: HttpClient) {}

    create(data: any): Observable<any> {
        const copy = this.convertDateFromClient(data.saReturn);
        data.saReturn = copy;
        return this.http
            .post<any>(this.resourceUrl, data, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateFromServer(res)));
    }

    update(data: any): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(data.saReturn);
        data.saReturn = copy;
        return this.http
            .put<ISaReturn>(this.resourceUrl, data, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http
            .get<any>(`${this.resourceUrl}/index`, { params: options, observe: 'response' })
            .pipe(map((res: any) => this.convertDateFromServer(res)));
    }

    findById(id: any): Observable<HttpResponse<any>> {
        return this.http
            .get<any>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISaReturn[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: any): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(saReturn: ISaReturn): ISaReturn {
        for (let i = 0; i < saReturn.saReturnDetails.length; i++) {
            // @ts-ignore
            saReturn.saReturnDetails[i].expiryDate =
                saReturn.saReturnDetails[i].expiryDate &&
                saReturn.saReturnDetails[i].expiryDate instanceof moment &&
                saReturn.saReturnDetails[i].expiryDate.isValid()
                    ? saReturn.saReturnDetails[i].expiryDate.format(DATE_FORMAT)
                    : saReturn.saReturnDetails[i].expiryDate;
        }
        const copy: ISaReturn = Object.assign({}, saReturn, {
            invoiceDate:
                saReturn.invoiceDate && saReturn.invoiceDate.isValid() ? saReturn.invoiceDate.format(DATE_TIME_SECOND_FORMAT) : null,
            date: saReturn.date != null && saReturn.date.isValid() ? saReturn.date.format(DATE_TIME_FORMAT) : null,
            dueDate: saReturn.dueDate != null && saReturn.dueDate.isValid() ? saReturn.dueDate.format(DATE_TIME_FORMAT) : null,
            postedDate: saReturn.postedDate != null && saReturn.postedDate.isValid() ? saReturn.postedDate.format(DATE_TIME_FORMAT) : null,
            listDate: saReturn.listDate != null && saReturn.listDate.isValid() ? saReturn.listDate.format(DATE_TIME_FORMAT) : null,
            dateSendMail:
                saReturn.dateSendMail != null && saReturn.dateSendMail.isValid() ? saReturn.dateSendMail.format(DATE_TIME_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: any): any {
        this.searchVoucher.id = null;
        if (res.body && res.body.saReturn) {
            res.body.saReturn.date = res.body.saReturn.date != null ? moment(res.body.saReturn.date) : null;
            res.body.saReturn.postedDate = res.body.saReturn.postedDate != null ? moment(res.body.saReturn.postedDate) : null;
            res.body.saReturn.dateSendMail = res.body.saReturn.dateSendMail != null ? moment(res.body.saReturn.dateSendMail) : null;
            res.body.saReturn.invoiceDate = res.body.saReturn.invoiceDate != null ? moment(res.body.saReturn.invoiceDate) : null;
            res.body.saReturn.dueDate = res.body.saReturn.dueDate != null ? moment(res.body.saReturn.dueDate) : null;
            res.body.saReturn.listDate = res.body.saReturn.listDate != null ? moment(res.body.saReturn.listDate) : null;
            if (res.body.saReturn.saReturnDetails) {
                for (let i = 0; i < res.body.saReturn.saReturnDetails.length; i++) {
                    const date = res.body.saReturn.saReturnDetails[i].expiryDate;
                    if (date) {
                        res.body.saReturn.saReturnDetails[i].expiryDate = moment(date);
                    }
                    const date2 = res.body.saReturn.saReturnDetails[i].date;
                    if (date2) {
                        res.body.saReturn.saReturnDetails[i].date = moment(date2, 'DD/MM/YYYY');
                    }
                }
            }

            if (res.body.saReturnDetails) {
                for (let i = 0; i < res.body.saReturnDetails.length; i++) {
                    const date = res.body.saReturnDetails[i].expiryDate;
                    if (date) {
                        res.body.saReturnDetails[i].expiryDate = moment(date, 'DD/MM/YYYY');
                    }
                    const date2 = res.body.saReturnDetails[i].date;
                    if (date2) {
                        res.body.saReturnDetails[i].date = moment(date2, 'DD/MM/YYYY');
                    }
                }
            }
        }
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((saReturn: ISaReturn) => {
            saReturn.invoiceDate = saReturn.invoiceDate != null ? moment(saReturn.invoiceDate) : null;
            saReturn.date = saReturn.date != null ? moment(saReturn.date) : null;
            saReturn.postedDate = saReturn.postedDate != null ? moment(saReturn.postedDate) : null;
            saReturn.dateSendMail = saReturn.dateSendMail != null ? moment(saReturn.dateSendMail) : null;
            saReturn.dueDate = saReturn.dueDate != null ? moment(saReturn.dueDate) : null;
            saReturn.listDate = saReturn.listDate != null ? moment(saReturn.listDate) : null;
        });
        return res;
    }

    getAllSearchData() {
        return this.http.get<any>(`${this.resourceUrl}/search-data`, { observe: 'response' });
    }

    exportExcel(): Observable<any> {
        if (this._searchVoucher) {
            this._searchVoucher.page = this._searchVoucher.page ? this._searchVoucher.page - 1 : 0;
        } else {
            this._searchVoucher = { page: 0 };
        }
        this._searchVoucher.fromInvoiceDate = this._searchVoucher.fromInvoiceDate ? this._searchVoucher.fromInvoiceDate : '';
        this._searchVoucher.toInvoiceDate = this._searchVoucher.toInvoiceDate ? this._searchVoucher.toInvoiceDate : '';
        this._searchVoucher.recorded = this._searchVoucher.recorded === 1 ? true : this._searchVoucher.recorded === 0 ? false : '';
        const options = createRequestOption(this._searchVoucher);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceUrl + '/export/excel', { params: options, observe: 'response', headers, responseType: 'blob' });
    }

    export(): Observable<HttpResponse<any>> {
        if (this._searchVoucher) {
            this._searchVoucher.page = this._searchVoucher.page ? this._searchVoucher.page - 1 : 0;
        } else {
            this._searchVoucher = { page: 0 };
        }
        this._searchVoucher.fromInvoiceDate = this._searchVoucher.fromInvoiceDate ? this._searchVoucher.fromInvoiceDate : '';
        this._searchVoucher.toInvoiceDate = this._searchVoucher.toInvoiceDate ? this._searchVoucher.toInvoiceDate : '';
        this._searchVoucher.recorded = this._searchVoucher.recorded === 1 ? true : this._searchVoucher.recorded === 0 ? false : '';
        const options = createRequestOption(this._searchVoucher);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/pdf');
        return this.http.get(this.resourceUrl + '/export/pdf', { params: options, observe: 'response', headers, responseType: 'blob' });
    }

    unrecord(record) {
        return this.http.post<Irecord>(`${this.resourceUrl}/unrecord`, record, { observe: 'response' });
    }

    checkRelateVoucher(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/check-relate-voucher`, { params: options, observe: 'response' });
    }

    multiDelete(obj: ISaReturn[]): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceUrl}/multi-delete-sa-returns`, obj, { observe: 'response' });
    }

    multiUnrecord(obj: ISaReturn[]): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceUrl}/multi-unrecord-sa-returns`, obj, { observe: 'response' });
    }
}
