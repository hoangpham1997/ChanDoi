import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { CHI_QUA_TON_QUY, SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IGeneralLedger } from 'app/shared/model/general-ledger.model';
import { Irecord } from 'app/shared/model/record';
import { RequestRecordListDtoModel } from 'app/shared/model/request-record-list-dto.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';

type EntityResponseType = HttpResponse<IGeneralLedger>;
type EntityArrayResponseType = HttpResponse<IGeneralLedger[]>;

@Injectable({ providedIn: 'root' })
export class GeneralLedgerService {
    private resourceUrl = SERVER_API_URL + 'api/general-ledgers';

    constructor(private http: HttpClient) {}

    calculatingLiabilities(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/calculating-liabilities`, { params: options, observe: 'response' });
    }

    create(generalLedger: IGeneralLedger): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(generalLedger);
        return this.http
            .post<IGeneralLedger>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(generalLedger: IGeneralLedger): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(generalLedger);
        return this.http
            .put<IGeneralLedger>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IGeneralLedger>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IGeneralLedger[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getCPExpenseListByCostSet(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IGeneralLedger[]>(`${this.resourceUrl}/get-cp-expenselist`, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getCPExpenseListAll(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<any[]>(`${this.resourceUrl}/get-cp-expenselist-all`, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<any[]>) => this.convertDateArrayFromServer(res)));
    }

    getByAllocationMethod(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/allocation-method`, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    // add  by Hautv
    record(record: Irecord): Observable<HttpResponse<Irecord>> {
        return this.http.post<Irecord>(`${this.resourceUrl}/record`, record, { observe: 'response' });
    }

    // add  by Hautv
    recordList(rq: RequestRecordListDtoModel): Observable<HttpResponse<HandlingResult>> {
        return this.http
            .post<any>(`${this.resourceUrl}/record-list`, rq, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServerForMultiAction(res)));
    }

    private convertDateArrayFromServerForMultiAction(res: any): any {
        res.body.listFail.forEach((xuLyChungTuModel: ViewVoucherNo) => {
            xuLyChungTuModel.date = xuLyChungTuModel.date != null ? moment(xuLyChungTuModel.date) : null;
            xuLyChungTuModel.postedDate = xuLyChungTuModel.postedDate != null ? moment(xuLyChungTuModel.postedDate) : null;
        });
        return res;
    }

    // add  by Hautv
    unrecord(record: Irecord): Observable<HttpResponse<Irecord>> {
        return this.http.post<Irecord>(`${this.resourceUrl}/unrecord`, record, { observe: 'response' });
    }

    unRecordList(rq: RequestRecordListDtoModel): Observable<HttpResponse<HandlingResult>> {
        return this.http.post<any>(`${this.resourceUrl}/unrecord-list`, rq, { observe: 'response' });
    }
    // add by chuongnv
    getSoDuSoQuy(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/getTotalBalanceAmount`, { params: options, observe: 'response' });
    }

    getCollectionVoucher(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/getCollectionVoucher`, { params: options, observe: 'response' });
    }

    getSpendingVoucher(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/getSpendingVoucher`, { params: options, observe: 'response' });
    }

    getListMatch(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/getListMatch`, { params: options, observe: 'response' });
    }
    // add by anmt
    getListForCPExpenseList(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/getListForCPExpenseList`, { params: options, observe: 'response' });
    }

    private convertDateFromClient(generalLedger: IGeneralLedger): IGeneralLedger {
        const copy: IGeneralLedger = Object.assign({}, generalLedger, {
            date: generalLedger.date != null && generalLedger.date.isValid() ? generalLedger.date.format(DATE_FORMAT) : null,
            postedDate:
                generalLedger.postedDate != null && generalLedger.postedDate.isValid()
                    ? generalLedger.postedDate.format(DATE_FORMAT)
                    : null,
            invoiceDate:
                generalLedger.invoiceDate != null && generalLedger.invoiceDate.isValid()
                    ? generalLedger.invoiceDate.format(DATE_FORMAT)
                    : null,
            refDateTime:
                generalLedger.refDateTime != null && generalLedger.refDateTime.isValid()
                    ? generalLedger.refDateTime.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        res.body.invoiceDate = res.body.invoiceDate != null ? moment(res.body.invoiceDate) : null;
        res.body.refDateTime = res.body.refDateTime != null ? moment(res.body.refDateTime) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((generalLedger: IGeneralLedger) => {
            generalLedger.date = generalLedger.date != null ? moment(generalLedger.date) : null;
            generalLedger.postedDate = generalLedger.postedDate != null ? moment(generalLedger.postedDate) : null;
            generalLedger.invoiceDate = generalLedger.invoiceDate != null ? moment(generalLedger.invoiceDate) : null;
            generalLedger.refDateTime = generalLedger.refDateTime != null ? moment(generalLedger.refDateTime) : null;
        });
        return res;
    }

    getViewGLPayExceedCash(): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/getViewGLPayExceedCash`, { observe: 'response' });
    }
}
