import { Component, OnInit, Input } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'app-contextmenu',
    templateUrl: './contextmenu.component.html',
    styleUrls: ['./contextmenu.component.css']
})
export class ContextMenuComponent implements OnInit {
    @Input() Details: any;
    @Input() DetailTaxs: any;
    @Input() selectedDetail: any;
    @Input() selectedDetailTax: any;
    @Input() x: 0;
    @Input() y: 0;

    constructor(private eventManager: JhiEventManager) {}

    ngOnInit() {}

    addNewRowByRightClick(arrObject: object[], status: number) {
        arrObject.push({});
        if (status === 0) {
            arrObject[arrObject.length - 1]['amount'] = 0;
            arrObject[arrObject.length - 1]['amountOriginal'] = 0;
            // this.utilsService.autoPrinciple(this.autoPrinciple, this.mCReceiptDetails[this.mCReceiptDetails.length - 1]);
        } else if (status === 1) {
            arrObject[arrObject.length - 1]['vATAmount'] = 0;
            arrObject[arrObject.length - 1]['vATAmountOriginal'] = 0;
            arrObject[arrObject.length - 1]['vATAmount'] = 0;
            arrObject[arrObject.length - 1]['vATAmountOriginal'] = 0;
            arrObject[arrObject.length - 1]['pretaxAmount'] = 0;
            arrObject[arrObject.length - 1]['pretaxAmountOriginal'] = 0;
        }
    }

    deleteRowByRightClick(arrObject: object[], eventData: any) {
        const index = arrObject.indexOf(eventData);
        arrObject.splice(index, 1);
        // sau khi xoa 1 dong
        this.eventManager.broadcast({
            name: 'afterDeleteRow'
        });
    }
}
