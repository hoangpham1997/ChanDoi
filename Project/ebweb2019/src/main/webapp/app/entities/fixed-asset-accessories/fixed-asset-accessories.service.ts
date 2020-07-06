import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFixedAssetAccessories } from 'app/shared/model/fixed-asset-accessories.model';

type EntityResponseType = HttpResponse<IFixedAssetAccessories>;
type EntityArrayResponseType = HttpResponse<IFixedAssetAccessories[]>;

@Injectable({ providedIn: 'root' })
export class FixedAssetAccessoriesService {
    private resourceUrl = SERVER_API_URL + 'api/fixed-asset-accessories';

    constructor(private http: HttpClient) {}

    create(fixedAssetAccessories: IFixedAssetAccessories): Observable<EntityResponseType> {
        return this.http.post<IFixedAssetAccessories>(this.resourceUrl, fixedAssetAccessories, { observe: 'response' });
    }

    update(fixedAssetAccessories: IFixedAssetAccessories): Observable<EntityResponseType> {
        return this.http.put<IFixedAssetAccessories>(this.resourceUrl, fixedAssetAccessories, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IFixedAssetAccessories>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFixedAssetAccessories[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
