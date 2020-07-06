import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { IUser, JhiLanguageHelper, Principal, User, UserService } from 'app/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { EbGroup, IEbGroup } from 'app/core/eb-group/eb-group.model';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'eb-user-modal',
    templateUrl: './checkPassword.modal.component.html',
    styleUrls: ['./checkPassword.modal.component.css']
})
export class CheckPasswordModalComponent implements OnInit {
    @ViewChild('success') success;
    @ViewChild('fail') fail;
    @Input() public login: string;
    languages: any[];
    authorities: any[];
    isSaving: boolean;
    ebGroups: IEbGroup[];
    newList: any[];
    modalRef: NgbModalRef;
    selectedGroup: EbGroup;
    eventSubscriber: Subscription;
    checkedAll: boolean;
    user: IUser;
    userResponse: IUser;
    isSendSuccess: Boolean;

    constructor(
        private languageHelper: JhiLanguageHelper,
        private userService: UserService,
        private route: ActivatedRoute,
        private router: Router,
        private principal: Principal,
        public activeModal: NgbActiveModal,
        private toastr: ToastrService,
        private translate: TranslateService,
        public utilsService: UtilsService,
        private modalService: NgbModal,
        private eventManager: JhiEventManager
    ) {
        this.login = this.login ? this.login : null;
        this.user = this.user ? this.user : {};
    }

    ngOnInit() {
        this.isSaving = false;
        this.isSendSuccess = false;
    }

    save() {
        this.isSaving = true;
        this.user.langKey = 'englis';
        // this.fillToSave();
        if (this.checkError()) {
            this.userService.sendMailResetPassword({ email: this.user.login }).subscribe(res => {
                this.userResponse = res.body;
                if (this.userResponse === undefined || this.userResponse === null) {
                    this.toastr.error(
                        this.translate.instant('userManagement.error.resetPasswordFail'),
                        this.translate.instant('ebwebApp.ebPackage.error.error')
                    );
                } else {
                    this.toastr.success(
                        this.translate.instant('userManagement.success.resetPasswordSuccess'),
                        this.translate.instant('ebwebApp.ebPackage.success.success')
                    );
                    this.isSendSuccess = true;
                    sessionStorage.setItem('isExpired', JSON.stringify(false));
                    sessionStorage.setItem('loginReset', this.userResponse.login);
                    sessionStorage.setItem('isLogin', JSON.stringify(false));
                    sessionStorage.setItem('isDisable', JSON.stringify(true));
                    this.close();
                }
            });
        }
    }

    checkError(): boolean {
        if (!this.user.login) {
            this.toastr.error(
                this.translate.instant('userManagement.error.emailRequired'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        }
        if (this.user.login && !this.user.login.includes('@')) {
            this.toastr.error(
                this.translate.instant('userManagement.error.emailInvalid'),
                this.translate.instant('ebwebApp.ebPackage.error.error')
            );
            return false;
        }
        return true;
    }

    close() {
        if (sessionStorage.getItem('isExpired') === undefined || sessionStorage.getItem('isExpired') === null) {
            sessionStorage.setItem('isExpired', JSON.stringify(true));
            sessionStorage.setItem('isLogin', JSON.stringify(true));
            sessionStorage.setItem('isDisable', JSON.stringify(false));
        }
        this.eventManager.broadcast({
            name: 'sendPasswordMail',
            content: null
        });
        this.activeModal.dismiss(false);
    }
}
