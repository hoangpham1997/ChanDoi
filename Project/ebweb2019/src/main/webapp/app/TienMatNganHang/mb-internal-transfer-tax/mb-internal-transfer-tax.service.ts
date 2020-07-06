import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMBInternalTransferTax } from 'app/shared/model/mb-internal-transfer-tax.model';

type EntityResponseType = HttpResponse<IMBInternalTransferTax>;
type EntityArrayResponseType = HttpResponse<IMBInternalTransferTax[]>;

@Injectable({ providedIn: 'root' })
export class MBInternalTransferTaxService {
    private resourceUrl = SERVER_API_URL + 'api/mb-internal-transfer-taxes';

    constructor(private http: HttpClient) {}

    create(mBInternalTransferTax: IMBInternalTransferTax): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mBInternalTransferTax);
        return this.http
            .post<IMBInternalTransferTax>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(mBInternalTransferTax: IMBInternalTransferTax): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mBInternalTransferTax);
        return this.http
            .put<IMBInternalTransferTax>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IMBInternalTransferTax>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMBInternalTransferTax[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(mBInternalTransferTax: IMBInternalTransferTax): IMBInternalTransferTax {
        const copy: IMBInternalTransferTax = Object.assign({}, mBInternalTransferTax, {
            invoiceDate:
                mBInternalTransferTax.invoiceDate != null && mBInternalTransferTax.invoiceDate.isValid()
                    ? mBInternalTransferTax.invoiceDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.invoiceDate = res.body.invoiceDate != null ? moment(res.body.invoiceDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((mBInternalTransferTax: IMBInternalTransferTax) => {
            mBInternalTransferTax.invoiceDate =
                mBInternalTransferTax.invoiceDate != null ? moment(mBInternalTransferTax.invoiceDate) : null;
        });
        return res;
    }
}
