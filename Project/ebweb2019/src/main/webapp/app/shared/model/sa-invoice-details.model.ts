import { Moment } from 'moment';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IMaterialGoods, MaterialGoods } from 'app/shared/model/material-goods.model';
import { IUnit } from 'app/shared/model/unit.model';
import { ExpenseItem } from 'app/shared/model/expense-item.model';
import { CostSet } from 'app/shared/model/cost-set.model';
import { EMContract } from 'app/shared/model/em-contract.model';
import { BudgetItem } from 'app/shared/model/budget-item.model';
import { OrganizationUnit } from 'app/shared/model/organization-unit.model';
import { StatisticsCode } from 'app/shared/model/statistics-code.model';
import { Repository } from 'app/shared/model/repository.model';
import { IViewLotNo } from 'app/shared/model/view-lotno.model';
import { ISaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';

export interface ISAInvoiceDetails {
    id?: number;
    sAInvoiceId?: string;
    materialGoodsID?: string;
    isPromotion?: boolean;
    repositoryID?: string;
    description?: string;
    debitAccount?: string;
    creditAccount?: string;
    unitID?: string;
    quantity?: number;
    unitPrice?: number;
    unitPriceOriginal?: number;
    unitPriceOriginals?: any[];
    mainUnitID?: string;
    mainQuantity?: number;
    mainUnitPrice?: number;
    mainConvertRate?: number;
    formula?: string;
    amount?: number;
    amountOriginal?: number;
    discountRate?: number;
    discountAmount?: number;
    discountAmountOriginal?: number;
    discountAccount?: string;
    vATRate?: number;
    vATAmount?: number;
    vATAmountOriginal?: number;
    vATAccount?: string;
    vATDescription?: string;
    repositoryAccount?: string;
    costAccount?: string;
    oWPrice?: number;
    oWAmount?: number;
    expiryDate?: any;
    lotNo?: string;
    waranty?: string;
    accountingObjectID?: string;
    exportTaxPrice?: number;
    exportTaxTaxRate?: number;
    exportTaxAmount?: number;
    exportTaxAmountAccount?: string;
    exportTaxAccountCorresponding?: string;
    careerGroupID?: string;
    departmentID?: string;
    expenseItemID?: string;
    budgetItemID?: string;
    costSetID?: string;
    statisticsCodeID?: string;
    contractID?: string;
    contractDetailID?: string;
    rSInwardOutwardID?: string;
    rSInwardOutwardDetailID?: string;
    sAQuoteID?: string;
    sAQuoteDetailID?: string;
    sAOrderID?: string;
    sAOrderDetailID?: string;
    sABillID?: string;
    sABillDetailID?: string;
    pPInvoiceID?: string;
    pPInvoiceDetailID?: string;
    rSTransferID?: string;
    rSTransferDetailID?: string;
    orderPriority?: number;
    debitAccountItem?: any;
    creditAccountItem?: IAccountList;
    vatAccountItem?: any;
    mainUnitName?: any;
    mainUnit?: IUnit;
    units?: IUnit[];
    unit?: IUnit;
    accountingObject?: any;
    materialGoods?: IMaterialGoods;
    sAOrderNo?: string;
    deductionDebitAccount?: string;
    lotNos?: IViewLotNo[];
    confrontID?: string;
    confrontDetailID?: string;
    saleDiscountPolicys?: ISaleDiscountPolicy[];
    materialGoodsSpecificationsLedgers?: any[];
}

export class SAInvoiceDetails implements ISAInvoiceDetails {
    constructor(
        public id?: number,
        public sAInvoiceId?: string,
        public materialGoodsID?: string,
        public isPromotion?: boolean,
        public repositoryID?: string,
        public description?: string,
        public debitAccount?: string,
        public creditAccount?: string,
        public unitID?: string,
        public quantity?: number,
        public unitPrice?: number,
        public unitPriceOriginal?: number,
        public unitPriceOriginals?: any[],
        public mainUnitID?: string,
        public mainQuantity?: number,
        public mainUnitPrice?: number,
        public mainConvertRate?: number,
        public formula?: string,
        public amount?: number,
        public amountOriginal?: number,
        public discountRate?: number,
        public discountAmount?: number,
        public discountAmountOriginal?: number,
        public discountAccount?: string,
        public vATRate?: number,
        public vATAmount?: number,
        public vATAmountOriginal?: number,
        public vATAccount?: string,
        public vATDescription?: string,
        public repositoryAccount?: string,
        public costAccount?: string,
        public oWPrice?: number,
        public oWAmount?: number,
        public expiryDate?: any,
        public lotNo?: string,
        public waranty?: string,
        public accountingObjectID?: string,
        public exportTaxPrice?: number,
        public exportTaxTaxRate?: number,
        public exportTaxAmount?: number,
        public exportTaxAmountAccount?: string,
        public exportTaxAccountCorresponding?: string,
        public careerGroupID?: string,
        public departmentID?: string,
        public expenseItemID?: string,
        public budgetItemID?: string,
        public costSetID?: string,
        public statisticsCodeID?: string,
        public contractID?: string,
        public contractDetailID?: string,
        public rSInwardOutwardID?: string,
        public rSInwardOutwardDetailID?: string,
        public sAQuoteID?: string,
        public sAQuoteDetailID?: string,
        public sAOrderID?: string,
        public sAOrderDetailID?: string,
        public sABillID?: string,
        public sABillDetailID?: string,
        public pPInvoiceID?: string,
        public pPInvoiceDetailID?: string,
        public rSTransferID?: string,
        public rSTransferDetailID?: string,
        public orderPriority?: number,
        public debitAccountItem?: any,
        public creditAccountItem?: IAccountList,
        public vatAccountItem?: any,
        public mainUnitName?: any,
        public mainUnit?: any,
        public units?: any,
        public unit?: any,
        public accountingObject?: any,
        public materialGoods?: IMaterialGoods,
        public sAOrderNo?: string,
        public deductionDebitAccount?: string,
        public lotNos?: IViewLotNo[],
        public confrontID?: string,
        public confrontDetailID?: string,
        public saleDiscountPolicys?: ISaleDiscountPolicy[],
        public materialGoodsSpecificationsLedgers?: any[]
    ) {
        this.isPromotion = this.isPromotion || false;
    }
}
