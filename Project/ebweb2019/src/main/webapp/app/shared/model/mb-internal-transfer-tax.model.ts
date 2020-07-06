import { Moment } from 'moment';
import { IMBInternalTransfer } from 'app/shared/model//mb-internal-transfer.model';

export interface IMBInternalTransferTax {
    id?: string;
    description?: string;
    vatAmount?: number;
    vatAmountOriginal?: number;
    vatRate?: number;
    vatAccount?: string;
    pretaxAmount?: number;
    pretaxAmountOriginal?: number;
    invoiceType?: number;
    invoiceDate?: Moment;
    invoiceNo?: string;
    invoiceSeries?: string;
    goodsServicePurchaseID?: string;
    accountingObjectID?: string;
    orderPriority?: number;
    mBInternalTransfer?: IMBInternalTransfer;
}

export class MBInternalTransferTax implements IMBInternalTransferTax {
    constructor(
        public id?: string,
        public description?: string,
        public vatAmount?: number,
        public vatAmountOriginal?: number,
        public vatRate?: number,
        public vatAccount?: string,
        public pretaxAmount?: number,
        public pretaxAmountOriginal?: number,
        public invoiceType?: number,
        public invoiceDate?: Moment,
        public invoiceNo?: string,
        public invoiceSeries?: string,
        public goodsServicePurchaseID?: string,
        public accountingObjectID?: string,
        public orderPriority?: number,
        public mBInternalTransfer?: IMBInternalTransfer
    ) {}
}
