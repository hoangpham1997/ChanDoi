import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMaterialGoodsConvertUnit } from 'app/shared/model/material-goods-convert-unit.model';

type EntityResponseType = HttpResponse<IMaterialGoodsConvertUnit>;
type EntityArrayResponseType = HttpResponse<IMaterialGoodsConvertUnit[]>;

@Injectable({ providedIn: 'root' })
export class MaterialGoodsConvertUnitService {
    private resourceUrl = SERVER_API_URL + 'api/material-goods-convert-units';

    constructor(private http: HttpClient) {}

    create(materialGoodsConvertUnit: IMaterialGoodsConvertUnit): Observable<EntityResponseType> {
        return this.http.post<IMaterialGoodsConvertUnit>(this.resourceUrl, materialGoodsConvertUnit, { observe: 'response' });
    }

    update(materialGoodsConvertUnit: IMaterialGoodsConvertUnit): Observable<EntityResponseType> {
        return this.http.put<IMaterialGoodsConvertUnit>(this.resourceUrl, materialGoodsConvertUnit, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IMaterialGoodsConvertUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoodsConvertUnit[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    getAllMaterialGoodsConvertUnits(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoodsConvertUnit[]>(this.resourceUrl + '/getAll', {
            params: options,
            observe: 'response'
        });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getMaterialGoodsConvertUnitPPInvoice(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoodsConvertUnit>(this.resourceUrl + '/get-by-materialgoodsid-and-unitid', {
            params: options,
            observe: 'response'
        });
    }

    findByMaterialGoodsID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/find-by-material-goods-id`, { params: options, observe: 'response' });
    }

    getNumberOrder(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(`${this.resourceUrl}/get-number-order`, { params: options, observe: 'response' });
    }
}
