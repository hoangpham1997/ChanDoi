import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiLanguageService } from 'ng-jhipster';

import { CheckHDCD, SO_LAM_VIEC, TCKHAC_SDTichHopHDDT, VERSION } from 'app/app.constants';
import { JhiLanguageHelper, LoginModalService, LoginService, Principal } from 'app/core';
import { ConnnectEInvoiceComponent } from 'app/hoa-don-dien-tu/ket_noi_hoa_don_dien_tu/ket_noi_hoa_don_dien_tu';
import { Subscription } from 'rxjs';
import { CalculateOWRepositoryComponent } from 'app/kho/tinh_gia_xuat_kho/tinh-gia-xuat-kho.component';

@Component({
    selector: 'eb-admin-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['sidebar.css']
})
export class SidebarAdminComponent implements OnInit {
    inProduction: boolean;
    isNavbarCollapsed: boolean;
    languages: any[];
    swaggerEnabled: boolean;
    modalRef: NgbModalRef;
    version: string;
    isHover: boolean;
    isInside: boolean;
    isTichHop: boolean; // Add by Hautv
    useInvoiceWait: boolean; // Add by Hautv
    account: any;
    eventSubscriber: Subscription; // Add by Hautv

    constructor(
        private loginService: LoginService,
        private languageService: JhiLanguageService,
        private languageHelper: JhiLanguageHelper,
        private principal: Principal,
        private loginModalService: LoginModalService,
        private router: Router,
        private modalService: NgbModal,
        private eventManager: JhiEventManager
    ) {
        this.version = VERSION ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
    }

    ngOnInit() {
        this.languageHelper.getAll().then(languages => {
            this.languages = languages;
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
        this.router.navigate(['']);
    }

    test() {
        console.log('CLICKED!!!');
        // alert('CLICKED!!!');
    }
}
