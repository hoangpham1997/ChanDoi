import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import * as moment from 'moment';
import { Moment } from 'moment';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { RefModalService } from '../../../core/login/ref-modal.service';
import { DATE_FORMAT, DATE_FORMAT_SEARCH } from 'app/shared';
import { BaoCao, MATERIAL_GOODS_TYPE } from 'app/app.constants';
import { IRequestReport, RequestReport } from 'app/bao-cao/reqest-report.model';
import { BaoCaoService } from 'app/bao-cao/bao-cao.service';
import { HttpResponse } from '@angular/common/http';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { RepositoryService } from 'app/danhmuc/repository';
import { IRepository } from 'app/shared/model/repository.model';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { MaterialGoodsCategoryService } from 'app/danhmuc/material-goods-category';
import { MaterialGoodsConvertUnitService } from 'app/entities/material-goods-convert-unit';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-calculate-ow-repository',
    templateUrl: './the-kho.component.html',
    styleUrls: ['./the-kho.component.css']
})
export class TheKhoComponent implements OnInit {
    requestReport: RequestReport;
    dataSession: IRequestReport;
    createDate: Moment;
    fromDate: Moment;
    toDate: Moment;
    listTimeLine: any[];
    timeLineVoucher: any;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    account: any;
    modalRef: NgbModalRef;
    option: number;
    organizationUnit: any;
    treeOrganizationUnits: any[];
    materialGoods: IMaterialGoods[];
    materialGoodsFilter: IMaterialGoods[];
    materialGoodsCategories: IMaterialGoodsCategory[];
    materialGoodsCategoryID: string;
    repositories: IRepository[];
    repositoryID: string;
    units: any[];
    unitID: number;
    description: string;
    defaultDescription: string;
    mgCodeFilter: string;
    mgNameFilter: string;
    similarBranch: boolean;
    ROLE_KetXuat = ROLE.TheKho_KetXuat;
    isKetXuat: boolean;
    constructor(
        public activeModal: NgbActiveModal,
        private principal: Principal,
        public translate: TranslateService,
        public eventManager: JhiEventManager,
        public utilsService: UtilsService,
        private modalService: NgbModal,
        private refModalService: RefModalService,
        private toastr: ToastrService,
        private baoCaoService: BaoCaoService,
        private organizationUnitService: OrganizationUnitService,
        private materialGoodsService: MaterialGoodsService,
        private repositoryService: RepositoryService,
        private materialGoodsCategoryService: MaterialGoodsCategoryService,
        private materialGoodsConvertUnitService: MaterialGoodsConvertUnitService
    ) {}

