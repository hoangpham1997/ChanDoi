import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_FORMAT_SLASH, DATE_FORMAT_YMD } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPrepaidExpense } from 'app/shared/model/prepaid-expense.model';
import { IAccountList } from 'app/shared/model/account-list.model';

type EntityResponseType = HttpResponse<IPrepaidExpense>;
type EntityArrayResponseType = HttpResponse<IPrepaidExpense[]>;

@Injectable({ providedIn: 'root' })
export class PrepaidExpenseService {
    private resourceUrl = SERVER_API_URL + 'api/prepaid-expenses';

    constructor(private http: HttpClient) {}

    create(prepaidExpense: IPrepaidExpense): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(prepaidExpense);
        return this.http
            .post<IPrepaidExpense>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(prepaidExpense: IPrepaidExpense): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(prepaidExpense);
        return this.http
            .put<IPrepaidExpense>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPrepaidExpense>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findPrepaidExpenseByID(id: any): Observable<HttpResponse<any[]>> {
        return this.http.get<any[]>(`${this.resourceUrl}/ref/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPrepaidExpense[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }
    getPrepaidExpenseAllocation(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(this.resourceUrl + '/prepaid-expense-allocation', { params: options, observe: 'response' });
    }

    getPrepaidExpenseAllocationCount(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(this.resourceUrl + '/prepaid-expense-allocation-count', { params: options, observe: 'response' });
    }
    getPrepaidExpenseAllocationDuplicate(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(this.resourceUrl + '/prepaid-expense-allocation-duplicate', { params: options, observe: 'response' });
    }

    delete(id: any): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(prepaidExpense: IPrepaidExpense): IPrepaidExpense {
        const copy: IPrepaidExpense = Object.assign({}, prepaidExpense, {
            date: prepaidExpense.date != null && prepaidExpense.date.isValid() ? prepaidExpense.date.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((prepaidExpense: IPrepaidExpense) => {
            prepaidExpense.date = prepaidExpense.date != null ? moment(prepaidExpense.date) : null;
        });
        return res;
    }

    getPrepaidExpenseAllocationByID(id: any) {
        return this.http.get<any[]>(`${this.resourceUrl}/prepaid-expense-allocation-by-id/${id}`, { observe: 'response' });
    }

    getPrepaidExpenseByCurrentBookToModal(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http
            .get<any[]>(this.resourceUrl + '/get-prepaid-expense-by-current-book-to-modal', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getPrepaidExpenses(): Observable<EntityArrayResponseType> {
        return this.http.get<IPrepaidExpense[]>(this.resourceUrl + '/find-by-company-id', {
            observe: 'response'
        });
    }

    private convertDateFromServerVoucher(res: HttpResponse<any[]>) {
        for (let i = 0; i < res.body.length; i++) {
            res.body[i].date = res.body[i].date != null ? moment(res.body[i].date, DATE_FORMAT_SLASH) : null;
            res.body[i].posteddate = res.body[i].posteddate != null ? moment(res.body[i].posteddate, DATE_FORMAT_SLASH) : null;
        }
        return res;
    }
}
