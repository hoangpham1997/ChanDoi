import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFixedAssetDetails } from 'app/shared/model/fixed-asset-details.model';

type EntityResponseType = HttpResponse<IFixedAssetDetails>;
type EntityArrayResponseType = HttpResponse<IFixedAssetDetails[]>;

@Injectable({ providedIn: 'root' })
export class FixedAssetDetailsService {
    private resourceUrl = SERVER_API_URL + 'api/fixed-asset-details';

    constructor(private http: HttpClient) {}

    create(fixedAssetDetails: IFixedAssetDetails): Observable<EntityResponseType> {
        return this.http.post<IFixedAssetDetails>(this.resourceUrl, fixedAssetDetails, { observe: 'response' });
    }

    update(fixedAssetDetails: IFixedAssetDetails): Observable<EntityResponseType> {
        return this.http.put<IFixedAssetDetails>(this.resourceUrl, fixedAssetDetails, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IFixedAssetDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFixedAssetDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
