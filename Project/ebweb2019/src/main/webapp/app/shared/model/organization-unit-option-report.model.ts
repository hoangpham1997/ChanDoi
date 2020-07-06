export interface IOrganizationUnitOptionReport {
    id?: string;
    organizationUnitID?: string;
    director?: string;
    chiefAccountant?: string;
    treasurer?: string;
    stocker?: string;
    reporter?: string;
    email?: string;
    fax?: string;
    website?: string;
    bankAccountDetailID?: string;
    isDisplayNameInReport?: boolean;
    isDisplayAccount?: boolean;
    headerSetting?: number;
    taxAgentName?: string;
    taxAgentTaxCode?: string;
    taxAgentTaxEmployee?: string;
    certificateNumber?: string;
    governingUnitName?: string;
    governingUnitTaxCode?: string;
}

export class OrganizationUnitOptionReport implements IOrganizationUnitOptionReport {
    constructor(
        public id?: string,
        public organizationUnitID?: string,
        public director?: string,
        public chiefAccountant?: string,
        public treasurer?: string,
        public stocker?: string,
        public reporter?: string,
        public email?: string,
        public fax?: string,
        public website?: string,
        public bankAccountDetailID?: string,
        public isDisplayNameInReport?: boolean,
        public isDisplayAccount?: boolean,
        public headerSetting?: number,
        public taxAgentName?: string,
        public taxAgentTaxCode?: string,
        public taxAgentTaxEmployee?: string,
        public certificateNumber?: string,
        public governingUnitName?: string,
        public governingUnitTaxCode?: string
    ) {
        this.isDisplayNameInReport = this.isDisplayNameInReport || false;
        this.isDisplayAccount = this.isDisplayAccount || false;
    }
}
