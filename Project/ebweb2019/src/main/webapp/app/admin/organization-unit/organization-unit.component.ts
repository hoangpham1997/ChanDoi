import { Component, OnInit, OnDestroy, ViewEncapsulation } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IOrganizationUnit, OrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IUser, Principal, UserService } from 'app/core';

import { ITEMS_PER_PAGE_ADMIN } from 'app/shared';
import { OrganizationUnitAdminService } from './organization-unit.service';
import { IAccountList } from 'app/shared/model/account-list.model';
import { TranslateService } from '@ngx-translate/core';
import { ITreeAccountList } from 'app/shared/model/account-list-tree.model';
import { ITreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ToastrService } from 'ngx-toastr';
import { EbPackageService } from 'app/admin/eb-package/eb-package.service';
import { EbPackage, IEbPackage } from 'app/shared/model/eb-package.model';
import { OrganizationUnitDeleteDialogAdminComponent } from 'app/admin';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserIDAndOrgID } from 'app/shared/model/UserIDAndOrgID';
import { OrganizationUnitOptionReport } from 'app/shared/model/organization-unit-option-report.model';
import { IOrganizationUnitSearch, OrganizationUnitSearch } from 'app/shared/model/organizationUnitSearch.model';
import { UserSearch } from 'app/shared/model/userSearch.model';

