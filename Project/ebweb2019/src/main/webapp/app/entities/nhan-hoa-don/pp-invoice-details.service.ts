import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPPInvoiceDetails } from 'app/shared/model/pp-invoice-details.model';
import { IPPInvoiceConvert } from 'app/shared/model/pp-invoice-convert.model';

type EntityResponseType = HttpResponse<IPPInvoiceDetails>;
type EntityArrayResponseType = HttpResponse<IPPInvoiceDetails[]>;
type EntityArrayResponseConvertType = HttpResponse<IPPInvoiceConvert[]>;

@Injectable({ providedIn: 'root' })
export class PPInvoiceDetailsService {
    private resourceUrl = SERVER_API_URL + 'api/pp-invoice-details';

    constructor(private http: HttpClient) {}

    create(pPInvoiceDetails: IPPInvoiceDetails): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(pPInvoiceDetails);
        return this.http
            .post<IPPInvoiceDetails>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(pPInvoiceDetails: IPPInvoiceDetails): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(pPInvoiceDetails);
        return this.http
            .put<IPPInvoiceDetails>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPPInvoiceDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    // đẫ phân biệt loại sổ chỉ cần gọi noMBook sẽ lấy được số chứng từ tương ứng
    getPPInvoiceDetailsGetLicense(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<any[]>(this.resourceUrl + '/get-license', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPPInvoiceDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(pPInvoiceDetails: IPPInvoiceDetails): IPPInvoiceDetails {
        const copy: IPPInvoiceDetails = Object.assign({}, pPInvoiceDetails, {
            expiryDate:
                pPInvoiceDetails.expiryDate != null && pPInvoiceDetails.expiryDate.isValid()
                    ? pPInvoiceDetails.expiryDate.format(DATE_FORMAT)
                    : null,
            invoiceDate:
                pPInvoiceDetails.invoiceDate != null && pPInvoiceDetails.invoiceDate.isValid()
                    ? pPInvoiceDetails.invoiceDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.expiryDate = res.body.expiryDate != null ? moment(res.body.expiryDate) : null;
        res.body.invoiceDate = res.body.invoiceDate != null ? moment(res.body.invoiceDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((pPInvoiceDetails: IPPInvoiceDetails) => {
            pPInvoiceDetails.expiryDate = pPInvoiceDetails.expiryDate != null ? moment(pPInvoiceDetails.expiryDate) : null;
            pPInvoiceDetails.invoiceDate = pPInvoiceDetails.invoiceDate != null ? moment(pPInvoiceDetails.invoiceDate) : null;
        });
        return res;
    }
}
