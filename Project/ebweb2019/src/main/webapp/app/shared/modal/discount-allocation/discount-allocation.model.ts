export interface IDiscountAllocation {
    checked?: boolean;
    object?: any;
    productCode?: string;
    reason?: string;
    quantity?: number;
    amountOriginal?: number;
    allocationRate?: number;
    discountAmountOriginal?: number;
}

export class DiscountAllocation implements IDiscountAllocation {
    constructor(
        checked?: boolean,
        object?: any,
        productCode?: string,
        reason?: string,
        quantity?: number,
        amountOriginal?: number,
        allocationRate?: number,
        discountAmountOriginal?: number
    ) {}
}
