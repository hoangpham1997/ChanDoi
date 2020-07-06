import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_FORMAT_SLASH } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { AccountingObjectDTO, IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IPPPayVendorBill } from 'app/shared/model/pp-pay-vendor-bill';
import { IPPPayVendor } from 'app/shared/model/pp-pay-vendor';
import { ISAReceiptDebit } from 'app/shared/model/sa-receipt-debit';
import { ISAReceiptDebitBill } from 'app/shared/model/sa-receipt-debit-bill';
import { IAccountDefaultCategory } from 'app/shared/model/account-default-category.model';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';

type EntityResponseType = HttpResponse<IAccountingObject>;
type EntityArrayResponseType = HttpResponse<IAccountingObject[]>;

@Injectable({ providedIn: 'root' })
export class AccountingObjectService {
    private resourceUrl = SERVER_API_URL + 'api/accounting-objects';
    private resourceUrlDTO = SERVER_API_URL + 'api/accounting-objectsDTO';
    private resourceUrlFindAccountObjectByTaskMethod = SERVER_API_URL + 'api/find-accounting-objectsDTOs';
    private resourceUrlFindAccountObjectByTaskMethodAll = SERVER_API_URL + 'api/find-accounting-objectsDTOs-all';
    private resourceUrlFindAccountObjectByGroupID = SERVER_API_URL + 'api/find-accounting-objects-by-group-id';
    constructor(private http: HttpClient) {}

