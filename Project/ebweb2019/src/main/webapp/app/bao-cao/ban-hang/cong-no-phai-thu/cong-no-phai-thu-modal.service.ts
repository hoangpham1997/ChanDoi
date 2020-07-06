import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CongNoPhaiThuComponent } from 'app/bao-cao/ban-hang/cong-no-phai-thu/cong-no-phai-thu.component';

@Injectable({ providedIn: 'root' })
export class CongNoPhaiThuModalService {
    private isOpen = false;
    constructor(private modalService: NgbModal) {}

    open(status): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        const modalRef = this.modalService.open(CongNoPhaiThuComponent, {
            size: 'sm',
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
