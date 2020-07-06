import { Moment } from 'moment';

export interface IMBTellerPaperDetailVendor {
    id?: string;
    mBTellerPaperID?: string;
    debitAccount?: string;
    voucherTypeID?: number;
    pPInvoiceID?: string;
    dueDate?: Moment;
    payableAmount?: number;
    payableAmountOriginal?: number;
    remainingAmount?: number;
    remainingAmountOriginal?: number;
    amount?: number;
    amountOriginal?: number;
    discountAmount?: number;
    discountAmountOriginal?: number;
    discountRate?: number;
    discountAccount?: string;
    accountingObjectID?: string;
    employeeID?: string;
    refVoucherExchangeRate?: number;
    lastExchangeRate?: number;
    differAmount?: number;
    orderPriority?: number;
    date?: Moment;
    noFBook?: string;
    noMBook?: string;
}

export class MBTellerPaperDetailVendor implements IMBTellerPaperDetailVendor {
    constructor(
        public id?: string,
        public mBTellerPaperID?: string,
        public debitAccount?: string,
        public voucherTypeID?: number,
        public pPInvoiceID?: string,
        public dueDate?: Moment,
        public payableAmount?: number,
        public payableAmountOriginal?: number,
        public remainingAmount?: number,
        public remainingAmountOriginal?: number,
        public amount?: number,
        public amountOriginal?: number,
        public discountAmount?: number,
        public discountAmountOriginal?: number,
        public discountRate?: number,
        public discountAccount?: string,
        public accountingObjectID?: string,
        public employeeID?: string,
        public refVoucherExchangeRate?: number,
        public lastExchangeRate?: number,
        public differAmount?: number,
        public orderPriority?: number,
        public date?: Moment,
        public noFBook?: string,
        public noMBook?: string
    ) {}
}
