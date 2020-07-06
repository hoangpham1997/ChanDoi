import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { IPPOrderDto } from 'app/shared/modal/pp-order/pp-order-dto.model';
import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { createRequestOption } from 'app/shared';
import { ICostVouchersDTO } from 'app/shared/modal/cost-vouchers/cost-vouchers-dto.model';

type EntityArrayResponseType = HttpResponse<IPPOrderDto[]>;
type EntityArrayResponseTypeSer = HttpResponse<ICostVouchersDTO[]>;

@Injectable({ providedIn: 'root' })
export class CostVouchersModalService {
    private resourceUrl = SERVER_API_URL + 'api/pporders';

    constructor(private http: HttpClient) {}

    find(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPPOrderDto[]>(`${this.resourceUrl}/search-all-dto`, { params: options, observe: 'response' });
    }

    findCostVouchers(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<any[]>('api/ppservice/search-cost-vouchers', { params: options, observe: 'response' });
    }

    getSumAmount(req: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<any>(SERVER_API_URL + 'api/pp-invoice-detail-cost/get-sum-amount-by-ppserviceid', {
            params: options,
            observe: 'response'
        });
    }
}
