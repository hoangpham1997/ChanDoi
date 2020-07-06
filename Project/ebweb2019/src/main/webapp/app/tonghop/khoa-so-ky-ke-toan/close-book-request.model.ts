import { Moment } from 'moment';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';

export class CloseBook {
    constructor(
        public postedDate?: String,
        public postedDateNew?: String,
        public listDataChangeDiff?: ViewVoucherNo[],
        public listChangePostedDateDiff?: ViewVoucherNo[],
        public choseFuntion?: number,
        public lstBranch?: any
    ) {}
}
