import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { JhiLanguageHelper, Principal, User, UserService } from 'app/core';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { selector } from 'rxjs-compat/operator/publish';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IEbGroup } from 'app/core/eb-group/eb-group.model';
import { EbGroupService } from 'app/phan-quyen/eb-group';
import { OrgGroup } from 'app/shared/model/org-group.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { JhiEventManager } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'eb-group-modal',
    templateUrl: './eb-group-modal.component.html',
    styleUrls: ['./eb-group-modal.component.css']
})
export class EbGroupModalComponent extends BaseComponent implements OnInit {
    @Input() public ebGroup: IEbGroup;
    @Input() public orgUnit: any;
    @Input() public user: any;
    languages: any[];
    authorities: any[];
    isSaving: boolean;
    ebGroups: IEbGroup[];
    newList: any[];
    account: any;
    isAdminWithTotalPackage: boolean;
    orgGroup: any;

    constructor(
        private languageHelper: JhiLanguageHelper,
        private ebGroupService: EbGroupService,
        private route: ActivatedRoute,
        private router: Router,
        private principal: Principal,
        public activeModal: NgbActiveModal,
        private toastr: ToastrService,
        private translate: TranslateService,
        public utilsService: UtilsService,
        public eventManager: JhiEventManager
    ) {
        super();
        this.principal.identity().then(account => {
            this.account = account;
            this.isAdminWithTotalPackage = account.ebPackage.isTotalPackage;
        });
        this.orgGroup = {};
    }

    ngOnInit() {
        this.isSaving = false;
        this.ebGroup = this.ebGroup ? this.ebGroup : {};
        this.ebGroupService.find(this.ebGroup.id).subscribe((res: HttpResponse<IEbGroup>) => {
            this.ebGroup = res.body;
            // console.log('ebGroups: ' + JSON.stringify(this.users));
        });
    }

    previousState() {
        window.history.back();
    }

    save(isNew = false) {
        event.preventDefault();
        this.isSaving = true;
        if (this.checkError()) {
            if (this.ebGroup.id !== undefined) {
                this.ebGroupService.update(this.ebGroup).subscribe(
                    (res: any) => {
                        if (res.body.status === 0) {
                            this.toastr.success(this.translate.instant('ebGroup.success.updated'));
                            if (this.isAdminWithTotalPackage) {
                                this.onSaveSuccessForAdminTotal(res);
                            } else {
                                this.onSaveSuccess(res);
                            }
                        } else {
                            this.toastr.error(this.translate.instant('ebGroup.error.codeExist'));
                        }
                    },
                    () => this.onSaveError()
                );
            } else {
                if (this.isAdminWithTotalPackage) {
                    this.orgGroup.ebGroup = this.ebGroup;
                    this.orgGroup.orgId = this.orgUnit.value;
                    this.ebGroupService.createForAdminTotalPackage(this.orgGroup).subscribe(
                        (res: any) => {
                            if (res.body.status === 0) {
                                this.toastr.success(this.translate.instant('ebGroup.success.created'));
                                this.onSaveSuccessForAdminTotal(res);
                            } else {
                                this.toastr.error(this.translate.instant('ebGroup.error.codeExist'));
                            }
                        },
                        () => this.onSaveError()
                    );
                } else {
                    this.ebGroupService.create(this.ebGroup).subscribe(
                        (res: any) => {
                            if (res.body.status === 0) {
                                this.toastr.success(this.translate.instant('ebGroup.success.created'));
                                this.onSaveSuccess(res);
                            } else {
                                this.toastr.error(this.translate.instant('ebGroup.error.codeExist'));
                            }
                        },
                        () => this.onSaveError()
                    );
                }
            }
        }
    }

    checkError(): boolean {
        if (!this.ebGroup.code) {
            this.toastr.error(this.translate.instant('ebGroup.error.codeRequired'));
            return false;
        } else if (!this.ebGroup.name) {
            this.toastr.error(this.translate.instant('ebGroup.error.nameRequired'));
            return false;
        } else {
            return true;
        }
    }

    private onSaveSuccess(result) {
        this.isSaving = false;
        this.closeForm();
        this.router.navigate(['/eb-group/new']).then(() => {
            this.router.navigate(['/eb-group']);
        });
    }

    private onSaveSuccessForAdminTotal(result) {
        this.isSaving = false;
        this.closeForm();
        this.router.navigate(['/eb-group']).then(() => {
            this.router.navigate(['permission-user', { login: this.user.login }]);
        });
    }

    private onSaveError() {
        this.isSaving = false;
    }

    closeForm() {
        event.preventDefault();
        this.eventManager.broadcast({
            name: 'closePopupInfo',
            content: false
        });
        this.activeModal.dismiss(false);
    }
}
