import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { DomSanitizer } from '@angular/platform-browser';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'eb-show-pdf-popup',
    templateUrl: './eb-report-pdf-popup.component.html'
})
export class EbReportPdfPopupComponent implements OnInit {
    credentials: any;
    modalData: any;
    filePdf: any;
    typeID: any;

    constructor(private sanitizer: DomSanitizer, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {
        this.credentials = {};
    }

    ngOnInit() {
        const blob = new Blob([this.modalData.body], { type: 'application/pdf' });
        const fileURL = URL.createObjectURL(blob);
        this.filePdf = this.sanitizer.bypassSecurityTrustResourceUrl(fileURL);
    }

    download() {
        this.eventManager.broadcast({
            name: `export-excel-${this.typeID}`
        });
    }
}
