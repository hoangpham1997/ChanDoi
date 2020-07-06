import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ITEMS_PER_PAGE } from 'app/shared';
import { Principal, UserService, User, IUser } from 'app/core';
import { IEbGroup } from 'app/core/eb-group/eb-group.model';
import { EbGroupModalService } from 'app/shared/modal/eb-group/eb-group-modal.service';
import { EbGroupService } from 'app/phan-quyen/eb-group/eb-group.service';
import { EbGroupDeleteDialogComponent } from 'app/phan-quyen/eb-group/eb-group-delete-dialog.component';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from 'app/shared/base-component/base.component';

@Component({
    selector: 'eb-group',
    templateUrl: './eb-group.component.html',
    styleUrls: ['./eb-group.component.css']
})
export class EbGroupComponent extends BaseComponent implements OnInit, OnDestroy {
    currentAccount: any;
    users: any[];
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
    selectedRow: IEbGroup;
    ebGroups: IEbGroup[];
    modalRef: NgbModalRef;

    constructor(
        private ebGroupService: EbGroupService,
        private alertService: JhiAlertService,
        private principal: Principal,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private modalService: NgbModal,
        private ebGroupModalService: EbGroupModalService,
        private toastr: ToastrService,
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
        this.eventManager.subscribe('ebGroupListModification', response => this.loadAll());
    }

    loadAll() {
        this.ebGroupService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<User[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    trackIdentity(index, item: IEbGroup) {
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
        this.router.navigate(['/eb-group'], {
            queryParams: {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    delete() {
        event.preventDefault();
        const modalRef = this.modalService.open(EbGroupDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.ebGroup = this.selectedRow;
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
        this.ebGroups = data;
        this.objects = data;
        //  load first element
        this.selectedRow = this.ebGroups[0];
        if (this.selectedRow) {
            this.ebGroupService.find(this.selectedRow.id).subscribe((res: HttpResponse<any>) => {
                this.users = res.body.users;
            });
        } else {
            this.users = null;
        }
    }

    private onError(error) {
        this.alertService.error(error.error, error.message, null);
    }

    onSelect(select: any) {
        this.selectedRow = select;
        this.ebGroupService.find(this.selectedRow.id).subscribe((res: HttpResponse<IEbGroup>) => {
            this.users = res.body.users;
            // console.log('ebGroups: ' + JSON.stringify(this.users));
        });
    }

    edit() {
        this.doubleClickRow(this.selectedRow);
    }

    doubleClickRow(ebGroup: IEbGroup) {
        event.preventDefault();
        // this.router.navigate(['admin/eb-group/', id, 'edit']);
        this.modalRef = this.ebGroupModalService.open(ebGroup);
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
        this.modalRef = this.ebGroupModalService.open(null);
    }

    navigateRolePermission() {
        this.router.navigate(['role-permission', { id: this.selectedRow.id }]);
        // if (this.selectedRow.authorities.length > 0) {
        //     this.router.navigate(['role-permission', { id: this.selectedRow.id }]);
        // } else {
        //     this.toastr.warning(this.translate.instant('userManagement.warning.roleNotFound'));
        //     return;
        // }
    }
}
