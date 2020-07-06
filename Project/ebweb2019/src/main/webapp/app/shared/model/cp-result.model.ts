export interface ICPResult {
    id?: string;
    cPPeriodID?: string;
    cPPeriodDetailID?: string;
    costSetID?: string;
    materialGoodsID?: string;
    coefficien?: number;
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
    totalQuantity?: number;
    unitPrice?: number;
}

export class CPResult implements ICPResult {
    constructor(
        public id?: string,
        public cPPeriodID?: string,
        public cPPeriodDetailID?: string,
        public costSetID?: string,
        public materialGoodsID?: string,
        public coefficien?: number,
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
        public totalQuantity?: number,
        public unitPrice?: number
    ) {}
}
