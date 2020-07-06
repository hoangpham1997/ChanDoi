export interface IAccountDefaultCategory {
    typeID?: number;
    typeName?: string;
    debitAccount?: string;
    defaultDebitAccount?: string;
    creditAccount?: string;
    defaultCreditAccount?: string;
}

export class AccountDefaultCategory implements IAccountDefaultCategory {
    constructor(
        public typeID?: number,
        public typeName?: string,
        public debitAccount?: string,
        public defaultDebitAccount?: string,
        public creditAccount?: string,
        public defaultCreditAccount?: string
    ) {}
}
