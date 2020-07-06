import { IBank } from 'app/shared/model//bank.model';
import { IOrganizationUnit } from 'app/shared/model//organization-unit.model';

export interface IBankAccountDetails {
    id?: string;
    bankAccount?: string;
    bankName?: string;
    address?: string;
    description?: string;
    bankBranchName?: string;
    isActive?: boolean;
    bankID?: IBank;
    branchID?: IOrganizationUnit;
}

export class BankAccountDetails implements IBankAccountDetails {
    constructor(
        public id?: string,
        public bankAccount?: string,
        public bankName?: string,
        public address?: string,
        public description?: string,
        public bankBranchName?: string,
        public isActive?: boolean,
        public bankID?: IBank,
        public branchID?: IOrganizationUnit
    ) {
        this.isActive = this.isActive || false;
        this.bankName = bankName ? bankName : '';
    }
}
