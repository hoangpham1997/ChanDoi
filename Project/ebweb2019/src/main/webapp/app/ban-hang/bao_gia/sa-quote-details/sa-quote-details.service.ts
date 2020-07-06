import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISAQuoteDetails } from 'app/shared/model/sa-quote-details.model';

type EntityResponseType = HttpResponse<ISAQuoteDetails>;
type EntityArrayResponseType = HttpResponse<ISAQuoteDetails[]>;

@Injectable({ providedIn: 'root' })
export class SAQuoteDetailsService {
    private resourceUrl = SERVER_API_URL + 'api/sa-quote-details';

    constructor(private http: HttpClient) {}

    create(sAQuoteDetails: ISAQuoteDetails): Observable<EntityResponseType> {
        return this.http.post<ISAQuoteDetails>(this.resourceUrl, sAQuoteDetails, { observe: 'response' });
    }

    update(sAQuoteDetails: ISAQuoteDetails): Observable<EntityResponseType> {
        return this.http.put<ISAQuoteDetails>(this.resourceUrl, sAQuoteDetails, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<ISAQuoteDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISAQuoteDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
