import { Moment } from 'moment';

export interface ISAReceiptDebitBill {
    check?: boolean;
    referenceID?: string;
    typeID?: number;
    date?: Moment;
    dueDate?: Moment;
    noFBook?: string;
    noMBook?: string;
    invoiceNo?: string;
    currencyID?: string;
    totalCreditOriginal?: number;
    totalCredit?: number;
    creditAmountOriginal?: number;
    creditAmount?: number;
    account?: string;
    employeeName?: string;
    paymentClause?: string;
    refVoucherExchangeRate?: number;
    lastExchangeRate?: number;
    differAmount?: number;
    amount?: number;
    amountOriginal?: number;
    discountAmount?: number;
    discountAmountOriginal?: number;
    discountRate?: number;
    discountAccount?: string;
    amountTT?: number;
}

export class SAReceiptDebitBill implements ISAReceiptDebitBill {
    constructor(
        public check?: boolean,
        public referenceID?: string,
        public typeID?: number,
        public date?: Moment,
        public dueDate?: Moment,
        public noFBook?: string,
        public noMBook?: string,
        public invoiceNo?: string,
        public currencyID?: string,
        public totalCreditOriginal?: number,
        public totalCredit?: number,
        public creditAmountOriginal?: number,
        public creditAmount?: number,
        public account?: string,
        public paymentClause?: string,
        public refVoucherExchangeRate?: number,
        public lastExchangeRate?: number,
        public differAmount?: number,
        public amount?: number,
        public amountOriginal?: number,
        public discountAmount?: number,
        public discountAmountOriginal?: number,
        public discountRate?: number,
        public discountAccount?: string,
        public amountTT?: number
    ) {
        this.check = this.check || false;
    }
}
