import { IStatisticsCode, StatisticsCode } from 'app/shared/model/statistics-code.model';
import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { StatisticsCodeService } from 'app/danhmuc/statistics-code/statistics-code.service';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CategoryName } from 'app/app.constants';
import { JhiEventManager } from 'ng-jhipster';
import { BaseComponent } from 'app/shared/base-component/base.component';

@Component({
    selector: 'eb-statistics-code-cobobox',
    templateUrl: './ma-thong-ke.html',
    styleUrls: ['./ma-thong-ke.css']
})
export class EbStatisticsCodeComboboxComponent extends BaseComponent implements OnInit {
    statisticsCode: IStatisticsCode;
    cbxStatisticsCodes: IStatisticsCode[];
    isSaveAndCreate: boolean;
    isClose: boolean;
    // saveSuccess: boolean;
    modalRef: NgbModalRef;
    data: IStatisticsCode;

    constructor(
        private statisticsCodeService: StatisticsCodeService,
        private toastr: ToastrService,
        private principal: Principal,
        private translate: TranslateService,
        private activeModal: NgbActiveModal,
        public utilsService: UtilsService,
        private eventManager: JhiEventManager
    ) {
        super();
    }

    ngOnInit(): void {
        this.statisticsCode = new StatisticsCode();
        // this.saveSuccess = false;
        this.isSaveAndCreate = false;
        this.statisticsCodeService.getActiveStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            this.cbxStatisticsCodes = res.body;
        });
        // this.copy();
    }

    previousState() {
        this.activeModal.dismiss(false);
    }

    reload() {
        this.ngOnInit();
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
        // this.copy();
        // this.saveSuccess = true;
        this.toastr.success(
            this.translate.instant('ebwebApp.statisticsCode.saveSuccessfully'),
            this.translate.instant('ebwebApp.statisticsCode.message')
        );
        if (!this.isSaveAndCreate) {
            this.eventManager.broadcast({
                name: 'saveSuccess',
                content: { name: CategoryName.MA_THONG_KE, data: this.data }
            });
            this.close();
        } else {
            this.eventManager.broadcast({
                name: 'saveAndNewSuccess',
                content: { name: CategoryName.MA_THONG_KE, data: this.data }
            });
            this.reload();
        }
    }

    private onSaveError(response) {
        this.toastr.error(this.translate.instant(`ebwebApp.statisticsCode.${response.error.errorKey}`));
    }

    save() {
        if (!this.checkEmpty()) {
            this.statisticsCodeService.create(this.statisticsCode).subscribe(
                (res: HttpResponse<any>) => {
                    this.data = res.body;
                    this.onSaveSuccess();
                },
                httpErrorResponse => this.onSaveError(httpErrorResponse)
            );
        }
        // this.closeContent();
    }

    saveAndNew() {
        this.isSaveAndCreate = true;
        this.save();
    }

    close() {
        this.eventManager.broadcast({
            name: 'closePopup',
            content: null
        });
        this.activeModal.dismiss(false);
    }
}
