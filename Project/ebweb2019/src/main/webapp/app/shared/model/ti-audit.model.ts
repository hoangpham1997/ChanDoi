import { Moment } from 'moment';
import { ITIAuditDetails } from 'app/shared/model/ti-audit-details.model';

export interface ITIAudit {
    id?: string;
    companyID?: string;
    branchID?: string;
    typeID?: number;
    date?: any;
    inventoryDate?: Moment;
    typeLedger?: number;
    noFBook?: string;
    noMBook?: string;
    description?: string;
    summary?: string;
    templateID?: string;
    tiAuditMemberDetails?: any;
    tiAuditDetails?: ITIAuditDetails[];
    tiIncrementID?: string;
    tiDecrementID?: any;
    noBookDecrement?: string;
    noBookIncrement?: string;
    viewVouchers?: any[];
}

export class TIAudit implements ITIAudit {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public typeID?: number,
        public date?: any,
        public inventoryDate?: Moment,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public description?: string,
        public summary?: string,
        public templateID?: string,
        public tiAuditMemberDetails?: any,
        public tiAuditDetails?: ITIAuditDetails[],
        public tiIncrementID?: string,
        public tiDecrementID?: any,
        public noBookDecrement?: string,
        public noBookIncrement?: string,
        public viewVouchers?: any[]
    ) {}
}
