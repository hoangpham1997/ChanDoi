import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISalePriceGroup } from 'app/shared/model/sale-price-group.model';

type EntityResponseType = HttpResponse<ISalePriceGroup>;
type EntityArrayResponseType = HttpResponse<ISalePriceGroup[]>;

@Injectable({ providedIn: 'root' })
export class SalePriceGroupService {
    private resourceUrl = SERVER_API_URL + 'api/sale-price-groups';

    constructor(private http: HttpClient) {}

    create(salePriceGroup: ISalePriceGroup): Observable<EntityResponseType> {
        return this.http.post<ISalePriceGroup>(this.resourceUrl, salePriceGroup, { observe: 'response' });
    }

    update(salePriceGroup: ISalePriceGroup): Observable<EntityResponseType> {
        return this.http.put<ISalePriceGroup>(this.resourceUrl, salePriceGroup, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISalePriceGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISalePriceGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
