import { IAccountList } from 'app/shared/model/account-list.model';

export interface IAccountDefault {
    id?: string;
    companyID?: string;
    branchID?: string;
    accountingType?: number;
    typeID?: number;
    columnName?: string;
    columnCaption?: string;
    filterAccount?: string;
    defaultAccount?: string;
    reduceAccount?: string;
    pPType?: boolean;
    orderPriority?: number;
    childAccount?: IAccountList[];
}

export class AccountDefault implements IAccountDefault {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public accountingType?: number,
        public typeID?: number,
        public columnName?: string,
        public columnCaption?: string,
        public filterAccount?: string,
        public defaultAccount?: string,
        public reduceAccount?: string,
        public pPType?: boolean,
        public orderPriority?: number,
        public childAccount?: IAccountList[]
    ) {
        this.pPType = this.pPType || false;
    }
}
