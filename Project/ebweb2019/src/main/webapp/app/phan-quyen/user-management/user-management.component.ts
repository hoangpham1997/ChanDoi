import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ITEMS_PER_PAGE } from 'app/shared';
import { Principal, UserService, User, IUser } from 'app/core';
import { UserModalService } from 'app/shared/modal/user/user-modal.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UserMgmtDeleteDialogComponent } from 'app/phan-quyen/user-management/user-management-delete-dialog.component';
import { EbPackageService } from 'app/admin';
import { EbPackage } from 'app/app.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';

@Component({
    selector: 'eb-user-mgmt',
    templateUrl: './user-management.component.html',
    styleUrls: ['./user-management.component.css']
})
export class UserMgmtComponent extends BaseComponent implements OnInit, OnDestroy {
    currentAccount: any;
    users: IUser[];
    error: any;
    success: any;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    selectedRow: IUser;
    ebGroups: any[];
    modalRef: NgbModalRef;
    selectedRowRes: IUser;

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
        private ebPackageService: EbPackageService,
        private translate: TranslateService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.loadAll();
            this.registerChangeInUsers();
        });
        this.eventManager.subscribe('closePopupInfo', res => {
            this.inPopup = res.content;
        });
    }

    ngOnDestroy() {
        this.routeData.unsubscribe();
    }

    registerChangeInUsers() {
        this.eventManager.subscribe('userListModification', response => this.loadAll());
    }

    setActive(user, isActivated) {
        user.activated = isActivated;

        this.userService.update(user).subscribe(response => {
            if (response.status === 200) {
                this.error = null;
                this.success = 'OK';
                this.loadAll();
            } else {
                this.success = null;
                this.error = 'ERROR';
            }
        });
    }

    loadAll() {
        this.userService
            .queryClient({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<User[]>) => this.onSuccess(res.body, res.headers),
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

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/user-management'], {
            queryParams: {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    delete() {
        event.preventDefault();
        const modalRef = this.modalService.open(UserMgmtDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.user = this.selectedRow;
        modalRef.result.then(
            result => {
                // Left blank intentionally, nothing to do here
            },
            reason => {
                // Left blank intentionally, nothing to do here
            }
        );
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        this.users = data;
        this.objects = data;
        //  load first element
        this.selectedRow = this.users[0];
        if (this.selectedRow) {
            this.userService.find(this.selectedRow.login).subscribe((res: HttpResponse<any>) => {
                this.selectedRowRes = res.body;
                this.selectedRowRes.ebGroups = res.body.ebGroups;
                this.ebGroups = res.body.ebGroups;
                this.selectedRowRes.ebGroups.forEach(item => {
                    item.check = true;
                });
            });
        } else {
            this.ebGroups = null;
        }
    }

    private onError(error) {
        this.alertService.error(error.error, error.message, null);
    }

    onSelect(select: IUser) {
        this.selectedRow = select;
        this.userService.find(this.selectedRow.login).subscribe((res: HttpResponse<IUser>) => {
            this.selectedRowRes = res.body;
            // console.log('USER find by Login (isSystem) : ' + this.selectedRowRes.isSystem);
            this.selectedRowRes.ebGroups = res.body.ebGroups;
            // console.log('ebGroups find by Login : ' + JSON.stringify(this.selectedRowRes.ebGroups));
            this.ebGroups = res.body.ebGroups;
            this.selectedRowRes.ebGroups.forEach(item => {
                item.check = true;
            });
        });
    }

    edit() {
        this.doubleClickRow(this.selectedRow);
    }

    doubleClickRow(user: IUser) {
        // console.log('ebGroups find by doubleclick : ' + JSON.stringify(this.selectedRowRes.ebGroups));
        // this.router.navigate(['admin/user-management/', user.login, 'edit']);
        event.preventDefault();
        this.modalRef = this.userModalService.open(this.selectedRowRes.login);
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    selectedItemPerPage() {
        this.loadAll();
    }

    addNew($event) {
        event.preventDefault();
        this.inPopup = true;
        this.modalRef = this.userModalService.open(null);
    }

    navigatePermissionUser() {
        this.router.navigate(['permission-user', { login: this.selectedRow.login }]);
    }
}
