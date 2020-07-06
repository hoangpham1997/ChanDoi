import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'eb-signatures-connect-popup',
    templateUrl: './signatures-popup.component.html'
})
export class SignaturesPopupComponent {
    PATH = 'https://easyinvoices.vn/tai-xuong/';

    constructor(public activeModal: NgbActiveModal) {}

    close() {
        this.activeModal.dismiss('cancel');
    }

    linkToEInvoiceTool() {
        window.open(this.PATH, '_blank');
    }
}
