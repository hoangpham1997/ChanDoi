import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ITEMS_PER_PAGE } from 'app/shared';
import { Principal, UserService, User, IUser } from 'app/core';
import { EbGroup, IEbGroup } from 'app/core/eb-group/eb-group.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { EbOrganizationUnitModalService } from 'app/shared/modal/eb-organization-unit/eb-organization-unit-modal.service';
import { Subscription } from 'rxjs';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { EbGroupDeleteDialogComponent, EbGroupService } from 'app/phan-quyen/eb-group';
import { OrganizationUnitTree } from 'app/shared/model/organization-unit-tree/organization-unit-tree.model';
import { OrgTreeUserDTO } from 'app/shared/model/OrgTreeUser';
import { EbGroupModalService } from 'app/shared/modal/eb-group/eb-group-modal.service';
import { EbGroupByOrgDeleteDialogComponent } from 'app/phan-quyen/permission-user/eb-group-delete-dialog.component';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { cloneDeep } from 'lodash';
import { SDSoQuanTri } from 'app/app.constants';

@Component({
    selector: 'eb-permission-user',
    templateUrl: './permission-user.component.html',
    styleUrls: ['./permission-user.component.css']
})
export class PermissionUserComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('contentForOne') public modalOneComponent: NgbModalRef;
    @ViewChild('contentForAll') public modalAllComponent: NgbModalRef;
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
    selectedUser: IUser;
    selectedOrg: any;
    ebGroups: IEbGroup[];
    modalRef: NgbModalRef;
    userLogin: string;
    isSaving: boolean;
    eventSubscriber: Subscription;
    orgs: any[];
    currentIndex: number;
    orgUnits: OrganizationUnitTree[];
    checkedAll: boolean;
    selectedGroup: EbGroup;
    orgTreeUserDTO: any;
    isAdminWithTotalPackage: boolean;
    myClonedArray: any[];
    isNotCheckOrg: boolean;
    isNotCheckGroup: boolean;
    isNotAnyChecked: boolean;
    SDSoQuanTri_NotUse: string;

    constructor(
        private ebGroupService: EbGroupService,
        private alertService: JhiAlertService,
        private principal: Principal,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private modalService: NgbModal,
        private ebOrganizationUnitModalService: EbOrganizationUnitModalService,
        private userService: UserService,
        private toastr: ToastrService,
        private translate: TranslateService,
        private organizationUnitService: OrganizationUnitService,
        public activeModal: NgbActiveModal,
        private ebGroupModalService: EbGroupModalService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        if (this.activatedRoute.snapshot.paramMap.has('login')) {
            this.userLogin = this.activatedRoute.snapshot.paramMap.get('login');
        }
        this.getUser();
        this.orgTreeUserDTO = {};
        this.SDSoQuanTri_NotUse = SDSoQuanTri.NOT_USE;
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.isAdminWithTotalPackage = account.ebPackage.isTotalPackage;
            this.loadAll();
            this.registerChangeInUsers();
            this.registerRef();
        });
    }

    ngOnDestroy() {
        this.routeData.unsubscribe();
    }

    registerChangeInUsers() {
        this.eventManager.subscribe('ebGroupListModification', response => this.loadAll());
    }

    getUser() {
        this.selectedUser = {};
        this.userService.find(this.userLogin).subscribe((res: HttpResponse<any>) => {
            this.selectedUser = res.body;
            console.log('selectUser : ');
            console.log(this.selectedUser);
        });
    }

    loadAll() {
        this.orgs = this.orgs ? this.orgs : [];
        this.organizationUnitService
            .getAllOuTreeByOrgId({
                userLogin: this.userLogin
            })
            .subscribe(res => {
                this.orgUnits = res.body;
                this.myClonedArray = cloneDeep(this.orgUnits);
                this.selectedOrg = this.orgUnits[0];
                this.currentIndex = 0;
                this.ebGroups = this.selectedOrg.groups;
                this.checkedAll = this.isCheckAllEbGroup();
            });
    }

    setCheckbox() {
        this.orgUnits.forEach(item => {
            const checked = this.orgs.some(org => org.value === item.value);
            item.check = checked ? checked : item.check;
            item.workingOnBook = checked ? this.orgs.find(x => x.value === item.value).workingOnBook : 2;
            // item.collapse = item.children.some(x => x.check);
            // check children
            item.children.forEach(item2 => {
                const checked2 = this.orgs.some(org => org.value === item2.value);
                item2.check = checked2 ? checked2 : item2.check;
                item2.workingOnBook = checked2 ? this.orgs.find(x => x.value === item2.value).workingOnBook : 2;
            });
        });
    }

    setValueDefault() {
        this.orgUnits.forEach(item => {
            item.check = false;
            item.workingOnBook = 2;
            // check children
            item.children.forEach(item2 => {
                item2.check = false;
                item2.workingOnBook = 2;
            });
        });
    }

    trackIdentity(index, item: IEbGroup) {
        return item.id;
    }

    private onError(error) {
        this.alertService.error(error.error, error.message, null);
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    openModal(i) {
        this.modalRef = this.ebOrganizationUnitModalService.open(this.ebGroups[i].listOrg);
        this.currentIndex = i;
    }

    save() {
        event.preventDefault();
        if (!this.checkPreFillToSave()) {
            return;
        }
        this.fillToSave();
        if (this.selectedUser.id !== undefined) {
            this.userService.updateForEbGroupOrg(this.orgTreeUserDTO).subscribe(
                response => {
                    if (response.body === true) {
                        this.toastr.success(this.translate.instant('permissionUser.success.updated'));
                        this.onSaveSuccess(response);
                    } else {
                        this.toastr.error(this.translate.instant('permissionUser.error.updatedFail'));
                    }
                },
                () => this.onSaveError()
            );
        }
    }

    checkError() {
        if (this.orgTreeUserDTO.listOrg.length === 0) {
            this.toastr.error(this.translate.instant('permissionUser.error.requiredOrgUnit'));
            this.orgUnits = cloneDeep(this.myClonedArray);
            return false;
        }
        if (this.orgTreeUserDTO.listOrg.length > 0 && this.orgTreeUserDTO.listOrg.some(x => x.groups === 0)) {
            this.toastr.error(this.translate.instant('permissionUser.error.requiredOrgUnit'));
            this.orgUnits = cloneDeep(this.myClonedArray);
            return false;
        }
        return true;
    }

    checkPreFillToSave() {
        this.isNotCheckOrg = this.orgUnits.some(
            x => (!x.check && x.groups.some(a => a.check)) || x.children.some(y => !y.check && y.groups.some(b => b.check))
        );
        this.isNotCheckGroup = this.orgUnits.some(
            x => (x.check && x.groups.every(a => !a.check)) || x.children.some(y => y.check && y.groups.every(b => !b.check))
        );
        this.isNotAnyChecked = this.orgUnits.some(
            x => x.check || x.groups.some(a => a.check) || x.children.some(y => y.check || y.groups.some(b => b.check))
        );
        if (!this.isNotAnyChecked) {
            this.toastr.error(this.translate.instant('permissionUser.error.requiredOrgUnit'));
            return false;
        }
        if (this.isNotCheckOrg || this.isNotCheckGroup) {
            this.toastr.error(this.translate.instant('permissionUser.error.requiredBothOrgUnit'));
            return false;
        }
        return true;
    }

    fillToSave() {
        const newListOrgs = [];
        const newListGroups = [];
        const newListGroupsChild = [];
        const listOrgs = [];
        this.orgUnits.forEach(org => {
            org.groups.forEach(gr => {
                if (gr.check) {
                    newListGroups.push(gr);
                }
            });
            org.groups = newListGroups;
            // check if has checked group and org, push arr
            if (org.check && org.groups.some(x => x.check)) {
                newListOrgs.push(org);
            }
            // check children
            if (org.children.some(x => x.check)) {
                org.children.forEach(child => {
                    child.groups.forEach(gr => {
                        if (gr.check) {
                            newListGroupsChild.push(gr);
                        }
                    });
                    child.groups = newListGroupsChild;
                    // check if has checked group and org child, push arr
                    if (child.check && child.groups.some(x => x.check)) {
                        newListOrgs.push(child);
                    }
                });
            }
        });
        this.orgTreeUserDTO.listOrg = newListOrgs;
        this.orgTreeUserDTO.user = this.selectedUser;
    }

    private onSaveSuccess(result) {
        this.isSaving = false;
        this.router.navigate(['/user-management']);
    }

    private onSaveError() {
        this.isSaving = false;
        this.toastr.error(this.translate.instant('permissionUser.error.updatedFail'));
    }

    previousState() {
        window.history.back();
    }

    registerRef() {
        this.eventSubscriber = this.eventManager.subscribe('selectOrgUnits', response => {
            this.orgs = response.content;
            console.log('after Apply: ');
            console.log(response.content);
            let strD = '';
            for (let i = 0; i < this.orgs.length; i++) {
                strD += this.orgs[i].text + '/';
            }
            this.ebGroups[this.currentIndex].textOrg = strD.substring(0, strD.length - 1);
            this.ebGroups[this.currentIndex].listOrg = this.orgs;
            // this.ebGroups[this.currentIndex].workingOnBook = this.orgs.map(x => x.workingOnBook);
        });
    }

    apply() {
        const result = [];
        this.orgUnits.forEach(item => {
            if (item.check) {
                result.push(item);
            }
            // check children
            item.children.forEach(item2 => {
                if (item2.check) {
                    result.push(item2);
                }
            });
        });
        console.log('APPLY:');
        console.log(result);
        this.eventManager.broadcast({
            name: 'selectOrgUnits',
            content: result
        });
        this.activeModal.dismiss(true);
    }

    isCheckAll() {
        const isCheckedAllParents = this.orgUnits ? this.orgUnits.every(item => item.check) : false;
        let isCheckedAllChildren = false;
        if (this.orgUnits ? this.orgUnits.some(item => item.children.length > 0) : false) {
            for (let i = 0; i < this.orgUnits.length; i++) {
                if (this.orgUnits[i].children.length > 0) {
                    const isCheck = this.orgUnits[i].children.every(x => x.check);
                    if (!isCheck) {
                        isCheckedAllChildren = false;
                        break;
                    } else {
                        isCheckedAllChildren = true;
                    }
                }
            }
        } else {
            isCheckedAllChildren = true;
        }
        return isCheckedAllChildren && isCheckedAllParents;
    }

    checkAll() {
        const isCheck = this.isCheckAll();
        this.orgUnits.forEach(item => {
            item.check = !isCheck;
            item.children.forEach(child => {
                child.check = !isCheck;
            });
        });
    }

    check(org: any) {
        org.check = !org.check;
    }

    close() {
        this.activeModal.dismiss(false);
    }

    getUnitType(unitType: number): string {
        if (unitType === 0) {
            return 'Tổng công ty/Công ty';
        } else if (unitType === 1) {
            return 'Chi nhánh';
        }
    }

    toggleChildren(org: any, index: number) {
        this.selectedOrg = org;
        this.ebGroups = this.selectedOrg.groups;
        org.collapse = !org.collapse;
        if (!this.selectedOrg.check) {
            this.unCheckEbGroup(this.selectedOrg);
        }
        this.checkedAll = this.isCheckAllEbGroup();
    }

    unCheckEbGroup(org: any): void {
        const isCheck = org.groups.some(x => x.check);
        if (isCheck) {
            org.groups.forEach(item => {
                if (item.check) {
                    item.check = false;
                }
            });
        }
        if (this.checkedAll) {
            this.checkedAll = false;
        }
    }

    isCheckAllEbGroup() {
        this.checkedAll = this.ebGroups ? this.ebGroups.every(item => item.check) : false;
        return this.checkedAll;
    }

    checkAllEbGroup() {
        this.checkedAll = this.ebGroups ? this.ebGroups.every(item => item.check) : false;
        if (this.checkedAll) {
            // bo check thi kiem tra ebgroup co listOrg
            if (this.ebGroups.some(item => item.listOrg.length > 0)) {
                this.modalRef = this.modalService.open(this.modalAllComponent, { backdrop: 'static' });
                this.checkedAll = false;
                setTimeout(() => {
                    this.checkedAll = true;
                }, 100);
            } else {
                this.ebGroups.forEach(item => {
                    item.check = false;
                });
                this.checkedAll = false;
            }
        } else {
            this.ebGroups.forEach(item => {
                if (!item.check) {
                    item.check = true;
                }
            });
            this.checkedAll = true;
        }
    }

    checkEbGroup(ebGroup: IEbGroup, event: any) {
        // neu co quyen trong list
        if (ebGroup.check && ebGroup.listOrg.length > 0) {
            this.selectedGroup = ebGroup;
            this.modalRef = this.modalService.open(this.modalOneComponent, { backdrop: 'static' });
        } else if (ebGroup.check && ebGroup.listOrg.length === 0) {
            ebGroup.check = false;
        } else {
            ebGroup.check = true;
        }
        this.checkedAll = this.isCheckAllEbGroup();
        event.stopPropagation();
    }

    onSelectChild(child: any) {
        this.selectedOrg = child;
        this.ebGroups = this.selectedOrg.groups;
        if (!this.selectedOrg.check) {
            this.unCheckEbGroup(this.selectedOrg);
        }
        this.checkedAll = this.isCheckAllEbGroup();
    }

    loadEbGroups(org: any) {
        this.ebGroupService
            .getEbGroupsByOrgId({
                orgID: org.value
            })
            .subscribe((res: any) => {
                this.ebGroups = res.body;
                console.log(this.ebGroups);
            });
    }

    addNewEbGroup(selectedOrg: any) {
        this.modalRef = this.ebGroupModalService.open(null, null, selectedOrg, this.selectedUser);
    }

    editEbGroup(ebGroup: any) {
        this.modalRef = this.ebGroupModalService.open(ebGroup, null, null, this.selectedUser);
    }

    deleteEbGroup(ebGroup: any) {
        const modalRef = this.modalService.open(EbGroupByOrgDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.ebGroup = ebGroup;
        modalRef.result.then(
            result => {
                // Left blank intentionally, nothing to do here
            },
            reason => {
                // Left blank intentionally, nothing to do here
            }
        );
    }

    authorityEbGroup(ebGroup: IEbGroup) {
        this.router.navigate(['role-permission', { id: ebGroup.id }]);
    }

    closeForm() {
        event.preventDefault();
        this.previousState();
    }
}
