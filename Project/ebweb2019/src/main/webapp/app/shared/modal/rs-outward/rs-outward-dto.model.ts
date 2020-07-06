import { Moment } from 'moment';

export interface IRSOutWardDTO {
    id?: string;
    rSInwardOutwardDetailID?: string;
    typeID?: number;
    typeGroupID?: number;
    companyID?: string;
    typeLedger?: number;
    no?: string;
    date?: Moment;
    materialGoodsID?: string;
    materialGoodsCode?: string;
    reason?: string;
    description?: string;
    quantity?: number;
    checked?: boolean;
    unitID?: string;
    mainQuantity?: number;
    mainUnitID?: string;
    mainUnitPrice?: number;
    mainConvertRate?: number;
    formula?: string;
    creditAccount?: string;
    debitAccount?: string;
    accountingObjectID?: string;
    accountingObjectName?: string;
    accountingObjectAddress?: string;
    contactName?: string;
    employeeID?: string;
}

export class RSOutWardDTO implements IRSOutWardDTO {
    constructor(
        public id?: string,
        public rSInwardOutwardDetailID?: string,
        public typeID?: number,
        public typeGroupID?: number,
        public companyID?: string,
        public typeLedger?: number,
        public no?: string,
        public date?: Moment,
        public materialGoodsID?: string,
        public materialGoodsCode?: string,
        public reason?: string,
        public description?: string,
        public quantity?: number,
        public checked?: boolean,
        public unitID?: string,
        public mainQuantity?: number,
        public mainUnitID?: string,
        public mainUnitPrice?: number,
        public mainConvertRate?: number,
        public formula?: string,
        public creditAccount?: string,
        public debitAccount?: string,
        public accountingObjectID?: string,
        public accountingObjectName?: string,
        public accountingObjectAddress?: string,
        public contactName?: string,
        public employeeID?: string
    ) {}
}
