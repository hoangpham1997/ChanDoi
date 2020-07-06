import { IAccountList } from 'app/shared/model/account-list.model';

export class TreeAccountListItem {
    parent: any;
    index?: number;
    select?: boolean;
    check?: boolean;
    children?: any[];
}
