export interface IPaymentClause {
    id?: string;
    companyID?: string;
    branchID?: string;
    paymentClauseCode?: string;
    paymentClauseName?: string;
    dueDate?: number;
    discountDate?: number;
    discountPercent?: number;
    overDueInterestPercent?: number;
    isActive?: boolean;
    isSecurity?: boolean;
}

export class PaymentClause implements IPaymentClause {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public paymentClauseCode?: string,
        public paymentClauseName?: string,
        public dueDate?: number,
        public discountDate?: number,
        public discountPercent?: number,
        public overDueInterestPercent?: number,
        public isActive?: boolean,
        public isSecurity?: boolean
    ) {
        this.isActive = this.isActive || true;
        this.isSecurity = this.isSecurity || false;
    }
}
