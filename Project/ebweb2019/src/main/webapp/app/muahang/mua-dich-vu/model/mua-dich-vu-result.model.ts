import { MuaDichVuDetailResult } from 'app/muahang/mua-dich-vu/model/mua-dich-vu-detail-result.model';
import { IRefVoucher } from 'app/shared/model/ref-voucher.model';

export interface IMuaDichVuResult {
    id?: string;
    receiptDate?: string;
    postedDate?: string;
    noBook?: string;
    receiptType?: string;
    typeId?: number;
    accountingObjectID?: string;
    accountingObjectCode?: string;
    accountingObjectName?: string;
    accountingObjectAddress?: string;
    companyTaxCode?: string;
    contactName?: string;
    totalAmount?: number;
    totalAmountOriginal?: number;
    totalDiscountAmount?: number;
    totalVATAmount?: number;
    resultAmount?: number;
    resultAmountOriginal?: number;
    billReceived?: Boolean;
    dueDate?: string;
    reason?: string;
    numberAttach?: string;
    employeeID?: string;
    employeeName?: string;
    noFBook?: string;
    noMBook?: string;
    currencyID?: string;
    exchangeRate?: number;
    typeLedger?: number;
    // other properties payment voucher
    receiverUserName?: string;
    otherNumberAttach?: string;
    otherReason?: string;

    accountPaymentId?: string;
    accountPaymentName?: string;
    accountReceiverId?: string;
    accountReceiverName?: string;

    otherNoBook?: string;

    creditCardNumber?: string;
    creditCardId?: string;
    creditCardType?: string;
    ownerCreditCard?: string;
    ppServiceDetailDTOS?: MuaDichVuDetailResult[];
    purchaseCosts?: Boolean;
    recorded?: boolean;
    isForeignCurrency?: boolean;
    currentBook?: string;
    refVouchers?: IRefVoucher[];
    paymentVoucherID?: string;
    receiver?: string;
    identificationNo?: string;
    issueDate?: string;
    issueBy?: string;
    invoiceNo?: string;
    rowNum?: number;
    totalquantity?: number;
    checkPPOrderQuantity?: boolean;
}

export class MuaDichVuResult implements IMuaDichVuResult {
    constructor(
        public id?: string,
        public purchaseCosts?: Boolean,
        public recorded?: boolean,
        public creditCardNumber?: string,
        public creditCardId?: string,
        public creditCardType?: string,
        public ownerCreditCard?: string,
        public receiptDate?: string,
        public typeId?: number,
        public postedDate?: string,
        public noBook?: string,
        public receiptType?: string,
        public accountingObject?: any,
        public accountingObjectID?: string,
        public accountingObjectCode?: string,
        public accountingObjectName?: string,
        public accountingObjectAddress?: string,
        public companyTaxCode?: string,
        public contactName?: string,
        public totalAmount?: number,
        public totalAmountOriginal?: number,
        public totalDiscountAmount?: number,
        public totalDiscountAmountOriginal?: number,
        public totalVATAmount?: number,
        public totalVATAmountOriginal?: number,
        public resultAmount?: number,
        public resultAmountOriginal?: number,
        public billReceived?: Boolean,
        public dueDate?: string,
        public reason?: string,
        public numberAttach?: string,
        public employeeID?: string,
        public employeeName?: string,
        public noFBook?: string,
        public noMBook?: string,
        public currencyID?: string,
        public exchangeRate?: number,
        public typeLedger?: number,
        public receiverUserName?: string,
        public otherNumberAttach?: string,
        public otherReason?: string,
        public accountPaymentId?: string,
        public accountPaymentName?: string,
        public accountReceiverId?: string,
        public accountReceiverName?: string,
        public otherNoBook?: string,
        public isForeignCurrency?: boolean,
        public ppServiceDetailDTOS?: MuaDichVuDetailResult[],
        public currentBook?: string,
        public refVouchers?: IRefVoucher[],
        public paymentVoucherID?: string,
        public receiver?: string,
        public identificationNo?: string,
        public issueDate?: string,
        public issueBy?: string,
        public rowNum?: number,
        public totalquantity?: number,
        public checkPPOrderQuantity?: boolean,
        public invoiceNo?: string
    ) {
        this.id = id ? id : '';
        this.receiptDate = receiptDate ? receiptDate : '';
        this.postedDate = postedDate ? postedDate : '';
        this.noBook = noBook ? noBook : '';
        this.recorded = recorded ? recorded : false;
        this.receiptType = receiptType ? receiptType : '';
        this.typeId = typeId ? typeId : 240;
        this.accountingObjectID = accountingObjectID ? accountingObjectID : null;
        this.accountingObjectName = accountingObjectName ? accountingObjectName : '';
        this.accountingObjectAddress = accountingObjectAddress ? accountingObjectAddress : null;
        this.companyTaxCode = companyTaxCode ? companyTaxCode : '';
        this.contactName = contactName ? contactName : null;
        this.totalAmount = totalAmount ? totalAmount : 0;
        this.totalAmountOriginal = totalAmountOriginal ? totalAmountOriginal : 0;
        this.totalDiscountAmount = totalDiscountAmount ? totalDiscountAmount : 0;
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal ? totalDiscountAmountOriginal : 0;
        this.totalVATAmount = totalVATAmount ? totalVATAmount : 0;
        this.totalVATAmountOriginal = totalVATAmountOriginal ? totalVATAmountOriginal : 0;
        this.resultAmount = resultAmount ? resultAmount : 0;
        this.billReceived = billReceived ? billReceived : false;
        this.purchaseCosts = purchaseCosts ? purchaseCosts : false;
        this.dueDate = dueDate ? dueDate : '';
        this.reason = reason ? reason : '';
        this.numberAttach = numberAttach ? numberAttach : '';
        this.employeeID = employeeID ? employeeID : '';
        this.employeeName = employeeName ? employeeName : '';
        this.noFBook = noFBook ? noFBook : '';
        this.noMBook = noMBook ? noMBook : '';
        this.currencyID = currencyID ? currencyID : '';
        this.exchangeRate = exchangeRate ? exchangeRate : 1;
        this.typeLedger = typeLedger;
        this.refVouchers = refVouchers ? refVouchers : [];
        this.ppServiceDetailDTOS = ppServiceDetailDTOS ? ppServiceDetailDTOS : [];
        this.totalquantity = totalquantity || 0;
    }
}
