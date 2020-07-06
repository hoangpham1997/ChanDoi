import { Moment } from 'moment';

export interface ICollectionVoucher {
    id?: string;
    typeId?: string;
    date?: string;
    no?: string;
    accountingObject?: string;
    rate?: number;
    amount?: number;
    amountOriginal?: number;
    description?: string;
    bankAccountDetailId?: string;
    mathDate?: Moment;
    status?: boolean;
}

export class CollectionVoucher implements ICollectionVoucher {
    constructor(
        public id?: string,
        public typeId?: string,
        public date?: string,
        public no?: string,
        public accountingObject?: string,
        public rate?: number,
        public amount?: number,
        public amountOriginal?: number,
        public description?: string,
        public bankAccountDetailId?: string,
        public mathDate?: Moment,
        public status?: boolean
    ) {
        this.status = this.status || false;
    }
}
