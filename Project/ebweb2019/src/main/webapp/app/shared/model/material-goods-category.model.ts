export interface IMaterialGoodsCategory {
    id?: string;
    companyID?: string;
    branchID?: string;
    materialGoodsCategoryCode?: string;
    materialGoodsCategoryName?: string;
    parentID?: string;
    isParentNode?: boolean;
    orderFixCode?: string;
    grade?: number;
    isActive?: boolean;
    isSecurity?: boolean;
}

export class MaterialGoodsCategory implements IMaterialGoodsCategory {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public materialGoodsCategoryCode?: string,
        public materialGoodsCategoryName?: string,
        public parentID?: string,
        public isParentNode?: boolean,
        public orderFixCode?: string,
        public grade?: number,
        public isActive?: boolean,
        public isSecurity?: boolean
    ) {
        this.isParentNode = this.isParentNode || false;
        this.isActive = this.isActive || false;
        this.isSecurity = this.isSecurity || false;
    }
}
