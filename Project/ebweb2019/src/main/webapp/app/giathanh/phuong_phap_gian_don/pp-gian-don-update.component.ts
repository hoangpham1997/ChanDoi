import { AfterViewChecked, Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ICPPeriod } from 'app/shared/model/cp-period.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { ICPAllocationGeneralExpenseDetails } from 'app/shared/model/cp-allocation-general-expense-details.model';
import { CostSetService } from 'app/entities/cost-set';
import { ICPPeriodDetails } from 'app/shared/model/cp-period-details.model';
import { GIA_THANH, PPDanhGiaDoDang, TieuThucPhanBo, TypeID } from 'app/app.constants';
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
import { forEach } from '@angular/router/src/utils/collection';

@Component({
    selector: 'eb-cp-period-update',
    templateUrl: './pp-gian-don-update.component.html',
    styleUrls: ['./pp-gian-don-update.component.css']
})
export class PpGianDonUpdateComponent extends BaseComponent implements OnInit, AfterViewChecked {
    @ViewChild('questionUpdateOWPrice') questionUpdateOWPrice;
    private _cPPeriod: ICPPeriod;
    type: string;
    PHUONG_PHAP_GIAN_DON = GIA_THANH.PHUONG_PHAP_GIAN_DON;
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
        this.isEdit = true;
        this.totalAmountEx1 = 0;
        this.totalAmountEx2 = 0;
        this.costSets = [];
        this.indexTab = 1;
        this.isSaved = false;
        this.method = PPDanhGiaDoDang.SP_HOAN_THANH_TUONG_DUONG;
        this.cPPeriod = {};
        this.cPExpenseList = {};
        this.listCostSet = [];
        this.cPExpenseListType0 = [];
        this.cPExpenseListType1 = [];
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
                this.cPPeriod = cPPeriod;
                if (!this.cPPeriod.id) {
                    this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
                    this.cPPeriod.type = this.PHUONG_PHAP_GIAN_DON;
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
                this.loadCalculateCost();
            }
        }
    }

    loadAllCostSets() {
        this.costSetService
            .getCostSetsByType({
                type: 4,
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

    loadAllCPUncomplete() {
        if (this.isEdit) {
            if (!this.cPUncompletes || this.cPUncompletes.length === 0) {
                this.cPUncompletes = [];
                const listCostSetChecked = this.costSets.filter(x => x.checked);
                listCostSetChecked.forEach(item => {
                    this.cPUncompletes.push({
                        quantumID: item.materialGoodsID,
                        quantumCode: item.costSetCode,
                        quantumName: item.costSetName,
                        quantityClosing: 0,
                        rate: 0,
                        costSetID: item.id,
                        costSetCode: item.costSetCode
                    });
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
                            item.costSetName = costSet.costSetName;
                            item.materialGoodsID = costSet.materialGoodsID;
                            item.materialGoodsCode = costSet.costSetCode;
                            item.materialGoodsName = costSet.costSetName;
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
        if (this.indexTab !== 6) {
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
        this.cPPeriod.cPResults = [];
        this.cPPeriod.cPAllocationRates = [];
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
        this.router.navigate(['./gia-thanh/gian-don']);
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
                if (this.cPAllocationGeneralExpense[i].allocationMethod === TieuThucPhanBo.NVLTT) {
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
                } else if (this.cPAllocationGeneralExpense[i].allocationMethod === TieuThucPhanBo.NHAN_CONG_TT) {
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
                } else if (this.cPAllocationGeneralExpense[i].allocationMethod === TieuThucPhanBo.CHI_PHI_TT) {
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
                const body = {
                    cPUncompletes: this.cPUncompletes,
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
