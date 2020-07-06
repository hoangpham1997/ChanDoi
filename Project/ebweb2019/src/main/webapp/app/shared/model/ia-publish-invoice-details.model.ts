import { Moment } from 'moment';
import { IInvoiceType } from 'app/shared/model/invoice-type.model';

export interface IIaPublishInvoiceDetails {
    id?: string;
    iaPublishInvoiceID?: string;
    iaReportID?: string;
    invoiceForm?: number;
    invoiceFormName?: string;
    invoiceTemplate?: string;
    invoiceType?: IInvoiceType;
    invoiceSeries?: string;
    fromNo?: string;
    toNo?: string;
    quantity?: number;
    startUsing?: Moment;
    companyName?: string;
    companyTaxCode?: string;
    contractNo?: string;
    contractDate?: Moment;
    orderPriority?: number;
    isCheck?: boolean;
    invoiceTypeID?: string;
}

export class IaPublishInvoiceDetails implements IIaPublishInvoiceDetails {
    constructor(
        public id?: string,
        public iaPublishInvoiceID?: string,
        public iaReportID?: string,
        public invoiceForm?: number,
        public invoiceFormName?: string,
        public invoiceTemplate?: string,
        public invoiceSeries?: string,
        public fromNo?: string,
        public toNo?: string,
        public quantity?: number,
        public startUsing?: Moment | any,
        public contractDate?: Moment | any,
        public startUsingHolder?: Moment | any,
        public contractDateHolder?: Moment | any,
        public companyName?: string,
        public companyTaxCode?: string,
        public contractNo?: string,
        public orderPriority?: number,
        public invoiceType?: IInvoiceType,
        public invoiceTypeID?: string,
        public isCheck?: boolean
    ) {}
}
