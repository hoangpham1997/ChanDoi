import { Moment } from 'moment';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';

export interface IMultiDelete {
    status?: boolean;
    date?: Moment;
    postedDate?: Moment;
    typeID?: string;
    reason?: string;
}

export class MultiDelete implements IMultiDelete {
    constructor(
        public status?: boolean,
        public date?: Moment,
        public postedDate?: Moment,
        public typeID?: string,
        public reason?: string
    ) {}
}
