import { IUnit } from 'app/shared/model//unit.model';
import { IMaterialGoodsResourceTaxGroup } from 'app/shared/model//material-goods-resource-tax-group.model';

export interface IMaterialGoodsResourceTaxGroup {
    id?: number;
    materialGoodsResourceTaxGroupCode?: string;
    materialGoodsResourceTaxGroupName?: string;
    unitID?: string;
    taxRate?: number;
    orderFixCode?: string;
    parentID?: string;
    isParentNode?: boolean;
    grade?: number;
    isActive?: boolean;
    isSecurity?: boolean;
    son?: IUnit;
    parent?: IMaterialGoodsResourceTaxGroup;
}

export class MaterialGoodsResourceTaxGroup implements IMaterialGoodsResourceTaxGroup {
    constructor(
        public id?: number,
        public materialGoodsResourceTaxGroupCode?: string,
        public materialGoodsResourceTaxGroupName?: string,
        public unitID?: string,
        public taxRate?: number,
        public orderFixCode?: string,
        public parentID?: string,
        public isParentNode?: boolean,
        public grade?: number,
        public isActive?: boolean,
        public isSecurity?: boolean,
        public son?: IUnit,
        public parent?: IMaterialGoodsResourceTaxGroup
    ) {
        this.isParentNode = this.isParentNode || false;
        this.isActive = this.isActive || false;
        this.isSecurity = this.isSecurity || false;
    }
}
