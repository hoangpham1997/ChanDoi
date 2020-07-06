import { AfterViewChecked, AfterViewInit, Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { CALCULATE_OW, MATERIAL_GOODS_TYPE, PP_TINH_GIA_XUAT_KHO } from 'app/app.constants';
import { JhiEventManager } from 'ng-jhipster';
import * as moment from 'moment';
import { Moment } from 'moment';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { RefModalService } from '../../core/login/ref-modal.service';
import { IMaterialGoods } from '../../shared/model/material-goods.model';
import { EbMaterialGoodsModalComponent } from '../../shared/modal/material-goods/material-goods.component';
import { RepositoryLedgerService } from 'app/entities/repository-ledger';
import { DATE_FORMAT, DATE_FORMAT_SEARCH } from 'app/shared';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { ErrorVouchersComponent } from 'app/shared/modal/error-vouchers/error-vouchers.component';

@Component({
    selector: 'eb-calculate-ow-repository',
    templateUrl: './tinh-gia-xuat-kho.component.html',
    styleUrls: ['./tinh-gia-xuat-kho.component.css']
})
export class CalculateOWRepositoryComponent extends BaseComponent implements OnInit, AfterViewChecked {
    phuongPhapTinhGia: string;
    isAllMaterial: number;
    fromDate: Moment;
    toDate: Moment;
    listTimeLine: any[];
    timeLineVoucher: any;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    account: any;
    modalRef: NgbModalRef;
    materialGoods: IMaterialGoods[];
    materialGoodsAll: IMaterialGoods[];
    calculationMethod: any;
    isByRepository: boolean;
    setEdit: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private principal: Principal,
        public translate: TranslateService,
        public eventManager: JhiEventManager,
        public utilsService: UtilsService,
        private modalService: NgbModal,
        private refModalService: RefModalService,
        private repositoryLedger: RepositoryLedgerService,
        private materialGoodsService: MaterialGoodsService,
        private toastr: ToastrService
    ) {
        super();
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
            this.isByRepository = false;
            this.setEdit = this.isEdit;
            this.isEdit = true;
            this.calculationMethod = this.account.systemOption.find(x => x.code === PP_TINH_GIA_XUAT_KHO).data;
            if (this.calculationMethod === CALCULATE_OW.BINH_QUAN_CUOI_KY_CODE) {
                this.phuongPhapTinhGia = CALCULATE_OW.BINH_QUAN_CUOI_KY;
            } else if (this.calculationMethod === CALCULATE_OW.BINH_QUAN_TUC_THOI_CODE) {
                this.phuongPhapTinhGia = CALCULATE_OW.BINH_QUAN_TUC_THOI;
            } else if (this.calculationMethod === CALCULATE_OW.NHAP_TRUOC_XUAT_TRUOC_CODE) {
                this.phuongPhapTinhGia = CALCULATE_OW.NHAP_TRUOC_XUAT_TRUOC;
            } else if (this.calculationMethod === CALCULATE_OW.DICH_DANH_CODE) {
                this.phuongPhapTinhGia = CALCULATE_OW.DICH_DANH;
            } else if (this.calculationMethod === CALCULATE_OW.GIA_TRI_CODE) {
                this.phuongPhapTinhGia = CALCULATE_OW.GIA_TRI;
            }
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            this.timeLineVoucher = this.listTimeLine[4].value;
            this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
            this.isAllMaterial = 1;
            this.materialGoods = [];
            this.materialGoodsService
                .getAllMaterialGoodsDTO({ companyID: this.account.organizationUnit.id })
                .subscribe((res: HttpResponse<IMaterialGoods[]>) => {
                    this.materialGoodsAll = res.body.filter(
                        x => x.materialGoodsType !== MATERIAL_GOODS_TYPE.DIFF && x.materialGoodsType !== MATERIAL_GOODS_TYPE.SERVICE
                    );
                });
        });
        this.registerRef();
    }

    calculate() {
        if (this.isAllMaterial === 1) {
            this.materialGoods = this.materialGoodsAll;
        }
        if (this.checkErr()) {
            const calculateOWDTO = {
                calculationMethod: this.calculationMethod ? this.calculationMethod : '',
                materialGoods: this.materialGoods.map(x => x.id),
                fromDate: this.fromDate ? moment(this.fromDate).format(DATE_FORMAT) : '',
                toDate: this.toDate ? moment(this.toDate).format(DATE_FORMAT) : ''
            };
            this.repositoryLedger.calculateOWPrice(calculateOWDTO).subscribe(
                res => {
                    if (res.body.status === 1) {
                        this.toastr.success(
                            this.translate.instant('global.messages.calculateOWSuccess'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.voucherResultDTOs) {
                        this.modalRef = this.refModalService.open(
                            res.body.voucherResultDTOs,
                            ErrorVouchersComponent,
                            null,
                            false,
                            null,
                            null,
                            null,
                            null,
                            null,
                            true
                        );
                    } else {
                        this.toastr.error(
                            this.translate.instant('global.messages.error.calculateOWError'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    }
                },
                (res: HttpErrorResponse) => {
                    this.toastr.error(
                        this.translate.instant('global.messages.error.calculateOWError'),
                        this.translate.instant('ebwebApp.mCReceipt.error.error')
                    );
                }
            );
        }
    }

    closeForm() {
        this.isEdit = this.setEdit;
        this.activeModal.dismiss('closed');
    }

    checkErr() {
        if (this.materialGoodsAll.length === 0) {
            this.toastr.error(
                this.translate.instant('global.messages.error.nullMaterialGoodsAll'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (this.materialGoods.length === 0) {
            this.toastr.error(
                this.translate.instant('global.messages.error.nullMaterialGoods'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.fromDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.nullFromDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.nullToDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (this.toDate && this.fromDate) {
            if (moment(this.toDate, DATE_FORMAT_SEARCH) < moment(this.fromDate, DATE_FORMAT_SEARCH)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.fromDateGreaterToDate'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        return true;
    }

    registerRef() {
        this.eventManager.subscribe('selectMaterialGoods', ref => {
            this.materialGoods = ref.content;
        });
    }

    chooseMaterialGoods() {
        this.modalRef = this.refModalService.open(
            this.materialGoods,
            EbMaterialGoodsModalComponent,
            null,
            false,
            null,
            'width-80 width-50'
        );
    }

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDate = moment(this.objTimeLine.dtBeginDate);
            this.toDate = moment(this.objTimeLine.dtEndDate);
        }
    }

    getCurrentDate(): { year; month; day } {
        const _date = moment();
        return { year: _date.year(), month: _date.month() + 1, day: _date.date() };
    }

    ngAfterViewChecked(): void {
        this.disableInput();
    }
}
