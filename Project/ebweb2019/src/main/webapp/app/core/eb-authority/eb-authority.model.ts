export interface IEbAuthority {
    id?: any;
    name?: string;
    code?: string;
    parentCode?: string;
    check?: boolean;
    children?: IEbAuthority[];
    collapse?: boolean;
}

export class EbAuthority implements IEbAuthority {
    constructor(
        public id?: any,
        public code?: string,
        public name?: string,
        public parentCode?: string,
        public check?: boolean,
        public collapse?: boolean,
        public children?: IEbAuthority[]
    ) {
        this.id = id ? id : null;
        this.name = name ? name : null;
        this.code = code ? code : null;
        this.parentCode = parentCode ? parentCode : null;
        this.children = children ? children : null;
        this.check = check ? check : null;
        this.collapse = collapse ? collapse : null;
    }
}
