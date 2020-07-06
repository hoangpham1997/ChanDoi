import { IMaterialGoods } from 'app/shared/model//material-goods.model';
import { IUnit } from 'app/shared/model/unit.model';
import { SaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';

export interface ISAQuoteDetails {
    id?: string;
    sAQuoteID?: string;
    description?: string;
    unit?: IUnit;
    quantity?: number;
    unitPrice?: number;
    unitPriceOriginal?: number;
    mainUnit?: IUnit;
    mainQuantity?: number;
    mainUnitPrice?: number;
    mainConvertRate?: number;
    formula?: string;
    vATDescription?: string;
    amount?: number;
    amountOriginal?: number;
    discountRate?: number;
    discountAmount?: number;
    discountAmountOriginal?: number;
    vATRate?: number;
    vATAmount?: number;
    vATAmountOriginal?: number;
    orderPriority?: number;
    materialGoods?: IMaterialGoods;
    saleDiscountPolicy?: SaleDiscountPolicy[];
    units?: IUnit[];
}

export class SAQuoteDetails implements ISAQuoteDetails {
    constructor(
        public id?: string,
        public sAQuoteID?: string,
        public description?: string,
        public unit?: IUnit,
        public quantity?: number,
        public unitPrice?: number,
        public unitPriceOriginal?: number,
        public mainUnit?: IUnit,
        public mainQuantity?: number,
        public mainUnitPrice?: number,
        public mainConvertRate?: number,
        public formula?: string,
        public vATDescription?: string,
        public amount?: number,
        public amountOriginal?: number,
        public discountRate?: number,
        public discountAmount?: number,
        public discountAmountOriginal?: number,
        public vATRate?: number,
        public vATAmount?: number,
        public vATAmountOriginal?: number,
        public orderPriority?: number,
        public _materialGoods?: IMaterialGoods,
        public saleDiscountPolicy?: SaleDiscountPolicy[],
        public units?: IUnit[]
    ) {}
}
