import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IExpenseItem } from 'app/shared/model/expense-item.model';

type EntityResponseType = HttpResponse<IExpenseItem>;
type EntityArrayResponseType = HttpResponse<IExpenseItem[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseItemService {
    private resourceUrl = SERVER_API_URL + 'api/expense-items';

    constructor(private http: HttpClient) {}

    create(expenseItem: IExpenseItem): Observable<EntityResponseType> {
        return this.http.post<IExpenseItem>(this.resourceUrl, expenseItem, { observe: 'response' });
    }

    update(expenseItem: IExpenseItem): Observable<EntityResponseType> {
        return this.http.put<IExpenseItem>(this.resourceUrl, expenseItem, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IExpenseItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IExpenseItem[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    getAllExpenseItems(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IExpenseItem[]>(this.resourceUrl + '/getAllExpenseItems', { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getExpenseItemActive(): Observable<EntityArrayResponseType> {
        return this.http.get<IExpenseItem[]>(this.resourceUrl + '/expense-item-active', { observe: 'response' });
    }

    getExpenseItems(): Observable<EntityArrayResponseType> {
        return this.http.get<IExpenseItem[]>(this.resourceUrl + '/find-all-expense-item-active-companyid', { observe: 'response' });
    }

    getExpenseItemsByCompanyID(): Observable<EntityArrayResponseType> {
        return this.http.get<IExpenseItem[]>(this.resourceUrl + '/find-all-expense-item-by-companyid', { observe: 'response' });
    }

    getExpenseItemByCompanyID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IExpenseItem[]>(this.resourceUrl + '/find-all-expense-item-active-by-companyID', {
            params: options,
            observe: 'response'
        });
    }

    getExpenseItemsAndDependent(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IExpenseItem[]>(this.resourceUrl + '/find-all-expense-item-active-companyid-and-is-dependent', {
            params: options,
            observe: 'response'
        });
    }
}
