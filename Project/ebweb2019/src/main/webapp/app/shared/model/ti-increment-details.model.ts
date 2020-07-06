import { ITools } from 'app/shared/model/tools.model';

export interface ITIIncrementDetails {
    id?: string;
    tiIncrementID?: string;
    toolsID?: string;
    toolsItem?: any;
    description?: string;
    unitID?: string;
    quantity?: number;
    unitPrice?: number;
    amount?: number;
    accountingObjectID?: string;
    budgetItemID?: string;
    costSetID?: string;
    contractID?: string;
    statisticCodeID?: string;
    departmentID?: string;
    expenseItemID?: string;
    orderPriority?: number;
    toolsName?: string;
    quantityRest?: any;
    organizationUnits?: any;
}

export class TIIncrementDetails implements ITIIncrementDetails {
    constructor(
        public id?: string,
        public tiIncrementID?: string,
        public toolsID?: string,
        public toolsItem?: ITools,
        public description?: string,
        public unitID?: string,
        public quantity?: number,
        public unitPrice?: number,
        public amount?: number,
        public accountingObjectID?: string,
        public budgetItemID?: string,
        public costSetID?: string,
        public contractID?: string,
        public statisticCodeID?: string,
        public departmentID?: string,
        public expenseItemID?: string,
        public toolsName?: string,
        public orderPriority?: number,
        public quantityRest?: any,
        public organizationUnits?: any
    ) {}
}
