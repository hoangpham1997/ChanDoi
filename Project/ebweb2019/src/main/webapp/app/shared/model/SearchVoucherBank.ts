import { Moment } from 'moment';

export interface ISearchVoucherBank {
    bankCode?: string;
    bankName?: string;
    description?: string;
    keySearch?: string;
}

export class SearchVoucherBank implements ISearchVoucherBank {
    constructor(public bankCode?: string, public bankName?: string, public description?: string, public keySearch?: string) {}
}
