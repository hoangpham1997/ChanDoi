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
import { ITIAllocation } from 'app/shared/model/ti-allocation.model';

const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
const EXCEL_EXTENSION = '.xlsx';

type EntityResponseType = HttpResponse<ITIAllocation>;
type EntityMutipleRecord = HttpResponse<IMultipleRecord>;
type EntityArrayResponseType = HttpResponse<ITIAllocation[]>;

@Injectable({ providedIn: 'root' })
export class PhanBoCcdcService {
    private resourceUrl = SERVER_API_URL + 'api/t-i-allocations';

    constructor(private http: HttpClient) {}

    create(gOtherVoucher: ITIAllocation): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClient(gOtherVoucher);
        return this.http
            .post<any>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(gOtherVoucher: ITIAllocation): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClient(gOtherVoucher);
        return this.http
            .put<any>(this.resourceUrl, copy, { observe: 'response' })
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
            .get<IGOtherVoucher>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }
    findDetailPB(id: any): Observable<HttpResponse<any>> {
        return this.http
            .get<any>(`${this.resourceUrl}/detail/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }
    findDetailViewPB(id: any): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/detail/view/${id}`, { observe: 'response' });
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

    private convertDateFromClient(gOtherVoucher: IGOtherVoucher): IGOtherVoucher {
        const copy: IGOtherVoucher = Object.assign({}, gOtherVoucher, {
            date: gOtherVoucher.date != null && gOtherVoucher.date.isValid() ? gOtherVoucher.date.format(DATE_FORMAT) : null,
            postedDate:
                gOtherVoucher.postedDate != null && gOtherVoucher.postedDate.isValid() ? gOtherVoucher.postedDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServerMulti(gOtherVoucher: any[]) {
        for (let i = 0; i < gOtherVoucher.length; i++) {
            gOtherVoucher[i].date = gOtherVoucher[i].date != null ? moment(gOtherVoucher[i].date.format(DATE_FORMAT)) : null;
            gOtherVoucher[i].postedDate =
                gOtherVoucher[i].postedDate != null ? moment(gOtherVoucher[i].postedDate.format(DATE_FORMAT)) : null;
        }
        return gOtherVoucher;
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
    convertDateFromClientToServerTools(res: any): any {
        res.date = res.date != null ? moment(res.date.format(DATE_FORMAT)) : null;
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
            .get<IGOtherVoucher>(`${this.resourceUrl}/findByRowNum`, { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }
    findByRowNumPB(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/find-by-row-num-allocations`, { params: options, observe: 'response' });
    }

    countToolsAllocation(req?: any): Observable<HttpResponse<any>> {
        const options = this.convertDateFromClientToServerTools(req);
        return this.http.get<any>(`${this.resourceUrl}/count-Tools-allocation`, { params: options, observe: 'response' });
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

    getTIAllocationCount(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(this.resourceUrl + '/ti-allocation-count', { params: options, observe: 'response' });
    }

    getMaxMonth(id: any) {
        // const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/get-max-month/${id}`, { observe: 'response' });
    }

    getTIAllocations(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/get-allocation-details', { params: options, observe: 'response' });
    }

    getTIAllocationDuplicate(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(this.resourceUrl + '/ti-allocation-duplicate', { params: options, observe: 'response' });
    }

    loadAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/load-all', {
            params: options,
            observe: 'response'
        });
    }
}
