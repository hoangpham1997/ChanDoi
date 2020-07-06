import { IFixedAsset } from 'app/shared/model//fixed-asset.model';
import { IExpenseItem } from 'app/shared/model//expense-item.model';
import { IAccountList } from 'app/shared/model//account-list.model';
import { IStatisticsCode } from 'app/shared/model//statistics-code.model';
import { ICostSet } from 'app/shared/model/cost-set.model';

export interface IFixedAssetAllocation {
    id?: string;

    objectType?: number;
    rate?: number;
    orderPriority?: number;
    fixedassetID?: string;
    expenseItem?: IExpenseItem;
    costAccount?: IAccountList;
    statisticsCode?: IStatisticsCode;
    objectID?: ICostSet;
}

export class FixedAssetAllocation implements IFixedAssetAllocation {
    constructor(
        public id?: string,
        public objectType?: number,
        public rate?: number,
        public orderPriority?: number,
        public fixedassetID?: string,
        public expenseItem?: IExpenseItem,
        public costAccount?: IAccountList,
        public statisticsCode?: IStatisticsCode,
        public objectID?: ICostSet
    ) {}
}
