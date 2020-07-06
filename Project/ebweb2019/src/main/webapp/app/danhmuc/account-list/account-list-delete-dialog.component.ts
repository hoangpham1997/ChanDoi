import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from './account-list.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
    selector: 'eb-account-list-delete-dialog',
    templateUrl: './account-list-delete-dialog.component.html'
})
export class AccountListDeleteDialogComponent {
    accountList: IAccountList;

    constructor(
        private accountListService: AccountListService,
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

    confirmDelete(accountListID: string) {
        this.accountListService.deleteAccountList({ id: accountListID }).subscribe(
            (res: HttpResponse<any>) => {
                this.eventManager.broadcast({
                    name: 'accountListListModification',
                    content: 'Deleted an accountList'
                });
                this.activeModal.dismiss(true);
                this.toastr.success(this.translate.instant('ebwebApp.accountList.deleteSuccessful'));
            },
            (res: HttpErrorResponse) => {
                this.activeModal.dismiss(true);
                if (res.error.errorKey === 'errorDeleteParent') {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.accountList.errorDeleteParent'),
                        this.translate.instant('ebwebApp.accountList.error')
                    );
                } else if (res.error.errorKey === 'existConstraint') {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.accountList.existConstraint'),
                        this.translate.instant('ebwebApp.accountList.error')
                    );
                } else {
                    this.toastr.error(this.translate.instant('ebwebApp.accountList.error'));
                }
                return;
            }
        );
        if (this.accountList && this.accountList.id) {
            this.router.navigate(['account-list']);
        } else {
            window.history.back();
        }
    }
}

@Component({
    selector: 'eb-account-list-delete-popup',
    template: ''
})
export class AccountListDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ accountList }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AccountListDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.accountList = accountList;
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
