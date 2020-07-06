import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPporder, Pporder } from 'app/shared/model/pporder.model';
import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';

type EntityResponseType = HttpResponse<IPporder>;
type EntityArrayResponseType = HttpResponse<IPporder[]>;

@Injectable({ providedIn: 'root' })
export class PporderService {
    private resourceUrl = SERVER_API_URL + 'api/pporders';

    private _searchVoucher: any;

    get searchVoucher(): any {
        return this._searchVoucher;
    }

    set searchVoucher(value: any) {
        this._searchVoucher = value;
    }

    get moveVoucher(): any {
        return this._searchVoucher.rowIndex;
    }

    set moveVoucher(value: any) {
        this._searchVoucher.rowIndex += value;
    }

    constructor(private http: HttpClient) {}

    create(data: any): Observable<EntityResponseType> {
        data.ppOrder = this.convertDateFromClient(data.ppOrder);
        return this.http
            .post<IPporder>(this.resourceUrl, data, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(data: any): Observable<EntityResponseType> {
        data.ppOrder = this.convertDateFromClient(data.ppOrder);
        return this.http
            .put<IPporder>(this.resourceUrl, data, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findById(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IPporder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    getRefVouchersByPPOrderID(id: string): Observable<EntityResponseType> {
        return this.http.get<any>(`${this.resourceUrl}/ref-voucher/${id}`, { observe: 'response' });
    }

    findByRowNum(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<Pporder>(`${this.resourceUrl}/index`, { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPporder[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    deleteValidate(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}/validate`, { observe: 'response' });
    }

    deletePporderCheckDuplicat(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}/validate-check-duplicate`, { observe: 'response' });
    }

    checkReferencesCount(id: string): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/${id}/validate`, { observe: 'response' });
    }

    deleteReferences(id: string) {
        return this.http.delete<any>(`${this.resourceUrl}/${id}/delete-references`, { observe: 'response' });
    }

    private convertDateFromClient(pporder: IPporder): IPporder {
        const copy: IPporder = Object.assign({}, pporder, {
            date: pporder.date != null && pporder.date.isValid() ? pporder.date.format(DATE_FORMAT) : null,
            deliverDate: pporder.deliverDate != null && pporder.deliverDate.isValid() ? pporder.deliverDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.deliverDate = res.body.deliverDate != null ? moment(res.body.deliverDate) : null;
        if (res.body.ppOrderDetails) {
            res.body.ppOrderDetails.sort((a, b) => a.orderPriority - b.orderPriority);
        }
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((pporder: IPporder) => {
            pporder.date = pporder.date != null ? moment(pporder.date) : null;
            pporder.deliverDate = pporder.deliverDate != null ? moment(pporder.deliverDate) : null;
            if (pporder.ppOrderDetails) {
                pporder.ppOrderDetails.sort((a, b) => a.orderPriority - b.orderPriority);
            }
        });
        return res;
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPporder[]>(`${this.resourceUrl}/search-all`, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    findByRowNumByID(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/row-num`, { params: options, observe: 'response' });
    }

    export(type: 'excel' | 'pdf', req?: any): Observable<HttpResponse<any>> {
        req.fromDate = req.fromDate && req.fromDate.isValid() ? req.fromDate.format(DATE_FORMAT) : '';
        req.toDate = req.toDate && req.toDate.isValid() ? req.toDate.format(DATE_FORMAT) : '';
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(`${this.resourceUrl}/export/${type}`, { params: options, observe: 'response', headers, responseType: 'blob' });
    }

    deletePPOrderAndReferences(id: string) {
        return this.http.delete<any>(`${this.resourceUrl}/${id}/references`, { observe: 'response' });
    }

    findTotalSum(req?: any) {
        const options = createRequestOption(req);
        return this.http.get<number>(`${this.resourceUrl}/total-sum`, { params: options, observe: 'response' });
    }

    multiDelete(obj: IPporder[]): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceUrl}/multi-delete-pp-order`, obj, { observe: 'response' });
    }
}
