import { Component, OnInit, OnDestroy, AfterViewChecked, AfterViewInit, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IAccountingObject, VoucherRefCatalogDTO } from 'app/shared/model/accounting-object.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { AccountingObjectService } from './accounting-object.service';
import { IAccountingObjectGroup } from 'app/shared/model/accounting-object-group.model';
import * as moment from 'moment';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IPaymentClause } from 'app/shared/model/payment-clause.model';
import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { DanhMucType, SO_LAM_VIEC, SU_DUNG_DM_DOI_TUONG, TCKHAC_MauCTuChuaGS, TCKHAC_SDSOQUANTRI } from 'app/app.constants';
import { IType } from 'app/shared/model/type.model';
import { TypeService } from 'app/entities/type';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ISearchVoucherAccountingObjects, SearchVoucherAccountingObjects } from 'app/shared/model/SearchAccountingObjects';
import { AccountingObjectGroupService } from 'app/danhmuc/accounting-object-group';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { TranslateService } from '@ngx-translate/core';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { DeleteMultipleLinesComponent } from 'app/shared/modal/delete-multiple-lines/delete-multiple-lines.component';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { IOrganizationUnitSearch, OrganizationUnitSearch } from 'app/shared/model/organizationUnitSearch.model';
import { ImportExcelDanhMucComponent } from 'app/shared/modal/import-excel-danh-muc/import-excel-danh-muc.component';

