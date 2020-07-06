import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMBTellerPaperDetailTax } from 'app/shared/model/mb-teller-paper-detail-tax.model';

type EntityResponseType = HttpResponse<IMBTellerPaperDetailTax>;
type EntityArrayResponseType = HttpResponse<IMBTellerPaperDetailTax[]>;

@Injectable({ providedIn: 'root' })
export class MBTellerPaperDetailTaxService {
    private resourceUrl = SERVER_API_URL + 'api/mb-teller-paper-detail-taxes';

    constructor(private http: HttpClient) {}

    create(mBTellerPaperDetailTax: IMBTellerPaperDetailTax): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mBTellerPaperDetailTax);
        return this.http
            .post<IMBTellerPaperDetailTax>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(mBTellerPaperDetailTax: IMBTellerPaperDetailTax): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mBTellerPaperDetailTax);
        return this.http
            .put<IMBTellerPaperDetailTax>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IMBTellerPaperDetailTax>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMBTellerPaperDetailTax[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(mBTellerPaperDetailTax: IMBTellerPaperDetailTax): IMBTellerPaperDetailTax {
        const copy: IMBTellerPaperDetailTax = Object.assign({}, mBTellerPaperDetailTax, {
            invoicedate:
                mBTellerPaperDetailTax.invoiceDate != null && mBTellerPaperDetailTax.invoiceDate.isValid()
                    ? mBTellerPaperDetailTax.invoiceDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.invoiceDate = res.body.invoiceDate != null ? moment(res.body.invoiceDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((mBTellerPaperDetailTax: IMBTellerPaperDetailTax) => {
            mBTellerPaperDetailTax.invoiceDate =
                mBTellerPaperDetailTax.invoiceDate != null ? moment(mBTellerPaperDetailTax.invoiceDate) : null;
        });
        return res;
    }
}
