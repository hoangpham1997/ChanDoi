import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IIARegisterInvoice } from 'app/shared/model/ia-register-invoice.model';
import { IPporder } from 'app/shared/model/pporder.model';

type EntityResponseType = HttpResponse<IIARegisterInvoice>;
type EntityArrayResponseType = HttpResponse<IIARegisterInvoice[]>;

@Injectable({ providedIn: 'root' })
export class IARegisterInvoiceService {
    private resourceUrl = SERVER_API_URL + 'api/ia-register-invoices';

    constructor(private http: HttpClient) {}

    create(iARegisterInvoice: IIARegisterInvoice): Observable<EntityResponseType> {
        // const copy = this.convertDateFromClient(iARegisterInvoice);
        return this.http
            .post<IIARegisterInvoice>(this.resourceUrl, iARegisterInvoice, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(iARegisterInvoice: IIARegisterInvoice): Observable<EntityResponseType> {
        // const copy = this.convertDateFromClient(iARegisterInvoice);
        return this.http
            .put<IIARegisterInvoice>(this.resourceUrl, iARegisterInvoice, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IIARegisterInvoice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findOneByCompanyID(): Observable<HttpResponse<any>> {
        return this.http.get<any>(`api/organization-unit-option-reports/company-id`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IIARegisterInvoice[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(iARegisterInvoice: IIARegisterInvoice): IIARegisterInvoice {
        const copy: IIARegisterInvoice = Object.assign({}, iARegisterInvoice, {
            date: iARegisterInvoice.date != null && iARegisterInvoice.date.isValid() ? iARegisterInvoice.date.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((iARegisterInvoice: IIARegisterInvoice) => {
            iARegisterInvoice.date = iARegisterInvoice.date != null ? moment(iARegisterInvoice.date) : null;
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

    downloadAttachFile(id: string) {
        return this.http.get(`${this.resourceUrl}/${id}/attach-file`, {
            observe: 'response',
            responseType: 'blob'
        });
    }

    multiDelete(obj: IIARegisterInvoice[]): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceUrl}/multi-delete-ia-register-invoice`, obj, { observe: 'response' });
    }
}
