import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { createRequestOption } from 'app/shared';
import { IOpAccountModel, OpAccountModel } from 'app/shared/model/op-account.model';
import { OpMaterialGoodsModel } from 'app/shared/model/op-material-goods.model';
import { IAccountList } from 'app/shared/model/account-list.model';

@Injectable({ providedIn: 'root' })
export class SoDuDauKyService {
    private resourceOPAccountUrl = SERVER_API_URL + 'api/op-account';
    private resourceOPMaterialGoodsUrl = SERVER_API_URL + 'api/op-material-goods';

    private accountList: IAccountList;
    private accountLists: IAccountList[];
    private accountingType: string[];

    getAccountingType(): string[] {
        return this.accountingType || [];
    }

    setAccountingType(value: string[]) {
        this.accountingType = value;
    }

    setAccountLists(value: IAccountList[]) {
        this.accountLists = value;
    }

    getAccountLists(): IAccountList[] {
        return this.accountLists || [];
    }

    setAccountList(value: IAccountList) {
        this.accountList = value;
    }

    getAccountList(): IAccountList {
        return this.accountList || {};
    }

    constructor(private http: HttpClient) {}

    createOPAccount(req: any): Observable<HttpResponse<any>> {
        return this.http.post<any>(this.resourceOPAccountUrl, req, { observe: 'response' });
    }

    createOPMaterialGoods(req: any): Observable<HttpResponse<any>> {
        return this.http.post<any>(this.resourceOPMaterialGoodsUrl, req, { observe: 'response' });
    }

    findAllOpAccounts(req: any): Observable<HttpResponse<OpAccountModel[]>> {
        const options = createRequestOption(req);
        return this.http.get<OpAccountModel[]>(this.resourceOPAccountUrl + '/find-all', { params: options, observe: 'response' });
    }

    getCheckHaveData(req: any): Observable<HttpResponse<boolean>> {
        const options = createRequestOption(req);
        return this.http.get<boolean>(this.resourceOPAccountUrl + '/check-have-data', { params: options, observe: 'response' });
    }

    getCheckReferenceData(req: any): Observable<HttpResponse<any>> {
        return this.http.post<boolean>(this.resourceOPAccountUrl + '/check-reference-data', req, { observe: 'response' });
    }

    acceptedOPAccount(req: any, type: number): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceOPAccountUrl}/upload/accepted/${type}`, req, { observe: 'response' });
    }

    acceptedOPMaterialGoods(req: any): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceOPMaterialGoodsUrl}/upload/accepted`, req, { observe: 'response' });
    }

    getCheckHaveDataMG(): Observable<HttpResponse<boolean>> {
        return this.http.get<boolean>(this.resourceOPMaterialGoodsUrl + '/check-have-data', { observe: 'response' });
    }
    findAllOPMaterialGoods(req: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<OpMaterialGoodsModel[]>(this.resourceOPMaterialGoodsUrl + '/find-all', {
            params: options,
            observe: 'response'
        });
    }

    deleteOPAccount(req: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.delete<any>(`${this.resourceOPAccountUrl}`, { params: options, observe: 'response' });
    }
    deleteOPMaterialGoods(req: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.delete<any>(`${this.resourceOPMaterialGoodsUrl}`, { params: options, observe: 'response' });
    }

    exportPDFAccountNormal(req: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceOPAccountUrl + '/export/pdf/normal', {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    exportPdfAccountingObject(req: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceOPAccountUrl + '/export/pdf/accounting-object', {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    exportPdfMaterialGoods(req: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceOPMaterialGoodsUrl + '/export/pdf', {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    downloadTem(req: any) {
        const options = createRequestOption(req);
        // @ts-ignore
        return this.http.post<any>(`${this.resourceOPAccountUrl}/temp/download`, null, {
            params: options,
            observe: 'response',
            responseType: 'blob'
        });
    }

    uploadAccountNormal(file: any, sheetName?: string): Observable<any> {
        const formData = new FormData();
        if (file) {
            formData.append('file', file, file.name);
        }
        // @ts-ignore
        return this.http.post<any>(`${this.resourceOPAccountUrl}/upload/normal${sheetName ? '/' + sheetName : ''}`, formData, {
            observe: 'response',
            responseType: 'blob'
        });
    }

    uploadAccountingObject(file: any, sheetName?: string): Observable<any> {
        const formData = new FormData();
        if (file) {
            formData.append('file', file, file.name);
        }
        // @ts-ignore
        return this.http.post<any>(`${this.resourceOPAccountUrl}/upload/accounting-object${sheetName ? '/' + sheetName : ''}`, formData, {
            observe: 'response',
            responseType: 'blob'
        });
    }
    uploadMaterialGoods(file: any, sheetName?: string): Observable<any> {
        const formData = new FormData();
        if (file) {
            formData.append('file', file, file.name);
        }
        // @ts-ignore
        return this.http.post<any>(`${this.resourceOPMaterialGoodsUrl}/upload${sheetName ? '/' + sheetName : ''}`, formData, {
            observe: 'response',
            responseType: 'blob'
        });
    }
}
