import { Moment } from 'moment';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';

export interface IMultipleRecord {
    listID?: any[];
    statusRecorded?: number;
}

export class MutipleRecord implements IMultipleRecord {
    constructor(public listID?: any[], public statusRecorded?: number) {}
}
