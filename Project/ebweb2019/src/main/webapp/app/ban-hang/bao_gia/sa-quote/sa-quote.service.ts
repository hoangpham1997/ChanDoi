import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISAQuote } from 'app/shared/model/sa-quote.model';
import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';
import { IViewSAQuoteDTO } from 'app/shared/model/view-sa-quote.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';

type EntityResponseType = HttpResponse<ISAQuote>;
type EntityArrayResponseType = HttpResponse<ISAQuote[]>;

@Injectable({ providedIn: 'root' })
export class SAQuoteService {
    private resourceUrl = SERVER_API_URL + 'api/sa-quotes';

    constructor(private http: HttpClient) {}

    create(sAQuote: ISAQuote): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sAQuote);
        return this.http
            .post<ISAQuote>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(sAQuote: ISAQuote): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sAQuote);
        return this.http
            .put<ISAQuote>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<ISAQuote>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    deleteByListID(req?: any[]): Observable<HttpResponse<HandlingResult>> {
        return this.http.post<any>(`${this.resourceUrl}/delete-list`, req, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISAQuote[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISAQuote[]>(`${this.resourceUrl}/search-all`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAllData(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISAQuote[]>(`${this.resourceUrl}/get-all-data`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    findByRowNum(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISAQuote>(`${this.resourceUrl}/findByRowNum`, { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    getIndexRow(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/getIndexRow`, { params: options, observe: 'response' });
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

    getViewSAQuoteDTO(req?: any): Observable<HttpResponse<IViewSAQuoteDTO[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<IViewSAQuoteDTO[]>(`${this.resourceUrl}/ViewSAQuoteDTO`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: HttpResponse<IViewSAQuoteDTO[]>) => this.convertDateArrayFromServer_View(res)));
    }

    exportExcel(req?: any): Observable<any> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceUrl + '/export/excel', { params: options, observe: 'response', headers, responseType: 'blob' });
    }

    export(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceUrl + '/export/pdf', { params: options, observe: 'response', headers, responseType: 'blob' });
    }

    private convertDateFromClient(sAQuote: ISAQuote): ISAQuote {
        const copy: ISAQuote = Object.assign({}, sAQuote, {
            date: sAQuote.date != null && sAQuote.date.isValid() ? sAQuote.date.format(DATE_FORMAT) : null,
            finalDate: sAQuote.finalDate != null && sAQuote.finalDate.isValid() ? sAQuote.finalDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.finalDate = res.body.finalDate != null ? moment(res.body.finalDate) : null;
        return res;
    }

    private convertDateArrayFromServer_View(res: HttpResponse<IViewSAQuoteDTO[]>): HttpResponse<IViewSAQuoteDTO[]> {
        res.body.forEach((sAQuote: IViewSAQuoteDTO) => {
            sAQuote.date = sAQuote.date != null ? moment(sAQuote.date) : null;
            // sAQuote.finalDate = sAQuote.finalDate != null ? moment(sAQuote.finalDate) : null;
        });
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((sAQuote: ISAQuote) => {
            sAQuote.date = sAQuote.date != null ? moment(sAQuote.date) : null;
            sAQuote.finalDate = sAQuote.finalDate != null ? moment(sAQuote.finalDate) : null;
        });
        return res;
    }

    checkRelateVoucher(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/check-relate-voucher`, { params: options, observe: 'response' });
    }

    deleteRef(id: string) {
        return this.http.post<any>(this.resourceUrl + '/delete-ref/' + id, { observe: 'response' });
    }
}
