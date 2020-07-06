export interface IUnit {
    id?: string;
    unitName?: string;
    unitDescription?: string;
    isActive?: boolean;
    isVthh?: boolean;
    convertRate?: number;
    formula?: string;
    materialGoodsID?: string;
}

export class Unit implements IUnit {
    constructor(
        public id?: string,
        public unitName?: string,
        public unitDescription?: string,
        public isActive?: boolean,
        public convertRate?: number,
        public formula?: string,
        public materialGoodsID?: string
    ) {
        this.isActive = this.isActive || false;
    }
}
