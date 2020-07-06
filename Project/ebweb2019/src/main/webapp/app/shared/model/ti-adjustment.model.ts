import { Moment } from 'moment';
import { ITIAdjustmentDetails } from 'app/shared/model/ti-adjustment-details.model';

export interface ITIAdjustment {
    id?: string;
    companyID?: string;
    branchID?: string;
    typeID?: number;
    date?: Moment;
    typeLedger?: number;
    noFBook?: string;
    noMBook?: string;
    postedDate?: Moment;
    reason?: string;
    totalAmount?: number;
    recorded?: boolean;
    templateID?: string;
    tiAdjustmentDetails?: ITIAdjustmentDetails[];
    viewVouchers?: any[];
    diffRemainingAmount?: any;
    differAllocationTime?: any;
}

export class TIAdjustment implements ITIAdjustment {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public typeID?: number,
        public date?: Moment,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public postedDate?: Moment,
        public reason?: string,
        public totalAmount?: number,
        public recorded?: boolean,
        public templateID?: string,
        public tiAdjustmentDetails?: ITIAdjustmentDetails[],
        public viewVouchers?: any[],
        public diffRemainingAmount?: any,
        public differAllocationTime?: any
    ) {
        this.recorded = this.recorded || false;
    }
}
