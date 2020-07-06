export interface IPrepaidExpenseAllocation {
    id?: number;
    prepaidExpenseID?: string;
    allocationObjectID?: string;
    allocationObjectType?: number;
    allocationObjectName?: string;
    allocationObjectCode?: string;
    allocationRate?: number;
    costAccount?: string;
    expenseItemID?: string;
    prepaidExpenseCodeItem?: any;
    expenseItemCode?: string;
    orderPriority?: number;
}

export class PrepaidExpenseAllocation implements IPrepaidExpenseAllocation {
    constructor(
        public id?: number,
        public prepaidExpenseID?: string,
        public allocationObjectID?: string,
        public allocationObjectType?: number,
        public allocationObjectName?: string,
        public allocationObjectCode?: string,
        public allocationRate?: number,
        public costAccount?: string,
        public expenseItemID?: string,
        public prepaidExpenseCodeItem?: any,
        public expenseItemCode?: string,
        public orderPriority?: number
    ) {}
}
