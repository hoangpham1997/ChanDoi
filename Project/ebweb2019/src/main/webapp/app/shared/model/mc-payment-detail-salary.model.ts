import { IOrganizationUnit } from 'app/shared/model//organization-unit.model';

export interface IMCPaymentDetailSalary {
    id?: string;
    mCPaymentID?: string;
    employeeID?: string;
    description?: string;
    accumAmount?: number;
    accumAmountOriginal?: number;
    currentMonthAmount?: number;
    currentMonthAmountOriginal?: number;
    payAmount?: number;
    payAmountOriginal?: number;
    orderPriority?: number;
    organizationUnit?: IOrganizationUnit;
}

export class MCPaymentDetailSalary implements IMCPaymentDetailSalary {
    constructor(
        public id?: string,
        public mCPaymentID?: string,
        public employeeID?: string,
        public description?: string,
        public accumAmount?: number,
        public accumAmountOriginal?: number,
        public currentMonthAmount?: number,
        public currentMonthAmountOriginal?: number,
        public payAmount?: number,
        public payAmountOriginal?: number,
        public orderPriority?: number,
        public organizationUnit?: IOrganizationUnit
    ) {}
}
