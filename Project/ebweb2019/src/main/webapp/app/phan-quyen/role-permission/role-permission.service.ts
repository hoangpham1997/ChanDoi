import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IEbGroup } from 'app/core/eb-group/eb-group.model';
import { IEbAuthority } from 'app/core/eb-authority/eb-authority.model';

@Injectable({ providedIn: 'root' })
export class RolePermissionService {
    private resourceUrl = SERVER_API_URL + 'api/ebAuthorities';

    constructor(private http: HttpClient) {}

    create(ebAuthority: IEbAuthority): Observable<HttpResponse<IEbAuthority>> {
        return this.http.post<IEbAuthority>(this.resourceUrl, ebAuthority, { observe: 'response' });
    }

    update(ebAuthority: IEbAuthority): Observable<HttpResponse<IEbGroup>> {
        return this.http.put<IEbAuthority>(this.resourceUrl, ebAuthority, { observe: 'response' });
    }

    find(id: string): Observable<HttpResponse<IEbAuthority>> {
        return this.http.get<IEbAuthority>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<HttpResponse<IEbAuthority[]>> {
        const options = createRequestOption(req);
        return this.http.get<IEbAuthority[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getAllAuthorities(req?: any): Observable<HttpResponse<IEbAuthority[]>> {
        const options = createRequestOption(req);
        return this.http.get<IEbAuthority[]>(this.resourceUrl + '/getAll', { params: options, observe: 'response' });
    }

    getAllAuthTree(): Observable<any> {
        return this.http.get<any>(this.resourceUrl + '/tree/get-all', { observe: 'response' });
    }
}
