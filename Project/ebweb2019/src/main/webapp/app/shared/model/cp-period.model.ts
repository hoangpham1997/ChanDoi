import { Moment } from 'moment';
import { ICPPeriodDetails } from 'app/shared/model/cp-period-details.model';
import { ICPExpenseList } from 'app/shared/model/cp-expense-list.model';

export interface ICPPeriod {
    id?: string;
    companyID?: string;
    branchID?: string;
    typeLedger?: number;
    type?: number;
    fromDate?: Moment;
    toDate?: Moment;
    name?: string;
    cPPeriodDetails?: any[];
    cPExpenseList?: any[];
    cPAllocationGeneralExpenses?: any[];
    cPAllocationGeneralExpenseDetails?: any[];
    cPUncompletes?: any[];
    cPAllocationRates?: any[];
    cPUncompleteDetails?: any[];
    cPResults?: any[];
    cPAcceptanceDetails?: any[];
    cPAcceptances?: any[];
    debitAccount?: string;
    creditAccount?: string;
}

export class CPPeriod implements ICPPeriod {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public typeLedger?: number,
        public type?: number,
        public fromDate?: Moment,
        public toDate?: Moment,
        public name?: string,
        public cPPeriodDetails?: any[],
        public cPAllocationGeneralExpenses?: any[],
        public cPAllocationGeneralExpenseDetails?: any[],
        public cPUncompletes?: any[],
        public cPAllocationRates?: any[],
        public cPUncompleteDetails?: any[],
        public cPResults?: any[],
        public cPExpenseList?: any[],
        public cPAcceptanceDetails?: any[],
        public cPAcceptances?: any[],
        public debitAccount?: string,
        public creditAccount?: string
    ) {}
}
