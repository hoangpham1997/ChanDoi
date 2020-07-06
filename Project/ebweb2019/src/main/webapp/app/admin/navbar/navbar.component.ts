import { Component, OnInit } from '@angular/core';
import { Event, NavigationEnd, NavigationStart, Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiLanguageService } from 'ng-jhipster';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { NGAY_HACH_TOAN, ROLE_ADMIN, SD_SO_QUAN_TRI, VERSION } from 'app/app.constants';
import { JhiLanguageHelper, Principal, LoginModalService, LoginService, UserService } from 'app/core';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { OrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { PasswordService } from 'app/account';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { Moment } from 'moment';
import { SystemOptionService } from 'app/he-thong/system-option';
import { DatePipe } from '@angular/common';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_FORMAT_TYPE1 } from 'app/shared';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

@Component({
    selector: 'eb-admin-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['navbar.css']
})
export class NavbarAdminComponent implements OnInit {
    inProduction: boolean;
    isNavbarCollapsed: boolean;
    languages: any[];
    swaggerEnabled: boolean;
    modalRef: NgbModalRef;
    version: string;
    currentAccount: any;
    data: any;
    org: TreeviewItem;
    book: string;
    isShow: boolean;
    ngbModalRef: any;
    ngbModalRef2: any;
    confirmPassword: string;
    currentPassword: string;
    newPassword: string;
    postedDate: Moment;
    isAdmin: boolean;
    isAdminLoginRoute: boolean;

    constructor(
        private loginService: LoginService,
        private languageService: JhiLanguageService,
        private languageHelper: JhiLanguageHelper,
        private principal: Principal,
        private loginModalService: LoginModalService,
        public router: Router,
        private eventManager: JhiEventManager,
        private modalService: NgbModal,
        private orgService: OrganizationUnitService,
        private userService: UserService,
        private passwordService: PasswordService,
        private translateService: TranslateService,
        private toastrService: ToastrService,
        private systemOptionService: SystemOptionService,
        private datepipe: DatePipe,
        public utilsService: UtilsService
    ) {
        this.version = VERSION ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
        /*this.router.events.subscribe((routerEvent: Event) => {
            if (routerEvent instanceof NavigationStart) {
                this.showLoading = true;
            }

            if (routerEvent instanceof NavigationEnd) {
                this.showLoading = false;
            }
        });*/
        this.isAdminLoginRoute = window.location.href.includes('/admin/login');
    }

    ngOnInit() {
        this.data = [];
        this.languageHelper.getAll().then(languages => {
            this.languages = languages;
        });

        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
            });
        });
    }

    changeLanguage(languageKey: string) {
        this.languageService.changeLanguage(languageKey);
    }

    collapseNavbar() {
        this.isNavbarCollapsed = true;
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    logout() {
        this.collapseNavbar();
        this.loginService.logout();
        this.router.navigate(['/admin/login']);
    }

    toggleNavbar() {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
    }

    getImageUrl() {
        return this.isAuthenticated() ? this.principal.getImageUrl() : null;
    }

    showPopup(popup) {
        this.principal.identity(true).then(account => {
            this.currentAccount = account;
            if (this.currentAccount) {
                this.isShow = this.currentAccount.systemOption.some(x => x.code === SD_SO_QUAN_TRI && x.data === '1');
            }
        });
        this.orgService.getOuTree().subscribe(res => {
            this.data = res.body.orgTrees;
            this.book = res.body.book;
            this.org = res.body.currentOrgLogin;
            this.ngbModalRef = this.modalService.open(popup, { size: 'lg', backdrop: 'static' });
        });
    }

    showChangePostedDate(popupChangePostedDate) {
        this.principal.identity(true).then(account => {
            this.currentAccount = account;
            if (this.currentAccount) {
                const data = this.currentAccount.systemOption.find(x => x.code === NGAY_HACH_TOAN && x.data);
                this.postedDate = this.utilsService.ngayHachToan(this.currentAccount);
                // this.postedDate = moment(ngayHachToan);
                // this.testPostedDate = '11/11/2019'
                this.ngbModalRef = this.modalService.open(popupChangePostedDate, { size: 'lg', backdrop: 'static' });
            }
        });
    }

    showPopupChangePassword(popup) {
        this.confirmPassword = this.currentPassword = this.newPassword = '';
        this.ngbModalRef2 = this.modalService.open(popup, { size: 'lg', backdrop: 'static' });
    }

    apply() {
        // update dữ liệu
        this.userService.updateSession({ currentBook: this.book, org: this.org.value }).subscribe(res => {
            const bearerToken = res.headers.get('Authorization');
            if (bearerToken && bearerToken.slice(0, 7) === 'Bearer ') {
                const jwt = bearerToken.slice(7, bearerToken.length);
                this.loginService.storeAuthenticationToken(jwt, false);
                this.loginService.storeAuthenticationToken(jwt, true);
            }
            this.eventManager.broadcast({
                name: 'changeSession',
                content: 'Change Authentication Session Success'
            });
            this.principal.identity(true).then(account => {
                this.currentAccount = account;
                if (this.currentAccount) {
                    this.isShow = this.currentAccount.systemOption.some(x => x.code === SD_SO_QUAN_TRI && x.data === '1');
                }
            });
            this.ngbModalRef.close();
        });
    }

    changePassword() {
        if (!this.newPassword) {
            this.toastrService.error(this.translateService.instant('navbar.password.error.newPassword'));
            return;
        }
        if (!this.confirmPassword) {
            this.toastrService.error(this.translateService.instant('navbar.password.error.newPassword'));
            return;
        }
        if (!this.currentPassword) {
            this.toastrService.error(this.translateService.instant('navbar.password.error.newPassword'));
            return;
        }
        if (this.newPassword !== this.confirmPassword) {
            this.toastrService.error(this.translateService.instant('navbar.password.error.doNotMatch'));
        } else {
            this.passwordService.save(this.newPassword, this.currentPassword).subscribe(
                () => {
                    this.toastrService.success(this.translateService.instant('navbar.password.success'));
                    this.ngbModalRef2.close();
                },
                error => {
                    if (error.error.type.includes('invalid-password')) {
                        this.toastrService.error(this.translateService.instant('navbar.password.error.invalidPassword'));
                    }
                }
            );
        }
    }

    updatePostedDate() {
        let data = '';
        const defaultData = this.datepipe.transform(new Date(), 'yyyy-MM-dd');
        if (!this.postedDate) {
            this.toastrService.error(
                this.translateService.instant('navbar.changePostedDate.postedDateIsNotNull'),
                this.translateService.instant('navbar.changePostedDate.message')
            );
            return;
        } else {
            data = this.datepipe.transform(this.postedDate, 'yyyy-MM-dd');
        }
        const obj = { data, defaultData };
        this.systemOptionService.updatePostedDate(JSON.stringify(obj)).subscribe(
            () => {
                this.toastrService.success(this.translateService.instant('navbar.changePostedDate.success'));
                this.ngbModalRef.close();
            },
            () => {}
        );
    }

    navigateUser() {
        this.router.navigate(['admin/user-management', this.currentAccount.login, 'edit']);
    }
}
