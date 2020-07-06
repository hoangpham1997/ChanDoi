import { Component, OnInit } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { Principal } from 'app/core';
import {
    EXCEL_FIELD_KH,
    EXCEL_FIELD_NCC,
    EXCEL_FIELD_NV,
    EXCEL_FIELD_VTHH,
    TYPE_VTHH
} from 'app/shared/modal/import-excel-danh-muc/import-excel-danh-muc-vi.constant';
import { SystemFieldsModel } from 'app/shared/modal/import-excel-danh-muc/systemFields.model';
import { ImportExcelDanhMucService } from 'app/shared/modal/import-excel-danh-muc/import-excel-danh-muc.service';
import { ACCOUNTING_TYPE_ID, DanhMucType } from 'app/app.constants';

@Component({
    selector: 'eb-import-excel-danh-muc',
    templateUrl: './import-excel-danh-muc.component.html',
    styleUrls: ['import-excel-danh-muc.component.css']
})
export class ImportExcelDanhMucComponent implements OnInit {
    modalData: any;
    link: any;
    file: any;
    sheetNames: { name: string; fields: string[] }[];
    sheetName: { name: string; fields: string[] };
    systemFields: SystemFieldsModel[];
    systemFieldsMaterialGoods: {
        INFO_VTHH: SystemFieldsModel[];
        DISCOUNT: SystemFieldsModel[];
        P_PRICE: SystemFieldsModel[];
        S_PRICE: SystemFieldsModel[];
        UNIT_CONVERT: SystemFieldsModel[];
        ASSEMBLY: SystemFieldsModel[];
    };
    excelFields: { name: string }[];
    isUploading: boolean;
    errorTaxCalculationMethod: string;
    METHOD_ABATEMENT = 'METHOD_ABATEMENT';
    METHOD_DIRECT = 'METHOD_DIRECT';
    DanhMucType = DanhMucType;
    importTypes: any[];
    importType: number;
    typeDanhMuc: string;
    refModel: any;
    refModel2: any;
    isValid: boolean;
    listFail: any[];
    onSaveSuccess: any;
    dataAccept: any;
    constructor(
        private eventManager: JhiEventManager,
        private activeModal: NgbActiveModal,
        public utilsService: UtilsService,
        public translateService: TranslateService,
        private toastr: ToastrService,
        private principal: Principal,
        private modalService: NgbModal,
        private importExcelDanhMucService: ImportExcelDanhMucService
    ) {}
    ngOnInit(): void {
        this.excelFields = [];
        this.typeDanhMuc = this.modalData.type;
        this.onSaveSuccess = this.modalData.onSaveSuccess;
        switch (this.typeDanhMuc) {
            case DanhMucType.KH:
                this.systemFields = EXCEL_FIELD_KH.sort((a, b) => a.index - b.index);
                for (const data of this.systemFields) {
                    data.excelField = '';
                }
                break;
            case DanhMucType.NCC:
                this.systemFields = EXCEL_FIELD_NCC.sort((a, b) => a.index - b.index);
                for (const data of this.systemFields) {
                    data.excelField = '';
                }
                break;
            case DanhMucType.NV:
                this.systemFields = EXCEL_FIELD_NV.sort((a, b) => a.index - b.index);
                for (const data of this.systemFields) {
                    data.excelField = '';
                }
                break;
            case DanhMucType.VTHH:
                this.systemFieldsMaterialGoods = {
                    INFO_VTHH: EXCEL_FIELD_VTHH.filter(x => x.type === TYPE_VTHH.INFO_VTHH).sort((a, b) => a.index - b.index),
                    ASSEMBLY: EXCEL_FIELD_VTHH.filter(x => x.type === TYPE_VTHH.ASSEMBLY).sort((a, b) => a.index - b.index),
                    DISCOUNT: EXCEL_FIELD_VTHH.filter(x => x.type === TYPE_VTHH.DISCOUNT).sort((a, b) => a.index - b.index),
                    S_PRICE: EXCEL_FIELD_VTHH.filter(x => x.type === TYPE_VTHH.S_PRICE).sort((a, b) => a.index - b.index),
                    P_PRICE: EXCEL_FIELD_VTHH.filter(x => x.type === TYPE_VTHH.P_PRICE).sort((a, b) => a.index - b.index),
                    UNIT_CONVERT: EXCEL_FIELD_VTHH.filter(x => x.type === TYPE_VTHH.UNIT_CONVERT).sort((a, b) => a.index - b.index)
                };
                for (const type of Object.keys(this.systemFieldsMaterialGoods)) {
                    for (const data of this.systemFieldsMaterialGoods[type]) {
                        data.excelField = '';
                    }
                }
                break;
        }
        this.loadImportType();
    }

    loadImportType() {
        this.translateService
            .get([
                'global.commonInfo.importType.newImport',
                'global.commonInfo.importType.updateImport',
                'global.commonInfo.importType.overrideImport'
            ])
            .subscribe((res: any) => {
                this.importTypes = [
                    {
                        name: res['global.commonInfo.importType.newImport'],
                        value: 0
                    },
                    {
                        name: res['global.commonInfo.importType.updateImport'],
                        value: 1
                    },
                    {
                        name: res['global.commonInfo.importType.overrideImport'],
                        value: 2
                    }
                ];
            });
    }

