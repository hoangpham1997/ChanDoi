export interface ICPAllocationGeneralExpense {
    id?: string;
    cPPeriodID?: string;
    accountNumber?: string;
    expenseItemID?: string;
    totalCost?: number;
    unallocatedAmount?: number;
    allocatedRate?: number;
    allocatedAmount?: number;
    allocationMethod?: number;
}

export class CPAllocationGeneralExpense implements ICPAllocationGeneralExpense {
    constructor(
        public id?: string,
        public cPPeriodID?: string,
        public accountNumber?: string,
        public expenseItemID?: string,
        public totalCost?: number,
        public unallocatedAmount?: number,
        public allocatedRate?: number,
        public allocatedAmount?: number,
        public allocationMethod?: number
    ) {}
}
