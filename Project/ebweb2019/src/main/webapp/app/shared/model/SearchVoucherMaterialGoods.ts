import { Moment } from 'moment';

export interface ISearchVoucherMaterialGoods {
    materialGoodsType?: string;
    unitID?: string;
    materialGoodsCategoryID?: string;
    keySearch?: string;
    materialGoodsCode?: String;
    materialGoodsName?: String;
}

export class SearchVoucherMaterialGoods implements ISearchVoucherMaterialGoods {
    constructor(
        public materialGoodsType?: string,
        public unitID?: string,
        public materialGoodsCategoryID?: string,
        public keySearch?: string,
        public materialGoodsCode?: string,
        public materialGoodsName?: string
    ) {}
}
