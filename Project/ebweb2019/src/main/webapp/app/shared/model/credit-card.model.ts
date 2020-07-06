import { IBank } from 'app/shared/model//bank.model';
import { IOrganizationUnit } from 'app/shared/model//organization-unit.model';

export interface ICreditCard {
    bankName?: string;
    id?: string;
    creditCardNumber?: string;
    creditCardType?: string;
    ownerCard?: string;
    exFromMonth?: number;
    exFromYear?: number;
    exToMonth?: number;
    exToYear?: number;
    description?: string;
    isActive?: boolean;
    bankIDIssueCard?: string;
    branchID?: IOrganizationUnit;
}

export class CreditCard implements ICreditCard {
    constructor(
        public id?: string,
        public bankName?: string,
        public creditCardNumber?: string,
        public creditCardType?: string,
        public ownerCard?: string,
        public exFromMonth?: number,
        public exFromYear?: number,
        public exToMonth?: number,
        public exToYear?: number,
        public description?: string,
        public isActive?: boolean,
        public bankIDIssueCard?: string,
        public branchID?: IOrganizationUnit
    ) {
        this.isActive = this.isActive || false;
        this.creditCardType = this.creditCardType ? creditCardType : '';
    }
}
