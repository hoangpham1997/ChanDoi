import { IUnit } from 'app/shared/model/unit.model';
import { MaterialGoods } from 'app/shared/model/material-goods.model';
import { SaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';

export interface ISAOrderDetails {
    id?: string;
    sAOrderID?: string;
    description?: string;
    quantity?: number;
    quantityDelivery?: number;
    unitPrice?: number;
    unitPriceOriginal?: number;
    mainQuantity?: number;
    mainUnitPrice?: number;
    mainConvertRate?: number;
    formula?: string;
    amount?: number;
    amountOriginal?: number;
    discountRate?: number;
    discountAmount?: number;
    discountAmountOriginal?: number;
    vATRate?: number;
    vATAmount?: number;
    vATAmountOriginal?: number;
    vATDescription?: string;
    sAQuoteID?: string;
    sAQuoteDetailID?: string;
    orderPriority?: number;
    materialGoods?: MaterialGoods;
    unit?: IUnit;
    units?: IUnit[];
    mainUnit?: IUnit;
    mainUnits?: IUnit[];
    unitPriceOriginals?: any[];
    saleDiscountPolicysDTO?: SaleDiscountPolicy[];
}

export class SAOrderDetails implements ISAOrderDetails {
    constructor(
        public id?: string,
        public sAOrderID?: string,
        public description?: string,
        public quantity?: number,
        public quantityDelivery?: number,
        public unitPrice?: number,
        public unitPriceOriginal?: number,
        public mainQuantity?: number,
        public mainUnitPrice?: number,
        public mainConvertRate?: number,
        public formula?: string,
        public amount?: number,
        public amountOriginal?: number,
        public discountRate?: number,
        public discountAmount?: number,
        public discountAmountOriginal?: number,
        public vATRate?: number,
        public vATAmount?: number,
        public vATAmountOriginal?: number,
        public vATDescription?: string,
        public sAQuoteID?: string,
        public sAQuoteDetailID?: string,
        public orderPriority?: number,
        public materialGoods?: MaterialGoods,
        public unit?: IUnit,
        public mainUnit?: IUnit,
        public units?: IUnit[],
        public mainUnits?: IUnit[],
        public unitPriceOriginals?: any[],
        public saleDiscountPolicysDTO?: SaleDiscountPolicy[]
    ) {}
}
