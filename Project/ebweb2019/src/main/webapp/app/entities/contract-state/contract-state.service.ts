import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IContractState } from 'app/shared/model/contract-state.model';

type EntityResponseType = HttpResponse<IContractState>;
type EntityArrayResponseType = HttpResponse<IContractState[]>;

@Injectable({ providedIn: 'root' })
export class ContractStateService {
    private resourceUrl = SERVER_API_URL + 'api/contract-states';

    constructor(private http: HttpClient) {}

    create(contractState: IContractState): Observable<EntityResponseType> {
        return this.http.post<IContractState>(this.resourceUrl, contractState, { observe: 'response' });
    }

    update(contractState: IContractState): Observable<EntityResponseType> {
        return this.http.put<IContractState>(this.resourceUrl, contractState, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IContractState>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IContractState[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
