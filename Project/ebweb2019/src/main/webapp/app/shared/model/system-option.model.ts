export interface ISystemOption {
    id?: number;
    userID?: string;
    companyID?: string;
    branchID?: string;
    code?: string;
    name?: string;
    type?: number;
    data?: any;
    defaultData?: string;
    note?: string;
    isSecurity?: boolean;
}

export class SystemOption implements ISystemOption {
    constructor(
        public id?: number,
        public userID?: string,
        public companyID?: string,
        public branchID?: string,
        public code?: string,
        public name?: string,
        public type?: number,
        public data?: any,
        public defaultData?: string,
        public note?: string,
        public isSecurity?: boolean
    ) {
        this.isSecurity = this.isSecurity || false;
    }
}
