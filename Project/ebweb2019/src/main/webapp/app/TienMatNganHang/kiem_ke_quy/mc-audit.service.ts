import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_SECOND_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMCAudit } from 'app/shared/model/mc-audit.model';
import { MCPayment } from 'app/shared/model/mc-payment.model';
import { MCReceipt } from 'app/shared/model/mc-receipt.model';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';

type EntityResponseType = HttpResponse<IMCAudit>;
type EntityArrayResponseType = HttpResponse<IMCAudit[]>;
const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
const EXCEL_EXTENSION = '.xlsx';

@Injectable({ providedIn: 'root' })
export class MCAuditService {
    private resourceUrl = SERVER_API_URL + 'api/mc-audits';
    private resourceUrlDTO = SERVER_API_URL + 'api/mc-auditsDTO';
    mCPayment: MCPayment;
    mCReceipt: MCReceipt;
    constructor(private http: HttpClient) {}

    create(mCAudit: IMCAudit): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mCAudit);
        return this.http
            .post<IMCAudit>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(mCAudit: IMCAudit): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mCAudit);
        return this.http
            .put<IMCAudit>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: any): Observable<EntityResponseType> {
        return this.http
            .get<IMCAudit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMCAudit[]>(this.resourceUrlDTO, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    edit(id: string): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceUrl}/deleteRef/${id}`, { observe: 'response' });
    }

    multiDelete(obj: IMCAudit[]): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceUrl}/multi-delete-mc-audits`, obj, { observe: 'response' });
    }

    findMCReceiptByMCAuditID(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/findMCReceiptByMCAuditID`, { params: options, observe: 'response' });
    }

    findMCPaymentByMCAuditID(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/findMCPaymentByMCAuditID`, { params: options, observe: 'response' });
    }

    searchMCAudit(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMCAudit[]>(SERVER_API_URL + 'api/mc-audit-objects-search', {
            params: options,
            observe: 'response'
        });
    }

    getDetailByID(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(this.resourceUrl + '/by-id', { params: options, observe: 'response' });
    }

    private convertDateFromClient(mCAudit: IMCAudit): IMCAudit {
        const copy: IMCAudit = Object.assign({}, mCAudit, {
            date: mCAudit.date != null && mCAudit.date.isValid() ? mCAudit.date.format(DATE_FORMAT) : null,
            auditDate: mCAudit.auditDate != null && mCAudit.auditDate.isValid() ? mCAudit.auditDate.format(DATE_TIME_SECOND_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.auditDate = res.body.auditDate != null ? moment(res.body.auditDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((mCAudit: IMCAudit) => {
            mCAudit.date = mCAudit.date != null ? moment(mCAudit.date) : null;
            mCAudit.auditDate = mCAudit.auditDate != null ? moment(mCAudit.auditDate) : null;
        });
        return res;
    }

    findByRowNum(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMCAudit>(`${this.resourceUrl}/index`, { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
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
}
