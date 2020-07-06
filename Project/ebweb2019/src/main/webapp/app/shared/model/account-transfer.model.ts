export interface IAccountTransfer {
    id?: string;
    companyID?: string;
    accountingType?: number;
    accountTransferCode?: string;
    accountTransferOrder?: number;
    description?: string;
    fromAccount?: string;
    toAccount?: string;
    fromAccountData?: number;
    debitAccount?: number;
    creditAccount?: number;
    isActive?: boolean;
    isSecurity?: boolean;
}

export class AccountTransfer implements IAccountTransfer {
    constructor(
        public id?: string,
        public companyID?: string,
        public accountingType?: number,
        public accountTransferCode?: string,
        public accountTransferOrder?: number,
        public description?: string,
        public fromAccount?: string,
        public toAccount?: string,
        public fromAccountData?: number,
        public debitAccount?: number,
        public creditAccount?: number,
        public isActive?: boolean,
        public isSecurity?: boolean
    ) {
        this.isActive = this.isActive || false;
        this.isSecurity = this.isSecurity || false;
    }
}
