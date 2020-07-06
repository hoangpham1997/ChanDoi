export interface ICPProductQuantum {
    id?: string;
    companyID?: string;
    branchID?: string;
    materialGoodsID?: string;
    materialGoodsCode?: string;
    materialGoodsName?: string;
    unitID?: string;
    typeLedger?: number;
    directMaterialAmount?: number;
    directLaborAmount?: number;
    machineMaterialAmount?: number;
    machineLaborAmount?: number;
    machineToolsAmount?: number;
    machineDepreciationAmount?: number;
    machineServiceAmount?: number;
    machineGeneralAmount?: number;
    generalMaterialAmount?: number;
    generalLaborAmount?: number;
    generalToolsAmount?: number;
    generalDepreciationAmount?: number;
    generalServiceAmount?: number;
    otherGeneralAmount?: number;
    totalCostAmount?: number;
}

export class CPProductQuantum implements ICPProductQuantum {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public materialGoodsID?: string,
        public materialGoodsCode?: string,
        public materialGoodsName?: string,
        public unitID?: string,
        public typeLedger?: number,
        public directMaterialAmount?: number,
        public directLaborAmount?: number,
        public machineMaterialAmount?: number,
        public machineLaborAmount?: number,
        public machineToolsAmount?: number,
        public machineDepreciationAmount?: number,
        public machineServiceAmount?: number,
        public machineGeneralAmount?: number,
        public generalMaterialAmount?: number,
        public generalLaborAmount?: number,
        public generalToolsAmount?: number,
        public generalDepreciationAmount?: number,
        public generalServiceAmount?: number,
        public otherGeneralAmount?: number,
        public totalCostAmount?: number
    ) {}
}
