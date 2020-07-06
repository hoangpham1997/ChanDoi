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
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';

@Component({
    selector: 'eb-handling-result',
    templateUrl: 'bank-result.component.html',
    styleUrls: ['bank-result.component.css']
})
export class MaterialGoodsResultComponent implements OnInit {
    data: HandlingResult;
    modalData: any[];
    typeID: number;
    listIDRecorded: string[];
    listIDUnrecorded: string[];
    countTotalVouchers: number;
    countSuccessVouchers: number;
    countFailVouchers: number;
    listDeleteFail: IMultiDelete[];
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

    trackId(index: number, item: IMBDeposit) {
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
