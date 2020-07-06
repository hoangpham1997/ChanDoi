export interface IContractState {
    id?: string;
    // contractStateCode?: string;
    contractStateName?: string;
    description?: string;
    isSecurity?: boolean;
}

export class ContractState implements IContractState {
    constructor(
        public id?: string,
        // public contractStateCode?: string,
        public contractStateName?: string,
        public description?: string,
        public isSecurity?: boolean
    ) {
        this.isSecurity = this.isSecurity || false;
    }
}
