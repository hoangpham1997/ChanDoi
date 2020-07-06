import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IEMContract } from 'app/shared/model/em-contract.model';

export interface IPPPayVendorAccounting {
    description?: string;
    debitAccount?: string;
    creditAccount?: string;
    totalAmountOriginal?: number;
    totalAmount?: number;
    accountingObjectCode?: string;
    budgetItem?: IBudgetItem;
    expenseItem?: IExpenseItem;
    department?: IOrganizationUnit;
    costSet?: ICostSet;
    emContract?: IEMContract;
    statisticsCode?: IStatisticsCode;
    isIrrationalCost?: boolean;
}

export class PPPayVendorAccounting implements IPPPayVendorAccounting {
    constructor(
        public description?: string,
        public debitAccount?: string,
        public creditAccount?: string,
        public totalAmountOriginal?: number,
        public totalAmount?: number,
        public accountingObjectCode?: string,
        public budgetItem?: IBudgetItem,
        public expenseItem?: IExpenseItem,
        public department?: IOrganizationUnit,
        public costSet?: ICostSet,
        public emContract?: IEMContract,
        public statisticsCode?: IStatisticsCode,
        public isIrrationalCost?: boolean
    ) {
        this.isIrrationalCost = this.isIrrationalCost || false;
    }
}
