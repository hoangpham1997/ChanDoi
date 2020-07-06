import { ICostSet } from 'app/shared/model//cost-set.model';
import { IMaterialGoods } from 'app/shared/model//material-goods.model';

export interface ICostSetMaterialGood {
    id?: string;
    deScription?: string;
    orderPriority?: number;
    costSet?: ICostSet;
    materialGoods?: IMaterialGoods;
    materialGoodsID?: string;
}

export class CostSetMaterialGood implements ICostSetMaterialGood {
    constructor(
        public id?: string,
        public deScription?: string,
        public orderPriority?: number,
        public costSet?: ICostSet,
        public materialGoods?: IMaterialGoods
    ) {}
}
