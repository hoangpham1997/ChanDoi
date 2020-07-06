import { IStatisticsCode, StatisticsCode } from 'app/shared/model/statistics-code.model';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { StatisticsCodeService } from 'app/danhmuc/statistics-code/statistics-code.service';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-statistics-code-update',
    templateUrl: './statistics-code-update.component.html',
    styleUrls: ['./statistics-code-update.component.css']
})
export class StatisticsCodeUpdateComponent extends BaseComponent implements OnInit, AfterViewInit {
    statisticsCode: IStatisticsCode;
    statisticsCodeCopy: IStatisticsCode;
    account: any;
    statisticsCodes: IStatisticsCode[];
    cbxStatisticsCodes: IStatisticsCode[];
    isSaveAndCreate: boolean;
    isClose: boolean;
    modalRef: NgbModalRef;
    @ViewChild('content') content: any;
    ROLE_MTK_Them = ROLE.DanhMucMaThongKe_Them;
    ROLE_MTK_Sua = ROLE.DanhMucMaThongKe_Sua;
    ROLE_MTK_Xoa = ROLE.DanhMucMaThongKe_Xoa;
    isEditUrl: boolean;
    isRoleSua: boolean;
    isRoleThem: boolean;
    isCreateUrl: boolean;
    arrAuthorities: any[];

    constructor(
        private statisticsCodeService: StatisticsCodeService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private toastr: ToastrService,
        private principal: Principal,
        private translate: TranslateService,
        public utilsService: UtilsService,
        private modalService: NgbModal
    ) {
        super();
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.account = account;
                this.isEditUrl = window.location.href.includes('edit');
                this.isCreateUrl = window.location.href.includes('/new');
                this.arrAuthorities = account.authorities;
                this.isRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN')
                    ? this.arrAuthorities.includes(ROLE.DanhMucDieuKhoanThanhToan_Sua)
                    : true;
                this.isRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN')
                    ? this.arrAuthorities.includes(ROLE.DanhMucDieuKhoanThanhToan_Them)
                    : true;
            });
        });
    }

    ngOnInit() {
        console.log('NgOnInit');
        this.isClose = false;
        this.isSaveAndCreate = false;
        this.activatedRoute.data.subscribe(({ statisticsCode }) => {
            this.statisticsCode = statisticsCode;
            if (this.statisticsCode.id === undefined || this.statisticsCode.id === null) {
                this.statisticsCodeService.getActiveStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
                    this.cbxStatisticsCodes = res.body;
                });
            } else {
                this.statisticsCodeService
                    .getCbxStatisticsCodes(this.statisticsCode.id)
                    .subscribe((res: HttpResponse<IStatisticsCode[]>) => {
                        this.cbxStatisticsCodes = res.body;
                    });
            }
        });
        this.copy();
    }

    previousState() {
        this.router.navigate(['statistics-code']);
    }

    reload() {
        this.ngOnInit();
        this.statisticsCode = new StatisticsCode();
        this.copy();
    }

    checkEmpty() {
        if (this.statisticsCode.statisticsCode === undefined || this.statisticsCode.statisticsCode.length === 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.statisticsCode.statisticsCodeNotNull'),
                this.translate.instant('ebwebApp.statisticsCode.error')
            );
            return true;
        } else if (this.statisticsCode.statisticsCodeName === undefined || this.statisticsCode.statisticsCodeName.length === 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.statisticsCode.statisticsCodeNameNotNull'),
                this.translate.instant('ebwebApp.statisticsCode.error')
            );
            return true;
        }
        return false;
    }

    private onSaveSuccess() {
        this.copy();
        this.toastr.success(
            this.translate.instant('ebwebApp.statisticsCode.saveSuccessfully'),
            this.translate.instant('ebwebApp.statisticsCode.message')
        );
        if (!this.isSaveAndCreate) {
            this.previousState();
        } else {
            this.reload();
        }
    }

    private onSaveError(response) {
        this.toastr.error(this.translate.instant(`ebwebApp.statisticsCode.${response.error.errorKey}`));
    }

    save() {
        event.preventDefault();
        if (!this.checkEmpty()) {
            if (this.statisticsCode.id !== undefined) {
                this.statisticsCodeService
                    .update(this.statisticsCode)
                    .subscribe((res: HttpResponse<any>) => this.onSaveSuccess(), httpErrorResponse => this.onSaveError(httpErrorResponse));
            } else {
                this.statisticsCodeService
                    .create(this.statisticsCode)
                    .subscribe((res: HttpResponse<any>) => this.onSaveSuccess(), httpErrorResponse => this.onSaveError(httpErrorResponse));
            }
        }
        this.closeContent();
    }

    saveAndNew() {
        event.preventDefault();
        this.isSaveAndCreate = true;
        this.save();
    }

    delete() {
        event.preventDefault();
        if (this.statisticsCode.id) {
            this.router.navigate(['/statistics-code', { outlets: { popup: this.statisticsCode.id + '/delete' } }]);
        }
    }

    copy() {
        this.statisticsCodeCopy = Object.assign({}, this.statisticsCode);
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
            this.modalRef = null;
        }
        this.isClose = true;
        this.router.navigate(['statistics-code']);
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
            this.modalRef = null;
        }
    }

    closeForm() {
        event.preventDefault();
        if (!this.utilsService.isEquivalent(this.statisticsCode, this.statisticsCodeCopy)) {
            if (!this.modalRef) {
                this.modalRef = this.modalService.open(this.content, { backdrop: 'static' });
            }
        } else {
            this.close();
        }
    }

    canDeactive(): boolean {
        if (!this.isClose) {
            return this.utilsService.isEquivalent(this.statisticsCode, this.statisticsCodeCopy);
        }
        return true;
    }

    ngAfterViewInit(): void {
        this.focusFirstInput();
    }
}
