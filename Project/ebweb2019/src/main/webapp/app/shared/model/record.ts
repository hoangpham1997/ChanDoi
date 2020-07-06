export interface Irecord {
    id?: string;
    typeID?: number;
    success?: boolean;
    msg?: string;
    repositoryLedgerID?: string;
    answer?: boolean;
}

export class Record implements Irecord {
    constructor(
        public id?: string,
        public typeID?: number,
        public success?: boolean,
        public msg?: string,
        public repositoryLedgerID?: string,
        public answer?: boolean
    ) {
        this.success = this.success || false;
    }
}