@Component({
    selector: 'eb-accounting-object',
    styleUrls: ['./accounting-object.component.css'],
    templateUrl: './accounting-object.component.html'
})
export class AccountingObjectComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewInit {
    @ViewChild('deletePopup') deletePopup;
    currentAccount: any;
    accountingObjects: IAccountingObject[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    totalItemsVoucher: any;
    queryCount: any;
    itemsPerPage: any;
    itemsPerPageVoucher: any;
    page: any;
    pageVoucher: any;
    predicate: any;
    previousPage: any;
    predicateVoucher: any;
    previousPageVoucher: any;
    reverse: any;
    reverseVoucher: any;
    selectedAccountingObject: IAccountingObject;
    selectedRow: IAccountingObject;
    isGetAllCompany: boolean;
    voucherRefCatalogDTO: VoucherRefCatalogDTO[];
    types: IType[];
    typeID: string;
    typeName: string;
    TCKHAC_SDSoQuanTri: any;
    isSoTaiChinh: boolean;
    isShowSearch: boolean;
    pageCount: any;
    selectedVoucher: any;
    accountingobjectgroups: IAccountingObjectGroup[];
    listAccountingObjectGroupes: ['accountingObjectGroupName'];
    listHeaderColumnsAccountingObjectGroupes: ['Nhóm Khách Hàng'];
    accountingObjectGroup: any;
    objectType: any;
    scaleType: any;
    ROLE = ROLE;
    ROLE_DanhMucKhachHang_Them = ROLE.DanhMucKhachHang_Them;
    ROLE_DanhMucKhachHang_Sua = ROLE.DanhMucKhachHang_Sua;
    ROLE_DanhMucKhachHang_Xoa = ROLE.DanhMucKhachHang_Xoa;
    search: ISearchVoucherAccountingObjects;
    ScaleTypeList = [{ id: 0, name: 'Tổ chức' }, { id: 1, name: 'Cá nhân' }];
    ObjectTypeList = [{ id: 1, name: 'Khách Hàng' }, { id: 2, name: 'Khách Hàng/Nhà Cung Cấp' }, { id: 3, name: 'Khác' }];
    modalRef: NgbModalRef;
    modalRefMess: NgbModalRef;
    dataSession: IDataSessionStorage = new class implements IDataSessionStorage {
        accountingObjectName: string;
        itemsPerPage: number;
        page: number;
        pageCount: number;
        predicate: number;
        reverse: number;
        rowNum: number;
        totalItems: number;
    }();
    pageSearch: any;
    previousPageSearch: any;
    pageShow: any;

    constructor(
        private accountingObjectService: AccountingObjectService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private typeService: TypeService,
        public utilsService: UtilsService,
        private accountingObjectGroupService: AccountingObjectGroupService,
        private modalService: NgbModal,
        private toastr: ToastrService,
        private refModalService: RefModalService,
        private translate: TranslateService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.itemsPerPageVoucher = ITEMS_PER_PAGE;
        this.search = new SearchVoucherAccountingObjects();
        if (sessionStorage.getItem('search')) {
            this.search = JSON.parse(sessionStorage.getItem('search'));
            this.isShowSearch = true;
        } else {
            this.isShowSearch = false;
        }
        this.routeData = this.activatedRoute.data.subscribe(data => {
            if (sessionStorage.getItem('pageCurrent')) {
                if (!this.checkSearch()) {
                    this.page = +sessionStorage.getItem('pageCurrent');
                    this.previousPage = +sessionStorage.getItem('pageCurrent');
                } else {
                    this.pageSearch = +sessionStorage.getItem('pageCurrent');
                    this.previousPageSearch = +sessionStorage.getItem('pageCurrent');
                }
            } else {
                this.page = data['pagingParams'].page;
                this.previousPage = data['pagingParams'].page;
                this.pageSearch = data['pagingParams'].page;
                this.previousPageSearch = data['pagingParams'].page;
                sessionStorage.setItem('pageCurrent', this.pageSearch.toString());
            }
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.TCKHAC_SDSoQuanTri = this.currentAccount.systemOption.find(x => x.code === TCKHAC_SDSOQUANTRI).data;
            if (account) {
                if (this.TCKHAC_SDSoQuanTri === '1') {
                    this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                } else {
                    this.isSoTaiChinh = true;
                }
            }
        });
    }

    startLoadALL() {
        this.page = this.page > 1 ? this.page : 1;
        this.previousPage = this.previousPage > 1 ? this.previousPage : 1;
        this.pageSearch = this.page;
        this.previousPageSearch = this.previousPage;
        this.pageShow = this.page;
        this.pageVoucher = 1;
        this.previousPageVoucher = 1;
        sessionStorage.setItem('pageCurrent', this.page.toString());
        this.loadAll();
    }

    checkSearch() {
        if (
            (this.search.scaleType !== undefined && this.search.scaleType !== null) ||
            (this.search.objectType !== undefined && this.search.objectType !== null) ||
            (this.search.accountingObjectGroup !== undefined && this.search.accountingObjectGroup !== null) ||
            (this.search.keySearch !== undefined && this.search.keySearch !== null)
        ) {
            return true;
        }
        return false;
    }

    doubleClickRow(any?, any2?) {
        this.edit();
    }

    ngAfterViewInit(): void {
        this.focusFirstInput();
    }

    loadAll() {
        if (sessionStorage.getItem('dataSession')) {
            this.dataSession = JSON.parse(sessionStorage.getItem('dataSession'));
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.isShowSearch = this.dataSession.isShowSearch;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
            this.previousPage = this.dataSession.page;
            this.search = JSON.parse(this.dataSession.searchVoucher);
            sessionStorage.removeItem('dataSession');
        }
        this.accountingObjectService
            .pageableAccountingObjects({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                typeAccountingObject: 1,
                searchVoucherAccountingObjects: JSON.stringify(this.search)
            })
            .subscribe(
                (res: HttpResponse<IAccountingObject[]>) => this.paginateAccountingObjects(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadAllVoucher() {
        if (this.utilsService.isShowPopup) {
            return;
        }
        this.utilsService
            .getVoucherRefCatalogDTOByID({
                page: this.pageVoucher - 1,
                size: this.itemsPerPageVoucher,
                sort: this.sort(),
                id: this.selectedRow ? this.selectedRow.id : null,
                nameColumn: 'AccountingObjectID'
            })
            .subscribe(
                (res: HttpResponse<VoucherRefCatalogDTO[]>) => this.paginateAccountingObjectsVoucher(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    RowSelected(u: any) {
        this.selectedAccountingObject = u; // declare variable in component.
        console.log(this.selectedAccountingObject);
    }

    loadPage(page: number) {
        sessionStorage.setItem('pageCurrent', page.toString());
        if (this.checkSearch()) {
            this.pageSearch = page;
            if (this.pageSearch !== this.previousPageSearch) {
                this.previousPageSearch = this.pageSearch;
                this.transition();
            }
        } else {
            this.page = page;
            if (this.page !== this.previousPage) {
                this.previousPage = this.page;
                this.transition();
            }
        }
    }

    transition() {
        if (this.checkSearch()) {
            this.router.navigate(['/accounting-object'], {
                queryParams: {
                    page: this.pageSearch,
                    size: this.itemsPerPage,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
            });
            this.loadAllForSearch();
        } else {
            this.router.navigate(['/accounting-object'], {
                queryParams: {
                    page: this.page,
                    size: this.itemsPerPage,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
            });
            this.loadAll();
        }
    }

    loadPageVoucher(pageVoucher: number) {
        if (pageVoucher !== this.previousPageVoucher) {
            this.previousPageVoucher = pageVoucher;
            this.transitionVoucher(pageVoucher);
        }
        //this.transitionVoucher(pageVoucher);
    }

    transitionVoucher(pageVoucher: any) {
        // this.router.navigate([], {
        //     queryParams: {
        //         page: this.pageVoucher,
        //         size: this.itemsPerPageVoucher,
        //         sort: this.predicateVoucher + ',' + (this.reverseVoucher ? 'asc' : 'desc')
        //     }
        // });
        // this.loadAllVoucher();
        // this.accountingObjectService
        //     .findOneWithPageable({
        //         accId: this.selectedRow.id,
        //         page: pageVoucher,
        //         size: this.itemsPerPage
        //     })
        //     .subscribe((res: HttpResponse<IAccountingObject>) => {
        //         this.voucherRefCatalogDTO = res.body.voucherRefCatalogDTOS;
        //     });
        this.loadAllVoucher();
    }

    selectedItemPerPage() {
        if (this.checkSearch()) {
            this.loadAllForSearch();
        } else {
            this.loadAll();
        }
    }

    selectedItemPerPageVoucher() {
        this.loadAllVoucher();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/accounting-object',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.selectedAccountingObject = new class implements IAccountingObject {
            accountingObjectBankAccounts: IAccountingObjectBankAccount[];
            accountingObjectGroup: IAccountingObjectGroup;
            accountingObjectAddress: string;
            accountingobjectcode: string;
            accountingObjectName: string;
            agreementsalary: number;
            bankname: string;
            branchid: string;
            contactaddress: string;
            contactemail: string;
            contacthometel: string;
            contactmobile: string;
            contactName: string;
            contactofficetel: string;
            contactsex: number;
            contacttitle: string;
            description: string;
            duetime: number;
            email: string;
            employeebirthday: moment.Moment;
            fax: string;
            id: string;
            identificationno: string;
            insurancesalary: number;
            isactive: boolean;
            isemployee: boolean;
            issueby: string;
            issuedate: moment.Moment;
            isunofficialstaff: boolean;
            maximizadebtamount: number;
            numberofdependent: number;
            objecttype: number;
            organizationUnit: IOrganizationUnit;
            paymentClause: IPaymentClause;
            salarycoefficient: number;
            scaletype: number;
            taxCode: string;
            tel: string;
            website: string;
            typeID: number;
        }();
        this.dataSession = new class implements IDataSessionStorage {
            creditCardType: string;
            itemsPerPage: number;
            ownerCard: string;
            page: number;
            pageCount: number;
            predicate: number;
            reverse: number;
            rowNum: number;
            searchVoucher: string;
            totalItems: number;
        }();
        // this.accountingobjectgroups = [];
        this.accountingObjectGroupService.getAllAccountingObjectGroup().subscribe((res: HttpResponse<IAccountingObjectGroup[]>) => {
            this.accountingobjectgroups = res.body;
        });
        this.isGetAllCompany = false;
        this.typeService.getAllTypes().subscribe((res: HttpResponse<IType[]>) => {
            this.types = res.body;
        });
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.isGetAllCompany = this.currentAccount.systemOption.find(x => x.code === SU_DUNG_DM_DOI_TUONG).data === '0';
            if (this.checkSearch()) {
                this.startLoadAllSearch();
            } else {
                this.startLoadALL();
            }
        });
        this.registerChangeInRepositories();
    }

    startLoadAllSearch() {
        this.pageSearch = this.pageSearch > 1 ? this.pageSearch : 1;
        this.previousPageSearch = this.previousPageSearch > 1 ? this.previousPageSearch : 1;
        this.page = 1;
        this.previousPage = 1;
        this.pageShow = this.pageSearch;
        sessionStorage.setItem('pageCurrent', this.pageSearch.toString());
        this.loadAllForSearch();
    }

    startLoadAllButtonSearch() {
        this.pageSearch = 1;
        this.previousPageSearch = 1;
        this.page = 1;
        this.previousPage = 1;
        this.pageShow = this.pageSearch;
        this.pageVoucher = 1;
        this.previousPageVoucher = 1;
        sessionStorage.setItem('pageCurrent', this.pageSearch.toString());
        sessionStorage.setItem('search', JSON.stringify(this.search));
        this.loadAllForSearch();
    }

    registerChangeInRepositories() {
        this.eventSubscriber = this.eventManager.subscribe('repositoryListModification', response => this.loadAll());
    }

    toggleSearch($event?) {
        this.isShowSearch = !this.isShowSearch;
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
        if (!window.location.href.includes('accounting-object')) {
            sessionStorage.removeItem('pageCurrent');
            sessionStorage.removeItem('selectIndex');
            sessionStorage.removeItem('search');
        }
    }

    trackId(index: number, item: IAccountingObject) {
        return item.id;
    }

    registerChangeInAccountingObjects() {
        this.eventSubscriber = this.eventManager.subscribe('accountingObjectListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    searchOrganizationUnit() {
        const searchOrg: ISearchVoucherAccountingObjects = {};
        if (this.search.scaleType === null) {
            this.search.scaleType = undefined;
        }
        if (this.search.objectType === null) {
            this.search.objectType = undefined;
        }
        if (this.search.accountingObjectGroup === '') {
            this.search.accountingObjectGroup = undefined;
        }
        if (this.search.keySearch === '') {
            this.search.keySearch = undefined;
        }
        searchOrg.scaleType = this.search.scaleType !== undefined ? this.search.scaleType : null;
        searchOrg.objectType = this.search.objectType !== undefined ? this.search.objectType : null;
        searchOrg.accountingObjectGroup = this.search.accountingObjectGroup !== undefined ? this.search.accountingObjectGroup : null;
        searchOrg.keySearch = this.search.keySearch !== undefined ? this.search.keySearch : null;
        return searchOrg;
    }

    private paginateAccountingObjects(data: IAccountingObject[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.accountingObjects = data;
        this.objects = data;
        // if (this.dataSession && this.dataSession.rowNum) {
        //     this.selectedRows.push(this.accountingObjects[this.dataSession.rowNum]);
        //     this.selectedRow = this.accountingObjects[this.dataSession.rowNum];
        // } else {
        //     this.selectedRows.push(this.accountingObjects[0]);
        //     this.selectedRow = this.accountingObjects[0];
        // }
        if (sessionStorage.getItem('selectIndex')) {
            this.selectedRow = this.accountingObjects[+sessionStorage.getItem('selectIndex')];
            sessionStorage.removeItem('selectIndex');
        } else {
            this.selectedRow = this.accountingObjects[0];
        }
        this.loadAllVoucher();
        this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
    }

    private paginateAccountingObjectsVoucher(data: VoucherRefCatalogDTO[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItemsVoucher = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItemsVoucher;
        //this.pageVoucher = this.page;
        this.voucherRefCatalogDTO = data;
        this.selectedVoucher = this.voucherRefCatalogDTO[0];
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
        this.voucherRefCatalogDTO = [];
    }

    edit() {
        event.preventDefault();
        if (this.selectedRow.id) {
            this.dataSession.page = this.page;
            this.dataSession.itemsPerPage = this.itemsPerPage;
            this.dataSession.pageCount = this.pageCount;
            this.dataSession.totalItems = this.totalItems;
            this.dataSession.rowNum = this.accountingObjects.indexOf(this.selectedRow);
            this.dataSession.isShowSearch = this.isShowSearch;
            this.dataSession.searchVoucher = JSON.stringify(this.search);
            // sort
            this.dataSession.predicate = this.predicate;
            this.dataSession.reverse = this.reverse;
            sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
            sessionStorage.setItem('selectIndex', this.accountingObjects.indexOf(this.selectedRow).toString());
            if (this.checkSearch()) {
                sessionStorage.setItem('search', JSON.stringify(this.search));
            }
            this.router.navigate(['./accounting-object', this.selectedRow.id, 'edit']);
        }
    }
    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucKhachHang_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.modalRef = this.modalService.open(this.deletePopup, { size: 'lg', backdrop: 'static' });
        } else {
            this.router.navigate(['/accounting-object', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
        }
    }

    confirmDeleteList() {
        this.accountingObjectService.deleteByListIDAcc(this.selectedRows.map(n => n.id)).subscribe((res: HttpResponse<HandlingResult>) => {
            if (res.body.countSuccessVouchers > 0) {
                this.loadAll();
            }
            if (res.body.countFailVouchers === 0) {
                this.toastr.success(this.translate.instant('ebwebApp.bank.deleteSuccess'));
            } else {
                const lstFailView = [];
                res.body.listIDFail.forEach(n => {
                    const sa = this.accountingObjects.find(m => m.id === n);
                    if (sa) {
                        const viewNo: ViewVoucherNo = {};
                        viewNo.accountingObjectCode = sa.accountingObjectCode;
                        viewNo.accountingObjectName = sa.accountingObjectName;
                        viewNo.reasonFail = this.translate.instant('ebwebApp.sAOrder.delete.deleteFail');
                        lstFailView.push(viewNo);
                    }
                });
                res.body.listFail = lstFailView;
                const lstHideColumn = ['accountingObjectCode'];
                this.modalRefMess = this.refModalService.open(
                    res.body,
                    DeleteMultipleLinesComponent,
                    lstHideColumn,
                    false,
                    null,
                    null,
                    null,
                    null,
                    null,
                    true
                );
            }
        });
    }

    onSelect(select: IAccountingObject) {
        event.preventDefault();
        this.selectedRow = select;
        this.pageVoucher = 1;
        this.loadAllVoucher();
        console.log(`selectedTest1 = ${JSON.stringify(this.selectedRow)}`);
    }

    selectRowVoucher(object, evt, objects: any[]) {
        this.selectedRows = [];
        this.selectedRows.push(object);
        // this.selectedRow = object;
        //this.onSelectVoucher(object);
        this.selectedVoucher = object;
    }

    onSelectVoucher(select: IAccountingObject) {
        event.preventDefault();
        // this.selectedRow = select;
        // console.log(`selectedTest1 = ${JSON.stringify(this.selectedRow)}`);
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    getTypeName(typeID) {
        if (typeID && this.types) {
            const accountingObjectType = this.types.find(type => type.id === typeID);
            if (accountingObjectType) {
                return accountingObjectType.typeName;
            } else {
                return '';
            }
        }
    }

    resetSearch() {
        this.search.scaleType = null;
        this.search.objectType = null;
        this.search.accountingObjectGroup = null;
        this.search.keySearch = null;
        sessionStorage.removeItem('search');
        // sessionStorage.removeItem('sessionSearchVoucher');
        this.startLoadALL();
    }

    addNew($event) {
        event.preventDefault();
        this.router.navigate(['/accounting-object/new']);
    }

    loadAllForSearch() {
        this.accountingObjectService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                typeAccountingObject: 1,
                searchVoucherAccountingObjects: JSON.stringify(this.search)
            })
            .subscribe(
                (res: HttpResponse<IAccountingObject[]>) => this.paginateAccountingObjects(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    importFromExcel() {
        this.utilsService.setShowPopup(true);
        this.refModalService.open(null, ImportExcelDanhMucComponent, {
            type: DanhMucType.KH,
            onSaveSuccess: () => {
                this.loadAll();
            }
        });
    }
}
