import { Moment } from 'moment';

export interface ISearchVoucherCurrency {
    currencyCode?: string;
    currencyName?: string;
    exchangeRate?: string;
    keySearch?: string;
}

export class SearchVoucherCurrency implements ISearchVoucherCurrency {
    constructor(public currencyCode?: string, public currencyName?: string, public exchangeRate?: string, public keySearch?: string) {}
}
