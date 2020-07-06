export interface ITIAllocationPost {
    id?: number;
    tiAllocationID?: string;
    description?: string;
    debitAccount?: string;
    creditAccount?: string;
    amount?: number;
    departmentID?: string;
    costSetID?: string;
    expenseItemID?: string;
    budgetItemID?: string;
    statisticCodeID?: string;
    orderPriority?: number;
}

export class TIAllocationPost implements ITIAllocationPost {
    constructor(
        public id?: number,
        public tiAllocationID?: string,
        public description?: string,
        public debitAccount?: string,
        public creditAccount?: string,
        public amount?: number,
        public departmentID?: string,
        public costSetID?: string,
        public expenseItemID?: string,
        public budgetItemID?: string,
        public statisticCodeID?: string,
        public orderPriority?: number
    ) {}
}
