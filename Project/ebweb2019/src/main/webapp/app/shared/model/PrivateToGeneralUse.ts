import { Moment } from 'moment';

export interface IPrivateToGeneralUse {
    iD?: string;
    code?: string;
    name?: string;
    companyID?: string;
    type?: number;
    nameCategory?: string;
}

export class PrivateToGeneralUse implements IPrivateToGeneralUse {
    constructor(
        public iD?: string,
        public code?: string,
        public name?: string,
        public companyID?: string,
        public type?: number,
        public nameCategory?: string
    ) {}
}
