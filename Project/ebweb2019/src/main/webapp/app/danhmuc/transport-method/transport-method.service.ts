import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

import { ITransportMethod } from 'app/shared/model/transport-method.model';

type EntityResponseType = HttpResponse<ITransportMethod>;
type EntityArrayResponseType = HttpResponse<ITransportMethod[]>;

@Injectable({ providedIn: 'root' })
export class TransportMethodService {
    private resourceUrl = SERVER_API_URL + 'api/transport-methods';
    constructor(private http: HttpClient) {}
    update(transportMethod: ITransportMethod): Observable<EntityResponseType> {
        return this.http.put<ITransportMethod>(this.resourceUrl, transportMethod, { observe: 'response' });
    }
    create(transportMethod: ITransportMethod): Observable<EntityResponseType> {
        return this.http.post<ITransportMethod>(this.resourceUrl, transportMethod, { observe: 'response' });
    }
    getAll(req?: any): Observable<EntityArrayResponseType> {
        const option = createRequestOption(req);
        return this.http.get<ITransportMethod[]>(this.resourceUrl, { params: option, observe: 'response' });
    }
    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
    find(id: string): Observable<EntityResponseType> {
        return this.http.get<ITransportMethod>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getTransportMethodCombobox(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ITransportMethod[]>(this.resourceUrl + '/combobox', {
            params: options,
            observe: 'response'
        });
    }
}
