import { Moment } from 'moment';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { IRepository } from 'app/shared/model/repository.model';
import { IAccountList } from 'app/shared/model/account-list.model';

export interface IPPInvoiceDetailCost {
    id?: string;
    costType?: boolean;
    refID?: string;
    typeID?: number;
    ppServiceID?: string;
    accountObjectID?: string;
    totalFreightAmount?: number;
    amount?: number;
    accumulatedAllocateAmount?: number;
    totalFreightAmountOriginal?: number;
    amountOriginal?: number;
    accumulatedAllocateAmountOriginal?: number;
    templateID?: string;
    orderPriority?: number;
    accountingObjectName?: string;
    accountingObjectId?: string;
    date?: Moment;
    postedDate?: Moment;
    noMBook?: string;
    noFBook?: string;
}

export class PPInvoiceDetailCost implements IPPInvoiceDetailCost {
    constructor(
        public id?: string,
        public costType?: boolean,
        public refID?: string,
        public typeID?: number,
        public ppServiceID?: string,
        public accountObjectID?: string,
        public totalFreightAmount?: number,
        public amount?: number,
        public accumulatedAllocateAmount?: number,
        public totalFreightAmountOriginal?: number,
        public amountOriginal?: number,
        public accumulatedAllocateAmountOriginal?: number,
        public templateID?: string,
        public orderPriority?: number,
        public accountingObjectName?: string,
        public accountingObjectId?: string,
        public date?: Moment,
        public postedDate?: Moment,
        public noMBook?: string,
        public noFBook?: string
    ) {}
}
