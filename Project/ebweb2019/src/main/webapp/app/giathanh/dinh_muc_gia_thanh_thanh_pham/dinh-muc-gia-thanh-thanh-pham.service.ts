import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { ICPProductQuantum } from 'app/shared/model/cp-product-quantum.model';

type EntityResponseType = HttpResponse<ICPProductQuantum[]>;

@Injectable({ providedIn: 'root' })
export class DinhMucGiaThanhThanhPhamService {
    private resourceUrl = SERVER_API_URL + 'api/c-p-product-quantums';

    constructor(private http: HttpClient) {}

    // create(materialGoods: IMaterialGoods): Observable<EntityResponseType> {
    //     return this.http.post<IMaterialGoods>(this.resourceUrl, materialGoods, { observe: 'response' });
    // }
    //
    // update(materialGoods: IMaterialGoods): Observable<EntityResponseType> {
    //     return this.http.put<IMaterialGoods>(this.resourceUrl, materialGoods, { observe: 'response' });
    // }

    save(cPProductQuantums: ICPProductQuantum[]): Observable<EntityResponseType> {
        return this.http.put<ICPProductQuantum[]>(`${this.resourceUrl}/save-all`, cPProductQuantums, { observe: 'response' });
    }

    // find(id: string): Observable<EntityResponseType> {
    //     return this.http.get<IMaterialGoods>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    // }
    //
    // query(req?: any): Observable<EntityArrayResponseType> {
    //     const options = createRequestOption(req);
    //     return this.http.get<IMaterialGoods[]>(this.resourceUrl, { params: options, observe: 'response' });
    // }
    //
    // delete(id: string): Observable<HttpResponse<any>> {
    //     return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    // }

    // findByCompanyID(req?: any): Observable<EntityArrayResponseType> {
    //     const options = createRequestOption(req);
    //     return this.http.get<IMaterialGoods[]>(`${this.resourceUrl}/company`, { params: options, observe: 'response' });
    // }
}
