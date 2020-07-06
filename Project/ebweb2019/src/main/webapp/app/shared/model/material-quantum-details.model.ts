import { IMaterialQuantum } from 'app/shared/model//material-quantum.model';
import { IMaterialGoods } from 'app/shared/model//material-goods.model';
import { IUnit } from 'app/shared/model//unit.model';

export interface IMaterialQuantumDetails {
    id?: string;
    description?: string;
    quantity?: number;
    unitPrice?: number;
    amount?: number;
    orderPriority?: number;
    materialQuantum?: IMaterialQuantum;
    materialGoodsID?: string;
    unitID?: string;
    materialQuantumID?: string;
}

export class MaterialQuantumDetails implements IMaterialQuantumDetails {
    constructor(
        public id?: string,
        public description?: string,
        public quantity?: number,
        public unitPrice?: number,
        public amount?: number,
        public orderPriority?: number,
        public materialQuantum?: IMaterialQuantum,
        public materialGoodsID?: string,
        public unitID?: string,
        public materialQuantumID?: string
    ) {}
}
