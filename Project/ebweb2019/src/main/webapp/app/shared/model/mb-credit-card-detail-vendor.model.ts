import { Moment } from 'moment';
import { IMBCreditCard } from 'app/shared/model//mb-credit-card.model';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';

export interface IMBCreditCardDetailVendor {
    id?: number;
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
    employeeID?: string;
    refVoucherExchangeRate?: number;
    lastExchangeRate?: number;
    differAmount?: number;
    orderPriority?: number;
    mBCreditCardID?: string;
    accountingObjectID?: string;
    date?: Moment;
    noFBook?: string;
    noMBook?: string;
    paymentClauseCode?: string;
}

export class MBCreditCardDetailVendor implements IMBCreditCardDetailVendor {
    constructor(
        public id?: number,
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
        public employeeID?: string,
        public refVoucherExchangeRate?: number,
        public lastExchangeRate?: number,
        public differAmount?: number,
        public orderPriority?: number,
        public mBCreditCardID?: string,
        public accountingObjectID?: string
    ) {}
}
