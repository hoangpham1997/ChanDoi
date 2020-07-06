import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { SystemFieldsModel } from 'app/shared/modal/import-excel-danh-muc/systemFields.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { map } from 'rxjs/operators';
import { createRequestOption } from 'app/shared';

@Injectable({ providedIn: 'root' })
export class ImportExcelDanhMucService {
    private resourceUrl = SERVER_API_URL + 'api/category';
    constructor(private http: HttpClient) {}
    uploadFileAndGetSheetNames(file: any): Observable<any> {
        const formData = new FormData();
        if (file) {
            formData.append('file', file, file.name);
        }
        // @ts-ignore
        return this.http.post<any>(`${this.resourceUrl}/get-fields-excel`, formData, { observe: 'response' });
    }

    uploadFile(file: any, systemFieldsModel: any): Observable<any> {
        const formData = new FormData();
        // formData.append('systemFieldsModel', systemFieldsModel);
        if (file) {
            formData.append('file', file, file.name);
        }
        formData.append('updateDataJson', JSON.stringify(systemFieldsModel));
        // @ts-ignore
        return this.http.post<any>(`${this.resourceUrl}/valid-data`, formData, { observe: 'response', responseType: 'blob' });
    }

    acceptData(req: any, type: number): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceUrl}/accept-data/${type}`, req, { observe: 'response' });
    }

    checkReference(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/check-reference`, {
            params: options,
            observe: 'response'
        });
    }

    downloadTem(req: any) {
        const options = createRequestOption(req);
        // @ts-ignore
        return this.http.post<any>(`${this.resourceUrl}/temp/download`, null, {
            params: options,
            observe: 'response',
            responseType: 'blob'
        });
    }
}
