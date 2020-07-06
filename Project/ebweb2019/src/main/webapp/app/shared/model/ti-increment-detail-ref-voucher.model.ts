export interface ITIIncrementDetailRefVoucher {
    id?: number;
    tiIncrementID?: string;
    refVoucherID?: string;
    orderPriority?: number;
}

export class TIIncrementDetailRefVoucher implements ITIIncrementDetailRefVoucher {
    constructor(public id?: number, public tiIncrementID?: string, public refVoucherID?: string, public orderPriority?: number) {}
}
