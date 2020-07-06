import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';

type EntityResponseType = HttpResponse<IStatisticsCode>;
type EntityArrayResponseType = HttpResponse<IStatisticsCode[]>;

@Injectable({ providedIn: 'root' })
export class StatisticsCodeService {
    private resourceUrl = SERVER_API_URL + 'api/statistics-codes';

    constructor(private http: HttpClient) {}

    create(statisticsCode: IStatisticsCode): Observable<EntityResponseType> {
        return this.http.post<IStatisticsCode>(this.resourceUrl, statisticsCode, { observe: 'response' });
    }

    update(statisticsCode: IStatisticsCode): Observable<EntityResponseType> {
        return this.http.put<IStatisticsCode>(this.resourceUrl, statisticsCode, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IStatisticsCode>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IStatisticsCode[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    getAllStatisticsCodes(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IStatisticsCode[]>(this.resourceUrl + '/find-all-statistics-codes-by-company-id', {
            params: options,
            observe: 'response'
        });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getAllStatisticsCodesActive(): Observable<EntityArrayResponseType> {
        return this.http.get<any[]>(this.resourceUrl + '/active', { observe: 'response' });
    }

    getStatisticsCodes(): Observable<EntityArrayResponseType> {
        return this.http.get<IStatisticsCode[]>(this.resourceUrl + '/find-all-statistics-codes-active-companyid', { observe: 'response' });
    }

    getStatisticsCodesByCompanyID(): Observable<EntityArrayResponseType> {
        return this.http.get<IStatisticsCode[]>(this.resourceUrl + '/find-all-statistics-codes-by-companyid', { observe: 'response' });
    }
}
