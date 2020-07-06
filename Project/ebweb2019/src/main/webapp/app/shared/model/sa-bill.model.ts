import { Moment } from 'moment';
import { AccountingObject } from 'app/shared/model//accounting-object.model';
import { IInvoiceType } from 'app/shared/model/invoice-type.model';
import { ISaBillDetails } from 'app/shared/model//sa-bill-details.model';

export interface ISaBill {
    id?: string;
    currencyID?: string;
    exchangeRate?: number;
    typeLedger?: number;
    accountingObjectName?: string;
    accountingObjectAddress?: string;
    companyTaxCode?: string;
    accountingObjectBankAccount?: string;
    accountingObjectBankName?: string;
    contactName?: string;
    reason?: string;
    isAttachList?: boolean;
    listNo?: string;
    listDate?: Moment;
    listCommonNameInventory?: string;
    totalAmount?: number;
    totalAmountOriginal?: number;
    totalVATAmount?: number;
    totalVATAmountOriginal?: number;
    invoiceTemplate?: string;
    invoiceSeries?: string;
    invoiceNo?: string;
    invoiceDate?: Moment;
    refDateTime?: Moment;
    paymentMethod?: string;
    accountingObject?: AccountingObject;
    invoiceTypeID?: string;
    templateID?: string;
    saBillDetails?: ISaBillDetails[];
    recorded?: boolean;
    documentNo?: string;
    documentDate?: Moment;
    documentNote?: string;
    totalDiscountAmount?: number;
    totalDiscountAmountOriginal?: number;
    total?: number;
    totalOriginal?: number;
    checkStatus?: boolean;
    statusInvoice?: number;
    invoiceForm?: any;
    typeID?: number;
    dateSendMail?: Moment;
    type?: number;
    iDReplaceInv?: string;
    iDAdjustInv?: string;
}

export class SaBill implements ISaBill {
    constructor(
        public id?: string,
        public currencyID?: string,
        public exchangeRate?: number,
        public typeLedger?: number,
        public accountingObjectName?: string,
        public accountingObjectAddress?: string,
        public companyTaxCode?: string,
        public accountingObjectBankAccount?: string,
        public accountingObjectBankName?: string,
        public contactName?: string,
        public reason?: string,
        public isAttachList?: boolean,
        public listNo?: string,
        public listDate?: Moment,
        public listCommonNameInventory?: string,
        public totalAmount?: number,
        public totalAmountOriginal?: number,
        public totalVATAmount?: number,
        public totalVATAmountOriginal?: number,
        public invoiceTemplate?: string,
        public invoiceSeries?: string,
        public invoiceNo?: string,
        public invoiceDate?: Moment,
        public refDateTime?: Moment,
        public paymentMethod?: string,
        public accountingObject?: AccountingObject,
        public invoiceTypeID?: string,
        public templateID?: string,
        public saBillDetails?: ISaBillDetails[],
        public recorded?: boolean,
        public documentNo?: string,
        public documentDate?: Moment,
        public documentNote?: string,
        public totalDiscountAmount?: number,
        public totalDiscountAmountOriginal?: number,
        public total?: number,
        public totalOriginal?: number,
        public checkStatus?: boolean,
        public statusInvoice?: number,
        public invoiceForm?: any,
        public typeID?: number,
        public dateSendMail?: Moment,
        public type?: number,
        public iDReplaceInv?: string,
        public iDAdjustInv?: string
    ) {
        this.isAttachList = this.isAttachList || false;
    }
}
