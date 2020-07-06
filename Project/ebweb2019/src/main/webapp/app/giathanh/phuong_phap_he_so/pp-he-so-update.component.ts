import { AfterViewChecked, Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ICPPeriod } from 'app/shared/model/cp-period.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { ICPAllocationGeneralExpenseDetails } from 'app/shared/model/cp-allocation-general-expense-details.model';
import { CostSetService } from 'app/entities/cost-set';
import { ICPPeriodDetails } from 'app/shared/model/cp-period-details.model';
import { GIA_THANH, PPDanhGiaDoDang, TieuThucPhanBo } from 'app/app.constants';
import { ICPExpenseList } from 'app/shared/model/cp-expense-list.model';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { NgbActiveModal, NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { JhiParseLinks } from 'ng-jhipster';
import { DATE_FORMAT, ITEMS_PER_PAGE, ITEMS_PER_PAGE_EXLIST } from 'app/shared';
import * as moment from 'moment';
import { CPAllocationQuantumService } from 'app/giathanh/dinh_muc_phan_bo_chi_phi';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Principal } from 'app/core';
import { CPUncompleteDetailsService } from 'app/entities/cp-uncomplete-details';
import { CPResultService } from 'app/entities/cp-result';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ExpenseItemService } from 'app/entities/expense-item';
import { RepositoryLedgerService } from 'app/entities/repository-ledger';
import { PpGianDonService } from 'app/giathanh/phuong_phap_gian_don/pp-gian-don.service';
import { ICPAllocationRate } from 'app/shared/model/cp-allocation-rate.model';
import { CPAllocationRateService } from 'app/entities/cp-allocation-rate';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';

@Component({
    selector: 'eb-cp-period-update',
    templateUrl: './pp-he-so-update.component.html',
    styleUrls: ['./pp-he-so-update.component.css']
})
export class PpHeSoUpdateComponent extends BaseComponent implements OnInit, AfterViewChecked {
    @ViewChild('questionUpdateOWPrice') questionUpdateOWPrice;
    private _cPPeriod: ICPPeriod;
    type: string;
    PHUONG_PHAP_TY_LE = GIA_THANH.PHUONG_PHAP_TY_LE;
    PHUONG_PHAP_HE_SO = GIA_THANH.PHUONG_PHAP_HE_SO;
    isEdit: boolean;
    costSets: ICostSet[];
    listCostSet: any[];
    totalItems: any;
    itemsPerPage: any;
    page: any;
    links: any;
    queryCount: any;

    totalItemsEx1: any;
    itemsPerPageEx1: any;
    pageEx1: any;
    linksEx1: any;
    queryCountEx1: any;
    totalAmountEx1: any;

    totalItemsEx2: any;
    itemsPerPageEx2: any;
    pageEx2: any;
    linksEx2: any;
    queryCountEx2: any;
    totalAmountEx2: any;

    materialGoods: IMaterialGoods[];
    cPPeriodDetails: ICPPeriodDetails[];
    indexTab: number;
    cPExpenseListType0: any[];
    cPExpenseListType0All: any[];
    cPExpenseListType1All: any[];
    cPExpenseListTypeAll: any[];
    cPExpenseListType1: any[];
    cPAllocationGeneralExpense: any[];
    cPAllocationGeneralExpenseAll: any[];
    cPAllocationGeneralExpenseDetails: ICPAllocationGeneralExpenseDetails[];
    cPUncompletes: any[];
    cPUncompleteDetails: any[];
    cPResult: any[];
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    cPExpenseList: ICPExpenseList;
    timeLineVoucher: any;
    listTimeLine: any;
    listColumnsType: string[] = ['name'];
    listHeaderColumnsStatus: string[] = ['Loại'];
    attributionCriteriaList = [
        { id: 0, name: 'NVL trực tiếp' },
        { id: 1, name: 'Nhân công trực tiếp' },
        { id: 2, name: 'Chi phí trực tiếp' },
        { id: 3, name: 'Định mức' }
    ];
    methods = [{ id: 0, name: 'Sản phẩm hoàn thành tương đương' }, { id: 1, name: 'NVL trực tiếp' }, { id: 2, name: 'Định mức' }];
    method: number;
    account: any;
    listType: any;
    isSaved: boolean;
    cPAllocationRates: any[];
    modalRef: NgbModalRef;

    constructor(
        private cPPeriodService: PpGianDonService,
        private cpAllocationQuantumService: CPAllocationQuantumService,
        private generalLedgerService: GeneralLedgerService,
        private activatedRoute: ActivatedRoute,
        private costSetService: CostSetService,
        private expenseItemService: ExpenseItemService,
        public utilsService: UtilsService,
        public activeModal: NgbActiveModal,
        private parseLinks: JhiParseLinks,
        private router: Router,
        private toasService: ToastrService,
        public translateService: TranslateService,
        private principal: Principal,
        private cpUncompleteDetailsService: CPUncompleteDetailsService,
        private cpResultService: CPResultService,
        private repositoryLedgerService: RepositoryLedgerService,
        private cPAllocationRateService: CPAllocationRateService,
        private materialGoodsSerivce: MaterialGoodsService,
        private modalService: NgbModal
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE_EXLIST;
        this.itemsPerPageEx1 = ITEMS_PER_PAGE_EXLIST;
        this.itemsPerPageEx2 = ITEMS_PER_PAGE_EXLIST;
        this.translateService
            .get([
                'ebwebApp.costSet.Type.Order',
                'ebwebApp.costSet.Type.Construction',
                'ebwebApp.costSet.Type.Factory',
                'ebwebApp.costSet.Type.Manufacturing technology',
                'ebwebApp.costSet.Type.Product',
                'ebwebApp.costSet.Type.Others'
            ])
            .subscribe(res => {
                this.listType = [
                    { value: 0, name: this.translateService.instant('ebwebApp.costSet.Type.Order') },
                    { value: 1, name: this.translateService.instant('ebwebApp.costSet.Type.Construction') },
                    { value: 2, name: this.translateService.instant('ebwebApp.costSet.Type.Factory') },
                    { value: 3, name: this.translateService.instant('ebwebApp.costSet.Type.Manufacturing technology') },
                    { value: 4, name: this.translateService.instant('ebwebApp.costSet.Type.Product') },
                    { value: 5, name: this.translateService.instant('ebwebApp.costSet.Type.Others') }
                ];
            });
    }

