export interface IType {
    id?: number;
    typeName?: string;
    typeGroupID?: number;
}

export class Type implements IType {
    constructor(public id?: number, public typeName?: string, public typeGroupID?: number) {}
}
