import { Moment } from 'moment';
import { TIDecrementDetails } from 'app/shared/model/ti-decrement-details.model';

export interface ITIDecrement {
    id?: string;
    companyID?: string;
    branchID?: string;
    typeID?: number;
    date?: Moment;
    typeLedger?: number;
    noFBook?: string;
    noMBook?: string;
    reason?: string;
    totalAmount?: number;
    recorded?: boolean;
    templateID?: string;
    tiDecrementDetails?: TIDecrementDetails[];
    viewVouchers?: any[];
}

export class TIDecrement implements ITIDecrement {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public typeID?: number,
        public date?: any,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public reason?: string,
        public totalAmount?: number,
        public recorded?: boolean,
        public templateID?: string,
        public tiDecrementDetails?: TIDecrementDetails[],
        public viewVouchers?: any[]
    ) {
        this.recorded = this.recorded || false;
    }
}
