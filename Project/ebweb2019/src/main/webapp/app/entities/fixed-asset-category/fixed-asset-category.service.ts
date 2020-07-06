import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFixedAssetCategory } from 'app/shared/model/fixed-asset-category.model';

type EntityResponseType = HttpResponse<IFixedAssetCategory>;
type EntityArrayResponseType = HttpResponse<IFixedAssetCategory[]>;

@Injectable({ providedIn: 'root' })
export class FixedAssetCategoryService {
    private resourceUrl = SERVER_API_URL + 'api/fixed-asset-categories';

    constructor(private http: HttpClient) {}

    create(fixedAssetCategory: IFixedAssetCategory): Observable<EntityResponseType> {
        return this.http.post<IFixedAssetCategory>(this.resourceUrl, fixedAssetCategory, { observe: 'response' });
    }

    update(fixedAssetCategory: IFixedAssetCategory): Observable<EntityResponseType> {
        return this.http.put<IFixedAssetCategory>(this.resourceUrl, fixedAssetCategory, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IFixedAssetCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFixedAssetCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
