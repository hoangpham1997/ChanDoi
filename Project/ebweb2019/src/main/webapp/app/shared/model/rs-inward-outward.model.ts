import { Moment } from 'moment';
import { Currency } from 'app/shared/model/currency.model';
import { AccountingObject } from 'app/shared/model/accounting-object.model';
import { AutoPrinciple } from 'app/shared/model/auto-principle.model';
import { RSInwardOutWardDetails } from 'app/shared/model/rs-inward-out-ward-details.model';

export interface IRSInwardOutward {
    id?: string;
    companyID?: string;
    branchID?: string;
    typeID?: any;
    refTypeID?: any;
    postedDate?: any;
    date?: any;
    typeLedger?: number;
    noFBook?: string;
    noMBook?: string;
    accountingObjectID?: string;
    accountingObjectName?: string;
    accountingObjectCode?: string;
    accountingObjectAddress?: string;
    contactName?: string;
    reason?: string;
    currencyID?: string;
    exchangeRate?: number;
    employeeID?: string;
    isImportPurchase?: boolean;
    transportMethodID?: string;
    totalAmount?: number;
    totalAmountOriginal?: number;
    templateID?: string;
    recorded?: boolean;
    exported?: boolean;
    accountingObject?: any;
    employee?: any;
    numberAttach?: string;
    refInvoiceForm?: number;
    refInvoiceNo?: string;
    refIsBill?: boolean;
    isDeliveryVoucher?: boolean;
    rsInwardOutwardDetails?: RSInwardOutWardDetails[];
}

export class RSInwardOutward implements IRSInwardOutward {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public typeID?: any,
        public refTypeID?: any,
        public postedDate?: any,
        public date?: any,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public noFBookInventory?: string,
        public noMBookInventory?: string,
        public accountingObjectID?: string,
        public accountingObjectName?: string,
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
        public employeeID?: string,
        public isImportPurchase?: boolean,
        public transportMethodID?: string,
        public totalAmount?: number,
        public totalAmountOriginal?: number,
        public sumTotalAmount?: number,
        public templateID?: string,
        public recorded?: boolean,
        public exported?: boolean,
        public accountingObject?: any,
        public employee?: any,
        public numberAttach?: string,
        public refID?: string,
        public refInvoiceForm?: number,
        public refInvoiceNo?: string,
        public refIsBill?: boolean,
        public isDeliveryVoucher?: boolean,
        public rsInwardOutwardDetails?: RSInwardOutWardDetails[]
    ) {
        this.isImportPurchase = this.isImportPurchase || false;
        this.recorded = this.recorded || false;
        this.exported = this.exported || false;
    }
}
