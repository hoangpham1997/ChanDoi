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
import { IPPDiscountReturn } from 'app/shared/model/pp-discount-return.model';
import { ITIDecrement, TIDecrement } from 'app/shared/model/ti-decrement.model';

const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
const EXCEL_EXTENSION = '.xlsx';

type EntityResponseType = HttpResponse<ITIDecrement>;
type EntityMutipleRecord = HttpResponse<IMultipleRecord>;
type EntityArrayResponseType = HttpResponse<ITIDecrement[]>;

@Injectable({ providedIn: 'root' })
export class GhiGiamCcdcService {
    private resourceUrl = SERVER_API_URL + 'api/t-i-decrements';

    constructor(private http: HttpClient) {}

    create(itiDecrement: ITIDecrement): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(itiDecrement);
        return this.http
            .post<ITIDecrement>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(itiDecrement: ITIDecrement): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(itiDecrement);
        return this.http
            .put<ITIDecrement>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    createPB(ref?: any): Observable<HttpResponse<any>> {
        // const copy = this.convertDateFromPBClient(ref);
        return this.http
            .post<IPPDiscountReturn>(this.resourceUrl, ref, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    updatePB(ref?: any): Observable<HttpResponse<any>> {
        // const copy = this.convertDateFromPBClient(ref);
        return this.http
            .put<IPPDiscountReturn>(this.resourceUrl, ref, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<ITIDecrement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findDetailPB(id: any): Observable<HttpResponse<any>> {
        return this.http
            .get<any>(`${this.resourceUrl}/details/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findDetailViewPB(id: any): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/details/view/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITIDecrement[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    deletePB(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/allocation/${id}`, { observe: 'response' });
    }
    multiDeletePB(obj: any[]): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromServerMulti(obj);
        return this.http.post<any>(`${this.resourceUrl}/multi-delete-g-other-vouchers-allocation`, copy, { observe: 'response' });
    }
    multiDelete(obj: any[]): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromServerMulti(obj);
        return this.http.post<any>(`${this.resourceUrl}/multi-delete-g-other-vouchers`, copy, { observe: 'response' });
    }

    private convertDateFromClient(itiDecrement: ITIDecrement): ITIDecrement {
        const copy: ITIDecrement = Object.assign({}, itiDecrement, {
            date: itiDecrement.date != null && itiDecrement.date.isValid() ? itiDecrement.date.format(DATE_FORMAT) : null
            // postedDate:
            //     itiDecrement.postedDate != null && itiDecrement.postedDate.isValid() ? itiDecrement.postedDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServerMulti(tiDecrements: TIDecrement[]) {
        for (let i = 0; i < tiDecrements.length; i++) {
            tiDecrements[i].date =
                tiDecrements[i].date != null
                    ? tiDecrements[i].date instanceof moment
                        ? tiDecrements[i].date.format(DATE_FORMAT)
                        : moment(tiDecrements[i].date).format(DATE_FORMAT)
                    : null;
        }
        return tiDecrements;
    }

    multiUnRecord(obj: IGOtherVoucher[]): Observable<HttpResponse<any>> {
        const options = this.convertDateFromServerMulti(obj);
        return this.http.post<any>(`${this.resourceUrl}/multi-un-record`, options, { observe: 'response' });
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        // res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((itiDecrement: ITIDecrement) => {
            itiDecrement.date = itiDecrement.date != null ? moment(itiDecrement.date) : null;
            // gOtherVoucher.postedDate = gOtherVoucher.postedDate != null ? moment(gOtherVoucher.postedDate) : null;
        });
        return res;
    }

    // searchAll(req?: any): Observable<EntityArrayResponseType> {
    //     const options = createRequestOption(req);
    //     return this.http
    //         .get<IGOtherVoucher[]>(`${this.resourceUrl}/search-all`, {
    //             params: options,
    //             observe: 'response'
    //         })
    //         .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    // }

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
            .get<ITIDecrement>(`${this.resourceUrl}/findByRowNum`, { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }
    findByRowNumPB(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/find-by-row-num-allocations`, { params: options, observe: 'response' });
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

    private convertDateFromPBClient(ref: any) {
        const copy: IGOtherVoucher = Object.assign({}, ref.data, {
            // gOtherVoucher: this.convertDateFromClient(ref.data.gOtherVoucher),
            // gOtherVoucherDetailExpenses: ref.data.gOtherVoucherDetailExpenses,
            // gOtherVoucherDetailExpenseAllocations: ref.data.gOtherVoucherDetailExpenseAllocations,
            // gOtherVoucherDetails: ref.data.gOtherVoucherDetails
        });
        return copy;
    }

    searchAll(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http
            .get<any[]>(`${this.resourceUrl}/search-all-ti-allocation`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    exportPDFPB(req?: any): Observable<any> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceUrl + '/export-pb/pdf', { params: options, observe: 'response', headers, responseType: 'blob' });
    }

    exportExcelPB(req?: any): Observable<any> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceUrl + '/export-pb/excel', {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    findRowNumPBByID(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/row-num`, { params: options, observe: 'response' });
    }

    getPrepaidExpenseAllocationCount(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(this.resourceUrl + '/prepaid-expense-allocation-count', { params: options, observe: 'response' });
    }

    getMaxMonth(id: any) {
        // const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/get-max-month/${id}`, { observe: 'response' });
    }

    getDetails(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/get-allocation-details', { params: options, observe: 'response' });
    }

    loadAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/load-all', {
            params: options,
            observe: 'response'
        });
    }
}
