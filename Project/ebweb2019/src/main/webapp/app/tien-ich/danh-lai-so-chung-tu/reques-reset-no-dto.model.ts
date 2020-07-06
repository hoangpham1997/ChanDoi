import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';

export class RequestResetNoDtoModel {
    voucher?: ViewVoucherNo[];
    fromDate?: string;
    toDate?: string;
    typeGroupID?: number;
    prefix?: string;
    suffix: string;
    currentValue?: string;
}
