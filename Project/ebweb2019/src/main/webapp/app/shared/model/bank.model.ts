export interface IBank {
    id?: string;
    bankCode?: string;
    bankName?: string;
    bankNameRepresent?: string;
    address?: string;
    description?: string;
    isActive?: boolean;
}

export class Bank implements IBank {
    constructor(
        public id?: string,
        public bankCode?: string,
        public bankName?: string,
        public bankNameRepresent?: string,
        public address?: string,
        public description?: string,
        public isActive?: boolean
    ) {
        this.isActive = this.isActive || false;
    }
}
