import { Moment } from 'moment';
import { IMBCreditCardDetails } from 'app/shared/model//mb-credit-card-details.model';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';
import { ITransportMethod } from 'app/shared/model//transport-method.model';
import { IPaymentClause } from 'app/shared/model//payment-clause.model';
import { IMBCreditCardDetailTax } from 'app/shared/model//mb-credit-card-detail-tax.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { IMBCreditCardDetailVendor } from 'app/shared/model/mb-credit-card-detail-vendor.model';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';

export interface IMBCreditCard {
    id?: string;
    branchID?: string;
    companyID?: string;
    typeID?: number;
    date?: Moment;
    postedDate?: Moment;
    typeLedger?: number;
    noFBook?: string;
    noMBook?: string;
    creditCardID?: string;
    creditCardNumber?: string;
    accountingObjectName?: string;
    accountingObjectAddress?: string;
    taxCode?: string;
    accountingObjectBankAccountDetailID?: string;
    accountingObjectBankName?: string;
    accountingObjectType?: number;
    reason?: string;
    currencyID?: string;
    exchangeRate?: number;
    isImportPurchase?: boolean;
    pPOrderID?: string;
    totalAmount?: number;
    totalAmountOriginal?: number;
    totalVATAmount?: number;
    totalVATAmountOriginal?: number;
    employeeID?: string;
    templateID?: string;
    recorded?: boolean;
    exported?: boolean;
    isMatch?: boolean;
    matchDate?: Moment;
    mBCreditCardDetails?: IMBCreditCardDetails[];
    mBCreditCardDetailVendor?: IMBCreditCardDetailVendor[];
    // currency?: ICurrency;
    accountingObjectID?: string;
    transportMethodID?: string;
    paymentClauseID?: string;
    mbCreditCardDetailTax?: IMBCreditCardDetailTax[];
    viewVouchers?: IViewVoucher[];
    ppServiceID?: string;
    total?: number;
    ppInvoiceID?: string;
    storedInRepository?: boolean;
}

export class MBCreditCard implements IMBCreditCard {
    constructor(
        public id?: string,
        public branchID?: string,
        public companyID?: string,
        public typeID?: number,
        public date?: Moment,
        public postedDate?: Moment,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public creditCardID?: string,
        public creditCardNumber?: string,
        public accountingObjectName?: string,
        public taxCode?: string,
        public accountingObjectAddress?: string,
        public accountingObjectBankAccountDetailID?: string,
        public accountingObjectBankName?: string,
        public accountingObjectType?: number,
        public reason?: string,
        public currencyID?: string,
        public exchangeRate?: number,
        public isImportPurchase?: boolean,
        public pPOrderID?: string,
        public totalAmount?: number,
        public totalAmountOriginal?: number,
        public totalVATAmount?: number,
        public totalVATAmountOriginal?: number,
        public employeeID?: string,
        public templateID?: string,
        public recorded?: boolean,
        public isMatch?: boolean,
        public exported?: boolean,
        public matchDate?: Moment,
        public mBCreditCardDetails?: IMBCreditCardDetails[],
        public accountingObjectID?: string,
        public transportMethodID?: string,
        public paymentClauseID?: string,
        public mbCreditCardDetailTax?: IMBCreditCardDetailTax[],
        public viewVouchers?: IViewVoucher[],
        public ppServiceID?: string,
        public total?: number
    ) {
        this.isImportPurchase = this.isImportPurchase || false;
        this.recorded = this.recorded || false;
        this.isMatch = this.isMatch || false;
        this.exported = this.exported || false;
    }
}
