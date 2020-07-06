import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMaterialGoods, IMaterialGoodsInStock } from 'app/shared/model/material-goods.model';
import { PPDiscountReturn } from 'app/shared/model/pp-discount-return.model';
import { IBank } from 'app/shared/model/bank.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { IMaterialGoodsSpecialTaxGroup } from 'app/shared/model/material-goods-special-tax-group.model';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { map } from 'rxjs/operators';
import { ISAInvoice } from 'app/shared/model/sa-invoice.model';
import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';

type EntityResponseType = HttpResponse<IMaterialGoods>;
type EntityArrayResponseType = HttpResponse<IMaterialGoods[]>;
type EntityArrayResponseAny = HttpResponse<any>;

@Injectable({ providedIn: 'root' })
export class MaterialGoodsService {
    private resourceUrl = SERVER_API_URL + 'api/material-goods';

    constructor(private http: HttpClient) {}

    create(materialGoods: IMaterialGoods): Observable<EntityResponseType> {
        return this.http.post<IMaterialGoods>(this.resourceUrl, materialGoods, { observe: 'response' });
    }

    update(materialGoods: IMaterialGoods): Observable<EntityResponseType> {
        return this.http.put<IMaterialGoods>(this.resourceUrl, materialGoods, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<IMaterialGoods>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getMaterialByUUID(id: string): Observable<EntityResponseType> {
        return this.http.get<IMaterialGoods>(`${this.resourceUrl}/${id}/uuid`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoods[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    getAllByCompanyID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoods[]>(`${this.resourceUrl}/get-all-by-company-id`, { params: options, observe: 'response' });
    }

    getMaterialGoodsPPInvoiceQuantity(req: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/pp-invoice-quantity`, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    queryForCombobox(): Observable<EntityArrayResponseAny> {
        return this.http.get<any>(this.resourceUrl + '/combobox', { observe: 'response' });
    }

    queryForComboboxGood(req?: any): Observable<EntityArrayResponseAny> {
        const options = createRequestOption(req);
        return this.http.get<any>(this.resourceUrl + '/combobox-good', { params: options, observe: 'response' });
    }

    findByCompanyID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoods[]>(`${this.resourceUrl}/company`, { params: options, observe: 'response' });
    }

    findAllByCompanyID(): Observable<EntityArrayResponseType> {
        return this.http.get<IMaterialGoods[]>(`${this.resourceUrl}/find-all-by-company-id`, { observe: 'response' });
    }

    ConvertToCombobox(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(`${this.resourceUrl}/combobox1`, { params: options, observe: 'response' });
    }
    findAllForPPService(): Observable<EntityArrayResponseType> {
        return this.http.get<IMaterialGoods[]>(`${this.resourceUrl}/find-all-for-pp-service`, { observe: 'response' });
    }

    findAllForPPInvoice(): Observable<EntityArrayResponseType> {
        return this.http.get<IMaterialGoods[]>(`${this.resourceUrl}/find-all-for-pp-invoice`, { observe: 'response' });
    }

    getMaterialGoods(): Observable<EntityArrayResponseType> {
        return this.http.get<IMaterialGoods[]>(this.resourceUrl + '/find-all-material-goods-active-companyid', { observe: 'response' });
    }

    getAllMaterialGoodsDTO(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoods[]>(this.resourceUrl + '/find-all-dto', { params: options, observe: 'response' });
    }

    getAllMaterialGoodsDTOSimilarBranch(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoods[]>(this.resourceUrl + '/find-all-dto-similar-branch', { params: options, observe: 'response' });
    }

    getMaterialGoodsCustom(): Observable<EntityArrayResponseType> {
        return this.http.get<IMaterialGoods[]>(this.resourceUrl + '/find-all-material-goods-custom', { observe: 'response' });
    }

    getMaterialGoodsRepository(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoods[]>(this.resourceUrl + '/find-all-material-goods-by-repository', {
            params: options,
            observe: 'response'
        });
    }

    pageableMaterialGoods(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoods[]>(this.resourceUrl + '/pageable-all-material-goods1', {
            params: options,
            observe: 'response'
        });
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialGoods[]>(`${this.resourceUrl}/search-all`, {
            params: options,
            observe: 'response'
        });
    }

    deleteByListID(rq: any[]): Observable<HttpResponse<HandlingResult>> {
        return this.http.post<any>(`${this.resourceUrl}/delete-material-goods-employee`, rq, { observe: 'response' });
    }
}
