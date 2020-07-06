import { Moment } from 'moment';

export interface ICostVouchersDTO {
    id?: string;
    accountingObjectID?: string;
    accountingObjectName?: string;
    noFBook?: string;
    noMBook?: string;
    reason?: string;
    postedDate?: Moment;
    date?: Moment;
    totalAmount?: number;
    totalAmountOriginal?: number;
    freightAmount?: number;
    totalDiscountAmount?: number;
    totalDiscountAmountOriginal?: number;
    totalVATAmount?: number;
    totalVATAmountOriginal?: number;
    amount?: number;
    accumulatedAllocateAmount?: number;
    accumulatedAllocateAmountOriginal?: number;
    checked?: boolean;
    sumAmount?: number;
    exchangeRate?: number;
    money?: number;
}

export class CostVouchersDTO implements ICostVouchersDTO {
    constructor(
        public id?: string,
        public accountingObjectID?: string,
        public accountingObjectName?: string,
        public noFBook?: string,
        public noMBook?: string,
        public reason?: string,
        public postedDate?: Moment,
        public date?: Moment,
        public totalAmount?: number,
        public totalAmountOriginal?: number,
        public freightAmount?: number,
        public amount?: number,
        public totalDiscountAmount?: number,
        public totalDiscountAmountOriginal?: number,
        public totalVATAmount?: number,
        public totalVATAmountOriginal?: number,
        public accumulatedAllocateAmount?: number,
        public accumulatedAllocateAmountOriginal?: number,
        public checked?: boolean,
        public sumAmount?: number,
        public exchangeRate?: number,
        public money?: number
    ) {}
}
