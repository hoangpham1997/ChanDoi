export interface IRefVoucher {
    id?: string;
    companyID?: string;
    refID1?: string;
    refID2?: string;
    no?: string;
    date?: string;
    postedDate?: string;
    reason?: string;
}

export class RefVoucher implements IRefVoucher {
    constructor(public id?: string, public companyID?: string, public refID1?: string, public refID2?: string) {}
}
