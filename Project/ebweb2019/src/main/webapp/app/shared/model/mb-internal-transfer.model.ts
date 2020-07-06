import { Moment } from 'moment';

export interface IMBInternalTransfer {
    id?: string;
    branchID?: string;
    typeID?: number;
    date?: Moment;
    postedDate?: Moment;
    typeLedger?: number;
    noFBook?: string;
    noMBook?: string;
    reason?: string;
    employeeID?: string;
    totalAmount?: number;
    totalAmountOriginal?: number;
    toBranchID?: string;
    recorded?: string;
}

export class MBInternalTransfer implements IMBInternalTransfer {
    constructor(
        public id?: string,
        public branchID?: string,
        public typeID?: number,
        public date?: Moment,
        public postedDate?: Moment,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public reason?: string,
        public employeeID?: string,
        public totalAmount?: number,
        public totalAmountOriginal?: number,
        public toBranchID?: string,
        public recorded?: string
    ) {}
}
