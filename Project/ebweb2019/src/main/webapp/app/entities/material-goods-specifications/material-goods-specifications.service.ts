import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMaterialGoodsSpecifications } from 'app/shared/model/material-goods-specifications.model';

type EntityResponseType = HttpResponse<IMaterialGoodsSpecifications>;
type EntityArrayResponseType = HttpResponse<IMaterialGoodsSpecifications[]>;

@Injectable({ providedIn: 'root' })
export class MaterialGoodsSpecificationsService {
    private resourceUrl = SERVER_API_URL + 'api/material-goods-specifications';
    private resourceLedgerUrl = SERVER_API_URL + 'api/material-goods-specifications-ledgers';

    constructor(private http: HttpClient) {}

    create(materialGoodsSpecifications: IMaterialGoodsSpecifications): Observable<EntityResponseType> {
        return this.http.post<IMaterialGoodsSpecifications>(this.resourceUrl, materialGoodsSpecifications, { observe: 'response' });
    }

    update(materialGoodsSpecifications: IMaterialGoodsSpecifications): Observable<EntityResponseType> {
        return this.http.put<IMaterialGoodsSpecifications>(this.resourceUrl, materialGoodsSpecifications, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IMaterialGoodsSpecifications>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoodsSpecifications[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findByMaterialGoodsID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/find-by-material-goods-id`, { params: options, observe: 'response' });
    }

    findLedgerByMaterialGoodsID(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(`${this.resourceLedgerUrl}/find-by-material-goods-id`, { params: options, observe: 'response' });
    }
}
