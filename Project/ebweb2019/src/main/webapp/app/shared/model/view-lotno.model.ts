import { Moment } from 'moment';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';

export interface IViewLotNo {
    companyID?: string;
    materialGoodsID?: string;
    lotNo?: string;
    expiryDate?: Moment;
    date?: any;
    totalIWQuantity?: number;
    totalOWQuantity?: number;
    totalQuantityBalance?: number;
}

export class ViewLotNo implements IViewLotNo {
    constructor(
        public companyID?: string,
        public materialGoodsID?: string,
        public lotNo?: string,
        public expiryDate?: Moment,
        public date?: any,
        public totalIWQuantity?: number,
        public totalOWQuantity?: number,
        public totalQuantityBalance?: number
    ) {}
}
