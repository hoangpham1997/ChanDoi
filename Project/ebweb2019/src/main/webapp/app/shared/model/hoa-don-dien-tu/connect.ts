import { ISupplier } from 'app/shared/model/hoa-don-dien-tu/supplier';

export interface IConnectEInvoice {
    path?: string;
    supplierCode?: string;
    userName?: string;
    password?: string;
    useInvoceWait?: boolean;
    useToken?: boolean;
    suppliers?: ISupplier[];
}

export class ConnectEInvoice implements IConnectEInvoice {
    constructor(
        public path?: string,
        public userName?: string,
        public password?: string,
        public useInvoceWait?: boolean,
        public useToken?: boolean,
        public suppliers?: ISupplier[]
    ) {}
}
