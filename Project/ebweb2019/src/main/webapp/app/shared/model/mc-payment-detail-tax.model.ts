import { Moment } from 'moment';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';

export interface IMCPaymentDetailTax {
    id?: string;
    mCPaymentID?: string;
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
    goodsServicePurchaseID?: number;
    orderPriority?: number;
    accountingObject?: IAccountingObject;
    accountingObjectName?: string;
    taxCode?: string;
    vatAccountItem?: any;
}

export class MCPaymentDetailTax implements IMCPaymentDetailTax {
    constructor(
        public id?: string,
        public mCPaymentID?: string,
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
        public goodsServicePurchaseID?: number,
        public orderPriority?: number,
        public accountingObject?: IAccountingObject,
        public accountingObjectName?: string,
        public taxCode?: string,
        public vatAccountItem?: any
    ) {}
}
