import { Moment } from 'moment';

export interface IToolledger {
    id?: number;
    companyID?: string;
    branchID?: string;
    typeID?: number;
    referenceID?: string;
    typeLedger?: number;
    noFBook?: string;
    noMBook?: string;
    date?: Moment;
    postedDate?: Moment;
    toolsID?: string;
    toolCode?: string;
    toolName?: string;
    reason?: string;
    description?: string;
    unitPrice?: number;
    incrementAllocationTime?: number;
    decrementAllocationTime?: number;
    incrementQuantity?: number;
    decrementQuantity?: number;
    incrementAmount?: number;
    decrementAmount?: number;
    allocationAmount?: number;
    allocatedAmount?: number;
    departmentID?: string;
    remainingQuantity?: number;
    remainingAllocaitonTimes?: number;
    remainingAmount?: number;
    orderPriority?: number;
}

export class Toolledger implements IToolledger {
    constructor(
        public id?: number,
        public companyID?: string,
        public branchID?: string,
        public typeID?: number,
        public referenceID?: string,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public date?: Moment,
        public postedDate?: Moment,
        public toolsID?: string,
        public toolCode?: string,
        public toolName?: string,
        public reason?: string,
        public description?: string,
        public unitPrice?: number,
        public incrementAllocationTime?: number,
        public decrementAllocationTime?: number,
        public incrementQuantity?: number,
        public decrementQuantity?: number,
        public incrementAmount?: number,
        public decrementAmount?: number,
        public allocationAmount?: number,
        public allocatedAmount?: number,
        public departmentID?: string,
        public remainingQuantity?: number,
        public remainingAllocaitonTimes?: number,
        public remainingAmount?: number,
        public orderPriority?: number
    ) {}
}
