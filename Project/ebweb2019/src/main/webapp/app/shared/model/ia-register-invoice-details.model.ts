export interface IIARegisterInvoiceDetails {
    id?: number;
    iaRegisterInvoiceID?: string;
    iaReportID?: string;
    purpose?: string;
}

export class IARegisterInvoiceDetails implements IIARegisterInvoiceDetails {
    constructor(public id?: number, public iaRegisterInvoiceID?: string, public iaReportID?: string, public purpose?: string) {}
}
