import { IMCReceipt } from 'app/shared/model//mc-receipt.model';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';
import { Moment } from 'moment';

export interface IMCReceiptDetailCustomer {
    id?: string;
    creditAccount?: string;
    saleInvoiceID?: string;
    voucherTypeID?: number;
    receipableAmount?: number;
    receipableAmountOriginal?: number;
    remainingAmount?: number;
    remainingAmountOriginal?: number;
    amount?: number;
    amountOriginal?: number;
    discountRate?: number;
    discountAmount?: number;
    discountAmountOriginal?: number;
    discountAccount?: string;
    employeeID?: string;
    orderPriority?: number;
    refVoucherExchangeRate?: number;
    lastExchangeRate?: number;
    differAmount?: number;
    mCReceiptID?: string;
    accountingObject?: IAccountingObject;
    date?: Moment;
    dueDate?: Moment;
    noFBook?: string;
    noMBook?: string;
    paymentClauseCode?: string;
}

export class MCReceiptDetailCustomer implements IMCReceiptDetailCustomer {
    constructor(
        public id?: string,
        public creditAccount?: string,
        public saleInvoiceID?: string,
        public voucherTypeID?: number,
        public receipableAmount?: number,
        public receipableAmountOriginal?: number,
        public remainingAmount?: number,
        public remainingAmountOriginal?: number,
        public amount?: number,
        public amountOriginal?: number,
        public discountRate?: number,
        public discountAmount?: number,
        public discountAmountOriginal?: number,
        public discountAccount?: string,
        public employeeID?: string,
        public orderPriority?: number,
        public refVoucherExchangeRate?: number,
        public lastExchangeRate?: number,
        public differAmount?: number,
        public mCReceiptID?: string,
        public accountingObject?: IAccountingObject,
        public date?: Moment,
        public dueDate?: Moment,
        public noFBook?: string,
        public noMBook?: string,
        public paymentClauseCode?: string
    ) {}
}
