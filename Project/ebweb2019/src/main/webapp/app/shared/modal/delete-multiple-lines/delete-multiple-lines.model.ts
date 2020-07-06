import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';

export class HandlingResult {
    constructor(
        public countTotalVouchers?: number,
        public countSuccessVouchers?: number,
        public countFailVouchers?: number,
        public listIDFail?: string[],
        public listFail?: ViewVoucherNo[]
    ) {}
}
