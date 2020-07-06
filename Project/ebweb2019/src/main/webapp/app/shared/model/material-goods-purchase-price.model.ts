import { IMaterialGoods } from 'app/shared/model//material-goods.model';
import { ICurrency } from 'app/shared/model//currency.model';
import { IUnit } from 'app/shared/model//unit.model';

export interface IMaterialGoodsPurchasePrice {
    id?: string;
    unitPrice?: number;
    materialGoods?: IMaterialGoods;
    currencyID?: string;
    unitID?: string;
}

export class MaterialGoodsPurchasePrice implements IMaterialGoodsPurchasePrice {
    constructor(
        public id?: string,
        public unitPrice?: number,
        public materialGoods?: IMaterialGoods,
        public currencyID?: string,
        public unitID?: string
    ) {}
}
