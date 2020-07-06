export interface IFAIncrementDetailRefVoucher {
    id?: number;
    faIncrementID?: string;
    refVoucherID?: string;
    orderPriority?: number;
}

export class FAIncrementDetailRefVoucher implements IFAIncrementDetailRefVoucher {
    constructor(public id?: number, public faIncrementID?: string, public refVoucherID?: string, public orderPriority?: number) {}
}
