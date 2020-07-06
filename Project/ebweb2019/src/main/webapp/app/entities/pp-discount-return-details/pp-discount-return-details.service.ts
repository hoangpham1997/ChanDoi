import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPPDiscountReturnDetails, PPDiscountReturnDetails } from 'app/shared/model/pp-discount-return-details.model';
import { SAOrderDetails } from 'app/shared/model/sa-order-details.model';

type EntityResponseType = HttpResponse<IPPDiscountReturnDetails>;
type EntityArrayResponseType = HttpResponse<IPPDiscountReturnDetails[]>;

@Injectable({ providedIn: 'root' })
export class PPDiscountReturnDetailsService {
    private resourceUrl = SERVER_API_URL + 'api/pp-discount-return-details';

    constructor(private http: HttpClient) {}

    create(pPDiscountReturnDetails: IPPDiscountReturnDetails): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(pPDiscountReturnDetails);
        return this.http
            .post<IPPDiscountReturnDetails>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(pPDiscountReturnDetails: IPPDiscountReturnDetails): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(pPDiscountReturnDetails);
        return this.http
            .put<IPPDiscountReturnDetails>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPPDiscountReturnDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPPDiscountReturnDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(pPDiscountReturnDetails: IPPDiscountReturnDetails): IPPDiscountReturnDetails {
        const copy: IPPDiscountReturnDetails = Object.assign({}, pPDiscountReturnDetails, {
            expiryDate:
                pPDiscountReturnDetails.expiryDate != null && pPDiscountReturnDetails.expiryDate.isValid()
                    ? pPDiscountReturnDetails.expiryDate.format(DATE_FORMAT)
                    : null,
            matchDate:
                pPDiscountReturnDetails.matchDate != null && pPDiscountReturnDetails.matchDate.isValid()
                    ? pPDiscountReturnDetails.matchDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.expiryDate = res.body.expiryDate != null ? moment(res.body.expiryDate) : null;
        res.body.matchDate = res.body.matchDate != null ? moment(res.body.matchDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((pPDiscountReturnDetails: IPPDiscountReturnDetails) => {
            pPDiscountReturnDetails.expiryDate =
                pPDiscountReturnDetails.expiryDate != null ? moment(pPDiscountReturnDetails.expiryDate) : null;
            pPDiscountReturnDetails.matchDate =
                pPDiscountReturnDetails.matchDate != null ? moment(pPDiscountReturnDetails.matchDate) : null;
        });
        return res;
    }
    getDetailByID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/by-id', { params: options, observe: 'response' });
    }

    findAllDetailsById(req: any): Observable<HttpResponse<PPDiscountReturnDetails[]>> {
        const options = createRequestOption(req);
        return this.http.get<PPDiscountReturnDetails[]>(`${this.resourceUrl}/details`, { params: options, observe: 'response' });
    }
}
