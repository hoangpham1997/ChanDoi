import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';

type EntityResponseType = HttpResponse<IGoodsServicePurchase>;
type EntityArrayResponseType = HttpResponse<IGoodsServicePurchase[]>;

@Injectable({ providedIn: 'root' })
export class GoodsServicePurchaseService {
    private resourceUrl = SERVER_API_URL + 'api/goods-service-purchases';

    constructor(private http: HttpClient) {}

    create(goodsServicePurchase: IGoodsServicePurchase): Observable<EntityResponseType> {
        return this.http.post<IGoodsServicePurchase>(this.resourceUrl, goodsServicePurchase, { observe: 'response' });
    }

    update(goodsServicePurchase: IGoodsServicePurchase): Observable<EntityResponseType> {
        return this.http.put<IGoodsServicePurchase>(this.resourceUrl, goodsServicePurchase, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IGoodsServicePurchase>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IGoodsServicePurchase[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getPurchaseName(): Observable<EntityResponseType> {
        return this.http.get<any>(`${this.resourceUrl}/getPurchaseName`, { observe: 'response' });
    }

    getPurchaseNameToCombobox(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/getOrganizationUnit`, { params: options, observe: 'response' });
    }

    getGoodServicePurchases(): Observable<EntityArrayResponseType> {
        return this.http.get<IGoodsServicePurchase[]>(this.resourceUrl + '/find-all-good-service-purchase-active-companyid', {
            observe: 'response'
        });
    }
}
