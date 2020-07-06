import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';
import * as XLSX from 'xlsx';
import * as FileSaver from 'file-saver';
import { IMultipleRecord, MutipleRecord } from 'app/shared/model/mutiple-record';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';

const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
const EXCEL_EXTENSION = '.xlsx';

type EntityResponseType = HttpResponse<IGOtherVoucher>;
type EntityMutipleRecord = HttpResponse<IMultipleRecord>;
type EntityArrayResponseType = HttpResponse<IGOtherVoucher[]>;

@Injectable({ providedIn: 'root' })
export class GOtherVoucherService {
    private resourceUrl = SERVER_API_URL + 'api/g-other-vouchers';

    constructor(private http: HttpClient) {}

    create(gOtherVoucher: IGOtherVoucher): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(gOtherVoucher);
        return this.http
            .post<IGOtherVoucher>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(gOtherVoucher: IGOtherVoucher): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(gOtherVoucher);
        return this.http
            .put<IGOtherVoucher>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IGOtherVoucher>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IGOtherVoucher[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(gOtherVoucher: IGOtherVoucher): IGOtherVoucher {
        const copy: IGOtherVoucher = Object.assign({}, gOtherVoucher, {
            date: gOtherVoucher.date != null && gOtherVoucher.date.isValid() ? gOtherVoucher.date.format(DATE_FORMAT) : null,
            postedDate:
                gOtherVoucher.postedDate != null && gOtherVoucher.postedDate.isValid() ? gOtherVoucher.postedDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((gOtherVoucher: IGOtherVoucher) => {
            gOtherVoucher.date = gOtherVoucher.date != null ? moment(gOtherVoucher.date) : null;
            gOtherVoucher.postedDate = gOtherVoucher.postedDate != null ? moment(gOtherVoucher.postedDate) : null;
        });
        return res;
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IGOtherVoucher[]>(`${this.resourceUrl}/search-all`, {
                params: options,
                observe: 'response'
            })
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

    mutipleRecord(listID: MutipleRecord): Observable<HttpResponse<EntityMutipleRecord>> {
        // const options = createRequestOption(listID);
        return this.http.put<EntityMutipleRecord>(`${this.resourceUrl}/mutiple-record`, listID, { observe: 'response' });
    }

    mutipleDelete(listID: string[]): Observable<HttpResponse<string[]>> {
        return this.http.put<string[]>(`${this.resourceUrl}/mutiple-delete`, listID, { observe: 'response' });
    }

    findByRowNum(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IGOtherVoucher>(`${this.resourceUrl}/findByRowNum`, { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    getIndexRow(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/getIndexRow`, { params: options, observe: 'response' });
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

    multiDelete(obj: IGOtherVoucher[]): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClientArr(obj);
        return this.http
            .post<any>(`${this.resourceUrl}/multi-delete-g-other-vouchers`, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServerForMultiAction(res)));
    }

    multiUnRecord(obj: IGOtherVoucher[]): Observable<HttpResponse<any>> {
        const options = this.convertDateFromClientArr(obj);
        return this.http
            .post<any>(`${this.resourceUrl}/multi-un-record-g-other-vouchers`, options, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServerForMultiAction(res)));
    }

    private convertDateFromClientArr(gOtherVouchers: IGOtherVoucher[]): IGOtherVoucher[] {
        const goArr = [];
        gOtherVouchers.forEach(n => {
            const copy: IGOtherVoucher = Object.assign({}, n, {
                date: n.date != null && n.date.isValid() ? n.date.format(DATE_FORMAT) : null,
                postedDate: n.postedDate != null && n.postedDate.isValid() ? n.postedDate.format(DATE_FORMAT) : null
            });
            goArr.push(copy);
        });
        return goArr;
    }

    private convertDateArrayFromServerForMultiAction(res: any): EntityArrayResponseType {
        res.body.listFail.forEach((xuLyChungTuModel: ViewVoucherNo) => {
            xuLyChungTuModel.date = xuLyChungTuModel.date != null ? moment(xuLyChungTuModel.date) : null;
            xuLyChungTuModel.postedDate = xuLyChungTuModel.postedDate != null ? moment(xuLyChungTuModel.postedDate) : null;
        });
        return res;
    }
}
