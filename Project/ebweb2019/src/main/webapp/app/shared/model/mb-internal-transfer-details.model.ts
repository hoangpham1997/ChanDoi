import { Moment } from 'moment';
import { IMBInternalTransfer } from 'app/shared/model//mb-internal-transfer.model';

export interface IMBInternalTransferDetails {
    id?: string;
    debitAccount?: string;
    creditAccount?: string;
    fromBankAccountDetailID?: string;
    toBankAccountDetailID?: string;
    fromBranchID?: string;
    toBranchID?: string;
    currencyID?: string;
    exchangeRate?: number;
    amount?: number;
    amountOriginal?: number;
    employeeID?: string;
    budgetItemID?: string;
    costSetID?: string;
    contractID?: string;
    statisticsCodeID?: string;
    organizationUnitID?: string;
    expenseItemID?: string;
    accountingObjectID?: string;
    cashOutExchangeRateFB?: number;
    cashOutAmountFB?: number;
    cashOutDifferAmountFB?: number;
    cashOutDifferAccountFB?: number;
    cashOutExchangeRateMB?: number;
    cashOutAmountMB?: number;
    cashOutDifferAmountMB?: number;
    cashOutDifferAccountMB?: string;
    isMatch?: boolean;
    matchDate?: Moment;
    orderPriority?: number;
    mBInternalTransfer?: IMBInternalTransfer;
}

export class MBInternalTransferDetails implements IMBInternalTransferDetails {
    constructor(
        public id?: string,
        public debitAccount?: string,
        public creditAccount?: string,
        public fromBankAccountDetailID?: string,
        public toBankAccountDetailID?: string,
        public fromBranchID?: string,
        public toBranchID?: string,
        public currencyID?: string,
        public exchangeRate?: number,
        public amount?: number,
        public amountOriginal?: number,
        public employeeID?: string,
        public budgetItemID?: string,
        public costSetID?: string,
        public contractID?: string,
        public statisticsCodeID?: string,
        public organizationUnitID?: string,
        public expenseItemID?: string,
        public accountingObjectID?: string,
        public cashOutExchangeRateFB?: number,
        public cashOutAmountFB?: number,
        public cashOutDifferAmountFB?: number,
        public cashOutDifferAccountFB?: number,
        public cashOutExchangeRateMB?: number,
        public cashOutAmountMB?: number,
        public cashOutDifferAmountMB?: number,
        public cashOutDifferAccountMB?: string,
        public isMatch?: boolean,
        public matchDate?: Moment,
        public orderPriority?: number,
        public mBInternalTransfer?: IMBInternalTransfer
    ) {
        this.isMatch = this.isMatch || false;
    }
}
