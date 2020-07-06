import { Component, OnInit } from '@angular/core';
import { IDiscountAllocation } from 'app/shared/modal/discount-allocation/discount-allocation.model';
import { JhiEventManager } from 'ng-jhipster';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { getEmptyRow } from 'app/shared/util/row-util';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';

@Component({
    selector: 'eb-discount-allocation-modal',
    templateUrl: './discount-allocation-modal.component.html',
    styleUrls: ['./discount-allocation-modal.component.css']
})
export class DiscountAllocationModalComponent implements OnInit {
    account: any;
    totalDiscount: number;
    allocationType: number;
    modalData: any;
    discountAllocations: IDiscountAllocation[];
    modalRef: NgbModalRef;
    totalQuantity: number;
    totalAmount: number;
    totalResultAmount: number;
    totalDiscountRate: number;
    isForeignCurrency: boolean;
    message: string;
    constructor(
        private eventManager: JhiEventManager,
        private principal: Principal,
        private activeModal: NgbActiveModal,
        private toastrService: ToastrService,
        public utilsService: UtilsService,
        private translateService: TranslateService,
        private modalService: NgbModal
    ) {}
    ngOnInit(): void {
        this.discountAllocations = this.discountAllocations ? this.discountAllocations : [];
        this.totalDiscount = 0;
        this.principal.identity().then(account => {
            this.account = account;
            if (this.modalData) {
                this.discountAllocations = this.modalData.discountAllocations;
                this.isForeignCurrency = this.account.organizationUnit.currencyID !== this.modalData.currencyCode;
                this.totalAmount = this.discountAllocations.reduce((a, b) => a + b.amountOriginal, 0);
                this.totalQuantity = this.discountAllocations.reduce((a, b) => a + b.quantity, 0);
                this.totalAmount = this.utilsService.round(this.totalAmount, this.account.systemOption, this.isForeignCurrency ? 8 : 7);
                this.totalQuantity = this.utilsService.round(this.totalQuantity, this.account.systemOption, this.isForeignCurrency ? 8 : 7);
            }
            this.allocationType = 1;
        });
    }

    apply(modal) {
        this.totalDiscountRate = 0;
        for (let i = 0; i < this.discountAllocations.length; i++) {
            if (this.discountAllocations[i].checked) {
                this.totalDiscountRate += this.discountAllocations[i].allocationRate;
            }
        }
        for (let i = 0; i < this.discountAllocations.length; i++) {
            if (this.discountAllocations[i].checked) {
                if (this.discountAllocations[i].discountAmountOriginal > this.discountAllocations[i].amountOriginal) {
                    this.toastrService.error(this.translateService.instant('global.commonInfo.discountAmountGreaterThan'));
                    // this.message = this.translateService.instant('global.commonInfo.discountAmountGreaterThan');
                    // this.modalRef = this.modalService.open(modal, { backdrop: 'static' });
                    return;
                }
            }
        }

        if (this.totalResultAmount !== this.totalDiscount) {
            this.toastrService.error(this.translateService.instant('global.commonInfo.totalDiscountGreaterThanOther'));
            // this.message = this.translateService.instant('global.commonInfo.totalDiscountGreaterThan');
            // this.modalRef = this.modalService.open(modal, { backdrop: 'static' });
            return;
        }
        this.eventManager.broadcast({
            name: 'updateDiscountAllocations',
            content: this.discountAllocations.filter(x => x.checked)
        });
        this.activeModal.dismiss(false);
    }

    close() {
        this.activeModal.dismiss(false);
    }

