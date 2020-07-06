import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { delay, map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMBCreditCard, MBCreditCard } from 'app/shared/model/mb-credit-card.model';
import { IUnit } from 'app/shared/model/unit.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import * as FileSaver from 'file-saver';
import * as XLSX from 'xlsx';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import { ISAInvoice } from 'app/shared/model/sa-invoice.model';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';

type EntityResponseType = HttpResponse<IMBCreditCard>;
type EntityArrayResponseType = HttpResponse<IMBCreditCard[]>;
const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
const EXCEL_EXTENSION = '.xlsx';

@Injectable({ providedIn: 'root' })
export class MBCreditCardService {
    private resourceUrl = SERVER_API_URL + 'api/mb-credit-cards';

    constructor(private http: HttpClient) {}

    create(mBCreditCard: IMBCreditCard): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mBCreditCard);
        return this.http
            .post<IMBCreditCard>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateFromServer(res)));
    }

    update(mBCreditCard: IMBCreditCard): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mBCreditCard);
        return this.http
            .put<IMBCreditCard>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IMBCreditCard>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMBCreditCard[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(mBCreditCard: IMBCreditCard): IMBCreditCard {
        const copy: IMBCreditCard = Object.assign({}, mBCreditCard, {
            date: mBCreditCard.date != null && mBCreditCard.date.isValid() ? mBCreditCard.date.format(DATE_FORMAT) : null,
            postedDate:
                mBCreditCard.postedDate != null && mBCreditCard.postedDate.isValid() ? mBCreditCard.postedDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    // private convertDateArrayFromClient(mBCreditCards: IMBCreditCard[]): IMBCreditCard[] {
    //     for (let i = 0; i < mBCreditCards.length; i++) {
    //         mBCreditCards[i].date = mBCreditCards[i].date != null && mBCreditCards[i].date.isValid() ? moment(mBCreditCards[i].date.format(DATE_FORMAT)) : null;
    //         mBCreditCards[i].postedDate = mBCreditCards[i].postedDate != null
    // && mBCreditCards[i].postedDate.isValid() ? moment(mBCreditCards[i].postedDate.format(DATE_FORMAT)) : null;
    //     }
    //     return mBCreditCards;
    // }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((mBCreditCard: IMBCreditCard) => {
            mBCreditCard.date = mBCreditCard.date != null ? moment(mBCreditCard.date) : null;
            mBCreditCard.postedDate = mBCreditCard.postedDate != null ? moment(mBCreditCard.postedDate) : null;
        });
        return res;
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMBCreditCard[]>(`${this.resourceUrl}/search-all`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMBCreditCard[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    public exportAsExcelFile(json: any[], excelFileName: string): void {
        const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(json);
        const workbook: XLSX.WorkBook = { Sheets: { 'Báo có': worksheet }, SheetNames: ['Báo có'] };
        const excelBuffer: any = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
        this.saveAsExcelFile(excelBuffer, excelFileName);
    }

    private saveAsExcelFile(buffer: any, fileName: string): void {
        const data: Blob = new Blob([buffer], { type: EXCEL_TYPE });
        FileSaver.saveAs(data, fileName + '_export_' + new Date().getTime() + EXCEL_EXTENSION);
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

    findByRowNum(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMBCreditCard>(`${this.resourceUrl}/findByRowNum`, { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    export(req?: any): Observable<any> {
        const options = createRequestOption(req);
        // @ts-ignore
        return this.http.post<any>(`${this.resourceUrl}/export`, null, {
            params: options,
            observe: 'response',
            responseType: 'blob'
        });
    }

    getIndexRow(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/getIndexRow`, { params: options, observe: 'response' });
    }

    exportPDF(req?: any): Observable<any> {
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

    multiDelete(obj: IMBCreditCard[]): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClientArr(obj);
        return this.http
            .post<any>(`${this.resourceUrl}/multi-delete-mb-credit-cards`, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServerForMultiAction(res)));
    }

    multiUnrecord(obj: IMBCreditCard[]): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClientArr(obj);
        return this.http
            .post<any>(`${this.resourceUrl}/multi-unrecord-mb-credit-cards`, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServerForMultiAction(res)));
    }

    private convertDateArrayFromServerForMultiAction(res: any): EntityArrayResponseType {
        res.body.listFail.forEach((xuLyChungTuModel: ViewVoucherNo) => {
            xuLyChungTuModel.date = xuLyChungTuModel.date != null ? moment(xuLyChungTuModel.date) : null;
            xuLyChungTuModel.postedDate = xuLyChungTuModel.postedDate != null ? moment(xuLyChungTuModel.postedDate) : null;
        });
        return res;
    }

    private convertDateFromClientArr(imbCreditCards: IMBCreditCard[]): IMBCreditCard[] {
        const mcArr = [];
        imbCreditCards.forEach(n => {
            const copy: IMBCreditCard = Object.assign({}, n, {
                date: n.date != null && n.date.isValid() ? n.date.format(DATE_FORMAT) : null,
                postedDate: n.postedDate != null && n.postedDate.isValid() ? n.postedDate.format(DATE_FORMAT) : null
            });
            mcArr.push(copy);
        });
        return mcArr;
    }
}
