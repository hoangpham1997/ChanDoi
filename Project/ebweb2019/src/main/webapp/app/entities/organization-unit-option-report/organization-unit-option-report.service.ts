import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOrganizationUnitOptionReport } from 'app/shared/model/organization-unit-option-report.model';
import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';
import { map } from 'rxjs/operators';

type EntityResponseType = HttpResponse<IOrganizationUnitOptionReport>;
type EntityArrayResponseType = HttpResponse<IOrganizationUnitOptionReport[]>;

@Injectable({ providedIn: 'root' })
export class OrganizationUnitOptionReportService {
    private resourceUrl = SERVER_API_URL + 'api/organization-unit-option-reports';

    constructor(private http: HttpClient) {}

    create(organizationUnitOptionReport: IOrganizationUnitOptionReport): Observable<EntityResponseType> {
        return this.http.post<IOrganizationUnitOptionReport>(this.resourceUrl, organizationUnitOptionReport, { observe: 'response' });
    }

    update(organizationUnitOptionReport: IOrganizationUnitOptionReport): Observable<EntityResponseType> {
        return this.http.put<IOrganizationUnitOptionReport>(this.resourceUrl, organizationUnitOptionReport, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IOrganizationUnitOptionReport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findByOrganizationUnitID(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOrganizationUnitOptionReport>(`${this.resourceUrl}/find-by-organization-unit`, {
            params: options,
            observe: 'response'
        });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOrganizationUnitOptionReport[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
