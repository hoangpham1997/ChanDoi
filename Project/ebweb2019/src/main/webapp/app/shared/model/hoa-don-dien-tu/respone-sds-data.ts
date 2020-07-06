export interface IResponeSdsData {
    pattern?: string;
    serial?: string;
    html?: string;
    invoiceStatus?: number;
    xmlReport?: string;
    ikeys?: string[];
    keyInvoiceNoDTO?: IKeyInvoiceNo[];
    keyInvoiceMsgDTO?: IKeyInvoiceMsg[];
    digestDataDTO?: IDigestDataDTO[];
}

export class ResponeSdsData implements IResponeSdsData {
    constructor(
        public pattern?: string,
        public serial?: string,
        public html?: string,
        public invoiceStatus?: number,
        public xmlReport?: string,
        public ikeys?: string[],
        public keyInvoiceNoDTO?: IKeyInvoiceNo[],
        public keyInvoiceMsgDTO?: IKeyInvoiceMsg[],
        public digestDataDTO?: IDigestDataDTO[]
    ) {}
}

export interface IKeyInvoiceNo {
    id?: string;
    no?: string;
}

export class KeyInvoiceNo implements IKeyInvoiceNo {
    constructor(public id?: string, public no?: string) {}
}

export interface IKeyInvoiceMsg {
    id?: string;
    message?: string;
}

export class KeyInvoiceMsg implements IKeyInvoiceMsg {
    constructor(public id?: string, public message?: string) {}
}

export interface IDigestDataDTO {
    key?: string;
    hashData?: string;
    sigData?: string;
}

export class DigestDataDTO implements IDigestDataDTO {
    constructor(public key?: string, public hashData?: string, public sigData?: string) {}
}
