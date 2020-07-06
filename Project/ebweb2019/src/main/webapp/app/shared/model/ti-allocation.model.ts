import { Moment } from 'moment';

export interface ITIAllocation {
    tiAllocationAllocateds?: any[];
    tiAllocationDetails?: any[];
    tiAllocationPosts?: any[];
    id?: string;
    companyID?: string;
    branchID?: string;
    typeID?: number;
    date?: Moment;
    postedDate?: Moment;
    typeLedger?: number;
    noFBook?: string;
    noMBook?: string;
    reason?: string;
    month?: number;
    Year?: number;
    totalAmount?: number;
    recorded?: boolean;
    templateID?: string;
    refVouchers?: any[];
}

export class TIAllocation implements ITIAllocation {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public typeID?: number,
        public date?: Moment,
        public postedDate?: Moment,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public reason?: string,
        public month?: number,
        public Year?: number,
        public totalAmount?: number,
        public recorded?: boolean,
        public templateID?: string,
        public tiAllocationAllocateds?: any[],
        public tiAllocationDetails?: any[],
        public tiAllocationPosts?: any[],
        public refVouchers?: any[]
    ) {
        this.recorded = this.recorded || false;
    }
}
