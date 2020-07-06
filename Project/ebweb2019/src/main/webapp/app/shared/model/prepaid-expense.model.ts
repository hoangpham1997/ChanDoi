import { Moment } from 'moment';
import { IPrepaidExpenseAllocation, PrepaidExpenseAllocation } from 'app/shared/model/prepaid-expense-allocation.model';

export interface IPrepaidExpense {
    id?: any;
    companyID?: string;
    branchID?: string;
    typeLedger?: number;
    typeExpense?: number;
    prepaidExpenseCode?: string;
    prepaidExpenseName?: string;
    date?: Moment;
    amount?: number;
    allocationAmount?: number;
    allocationTime?: number;
    allocatedPeriod?: number;
    allocatedAmount?: number;
    allocationAccount?: string;
    isActive?: boolean;
    active?: boolean;
    prepaidExpenseAllocation?: PrepaidExpenseAllocation[];
    prepaidExpenseVouchers?: IPrepaidExpenseAllocation[];
    prepaidExpenseCodeItem?: any;
    isAllocation?: any;
}

export class PrepaidExpense implements IPrepaidExpense {
    constructor(
        public id?: any,
        public companyID?: string,
        public branchID?: string,
        public typeLedger?: number,
        public typeExpense?: number,
        public prepaidExpenseCode?: string,
        public prepaidExpenseName?: string,
        public date?: Moment,
        public amount?: number,
        public allocationAmount?: number,
        public allocationTime?: number,
        public allocatedPeriod?: number,
        public allocatedAmount?: number,
        public allocationAccount?: string,
        public isActive?: boolean,
        public prepaidExpenseCodeItem?: any,
        public prepaidExpenseAllocation?: PrepaidExpenseAllocation[],
        public prepaidExpenseVouchers?: IPrepaidExpenseAllocation[],
        public active?: boolean,
        public isAllocation?: any
    ) {
        this.isActive = this.isActive || false;
    }
}
