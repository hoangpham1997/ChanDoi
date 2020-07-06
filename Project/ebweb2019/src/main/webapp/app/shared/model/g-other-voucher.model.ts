import { Moment } from 'moment';
import { IMBDepositDetails } from 'app/shared/model/mb-deposit-details.model';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';
import { IGOtherVoucherDetails } from 'app/shared/model/g-other-voucher-details.model';
import { IGOtherVoucherDetailTax } from 'app/shared/model/g-other-voucher-detail-tax.model';
import { IRefVoucher } from 'app/shared/model/ref-voucher.model';
import { IGOtherVoucherDetailExpenseAllocation } from 'app/shared/model/g-other-voucher-detail-expense-allocation.model';
import { IGOtherVoucherDetailExpense } from 'app/shared/model/g-other-voucher-detail-expense.model';

export interface IGOtherVoucher {
    id?: string;
    companyID?: string;
    branchID?: string;
    typeID?: number;
    currencyID?: string;
    exchangeRate?: number;
    typeLedger?: number;
    reason?: string;
    noMBook?: string;
    noFBook?: string;
    date?: Moment;
    postedDate?: Moment;
    totalAmount?: number;
    totalAmountOriginal?: number;
    recorded?: boolean;
    gOtherVoucherDetails?: IGOtherVoucherDetails[];
    gOtherVoucherDetailTax?: IGOtherVoucherDetailTax[];
    gOtherVoucherDetailExpenses?: IGOtherVoucherDetailExpense[];
    gOtherVoucherDetailExpenseAllocations?: IGOtherVoucherDetailExpenseAllocation[];
    viewVouchers?: IViewVoucher[];
    templateID?: string;
    no?: string;
    typeGroup?: number;
    refVouchers?: IRefVoucher[];
    currentBook?: string;
    noBook?: any;
    total?: number;
    sumAmount?: any;
    sumTotalAmount?: number;
}

export class GOtherVoucher implements IGOtherVoucher {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public typeID?: number,
        public currencyID?: string,
        public exchangeRate?: number,
        public typeLedger?: number,
        public reason?: string,
        public noMBook?: string,
        public noFBook?: string,
        public date?: Moment,
        public postedDate?: Moment,
        public totalAmount?: number,
        public totalAmountOriginal?: number,
        public recorded?: boolean,
        public gOtherVoucherDetails?: IGOtherVoucherDetails[],
        public gOtherVoucherDetailTax?: IGOtherVoucherDetailTax[],
        public gOtherVoucherDetailExpenses?: IGOtherVoucherDetailExpense[],
        public gOtherVoucherDetailExpenseAllocations?: IGOtherVoucherDetailExpenseAllocation[],
        public viewVouchers?: IViewVoucher[],
        public templateID?: string,
        public no?: string,
        public typeGroup?: number,
        public refVouchers?: IRefVoucher[],
        public noBook?: any,
        public total?: number,
        public sumAmount?: any,
        public sumTotalAmount?: number
    ) {
        this.recorded = this.recorded || false;
    }
}
