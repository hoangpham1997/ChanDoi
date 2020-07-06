import { Moment } from 'moment';
import { IAccountingObjectGroup } from 'app/shared/model/accounting-object-group.model';

export interface ISearchVoucherAccountingObjects {
    statusRecorded?: boolean;
    scaleType?: number;
    objectType?: number;
    accountingObjectGroup?: string;
    keySearch?: string;
    textSearch?: string;
}

export class SearchVoucherAccountingObjects implements ISearchVoucherAccountingObjects {
    constructor(
        public statusRecorded?: boolean,
        public scaleType?: number,
        public objectType?: number,
        public accountingObjectGroup?: string,
        public keySearch?: string,
        public textSearch?: string
    ) {}
}
