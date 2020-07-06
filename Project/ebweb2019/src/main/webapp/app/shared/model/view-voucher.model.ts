import { Moment } from 'moment';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';

export interface IViewVoucher {
    id?: number;
    refID?: string;
    typeID?: number;
    typeGroupID?: number;
    companyID?: string;
    branchID?: string;
    typeLedger?: number;
    noMBook?: string;
    noFBook?: string;
    date?: Moment;
    postedDate?: Moment;
    currencyID?: string;
    reason?: string;
    recorded?: boolean;
    totalAmount?: number;
    totalAmountOriginal?: number;
    refTable?: string;
    accountingObject?: IAccountingObject;
    employee?: IAccountingObject;
    checked?: boolean;
}

export class ViewVoucher implements IViewVoucher {
    constructor(
        public id?: number,
        public refID?: string,
        public typeID?: number,
        public typeGroupID?: number,
        public companyID?: string,
        public branchID?: string,
        public typeLedger?: number,
        public noMBook?: string,
        public noFBook?: string,
        public date?: Moment,
        public postedDate?: Moment,
        public currencyID?: string,
        public reason?: string,
        public recorded?: boolean,
        public totalAmount?: number,
        public totalAmountOriginal?: number,
        public refTable?: string,
        public accountingObject?: IAccountingObject,
        public employee?: IAccountingObject,
        public checked?: boolean
    ) {
        this.recorded = this.recorded || false;
    }
}