    onAllocating(modal) {
        const listDataCheck = this.discountAllocations.filter(x => x.checked);
        if (!listDataCheck || listDataCheck.length < 1) {
            this.toastrService.error(this.translateService.instant('global.commonInfo.discountAllocationsNull'));
            return;
        }
        if (this.totalDiscount < 1) {
            this.toastrService.error(this.translateService.instant('global.commonInfo.totalDiscountNull'));
            return;
        }

        const quantityRate = this.discountAllocations.filter(x => x.checked).reduce((a, b) => a + b.quantity, 0);
        const amountOriginalRate = this.discountAllocations.filter(x => x.checked).reduce((a, b) => a + b.amountOriginal, 0);
        this.totalDiscountRate = 0;
        if (this.totalDiscount > amountOriginalRate) {
            this.toastrService.error(this.translateService.instant('global.commonInfo.totalDiscountGreaterThanOther'));
            // this.message = this.translateService.instant('global.commonInfo.totalDiscountGreaterThan');
            // this.modalRef = this.modalService.open(modal, { backdrop: 'static' });
            return;
        }
        for (let i = 0; i < this.discountAllocations.length; i++) {
            if (this.discountAllocations[i].checked) {
                this.discountAllocations[i].allocationRate = this.allocationType
                    ? this.discountAllocations[i].quantity / quantityRate * 100
                    : this.discountAllocations[i].amountOriginal / amountOriginalRate * 100;
                this.totalDiscountRate += this.discountAllocations[i].allocationRate;
            } else {
                this.discountAllocations[i].allocationRate = null;
                this.discountAllocations[i].discountAmountOriginal = null;
            }
        }
        for (let i = 0; i < this.discountAllocations.length; i++) {
            if (this.discountAllocations[i].allocationRate) {
                this.discountAllocations[i].discountAmountOriginal = this.totalDiscount * this.discountAllocations[i].allocationRate / 100;
                // this.discountAllocations[i].discountAmountOriginal =
                //     this.utilsService.round(this.discountAllocations[i].discountAmountOriginal, this.account.systemOption, this.isForeignCurrency ? 8 : 7);
            }
        }
        this.totalResultAmount = this.discountAllocations.filter(x => x.checked).reduce((a, b) => a + b.discountAmountOriginal, 0);
        this.totalResultAmount = this.utilsService.round(this.totalResultAmount, this.account.systemOption, this.isForeignCurrency ? 8 : 7);
    }

    isCheckAll() {
        // return this.discountAllocations && !this.discountAllocations.some(x => !x.checked);
        if (this.discountAllocations && this.discountAllocations.length > 0) {
            for (let i = 0; i < this.discountAllocations.length; i++) {
                if (!this.discountAllocations[i].checked) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    checkAll() {
        // this.discountAllocations.forEach(x => (x.checked = true));
        if (this.discountAllocations) {
            const isCheckAll = this.isCheckAll();
            for (let i = 0; i < this.discountAllocations.length; i++) {
                this.discountAllocations[i].checked = !isCheckAll;
            }
        }
    }

    check(i: number) {
        this.discountAllocations[i].checked = !this.discountAllocations[i].checked;
    }

    getEmptyRow(data: IDiscountAllocation[]) {
        return getEmptyRow(data);
    }

    onChangeAllocationRate(discountAllocation: IDiscountAllocation) {
        discountAllocation.discountAmountOriginal = this.totalDiscount * discountAllocation.allocationRate / 100;
        let totalRate = 0;
        for (let i = 0; i < this.discountAllocations.length; i++) {
            if (this.discountAllocations[i].allocationRate) {
                totalRate += this.discountAllocations[i].allocationRate;
                totalRate = this.utilsService.round(totalRate, this.account.systemOption, 5);
                this.discountAllocations[i].discountAmountOriginal = this.totalDiscount * this.discountAllocations[i].allocationRate / 100;
                this.discountAllocations[i].discountAmountOriginal = this.utilsService.round(
                    this.discountAllocations[i].discountAmountOriginal,
                    this.account.systemOption,
                    this.isForeignCurrency ? 8 : 7
                );
            }
        }
        this.totalDiscountRate = totalRate;
        this.totalDiscountRate = this.utilsService.round(this.totalDiscountRate, this.account.systemOption, 5);
        this.totalResultAmount = this.discountAllocations.filter(x => x.checked).reduce((a, b) => a + b.discountAmountOriginal, 0);
        this.totalResultAmount = this.utilsService.round(this.totalResultAmount, this.account.systemOption, this.isForeignCurrency ? 8 : 7);
    }
}
