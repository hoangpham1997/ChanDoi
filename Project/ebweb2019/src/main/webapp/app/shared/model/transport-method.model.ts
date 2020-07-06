export interface ITransportMethod {
    id?: string;
    companyId?: string;
    transportMethodCode?: string;
    transportMethodName?: string;
    description?: string;
    isActive?: boolean;
    isSecurity?: boolean;
}

export class TransportMethod implements ITransportMethod {
    constructor(
        public id?: string,
        companyId?: string,
        public transportMethodCode?: string,
        public transportMethodName?: string,
        public description?: string,
        public isActive?: boolean,
        public isSecurity?: boolean
    ) {
        this.isActive = this.isActive || false;
        this.isSecurity = this.isSecurity || false;
    }
}
