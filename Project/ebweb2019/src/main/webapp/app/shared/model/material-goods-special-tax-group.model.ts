import { IMaterialGoodsSpecialTaxGroup } from 'app/shared/model//material-goods-special-tax-group.model';

export interface IMaterialGoodsSpecialTaxGroup {
    id?: string;
    materialGoodsSpecialTaxGroupCode?: string;
    materialGoodsSpecialTaxGroupName?: string;
    taxRate?: number;
    unitID?: string;
    orderFixCode?: string;
    parentID?: string;
    isParentNode?: boolean;
    grade?: number;
    isActive?: boolean;
    isSecurity?: boolean;
    unitName?: string;
    parent?: IMaterialGoodsSpecialTaxGroup;
}

export class MaterialGoodsSpecialTaxGroup implements IMaterialGoodsSpecialTaxGroup {
    constructor(
        public id?: string,
        public materialGoodsSpecialTaxGroupCode?: string,
        public materialGoodsSpecialTaxGroupName?: string,
        public taxRate?: number,
        public unitID?: string,
        public orderFixCode?: string,
        public parentID?: string,
        public isParentNode?: boolean,
        public grade?: number,
        public isActive?: boolean,
        public isSecurity?: boolean,
        public unitName?: string,
        public parent?: IMaterialGoodsSpecialTaxGroup
    ) {
        this.isParentNode = this.isParentNode || false;
        this.isActive = this.isActive || false;
        this.isSecurity = this.isSecurity || false;
    }
}
