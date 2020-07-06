import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { createRequestOption } from 'app/shared';

@Injectable({ providedIn: 'root' })
export class SaInvoiceOutwardModalService {
    private resourceUrl = SERVER_API_URL + 'api/sa-invoice';

    constructor(private http: HttpClient) {}

    find(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(`${this.resourceUrl}/search-all-dto`, { params: options, observe: 'response' });
    }
}
