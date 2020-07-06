import * as moment from 'moment';
import { Moment } from 'moment';

export interface ICPOPN {
    id?: string;
    companyID?: string;
    branchID?: string;
    typeLedger?: number;
    objectType?: number;
    costSetID?: string;
    costSetCode?: string;
    costSetName?: string;
    contractID?: string;
    contractCode?: string;
    signedDate?: Moment;
    accountingObjectID?: string;
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
    acceptedAmount?: number;
    notAcceptedAmount?: number;
    uncompletedAccount?: string;
    objectID?: string;
}

export class CPOPN implements ICPOPN {
    constructor(
        public id?: string,
        public companyID?: string,
        public branchID?: string,
        public typeLedger?: number,
        public objectType?: number,
        public costSetID?: string,
        public costSetCode?: string,
        public costSetName?: string,
        public contractID?: string,
        public contractCode?: string,
        public signedDate?: Moment,
        public accountingObjectID?: string,
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
        public acceptedAmount?: number,
        public notAcceptedAmount?: number,
        public uncompletedAccount?: string,
        public objectID?: string
    ) {}
}
