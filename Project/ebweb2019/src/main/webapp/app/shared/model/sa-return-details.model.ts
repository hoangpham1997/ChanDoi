import { Moment } from 'moment';
import { IMaterialGoods } from 'app/shared/model//material-goods.model';
import { IUnit } from 'app/shared/model//unit.model';
import { IRepository } from 'app/shared/model//repository.model';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';
import { EMContract, IEMContract } from 'app/shared/model/em-contract.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IOrganizationUnit, OrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { ISaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';
// import { ISaInvoice } from 'app/shared/model/sa-invoice.model';
// import { ISaInvoiceDetails } from 'app/shared/model/sa-invoice-details.model';

export interface ISaReturnDetails {
    id?: string;
    saReturnID?: string;
    isPromotion?: boolean;
    description?: string;
    debitAccount?: string;
    creditAccount?: string;
    quantity?: number;
    unitPrice?: number;
    unitPriceOriginal?: number;
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
    vatRate?: number;
    vatAmount?: number;
    vatAmountOriginal?: number;
    vatAccount?: string;
    deductionDebitAccount?: string;
    owPrice?: number;
    owAmount?: number;
    costAccount?: string;
    repositoryAccount?: string;
    expiryDate?: Moment;
    lotNo?: string;
    department?: IOrganizationUnit;
    departmentID?: string;
    expenseItem?: IExpenseItem;
    expenseItemID?: string;
    budgetItem?: IBudgetItem;
    budgetItemID?: string;
    costSet?: ICostSet;
    costSetID?: string;
    contract?: EMContract;
    contractID?: string;
    statisticsCode?: IStatisticsCode;
    statisticsCodeID?: string;
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
    vatDescription?: string;
    materialGoods?: IMaterialGoods;
    materialGoodsID?: string;
    unit?: IUnit;
    unitID?: string;
    repository?: IRepository;
    repositoryID?: string;
    mainUnit?: IUnit;
    mainUnitID?: string;
    accountingObject?: IAccountingObject;
    accountingObjectID?: string;
    units?: any;
    organizationUnitID?: string;
    saBillDetailID?: string;
    saBillID?: string;
    saInvoiceID?: string;
    date?: any;
    saInvoiceDetailsID?: string;
    totalQuantity?: number;
    isLotNoReadOnly?: boolean;
    lotNoObject?: any;
    saleDiscountPolicys?: ISaleDiscountPolicy[];
    careerGroupID?: string;
    noFBook?: string;
    materialGoodsSpecificationsLedgers?: any[];
}

export class SaReturnDetails implements ISaReturnDetails {
    constructor(
        public id?: string,
        public saReturnID?: string,
        public isPromotion?: boolean,
        public description?: string,
        public debitAccount?: string,
        public creditAccount?: string,
        public quantity?: number,
        public unitPrice?: number,
        public unitPriceOriginal?: number,
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
        public vatRate?: number,
        public vatAmount?: number,
        public vatAmountOriginal?: number,
        public owPrice?: number,
        public owAmount?: number,
        public costAccount?: string,
        public repositoryAccount?: string,
        public expiryDate?: Moment,
        public lotNo?: string,
        public department?: IOrganizationUnit,
        public departmentID?: string,
        public expenseItem?: IExpenseItem,
        public expenseItemID?: string,
        public budgetItem?: IBudgetItem,
        public budgetItemID?: string,
        public costSet?: ICostSet,
        public costSetID?: string,
        public contract?: EMContract,
        public contractID?: string,
        public statisticsCode?: IStatisticsCode,
        public statisticsCodeID?: string,
        public organizationUnitID?: string,
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
        public vatDescription?: string,
        public materialGoods?: IMaterialGoods,
        public materialGoodsID?: string,
        public unit?: IUnit,
        public unitID?: string,
        public repository?: IRepository,
        public repositoryID?: string,
        public mainUnit?: IUnit,
        public mainUnitID?: string,
        public accountingObject?: IAccountingObject,
        public accountingObjectID?: string,
        public units?: any,
        public vatAccount?: string,
        public saBillDetailID?: string,
        public saBillID?: string,
        public saInvoiceDetailID?: string,
        public saInvoiceID?: string,
        public date?: any,
        public totalQuantity?: number,
        public lotNoObject?: any,
        public isLotNoReadOnly?: boolean,
        public deductionDebitAccount?: string,
        public saleDiscountPolicys?: ISaleDiscountPolicy[],
        public careerGroupID?: string,
        public noFBook?: string,
        public materialGoodsSpecificationsLedgers?: any[]
    ) {
        this.isPromotion = this.isPromotion || false;
    }
}
