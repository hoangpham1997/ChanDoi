import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMBInternalTransferDetails } from 'app/shared/model/mb-internal-transfer-details.model';

type EntityResponseType = HttpResponse<IMBInternalTransferDetails>;
type EntityArrayResponseType = HttpResponse<IMBInternalTransferDetails[]>;

@Injectable({ providedIn: 'root' })
export class MBInternalTransferDetailsService {
    private resourceUrl = SERVER_API_URL + 'api/mb-internal-transfer-details';

    constructor(private http: HttpClient) {}

    create(mBInternalTransferDetails: IMBInternalTransferDetails): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mBInternalTransferDetails);
        return this.http
            .post<IMBInternalTransferDetails>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(mBInternalTransferDetails: IMBInternalTransferDetails): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mBInternalTransferDetails);
        return this.http
            .put<IMBInternalTransferDetails>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IMBInternalTransferDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMBInternalTransferDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(mBInternalTransferDetails: IMBInternalTransferDetails): IMBInternalTransferDetails {
        const copy: IMBInternalTransferDetails = Object.assign({}, mBInternalTransferDetails, {
            matchDate:
                mBInternalTransferDetails.matchDate != null && mBInternalTransferDetails.matchDate.isValid()
                    ? mBInternalTransferDetails.matchDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.matchDate = res.body.matchDate != null ? moment(res.body.matchDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((mBInternalTransferDetails: IMBInternalTransferDetails) => {
            mBInternalTransferDetails.matchDate =
                mBInternalTransferDetails.matchDate != null ? moment(mBInternalTransferDetails.matchDate) : null;
        });
        return res;
    }
}
