import { Component, OnInit } from '@angular/core';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'eb-delete-result',
    templateUrl: 'delete-result.component.html',
    styleUrls: ['delete-result.component.css']
})
export class DeleteResultComponent implements OnInit {
    modalData: any;
    countDeletedFail: number;
    countDeletedSuccess: number;
    countTotalDelete: number;
    messages: string;
    listResult: any[];
    private listCostSetName: any[];

    constructor(
        public activeModal: NgbActiveModal,
        private principal: Principal,
        private toastr: ToastrService,
        public translate: TranslateService,
        public eventManager: JhiEventManager
    ) {
        this.translate
            .get([
                'ebwebApp.costSet.Type.Order',
                'ebwebApp.costSet.Type.Construction',
                'ebwebApp.costSet.Type.Factory',
                'ebwebApp.costSet.Type.Manufacturing technology',
                'ebwebApp.costSet.Type.Product',
                'ebwebApp.costSet.Type.Others'
            ])
            .subscribe(res => {
                this.listCostSetName = [
                    { value: 0, name: this.translate.instant('ebwebApp.costSet.Type.Order') },
                    { value: 1, name: this.translate.instant('ebwebApp.costSet.Type.Construction') },
                    { value: 2, name: this.translate.instant('ebwebApp.costSet.Type.Factory') },
                    { value: 3, name: this.translate.instant('ebwebApp.costSet.Type.Manufacturing technology') },
                    { value: 4, name: this.translate.instant('ebwebApp.costSet.Type.Product') },
                    { value: 5, name: this.translate.instant('ebwebApp.costSet.Type.Others') }
                ];
            });
    }

    ngOnInit(): void {
        if (this.modalData) {
            console.log(this.modalData);
            console.log(this.modalData.listDelete);
            console.log(this.modalData.listDeletedFail);
            console.log(this.modalData.listDeletedSuccess);
            console.log(this.modalData.messages);
            this.countDeletedFail = this.modalData.listDeletedFail.length;
            this.countDeletedSuccess = this.modalData.listDeletedSuccess.length;
            this.countTotalDelete = this.modalData.listDelete.length;
            this.messages = this.modalData.messages;
            this.listResult = this.modalData.listDeletedFail;
        }
    }

    trackId(index: number, item: ICostSet) {
        return item.id;
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    getCostSetName(type): string {
        return this.listCostSetName.find(x => x.value === type).name;
    }
}
