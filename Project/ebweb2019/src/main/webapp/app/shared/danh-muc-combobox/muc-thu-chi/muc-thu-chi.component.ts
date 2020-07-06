import { AfterViewInit, Component, OnInit } from '@angular/core';
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
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { BudgetItemService } from 'app/entities/budget-item';
import { BaseComponent } from 'app/shared/base-component/base.component';

@Component({
    selector: 'eb-budget-item-cobobox',
    templateUrl: './muc-thu-chi.html',
    styleUrls: ['./muc-thu-chi.css']
})
export class EbBudgetItemComboboxComponent extends BaseComponent implements OnInit {
    budgetItem: IBudgetItem;
    data: IBudgetItem;
    isSaving: boolean;
    saveSuccess: boolean;
    isSaveAndCreate: boolean;
    budgetitems: IBudgetItem[];
    listType: any[];
    listColumnsType = ['name'];

    constructor(
        private jhiAlertService: JhiAlertService,
        private budgetItemService: BudgetItemService,
        private activatedRoute: ActivatedRoute,
        public utilsService: UtilsService,
        private activeModal: NgbActiveModal,
        public translate: TranslateService,
        private toastr: ToastrService,
        private eventManager: JhiEventManager
    ) {
        super();
    }

    creatBudgetItem() {
        this.isSaving = false;
        this.saveSuccess = false;
        this.budgetItem = {};
        this.budgetItem.parentID = null;
        this.budgetItem.budgetItemName = '';
        this.budgetItem.budgetItemCode = '';
        this.budgetItem.isParentNode = false;
        this.budgetItem.grade = 1;
        this.budgetItem.budgetItemType = 0;
        this.budgetItem.isActive = true;
        this.isSaveAndCreate = false;
    }

    ngOnInit() {
        this.creatBudgetItem();
        this.budgetItemService.getBudgetItems().subscribe(
            (res: HttpResponse<IBudgetItem[]>) => {
                this.budgetitems = res.body;
                this.budgetitems = this.budgetitems.filter(bud => bud.isActive);
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    changeModel() {
        if (this.budgetItem.parentID !== null) {
            this.budgetItem.grade = this.budgetitems.filter(bud => this.budgetItem.parentID === bud.id)[0].grade + 1;
        }
    }

    save() {
        this.isSaveAndCreate = false;
        this.continueSave();
    }

    continueSave() {
        if (this.budgetItem.budgetItemCode === '') {
            this.toastr.error(this.translate.instant('ebwebApp.budgetItem.errorInputCode'));
        } else if (this.budgetItem.budgetItemName === '') {
            this.toastr.error(this.translate.instant('ebwebApp.budgetItem.errorInputName'));
        } else {
            this.budgetItemService.create(this.budgetItem).subscribe(
                res => {
                    this.saveSuccess = true;
                    this.data = res.body;
                    if (this.isSaveAndCreate) {
                        this.eventManager.broadcast({
                            name: 'saveAndNewSuccess',
                            content: { name: CategoryName.MUC_THU_CHI, data: this.data }
                        });
                        this.creatBudgetItem();
                    } else {
                        this.eventManager.broadcast({
                            name: 'saveSuccess',
                            content: { name: CategoryName.MUC_THU_CHI, data: this.data }
                        });
                        this.close();
                    }
                    this.toastr.success(this.translate.instant('ebwebApp.budgetItem.successful'));
                },
                error => {
                    this.error(error);
                }
            );
        }
    }

    error(err) {
        this.toastr.error(this.translate.instant(`ebwebApp.budgetItem.${err.error.message}`));
    }

    saveAndCreate() {
        this.isSaving = true;
        this.isSaveAndCreate = true;
        this.continueSave();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    closeForm() {
        this.activeModal.close();
    }

    close() {
        this.eventManager.broadcast({
            name: 'closePopup',
            content: null
        });
        this.activeModal.dismiss(false);
    }
}
