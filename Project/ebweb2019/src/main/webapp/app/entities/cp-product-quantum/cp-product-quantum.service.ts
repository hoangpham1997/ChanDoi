import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICPProductQuantum } from 'app/shared/model/cp-product-quantum.model';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';

type EntityResponseType = HttpResponse<ICPProductQuantum>;
type EntityArrayResponseType = HttpResponse<ICPProductQuantum[]>;

@Injectable({ providedIn: 'root' })
export class CPProductQuantumService {
    private resourceUrl = SERVER_API_URL + 'api/c-p-product-quantums';

    constructor(private http: HttpClient) {}

    create(cPProductQuantum: ICPProductQuantum): Observable<EntityResponseType> {
        return this.http.post<ICPProductQuantum>(this.resourceUrl, cPProductQuantum, { observe: 'response' });
    }

    update(cPProductQuantum: ICPProductQuantum): Observable<EntityResponseType> {
        return this.http.put<ICPProductQuantum>(this.resourceUrl, cPProductQuantum, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICPProductQuantum>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICPProductQuantum[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getCPProductQuantums(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICPProductQuantum[]>(this.resourceUrl + '/find-all-c-p-product-quantums-active-companyid', {
            params: options,
            observe: 'response'
        });
    }
    save(cPProductQuantums: ICPProductQuantum[]): Observable<EntityArrayResponseType> {
        return this.http.put<ICPProductQuantum[]>(`${this.resourceUrl}/save-all`, cPProductQuantums, { observe: 'response' });
    }
}
