import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPPInvoice } from 'app/shared/model/pp-invoice.model';
import { IReceiveBill } from 'app/shared/model/receive-bill.model';

type EntityResponseList = HttpResponse<string[]>;
type EntityResponseReceiveBill = HttpResponse<IReceiveBill>;
type EntityResponseArrayReceiveBill = HttpResponse<IReceiveBill[]>;
type EntityResponseType = HttpResponse<IPPInvoice>;
type EntityArrayResponseType = HttpResponse<IPPInvoice[]>;
const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
const EXCEL_EXTENSION = '.xlsx';

@Injectable({ providedIn: 'root' })
export class NhanHoaDonService {
    private resourceUrl = SERVER_API_URL + 'api/pp-invoices';
    private resourceUrlPPService = SERVER_API_URL + 'api/ppService';
    private resourceUrlPPServiceDetail = SERVER_API_URL + 'api/ppServiceDetail';
    private resourceUrlDetails = SERVER_API_URL + 'api/pp-invoices-details';

    constructor(private http: HttpClient) {}

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((ppInvoice: IPPInvoice) => {
            ppInvoice.date = ppInvoice.date != null ? moment(ppInvoice.date) : null;
            ppInvoice.postedDate = ppInvoice.postedDate != null ? moment(ppInvoice.postedDate) : null;
        });
        return res;
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPPInvoice[]>(`${this.resourceUrl}/search-all`, {
            params: options,
            observe: 'response'
        });
    }

    findAllPPServices(req?: any): Observable<EntityResponseArrayReceiveBill> {
        return this.http.get<IReceiveBill[]>(`${this.resourceUrlPPService}/find-all-receive-bill-pPService`, {
            params: req,
            observe: 'response'
        });
    }

    findAllPPInvoices(req?: any): Observable<EntityResponseArrayReceiveBill> {
        return this.http.get<IReceiveBill[]>(`${this.resourceUrl}/find-all-receive-bill-ppInvoice`, { params: req, observe: 'response' });
    }

    findAll(req?: any): Observable<EntityResponseArrayReceiveBill> {
        return this.http.get<IReceiveBill[]>(`${this.resourceUrl}/find-all-receive-bill`, { params: req, observe: 'response' });
    }

    updatePPInvoiceDetails(receiveBill: IReceiveBill): Observable<EntityResponseReceiveBill> {
        return this.http.put<IReceiveBill>(`${this.resourceUrlDetails}/update-receive-bill`, receiveBill, { observe: 'response' });
    }

    updateAll(receiveBill: IReceiveBill): Observable<EntityResponseReceiveBill> {
        return this.http.put<IReceiveBill>(`${this.resourceUrlDetails}/update-all-receive-bill`, receiveBill, { observe: 'response' });
    }

    updatePPInvoices(listIDPPInvoices: string[]): Observable<EntityResponseList> {
        return this.http.put<string[]>(`${this.resourceUrl}/update-receive-bill`, listIDPPInvoices, { observe: 'response' });
    }

    updatePPServiceDetails(receiveBill: IReceiveBill): Observable<EntityResponseReceiveBill> {
        return this.http.put<IReceiveBill>(`${this.resourceUrlPPServiceDetail}/update-receive-bill`, receiveBill, { observe: 'response' });
    }

    updatePPServices(listIDPPInvoices: string[]): Observable<EntityResponseList> {
        return this.http.put<string[]>(`${this.resourceUrlPPService}/update-receive-bill`, listIDPPInvoices, { observe: 'response' });
    }
}
