import { Moment } from 'moment';

export interface IOpAccountModel {
    id?: string;

    companyId?: string;

    typeId?: number;

    postedDate?: Moment;

    postedDateStr?: string;

    expiryDate?: Moment;

    expiredDate?: string;

    parentAccountID?: string;

    typeLedger?: number;

    accountNumber?: string;

    currencyId?: string;

    exchangeRate?: number;

    debitAmount?: number;

    debitAmountOriginal?: number;

    debitBalance?: number;

    creditAmount?: number;

    creditAmountOriginal?: number;

    creditBalance?: number;

    accountingObjectId?: string;

    accountingObjectName?: string;

    accountingObjectCode?: string;

    bankAccountDetailId?: string;

    bankAccount?: string;

    contractId?: string;

    noBookContract?: string;

    costSetId?: string;

    costSetCode?: string;

    expenseItemId?: string;

    expenseItemCode?: string;

    departmentId?: string;

    organizationUnitCode?: string;

    statisticsCodeId?: string;

    statisticsCode?: string;

    budgetItemId?: string;

    budgetItemCode?: string;

    orderPriority?: number;

    count?: number;
}

export class OpAccountModel implements IOpAccountModel {
    constructor(
        public id?: string,
        public companyID?: string,
        public typeID?: number,
        public postedDate?: Moment,
        public postedDateStr?: string,
        public expiryDate?: Moment,
        public typeLedger?: number,
        public accountNumber?: string,
        public currencyID?: string,
        public exchangeRate?: number,
        public debitAmount?: number,
        public debitAmountOriginal?: number,
        public debitBalance?: number,
        public creditAmount?: number,
        public creditAmountOriginal?: number,
        public creditBalance?: number,
        public accountingObjectId?: string,
        public accountingObjectCode?: string,
        public accountingObjectName?: string,
        public bankAccountDetailId?: string,
        public bankAccount?: string,
        public contractId?: string,
        public noBookContract?: string,
        public costSetId?: string,
        public costSetCode?: string,
        public expenseItemId?: string,
        public expenseItemCode?: string,
        public departmentId?: string,
        public organizationUnitCode?: string,
        public statisticsCodeId?: string,
        public statisticsCode?: string,
        public budgetItemId?: string,
        public budgetItemCode?: string,
        public orderPriority?: number,
        public count?: number
    ) {}
}
