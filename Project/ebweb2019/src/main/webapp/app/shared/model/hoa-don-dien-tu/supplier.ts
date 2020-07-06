export interface ISupplier {
    id?: string;
    supplierServiceCode?: string;
    supplierServiceName?: string;
    pathAccess?: string;
}

export class Supplier implements ISupplier {
    constructor(public id?: string, public supplierServiceCode?: string, public supplierServiceName?: any, public pathAccess?: string) {}
}
