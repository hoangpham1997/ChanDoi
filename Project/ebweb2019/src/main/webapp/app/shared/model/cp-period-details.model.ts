export interface ICPPeriodDetails {
    id?: string;
    cPPeriodID?: string;
    costSetID?: string;
    contractID?: string;
}

export class CPPeriodDetails implements ICPPeriodDetails {
    constructor(public id?: string, public cPPeriodID?: string, public costSetID?: string, public contractID?: string) {}
}
