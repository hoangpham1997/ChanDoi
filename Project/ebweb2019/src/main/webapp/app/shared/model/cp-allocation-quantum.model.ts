export interface ICPAllocationQuantum {
    id?: string;
    companyID?: string;
    branchID?: string;
    objectCode?: string;
    objectName?: string;
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
    objectID?: string;
}

export class CPAllocationQuantum implements ICPAllocationQuantum {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public objectCode?: string,
        public objectName?: string,
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
        public totalCostAmount?: number,
        public objectID?: string
    ) {}
}