    ngOnInit() {
        this.toolbarClass = 'gr-toolbar';
        this.totalAmountEx1 = 0;
        this.totalAmountEx2 = 0;
        this.isEdit = true;
        this.costSets = [];
        this.indexTab = 1;
        this.isSaved = false;
        this.method = PPDanhGiaDoDang.SP_HOAN_THANH_TUONG_DUONG;
        this.cPPeriod = {};
        this.cPExpenseList = {};
        this.listCostSet = [];
        this.cPExpenseListType0 = [];
        this.cPExpenseListType1 = [];
        this.cPAllocationRates = [];
        this.cPAllocationGeneralExpense = [];
        this.cPAllocationGeneralExpenseDetails = [];
        this.cPUncompletes = [];
        this.cPUncompleteDetails = [];
        this.cPResult = [];
        this.listTimeLine = this.utilsService.getCbbTimeLine();
        this.timeLineVoucher = this.listTimeLine[3].value;
        this.expenseItemService.getExpenseItems().subscribe(ref => {
            this.expenseItems = ref.body;
        });
        this.activatedRoute.data.subscribe(({ cPPeriod }) => {
            this.principal.identity().then(account => {
                this.account = account;
                this.materialGoodsSerivce
                    .getAllMaterialGoodsDTO({ companyID: this.account.organizationUnit.id })
                    .subscribe((res: HttpResponse<IMaterialGoods[]>) => {
                        this.materialGoods = res.body;
                    });
                this.cPPeriod = cPPeriod;
                if (!this.cPPeriod.id) {
                    this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
                    if (window.location.href.includes('he-so')) {
                        this.cPPeriod.type = this.PHUONG_PHAP_HE_SO;
                    } else if (window.location.href.includes('ty-le')) {
                        this.cPPeriod.type = this.PHUONG_PHAP_TY_LE;
                    }
                    this.loadAllCostSets();
                } else {
                    this.isSaved = true;
                    this.timeLineVoucher = null;
                    this.isEdit = false;
                    this.cPPeriod.cPPeriodDetails.forEach(item => {
                        const costSet = {
                            checked: true,
                            id: item.costSetID,
                            costSetCode: item.costSetCode,
                            costSetName: item.costSetName,
                            costSetType: item.costSetType
                        };
                        this.costSets.push(costSet);
                    });
                    this.totalItems = this.costSets.length;
                }
            });
        });
    }

