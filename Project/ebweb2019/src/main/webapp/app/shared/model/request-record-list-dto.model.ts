import { Irecord } from 'app/shared/model/record';

export class RequestRecordListDtoModel {
    constructor(public records?: Irecord[], public typeIDMain?: number, public kho?: boolean) {}
}
