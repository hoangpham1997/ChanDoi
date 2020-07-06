import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICPAllocationRate } from 'app/shared/model/cp-allocation-rate.model';
import { IBank } from 'app/shared/model/bank.model';

type EntityResponseType = HttpResponse<ICPAllocationRate>;
type EntityArrayResponseType = HttpResponse<ICPAllocationRate[]>;

@Injectable({ providedIn: 'root' })
export class CPAllocationRateService {
    private resourceUrl = SERVER_API_URL + 'api/c-p-allocation-rates';

    constructor(private http: HttpClient) {}

    create(cPAllocationRate: ICPAllocationRate): Observable<EntityResponseType> {
        return this.http.post<ICPAllocationRate>(this.resourceUrl, cPAllocationRate, { observe: 'response' });
    }

    update(cPAllocationRate: ICPAllocationRate): Observable<EntityResponseType> {
        return this.http.put<ICPAllocationRate>(this.resourceUrl, cPAllocationRate, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<ICPAllocationRate>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICPAllocationRate[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findByCPPeriodID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBank[]>(this.resourceUrl + '/find-all-by-c-p-period-id', { params: options, observe: 'response' });
    }

    findByListCostSetID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICPAllocationRate[]>(this.resourceUrl + '/find-all-by-list-cost-set', {
            params: options,
            observe: 'response'
        });
    }
}