    changeTab(value: number) {
        let go = true;
        if (this.listCostSet.length === 0 && this.indexTab === 1 && this.isEdit) {
            this.toasService.error(this.translateService.instant('ebwebApp.cPPeriod.messageWarning.nullCostSet'));
            return;
        }
        if (this.indexTab === 1 && value > 0 && this.isEdit) {
            go = false;
            if (!this.cPPeriod.fromDate) {
                this.toasService.error(
                    this.translateService.instant('ebwebApp.mCReceipt.error.nullFromDate'),
                    this.translateService.instant('ebwebApp.mCReceipt.error.error')
                );
                return;
            }
            if (!this.cPPeriod.toDate) {
                this.toasService.error(
                    this.translateService.instant('ebwebApp.mCReceipt.error.nullToDate'),
                    this.translateService.instant('ebwebApp.mCReceipt.error.error')
                );
                return;
            }
            if (moment(this.cPPeriod.toDate) < moment(this.cPPeriod.fromDate)) {
                this.toasService.error(
                    this.translateService.instant('ebwebApp.mBDeposit.fromDateMustBeLessThanToDate'),
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
                return;
            }
            this.cPPeriodService
                .checkPeriod({
                    fromDate: this.cPPeriod.fromDate.format(DATE_FORMAT),
                    toDate: this.cPPeriod.toDate.format(DATE_FORMAT),
                    costSetIDs: this.listCostSet,
                    type: this.cPPeriod.type
                })
                .subscribe(res => {
                    if (res.body) {
                        this.indexTab = this.indexTab + value;
                        if (this.indexTab === 2) {
                            this.pageEx1 = 0;
                            this.loadAllCPExpenseList0();
                        }
                    } else {
                        this.toasService.error(
                            this.translateService.instant('ebwebApp.cPPeriod.messageWarning.errorPeriod'),
                            this.translateService.instant('ebwebApp.mCReceipt.error.error')
                        );
                    }
                });
        }
        if (this.indexTab === 6 && this.cPPeriod.type === GIA_THANH.PHUONG_PHAP_HE_SO && this.cPAllocationRates.length > 0) {
            const group = this.cPAllocationRates.reduce((g: any, cPAllocationRate) => {
                g[cPAllocationRate.costSetID] = g[cPAllocationRate.costSetID] || [];
                g[cPAllocationRate.costSetID].push(cPAllocationRate);
                return g;
            }, {});
            const groupData = Object.keys(group).map(a => {
                return {
                    costSetID: a,
                    cPAllocationRates: group[a]
                };
            });
            let costSetCodes = '';
            for (let i = 0; i < groupData.length; i++) {
                if (!groupData[i].cPAllocationRates.some(x => x.isStandardItem)) {
                    const costSet = this.costSets.find(x => x.id === groupData[i].costSetID);
                    costSetCodes = costSetCodes + ', ' + costSet.costSetCode;
                }
            }
            if (costSetCodes) {
                costSetCodes = costSetCodes.substring(2, costSetCodes.length);
                this.toasService.error(
                    this.translateService.instant('ebwebApp.cPPeriod.messageWarning.notCheckIsStandardItem1') +
                        costSetCodes +
                        this.translateService.instant('ebwebApp.cPPeriod.messageWarning.notCheckIsStandardItem2'),
                    this.translateService.instant('ebwebApp.mCReceipt.error.error')
                );
                return;
            }
        }
        if (go) {
            this.indexTab = this.indexTab + value;
            if (this.indexTab === 2 && value > 0) {
                this.pageEx1 = 0;
                this.loadAllCPExpenseList0();
            } else if (this.indexTab === 3 && value > 0) {
                this.pageEx2 = 0;
                this.loadAllCPExpenseList1();
            } else if (this.indexTab === 4 && value > 0) {
                this.loadAllCPAllocationGeneralExpense();
            } else if (this.indexTab === 5 && value > 0) {
                this.loadAllCPUncomplete();
            } else if (this.indexTab === 6 && value > 0) {
                this.loadAllCPAllocationRates();
            } else if (this.indexTab === 7 && value > 0) {
                this.loadCalculateCost();
            }
        }
    }

    loadAllCPAllocationRates() {
        if (this.isEdit) {
            if (this.cPPeriod.type === this.PHUONG_PHAP_HE_SO) {
                this.cPAllocationRateService
                    .findByListCostSetID({
                        uuids: this.listCostSet,
                        fromDate: this.cPPeriod.fromDate.format(DATE_FORMAT),
                        toDate: this.cPPeriod.toDate.format(DATE_FORMAT)
                    })
                    .subscribe((res: HttpResponse<ICPAllocationRate[]>) => {
                        this.cPAllocationRates = res.body;
                        let i = 0;
                        this.cPAllocationRates.forEach(item => {
                            item.key = i++;
                            const max = Math.max(
                                ...this.cPAllocationRates.filter(a => a.costSetID === item.costSetID).map(n => n.quantity)
                            );
                            if (
                                item.quantity === max &&
                                !this.cPAllocationRates.some(a => a.costSetID === item.costSetID && a.isStandardItem)
                            ) {
                                item.isStandardItem = true;
                                item.coefficient = 1;
                                item.quantityStandard = max;
                            } else {
                                item.isStandardItem = false;
                            }
                        });
                        this.cPAllocationRates.filter(x => !x.isStandardItem).forEach(item => {
                            const cPAllocationRateStandard = this.cPAllocationRates.find(
                                a => a.costSetID === item.costSetID && a.isStandardItem
                            );
                            item.coefficient = item.priceQuantum / cPAllocationRateStandard.priceQuantum;
                            item.quantityStandard = item.coefficient * item.quantity;
                        });
                        this.cPAllocationRates.forEach(item => {
                            const totalQuantityStandard = this.cPAllocationRates
                                .filter(x => x.costSetID === item.costSetID)
                                .reduce(function(prev, cur) {
                                    return prev + cur.quantityStandard;
                                }, 0);
                            item.allocatedRate = totalQuantityStandard ? item.quantityStandard / totalQuantityStandard * 100 : 0;
                        });
                    });
            } else {
                this.cPAllocationRateService
                    .findByListCostSetID({
                        uuids: this.listCostSet,
                        fromDate: this.cPPeriod.fromDate.format(DATE_FORMAT),
                        toDate: this.cPPeriod.toDate.format(DATE_FORMAT)
                    })
                    .subscribe((res: HttpResponse<ICPAllocationRate[]>) => {
                        this.cPAllocationRates = res.body;
                        this.cPAllocationRates.forEach(item => {
                            item.allocationMethod = 1;
                            item.priceStandard = 1;
                            item.allocationStandard = item.quantity * item.priceQuantum;
                        });
                        this.cPAllocationRates.forEach(item => {
                            const totalAllocationStandard = this.cPAllocationRates
                                .filter(x => x.costSetID === item.costSetID)
                                .reduce(function(prev, cur) {
                                    return prev + cur.allocationStandard;
                                }, 0);
                            item.allocatedRate = totalAllocationStandard ? item.allocationStandard / totalAllocationStandard * 100 : 0;
                        });
                    });
            }
        } else {
            this.cPAllocationRates = this.cPPeriod.cPAllocationRates;
        }
    }

    loadAllCostSets() {
        this.costSetService
            .getCostSetsByType({
                type: 2,
                page: this.page ? this.page - 1 : 0,
                size: this.itemsPerPage
            })
            .subscribe((res: HttpResponse<ICostSet[]>) => {
                this.costSets = res.body;
                if (this.listCostSet) {
                    this.costSets.forEach(item => {
                        item.checked = this.listCostSet.some(data => data === item.id);
                    });
                }
                this.links = this.parseLinks.parse(res.headers.get('link'));
                this.totalItems = parseInt(res.headers.get('X-Total-Count'), 10);
                this.queryCount = this.totalItems;
            });
    }

    loadAllCPExpenseList0() {
        if (this.isEdit) {
            this.generalLedgerService
                .getCPExpenseListByCostSet({
                    page: this.pageEx1 ? this.pageEx1 - 1 : 0,
                    size: this.itemsPerPageEx1 ? this.itemsPerPageEx1 : 0,
                    fromDate: this.cPPeriod.fromDate.format(DATE_FORMAT),
                    toDate: this.cPPeriod.toDate.format(DATE_FORMAT),
                    costSetID: this.listCostSet,
                    status: 1
                })
                .subscribe(res => {
                    this.cPExpenseListType0 = res.body;
                    this.linksEx1 = this.parseLinks.parse(res.headers.get('link'));
                    this.totalItemsEx1 = parseInt(res.headers.get('X-Total-Count'), 10);
                    this.queryCountEx1 = this.totalItemsEx1;
                });
            console.table(this.cPExpenseListType0);
            this.generalLedgerService
                .getCPExpenseListAll({
                    fromDate: this.cPPeriod.fromDate.format(DATE_FORMAT),
                    toDate: this.cPPeriod.toDate.format(DATE_FORMAT),
                    costSetID: this.listCostSet
                })
                .subscribe(res => {
                    this.cPExpenseListTypeAll = res.body;
                    this.cPExpenseListType0All = res.body.filter(x => x.typeVoucher === 0);
                    this.cPExpenseListType1All = res.body.filter(x => x.typeVoucher === 1);
                    this.totalAmountEx1 = this.cPExpenseListType0All.reduce(function(prev, cur) {
                        return prev + cur.amount;
                    }, 0);
                    this.totalAmountEx2 = this.cPExpenseListType1All.reduce(function(prev, cur) {
                        return prev + cur.amount;
                    }, 0);
                });
        } else {
            if (this.cPExpenseListType0.length === 0) {
                this.cPExpenseListType0All = this.cPPeriod.cPExpenseList.filter(x => x.typeVoucher === 0);
                this.cPExpenseListType0 = this.cPExpenseListType0All;
                this.cPExpenseListType0.forEach(item => {
                    item.reason = item.description;
                    this.totalAmountEx1 = this.totalAmountEx1 + item.amount;
                });
                this.totalItemsEx1 = this.cPExpenseListType0.length;
            }
        }
    }

    loadAllCPExpenseList1() {
        if (this.isEdit) {
            this.generalLedgerService
                .getCPExpenseListByCostSet({
                    page: this.pageEx2 ? this.pageEx2 - 1 : 0,
                    size: this.itemsPerPageEx2 ? this.itemsPerPageEx2 : 0,
                    fromDate: this.cPPeriod.fromDate.format(DATE_FORMAT),
                    toDate: this.cPPeriod.toDate.format(DATE_FORMAT),
                    costSetID: this.listCostSet,
                    status: 2
                })
                .subscribe(res => {
                    this.cPExpenseListType1 = res.body;
                    this.linksEx2 = this.parseLinks.parse(res.headers.get('link'));
                    this.totalItemsEx2 = parseInt(res.headers.get('X-Total-Count'), 10);
                    this.queryCountEx2 = this.totalItemsEx2;
                });
            console.table(this.cPExpenseListType1);
        } else {
            if (this.cPExpenseListType1.length === 0) {
                this.cPExpenseListType1All = this.cPPeriod.cPExpenseList.filter(x => x.typeVoucher === 1);
                this.cPExpenseListType1 = this.cPExpenseListType1All;
                this.cPExpenseListType1.forEach(item => {
                    item.reason = item.description;
                    this.totalAmountEx1 = this.totalAmountEx1 + item.amount;
                });
                this.totalItemsEx2 = this.cPExpenseListType1.length;
            }
        }
    }

    loadAllCPAllocationGeneralExpense() {
        if (this.isEdit) {
            if (!this.cPAllocationGeneralExpense || this.cPAllocationGeneralExpense.length === 0) {
                this.generalLedgerService
                    .getByAllocationMethod({
                        page: this.page ? this.page - 1 : 0,
                        size: this.itemsPerPage ? this.itemsPerPage : 0,
                        fromDate: this.cPPeriod.fromDate.format(DATE_FORMAT),
                        toDate: this.cPPeriod.toDate.format(DATE_FORMAT),
                        costSetID: this.listCostSet,
                        status: 3
                    })
                    .subscribe(res => {
                        // this.cPAllocationGeneralExpense = res.body.giaThanhAllocationPoPupDTOSums;
                        this.cPAllocationGeneralExpense = res.body.giaThanhAllocationPoPupDTOAll;
                        // this.cPAllocationGeneralExpenseAll = res.body.giaThanhAllocationPoPupDTOAll;
                    });
            }
        } else {
            if (this.cPAllocationGeneralExpense.length === 0) {
                this.cPAllocationGeneralExpense = this.cPPeriod.cPAllocationGeneralExpenses;
                if (!this.cPAllocationGeneralExpense || this.cPAllocationGeneralExpense.length === 0) {
                    this.generalLedgerService
                        .getByAllocationMethod({
                            page: this.page ? this.page - 1 : 0,
                            size: this.itemsPerPage ? this.itemsPerPage : 0,
                            fromDate: this.cPPeriod.fromDate.format(DATE_FORMAT),
                            toDate: this.cPPeriod.toDate.format(DATE_FORMAT),
                            costSetID: this.listCostSet,
                            status: 3
                        })
                        .subscribe(res => {
                            this.cPAllocationGeneralExpense = res.body.giaThanhAllocationPoPupDTOAll;
                        });
                }
            }
            if (this.cPAllocationGeneralExpenseDetails.length === 0) {
                this.cPAllocationGeneralExpenseDetails = this.cPPeriod.cPAllocationGeneralExpenseDetails;
            }
        }
    }

    loadAllCPUncomplete() {
        if (this.isEdit) {
            if (!this.cPUncompletes || this.cPUncompletes.length === 0) {
                const listCostSetChecked = this.costSets.filter(x => x.checked).map(a => a.id);
                this.costSetService.findByListID({ uuids: listCostSetChecked }).subscribe((res: HttpResponse<any[]>) => {
                    this.cPUncompletes = res.body;
                });
            }
        } else {
            if (this.cPUncompletes.length === 0) {
                this.cPUncompletes = this.cPPeriod.cPUncompletes;
            }
            if (this.cPUncompleteDetails.length === 0) {
                this.cPUncompleteDetails = this.cPPeriod.cPUncompleteDetails;
            }
        }
    }

    loadCalculateCost() {
        if (this.isEdit) {
            this.cPPeriod.cPPeriodDetails = [];
            this.listCostSet.forEach(item => {
                this.cPPeriod.cPPeriodDetails.push({ costSetID: item });
            });
            const body = {
                cPAllocationGeneralExpenseDetails: this.cPAllocationGeneralExpenseDetails,
                cPUncompleteDetails: this.cPUncompleteDetails,
                cPAllocationRates: this.cPAllocationRates,
                fromDate: this.cPPeriod.fromDate.format(DATE_FORMAT),
                toDate: this.cPPeriod.toDate.format(DATE_FORMAT),
                cPExpenseLists1: this.cPExpenseListType0All,
                cPExpenseLists2: this.cPExpenseListType1All,
                cPPeriodDetails: this.cPPeriod.cPPeriodDetails
            };
            this.cpResultService.calculateCost(body).subscribe(res => {
                this.cPResult = res.body;
                if (this.cPResult) {
                    this.cPResult.forEach(item => {
                        const costSet = this.costSets.find(x => x.id === item.costSetID);
                        if (costSet) {
                            item.costSetCode = costSet.costSetCode;
                        }
                        const materialGoods = this.materialGoods.find(x => x.id === item.materialGoodsID);
                        if (materialGoods) {
                            item.materialGoodsCode = materialGoods.materialGoodsCode;
                            item.materialGoodsName = materialGoods.materialGoodsName;
                        }
                    });
                }
            });
        } else {
            if (this.cPResult.length === 0) {
                this.cPResult = this.cPPeriod.cPResults;
            }
        }
    }

    save() {
        event.preventDefault();
        if (this.indexTab !== 7) {
            return;
        }
        if (moment(this.cPPeriod.toDate) < moment(this.cPPeriod.fromDate)) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.mBDeposit.fromDateMustBeLessThanToDate'),
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
        this.isEdit = true;
        this.fillToSave();
        if (this.cPPeriod.id !== undefined) {
            this.subscribeToSaveResponse(this.cPPeriodService.update(this.cPPeriod));
        } else {
            this.subscribeToSaveResponse(this.cPPeriodService.create(this.cPPeriod));
        }
    }

    fillToSave() {
        this.cPPeriod.cPExpenseList = [];
        this.cPPeriod.cPAllocationGeneralExpenses = [];
        this.cPPeriod.cPAllocationGeneralExpenseDetails = [];
        this.cPPeriod.cPUncompletes = [];
        this.cPPeriod.cPUncompleteDetails = [];
        this.cPPeriod.cPAllocationRates = [];
        this.cPPeriod.cPResults = [];
        this.cPPeriod.cPAcceptances = [];
        this.cPPeriod.cPAcceptanceDetails = [];
        if (this.cPPeriod.id) {
            this.cPPeriod.cPExpenseList.push(...this.cPExpenseListType0);
            this.cPPeriod.cPExpenseList.push(...this.cPExpenseListType1);
        } else {
            this.cPExpenseListTypeAll.forEach(item => {
                item.date = item.date.format(DATE_FORMAT);
                item.postedDate = item.postedDate.format(DATE_FORMAT);
            });
            this.cPPeriod.cPExpenseList.push(...this.cPExpenseListTypeAll);
        }
        // this.cPAllocationGeneralExpense.forEach(item => {
        //     this.cPAllocationGeneralExpenseAll
        //         .filter(x => x.accountNumber === item.accountNumber && x.expenseItemID === item.expenseItemID)
        //         .forEach(allocation => {
        //             allocation.allocatedRate = item.allocatedRate;
        //             allocation.allocatedAmount = item.allocatedRate * allocation.unallocatedAmount / 100;
        //             allocation.allocationMethod = item.allocationMethod;
        //         });
        // });
        if (this.cPAllocationGeneralExpenseDetails && this.cPAllocationGeneralExpenseDetails.length > 0) {
            this.cPPeriod.cPAllocationGeneralExpenses.push(...this.cPAllocationGeneralExpense);
            this.cPPeriod.cPAllocationGeneralExpenseDetails.push(...this.cPAllocationGeneralExpenseDetails);
        }
        this.cPPeriod.cPUncompletes.push(...this.cPUncompletes);
        this.cPPeriod.cPUncompleteDetails.push(...this.cPUncompleteDetails);
        this.cPPeriod.cPAllocationRates.push(...this.cPAllocationRates);
        this.cPPeriod.cPResults.push(...this.cPResult);
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICPPeriod>>) {
        result.subscribe((res: HttpResponse<ICPPeriod>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isEdit = false;
        this.isSaved = true;
        this.toasService.success(
            this.translateService.instant('ebwebApp.mCReceipt.home.saveSuccess'),
            this.translateService.instant('ebwebApp.mBDeposit.message')
        );
    }

    private onSaveError() {
        this.isEdit = false;
    }

    get cPPeriod() {
        return this._cPPeriod;
    }

    set cPPeriod(cPPeriod: ICPPeriod) {
        this._cPPeriod = cPPeriod;
    }

    isCheckAll() {
        return this.costSets.every(item => item.checked) && this.costSets.length;
    }

    checkAll() {
        if (this.isEdit) {
            const isCheck = this.costSets.every(item => item.checked) && this.costSets.length;
            this.costSets.forEach(item => (item.checked = !isCheck));

            if (!isCheck) {
                for (let j = 0; j < this.costSets.length; j++) {
                    let isPush = true;
                    for (let i = 0; i < this.listCostSet.length; i++) {
                        if (this.listCostSet[i] === this.costSets[j].id) {
                            isPush = true;
                        }
                    }
                    if (isPush) {
                        this.listCostSet.push(this.costSets[j].id);
                    }
                }
            } else {
                for (let j = 0; j < this.costSets.length; j++) {
                    for (let i = 0; i < this.listCostSet.length; i++) {
                        if (this.listCostSet[i] === this.costSets[j].id) {
                            this.listCostSet.splice(i, 1);
                            i--;
                        }
                    }
                }
            }
        }
    }

    check(costSet: ICostSet) {
        if (this.isEdit) {
            costSet.checked = !costSet.checked;
            if (costSet.checked) {
                this.listCostSet.push(costSet.id);
            } else {
                for (let i = 0; i < this.listCostSet.length; i++) {
                    if (this.listCostSet[i] === costSet.id) {
                        this.listCostSet.splice(i, 1);
                        i--;
                    }
                }
            }
        }
    }

    selectedItemPerPage() {
        if (this.indexTab === 1 && this.isEdit) {
            this.loadAllCostSets();
        }
    }

    loadPage(page: any) {
        if (this.indexTab === 1 && this.isEdit) {
            this.loadAllCostSets();
        }
    }

    selectedItemPerPageEx1() {
        if (this.indexTab === 2 && this.isEdit) {
            this.loadAllCPExpenseList0();
        }
    }

    loadPageEx1(page: any) {
        if (this.indexTab === 2 && this.isEdit) {
            this.loadAllCPExpenseList0();
        }
    }

    selectedItemPerPageEx2() {
        if (this.indexTab === 3 && this.isEdit) {
            this.loadAllCPExpenseList1();
        }
    }

    loadPageEx2(page: any) {
        if (this.indexTab === 3 && this.isEdit) {
            this.loadAllCPExpenseList1();
        }
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    closeForm() {
        event.preventDefault();
        if (this.cPPeriod.type === this.PHUONG_PHAP_HE_SO) {
            this.router.navigate(['./gia-thanh/he-so']);
        } else {
            this.router.navigate(['./gia-thanh/ty-le']);
        }
    }

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.cPPeriod.fromDate = moment(this.objTimeLine.dtBeginDate);
            this.cPPeriod.toDate = moment(this.objTimeLine.dtEndDate);
            this.cPPeriod.name =
                'Từ ngày ' + this.cPPeriod.fromDate.format('DD/MM/YYYY') + ' Đến ngày ' + this.cPPeriod.toDate.format('DD/MM/YYYY');
        }
    }

    changeFromDate() {
        if (moment(this.cPPeriod.toDate) < moment(this.cPPeriod.fromDate)) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.mBDeposit.fromDateMustBeLessThanToDate'),
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
            return;
        } else {
            this.cPPeriod.name =
                'Từ ngày ' + this.cPPeriod.fromDate.format('DD/MM/YYYY') + ' Đến ngày ' + this.cPPeriod.toDate.format('DD/MM/YYYY');
        }
    }

    changeToDate() {
        if (moment(this.cPPeriod.toDate) < moment(this.cPPeriod.fromDate)) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.mBDeposit.fromDateMustBeLessThanToDate'),
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
            return;
        } else {
            this.cPPeriod.name =
                'Từ ngày ' + this.cPPeriod.fromDate.format('DD/MM/YYYY') + ' Đến ngày ' + this.cPPeriod.toDate.format('DD/MM/YYYY');
        }
    }