    create(accountingObject: IAccountingObject): Observable<EntityResponseType> {
        // const copy = this.convertDateFromClient(accountingObject);
        return this.http
            .post<IAccountingObject>(this.resourceUrl, accountingObject, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(accountingObject: IAccountingObject): Observable<EntityResponseType> {
        // const copy = this.convertDateFromClient(accountingObject);
        return this.http
            .put<IAccountingObject>(this.resourceUrl, accountingObject, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: any): Observable<EntityResponseType> {
        return this.http
            .get<IAccountingObject>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAccountingObject[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    pageableAccountingObjects(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAccountingObject[]>(this.resourceUrl + '/pageable-all-accounting-objects', {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAccountingObject[]>(this.resourceUrl + '/accounting-object-search-all', {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    searchAllEmployee(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAccountingObject[]>(this.resourceUrl + '/employee-search-all', {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAccountingObjectsForProvider(): Observable<HttpResponse<AccountingObjectDTO[]>> {
        return this.http
            .get<IAccountingObject[]>(this.resourceUrl + '/provider', { observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAccountingObjectsActive(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/is-active', { params: options, observe: 'response' });
    }

    getAccountingObjectsActiveForRSInwardOutward(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/rs-inwardoutward/is-active', {
            params: options,
            observe: 'response'
        });
    }

    getAllAccountingObjectsForRS(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/rs-inwardoutward/get-all', {
            params: options,
            observe: 'response'
        });
    }

    getAccountingObjectsRSOutward(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/rs-outward/is-active', {
            params: options,
            observe: 'response'
        });
    }

    getAllAccountingObjectsRSOutward(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/rs-outward/get-all', {
            params: options,
            observe: 'response'
        });
    }

    getAccountingObjectsForEmployee(req?: any): Observable<HttpResponse<AccountingObjectDTO[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<IAccountingObject[]>(this.resourceUrl + '/employee', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAllEmployee(req?: any): Observable<HttpResponse<AccountingObjectDTO[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<IAccountingObject[]>(this.resourceUrl + '/employees', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAllAccountingObjects(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAccountingObject[]>(this.resourceUrl + '/getAllAccountingObjects', {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getAllDTO(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAccountingObject[]>(this.resourceUrlDTO, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAllAccountingObjectDTO(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAccountingObject[]>(this.resourceUrlDTO + '/getAll', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAccountingObjectDTOByTaskMethod(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAccountingObject[]>(this.resourceUrlFindAccountObjectByTaskMethod, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAccountingObjectDTOByTaskMethodAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAccountingObject[]>(this.resourceUrlFindAccountObjectByTaskMethodAll, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(accountingObject: IAccountingObject): IAccountingObject {
        const copy: IAccountingObject = Object.assign({}, accountingObject, {
            issueDate:
                accountingObject.issueDate != null && accountingObject.issueDate.isValid()
                    ? accountingObject.issueDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.issueDate = res.body.issueDate != null ? moment(res.body.issueDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((accountingObject: IAccountingObject) => {
            accountingObject.employeeBirthday =
                accountingObject.employeeBirthday != null ? moment(accountingObject.employeeBirthday) : null;
            accountingObject.issueDate = accountingObject.issueDate != null ? moment(accountingObject.issueDate) : null;
        });
        return res;
    }

    getAccountingObjectActive(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountingObject[]>(SERVER_API_URL + 'api/accounting-objects-active', {
            params: options,
            observe: 'response'
        });
    }

    getAccountingObjectsIsActive(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/is-active-transfer', { params: options, observe: 'response' });
    }

    getAccountingObjectByGroupID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountingObject[]>(this.resourceUrlFindAccountObjectByGroupID + '/by-id', {
            params: options,
            observe: 'response'
        });
    }

    getAccountingObjectByGroupIDKH(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountingObject[]>(this.resourceUrl + '/customer-id', {
            params: options,
            observe: 'response'
        });
    }

    getAccountingObjectByGroupIDSimilarBranch(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountingObject[]>(this.resourceUrlFindAccountObjectByGroupID + '/by-id-similar-branch', {
            params: options,
            observe: 'response'
        });
    }

    getAccountingObjectByGroupIDKHSimilarBranch(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountingObject[]>(this.resourceUrl + '/customer-id-similar-branch', {
            params: options,
            observe: 'response'
        });
    }

    getByAllAccountingObjectActive(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountingObject[]>(SERVER_API_URL + 'api/accounting-objects-all-active', {
            params: options,
            observe: 'response'
        });
    }

    getAccountingObjectsRSTransfer(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/rs-transfer/is-active', {
            params: options,
            observe: 'response'
        });
    }

    getAccountingObjectEmployee(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountingObject[]>(SERVER_API_URL + 'api/accounting-objects-employee', {
            params: options,
            observe: 'response'
        });
    }

    getPPPayVendorBills(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<IPPPayVendorBill[]>(`${this.resourceUrl}/getPPPayVendorBill`, {
            params: options,
            observe: 'response'
        });
        // .pipe(map((res: HttpResponse<any[]>) => this.convertDateArrayFromServerSA(res)));
    }

    getPPPayVendors(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<IPPPayVendor[]>(`${this.resourceUrl}/getPPPayVendor`, {
            params: options,
            observe: 'response'
        });
    }

    getSAReceiptDebitBills(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<ISAReceiptDebitBill[]>(`${this.resourceUrl}/getSAReceiptDebitBill`, {
            params: options,
            observe: 'response'
        });
        // .pipe(map((res: HttpResponse<any[]>) => this.convertDateArrayFromServerSA(res)));
    }

    getSAReceiptDebits(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<ISAReceiptDebit[]>(`${this.resourceUrl}/getSAReceiptDebit`, {
            params: options,
            observe: 'response'
        });
    }

    private convertDateArrayFromServerSA(res: HttpResponse<any[]>): HttpResponse<any[]> {
        res.body.forEach((object: any) => {
            object.date = object.date != null ? moment(object.date, DATE_FORMAT) : null;
            object.dueDate = object.dueDate != null ? moment(object.dueDate, DATE_FORMAT) : null;
        });
        return res;
    }

    getAccountingObjectByCompanyID(): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountDefaultCategory[]>(this.resourceUrl + '/find-all-accounting-objects-by-companyid', {
            observe: 'response'
        });
    }

    findOneWithPageable(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAccountingObject>(this.resourceUrl + '/find-one-with-pageable', { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    deleteByListID(rq: any[]): Observable<HttpResponse<HandlingResult>> {
        return this.http.post<any>(`${this.resourceUrl}/delete-list-employee`, rq, { observe: 'response' });
    }

    deleteByListIDAcc(rq: any[]): Observable<HttpResponse<HandlingResult>> {
        return this.http.post<any>(`${this.resourceUrl}/delete-list`, rq, { observe: 'response' });
    }

    getAccountingObjectActiveByReportRequest(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountingObject[]>(SERVER_API_URL + 'api/accounting-objects-active-by-report-request', {
            params: options,
            observe: 'response'
        });
    }

    getAccountingObjectEmployeeSimilarBranch(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountingObject[]>(SERVER_API_URL + 'api/accounting-objects-employee-similar-branch', {
            params: options,
            observe: 'response'
        });
    }

    getAllAccountingObjectsByCompanyID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountingObject[]>(SERVER_API_URL + 'api/get-all-accounting-objects-by-selected-companyID', {
            params: options,
            observe: 'response'
        });
    }

    findAllAccountingObjectByCompany(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISAReceiptDebit[]>(`${this.resourceUrl}/find-all-accounting-object-by-company`, {
            params: options,
            observe: 'response'
        });
    }
}
