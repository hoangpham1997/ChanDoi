import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LoginModalService, Principal, Account } from 'app/core';
import { Router } from '@angular/router';

@Component({
    selector: 'eb-admin-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.css']
})
export class HomeAdminComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        public router: Router
    ) {}

    ngOnInit() {
        this.principal.identity().then(account => {
            if (account) {
                this.account = account;
            } else {
                if (this.router.url.includes('/admin/login')) {
                    this.router.navigate(['/admin/login']);
                } else {
                    this.router.navigate(['/login']);
                }
            }
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccessAdmin', message => {
            this.principal.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }
}
