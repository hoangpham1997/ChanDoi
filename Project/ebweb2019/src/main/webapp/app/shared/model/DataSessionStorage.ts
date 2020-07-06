import { ISearchVoucher } from 'app/shared/model/SearchVoucher';

export interface IDataSessionStorage {
    page?: number;
    itemsPerPage?: number;
    pageCount?: number;
    totalItems?: number;
    rowNum?: number;
    predicate?: number;
    searchVoucher?: string;
    accountingObjectName?: string;
    reverse?: number;
    statusDelete?: Boolean;
    isShowSearch?: boolean;
    isEdit?: boolean;
}

export class DataSessionStorage implements IDataSessionStorage {
    constructor(
        public searchVoucher?: string,
        public accountingObjectName?: string,
        public itemsPerPage?: number,
        public page?: number,
        public pageCount?: number,
        public predicate?: number,
        public reverse?: number,
        public rowNum?: number,
        public totalItems?: number,
        public ownerCard?: string,
        public isShowSearch?: boolean,
        public creditCardType?: string,
        public statusDelete?: Boolean,
        public isEdit?: boolean
    ) {
        this.isEdit = this.isEdit || false;
    }
}
