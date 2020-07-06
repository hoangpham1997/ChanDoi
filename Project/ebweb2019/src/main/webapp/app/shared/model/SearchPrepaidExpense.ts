import { Moment } from 'moment';

export interface ISearchPrepaidExpense {
    typeExpense?: any /*Loại chứng từ*/;
    fromDate?: Moment;
    toDate?: Moment;
    textSearch?: string;
}

export class SearchPrepaidExpense implements ISearchPrepaidExpense {
    constructor(public typeExpense?: any, public fromDate?: Moment, public toDate?: Moment, public textSearch?: string) {}
}
