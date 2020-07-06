import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMaterialGoodsPurchasePrice } from 'app/shared/model/material-goods-purchase-price.model';

type EntityResponseType = HttpResponse<IMaterialGoodsPurchasePrice>;
type EntityArrayResponseType = HttpResponse<IMaterialGoodsPurchasePrice[]>;

@Injectable({ providedIn: 'root' })
export class MaterialGoodsPurchasePriceService {
    private resourceUrl = SERVER_API_URL + 'api/material-goods-purchase-prices';

    constructor(private http: HttpClient) {}

    create(materialGoodsPurchasePrice: IMaterialGoodsPurchasePrice): Observable<EntityResponseType> {
        return this.http.post<IMaterialGoodsPurchasePrice>(this.resourceUrl, materialGoodsPurchasePrice, { observe: 'response' });
    }

    update(materialGoodsPurchasePrice: IMaterialGoodsPurchasePrice): Observable<EntityResponseType> {
        return this.http.put<IMaterialGoodsPurchasePrice>(this.resourceUrl, materialGoodsPurchasePrice, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IMaterialGoodsPurchasePrice>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoodsPurchasePrice[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findByMaterialGoodsID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/find-by-material-goods-id`, { params: options, observe: 'response' });
    }
}
