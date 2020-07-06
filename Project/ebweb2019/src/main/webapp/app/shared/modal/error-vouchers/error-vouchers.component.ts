import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import { DBDateClosed, DBDateClosedOld, SO_LAM_VIEC, TypeID } from 'app/app.constants';
import { Component, OnInit } from '@angular/core';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { SAInvoiceService } from 'app/ban-hang/ban_hang_chua_thu_tien';

@Component({
    selector: 'eb-handling-result',
    templateUrl: 'error-vouchers.component.html',
    styleUrls: ['error-vouchers.component.css']
})
export class ErrorVouchersComponent implements OnInit {
    data: any[];
    modalData: any[];
    typeID: number;
    account: any;
    isSoTaiChinh: boolean;
    XUAT_HOA_DON = TypeID.XUAT_HOA_DON;

    constructor(
        public activeModal: NgbActiveModal,
        private principal: Principal,
        private toastr: ToastrService,
        public translate: TranslateService,
        public eventManager: JhiEventManager,
        private sAInvoiceService: SAInvoiceService
    ) {}

    ngOnInit(): void {
        this.principal.identity().then(account => {
            this.account = account;
            if (account) {
                this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
            }
        });
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    view(typeID, refID) {
        let url = '';
        switch (typeID) {
            case TypeID.XUAT_KHO:
                url = `/#/xuat-kho/${refID}/edit/from-ref`;
                break;
            case TypeID.MUA_HANG_TRA_LAI:
                url = `/#/hang-mua/tra-lai/${refID}/view`;
                break;
            case TypeID.BAN_HANG_CHUA_THU_TIEN:
            case TypeID.BAN_HANG_THU_TIEN_NGAY_CK:
            case TypeID.BAN_HANG_THU_TIEN_NGAY_TM:
                url = `/#/chung-tu-ban-hang/${refID}/edit/from-ref`;
                break;
        }
        window.open(url, '_blank');
    }
}
