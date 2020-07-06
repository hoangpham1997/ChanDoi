import { IFixedAsset } from 'app/shared/model//fixed-asset.model';
import { IUnit } from 'app/shared/model//unit.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';

export interface IFixedAssetAccessories {
    id?: string;
    fixedAssetAccessoriesName?: string;
    fixedAssetAccessoriesQuantity?: number;
    fixedAssetAccessoriesAmount?: number;
    orderPriority?: number;
    fixedassetID?: string;
    fixedAssetAccessoriesUnitID?: IUnit;
}

export class FixedAssetAccessories implements IFixedAssetAccessories {
    constructor(
        public id?: string,
        public fixedAssetAccessoriesName?: string,
        public fixedAssetAccessoriesQuantity?: number,
        public fixedAssetAccessoriesAmount?: number,
        public orderPriority?: number,
        public fixedassetID?: string,
        public fixedAssetAccessoriesUnitID?: IUnit
    ) {}
}
