export interface IPPPayVendor {
    accountingObjectID?: string;
    accountingObjectName?: string;
    accountingObjectCode?: string;
    soDuDauNam?: number;
    soPhatSinh?: number;
    soDaTra?: number;
    soConPhaiTra?: number;
}

export class PPPayVendor implements IPPPayVendor {
    constructor(
        public accountingObjectID?: string,
        public accountingObjectName?: string,
        public accountingObjectCode?: string,
        public soDuDauNam?: number,
        public soPhatSinh?: number,
        public soDaTra?: number,
        public soConPhaiTra?: number
    ) {}
}
