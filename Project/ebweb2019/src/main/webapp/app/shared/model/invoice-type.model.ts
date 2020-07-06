export interface IInvoiceType {
    id?: string;
    invoiceTypeCode?: string;
    invoiceTypeName?: string;
}

export class InvoiceType implements IInvoiceType {
    constructor(public id?: string, public invoiceTypeCode?: string, public invoiceTypeName?: string) {}
}
