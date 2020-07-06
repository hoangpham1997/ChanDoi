import { Moment } from 'moment';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';
import { IInvoiceType } from 'app/shared/model//invoice-type.model';
import { ITemplate } from 'app/shared/model//template.model';
// import { IRsInwardOutward } from 'app/shared/model//rs-inward-outward.model';
import { ISaReturnDetails } from 'app/shared/model/sa-return-details.model';

export interface ISaReturn {
    id?: string;
    companyID?: string;
    typeID?: number;
    date?: Moment;
    postedDate?: Moment;
    typeLedger?: number;
    noFBook?: string;
    noMBook?: string;
    noFBookInventory?: string;
    noMBookInventory?: string;
    autoOWAmountCal?: number;
    accountingObjectName?: string;
    accountingObjectAddress?: string;
    companyTaxCode?: string;
    accountingObjectBankAccount?: string;
    accountingObjectBankName?: string;
    contactName?: string;
    reason?: string;
    numberAttach?: string;
    invoiceTemplate?: string;
    invoiceSeries?: string;
    invoiceNo?: string;
    invoiceDate?: Moment;
    paymentMethod?: string;
    currencyID?: string;
    exchangeRate?: number;
    totalAmount?: number;
    totalAmountOriginal?: number;
    totalDiscountAmount?: number;
    totalDiscountAmountOriginal?: number;
    totalPaymentAmount?: number;
    totalPaymentAmountOriginal?: number;
    totalVatAmount?: number;
    totalVatAmountOriginal?: number;
    totalOWAmount?: number;
    recorded?: boolean;
    statusInvoice?: number;
    statusSendMail?: number;
    statusConverted?: number;
    dateSendMail?: Moment;
    email?: string;
    totalExportTaxAmount?: number;
    isBill?: boolean;
    IDAdjustInv?: string;
    ID_MIV?: string;
    IDReplaceInv?: string;
    accountingObject?: IAccountingObject;
    accountingObjectID?: string;
    employee?: IAccountingObject;
    invoiceTypeID?: string;
    templateID?: string;
    rsInwardOutwardID?: string;
    saReturnDetails?: ISaReturnDetails[];
    dueDate?: Moment;
    invoiceForm?: number;
    isDeliveryVoucher?: boolean;
    isExportInvoice?: boolean;
    total?: number;
    attachList?: boolean;
    listDate?: any;
    listNo?: string;
    listCommonNameInventory?: string;
}

export class SaReturn implements ISaReturn {
    constructor(
        public id?: string,
        public companyID?: string,
        public typeID?: number,
        public date?: Moment,
        public postedDate?: Moment,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public noFBookInventory?: string,
        public noMBookInventory?: string,
        public autoOWAmountCal?: number,
        public accountingObjectName?: string,
        public accountingObjectAddress?: string,
        public companyTaxCode?: string,
        public accountingObjectBankAccount?: string,
        public accountingObjectBankName?: string,
        public contactName?: string,
        public reason?: string,
        public numberAttach?: string,
        public invoiceTemplate?: string,
        public invoiceSeries?: string,
        public invoiceNo?: string,
        public invoiceDate?: Moment,
        public paymentMethod?: string,
        public currencyID?: string,
        public exchangeRate?: number,
        public totalAmount?: number,
        public totalAmountOriginal?: number,
        public totalDiscountAmount?: number,
        public totalDiscountAmountOriginal?: number,
        public totalPaymentAmount?: number,
        public totalPaymentAmountOriginal?: number,
        public totalVatAmount?: number,
        public totalVatAmountOriginal?: number,
        public totalOWAmount?: number,
        public recorded?: boolean,
        public statusInvoice?: number,
        public statusSendMail?: number,
        public statusConverted?: number,
        public dateSendMail?: Moment,
        public email?: string,
        public totalExportTaxAmount?: number,
        public isBill?: boolean,
        public IDAdjustInv?: string,
        public ID_MIV?: string,
        public IDReplaceInv?: string,
        public accountingObject?: IAccountingObject,
        public accountingObjectID?: string,
        public employee?: IAccountingObject,
        public invoiceTypeID?: string,
        public templateID?: string,
        public rsInwardOutwardID?: string,
        public saReturnDetails?: ISaReturnDetails[],
        public dueDate?: Moment,
        public invoiceForm?: number,
        public total?: number,
        public isDeliveryVoucher?: boolean,
        public isExportInvoice?: boolean,
        public isAttachList?: boolean,
        public listDate?: any,
        public listNo?: string,
        public listCommonNameInventory?: string
    ) {
        this.recorded = this.recorded || false;
        this.isBill = this.isBill || false;
    }
}
