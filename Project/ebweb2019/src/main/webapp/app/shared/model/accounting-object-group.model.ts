export interface IAccountingObjectGroup {
    id?: string;
    accountingObjectGroupCode?: string;
    accountingObjectGroupName?: string;
    parentId?: string;
    isParentNode?: boolean;
    orderFixCode?: string;
    grade?: number;
    description?: string;
    isActive?: boolean;
    isSecurity?: boolean;
}

export class AccountingObjectGroup implements IAccountingObjectGroup {
    constructor(
        public id?: string,
        public accountingObjectGroupCode?: string,
        public accountingObjectGroupName?: string,
        public parentId?: string,
        public isParentNode?: boolean,
        public orderFixCode?: string,
        public grade?: number,
        public description?: string,
        public isActive?: boolean,
        public isSecurity?: boolean
    ) {
        this.isParentNode = this.isParentNode || false;
        this.isActive = this.isActive || false;
        this.isSecurity = this.isSecurity || false;
    }
}
