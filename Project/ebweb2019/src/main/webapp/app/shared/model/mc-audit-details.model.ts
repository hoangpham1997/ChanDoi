export interface IMCAuditDetails {
    id?: string;
    mCAuditId?: string;
    valueOfMoney?: number;
    quantity?: number;
    description?: string;
    amount?: number;
    orderPriority?: number;
}

export class MCAuditDetails implements IMCAuditDetails {
    constructor(
        public id?: string,
        public mCAuditId?: string,
        public valueOfMoney?: number,
        public quantity?: number,
        public description?: string,
        public amount?: number,
        public orderPriority?: number
    ) {}
}
