export interface ISAReceiptDebit {
    accountingObjectID?: string;
    accountingObjectName?: string;
    accountingObjectCode?: string;
    soDuDauNam?: number;
    soPhatSinh?: number;
    soDaThu?: number;
    soConPhaiThu?: number;
}

export class SAReceiptDebit implements ISAReceiptDebit {
    constructor(
        public accountingObjectID?: string,
        public accountingObjectName?: string,
        public accountingObjectCode?: string,
        public soDuDauNam?: number,
        public soPhatSinh?: number,
        public soDaThu?: number,
        public soConPhaiThu?: number
    ) {}
}
