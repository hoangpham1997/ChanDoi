import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EbRefModalComponent } from 'app/shared/modal/ref/ref.component';
import { EbExportIncoiceModalComponent } from 'app/shared/export-invoice/export-invoice.component';
import { EbPrepaidExpenseVoucherModalComponent } from 'app/shared/prepaid-expense-voucher/prepaid-expense-voucher.component';
import { EbPrepaidExpenseCurrentBookModalComponent } from 'app/shared/prepaid-expense-current-book/prepaid-expense-current-book.component';

@Injectable({ providedIn: 'root' })
export class ExportPrepaidExpenseCurrentBookModalService {
    private isOpen = false;
    constructor(private modalService: NgbModal) {}

    open(data): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        const modalRef = this.modalService.open(EbPrepaidExpenseCurrentBookModalComponent, {
            size: 'lg',
            windowClass: 'width-80',
            backdrop: 'static'
        });
        modalRef.componentInstance.data = data;
        modalRef.result.then(
            result => {
                this.isOpen = false;
            },
            reason => {
                this.isOpen = false;
            }
        );
        return modalRef;
    }
}
