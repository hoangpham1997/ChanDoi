export class SystemFieldsModel {
    code: string;
    //excelField mac dinh = null, de khach hang chon tren file import
    // Tu dong load sheet, get cell header tu sheet da chon
    excelField: string;
    description: string;
    systemField: string;
    required: boolean;
    index: number;
    type: string;
    constructor(code: string, systemField: string, description: string, index: number, required?: boolean, type?: string) {
        // truyen code len sver k qtam truong day la j
        this.code = code;
        // mo ta hien popup
        this.description = description;
        // cot du lieu tren fan mem
        this.systemField = systemField;
        // thu tu xuat hien cua system field
        this.index = index;
        // them truong bat buoc nhap
        this.required = required ? required : false;
        this.type = type;
    }
}
