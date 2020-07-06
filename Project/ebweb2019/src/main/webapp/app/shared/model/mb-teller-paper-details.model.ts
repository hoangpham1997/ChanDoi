import { IMBTellerPaper } from 'app/shared/model//mb-teller-paper.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IContractState } from 'app/shared/model/contract-state.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IEMContract } from 'app/shared/model/em-contract.model';

export interface IMBTellerPaperDetails {
    id?: string;
    description?: string;
    debitAccount?: string;
    creditAccount?: string;
    amount?: number;
    amountOriginal?: number;
    budgetItemID?: string;
    costSetID?: string;
    eMContractID?: string;
    accountingObjectID?: string;
    statisticsCodeID?: string;
    departmentID?: string;
    expenseItemID?: string;
    cashOutExchangeRateFb?: number;
    cashOutAmountFb?: number;
    cashOutDifferAmountFb?: number;
    cashOutDifferAccountFb?: string;
    cashOutExchangeRateMb?: number;
    cashOutAmountMb?: number;
    cashOutDifferAmountMb?: number;
    cashOutDifferAccountMb?: string;
    orderPriority?: number;
    mBTellerPaperId?: string;
    debitAccountItem?: any;
    creditAccountItem?: any;
}

export class MBTellerPaperDetails implements IMBTellerPaperDetails {
    constructor(
        public id?: string,
        public description?: string,
        public debitAccount?: string,
        public creditAccount?: string,
        public amount?: number,
        public amountOriginal?: number,
        public budgetItemID?: string,
        public costSetID?: string,
        public eMContractID?: string,
        public accountingObjectID?: string,
        public statisticsCodeID?: string,
        public departmentID?: string,
        public expenseItemID?: string,
        public cashOutExchangeRateFb?: number,
        public cashOutAmountFb?: number,
        public cashOutDifferAmountFb?: number,
        public cashOutDifferAccountFb?: string,
        public cashOutExchangeRateMb?: number,
        public cashOutAmountMb?: number,
        public cashOutDifferAmountMb?: number,
        public cashOutDifferAccountMb?: string,
        public orderPriority?: number,
        public mBTellerPaperId?: string,
        public debitAccountItem?: any,
        public creditAccountItem?: any
    ) {}
}
