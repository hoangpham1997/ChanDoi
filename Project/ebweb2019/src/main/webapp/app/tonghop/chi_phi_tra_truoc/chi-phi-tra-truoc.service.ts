import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_FORMAT_SLASH, DATE_FORMAT_YMD } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPrepaidExpense } from 'app/shared/model/prepaid-expense.model';

const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
const EXCEL_EXTENSION = '.xlsx';

type EntityResponseType = HttpResponse<IPrepaidExpense>;
type EntityArrayResponseType = HttpResponse<IPrepaidExpense[]>;

@Injectable({ providedIn: 'root' })
export class ChiPhiTraTruocService {
    private resourceUrl = SERVER_API_URL + 'api/prepaid-expenses';

    constructor(private http: HttpClient) {}

    create(prepaidExpenseDTO: any): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromDTOClient(prepaidExpenseDTO);
        return this.http
            .post<any>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(prepaidExpenseDTO: any): Observable<HttpResponse<any>> {
        const copy = this.convertDateFromDTOClient(prepaidExpenseDTO);
        return this.http
            .put<any>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<HttpResponse<any>> {
        return this.http
            .get<any>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPrepaidExpense[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: any): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    updateIsActive(id: any): Observable<HttpResponse<any>> {
        return this.http.put<any>(`${this.resourceUrl}/is-active/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(prepaidExpense: IPrepaidExpense): IPrepaidExpense {
        const copy: IPrepaidExpense = Object.assign({}, prepaidExpense, {
            date: prepaidExpense.date != null && prepaidExpense.date.isValid() ? prepaidExpense.date.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: any): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        res.body.prepaidExpenseVouchers = res.body.prepaidExpenseVouchers
            ? this.convertDateFromServerVoucher(res.body.prepaidExpenseVouchers)
            : null;
        return res;
    }
    private convertDateFromServerVoucher(res: any): EntityResponseType {
        if (res.body) {
            for (let i = 0; i < res.body.length; i++) {
                res.body[i].date = res.body[i].date != null ? moment(res.body[i].date, DATE_FORMAT_YMD) : null;
                res.body[i].postdate = res.body[i].postdate != null ? moment(res.body[i].postdate, DATE_FORMAT_YMD) : null;
            }
        }
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((prepaidExpense: IPrepaidExpense) => {
            prepaidExpense.date = prepaidExpense.date != null ? moment(prepaidExpense.date) : null;
        });
        return res;
    }

    // lấy  danh sách đối tượng phân bổ đã active
    getPrepaidExpenseCode(): Observable<any> {
        return this.http.get<any>(`${this.resourceUrl}/get-prepaid-expense-code`, { observe: 'response' });
    }
    // lấy all danh sách đối tượng phân bổ
    getPrepaidExpenseCodeCanActive(): Observable<any> {
        return this.http.get<any>(`${this.resourceUrl}/get-prepaid-expense-code-can-active`, { observe: 'response' });
    }
    getCostAccounts(): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/get-cost-accounts`, { observe: 'response' });
    }

    getAll(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(SERVER_API_URL + 'api/prepaid-expenses/get-all', {
            params: options,
            observe: 'response'
        });
    }

    private convertDateFromDTOClient(prepaidExpenseDTO: any) {
        const copy: IPrepaidExpense = Object.assign({}, prepaidExpenseDTO, {
            date: prepaidExpenseDTO.date != null && prepaidExpenseDTO.date.isValid() ? prepaidExpenseDTO.date.format(DATE_FORMAT) : null,
            prepaidExpenseVouchers: prepaidExpenseDTO.prepaidExpenseVouchers
                ? this.convertDateFromPrepaidExpenseVoucherClient(prepaidExpenseDTO.prepaidExpenseVouchers)
                : null
        });
        return copy;
    }

    private convertDateFromPrepaidExpenseVoucherClient(prepaidExpenseVoucher: any[]) {
        for (let i = 0; i < prepaidExpenseVoucher.length; i++) {
            prepaidExpenseVoucher[i] = this.convertPrepaidExpenseVoucherItem(prepaidExpenseVoucher[i]);
        }
        // prepaidExpenseVoucher.forEach(item => {
        //     item = this.convertPrepaidExpenseVoucherItem(item);
        // });
        return prepaidExpenseVoucher;
    }

    private convertPrepaidExpenseVoucherItem(item: any) {
        const copy: IPrepaidExpense = Object.assign({}, item, {
            date: item.date != null ? moment(item.date, DATE_FORMAT_SLASH) : null,
            postedDate: item.postedDate != null ? moment(item.postedDate, DATE_FORMAT_SLASH) : null
        });
        return copy;
    }

    countByPrepaidExpenseID(id: any): Observable<HttpResponse<any>> {
        // const options = createRequestOption(req);
        // return this.http.get<any>(this.resourceUrl + '/count-by-prepaid-expense-id', { params: options, observe: 'response' });
        return this.http.get<any>(`${this.resourceUrl}/count-by-prepaid-expense-id/${id}`, { observe: 'response' });
    }

    private convertDateFromServerMulti(prepaidExpense: any[]) {
        for (let i = 0; i < prepaidExpense.length; i++) {
            prepaidExpense[i].date = prepaidExpense[i].date != null ? prepaidExpense[i].date.format(DATE_FORMAT) : null;
        }
        return prepaidExpense;
    }

    multiDelete(selectedRows: any[]) {
        // const copy = this.convertDateFromServerMulti(selectedRows);
        return this.http.post<any>(`${this.resourceUrl}/multi-delete-prepaid-expenses`, selectedRows, { observe: 'response' });
    }
}
