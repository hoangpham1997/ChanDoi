import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_FORMAT_TYPE1 } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRepositoryLedger } from 'app/shared/model/repository-ledger.model';
import { IViewLotNo } from 'app/shared/model/view-lotno.model';

type EntityResponseType = HttpResponse<IRepositoryLedger>;
type EntityArrayResponseType = HttpResponse<IRepositoryLedger[]>;

@Injectable({ providedIn: 'root' })
export class RepositoryLedgerService {
    private resourceUrl = SERVER_API_URL + 'api/repository-ledgers';

    constructor(private http: HttpClient) {}

    create(repositoryLedger: IRepositoryLedger): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(repositoryLedger);
        return this.http
            .post<IRepositoryLedger>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(repositoryLedger: IRepositoryLedger): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(repositoryLedger);
        return this.http
            .put<IRepositoryLedger>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IRepositoryLedger>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRepositoryLedger[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getListLotNoByMaterialGoodsID(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/getListLotNoByMaterialGoodsID`, { params: options, observe: 'response' });
    }

    private convertDateFromClient(repositoryLedger: IRepositoryLedger): IRepositoryLedger {
        const copy: IRepositoryLedger = Object.assign({}, repositoryLedger, {
            date: repositoryLedger.date != null && repositoryLedger.date.isValid() ? repositoryLedger.date.format(DATE_FORMAT) : null,
            postedDate:
                repositoryLedger.postedDate != null && repositoryLedger.postedDate.isValid()
                    ? repositoryLedger.postedDate.format(DATE_FORMAT)
                    : null,
            expiryDate:
                repositoryLedger.expiryDate != null && repositoryLedger.expiryDate.isValid()
                    ? repositoryLedger.expiryDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        res.body.expiryDate = res.body.expiryDate != null ? moment(res.body.expiryDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((repositoryLedger: IRepositoryLedger) => {
            repositoryLedger.date = repositoryLedger.date != null ? moment(repositoryLedger.date) : null;
            repositoryLedger.postedDate = repositoryLedger.postedDate != null ? moment(repositoryLedger.postedDate) : null;
            repositoryLedger.expiryDate = repositoryLedger.expiryDate != null ? moment(repositoryLedger.expiryDate) : null;
        });
        return res;
    }

    calculateOWPrice(calculateOWDTO: any): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceUrl}/calculate_OWPrice`, calculateOWDTO, { observe: 'response' });
    }

    updateIWPriceFromCost(calculateOWDTO: any): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceUrl}/update_IW_PriceFromCost`, calculateOWDTO, { observe: 'response' });
    }

    updateOWPriceFromCost(calculateOWDTO: any): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceUrl}/update_OW_PriceFromCost`, calculateOWDTO, { observe: 'response' });
    }

    getIWVoucher(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/get_IWVoucher`, { params: options, observe: 'response' });
    }

    getLotNoArray(materialGoodsID: string): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/lot-no-array/${materialGoodsID}`, { observe: 'response' });
    }
}
