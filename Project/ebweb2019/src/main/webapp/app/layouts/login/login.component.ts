import { Component, ElementRef, HostListener, OnInit, Renderer } from '@angular/core';
import { Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
import { LoginService, StateStorageService, UserService } from 'app/core';
import { OrganizationUnit } from 'app/shared/model/organization-unit.model';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CheckPasswordModalService } from 'app/shared/modal/checkPassword/checkPassword-modal.service';
import { Subscription } from 'rxjs';

@Component({
    selector: 'eb-error',
    templateUrl: './login.component.html',
    styleUrls: ['./login.css']
})
export class LoginComponent implements OnInit {
    authenticationError: boolean;
    password: string;
    rememberMe: boolean;
    username: string;
    credentials: any;
    ouParent: OrganizationUnit[];
    ouChildren: OrganizationUnit[];
    ou: any;
    ouChild: OrganizationUnit;
    signedIn: boolean;
    companyError: boolean;
    data: any;
    org: any;
    book: string;
    state: string;
    hide: boolean;
    hide2: boolean;
    isActivePackage: Boolean;
    modalRef: NgbModalRef;
    isDisable: Boolean;
    eventSubscriber: Subscription;
    expired: Boolean;

    constructor(
        private eventManager: JhiEventManager,
        private loginService: LoginService,
        private stateStorageService: StateStorageService,
        private userService: UserService,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private router: Router,
        private checkPasswordModalService: CheckPasswordModalService
    ) {
        this.credentials = {};
    }

    ngOnInit(): void {
        this.signedIn = false;
        this.isDisable = false;
        this.expired = false;
        sessionStorage.setItem('isChangePassword', JSON.stringify(false));
        this.sendMail();
    }

    @HostListener('document:keydown.enter')
    login() {
        if (!this.username || !this.password || (this.signedIn && !this.org)) {
            return;
        }
        if (this.signedIn) {
            this.loginService
                .login({
                    username: this.username,
                    password: this.password,
                    rememberMe: this.rememberMe,
                    org: this.org.parent.id,
                    orgGetData: this.org.parent.parentID
                })
                .then(() => {
                    this.router.navigate(['']);

                    setTimeout(() => {
                        this.eventManager.broadcast({
                            name: 'authenticationSuccess',
                            content: 'Sending Authentication Success'
                        });
                    }, 200);

                    this.companyError = false;
                })
                .catch(() => {
                    this.companyError = true;
                });
        } else {
            this.companyError = false;
            const isExpired = sessionStorage.getItem('isExpired');
            const loginReset = sessionStorage.getItem('loginReset');
            this.org = {};
            this.data = [];
            if (isExpired !== undefined && loginReset !== undefined && isExpired === 'true' && loginReset === this.username) {
                this.authenticationError = true;
            } else {
                this.companyError = false;
                this.loginService
                    .preLogin({
                        username: this.username,
                        password: this.password,
                        rememberMe: this.rememberMe
                    })
                    .subscribe(
                        (res: any) => {
                            this.data = res.body.orgTrees;
                            if (this.username === loginReset) {
                                sessionStorage.setItem('isLogin', JSON.stringify(true));
                            }
                            this.signedIn = true;
                            this.authenticationError = false;
                            if (this.data && this.data.length === 1 && (!this.data[0].children || !this.data[0].children.length)) {
                                this.org = this.data[0];
                            }
                            setTimeout(() => (this.hide = true), 300);
                            setTimeout(() => (this.hide2 = true), 400);
                        },
                        () => {
                            this.authenticationError = true;
                        }
                    );
            }
        }
    }

    reset() {
        this.signedIn = false;
        this.username = '';
        this.password = '';
        this.ouParent = [];
        this.ouChildren = null;
        this.ou = null;
        this.ouChild = null;
        this.authenticationError = false;
        this.hide = false;
        this.hide2 = false;
    }

    requestResetPassword() {
        event.preventDefault();
        if (
            sessionStorage.getItem('isDisable') === undefined ||
            sessionStorage.getItem('isDisable') === null ||
            sessionStorage.getItem('isDisable') === 'false'
        ) {
            this.modalRef = this.checkPasswordModalService.open(null);
        }
    }

    sendMail() {
        this.eventSubscriber = this.eventManager.subscribe('sendPasswordMail', res => {
            if (sessionStorage.getItem('isDisable') === 'true') {
                setTimeout(() => {
                    sessionStorage.setItem('isDisable', JSON.stringify(false));
                    if (sessionStorage.getItem('isChangePassword') === 'false') {
                        this.checkChangePassword();
                    }
                }, 300000);
            }
        });
    }

    checkChangePassword() {
        if (sessionStorage.getItem('isLogin') === 'false') {
            sessionStorage.setItem('isExpired', JSON.stringify(true));
            this.userService.resetPassword({ login: sessionStorage.getItem('loginReset') }).subscribe(res => {
                sessionStorage.setItem('isChangePassword', JSON.stringify(true));
            });
        }
    }

    checkDisable() {
        return sessionStorage.getItem('isDisable') === 'true';
    }
}
