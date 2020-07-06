import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMBInternalTransfer } from 'app/shared/model/mb-internal-transfer.model';

type EntityResponseType = HttpResponse<IMBInternalTransfer>;
type EntityArrayResponseType = HttpResponse<IMBInternalTransfer[]>;

@Injectable({ providedIn: 'root' })
export class MBInternalTransferService {
    private resourceUrl = SERVER_API_URL + 'api/mb-internal-transfers';

    constructor(private http: HttpClient) {}

    create(mBInternalTransfer: IMBInternalTransfer): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mBInternalTransfer);
        return this.http
            .post<IMBInternalTransfer>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(mBInternalTransfer: IMBInternalTransfer): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mBInternalTransfer);
        return this.http
            .put<IMBInternalTransfer>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IMBInternalTransfer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMBInternalTransfer[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(mBInternalTransfer: IMBInternalTransfer): IMBInternalTransfer {
        const copy: IMBInternalTransfer = Object.assign({}, mBInternalTransfer, {
            date: mBInternalTransfer.date != null && mBInternalTransfer.date.isValid() ? mBInternalTransfer.date.format(DATE_FORMAT) : null,
            postedDate:
                mBInternalTransfer.postedDate != null && mBInternalTransfer.postedDate.isValid()
                    ? mBInternalTransfer.postedDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((mBInternalTransfer: IMBInternalTransfer) => {
            mBInternalTransfer.date = mBInternalTransfer.date != null ? moment(mBInternalTransfer.date) : null;
            mBInternalTransfer.postedDate = mBInternalTransfer.postedDate != null ? moment(mBInternalTransfer.postedDate) : null;
        });
        return res;
    }
}