    getCPPeriodName() {
        return this.cPPeriod.name;
    }

    selectAllocationMethod($event: NgbTabChangeEvent) {
        if ($event.nextId === 'allocation') {
            $event.preventDefault();
            let sumNVLTT = 0;
            let sumNhanCongTT = 0;
            let sumChiPhiTT = 0;
            const listCostSetChecked = this.costSets.filter(x => x.checked);
            if (this.cPExpenseListType0All && this.cPExpenseListType0All.length > 0) {
                for (let j = 0; j < this.cPExpenseListType0All.length; j++) {
                    if (
                        this.cPExpenseListType0All[j].accountNumber.startsWith('621') ||
                        (this.cPExpenseListType0All[j].accountNumber.startsWith('154') &&
                            this.cPExpenseListType0All[j].expenseItemType === 0)
                    ) {
                        sumNVLTT += this.cPExpenseListType0All[j].amount;
                    }
                    if (
                        this.cPExpenseListType0All[j].accountNumber.startsWith('622') ||
                        (this.cPExpenseListType0All[j].accountNumber.startsWith('154') &&
                            this.cPExpenseListType0All[j].expenseItemType === 1)
                    ) {
                        sumNhanCongTT += this.cPExpenseListType0All[j].amount;
                    }
                }
                sumChiPhiTT = sumNVLTT + sumNhanCongTT;
                listCostSetChecked.forEach(item => {
                    item.totalAmountExp0 = this.cPExpenseListType0All
                        .filter(x => (x.expenseItemType === 0 || x.accountNumber.startsWith('621')) && x.costSetID === item.id)
                        .reduce(function(prev, cur) {
                            return prev + cur.amount;
                        }, 0);
                    item.totalAmountExp1 = this.cPExpenseListType0All
                        .filter(x => (x.expenseItemType === 1 || x.accountNumber.startsWith('622')) && x.costSetID === item.id)
                        .reduce(function(prev, cur) {
                            return prev + cur.amount;
                        }, 0);
                });
            }
            this.cPAllocationGeneralExpenseDetails = [];
            for (let i = 0; i < this.cPAllocationGeneralExpense.length; i++) {
                if (this.cPAllocationGeneralExpense[i].allocationMethod === TieuThucPhanBo.NVLTT && sumNVLTT) {
                    listCostSetChecked.forEach(item => {
                        const cpAllocationDetail: ICPAllocationGeneralExpenseDetails = {};
                        cpAllocationDetail.cPAllocationGeneralExpenseID = this.cPAllocationGeneralExpense[i].id;
                        cpAllocationDetail.costSetID = item.id;
                        cpAllocationDetail.costSetCode = item.costSetCode;
                        cpAllocationDetail.costSetName = item.costSetCode;
                        cpAllocationDetail.accountNumber = this.cPAllocationGeneralExpense[i].accountNumber;
                        cpAllocationDetail.expenseItemID = this.cPAllocationGeneralExpense[i].expenseItemID;
                        cpAllocationDetail.expenseItemCode = this.cPAllocationGeneralExpense[i].expenseItemCode;
                        cpAllocationDetail.expenseItemType = this.cPAllocationGeneralExpense[i].expenseItemType;
                        cpAllocationDetail.allocatedRate = sumNVLTT ? item.totalAmountExp0 / sumNVLTT * 100 : 0;
                        cpAllocationDetail.allocatedAmount =
                            this.cPAllocationGeneralExpense[i].allocatedAmount * cpAllocationDetail.allocatedRate / 100;
                        this.cPAllocationGeneralExpenseDetails.push(cpAllocationDetail);
                    });
                } else if (this.cPAllocationGeneralExpense[i].allocationMethod === TieuThucPhanBo.NHAN_CONG_TT && sumNhanCongTT) {
                    listCostSetChecked.forEach(item => {
                        const cpAllocationDetail: ICPAllocationGeneralExpenseDetails = {};
                        cpAllocationDetail.cPAllocationGeneralExpenseID = this.cPAllocationGeneralExpense[i].id;
                        cpAllocationDetail.costSetID = item.id;
                        cpAllocationDetail.costSetCode = item.costSetCode;
                        cpAllocationDetail.costSetName = item.costSetCode;
                        cpAllocationDetail.accountNumber = this.cPAllocationGeneralExpense[i].accountNumber;
                        cpAllocationDetail.expenseItemID = this.cPAllocationGeneralExpense[i].expenseItemID;
                        cpAllocationDetail.expenseItemCode = this.cPAllocationGeneralExpense[i].expenseItemCode;
                        cpAllocationDetail.expenseItemType = this.cPAllocationGeneralExpense[i].expenseItemType;
                        cpAllocationDetail.allocatedRate = sumNhanCongTT ? item.totalAmountExp1 / sumNhanCongTT * 100 : 0;
                        cpAllocationDetail.allocatedAmount =
                            this.cPAllocationGeneralExpense[i].allocatedAmount * cpAllocationDetail.allocatedRate / 100;
                        this.cPAllocationGeneralExpenseDetails.push(cpAllocationDetail);
                    });
                } else if (this.cPAllocationGeneralExpense[i].allocationMethod === TieuThucPhanBo.CHI_PHI_TT && sumChiPhiTT) {
                    listCostSetChecked.forEach(item => {
                        const cpAllocationDetail: ICPAllocationGeneralExpenseDetails = {};
                        cpAllocationDetail.cPAllocationGeneralExpenseID = this.cPAllocationGeneralExpense[i].id;
                        cpAllocationDetail.costSetID = item.id;
                        cpAllocationDetail.costSetCode = item.costSetCode;
                        cpAllocationDetail.costSetName = item.costSetCode;
                        cpAllocationDetail.accountNumber = this.cPAllocationGeneralExpense[i].accountNumber;
                        cpAllocationDetail.expenseItemID = this.cPAllocationGeneralExpense[i].expenseItemID;
                        cpAllocationDetail.expenseItemCode = this.cPAllocationGeneralExpense[i].expenseItemCode;
                        cpAllocationDetail.expenseItemType = this.cPAllocationGeneralExpense[i].expenseItemType;
                        cpAllocationDetail.allocatedRate =
                            item.totalAmountExp0 + item.totalAmountExp1
                                ? (item.totalAmountExp0 + item.totalAmountExp1) / sumChiPhiTT * 100
                                : 0;
                        cpAllocationDetail.allocatedAmount =
                            this.cPAllocationGeneralExpense[i].allocatedAmount * cpAllocationDetail.allocatedRate / 100;
                        this.cPAllocationGeneralExpenseDetails.push(cpAllocationDetail);
                    });
                } else if (this.cPAllocationGeneralExpense[i].allocationMethod === TieuThucPhanBo.DINH_MUC) {
                    this.cpAllocationQuantumService.findByCostSetID(this.listCostSet).subscribe(res => {
                        const listCPAllocationQuantum = res.body;
                        if (listCPAllocationQuantum && listCPAllocationQuantum.length > 0) {
                            const totalCost = listCPAllocationQuantum.reduce(function(prev, cur) {
                                return prev + cur.totalCostAmount;
                            }, 0);
                            listCostSetChecked.forEach(item => {
                                const cpAllocationDetail: ICPAllocationGeneralExpenseDetails = {};
                                const cost = listCPAllocationQuantum.find(x => x.id === item.id).totalCostAmount;
                                cpAllocationDetail.cPAllocationGeneralExpenseID = this.cPAllocationGeneralExpense[i].id;
                                cpAllocationDetail.costSetID = item.id;
                                cpAllocationDetail.costSetCode = item.costSetCode;
                                cpAllocationDetail.costSetName = item.costSetCode;
                                cpAllocationDetail.accountNumber = this.cPAllocationGeneralExpense[i].accountNumber;
                                cpAllocationDetail.expenseItemID = this.cPAllocationGeneralExpense[i].expenseItemID;
                                cpAllocationDetail.expenseItemCode = this.cPAllocationGeneralExpense[i].expenseItemCode;
                                cpAllocationDetail.expenseItemType = this.cPAllocationGeneralExpense[i].expenseItemType;
                                cpAllocationDetail.allocatedRate = cost / totalCost * 100;
                                cpAllocationDetail.allocatedAmount =
                                    this.cPAllocationGeneralExpense[i].allocatedAmount * cpAllocationDetail.allocatedRate / 100;
                                this.cPAllocationGeneralExpenseDetails.push(cpAllocationDetail);
                            });
                        }
                    });
                }
            }
            console.table(this.cPAllocationGeneralExpenseDetails);
        }
    }

