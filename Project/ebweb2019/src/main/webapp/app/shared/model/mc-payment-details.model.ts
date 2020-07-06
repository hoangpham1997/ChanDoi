import { Moment } from 'moment';
import { IBankAccountDetails } from 'app/shared/model//bank-account-details.model';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';
import { IBudgetItem } from 'app/shared/model//budget-item.model';
import { ICostSet } from 'app/shared/model//cost-set.model';
import { IStatisticsCode } from 'app/shared/model//statistics-code.model';
import { IOrganizationUnit } from 'app/shared/model//organization-unit.model';
import { IExpenseItem } from 'app/shared/model//expense-item.model';
import { IEMContract } from 'app/shared/model/em-contract.model';

export interface IMCPaymentDetails {
    id?: string;
    mCPaymentID?: string;
    description?: string;
    debitAccount?: string;
    creditAccount?: string;
    amount?: number;
    amountOriginal?: number;
    contractID?: IEMContract;
    cashOutExchangeRateFB?: number;
    cashOutAmountFB?: number;
    cashOutDifferAmountFB?: number;
    cashOutDifferAccountFB?: number;
    cashOutExchangeRateMB?: number;
    cashOutAmountMB?: number;
    cashOutDifferAmountMB?: number;
    cashOutDifferAccountMB?: number;
    isMatch?: boolean;
    matchDate?: Moment;
    orderPriority?: number;
    bankAccountDetails?: IBankAccountDetails;
    accountingObject?: IAccountingObject;
    budgetItem?: IBudgetItem;
    costSet?: ICostSet;
    statisticsCode?: IStatisticsCode;
    organizationUnit?: IOrganizationUnit;
    expenseItem?: IExpenseItem;
}

export class MCPaymentDetails implements IMCPaymentDetails {
    constructor(
        public id?: string,
        public mCPaymentID?: string,
        public description?: string,
        public debitAccount?: string,
        public creditAccount?: string,
        public amount?: number,
        public amountOriginal?: number,
        public contractID?: IEMContract,
        public cashOutExchangeRateFB?: number,
        public cashOutAmountFB?: number,
        public cashOutDifferAmountFB?: number,
        public cashOutDifferAccountFB?: number,
        public cashOutExchangeRateMB?: number,
        public cashOutAmountMB?: number,
        public cashOutDifferAmountMB?: number,
        public cashOutDifferAccountMB?: number,
        public isMatch?: boolean,
        public matchDate?: Moment,
        public orderPriority?: number,
        public bankAccountDetails?: IBankAccountDetails,
        public accountingObject?: IAccountingObject,
        public budgetItem?: IBudgetItem,
        public costSet?: ICostSet,
        public statisticsCode?: IStatisticsCode,
        public organizationUnit?: IOrganizationUnit,
        public expenseItem?: IExpenseItem
    ) {
        this.isMatch = this.isMatch || false;
    }
}
