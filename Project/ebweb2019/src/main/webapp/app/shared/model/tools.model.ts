import { Moment } from 'moment';
import { IOrganizationUnit } from 'app/shared/model//organization-unit.model';
import { IUnit } from 'app/shared/model//unit.model';

export interface ITools {
    id?: string;
    declareType?: number;
    postedDate?: Moment;
    branchID?: string;
    toolCode?: string;
    toolName?: string;
    quantity?: number;
    unitPrice?: number;
    amount?: number;
    allocationTimes?: number;
    remainAllocationTimes?: number;
    allocatedAmount?: number;
    allocationAmount?: number;
    remainAmount?: number;
    allocationAwaitAccount?: string;
    isActive?: boolean;
    organizationUnit?: IOrganizationUnit;
    unit?: IUnit;
    checked?: boolean;
    unitID?: any;
}

export class Tools implements ITools {
    constructor(
        public id?: string,
        public declareType?: number,
        public postedDate?: Moment,
        public branchID?: string,
        public toolCode?: string,
        public toolName?: string,
        public quantity?: number,
        public unitPrice?: number,
        public amount?: number,
        public allocationTimes?: number,
        public remainAllocationTimes?: number,
        public allocatedAmount?: number,
        public allocationAmount?: number,
        public remainAmount?: number,
        public allocationAwaitAccount?: string,
        public isActive?: boolean,
        public organizationUnit?: IOrganizationUnit,
        public unit?: IUnit,
        public checked?: boolean,
        public unitID?: any
    ) {
        this.isActive = this.isActive || false;
        this.checked = this.checked || false;
    }
}
