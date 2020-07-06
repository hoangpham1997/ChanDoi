import { Moment } from 'moment';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { IRepository } from 'app/shared/model/repository.model';
import { IAccountList } from 'app/shared/model/account-list.model';

export interface IPPInvoiceDetails {
    id?: string;
    ppInvoiceId?: string;
    materialGoodsId?: string;
    materialGood?: any;
    repositoryId?: string;
    repository?: any;
    description?: string;
    debitAccount?: string;
    creditAccount?: string;
    unitId?: string;
    quantity?: number;
    unitPrice?: number;
    unitPriceOriginal?: number;
    amount?: number;
    amountOriginal?: number;
    discountRate?: number;
    discountAmount?: number;
    discountAmountOriginal?: number;
    inwardAmount?: number;
    inwardAmountOriginal?: number;
    freightAmount?: number;
    freightAmountOriginal?: number;
    importTaxExpenseAmount?: number;
    importTaxExpenseAmountOriginal?: number;
    expiryDate?: Moment;
    lotNo?: string;
    customUnitPrice?: number;
    vatRate?: number;
    vatAmount?: number;
    vatAmountOriginal?: number;
    vatAccount?: string;
    deductionDebitAccount?: string;
    mainUnitId?: string;
    mainQuantity?: number;
    mainUnitPrice?: number;
    mainConvertRate?: number;
    formula?: string;
    importTaxRate?: number;
    importTaxAmount?: number;
    importTaxAmountOriginal?: number;
    importTaxAccount?: string;
    specialConsumeTaxRate?: number;
    specialConsumeTaxAmount?: number;
    specialConsumeTaxAmountOriginal?: number;
    specialConsumeTaxAccount?: string;
    invoiceType?: string;
    invoiceDate?: Moment;
    invoiceNo?: string;
    invoiceSeries?: string;
    goodsServicePurchaseId?: string;
    accountingObjectId?: string;
    budgetItemId?: string;
    costSetId?: string;
    contractId?: string;
    statisticCodeId?: string;
    departmentId?: string;
    expenseItemId?: string;
    ppOrderId?: string;
    ppOrderDetailId?: string;
    cashOutExchangeRateFB?: number;
    cashOutAmountFB?: number;
    cashOutDifferAmountFB?: number;
    cashOutDifferAccountFB?: string;
    cashOutExchangeRateMB?: number;
    cashOutAmountMB?: number;
    cashOutDifferAmountMB?: number;
    cashOutDifferAccountMB?: string;
    cashOutVATAmountFB?: number;
    cashOutDifferVATAmountFB?: number;
    cashOutVATAmountMB?: number;
    cashOutDifferVATAmountMB?: number;
    orderPriority?: number;
    isSelected?: boolean;
    accountingObjectHd?: any;
    unit?: any;
    debitAccountItem?: any;
    creditAccountItem?: IAccountList;
    importTaxAccountItem?: any;
    specialConsumeTaxAccountItem?: any;
    vatAccountItem?: any;
    deductionDebitAccountItem?: any;
    goodsServicePurchase?: any;
    invoiceTemplate?: string;
    vatDescription?: string;
    expenseItem?: any;
    costSetItem?: any;
    emContractItem?: any;
    budgetItem?: any;
    organizationUnitItem?: any;
    statisticsCodeItem?: any;
    units?: any;
    mainUnitName?: string;
    unitPrices?: any[];
    ppOrderNo?: any;
    materialGoodsCode?: string;
    quantityFromDB?: number;
    newPriority?: number;
    materialGoodsSpecificationsLedgers?: any[];
}

export class PPInvoiceDetails implements IPPInvoiceDetails {
    constructor(
        public id?: string,
        public ppInvoiceId?: string,
        public materialGoodsId?: string,
        public materialGood?: any,
        public repositoryId?: string,
        public repository?: any,
        public description?: string,
        public debitAccount?: string,
        public creditAccount?: string,
        public unitId?: string,
        public quantity?: number,
        public unitPrice?: number,
        public unitPriceOriginal?: number,
        public amount?: number,
        public amountOriginal?: number,
        public discountRate?: number,
        public discountAmount?: number,
        public discountAmountOriginal?: number,
        public inwardAmount?: number,
        public inwardAmountOriginal?: number,
        public freightAmount?: number,
        public freightAmountOriginal?: number,
        public importTaxExpenseAmount?: number,
        public importTaxExpenseAmountOriginal?: number,
        public expiryDate?: Moment,
        public lotNo?: string,
        public customUnitPrice?: number,
        public vatRate?: number,
        public vatAmount?: number,
        public vatAmountOriginal?: number,
        public vatAccount?: string,
        public deductionDebitAccount?: string,
        public mainUnitId?: string,
        public mainQuantity?: number,
        public mainUnitPrice?: number,
        public mainConvertRate?: number,
        public formula?: string,
        public importTaxRate?: number,
        public importTaxAmount?: number,
        public importTaxAmountOriginal?: number,
        public importTaxAccount?: string,
        public specialConsumeTaxRate?: number,
        public specialConsumeTaxAmount?: number,
        public specialConsumeTaxAmountOriginal?: number,
        public specialConsumeTaxAccount?: string,
        public invoiceType?: string,
        public invoiceDate?: Moment,
        public invoiceNo?: string,
        public invoiceSeries?: string,
        public goodsServicePurchaseId?: string,
        public accountingObjectId?: string,
        public budgetItemId?: string,
        public costSetId?: string,
        public contractId?: string,
        public statisticCodeId?: string,
        public departmentId?: string,
        public expenseItemId?: string,
        public ppOrderId?: string,
        public ppOrderDetailId?: string,
        public cashOutExchangeRateFB?: number,
        public cashOutAmountFB?: number,
        public cashOutDifferAmountFB?: number,
        public cashOutDifferAccountFB?: string,
        public cashOutExchangeRateMB?: number,
        public cashOutAmountMB?: number,
        public cashOutDifferAmountMB?: number,
        public cashOutDifferAccountMB?: string,
        public cashOutVATAmountFB?: number,
        public cashOutDifferVATAmountFB?: number,
        public cashOutVATAmountMB?: number,
        public cashOutDifferVATAmountMB?: number,
        public orderPriority?: number,
        public isSelected?: boolean,
        public accountingObjectHd?: any,
        public unit?: any,
        public debitAccountItem?: any,
        public creditAccountItem?: IAccountList,
        public importTaxAccountItem?: any,
        public specialConsumeTaxAccountItem?: any,
        public vatAccountItem?: any,
        public deductionDebitAccountItem?: any,
        public goodsServicePurchase?: any,
        public invoiceTemplate?: string,
        public vatDescription?: string,
        public expenseItem?: any,
        public costSetItem?: any,
        public emContractItem?: any,
        public budgetItem?: any,
        public organizationUnitItem?: any,
        public statisticsCodeItem?: any,
        public units?: any,
        public mainUnitName?: string,
        public unitPrices?: any[],
        public ppOrderNo?: any,
        public materialGoodsCode?: string,
        public quantityFromDB?: number,
        public newPriority?: number,
        public materialGoodsSpecificationsLedgers?: any[]
    ) {
        this.isSelected = this.isSelected || false;
    }
}
