import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT, DATE_TIME_SECOND_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISaBill } from 'app/shared/model/sa-bill.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { ISaBillDetails } from 'app/shared/model/sa-bill-details.model';
import { ISaBillCreated } from 'app/shared/model/sa-bill-created';
import { IMCAudit } from 'app/shared/model/mc-audit.model';

type EntityResponseType = HttpResponse<ISaBill>;
type EntityArrayResponseType = HttpResponse<ISaBill[]>;

type EntityResponseTypeDetail = HttpResponse<ISaBillDetails>;
type EntityArrayResponseTypeDetail = HttpResponse<ISaBillDetails[]>;

@Injectable({ providedIn: 'root' })
export class SaBillService {
    private resourceUrl = SERVER_API_URL + 'api/sa-bills';

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
        const copy = this.convertDateFromClient(data.saBill);
        data.saBill = copy;
        return this.http
            .post<any>(this.resourceUrl, data, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(data): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(data.saBill);
        data.saBill = copy;
        return this.http
            .put<ISaBill>(this.resourceUrl, data, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id = null): Observable<any> {
        if (this._searchVoucher) {
            this._searchVoucher.page = this._searchVoucher.page ? this._searchVoucher.page - 1 : 0;
        } else {
            this._searchVoucher = { page: 0 };
        }
        if (id && isNaN(id)) {
            this._searchVoucher.id = id;
        } else {
            this._searchVoucher.id = '';
        }
        this._searchVoucher.fromInvoiceDate = this._searchVoucher.fromInvoiceDate ? this._searchVoucher.fromInvoiceDate : '';
        this._searchVoucher.toInvoiceDate = this._searchVoucher.toInvoiceDate ? this._searchVoucher.toInvoiceDate : '';
        const options = createRequestOption(this._searchVoucher);
        return this.http
            .get<any>(`${this.resourceUrl}/index`, { params: options, observe: 'response' })
            .pipe(map((res: any) => this.convertDateFromServer(res)));
    }

    findById(id: any): Observable<HttpResponse<any>> {
        return this.http
            .get<any>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findOneById(id: any): Observable<EntityResponseType> {
        const options = createRequestOption(id);
        return this.http
            .get<ISaBill>(this.resourceUrl + '/find-one', { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISaBill[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: any): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(saBill: ISaBill): ISaBill {
        for (let i = 0; i < saBill.saBillDetails.length; i++) {
            // @ts-ignore
            saBill.saBillDetails[i].expiryDate =
                saBill.saBillDetails[i].expiryDate != null &&
                saBill.saBillDetails[i].expiryDate instanceof moment &&
                saBill.saBillDetails[i].expiryDate.isValid()
                    ? saBill.saBillDetails[i].expiryDate.format(DATE_FORMAT)
                    : null;
        }
        const copy: ISaBill = Object.assign({}, saBill, {
            listDate:
                saBill.listDate != null && saBill.listDate instanceof moment && saBill.listDate.isValid()
                    ? saBill.listDate.format(DATE_FORMAT)
                    : null,
            invoiceDate: saBill.invoiceDate != null && saBill.invoiceDate.isValid() ? saBill.invoiceDate.format(DATE_TIME_FORMAT) : null,
            refDateTime:
                saBill.refDateTime != null && saBill.refDateTime.isValid() ? saBill.refDateTime.format(DATE_TIME_SECOND_FORMAT) : null,
            documentDate: saBill.documentDate != null && saBill.documentDate.isValid() ? saBill.invoiceDate.format(DATE_TIME_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: any): any {
        this.searchVoucher.id = null;
        if (res.body && res.body.saBill) {
            res.body.saBill.listDate = res.body.saBill.listDate != null ? moment(res.body.saBill.listDate) : null;
            res.body.saBill.invoiceDate = res.body.saBill.invoiceDate != null ? moment(res.body.saBill.invoiceDate) : null;
            res.body.saBill.refDateTime = res.body.saBill.refDateTime != null ? moment(res.body.saBill.refDateTime) : null;
            res.body.saBill.documentDate = res.body.saBill.documentDate != null ? moment(res.body.saBill.documentDate) : null;
            for (let i = 0; i < res.body.saBill.saBillDetails.length; i++) {
                const date = res.body.saBill.saBillDetails[i].expiryDate;
                if (date) {
                    res.body.saBill.saBillDetails[i].expiryDate = moment(date);
                }
            }
        }
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((saBill: ISaBill) => {
            saBill.listDate = saBill.listDate != null ? moment(saBill.listDate) : null;
            saBill.invoiceDate = saBill.invoiceDate != null ? moment(saBill.invoiceDate) : null;
            saBill.refDateTime = saBill.refDateTime != null ? moment(saBill.refDateTime) : null;
            saBill.documentDate = saBill.documentDate != null ? moment(saBill.documentDate) : null;
        });
        return res;
    }

    private convertDateArrayFromServerSaBillDTO(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((saBill: any) => {
            // saBill.listDate = saBill.listDate != null ? moment(saBill.listDate) : null;
            saBill.invoiceDate = saBill.invoiceDate != null ? moment(saBill.invoiceDate) : null;
            saBill.refDateTime = saBill.refDateTime != null ? moment(saBill.refDateTime) : null;
            saBill.documentDate = saBill.documentDate != null ? moment(saBill.documentDate) : null;
        });
        return res;
    }

    getAllSearchData() {
        return this.http.get<any>(`${this.resourceUrl}/search-data`, { observe: 'response' });
    }

    getAll() {
        return this.http.get<any>(`${this.resourceUrl}/search-all`, { observe: 'response' });
    }

    upload(file: any): Observable<any> {
        const formData = new FormData();
        if (file) {
            formData.append('file', file, file.name);
        }
        // @ts-ignore
        return this.http.post<any>(`${this.resourceUrl}/upload`, formData, { observe: 'response', responseType: 'blob' });
    }

    downloadTem(): Observable<any> {
        // @ts-ignore
        return this.http.post<any>(`${this.resourceUrl}/download`, null, { observe: 'response', responseType: 'blob' });
    }

    exportExcel(): Observable<any> {
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
        const options = createRequestOption(this._searchVoucher);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceUrl + '/export/pdf', { params: options, observe: 'response', headers, responseType: 'blob' });
    }

    getSabillCreated(req?: any): Observable<HttpResponse<any[]>> {
        // const options = this.convertDateArrayFromServer(req);
        const options1 = createRequestOption(req);
        return this.http
            .get<any[]>(this.resourceUrl + '/search-sabill-created', { params: options1, observe: 'response' })
            .pipe(map(res => this.convertDateArrayFromServerSaBillDTO(res)));
    }

    getSabillCreatedDetail(req: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http
            .get<any>(this.resourceUrl + '/search-sabill-created/detail', { params: options, observe: 'response' })
            .pipe(map(res => this.convertDateArrayFromServerSaBill(res)));
    }

    convertDateArrayFromServerSaBill(res: EntityArrayResponseType): EntityArrayResponseType {
        const saBill: any = res.body;
        for (let i = 0; i < saBill.length; i++) {
            // saBill.saBillCreatedDetailDTO.forEach((item: any) => {
            saBill[i].expiryDate = saBill[i].expiryDate != null ? moment(saBill[i].expiryDate) : null;

            saBill[i].invoiceDate = saBill[i].invoiceDate != null ? moment(saBill[i].invoiceDate) : null;
            saBill[i].refDateTime = saBill[i].refDateTime != null ? moment(saBill[i].refDateTime) : null;
        }
        return saBill;
    }

    // add by chuongnv
    checkRelateVoucher(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/check-relate-voucher`, { params: options, observe: 'response' });
    }

    multiDelete(obj: ISaBill[]): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceUrl}/multi-delete-sa-bills`, obj, { observe: 'response' });
    }
}
