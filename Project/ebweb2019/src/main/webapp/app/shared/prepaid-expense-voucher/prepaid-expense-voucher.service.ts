import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITypeGroup } from 'app/shared/model/type-group.model';

type EntityResponseType = HttpResponse<ITypeGroup>;
type EntityArrayResponseType = HttpResponse<ITypeGroup[]>;

@Injectable({ providedIn: 'root' })
export class PrepaidExpenseVoucherService {
    private resourceUrl = SERVER_API_URL + 'api/type-groups';

    constructor(private http: HttpClient) {}

    create(typeGroup: ITypeGroup): Observable<EntityResponseType> {
        return this.http.post<ITypeGroup>(this.resourceUrl, typeGroup, { observe: 'response' });
    }

    update(typeGroup: ITypeGroup): Observable<EntityResponseType> {
        return this.http.put<ITypeGroup>(this.resourceUrl, typeGroup, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ITypeGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ITypeGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
