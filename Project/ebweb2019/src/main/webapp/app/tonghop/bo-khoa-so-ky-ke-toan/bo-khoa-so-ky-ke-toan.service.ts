import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { IEInvoice } from 'app/shared/model/hoa-don-dien-tu/e-invoice.model';
import { UpdateDateClosedBook } from 'app/tonghop/bo-khoa-so-ky-ke-toan/update-date-closed-book.model';
import { DATE_FORMAT } from 'app/shared';

type EntityResponseType = HttpResponse<IEInvoice>;
type EntityArrayResponseType = HttpResponse<IEInvoice[]>;

@Injectable({ providedIn: 'root' })
export class BoKhoaSoKyKeToanService {
    private resourceUrl = SERVER_API_URL + 'api/view-voucher-no';

    constructor(private http: HttpClient) {}

    updateDateClosedBook(updateDateClosedBook1: UpdateDateClosedBook): Observable<any> {
        const copy = this.convertDateFromClient(updateDateClosedBook1);
        return this.http.post<any>(this.resourceUrl + '/update-date-closed-book', copy, { observe: 'response' });
    }

    private convertDateFromClient(updateDateClosedBook1: UpdateDateClosedBook): UpdateDateClosedBook {
        const copy: UpdateDateClosedBook = Object.assign({}, updateDateClosedBook1, {
            dateClosedBook:
                updateDateClosedBook1.dateClosedBook != null && updateDateClosedBook1.dateClosedBook.isValid()
                    ? updateDateClosedBook1.dateClosedBook.format(DATE_FORMAT)
                    : null,
            dateClosedBookOld:
                updateDateClosedBook1.dateClosedBookOld != null && updateDateClosedBook1.dateClosedBookOld.isValid()
                    ? updateDateClosedBook1.dateClosedBookOld.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }
}
