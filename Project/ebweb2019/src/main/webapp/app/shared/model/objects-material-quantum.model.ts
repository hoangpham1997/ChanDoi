export interface IObjectsMaterialQuantum {
    objectID?: string;
    objectCode?: string;
    objectName?: string;
    objectTypeString?: string;
    objectType?: number;
    isActive?: boolean;
}

export class ObjectsMaterialQuantum implements IObjectsMaterialQuantum {
    constructor(
        public objectID?: string,
        public objectCode?: string,
        public objectName?: string,
        public objectTypeString?: string,
        public objectType?: number,
        public isActive?: boolean
    ) {}
}
