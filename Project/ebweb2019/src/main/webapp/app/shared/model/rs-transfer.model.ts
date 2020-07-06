import { Currency } from 'app/shared/model/currency.model';
import { AccountingObject } from 'app/shared/model/accounting-object.model';
import { AutoPrinciple } from 'app/shared/model/auto-principle.model';
import { RSInwardOutWardDetails } from 'app/shared/model/rs-inward-out-ward-details.model';
import { RSTransferDetails } from 'app/shared/model/rs-transfer-details.model';
import { TransportMethod } from 'app/shared/model/transport-method.model';

export interface IRSTransfer {
    id?: string;
    companyID?: string;
    branchID?: string;
    typeID?: any;
    refTypeID?: any;
    postedDate?: any;
    invoiceDate?: any;
    date?: any;
    typeLedger?: number;
    noFBook?: string;
    noMBook?: string;
    invoiceTypeID?: string;
    invoiceTemplate?: string;
    invoiceSeries?: string;
    invoiceNo?: string;
    accountingObjectID?: string;
    accountingObjectName?: string;
    employeeID?: string;
    employeeName?: string;
    employee?: any;
    mobilizationOrderNo?: string;
    mobilizationOrderDate?: any;
    mobilizationOrderOf?: string;
    mobilizationOrderFor?: string;
    accountingObjectCode?: string;
    accountingObjectAddress?: string;
    contactName?: string;
    reason?: string;
    currencyID?: string;
    exchangeRate?: number;
    isImportPurchase?: boolean;
    totalAmount?: number;
    totalOWAmount?: number;
    totalAmountOriginal?: number;
    templateID?: string;
    recorded?: boolean;
    exported?: boolean;
    accountingObject?: any;
    numberAttach?: string;
    refInvoiceForm?: number;
    invoiceForm?: number;
    refInvoiceNo?: string;
    refIsBill?: boolean;
    rsTransferDetails?: RSTransferDetails[];
}

export class RSTransfer implements IRSTransfer {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public typeID?: any,
        public refTypeID?: any,
        public postedDate?: any,
        public invoiceDate?: any,
        public date?: any,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public invoiceTypeID?: string,
        public invoiceTemplate?: string,
        public invoiceSeries?: string,
        public invoiceForm?: number,
        public invoiceNo?: string,
        public noFBookInventory?: string,
        public noMBookInventory?: string,
        public accountingObjectID?: string,
        public accountingObjectName?: string,
        public employeeID?: string,
        public employeeName?: string,
        public employee?: any,
        public mobilizationOrderNo?: string,
        public mobilizationOrderDate?: any,
        public mobilizationOrderOf?: string,
        public mobilizationOrderFor?: string,
        public accountingObjectCode?: string,
        public accountingObjectAddress?: string,
        public contactName?: string,
        public reason?: string,
        public typeName?: string,
        public currencyID?: string,
        public currency?: Currency,
        public autoPrinciple?: AutoPrinciple,
        public accountingObjectEmployee?: AccountingObject,
        public exchangeRate?: number,
        public isImportPurchase?: boolean,
        public transportMethodID?: string,
        public totalAmount?: number,
        public totalOWAmount?: number,
        public totalAmountOriginal?: number,
        public sumTotalAmount?: number,
        public templateID?: string,
        public recorded?: boolean,
        public exported?: boolean,
        public accountingObject?: any,
        public numberAttach?: string,
        public refID?: string,
        public rsTransferDetails?: RSTransferDetails[]
    ) {
        this.isImportPurchase = this.isImportPurchase || false;
        this.recorded = this.recorded || false;
        this.exported = this.exported || false;
    }
}
