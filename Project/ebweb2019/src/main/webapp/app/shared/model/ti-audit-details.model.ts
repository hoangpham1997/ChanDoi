export interface ITIAuditDetails {
    id?: string;
    tiAuditID?: string;
    toolsID?: string;
    departmentID?: string;
    quantityOnBook?: number;
    quantityInventory?: number;
    diffQuantity?: number;
    executeQuantity?: number;
    recommendation?: number;
    note?: string;
    orderPriority?: number;
    toolsItem?: any;
    toolsName?: any;
    toolsCode?: any;
    toolName?: any;
    toolCode?: any;
    unit?: any;
    unitID?: any;
    quantity?: number;
    toolID?: any;
}

export class TIAuditDetails implements ITIAuditDetails {
    constructor(
        public id?: string,
        public tiAuditID?: string,
        public toolsID?: string,
        public departmentID?: string,
        public quantityOnBook?: number,
        public quantityInventory?: number,
        public diffQuantity?: number,
        public executeQuantity?: number,
        public recommendation?: number,
        public note?: string,
        public orderPriority?: number,
        public toolsItem?: any,
        public toolsName?: any,
        public toolsCode?: any,
        public toolName?: any,
        public toolCode?: any,
        public unit?: any,
        public unitID?: any,
        public quantity?: number,
        public toolID?: any
    ) {}
}
