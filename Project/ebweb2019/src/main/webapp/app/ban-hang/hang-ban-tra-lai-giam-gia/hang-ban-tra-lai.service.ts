import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import * as FileSaver from 'file-saver';
import * as XLSX from 'xlsx';
import { SaReturnDetails } from '../../shared/model/sa-return-details.model';

type EntityResponseType = HttpResponse<IMBDeposit>;
type EntityArrayResponseType = HttpResponse<IMBDeposit[]>;
const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
const EXCEL_EXTENSION = '.xlsx';

@Injectable({ providedIn: 'root' })
export class HangBanTraLaiService {
    private resourceUrl = SERVER_API_URL + 'api/sa-return';

    constructor(private http: HttpClient) {}

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((mBDeposit: IMBDeposit) => {
            mBDeposit.date = mBDeposit.date != null ? moment(mBDeposit.date) : null;
            mBDeposit.postedDate = mBDeposit.postedDate != null ? moment(mBDeposit.postedDate) : null;
        });
        return res;
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMBDeposit[]>(`${this.resourceUrl}/search-all`, { params: options, observe: 'response' });
    }

    getAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMBDeposit[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getCustomerReport(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/pdf');
        return this.http.get(SERVER_API_URL + 'api/report/pdf', {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    findByRowNum(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMBDeposit>(`${this.resourceUrl}/findByRowNum`, { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    getAllSearchData() {
        return this.http.get<any>(`${this.resourceUrl}/search-data`, { observe: 'response' });
    }

    findAllDetailsById(req: any): Observable<HttpResponse<SaReturnDetails[]>> {
        const options = createRequestOption(req);
        return this.http.get<SaReturnDetails[]>(`${this.resourceUrl}/details`, { params: options, observe: 'response' });
    }
}
