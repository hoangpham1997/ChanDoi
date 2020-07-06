import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISAInvoiceDetails, SAInvoiceDetails } from 'app/shared/model/sa-invoice-details.model';
import { PPDiscountReturnDetails } from 'app/shared/model/pp-discount-return-details.model';

type EntityResponseType = HttpResponse<ISAInvoiceDetails>;
type EntityArrayResponseType = HttpResponse<ISAInvoiceDetails[]>;

@Injectable({ providedIn: 'root' })
export class SAInvoiceDetailsService {
    private resourceUrl = SERVER_API_URL + 'api/sa-invoice-details';

    constructor(private http: HttpClient) {}

    create(sAInvoiceDetail: ISAInvoiceDetails): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sAInvoiceDetail);
        return this.http
            .post<ISAInvoiceDetails>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(sAInvoiceDetail: ISAInvoiceDetails): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sAInvoiceDetail);
        return this.http
            .put<ISAInvoiceDetails>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISAInvoiceDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISAInvoiceDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(sAInvoiceDetail: ISAInvoiceDetails): ISAInvoiceDetails {
        const copy: ISAInvoiceDetails = Object.assign({}, sAInvoiceDetail, {
            expiryDate:
                sAInvoiceDetail.expiryDate != null && sAInvoiceDetail.expiryDate.isValid()
                    ? sAInvoiceDetail.expiryDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.expiryDate = res.body.expiryDate != null ? moment(res.body.expiryDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((sAInvoiceDetail: ISAInvoiceDetails) => {
            sAInvoiceDetail.expiryDate = sAInvoiceDetail.expiryDate != null ? moment(sAInvoiceDetail.expiryDate) : null;
        });
        return res;
    }

    getDetailByID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/by-id', { params: options, observe: 'response' });
    }

    // Add by Hautv
    getDetailByMCReceiptID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/by-mcreceipt-id', { params: options, observe: 'response' });
    }

    // Add by namnh
    getDetailByMBDepositID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/by-mb-deposit-id', { params: options, observe: 'response' });
    }

    findAllDetailsById(req: any): Observable<HttpResponse<SAInvoiceDetails[]>> {
        const options = createRequestOption(req);
        return this.http.get<SAInvoiceDetails[]>(`${this.resourceUrl}/details`, { params: options, observe: 'response' });
    }
}
