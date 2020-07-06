import { Component, OnInit, OnDestroy, ViewChild, AfterViewInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
// import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// import { Observable } from 'rxjs';
import { BudgetItemService } from './budget-item.service';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { BudgetItem } from 'app/shared/model/budget-item.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { Principal } from 'app/core';

@Component({
    selector: 'eb-budget-item-update',
    templateUrl: './budget-item-update.component.html',
    styleUrls: ['./budget-item-update.component.css']
})
export class BudgetItemUpdateComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewInit {
    @ViewChild('content') content: any;
    budgetItem: IBudgetItem;
    budgetItemCopy: IBudgetItem;
    modalRef: NgbModalRef;
    arrAuthorities: any[];
    roleSua: boolean;
    roleThem: boolean;
    budgetItems: BudgetItem[];
    saveandadd = false;
    listColumnsType = ['name'];
    headerColumnsBudgetItem = [this.translate.instant('ebwebApp.budgetItem.code'), this.translate.instant('ebwebApp.budgetItem.name')];
    listColumnsBudgetItem = ['budgetItemCode', 'budgetItemName'];
    isClose = false;
    ROLE_BudgetItem_Them = ROLE.DanhMucThuChi_Them;
    ROLE_BudgetItem_Sua = ROLE.DanhMucThuChi_Sua;
    ROLE_BudgetItem_Xoa = ROLE.DanhMucThuChi_Xoa;
    constructor(
        private budgetItemService: BudgetItemService,
        public utilsService: UtilsService,
        private toastr: ToastrService,
        private translate: TranslateService,
        private router: Router,
        private principal: Principal,
        private activeRoute: ActivatedRoute,
        private eventManager: JhiEventManager,
        private modalService: NgbModal
    ) {
        super();
    }
    newBudgetItem() {
        this.budgetItem = {};
        this.budgetItem.grade = 1;
        this.budgetItem.isActive = true;
        this.budgetItem.budgetItemName = '';
        this.budgetItem.budgetItemCode = '';
        this.budgetItem.budgetItemType = 0;
        this.budgetItem.isParentNode = false;
        this.budgetItem.parent = null;
    }
    ngOnInit() {
        this.saveandadd = false;
        this.newBudgetItem();
        this.budgetItems = [{}];
        this.budgetItem.grade = 1;
        this.principal.identity().then(account => {
            this.arrAuthorities = account.authorities;
            this.roleSua = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_BudgetItem_Sua) : true;
            this.roleThem = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_BudgetItem_Them) : true;
        });
        this.budgetItemService.getAll().subscribe(res => {
            this.budgetItems = res.body;
            this.activeRoute.data.subscribe(budgetItem => {
                if (budgetItem.budgetItem.id) {
                    this.budgetItem = budgetItem.budgetItem;
                    if (this.budgetItem.isParentNode) {
                        this.budgetItems = this.budgetItems.filter(budget => !this.checkIsChildren(budget));
                    }
                    this.budgetItems = this.budgetItems.filter(budget => budget.id !== this.budgetItem.id);
                    this.budgetItem.parent = this.budgetItems.filter(budget => budget.id === this.budgetItem.parentID)[0];
                }
                this.budgetItems = this.budgetItems.filter(budget => budget.isActive);
                this.copy();
            });
        });
    }

    checkIsChildren(budgetItem: BudgetItem) {
        if (budgetItem.parentID === null) {
            return false;
        }
        if (budgetItem.parentID === this.budgetItem.id) {
            return true;
        }
        const budget = this.budgetItems.filter(bud => bud.id === budgetItem.parentID)[0];
        return this.checkIsChildren(budget);
    }

    copy() {
        this.budgetItemCopy = this.utilsService.deepCopy([this.budgetItem])[0];
    }

    ngOnDestroy(): void {}

    previousState() {
        this.router.navigate(['budget-item']);
    }
    saveAndNew() {
        event.preventDefault();
        this.saveandadd = true;
        this.save();
    }
    delete() {
        event.preventDefault();
        if (this.budgetItem.isParentNode) {
            this.toastr.error(this.translate.instant('ebwebApp.budgetItem.notDeleteParentNode'));
        } else if (this.budgetItem.id) {
            this.router.navigate(['/budget-item', { outlets: { popup: this.budgetItem.id + '/delete' } }]);
        }
    }
    save(isNew = false) {
        event.preventDefault();
        if (this.budgetItem.budgetItemCode === '') {
            this.toastr.error(this.translate.instant('ebwebApp.budgetItem.errorInputCode'));
        } else if (this.budgetItem.budgetItemName === '') {
            this.toastr.error(this.translate.instant('ebwebApp.budgetItem.errorInputName'));
        } else {
            if (this.budgetItem.id != null) {
                this.budgetItemService.update(this.budgetItem).subscribe(
                    res => {
                        this.copy();
                        this.toastr.success(this.translate.instant('ebwebApp.budgetItem.successful'));
                        if (!this.saveandadd) {
                            this.previousState();
                        } else {
                            this.router.navigate(['budget-item/new']);
                        }
                    },
                    error => {
                        this.error(error);
                    }
                );
            } else {
                this.budgetItemService.create(this.budgetItem).subscribe(
                    res => {
                        this.copy();
                        this.toastr.success(this.translate.instant('ebwebApp.budgetItem.successful'));
                        if (!this.saveandadd) {
                            this.previousState();
                        } else {
                            this.ngOnInit();
                        }
                    },
                    error => {
                        this.error(error);
                    }
                );
            }
        }
    }
    changeListType() {
        console.log(this.budgetItem.budgetItemType);
    }
    changeBudgetItemParent() {
        console.log(this.budgetItem.parent);
        if (this.budgetItem.parent === null) {
            this.budgetItem.parent = {};
            this.budgetItem.parent.id = null;
            this.budgetItem.parent.grade = 0;
        }
        this.budgetItem.parentID = this.budgetItem.parent.id;
        this.budgetItem.grade = this.budgetItem.parent.grade + 1;
    }
    error(err) {
        this.toastr.error(this.translate.instant(`ebwebApp.budgetItem.${err.error.message}`));
    }
    closeForm() {
        event.preventDefault();
        if (!this.utilsService.isEquivalent(this.budgetItem, this.budgetItemCopy)) {
            if (!this.isClose) {
                this.modalRef = this.modalService.open(this.content, { backdrop: 'static' });
                this.isClose = true;
            }
        } else {
            this.copy();
            this.previousState();
        }
    }
    close() {
        this.isClose = false;
        this.modalRef.close();
    }
    closeContent() {
        this.close();
        this.copy();
        this.previousState();
    }

    saveContent() {
        this.close();
        this.save();
    }
    canDeactive() {
        return this.utilsService.isEquivalent(this.budgetItem, this.budgetItemCopy);
    }

    ngAfterViewInit(): void {
        this.focusFirstInput();
    }
}
