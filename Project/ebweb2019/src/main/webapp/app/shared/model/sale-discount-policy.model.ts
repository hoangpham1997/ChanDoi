import { IMaterialGoods } from 'app/shared/model//material-goods.model';

export interface ISaleDiscountPolicy {
    id?: string;
    quantityFrom?: number;
    quantityTo?: number;
    discountType?: number;
    discountResult?: number;
    materialGoods?: IMaterialGoods;
    materialGoodsID?: string;
}

export class SaleDiscountPolicy implements ISaleDiscountPolicy {
    constructor(
        public id?: string,
        public quantityFrom?: number,
        public quantityTo?: number,
        public discountType?: number,
        public discountResult?: number,
        public materialGoods?: IMaterialGoods,
        public materialGoodsID?: string
    ) {}
}
