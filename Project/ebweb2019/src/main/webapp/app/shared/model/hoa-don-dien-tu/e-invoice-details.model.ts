import { Moment } from 'moment';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IMCAuditDetails } from 'app/shared/model/mc-audit-details.model';
import { IMCAuditDetailMember } from 'app/shared/model/mc-audit-detail-member.model';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';

export interface IEInvoiceDetails {
    id?: string;
    eInvoiceID?: string;
    materialGoodsID?: string;
    materialGoodsCode?: string;
    description?: string; // materialGoodsName
    isPromotion?: boolean; // hàng khuyến mại
    unitID?: string;
    unitName?: string;
    quantity?: number;
    unitPrice?: number;
    unitPriceOriginal?: number;
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
    vATRate?: number;
    vATAmount?: number;
    vATAmountOriginal?: number;
    lotNo?: string;
    expiryDate?: Moment;
    orderPriority?: number;
}

export class EInvoiceDetails implements IEInvoiceDetails {
    constructor(
        public id?: string,
        public eInvoiceID?: string,
        public materialGoodsID?: string,
        public materialGoodsCode?: string,
        public description?: string, // materialGoodsName
        public isPromotion?: boolean, // hàng khuyến mại
        public unitID?: string,
        public unitName?: string,
        public quantity?: number,
        public unitPrice?: number,
        public unitPriceOriginal?: number,
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
        public vATRate?: number,
        public vATAmount?: number,
        public vATAmountOriginal?: number,
        public lotNo?: string,
        public expiryDate?: Moment,
        public orderPriority?: number
    ) {}
}
