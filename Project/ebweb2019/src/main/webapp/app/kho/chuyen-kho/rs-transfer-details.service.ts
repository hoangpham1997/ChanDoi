import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRSInwardOutWardDetails } from 'app/shared/model/rs-inward-out-ward-details.model';

type EntityResponseType = HttpResponse<IRSInwardOutWardDetails>;
type EntityArrayResponseType = HttpResponse<IRSInwardOutWardDetails[]>;

@Injectable({ providedIn: 'root' })
export class RSInwardOutWardDetailsService {
    private resourceUrl = SERVER_API_URL + 'api/rs-inward-out-ward-details';

    constructor(private http: HttpClient) {}

    create(rSInwardOutWardDetails: IRSInwardOutWardDetails): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(rSInwardOutWardDetails);
        return this.http
            .post<IRSInwardOutWardDetails>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(rSInwardOutWardDetails: IRSInwardOutWardDetails): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(rSInwardOutWardDetails);
        return this.http
            .put<IRSInwardOutWardDetails>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IRSInwardOutWardDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRSInwardOutWardDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(rSInwardOutWardDetails: IRSInwardOutWardDetails): IRSInwardOutWardDetails {
        const copy: IRSInwardOutWardDetails = Object.assign({}, rSInwardOutWardDetails, {
            expiryDate:
                rSInwardOutWardDetails.expiryDate != null && rSInwardOutWardDetails.expiryDate.isValid()
                    ? rSInwardOutWardDetails.expiryDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.expiryDate = res.body.expiryDate != null ? moment(res.body.expiryDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((rSInwardOutWardDetails: IRSInwardOutWardDetails) => {
            rSInwardOutWardDetails.expiryDate =
                rSInwardOutWardDetails.expiryDate != null ? moment(rSInwardOutWardDetails.expiryDate) : null;
        });
        return res;
    }
}
