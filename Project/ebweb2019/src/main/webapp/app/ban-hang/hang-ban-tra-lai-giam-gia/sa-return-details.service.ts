import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISaReturnDetails } from 'app/shared/model/sa-return-details.model';

type EntityResponseType = HttpResponse<ISaReturnDetails>;
type EntityArrayResponseType = HttpResponse<ISaReturnDetails[]>;

@Injectable({ providedIn: 'root' })
export class SaReturnDetailsService {
    private resourceUrl = SERVER_API_URL + 'api/sa-return-details';

    constructor(private http: HttpClient) {}

    create(saReturnDetails: ISaReturnDetails): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(saReturnDetails);
        return this.http
            .post<ISaReturnDetails>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(saReturnDetails: ISaReturnDetails): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(saReturnDetails);
        return this.http
            .put<ISaReturnDetails>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<any> {
        return this.http
            .get<ISaReturnDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISaReturnDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(saReturnDetails: ISaReturnDetails): ISaReturnDetails {
        const copy: ISaReturnDetails = Object.assign({}, saReturnDetails, {
            expiryDate:
                saReturnDetails.expiryDate != null && saReturnDetails.expiryDate.isValid()
                    ? saReturnDetails.expiryDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res): EntityResponseType {
        res.body.saReturnDetailsViewDTOs.forEach(item => {
            item.date = item.date != null ? moment(item.date, 'DD/MM/YYYY') : null;
        });
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((saReturnDetails: ISaReturnDetails) => {
            saReturnDetails.expiryDate = saReturnDetails.expiryDate != null ? moment(saReturnDetails.expiryDate) : null;
            saReturnDetails.date = saReturnDetails.date != null ? moment(saReturnDetails.date, 'DD/MM/YYYY') : null;
        });
        return res;
    }
}
