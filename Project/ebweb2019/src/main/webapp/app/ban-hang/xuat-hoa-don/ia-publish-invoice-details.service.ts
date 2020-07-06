import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IIaPublishInvoiceDetails } from 'app/shared/model/ia-publish-invoice-details.model';

type EntityResponseType = HttpResponse<IIaPublishInvoiceDetails>;
type EntityArrayResponseType = HttpResponse<IIaPublishInvoiceDetails[]>;

@Injectable({ providedIn: 'root' })
export class IaPublishInvoiceDetailsService {
    private resourceUrl = SERVER_API_URL + 'api/ia-publish-invoice-details';

    constructor(private http: HttpClient) {}

    create(iaPublishInvoiceDetails: IIaPublishInvoiceDetails): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(iaPublishInvoiceDetails);
        return this.http
            .post<IIaPublishInvoiceDetails>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(iaPublishInvoiceDetails: IIaPublishInvoiceDetails): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(iaPublishInvoiceDetails);
        return this.http
            .put<IIaPublishInvoiceDetails>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IIaPublishInvoiceDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IIaPublishInvoiceDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAllByCompany(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IIaPublishInvoiceDetails[]>(`${this.resourceUrl}/get-by-company`, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getFollowTransferByCompany(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IIaPublishInvoiceDetails[]>(`${this.resourceUrl}/get-follow-transfer-by-company`, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(iaPublishInvoiceDetails: IIaPublishInvoiceDetails): IIaPublishInvoiceDetails {
        const copy: IIaPublishInvoiceDetails = Object.assign({}, iaPublishInvoiceDetails, {
            startUsing:
                iaPublishInvoiceDetails.startUsing != null && iaPublishInvoiceDetails.startUsing.isValid()
                    ? iaPublishInvoiceDetails.startUsing.format(DATE_FORMAT)
                    : null,
            contractDate:
                iaPublishInvoiceDetails.contractDate != null && iaPublishInvoiceDetails.contractDate.isValid()
                    ? iaPublishInvoiceDetails.contractDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.startUsing = res.body.startUsing != null ? moment(res.body.startUsing) : null;
        res.body.contractDate = res.body.contractDate != null ? moment(res.body.contractDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((iaPublishInvoiceDetails: IIaPublishInvoiceDetails) => {
            iaPublishInvoiceDetails.startUsing =
                iaPublishInvoiceDetails.startUsing != null ? moment(iaPublishInvoiceDetails.startUsing) : null;
            iaPublishInvoiceDetails.contractDate =
                iaPublishInvoiceDetails.contractDate != null ? moment(iaPublishInvoiceDetails.contractDate) : null;
        });
        return res;
    }
}