    close() {
        if (this.refModel) {
            this.refModel.close();
        }
        if (this.refModel2) {
            this.refModel2.close();
        }
        this.utilsService.setShowPopup(false);
        this.activeModal.dismiss(false);
    }

    apply(modal) {
        if (!this.isValid) {
            return;
        }
        // valid
        this.refModel = this.modalService.open(modal, { size: 'lg', backdrop: 'static' });
    }

    accept(modal, modal2, acceptDataModal) {
        this.toastr.info(this.translateService.instant('ebwebApp.saBill.upload.warning'));
        if (this.importType === this.importTypes[2].value) {
            this.onCheckReference(modal, modal2, acceptDataModal);
        } else {
            this.onAccept(modal, acceptDataModal);
        }
    }

    onAccept(modal, acceptDataModal) {
        let excelSystemFieldDTOS = [];
        if (this.typeDanhMuc === DanhMucType.VTHH) {
            for (const type of Object.keys(this.systemFieldsMaterialGoods)) {
                excelSystemFieldDTOS.push(...this.systemFieldsMaterialGoods[type]);
            }
        } else {
            excelSystemFieldDTOS = this.systemFields;
        }
        this.isUploading = true;
        this.importExcelDanhMucService
            .uploadFile(this.file, {
                sheetName: this.sheetName.name,
                type: this.typeDanhMuc,
                excelSystemFieldDTOS,
                importType: this.importType
            })
            .subscribe(res => {
                this.isUploading = false;
                if (res.headers.get('isError') === '1') {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    this.link = document.createElement('a');
                    document.body.appendChild(this.link);
                    this.link.download = fileURL;
                    this.link.setAttribute('style', 'display: none');
                    let name;
                    switch (this.typeDanhMuc) {
                        case DanhMucType.KH:
                            name = 'Import_KhachHang_Loi.xlsx';
                            break;
                        case DanhMucType.NCC:
                            name = 'Import_NhaCungCap_Loi.xlsx';
                            break;
                        case DanhMucType.NV:
                            name = 'Import_NhanVien_Loi.xlsx';
                            break;
                        case DanhMucType.VTHH:
                            name = 'Import_VTHH_Loi.xlsx';
                            break;
                    }
                    this.link.setAttribute('download', name);
                    this.link.href = fileURL;
                    this.refModel = this.modalService.open(modal, { size: 'lg', backdrop: 'static' });
                } else {
                    if (['METHOD_ABATEMENT', 'METHOD_DIRECT'].includes(res.headers.get('message'))) {
                        const blob = new Blob([res.body], { type: 'application/json' });
                        const reader = new FileReader();

                        this.errorTaxCalculationMethod = res.headers.get('message');
                        const that = this;
                        reader.onload = function() {
                            that.dataAccept = JSON.parse(this.result);
                            that.refModel = that.modalService.open(acceptDataModal, { size: 'lg', backdrop: 'static' });
                        };
                        reader.readAsText(blob);
                    }
                    if (res.headers.get('message') === 'REQUIRED_ITEM') {
                        const blob = new Blob([res.body], { type: 'application/json' });
                        const reader = new FileReader();

                        this.errorTaxCalculationMethod = res.headers.get('message');
                        const that = this;
                        reader.onload = function() {
                            const requitedHeader = JSON.parse(this.result);
                            that.isValid = false;
                        };
                        reader.readAsText(blob);
                    }
                    if (res.headers.get('message') === 'FILE_TOO_LARGE') {
                        this.toastr.error(this.translateService.instant('ebwebApp.saBill.upload.fileTooLarge'));
                        this.close();
                    } else {
                        if (['METHOD_ABATEMENT', 'METHOD_DIRECT'].includes(res.headers.get('message'))) {
                            this.close();
                            if (this.onSaveSuccess) {
                                this.onSaveSuccess();
                            }
                        } else {
                            this.toastr.success(this.translateService.instant('ebwebApp.saBill.upload.success'));
                            this.close();
                            if (this.onSaveSuccess) {
                                this.onSaveSuccess();
                            }
                        }
                    }
                }
            });
    }

    changeFile(event) {
        const file = event.target.files;
        this.file = null;
        if (file && file.length) {
            this.file = file[0];
        }
        this.upLoadFile();
    }

