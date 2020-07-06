import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICPPeriod } from 'app/shared/model/cp-period.model';

type EntityResponseType = HttpResponse<ICPPeriod>;
type EntityArrayResponseType = HttpResponse<ICPPeriod[]>;

@Injectable({ providedIn: 'root' })
export class PpGianDonService {
    private resourceUrl = SERVER_API_URL + 'api/cp-periods';

    constructor(private http: HttpClient) {}

    create(cPPeriod: ICPPeriod): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(cPPeriod);
        return this.http
            .post<ICPPeriod>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(cPPeriod: ICPPeriod): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(cPPeriod);
        return this.http
            .put<ICPPeriod>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<ICPPeriod>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICPPeriod[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAllCPPeriod(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICPPeriod[]>(this.resourceUrl + '/getAllByType', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    checkDelete(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(this.resourceUrl + '/checkDelete', { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    multiDelete(obj: any[]): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceUrl}/multi-delete-cp-period`, obj, { observe: 'response' });
    }

    private convertDateFromClient(cPPeriod: ICPPeriod): ICPPeriod {
        const copy: ICPPeriod = Object.assign({}, cPPeriod, {
            fromDate: cPPeriod.fromDate != null && cPPeriod.fromDate.isValid() ? cPPeriod.fromDate.format(DATE_FORMAT) : null,
            toDate: cPPeriod.toDate != null && cPPeriod.toDate.isValid() ? cPPeriod.toDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fromDate = res.body.fromDate != null ? moment(res.body.fromDate) : null;
        res.body.toDate = res.body.toDate != null ? moment(res.body.toDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((cPPeriod: ICPPeriod) => {
            cPPeriod.fromDate = cPPeriod.fromDate != null ? moment(cPPeriod.fromDate) : null;
            cPPeriod.toDate = cPPeriod.toDate != null ? moment(cPPeriod.toDate) : null;
        });
        return res;
    }

    checkPeriod(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICPPeriod[]>(this.resourceUrl + '/checkPeriod', { params: options, observe: 'response' });
    }

    getCPPeriod() {
        return this.http.get<ICPPeriod[]>(this.resourceUrl + '/get-all-c-p-periods', { observe: 'response' });
    }

    getCPPeriodForReport(req?: any) {
        const options = createRequestOption(req);
        return this.http.get<ICPPeriod[]>(this.resourceUrl + '/get-all-c-p-periods-for-report', { params: options, observe: 'response' });
    }

    export(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceUrl + '/export/pdf', { params: options, observe: 'response', headers, responseType: 'blob' });
    }

    exportExcel(req?: any): Observable<any> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceUrl + '/export/excel', { params: options, observe: 'response', headers, responseType: 'blob' });
    }

    getCPPeriodByID(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http
            .get<any>(this.resourceUrl + '/get-cpperiod-by-id', { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }
}
