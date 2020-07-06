import { ITools } from 'app/shared/model/tools.model';
import { IFixedAsset } from 'app/shared/model/fixed-asset.model';

export interface IFAIncrementDetails {
    id?: number;
    faIncrementID?: string;
    fixedAssetID?: string;
    fixedAssetItem?: any;
    description?: string;
    unitID?: string;
    quantity?: number;
    unitPrice?: number;
    amount?: number;
    accountingObjectID?: string;
    budgetItemID?: string;
    costSetID?: string;
    contractID?: string;
    statisticCodeID?: string;
    departmentID?: string;
    expenseItemID?: string;
    orderPriority?: number;
    fixedAssetName?: string;
    fixedAssetCode?: string;
}

export class FAIncrementDetails implements IFAIncrementDetails {
    constructor(
        public id?: number,
        public faIncrementID?: string,
        public fixedAssetID?: string,
        public fixedAssetItem?: IFixedAsset,
        public description?: string,
        public unitID?: string,
        public quantity?: number,
        public unitPrice?: number,
        public amount?: number,
        public accountingObjectID?: string,
        public budgetItemID?: string,
        public costSetID?: string,
        public contractID?: string,
        public statisticCodeID?: string,
        public departmentID?: string,
        public expenseItemID?: string,
        public fixedAssetName?: string,
        public fixedAssetCode?: string,
        public orderPriority?: number
    ) {}
}
