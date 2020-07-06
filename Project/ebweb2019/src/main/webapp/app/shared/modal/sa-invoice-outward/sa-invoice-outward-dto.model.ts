export interface SaReturnDto {
    id?: string;
    orderDate?: string;
    orderNumber?: string;
    productCode?: string;
    productName?: string;
    quantityReceipt?: number;
    receivedQuantity?: number;
    ppOrderId?: string;
    checked?: boolean;
    priority?: number;
}

export class SaInvoiceOutwardDtoModel implements SaReturnDto {
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
        public checked?: boolean
    ) {}
}
