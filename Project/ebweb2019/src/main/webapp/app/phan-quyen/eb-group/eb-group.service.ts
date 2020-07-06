import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IEbGroup } from 'app/core/eb-group/eb-group.model';

@Injectable({ providedIn: 'root' })
export class EbGroupService {
    private resourceUrl = SERVER_API_URL + 'api/ebGroups';

    constructor(private http: HttpClient) {}

    create(ebGroup: IEbGroup): Observable<HttpResponse<any>> {
        return this.http.post<any>(this.resourceUrl, ebGroup, { observe: 'response' });
    }

    update(ebGroup: IEbGroup): Observable<HttpResponse<any>> {
        return this.http.put<any>(this.resourceUrl, ebGroup, { observe: 'response' });
    }

    updateAuthorities(ebGroup: IEbGroup): Observable<HttpResponse<any>> {
        return this.http.put<any>(this.resourceUrl + '/update-authorities', ebGroup, { observe: 'response' });
    }

    find(id: string): Observable<HttpResponse<IEbGroup>> {
        return this.http.get<IEbGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<HttpResponse<IEbGroup[]>> {
        const options = createRequestOption(req);
        return this.http.get<IEbGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    authorities(): Observable<string[]> {
        return this.http.get<string[]>(SERVER_API_URL + 'api/ebGroups/authorities');
    }

    getEbGroups(): Observable<any[]> {
        return this.http.get<any[]>(SERVER_API_URL + 'api/ebGroups/ebGroups');
    }

    getEbGroupsByOrgId(req: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(SERVER_API_URL + 'api/ebGroups/get-all-by-orgId', { params: options, observe: 'response' });
    }

    createForAdminTotalPackage(orgGroup: any) {
        return this.http.post<any>(this.resourceUrl + '/for-admin-total-package', orgGroup, { observe: 'response' });
    }
}