    evaluate($event: NgbTabChangeEvent) {
        if ($event.nextId === 'unCompleteDefine') {
            $event.preventDefault();
            if (this.isEdit) {
                const cPUncompletesDTOs = [];
                const group = this.cPUncompletes.reduce((g: any, cPUncomplete) => {
                    g[cPUncomplete.costSetID] = g[cPUncomplete.costSetID] || [];
                    g[cPUncomplete.costSetID].push(cPUncomplete);
                    return g;
                }, {});
                const groupData = Object.keys(group).map(a => {
                    return {
                        costSetID: a,
                        cPUncompletes: group[a]
                    };
                });
                for (let i = 0; i < groupData.length; i++) {
                    const cPUncompletesDTO: any = {};
                    cPUncompletesDTO.quantumID = groupData[i].cPUncompletes[0].quantumID;
                    cPUncompletesDTO.quantumCode = groupData[i].cPUncompletes[0].quantumCode;
                    cPUncompletesDTO.quantumName = groupData[i].cPUncompletes[0].quantumName;
                    cPUncompletesDTO.costSetID = groupData[i].cPUncompletes[0].costSetID;
                    cPUncompletesDTO.quantityClosing = groupData[i].cPUncompletes.reduce(function(prev, cur) {
                        return prev + cur.quantityClosing;
                    }, 0);
                    const quantity = groupData[i].cPUncompletes.reduce(function(prev, cur) {
                        return prev + cur.quantityClosing * cur.rate / 100;
                    }, 0);
                    cPUncompletesDTO.rate = quantity / cPUncompletesDTO.quantityClosing * 100;
                    cPUncompletesDTOs.push(cPUncompletesDTO);
                }
                const body = {
                    cPUncompletes: cPUncompletesDTOs,
                    cPExpenseLists1: this.cPExpenseListType0All,
                    cPExpenseLists2: this.cPExpenseListType1All,
                    fromDate: this.cPPeriod.fromDate.format(DATE_FORMAT),
                    toDate: this.cPPeriod.toDate.format(DATE_FORMAT),
                    method: this.method,
                    cPAllocationGeneralExpenseDetails: this.cPAllocationGeneralExpenseDetails
                };
                this.cpUncompleteDetailsService.evaluate(body).subscribe(res => {
                    this.cPUncompleteDetails = res.body;
                    if (this.cPUncompleteDetails) {
                        this.cPUncompleteDetails.forEach(item => {
                            const costSet = this.costSets.find(x => x.id === item.costSetID);
                            if (costSet) {
                                item.costSetCode = costSet.costSetCode;
                                item.costSetName = costSet.costSetName;
                            }
                        });
                    }
                });
            }
        }
    }

