import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPPInvoice } from 'app/shared/model/pp-invoice.model';
import { PPDiscountReturn } from 'app/shared/model/pp-discount-return.model';
import { IPPInvoiceDetails } from 'app/shared/model/pp-invoice-details.model';
import { IRefVoucher } from 'app/shared/model/ref-voucher.model';
import { IPPInvoiceDetailCost } from 'app/shared/model/pp-invoice-detail-cost.model';
import { IPPInvoiceDTO } from 'app/shared/modal/pp-invoice/pp-invoice-dto.model';
import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';
import { IGOtherVoucherDetails } from 'app/shared/model/g-other-voucher-details.model';

type EntityResponseType = HttpResponse<IGOtherVoucher>;
type EntityResponseTypeAny = HttpResponse<any>;
type EntityArrayResponseType = HttpResponse<IGOtherVoucher[]>;
type EntityArrayResponseDetailType = HttpResponse<IGOtherVoucherDetails[]>;

@Injectable({ providedIn: 'root' })
export class KetChuyenLaiLoService {
    private resourceUrl = SERVER_API_URL + 'api/g-other-vouchers';

    constructor(private http: HttpClient) {}

    create(otherVoucher: IGOtherVoucher): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(otherVoucher);
        return this.http
            .post<IGOtherVoucher>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(otherVoucher: IGOtherVoucher): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(otherVoucher);
        return this.http
            .put<IGOtherVoucher>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IGOtherVoucher>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findById(req: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http
            .get<any>(SERVER_API_URL + 'api/g-other-vouchers/get-by-id', { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    searchGOtherVoucher(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<PPDiscountReturn[]>(SERVER_API_URL + 'api/g-other-vouchers/search', {
            params: options,
            observe: 'response'
        });
    }

    findDetailById(req: any): Observable<EntityArrayResponseDetailType> {
        const options = createRequestOption(req);
        return this.http.get<any>(SERVER_API_URL + 'api/g-other-vouchers/get-detail-by-gothervoucher-id', {
            params: options,
            observe: 'response'
        });
    }

    createGOtherVoucher(ref?: any): Observable<any> {
        const copy = this.convertDateFromClient(ref);
        return this.http
            .post<IGOtherVoucher>(SERVER_API_URL + 'api/g-other-vouchers-kc/save', copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    private convertDateFromClient(gOtherVoucher: IGOtherVoucher): IPPInvoice {
        return Object.assign({}, gOtherVoucher, {
            date: gOtherVoucher.date != null && gOtherVoucher.date.isValid() ? gOtherVoucher.date.format(DATE_FORMAT) : null,
            postedDate:
                gOtherVoucher.postedDate != null && gOtherVoucher.postedDate.isValid() ? gOtherVoucher.postedDate.format(DATE_FORMAT) : null
        });
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        return res;
    }

    deleteById(req: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<any>(SERVER_API_URL + 'api/g-other-vouchers-kc/delete-by-id', {
            params: options,
            observe: 'response'
        });
    }

    findByRowNum(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPPInvoice>('api/g-other-vouchers-kc/index', { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    export(type: 'excel' | 'pdf', req?: any): Observable<HttpResponse<any>> {
        req.fromDate = req.fromDate && req.fromDate.isValid() ? req.fromDate.format(DATE_FORMAT) : '';
        req.toDate = req.toDate && req.toDate.isValid() ? req.toDate.format(DATE_FORMAT) : '';
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(`${this.resourceUrl}/kc-export/${type}`, {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    findRefVoucherByRefId(req: any): Observable<HttpResponse<IRefVoucher[]>> {
        return this.http.get<IRefVoucher[]>(SERVER_API_URL + 'api/view-vouchers/find-by-ref-id-ppinvoice', {
            params: req,
            observe: 'response'
        });
    }

    getDataKc(req: any): Observable<HttpResponse<any[]>> {
        return this.http.get<any[]>(SERVER_API_URL + 'api/view-vouchers/get-data-kc', {
            params: req,
            observe: 'response'
        });
    }

    getDataKcDiff(req: any): Observable<HttpResponse<any[]>> {
        return this.http.get<any[]>(SERVER_API_URL + 'api/view-vouchers/get-data-kc-diff', {
            params: req,
            observe: 'response'
        });
    }

    getDataAccountSpecial(req: any): Observable<HttpResponse<any[]>> {
        return this.http.get<any[]>(SERVER_API_URL + 'api/view-vouchers/get-data-kc-account-special', {
            params: req,
            observe: 'response'
        });
    }
}
