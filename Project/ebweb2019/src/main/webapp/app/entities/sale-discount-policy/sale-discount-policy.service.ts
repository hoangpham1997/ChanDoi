import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';

type EntityResponseType = HttpResponse<ISaleDiscountPolicy>;
type EntityArrayResponseType = HttpResponse<ISaleDiscountPolicy[]>;

@Injectable({ providedIn: 'root' })
export class SaleDiscountPolicyService {
    private resourceUrl = SERVER_API_URL + 'api/sale-discount-policies';

    constructor(private http: HttpClient) {}

    create(saleDiscountPolicy: ISaleDiscountPolicy): Observable<EntityResponseType> {
        return this.http.post<ISaleDiscountPolicy>(this.resourceUrl, saleDiscountPolicy, { observe: 'response' });
    }

    update(saleDiscountPolicy: ISaleDiscountPolicy): Observable<EntityResponseType> {
        return this.http.put<ISaleDiscountPolicy>(this.resourceUrl, saleDiscountPolicy, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISaleDiscountPolicy>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISaleDiscountPolicy[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    // findByMaterialGoodsID(id: string) {
    //     return this.http.get<ISaleDiscountPolicy[]>(`${this.resourceUrl}/material-goods-id/${id}`, { observe: 'response' });
    // }

    findAllSaleDiscountPolicyDTO() {
        return this.http.get<ISaleDiscountPolicy[]>(`${this.resourceUrl}DTO`, { observe: 'response' });
    }

    findByMaterialGoodsID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/find-by-material-goods-id`, { params: options, observe: 'response' });
    }
}
