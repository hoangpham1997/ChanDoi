export interface IGenCode {
    id?: number;
    companyID?: string;
    branchID?: string;
    displayOnBook?: number;
    typeGroupID?: number;
    typeGroupName?: string;
    prefix?: string;
    currentValue?: number;
    suffix?: string;
    length?: number;
    numberDisplay?: string;
}

export class GenCode implements IGenCode {
    constructor(
        public id?: number,
        public companyID?: string,
        public branchID?: string,
        public displayOnBook?: number,
        public typeGroupID?: number,
        public typeGroupName?: string,
        public prefix?: string,
        public currentValue?: number,
        public suffix?: string,
        public length?: number,
        numberDisplay?: string
    ) {}
}
