import { IFixedAsset } from 'app/shared/model//fixed-asset.model';

export interface IFixedAssetDetails {
    id?: string;
    description?: string;
    quantity?: number;
    warranty?: string;
    orderPriority?: number;
    fixedassetID?: string;
}

export class FixedAssetDetails implements IFixedAssetDetails {
    constructor(
        public id?: string,
        public description?: string,
        public quantity?: number,
        public warranty?: string,
        public orderPriority?: number,
        public fixedassetID?: string
    ) {}
}
