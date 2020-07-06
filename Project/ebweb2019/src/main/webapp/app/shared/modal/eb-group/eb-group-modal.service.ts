import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EbGroupModalComponent } from 'app/shared/modal/eb-group/eb-group-modal.component';

@Injectable({ providedIn: 'root' })
export class EbGroupModalService {
    private isOpen = false;
    constructor(private modalService: NgbModal) {}
    open(data, refModal?, modalData?, optional?, isSizeSm?: boolean, disableWindowClass?): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        const modalRef = this.modalService.open(refModal ? refModal : EbGroupModalComponent, {
            size: 'lg',
            backdrop: 'static'
        });
        modalRef.componentInstance.ebGroup = data;
        if (modalData) {
            modalRef.componentInstance.orgUnit = modalData;
        }
        if (optional) {
            modalRef.componentInstance.user = optional;
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
