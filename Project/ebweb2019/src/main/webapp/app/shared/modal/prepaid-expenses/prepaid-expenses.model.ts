import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { DetailDelFailCategory } from 'app/shared/model/detail-del-fail-category';
import { PrepaidExpense } from 'app/shared/model/prepaid-expense.model';

export class PrepaidExpenses {
    constructor(
        public countTotalVouchers?: number,
        public countSuccessVouchers?: number,
        public countFailVouchers?: number,
        public listIDFail?: string[],
        public listFail?: ViewVoucherNo[],
        public listFailCategory?: DetailDelFailCategory[]
    ) {}
}
