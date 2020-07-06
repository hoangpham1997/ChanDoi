export interface IMuaDichVuSearch {
    fromDate?: string;
    toDate?: string;
    receiptType?: number;
    currencyID?: string;
    accountingObjectID?: string;
    hasRecord?: number;
    freeSearch?: string;
    ppServiceId?: string;
    action?: number;
    noBookType?: number;
    page?: number;
}

export class MuaDichVuSearch implements IMuaDichVuSearch {
    constructor(
        fromDate?: string,
        toDate?: string,
        receiptType?: number,
        currencyID?: string,
        accountingObjectID?: string,
        hasRecord?: number,
        freeSearch?: string,
        ppServiceId?: string,
        action?: number,
        noBookType?: number,
        page?: number
    ) {}
}
