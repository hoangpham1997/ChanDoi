export interface IGOtherVoucherDetailExpense {
    id?: number;
    gOtherVoucherID?: string;
    prepaidExpenseID?: string;
    amount?: number;
    remainingAmount?: number;
    allocationAmount?: number;
    orderPriority?: number;
    prepaidExpenseCode?: any;
    prepaidExpenseName?: any;
}

export class GOtherVoucherDetailExpense implements IGOtherVoucherDetailExpense {
    constructor(
        public id?: number,
        public gOtherVoucherID?: string,
        public prepaidExpenseID?: string,
        public amount?: number,
        public remainingAmount?: number,
        public allocationAmount?: number,
        public orderPriority?: number,
        public prepaidExpenseCode?: any,
        public prepaidExpenseName?: any
    ) {}
}
