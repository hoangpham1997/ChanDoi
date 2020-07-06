import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { IBank } from 'app/shared/model/bank.model';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { CategoryName } from 'app/app.constants';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { EMContractService } from 'app/entities/em-contract';

@Component({
    selector: 'eb-e-m-contract-cobobox',
    templateUrl: './hop-dong.html',
    styleUrls: ['./hop-dong.css']
})
export class EbEMContractComboboxComponent implements OnInit {
    private _eMContract: IEMContract;
    isSaving: boolean;
    signedDateDp: any;
    startedDateDp: any;
    closedDateDp: any;
    listColumnsBank: string[];
    listHeaderColumnnsBank: string[];
    saveSuccess: boolean;
    isSaveAndCreate: boolean;

    constructor(
        private eMContractService: EMContractService,
        private activatedRoute: ActivatedRoute,
        private jhiAlertService: JhiAlertService,
        private activeModal: NgbActiveModal,
        public translate: TranslateService,
        private toastr: ToastrService,
        private eventManager: JhiEventManager,
        public utilsService: UtilsService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.isSaveAndCreate = false;
        this.eMContract = {};
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.isSaveAndCreate = false;
        if (this.eMContract.id !== undefined) {
            this.subscribeToSaveResponse(this.eMContractService.update(this.eMContract));
        } else {
            this.subscribeToSaveResponse(this.eMContractService.create(this.eMContract));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IEMContract>>) {
        result.subscribe((res: HttpResponse<IEMContract>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.saveSuccess = true;
        this.toastr.success(
            this.translate.instant('ebwebApp.mCReceipt.home.saveSuccess'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
        this.close();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    get eMContract() {
        return this._eMContract;
    }

    set eMContract(eMContract: IEMContract) {
        this._eMContract = eMContract;
    }

    saveAndCreate() {
        this.isSaving = true;
        this.isSaveAndCreate = true;
        if (this.eMContract.id !== undefined) {
            this.isSaving = false;
            this.subscribeToSaveResponse(this.eMContractService.update(this.eMContract));
        } else {
            this.isSaving = false;
            this.subscribeToSaveResponse(this.eMContractService.create(this.eMContract));
        }
        this.clearValue();
    }

    close() {
        if (this.saveSuccess) {
            this.eventManager.broadcast({
                name: 'saveSuccess',
                content: { name: CategoryName.HOP_DONG, data: this.eMContract }
            });
        }
        this.eventManager.broadcast({
            name: 'closePopup',
            content: null
        });
        if (!this.isSaveAndCreate && this.saveSuccess) {
            this.activeModal.dismiss(false);
        }
    }

    clearValue() {
        // this.eMContract.bankAccount = null;
        // this.eMContract.bankName = null;
        // this.eMContract.bankBranchName = null;
        // this.eMContract.description = null;
        // this.eMContract.address = null;
        // this.eMContract.isActive = false;
    }
}
