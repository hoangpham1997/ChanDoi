import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICPUncompleteDetails } from 'app/shared/model/cp-uncomplete-details.model';
import { ICPAllocationQuantum } from 'app/shared/model/cp-allocation-quantum.model';

type EntityResponseType = HttpResponse<ICPUncompleteDetails>;
type EntityArrayResponseType = HttpResponse<ICPUncompleteDetails[]>;

@Injectable({ providedIn: 'root' })
export class CPUncompleteDetailsService {
    private resourceUrl = SERVER_API_URL + 'api/cp-uncomplete-details';

    constructor(private http: HttpClient) {}

    create(cPUncompleteDetails: ICPUncompleteDetails): Observable<EntityResponseType> {
        return this.http.post<ICPUncompleteDetails>(this.resourceUrl, cPUncompleteDetails, { observe: 'response' });
    }

    update(cPUncompleteDetails: ICPUncompleteDetails): Observable<EntityResponseType> {
        return this.http.put<ICPUncompleteDetails>(this.resourceUrl, cPUncompleteDetails, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<ICPUncompleteDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICPUncompleteDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    getAllByCPPeriodID(cPPeriodID: string): Observable<EntityArrayResponseType> {
        return this.http.get<ICPUncompleteDetails[]>(`${this.resourceUrl + '/get-all-by-cPPeriodID'}/${cPPeriodID}`, {
            observe: 'response'
        });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    evaluate(req: any): Observable<EntityArrayResponseType> {
        return this.http.post<ICPUncompleteDetails[]>(`${this.resourceUrl}/evaluate`, req, { observe: 'response' });
    }
}
