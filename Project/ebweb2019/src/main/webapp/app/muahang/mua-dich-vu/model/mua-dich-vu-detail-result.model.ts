import { IUnit } from 'app/shared/model/unit.model';

export interface IMuaDichVuDetailResult {
    id?: string;
    materialGoodsId?: string;
    materialGoodsCode?: string;
    materialGoodsName?: string;
    debitAccount?: string;
    creditAccount?: string;
    postedObjectId?: string;
    postedObjectCode?: string;
    postedObjectName?: string;
    unitId?: string;
    unitName?: string;
    quantity?: number;
    quantityFromDB?: number;
    quantityReceipt?: number;
    unitPrices?: any[];
    unitPrice?: number;
    amount?: number;
    discountRate?: number;
    discountAmount?: number;
    ppOrderNo?: string;
    unitPriceOriginal?: number;
    amountOriginal?: number;
    discountAmountOriginal?: number;
    vatDescription?: string;
    vatRate?: number;
    vatAmount?: number;
    isRequiredVATAccount?: boolean;
    vatAccount?: string;
    deductionDebitAccount?: string;
    invoiceTemplate?: string;
    invoiceSeries?: string;
    invoiceNo?: string;
    invoiceDate?: string;
    goodsServicePurchaseId?: string;
    goodsServicePurchaseName?: string;
    isForeignCurrency?: boolean;
    vatAmountOriginal?: number;
    expenseItemId?: string;
    expenseItemCode?: string;
    costSetId?: string;
    costSetCode?: string;
    emContractId?: string;
    emContractCode?: string;
    budgetItemId?: string;
    budgetItemCode?: string;
    departmentId?: string;
    departmentCode?: string;
    statisticsId?: string;
    statisticsCode?: string;
    exchangeRate?: number;
    units?: IUnit[];
    ppOrderId?: string;
    ppOrderDetailId?: string;
    orderPriority?: number;
    newPriority?: number;
}

export class MuaDichVuDetailResult implements IMuaDichVuDetailResult {
    constructor(
        public id?: string,
        public materialGoodsId?: string,
        public materialGoodsCode?: string,
        public materialGoodsName?: string,
        public debitAccount?: string,
        public creditAccount?: string,
        public postedObjectId?: string,
        public postedObjectCode?: string,
        public unitId?: string,
        public unitName?: string,
        public quantity?: number,
        public quantityFromDB?: number,
        public quantityReceipt?: number,
        public unitPrices?: any[],
        public unitPrice?: number,
        public amount?: number,
        public discountRate?: number,
        public discountAmount?: number,
        public unitPriceOriginal?: number,
        public amountOriginal?: number,
        public discountAmountOriginal?: number,
        public vatDescription?: string,
        public vatRate?: number,
        public vatAmount?: number,
        public vatAccount?: string,
        public isRequiredVATAccount?: boolean,
        public deductionDebitAccount?: string,
        public invoiceTemplate?: string,
        public invoiceSeries?: string,
        public invoiceNo?: string,
        public ppOrderId?: string,
        public ppOrderDetailId?: string,
        public invoiceDate?: string,
        public goodsServicePurchaseId?: string,
        public goodsServicePurchaseName?: string,
        public isForeignCurrency?: boolean,
        public vatAmountOriginal?: number,
        public expenseItemId?: string,
        public expenseItemCode?: string,
        public costSetId?: string,
        public costSetCode?: string,
        public emContractId?: string,
        public emContractCode?: string,
        public budgetItemId?: string,
        public budgetItemCode?: string,
        public organizationUnitName?: string,
        public statisticsId?: string,
        public statisticsCode?: string,
        public ppOrderNo?: string,
        public departmentId?: string,
        public departmentCode?: string,
        public exchangeRate?: number,
        public orderPriority?: number,
        public newPriority?: number,
        public units?: IUnit[]
    ) {
        this.units = units ? units : [];
    }
}
