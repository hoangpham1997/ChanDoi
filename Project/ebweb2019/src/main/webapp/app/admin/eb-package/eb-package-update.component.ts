import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IEbPackage } from 'app/shared/model/eb-package.model';
import { EbPackageService } from './eb-package.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from 'app/shared/base-component/base.component';

@Component({
    selector: 'eb-eb-package-update',
    templateUrl: './eb-package-update.component.html',
    styleUrls: ['./eb-package-update.component.css']
})
export class EbPackageUpdateComponent extends BaseComponent implements OnInit {
    private _ebPackage: IEbPackage;
    isSaving: boolean;
    checkUser: boolean;
    checkVoucher: boolean;
    checkTime: boolean;
    userShow: any;
    voucherShow: any;
    timeShow: any;
    listType = [{ id: 1, nameType: 'Kế toán thường' }, { id: 2, nameType: 'Kế toán đa chi nhánh' }, { id: 3, nameType: 'Kế toán dịch vụ' }];

    constructor(
        private ebPackageService: EbPackageService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {
        super();
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ ebPackage }) => {
            this.ebPackage = ebPackage;
            this.checkUser = this.ebPackage.limitedUser === -1;
            this.checkVoucher = this.ebPackage.limitedVoucher === -1;
            this.checkTime = this.ebPackage.expiredTime === -1;
            this.userShow = this.ebPackage.limitedUser !== -1 ? this.ebPackage.limitedUser : undefined;
            this.voucherShow = this.ebPackage.limitedVoucher !== -1 ? this.ebPackage.limitedVoucher : undefined;
            this.timeShow = this.ebPackage.expiredTime !== -1 ? this.ebPackage.expiredTime : undefined;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        event.preventDefault();
        this.isSaving = true;
        if (this.checkError()) {
            if (this.userShow !== undefined && this.userShow !== null) {
                this.ebPackage.limitedUser = this.userShow;
            }
            if (this.voucherShow !== undefined && this.voucherShow !== null) {
                this.ebPackage.limitedVoucher = this.voucherShow;
            }
            if (this.timeShow !== undefined && this.timeShow !== null) {
                this.ebPackage.expiredTime = this.timeShow;
            }
            if (this.ebPackage.id !== undefined) {
                this.subscribeToSaveResponse(this.ebPackageService.update(this.ebPackage));
            } else {
                this.subscribeToSaveResponse(this.ebPackageService.create(this.ebPackage));
            }
        }
    }

    checkError() {
        if (!this.ebPackage.packageCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.ebPackage.error.requiredPackageCode'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        } else if (!this.ebPackage.packageName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.ebPackage.error.requiredPackageName'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        } else if (!this.ebPackage.limitedCompany || this.ebPackage.limitedCompany <= 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.ebPackage.error.requiredLimitedCompany'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        } else if ((!this.userShow || this.userShow <= 0) && !this.checkUser) {
            this.toastr.error(
                this.translate.instant('ebwebApp.ebPackage.error.requiredLimitedUser'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        } else if ((!this.voucherShow || this.voucherShow <= 0) && !this.checkVoucher) {
            this.toastr.error(
                this.translate.instant('ebwebApp.ebPackage.error.requiredLimitedVoucher'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        } else if ((!this.timeShow || this.timeShow <= 0) && !this.checkTime) {
            this.toastr.error(
                this.translate.instant('ebwebApp.ebPackage.error.requiredExpiredTime'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        } else if (this.ebPackage.comType === undefined || this.ebPackage.comType === null) {
            this.toastr.error(
                this.translate.instant('ebwebApp.ebPackage.error.requiredType'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        }
        return true;
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IEbPackage>>) {
        result.subscribe((res: HttpResponse<IEbPackage>) => this.onSaveSuccess(res), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(res: HttpResponse<any>) {
        this.isSaving = false;
        if (res.body.isSave === true) {
            this.toastr.success(
                this.translate.instant('ebwebApp.organizationUnit.saveSuccess'),
                this.translate.instant('ebwebApp.organizationUnit.message')
            );
            this.previousState();
        } else {
            this.toastr.error(
                this.translate.instant('ebwebApp.ebPackage.packageCodeExist'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
        }
    }

    private onSaveError() {
        this.isSaving = false;
        this.toastr.error(
            this.translate.instant('ebwebApp.ebPackage.packageCodeExist'),
            this.translate.instant('ebwebApp.ebPackage.error.error')
        );
    }
    get ebPackage() {
        return this._ebPackage;
    }

    set ebPackage(ebPackage: IEbPackage) {
        this._ebPackage = ebPackage;
    }

    closeForm() {
        this.router.navigate(['/admin/eb-package']);
    }

    checkCheckBox() {
        if (this.checkUser) {
            this.ebPackage.limitedUser = -1;
            this.userShow = undefined;
        } else if (!this.ebPackage.limitedUser || this.ebPackage.limitedUser <= 0) {
            this.ebPackage.limitedUser = undefined;
        }
        if (this.checkVoucher) {
            this.ebPackage.limitedVoucher = -1;
            this.voucherShow = undefined;
        } else if (!this.ebPackage.limitedVoucher || this.ebPackage.limitedVoucher <= 0) {
            this.ebPackage.limitedVoucher = undefined;
        }
        if (this.checkTime) {
            this.ebPackage.expiredTime = -1;
            this.timeShow = undefined;
        } else if (!this.ebPackage.expiredTime || this.ebPackage.expiredTime <= 0) {
            this.ebPackage.expiredTime = undefined;
        }
    }
}
