import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMBTellerPaperDetails } from 'app/shared/model/mb-teller-paper-details.model';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';

type EntityResponseType = HttpResponse<IMBTellerPaperDetails>;
type EntityArrayResponseType = HttpResponse<IMBTellerPaperDetails[]>;

@Injectable({ providedIn: 'root' })
export class MBTellerPaperDetailsService {
    private resourceUrl = SERVER_API_URL + 'api/mb-teller-paper-details';

    constructor(private http: HttpClient) {}

    create(mBTellerPaperDetails: IMBTellerPaperDetails): Observable<EntityResponseType> {
        return this.http.post<IMBTellerPaperDetails>(this.resourceUrl, mBTellerPaperDetails, { observe: 'response' });
    }

    update(mBTellerPaperDetails: IMBTellerPaperDetails): Observable<EntityResponseType> {
        return this.http.put<IMBTellerPaperDetails>(this.resourceUrl, mBTellerPaperDetails, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<IMBTellerPaperDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMBTellerPaperDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
