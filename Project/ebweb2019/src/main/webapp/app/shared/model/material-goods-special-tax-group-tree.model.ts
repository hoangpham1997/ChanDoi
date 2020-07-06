import { IMaterialGoodsSpecialTaxGroup } from 'app/shared/model/material-goods-special-tax-group.model';

export interface ITreeMaterialGoodsSpecialTaxGroup {
    parent?: IMaterialGoodsSpecialTaxGroup;
    index?: number;
    select?: boolean;
    check?: boolean;
    children?: ITreeMaterialGoodsSpecialTaxGroup[];
}

export class TreeMaterialGoodsSpecialTaxGroup implements ITreeMaterialGoodsSpecialTaxGroup {
    constructor(
        public parent?: IMaterialGoodsSpecialTaxGroup,
        public index?: number,
        public select?: boolean,
        public check?: boolean,
        public children?: ITreeMaterialGoodsSpecialTaxGroup[]
    ) {
        this.select = this.select || false;
        this.check = this.check || false;
    }
}
