import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

import { IUnit } from 'app/shared/model/unit.model';
import { IAccountDefault } from 'app/shared/model/account-default.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { IBank } from 'app/shared/model/bank.model';
import { IAccountAllList } from 'app/shared/model/account-all-list.model';
import { IOrganizationUnitOptionReport } from 'app/shared/model/organization-unit-option-report.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';

type EntityResponseType = HttpResponse<IExpenseItem>;
type EntityArrayResponseType = HttpResponse<IExpenseItem[]>;
type EntityArrayResponseAccountType = HttpResponse<IExpenseItem>;

@Injectable({ providedIn: 'root' })
export class KhoanMucChiPhiListService {
    private resourceUrl = SERVER_API_URL + 'api/expense-items';

    constructor(private http: HttpClient) {}

    create(expenseItem: IExpenseItem): Observable<EntityResponseType> {
        return this.http.post<IExpenseItem>(this.resourceUrl, expenseItem, { observe: 'response' });
    }

    update(expenseItem: IExpenseItem): Observable<EntityResponseType> {
        return this.http.put<IExpenseItem>(this.resourceUrl, expenseItem, { observe: 'response' });
    }

    find(id: any): Observable<EntityResponseType> {
        return this.http.get<IExpenseItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IExpenseItem[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IExpenseItem[]>(`${this.resourceUrl}/search-all`, { params: options, observe: 'response' });
    }

    getAccountType(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IExpenseItem[]>(this.resourceUrl + '/getAllAccountType', {
            params: options,
            observe: 'response'
        });
    }

    /**
     * @author phuonghv find Account List by Account type
     * @param ppServiceType 240: PP_SERVICE_UNPAID, 241: PP_SERVICE_CASH,
     *                     242: PP_SERVICE_PAYMENT_ORDER, 243: PPSERVICE_CHECK_TRANSFER
     *                     244: PP_SERVICE_CREDIT_CARD, 245: PPSERVICE_CASH_CHECK
     * @param accountType 0 - debit , 1 - credit
     * @return
     */
    findAllByCompanyID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IExpenseItem[]>(`${this.resourceUrl}/expense-companyid`, {
            params: options,
            observe: 'response'
        });
    }

    getAllByCompanyID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IExpenseItem[]>(`${this.resourceUrl}/getexpense-companyid`, {
            params: options,
            observe: 'response'
        });
    }

    findAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IExpenseItem[]>(`${this.resourceUrl}/getAllExpenseItems`, {
            params: options,
            observe: 'response'
        });
    }

    getAllExpenseItems(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IExpenseItem[]>(this.resourceUrl + '/getAllExpenseItems', {
            params: options,
            observe: 'response'
        });
    }

    getAccountLists(): Observable<EntityArrayResponseType> {
        return this.http.get<IExpenseItem[]>(this.resourceUrl + '/find-all-account-lists-companyid', { observe: 'response' });
    }

    findAllByAndCompanyID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IExpenseItem[]>(`${this.resourceUrl}/expense-findallAndcompanyid`, {
            params: options,
            observe: 'response'
        });
    }

    multiDelete(obj: any[]): Observable<any> {
        return this.http.post<any>(`${this.resourceUrl}/multi-delete-expenseItem`, obj, { observe: 'response' });
    }

    getExpenseItemSimilarBranch(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceUrl + '/find-all-expense-item-by-company-id-similar-branch', {
            params: options,
            observe: 'response'
        });
    }
}
