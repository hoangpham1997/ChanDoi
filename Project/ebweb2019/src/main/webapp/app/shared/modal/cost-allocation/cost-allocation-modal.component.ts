import { Component, OnInit } from '@angular/core';
import { IDiscountAllocation } from 'app/shared/modal/discount-allocation/discount-allocation.model';
import { JhiEventManager } from 'ng-jhipster';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ICostAllocation } from 'app/shared/modal/cost-allocation/cost-allocation.model';
import { CostVouchersModalComponent } from 'app/shared/modal/cost-vouchers/cost-vouchers-modal.component';
import { ICostVouchersDTO } from 'app/shared/modal/cost-vouchers/cost-vouchers-dto.model';
import { IPPInvoiceDetailCost } from 'app/shared/model/pp-invoice-detail-cost.model';
import { IPPInvoice } from 'app/shared/model/pp-invoice.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';

@Component({
    selector: 'eb-cost-allocation-modal',
    templateUrl: './cost-allocation-modal.component.html',
    styleUrls: ['./cost-allocation-modal.component.css']
})
export class CostAllocationModalComponent implements OnInit {
    account: any;
    totalDiscount: number;
    allocationType: number;
    modalData: any;
    modalRef: NgbModalRef;
    costAllocations: ICostAllocation[];
    ppInvoiceId: string;
    ppInvoiceDetailCost: IPPInvoiceDetailCost[]; // list chứng từ chi phí
    isHaiQuan: boolean;
    pPInvoice: IPPInvoice;

    costVouchers: ICostVouchersDTO[]; // trả về từ popup chọn chứng từ chi phí (newList)
    sumQuantity: number;
    sumFreightAmount: number;
    sumAmount: number;

    constructor(
        private eventManager: JhiEventManager,
        private principal: Principal,
        private refModalService: NgbModal,
        private toastService: ToastrService,
        private translateService: TranslateService,
        public utilsService: UtilsService,
        private activeModal: NgbActiveModal
    ) {
        this.costAllocations = this.costAllocations ? this.costAllocations : [];
        this.account = { organizationUnit: {} };
    }
    ngOnInit(): void {
        this.principal.identity().then(account => {
            this.account = account;
            if (this.modalData) {
                this.costAllocations = this.modalData.costAllocations;
                this.ppInvoiceDetailCost = this.modalData.ppInvoiceDetailCost;
                this.isHaiQuan = this.modalData.isHaiQuan;
                this.ppInvoiceId = this.modalData.ppInvoiceId;
                this.costVouchers = this.modalData.costVouchers;
                this.pPInvoice = this.modalData.pPInvoice;
                // if (this.costVouchers && this.costVouchers.length) {
                //     this.costVouchers.forEach(item => {
                //         this.totalDiscount += item.freightAmount ? item.freightAmount : 0;
                //     });
                // }
                // với trường hợp update tính lại totalDiscount
                if (this.ppInvoiceDetailCost && this.ppInvoiceDetailCost.length) {
                    this.totalDiscount = this.ppInvoiceDetailCost
                        .filter(item => item.costType === this.isHaiQuan)
                        .reduce((a, b) => a + b.amount, 0);
                }
            }
            this.changeSum();
            this.allocationType = 0;
        });
        this.totalDiscount = this.totalDiscount ? this.totalDiscount : 0;
        // tính totalDiscount khi dữ liệu được trả về từ form chọn chứng từ chi phí
        if (this.costVouchers && this.costVouchers.length) {
            this.costVouchers.forEach(item => {
                this.totalDiscount += item.freightAmount ? item.freightAmount : 0;
            });
        }
    }

    changeSum() {
        if (this.costAllocations && this.costAllocations.length) {
            this.sumQuantity = 0;
            this.sumFreightAmount = 0;
            this.sumAmount = 0;
            this.costAllocations.forEach(item => {
                this.sumQuantity += item.quantity ? item.quantity : 0;
                this.sumFreightAmount += item.freightAmount ? item.freightAmount : 0;
                this.sumAmount += item.amount ? item.amount : 0;
            });
        }
    }

