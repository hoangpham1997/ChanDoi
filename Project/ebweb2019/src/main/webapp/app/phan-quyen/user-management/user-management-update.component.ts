import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { JhiLanguageHelper, Principal, User, UserService } from 'app/core';
import { SD_SO_QUAN_TRI, SO_LAM_VIEC, TCKHAC_SDSoQuanTri } from 'app/app.constants';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { EbPackageService } from 'app/admin';
import { HttpResponse } from '@angular/common/http';
import { EbPackage } from 'app/app.constants';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';

@Component({
    selector: 'eb-user-mgmt-update',
    templateUrl: './user-management-update.component.html',
    styleUrls: ['./user-management-update.component.css']
})
export class UserMgmtUpdateComponent implements OnInit {
    user: User;
    languages: any[];
    authorities: any[];
    isSaving: boolean;
    currentAccount: any;
    isShow: boolean;
    workOnBook: number;
    dataTCKHAC_SDSoQuanTri: number;
    dataSO_LAM_VIEC: number;
    isCreateUser: number;

    constructor(
        private languageHelper: JhiLanguageHelper,
        private userService: UserService,
        private route: ActivatedRoute,
        private router: Router,
        private principal: Principal,
        private toastr: ToastrService,
        private translate: TranslateService,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.route.data.subscribe(({ user }) => {
            this.user = user.body ? user.body : user;
            this.principal.identity().then(account => {
                this.currentAccount = account;
            });
        });
        this.authorities = [];
        this.userService.authorities().subscribe(authorities => {
            this.authorities = authorities;
        });
        this.languageHelper.getAll().then(languages => {
            this.languages = languages;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.user.langKey = 'vi';
        if (this.checkError()) {
            if (this.user.id !== null) {
                this.userService.updateInfo(this.user).subscribe(
                    response => {
                        this.toastr.success(this.translate.instant('userManagement.success.updateInfo'));
                        this.onSaveSuccess(response);
                        const nameUser = response.body.fullName;
                        this.eventManager.broadcast({
                            name: 'changeUserInfo',
                            content: nameUser
                        });
                    },
                    (res: any) => this.onSaveError()
                );
            } else {
                // this.userService.create(this.user).subscribe(
                //     response => {
                //         this.onSaveSuccess(response);
                //     },
                //     (res: any) => this.onSaveError()
                // );
            }
        }
    }

    checkError(): boolean {
        if (!this.user.fullName) {
            this.toastr.error(this.translate.instant('userManagement.error.fullNameRequired'));
            return false;
        } else if (!this.user.email) {
            this.toastr.error(this.translate.instant('userManagement.error.emailRequired'));
            return false;
        } else if (!this.user.email.includes('@')) {
            this.toastr.error(this.translate.instant('userManagement.error.emailInvalid'));
            return false;
        }
        return true;
    }

    private onSaveSuccess(result) {
        this.isSaving = false;
        // this.previousState();
        this.user = result.body;
    }

    private onSaveError() {
        this.isSaving = false;
    }

    getWorkingOnBookFromEbGroup(): number {
        return 2;
    }
}
