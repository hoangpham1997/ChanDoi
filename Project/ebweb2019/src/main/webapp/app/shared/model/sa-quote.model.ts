import { Moment } from 'moment';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';
import { IPaymentClause } from 'app/shared/model//payment-clause.model';
import { ISAQuoteDetails } from 'app/shared/model/sa-quote-details.model';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';

export interface ISAQuote {
    id?: string;
    companyId?: string;
    branchId?: string;
    typeId?: number;
    date?: Moment;
    no?: string;
    accountingObjectName?: string;
    accountingObjectAddress?: string;
    companyTaxCode?: string;
    contactName?: string;
    contactMobile?: string;
    contactEmail?: string;
    deliveryTime?: string;
    guaranteeDuration?: string;
    description?: string;
    reason?: string;
    currencyID?: string;
    exchangeRate?: number;
    finalDate?: Moment;
    employeeID?: string;
    totalAmount?: number;
    totalAmountOriginal?: number;
    totalVATAmount?: number;
    totalVATAmountOriginal?: number;
    totalDiscountAmount?: number;
    totalDiscountAmountOriginal?: number;
    templateID?: string;
    accountingObject?: IAccountingObject;
    paymentClause?: IPaymentClause;
    sAQuoteDetails?: ISAQuoteDetails[];
    viewVouchers?: IViewVoucher[];
    total?: number;
    totalOriginal?: number;
    totalMoney?: number;
}

export class SAQuote implements ISAQuote {
    constructor(
        public id?: string,
        public companyId?: string,
        public branchId?: string,
        public typeId?: number,
        public date?: Moment,
        public no?: string,
        public accountingObjectName?: string,
        public accountingObjectAddress?: string,
        public companyTaxCode?: string,
        public contactName?: string,
        public contactMobile?: string,
        public contactEmail?: string,
        public deliveryTime?: string,
        public guaranteeDuration?: string,
        public description?: string,
        public reason?: string,
        public currencyID?: string,
        public exchangeRate?: number,
        public finalDate?: Moment,
        public employeeID?: string,
        public totalAmount?: number,
        public totalAmountOriginal?: number,
        public totalVATAmount?: number,
        public totalVATAmountOriginal?: number,
        public totalDiscountAmount?: number,
        public totalDiscountAmountOriginal?: number,
        public templateID?: string,
        public accountingObject?: IAccountingObject,
        public paymentClause?: IPaymentClause,
        public sAQuoteDetails?: ISAQuoteDetails[],
        public viewVouchers?: IViewVoucher[],
        public total?: number,
        public totalMoney?: number,
        public totalOriginal?: number
    ) {}
}
