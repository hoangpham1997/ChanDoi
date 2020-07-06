import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_SECOND_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPPDiscountReturn, PPDiscountReturn } from 'app/shared/model/pp-discount-return.model';
import { IRSInwardOutward } from 'app/shared/model/rs-inward-outward.model';
import { ISaBillDetails } from 'app/shared/model/sa-bill-details.model';
import { ISaBill } from 'app/shared/model/sa-bill.model';
import { IPPDiscountReturnDetails } from 'app/shared/model/pp-discount-return-details.model';

type EntityResponseType = HttpResponse<IPPDiscountReturn>;
type EntityArrayResponseType = HttpResponse<IPPDiscountReturn[]>;

@Injectable({ providedIn: 'root' })
export class PPDiscountReturnService {
    private resourceUrl = SERVER_API_URL + 'api/pp-discount-returns';

    constructor(private http: HttpClient) {}

    create(ref?: any): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClientDTO(ref);
        return this.http
            .post<any>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServerSave(res)));
    }

    update(ref?: any): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromClientDTO(ref);
        return this.http
            .put<any>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServerSave(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IPPDiscountReturn>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findByRowNum(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<PPDiscountReturn>(`${this.resourceUrl}/index`, { params: options, observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPPDiscountReturn[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    multipleDelete(obj: any): Observable<HttpResponse<any>> {
        return this.http.post<any>(`${this.resourceUrl}/multiple-delete`, obj, { observe: 'response' });
    }

    delete(id: any): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromServerSave(res: HttpResponse<any>): EntityResponseType {
        res.body.ppDiscountReturn.date = res.body.ppDiscountReturn.date != null ? moment(res.body.ppDiscountReturn.date) : null;
        res.body.ppDiscountReturn.postedDate =
            res.body.ppDiscountReturn.postedDate != null ? moment(res.body.ppDiscountReturn.postedDate) : null;
        res.body.ppDiscountReturn.invoiceDate =
            res.body.ppDiscountReturn.invoiceDate != null ? moment(res.body.ppDiscountReturn.invoiceDate) : null;
        res.body.ppDiscountReturn.dueDate = res.body.ppDiscountReturn.dueDate != null ? moment(res.body.ppDiscountReturn.dueDate) : null;
        res.body.ppDiscountReturn.listDate = res.body.ppDiscountReturn.listDate != null ? moment(res.body.ppDiscountReturn.listDate) : null;
        if (res.body.rsInwardOutward) {
            res.body.rsInwardOutward.date = res.body.rsInwardOutward.date != null ? moment(res.body.rsInwardOutward.date) : null;
            res.body.rsInwardOutward.postedDate =
                res.body.rsInwardOutward.postedDate != null ? moment(res.body.rsInwardOutward.postedDate) : null;
        }
        if (res.body.saBill) {
            res.body.saBill.invoiceDate = res.body.saBill.invoiceDate != null ? moment(res.body.saBill.invoiceDate) : null;
            res.body.saBill.listDate = res.body.saBill.listDate != null ? moment(res.body.saBill.listDate) : null;
        }
        for (let i = 0; i < res.body.ppDiscountReturn.ppDiscountReturnDetails.length; i++) {
            res.body.ppDiscountReturn.ppDiscountReturnDetails[i].expiryDate =
                res.body.ppDiscountReturn.ppDiscountReturnDetails[i].expiryDate != null
                    ? moment(res.body.ppDiscountReturn.ppDiscountReturnDetails[i].expiryDate)
                    : null;
            res.body.ppDiscountReturn.ppDiscountReturnDetails[i].matchDate =
                res.body.ppDiscountReturn.ppDiscountReturnDetails[i].matchDate != null
                    ? moment(res.body.ppDiscountReturn.ppDiscountReturnDetails[i].matchDate)
                    : null;
        }
        // res.body.dateSendMail = res.body.dateSendMail != null ? moment(res.body.dateSendMail) : null;
        return res;
    }

    private convertDateFromClient(pPDiscountReturn: IPPDiscountReturn): IPPDiscountReturn {
        const copy: IPPDiscountReturn = Object.assign({}, pPDiscountReturn, {
            date: pPDiscountReturn.date != null ? pPDiscountReturn.date.format(DATE_FORMAT) : null,
            postedDate: pPDiscountReturn.postedDate != null ? pPDiscountReturn.postedDate.format(DATE_FORMAT) : null,
            invoiceDate: pPDiscountReturn.invoiceDate != null ? pPDiscountReturn.invoiceDate.format(DATE_FORMAT) : null,
            dueDate: pPDiscountReturn.dueDate != null ? pPDiscountReturn.dueDate.format(DATE_FORMAT) : null,
            ListDate: pPDiscountReturn.listDate != null ? pPDiscountReturn.listDate.format(DATE_FORMAT) : null,
            ppDiscountReturnDetails: pPDiscountReturn.ppDiscountReturnDetails
                ? this.convertPPDiscountReturnDetail(pPDiscountReturn.ppDiscountReturnDetails)
                : null
            // dateSendMail:
            //     pPDiscountReturn.dateSendMail != null && pPDiscountReturn.dateSendMail.isValid()
            //         ? pPDiscountReturn.dateSendMail.format(DATE_FORMAT)
            //         : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        res.body.invoiceDate = res.body.invoiceDate != null ? moment(res.body.invoiceDate) : null;
        res.body.dueDate = res.body.dueDate != null ? moment(res.body.dueDate) : null;
        res.body.listDate = res.body.listDate != null ? moment(res.body.listDate) : null;

        for (let i = 0; i < res.body.ppDiscountReturnDetails.length; i++) {
            res.body.ppDiscountReturnDetails[i].expiryDate =
                res.body.ppDiscountReturnDetails[i].expiryDate != null ? moment(res.body.ppDiscountReturnDetails[i].expiryDate) : null;
            res.body.ppDiscountReturnDetails[i].matchDate =
                res.body.ppDiscountReturnDetails[i].matchDate != null ? moment(res.body.ppDiscountReturnDetails[i].matchDate) : null;
        }
        // res.body.dateSendMail = res.body.dateSendMail != null ? moment(res.body.dateSendMail) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((pPDiscountReturn: IPPDiscountReturn) => {
            pPDiscountReturn.date = pPDiscountReturn.date != null ? moment(pPDiscountReturn.date) : null;
            pPDiscountReturn.postedDate = pPDiscountReturn.postedDate != null ? moment(pPDiscountReturn.postedDate) : null;
            pPDiscountReturn.invoiceDate = pPDiscountReturn.invoiceDate != null ? moment(pPDiscountReturn.invoiceDate) : null;
            pPDiscountReturn.dueDate = pPDiscountReturn.dueDate != null ? moment(pPDiscountReturn.dueDate) : null;
            pPDiscountReturn.listDate = pPDiscountReturn.listDate != null ? moment(pPDiscountReturn.listDate) : null;
            // pPDiscountReturn.dateSendMail = pPDiscountReturn.dateSendMail != null ? moment(pPDiscountReturn.dateSendMail) : null;
        });
        return res;
    }

    searchPPDiscountReturn(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(SERVER_API_URL + 'api/pp-discount-return-objects-search', {
            params: options,
            observe: 'response'
        });
    }

    getRefVouchersByPPdiscountReturnID(id: string): Observable<HttpResponse<any[]>> {
        return this.http.get<any[]>(`${this.resourceUrl}/ref-voucher/${id}`, { observe: 'response' });
    }

    private convertDateFromClientDTO(ref: any) {
        ref.ppDiscountReturn = this.convertDateFromClient(ref.ppDiscountReturn);
        ref.rsInwardOutward = this.convertDateFromClientRSInwardOutward(ref.rsInwardOutward);
        ref.saBill = this.convertDateFromClientSaBill(ref.saBill);
        return ref;
    }

    private convertDateFromClientRSInwardOutward(rSInwardOutward: IRSInwardOutward): IRSInwardOutward {
        const copy: IRSInwardOutward = Object.assign({}, rSInwardOutward, {
            postedDate:
                rSInwardOutward.postedDate != null && rSInwardOutward.postedDate.isValid()
                    ? rSInwardOutward.postedDate.format(DATE_FORMAT)
                    : null,
            date: rSInwardOutward.date != null ? rSInwardOutward.date.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromClientSaBillDetail(saBillDetails: any) {
        const copy: ISaBillDetails = Object.assign({}, saBillDetails, {
            expiryDate: saBillDetails.expiryDate !== null ? saBillDetails.expiryDate.format(DATE_FORMAT) : null,
            invoiceDate: saBillDetails.invoiceDate !== null ? saBillDetails.invoiceDate.format(DATE_FORMAT) : null
        });
        saBillDetails.expiryDate = saBillDetails.expiryDate !== null ? saBillDetails.expiryDate.format(DATE_FORMAT) : null;
        return copy;
    }
    // private convertDateFromClientSaBill(saBill: ISaBill): ISaBill {
    //     // const a = saBill.invoiceDate != null && saBill.invoiceDate.isValid() ? saBill.invoiceDate.format(DATE_FORMAT) : null;
    //     // console.log(a);
    //     const copy: ISaBill = Object.assign({}, saBill, {
    //         invoiceDate: saBill.invoiceDate != null ? saBill.invoiceDate.format(DATE_FORMAT) : null,
    //         listDate: saBill.listDate != null ? saBill.listDate.format(DATE_FORMAT) : null,
    //         dateSendMail: saBill.dateSendMail != null ? saBill.dateSendMail.format(DATE_FORMAT) : null,
    //         documentDate: saBill.documentDate != null ? saBill.documentDate.format(DATE_FORMAT) : null,
    //     });
    //     // saBill.invoiceDate = saBill.invoiceDate != null ? moment(saBill.invoiceDate.format(DATE_FORMAT)) : null;
    //     // saBill.listDate = saBill.listDate != null ? moment(saBill.listDate.format(DATE_FORMAT)) : null;
    //     // saBill.dateSendMail = saBill.dateSendMail != null ? moment(saBill.dateSendMail.format(DATE_FORMAT)) : null;
    //     // saBill.documentDate = saBill.documentDate != null ? moment(saBill.documentDate.format(DATE_FORMAT)) : null;
    //     // if (saBill.saBillDetails) {
    //     //         saBill.saBillDetails[i].expiryDate =
    //     //             saBill.saBillDetails[i].expiryDate != null ? moment(saBill.saBillDetails[i].expiryDate.format(DATE_FORMAT)) : null;
    //     //     }
    //     // }
    //     return copy;
    // }
    private convertPPDiscountReturnDetail(pPDiscountReturnDetails: IPPDiscountReturnDetails[]) {
        // const copy1: IPPDiscountReturnDetails[] ;
        // pPDiscountReturnDetails.forEach(item => {
        //     item = this.convertDateFromDetailClient(item);
        // });
        for (let item of pPDiscountReturnDetails) {
            if (item.expiryDate) {
                item = this.convertDateFromDetailClient(item);
            }
        }
        return pPDiscountReturnDetails;
    }

    private convertDateFromDetailClient(pPDiscountReturnDetails: IPPDiscountReturnDetails): IPPDiscountReturnDetails {
        const copy: IPPDiscountReturnDetails = Object.assign({}, pPDiscountReturnDetails, {
            expiryDate: pPDiscountReturnDetails.expiryDate != null ? moment(pPDiscountReturnDetails.expiryDate.format(DATE_FORMAT)) : null,
            matchDate: pPDiscountReturnDetails.matchDate != null ? moment(pPDiscountReturnDetails.matchDate.format(DATE_FORMAT)) : null
        });
        return copy;
    }
    export(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceUrl + '/export/pdf', { params: options, observe: 'response', headers, responseType: 'blob' });
    }
    exportExcel(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceUrl + '/export/excel', { params: options, observe: 'response', headers, responseType: 'blob' });
    }

    private convertDateFromClientSaBill(saBill: ISaBill) {
        const copy: ISaBill = Object.assign({}, saBill, {
            invoiceDate: saBill.invoiceDate != null && saBill.invoiceDate.isValid() ? saBill.invoiceDate.format(DATE_FORMAT) : null,
            listDate: saBill.listDate != null && saBill.listDate.isValid() ? saBill.listDate.format(DATE_FORMAT) : null,
            dateSendMail: saBill.dateSendMail != null && saBill.dateSendMail.isValid() ? saBill.dateSendMail.format(DATE_FORMAT) : null,
            documentDate: saBill.documentDate != null && saBill.documentDate.isValid() ? saBill.documentDate.format(DATE_FORMAT) : null,
            refDateTime:
                saBill.refDateTime != null && saBill.refDateTime.isValid() ? saBill.refDateTime.format(DATE_TIME_SECOND_FORMAT) : null
        });
        return copy;
    }

    getCustomerReport(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/pdf');
        return this.http.get(SERVER_API_URL + 'api/report/pdf', {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    countFromRSI(id: string): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/count-from-rsi/${id}`, { observe: 'response' });
    }

    multiUnRecord(obj: IPPDiscountReturn[]): Observable<HttpResponse<any>> {
        // const options = this.convertDateFromClientArr(obj);
        return this.http.post<any>(`${this.resourceUrl}/multi-unrecord-pp-discount-returns`, obj, { observe: 'response' });
    }

    private convertDateFromClientArr(ippDiscountReturns: IPPDiscountReturn[]): IPPDiscountReturn[] {
        const ppArr = [];
        ippDiscountReturns.forEach(n => {
            const copy: IPPDiscountReturn = Object.assign({}, n, {
                date: n.date != null && !(n.date instanceof moment) ? moment(n.date, DATE_FORMAT) : null,
                postedDate: n.postedDate != null && !(n.postedDate instanceof moment) ? moment(n.postedDate, DATE_FORMAT) : null
            });
            ppArr.push(copy);
        });
        return ppArr;
    }
}
