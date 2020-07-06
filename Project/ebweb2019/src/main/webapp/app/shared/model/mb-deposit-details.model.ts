import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IContractState } from 'app/shared/model/contract-state.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IEMContract } from 'app/shared/model/em-contract.model';

export interface IMBDepositDetails {
    id?: string;
    mBDepositID?: string;
    description?: string;
    debitAccount?: string;
    creditAccount?: string;
    amount?: number;
    amountOriginal?: number;
    refVoucherExchangeRate?: number;
    lastExchangeRate?: number;
    differAmount?: number;
    orderPriority?: number;
    accountingObjectID?: string;
    // accountingObjectID?: string;
    expenseItemID?: string;
    costSetID?: string;
    departmentID?: string;
    contractID?: string;
    budgetItemID?: string;
    statisticsCodeID?: string;
    debitAccountItem?: any;
    creditAccountItem?: any;
    deductionDebitAccountItem?: any;
}

export class MBDepositDetails implements IMBDepositDetails {
    constructor(
        public id?: string,
        public mBDepositID?: string,
        public description?: string,
        public debitAccount?: string,
        public creditAccount?: string,
        public amount?: number,
        public amountOriginal?: number,
        public refVoucherExchangeRate?: number,
        public lastExchangeRate?: number,
        public differAmount?: number,
        public orderPriority?: number,
        public accountingObjectID?: string,
        // public accountingObjectID?: string,
        public expenseItemID?: string,
        public costSetID?: string,
        public departmentID?: string,
        public contractID?: string,
        public budgetItem?: string,
        public statisticsCodeID?: string,
        public debitAccountItem?: any,
        public creditAccountItem?: any
    ) {}
}
