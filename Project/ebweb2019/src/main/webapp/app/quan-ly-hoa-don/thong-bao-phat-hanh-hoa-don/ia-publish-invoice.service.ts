import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IIaPublishInvoice } from 'app/shared/model/ia-publish-invoice.model';

type EntityResponseType = HttpResponse<IIaPublishInvoice>;
type EntityArrayResponseType = HttpResponse<IIaPublishInvoice[]>;

@Injectable({ providedIn: 'root' })
export class IaPublishInvoiceService {
    private resourceUrl = SERVER_API_URL + 'api/ia-publish-invoices';

    constructor(private http: HttpClient) {}

    create(iaPublishInvoice: IIaPublishInvoice): Observable<EntityResponseType> {
        return this.http
            .post<IIaPublishInvoice>(this.resourceUrl, iaPublishInvoice, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(iaPublishInvoice: IIaPublishInvoice): Observable<EntityResponseType> {
        return this.http
            .put<IIaPublishInvoice>(this.resourceUrl, iaPublishInvoice, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IIaPublishInvoice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IIaPublishInvoice[]>(this.resourceUrl + '/pageable', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(iaPublishInvoice: IIaPublishInvoice): IIaPublishInvoice {
        const copy: IIaPublishInvoice = Object.assign({}, iaPublishInvoice, {
            date: iaPublishInvoice.date != null && iaPublishInvoice.date.isValid() ? iaPublishInvoice.date.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        if (res.body.iaPublishInvoiceDetails) {
            res.body.iaPublishInvoiceDetails = res.body.iaPublishInvoiceDetails.map(item => {
                item.startUsingHolder = item.startUsing != null ? moment(item.startUsing) : null;
                item.contractDateHolder = item.contractDate != null ? moment(item.contractDate) : null;
                return item;
            });
        }
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((iaPublishInvoice: IIaPublishInvoice) => {
            iaPublishInvoice.date = iaPublishInvoice.date != null ? moment(iaPublishInvoice.date) : null;
        });
        return res;
    }

    export(type: 'excel' | 'pdf') {
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(`${this.resourceUrl}/export/${type}`, {
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    findCurrentMaxFromNo(iaReportID: string): Observable<HttpResponse<any>> {
        return this.http.get<number>(`${this.resourceUrl}/max-from-no/${iaReportID}`, { observe: 'response' });
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
}
