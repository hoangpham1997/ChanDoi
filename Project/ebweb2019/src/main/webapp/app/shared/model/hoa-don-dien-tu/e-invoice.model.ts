import { Moment } from 'moment';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IMCAuditDetails } from 'app/shared/model/mc-audit-details.model';
import { IMCAuditDetailMember } from 'app/shared/model/mc-audit-detail-member.model';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';
import { IEInvoiceDetails } from 'app/shared/model/hoa-don-dien-tu/e-invoice-details.model';

export interface IEInvoice {
    id?: string;
    companyID?: string;
    typeID?: number;
    invoiceTemplate?: string;
    invoiceSeries?: string;
    invoiceNo?: string;
    invoiceDate?: Moment;
    accountingObjectCode?: string;
    contactName?: string;
    accountingObjectName?: string;
    accountingObjectAddress?: string;
    accountingObjectBankName?: string;
    accountingObjectBankAccount?: string;
    contactMobile?: string;
    paymentMethod?: string;
    companyTaxCode?: string;
    exchangeRate?: number;
    currencyID?: string;
    email?: string;
    totalAmountOriginal?: number;
    totalAmount?: number;
    totalDiscountAmount?: number;
    totalDiscountAmountOriginal?: number;
    totalVATAmount?: number;
    totalVATAmountOriginal?: number;
    statusInvoice?: number;
    statusSendMail?: boolean;
    refTable?: string;
    statusConverted?: boolean;
    iDAdjustInv?: string;
    invocieNoAdjusted?: string;
    invoiceTemplateAdjusted?: string;
    invoiceSeriesAdjusted?: string;
    iDReplaceInv?: string;
    invocieNoReplaced?: string;
    invoiceTemplateReplaced?: string;
    invoiceSeriesReplaced?: string;
    checked?: boolean;
    recorded?: boolean;
    total?: number;
    eInvoiceDetails?: IEInvoiceDetails[];
    eInvoiceDetailsAjRp?: IEInvoiceDetails[];
}

export class EInvoice implements IEInvoice {
    constructor(
        public id?: string,
        public companyID?: string,
        public typeID?: number,
        public invoiceTemplate?: string,
        public invoiceSeries?: string,
        public invoiceNo?: string,
        public invoiceDate?: Moment,
        public accountingObjectCode?: string,
        public contactName?: string,
        public accountingObjectName?: string,
        public accountingObjectAddress?: string,
        public accountingObjectBankName?: string,
        public accountingObjectBankAccount?: string,
        public contactMobile?: string,
        public paymentMethod?: string,
        public companyTaxCode?: string,
        public exchangeRate?: number,
        public currencyID?: string,
        public email?: string,
        public totalAmountOriginal?: number,
        public totalAmount?: number,
        public totalDiscountAmount?: number,
        public totalDiscountAmountOriginal?: number,
        public totalVATAmount?: number,
        public totalVATAmountOriginal?: number,
        public statusInvoice?: number,
        public statusSendMail?: boolean,
        public refTable?: string,
        public statusConverted?: boolean,
        public iDAdjustInv?: string,
        public invocieNoAdjusted?: string,
        public invoiceTemplateAdjusted?: string,
        public invoiceSeriesAdjusted?: string,
        public iDReplaceInv?: string,
        public invocieNoReplaced?: string,
        public invoiceTemplateReplaced?: string,
        public invoiceSeriesReplaced?: string,
        public checked?: boolean,
        public recorded?: boolean,
        public total?: number,
        public eInvoiceDetails?: IEInvoiceDetails[],
        public eInvoiceDetailsAjRp?: IEInvoiceDetails[]
    ) {}
}
