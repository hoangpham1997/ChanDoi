export interface ITypeGroup {
    id?: number;
    typeGroupName?: string;
}

export class TypeGroup implements ITypeGroup {
    constructor(public id?: number, public typeGroupName?: string) {}
}
