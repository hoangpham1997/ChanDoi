import { Moment } from 'moment';
import { IMCReceiptDetails } from 'app/shared/model//mc-receipt-details.model';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';
import { ITransportMethod } from 'app/shared/model//transport-method.model';
import { IPaymentClause } from 'app/shared/model//payment-clause.model';
import { IMCReceiptDetailCustomer } from 'app/shared/model//mc-receipt-detail-customer.model';
import { IMCReceiptDetailTax } from 'app/shared/model//mc-receipt-detail-tax.model';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';

export interface IMCReceipt {
    id?: string;
    branchID?: string;
    typeID?: number;
    date?: Moment;
    postedDate?: Moment;
    typeLedger?: number;
    noFBook?: string;
    noMBook?: string;
    accountingObjectName?: string;
    accountingObjectAddress?: string;
    payers?: string;
    reason?: string;
    numberAttach?: string;
    currencyID?: string;
    exchangeRate?: number;
    sAQuoteID?: string;
    sAOrderID?: string;
    totalAmount?: number;
    totalAmountOriginal?: number;
    totalVATAmount?: number;
    totalVATAmountOriginal?: number;
    employeeID?: string;
    accountingObjectType?: number;
    templateID?: string;
    recorded?: boolean;
    exported?: boolean;
    taxCode?: string;
    mcreceiptDetails?: IMCReceiptDetails[];
    accountingObject?: IAccountingObject;
    transportMethod?: ITransportMethod;
    paymentClause?: IPaymentClause;
    mcreceiptDetailCustomers?: IMCReceiptDetailCustomer[];
    mcreceiptDetailTaxes?: IMCReceiptDetailTax[];
    mCAuditID?: string;
    viewVouchers?: IViewVoucher[];
    total?: number;
    sAInvoiceID?: string;
    sAIsBill?: boolean;
    sAInvoiceForm?: number;
    sAInvoiceNo?: string;
}

export class MCReceipt implements IMCReceipt {
    constructor(
        public id?: string,
        public branchID?: string,
        public typeID?: number,
        public date?: Moment,
        public postedDate?: Moment,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public accountingObjectName?: string,
        public accountingObjectAddress?: string,
        public payers?: string,
        public reason?: string,
        public numberAttach?: string,
        public currencyID?: string,
        public exchangeRate?: number,
        public sAQuoteID?: string,
        public sAOrderID?: string,
        public totalAmount?: number,
        public totalAmountOriginal?: number,
        public totalVATAmount?: number,
        public totalVATAmountOriginal?: number,
        public employeeID?: string,
        public accountingObjectType?: number,
        public templateID?: string,
        public recorded?: boolean,
        public exported?: boolean,
        public taxCode?: string,
        public mCAuditID?: string,
        public sAIsBill?: boolean,
        public sAInvoiceForm?: number,
        public sAInvoiceNo?: string,
        public mcreceiptDetails?: IMCReceiptDetails[],
        public accountingObject?: IAccountingObject,
        public transportMethod?: ITransportMethod,
        public paymentClause?: IPaymentClause,
        public mcreceiptDetailCustomers?: IMCReceiptDetailCustomer[],
        public mcreceiptDetailTaxes?: IMCReceiptDetailTax[],
        public viewVouchers?: IViewVoucher[],
        public total?: number,
        public sAInvoiceID?: string
    ) {
        this.recorded = this.recorded || false;
        this.exported = this.exported || false;
    }
}
