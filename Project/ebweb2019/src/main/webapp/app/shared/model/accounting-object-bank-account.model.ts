import { IAccountingObject } from 'app/shared/model//accounting-object.model';

export interface IAccountingObjectBankAccount {
    id?: string;
    bankAccount?: string;
    bankName?: string;
    bankBranchName?: string;
    accountHolderName?: string;
    orderPriority?: number;
    accountingObjectId?: string;
}

export class AccountingObjectBankAccount implements IAccountingObjectBankAccount {
    constructor(
        public id?: string,
        public bankAccount?: string,
        public bankName?: string,
        public bankBranchName?: string,
        public accountHolderName?: string,
        public orderPriority?: number,
        public accountingObjectId?: string
    ) {}
}
