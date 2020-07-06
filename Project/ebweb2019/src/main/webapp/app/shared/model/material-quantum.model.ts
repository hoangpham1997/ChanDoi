import { Moment } from 'moment';
import { IMaterialQuantumDetails } from 'app/shared/model/material-quantum-details.model';

export interface IMaterialQuantum {
    id?: string;
    objectID?: string;
    objectCode?: string;
    objectName?: string;
    objectType?: number;
    fromDate?: Moment;
    toDate?: Moment;
    isChange?: boolean;
    materialQuantumDetails?: IMaterialQuantumDetails[];
}

export class MaterialQuantum implements IMaterialQuantum {
    constructor(
        public id?: string,
        public objectID?: string,
        public objectCode?: string,
        public objectName?: string,
        public objectType?: number,
        public fromDate?: Moment,
        public toDate?: Moment,
        public isChange?: boolean,
        public materialQuantumDetails?: IMaterialQuantumDetails[]
    ) {}
}
