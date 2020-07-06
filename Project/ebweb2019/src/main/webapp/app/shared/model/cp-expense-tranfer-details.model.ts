export interface ICPExpenseTranferDetails {
    id?: string;
    cPExpenseTranferID?: string;
    description?: string;
    debitAccount?: string;
    creditAccount?: string;
    amount?: number;
    amountOriginal?: number;
    costSetID?: string;
    costSetName?: string;
    statisticsCodeID?: string;
    expenseItemID?: string;
    orderPriority?: number;
}

export class CPExpenseTranferDetails implements ICPExpenseTranferDetails {
    constructor(
        public id?: string,
        public cPExpenseTranferID?: string,
        public description?: string,
        public debitAccount?: string,
        public creditAccount?: string,
        public amount?: number,
        public amountOriginal?: number,
        public costSetID?: string,
        public costSetName?: string,
        public statisticsCodeID?: string,
        public expenseItemID?: string,
        public orderPriority?: number
    ) {}
}
