import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';

export interface ITIDecrementDetails {
    id?: number;
    tiDecrementID?: string;
    toolsID?: string;
    description?: string;
    quantity?: number;
    decrementQuantity?: number;
    amount?: number;
    remainingDecrementAmount?: number;
    tiAuditID?: string;
    orderPriority?: number;
    toolsItem?: any;
    toolsName?: any;
    toolsCode?: any;
    organizationUnits?: any[];
}

export class TIDecrementDetails implements ITIDecrementDetails {
    constructor(
        public id?: number,
        public tiDecrementID?: string,
        public toolsID?: string,
        public description?: string,
        public quantity?: number,
        public decrementQuantity?: number,
        public amount?: number,
        public remainingDecrementAmount?: number,
        public tiAuditID?: string,
        public orderPriority?: number,
        public toolsItem?: any,
        public toolsName?: any,
        public toolsCode?: any,
        public organizationUnits?: any[]
    ) {}
}
