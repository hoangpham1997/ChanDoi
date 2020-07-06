import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMaterialGoodsResourceTaxGroup } from 'app/shared/model/material-goods-resource-tax-group.model';

type EntityResponseType = HttpResponse<IMaterialGoodsResourceTaxGroup>;
type EntityArrayResponseType = HttpResponse<IMaterialGoodsResourceTaxGroup[]>;

@Injectable({ providedIn: 'root' })
export class MaterialGoodsResourceTaxGroupService {
    private resourceUrl = SERVER_API_URL + 'api/material-goods-resource-tax-groups';

    constructor(private http: HttpClient) {}

    create(materialGoodsResourceTaxGroup: IMaterialGoodsResourceTaxGroup): Observable<EntityResponseType> {
        return this.http.post<IMaterialGoodsResourceTaxGroup>(this.resourceUrl, materialGoodsResourceTaxGroup, { observe: 'response' });
    }

    update(materialGoodsResourceTaxGroup: IMaterialGoodsResourceTaxGroup): Observable<EntityResponseType> {
        return this.http.put<IMaterialGoodsResourceTaxGroup>(this.resourceUrl, materialGoodsResourceTaxGroup, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IMaterialGoodsResourceTaxGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoodsResourceTaxGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
