import { Component, ElementRef, HostListener, OnInit, Renderer } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
import { LoginService, StateStorageService } from 'app/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { OrganizationUnit } from 'app/shared/model/organization-unit.model';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { map } from 'rxjs/operators';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';

@Component({
    selector: 'eb-admin-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.css']
})
export class AdminLoginComponent implements OnInit {
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
    org: TreeviewItem;
    book: string;
    state: string;
    hide: boolean;
    hide2: boolean;

    constructor(
        private eventManager: JhiEventManager,
        private loginService: LoginService,
        private stateStorageService: StateStorageService,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private router: Router,
        private organizationUnitService: OrganizationUnitService
    ) {
        this.credentials = {};
    }

    ngOnInit(): void {
        this.signedIn = false;
    }

    @HostListener('document:keydown.enter')
    login() {
        if (!this.username || !this.password) {
            return;
        }
        this.loginService
            .login({
                username: this.username,
                password: this.password,
                rememberMe: this.rememberMe,
                admin: true
            })
            .then(() => {
                this.router.navigate(['/admin/home']);

                this.eventManager.broadcast({
                    name: 'authenticationSuccessAdmin',
                    content: 'Sending Authentication Success Admin'
                });
                this.authenticationError = false;
            })
            .catch(() => {
                this.authenticationError = true;
            });
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
    }

    requestResetPassword() {}
}
