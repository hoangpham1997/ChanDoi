import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPPPayVendor } from 'app/shared/model/pp-pay-vendor';
import { ISAReceiptDebit } from 'app/shared/model/sa-receipt-debit';

type EntityResponseType = HttpResponse<ISAReceiptDebit>;
type EntityArrayResponseType = HttpResponse<ISAReceiptDebit[]>;

@Injectable({ providedIn: 'root' })
export class PPPayVendorService {
    private resourceUrl = SERVER_API_URL + 'api/sa-receipt-debit';

    constructor(private http: HttpClient) {}

    find(id: any): Observable<EntityResponseType> {
        return this.http.get<ISAReceiptDebit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISAReceiptDebit[]>(this.resourceUrl, { params: options, observe: 'response' });
    }
}
