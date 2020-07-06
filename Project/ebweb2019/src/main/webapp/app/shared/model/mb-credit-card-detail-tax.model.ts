import { Moment } from 'moment';
import { IMBCreditCard } from 'app/shared/model//mb-credit-card.model';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';

export interface IMBCreditCardDetailTax {
    id?: number;
    mBCreditCardID?: string;
    description?: string;
    vATAmount?: number;
    vATAmountOriginal?: number;
    vATRate?: number;
    vATAccount?: string;
    pretaxAmount?: number;
    pretaxAmountOriginal?: number;
    invoiceTemplate?: string;
    invoiceDate?: Moment;
    invoiceNo?: string;
    invoiceSeries?: string;
    goodsServicePurchaseID?: string;
    orderPriority?: number;
    mBCreditCard?: IMBCreditCard;
    accountingObjectID?: string;
    accountingObjectName?: string;
    taxCode?: string;
    vatAccountItem?: any;
}

export class MBCreditCardDetailTax implements IMBCreditCardDetailTax {
    constructor(
        public id?: number,
        public mBCreditCardID?: string,
        public description?: string,
        public vATAmount?: number,
        public vATAmountOriginal?: number,
        public vATRate?: number,
        public vATAccount?: string,
        public pretaxAmount?: number,
        public pretaxAmountOriginal?: number,
        public invoiceTemplate?: string,
        public invoiceDate?: Moment,
        public invoiceNo?: string,
        public invoiceSeries?: string,
        public goodsServicePurchaseID?: string,
        public orderPriority?: number,
        public mBCreditCard?: IMBCreditCard,
        public accountingObjectID?: string,
        public accountingObjectName?: string,
        public taxCode?: string,
        public vatAccountItem?: any
    ) {}
}
