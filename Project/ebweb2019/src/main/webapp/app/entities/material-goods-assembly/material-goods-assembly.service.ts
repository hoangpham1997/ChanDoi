import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMaterialGoodsAssembly } from 'app/shared/model/material-goods-assembly.model';

type EntityResponseType = HttpResponse<IMaterialGoodsAssembly>;
type EntityArrayResponseType = HttpResponse<IMaterialGoodsAssembly[]>;

@Injectable({ providedIn: 'root' })
export class MaterialGoodsAssemblyService {
    private resourceUrl = SERVER_API_URL + 'api/material-goods-assemblies';

    constructor(private http: HttpClient) {}

    create(materialGoodsAssembly: IMaterialGoodsAssembly): Observable<EntityResponseType> {
        return this.http.post<IMaterialGoodsAssembly>(this.resourceUrl, materialGoodsAssembly, { observe: 'response' });
    }

    update(materialGoodsAssembly: IMaterialGoodsAssembly): Observable<EntityResponseType> {
        return this.http.put<IMaterialGoodsAssembly>(this.resourceUrl, materialGoodsAssembly, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IMaterialGoodsAssembly>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoodsAssembly[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findByMaterialGoodsID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/find-by-material-goods-id`, { params: options, observe: 'response' });
    }
}
