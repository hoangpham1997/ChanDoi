import { Component, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'eb-dialog-delete',
    templateUrl: './dialog-delete.component.html'
})
export class DialogDeleteComponent implements OnInit {
    modalData: any;
    constructor(public activeModal: NgbActiveModal) {}

    ngOnInit(): void {
        if (!this.modalData) {
            this.activeModal.dismiss('cancel');
        }
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.modalData.confirmDelete(id);
        this.activeModal.dismiss(true);
    }
}
