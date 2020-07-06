import { Component, OnInit } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { Principal } from 'app/core';
import { GeneralLedgerService } from 'app/entities/general-ledger';

@Component({
    selector: 'eb-liabilities-modal',
    templateUrl: './view-liabilities.component.html'
})
export class ViewLiabilitiesComponent implements OnInit {
    modalData: any;
    account: any;
    liabilities: any;
    organizationUnit: any;
    postDate: string;
    objectCode: string;
    isReady: boolean;
    isVNDFormat: boolean;
    currencyID: string;

    constructor(
        private eventManager: JhiEventManager,
        private activeModal: NgbActiveModal,
        public utilsService: UtilsService,
        private principal: Principal,
        private generalLedgerService: GeneralLedgerService
    ) {}

    ngOnInit(): void {
        this.principal.identity().then(account => {
            this.account = account;

            if (this.modalData) {
                this.postDate = this.modalData.postDate;
                this.objectCode = this.modalData.objectCode;
                this.currencyID = this.modalData.currencyID;
                this.organizationUnit = this.account.organizationUnit.currencyID;
                const accountingObjectId: any = this.modalData.objectId;
                this.generalLedgerService
                    .calculatingLiabilities({
                        accountingObjectId,
                        postDate: this.postDate
                    })
                    .subscribe(res => {
                        if (res.body.messages === 'SUCCESS') {
                            this.liabilities = res.body.result ? res.body.result : 0;
                            this.isReady = true;
                        }
                    });
                if (this.account && this.account.organizationUnit && this.account.organizationUnit.currencyID) {
                    if (this.currencyID === this.account.organizationUnit.currencyID) {
                        this.isVNDFormat = true;
                    } else {
                        this.isVNDFormat = false;
                    }
                }
            }
        });
    }

    close() {
        this.activeModal.dismiss(false);
    }
}
