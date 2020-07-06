import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { SERVER_API_URL } from 'app/app.constants';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';

type EntityResponseType = HttpResponse<ViewVoucherNo>;
type EntityArrayResponseType = HttpResponse<ViewVoucherNo[]>;

@Injectable({ providedIn: 'root' })
export class KhoaSoKyKeToanService {
    private resourceUrl = SERVER_API_URL + 'api/view-voucher-no';

    constructor(private http: HttpClient) {}
}
