import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMaterialQuantumDetails, MaterialQuantumDetails } from 'app/shared/model/material-quantum-details.model';
import { SAInvoiceDetails } from 'app/shared/model/sa-invoice-details.model';

type EntityResponseType = HttpResponse<IMaterialQuantumDetails>;
type EntityArrayResponseType = HttpResponse<IMaterialQuantumDetails[]>;

@Injectable({ providedIn: 'root' })
export class MaterialQuantumDetailsService {
    private resourceUrl = SERVER_API_URL + 'api/material-quantum-details';

    constructor(private http: HttpClient) {}

    create(materialQuantumDetails: IMaterialQuantumDetails): Observable<EntityResponseType> {
        return this.http.post<IMaterialQuantumDetails>(this.resourceUrl, materialQuantumDetails, { observe: 'response' });
    }

    update(materialQuantumDetails: IMaterialQuantumDetails): Observable<EntityResponseType> {
        return this.http.put<IMaterialQuantumDetails>(this.resourceUrl, materialQuantumDetails, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IMaterialQuantumDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMaterialQuantumDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findAllDetailsById(req: any): Observable<HttpResponse<MaterialQuantumDetails[]>> {
        const options = createRequestOption(req);
        return this.http.get<MaterialQuantumDetails[]>(`${this.resourceUrl}/details`, { params: options, observe: 'response' });
    }
}
