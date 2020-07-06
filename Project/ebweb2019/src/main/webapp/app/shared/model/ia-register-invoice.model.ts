import { Moment } from 'moment';
import { IIARegisterInvoiceDetails } from 'app/shared/model/ia-register-invoice-details.model';

export interface IIARegisterInvoice {
    id?: string;
    companyID?: string;
    typeID?: number;
    date?: Moment;
    no?: string;
    description?: string;
    signer?: string;
    status?: number;
    attachFileName?: string;
    attachFileContent?: any;
    iaRegisterInvoiceDetails?: IIARegisterInvoiceDetails[];
}

export class IARegisterInvoice implements IIARegisterInvoice {
    constructor(
        public id?: string,
        public companyID?: string,
        public typeID?: number,
        public date?: Moment,
        public no?: string,
        public description?: string,
        public signer?: string,
        public status?: number,
        public attachFileName?: string,
        public attachFileContent?: any,
        public iaRegisterInvoiceDetails?: IIARegisterInvoiceDetails[]
    ) {}
}
