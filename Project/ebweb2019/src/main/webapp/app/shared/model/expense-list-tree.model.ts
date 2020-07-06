import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { OpMaterialGoodsModel } from 'app/shared/model/op-material-goods.model';
export interface ITreeExpenseList {
    length: number;
    parent?: IExpenseItem;
    index?: number;
    select?: boolean;
    check?: boolean;
    children?: ITreeExpenseList[];
}
export class TreeExpenseList implements ITreeExpenseList {
    constructor(
        public length: number,
        public parent?: IExpenseItem,
        public index?: number,
        public select?: boolean,
        public check?: boolean,
        public children?: ITreeExpenseList[]
    ) {
        this.select = this.select || false;
        this.check = this.check || false;
    }
}
