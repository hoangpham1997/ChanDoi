import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';

export interface IMCAuditDetailMember {
    id?: string;
    mCAuditId?: string;
    accountingObject?: IAccountingObject;
    accountingObjectName?: string;
    accountingObjectTitle?: string;
    role?: string;
    organizationUnit?: IOrganizationUnit;
    orderPriority?: number;
}

export class MCAuditDetailMember implements IMCAuditDetailMember {
    constructor(
        public id?: string,
        public mCAuditId?: string,
        public accountingObject?: IAccountingObject,
        public accountingObjectName?: string,
        public accountingObjectTitle?: string,
        public role?: string,
        public organizationUnit?: IOrganizationUnit,
        public orderPriority?: number
    ) {}
}
