import { Moment } from 'moment';
import { IAccountingObjectGroup } from 'app/shared/model/accounting-object-group.model';

export interface ISearchVoucherEmployee {
    statusRecorded?: boolean;
    organizationUnitName?: string;
    contactSex?: number;
    objectType?: number;
    keySearch?: string;
    textSearch?: string;
}

export class SearchVoucherEmployee implements ISearchVoucherEmployee {
    constructor(
        public statusRecorded?: boolean,
        public organizationUnitName?: string,
        public contactSex?: number,
        public objectType?: number,
        public keySearch?: string,
        public textSearch?: string
    ) {}
}
