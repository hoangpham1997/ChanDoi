import { InvoiceType } from 'app/shared/model/invoice-type.model';

export interface IIAReport {
    id?: string;
    companyID?: string;
    branchID?: string;
    reportName?: string;
    invoiceForm?: number;
    invoiceType?: InvoiceType;
    invoiceTypeID?: string;
    invoiceTemplate?: string;
    invoiceSeries?: string;
    copyNumber?: number;
    tempSortOrder?: number;
    purpose1?: string;
    codeColor1?: string;
    purpose2?: string;
    codeColor2?: string;
    purpose3?: string;
    codeColor3?: string;
    purpose4?: string;
    codeColor4?: string;
    purpose5?: string;
    codeColor5?: string;
    purpose6?: string;
    codeColor6?: string;
    purpose7?: string;
    codeColor7?: string;
    purpose8?: string;
    codeColor8?: string;
    purpose9?: string;
    codeColor9?: string;
}

export class IAReport implements IIAReport {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public reportName?: string,
        public invoiceForm?: number,
        public invoiceType?: InvoiceType,
        public invoiceTypeID?: string,
        public invoiceTemplate?: string,
        public invoiceSeries?: string,
        public copyNumber?: number,
        public tempSortOrder?: number,
        public purpose1?: string,
        public codeColor1?: string,
        public purpose2?: string,
        public codeColor2?: string,
        public purpose3?: string,
        public codeColor3?: string,
        public purpose4?: string,
        public codeColor4?: string,
        public purpose5?: string,
        public codeColor5?: string,
        public purpose6?: string,
        public codeColor6?: string,
        public purpose7?: string,
        public codeColor7?: string,
        public purpose8?: string,
        public codeColor8?: string,
        public purpose9?: string,
        public codeColor9?: string
    ) {}
}
