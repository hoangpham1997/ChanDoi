import { IType } from 'app/shared/model/type.model';

export interface ITemplate {
    id?: number;
    companyId?: string;
    templateName?: string;
    isDefault?: boolean;
    isActive?: boolean;
    isSecurity?: boolean;
    orderPriority?: number;
    type?: IType;
}

export class Template implements ITemplate {
    constructor(
        public id?: number,
        public companyId?: string,
        public templateName?: string,
        public isDefault?: boolean,
        public isActive?: boolean,
        public isSecurity?: boolean,
        public orderPriority?: number,
        public type?: IType
    ) {
        this.isDefault = this.isDefault || false;
        this.isActive = this.isActive || false;
        this.isSecurity = this.isSecurity || false;
    }
}
