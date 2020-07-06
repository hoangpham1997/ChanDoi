import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMCAuditDetails } from 'app/shared/model/mc-audit-details.model';

type EntityResponseType = HttpResponse<IMCAuditDetails>;
type EntityArrayResponseType = HttpResponse<IMCAuditDetails[]>;

@Injectable({ providedIn: 'root' })
export class MCAuditDetailsService {
    private resourceUrl = SERVER_API_URL + 'api/mc-audit-details';

    constructor(private http: HttpClient) {}

    create(mCAuditDetails: IMCAuditDetails): Observable<EntityResponseType> {
        return this.http.post<IMCAuditDetails>(this.resourceUrl, mCAuditDetails, { observe: 'response' });
    }

    update(mCAuditDetails: IMCAuditDetails): Observable<EntityResponseType> {
        return this.http.put<IMCAuditDetails>(this.resourceUrl, mCAuditDetails, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IMCAuditDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMCAuditDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
