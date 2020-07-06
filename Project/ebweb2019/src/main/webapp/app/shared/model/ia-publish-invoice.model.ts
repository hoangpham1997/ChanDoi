import { Moment } from 'moment';
import { IaPublishInvoiceDetails } from 'app/shared/model/ia-publish-invoice-details.model';

export interface IIaPublishInvoice {
    id?: string;
    companyId?: string;
    date?: Moment;
    no?: string;
    typeId?: number;
    receiptedTaxOffical?: string;
    representationInLaw?: string;
    status?: number;
    iaPublishInvoiceDetails?: IaPublishInvoiceDetails[];
}

export class IaPublishInvoice implements IIaPublishInvoice {
    constructor(
        public id?: string,
        public companyId?: string,
        public date?: Moment,
        public no?: string,
        public typeId?: number,
        public receiptedTaxOffical?: string,
        public representationInLaw?: string,
        public status?: number,
        public iaPublishInvoiceDetails?: IaPublishInvoiceDetails[]
    ) {}
}
