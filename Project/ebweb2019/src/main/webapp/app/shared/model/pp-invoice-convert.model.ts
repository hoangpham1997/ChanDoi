import { Moment } from 'moment';
import { IMBDepositDetails } from 'app/shared/model/mb-deposit-details.model';
import { IMBDepositDetailTax } from 'app/shared/model/mb-deposit-detail-tax.model';
import { IPPInvoiceDetails } from 'app/shared/model/pp-invoice-details.model';
import { IPPDiscountReturnDetails, PPDiscountReturnDetails } from 'app/shared/model/pp-discount-return-details.model';

export interface IPPInvoiceConvert {
    date?: Moment;
    materialGoodsCode?: string;
    noMBook?: string;
    noFBook?: string;
    description?: string;
    quantity?: number;
    quantityRollBack?: number;
    checkStatus?: boolean;
    ppDiscountReturnDetails?: IPPDiscountReturnDetails;
}

export class PPInvoiceConvert implements IPPInvoiceConvert {
    constructor(
        public date?: Moment,
        public materialGoodsCode?: string,
        public noMBook?: string,
        public noFBook?: string,
        public description?: string,
        public quantity?: number,
        public checkStatus?: boolean,
        public quantityRollBack?: number,
        public ppDiscountReturnDetails?: IPPDiscountReturnDetails
    ) {
        this.quantityRollBack = this.quantity;
    }
}