    changeQuantity(detail) {
        if (detail.totalQuantity) {
            detail.unitPrice = this.utilsService.round(detail.totalCostAmount / detail.totalQuantity, this.account.systemOption, 1);
        } else {
            detail.totalQuantity = 0;
            detail.unitPrice = 0;
        }
    }

    changeAllocationRate(detail) {
        if (detail.allocatedRate && detail.allocatedRate > 100) {
            this.toasService.error(this.translateService.instant('ebwebApp.cPPeriod.messageWarning.errorAllocationRate'));
            detail.allocatedRate = 0;
        }
        if (detail.allocatedRate && detail.allocatedRate <= 100) {
            detail.allocatedAmount = this.utilsService.round(
                detail.allocatedRate * detail.unallocatedAmount / 100,
                this.account.systemOption,
                7
            );
        } else {
            detail.allocatedAmount = 0;
        }
    }

    ngAfterViewChecked(): void {
        this.disableInput();
    }

    updateIWPrice() {
        if (this.isSaved) {
            if (this.cPResult.some(x => x.unitPrice < 0)) {
                this.toasService.error(this.translateService.instant('ebwebApp.cPPeriod.messageWarning.negativeUnitPrice'));
                return;
            }
            if (this.cPResult.filter(x => x.unitPrice === 0).length === this.cPResult.length) {
                this.toasService.error(this.translateService.instant('ebwebApp.cPPeriod.messageWarning.zeroUnitPrice'));
                return;
            }
            this.cPResult.forEach(item => {
                item.fromDate = this.cPPeriod.fromDate;
                item.toDate = this.cPPeriod.toDate;
                item.type = this.cPPeriod.type;
            });
            this.repositoryLedgerService.updateIWPriceFromCost(this.cPResult).subscribe(
                res => {
                    if (res.body) {
                        this.toasService.success(this.translateService.instant('ebwebApp.cPPeriod.messageWarning.updateIWUnitPrice'));
                    }
                },
                error => {
                    this.toasService.error(this.translateService.instant('ebwebApp.cPPeriod.messageWarning.failUpdateIWUnitPrice'));
                }
            );
        }
    }

