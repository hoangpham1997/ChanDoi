import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { delay, map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMBDeposit, MBDeposit } from 'app/shared/model/mb-deposit.model';
import { IUnit } from 'app/shared/model/unit.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import * as FileSaver from 'file-saver';
import * as XLSX from 'xlsx';
import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';

type EntityResponseType = HttpResponse<IMBDeposit>;
type EntityArrayResponseType = HttpResponse<IMBDeposit[]>;
const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
const EXCEL_EXTENSION = '.xlsx';

@Injectable({ providedIn: 'root' })
export class XuatHoaDonService {
    private resourceUrl = SERVER_API_URL + 'api/mb-deposits';

    constructor(private http: HttpClient) {}

    create(mBDeposit: IMBDeposit): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mBDeposit);
        return this.http
            .post<IMBDeposit>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(mBDeposit: IMBDeposit): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mBDeposit);
        return this.http
            .put<IMBDeposit>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IMBDeposit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMBDeposit[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(mBDeposit: IMBDeposit): IMBDeposit {
        const copy: IMBDeposit = Object.assign({}, mBDeposit, {
            date: mBDeposit.date != null && mBDeposit.date.isValid() ? mBDeposit.date.format(DATE_FORMAT) : null,
            postedDate: mBDeposit.postedDate != null && mBDeposit.postedDate.isValid() ? mBDeposit.postedDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((mBDeposit: IMBDeposit) => {
            mBDeposit.date = mBDeposit.date != null ? moment(mBDeposit.date) : null;
            mBDeposit.postedDate = mBDeposit.postedDate != null ? moment(mBDeposit.postedDate) : null;
        });
        return res;
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMBDeposit[]>(`${this.resourceUrl}/search-all`, { params: options, observe: 'response' });
    }

    getAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMBDeposit[]>(this.resourceUrl, { params: options, observe: 'response' })
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
            .get<IMBDeposit>(`${this.resourceUrl}/findByRowNum`, { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    private convertStartUsingDate(res: any) {
        res.body = res.body != null ? moment(res.body) : null;
        return res;
    }
}
