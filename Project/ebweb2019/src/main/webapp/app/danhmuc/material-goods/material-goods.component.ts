import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { MaterialGoodsService } from './material-goods.service';
import { ICurrency } from 'app/shared/model/currency.model';
import { IBank } from 'app/shared/model/bank.model';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';
import { DanhMucType, SO_LAM_VIEC, SU_DUNG_DM_VTHH, TCKHAC_SDSOQUANTRI, TypeID } from 'app/app.constants';
import { IAccountingObject, VoucherRefCatalogDTO } from 'app/shared/model/accounting-object.model';
import { IType } from 'app/shared/model/type.model';
import { TypeService } from 'app/entities/type';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { MaterialGoodsCategoryService } from 'app/danhmuc/material-goods-category';
import { IMCReceiptDetailCustomer } from 'app/shared/model/mc-receipt-detail-customer.model';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';
import { ISearchVoucherMaterialGoods, SearchVoucherMaterialGoods } from 'app/shared/model/SearchVoucherMaterialGoods';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { DeleteMultipleLinesComponent } from 'app/shared/modal/delete-multiple-lines/delete-multiple-lines.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { EbMaterialGoodsModalComponent } from 'app/shared/modal/material-goods/material-goods.component';
import { MaterialGoodsResultComponent } from 'app/shared/modal/material-goods-1/bank-result.component';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { ImportExcelDanhMucComponent } from 'app/shared/modal/import-excel-danh-muc/import-excel-danh-muc.component';

