export interface ICPUncompleteDetails {
    id?: string;
    cPUncompleteID?: string;
    cPPeriodID?: string;
    costSetID?: string;
    directMatetialAmount?: number;
    directLaborAmount?: number;
    machineMatetialAmount?: number;
    machineLaborAmount?: number;
    machineToolsAmount?: number;
    machineDepreciationAmount?: number;
    machineServiceAmount?: number;
    machineGeneralAmount?: number;
    generalMatetialAmount?: number;
    generalLaborAmount?: number;
    generalToolsAmount?: number;
    generalDepreciationAmount?: number;
    generalServiceAmount?: number;
    otherGeneralAmount?: number;
    totalCostAmount?: number;
    quantity?: number;
}

export class CPUncompleteDetails implements ICPUncompleteDetails {
    constructor(
        public id?: string,
        public cPUncompleteID?: string,
        public cPPeriodID?: string,
        public costSetID?: string,
        public directMatetialAmount?: number,
        public directLaborAmount?: number,
        public machineMatetialAmount?: number,
        public machineLaborAmount?: number,
        public machineToolsAmount?: number,
        public machineDepreciationAmount?: number,
        public machineServiceAmount?: number,
        public machineGeneralAmount?: number,
        public generalMatetialAmount?: number,
        public generalLaborAmount?: number,
        public generalToolsAmount?: number,
        public generalDepreciationAmount?: number,
        public generalServiceAmount?: number,
        public otherGeneralAmount?: number,
        public totalCostAmount?: number,
        public quantity?: number
    ) {}
}
