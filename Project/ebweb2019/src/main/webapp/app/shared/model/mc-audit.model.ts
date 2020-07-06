import { Moment } from 'moment';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IMCAuditDetails } from 'app/shared/model/mc-audit-details.model';
import { IMCAuditDetailMember } from 'app/shared/model/mc-audit-detail-member.model';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';

export interface IMCAudit {
    id?: string;
    no?: string;
    typeID?: number;
    typeLedger?: number;
    date?: Moment;
    description?: string;
    auditDate?: Moment;
    summary?: string;
    currencyID?: string;
    totalAuditAmount?: number;
    totalBalanceAmount?: number;
    exchangeRate?: number;
    differAmount?: number;
    templateID?: string;
    branchID?: IOrganizationUnit;
    companyID?: IOrganizationUnit;
    mcAuditDetails?: IMCAuditDetails[];
    mcAuditDetailMembers?: IMCAuditDetailMember[];
    viewVouchers?: IViewVoucher[];
}

export class MCAudit implements IMCAudit {
    constructor(
        public id?: string,
        public no?: string,
        public typeID?: number,
        public typeLedger?: number,
        public date?: Moment,
        public description?: string,
        public auditDate?: Moment,
        public summary?: string,
        public currencyID?: string,
        public totalAuditAmount?: number,
        public exchangeRate?: number,
        public totalBalanceAmount?: number,
        public differAmount?: number,
        public templateID?: string,
        public branchID?: IOrganizationUnit,
        public companyID?: IOrganizationUnit,
        public mcAuditDetails?: IMCAuditDetails[],
        public mcAuditDetailMembers?: IMCAuditDetailMember[],
        public viewVouchers?: IViewVoucher[]
    ) {}
}
