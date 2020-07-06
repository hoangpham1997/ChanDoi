import { Moment } from 'moment';

export interface IOPMaterialGoods {
    id?: string;

    companyId?: string;

    typeId?: number;

    postedDate?: Moment;

    typeLedger?: number;

    accountNumber?: string;

    materialGoodsId?: string;

    materialGoodsName?: string;

    materialGoodsCode?: string;

    units?: any[];

    currencyId?: string;

    exchangeRate?: number;

    debitAmount?: number;

    debitAmountOriginal?: number;

    creditAmount?: number;

    creditAmountOriginal?: number;

    unitId?: string;

    unitName?: string;

    quantity?: number;

    unitPrice?: number;

    unitPriceOriginal?: number;

    mainUnitId?: string;

    mainUnitName?: string;

    mainUnitPrice?: number;

    mainConvertRate?: number;

    mainQuantity?: number;

    formula?: string;

    amount?: number;

    amountOriginal?: number;

    lotNo?: string;

    expiryDate?: Moment;

    expiryDateStr?: string;

    repositoryId?: string;

    repositoryCode?: string;

    contractId?: string;

    bankAccountDetailId?: string;

    bankAccount?: string;

    noBookContract?: string;

    costSetId?: string;

    costSetCode?: string;

    expenseItemId?: string;

    expenseItemCode?: string;

    departmentId?: string;

    organizationUnitCode?: string;

    statisticsCodeId?: string;

    statisticsCode?: string;

    budgetItemId?: string;

    budgetItemCode?: string;

    orderPriority?: number;
}

export class OpMaterialGoodsModel implements IOPMaterialGoods {
    constructor(
        public id?: string,
        public companyId?: string,
        public typeIs?: number,
        public postedDate?: Moment,
        public typeLedger?: number,
        public typeId?: number,
        public accountNumber?: string,
        public materialGoodsId?: string,
        public materialGoodsCode?: string,
        public materialGoodsName?: string,
        public currencyId?: string,
        public exchangeRate?: number,
        public debitAmount?: number,
        public debitAmountOriginal?: number,
        public creditAmount?: number,
        public creditAmountOriginal?: number,
        public unitId?: string,
        public unitName?: string,
        public quantity?: number,
        public unitPrice?: number,
        public unitPriceOriginal?: number,
        public mainUnitId?: string,
        public mainUnitName?: string,
        public mainUnitPrice?: number,
        public mainConvertRate?: number,
        public formula?: string,
        public amount?: number,
        public amountOriginal?: number,
        public lotNo?: string,
        public expiryDate?: Moment,
        public expiryDateStr?: string,
        public repositoryId?: string,
        public repositoryCode?: string,
        public bankAccountDetailId?: string,
        public bankAccount?: string,
        public contractId?: string,
        public noBookContract?: string,
        public costSetId?: string,
        public costSetCode?: string,
        public expenseItemId?: string,
        public expenseItemCode?: string,
        public departmentId?: string,
        public organizationUnitCode?: string,
        public statisticsCodeId?: string,
        public statisticsCode?: string,
        public budgetItemId?: string,
        public budgetItemCode?: string,
        public orderPriority?: number
    ) {}
}
