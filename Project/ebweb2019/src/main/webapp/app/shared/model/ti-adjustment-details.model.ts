export interface ITIAdjustmentDetails {
    toolsName?: any;
    id?: string;
    toolsITem?: any;
    tiAdjustmentID?: string;
    toolsID?: string;
    description?: string;
    quantity?: number;
    remainingAmount?: number;
    newRemainingAmount?: number;
    diffRemainingAmount?: number;
    remainAllocationTimes?: number;
    newRemainingAllocationTime?: number;
    differAllocationTime?: number;
    allocatedAmount?: number;
    orderPriority?: number;
    toolsItem?: any;
    toolsCode?: any;
}

export class TIAdjustmentDetails implements ITIAdjustmentDetails {
    constructor(
        public toolsName?: any,
        public id?: string,
        public toolsITem?: any,
        public tiAdjustmentID?: string,
        public toolsID?: string,
        public description?: string,
        public quantity?: number,
        public remainingAmount?: number,
        public newRemainingAmount?: number,
        public diffRemainingAmount?: number,
        public remainAllocationTimes?: number,
        public newRemainingAllocationTime?: number,
        public differAllocationTime?: number,
        public allocatedAmount?: number,
        public orderPriority?: number,
        public toolsItem?: any,
        public toolsCode?: any
    ) {}
}
