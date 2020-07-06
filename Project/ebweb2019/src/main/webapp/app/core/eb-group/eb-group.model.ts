export interface IEbGroup {
    id?: any;
    name?: string;
    companyID?: string;
    code?: string;
    description?: string;
    isSystem?: boolean;
    check?: boolean;
    authorities?: any[];
    users?: any[];
    textOrg?: string;
    listOrg?: any[];
}

export class EbGroup implements IEbGroup {
    constructor(
        public id?: any,
        public name?: string,
        public companyID?: string,
        public code?: string,
        public description?: string,
        public isSystem?: boolean,
        public check?: boolean,
        public authorities?: any[],
        public users?: any[],
        public textOrg?: string,
        public listOrg?: any[]
    ) {
        this.id = id ? id : null;
        this.name = name ? name : null;
        this.code = code ? code : null;
        this.description = description ? description : null;
        this.isSystem = isSystem ? isSystem : null;
        this.check = check ? check : null;
        this.authorities = authorities ? authorities : null;
        this.users = users ? users : null;
        this.textOrg = textOrg ? textOrg : null;
        this.listOrg = listOrg ? listOrg : null;
        this.companyID = companyID ? companyID : null;
    }
}
