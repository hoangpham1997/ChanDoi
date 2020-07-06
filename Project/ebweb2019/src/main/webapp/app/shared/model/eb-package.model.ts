export interface IEbPackage {
    id?: string;
    packageCode?: string;
    packageName?: string;
    limitedCompany?: number;
    limitedUser?: number;
    limitedVoucher?: number;
    expiredTime?: number;
    status?: boolean;
    description?: string;
    type?: number;
    isTotalPackage?: boolean;
    isSave?: boolean;
    comType?: number;
}

export class EbPackage implements IEbPackage {
    constructor(
        public id?: string,
        public packageCode?: string,
        public packageName?: string,
        public limitedCompany?: number,
        public limitedUser?: number,
        public limitedVoucher?: number,
        public expiredTime?: number,
        public status?: boolean,
        public description?: string,
        public type?: number,
        public isTotalPackage?: boolean,
        public isSave?: boolean,
        public comType?: number
    ) {
        this.status = this.status || false;
        this.isTotalPackage = this.isTotalPackage || false;
        this.isSave = this.isSave || false;
    }
}
