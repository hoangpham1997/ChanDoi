import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { JhiLanguageHelper, Principal, User, UserService } from 'app/core';
import { SD_SO_QUAN_TRI, SO_LAM_VIEC, TCKHAC_SDSoQuanTri } from 'app/app.constants';
import { el } from '@angular/platform-browser/testing/src/browser_util';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'eb-group-update',
    templateUrl: './eb-group-update.component.html',
    styleUrls: ['./eb-group-update.component.css']
})
export class EbGroupUpdateComponent implements OnInit {
    user: User;
    languages: any[];
    authorities: any[];
    isSaving: boolean;
    currentAccount: any;
    isShow: boolean;
    workOnBook: number;
    dataTCKHAC_SDSoQuanTri: number;
    dataSO_LAM_VIEC: number;

    constructor(
        private languageHelper: JhiLanguageHelper,
        private userService: UserService,
        private route: ActivatedRoute,
        private router: Router,
        private principal: Principal
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.route.data.subscribe(({ user }) => {
            this.user = user.body ? user.body : user;
            this.principal.identity().then(account => {
                this.currentAccount = account;
                if (this.currentAccount) {
                    this.dataTCKHAC_SDSoQuanTri = +this.currentAccount.systemOption.find(x => x.code === TCKHAC_SDSoQuanTri).data;
                    this.dataSO_LAM_VIEC = +this.currentAccount.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                    this.workOnBook = this.dataTCKHAC_SDSoQuanTri === 0 ? 0 : this.dataSO_LAM_VIEC;
                    this.user.workOnBook = this.user.workOnBook ? this.user.workOnBook : this.workOnBook;
                }
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
        this.user.langKey = 'englis';
        if (this.user.id !== null) {
            this.userService.update(this.user).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        } else {
            this.userService.create(this.user).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        }
    }

    private onSaveSuccess(result) {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
