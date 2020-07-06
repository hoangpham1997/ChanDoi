import { Moment } from 'moment';
import { IMaterialGoods, MaterialGoods } from 'app/shared/model//material-goods.model';
import { IUnit } from 'app/shared/model/unit.model';
import { IViewLotNo } from 'app/shared/model/view-lotno.model';
import { ISaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';

export interface ISaBillDetails {
    id?: string;
    saBillId?: string;
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
    lotNo?: string;
    saInvoiceDetailID?: string;
    saReturnDetailID?: string;
    ppDiscountReturnDetailID?: string;
    saInvoiceID?: string;
    saReturnID?: string;
    ppDiscountReturnID?: string;
    expiryDate?: Moment;
    isPromotion?: boolean;
    orderPriority?: number;
    materialGoods?: MaterialGoods;
    unitPriceOriginals?: any[];
    unitID?: string;
    mainUnitID?: string;
    unit?: IUnit;
    units?: IUnit[];
    mainUnit?: IUnit;
    mainUnits?: IUnit[];
    lotNos?: IViewLotNo[];
    saleDiscountPolicys?: ISaleDiscountPolicy[];
    careerGroupID?: string;
}

export class SaBillDetails implements ISaBillDetails {
    constructor(
        public id?: string,
        public saBillId?: string,
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
        public lotNo?: string,
        public expiryDate?: Moment,
        public isPromotion?: boolean,
        public orderPriority?: number,
        public saInvoiceDetailID?: string,
        public saReturnDetailID?: string,
        public ppDiscountReturnDetailID?: string,
        public saInvoiceID?: string,
        public saReturnID?: string,
        public ppDiscountReturnID?: string,
        public materialGoods?: MaterialGoods,
        public unitPriceOriginals?: any[],
        public unitID?: string,
        public mainUnitID?: string,
        public unit?: IUnit,
        public mainUnit?: IUnit,
        public units?: IUnit[],
        public mainUnits?: IUnit[],
        public lotNos?: IViewLotNo[],
        public saleDiscountPolicys?: ISaleDiscountPolicy[],
        public careerGroupID?: string
    ) {
        this.isPromotion = this.isPromotion || false;
    }
}
