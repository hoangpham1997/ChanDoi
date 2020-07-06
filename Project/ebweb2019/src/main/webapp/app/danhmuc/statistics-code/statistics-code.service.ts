import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';

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

    find(id?: any): Observable<EntityResponseType> {
        return this.http.get<IStatisticsCode>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getStatisticsCodes(): Observable<EntityArrayResponseType> {
        return this.http.get<IStatisticsCode[]>(this.resourceUrl, { observe: 'response' });
    }

    getActiveStatisticsCodes(): Observable<EntityArrayResponseType> {
        return this.http.get<IStatisticsCode[]>(this.resourceUrl + '/get-all-active', { observe: 'response' });
    }

    getCbxStatisticsCodes(id: any): Observable<EntityArrayResponseType> {
        return this.http.get<IStatisticsCode[]>(`${this.resourceUrl + '/getCbxStatisticsCodes'}/${id}`, {
            observe: 'response'
        });
    }

    getStatisticsCodesSimilarBranch(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IStatisticsCode[]>(this.resourceUrl + '/find-all-statistics-code-by-company-id-similar-branch', {
            params: options,
            observe: 'response'
        });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    // getAllStatisticCode(req?: any): Observable<EntityArrayResponseType> {
    //     const options = createRequestOption(req);
    //     return this.http.get<IStatisticsCode[]>(this.resourceUrl + '/getAllStatisticsCode', {
    //         params: options,
    //         observe: 'response'
    //     });
    // }
}
