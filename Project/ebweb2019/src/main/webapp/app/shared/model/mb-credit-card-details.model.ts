import { IMBCreditCard } from 'app/shared/model//mb-credit-card.model';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';
import { IStatisticsCode } from 'app/shared/model//statistics-code.model';
import { IOrganizationUnit } from 'app/shared/model//organization-unit.model';
import { IExpenseItem } from 'app/shared/model//expense-item.model';
import { ICostSet } from 'app/shared/model//cost-set.model';
import { IBudgetItem } from 'app/shared/model//budget-item.model';
import { IContractState } from 'app/shared/model/contract-state.model';
import { IEMContract } from 'app/shared/model/em-contract.model';

export interface IMBCreditCardDetails {
    id?: number;
    mBCreditCardID?: string;
    description?: string;
    debitAccount?: string;
    creditAccount?: string;
    amount?: number;
    amountOriginal?: number;
    contractID?: string;
    cashOutExchangeRateFB?: number;
    cashOutAmountFB?: number;
    cashOutDifferAmountFB?: number;
    cashOutDifferAccountFB?: string;
    cashOutExchangeRateMB?: number;
    cashOutAmountMB?: number;
    cashOutDifferAmountMB?: number;
    cashOutDifferAccountMB?: string;
    orderPriority?: number;
    mBCreditCard?: IMBCreditCard;
    accountingObjectID?: string;
    statisticsCodeID?: string;
    departmentID?: string;
    expenseItemID?: string;
    costSetID?: string;
    budgetItemID?: string;
    debitAccountItem?: any;
    creditAccountItem?: any;
}

export class MBCreditCardDetails implements IMBCreditCardDetails {
    constructor(
        public id?: number,
        public mBCreditCardID?: string,
        public description?: string,
        public debitAccount?: string,
        public creditAccount?: string,
        public amount?: number,
        public amountOriginal?: number,
        public cashOutExchangeRateFB?: number,
        public cashOutAmountFB?: number,
        public cashOutDifferAmountFB?: number,
        public cashOutDifferAccountFB?: string,
        public cashOutExchangeRateMB?: number,
        public cashOutAmountMB?: number,
        public cashOutDifferAmountMB?: number,
        public cashOutDifferAccountMB?: string,
        public orderPriority?: number,
        public mBCreditCard?: IMBCreditCard,
        public accountingObjectID?: string,
        public statisticsCodeID?: string,
        public departmentID?: string,
        public expenseItemID?: string,
        public costSetID?: string,
        public contractID?: string,
        public budgetItemID?: string,
        public debitAccountItem?: any,
        public creditAccountItem?: any
    ) {}
}
