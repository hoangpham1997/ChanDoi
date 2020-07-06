import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { DetailDelFailCategory } from 'app/shared/model/detail-del-fail-category';

export class HandlingResult {
    constructor(
        public countTotalVouchers?: number,
        public countSuccessVouchers?: number,
        public countFailVouchers?: number,
        public listIDFail?: string[],
        public listFail?: ViewVoucherNo[],
        public listFailCategory?: DetailDelFailCategory[]
    ) {}
}
