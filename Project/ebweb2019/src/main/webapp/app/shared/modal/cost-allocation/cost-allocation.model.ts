export interface ICostAllocation {
    object?: any;
    productCode?: string;
    reason?: string;
    quantity?: number;
    amountOriginal?: number;
    amount?: number;
    allocationRate?: number;
    freightAmountOriginal?: number;
    freightAmount?: number;
    importTaxExpenseAmountOriginal?: number;
}

export class CostAllocation implements ICostAllocation {
    constructor(
        object?: any,
        productCode?: string,
        reason?: string,
        quantity?: number,
        amountOriginal?: number,
        amount?: number,
        allocationRate?: number,
        freightAmountOriginal?: number,
        freightAmount?: number,
        importTaxExpenseAmountOriginal?: number
    ) {}
}
