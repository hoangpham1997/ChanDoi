import { Injectable } from '@angular/core';
import * as moment from 'moment';
import { Moment } from 'moment';
import { MuaDichVuSearch } from 'app/muahang/mua-dich-vu/model/mua-dich-vu-search.model';
import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { IMuaDichVuResult, MuaDichVuResult } from 'app/muahang/mua-dich-vu/model/mua-dich-vu-result.model';
import { createRequestOption } from 'app/shared';
import { IMuaDichVuDetailResult } from 'app/muahang/mua-dich-vu/model/mua-dich-vu-detail-result.model';
import { IRefVoucher } from 'app/shared/model/ref-voucher.model';
import { DataSessionStorage, IDataSessionStorage } from 'app/shared/model/DataSessionStorage';

type EntityArrayResponsePPServiceDetailType = HttpResponse<IMuaDichVuDetailResult[]>;

@Injectable({ providedIn: 'root' })
export class MuaDichVuService {
    private resourcePPServiceUrl = SERVER_API_URL + 'api/ppService';
    private resourcePPServiceDetailUrl = SERVER_API_URL + 'api/ppServiceDetail';
    private muaDichVuSearchSnapShot: MuaDichVuSearch;
    private isReadOnly: boolean;
    private statusSearchBar: boolean;
    private dataSession: IDataSessionStorage;
    private isFromOtherScene: boolean;
    private onClose: any;
    private dataResult: any;
    private isEdit: boolean;
    //
    private ppServiceList: MuaDichVuResult[];
    private ppServiceSelected: MuaDichVuResult;

    setEdit(value: boolean) {
        this.isEdit = value;
    }

    getEdit(): boolean {
        return this.isEdit;
    }

    setDataResult(value: any) {
        this.dataResult = value || {};
    }

    getDataResult(): any {
        return this.dataResult || {};
    }

    cleanService() {
        this.muaDichVuSearchSnapShot = new MuaDichVuSearch();
        this.isReadOnly = false;
        this.statusSearchBar = false;
        this.dataSession = new DataSessionStorage();
        this.isFromOtherScene = false;
        this.ppServiceList = [];
        this.ppServiceSelected = new MuaDichVuResult();
    }
    getDataSession(): IDataSessionStorage {
        return this.dataSession ? this.dataSession : new DataSessionStorage();
    }

    setDataSession(value: IDataSessionStorage) {
        this.dataSession = value;
    }
    getStatusSearchBar() {
        return this.statusSearchBar ? this.statusSearchBar : false;
    }

    setStatusSearchBar(value: boolean) {
        this.statusSearchBar = value;
    }

    getReadOnly(): boolean {
        return this.isReadOnly ? this.isReadOnly : false;
    }

    setReadOnly(value: boolean) {
        this.isReadOnly = value;
    }

    getIsFromOtherScene(): boolean {
        return this.isFromOtherScene ? this.isFromOtherScene : false;
    }

    setIsFromOtherScene(value: boolean) {
        this.isFromOtherScene = value;
    }

    getPPServiceList(): MuaDichVuResult[] {
        return this.ppServiceList ? this.ppServiceList : [];
    }

    setOnClose(value: any) {
        this.onClose = value;
    }

    getOnClose(): any {
        return this.onClose;
    }

    setPPServiceList(value: MuaDichVuResult[]) {
        this.ppServiceList = value;
    }

    getPPServiceSelected(): MuaDichVuResult {
        return this.ppServiceSelected ? this.ppServiceSelected : new MuaDichVuResult();
    }

    setPPServiceSelected(value: MuaDichVuResult) {
        this.ppServiceSelected = value;
    }

    constructor(private http: HttpClient) {}

    create(req: any): Observable<HttpResponse<any>> {
        return this.http.post<any>(this.resourcePPServiceUrl, req, { observe: 'response' });
    }
    /***
     *
     * @param req {noBookType:  0 - Sổ tài chính, 1 - Sổ quản trị}
     */
    findAllPPServices(req: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<MuaDichVuResult[]>(this.resourcePPServiceUrl + '/find-all', { params: options, observe: 'response' });
    }

    /**
     *
     * @param req {
     *     nextItem: action : 1, -1,
     *     ppServiceId: uuid
     * }
     */
    findPPServiceByLocationItem(req: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(this.resourcePPServiceUrl + '/find-by-location', { params: options, observe: 'response' });
    }
    /**
     *
     * @param req {
     *     ppServiceId: uuid
     * }
     */
    checkHadReference(req: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(this.resourcePPServiceUrl + '/had-reference', { params: options, observe: 'response' });
    }

    /***
     *
     * @param req:  {ppServiceId: UUID}
     */
    findAllPPServiceDetails(req: any): Observable<EntityArrayResponsePPServiceDetailType> {
        const options = createRequestOption(req);
        return this.http.get<IMuaDichVuDetailResult[]>(this.resourcePPServiceDetailUrl + '/find-by-id', {
            params: options,
            observe: 'response'
        });
    }

    /*
    * Add by Hautv
    * */
    findAllPPServiceDetailsByPaymentVoucherID(req: any): Observable<EntityArrayResponsePPServiceDetailType> {
        const options = createRequestOption(req);
        return this.http.get<IMuaDichVuDetailResult[]>(this.resourcePPServiceDetailUrl + '/find-by-paymentvoucherid', {
            params: options,
            observe: 'response'
        });
    }

    findRefVoucherByRefId(req: any): Observable<HttpResponse<IRefVoucher[]>> {
        return this.http.get<IRefVoucher[]>(SERVER_API_URL + 'api/view-vouchers/find-by-ref-id', {
            params: req,
            observe: 'response'
        });
    }

    findRefVoucherByByPaymentVoucherID(req: any): Observable<HttpResponse<IRefVoucher[]>> {
        return this.http.get<IRefVoucher[]>(SERVER_API_URL + 'api/view-vouchers/find-by-ref-paymentVoucherid', {
            params: req,
            observe: 'response'
        });
    }

    deletePPServiceById(req: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.delete<any>(this.resourcePPServiceUrl + '/delete-by-id', {
            params: options,
            observe: 'response'
        });
    }
    deletePPServiceInId(req: any): Observable<any> {
        return this.http.post<any>(`${this.resourcePPServiceUrl}/delete-in-id`, req, { observe: 'response' });
    }

    getMuaDichVuSearchSnapShot(): MuaDichVuSearch {
        return this.muaDichVuSearchSnapShot ? this.muaDichVuSearchSnapShot : new MuaDichVuSearch();
    }

    setMuaDichVuSearchSnapShot(muaDichVuSearch: MuaDichVuSearch) {
        this.muaDichVuSearchSnapShot = muaDichVuSearch;
    }

    getCurrentDate(): { year; month; day } {
        const _date = moment();
        return { year: _date.year(), month: _date.month() + 1, day: _date.date() };
    }

    getFromToMoment(date?: Moment, isMaxDate?: boolean): { year; month; day } {
        const _date = date && moment(date).isValid() ? date : isMaxDate ? null : moment();
        return _date ? { year: _date.year(), month: _date.month() + 1, day: _date.date() } : null;
    }

    export(req: any): Observable<HttpResponse<any>> {
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        const options = createRequestOption(req);
        return this.http.get(this.resourcePPServiceUrl + '/export/pdf', {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    exportExcel(req: any): Observable<any> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourcePPServiceUrl + '/export/excel', {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }
}
