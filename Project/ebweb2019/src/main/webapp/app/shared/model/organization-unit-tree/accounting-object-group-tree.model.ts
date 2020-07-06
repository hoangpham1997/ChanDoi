import { IAccountList } from 'app/shared/model/account-list.model';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { IAccountingObjectGroup } from 'app/shared/model/accounting-object-group.model';

export interface ITreeAccountingObjectGroup {
    parent?: IAccountingObjectGroup;
    index?: number;
    select?: boolean;
    check?: boolean;
    children?: ITreeAccountingObjectGroup[];
}

export class TreeAccountingObjectGroup implements ITreeAccountingObjectGroup {
    constructor(
        public parent?: IAccountingObjectGroup,
        public index?: number,
        public select?: boolean,
        public check?: boolean,
        public children?: ITreeAccountingObjectGroup[]
    ) {
        this.select = this.select || false;
        this.check = this.check || false;
    }
}
