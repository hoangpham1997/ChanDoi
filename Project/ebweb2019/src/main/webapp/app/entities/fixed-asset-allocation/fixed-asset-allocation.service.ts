import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFixedAssetAllocation } from 'app/shared/model/fixed-asset-allocation.model';

type EntityResponseType = HttpResponse<IFixedAssetAllocation>;
type EntityArrayResponseType = HttpResponse<IFixedAssetAllocation[]>;

@Injectable({ providedIn: 'root' })
export class FixedAssetAllocationService {
    private resourceUrl = SERVER_API_URL + 'api/fixed-asset-allocations';

    constructor(private http: HttpClient) {}

    create(fixedAssetAllocation: IFixedAssetAllocation): Observable<EntityResponseType> {
        return this.http.post<IFixedAssetAllocation>(this.resourceUrl, fixedAssetAllocation, { observe: 'response' });
    }

    update(fixedAssetAllocation: IFixedAssetAllocation): Observable<EntityResponseType> {
        return this.http.put<IFixedAssetAllocation>(this.resourceUrl, fixedAssetAllocation, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IFixedAssetAllocation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFixedAssetAllocation[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
