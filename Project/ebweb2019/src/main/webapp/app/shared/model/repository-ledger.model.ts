import { Moment } from 'moment';

export interface IRepositoryLedger {
    id?: number;
    companyID?: string;
    branchID?: string;
    referenceID?: string;
    date?: Moment;
    postedDate?: Moment;
    typeLedger?: number;
    noFBook?: string;
    noMBook?: string;
    account?: string;
    accountCorresponding?: string;
    repositoryID?: string;
    repositoryCode?: string;
    RepositoryName?: string;
    materialGoodsID?: string;
    materialGoodsCode?: string;
    materialGoodsName?: string;
    unitID?: string;
    unitPrice?: number;
    iwQuantity?: number;
    owQuantity?: number;
    iwAmount?: number;
    owAmount?: number;
    mainUnitID?: string;
    mainUnitPrice?: number;
    mainIWQuantity?: number;
    mainOWQuantity?: number;
    mainConvertRate?: number;
    formula?: string;
    reason?: string;
    description?: string;
    expiryDate?: Moment;
    lotNo?: string;
    budgetItemID?: string;
    costSetID?: string;
    statisticsCodeID?: string;
    expenseItemID?: string;
    orderPriority?: string;
}

export class RepositoryLedger implements IRepositoryLedger {
    constructor(
        public id?: number,
        public companyID?: string,
        public branchID?: string,
        public referenceID?: string,
        public date?: Moment,
        public postedDate?: Moment,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public account?: string,
        public accountCorresponding?: string,
        public repositoryID?: string,
        public repositoryCode?: string,
        public RepositoryName?: string,
        public materialGoodsID?: string,
        public materialGoodsCode?: string,
        public materialGoodsName?: string,
        public unitID?: string,
        public unitPrice?: number,
        public iwQuantity?: number,
        public owQuantity?: number,
        public iwAmount?: number,
        public owAmount?: number,
        public mainUnitID?: string,
        public mainUnitPrice?: number,
        public mainIWQuantity?: number,
        public mainOWQuantity?: number,
        public mainConvertRate?: number,
        public formula?: string,
        public reason?: string,
        public description?: string,
        public expiryDate?: Moment,
        public lotNo?: string,
        public budgetItemID?: string,
        public costSetID?: string,
        public statisticsCodeID?: string,
        public expenseItemID?: string,
        public orderPriority?: string
    ) {}
}
