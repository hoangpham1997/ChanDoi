export interface ISaBillDetailConvert {
    id?: string;
    saBillID?: string;
    materialGoodsID?: string;
    description?: string;
    debitAccount?: string;
    creditAccount?: string;
    unitID?: string;
    quantity?: number;
    unitPrice?: number;
    unitPriceOriginal?: number;
    amount?: number;
    amountOriginal?: number;
    mainUnitID?: string;
    mainQuantity?: number;
    mainUnitPrice?: number;
    mainConvertRate?: number;
    formula?: string;
    expiryDate?: any;
    lotNo?: string;
    vatRate?: number;
    vatAmount?: number;
    vatAmountOriginal?: number;
    orderPriority?: number;
    discountRate?: number;
    discountAmount?: number;
    discountAmountOriginal?: number;
    isPromotion?: boolean;
    units?: any[];
    saBillIdList?: any[];
}

export class SaBillDetailConvert implements ISaBillDetailConvert {
    constructor(
        public id?: string,
        public saBillID?: string,
        public materialGoodsID?: string,
        public description?: string,
        public debitAccount?: string,
        public creditAccount?: string,
        public unitID?: string,
        public quantity?: number,
        public unitPrice?: number,
        public unitPriceOriginal?: number,
        public amount?: number,
        public amountOriginal?: number,
        public mainUnitID?: string,
        public mainQuantity?: number,
        public mainUnitPrice?: number,
        public mainConvertRate?: number,
        public formula?: string,
        public expiryDate?: any,
        public lotNo?: string,
        public vatRate?: number,
        public vatAmount?: number,
        public vatAmountOriginal?: number,
        public orderPriority?: number,
        public discountRate?: number,
        public discountAmount?: number,
        public discountAmountOriginal?: number,
        public isPromotion?: boolean,
        public units?: any[],
        public saBillIdList?: any[]
    ) {}
}
