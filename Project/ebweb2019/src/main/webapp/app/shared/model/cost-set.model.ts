import { ICostSetMaterialGood } from 'app/shared/model/cost-set-material-good.model';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';

export interface ICostSet {
    id?: string;
    branchID?: string;
    costSetCode?: string;
    costSetName?: string;
    costSetType?: number;
    deScription?: string;
    parentID?: string;
    isParentNode?: boolean;
    orderFixCode?: string;
    grade?: number;
    isActive?: boolean;
    checked?: boolean;
    costSetMaterialGoods?: ICostSetMaterialGood[];
    materialGoods?: IMaterialGoods;
    materialGoodName?: string;
    materialGoodCode?: string;
    totalAmountExp0?: number;
    totalAmountExp1?: number;
    materialGoodsID?: string;
}

export class CostSet implements ICostSet {
    constructor(
        public id?: string,
        public branchID?: string,
        public costSetCode?: string,
        public costSetName?: string,
        public costSetType?: number,
        public deScription?: string,
        public parentID?: string,
        public isParentNode?: boolean,
        public orderFixCode?: string,
        public grade?: number,
        public isActive?: boolean,
        public checked?: boolean,
        public costSetMaterialGoods?: ICostSetMaterialGood[],
        public totalAmountExp0?: number,
        public totalAmountExp1?: number
    ) {
        this.isParentNode = this.isParentNode || false;
        this.isActive = this.isActive || false;
    }
}
