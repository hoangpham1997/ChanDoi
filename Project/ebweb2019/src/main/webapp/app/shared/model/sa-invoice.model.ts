import { Moment } from 'moment';
import { ISAInvoiceDetails } from 'app/shared/model/sa-invoice-details.model';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';
import { IRSInwardOutward } from 'app/shared/model/rs-inward-outward.model';
import { ISaBill } from 'app/shared/model/sa-bill.model';
import { IMCReceipt } from 'app/shared/model/mc-receipt.model';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';

export interface ISAInvoice {
    id?: string;
    companyID?: string;
    branchID?: string;
    typeID?: number;
    date?: Moment;
    postedDate?: Moment;
    typeLedger?: number;
    noFBook?: string;
    noMBook?: string;
    noBook?: string;
    accountingObject?: any;
    accountingObjectID?: string;
    paymentVoucherID?: string;
    accountingObjectName?: string;
    accountingObjectAddress?: string;
    accountingObjectBankAccount?: string;
    accountingObjectBankName?: string;
    companyTaxCode?: string;
    contactName?: string;
    reason?: string;
    numberAttach?: string;
    isDeliveryVoucher?: boolean;
    rsInwardOutwardID?: string;
    paymentMethod?: string;
    invoiceForm?: number;
    invoiceTemplate?: string;
    invoiceTypeID?: string;
    invoiceSeries?: string;
    invoiceNo?: string;
    invoiceDate?: Moment;
    currencyID?: string;
    exchangeRate?: number;
    paymentClauseID?: string;
    dueDate?: Moment;
    transportMethodID?: string;
    employeeID?: string;
    totalAmount?: number;
    totalAmountOriginal?: number;
    totalDiscountAmount?: number;
    totalDiscountAmountOriginal?: number;
    totalVATAmount?: number;
    totalVATAmountOriginal?: number;
    totalCapitalAmount?: number;
    placeOfDelivery?: string;
    listNo?: string;
    listDate?: Moment;
    listCommonNameInventory?: string;
    isAttachList?: boolean;
    isDiscountByVoucher?: boolean;
    statusInvoice?: number;
    statusSendMail?: number;
    statusConverted?: number;
    dateSendMail?: Moment;
    email?: string;
    iDAdjustInv?: string;
    iDReplaceInv?: string;
    isBill?: boolean;
    totalExportTaxAmount?: number;
    iD_MIV?: string;
    templateID?: string;
    recorded?: boolean;
    isMatch?: boolean;
    matchDate?: Moment;
    exported?: boolean;
    isExportInvoice?: boolean;
    sAInvoiceDetails?: ISAInvoiceDetails[];
    viewVouchers?: IViewVoucher[];
    rsInwardOutward?: IRSInwardOutward;
    saBill?: ISaBill;
    mcReceipt?: IMCReceipt;
    mcReceiptID?: string;
    mbDeposit?: IMBDeposit;
    mbDepositID?: string;
    total?: number;
    checkRecord?: boolean;
    bankAccountDetailID?: string;
    bankName?: string;
    typeName?: string;
}

export class SAInvoice implements ISAInvoice {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public typeID?: number,
        public date?: Moment,
        public postedDate?: Moment,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public noBook?: string,
        public accountingObject?: any,
        public accountingObjectID?: string,
        public paymentVoucherID?: string,
        public accountingObjectName?: string,
        public accountingObjectAddress?: string,
        public accountingObjectBankAccount?: string,
        public accountingObjectBankName?: string,
        public companyTaxCode?: string,
        public contactName?: string,
        public reason?: string,
        public numberAttach?: string,
        public isDeliveryVoucher?: boolean,
        public paymentMethod?: string,
        public invoiceForm?: number,
        public invoiceTemplate?: string,
        public invoiceTypeID?: string,
        public invoiceSeries?: string,
        public invoiceNo?: string,
        public invoiceDate?: Moment,
        public currencyID?: string,
        public exchangeRate?: number,
        public paymentClauseID?: string,
        public dueDate?: Moment,
        public transportMethodID?: string,
        public employeeID?: string,
        public totalAmount?: number,
        public totalAmountOriginal?: number,
        public totalDiscountAmount?: number,
        public totalDiscountAmountOriginal?: number,
        public totalVATAmount?: number,
        public totalVATAmountOriginal?: number,
        public totalCapitalAmount?: number,
        public placeOfDelivery?: string,
        public listNo?: string,
        public listDate?: Moment,
        public listCommonNameInventory?: string,
        public isAttachList?: boolean,
        public isDiscountByVoucher?: boolean,
        public statusInvoice?: number,
        public statusSendMail?: number,
        public statusConverted?: number,
        public dateSendMail?: Moment,
        public email?: string,
        public iDAdjustInv?: string,
        public iDReplaceInv?: string,
        public isBill?: boolean,
        public totalExportTaxAmount?: number,
        public iD_MIV?: string,
        public templateID?: string,
        public recorded?: boolean,
        public isMatch?: boolean,
        public matchDate?: Moment,
        public exported?: boolean,
        public isExportInvoice?: boolean,
        public sAInvoiceDetails?: ISAInvoiceDetails[],
        public viewVouchers?: IViewVoucher[],
        public rsInwardOutward?: IRSInwardOutward,
        public saBill?: ISaBill,
        public mcReceipt?: IMCReceipt,
        public mbDeposit?: IMBDeposit,
        public rsInwardOutwardID?: string,
        public mcReceiptID?: string,
        public total?: number,
        public mbDepositID?: string,
        public checkRecord?: boolean,
        public bankAccountDetailID?: string,
        public bankName?: string,
        public typeName?: string
    ) {
        this.isDeliveryVoucher = this.isDeliveryVoucher || false;
        this.isAttachList = this.isAttachList || false;
        this.isDiscountByVoucher = this.isDiscountByVoucher || false;
        this.isBill = this.isBill || false;
        this.recorded = this.recorded || false;
        this.isMatch = this.isMatch || false;
        this.exported = this.exported || false;
        this.isExportInvoice = this.isExportInvoice || false;
        this.checkRecord = this.checkRecord || false;
    }
}
