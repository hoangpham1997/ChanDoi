import { Moment } from 'moment';
import * as moment from 'moment';

export interface IViewSAQuoteDTO {
    id?: string;
    sAQuoteDetailID?: string;
    typeID?: number;
    typeGroupID?: number;
    companyID?: string;
    typeLedger?: number;
    no?: string;
    date?: Moment;
    materialGoodsID?: string;
    materialGoodsCode?: string;
    description?: string;
    quantity?: number;
    unitPrice?: number;
    amount?: number;
    checked?: boolean;

    unitID?: string;
    unitPriceOriginal?: number;
    mainQuantity?: number;
    mainUnitID?: string;
    mainUnitPrice?: number;
    mainConvertRate?: number;
    formula?: string;
    vATDescription?: string;
    amountOriginal?: number;
    discountRate?: number;
    discountAmount?: number;
    discountAmountOriginal?: number;
    vATRate?: number;
    vATAmount?: number;
    vATAmountOriginal?: number;
    accountingObjectID?: string;
    accountingObjectName?: string;
    accountingObjectAddress?: string;
    companyTaxCode?: string;
    contactName?: string;
    descriptionParent?: string;
    employeeID?: string;
    deliveryTime?: Moment;
}

export class ViewSAQuoteDTO implements IViewSAQuoteDTO {
    constructor(
        public id?: string,
        public sAQuoteDetailID?: string,
        public typeID?: number,
        public typeGroupID?: number,
        public companyID?: string,
        public typeLedger?: number,
        public no?: string,
        public date?: Moment,
        public materialGoodsID?: string,
        public materialGoodsCode?: string,
        public description?: string,
        public quantity?: number,
        public unitPrice?: number,
        public amount?: number,
        public checked?: boolean,
        public unitID?: string,
        public unitPriceOriginal?: number,
        public mainQuantity?: number,
        public mainUnitID?: string,
        public mainUnitPrice?: number,
        public mainConvertRate?: number,
        public formula?: string,
        public vATDescription?: string,
        public amountOriginal?: number,
        public discountRate?: number,
        public discountAmount?: number,
        public discountAmountOriginal?: number,
        public vATRate?: number,
        public vATAmount?: number,
        public vATAmountOriginal?: number,
        public accountingObjectID?: string,
        public accountingObjectName?: string,
        public accountingObjectAddress?: string,
        public companyTaxCode?: string,
        public contactName?: string,
        public descriptionParent?: string,
        public employeeID?: string,
        public deliveryTime?: Moment
    ) {}
}
