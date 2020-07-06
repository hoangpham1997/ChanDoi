import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUnit } from 'app/shared/model/unit.model';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { ITIIncrement } from 'app/shared/model/ti-increment.model';

type EntityResponseType = HttpResponse<IUnit>;
type EntityArrayResponseType = HttpResponse<IUnit[]>;

@Injectable({ providedIn: 'root' })
export class UnitService {
    private resourceUrl = SERVER_API_URL + 'api/units';

    constructor(private http: HttpClient) {}

    create(unit: IUnit): Observable<EntityResponseType> {
        return this.http.post<IUnit>(this.resourceUrl, unit, { observe: 'response' });
    }

    update(unit: IUnit): Observable<EntityResponseType> {
        return this.http.put<IUnit>(this.resourceUrl, unit, { observe: 'response' });
    }

    find(id?: any): Observable<EntityResponseType> {
        return this.http.get<IUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
    findbyID(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IUnit>(this.resourceUrl + '/by-id', { params: options, observe: 'response' });
    }

    getUUIDUnit(id: string): Observable<EntityResponseType> {
        return this.http.get<IUnit>(`${this.resourceUrl}/${id}/uuid`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IUnit[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IUnit[]>(`${this.resourceUrl}/search-all`, { params: options, observe: 'response' });
    }

    getCustomerReport(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/pdf');
        return this.http.get(SERVER_API_URL + 'api/report/pdf', { params: options, observe: 'response', headers, responseType: 'blob' });
    }

    exportExcel(req?: any) {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(SERVER_API_URL + 'api/report/excel', { params: options, observe: 'response', headers, responseType: 'blob' });
    }

    getAllByMaterialGoodsId(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IUnit[]>(`${this.resourceUrl}/material-goods`, { params: options, observe: 'response' });
    }

    convertRateForMaterialGoods(req?: any) {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/material-goods-convert-rate', { params: options, observe: 'response' });
    }
    convertRateForMaterialGoodsCombobox(req?: any) {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/material-goods-convert-rate/combobox', { params: options, observe: 'response' });
    }

    convertRateForMaterialGoodsComboboxCustom(req?: any) {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/material-goods-convert-rate/combobox/custom', {
            params: options,
            observe: 'response'
        });
    }

    getUnitByITIIncrementID(req?: any) {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/get-unit-by-it-incrementID/combobox/custom/increment', {
            params: options,
            observe: 'response'
        });
    }

    convertRateForMaterialGoodsComboboxCustomList(req?: any) {
        const options = createRequestOption(req);
        return this.http.post<any[]>(this.resourceUrl + '/material-goods-convert-rate/combobox/custom-list', {
            params: options,
            observe: 'response'
        });
    }

    getMainUnitName(req?: any) {
        const options = createRequestOption(req);
        return this.http.get<any>(this.resourceUrl + '/main-unit-name', { params: options, observe: 'response' });
    }

    unitPriceOriginalForMaterialGoods(req?: any) {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/material-goods-unit-price', {
            params: options,
            observe: 'response'
        });
    }

    getUnits(): Observable<EntityArrayResponseType> {
        return this.http.get<IUnit[]>(this.resourceUrl + '/find-all-units-active-companyid', {
            observe: 'response'
        });
    }

    getAllUnits(): Observable<EntityArrayResponseType> {
        return this.http.get<IUnit[]>(this.resourceUrl + '/find-all-units-by-company-id', {
            observe: 'response'
        });
    }

    // hautv
    findAllWithConvertRate(): Observable<EntityArrayResponseType> {
        return this.http.get<IUnit[]>(this.resourceUrl + '/find-all-with-convertrate', {
            observe: 'response'
        });
    }

    pageableUnit(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IUnit[]>(this.resourceUrl + '/pageable-all-unit', { params: options, observe: 'response' });
    }

    deleteByListIDUnit(rq: any[]): Observable<HttpResponse<HandlingResult>> {
        return this.http.post<any>(`${this.resourceUrl}/delete-list-unit`, rq, { observe: 'response' });
    }
}