    upLoadFile() {
        if (!this.file) {
            return;
        }
        this.isUploading = true;
        this.importExcelDanhMucService.uploadFileAndGetSheetNames(this.file).subscribe(
            res => {
                this.isUploading = false;
                if (res.body.message === 'SUCCESS') {
                    this.sheetNames = [];
                    this.sheetName = null;
                    for (const property in res.body.excelFields) {
                        if (res.body.excelFields.hasOwnProperty(property)) {
                            this.sheetNames.push({ name: property, fields: res.body.excelFields[property] });
                            this.excelFields = [];
                            if (this.systemFields) {
                                for (const data of this.systemFields) {
                                    data.excelField = null;
                                }
                            }
                            if (this.sheetNames.length === 1) {
                                this.sheetName = this.sheetNames[0];
                            }
                            this.onChangeSheetName();
                        }
                    }
                } else {
                    this.toastr.error(this.translateService.instant('ebwebApp.saBill.upload.invalidFileFormat'));
                }
            },
            () => {
                this.toastr.error(this.translateService.instant('ebwebApp.saBill.upload.invalidFileFormat'));
                this.isUploading = false;
            }
        );
    }

    onChangeSheetName() {
        this.excelFields = [];
        this.excelFields.push(
            ...this.sheetName.fields.map(x => {
                return { name: x };
            })
        );
        if (this.typeDanhMuc === DanhMucType.VTHH) {
            // clear excel field khi chon sheet #
            for (const type of Object.keys(this.systemFieldsMaterialGoods)) {
                for (const data of this.systemFieldsMaterialGoods[type]) {
                    data.excelField = '';
                }
            }
            // vong 1 lap lay type, 2 lay tat ca excel field,
            for (const type of Object.keys(this.systemFieldsMaterialGoods)) {
                for (const data of this.systemFieldsMaterialGoods[type]) {
                    // neu excel field trung voi header tren excel thi fill vao cbb
                    if (this.sheetName.fields.includes(data.systemField)) {
                        data.excelField = this.sheetName.fields.find(x => x === data.systemField);
                    }
                }
            }
        } else {
            for (const data of this.systemFields) {
                data.excelField = '';
            }
            for (const data of this.systemFields) {
                if (this.sheetName.fields.includes(data.systemField)) {
                    data.excelField = this.sheetName.fields.find(x => x === data.systemField);
                }
            }
        }
        this.isValid = this.checkValid();
    }

    download() {
        this.link.click();
    }

    onChangeImportType() {
        this.isValid = this.checkValid();
    }

    checkValid() {
        let excelSystemFields = [];
        if (this.typeDanhMuc === DanhMucType.VTHH) {
            if (this.importType !== null && this.importType !== undefined) {
                for (const type of Object.keys(this.systemFieldsMaterialGoods)) {
                    for (const data of this.systemFieldsMaterialGoods[type]) {
                        excelSystemFields.push(data.excelField);
                        if (data.required) {
                            if (data.excelField === null || data.excelField === undefined || data.excelField === '') {
                                return false;
                            }
                        }
                    }
                }
                return true;
            }
        } else {
            if (this.importType !== null && this.importType !== undefined) {
                for (const data of this.systemFields) {
                    if (data.required) {
                        if (data.excelField === null || data.excelField === undefined || data.excelField === '') {
                            return false;
                        }
                    }
                }
                return true;
            }
            return false;
        }
    }

    onChangeExcelField() {
        this.isValid = this.checkValid();
    }

    onCheckReference(modal, modal2, acceptDataModal) {
        this.importExcelDanhMucService.checkReference({ type: this.typeDanhMuc }).subscribe(res => {
            if (res.body.errors && res.body.errors.length > 0) {
                this.listFail = res.body.errors;
                this.refModel2 = this.modalService.open(modal2, { size: 'lg', backdrop: 'static' });
            } else {
                this.onAccept(modal, acceptDataModal);
            }
        });
    }
    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }
    downloadTem() {
        this.importExcelDanhMucService.downloadTem({ type: this.typeDanhMuc }).subscribe(
            res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                let name;
                switch (this.typeDanhMuc) {
                    case DanhMucType.KH:
                        name = 'Import_KhachHang.xlsx';
                        break;
                    case DanhMucType.NCC:
                        name = 'Import_NhaCungCap.xlsx';
                        break;
                    case DanhMucType.NV:
                        name = 'Import_NhanVien.xlsx';
                        break;
                    case DanhMucType.VTHH:
                        name = 'Import_VTHH.xlsx';
                        break;
                }
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            },
            () => {}
        );
    }

    closePopup() {
        this.isUploading = false;
    }

    acceptData() {
        this.importExcelDanhMucService.acceptData(this.dataAccept, this.importType).subscribe(res => {
            if (res.body) {
                this.toastr.success(this.translateService.instant('ebwebApp.saBill.upload.success'));
                this.close();
                if (this.onSaveSuccess) {
                    this.onSaveSuccess();
                }
            } else {
                this.toastr.error(this.translateService.instant('ebwebApp.paymentClause.saveError'));
            }
        });
    }

    checkDuplicateColumn(listData: any[], key: any): void {
        this.isValid = this.checkValid();

        let countKey = 0;
        for (let i = 0; i < listData.length; i++) {
            if (listData[i].excelField === key) {
                countKey++;
            }
        }

        if (countKey > 1) {
            this.toastr.error(this.translateService.instant('ebwebApp.saBill.upload.duplicatedCell'));
        }
    }
}