    ngOnInit() {
        this.option = 1;
        this.defaultDescription = this.translate.instant('ebwebApp.baoCao.soChiTietVatLieuDungCuSPHH.donViTinh');
        this.description = this.translate.instant('ebwebApp.baoCao.soChiTietVatLieuDungCuSPHH.donViTinhCD');
        this.units = [];
        this.similarBranch = false;
        this.principal.identity().then(account => {
            this.account = account;
            this.isKetXuat = !this.utilsService.isPackageDemo(account);
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            this.timeLineVoucher = this.listTimeLine[4].value;
            this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
            this.getSessionData();
            // this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
            //     this.treeOrganizationUnits = res.body.orgTrees;
            //     this.organizationUnit = res.body.currentOrgLogin;
            //     this.baoCaoService.checkHiddenDependent(this.treeOrganizationUnits);
            //     this.baoCaoService.checkHiddenFirstCompany(this.organizationUnit);
            //     this.changeOptionData();
            // });
            this.changeOptionData();
        });
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('searchTheKho'));
        if (this.dataSession) {
            this.toDate = this.dataSession.toDate;
            this.fromDate = this.dataSession.fromDate;
            this.timeLineVoucher = this.dataSession.timeLineVoucher ? this.dataSession.timeLineVoucher : '0';
        } else {
            this.dataSession = {};
        }
    }

    setSessionData() {
        this.dataSession.toDate = this.toDate;
        this.dataSession.fromDate = this.fromDate;
        this.dataSession.timeLineVoucher = this.timeLineVoucher ? this.timeLineVoucher : '';
        sessionStorage.setItem('searchTheKho', JSON.stringify(this.dataSession));
    }

    showReport() {
        if (this.checkErr()) {
            this.setSessionData();
            this.requestReport = {};
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT);
            this.requestReport.unitType = this.unitID;
            this.requestReport.repositoryID = this.repositoryID;
            this.requestReport.listMaterialGoods = this.materialGoods.filter(x => x.checked).map(c => c.id);
            this.requestReport.typeReport = BaoCao.Kho.THE_KHO;
            this.requestReport.dependent = this.similarBranch;
            this.baoCaoService.getReport(this.requestReport);
        }
    }

    exportExcel() {
        if (this.checkErr()) {
            this.setSessionData();
            this.requestReport = {};
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT);
            this.requestReport.unitType = this.unitID;
            this.requestReport.repositoryID = this.repositoryID;
            this.requestReport.listMaterialGoods = this.materialGoods.filter(x => x.checked).map(c => c.id);
            this.requestReport.typeReport = BaoCao.Kho.THE_KHO;
            this.requestReport.fileName = BaoCao.Kho.THE_KHO_XLS;
            this.requestReport.dependent = this.similarBranch;
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = BaoCao.Kho.THE_KHO_XLS;
                    link.setAttribute('download', name);
                    link.href = fileURL;
                    link.click();
                },
                () => {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.saReturn.error.undefined'),
                        this.translate.instant('ebwebApp.mCReceipt.error.error')
                    );
                }
            );
        }
    }

    changeOptionData(reload?) {
        if (reload) {
            this.similarBranch = false;
        }
        this.materialGoodsService
            .getAllMaterialGoodsDTO({
                companyID: this.organizationUnit.parent.id,
                similarBranch: this.similarBranch
            })
            .subscribe((res: HttpResponse<IMaterialGoods[]>) => {
                this.materialGoods = res.body
                    .filter(x => x.materialGoodsType !== MATERIAL_GOODS_TYPE.DIFF && x.materialGoodsType !== MATERIAL_GOODS_TYPE.SERVICE)
                    .sort((a, b) => a.materialGoodsCode.localeCompare(b.materialGoodsCode));
                this.materialGoodsFilter = this.materialGoods;
            });
        this.repositoryService
            .getRepositoryReport({
                companyID: this.organizationUnit.parent.id,
                similarBranch: this.similarBranch
            })
            .subscribe(res => {
                this.repositories = res.body;
            });
        this.materialGoodsCategoryService
            .getMaterialGoodsCategoryForReport({
                companyID: this.organizationUnit.parent.id,
                similarBranch: this.similarBranch
            })
            .subscribe((res: HttpResponse<IMaterialGoodsCategory[]>) => {
                this.materialGoodsCategories = res.body;
            });
        this.units = [{ name: this.defaultDescription, value: 0 }];
        this.unitID = 0;
        this.materialGoodsConvertUnitService
            .getNumberOrder({
                companyID: this.organizationUnit.parent.id,
                similarBranch: this.similarBranch
            })
            .subscribe((res: HttpResponse<any[]>) => {
                if (res.body.length > 0) {
                    res.body.forEach(item => {
                        this.units.push({ name: this.description + item, value: item });
                    });
                }
            });
    }

    changMaterialGoodsCategory() {
        if (this.materialGoodsCategoryID) {
            this.materialGoodsService
                .getAllMaterialGoodsDTO({ similarBranch: this.similarBranch })
                .subscribe((res: HttpResponse<IMaterialGoods[]>) => {
                    this.materialGoods = res.body
                        .filter(
                            x =>
                                x.materialGoodsCategoryID === this.materialGoodsCategoryID &&
                                x.materialGoodsType !== MATERIAL_GOODS_TYPE.DIFF &&
                                x.materialGoodsType !== MATERIAL_GOODS_TYPE.SERVICE
                        )
                        .sort((a, b) => a.materialGoodsCode.localeCompare(b.materialGoodsCode));
                    this.changeMGFilter();
                });
        } else {
            this.materialGoodsService
                .getAllMaterialGoodsDTO({ similarBranch: this.similarBranch })
                .subscribe((res: HttpResponse<IMaterialGoods[]>) => {
                    this.materialGoods = res.body.sort((a, b) => a.materialGoodsCode.localeCompare(b.materialGoodsCode));
                    this.changeMGFilter();
                });
        }
    }

    changeMGFilter() {
        if (this.mgNameFilter && this.mgCodeFilter) {
            this.materialGoodsFilter = this.materialGoods.filter(
                x =>
                    x.materialGoodsCode.toLowerCase().includes(this.mgCodeFilter.toLowerCase()) &&
                    x.materialGoodsName.toLowerCase().includes(this.mgNameFilter.toLowerCase())
            );
        } else if (this.mgCodeFilter) {
            this.materialGoodsFilter = this.materialGoods.filter(x =>
                x.materialGoodsCode.toLowerCase().includes(this.mgCodeFilter.toLowerCase())
            );
        } else if (this.mgNameFilter) {
            this.materialGoodsFilter = this.materialGoods.filter(x =>
                x.materialGoodsName.toLowerCase().includes(this.mgNameFilter.toLowerCase())
            );
        } else {
            this.materialGoodsFilter = this.materialGoods;
        }
    }

    checkErr() {
        if (!this.organizationUnit) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.error.nullOrg'),
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
        if (this.materialGoods.filter(x => x.checked).length === 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.error.nullMaterialGoods'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        return true;
    }

    isCheckAll() {
        if (this.materialGoods) {
            return this.materialGoods.every(item => item.checked) && this.materialGoods.length;
        }
        return false;
    }

    checkAll() {
        const isCheck = this.materialGoods.every(item => item.checked) && this.materialGoods.length;
        this.materialGoods.forEach(item => (item.checked = !isCheck));
    }

    check(materialGood: IMaterialGoods) {
        materialGood.checked = !materialGood.checked;
    }

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDate = moment(this.objTimeLine.dtBeginDate);
            this.toDate = moment(this.objTimeLine.dtEndDate);
            this.createDate = this.fromDate;
        }
    }

    getCurrentDate(): { year; month; day } {
        const _date = moment();
        return { year: _date.year(), month: _date.month() + 1, day: _date.date() };
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }
}