    apply() {
        let sumRate = this.costAllocations.reduce((a, b) => a + (b.allocationRate ? b.allocationRate : 0), 0);
        sumRate = this.utilsService.round(sumRate, this.account.systemOption, 6);
        if (sumRate > 100) {
            this.toastService.error(this.translateService.instant('ebwebApp.pPInvoice.error.allocationRateSumEnoughInvalid'));
            return;
        } else if (sumRate < 100) {
            this.toastService.error(this.translateService.instant('ebwebApp.pPInvoice.error.allocationRateSumNotEnoughInvalid'));
            return;
        } else {
            for (const item of this.costAllocations) {
                if (item.allocationRate > 100) {
                    this.toastService.error(this.translateService.instant('ebwebApp.pPInvoice.error.allocationRateInvalid'));
                    return;
                }
            }

            if (this.costAllocations.filter(item => item.freightAmount).length > 0) {
                this.eventManager.broadcast({
                    name: 'updateCostAllocations',
                    content: {
                        costAllocations: this.costAllocations,
                        costVouchers: this.costVouchers,
                        isHaiQuan: this.isHaiQuan
                    }
                });
                this.activeModal.dismiss(false);
            }
        }
    }

    close() {
        this.activeModal.dismiss(false);
    }

    onAllocating() {
        if (this.costVouchers && this.costVouchers.length > 0) {
            // giá trị trên mỗi phần trăm
            const valuePerPercent = this.allocationType
                ? this.costAllocations.reduce((a, b) => a + (b.amount ? b.amount : 0), 0) / 100
                : this.costAllocations.reduce((a, b) => a + (b.quantity ? b.quantity : 0), 0) / 100;

            for (let i = 0; i < this.costAllocations.length - 1; i++) {
                // tính phần trăm
                this.costAllocations[i].allocationRate = this.allocationType
                    ? this.costAllocations[i].amount / valuePerPercent
                    : this.costAllocations[i].quantity / valuePerPercent;

                // tính giá trị phân bổ
                this.costAllocations[i].freightAmount = this.totalDiscount * this.costAllocations[i].allocationRate / 100;
            }
            this.costAllocations[this.costAllocations.length - 1].allocationRate =
                100 - this.costAllocations.reduce((a, b) => a + (b.allocationRate ? b.allocationRate : 0), 0);
            this.costAllocations[this.costAllocations.length - 1].freightAmount =
                this.totalDiscount - this.costAllocations.reduce((a, b) => a + (b.freightAmount ? b.freightAmount : 0), 0);
            this.changeSum();
        } else {
            this.toastService.error(this.translateService.instant('ebwebApp.pPInvoice.error.noDataSelect'));
        }
    }

    selectCost() {
        if (this.costAllocations && this.costAllocations.length > 0) {
            this.activeModal.dismiss(false);
            const modalRef = this.refModalService.open(CostVouchersModalComponent, {
                size: 'lg',
                windowClass: 'modal-xl1',
                backdrop: 'static'
            });
            modalRef.componentInstance.costAllocations = this.costAllocations;
            modalRef.componentInstance.ppInvoiceId = this.ppInvoiceId;
            modalRef.componentInstance.ppInvoiceDetailCost = this.ppInvoiceDetailCost;
            modalRef.componentInstance.isHaiQuan = this.isHaiQuan;
            modalRef.componentInstance.pPInvoice = this.pPInvoice;
            modalRef.result.then(result => {}, reason => {});
        } else {
            this.toastService.warning(this.translateService.instant('ebwebApp.pPInvoice.error.nullCostAllocation'));
        }
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    changeRate(costAllocation: ICostAllocation) {
        costAllocation.freightAmount = this.totalDiscount * (costAllocation.allocationRate ? costAllocation.allocationRate : 0) / 100;
        this.changeSum();
    }

    changeFreightAmount(costAllocation: ICostAllocation) {
        if (this.totalDiscount) {
            costAllocation.allocationRate = costAllocation.freightAmount / this.totalDiscount * 100;
            this.changeSum();
        }
    }
}
