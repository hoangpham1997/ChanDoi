import { Moment } from 'moment';
import { ICPExpenseTranferDetails } from 'app/shared/model/cp-expense-tranfer-details.model';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';

export interface ICPExpenseTranfer {
    id?: string;
    companyID?: string;
    branchID?: string;
    typeLedger?: number;
    typeID?: number;
    noFBook?: string;
    noMBook?: string;
    reason?: string;
    postedDate?: Moment;
    date?: Moment;
    cPPeriodID?: string;
    totalAmount?: number;
    totalAmountOriginal?: number;
    recorded?: boolean;
    templateID?: string;
    cPExpenseTranferDetails?: ICPExpenseTranferDetails[];
    viewVouchers?: IViewVoucher[];
    total?: number;
}

export class CPExpenseTranfer implements ICPExpenseTranfer {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public typeLedger?: number,
        public typeID?: number,
        public noFBook?: string,
        public noMBook?: string,
        public reason?: string,
        public postedDate?: Moment,
        public date?: Moment,
        public cPPeriodID?: string,
        public totalAmount?: number,
        public totalAmountOriginal?: number,
        public recorded?: boolean,
        public templateID?: string,
        public cPExpenseTranferDetails?: ICPExpenseTranferDetails[],
        public viewVouchers?: IViewVoucher[],
        public total?: number
    ) {
        this.recorded = this.recorded || false;
    }
}
