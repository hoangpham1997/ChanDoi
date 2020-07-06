import { IAccountList } from 'app/shared/model/account-list.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';

export interface ITreeOrganizationUnit {
    parent?: IOrganizationUnit;
    index?: number;
    select?: boolean;
    check?: boolean;
    children?: ITreeOrganizationUnit[];
}

export class TreeOrganizationUnit implements ITreeOrganizationUnit {
    constructor(
        public parent?: IOrganizationUnit,
        public index?: number,
        public select?: boolean,
        public check?: boolean,
        public children?: ITreeOrganizationUnit[]
    ) {
        this.select = this.select || false;
        this.check = this.check || false;
    }
}
