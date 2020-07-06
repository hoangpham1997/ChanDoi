import { IBudgetItem } from './budget-item.model';

export interface ITreeBudgetItem {
    parent?: IBudgetItem;
    index?: number;
    select?: boolean;
    check?: boolean;
    children?: ITreeBudgetItem[];
}

export class TreeBudgetItem implements ITreeBudgetItem {
    constructor(
        public parent?: IBudgetItem,
        public index?: number,
        public check?: boolean,
        public select?: boolean,
        public children?: ITreeBudgetItem[]
    ) {
        this.select = this.select || false;
        this.check = this.check || false;
    }
}
