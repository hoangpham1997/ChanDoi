import { IAccountingObject } from 'app/shared/model//accounting-object.model';
import { IMBDeposit } from 'app/shared/model//mb-deposit.model';
import { Moment } from 'moment';

export interface IMBDepositDetailCustomer {
    id?: number;
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
    refVoucherExchangeRate?: number;
    lastExchangeRate?: number;
    differAmount?: number;
    orderPriority?: number;
    employeeID?: string;
    mbDepositID?: string;
    accountingObject?: IAccountingObject;
    date?: Moment;
    dueDate?: Moment;
    noFBook?: string;
    noMBook?: string;
    paymentClauseCode?: string;
}

export class MBDepositDetailCustomer implements IMBDepositDetailCustomer {
    constructor(
        public id?: number,
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
        public refVoucherExchangeRate?: number,
        public lastExchangeRate?: number,
        public differAmount?: number,
        public orderPriority?: number,
        public employee?: IAccountingObject,
        public mbDepositID?: string,
        public accountingObject?: IAccountingObject
    ) {}
}
