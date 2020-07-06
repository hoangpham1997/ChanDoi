import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICPAllocationQuantum } from 'app/shared/model/cp-allocation-quantum.model';
import { ICPProductQuantum } from 'app/shared/model/cp-product-quantum.model';

type EntityResponseType = HttpResponse<ICPAllocationQuantum>;
type EntityArrayResponseType = HttpResponse<ICPAllocationQuantum[]>;

@Injectable({ providedIn: 'root' })
export class CPAllocationQuantumService {
    private resourceUrl = SERVER_API_URL + 'api/c-p-allocation-quantums';

    constructor(private http: HttpClient) {}

    create(cPAllocationQuantum: ICPAllocationQuantum): Observable<EntityResponseType> {
        return this.http.post<ICPAllocationQuantum>(this.resourceUrl, cPAllocationQuantum, { observe: 'response' });
    }

    update(cPAllocationQuantum: ICPAllocationQuantum): Observable<EntityResponseType> {
        return this.http.put<ICPAllocationQuantum>(this.resourceUrl, cPAllocationQuantum, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<ICPAllocationQuantum>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICPAllocationQuantum[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    findByCostSetID(ids: any): Observable<EntityArrayResponseType> {
        return this.http.post<ICPAllocationQuantum[]>(`${this.resourceUrl}/find-by-costset-id`, ids, { observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    save(icpAllocationQuantums: ICPAllocationQuantum[]): Observable<EntityArrayResponseType> {
        return this.http.put<ICPAllocationQuantum[]>(`${this.resourceUrl}/save-all`, icpAllocationQuantums, { observe: 'response' });
    }

    getCPAllocationQuantums(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICPAllocationQuantum[]>(this.resourceUrl + '/find-all-c-p-allocation-quantums-active-companyid', {
            params: options,
            observe: 'response'
        });
    }
}
