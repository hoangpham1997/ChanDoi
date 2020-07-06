import { Moment } from 'moment';

export interface IGeneralLedger {
    id?: string;
    branchID?: number;
    referenceID?: number;
    detailID?: number;
    typeID?: number;
    date?: Moment;
    postedDate?: Moment;
    typeLedger?: number;
    noFBook?: string;
    noMBook?: string;
    invoiceSeries?: string;
    invoiceDate?: Moment;
    invoiceNo?: string;
    account?: string;
    accountCorresponding?: string;
    bankAccountDetailID?: number;
    bankAccount?: string;
    bankName?: string;
    currencyID?: string;
    exchangeRate?: number;
    debitAmount?: number;
    debitAmountOriginal?: number;
    creditAmount?: number;
    creditAmountOriginal?: number;
    reason?: string;
    description?: string;
    accountingObjectID?: number;
    accountingObjectCode?: string;
    accountingObjectName?: string;
    accountingObjectAddress?: string;
    contactName?: string;
    employeeID?: number;
    employeeCode?: string;
    employeeName?: string;
    materialGoodsID?: number;
    materialGoodsCode?: string;
    materialGoodsName?: string;
    repositoryID?: number;
    repositoryCode?: string;
    repositoryName?: string;
    unitID?: number;
    quantity?: number;
    unitPrice?: number;
    unitPriceOriginal?: number;
    mainUnitID?: number;
    mainQuantity?: number;
    mainUnitPrice?: number;
    mainConvertRate?: number;
    formula?: string;
    organizationUnitID?: number;
    expenseItemID?: number;
    budgetItemID?: number;
    costSetID?: number;
    contractID?: number;
    statisticsCodeID?: number;
    refDateTime?: Moment;
    orderPriority?: number;
}

export class GeneralLedger implements IGeneralLedger {
    constructor(
        public id?: string,
        public branchID?: number,
        public referenceID?: number,
        public detailID?: number,
        public typeID?: number,
        public date?: Moment,
        public postedDate?: Moment,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public invoiceSeries?: string,
        public invoiceDate?: Moment,
        public invoiceNo?: string,
        public account?: string,
        public accountCorresponding?: string,
        public bankAccountDetailID?: number,
        public bankAccount?: string,
        public bankName?: string,
        public currencyID?: string,
        public exchangeRate?: number,
        public debitAmount?: number,
        public debitAmountOriginal?: number,
        public creditAmount?: number,
        public creditAmountOriginal?: number,
        public reason?: string,
        public description?: string,
        public accountingObjectID?: number,
        public accountingObjectCode?: string,
        public accountingObjectName?: string,
        public accountingObjectAddress?: string,
        public contactName?: string,
        public employeeID?: number,
        public employeeCode?: string,
        public employeeName?: string,
        public materialGoodsID?: number,
        public materialGoodsCode?: string,
        public materialGoodsName?: string,
        public repositoryID?: number,
        public repositoryCode?: string,
        public repositoryName?: string,
        public unitID?: number,
        public quantity?: number,
        public unitPrice?: number,
        public unitPriceOriginal?: number,
        public mainUnitID?: number,
        public mainQuantity?: number,
        public mainUnitPrice?: number,
        public mainConvertRate?: number,
        public formula?: string,
        public organizationUnitID?: number,
        public expenseItemID?: number,
        public budgetItemID?: number,
        public costSetID?: number,
        public contractID?: number,
        public statisticsCodeID?: number,
        public refDateTime?: Moment,
        public orderPriority?: number
    ) {}
}
