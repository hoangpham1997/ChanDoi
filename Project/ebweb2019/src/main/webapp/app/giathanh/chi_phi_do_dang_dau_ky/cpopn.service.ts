import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICPOPN } from 'app/shared/model/cpopn.model';
import { ICPProductQuantum } from 'app/shared/model/cp-product-quantum.model';
import { ICPAllocationQuantum } from 'app/shared/model/cp-allocation-quantum.model';

type EntityResponseType = HttpResponse<ICPOPN>;
type EntityArrayResponseType = HttpResponse<ICPOPN[]>;

@Injectable({ providedIn: 'root' })
export class CPOPNService {
    private resourceUrl = SERVER_API_URL + 'api/c-popns';

    constructor(private http: HttpClient) {}

    create(cPOPN: ICPOPN): Observable<EntityResponseType> {
        return this.http.post<ICPOPN>(this.resourceUrl, cPOPN, { observe: 'response' });
    }

    update(cPOPN: ICPOPN): Observable<EntityResponseType> {
        return this.http.put<ICPOPN>(this.resourceUrl, cPOPN, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICPOPN>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICPOPN[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    save(cPOPNs: ICPOPN[]): Observable<EntityArrayResponseType> {
        return this.http.put<ICPOPN[]>(`${this.resourceUrl}/save-all`, cPOPNs, { observe: 'response' });
    }

    getCPOPNs(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICPOPN[]>(this.resourceUrl + '/find-all-c-popns-active-companyid', {
            params: options,
            observe: 'response'
        });
    }
}
