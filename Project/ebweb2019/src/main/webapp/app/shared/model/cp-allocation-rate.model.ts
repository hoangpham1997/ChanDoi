export interface ICPAllocationRate {
    id?: string;
    cPPeriodID?: string;
    cPPeriodDetailID?: string;
    allocationMethod?: number;
    costSetID?: string;
    materialGoodsID?: string;
    isStandardItem?: boolean;
    quantity?: number;
    priceQuantum?: number;
    priceStandard?: number;
    coefficient?: number;
    quantityStandard?: number;
    allocationStandard?: number;
    allocatedRate?: number;
}

export class CPAllocationRate implements ICPAllocationRate {
    constructor(
        public id?: string,
        public cPPeriodID?: string,
        public cPPeriodDetailID?: string,
        public allocationMethod?: number,
        public costSetID?: string,
        public materialGoodsID?: string,
        public isStandardItem?: boolean,
        public quantity?: number,
        public priceQuantum?: number,
        public priceStandard?: number,
        public coefficient?: number,
        public quantityStandard?: number,
        public allocationStandard?: number,
        public allocatedRate?: number
    ) {
        this.isStandardItem = this.isStandardItem || false;
    }
}
