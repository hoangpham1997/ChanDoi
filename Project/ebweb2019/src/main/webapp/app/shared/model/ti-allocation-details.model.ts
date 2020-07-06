export interface ITIAllocationDetails {
    id?: number;
    tiAllocationID?: string;
    toolsID?: string;
    toolsName?: string;
    toolsCode?: any;
    description?: string;
    totalAllocationAmount?: number;
    allocationAmount?: number;
    remainingAmount?: number;
    orderPriority?: number;
    tiAllocationAllocateds?: any;
    tiAllocationPosts?: any;
    toolsDetailsConvertDTOS?: any;
    allocationAwaitAccount?: any;
}

export class TIAllocationDetails implements ITIAllocationDetails {
    constructor(
        public id?: number,
        public tiAllocationID?: string,
        public toolsID?: string,
        public toolsCode?: any,
        public toolsName?: string,
        public description?: string,
        public totalAllocationAmount?: number,
        public allocationAmount?: number,
        public remainingAmount?: number,
        public orderPriority?: number,
        public tiAllocationAllocateds?: any,
        public tiAllocationPosts?: any,
        public toolsDetailsConvertDTOS?: any,
        public allocationAwaitAccount?: any
    ) {}
}
