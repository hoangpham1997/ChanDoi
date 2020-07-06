import { OpAccountModel } from 'app/shared/model/op-account.model';
import { OpMaterialGoodsModel } from 'app/shared/model/op-material-goods.model';

export interface IAccountList {
    typeId?: any;
    orderPriority?: number;
    id?: string;
    companyID?: string;
    branchID?: string;
    accountingType?: number;
    accountNumber?: string;
    accountName?: string;
    accountNameGlobal?: string;
    description?: string;
    parentAccountID?: string;
    isParentNode?: boolean;
    parentNode?: boolean;
    grade?: number;
    accountGroupKind?: number;
    getAccountGroupKind?: string;
    detailType?: string;
    active?: boolean;
    isActive?: boolean;
    detailByAccountObject?: number;
    isForeignCurrency?: boolean;
    accountGroupID?: string;
    isCheck?: boolean;
    debitAmountOriginal?: number;
    amountOriginal?: number;
    creditAmountOriginal?: number;
    isAccountList?: boolean;
    children?: IAccountList[];
    opAccountDTOList?: OpAccountModel[];
    opMaterialGoodsDTOs?: OpMaterialGoodsModel[];
    count?: number;
    checked?: boolean;
    closingDebitAmount?: number;
    closingCreditAmount?: number;
    closingDebitAmountString?: string;
    closingCreditAmountString?: string;
}

export class AccountList implements IAccountList {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public accountingType?: number,
        public accountNumber?: string,
        public accountName?: string,
        public accountNameGlobal?: string,
        public description?: string,
        public isParentNode?: boolean,
        public parentNode?: boolean,
        public grade?: number,
        public accountGroupKind?: number,
        public getAccountGroupKind?: string,
        public detailType?: string,
        public active?: boolean,
        public isActive?: boolean,
        public detailByAccountObject?: number,
        public isForeignCurrency?: boolean,
        public parentAccountID?: string,
        public accountGroupID?: string,
        public isCheck?: boolean,
        public debitBalance?: number,
        public creditBalance?: number,
        public count?: number,
        public isAccountList?: boolean,
        public children?: IAccountList[],
        public checked?: boolean,
        public closingDebitAmount?: number,
        public closingCreditAmount?: number,
        public closingDebitAmountString?: string,
        public closingCreditAmountString?: string
    ) {
        this.isParentNode = this.isParentNode || false;
        this.isActive = this.isActive || false;
        this.isForeignCurrency = this.isForeignCurrency || false;
        this.isCheck = this.isCheck || false;
    }
}
