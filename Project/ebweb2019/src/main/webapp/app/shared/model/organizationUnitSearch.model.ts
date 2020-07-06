import { IAccountList } from 'app/shared/model/account-list.model';

export interface IOrganizationUnitSearch {
    organizationUnitName?: string;
    organizationUnitCode?: string;
    taxCode?: string;
}

export class OrganizationUnitSearch implements IOrganizationUnitSearch {
    constructor(public organizationUnitName?: string, public organizationUnitCode?: string, public taxCode?: string) {}
}
