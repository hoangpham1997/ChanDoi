import { Moment } from 'moment';

export class UpdateDateClosedBook {
    constructor(public dateClosedBook?: Moment, public dateClosedBookOld?: Moment) {}
}
