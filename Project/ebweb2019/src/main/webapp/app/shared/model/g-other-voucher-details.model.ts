import * as moment from 'moment';
import { Moment } from 'moment';

export interface IGOtherVoucherDetails {
    id?: string;
    description?: string;
    debitAccount?: string;
    creditAccount?: string;
    amountOriginal?: number;
    amount?: number;
    debitAccountingObjectID?: string;
    creditAccountingObjectID?: string;
    employeeID?: string;
    bankAccountDetailID?: string;
    expenseItemID?: string;
    costSetID?: string;
    contractID?: string;
    budgetItemID?: string;
    departmentID?: string;
    statisticCodeID?: string;
    cashOutAmountVoucherOriginal?: number;
    cashOutAmountVoucher?: number;
    cashOutExchangeRateFB?: number;
    cashOutAmountFB?: number;
    cashOutDifferAmountFB?: number;
    cashOutDifferAccountFB?: string;
    cashOutExchangeRateMB?: number;
    cashOutAmountMB?: number;
    cashOutDifferAmountMB?: number;
    cashOutDifferAccountMB?: string;
    isMatch?: boolean;
    matchDate?: Moment;
    orderPriority?: number;
    debitAccountItem?: any;
    creditAccountItem?: any;
}

export class GOtherVoucherDetails implements IGOtherVoucherDetails {
    constructor(
        public id?: string,
        public description?: string,
        public debitAccount?: string,
        public creditAccount?: string,
        public amountOriginal?: number,
        public amount?: number,
        public debitAccountingObjectID?: string,
        public creditAccountingObjectID?: string,
        public employeeID?: string,
        public bankAccountDetailID?: string,
        public expenseItemID?: string,
        public costSetID?: string,
        public contractID?: string,
        public budgetItemID?: string,
        public departmentID?: string,
        public statisticCodeID?: string,
        public cashOutAmountVoucherOriginal?: number,
        public cashOutAmountVoucher?: number,
        public cashOutExchangeRateFB?: number,
        public cashOutAmountFB?: number,
        public cashOutDifferAmountFB?: number,
        public cashOutDifferAccountFB?: string,
        public cashOutExchangeRateMB?: number,
        public cashOutAmountMB?: number,
        public cashOutDifferAmountMB?: number,
        public cashOutDifferAccountMB?: string,
        public isMatch?: boolean,
        public matchDate?: Moment,
        public orderPriority?: number,
        public debitAccountItem?: any,
        public creditAccountItem?: any
    ) {
        this.isMatch = this.isMatch || false;
    }
}
