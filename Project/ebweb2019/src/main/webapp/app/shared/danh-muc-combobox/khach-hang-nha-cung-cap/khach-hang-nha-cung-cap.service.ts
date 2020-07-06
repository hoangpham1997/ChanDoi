import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { AccountingObjectDTO, IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IPPPayVendorBill } from 'app/shared/model/pp-pay-vendor-bill';
import { IPPPayVendor } from 'app/shared/model/pp-pay-vendor';

type EntityResponseType = HttpResponse<IAccountingObject>;
type EntityArrayResponseType = HttpResponse<IAccountingObject[]>;

@Injectable({ providedIn: 'root' })
export class KhachHangNhaCungCapService {
    private resourceUrl = SERVER_API_URL + 'api/accounting-objects';
    private resourceUrlDTO = SERVER_API_URL + 'api/accounting-objectsDTO';
    private resourceUrlFindAccountObjectByTaskMethod = SERVER_API_URL + 'api/find-accounting-objectsDTOs';

    constructor(private http: HttpClient) {}

    create(accountingObject: IAccountingObject): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(accountingObject);
        return this.http
            .post<IAccountingObject>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(accountingObject: IAccountingObject): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(accountingObject);
        return this.http
            .put<IAccountingObject>(this.resourceUrl, copy, { observe: 'response' })
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

    getAccountingObjectsForProvider(req?: any): Observable<HttpResponse<AccountingObjectDTO[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<IAccountingObject[]>(this.resourceUrl + '/provider', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAccountingObjectsForEmployee(req?: any): Observable<HttpResponse<AccountingObjectDTO[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<IAccountingObject[]>(this.resourceUrl + '/employee', { params: options, observe: 'response' })
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

    getAccountingObjectDTOByTaskMethod(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAccountingObject[]>(this.resourceUrlFindAccountObjectByTaskMethod, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(accountingObject: IAccountingObject): IAccountingObject {
        const copy: IAccountingObject = Object.assign({}, accountingObject, {
            employeebirthday:
                accountingObject.employeeBirthday != null && accountingObject.employeeBirthday.isValid()
                    ? accountingObject.employeeBirthday.format(DATE_FORMAT)
                    : null,
            issuedate:
                accountingObject.issueDate != null && accountingObject.issueDate.isValid()
                    ? accountingObject.issueDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.employeeBirthday = res.body.employeeBirthday != null ? moment(res.body.employeeBirthday) : null;
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

    getAccountingObjectEmployee(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountingObject[]>(SERVER_API_URL + 'api/accounting-objects-employee', {
            params: options,
            observe: 'response'
        });
    }
    getPPPayVendorBills(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<IPPPayVendorBill[]>(`${this.resourceUrl}/getPPPayVendorBill`, { params: options, observe: 'response' });
    }

    getPPPayVendors(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<IPPPayVendor[]>(`${this.resourceUrl}/getPPPayVendor`, { params: options, observe: 'response' });
    }
}
