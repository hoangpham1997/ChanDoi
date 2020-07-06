export interface IExpenseItem {
    id?: string;
    expenseItemCode?: string;
    expenseItemName?: string;
    expenseType?: number;
    expenseTypeName?: string;
    description?: string;
    parentID?: string;
    isParentNode?: boolean;
    orderFixCode?: string;
    grade?: number;
    isActive?: boolean;
    isSecurity?: boolean;
    children?: IExpenseItem[];
    parent?: ExpenseItem;
    checked?: boolean;
}

export class ExpenseItem implements IExpenseItem {
    constructor(
        public id?: string,
        public expenseItemCode?: string,
        public expenseItemName?: string,
        public expenseType?: number,
        public description?: string,
        public parentID?: string,
        public isParentNode?: boolean,
        public orderFixCode?: string,
        public grade?: number,
        public isActive?: boolean,
        public isSecurity?: boolean,
        public children?: IExpenseItem[],
        public parent?: ExpenseItem,
        public checked?: boolean
    ) {
        this.isParentNode = this.isParentNode || false;
        this.isActive = this.isActive || false;
        this.isSecurity = this.isSecurity || false;
        this.checked = this.checked || false;
    }
}
