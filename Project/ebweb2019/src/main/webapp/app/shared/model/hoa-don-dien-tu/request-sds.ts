import { IDigestDataDTO } from 'app/shared/model/hoa-don-dien-tu/respone-sds-data';

export interface IRequestSDS {
    Ikeys?: string[];
    CertString?: string;
    Pattern?: string;
    Serial?: string;
    signatureDTO?: IDigestDataDTO[];
}

export class RequestSDS implements IRequestSDS {
    constructor(
        public Ikeys?: string[],
        public CertString?: string,
        public Pattern?: string,
        public Serial?: string,
        public signatureDTO?: IDigestDataDTO[]
    ) {}
}
