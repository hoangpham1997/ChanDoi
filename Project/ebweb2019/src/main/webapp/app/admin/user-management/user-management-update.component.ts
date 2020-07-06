import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { ToastrService } from 'ngx-toastr';

import { IUser, JhiLanguageHelper, Principal, User, UserService } from 'app/core';
import { SD_SO_QUAN_TRI, SO_LAM_VIEC, TCKHAC_SDSoQuanTri } from 'app/app.constants';
import { TranslateService } from '@ngx-translate/core';
import { IEbPackage } from 'app/shared/model/eb-package.model';
import { EbPackageService } from 'app/admin/eb-package/eb-package.service';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitAdminService } from 'app/admin/organization-unit/organization-unit.service';
import { BaseComponent } from 'app/shared/base-component/base.component';

@Component({
    selector: 'eb-user-mgmt-update',
    templateUrl: './user-management-update.component.html',
    styleUrls: ['./user-management-update.component.css']
})
export class UserMgmtUpdateAdminComponent extends BaseComponent implements OnInit, OnDestroy {
    user: IUser;
    languages: any[];
    authorities: any[];
    isSaving: boolean;
    currentAccount: any;
    isShow: boolean;
    listStatus = [{ id: 0, name: 'Chưa dùng' }, { id: 1, name: 'Đang dùng' }, { id: 2, name: 'Hủy' }, { id: 3, name: 'Xóa' }];
    workOnBook: number;
    dataTCKHAC_SDSoQuanTri: number;
    dataSO_LAM_VIEC: number;
    listEbPackage: IEbPackage[];
    listColumnsEbPackage: any;
    listHeaderColumnsEbPackage: any;
    data: IOrganizationUnit[];
    dataCurrentName: string;
    listColumnsOrganizationUnit: any;
    listHeaderColumnsOrganizationUnit: any;
    oldPassword: any;
    toEmail: any;

    constructor(
        private languageHelper: JhiLanguageHelper,
        private userService: UserService,
        private ebPackageService: EbPackageService,
        private organizationUnitAdminService: OrganizationUnitAdminService,
        private route: ActivatedRoute,
        private router: Router,
        private toastr: ToastrService,
        private translate: TranslateService,
        private principal: Principal
    ) {
        super();
        this.listColumnsOrganizationUnit = ['organizationUnitName'];
        this.listHeaderColumnsOrganizationUnit = ['Tên công ty'];
    }

    ngOnInit() {
        this.isSaving = false;
        this.toEmail = true;
        this.route.data.subscribe(({ user }) => {
            this.user = user.body ? user.body : user;
            this.user.confirmPassword = this.user.password;
            this.oldPassword = this.user.password;
            // this.principal.identity().then(account => {
            //     this.currentAccount = account;
            //     if (this.currentAccount) {
            //         this.dataTCKHAC_SDSoQuanTri = +this.currentAccount.systemOption.find(x => x.code === TCKHAC_SDSoQuanTri).data;
            //         this.dataSO_LAM_VIEC = +this.currentAccount.systemOption.find(x => x.code === SO_LAM_VIEC).data;
            //         this.workOnBook = this.dataTCKHAC_SDSoQuanTri === 0 ? 0 : this.dataSO_LAM_VIEC;
            //         this.user.workOnBook = this.user.workOnBook ? this.user.workOnBook : this.workOnBook;
            //     }
            // });
        });
        this.authorities = [];
        this.userService.authorities().subscribe(authorities => {
            this.authorities = authorities;
        });
        this.languageHelper.getAll().then(languages => {
            this.languages = languages;
        });
        this.listColumnsEbPackage = ['packageCode', 'packageName'];
        this.listHeaderColumnsEbPackage = ['Mã gói', 'Tên gói'];
        this.ebPackageService.queryList().subscribe(res => {
            this.listEbPackage = res.body;
        });
        if (this.user.id) {
            this.organizationUnitAdminService.queryListBigOrg({ userId: this.user.id }).subscribe(res => {
                this.data = res.body;
                if (this.data[this.data.length - 1].isHaveOrg === true) {
                    this.dataCurrentName = this.data[this.data.length - 1].organizationUnitName;
                }
            });
        } else {
            this.organizationUnitAdminService.queryListBigOrg().subscribe(res => {
                this.data = res.body;
            });
        }
    }

    previousState() {
        window.history.back();
    }

    save() {
        event.preventDefault();
        this.isSaving = true;
        this.user.langKey = 'vi';
        if (this.checkError()) {
            if (this.user.id !== null) {
                if (this.oldPassword !== this.user.password) {
                    this.user.isChangePassword = true;
                } else {
                    this.user.isChangePassword = false;
                }
                this.userService.updateInfoAdmin(this.user).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
            } else {
                this.userService.createUserAdmin(this.user).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
            }
        }
        // edit tam thoi -> xoa
        // this.user.ebGroups.forEach(item => {
        //     item.check = true;
        // });
    }

    private checkError() {
        if (!this.user.login) {
            this.toastr.error(
                this.translate.instant('userManagement.error.emailLoginRequired'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        }
        if (!this.user.login.includes('@')) {
            this.toastr.error(
                this.translate.instant('userManagement.error.emailInvalid'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        }
        if (!this.user.password) {
            this.toastr.error(
                this.translate.instant('userManagement.error.passRequired'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        }
        if (this.user.password !== this.user.confirmPassword) {
            this.toastr.error(
                this.translate.instant('userManagement.error.passworddonotmatch'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        }
        if (!this.user.fullName) {
            this.toastr.error(
                this.translate.instant('userManagement.error.fullNameRequired'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        }
        if (!this.user.email) {
            this.toastr.error(
                this.translate.instant('userManagement.error.emailRequired'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        }
        if (!this.user.email.includes('@')) {
            this.toastr.error(
                this.translate.instant('userManagement.error.emailInvalid'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        }
        if (this.user.password.length < 4) {
            this.toastr.error(
                this.translate.instant('userManagement.error.passwordLength'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        }
        if (!this.user.confirmPassword) {
            this.toastr.error(
                this.translate.instant('userManagement.error.confirmPassRequired'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        }
        if (!this.user.packageID) {
            this.toastr.error(
                this.translate.instant('userManagement.error.packageRequired'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        }
        if (this.user.status == null) {
            this.toastr.error(
                this.translate.instant('userManagement.error.statusRequired'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        }
        if (!this.user.orgID) {
            this.toastr.error(
                this.translate.instant('userManagement.error.OrgRequired'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        }
        return true;
    }

    private onSaveSuccess(result) {
        this.isSaving = false;
        if (result.body.status === 0) {
            this.toastr.success(
                this.translate.instant('ebwebApp.ebPackage.success.createComplete'),
                this.translate.instant('ebwebApp.ebPackage.success.success')
            );
            this.router.navigate(['/admin/user-management']);
        } else {
            this.toastr.error(this.translate.instant('userManagement.error.loginExist'));
        }
    }

    private onSaveError() {
        this.isSaving = false;
    }

    closeForm() {
        this.router.navigate(['admin/user-management']);
    }

    ngOnDestroy(): void {
        if (!window.location.href.includes('/admin/user-management')) {
            sessionStorage.removeItem('pageCurrent');
            sessionStorage.removeItem('selectIndex');
            sessionStorage.removeItem('userSearch');
        }
    }

    LoginToEmail() {
        if (this.toEmail) {
            this.user.email = this.user.login;
        }
    }

    checkToEmail() {
        this.toEmail = false;
    }
}
