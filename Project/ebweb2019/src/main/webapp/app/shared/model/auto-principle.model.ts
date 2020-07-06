export interface IAutoPrinciple {
    id?: string;
    companyID?: string;
    branchID?: string;
    accountingType?: number;
    autoPrincipleName?: string;
    typeId?: number;
    debitAccount?: string;
    creditAccount?: string;
    description?: string;
    isActive?: boolean;
}

export class AutoPrinciple implements IAutoPrinciple {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public accountingType?: number,
        public autoPrincipleName?: string,
        public typeId?: number,
        public debitAccount?: string,
        public creditAccount?: string,
        public description?: string,
        public isActive?: boolean
    ) {
        this.isActive = this.isActive || false;
    }
}
