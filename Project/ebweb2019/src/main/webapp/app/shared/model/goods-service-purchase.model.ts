export interface IGoodsServicePurchase {
    id?: string;
    goodsServicePurchaseCode?: string;
    goodsServicePurchaseName?: string;
    description?: string;
    isActive?: boolean;
    isSecurity?: boolean;
}

export class GoodsServicePurchase implements IGoodsServicePurchase {
    constructor(
        public id?: string,
        public goodsServicePurchaseCode?: string,
        public goodsServicePurchaseName?: string,
        public description?: string,
        public isActive?: boolean,
        public isSecurity?: boolean
    ) {
        this.isActive = this.isActive || false;
        this.isSecurity = this.isSecurity || false;
    }
}
