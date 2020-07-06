import { Moment } from 'moment';
import { PPDiscountReturn } from 'app/shared/model/pp-discount-return.model';
import { RSInwardOutward } from 'app/shared/model/rs-inward-outward.model';
import { SaBill } from 'app/shared/model/sa-bill.model';

export interface IDiscountSaBillRSI {
    ppDiscountReturn?: PPDiscountReturn;
    rsInwardOutward?: RSInwardOutward;
    saBill?: SaBill;
}

export class DiscountSaBillRSI implements IDiscountSaBillRSI {
    constructor(public ppDiscountReturn?: PPDiscountReturn, public rsInwardOutward?: RSInwardOutward, public saBill?: SaBill) {}
}
