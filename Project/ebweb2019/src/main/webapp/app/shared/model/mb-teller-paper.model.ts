import { Moment } from 'moment';
import { IMBTellerPaperDetails } from 'app/shared/model/mb-teller-paper-details.model';
import { IMBTellerPaperDetailTax } from 'app/shared/model/mb-teller-paper-detail-tax.model';
import { BankAccountDetails, IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { IPaymentClause } from 'app/shared/model/payment-clause.model';
import { ITransportMethod } from 'app/shared/model/transport-method.model';
import { IType } from 'app/shared/model/type.model';
import { IMBTellerPaperDetailVendor } from 'app/shared/model/mb-teller-paper-detail-vendor.model';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';

export interface IMBTellerPaper {
    id?: string;
    companyId?: string;
    branchId?: string;
    typeId?: number;
    date?: Moment;
    postedDate?: Moment;
    typeLedger?: number;
    noFBook?: string;
    noMBook?: string;
    bankAccountDetailID?: string;
    bankName?: string;
    reason?: string;
    accountingObjectType?: number;
    accountingObjectID?: string;
    accountingObjectName?: string;
    accountingObjectAddress?: string;
    accountingObjectBankAccount?: string;
    accountingObjectBankName?: string;
    taxCode?: string;
    receiver?: string;
    identificationNo?: string;
    issueDate?: Moment;
    issueBy?: string;
    currencyId?: string;
    exchangeRate?: number;
    isImportPurchase?: boolean;
    paymentClauseID?: string;
    transportMethodID?: string;
    total?: number;
    totalAmount?: number;
    totalAmountOriginal?: number;
    totalVatAmount?: number;
    totalVatAmountOriginal?: number;
    employeeID?: string;
    templateId?: string;
    isMatch?: boolean;
    matchDate?: Moment;
    recorded?: boolean;
    mBTellerPaperDetails?: IMBTellerPaperDetails[];
    mBTellerPaperDetailVendor?: IMBTellerPaperDetailVendor[];
    mBTellerPaperDetailTaxs?: IMBTellerPaperDetailTax[];
    viewVouchers?: IViewVoucher[];
    ppServiceID?: string;
    ppInvocieID?: string;
    storedInRepository?: boolean;
}

export class MBTellerPaper implements IMBTellerPaper {
    constructor(
        public id?: string,
        public companyId?: string,
        public branchId?: string,
        public typeId?: number,
        public date?: Moment,
        public postedDate?: Moment,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public bankAccountDetailID?: string,
        public bankName?: string,
        public reason?: string,
        public accountingObjectType?: number,
        public accountingObjectID?: string,
        public accountingObjectName?: string,
        public accountingObjectAddress?: string,
        public accountingObjectBankAccount?: string,
        public accountingObjectBankName?: string,
        public taxCode?: string,
        public receiver?: string,
        public identificationNo?: string,
        public issueDate?: Moment,
        public issueBy?: string,
        public currencyId?: string,
        public exchangeRate?: number,
        public isImportPurchase?: boolean,
        public paymentClauseID?: string,
        public transportMethodID?: string,
        public total?: number,
        public totalAmount?: number,
        public totalAmountOriginal?: number,
        public totalVatAmount?: number,
        public totalVatAmountOriginal?: number,
        public employeeID?: string,
        public templateId?: string,
        public isMatch?: boolean,
        public matchDate?: Moment,
        public recorded?: boolean,
        public mBTellerPaperDetails?: IMBTellerPaperDetails[],
        public mBTellerPaperDetailTaxs?: IMBTellerPaperDetailTax[],
        public mBTellerPaperDetailVendor?: IMBTellerPaperDetailVendor[],
        public viewVouchers?: IViewVoucher[],
        public ppServiceID?: string,
        public ppInvocieID?: string,
        public storedInRepository?: boolean
    ) {
        this.isImportPurchase = this.isImportPurchase || false;
        this.isMatch = this.isMatch || false;
        this.recorded = this.recorded || false;
        this.reason = reason ? reason : '';
        // this.bankName = bankName ? bankName : '';
        // this.bankAccountDetails = bankAccountDetails ? bankAccountDetails : new BankAccountDetails();
    }
}
