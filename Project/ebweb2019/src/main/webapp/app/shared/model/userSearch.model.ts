import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';

export interface IUserSearch {
    login?: string;
    fullName?: string;
    mobilePhone?: string;
    organizationUnit?: IOrganizationUnit;
    status?: any;
}

export class UserSearch implements IUserSearch {
    constructor(
        public login?: string,
        public fullName?: string,
        public mobilePhone?: string,
        public organizationUnitName?: IOrganizationUnit,
        public status?: any
    ) {}
}
