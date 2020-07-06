export interface ICurrency {
    id?: string;
    currencyCode?: string;
    currencyName?: string;
    exchangeRate?: number;
    companyID?: string;
    branchID?: string;
    isActive?: boolean;
    formula?: string;
}

export class Currency implements ICurrency {
    constructor(
        public id?: string,
        public currencyCode?: string,
        public currencyName?: string,
        public exchangeRate?: number,
        public companyID?: string,
        public branchID?: string,
        public isActive?: boolean,
        public formula?: string
    ) {
        this.isActive = this.isActive || false;
    }
}
