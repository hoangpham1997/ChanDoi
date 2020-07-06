import { IAccountList } from 'app/shared/model/account-list.model';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';

export interface ITreeMaterialGoodsCategory {
    parent?: IMaterialGoodsCategory;
    index?: number;
    select?: boolean;
    check?: boolean;
    children?: ITreeMaterialGoodsCategory[];
}

export class TreeMaterialGoodsCategory implements ITreeMaterialGoodsCategory {
    constructor(
        public parent?: IMaterialGoodsCategory,
        public index?: number,
        public select?: boolean,
        public check?: boolean,
        public children?: ITreeMaterialGoodsCategory[]
    ) {
        this.select = this.select || false;
        this.check = this.check || false;
    }
}
