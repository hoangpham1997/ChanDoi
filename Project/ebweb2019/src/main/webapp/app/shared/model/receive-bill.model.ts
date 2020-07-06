import { Moment } from 'moment';

export interface IReceiveBill {
    id?: string;
    pPID?: string;
    typeID?: number;
    listIDPP?: string[];
    listIDPPDetail?: string[];
    date?: string;
    postedDate?: string;
    noFBook?: string;
    noMBook?: string;
    description?: string;
    amount?: number;
    vATRate?: number;
    vATAccount?: string;
    invoiceTemplate?: string;
    invoiceSeries?: string;
    invoiceNo?: string;
    invoiceDate?: Moment;
    goodsServicePurchaseID?: string;
    accountingObjectID?: string;
    isSelected?: boolean;
}

export class ReceiveBill implements IReceiveBill {
    constructor(
        public id?: string,
        public pPID?: string,
        public typeID?: number,
        public listIDPP?: string[],
        public listIDPPDetail?: string[],
        public date?: string,
        public postedDate?: string,
        public noFBook?: string,
        public noMBook?: string,
        public description?: string,
        public totalAmount?: number,
        public vATRate?: number,
        public vATAccount?: string,
        public invoiceTemplate?: string,
        public invoiceSeries?: string,
        public invoiceNo?: string,
        public invoiceDate?: Moment,
        public goodsServicePurchaseID?: string,
        public accountingObjectID?: string,
        public isSelected?: boolean
    ) {}
}
