export interface ICPAllocationGeneralExpenseDetails {
    id?: string;
    cPAllocationGeneralExpenseID?: string;
    costSetID?: string;
    costSetCode?: string;
    costSetName?: string;
    contractID?: string;
    accountNumber?: string;
    expenseItemID?: string;
    expenseItemCode?: string;
    allocatedRate?: number;
    allocatedAmount?: number;
    expenseItemType?: number;
}

export class CPAllocationGeneralExpenseDetails implements ICPAllocationGeneralExpenseDetails {
    constructor(
        public id?: string,
        public cPAllocationGeneralExpenseID?: string,
        public costSetID?: string,
        public costSetCode?: string,
        public costSetName?: string,
        public contractID?: string,
        public accountNumber?: string,
        public expenseItemID?: string,
        public expenseItemCode?: string,
        public allocatedRate?: number,
        public allocatedAmount?: number,
        public expenseItemType?: number
    ) {}
}
