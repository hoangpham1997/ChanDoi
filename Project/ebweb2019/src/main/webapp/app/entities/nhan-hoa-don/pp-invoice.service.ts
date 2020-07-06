import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPPInvoice } from 'app/shared/model/pp-invoice.model';

type EntityResponseType = HttpResponse<IPPInvoice>;
type EntityArrayResponseType = HttpResponse<IPPInvoice[]>;

@Injectable({ providedIn: 'root' })
export class PPInvoiceService {
    private resourceUrl = SERVER_API_URL + 'api/pp-invoices';

    constructor(private http: HttpClient) {}

    create(pPInvoice: IPPInvoice): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(pPInvoice);
        return this.http
            .post<IPPInvoice>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(pPInvoice: IPPInvoice): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(pPInvoice);
        return this.http
            .put<IPPInvoice>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPPInvoice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPPInvoice[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(pPInvoice: IPPInvoice): IPPInvoice {
        const copy: IPPInvoice = Object.assign({}, pPInvoice, {
            date: pPInvoice.date != null && pPInvoice.date.isValid() ? pPInvoice.date.format(DATE_FORMAT) : null,
            postedDate: pPInvoice.postedDate != null && pPInvoice.postedDate.isValid() ? pPInvoice.postedDate.format(DATE_FORMAT) : null,
            dueDate: pPInvoice.dueDate != null && pPInvoice.dueDate.isValid() ? pPInvoice.dueDate.format(DATE_FORMAT) : null,
            matchDate: pPInvoice.matchDate != null && pPInvoice.matchDate.isValid() ? pPInvoice.matchDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        res.body.dueDate = res.body.dueDate != null ? moment(res.body.dueDate) : null;
        res.body.matchDate = res.body.matchDate != null ? moment(res.body.matchDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((pPInvoice: IPPInvoice) => {
            pPInvoice.date = pPInvoice.date != null ? moment(pPInvoice.date) : null;
            pPInvoice.postedDate = pPInvoice.postedDate != null ? moment(pPInvoice.postedDate) : null;
            pPInvoice.dueDate = pPInvoice.dueDate != null ? moment(pPInvoice.dueDate) : null;
            pPInvoice.matchDate = pPInvoice.matchDate != null ? moment(pPInvoice.matchDate) : null;
        });
        return res;
    }
}
