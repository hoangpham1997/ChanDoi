export interface IDetailDelFailCategory {
    name?: string;
    code?: string;
    des?: string;
}

export class DetailDelFailCategory implements IDetailDelFailCategory {
    constructor(public name?: string, public code?: string, public des?: string) {}
}
