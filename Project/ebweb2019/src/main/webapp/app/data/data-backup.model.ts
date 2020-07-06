import { Moment } from 'moment';

export class DataBackupModel {
    id?: string;
    name?: string;
    dateBackup?: Moment;
    timeRestore?: Moment;
    size?: number;
    path?: string;
    note?: string;
    status?: number;
}
