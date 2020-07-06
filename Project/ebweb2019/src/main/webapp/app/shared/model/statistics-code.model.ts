export interface IStatisticsCode {
    id?: string;
    statisticsCode?: string;
    statisticsCodeName?: string;
    description?: string;
    parentID?: string;
    isParentNode?: boolean;
    // parentNode?: boolean;
    orderFixCode?: string;
    grade?: number;
    isActive?: boolean;
    // active?: boolean;
    isSecurity?: boolean;
    children?: IStatisticsCode[];
    checked?: boolean;
    // isCheck?: boolean;
}

export class StatisticsCode implements IStatisticsCode {
    constructor(
        public id?: string,
        public statisticsCode?: string,
        public statisticsCodeName?: string,
        public description?: string,
        public parentID?: string,
        public isParentNode?: boolean,
        // public parentNode?: boolean,
        public orderFixCode?: string,
        public grade?: number,
        public isActive?: boolean,
        // public active?: boolean,
        public isSecurity?: boolean,
        public checked?: boolean,
        public children?: IStatisticsCode[] // public isCheck?: boolean
    ) {
        this.isParentNode = this.isParentNode || false;
        this.isActive = this.isActive || true;
        this.isSecurity = this.isSecurity || false;
        this.checked = this.checked || false;
        // this.isCheck = this.isCheck || false;
    }
}
