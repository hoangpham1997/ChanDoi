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
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { RepositoryService } from 'app/danhmuc/repository';
import { IRepository } from 'app/shared/model/repository.model';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { MaterialGoodsCategoryService } from 'app/danhmuc/material-goods-category';
import { MaterialGoodsConvertUnitService } from 'app/entities/material-goods-convert-unit';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-calculate-ow-repository',
    templateUrl: './tong-hop-ton-kho.component.html',
    styleUrls: ['./tong-hop-ton-kho.component.css']
})
export class TongHopTonKhoComponent implements OnInit {
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
    materialGoodsCategories: IMaterialGoodsCategory[];
    materialGoodsCategoryID: string;
    repositories: IRepository[];
    repositoriesFilter: IRepository[];
    repositoryID: string;
    units: any[];
    unitID: number;
    description: string;
    defaultDescription: string;
    rCodeFilter: string;
    rNameFilter: string;
    similarBranch: boolean;
    ROLE_KetXuat = ROLE.TongHopTonKho_KetXuat;
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
            // });
            this.changeOptionData();
        });
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('searchTongHopTonKho'));
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
        sessionStorage.setItem('searchTongHopTonKho', JSON.stringify(this.dataSession));
    }

    changeOptionData(reload?) {
        if (reload) {
            this.similarBranch = false;
        }
        this.repositoryService
            .getRepositoryReport({
                companyID: this.organizationUnit.parent.id,
                similarBranch: this.similarBranch
            })
            .subscribe(res => {
                this.repositories = res.body;
                this.repositoriesFilter = this.repositories;
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

    changeMGFilter() {
        if (this.rNameFilter && this.rCodeFilter) {
            this.repositoriesFilter = this.repositories.filter(
                x =>
                    x.repositoryCode.toLowerCase().includes(this.rCodeFilter.toLowerCase()) &&
                    x.repositoryCode.toLowerCase().includes(this.rNameFilter.toLowerCase())
            );
        } else if (this.rCodeFilter) {
            this.repositoriesFilter = this.repositories.filter(x =>
                x.repositoryCode.toLowerCase().includes(this.rCodeFilter.toLowerCase())
            );
        } else if (this.rNameFilter) {
            this.repositoriesFilter = this.repositories.filter(x =>
                x.repositoryName.toLowerCase().includes(this.rNameFilter.toLowerCase())
            );
        } else {
            this.repositoriesFilter = this.repositories;
        }
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
            this.requestReport.materialGoodsCategoryID = this.materialGoodsCategoryID;
            this.requestReport.listMaterialGoods = this.repositories.filter(x => x.checked).map(c => c.id);
            this.requestReport.typeReport = BaoCao.Kho.TONG_HOP_TON_KHO;
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
            this.requestReport.materialGoodsCategoryID = this.materialGoodsCategoryID;
            this.requestReport.listMaterialGoods = this.repositories.filter(x => x.checked).map(c => c.id);
            this.requestReport.typeReport = BaoCao.Kho.TONG_HOP_TON_KHO;
            this.requestReport.fileName = BaoCao.Kho.TONG_HOP_TON_KHO_XLS;
            this.requestReport.dependent = this.similarBranch;
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = BaoCao.Kho.TONG_HOP_TON_KHO_XLS;
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
        if (this.repositories.filter(x => x.checked).length === 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.error.nullRepository'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        return true;
    }

    isCheckAll() {
        if (this.repositories) {
            return this.repositories.every(item => item.checked) && this.repositories.length;
        }
        return false;
    }

    checkAll() {
        const isCheck = this.repositories.every(item => item.checked) && this.repositories.length;
        this.repositories.forEach(item => (item.checked = !isCheck));
    }

    check(repository: IRepository) {
        repository.checked = !repository.checked;
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
