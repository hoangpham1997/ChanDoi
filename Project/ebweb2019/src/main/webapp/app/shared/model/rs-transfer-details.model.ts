import { Moment } from 'moment';
import { IRepository } from 'app/shared/model/repository.model';
import { IMaterialGoods, IMaterialGoodsInStock } from 'app/shared/model/material-goods.model';
import { IUnit, Unit } from 'app/shared/model/unit.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { EMContract } from 'app/shared/model/em-contract.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IViewLotNo } from 'app/shared/model/view-lotno.model';
import { IPPDiscountReturn } from 'app/shared/model/pp-discount-return.model';
import { IPPDiscountReturnDetails } from 'app/shared/model/pp-discount-return-details.model';
import { ISAOrder } from 'app/shared/model/sa-order.model';
import { ISAOrderDetails } from 'app/shared/model/sa-order-details.model';
import { ISAInvoice } from 'app/shared/model/sa-invoice.model';
import { ISAInvoiceDetails } from 'app/shared/model/sa-invoice-details.model';

export interface IRSTransferDetails {
    id?: number;
    rsInwardOutwardID?: string;
    materialGoodsID?: string;
    description?: string;
    repositoryID?: string;
    debitAccount?: string;
    creditAccount?: string;
    unitID?: string;
    quantity?: number;
    unitPrice?: number;
    unitPriceOriginal?: number;
    mainUnitID?: string;
    accountingObjectAddress?: string;
    mainQuantity?: number;
    mainUnitPrice?: number;
    mainConvertRate?: number;
    formula?: string;
    amount?: number;
    amountOriginal?: number;
    owPurpose?: number;
    expiryDate?: any;
    materialGoodLotNo?: IMaterialGoods;
    sAOrderDetailID?: string;
    lotNo?: string;
    ppOrderID?: string;
    ppOrderDetailsID?: string;
    saInvoiceDetailID?: number;
    saOrderDetailID?: string;
    ppDiscountReturn?: IPPDiscountReturn;
    ppDiscountReturnDetails?: IPPDiscountReturnDetails;
    saOrder?: ISAOrder;
    saOrderDetail?: ISAOrderDetails;
    saInvoice?: ISAInvoice;
    saInvoiceDetail?: ISAInvoiceDetails;
    saOrderID?: string;
    rsAssemblyDismantlementID?: string;
    rsAssemblyDismantlementDetailID?: string;
    rsProductionOrderID?: string;
    rsProductionOrderDetailID?: string;
    costSetID?: string;
    contractID?: string;
    employeeID?: string;
    statisticsCodeID?: string;
    departmentID?: string;
    expenseItemID?: string;
    budgetItemID?: string;
    detailID?: string;
    orderPriority?: number;
    lotNoArray?: IViewLotNo[];
    noBookPPDiscountReturn?: string;
    noBookSaOrder?: string;
    noBookSaInvoice?: string;
    confrontID?: string;
    confrontDetailID?: string;
    owPrice?: number;
    owAmount?: number;
    fromRepositoryID?: string;
    toRepositoryID?: string;
    materialGoodsCode?: string;
    unitName?: string;
    fromRepositoryCode?: string;
    toRepositoryCode?: string;
    expenseItemCode?: string;
    costSetCode?: string;
    budgetItemCode?: string;
    organizationUnitCode?: string;
    statisticsCode?: string;
    units?: IUnit[];
}

export class RSTransferDetails implements IRSTransferDetails {
    constructor(
        public id?: number,
        public rsInwardOutwardID?: string,
        public materialGoodsID?: string,
        public description?: string,
        public repositoryID?: string,
        public debitAccount?: string,
        public creditAccount?: string,
        public unitID?: string,
        public quantity?: number,
        public quantityFromDB?: number,
        public unitPrice?: number,
        public unitPriceOriginal?: number,
        public oWPrice?: number,
        public oWAmount?: number,
        public mainUnitID?: string,
        public accountingObjectAddress?: string,
        public mainQuantity?: number,
        public mainUnitPrice?: number,
        public mainConvertRate?: number,
        public formula?: string,
        public amount?: number,
        public amountOriginal?: number,
        public owPurpose?: number,
        public materialGoodLotNo?: IMaterialGoods,
        public expiryDate?: any,
        public lotNo?: string,
        public ppOrderID?: string,
        public ppOrderDetailId?: string,
        public saReturnID?: string,
        public materialQuantumID?: string,
        public materialQuantumDetailsID?: string,
        public saInvoiceID?: string,
        public saReturnDetailsID?: string,
        public saInvoiceDetailID?: number,
        public saOrderDetailID?: string,
        public saOrderID?: string,
        public sAOrderDetailID?: string,
        public units?: IUnit[],
        public ppDiscountReturn?: IPPDiscountReturn,
        public ppDiscountReturnDetail?: IPPDiscountReturnDetails,
        public saOrder?: ISAOrder,
        public saOrderDetail?: ISAOrderDetails,
        public saInvoice?: ISAInvoice,
        public saInvoiceDetail?: ISAInvoiceDetails,
        public rsAssemblyDismantlementID?: string,
        public rsAssemblyDismantlementDetailID?: string,
        public rsProductionOrderID?: string,
        public rsProductionOrderDetailID?: string,
        public costSetID?: string,
        public contractID?: string,
        public employeeID?: string,
        public statisticsCodeID?: string,
        public departmentID?: string,
        public expenseItemID?: string,
        public budgetItemID?: string,
        public detailID?: string,
        public repository?: IRepository,
        public unit?: Unit,
        public department?: IOrganizationUnit,
        public expenseItem?: IExpenseItem,
        public budgetItem?: IBudgetItem,
        public costSet?: ICostSet,
        public contract?: EMContract,
        public statisticCode?: IStatisticsCode,
        public mainUnit?: Unit,
        public unitId?: string,
        public mainUnitId?: string,
        public convertRates?: any,
        public unitPrices?: any[],
        public quantityReceipt?: number,
        public materialGood?: IMaterialGoodsInStock,
        public orderPriority?: number,
        public lotNoArray?: IViewLotNo[],
        public confrontID?: string,
        public confrontDetailID?: string,
        public fromRepositoryID?: string,
        public toRepositoryID?: string,
        public materialGoodsCode?: string,
        public unitName?: string,
        public fromRepositoryCode?: string,
        public toRepositoryCode?: string,
        public expenseItemCode?: string,
        public costSetCode?: string,
        public budgetItemCode?: string,
        public organizationUnitCode?: string,
        public statisticsCode?: string
    ) {}
}