    updateOWPrice() {
        if (this.isSaved) {
            if (this.cPResult.some(x => x.unitPrice < 0)) {
                this.toasService.error(this.translateService.instant('ebwebApp.cPPeriod.messageWarning.negativeUnitPrice'));
                return;
            }
            if (this.cPResult.filter(x => x.unitPrice === 0).length === this.cPResult.length) {
                this.toasService.error(this.translateService.instant('ebwebApp.cPPeriod.messageWarning.zeroUnitPrice'));
                return;
            }
            this.cPResult.forEach(item => {
                item.fromDate = this.cPPeriod.fromDate;
                item.toDate = this.cPPeriod.toDate;
                item.type = this.cPPeriod.type;
            });
            this.repositoryLedgerService.updateOWPriceFromCost(this.cPResult).subscribe(
                res => {
                    if (res.body) {
                        this.toasService.success(this.translateService.instant('ebwebApp.cPPeriod.messageWarning.updateOWUnitPrice'));
                    }
                },
                error => {
                    this.toasService.error(this.translateService.instant('ebwebApp.cPPeriod.messageWarning.failUpdateOWUnitPrice'));
                }
            );
        }
    }

    sumCPExpenseListType0(prop) {
        let total = 0;
        for (let i = 0; i < this.cPExpenseListType0.length; i++) {
            total += this.cPExpenseListType0[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    sumCPExpenseListType1(prop) {
        let total = 0;
        for (let i = 0; i < this.cPExpenseListType1.length; i++) {
            total += this.cPExpenseListType1[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    sumCPAllocation(prop) {
        let total = 0;
        if (this.cPAllocationGeneralExpense) {
            for (let i = 0; i < this.cPAllocationGeneralExpense.length; i++) {
                total += this.cPAllocationGeneralExpense[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    sumCPAllocationDetail(prop) {
        let total = 0;
        if (this.cPAllocationGeneralExpenseDetails) {
            for (let i = 0; i < this.cPAllocationGeneralExpenseDetails.length; i++) {
                total += this.cPAllocationGeneralExpenseDetails[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    sumcPUncomplete(prop) {
        let total = 0;
        if (this.cPUncompletes) {
            for (let i = 0; i < this.cPUncompletes.length; i++) {
                total += this.cPUncompletes[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    sumcPUncompleteDetail(prop) {
        let total = 0;
        if (this.cPUncompleteDetails) {
            for (let i = 0; i < this.cPUncompleteDetails.length; i++) {
                total += this.cPUncompleteDetails[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    sumcPResult(prop) {
        let total = 0;
        if (this.cPResult) {
            for (let i = 0; i < this.cPResult.length; i++) {
                total += this.cPResult[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    sumCPAllocationRates(prop) {
        let total = 0;
        if (this.cPAllocationRates) {
            for (let i = 0; i < this.cPAllocationRates.length; i++) {
                total += this.cPAllocationRates[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    getCostSetCode(id) {
        if (this.costSets && id) {
            const costSet = this.costSets.find(n => n.id === id.toLowerCase());
            if (costSet) {
                return costSet.costSetCode;
            }
        }
    }

    getMaterialGoodsCode(id) {
        if (this.materialGoods && id) {
            const materialGood = this.materialGoods.find(n => n.id === id.toLowerCase());
            if (materialGood) {
                return materialGood.materialGoodsCode;
            }
        }
    }

    getMaterialGoodsName(id) {
        if (this.materialGoods && id) {
            const materialGood = this.materialGoods.find(n => n.id === id.toLowerCase());
            if (materialGood) {
                return materialGood.materialGoodsName;
            }
        }
    }

    selectChangeIsStandardItem(detail) {
        detail.coefficient = 1;
        detail.quantityStandard = detail.quantity;
        this.cPAllocationRates.filter(x => x.costSetID === detail.costSetID && x.key !== detail.key).forEach(item => {
            item.isStandardItem = false;
        });
        this.cPAllocationRates.filter(x => !x.isStandardItem).forEach(item => {
            const cPAllocationRateStandard = this.cPAllocationRates.find(a => a.costSetID === item.costSetID && a.isStandardItem);
            item.coefficient = item.priceQuantum / cPAllocationRateStandard.priceQuantum;
            item.quantityStandard = item.coefficient * item.quantity;
        });
        this.cPAllocationRates.forEach(item => {
            const totalQuantityStandard = this.cPAllocationRates.filter(x => x.costSetID === item.costSetID).reduce(function(prev, cur) {
                return prev + cur.quantityStandard;
            }, 0);
            item.allocatedRate = item.quantityStandard / totalQuantityStandard * 100;
        });
    }

    selectChangeQuantity(detail) {
        if (this.cPAllocationRates && this.cPAllocationRates.length > 0) {
            detail.allocationMethod = 1;
            detail.priceStandard = 1;
            detail.allocationStandard = detail.quantity * detail.priceQuantum;
            this.cPAllocationRates.filter(x => x.costSetID === detail.costSetID).forEach(item => {
                const totalAllocationStandard = this.cPAllocationRates
                    .filter(x => x.costSetID === item.costSetID)
                    .reduce(function(prev, cur) {
                        return prev + cur.allocationStandard;
                    }, 0);
                item.allocatedRate = totalAllocationStandard ? item.allocationStandard / totalAllocationStandard * 100 : 0;
            });
        }
    }

    closePopUp() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    showUpdateOWPrice() {
        if (this.isSaved) {
            this.modalRef = this.modalService.open(this.questionUpdateOWPrice, { size: 'lg', backdrop: 'static' });
        }
    }
}
