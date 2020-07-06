import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IEbPackage } from 'app/shared/model/eb-package.model';

type EntityResponseType = HttpResponse<IEbPackage>;
type EntityArrayResponseType = HttpResponse<IEbPackage[]>;

@Injectable({ providedIn: 'root' })
export class EbPackageService {
    private resourceUrl = SERVER_API_URL + 'api/eb-packages';

    constructor(private http: HttpClient) {}

    create(ebPackage: IEbPackage): Observable<EntityResponseType> {
        return this.http.post<IEbPackage>(this.resourceUrl, ebPackage, { observe: 'response' });
    }

    update(ebPackage: IEbPackage): Observable<EntityResponseType> {
        return this.http.put<IEbPackage>(this.resourceUrl, ebPackage, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<IEbPackage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IEbPackage[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    queryList(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IEbPackage[]>(this.resourceUrl + '/list-ebPackage', { params: options, observe: 'response' });
    }

    // getEbPackageByUserAndCompany(req?: any): Observable<any> {
    //     const options = createRequestOption(req);
    //     return this.http.get<any>(this.resourceUrl + '/get-PackageByUserAndCompany', { params: options, observe: 'response' });
    // }

    getEbPackageByUser(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<any>(this.resourceUrl + '/get-PackageByUser', { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
