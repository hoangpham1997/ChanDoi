export interface ITIAllocationAllocated {
    id?: number;
    tiAllocationID?: string;
    toolsID?: string;
    toolsItem?: any;
    toolsCode?: string;
    toolsName?: string;
    objectID?: string;
    objectType?: number;
    totalAllocationAmount?: number;
    rate?: number;
    allocationAmount?: number;
    costAccount?: string;
    expenseItemID?: string;
    costSetID?: string;
    orderPriority?: number;
}

export class TIAllocationAllocated implements ITIAllocationAllocated {
    constructor(
        public id?: number,
        public tiAllocationID?: string,
        public toolsID?: string,
        public toolsItem?: any,
        public toolsCode?: string,
        public toolsName?: string,
        public objectID?: string,
        public objectType?: number,
        public totalAllocationAmount?: number,
        public rate?: number,
        public allocationAmount?: number,
        public costAccount?: string,
        public expenseItemID?: string,
        public costSetID?: string,
        public orderPriority?: number
    ) {}
}
