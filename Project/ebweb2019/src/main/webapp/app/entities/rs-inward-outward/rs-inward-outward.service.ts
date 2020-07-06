import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRSInwardOutward, RSInwardOutward } from 'app/shared/model/rs-inward-outward.model';
import { IPporder, Pporder } from 'app/shared/model/pporder.model';
import { RSInwardOutWardDetails } from 'app/shared/model/rs-inward-out-ward-details.model';
import { IRSOutWardDTO } from 'app/shared/modal/rs-outward/rs-outward-dto.model';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';

type EntityResponseType = HttpResponse<IRSInwardOutward>;
type EntityArrayResponseType = HttpResponse<IRSInwardOutward[]>;

@Injectable({ providedIn: 'root' })
export class RSInwardOutwardService {
    private resourceUrl = SERVER_API_URL + 'api/rs-inward-outwards';

    constructor(private http: HttpClient) {}

    create(data: any): Observable<EntityResponseType> {
        data.rsInwardOutward = this.convertDateFromClient(data.rsInwardOutward);
        return this.http
            .post<IRSInwardOutward>(this.resourceUrl, data, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(data: any): Observable<EntityResponseType> {
        data.rsInwardOutward = this.convertDateFromClient(data.rsInwardOutward);
        return this.http
            .put<IRSInwardOutward>(this.resourceUrl, data, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    getRefVouchersByRSInwardOutwardID(id: number): Observable<EntityResponseType> {
        return this.http.get<any>(`${this.resourceUrl}/ref-voucher/${id}`, { observe: 'response' });
    }

    find(id: any): Observable<EntityResponseType> {
        return this.http
            .get<IRSInwardOutward>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRSInwardOutward[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(rSInwardOutward: RSInwardOutward): RSInwardOutward {
        const copy: RSInwardOutward = Object.assign({}, rSInwardOutward, {
            postedDate:
                rSInwardOutward.postedDate != null && rSInwardOutward.postedDate.isValid()
                    ? rSInwardOutward.postedDate.format(DATE_FORMAT)
                    : null,
            date: rSInwardOutward.date != null && rSInwardOutward.date.isValid() ? rSInwardOutward.date.format(DATE_FORMAT) : null
        });
        if (copy.rsInwardOutwardDetails && copy.rsInwardOutwardDetails.length > 0) {
            for (let i = 0; i < copy.rsInwardOutwardDetails.length; i++) {
                copy.rsInwardOutwardDetails[i] = Object.assign({}, copy.rsInwardOutwardDetails[i], {
                    expiryDate:
                        copy.rsInwardOutwardDetails[i].expiryDate != null && copy.rsInwardOutwardDetails[i].expiryDate.isValid()
                            ? copy.rsInwardOutwardDetails[i].expiryDate.format(DATE_FORMAT)
                            : null
                });
            }
        }
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        if (res.body.rsInwardOutwardDetails && res.body.rsInwardOutwardDetails.length > 0) {
            res.body.rsInwardOutwardDetails.forEach((item: RSInwardOutWardDetails) => {
                item.expiryDate = item.expiryDate != null ? moment(item.expiryDate) : null;
            });
            res.body.rsInwardOutwardDetails.sort((a, b) => a.orderPriority - b.orderPriority);
        }
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((rSInwardOutward: IRSInwardOutward) => {
            rSInwardOutward.postedDate = rSInwardOutward.postedDate != null ? moment(rSInwardOutward.postedDate) : null;
            rSInwardOutward.date = rSInwardOutward.date != null ? moment(rSInwardOutward.date) : null;
            if (rSInwardOutward.rsInwardOutwardDetails && rSInwardOutward.rsInwardOutwardDetails.length > 0) {
                rSInwardOutward.rsInwardOutwardDetails.forEach((item: RSInwardOutWardDetails) => {
                    item.expiryDate = item.expiryDate != null ? moment(item.expiryDate) : null;
                });
                rSInwardOutward.rsInwardOutwardDetails.sort((a, b) => a.orderPriority - b.orderPriority);
            }
        });
        return res;
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<RSInwardOutward[]>(`${this.resourceUrl}/search-all`, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    searchAllOutWard(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<RSInwardOutward[]>(`${this.resourceUrl}/search-all-out-ward`, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getRefVouchersByPPOrderID(id: string): Observable<EntityResponseType> {
        return this.http.get<any>(`api/pporders/ref-voucher/${id}`, { observe: 'response' });
    }

    getDetailsById(id: string, typeID: number): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/in-ward-details/${id}/${typeID}`, { observe: 'response' });
    }

    getDetailsOutWardById(id: string, typeID: number): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/out-ward-details/${id}/${typeID}`, { observe: 'response' });
    }

    getReferenceIDByRSInwardID(id: string, typeID: number): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/reference-table/${id}/${typeID}`, { observe: 'response' });
    }

    findReferenceTablesID(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http
            .get<Pporder>(`${this.resourceUrl}/index/reference-tables`, { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findReferenceTableID(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http
            .get<any>(`${this.resourceUrl}/index/reference-table`, { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findRowNumByID(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/row-num`, { params: options, observe: 'response' });
    }

    findRowNumOutWardByID(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/row-num-out-ward`, { params: options, observe: 'response' });
    }

    export(type: 'excel' | 'pdf', req?: any): Observable<HttpResponse<any>> {
        // req.fromDate = req.fromDate && req.fromDate.isValid() ? req.fromDate.format(DATE_FORMAT) : '';
        // req.toDate = req.toDate && req.toDate.isValid() ? req.toDate.format(DATE_FORMAT) : '';
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(`${this.resourceUrl}/export/${type}`, { params: options, observe: 'response', headers, responseType: 'blob' });
    }

    exportOutWard(type: 'excel' | 'pdf', req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(`${this.resourceUrl}/exportOutWard/${type}`, {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    getViewRSOutwardDTO(req?: any): Observable<HttpResponse<IRSOutWardDTO[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<IRSOutWardDTO[]>(`${this.resourceUrl}/ViewRSOutWardDTO`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: HttpResponse<IRSOutWardDTO[]>) => this.convertDateArrayFromServer_View(res)));
    }

    private convertDateArrayFromServer_View(res: HttpResponse<IRSOutWardDTO[]>): HttpResponse<IRSOutWardDTO[]> {
        res.body.forEach((sAOrder: IRSOutWardDTO) => {
            sAOrder.date = sAOrder.date != null ? moment(sAOrder.date) : null;
        });
        return res;
    }

    updateUnRecordDetails(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/detail/un-record`, {
            params: options,
            observe: 'response'
        });
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

    private convertDateFromClientArr(irsInwardOutwards: IRSInwardOutward[]): IRSInwardOutward[] {
        const rsArr = [];
        irsInwardOutwards.forEach(n => {
            const copy: IRSInwardOutward = Object.assign({}, n, {
                date: n.date != null && n.date.isValid() ? n.date.format(DATE_FORMAT) : null,
                postedDate: n.postedDate != null && n.postedDate.isValid() ? n.postedDate.format(DATE_FORMAT) : null
            });
            rsArr.push(copy);
        });
        return rsArr;
    }

    private convertDateArrayFromServerForMultiAction(res: any): EntityArrayResponseType {
        res.body.listFail.forEach((xuLyChungTuModel: ViewVoucherNo) => {
            xuLyChungTuModel.date = xuLyChungTuModel.date != null ? moment(xuLyChungTuModel.date) : null;
            xuLyChungTuModel.postedDate = xuLyChungTuModel.postedDate != null ? moment(xuLyChungTuModel.postedDate) : null;
        });
        return res;
    }

    multiDelete(obj: IRSInwardOutward[]): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClientArr(obj);
        return this.http
            .post<any>(`${this.resourceUrl}/multi-delete-rsInwardOutward`, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServerForMultiAction(res)));
    }

    multiUnRecord(obj: IRSInwardOutward[]): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClientArr(obj);
        return this.http
            .post<any>(`${this.resourceUrl}/multi-unrecord-rs-inward-outward-returns`, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServerForMultiAction(res)));
    }
}
