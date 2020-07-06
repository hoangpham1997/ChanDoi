import { Moment } from 'moment';
import * as moment from 'moment';
import { IOrganizationUnitOptionReport } from 'app/shared/model/organization-unit-option-report.model';

export interface IOrganizationUnit {
    id?: string;
    branchID?: string;
    companyID?: string;
    accountingType?: number;
    organizationUnitCode?: string;
    organizationUnitName?: string;
    organizationUnitEName?: string;
    unitType?: number;
    getUnitName?: string;
    address?: string;
    taxCode?: string;
    phoneNumber?: string;
    businessRegistrationNumber?: string;
    issueDate?: Moment;
    issueBy?: string;
    accType?: number;
    isPrivateVAT?: boolean;
    financialYear?: number;
    fromDate?: Moment;
    toDate?: Moment;
    startDate?: Moment;
    currencyID?: string;
    taxCalculationMethod?: number;
    goodsServicePurchaseID?: string;
    careerGroupID?: string;
    costAccount?: string;
    orderFixCode?: string;
    parentID?: string;
    getParentName?: string;
    isParentNode?: boolean;
    grade?: number;
    isActive?: boolean;
    organizationUnitOptionReport?: IOrganizationUnitOptionReport;
    userID?: number;
    packageID?: string;
    status?: number;
    isHaveOrg?: boolean;
    quantityRest?: number;
    quantity?: number;
    checked?: boolean;
}

export class OrganizationUnit implements IOrganizationUnit {
    constructor(
        public id?: string,
        public branchID?: string,
        public companyID?: string,
        public accountingType?: number,
        public organizationUnitCode?: string,
        public organizationUnitName?: string,
        public organizationUnitEName?: string,
        public unitType?: number,
        public getUnitName?: string,
        public address?: string,
        public taxCode?: string,
        public phoneNumber?: string,
        public businessRegistrationNumber?: string,
        public issueDate?: Moment,
        public issueBy?: string,
        public accType?: number,
        public isPrivateVAT?: boolean,
        public financialYear?: number,
        public fromDate?: Moment,
        public toDate?: Moment,
        public startDate?: Moment,
        public currencyID?: string,
        public taxCalculationMethod?: number,
        public goodsServicePurchaseID?: string,
        public careerGroupID?: string,
        public costAccount?: string,
        public orderFixCode?: string,
        public parentID?: string,
        public getParentName?: string,
        public isParentNode?: boolean,
        public grade?: number,
        public isActive?: boolean,
        public organizationUnitOptionReport?: IOrganizationUnitOptionReport,
        public userID?: number,
        public packageID?: string,
        public status?: number,
        public quantityRest?: number,
        public quantity?: number,
        public isHaveOrg?: boolean,
        public checked?: boolean
    ) {
        this.isPrivateVAT = this.isPrivateVAT || false;
        this.isParentNode = this.isParentNode || false;
        this.isActive = this.isActive || false;
        isHaveOrg = this.isHaveOrg || false;
        checked = this.checked || false;
    }
}
