import { Moment } from 'moment';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';

export interface IRequestReport {
    typeReport?: string;
    bill?: boolean;
    fromDate?: Moment; // từ ngày
    toDate?: Moment; // đến ngày
    groupTheSameItem?: boolean; // Cộng gộp các bút toán giống nhau
    showAccumAmount?: boolean;
    similarSum?: boolean;
    companyID?: string;
    option?: boolean;
    unitType?: number;
    repositoryID?: string;
    grade?: number;
    listMaterialGoods?: any[];
    listCostSets?: any[];
    listEMContracts?: any[];
    listCostSetID?: any[];
    listExpenseItems?: any[];
    accountingObjects?: any[];
    currencyID?: string;
    accountNumber?: string;
    bankAccountDetail?: string;
    accountList?: any[];
    createDate?: string;
    bankAccountDetailID?: string;
    materialGoodsCategoryID?: string;
    timeLineVoucher?: string;
    employeeID?: string;
    fileName?: string;
    tools?: any[];
    departments?: any[];
    typeShowCurrency?: boolean;
    getAmountOriginal?: boolean;
    cPPeriodID?: string;
    typeMethod?: number;
    statisticsCodes?: any[];
    expenseItems?: any[];
}

export class RequestReport implements IRequestReport {
    constructor(
        public typeReport?: string,
        public bill?: boolean,
        public fromDate?: Moment, // từ ngày
        public toDate?: Moment, // đến ngày
        public groupTheSameItem?: boolean, // Cộng gộp các bút toán giống nhau
        public showAccumAmount?: boolean,
        public similarSum?: boolean,
        public dependent?: boolean,
        public companyID?: string,
        public option?: boolean,
        public unitType?: number,
        public repositoryID?: string,
        public grade?: number,
        public listMaterialGoods?: any[],
        public listEMContracts?: any[],
        public listCostSetID?: any[],
        public listCostSets?: any[],
        public listExpenseItems?: any[],
        public accountingObjects?: any[],
        public currencyID?: string,
        public accountNumber?: string,
        public bankAccountDetail?: string,
        public accountList?: any[],
        public createDate?: string,
        public bankAccountDetailID?: string,
        public materialGoodsCategoryID?: string,
        public timeLineVoucher?: string,
        public employeeID?: string,
        public fileName?: string,
        public tools?: any[],
        public departments?: any[],
        public typeShowCurrency?: boolean,
        public getAmountOriginal?: boolean,
        public cPPeriodID?: string,
        public typeMethod?: number,
        public statisticsCodes?: any[],
        public expenseItems?: any[]
    ) {}
}
