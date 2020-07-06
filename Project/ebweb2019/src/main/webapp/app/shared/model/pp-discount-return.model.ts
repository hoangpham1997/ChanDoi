import { Moment } from 'moment';
import { IPPDiscountReturnDetails } from 'app/shared/model/pp-discount-return-details.model';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { Unit } from 'app/shared/model/unit.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';

export interface IPPDiscountReturn {
    id?: any;
    companyID?: string;
    branchID?: string;
    typeID?: number;
    date?: Moment;
    postedDate?: Moment;
    typeLedger?: number;
    noFBook?: string;
    noMBook?: string;
    rsInwardOutwardID?: string;
    accountingObjectID?: string;
    accountingObjectName?: string;
    accountingObjectAddress?: string;
    accountingObjectBankAccount?: any;
    accountingObjectBankName?: string;
    accountingObjectType?: number;
    accountingObject?: IAccountingObject;
    companyTaxCode?: string;
    reason?: string;
    invoiceTypeID?: string;
    invoiceTemplate?: string;
    paymentMethod?: string;
    invoiceDate?: Moment;
    invoiceNo?: string;
    invoiceSeries?: string;
    currencyID?: string;
    exchangeRate?: number;
    transportMethodID?: string;
    dueDate?: Moment;
    paymentClauseID?: string;
    employeeID?: string;
    isAttachList?: boolean;
    listNo?: string;
    listDate?: Moment;
    listCommonNameInventory?: string;
    totalAmount?: number;
    totalAmountOriginal?: number;
    totalVATAmount?: number;
    totalVATAmountOriginal?: number;
    totalDiscountAmount?: number;
    totalDiscountAmounOriginal?: number;
    totalDiscountAmountReturn?: number;
    totalDiscountAmountOriginalReturn?: number;
    statusInvoice?: number;
    statusSendMail?: number;
    statusConverted?: number;
    dateSendMail?: Moment;
    email?: string;
    idAdjustInv?: string;
    idReplaceInv?: string;
    isBill?: boolean;
    idMIV?: string;
    templateID?: string;
    recorded?: boolean;
    contactName?: string;
    ppDiscountReturnDetails?: IPPDiscountReturnDetails[];
    numberAttach?: string;
    isDeliveryVoucher?: boolean;
    isExportInvoice?: any;
    invoiceForm?: any;
    noBook?: any;
    saBillInvoiceNo?: string;
    totalSumAmount?: any;
    materialGoodCode?: any;
    repositoryCode?: any;
}

export class PPDiscountReturn implements IPPDiscountReturn {
    constructor(
        public id?: any,
        public companyID?: string,
        public branchID?: string,
        public typeID?: number,
        public date?: any,
        public postedDate?: any,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public rsInwardOutwardID?: string,
        public accountingObjectID?: string,
        public accountingObjectName?: string,
        public accountingObjectAddress?: string,
        public accountingObjectBankAccount?: any,
        public accountingObjectBankName?: string,
        public companyTaxCode?: string,
        public reason?: string,
        public invoiceTypeID?: string,
        public invoiceTemplate?: string,
        public paymentMethod?: string,
        public invoiceDate?: Moment,
        public invoiceNo?: string,
        public invoiceSeries?: string,
        public currencyID?: string,
        public exchangeRate?: number,
        public transportMethodID?: string,
        public dueDate?: Moment,
        public paymentClauseID?: string,
        public employeeID?: string,
        public isAttachList?: boolean,
        public listNo?: string,
        public listDate?: Moment,
        public listCommonNameInventory?: string,
        public totalAmount?: number,
        public totalAmountOriginal?: number,
        public totalVATAmount?: number,
        public totalVATAmountOriginal?: number,
        public totalDiscountAmount?: number,
        public totalDiscountAmounOriginal?: number,
        public totalDiscountAmountReturn?: number,
        public totalDiscountAmountOriginalReturn?: number,
        public statusInvoice?: number,
        public statusSendMail?: number,
        public statusConverted?: number,
        public dateSendMail?: Moment,
        public email?: string,
        public idAdjustInv?: string,
        public idReplaceInv?: string,
        public isBill?: boolean,
        public idMIV?: string,
        public templateID?: string,
        public recorded?: boolean,
        public contactName?: string,
        public ppDiscountReturnDetails?: IPPDiscountReturnDetails[],
        public numberAttach?: any,
        public isDeliveryVoucher?: boolean,
        public isExportInvoice?: any,
        public invoiceForm?: any,
        public noBook?: any,
        public saBillInvoiceNo?: string,
        public totalSumAmount?: any,
        public attachList?: any,
        public materialGoodCode?: any,
        public repositoryCode?: any
    ) {
        this.isAttachList = this.isAttachList || false;
        this.isBill = this.isBill || false;
        this.recorded = this.recorded || false;
    }
}
