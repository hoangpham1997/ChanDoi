import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
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
        const copy = this.convertDateFromClient(iaPublishInvoice);
        return this.http
            .post<IIaPublishInvoice>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(iaPublishInvoice: IIaPublishInvoice): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(iaPublishInvoice);
        return this.http
            .put<IIaPublishInvoice>(this.resourceUrl, copy, { observe: 'response' })
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
            .get<IIaPublishInvoice[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
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
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((iaPublishInvoice: IIaPublishInvoice) => {
            iaPublishInvoice.date = iaPublishInvoice.date != null ? moment(iaPublishInvoice.date) : null;
        });
        return res;
    }
}