@Component({
    selector: 'eb-material-goods',
    templateUrl: './material-goods.component.html',
    styleUrls: ['./material-goods.component.css']
})
export class MaterialGoodsComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('deletePopup') deletePopup;
    currentAccount: any;
    materialGoods: IMaterialGoods[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    pageSearch: any;
    previousPageSearch: any;
    previousPageVoucher: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    isShowSearch: boolean;
    selectedRow: IMaterialGoods;
    voucherRefCatalogDTO: VoucherRefCatalogDTO[];
    types: IType[];
    typeID: string;
    pageShow: any;
    TCKHAC_SDSoQuanTri: any;
    materialGoodsTypeList = [
        { id: 0, name: 'Vật tư hàng hóa' },
        { id: 1, name: 'VTHH lắp ráp/tháo dỡ' },
        { id: 2, name: 'Dịch vụ' },
        { id: 3, name: 'Thành phẩm' },
        { id: 4, name: 'Khác' }
    ];
    ROLE = ROLE;
    index: any;
    rowNum: number;
    isGetAllCompany: boolean;
    ROLE_VatTuHangHoa_Them = ROLE.DanhMucVatTuHangHoa_Them;
    ROLE_VatTuHangHoa_Sua = ROLE.DanhMucVatTuHangHoa_Sua;
    ROLE_VatTuHangHoa_Xoa = ROLE.DanhMucVatTuHangHoa_Xoa;
    materialGoodsType: any;
    unitID: any;
    units: IUnit[];
    listColumnsUnitId = ['unitName'];
    listHeaderColumnsUnitId = ['Đơn vị tính'];
    keySearch: any;
    listColumnsmaterialGoodsCategory = ['materialGoodsCategoryCode', 'materialGoodsCategoryName'];
    listHeaderColumnsmaterialGoodsCategor = ['Mã loại', 'Tên loại'];
    materialGoodsCategories: IMaterialGoodsCategory[];
    materialGoodsCategoryID: any;
    isSoTaiChinh: boolean;
    search: ISearchVoucherMaterialGoods;
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
    totalItemsVoucher: any;
    itemsPerPageVoucher: any;
    pageVoucher: any;
    modalRef: NgbModalRef;
    modalRefMess: NgbModalRef;
    pageCount: any;

    constructor(
        private materialGoodsService: MaterialGoodsService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private materialGoodsCategoryService: MaterialGoodsCategoryService,
        private eventManager: JhiEventManager,
        private unitService: UnitService,
        private typeService: TypeService,
        private viewVoucherService: ViewVoucherService,
        private toastrService: ToastrService,
        private translateService: TranslateService,
        public utilsService: UtilsService,
        private modalService: NgbModal,
        private toastr: ToastrService,
        private refModalService: RefModalService,
        private translate: TranslateService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.itemsPerPageVoucher = ITEMS_PER_PAGE;
        this.search = new SearchVoucherMaterialGoods();
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
                this.page = data.pagingParams.page;
                this.previousPage = data.pagingParams.page;
                this.pageSearch = data.pagingParams.page;
                this.previousPageSearch = data.pagingParams.page;
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

    saveSearchVoucher() {
        const searchObj: ISearchVoucherMaterialGoods = {};
        searchObj.materialGoodsCategoryID = this.search.materialGoodsCategoryID !== undefined ? this.search.materialGoodsCategoryID : null;
        searchObj.materialGoodsType = this.search.materialGoodsType !== undefined ? this.search.materialGoodsType : null;
        searchObj.unitID = this.search.unitID !== undefined ? this.search.unitID : null;
        searchObj.keySearch = this.search.keySearch !== undefined ? this.search.keySearch : null;
    }

    isCheckingSearchVoucher(): boolean {
        const obj = this.saveSearchVoucher();
        const objNull: ISearchVoucherMaterialGoods = {
            materialGoodsType: null,
            materialGoodsCategoryID: null,
            unitID: null,
            keySearch: null
        };
        return !this.utilsService.isEquivalent(obj, objNull);
    }

    startLoadALL() {
        // this.page = this.page > 1 ? this.page : 1;
        // this.previousPage = this.previousPage > 1 ? this.previousPage : 1;
        // this.pageSearch = this.page;
        // this.previousPageSearch = this.previousPage;
        // this.pageShow = this.page;
        // sessionStorage.setItem('pageCurrent', this.page.toString());
        // this.loadAll();
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

    loadAll() {
        if (sessionStorage.getItem('dataSession')) {
            this.dataSession = JSON.parse(sessionStorage.getItem('dataSession'));
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
            this.previousPage = this.dataSession.page;
            sessionStorage.removeItem('dataSession');
        }
        this.materialGoodsService
            .pageableMaterialGoods({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IMaterialGoods[]>) => {
                    this.paginateMaterialGoods(res.body, res.headers);
                    if (this.materialGoods.length > 0) {
                        this.onSelect(this.selectedRow);
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    checkSearch() {
        if (
            (this.search.unitID !== undefined && this.search.unitID !== null) ||
            (this.search.materialGoodsType !== undefined && this.search.materialGoodsType !== null) ||
            (this.search.materialGoodsCategoryID !== undefined && this.search.materialGoodsCategoryID !== null) ||
            (this.search.keySearch !== undefined && this.search.keySearch !== null)
        ) {
            return true;
        }
        return false;
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
        this.dataSession.rowNum = 0;
        if (this.checkSearch()) {
            this.router.navigate(['/material-goods'], {
                queryParams: {
                    page: this.pageSearch,
                    size: this.itemsPerPage,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
            });
            this.loadAllForSearch();
        } else {
            this.router.navigate(['/material-goods'], {
                queryParams: {
                    page: this.page,
                    size: this.itemsPerPage,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
            });
            this.loadAll();
        }
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/material-goods',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.units = [];
        this.materialGoodsCategories = [];
        this.typeService.getAllTypes().subscribe((res: HttpResponse<IType[]>) => {
            this.types = res.body;
        });
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.isGetAllCompany = this.currentAccount.systemOption.find(x => x.code === SU_DUNG_DM_VTHH).data === '0';
            if (this.checkSearch()) {
                this.startLoadAllSearch();
            } else {
                this.startLoadALL();
            }
        });
        this.registerChangeInMaterialGoods();
        this.materialGoodsCategoryService.getMaterialGoodsCategory().subscribe((res: HttpResponse<IMaterialGoodsCategory[]>) => {
            this.materialGoodsCategories = res.body;
        });
        this.unitService.getAllUnits().subscribe((res: HttpResponse<IUnit[]>) => {
            this.units = res.body;
        });
    }

    registerChangeInMaterialGoods() {
        this.eventSubscriber = this.eventManager.subscribe('materialGoodsListModification', response => this.loadAll());
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
        this.routeData.unsubscribe();
        if (!window.location.href.includes('/material-goods')) {
            sessionStorage.removeItem('pageCurrent');
            sessionStorage.removeItem('selectIndex');
            sessionStorage.removeItem('search');
        }
    }

    trackId(index: number, item: IMaterialGoods) {
        return item.id;
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    // private paginateAccountingObjectsVoucher(data: VoucherRefCatalogDTO[], headers: HttpHeaders) {
    //     this.links = this.parseLinks.parse(headers.get('link'));
    //     this.totalItemsVoucher = parseInt(headers.get('X-Total-Count'), 10);
    //     this.queryCount = this.totalItemsVoucher;
    //     this.pageVoucher = this.page;
    //     this.voucherRefCatalogDTO = data;
    //     this.objects = data;
    // }

    // private paginateAccountingObjectsVoucher(data: VoucherRefCatalogDTO[], headers: HttpHeaders) {
    //     this.links = this.parseLinks.parse(headers.get('link'));
    //     this.totalItemsVoucher = parseInt(headers.get('X-Total-Count'), 10);
    //     this.queryCount = this.totalItemsVoucher;
    //     this.pageVoucher = this.page;
    //     this.voucherRefCatalogDTO = data;
    //     this.objects = data;
    // }

    private paginateMaterialGoods(data: IMaterialGoods[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        if (data.length === 0 && this.page !== 1) {
            this.page = 1;
            this.pageVoucher = 1;
            this.pageSearch = 1;
            this.pageShow = 1;
            this.loadAll();
        } else {
            this.materialGoods = data;
        }

        this.objects = data;
        if (sessionStorage.getItem('selectIndex')) {
            this.selectedRow = this.materialGoods[+sessionStorage.getItem('selectIndex')];
            sessionStorage.removeItem('selectIndex');
        } else {
            this.selectedRow = this.materialGoods[0];
        }
        if (this.dataSession && this.dataSession.rowNum) {
            this.selectedRows.push(this.materialGoods[this.dataSession.rowNum]);
            this.selectedRow = this.materialGoods[this.dataSession.rowNum];
        } else {
            this.selectedRows.push(this.materialGoods[0]);
            this.selectedRow = this.materialGoods[0];
        }
        this.loadAllVoucher();
        this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
    }

    private paginateAccountingObjectsVoucher(data: VoucherRefCatalogDTO[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItemsVoucher = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItemsVoucher;
        this.pageVoucher = this.page;
        this.voucherRefCatalogDTO = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucVatTuHangHoa_Xem])
    edit() {
        event.preventDefault();
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.rowNum = this.materialGoods.indexOf(this.selectedRow);
        // sort
        this.dataSession.predicate = this.predicate;
        this.dataSession.reverse = this.reverse;
        sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
        sessionStorage.setItem('selectIndex', this.materialGoods.indexOf(this.selectedRow).toString());
        if (this.checkSearch()) {
            sessionStorage.setItem('search', JSON.stringify(this.search));
        }
        if (this.selectedRow.id) {
            this.router.navigate(['./material-goods', this.selectedRow.id, 'edit']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucVatTuHangHoa_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.modalRef = this.modalService.open(this.deletePopup, { size: 'lg', backdrop: 'static' });
        } else {
            this.router.navigate(['/material-goods', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
        }
    }

    getUnitName(unitID) {
        if (unitID) {
            const currentUnit = this.units.find(unit => unit.id === unitID);
            if (currentUnit) {
                return currentUnit.unitName;
            }
        }
    }

    getMaterialGoodsType(materialGoodsType) {
        if (materialGoodsType !== null || materialGoodsType !== undefined) {
            const currentMaterialGoodsType = this.materialGoodsTypeList.find(materialGoods => materialGoods.id === materialGoodsType);
            if (currentMaterialGoodsType) {
                return currentMaterialGoodsType.name;
            }
        }
    }

    selectedItemPerPage() {
        if (this.checkSearch()) {
            this.loadAllForSearch();
        } else {
            this.loadAll();
        }
    }

    loadAllVoucher() {
        this.utilsService
            .getVoucherRefCatalogDTOByID({
                page: this.pageVoucher - 1,
                size: this.itemsPerPageVoucher,
                sort: this.sort(),
                id: this.selectedRow.id,
                nameColumn: 'MaterialGoodsID'
            })
            .subscribe(
                (res: HttpResponse<VoucherRefCatalogDTO[]>) => this.paginateAccountingObjectsVoucher(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    // loadAllVoucher() {
    //     this.utilsService
    //         .getVoucherRefCatalogDTOByID({
    //             page: this.pageVoucher - 1,
    //             size: this.itemsPerPageVoucher,
    //             sort: this.sort(),
    //             id: this.selectedRow.id,
    //             nameColumn: 'MaterialGoodsID'
    //         })
    //         .subscribe(
    //             (res: HttpResponse<VoucherRefCatalogDTO[]>) => this.paginateAccountingObjectsVoucher(res.body, res.headers),
    //             (res: HttpErrorResponse) => this.onError(res.message)
    //         );
    // }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    getTypeName(typeID) {
        if (typeID && this.types) {
            const materialGoodsType = this.types.find(type => type.id === typeID);
            if (materialGoodsType) {
                return materialGoodsType.typeName;
            } else {
                return '';
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucVatTuHangHoa_Them])
    addNew($event) {
        event.preventDefault();
        this.router.navigate(['/material-goods/new']);
    }

    getRowNumberOfRecord(page: number, index: number): number {
        if (page > 0 && index !== -1) {
            return (page - 1) * this.itemsPerPage + index + 1;
        }
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
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
        sessionStorage.setItem('pageCurrent', this.pageSearch.toString());
        sessionStorage.setItem('userSearch', JSON.stringify(this.search));
        this.loadAllForSearch();
    }

    loadAllForSearch() {
        this.materialGoodsService
            .searchAll({
                page: this.pageSearch - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                searchVoucherMaterialGoods: JSON.stringify(this.searchMaterialGood())
            })
            .subscribe(
                (res: HttpResponse<IMaterialGoods[]>) => this.paginateMaterialGoods(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    searchMaterialGood() {
        const searchTrans: ISearchVoucherMaterialGoods = new SearchVoucherMaterialGoods();
        if (this.search.unitID === '') {
            this.search.unitID = undefined;
        }
        if (this.search.materialGoodsType === '') {
            this.search.materialGoodsType = undefined;
        }
        if (this.search.materialGoodsCategoryID === '') {
            this.search.materialGoodsCategoryID = undefined;
        }
        if (this.search.keySearch === '') {
            this.search.keySearch = undefined;
        }
        searchTrans.unitID = this.search.unitID !== undefined ? this.search.unitID : null;
        searchTrans.materialGoodsCategoryID =
            this.search.materialGoodsCategoryID !== undefined ? this.search.materialGoodsCategoryID : null;
        searchTrans.materialGoodsType = this.search.materialGoodsType !== undefined ? this.search.materialGoodsType : null;
        searchTrans.keySearch = this.search.keySearch !== undefined ? this.search.keySearch : null;
        return searchTrans;
    }

    resetSearch() {
        this.search.materialGoodsType = null;
        this.search.unitID = null;
        this.search.materialGoodsCategoryID = null;
        this.search.keySearch = null;
        sessionStorage.removeItem('search');
        this.startLoadALL();
    }

    doubleClickRow(any?) {
        this.edit();
    }

    confirmDeleteList() {
        this.materialGoodsService.deleteByListID(this.selectedRows.map(n => n.id)).subscribe((res: HttpResponse<HandlingResult>) => {
            if (res.body.countSuccessVouchers > 0) {
                this.loadAll();
            }
            if (res.body.countFailVouchers === 0) {
                this.toastr.success(this.translate.instant('ebwebApp.bank.deleteSuccess'));
            } else {
                const lstFailView = [];
                res.body.listIDFail.forEach(n => {
                    const sa = this.materialGoods.find(m => m.id === n);
                    if (sa) {
                        const viewNo: ViewVoucherNo = {};
                        viewNo.materialGoodsCode = sa.materialGoodsCode;
                        viewNo.materialGoodsName = sa.materialGoodsName;
                        viewNo.reasonFail = this.translate.instant('ebwebApp.sAOrder.delete.hasRef');
                        lstFailView.push(viewNo);
                    }
                });
                res.body.listFail = lstFailView;
                const lstHideColumn = ['materialGoodsCode'];
                this.modalRefMess = this.refModalService.open(
                    res.body,
                    MaterialGoodsResultComponent,
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
        this.modalRef.close();
    }

    // transition() {
    //     this.router.navigate(['/material-goods'], {
    //         queryParams: {
    //             page: this.page,
    //             size: this.itemsPerPage,
    //             sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
    //         }
    //     });
    //     this.loadAll();
    // }

    loadPageVoucher(pageVoucher: number) {
        this.transitionVoucher(pageVoucher);
    }

    transitionVoucher(pageVoucher: any) {
        this.loadAllVoucher();
    }

    selectedItemPerPageVoucher() {
        this.loadAllVoucher();
    }

    onSelect(select: IMaterialGoods) {
        event.preventDefault();
        this.selectedRow = select;
        this.loadAllVoucher();
        console.log(`selectedTest1 = ${JSON.stringify(this.selectedRow)}`);
    }

    selectRowVoucher(object, evt, objects: any[]) {
        this.selectedRows = [];
        this.selectedRows.push(object);
        this.selectedRow = object;
        this.onSelectVoucher(object);
    }

    onSelectVoucher(select: IMaterialGoods) {
        event.preventDefault();
        this.selectedRow = select;
        console.log(`selectedTest1 = ${JSON.stringify(this.selectedRow)}`);
    }

    importFromExcel() {
        this.utilsService.setShowPopup(true);
        this.refModalService.open(null, ImportExcelDanhMucComponent, {
            type: DanhMucType.VTHH,

            onSaveSuccess: () => {
                this.ngOnInit();
            }
        });
    }
}
