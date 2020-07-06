import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { ActivatedRoute, Router } from '@angular/router';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { ITEMS_PER_PAGE } from 'app/shared';
import { Principal } from 'app/core';
import { IEbGroup } from 'app/core/eb-group/eb-group.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { EbGroupService } from 'app/phan-quyen/eb-group';
import { RolePermissionService } from 'app/phan-quyen/role-permission/role-permission.service';
import { IEbAuthority } from 'app/core/eb-authority/eb-authority.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { BaseComponent } from 'app/shared/base-component/base.component';

@Component({
    selector: 'eb-role-permission',
    templateUrl: './role-permission.component.html',
    styleUrls: ['./role-permission.component.css']
})
export class RolePermissionComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('content') public modalCloseComponent: NgbModalRef;
    currentAccount: any;
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
    modalRef: NgbModalRef;
    isSaving: boolean;
    eventSubscriber: Subscription;
    ebGroup: IEbGroup;
    ebGroupID: string;
    ebAuthorities: IEbAuthority[];
    newList: any[];
    ebAuthoritiesCopy: IEbAuthority[];
    isReadOnly: any;
    isCheckCanDeactive: any;
    index: any;

    constructor(
        private ebGroupService: EbGroupService,
        private alertService: JhiAlertService,
        private principal: Principal,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private modalService: NgbModal,
        private toastr: ToastrService,
        private translate: TranslateService,
        private rolePermissionService: RolePermissionService,
        public utilsService: UtilsService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        if (this.activatedRoute.snapshot.paramMap.has('id')) {
            this.ebGroupID = this.activatedRoute.snapshot.paramMap.get('id');
        }
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.isCheckCanDeactive = true;
            this.loadAll();
        });
    }

    copyObject() {
        this.ebAuthoritiesCopy = this.utilsService.deepCopy(this.ebAuthorities);
    }

    ngOnDestroy() {
        this.routeData.unsubscribe();
    }

    loadAll() {
        this.ebGroupService.find(this.ebGroupID).subscribe((res: HttpResponse<any>) => {
            this.ebGroup = res.body;
            this.rolePermissionService.getAllAuthTree().subscribe((resD: HttpResponse<any>) => {
                this.ebAuthorities = resD.body;
                // lay ra cac quyen da chon
                for (let l = 0; l < this.ebAuthorities.length; l++) {
                    for (let m = 0; m < this.ebAuthorities[l].children.length; m++) {
                        for (let n = 0; n < this.ebAuthorities[l].children[m].children.length; n++) {
                            for (const item of this.ebGroup.authorities) {
                                if (item.code === this.ebAuthorities[l].children[m].children[n].code) {
                                    this.ebAuthorities[l].children[m].children[n].check = true;
                                }
                            }
                        }
                    }
                }
                // lay ra tat ca cac quyen
                for (let j = 0; j < this.ebAuthorities.length; j++) {
                    for (let i = 0; i < this.ebAuthorities[j].children.length; i++) {
                        let x = 0;
                        for (let k = 0; k < 20; k++) {
                            let number = 20;
                            if (this.ebAuthorities[j].children[i].children[x] && this.ebAuthorities[j].children[i].children[x].code) {
                                number = parseInt(this.ebAuthorities[j].children[i].children[x].code.substring(4), 10);
                            }
                            if (number !== k + 1) {
                                this.ebAuthorities[j].children[i].children.splice(k, 0, {});
                            }
                            x++;
                        }
                    }
                }
                this.copyObject();
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

    save() {
        event.preventDefault();
        this.fillToSave();
        if (this.ebGroup.id !== undefined) {
            this.ebGroupService.updateAuthorities(this.ebGroup).subscribe(
                response => {
                    this.toastr.success(this.translate.instant('rolePermission.success.updated'));
                    this.onSaveSuccess(response);
                },
                () => this.onSaveError()
            );
        }
    }

    fillToSave() {
        this.newList = [];
        for (let j = 0; j < this.ebAuthorities.length; j++) {
            for (let i = 0; i < this.ebAuthorities[j].children.length; i++) {
                for (let k = 0; k < this.ebAuthorities[j].children[i].children.length; k++) {
                    if (this.ebAuthorities[j].children[i].children[k].check) {
                        this.newList.push(this.ebAuthorities[j].children[i].children[k]);
                    }
                }
            }
        }
        // add quyen cha
        this.ebAuthorities.forEach(item => {
            if (this.newList.some(x => x.code.substring(0, 2) === item.code)) {
                this.newList.push(item);
            }
        });
        this.ebGroup.authorities = this.newList;
        // console.log('this.newList');
        // console.log(this.newList);
    }

    private onSaveSuccess(result) {
        this.isSaving = false;
        this.closeModal();
        this.isCheckCanDeactive = false;
        this.router.navigate(['/eb-group']);
        // this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
        this.toastr.error(this.translate.instant('permissionUser.error.updatedFail'));
    }

    previousState() {
        window.history.back();
    }

    closeForm() {
        // event.preventDefault();
        this.isCheckCanDeactive = true;
        if (!this.canDeactive()) {
            this.modalRef = this.modalService.open(this.modalCloseComponent, { backdrop: 'static' });
        } else {
            this.noSaveCloseAndBackList();
        }
        console.log(this.ebAuthorities);
        console.log(this.ebAuthoritiesCopy);
    }

    /*
    * hàm ss du lieu 2 object và 2 mảng object
    * return true: neu tat ca giong nhau
    * return fale: neu 1 trong cac object ko giong nhau
    * */
    canDeactive(): boolean {
        if (this.isCheckCanDeactive) {
            return this.utilsService.isEquivalentArray(this.ebAuthorities, this.ebAuthoritiesCopy);
        }
        return true;
    }

    closeModal() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    noSaveCloseAndBackList() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isCheckCanDeactive = false;
        this.router.navigate(['/eb-group']);
    }

    toggleChildren(authority: IEbAuthority) {
        authority.collapse = !authority.collapse;
    }

    isCheckedFirst(children: any[]): boolean {
        let newList = [];
        newList = children.filter(n => n.id);
        return newList.some(item => item.check) && newList.length > 0;
    }

    isCheckedRow(children: any[]): boolean {
        let newList = [];
        newList = children.filter(n => n.id);
        return newList.every(item => item.check) && newList.length > 0;
    }

    checkRow(children: any[]) {
        // check row is Check all?
        let newList = [];
        newList = children.filter(n => n.id);
        const isCheck = newList.every(item => item.check) && newList.length > 0;
        // set value for check all
        children.forEach(item => {
            if (item.id) {
                item.check = !isCheck;
            }
        });
    }

    checkFirst(children: IEbAuthority[], child: IEbAuthority) {
        child.check = !child.check;
        let newList = [];
        newList = children.filter(n => n.id);
        newList.splice(0, 1);
        const isCheck = newList.some(item => item.check) && newList.length > 0;
        children[0].check = isCheck;
    }

    getIndex(index: any, code: any) {
        // bỏ qua các quyền không hiển thị
        const authArr = ['0510', '1514', '1515', '1516', '1518', '1523', '1524', '1525', '1533', '1534'];
        if (!authArr.some(n => n === code)) {
            this.index = index === 0 ? 1 : this.index + 1;
            return this.index;
        }
    }

    checkViewAuthority(children: IEbAuthority[], child: IEbAuthority) {
        child.check = !child.check;
        let newList = [];
        newList = children.filter(n => n.id);
        newList.splice(0, 1);
        if (!child.check) {
            newList.forEach(item => {
                if (item.check) {
                    item.check = false;
                }
            });
        }
    }
}
