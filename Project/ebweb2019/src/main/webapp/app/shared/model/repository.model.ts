import { IOrganizationUnit } from 'app/shared/model//organization-unit.model';

export interface IRepository {
    checked?: boolean;
    id?: string;
    companyID?: string;
    repositoryCode?: string;
    repositoryName?: string;
    description?: string;
    defaultAccount?: string;
    isActive?: boolean;
    branchID?: IOrganizationUnit;
}

export class Repository implements IRepository {
    constructor(
        public checked?: boolean,
        public id?: string,
        public companyID?: string,
        public repositoryCode?: string,
        public repositoryName?: string,
        public description?: string,
        public defaultAccount?: string,
        public isActive?: boolean,
        public branchID?: IOrganizationUnit
    ) {
        this.isActive = this.isActive || false;
        this.checked = this.checked || false;
    }
}
