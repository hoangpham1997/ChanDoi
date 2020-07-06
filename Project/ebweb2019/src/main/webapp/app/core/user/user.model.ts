import { IEbGroup } from 'app/core/eb-group/eb-group.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';

export interface IUser {
    id?: any;
    login?: string;
    firstName?: string;
    lastName?: string;
    email?: string;
    activated?: boolean;
    langKey?: string;
    authorities?: any[];
    createdBy?: string;
    createdDate?: Date;
    lastModifiedBy?: string;
    lastModifiedDate?: Date;
    password?: string;
    // add by anmt
    fullName?: string;
    address?: string;
    mobilePhone?: string;
    organizationUnits?: any[];
    ebGroups?: IEbGroup[];
    birthday?: Date;
    country?: string;
    city?: string;
    province?: string;
    idCard?: string;
    description?: string;
    homePhone?: string;
    fax?: string;
    workOnBook?: number;
    job?: string;
    confirmPassword?: string;
    checked?: boolean;
    isSystem?: boolean;
    status?: number;
    statusClone?: number;
    orgID?: string;
    packageID?: string;
    organizationUnitName?: string;
    packageName?: string;
    isChangePassword?: Boolean;
    ebUserPackage?: any;
    isTotalPackage?: Boolean;
}

export class User implements IUser {
    constructor(
        public id?: any,
        public login?: string,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public activated?: boolean,
        public langKey?: string,
        public authorities?: any[],
        public createdBy?: string,
        public createdDate?: Date,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Date,
        public password?: string,
        public confirmPassword?: string,
        public fullName?: string,
        public address?: string,
        public mobilePhone?: string,
        public organizationUnits?: any[],
        public ebGroups?: IEbGroup[],
        public birthday?: Date,
        public country?: string,
        public city?: string,
        public province?: string,
        public idCard?: string,
        public description?: string,
        public homePhone?: string,
        public fax?: string,
        public workOnBook?: number,
        public job?: string,
        public checked?: boolean,
        public isSystem?: boolean,
        public status?: number,
        public statusClone?: number,
        public orgID?: string,
        public packageID?: string,
        public organizationUnitName?: string,
        public packageName?: string,
        public isChangePassword?: Boolean,
        public ebUserPackage?: any,
        public isTotalPackage?: Boolean
    ) {
        this.id = id ? id : null;
        this.login = login ? login : null;
        this.firstName = firstName ? firstName : null;
        this.lastName = lastName ? lastName : null;
        this.email = email ? email : null;
        this.activated = activated ? activated : false;
        this.langKey = langKey ? langKey : null;
        this.authorities = authorities ? authorities : null;
        this.createdBy = createdBy ? createdBy : null;
        this.createdDate = createdDate ? createdDate : null;
        this.lastModifiedBy = lastModifiedBy ? lastModifiedBy : null;
        this.lastModifiedDate = lastModifiedDate ? lastModifiedDate : null;
        this.password = password ? password : null;
        this.confirmPassword = confirmPassword ? confirmPassword : null;
        this.fullName = fullName ? fullName : null;
        this.address = address ? address : null;
        this.mobilePhone = mobilePhone ? mobilePhone : null;
        this.organizationUnits = organizationUnits ? organizationUnits : null;
        this.ebGroups = ebGroups ? ebGroups : null;
        this.birthday = birthday ? birthday : null;
        this.country = country ? country : null;
        this.city = city ? city : null;
        this.province = province ? province : null;
        this.idCard = idCard ? idCard : null;
        this.description = description ? description : null;
        this.homePhone = homePhone ? homePhone : null;
        this.fax = fax ? fax : null;
        this.workOnBook = workOnBook ? workOnBook : null;
        this.job = job ? job : null;
        this.checked = checked ? checked : null;
        this.isSystem = this.isSystem || false;
        this.status = status ? status : null;
        this.orgID = orgID ? orgID : null;
        this.packageID = packageID ? packageID : null;
        this.organizationUnitName = organizationUnitName ? organizationUnitName : null;
        this.packageName = this.packageName ? this.packageName : null;
        this.isChangePassword = this.isChangePassword || false;
        this.ebUserPackage = this.ebUserPackage ? this.ebUserPackage : null;
        this.isTotalPackage = this.isTotalPackage || false;
    }
}
