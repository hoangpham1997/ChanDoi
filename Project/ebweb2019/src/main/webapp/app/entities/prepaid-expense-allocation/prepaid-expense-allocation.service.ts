import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPrepaidExpenseAllocation } from 'app/shared/model/prepaid-expense-allocation.model';

type EntityResponseType = HttpResponse<IPrepaidExpenseAllocation>;
type EntityArrayResponseType = HttpResponse<IPrepaidExpenseAllocation[]>;

@Injectable({ providedIn: 'root' })
export class PrepaidExpenseAllocationService {
    private resourceUrl = SERVER_API_URL + 'api/prepaid-expense-allocations';

    constructor(private http: HttpClient) {}

    create(prepaidExpenseAllocation: IPrepaidExpenseAllocation): Observable<EntityResponseType> {
        return this.http.post<IPrepaidExpenseAllocation>(this.resourceUrl, prepaidExpenseAllocation, { observe: 'response' });
    }

    update(prepaidExpenseAllocation: IPrepaidExpenseAllocation): Observable<EntityResponseType> {
        return this.http.put<IPrepaidExpenseAllocation>(this.resourceUrl, prepaidExpenseAllocation, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IPrepaidExpenseAllocation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPrepaidExpenseAllocation[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
