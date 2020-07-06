import { AfterViewChecked, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
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
import { NgbActiveModal, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { JhiParseLinks } from 'ng-jhipster';
import { DATE_FORMAT, ITEMS_PER_PAGE_EXLIST } from 'app/shared';
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
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { CPAllocationRateService } from 'app/entities/cp-allocation-rate';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { ICPAllocationRate } from 'app/shared/model/cp-allocation-rate.model';
import { CPAcceptanceDetailsService } from 'app/giathanh/phuong_phap_cong_trinh_vu_viec/cp-acceptance-details.service';
import { AccountListService } from 'app/danhmuc/account-list';
import { PpCongTrinhVuViecService } from 'app/giathanh/phuong_phap_cong_trinh_vu_viec/pp-cong-trinh-vu-viec.service';

@Component({
    selector: 'eb-pp-cong-trinh-vu-viec-update',
    templateUrl: './pp-cong-trinh-vu-viec-update.component.html',
    styleUrls: ['./pp-cong-trinh-vu-viec-update.component.css']
})
export class PpCongTrinhVuViecUpdateComponent extends BaseComponent implements OnInit, AfterViewChecked {
    private _cPPeriod: ICPPeriod;
    type: string;
    PHUONG_PHAP_CTVV = GIA_THANH.PHUONG_PHAP_CONG_TRINH_VU_VIEC;
    PHUONG_PHAP_DON_HANG = GIA_THANH.PHUONG_PHAP_DON_HANG;
    isEdit: boolean;
    costSets: ICostSet[];
    costSetAll: ICostSet[];
    debitAccountList: any[];
    debitAccount: string;
    creditAccountList: any[];
    creditAccount: string;
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
    cPAcceptanceDetails: any[];
    cPAcceptances: any[];
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
        { id: 3, name: 'Doanh số' }
    ];
    account: any;
    listType: any;
    isSaved: boolean;
    isAcceptance: boolean;

    constructor(
        private cPPeriodService: PpCongTrinhVuViecService,
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
        private cpAcceptanceDetailsService: CPAcceptanceDetailsService,
        private cpResultService: CPResultService,
        private repositoryLedgerService: RepositoryLedgerService,
        private cPAllocationRateService: CPAllocationRateService,
        private materialGoodsSerivce: MaterialGoodsService,
        private accountListService: AccountListService
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
        this.isAcceptance = false;
        this.cPPeriod = {};
        this.cPExpenseList = {};
        this.listCostSet = [];
        this.cPExpenseListType0 = [];
        this.cPExpenseListType1 = [];
        this.cPAllocationGeneralExpense = [];
        this.cPAllocationGeneralExpenseDetails = [];
        this.cPAcceptanceDetails = [];
        this.cPAcceptances = [];
        this.debitAccountList = [];
        this.creditAccountList = [];
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
                this.accountListService.getAccountListsActive().subscribe((res: HttpResponse<any[]>) => {
                    this.debitAccountList = res.body.filter(x => !x.isParentNode);
                    this.creditAccountList = this.debitAccountList;
                    this.debitAccount = '632';
                    this.creditAccount = '154';
                });
                this.cPPeriod = cPPeriod;
                if (!this.cPPeriod.id) {
                    this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
                    if (window.location.href.includes('don-hang')) {
                        this.cPPeriod.type = this.PHUONG_PHAP_DON_HANG;
                    } else if (window.location.href.includes('cong-trinh-vu-viec')) {
                        this.cPPeriod.type = this.PHUONG_PHAP_CTVV;
                    }
                    this.loadAllCostSets();
                } else {
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
                    if (window.location.href.includes('acceptance')) {
                        this.isAcceptance = true;
                        this.indexTab = 5;
                        this.loadAllCPAcceptanceDetail();
                    }
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
                this.loadAllCPAcceptanceDetail();
            }
        }
    }

    loadAllCostSets() {
        this.costSetService
            .getCostSetsByType({
                type: this.cPPeriod.type === this.PHUONG_PHAP_CTVV ? 1 : 0,
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
                this.cPExpenseListType0 = this.cPPeriod.cPExpenseList.filter(x => x.typeVoucher === 0);
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
                this.cPExpenseListType1 = this.cPPeriod.cPExpenseList.filter(x => x.typeVoucher === 1);
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
                        // this.cPAllocationGeneralExpenseAll = res.body.giaThanhAllocationPoPupDTOAll;
                        this.cPAllocationGeneralExpense = res.body.giaThanhAllocationPoPupDTOAll;
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

    loadAllCPAcceptanceDetail() {
        if (this.isEdit) {
            this.cPPeriod.cPPeriodDetails = [];
            this.listCostSet.forEach(item => {
                this.cPPeriod.cPPeriodDetails.push({ costSetID: item });
            });
            const body = {
                cPPeriodDetails: this.cPPeriod.cPPeriodDetails,
                fromDate: this.cPPeriod.fromDate.format(DATE_FORMAT),
                toDate: this.cPPeriod.toDate.format(DATE_FORMAT),
                cPExpenseLists1: this.cPExpenseListType0All,
                cPExpenseLists2: this.cPExpenseListType1All,
                cPAllocationGeneralExpenseDetails: this.cPAllocationGeneralExpenseDetails
            };
            this.cpAcceptanceDetailsService.evaluate(body).subscribe(res => {
                this.cPAcceptanceDetails = res.body;
                if (this.cPAcceptanceDetails) {
                    this.cPAcceptanceDetails.forEach(item => {
                        const costSet = this.costSets.find(x => x.id === item.costSetID);
                        if (costSet) {
                            item.costSetCode = costSet.costSetCode;
                            item.costSetName = costSet.costSetName;
                        }
                    });
                }
            });
        } else {
            this.cPPeriod.cPAcceptanceDetails.forEach(item => {
                item.acceptedRate = 100;
                item.totalAcceptedAmount = item.amount;
            });
            this.cPAcceptanceDetails = this.cPPeriod.cPAcceptanceDetails;
        }
    }

    save() {
        event.preventDefault();
        if (this.indexTab !== 5) {
            return;
        }
        if (moment(this.cPPeriod.toDate) < moment(this.cPPeriod.fromDate)) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.mBDeposit.fromDateMustBeLessThanToDate'),
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
        if (!this.creditAccount) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.cPPeriod.messageWarning.nullCreditAccount'),
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
        if (!this.debitAccount) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.cPPeriod.messageWarning.nullDebitAccount'),
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
        if (!this.cPPeriod.id) {
            this.cPPeriod.cPAcceptanceDetails.push(...this.cPAcceptanceDetails);
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICPPeriod>>) {
        result.subscribe(
            (res: HttpResponse<ICPPeriod>) => {
                this.cPPeriod = res.body;
                this.onSaveSuccess();
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isEdit = false;
        this.isSaved = true;
        this.toasService.success(
            this.translateService.instant('ebwebApp.mCReceipt.home.saveSuccess'),
            this.translateService.instant('ebwebApp.mBDeposit.message')
        );
    }

    accepted() {
        if (this.cPAcceptanceDetails.some(x => x.amount > 0)) {
            this.cPPeriod.creditAccount = this.creditAccount;
            this.cPPeriod.debitAccount = this.debitAccount;
            this.cPPeriod.cPAcceptances = [];
            this.cPPeriod.cPAcceptanceDetails = this.cPAcceptanceDetails;
            this.cPPeriodService.accepted(this.cPPeriod).subscribe(res => {
                this.toasService.success(
                    this.translateService.instant('ebwebApp.cPPeriod.CPAcceptanceDetail.acceptedSuccess'),
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
                this.cPAcceptanceDetails.forEach(item => {
                    item.amount = item.amount - item.totalAcceptedAmount;
                    item.acceptedRate = 100;
                    item.totalAcceptedAmount = item.amount;
                });
            });
        } else {
            this.toasService.error(this.translateService.instant('ebwebApp.cPPeriod.messageWarning.noAcceptanceAmount'));
        }
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
        if (this.cPPeriod.type === this.PHUONG_PHAP_CTVV) {
            this.router.navigate(['./gia-thanh/cong-trinh-vu-viec']);
        } else {
            this.router.navigate(['./gia-thanh/don-hang']);
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
            if (this.isEdit) {
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
                    } else if (this.cPAllocationGeneralExpense[i].allocationMethod === TieuThucPhanBo.DOANH_THU) {
                        const costSet = {
                            costSetIDs: this.listCostSet,
                            fromDate: this.cPPeriod.fromDate.format(DATE_FORMAT),
                            toDate: this.cPPeriod.toDate.format(DATE_FORMAT)
                        };
                        this.costSetService.findRevenueByCostSetID(costSet).subscribe(res => {
                            const costSetDTO = res.body;
                            if (costSetDTO && costSetDTO.length > 0) {
                                const totalCost = costSetDTO.reduce(function(prev, cur) {
                                    return prev + cur.revenueAmount;
                                }, 0);
                                listCostSetChecked.forEach(item => {
                                    const cpAllocationDetail: ICPAllocationGeneralExpenseDetails = {};
                                    const cost = costSetDTO.find(x => x.costSetID === item.id).revenueAmount;
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
            }
            console.table(this.cPAllocationGeneralExpenseDetails);
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

    changeAcceptedRate(detail) {
        if (detail.acceptedRate && detail.acceptedRate > 100) {
            this.toasService.error(this.translateService.instant('ebwebApp.cPPeriod.messageWarning.errorAllocationRate'));
            detail.acceptedRate = 0;
        }
        if (detail.acceptedRate && detail.acceptedRate <= 100) {
            detail.totalAcceptedAmount = this.utilsService.round(detail.acceptedRate * detail.amount / 100, this.account.systemOption, 7);
        } else {
            detail.totalAcceptedAmount = 0;
        }
    }

    ngAfterViewChecked(): void {
        this.disableInput();
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

    sumCPAcceptanceDetail(prop) {
        let total = 0;
        if (this.cPAcceptanceDetails) {
            for (let i = 0; i < this.cPAcceptanceDetails.length; i++) {
                total += this.cPAcceptanceDetails[i][prop];
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
}
