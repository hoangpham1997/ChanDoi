import { IAccountList } from 'app/shared/model/account-list.model';

export interface IAccountAllList {
    deductionDebitAccount?: IAccountList[];
    debitAccount?: IAccountList[];
    vatAccount?: IAccountList[];
    discountAccount?: IAccountList[];
    creditAccount?: IAccountList[];
    importTaxAccount?: IAccountList[];
    exportTaxAccount?: IAccountList[];
    repositoryAccount?: IAccountList[];
    specialConsumeTaxAccount?: IAccountList[];
    costAccount?: IAccountList[];
}

export class AccountAllList implements IAccountAllList {
    constructor(
        public deductionDebitAccount?: IAccountList[],
        public debitAccount?: IAccountList[],
        public vatAccount?: IAccountList[],
        public discountAccount?: IAccountList[],
        public creditAccount?: IAccountList[],
        public importTaxAccount?: IAccountList[],
        public exportTaxAccount?: IAccountList[],
        public repositoryAccount?: IAccountList[],
        public specialConsumeTaxAccount?: IAccountList[],
        public costAccount?: IAccountList[]
    ) {}
}
