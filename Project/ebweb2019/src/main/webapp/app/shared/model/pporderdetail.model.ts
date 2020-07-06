import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { Unit } from 'app/shared/model/unit.model';
import { IMaterialGoodsInStock } from 'app/shared/model/material-goods.model';

export interface IPporderdetail {
    id?: string;
    ppOrderId?: string;
    materialGoodsId?: string;
    description?: string;
    unitId?: string;
    quantity?: number;
    quantityReceipt?: number;
    unitPrice?: number;
    unitPriceOriginal?: number;
    discountRate?: number;
    discountAmount?: number;
    discountAmountOriginal?: number;
    vatRate?: number;
    vatAmount?: number;
    vatAmountOriginal?: number;
    mainUnitId?: string;
    mainQuantity?: number;
    mainUnitPrice?: number;
    mainConvertRate?: number;
    formula?: string;
    orderPriority?: number;
    amount?: number;
    amountOriginal?: number;
    accountingObject?: IAccountingObject;
}

export class PPOrderDetail implements IPporderdetail {
    constructor(
        public id?: string,
        public ppOrderId?: string,
        public materialGoodsId?: string,
        public description?: string,
        public unitId?: string,
        public quantity?: number,
        public quantityReceipt?: number,
        public unitPrice?: number,
        public unitPrices?: any[],
        public unitPriceOriginal?: number,
        public discountRate?: number,
        public amountOriginal?: number,
        public amount?: number,
        public unit?: Unit,
        public convertRates?: any,
        public mainUnit?: Unit,
        public materialGood?: IMaterialGoodsInStock,
        public discountAmount?: number,
        public discountAmountOriginal?: number,
        public vatRate?: number,
        public vatAmount?: number,
        public vatAmountOriginal?: number,
        public mainUnitId?: string,
        public mainQuantity?: number,
        public mainUnitPrice?: number,
        public mainConvertRate?: number,
        public formula?: string,
        public orderPriority?: number
    ) {}
}
