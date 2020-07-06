import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';
import { IUnit } from 'app/shared/model/unit.model';
import { DatePipe, formatDate, getLocaleDateFormat } from '@angular/common';
import { IMBCreditCard } from 'app/shared/model/mb-credit-card.model';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';

type EntityResponseType = HttpResponse<IMBTellerPaper>;
type EntityArrayResponseType = HttpResponse<IMBTellerPaper[]>;

@Injectable({ providedIn: 'root' })
export class MBTellerPaperService {
    private resourceUrl = SERVER_API_URL + 'api/mb-teller-papers';
    private resourceUrlDTO = SERVER_API_URL + 'api/mb-teller-papersDTO';

    constructor(private http: HttpClient) {}

    create(mBTellerPaper: IMBTellerPaper): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mBTellerPaper);
        return this.http
            .post<IMBTellerPaper>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(mBTellerPaper: IMBTellerPaper): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mBTellerPaper);
        return this.http
            .put<IMBTellerPaper>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IMBTellerPaper>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMBTellerPaper[]>(this.resourceUrlDTO, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMBTellerPaper[]>(`${this.resourceUrl}/search-all`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    findByRowNum(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMBTellerPaper>(`${this.resourceUrl}/findByRowNum`, { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    getIndexRow(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/getIndexRow`, { params: options, observe: 'response' });
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

    exportExcel(req?: any): Observable<any> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceUrl + '/export/excel', {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    export(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceUrl + '/export/pdf', {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    private convertDateFromClient(mBTellerPaper: IMBTellerPaper): IMBTellerPaper {
        const copy: IMBTellerPaper = Object.assign({}, mBTellerPaper, {
            date: mBTellerPaper.date != null && mBTellerPaper.date.isValid() ? mBTellerPaper.date.format(DATE_FORMAT) : null,
            postedDate:
                mBTellerPaper.postedDate != null && mBTellerPaper.postedDate.isValid()
                    ? mBTellerPaper.postedDate.format(DATE_FORMAT)
                    : null,
            issueDate:
                mBTellerPaper.issueDate != null && mBTellerPaper.issueDate.isValid() ? mBTellerPaper.issueDate.format(DATE_FORMAT) : null,
            matchDate:
                mBTellerPaper.matchDate != null && mBTellerPaper.matchDate.isValid() ? mBTellerPaper.matchDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        res.body.issueDate = res.body.issueDate != null ? moment(res.body.issueDate) : null;
        res.body.matchDate = res.body.matchDate != null ? moment(res.body.matchDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((mBTellerPaper: IMBTellerPaper) => {
            mBTellerPaper.date = mBTellerPaper.date != null ? moment(mBTellerPaper.date) : null;
            mBTellerPaper.postedDate = mBTellerPaper.postedDate != null ? moment(mBTellerPaper.postedDate) : null;
            mBTellerPaper.issueDate = mBTellerPaper.issueDate != null ? moment(mBTellerPaper.issueDate) : null;
            mBTellerPaper.matchDate = mBTellerPaper.matchDate != null ? moment(mBTellerPaper.matchDate) : null;
        });
        return res;
    }

    multiDelete(obj: IMBTellerPaper[]): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClientArr(obj);
        return this.http
            .post<any>(`${this.resourceUrl}/multi-delete-mb-teller-papers`, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServerForMultiAction(res)));
    }

    multiUnrecord(obj: IMBTellerPaper[]): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClientArr(obj);
        return this.http
            .post<any>(`${this.resourceUrl}/multi-unrecord-mb-teller-papers`, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServerForMultiAction(res)));
    }

    private convertDateArrayFromServerForMultiAction(res: any): EntityArrayResponseType {
        res.body.listFail.forEach((xuLyChungTuModel: ViewVoucherNo) => {
            xuLyChungTuModel.date = xuLyChungTuModel.date != null ? moment(xuLyChungTuModel.date) : null;
            xuLyChungTuModel.postedDate = xuLyChungTuModel.postedDate != null ? moment(xuLyChungTuModel.postedDate) : null;
        });
        return res;
    }

    private convertDateFromClientArr(imbTellerPapers: IMBTellerPaper[]): IMBTellerPaper[] {
        const mcArr = [];
        imbTellerPapers.forEach(n => {
            const copy: IMBTellerPaper = Object.assign({}, n, {
                date: n.date != null && n.date.isValid() ? n.date.format(DATE_FORMAT) : null,
                postedDate: n.postedDate != null && n.postedDate.isValid() ? n.postedDate.format(DATE_FORMAT) : null
            });
            mcArr.push(copy);
        });
        return mcArr;
    }
}
