import { Moment } from 'moment';

export class ViewVoucherNo {
    constructor(
        public id?: string,
        public refID?: string,
        public typeID?: number,
        public typeName?: string,
        public typeGroupName?: string,
        public invoiceNo?: string,
        public date?: Moment,
        public postedDate?: Moment,
        public postedDateChange?: Moment,
        public noFBook?: string,
        public noMBook?: string,
        public noNew?: string,
        public reason?: string,
        public totalAmount?: number,
        public totalAmountOriginal?: number,
        public checked1?: boolean,
        public checked2?: boolean,
        public checked3?: boolean,
        public choseFuntion?: number,
        public mCReceiptID?: string,
        public mBDepositID?: string,
        public mBCreditCardID?: string,
        public mBTellerPaperID?: string,
        public paymentVoucherID?: string,
        public rSInwardOutwardID?: string,
        public rSTransferID?: string,
        public bankCode?: string,
        public bankName?: string,
        public reasonFail?: string, // dùng cho đưa ra kết quả không thành công
        public accountingObjectCode?: string,
        public accountingObjectName?: string,
        public materialGoodsCode?: string,
        public materialGoodsName?: string,
        public creditCardNumber?: string,
        public unitName?: string,
        public repositoryCode?: string,
        public bankAccount?: string,
        public recorded?: boolean
    ) {}
}
