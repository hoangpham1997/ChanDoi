import { Moment } from 'moment';
import { IMBDeposit } from 'app/shared/model//mb-deposit.model';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';

export interface IMBDepositDetailTax {
    id?: string;
    mBDepositID?: string;
    description?: string;
    vATAmount?: number;
    vATAmountOriginal?: number;
    vATRate?: number;
    vATAccount?: string;
    pretaxAmount?: number;
    pretaxAmountOriginal?: number;
    invoiceType?: number;
    invoiceDate?: Moment;
    invoiceNo?: string;
    invoiceSeries?: string;
    goodsServicePurchaseID?: string;
    orderPriority?: number;
    mbdeposit?: IMBDeposit;
    // accountingObjectID?: string;
    accountingObjectID?: string;
    vatAccountItem?: any;
}

export class MBDepositDetailTax implements IMBDepositDetailTax {
    constructor(
        public id?: string,
        public mBDepositID?: string,
        public description?: string,
        public vATAmount?: number,
        public vATAmountOriginal?: number,
        public vATRate?: number,
        public vATAccount?: string,
        public pretaxAmount?: number,
        public pretaxAmountOriginal?: number,
        public invoiceType?: number,
        public invoiceDate?: Moment,
        public invoiceNo?: string,
        public invoiceSeries?: string,
        public goodsServicePurchaseID?: string,
        public orderPriority?: number,
        public mbdeposit?: IMBDeposit,
        public accountingObjectID?: string,
        public vatAccountItem?: any
    ) {}
}
