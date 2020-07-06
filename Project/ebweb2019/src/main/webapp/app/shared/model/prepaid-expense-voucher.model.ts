import { Moment } from 'moment';

export interface IPrepaidExpenseVoucher {
    id?: number;
    prepaidExpenseID?: string;
    date?: Moment;
    no?: string;
    reason?: string;
    debitAccount?: string;
    creditAccount?: string;
    amount?: number;
}

export class PrepaidExpenseVoucher implements IPrepaidExpenseVoucher {
    constructor(
        public id?: number,
        public prepaidExpenseID?: string,
        public date?: Moment,
        public no?: string,
        public reason?: string,
        public debitAccount?: string,
        public creditAccount?: string,
        public amount?: number
    ) {}
}
