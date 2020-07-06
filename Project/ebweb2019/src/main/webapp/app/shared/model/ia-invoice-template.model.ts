export interface IIAInvoiceTemplate {
    id?: number;
    invoiceTemplateName?: string;
    invoiceTemplatePath?: string;
    invoiceType?: number;
}

export class IAInvoiceTemplate implements IIAInvoiceTemplate {
    constructor(
        public id?: number,
        public invoiceTemplateName?: string,
        public invoiceTemplatePath?: string,
        public invoiceType?: number
    ) {}
}
