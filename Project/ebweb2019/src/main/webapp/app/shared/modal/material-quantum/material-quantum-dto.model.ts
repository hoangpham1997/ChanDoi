export interface MaterialQuantumDto {
    id?: string;
    objectID?: string;
    objectType?: string;
    fromDate?: string;
    toDate?: string;
    quantity?: number;
    checked?: boolean;
    materialGoodsCode?: string;
    materialGoodsName?: string;
    costSetCode?: string;
    costSetName?: string;
}

export class MaterialQuantumDtoModel implements MaterialQuantumDto {
    constructor(
        public id?: string,
        public orderDate?: string,
        public orderNumber?: string,
        public productCode?: string,
        public productName?: string,
        public quantityReceipt?: number,
        public receivedQuantity?: number,
        public ppOrderId?: string,
        public priority?: number,
        public checked?: boolean,
        public materialGoodsCode?: string,
        public materialGoodsName?: string,
        public costSetCode?: string,
        public costSetName?: string
    ) {}
}
