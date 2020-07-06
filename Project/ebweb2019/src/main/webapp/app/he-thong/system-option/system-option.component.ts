import { Component, OnInit, OnDestroy, ViewChild, TemplateRef } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ISystemOption } from 'app/shared/model/system-option.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { SystemOptionService } from './system-option.service';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IGenCode } from 'app/shared/model/gen-code.model';
import { GenCodeService } from 'app/entities/gen-code';
import { IAccountDefault } from 'app/shared/model/account-default.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { ROLE } from 'app/role.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { IPrivateToGeneralUse } from 'app/shared/model/PrivateToGeneralUse';

@Component({
    selector: 'eb-system-option',
    templateUrl: './system-option.component.html',
    styleUrls: ['./system-option.component.css']
})
export class SystemOptionComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('content') content: TemplateRef<any>;
    @ViewChild('popupPrivateToGeneralUse') popupPrivateToGeneralUse: TemplateRef<any>;
    @ViewChild('popupQuestionShowError') popupQuestionShowError: TemplateRef<any>;
    @ViewChild('popupPPTinhGiaXuatKho') popUpPPTGXK: TemplateRef<any>;
    currentAccount: any;
    systemOptions: ISystemOption[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    accountLists: IAccountList[];
    privateToGeneralUses: IPrivateToGeneralUse[];
    color: any;
    color2: any;
    checkModalRef: NgbModalRef;
    selectedColor: string;
    nameColor: string;
    genCodes: IGenCode[];
    systemOptionsCopy: ISystemOption[];
    systemOptionsBackUp: ISystemOption[];
    genCodesCopy: ISystemOption[];
    modalRef: NgbModalRef;
    ROLE_TuyChon_Sua = ROLE.TuyChon_Sua;
    ROLE_TuyChon_Xem = ROLE.TuyChon_Xem;
    arrAuthorities: any[];
    isRoleSua: boolean;
    isRoleThem: boolean;
    isCreateUrl: boolean;
    isEditUrl: boolean;

    buttonDeleteTranslate = 'ebwebApp.mBDeposit.toolTip.delete';
    buttonAddTranslate = 'ebwebApp.mBDeposit.toolTip.add';
    buttonEditTranslate = 'ebwebApp.mBDeposit.toolTip.edit';
    buttonSaveTranslate = 'ebwebApp.mBDeposit.toolTip.save';
    buttonRecordTranslate = 'ebwebApp.mBDeposit.toolTip.record';
    buttonUnRecordTranslate = 'ebwebApp.mBDeposit.toolTip.unrecord';
    buttonPrintTranslate = 'ebwebApp.mBDeposit.toolTip.print';
    buttonSaveAndNewTranslate = 'ebwebApp.mBDeposit.toolTip.saveAndNew';
    buttonCopyAndNewTranslate = 'ebwebApp.mBDeposit.toolTip.copyAndNew';
    buttonCloseFormTranslate = 'ebwebApp.mBDeposit.toolTip.closeForm';

    constructor(
        private systemOptionService: SystemOptionService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        public utilsService: UtilsService,
        private accountListService: AccountListService,
        private modalService: NgbModal,
        private genCodeService: GenCodeService,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        this.systemOptionService.getSystemOptions().subscribe((res: HttpResponse<ISystemOption[]>) => {
            this.systemOptions = res.body;
            this.systemOptionsBackUp = this.systemOptions.map(object => ({ ...object }));
            for (let i = 0; i < this.systemOptions.length; i++) {
                if (!this.systemOptions[i].data) {
                    this.systemOptions[i].data = this.systemOptions[i].defaultData;
                }
            }
            this.color = this.systemOptions[23].data;
            this.color2 = this.systemOptions[24].data;
            this.copy();
        });
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/system-option'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/system-option',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.privateToGeneralUses = [];
        this.genCodesCopy = [];
        this.systemOptionsCopy = [];
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.arrAuthorities = account.authorities;
            this.isRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_TuyChon_Sua) : true;
        });
        this.accountListService.findAllForSystemOptions().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.accountLists = res.body;
        });
        this.genCodeService.getGenCodes().subscribe((res: HttpResponse<IGenCode[]>) => {
            this.genCodes = res.body;
            for (let i = 0; i < this.genCodes.length; i++) {
                this.changeNumberDisplay(i);
            }
            this.copy();
        });
        this.registerChangeInSystemOptions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISystemOption) {
        return item.id;
    }

    registerChangeInSystemOptions() {
        this.eventSubscriber = this.eventManager.subscribe('systemOptionListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateSystemOptions(data: ISystemOption[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.systemOptions = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    openModelDelete(content, nameColor) {
        this.nameColor = nameColor;
        this.checkModalRef = this.modalService.open(content, { backdrop: 'static' });
    }

    selectColor() {
        if (this.nameColor === 'color1') {
            this.color = this.selectedColor;
        } else {
            this.color2 = this.selectedColor;
        }
    }

    changeCheckBox(i, $event) {
        this.systemOptions[i].data = $event.target.checked ? '1' : '0';
        console.log(this.systemOptions[i].data);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TuyChon_Sua])
    save(isNew = false) {
        event.preventDefault();
        this.systemOptions[23].data = this.color;
        this.systemOptions[24].data = this.color2;
        if (this.systemOptions[0].data !== this.systemOptionsBackUp[0].data) {
            this.modalRef = this.modalService.open(this.popUpPPTGXK, { backdrop: 'static' });
            return;
        }
        const obj = { systemOptions: this.systemOptions, genCodes: this.genCodes };
        for (let i = 0; i < this.systemOptions.length; i++) {
            if (this.systemOptions[i].data === null || this.systemOptions[i].data === undefined) {
                this.systemOptions[i].data = this.systemOptionsBackUp[i].data;
            }
        }
        if (this.systemOptions[21].data === this.systemOptions[22].data) {
            this.toastr.error(
                this.translate.instant('ebwebApp.systemOption.errorCommaSeparated'),
                this.translate.instant('ebwebApp.systemOption.error')
            );
            return;
        }
        this.systemOptionService.save(obj).subscribe(
            (res: HttpResponse<any>) => {
                if (res.body && res.body.length > 0) {
                    this.privateToGeneralUses = res.body;
                    this.modalRef = this.modalService.open(this.popupQuestionShowError, {
                        size: 'lg',
                        backdrop: 'static'
                    });
                } else {
                    this.toastr.success(
                        this.translate.instant('ebwebApp.systemOption.saveSuccess'),
                        this.translate.instant('ebwebApp.systemOption.message')
                    );
                    this.principal.identity(true).then(account => {
                        this.currentAccount = account;
                    });
                    this.loadAll();
                }
            },
            (res: HttpErrorResponse) => {
                if (res.error && res.error.errorKey === 'errorGeneralToPrivateUse') {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.systemOption.errorGeneralToPrivateUse'),
                        this.translate.instant('ebwebApp.systemOption.message')
                    );
                } else {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.systemOption.error'),
                        this.translate.instant('ebwebApp.systemOption.message')
                    );
                }
            }
        );
    }

    changePhuongPhapTinhGiaXuatKho() {
        if (this.systemOptions[0].data === '0') {
            this.systemOptions[1].data = this.systemOptionsBackUp[1].data;
            // this.systemOptions[2].data = '1';
        } else if (this.systemOptions[0].data === '1' || this.systemOptions[0].data === '2') {
            this.systemOptions[2].data = this.systemOptionsBackUp[2].data;
            // this.systemOptions[1].data = '1';
        } else {
            this.systemOptions[2].data = this.systemOptionsBackUp[2].data;
            this.systemOptions[1].data = this.systemOptionsBackUp[1].data;
        }
    }

    resetFormatNumber() {
        this.systemOptions[11].data = this.systemOptions[11].defaultData;
        this.systemOptions[12].data = this.systemOptions[12].defaultData;
        this.systemOptions[15].data = this.systemOptions[15].defaultData;
        this.systemOptions[13].data = this.systemOptions[13].defaultData;
        this.systemOptions[14].data = this.systemOptions[14].defaultData;
        this.systemOptions[16].data = this.systemOptions[16].defaultData;
        this.systemOptions[17].data = this.systemOptions[17].defaultData;
        this.systemOptions[18].data = this.systemOptions[18].defaultData;
    }

    changeNumberDisplay(i) {
        if (this.genCodes[i].length) {
            if (this.genCodes[i].currentValue !== null && this.genCodes[i].currentValue !== undefined) {
                const countString = this.genCodes[i].currentValue.toString().length;
                if (countString >= this.genCodes[i].length) {
                    this.genCodes[i].numberDisplay =
                        this.genCodes[i].prefix + this.genCodes[i].currentValue + (this.genCodes[i].suffix ? this.genCodes[i].suffix : '');
                } else {
                    if (this.genCodes[i].prefix) {
                        this.genCodes[i].numberDisplay = this.genCodes[i].prefix;
                    } else {
                        this.genCodes[i].numberDisplay = '';
                    }
                    const add = this.genCodes[i].length - countString;
                    for (let j = 0; j < add; j++) {
                        this.genCodes[i].numberDisplay += '0';
                    }
                    if (this.genCodes[i].suffix) {
                        this.genCodes[i].numberDisplay += this.genCodes[i].currentValue + this.genCodes[i].suffix;
                    } else {
                        this.genCodes[i].numberDisplay += this.genCodes[i].currentValue + '';
                    }
                }
            }
        }
    }

    copy() {
        this.genCodesCopy = this.genCodes ? this.genCodes.map(object => ({ ...object })) : [];
        this.systemOptionsCopy = this.systemOptions ? this.systemOptions.map(object => ({ ...object })) : [];
    }

    canDeactive(): boolean {
        return (
            this.utilsService.isEquivalentArray(this.genCodes, this.genCodesCopy) &&
            this.utilsService.isEquivalentArray(this.systemOptions, this.systemOptionsCopy)
        );
    }

    closeForm() {
        console.log(
            !this.utilsService.isEquivalentArray(this.genCodes, this.genCodesCopy),
            !this.utilsService.isEquivalentArray(this.systemOptions, this.systemOptionsCopy)
        );
        if (
            !this.utilsService.isEquivalentArray(this.genCodes, this.genCodesCopy) ||
            !this.utilsService.isEquivalentArray(this.systemOptions, this.systemOptionsCopy)
        ) {
            this.modalRef = this.modalService.open(this.content, { backdrop: 'static' });
            return;
        } else {
            this.closeAll();
            return;
        }
    }

    closeAll() {
        window.history.back();
    }

    saveContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.save();
    }

    close() {
        this.copy();
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.closeAll();
    }

    exit() {
        if (this.modalRef) {
            this.modalRef.close();
            return;
        }
    }

    continueSave() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        const obj = { systemOptions: this.systemOptions, genCodes: this.genCodes };
        for (let i = 0; i < this.systemOptions.length; i++) {
            if (this.systemOptions[i].data === null || this.systemOptions[i].data === undefined) {
                this.systemOptions[i].data = this.systemOptionsBackUp[i].data;
            }
        }
        if (this.systemOptions[21].data === this.systemOptions[22].data) {
            this.toastr.error(
                this.translate.instant('ebwebApp.systemOption.errorCommaSeparated'),
                this.translate.instant('ebwebApp.systemOption.error')
            );
            return;
        }
        this.systemOptionService.save(obj).subscribe(
            (res: HttpResponse<any>) => {
                if (res.body && res.body.length > 0) {
                    this.privateToGeneralUses = res.body;
                    this.modalRef = this.modalService.open(this.popupQuestionShowError, {
                        size: 'lg',
                        backdrop: 'static'
                    });
                } else {
                    this.toastr.success(
                        this.translate.instant('ebwebApp.systemOption.saveSuccess'),
                        this.translate.instant('ebwebApp.systemOption.message')
                    );
                    this.principal.identity(true).then(account => {
                        this.currentAccount = account;
                    });
                    this.loadAll();
                }
            },
            (res: HttpErrorResponse) => {
                if (res.error && res.error.errorKey === 'errorGeneralToPrivateUse') {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.systemOption.errorGeneralToPrivateUse'),
                        this.translate.instant('ebwebApp.systemOption.message')
                    );
                } else {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.systemOption.error'),
                        this.translate.instant('ebwebApp.systemOption.message')
                    );
                }
            }
        );
    }

    continueShowPopup() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.modalRef = this.modalService.open(this.popupPrivateToGeneralUse, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
    }

    checkInputValue(index, max, translate) {
        const value = parseInt(this.systemOptions[index].data);
        if (value > max) {
            this.systemOptions[index].data = 0;
            this.toastr.warning(
                this.translate.instant('ebwebApp.systemOption.h3.dinhDangSauDauPhay') +
                    ' ' +
                    this.translate.instant(translate) +
                    ' ' +
                    this.translate.instant('ebwebApp.systemOption.doNotEnterMoreThan') +
                    max +
                    '!'
            );
        }
    }
}
