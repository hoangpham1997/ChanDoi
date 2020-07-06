import { Moment } from 'moment';
import { IFixedAssetCategory } from 'app/shared/model//fixed-asset-category.model';
import { IOrganizationUnit } from 'app/shared/model//organization-unit.model';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';
import { IAccountList } from 'app/shared/model//account-list.model';
import { IWarranty } from 'app/shared/model/warranty.model';
import { IFixedAssetAllocation } from 'app/shared/model/fixed-asset-allocation.model';
import { IFixedAssetAccessories } from 'app/shared/model/fixed-asset-accessories.model';
import { IFixedAssetDetails } from 'app/shared/model/fixed-asset-details.model';

export interface IFixedAsset {
    id?: string;
    fixedAssetCode?: string;
    fixedAssetName?: string;
    description?: string;
    productionYear?: number;
    madeIn?: string;
    serialNumber?: string;
    accountingObjectName?: string;
    warranty?: string;
    guaranteeCodition?: string;
    isSecondHand?: boolean;
    currentState?: number;
    deliveryRecordNo?: string;
    deliveryRecordDate?: Moment;
    purchasedDate?: Moment;
    incrementDate?: Moment;
    depreciationDate?: Moment;
    usedDate?: Moment;
    purchasePrice?: number;
    originalPrice?: number;
    depreciationMethod?: number;
    usedTime?: number;
    displayMonthYear?: boolean;
    periodDepreciationAmount?: number;
    depreciationRate?: number;
    monthDepreciationRate?: number;
    monthPeriodDepreciationAmount?: number;
    isActive?: boolean;
    fixedAssetAllocation?: IFixedAssetAllocation[];
    fixedAssetAccessories?: IFixedAssetAccessories[];
    fixedAssetDetails?: IFixedAssetDetails[];
    fixedAssetCategoryID?: IFixedAssetCategory;
    branchID?: IOrganizationUnit;
    organizationUnitID?: IOrganizationUnit;
    accountingObjectID?: IAccountingObject;
    depreciationAccount?: string;
    originalPriceAccount?: string;
    expenditureAccount?: string;
}

export class FixedAsset implements IFixedAsset {
    constructor(
        public id?: string,
        public fixedAssetCode?: string,
        public fixedAssetName?: string,
        public description?: string,
        public productionYear?: number,
        public madeIn?: string,
        public serialNumber?: string,
        public accountingObjectName?: string,
        public warranty?: string,
        public guaranteeCodition?: string,
        public isSecondHand?: boolean,
        public currentState?: number,
        public deliveryRecordNo?: string,
        public deliveryRecordDate?: Moment,
        public purchasedDate?: Moment,
        public incrementDate?: Moment,
        public depreciationDate?: Moment,
        public usedDate?: Moment,
        public purchasePrice?: number,
        public originalPrice?: number,
        public depreciationMethod?: number,
        public usedTime?: number,
        public displayMonthYear?: boolean,
        public periodDepreciationAmount?: number,
        public depreciationRate?: number,
        public monthDepreciationRate?: number,
        public monthPeriodDepreciationAmount?: number,
        public isActive?: boolean,
        public fixedAssetAllocation?: IFixedAssetAllocation[],
        public fixedAssetAccessories?: IFixedAssetAccessories[],
        public fixedAssetDetails?: IFixedAssetDetails[],
        public fixedAssetCategoryID?: IFixedAssetCategory,
        public branchID?: IOrganizationUnit,
        public organizationUnitID?: IOrganizationUnit,
        public accountingObjectID?: IAccountingObject,
        public depreciationAccount?: string,
        public originalPriceAccount?: string,
        public expenditureAccount?: string
    ) {
        this.isSecondHand = this.isSecondHand || false;
        this.displayMonthYear = this.displayMonthYear || false;
        this.isActive = this.isActive || false;
    }
}
