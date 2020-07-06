import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISaBillDetails } from 'app/shared/model/sa-bill-details.model';

type EntityResponseType = HttpResponse<ISaBillDetails>;
type EntityArrayResponseType = HttpResponse<ISaBillDetails[]>;

@Injectable({ providedIn: 'root' })
export class SaBillDetailsService {
    private resourceUrl = SERVER_API_URL + 'api/sa-bill-details';

    constructor(private http: HttpClient) {}

    create(saBillDetails: ISaBillDetails): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(saBillDetails);
        return this.http
            .post<ISaBillDetails>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(saBillDetails: ISaBillDetails): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(saBillDetails);
        return this.http
            .put<ISaBillDetails>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: any): Observable<any> {
        return this.http
            .get<any>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISaBillDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(saBillDetails: ISaBillDetails): ISaBillDetails {
        const copy: ISaBillDetails = Object.assign({}, saBillDetails, {
            expiryDate:
                saBillDetails.expiryDate != null && saBillDetails.expiryDate.isValid() ? saBillDetails.expiryDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.expiryDate = res.body && res.body.expiryDate != null ? moment(res.body.expiryDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: any): any {
        res.body.saBillDetails.forEach((saBillDetails: ISaBillDetails) => {
            saBillDetails.expiryDate = saBillDetails.expiryDate != null ? moment(saBillDetails.expiryDate) : null;
        });
        return res;
    }
}
