export interface IGOtherVoucherDetailExpenseAllocation {
    id?: number;
    gOtherVoucherID?: string;
    prepaidExpenseID?: string;
    allocationAmount?: number;
    objectID?: string;
    objectType?: number;
    allocationRate?: number;
    amount?: number;
    costAccount?: string;
    expenseItemID?: string;
    costSetID?: string;
    orderPriority?: number;
    gOtherVoucherDetailExpensesItem?: any;
    prepaidExpenseCode?: any;
    prepaidExpenseName?: string;
    checked?: boolean;
}

export class GOtherVoucherDetailExpenseAllocation implements IGOtherVoucherDetailExpenseAllocation {
    constructor(
        public id?: number,
        public gOtherVoucherID?: string,
        public prepaidExpenseID?: string,
        public allocationAmount?: number,
        public objectID?: string,
        public objectType?: number,
        public allocationRate?: number,
        public amount?: number,
        public costAccount?: string,
        public expenseItemID?: string,
        public costSetID?: string,
        public orderPriority?: number,
        public gOtherVoucherDetailExpensesItem?: any,
        public prepaidExpenseCode?: any,
        public prepaidExpenseName?: string,
        public checked?: boolean
    ) {
        this.checked = false;
    }
}
