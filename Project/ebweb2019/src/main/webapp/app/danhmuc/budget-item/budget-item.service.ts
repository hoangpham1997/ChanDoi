import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

import { IBudgetItem } from 'app/shared/model/budget-item.model';

type EntityResponseType = HttpResponse<IBudgetItem>;
type EntityArrayResponseType = HttpResponse<IBudgetItem[]>;

@Injectable({ providedIn: 'root' })
export class BudgetItemService {
    private resourceUrl = SERVER_API_URL + 'api/budget-items';
    constructor(private http: HttpClient) {}
    update(budgetItem: IBudgetItem): Observable<EntityResponseType> {
        return this.http.put<IBudgetItem>(this.resourceUrl, budgetItem, { observe: 'response' });
    }
    create(budgetItem: IBudgetItem): Observable<EntityResponseType> {
        return this.http.post<IBudgetItem>(this.resourceUrl, budgetItem, { observe: 'response' });
    }
    getAll(req?: any): Observable<EntityArrayResponseType> {
        const option = createRequestOption(req);
        return this.http.get<IBudgetItem[]>(this.resourceUrl, { params: option, observe: 'response' });
    }
    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
    find(id: string): Observable<EntityResponseType> {
        return this.http.get<IBudgetItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    multiDelete(obj: any[]): Observable<any> {
        return this.http.post<any>(`${this.resourceUrl}/multi-delete-budgetItem`, obj, { observe: 'response' });
    }
}
