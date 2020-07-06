import * as moment from 'moment';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import { DBDateClosed, DBDateClosedOld, SO_LAM_VIEC, TypeID } from 'app/app.constants';
import { DATE_FORMAT } from 'app/shared';
import { Moment } from 'moment';
import { Component, OnInit } from '@angular/core';
import { IMultiDelete } from 'app/shared/model/multi-delete';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import { PrepaidExpenses } from 'app/shared/modal/prepaid-expenses/prepaid-expenses.model';
import { IPrepaidExpense } from 'app/shared/model/prepaid-expense.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';

@Component({
    selector: 'eb-prepaid-expenses',
    templateUrl: 'prepaid-expenses.component.html',
    styleUrls: ['prepaid-expenses.css']
})
export class PrepaidExpensesComponent implements OnInit {
    data: any;
    modalData: any;
    countDeletedFail: number;
    countDeletedSuccess: number;
    messages: string;
    listResult: any[];
    typeID: number;
    countTotalVouchers: number;
    countSuccessVouchers: number;
    countFailVouchers: number;
    dateLockNow: Moment;
    dateLockNew: Moment;
    account: any;
    isSoTaiChinh: boolean;
    XUAT_HOA_DON = TypeID.XUAT_HOA_DON;

    constructor(
        public activeModal: NgbActiveModal,
        private principal: Principal,
        private toastr: ToastrService,
        public translate: TranslateService,
        public eventManager: JhiEventManager
    ) {}

    ngOnInit(): void {
        this.principal.identity().then(account => {
            this.account = account;
            if (account) {
                console.log(this.modalData);
                console.log(this.modalData.listDelete);
                console.log(this.modalData.listDeletedFail);
                console.log(this.modalData.messages);
                this.countDeletedFail = this.modalData.listDeletedFail.length;
                this.countDeletedSuccess = this.modalData.listDeletedSuccess.length;
                const dbDateClosed = this.account.systemOption.find(x => x.code === DBDateClosed).data;
                const dbDateClosedOld = this.account.systemOption.find(x => x.code === DBDateClosedOld).data;
                this.dateLockNow = dbDateClosed ? moment(dbDateClosed, DATE_FORMAT) : null;
                this.dateLockNew = dbDateClosedOld ? moment(dbDateClosedOld, DATE_FORMAT) : null;
                this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
            }
        });
        if (this.data) {
            this.countTotalVouchers = this.data.countTotalVouchers;
            this.countSuccessVouchers = this.data.countSuccessVouchers;
            this.countFailVouchers = this.data.countFailVouchers;
        }
    }

    trackId(index: number, item: IPrepaidExpense) {
        return item.id;
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }
}
