import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CongNoPhaiTraComponent } from 'app/bao-cao/mua-hang/cong-no-phai-tra/cong-no-phai-tra.component';

@Injectable({ providedIn: 'root' })
export class CongNoPhaiTraModalService {
    private isOpen = false;
    constructor(private modalService: NgbModal) {}

    open(status): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        const modalRef = this.modalService.open(CongNoPhaiTraComponent, {
            size: 'lg',
            windowClass: 'width-80 width-50',
            backdrop: 'static'
        });
        modalRef.componentInstance.status = status;
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
