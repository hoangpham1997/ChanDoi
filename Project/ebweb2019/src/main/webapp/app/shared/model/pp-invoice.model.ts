import { Moment } from 'moment';
import { IPPInvoiceDetails } from 'app/shared/model/pp-invoice-details.model';
import { BankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IRefVoucher } from 'app/shared/model/ref-voucher.model';
import { IPPInvoiceDetailCost } from 'app/shared/model/pp-invoice-detail-cost.model';

export interface IPPInvoice {
    id?: string;
    companyId?: string;
    typeId?: number;
    date?: Moment;
    postedDate?: Moment;
    typeLedger?: number;
    noFBook?: string;
    noMBook?: string;
    rsInwardOutwardId?: string;
    paymentVoucherId?: string;
    accountingObjectId?: string;
    accountingObjectName?: string;
    accountingObjectAddress?: string;
    companyTaxCode?: string;
    contactName?: string;
    reason?: string;
    numberAttach?: string;
    billReceived?: boolean;
    currencyId?: string;
    exchangeRate?: number;
    paymentClauseId?: string;
    dueDate?: Moment;
    transportMethodId?: string;
    employeeId?: string;
    isImportPurchase?: boolean;
    storedInRepository?: boolean;
    totalAmount?: number;
    totalAmountOriginal?: number;
    totalImportTaxAmount?: number;
    totalImportTaxAmountOriginal?: number;
    totalVATAmount?: number;
    totalVATAmountOriginal?: number;
    totalDiscountAmount?: number;
    totalDiscountAmountOriginal?: number;
    totalInwardAmount?: number;
    totalInwardAmountOriginal?: number;
    totalSpecialConsumeTaxAmount?: number;
    totalSpecialConsumeTaxAmountOriginal?: number;
    isMatch?: boolean;
    matchDate?: Moment;
    templateID?: string;
    recorded?: boolean;
    ppInvoiceDetails?: IPPInvoiceDetails[];
    accountingObject?: any;
    employee?: any;
    totalFreightAmount?: number;
    totalFreightAmountOriginal?: number;
    totalImportTaxExpenseAmount?: number;
    totalImportTaxExpenseAmountOriginal?: number;
    noFBookRs?: string;
    reasonRs?: string;
    numberAttachRs?: string;
    accountPayment?: BankAccountDetails;
    accountPaymentName?: string;
    accountReceiverName?: string;
    accountReceiver?: any;
    otherNoFBook?: string;
    ownerCreditCard?: string;
    creditCardType?: string;
    creditCardItem?: any;
    currentBook?: string;
    receiverUserName?: string;
    otherNumberAttach?: string;
    otherReason?: string;
    typeGroup?: number;
    accountReceiverId?: string;
    accountPaymentId?: string;
    creditCardNumber?: string;
    bankAccountReceiverName?: any;
    bankAccountReceiverId?: any;
    bankAccountReceiverItem?: any;
    otherNoMBook?: string;
    importPurchase?: boolean;
    refVouchers?: IRefVoucher[];
    noMBookRs?: string;
    accountingObjectCode?: string;
    accountReceiverFullName?: string;
    identificationNo?: string;
    issueDate?: Moment;
    issueBy?: string;
    ppInvoiceDetailCost?: IPPInvoiceDetailCost[];
    ppOrderNo?: string;
    no?: string;
    typeIdStr?: string;
    amountTT?: number;
    sumTotalAmount?: number;
    isPlayVendor?: boolean;
    isUsed?: boolean;
}

export class PPInvoice implements IPPInvoice {
    constructor(
        public id?: string,
        public companyId?: string,
        public typeId?: number,
        public date?: Moment,
        public postedDate?: Moment,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public rsInwardOutwardId?: string,
        public paymentVoucherId?: string,
        public accountingObjectId?: string,
        public accountingObjectName?: string,
        public accountingObjectAddress?: string,
        public companyTaxCode?: string,
        public contactName?: string,
        public reason?: string,
        public numberAttach?: string,
        public billReceived?: boolean,
        public currencyId?: string,
        public exchangeRate?: number,
        public paymentClauseId?: string,
        public dueDate?: Moment,
        public transportMethodId?: string,
        public employeeId?: string,
        public isImportPurchase?: boolean,
        public storedInRepository?: boolean,
        public totalAmount?: number,
        public totalAmountOriginal?: number,
        public totalImportTaxAmount?: number,
        public totalImportTaxAmountOriginal?: number,
        public totalVATAmount?: number,
        public totalVATAmountOriginal?: number,
        public totalDiscountAmount?: number,
        public totalDiscountAmountOriginal?: number,
        public totalInwardAmount?: number,
        public totalInwardAmountOriginal?: number,
        public totalSpecialConsumeTaxAmount?: number,
        public totalSpecialConsumeTaxAmountOriginal?: number,
        public isMatch?: boolean,
        public matchDate?: Moment,
        public templateID?: string,
        public recorded?: boolean,
        public pPInvoiceDetails?: IPPInvoiceDetails[],
        public accountingObject?: any,
        public employee?: any,
        public totalFreightAmount?: number,
        public totalFreightAmountOriginal?: number,
        public totalImportTaxExpenseAmount?: number,
        public totalImportTaxExpenseAmountOriginal?: number,
        public noFBookRs?: string,
        public reasonRs?: string,
        public numberAttachRs?: string,
        public accountPayment?: BankAccountDetails,
        public accountPaymentName?: string,
        public accountReceiverName?: string,
        public accountReceiver?: any,
        public ownerCreditCard?: string,
        public creditCardType?: string,
        public creditCardItem?: any,
        public otherNoFBook?: string,
        public currentBook?: string,
        public receiverUserName?: string,
        public otherNumberAttach?: string,
        public otherReason?: string,
        public typeGroup?: number,
        public accountReceiverId?: string,
        public accountPaymentId?: string,
        public creditCardNumber?: string,
        public bankAccountReceiverName?: any,
        public bankAccountReceiverId?: any,
        public bankAccountReceiverItem?: any,
        public otherNoMBook?: string,
        public importPurchase?: boolean,
        public refVouchers?: IRefVoucher[],
        public noMBookRs?: string,
        public accountingObjectCode?: string,
        public accountReceiverFullName?: string,
        public identificationNo?: string,
        public issueDate?: Moment,
        public issueBy?: string,
        public ppInvoiceDetailCost?: IPPInvoiceDetailCost[],
        public ppOrderNo?: string,
        public no?: string,
        public typeIdStr?: string,
        public amountTT?: number,
        public sumTotalAmount?: number,
        public isPlayVendor?: boolean,
        public isUsed?: boolean
    ) {
        this.billReceived = this.billReceived || false;
        this.isImportPurchase = this.isImportPurchase || false;
        this.storedInRepository = this.storedInRepository || false;
        this.isMatch = this.isMatch || false;
        this.recorded = this.recorded || false;
    }
}
