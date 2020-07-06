import { IBudgetItem } from 'app/shared/model//budget-item.model';

export interface IBudgetItem {
    id?: string;
    budgetItemCode?: string;
    budgetItemName?: string;
    budgetItemType?: number;
    budgetItemTypeName?: string;
    description?: string;
    parentID?: string;
    isParentNode?: boolean;
    orderFixCode?: string;
    grade?: number;
    isActive?: boolean;
    parent?: IBudgetItem;
}

export class BudgetItem implements IBudgetItem {
    constructor(
        public id?: string,
        public budgetItemCode?: string,
        public budgetItemName?: string,
        public budgetItemType?: number,
        public budgetItemTypeName?: string,
        public description?: string,
        public parentID?: string,
        public isParentNode?: boolean,
        public orderFixCode?: string,
        public grade?: number,
        public isActive?: boolean,
        public parent?: IBudgetItem
    ) {
        this.isParentNode = this.isParentNode || false;
        this.isActive = this.isActive || false;
    }
}
