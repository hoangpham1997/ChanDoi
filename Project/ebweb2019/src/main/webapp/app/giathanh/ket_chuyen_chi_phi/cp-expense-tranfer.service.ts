import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { ICPExpenseTranfer } from 'app/shared/model/cp-expense-tranfer.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';

type EntityResponseType = HttpResponse<ICPExpenseTranfer>;
type EntityArrayResponseType = HttpResponse<ICPExpenseTranfer[]>;

@Injectable({ providedIn: 'root' })
export class CPExpenseTranferService {
    private resourceUrl = SERVER_API_URL + 'api/c-p-expense-tranfers';

    constructor(private http: HttpClient) {}

    create(cPExpenseTranfer: ICPExpenseTranfer): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(cPExpenseTranfer);
        return this.http
            .post<ICPExpenseTranfer>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(cPExpenseTranfer: ICPExpenseTranfer): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(cPExpenseTranfer);
        return this.http
            .put<ICPExpenseTranfer>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<ICPExpenseTranfer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICPExpenseTranfer[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(cPExpenseTranfer: ICPExpenseTranfer): ICPExpenseTranfer {
        const copy: ICPExpenseTranfer = Object.assign({}, cPExpenseTranfer, {
            postedDate:
                cPExpenseTranfer.postedDate != null && cPExpenseTranfer.postedDate.isValid()
                    ? cPExpenseTranfer.postedDate.format(DATE_FORMAT)
                    : null,
            date: cPExpenseTranfer.date != null && cPExpenseTranfer.date.isValid() ? cPExpenseTranfer.date.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((cPExpenseTranfer: ICPExpenseTranfer) => {
            cPExpenseTranfer.postedDate = cPExpenseTranfer.postedDate != null ? moment(cPExpenseTranfer.postedDate) : null;
            cPExpenseTranfer.date = cPExpenseTranfer.date != null ? moment(cPExpenseTranfer.date) : null;
        });
        return res;
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICPExpenseTranfer[]>(`${this.resourceUrl}/search-all`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    exportPDF(req?: any): Observable<any> {
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

    multiUnrecord(obj: ICPExpenseTranfer[]): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClientArr(obj);
        return this.http
            .post<any>(`${this.resourceUrl}/multi-unrecord-c-p-expense-tranfers`, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServerForMultiAction(res)));
    }

    private convertDateArrayFromServerForMultiAction(res: any): EntityArrayResponseType {
        res.body.listFail.forEach((xuLyChungTuModel: ViewVoucherNo) => {
            xuLyChungTuModel.date = xuLyChungTuModel.date != null ? moment(xuLyChungTuModel.date) : null;
            xuLyChungTuModel.postedDate = xuLyChungTuModel.postedDate != null ? moment(xuLyChungTuModel.postedDate) : null;
        });
        return res;
    }

    private convertDateFromClientArr(imbDeposits: ICPExpenseTranfer[]): ICPExpenseTranfer[] {
        const mcArr = [];
        imbDeposits.forEach(n => {
            const copy: ICPExpenseTranfer = Object.assign({}, n, {
                date: n.date != null && n.date.isValid() ? n.date.format(DATE_FORMAT) : null,
                postedDate: n.postedDate != null && n.postedDate.isValid() ? n.postedDate.format(DATE_FORMAT) : null
            });
            mcArr.push(copy);
        });
        return mcArr;
    }

    multiDelete(obj: ICPExpenseTranfer[]): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClientArr(obj);
        return this.http
            .post<any>(`${this.resourceUrl}/multi-delete-c-p-expense-tranfers`, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServerForMultiAction(res)));
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

    getCPExpenseTransferDetails(req?: any) {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/get-all-c-p-expense-tranfer-details', { observe: 'response', params: options });
    }

    findByCPPeriodID(req?: any) {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/find-by-c-p-period-id', { observe: 'response', params: options });
    }
}
