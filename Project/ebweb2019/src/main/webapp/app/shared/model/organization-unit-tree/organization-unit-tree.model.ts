import { IEbGroup } from 'app/core/eb-group/eb-group.model';

export class OrganizationUnitTree {
    value: string;
    text: string;
    workingOnBook: number;
    unitType: number;
    check: boolean;
    collapse: boolean;
    children: OrganizationUnitTree[];
    data: string;
    groups: IEbGroup[];
}
