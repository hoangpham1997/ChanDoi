export interface IWarranty {
    id?: string;
    warrantyTime?: number;
    warrantyName?: string;
    description?: string;
    isActive?: boolean;
}

export class Warranty implements IWarranty {
    constructor(
        public id?: string,
        public warrantyTime?: number,
        public warrantyName?: string,
        public description?: string,
        public isActive?: boolean
    ) {
        this.isActive = this.isActive || false;
    }
}
