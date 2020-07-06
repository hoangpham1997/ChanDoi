import { Moment } from 'moment';
import { ISAOrderDetails } from 'app/shared/model/sa-order-details.model';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';

export interface ISAOrder {
    id?: string;
    companyID?: string;
    branchID?: string;
    typeID?: number;
    date?: Moment;
    no?: string;
    deliverDate?: Moment;
    deliveryPlace?: string;
    accountingObjectID?: string;
    accountingObjectName?: string;
    accountingObjectAddress?: string;
    companyTaxCode?: string;
    contactName?: string;
    reason?: string;
    currencyID?: string;
    exchangeRate?: number;
    transpotMethodID?: string;
    employeeID?: string;
    totalAmount?: number;
    totalAmountOriginal?: number;
    totalDiscountAmount?: number;
    totalDiscountAmountOriginal?: number;
    totalVATAmount?: number;
    totalVATAmountOriginal?: number;
    templateID?: string;
    status?: number;
    saOrderDetails?: ISAOrderDetails[];
    total?: number;
    totalOriginal?: number;
    viewVouchers?: IViewVoucher[];
}

export class SAOrder implements ISAOrder {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public typeID?: number,
        public date?: Moment,
        public no?: string,
        public deliverDate?: Moment,
        public deliveryPlace?: string,
        public accountingObjectID?: string,
        public accountingObjectName?: string,
        public accountingObjectAddress?: string,
        public companyTaxCode?: string,
        public contactName?: string,
        public reason?: string,
        public currencyID?: string,
        public exchangeRate?: number,
        public transpotMethodID?: string,
        public employeeID?: string,
        public totalAmount?: number,
        public totalAmountOriginal?: number,
        public totalDiscountAmount?: number,
        public totalDiscountAmountOriginal?: number,
        public totalVATAmount?: number,
        public totalVATAmountOriginal?: number,
        public templateID?: string,
        public status?: number,
        public saOrderDetails?: ISAOrderDetails[],
        public total?: number,
        public totalOriginal?: number,
        public viewVouchers?: IViewVoucher[]
    ) {}
}