@Component({
    selector: 'eb-organization-unit-admin',
    templateUrl: './organization-unit.component.html',
    styleUrls: ['./organization-unit.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class OrganizationUnitAdminComponent extends BaseComponent implements OnInit, OnDestroy {
    currentAccount: any;
    organizationUnits: IOrganizationUnit[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    pageSearch: any;
    previousPageSearch: any;
    predicate: any;
    ngbModalRef: any;
    previousPage: any;
    reverse: any;
    unitTypes: any[];
    selectedRow: ITreeOrganizationUnit;
    listOrganizationUnit: IOrganizationUnit[];
    listTHead: string[];
    listKey: any[];
    navigateForm: string;
    ebPackage: IEbPackage;
    selectedRowOrg: IOrganizationUnit;
    data: IUser[];
    listColumnsUser: any;
    listHeaderColumnsUser: any;
    userId: number;
    userOrgId: UserIDAndOrgID;
    isSaveUserOrg: Boolean;
    isShowSearch: boolean;
    organizationUnitSearch: IOrganizationUnitSearch;
    pageShow: any;

    constructor(
        private organizationUnitService: OrganizationUnitAdminService,
        private ebPackageService: EbPackageService,
        private userService: UserService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private translate: TranslateService,
        private modalService: NgbModal,
        private toastr: ToastrService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE_ADMIN;
        this.organizationUnitSearch = new OrganizationUnitSearch();
        if (sessionStorage.getItem('organizationUnitSearch')) {
            this.organizationUnitSearch = JSON.parse(sessionStorage.getItem('organizationUnitSearch'));
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
            // this.page = data.pagingParams.page;
            // this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    startLoadALL() {
        this.page = this.page > 1 ? this.page : 1;
        this.previousPage = this.previousPage > 1 ? this.previousPage : 1;
        this.pageSearch = this.page;
        this.previousPageSearch = this.previousPage;
        this.pageShow = this.page;
        sessionStorage.setItem('pageCurrent', this.page.toString());
        this.loadAll();
    }

    loadAll() {
        // this.translate
        //     .get([
        //         'ebwebApp.organizationUnit.corporations',
        //         'ebwebApp.organizationUnit.branch',
        //         'ebwebApp.organizationUnit.representativeOffice',
        //         'ebwebApp.organizationUnit.placeOfBusiness',
        //         'ebwebApp.organizationUnit.department',
        //         'ebwebApp.organizationUnit.other'
        //     ])
        //     .subscribe(translate => {
        //         this.organizationUnitService.getOrganizationUnitsByCompanyID().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
        //             this.listParentOrganizationUnit = [];
        //             this.organizationUnits = res.body;
        //             for (let i = 0; i < this.organizationUnits.length; i++) {
        //                 if (this.organizationUnits[i].unitType !== null || this.organizationUnits[i].unitType !== undefined) {
        //                     if (this.organizationUnits[i].unitType === 0) {
        //                         this.organizationUnits[i].getUnitName = translate['ebwebApp.organizationUnit.corporations'];
        //                     } else if (this.organizationUnits[i].unitType === 1) {
        //                         this.organizationUnits[i].getUnitName = translate['ebwebApp.organizationUnit.branch'];
        //                     } else if (this.organizationUnits[i].unitType === 2) {
        //                         this.organizationUnits[i].getUnitName = translate['ebwebApp.organizationUnit.representativeOffice'];
        //                     } else if (this.organizationUnits[i].unitType === 3) {
        //                         this.organizationUnits[i].getUnitName = translate['ebwebApp.organizationUnit.placeOfBusiness'];
        //                     } else if (this.organizationUnits[i].unitType === 4) {
        //                         this.organizationUnits[i].getUnitName = translate['ebwebApp.organizationUnit.department'];
        //                     } else if (this.organizationUnits[i].unitType === 5) {
        //                         this.organizationUnits[i].getUnitName = translate['ebwebApp.organizationUnit.other'];
        //                     }
        //                 }
        //                 if (this.organizationUnits[i].parentID !== null || this.organizationUnits[i].parentID !== undefined) {
        //                     this.organizationUnits[i].getParentName = this.getOrganizationUnitName(this.organizationUnits[i].parentID);
        //                 }
        //             }
        //             let index = 0;
        //             const listOrganizationUnit = this.organizationUnits.filter(a => a.grade === 1);
        //             for (let i = 0; i < listOrganizationUnit.length; i++) {
        //                 this.listParentOrganizationUnit.push(Object.assign({}));
        //                 this.listParentOrganizationUnit[index].parent = listOrganizationUnit[i];
        //                 index++;
        //             }
        //             this.tree(this.listParentOrganizationUnit, 1);
        //         });
        //     });

        // this.ebPackageService.getEbPackageByUserAndCompany().subscribe((res: HttpResponse<IEbPackage>) => {
        //     this.ebPackage = res.body;
        // this.organizationUnitService.recursiveOrganizationUnitByParentID().subscribe((res2: HttpResponse<IOrganizationUnit[]>) => {
        //     this.organizationUnits = res2.body;
        //     this.getUnitNameAndParentName(this.listParentOrganizationUnit);
        // });
        // });

        // this.organizationUnitService.getAllOrganizationUnits().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
        //     this.listOrganizationUnit = res.body;
        //     console.log(res.body);
        //     // this.organizationUnitService.recursiveOrganizationUnitByParentID().subscribe((res2: HttpResponse<IOrganizationUnit[]>) => {
        //     //     this.organizationUnits = res2.body;
        //     //     this.getUnitNameAndParentName(this.listParentOrganizationUnit);
        //     // });
        // });
        this.organizationUnitService
            .queryBigOrg({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IOrganizationUnit[]>) => this.paginateOrganizationUnits(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    getUnitNameAndParentName(listOrg: ITreeOrganizationUnit[]) {
        this.translate
            .get([
                'ebwebApp.organizationUnit.corporations',
                'ebwebApp.organizationUnit.branch',
                'ebwebApp.organizationUnit.representativeOffice',
                'ebwebApp.organizationUnit.placeOfBusiness',
                'ebwebApp.organizationUnit.department',
                'ebwebApp.organizationUnit.other'
            ])
            .subscribe(translate => {
                for (let i = 0; i < listOrg.length; i++) {
                    if (listOrg[i].parent.unitType !== null || listOrg[i].parent.unitType !== undefined) {
                        if (listOrg[i].parent.unitType === 0) {
                            listOrg[i].parent.getUnitName = translate['ebwebApp.organizationUnit.corporations'];
                        } else if (listOrg[i].parent.unitType === 1) {
                            listOrg[i].parent.getUnitName = translate['ebwebApp.organizationUnit.branch'];
                        } else if (listOrg[i].parent.unitType === 2) {
                            listOrg[i].parent.getUnitName = translate['ebwebApp.organizationUnit.representativeOffice'];
                        } else if (listOrg[i].parent.unitType === 3) {
                            listOrg[i].parent.getUnitName = translate['ebwebApp.organizationUnit.placeOfBusiness'];
                        } else if (listOrg[i].parent.unitType === 4) {
                            listOrg[i].parent.getUnitName = translate['ebwebApp.organizationUnit.department'];
                        } else if (listOrg[i].parent.unitType === 5) {
                            listOrg[i].parent.getUnitName = translate['ebwebApp.organizationUnit.other'];
                        }
                    }
                    if (listOrg[i].parent.parentID !== null || listOrg[i].parent.parentID !== undefined) {
                        listOrg[i].parent.getParentName = this.getOrganizationUnitName(listOrg[i].parent.parentID);
                    }
                    if (listOrg[i].children && listOrg[i].children.length > 0) {
                        this.getUnitNameAndParentName(listOrg[i].children);
                    }
                }
            });
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    checkSearch() {
        if (
            (this.organizationUnitSearch.organizationUnitCode !== undefined && this.organizationUnitSearch.organizationUnitCode !== null) ||
            (this.organizationUnitSearch.organizationUnitName !== undefined && this.organizationUnitSearch.organizationUnitName !== null) ||
            (this.organizationUnitSearch.taxCode !== undefined && this.organizationUnitSearch.taxCode !== null)
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

    registerSelectedRow() {
        this.eventSubscriber = this.eventManager.subscribe('selectRow', response => {
            this.selectedRow = response.data;
        });
    }

    transition() {
        if (this.checkSearch()) {
            this.router.navigate(['/admin/organization-unit'], {
                queryParams: {
                    page: this.pageSearch,
                    size: this.itemsPerPage,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
            });
            this.loadAllForSearch();
        } else {
            this.router.navigate(['/admin/organization-unit'], {
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
            '/organization-unit',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.navigateForm = './organization-unit';
        this.listTHead = [];
        this.listKey = [];
        this.listTHead.push('ebwebApp.organizationUnit.organizationUnitCode');
        this.listTHead.push('ebwebApp.organizationUnit.organizationUnitName');
        this.listTHead.push('ebwebApp.organizationUnit.unitType');
        this.listTHead.push('ebwebApp.organizationUnit.parentID');
        this.listTHead.push('ebwebApp.organizationUnit.isActive');
        this.listKey.push({ key: 'organizationUnitCode', type: 1 });
        this.listKey.push({ key: 'organizationUnitName', type: 1 });
        this.listKey.push({ key: 'getUnitName', type: 1 });
        this.listKey.push({ key: 'getParentName', type: 2 });
        this.listKey.push({ key: 'isActive', type: 1 });
        if (this.checkSearch()) {
            this.startLoadAllSearch();
        } else {
            this.startLoadALL();
        }
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInOrganizationUnits();
        this.unitTypes = ['Tổng cty/cty', 'Chi nhánh', 'Văn phòng đại diện', 'Địa điểm kinh doanh', 'Phòng ban', 'Khác'];
        this.registerSelectedRow();
        // this.listColumnsUser = ['login', 'fullName'];
        // this.listHeaderColumnsUser = ['username', 'fullname'];
        // this.userService.queryListUser().subscribe(res => {
        //     this.data = res.body;
        // });
        this.userOrgId = new UserIDAndOrgID();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
        if (!window.location.href.includes('/admin/organization-unit')) {
            sessionStorage.removeItem('pageCurrent');
            sessionStorage.removeItem('selectIndex');
            sessionStorage.removeItem('organizationUnitSearch');
        }
    }

    trackId(index: number, item: IOrganizationUnit) {
        return item.id;
    }

    registerChangeInOrganizationUnits() {
        this.eventSubscriber = this.eventManager.subscribe('organizationUnitListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateOrganizationUnits(data: IOrganizationUnit[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.listOrganizationUnit = data;
        if (sessionStorage.getItem('selectIndex')) {
            this.selectedRowOrg = this.listOrganizationUnit[+sessionStorage.getItem('selectIndex')];
            sessionStorage.removeItem('selectIndex');
        } else {
            this.selectedRowOrg = this.listOrganizationUnit[0];
        }
    }

    onSelect(select: ITreeOrganizationUnit) {
        this.selectedRow = select;
    }

    onSelectOrg(select: IOrganizationUnit) {
        this.selectedRowOrg = select;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    edit() {
        event.preventDefault();
        if (this.selectedRowOrg.id) {
            sessionStorage.setItem('selectIndex', this.listOrganizationUnit.indexOf(this.selectedRowOrg).toString());
            if (this.checkSearch()) {
                sessionStorage.setItem('organizationUnitSearch', JSON.stringify(this.organizationUnitSearch));
            }
            this.router.navigate(['/admin/organization-unit', this.selectedRowOrg.id, 'edit']);
        }
    }

    delete() {
        event.preventDefault();
        const modalRef = this.modalService.open(OrganizationUnitDeleteDialogAdminComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.organizationUnit = this.selectedRowOrg;
        modalRef.result.then(
            result => {
                // Left blank intentionally, nothing to do here
            },
            reason => {
                // Left blank intentionally, nothing to do here
            }
        );
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    getOrganizationUnitName(organizationUnitID) {
        if (organizationUnitID) {
            const organizationUnit = this.organizationUnits.find(a => a.id === organizationUnitID);
            if (organizationUnit) {
                return organizationUnit.organizationUnitCode;
            }
        }
    }

    getUnitType(unitType) {
        if (unitType === 0) {
            return this.translate.instant('ebwebApp.organizationUnit.corporations');
        }
        if (unitType === 1) {
            return this.translate.instant('ebwebApp.organizationUnit.branch');
        }
        if (unitType === 2) {
            return this.translate.instant('ebwebApp.organizationUnit.representativeOffice');
        }
        if (unitType === 3) {
            return this.translate.instant('ebwebApp.organizationUnit.placeOfBusiness');
        }
        if (unitType === 4) {
            return this.translate.instant('ebwebApp.organizationUnit.department');
        }
        if (unitType === 5) {
            return this.translate.instant('ebwebApp.organizationUnit.other');
        }
    }

    tree(organizationUnits: ITreeOrganizationUnit[], grade) {
        for (let i = 0; i < organizationUnits.length; i++) {
            const newList = this.organizationUnits.filter(a => a.parentID === organizationUnits[i].parent.id);
            for (let j = 0; j < newList.length; j++) {
                if (j === 0) {
                    organizationUnits[i].children = [];
                }
                organizationUnits[i].children.push(Object.assign({}));
                organizationUnits[i].children[j].parent = newList[j];
            }
            if (organizationUnits[i].children && organizationUnits[i].children.length > 0) {
                this.tree(organizationUnits[i].children, grade + 1);
            }
        }
    }

    addNew(event) {
        event.preventDefault();
        this.router.navigate(['admin/organization-unit', 'new']);
    }

    selectedItemPerPage() {
        if (this.checkSearch()) {
            this.loadAllForSearch();
        } else {
            this.loadAll();
        }
    }

    showPopup(popup) {
        this.ngbModalRef = this.modalService.open(popup, { size: 'lg', backdrop: 'static' });
    }

    saveUserOrg() {
        this.userOrgId.orgID = this.selectedRowOrg.id;
        this.userOrgId.userID = this.selectedRowOrg.userID;
        this.userService.createUserOrg(this.userOrgId).subscribe(res => {
            this.isSaveUserOrg = res.body;
            if (this.isSaveUserOrg) {
                this.toastr.success(
                    this.translate.instant('userManagement.success.saveOrgUser'),
                    this.translate.instant('ebwebApp.ebPackage.success.success')
                );
            } else {
                this.toastr.error(
                    this.translate.instant('userManagement.error.saveOrgUser'),
                    this.translate.instant('ebwebApp.ebPackage.error.error')
                );
            }
            this.ngbModalRef.close();
        });
    }

    editOrg() {}

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
        sessionStorage.setItem('organizationUnitSearch', JSON.stringify(this.organizationUnitSearch));
        this.loadAllForSearch();
    }

    loadAllForSearch() {
        this.organizationUnitService
            .getOrganizationUnitsBySearch({
                page: this.pageSearch - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                organizationUnitSearch: JSON.stringify(this.searchOrganizationUnit())
            })
            .subscribe(
                (res: HttpResponse<IOrganizationUnit[]>) => this.paginateOrganizationUnits(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    searchOrganizationUnit() {
        const searchOrg: IOrganizationUnitSearch = {};
        if (this.organizationUnitSearch.organizationUnitCode === '') {
            this.organizationUnitSearch.organizationUnitCode = undefined;
        }
        if (this.organizationUnitSearch.organizationUnitName === '') {
            this.organizationUnitSearch.organizationUnitName = undefined;
        }
        if (this.organizationUnitSearch.taxCode === '') {
            this.organizationUnitSearch.taxCode = undefined;
        }
        searchOrg.organizationUnitCode =
            this.organizationUnitSearch.organizationUnitCode !== undefined ? this.organizationUnitSearch.organizationUnitCode : null;
        searchOrg.organizationUnitName =
            this.organizationUnitSearch.organizationUnitName !== undefined ? this.organizationUnitSearch.organizationUnitName : null;
        searchOrg.taxCode = this.organizationUnitSearch.taxCode !== undefined ? this.organizationUnitSearch.taxCode : null;
        return searchOrg;
    }

    resetSearch() {
        this.organizationUnitSearch.organizationUnitCode = null;
        this.organizationUnitSearch.organizationUnitName = null;
        this.organizationUnitSearch.taxCode = null;
        sessionStorage.removeItem('organizationUnitSearch');
        this.startLoadALL();
    }
}
