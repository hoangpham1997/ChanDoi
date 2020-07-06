import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMCAudit } from 'app/shared/model/mc-audit.model';

type EntityResponseType = HttpResponse<IMCAudit>;
type EntityArrayResponseType = HttpResponse<IMCAudit[]>;

@Injectable({ providedIn: 'root' })
export class BankCompareService {
    private resourceUrl = SERVER_API_URL + 'api/bank-compare';

    constructor(private http: HttpClient) {}

    create(mCAudit: IMCAudit): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mCAudit);
        return this.http
            .post<IMCAudit>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(mCAudit: IMCAudit): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mCAudit);
        return this.http
            .put<IMCAudit>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: any): Observable<EntityResponseType> {
        return this.http
            .get<IMCAudit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMCAudit[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: any): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(mCAudit: IMCAudit): IMCAudit {
        const copy: IMCAudit = Object.assign({}, mCAudit, {
            date: mCAudit.date != null && mCAudit.date.isValid() ? mCAudit.date.format(DATE_FORMAT) : null,
            auditDate: mCAudit.auditDate != null && mCAudit.auditDate.isValid() ? mCAudit.auditDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.auditDate = res.body.auditDate != null ? moment(res.body.auditDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((mCAudit: IMCAudit) => {
            mCAudit.date = mCAudit.date != null ? moment(mCAudit.date) : null;
            mCAudit.auditDate = mCAudit.auditDate != null ? moment(mCAudit.auditDate) : null;
        });
        return res;
    }
}
