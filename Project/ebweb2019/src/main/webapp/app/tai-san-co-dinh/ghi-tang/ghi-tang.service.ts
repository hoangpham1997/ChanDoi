import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMCReceipt } from 'app/shared/model/mc-receipt.model';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { FAIncrement } from 'app/shared/model/fa-increment.model';

type EntityResponseType = HttpResponse<FAIncrement>;
type EntityArrayResponseType = HttpResponse<FAIncrement[]>;

@Injectable({ providedIn: 'root' })
export class GhiTangService {
    private resourceUrl = SERVER_API_URL + 'api/fa-increments';
    // private resourceUrlDTO = SERVER_API_URL + 'api/m-c-receiptsDTO';

    constructor(private http: HttpClient) {}

    create(faIncrement: FAIncrement): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(faIncrement);
        return this.http
            .post<FAIncrement>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateFromServer(res)));
    }

    update(faIncrement: FAIncrement): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(faIncrement);
        return this.http
            .put<FAIncrement>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<FAIncrement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findDetailsByID(id: string): Observable<HttpResponse<any>> {
        return this.http
            .get<any>(`${this.resourceUrl}/details/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    getDataRefVouchers(id: string): Observable<HttpResponse<any>> {
        return this.http
            .get<any>(`${this.resourceUrl}/data_ref_vouchers/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMCReceipt[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    loadAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/load-all', {
            params: options,
            observe: 'response'
        });
    }

    findByRowNum(req?: any): Observable<HttpResponse<FAIncrement>> {
        const options = createRequestOption(req);
        return this.http.get<any>(this.resourceUrl + '/find-by-row-num', {
            params: options,
            observe: 'response'
        });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(mCReceipt: IMCReceipt): IMCReceipt {
        const copy: IMCReceipt = Object.assign({}, mCReceipt, {
            date: mCReceipt.date != null && mCReceipt.date.isValid() ? mCReceipt.date.format(DATE_FORMAT) : null
            // postedDate: mCReceipt.postedDate != null && mCReceipt.postedDate.isValid() ? mCReceipt.postedDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromClientArr(mCReceipts: IMCReceipt[]): IMCReceipt[] {
        const mcArr = [];
        mCReceipts.forEach(n => {
            const copy: IMCReceipt = Object.assign({}, n, {
                // date: n.date != null ? n.date.format(DATE_FORMAT) : null,
            });
            mcArr.push(copy);
        });
        return mcArr;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        return res;
    }

    private convertDateFromServerDTO(res: any): any {
        res.body.mCReceipt.date = res.body.mCReceipt.date != null ? moment(res.body.mCReceipt.date) : null;
        res.body.mCReceipt.postedDate = res.body.mCReceipt.postedDate != null ? moment(res.body.mCReceipt.postedDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((mCReceipt: IMCReceipt) => {
            mCReceipt.date = mCReceipt.date != null ? moment(mCReceipt.date) : null;
            mCReceipt.postedDate = mCReceipt.postedDate != null ? moment(mCReceipt.postedDate) : null;
        });
        return res;
    }

    exportExcel(req?: any) {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(SERVER_API_URL + 'api/report/excel', {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMCReceipt[]>(`${this.resourceUrl}/search-all`, { params: options, observe: 'response' });
    }

    multiUnrecord(obj: FAIncrement[]): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClientArr(obj);
        return this.http
            .post<any>(`${this.resourceUrl}/multi-unrecord`, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServerForMultiAction(res)));
    }

    multiDelete(obj: FAIncrement[]): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClientArr(obj);
        return this.http
            .post<any>(`${this.resourceUrl}/multi-delete`, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServerForMultiAction(res)));
    }

    getTSCD(): Observable<HttpResponse<any>> {
        return this.http.get<any>(`api/fa-increments/get-tscd`, { observe: 'response' });
    }

    private convertDateArrayFromServerForMultiAction(res: any): EntityArrayResponseType {
        res.body.listFail.forEach((xuLyChungTuModel: ViewVoucherNo) => {
            xuLyChungTuModel.date = xuLyChungTuModel.date != null ? moment(xuLyChungTuModel.date) : null;
            xuLyChungTuModel.postedDate = xuLyChungTuModel.postedDate != null ? moment(xuLyChungTuModel.postedDate) : null;
        });
        return res;
    }

    export(type: 'excel' | 'pdf', req?: any): Observable<HttpResponse<any>> {
        req.fromDate = req.fromDate && req.fromDate.isValid() ? req.fromDate.format(DATE_FORMAT) : '';
        req.toDate = req.toDate && req.toDate.isValid() ? req.toDate.format(DATE_FORMAT) : '';
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(`${this.resourceUrl}/g-t/export/${type}`, {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }
}
