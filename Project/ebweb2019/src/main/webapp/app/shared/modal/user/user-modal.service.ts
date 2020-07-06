import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UserModalComponent } from 'app/shared/modal/user/user.modal.component';

@Injectable({ providedIn: 'root' })
export class UserModalService {
    private isOpen = false;
    constructor(private modalService: NgbModal) {}
    open(data, refModal?, modalData?, isSizeSm?: boolean, typeID?, disableWindowClass?): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        const modalRef = this.modalService.open(refModal ? refModal : UserModalComponent, {
            size: 'lg',
            backdrop: 'static'
        });
        modalRef.componentInstance.login = data;
        if (modalData) {
            modalRef.componentInstance.modalData = modalData;
        }
        if (typeID) {
            modalRef.componentInstance.typeID = typeID;
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
