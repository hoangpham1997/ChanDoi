import { Moment } from 'moment';
import { IMCReceipt } from 'app/shared/model//mc-receipt.model';
import { IBankAccountDetails } from 'app/shared/model//bank-account-details.model';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';
import { IStatisticsCode } from 'app/shared/model//statistics-code.model';
import { IOrganizationUnit } from 'app/shared/model//organization-unit.model';
import { IExpenseItem } from 'app/shared/model//expense-item.model';
import { ICostSet } from 'app/shared/model//cost-set.model';
import { IBudgetItem } from 'app/shared/model//budget-item.model';
import { IEMContract } from 'app/shared/model/em-contract.model';

export interface IMCReceiptDetails {
    id?: string;
    description?: string;
    debitAccount?: string;
    creditAccount?: string;
    amount?: number;
    amountOriginal?: number;
    contractID?: IEMContract;
    cashOutExchangeRateFB?: number;
    cashOutAmountFB?: number;
    cashOutDifferAmountFB?: number;
    cashOutDifferAccountFB?: string;
    cashOutExchangeRateMB?: number;
    cashOutAmountMB?: number;
    cashOutDifferAmountMB?: number;
    cashOutDifferAccountMB?: string;
    isMatch?: boolean;
    matchDate?: Moment;
    orderPriority?: number;
    mcreceipt?: IMCReceipt;
    bankAccountDetails?: IBankAccountDetails;
    accountingObject?: IAccountingObject;
    statisticsCode?: IStatisticsCode;
    organizationUnit?: IOrganizationUnit;
    expenseItem?: IExpenseItem;
    costSet?: ICostSet;
    budgetItem?: IBudgetItem;
}

export class MCReceiptDetails implements IMCReceiptDetails {
    constructor(
        public id?: string,
        public description?: string,
        public debitAccount?: string,
        public creditAccount?: string,
        public amount?: number,
        public amountOriginal?: number,
        public contractID?: IEMContract,
        public cashOutExchangeRateFB?: number,
        public cashOutAmountFB?: number,
        public cashOutDifferAmountFB?: number,
        public cashOutDifferAccountFB?: string,
        public cashOutExchangeRateMB?: number,
        public cashOutAmountMB?: number,
        public cashOutDifferAmountMB?: number,
        public cashOutDifferAccountMB?: string,
        public isMatch?: boolean,
        public matchDate?: Moment,
        public orderPriority?: number,
        public mcreceipt?: IMCReceipt,
        public bankAccountDetails?: IBankAccountDetails,
        public accountingObject?: IAccountingObject,
        public statisticsCode?: IStatisticsCode,
        public organizationUnit?: IOrganizationUnit,
        public expenseItem?: IExpenseItem,
        public costSet?: ICostSet,
        public budgetItem?: IBudgetItem
    ) {
        this.isMatch = this.isMatch || false;
    }
}
