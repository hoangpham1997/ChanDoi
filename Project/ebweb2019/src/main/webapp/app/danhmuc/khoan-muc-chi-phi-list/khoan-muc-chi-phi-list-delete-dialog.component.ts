import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAccountList } from 'app/shared/model/account-list.model';
import { KhoanMucChiPhiListService } from './khoan-muc-chi-phi-list.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IExpenseItem } from 'app/shared/model/expense-item.model';

@Component({
    selector: 'eb-expense-list-delete-dialog',
    templateUrl: './khoan-muc-chi-phi-list-delete-dialog.component.html'
})
export class KhoanMucChiPhiListDeleteDialogComponent {
    accountList: IExpenseItem;

    constructor(
        private accountListService: KhoanMucChiPhiListService,
        private router: Router,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {}

    clear() {
        window.history.back();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(IExpenseItemID: string) {
        this.accountListService.delete(IExpenseItemID).subscribe(
            (res: HttpResponse<any>) => {
                this.eventManager.broadcast({
                    name: 'IExpenseItemListModification',
                    content: 'Deleted an accountList'
                });
                this.activeModal.dismiss(true);
                this.toastr.success(this.translate.instant('ebwebApp.expenseItem.deleteSuccessful'));
            },
            (res: HttpErrorResponse) => {
                this.activeModal.dismiss(true);
                this.toastr.error(
                    this.translate.instant(`ebwebApp.expenseItem.licenseError`),
                    this.translate.instant('ebwebApp.expenseItem.error')
                );
            }
        );
        this.router.navigate(['/khoan-muc-chi-phi-list']);
    }
}

@Component({
    selector: 'eb-account-list-delete-popup',
    template: ''
})
export class KhoanMucChiPhiListDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(expenList => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(KhoanMucChiPhiListDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.accountList = expenList.accountList;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], {
                            replaceUrl: true,
                            queryParamsHandling: 'merge'
                        });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], {
                            replaceUrl: true,
                            queryParamsHandling: 'merge'
                        });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
