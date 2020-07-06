import { OrganizationUnitTree } from 'app/shared/model/organization-unit-tree/organization-unit-tree.model';
import { IUser } from 'app/core';

export class OrgTreeUserDTO {
    listOrg: OrganizationUnitTree[];
    user: IUser;
}
