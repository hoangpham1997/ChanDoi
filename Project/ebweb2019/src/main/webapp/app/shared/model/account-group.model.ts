export interface IAccountGroup {
    id?: string;
    accountingType?: number;
    accountGroupName?: string;
    accountGroupKind?: number;
    detailType?: string;
}

export class AccountGroup implements IAccountGroup {
    constructor(
        public id?: string,
        public accountingType?: number,
        public accountGroupName?: string,
        public accountGroupKind?: number,
        public detailType?: string
    ) {}
}
