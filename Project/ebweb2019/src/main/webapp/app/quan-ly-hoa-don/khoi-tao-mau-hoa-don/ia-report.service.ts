import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IIAReport } from 'app/shared/model/ia-report.model';
import { IAInvoiceTemplate } from 'app/shared/model/ia-invoice-template.model';
import { IIARegisterInvoice } from 'app/shared/model/ia-register-invoice.model';

type EntityResponseType = HttpResponse<IIAReport>;
type EntityArrayResponseType = HttpResponse<IIAReport[]>;

@Injectable({ providedIn: 'root' })
export class IAReportService {
    private resourceUrl = SERVER_API_URL + 'api/ia-reports';

    constructor(private http: HttpClient) {}

    create(iAReport: IIAReport): Observable<EntityResponseType> {
        return this.http.post<IIAReport>(this.resourceUrl, iAReport, { observe: 'response' });
    }

    update(iAReport: IIAReport): Observable<EntityResponseType> {
        return this.http.put<IIAReport>(this.resourceUrl, iAReport, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<IIAReport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IIAReport[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    queryStatus(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IIAReport[]>(`${this.resourceUrl}/ia-reports-status`, { params: options, observe: 'response' });
    }

    queryInvoiceType(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>('api/invoice-types', { params: options, observe: 'response' });
    }

    queryIAInvoiceTemplate(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>('api/ia-invoice-templates', { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    export(type: 'excel' | 'pdf') {
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(`${this.resourceUrl}/export/${type}`, {
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    findByIds(req?: any) {
        const options = createRequestOption(req);
        return this.http.get<any[]>(`${this.resourceUrl}/ids`, { params: options, observe: 'response' });
    }

    checkPublished(req?: any) {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/check-published`, { params: options, observe: 'response' });
    }

    previewInvoiceTemplate(req?: any) {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/pdf');
        const respone = this.http.get(`${this.resourceUrl}/preview-invoice-template`, {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
        respone.subscribe(response => {
            // this.showReport(response);
            const file = new Blob([response.body], { type: 'application/pdf' });
            const fileURL = window.URL.createObjectURL(file);
            const contentDispositionHeader = response.headers.get('Content-Disposition');
            const result = contentDispositionHeader
                .split(';')[1]
                .trim()
                .split('=')[1];
            const newWin = window.open(fileURL, '_blank');
            // add a load listener to the window so that the title gets changed on page load

            newWin.addEventListener('load', () => {
                newWin.document.title = result.replace(/"/g, '');
                // this.router.navigate(['/report/buy']);
            });
        });
    }

    multiDelete(obj: IAReportService[]): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceUrl}/multi-delete-ia-report-invoice`, obj, { observe: 'response' });
    }
}
