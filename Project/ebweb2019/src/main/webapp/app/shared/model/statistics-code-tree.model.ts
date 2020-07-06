import { IStatisticsCode } from 'app/shared/model/statistics-code.model';

export interface ITreeStatisticsCode {
    parent?: IStatisticsCode;
    index?: number;
    select?: boolean;
    check?: boolean;
    children?: ITreeStatisticsCode[];
}
export class TreeStatisticsCode implements ITreeStatisticsCode {
    constructor(
        public parent?: IStatisticsCode,
        public index?: number,
        public select?: boolean,
        public check?: boolean,
        public children?: ITreeStatisticsCode[]
    ) {
        this.select = this.select || false;
        this.check = this.check || false;
    }
}
//import { IAccountList } from 'app/shared/model/account-list.model';
//
// export interface ITreeAccountList {
//     parent?: IAccountList;
//     index?: number;
//     select?: boolean;
//     check?: boolean;
//     children?: ITreeAccountList[];
// }
//
// export class TreeAccountList implements ITreeAccountList {
//     constructor(
//         public parent?: IAccountList,
//         public index?: number,
//         public select?: boolean,
//         public check?: boolean,
//         public children?: ITreeAccountList[]
//     ) {
//         this.select = this.select || false;
//         this.check = this.check || false;
//     }
// }
