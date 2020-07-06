import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { Pporder } from 'app/shared/model/pporder.model';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { IRSTransfer, RSTransfer } from 'app/shared/model/rs-transfer.model';
import { RSTransferDetails } from 'app/shared/model/rs-transfer-details.model';

type EntityResponseType = HttpResponse<IRSTransfer>;
type EntityArrayResponseType = HttpResponse<IRSTransfer[]>;

@Injectable({ providedIn: 'root' })
export class RsTransferService {
    private resourceUrl = SERVER_API_URL + 'api/rs-tranfers';

    constructor(private http: HttpClient) {}

    create(data: any): Observable<EntityResponseType> {
        data.rsTransfer = this.convertDateFromClient(data.rsTransfer);
        return this.http
            .post<IRSTransfer>(this.resourceUrl, data, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(data: any): Observable<EntityResponseType> {
        data.rsTransfer = this.convertDateFromClient(data.rsTransfer);
        return this.http
            .put<IRSTransfer>(this.resourceUrl, data, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: any): Observable<EntityResponseType> {
        return this.http
            .get<IRSTransfer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRSTransfer[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(rSTransfer: RSTransfer): RSTransfer {
        const copy: RSTransfer = Object.assign({}, rSTransfer, {
            postedDate: rSTransfer.postedDate != null && rSTransfer.postedDate.isValid() ? rSTransfer.postedDate.format(DATE_FORMAT) : null,
            date: rSTransfer.date != null && rSTransfer.date.isValid() ? rSTransfer.date.format(DATE_FORMAT) : null,
            mobilizationOrderDate:
                rSTransfer.mobilizationOrderDate != null && rSTransfer.mobilizationOrderDate.isValid()
                    ? rSTransfer.mobilizationOrderDate.format(DATE_FORMAT)
                    : null
        });
        if (copy.rsTransferDetails && copy.rsTransferDetails.length > 0) {
            for (let i = 0; i < copy.rsTransferDetails.length; i++) {
                copy.rsTransferDetails[i] = Object.assign({}, copy.rsTransferDetails[i], {
                    expiryDate:
                        copy.rsTransferDetails[i].expiryDate != null && copy.rsTransferDetails[i].expiryDate.isValid()
                            ? copy.rsTransferDetails[i].expiryDate.format(DATE_FORMAT)
                            : null
                });
            }
        }
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.mobilizationOrderDate = res.body.mobilizationOrderDate != null ? moment(res.body.mobilizationOrderDate) : null;
        if (res.body.rsTransferDetails && res.body.rsTransferDetails.length > 0) {
            res.body.rsTransferDetails.forEach((item: RSTransferDetails) => {
                item.expiryDate = item.expiryDate != null ? moment(item.expiryDate) : null;
            });
            res.body.rsTransferDetails.sort((a, b) => a.orderPriority - b.orderPriority);
        }
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((rSTransfer: IRSTransfer) => {
            rSTransfer.postedDate = rSTransfer.postedDate != null ? moment(rSTransfer.postedDate) : null;
            rSTransfer.date = rSTransfer.date != null ? moment(rSTransfer.date) : null;
            if (rSTransfer.rsTransferDetails && rSTransfer.rsTransferDetails.length > 0) {
                rSTransfer.rsTransferDetails.forEach((item: RSTransferDetails) => {
                    item.expiryDate = item.expiryDate != null ? moment(item.expiryDate) : null;
                });
                rSTransfer.rsTransferDetails.sort((a, b) => a.orderPriority - b.orderPriority);
            }
        });
        return res;
    }

    searchAllTransfer(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<RSTransfer[]>(`${this.resourceUrl}/search-all-transfer`, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<RSTransfer[]>(`${this.resourceUrl}/search-transfer`, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getRefVouchersByPPOrderID(id: string): Observable<EntityResponseType> {
        return this.http.get<any>(`api/pporders/ref-voucher/${id}`, { observe: 'response' });
    }

    getDetailsTransferById(id: string, typeID: number): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/transfer-details/${id}/${typeID}`, { observe: 'response' });
    }

    findReferenceTablesID(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http
            .get<Pporder>(`${this.resourceUrl}/index/reference-tables`, { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findRowNumOutWardByID(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/row-num`, { params: options, observe: 'response' });
    }

    exportTranfer(type: 'excel' | 'pdf', req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(`${this.resourceUrl}/export/${type}`, {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    // getViewRSOutwardDTO(req?: any): Observable<HttpResponse<IRSOutWardDTO[]>> {
    //     const options = createRequestOption(req);
    //     return this.http
    //         .get<IRSOutWardDTO[]>(`${this.resourceUrl}/ViewRSOutWardDTO`, {
    //             params: options,
    //             observe: 'response'
    //         })
    //         .pipe(map((res: HttpResponse<IRSOutWardDTO[]>) => this.convertDateArrayFromServer_View(res)));
    // }
    //
    // private convertDateArrayFromServer_View(res: HttpResponse<IRSOutWardDTO[]>): HttpResponse<IRSOutWardDTO[]> {
    //     res.body.forEach((sAOrder: IRSOutWardDTO) => {
    //         sAOrder.date = sAOrder.date != null ? moment(sAOrder.date) : null;
    //     });
    //     return res;
    // }

    // updateUnRecordDetails(req?: any): Observable<HttpResponse<any>> {
    //     const options = createRequestOption(req);
    //     return this.http.get<any>(`${this.resourceUrl}/detail/un-record`, {
    //         params: options,
    //         observe: 'response'
    //     });
    // }

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

    private convertDateFromClientArr(irsTransfers: IRSTransfer[]): IRSTransfer[] {
        const rsArr = [];
        irsTransfers.forEach(n => {
            const copy: IRSTransfer = Object.assign({}, n, {
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

    multiDelete(obj: IRSTransfer[]): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClientArr(obj);
        return this.http
            .post<any>(`${this.resourceUrl}/multi-delete-rsTransfer`, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServerForMultiAction(res)));
    }

    multiUnRecord(obj: IRSTransfer[]): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClientArr(obj);
        return this.http
            .post<any>(`${this.resourceUrl}/multi-unrecord-rs-transfer`, copy, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServerForMultiAction(res)));
    }
}
