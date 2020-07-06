import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ITEMS_PER_PAGE_ADMIN } from 'app/shared';
import { Principal, UserService, User, IUser } from 'app/core';
import { UserModalService } from 'app/shared/modal/user/user-modal.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UserMgmtDeleteDialogAdminComponent } from 'app/admin';
import { EbPackageService } from 'app/admin/eb-package/eb-package.service';
import { OrganizationUnitAdminService } from 'app/admin/organization-unit/organization-unit.service';
import { SD_SO_QUAN_TRI, statusOfUser } from 'app/app.constants';
import { IOrganizationUnit, OrganizationUnit } from 'app/shared/model/organization-unit.model';
import { UserIDAndOrgID } from 'app/shared/model/UserIDAndOrgID';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IEbPackage } from 'app/shared/model/eb-package.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { forEach } from '@angular/router/src/utils/collection';
import { IUserSearch, UserSearch } from 'app/shared/model/userSearch.model';

@Component({
    selector: 'eb-user-mgmt-admin',
    templateUrl: './user-management.component.html',
    styleUrls: ['./user-management.component.css']
})
export class UserMgmtAdminComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('alertActiveModal') alertActiveModal: any;
    currentAccount: any;
    users: User[];
    error: any;
    success: any;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    pageSearch: any;
    previousPageSearch: any;
    predicate: any;
    previousPage: any;
    ngbModalRef: any;
    reverse: any;
    data: IOrganizationUnit[];
    selectedRow: IUser;
    // ebGroups: any[];
    modalRef: NgbModalRef;
    listColumnsOrganizationUnit: any;
    listHeaderColumnsOrganizationUnit: any;
    orgId: string;
    userOrgID: UserIDAndOrgID;
    isSaveUserOrg: boolean;
    listEbPackage: IEbPackage[];
    private _user: IUser;
    listStatus = [{ id: 0, name: 'Chưa dùng' }, { id: 1, name: 'Đang dùng' }, { id: 2, name: 'Hủy' }, { id: 3, name: 'Xóa' }];
    checkPackage: boolean;
    packageName: string;
    isShowSearch: boolean;
    userSearch: IUserSearch;
    totalOrg: IOrganizationUnit[];
    pageShow: any;

    constructor(
        private userService: UserService,
        private alertService: JhiAlertService,
        private principal: Principal,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private modalService: NgbModal,
        private userModalService: UserModalService,
        private toastr: ToastrService,
        private organizationUnitAdminService: OrganizationUnitAdminService,
        private translate: TranslateService,
        public utilsService: UtilsService,
        private ebPackageService: EbPackageService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE_ADMIN;
        this.userSearch = new UserSearch();
        this.userSearch.organizationUnit = new OrganizationUnit();
        if (sessionStorage.getItem('userSearch')) {
            this.userSearch = JSON.parse(sessionStorage.getItem('userSearch'));
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
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
    }

    ngOnInit() {
        this.userOrgID = new UserIDAndOrgID();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.registerChangeInUsers();
            if (this.checkSearch()) {
                this.startLoadAllSearch();
            } else {
                this.startLoadALL();
            }
        });
        this.ebPackageService.queryList().subscribe(res => {
            this.listEbPackage = res.body;
        });
        this.organizationUnitAdminService.queryListBigOrg().subscribe(res => {
            this.data = res.body;
        });
        this.organizationUnitAdminService.queryTotalOrg().subscribe(res => {
            this.totalOrg = res.body;
        });
        this.listColumnsOrganizationUnit = ['organizationUnitName'];
        this.listHeaderColumnsOrganizationUnit = ['Tên công ty'];
    }

    ngOnDestroy() {
        this.routeData.unsubscribe();
        if (!window.location.href.includes('/admin/user-management')) {
            sessionStorage.removeItem('pageCurrent');
            sessionStorage.removeItem('selectIndex');
            sessionStorage.removeItem('userSearch');
        }
    }

    registerChangeInUsers() {
        this.eventManager.subscribe('userListModification', response => this.loadAll());
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    activeUserOnSave(alertActiveModal) {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this._user.status = this._user.statusClone;
        if (this.checkPackage) {
            this.openModal(alertActiveModal);
            this.checkPackage = false;
            return;
        }
        this._user.ebUserPackage.organizationUnit = {};
        this._user.ebUserPackage.organizationUnit.id = this.user.orgID;
        this.userService.activePackageNoSendCrm(this._user.ebUserPackage).subscribe(response => {
            this.toastr.success(
                this.translate.instant('ebwebApp.ebPackage.success.createComplete'),
                this.translate.instant('ebwebApp.ebPackage.success.success')
            );
        });
        this.close();
        this.loadAll();
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
        this.userService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<any>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    trackIdentity(index, item: User) {
        return item.id;
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    checkSearch() {
        if (
            (this.userSearch.login !== undefined && this.userSearch.login !== null) ||
            (this.userSearch.fullName !== undefined && this.userSearch.fullName !== null) ||
            (this.userSearch.mobilePhone !== undefined && this.userSearch.mobilePhone !== null) ||
            (this.userSearch.organizationUnit !== undefined &&
                this.userSearch.organizationUnit !== null &&
                this.userSearch.organizationUnit.id) ||
            (this.userSearch.status !== undefined && this.userSearch.status !== null)
        ) {
            return true;
        }
        return false;
    }

    loadPage(page) {
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
            this.router.navigate(['/admin/user-management'], {
                queryParams: {
                    page: this.pageSearch,
                    size: this.itemsPerPage,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
            });
            this.loadAllForSearch();
        } else {
            this.router.navigate(['/admin/user-management'], {
                queryParams: {
                    page: this.page,
                    size: this.itemsPerPage,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
            });
            this.loadAll();
        }
    }

    delete() {
        event.preventDefault();
        const modalRef = this.modalService.open(UserMgmtDeleteDialogAdminComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance._user = this.selectedRow;
        modalRef.result.then(
            result => {
                // Left blank intentionally, nothing to do here
            },
            reason => {
                // Left blank intentionally, nothing to do here
            }
        );
    }

    getStatus(status: number): string {
        if (status === statusOfUser.CHUA_DUNG) {
            return 'Chưa dùng';
        } else if (status === statusOfUser.DANG_DUNG) {
            return 'Đang dùng';
        } else if (status === statusOfUser.HUY) {
            return 'Hủy';
        } else if (status === statusOfUser.XOA) {
            return 'Xóa';
        }
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        this.users = data.sort((a, b) => (a.login > b.login ? 1 : -1));
        this.users.forEach(user => (user.statusClone = user.status));
        //  load first element
        if (sessionStorage.getItem('selectIndex')) {
            this.selectedRow = this.users[+sessionStorage.getItem('selectIndex')];
            sessionStorage.removeItem('selectIndex');
        } else {
            this.selectedRow = this.users[0];
        }
        this._user = this.users[0];
        if (this.selectedRow) {
            this.userService.find(this.selectedRow.login).subscribe((res: HttpResponse<any>) => {
                this.selectedRow.ebGroups = res.body.ebGroups;
                // this.ebGroups = res.body.ebGroups;
                this.selectedRow.ebGroups.forEach(item => {
                    item.check = true;
                });
            });
        } else {
            // this.ebGroups = null;
        }
    }

    private onError(error) {
        this.alertService.error(error.error, error.message, null);
    }

    onSelect(select: any) {
        this._user = select;
        this._user.statusClone = this._user.status;
        this.selectedRow = this._user;
        this.userService.find(this.selectedRow.login).subscribe((res: HttpResponse<any>) => {
            // console.log('USERabccc : ' + JSON.stringify(res.body));
            this.user = res.body;
            // this.selectedRow.ebGroups = res.body.ebGroups;
            // this.ebGroups = res.body.ebGroups;
            // this.selectedRow.ebGroups.forEach(item => {
            //     item.check = true;
            // });
        });
    }

    doubleClickRow(user: IUser) {
        sessionStorage.setItem('selectIndex', this.users.indexOf(user).toString());
        if (this.checkSearch()) {
            sessionStorage.setItem('userSearch', JSON.stringify(this.userSearch));
        }
        this.router.navigate(['admin/user-management/', user.login, 'edit']);
        // this.modalRef = this.userModalService.open(user);
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    selectedItemPerPage() {
        if (this.checkSearch()) {
            this.loadAllForSearch();
        } else {
            this.loadAll();
        }
    }

    openModal(alertActiveModal) {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.modalRef = this.modalService.open(alertActiveModal, { backdrop: 'static' });
    }

    navigatePermissionUser() {
        if (this.selectedRow.ebGroups.length > 0) {
            this.router.navigate(['admin/permission-user', { login: this.selectedRow.login }]);
        } else {
            this.toastr.warning(this.translate.instant('userManagement.warning.roleNotFound'));
            return;
        }
    }

    addNew($event) {
        this.router.navigate(['admin/user-management/new']);
    }

    onChangeStatus() {
        if (this._user.status !== this._user.statusClone) {
            this.checkPackage = true;
        }
    }

    showPopup(popup) {
        if (this._user.packageID != null) {
            this.packageName = this.listEbPackage.find(x => x.id === this._user.packageID).packageName;
        } else {
        }
        // this.checkPackage = true;

        this.user.ebUserPackage = this.user.ebUserPackage || {};
        this.ngbModalRef = this.modalService.open(popup, { size: 'lg', backdrop: 'static' });
        this._user.statusClone = this._user.status;
        // this.organizationUnitAdminService.queryListUserPackage({ userId: this._user.id }).subscribe(res => {
        //     this.data = res.body;
        // });
    }

    saveUserOrg() {
        this.userOrgID.orgID = this.selectedRow.orgID;
        this.userOrgID.userID = this.selectedRow.id;
        this.userService.createUserOrg(this.userOrgID).subscribe(res => {
            this.isSaveUserOrg = res.body;
            if (this.isSaveUserOrg) {
                this.toastr.success(
                    this.translate.instant('userManagement.success.saveUserOrg'),
                    this.translate.instant('ebwebApp.ebPackage.success.success')
                );
            } else {
                this.toastr.error(
                    this.translate.instant('userManagement.error.saveUserOrg'),
                    this.translate.instant('ebwebApp.ebPackage.error.error')
                );
            }
            this.ngbModalRef.close();
        });
    }

    close() {
        this.ngbModalRef.close();
    }

    get user(): IUser {
        return this._user;
    }

    set user(user: IUser) {
        this._user = user;
    }

    doss() {
        console.log(this.user);
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
        sessionStorage.setItem('userSearch', JSON.stringify(this.userSearch));
        this.loadAllForSearch();
    }

    loadAllForSearch() {
        this.userService
            .queryUserSearch({
                page: this.pageSearch - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                userSearch: JSON.stringify(this.searchUser())
            })
            .subscribe(
                (res: HttpResponse<User[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    searchUser() {
        const userSearchTrans: IUserSearch = new UserSearch();
        userSearchTrans.organizationUnit = new OrganizationUnit();
        if (this.userSearch.login === '') {
            this.userSearch.login = undefined;
        }
        if (this.userSearch.fullName === '') {
            this.userSearch.fullName = undefined;
        }
        if (this.userSearch.mobilePhone === '') {
            this.userSearch.mobilePhone = undefined;
        }
        if (this.userSearch.organizationUnit === '') {
            this.userSearch.organizationUnit = undefined;
        }
        if (this.userSearch.status === '') {
            this.userSearch.status = undefined;
        }
        userSearchTrans.login = this.userSearch.login !== undefined ? this.userSearch.login : null;
        userSearchTrans.fullName = this.userSearch.fullName !== undefined ? this.userSearch.fullName : null;
        userSearchTrans.mobilePhone = this.userSearch.mobilePhone !== undefined ? this.userSearch.mobilePhone : null;
        userSearchTrans.organizationUnit = this.userSearch.organizationUnit !== undefined ? this.userSearch.organizationUnit : null;
        userSearchTrans.status = this.userSearch.status !== undefined ? this.userSearch.status : null;
        return userSearchTrans;
    }

    resetSearch() {
        this.userSearch.login = null;
        this.userSearch.fullName = null;
        this.userSearch.mobilePhone = null;
        this.userSearch.organizationUnit = null;
        this.userSearch.status = null;
        sessionStorage.removeItem('userSearch');
        this.startLoadALL();
    }
}
