import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMCPayment } from 'app/shared/model/mc-payment.model';
import { IMCPaymentDetails } from 'app/shared/model/mc-payment-details.model';
import { IMCPaymentDetailTax } from 'app/shared/model/mc-payment-detail-tax.model';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { IMCReceipt } from 'app/shared/model/mc-receipt.model';

type EntityResponseType = HttpResponse<IMCPayment>;
type EntityArrayResponseType = HttpResponse<IMCPayment[]>;
type EntityArrayResponseTypeDetails = HttpResponse<IMCPaymentDetails[]>;

@Injectable({ providedIn: 'root' })
export class MCPaymentService {
    private resourceUrl = SERVER_API_URL + 'api/m-c-payments';
    private resourceUrlDTO = SERVER_API_URL + 'api/m-c-paymentsDTO';

    constructor(private http: HttpClient) {}

    create(mCPayment: IMCPayment): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mCPayment);
        return this.http
            .post<IMCPayment>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateFromServerDTO(res)));
    }

    update(mCPayment: IMCPayment): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mCPayment);
        return this.http
            .put<IMCPayment>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateFromServerDTO(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IMCPayment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findMCPaymentDetails(id: string): Observable<EntityArrayResponseTypeDetails> {
        const options = createRequestOption({ id });
        return this.http
            .get<IMCPaymentDetails[]>(`${this.resourceUrl}/findMCPaymentDetails`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: EntityArrayResponseTypeDetails) => this.convertDateArrayFromServerDetails(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMCPayment[]>(this.resourceUrlDTO, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(mCPayment: IMCPayment): IMCPayment {
        const copy: IMCPayment = Object.assign({}, mCPayment, {
            date: mCPayment.date != null && mCPayment.date.isValid() ? mCPayment.date.format(DATE_FORMAT) : null,
            postedDate: mCPayment.postedDate != null && mCPayment.postedDate.isValid() ? mCPayment.postedDate.format(DATE_FORMAT) : null
        });
        /* if (copy.mcpaymentDetailTaxes) {
            const copyTaxDetails = [];
            copy.mcpaymentDetailTaxes.forEach(n => {
                const copyMcpaymentDetailTax = Object.assign({}, n, {
                    invoiceDate: n.invoiceDate != null && n.invoiceDate.isValid() ? n.invoiceDate.format(DATE_FORMAT) : null
                });
                copyTaxDetails.push(copyMcpaymentDetailTax);
            });
            copy.mcpaymentDetailTaxes = copyTaxDetails;
        }*/
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        if (res.body.mcpaymentDetailTaxes) {
            res.body.mcpaymentDetailTaxes = this.convertDateArrayFromServerForTabTax(res.body.mcpaymentDetailTaxes);
        }
        return res;
    }

    private convertDateFromServerDTO(res: any): any {
        res.body.mCPayment.date = res.body.mCPayment.date != null ? moment(res.body.mCPayment.date) : null;
        res.body.mCPayment.postedDate = res.body.mCPayment.postedDate != null ? moment(res.body.mCPayment.postedDate) : null;
        return res;
    }

    private convertDateArrayFromServerForTabTax(res: IMCPaymentDetailTax[]): IMCPaymentDetailTax[] {
        res.forEach((mCPayment: IMCPaymentDetailTax) => {
            mCPayment.invoiceDate = mCPayment.invoiceDate != null ? moment(mCPayment.invoiceDate) : null;
        });
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((mCPayment: IMCPayment) => {
            mCPayment.date = mCPayment.date != null ? moment(mCPayment.date) : null;
            mCPayment.postedDate = mCPayment.postedDate != null ? moment(mCPayment.postedDate) : null;
        });
        return res;
    }

    private convertDateArrayFromServerDetails(res: EntityArrayResponseTypeDetails): EntityArrayResponseTypeDetails {
        res.body.forEach((mCPaymentDetails: IMCPaymentDetails) => {
            mCPaymentDetails.matchDate = mCPaymentDetails.matchDate != null ? moment(mCPaymentDetails.matchDate) : null;
        });
        return res;
    }

    private convertDateFromClientArr(mCPayments: IMCPayment[]): IMCPayment[] {
        const mcArr = [];
        mCPayments.forEach(n => {
            const copy: IMCPayment = Object.assign({}, n, {
                date: n.date != null && n.date.isValid() ? n.date.format(DATE_FORMAT) : null,
                postedDate: n.postedDate != null && n.postedDate.isValid() ? n.postedDate.format(DATE_FORMAT) : null
            });
            mcArr.push(copy);
        });
        return mcArr;
    }

    multiUnrecord(obj: IMCPayment[]): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClientArr(obj);
        return this.http
            .post<any>(`${this.resourceUrl}/multi-unrecord`, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServerForMultiAction(res)));
    }

    multiDelete(obj: IMCPayment[]): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClientArr(obj);
        return this.http
            .post<any>(`${this.resourceUrl}/multi-delete`, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServerForMultiAction(res)));
    }

    private convertDateArrayFromServerForMultiAction(res: any): EntityArrayResponseType {
        res.body.listFail.forEach((xuLyChungTuModel: ViewVoucherNo) => {
            xuLyChungTuModel.date = xuLyChungTuModel.date != null ? moment(xuLyChungTuModel.date) : null;
            xuLyChungTuModel.postedDate = xuLyChungTuModel.postedDate != null ? moment(xuLyChungTuModel.postedDate) : null;
        });
        return res;
    }
}
