export interface ITIAuditMemberDetails {
    id?: number;
    tiAuditID?: string;
    accountObjectName?: string;
    accountingObjectTitle?: string;
    role?: string;
    departmentID?: string;
    accountingObjectID?: string;
    orderPriority?: number;
    accountingObject?: any;
}

export class TIAuditMemberDetails implements ITIAuditMemberDetails {
    constructor(
        public id?: number,
        public tiAuditID?: string,
        public accountObjectName?: string,
        public accountingObjectTitle?: string,
        public role?: string,
        public departmentID?: string,
        public accountingObjectID?: string,
        public orderPriority?: number,
        public accountingObject?: any
    ) {}
}
