import { Moment } from 'moment';

export interface IEMContract {
    id?: string;
    companyID?: string;
    branchID?: string;
    typeID?: number;
    typeLedger?: number;
    noFBook?: string;
    noMBook?: string;
    name?: string;
    description?: string;
    signedDate?: Moment;
    currencyID?: string;
    exchangeRate?: number;
    amount?: number;
    amountOriginal?: number;
    accountingObjectID?: string;
    signName?: string;
    startedDate?: Moment;
    closedDate?: Moment;
    contractState?: number;
    isWatchForCostPrice?: boolean;
    billReceived?: boolean;
    isActive?: boolean;
    checked?: boolean;
}

export class EMContract implements IEMContract {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public typeID?: number,
        public typeLedger?: number,
        public noFBook?: string,
        public noMBook?: string,
        public name?: string,
        public description?: string,
        public signedDate?: Moment,
        public currencyID?: string,
        public exchangeRate?: number,
        public amount?: number,
        public amountOriginal?: number,
        public accountingObjectID?: string,
        public signName?: string,
        public startedDate?: Moment,
        public closedDate?: Moment,
        public contractState?: number,
        public isWatchForCostPrice?: boolean,
        public billReceived?: boolean,
        public isActive?: boolean,
        public checked?: boolean
    ) {
        this.isWatchForCostPrice = this.isWatchForCostPrice || false;
        this.billReceived = this.billReceived || false;
        this.isActive = this.isActive || false;
    }
}
