import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EbRefModalComponent } from 'app/shared/modal/ref/ref.component';
import { EbExportIncoiceModalComponent } from 'app/shared/export-invoice/export-invoice.component';

@Injectable({ providedIn: 'root' })
export class ExportInvoiceModalService {
    private isOpen = false;
    constructor(private modalService: NgbModal) {}

    open(data, currencyCode, accountingObject, typeID): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        const modalRef = this.modalService.open(EbExportIncoiceModalComponent, { size: 'lg', windowClass: 'width-80', backdrop: 'static' });
        modalRef.componentInstance.data = data;
        modalRef.componentInstance.currencyCode = currencyCode;
        modalRef.componentInstance.accountingObject = accountingObject;
        modalRef.componentInstance.typeID = typeID;
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
