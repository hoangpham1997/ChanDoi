import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { IBank } from 'app/shared/model/bank.model';
import { IUnit } from 'app/shared/model/unit.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IAccountList } from 'app/shared/model/account-list.model';

type EntityResponseType = HttpResponse<IMaterialGoodsCategory>;
type EntityArrayResponseType = HttpResponse<IMaterialGoodsCategory[]>;

@Injectable({ providedIn: 'root' })
export class MaterialGoodsCategoryService {
    private resourceUrl = SERVER_API_URL + 'api/material-goods-categories';

    constructor(private http: HttpClient) {}

    create(materialGoodsCategory: IMaterialGoodsCategory): Observable<EntityResponseType> {
        return this.http.post<IMaterialGoodsCategory>(this.resourceUrl, materialGoodsCategory, { observe: 'response' });
    }

    update(materialGoodsCategory: IMaterialGoodsCategory): Observable<EntityResponseType> {
        return this.http.put<IMaterialGoodsCategory>(this.resourceUrl, materialGoodsCategory, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IMaterialGoodsCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoodsCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    getMaterialGoodsCategory(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoodsCategory[]>(this.resourceUrl + '/find-all-material-goods-category-by-company-id', {
            params: options,
            observe: 'response'
        });
    }

    getMaterialGoodsCategoryForReport(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoodsCategory[]>(this.resourceUrl + '/get-for-report', {
            params: options,
            observe: 'response'
        });
    }

    getMaterialGoodsCategoriesActiveExceptID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountList[]>(this.resourceUrl + '/find-all-material-goods-categories-active-companyid-except-id', {
            params: options,
            observe: 'response'
        });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
    pageableMaterialGoodsCategories(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoodsCategory[]>(this.resourceUrl + '/pageable-all-material-goods-categories', {
            params: options,
            observe: 'response'
        });
    }

    getMaterialGoodsCategorySimilarBranch(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoodsCategory[]>(
            this.resourceUrl + '/find-all-material-goods-category-by-company-id-similar-branch',
            {
                params: options,
                observe: 'response'
            }
        );
    }
}
