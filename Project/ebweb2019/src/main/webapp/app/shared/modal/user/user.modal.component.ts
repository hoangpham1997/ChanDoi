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
import { BaseComponent } from 'app/shared/base-component/base.component';

@Component({
    selector: 'eb-user-modal',
    templateUrl: './user.modal.component.html',
    styleUrls: ['./user.modal.component.css']
})
export class UserModalComponent extends BaseComponent implements OnInit {
    @ViewChild('contentForOne') public modalOneComponent: NgbModalRef;
    @ViewChild('contentForAll') public modalAllComponent: NgbModalRef;
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
    oldPassword: any;

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
        super();
        this.login = this.login ? this.login : null;
        this.user = this.user ? this.user : {};
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = [];
        this.newList = [];
        this.userService.authorities().subscribe(authorities => {
            this.authorities = authorities;
        });
        this.loadAll();
        this.languageHelper.getAll().then(languages => {
            this.languages = languages;
        });
    }

    loadAll() {
        this.userService.getEbGroups().subscribe(ebGroups => {
            this.ebGroups = ebGroups;
            if (this.login) {
                this.userService.find(this.login).subscribe((res: HttpResponse<IUser>) => {
                    this.user = res.body;
                    console.log('userMODAL');
                    console.log(this.user);
                    if (this.user.id) {
                        for (let i = 0; i < this.ebGroups.length; i++) {
                            for (let j = 0; j < this.user.ebGroups.length; j++) {
                                if (this.ebGroups[i].id === this.user.ebGroups[j].id) {
                                    this.ebGroups[i] = this.user.ebGroups[j];
                                    this.ebGroups[i].check = true;
                                    break;
                                } else {
                                    this.ebGroups[i].check = false;
                                }
                            }
                        }
                        this.user.confirmPassword = this.user.password;
                        this.oldPassword = this.user.password;
                    }
                });
            }
        });
    }

    previousState() {
        window.history.back();
    }

    isCheckAll() {
        this.checkedAll = this.ebGroups ? this.ebGroups.every(item => item.check) : false;
        return this.checkedAll;
    }

    checkAll() {
        this.checkedAll = this.ebGroups ? this.ebGroups.every(item => item.check) : false;
        if (this.checkedAll) {
            // bo check thi kiem tra ebgroup co listOrg
            if (this.ebGroups.some(item => item.listOrg.length > 0)) {
                this.modalRef = this.modalService.open(this.modalAllComponent, { backdrop: 'static' });
                this.checkedAll = false;
                setTimeout(() => {
                    this.checkedAll = true;
                }, 100);
            } else {
                this.ebGroups.forEach(item => {
                    item.check = false;
                });
                this.checkedAll = false;
            }
        } else {
            this.ebGroups.forEach(item => {
                if (!item.check) {
                    item.check = true;
                }
            });
            this.checkedAll = true;
        }
    }

    check(ebGroup: IEbGroup, event: any) {
        // neu co quyen trong list
        if (ebGroup.check && ebGroup.listOrg.length > 0) {
            this.selectedGroup = ebGroup;
            this.modalRef = this.modalService.open(this.modalOneComponent, { backdrop: 'static' });
        } else if (ebGroup.check && ebGroup.listOrg.length === 0) {
            ebGroup.check = false;
        } else {
            ebGroup.check = true;
        }
        this.checkedAll = this.isCheckAll();
        event.stopPropagation();
    }

    save(isNew = false) {
        event.preventDefault();
        this.isSaving = true;
        this.user.langKey = 'englis';
        // this.fillToSave();
        if (this.checkError()) {
            if (this.user.id !== undefined) {
                if (this.oldPassword !== this.user.password) {
                    this.user.isChangePassword = true;
                } else {
                    this.user.isChangePassword = false;
                }
                this.userService.update(this.user).subscribe(
                    (response: any) => {
                        if (response.body === true) {
                            this.toastr.success(this.translate.instant('userManagement.success.updated'));
                            this.router.navigate(['user-management/new']).then(() => {
                                this.router.navigate(['user-management']);
                            });
                            this.closeForm();
                        } else {
                            this.toastr.error(this.translate.instant('userManagement.error.updateFailed'));
                        }
                    },
                    (res: any) => this.onSaveError(res)
                );
            } else {
                this.userService.create(this.user).subscribe(
                    (response: any) => {
                        if (response.body.status === 0) {
                            this.toastr.success(this.translate.instant('userManagement.success.created'));
                            this.router.navigate(['user-management/new']).then(() => {
                                this.router.navigate(['user-management']);
                            });
                            this.closeForm();
                        } else {
                            this.toastr.error(this.translate.instant('userManagement.error.loginExist'));
                        }
                    },
                    (res: any) => this.onSaveError(res)
                );
            }
        }
    }

    fillToSave() {
        const result = [];
        this.ebGroups.forEach(item => {
            if (item.check) {
                result.push(item);
            }
        });
        this.user.ebGroups = result;
        // console.log('ebGroups Modal save: ');
        // console.log(result);
    }

    checkError(): boolean {
        //const regExp = /^[a-zA-Z0-9_.]*$/;
        if (!this.user.password) {
            this.toastr.error(this.translate.instant('userManagement.error.passRequired'));
            return false;
        } else if (!this.user.login) {
            this.toastr.error(this.translate.instant('userManagement.error.emailLoginRequired'));
            return false;
        } else if (!this.user.login.includes('@')) {
            this.toastr.error(this.translate.instant('userManagement.error.emailLoginInvalid'));
            return false;
        } else if (!this.user.fullName) {
            this.toastr.error(this.translate.instant('userManagement.error.fullNameRequired'));
            return false;
        } else if (!this.user.confirmPassword) {
            this.toastr.error(this.translate.instant('userManagement.error.confirmPassRequired'));
            return false;
        } else if (!this.confirmPassword()) {
            return false;
        } else if (this.user.password.length < 4) {
            this.toastr.error(this.translate.instant('userManagement.error.passwordLength'));
            return false;
        }
        return true;
    }

    private onSaveSuccess(result) {
        this.isSaving = false;
        this.closeForm();
        this.router.navigate(['permission-user']).then(() => {
            this.router.navigate(['user-management']);
        });
        // this.previousState();
    }

    private onSaveError(err) {
        this.isSaving = false;
        if (err && err.error) {
            this.toastr.error(this.translate.instant(`ebwebApp.ebPackage.${err.error.message}`));
        }
    }

    closeForm() {
        this.activeModal.dismiss(false);
        this.eventManager.broadcast({
            name: 'closePopupInfo',
            content: false
        });
    }

    confirmPassword(): boolean {
        if (this.user.password && this.user.confirmPassword && this.user.password !== this.user.confirmPassword) {
            this.toastr.error(this.translate.instant('userManagement.error.passworddonotmatch'));
            return false;
        } else {
            return true;
        }
    }

    action(value: boolean) {
        this.selectedGroup.check = value;
        // neu dong y Hủy => remove quyền của user
        if (!value) {
            this.ebGroups.find(x => x.id === this.selectedGroup.id).listOrg = [];
        }
        this.modalRef.close();
    }

    actionAll(value: boolean) {
        // neu dong y Hủy => remove quyền của user
        if (!value) {
            this.ebGroups.forEach(item => {
                item.check = false;
                item.listOrg = [];
            });
            setTimeout(() => {
                this.checkedAll = false;
            }, 101);
        }
        this.modalRef.close();
    }
}
