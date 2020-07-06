import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { BaoCaoKetQuaComponent } from 'app/bao-cao/bao-cao-tai-chinh/bao-cao-ket-qua/bao-cao-ket-qua.component';

@Injectable({ providedIn: 'root' })
export class BaoCaoKetQuaService {
    private isOpen = false;
    constructor(private modalService: NgbModal) {}

    open(typeName, treeOrganizationUnits, organizationUnit): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        const modalRef = this.modalService.open(BaoCaoKetQuaComponent, {
            size: 'lg',
            windowClass: 'width-80 width-50',
            backdrop: 'static'
        });
        modalRef.componentInstance.typeName = typeName;
        modalRef.componentInstance.treeOrganizationUnits = treeOrganizationUnits;
        modalRef.componentInstance.organizationUnit = organizationUnit;
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
