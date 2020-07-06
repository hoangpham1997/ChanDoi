import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EbRefModalComponent } from 'app/shared/modal/ref/ref.component';

@Injectable({ providedIn: 'root' })
export class RefModalService {
    private isOpen = false;
    constructor(private modalService: NgbModal) {}
    open(
        data,
        refModal?,
        modalData?,
        isSizeSm?: boolean,
        typeID?,
        disableWindowClass?,
        currencyID?,
        objectId?,
        accountingObjectId?,
        force?
    ): NgbModalRef {
        if (!force) {
            if (this.isOpen) {
                return;
            }
        }
        this.isOpen = true;
        const modalRef = this.modalService.open(refModal ? refModal : EbRefModalComponent, {
            size: isSizeSm ? 'sm' : 'lg',
            windowClass: disableWindowClass ? disableWindowClass : 'width-80',
            backdrop: 'static'
        });
        modalRef.componentInstance.data = data;
        if (modalData) {
            modalRef.componentInstance.modalData = modalData;
        }
        if (typeID) {
            modalRef.componentInstance.typeID = typeID;
        }
        if (currencyID) {
            modalRef.componentInstance.currencyID = currencyID;
        }
        if (objectId) {
            modalRef.componentInstance.objectId = objectId;
        }
        if (accountingObjectId) {
            modalRef.componentInstance.accountingObjectId = accountingObjectId;
        }
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
