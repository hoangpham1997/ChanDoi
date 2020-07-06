import { Moment } from 'moment';
import { ITIIncrementDetails } from 'app/shared/model/ti-increment-details.model';

export interface ITIIncrement {
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
    templateID?: string;
    recorded?: boolean;
    tiIncrementDetails?: ITIIncrementDetails[];
    tiIncrementDetailRefVouchers?: any;
}

export class TIIncrement implements ITIIncrement {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public typeID?: number,
        public date?: Moment,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public reason?: string,
        public totalAmount?: number,
        public templateID?: string,
        public recorded?: boolean,
        public tiIncrementDetails?: ITIIncrementDetails[],
        public tiIncrementDetailRefVouchers?: any
    ) {
        this.recorded = this.recorded || false;
    }
}
