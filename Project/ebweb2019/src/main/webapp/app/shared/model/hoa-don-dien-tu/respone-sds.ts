import { IResponeSdsData } from 'app/shared/model/hoa-don-dien-tu/respone-sds-data';

export interface IResponeSds {
    status?: number;
    message?: string;
    rawBytes?: any;
    fileName?: string;
    data?: IResponeSdsData;
}

export class ResponeSds implements IResponeSds {
    constructor(
        public status?: number,
        public message?: string,
        public rawBytes?: any,
        public fileName?: string,
        public responseData?: IResponeSdsData
    ) {}
}
