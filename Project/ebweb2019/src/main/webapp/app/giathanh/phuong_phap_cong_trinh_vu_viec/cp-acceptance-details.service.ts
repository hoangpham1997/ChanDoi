import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICPUncompleteDetails } from 'app/shared/model/cp-uncomplete-details.model';
import { ICPAllocationQuantum } from 'app/shared/model/cp-allocation-quantum.model';

type EntityResponseType = HttpResponse<any>;
type EntityArrayResponseType = HttpResponse<any[]>;

@Injectable({ providedIn: 'root' })
export class CPAcceptanceDetailsService {
    private resourceUrl = SERVER_API_URL + 'api/cp-acceptance-details';

    constructor(private http: HttpClient) {}

    evaluate(req: any): Observable<EntityArrayResponseType> {
        return this.http.post<any[]>(`${this.resourceUrl}/evaluate`, req, { observe: 'response' });
    }
}
