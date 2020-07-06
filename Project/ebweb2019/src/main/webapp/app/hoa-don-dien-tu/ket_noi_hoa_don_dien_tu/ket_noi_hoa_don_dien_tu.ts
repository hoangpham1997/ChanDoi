import { Component, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EInvoiceService } from 'app/hoa-don-dien-tu/danh_sach_hoa_don/danh-sach-hoa-don.service';
import { HttpResponse } from '@angular/common/http';
import { ISupplier } from 'app/shared/model/hoa-don-dien-tu/supplier';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { IConnectEInvoice } from 'app/shared/model/hoa-don-dien-tu/connect';
import { IResponeSds } from 'app/shared/model/hoa-don-dien-tu/respone-sds';
import { CategoryName, NCCDV_EINVOICE } from 'app/app.constants';
import { JhiEventManager } from 'ng-jhipster';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-connect-e-invoice',
    templateUrl: './ket_noi_hoa_don_dien_tu.html',
    styleUrls: ['./ket_noi_hoa_don_dien_tu.css']
})
export class ConnnectEInvoiceComponent implements OnInit {
    suppliers: ISupplier[];
    connectEInvoice: IConnectEInvoice;
    ROLE_KetNoiHD = ROLE.KNHD_KET_NOI_HD;
    NCCDV_EINVOICE = NCCDV_EINVOICE;

    constructor(
        public activeModal: NgbActiveModal,
        private principal: Principal,
        private eInvoiceService: EInvoiceService,
        private toastr: ToastrService,
        public translate: TranslateService,
        public eventManager: JhiEventManager
    ) {
        this.suppliers = [];
        this.connectEInvoice = {};
    }

    ngOnInit() {
        this.eInvoiceService.getConnect().subscribe((res: HttpResponse<IConnectEInvoice>) => {
            this.suppliers = res.body.suppliers.sort((a, b) => a.supplierServiceCode.localeCompare(b.supplierServiceCode));
            this.connectEInvoice = res.body;
            if (!this.connectEInvoice.supplierCode) {
                this.connectEInvoice.supplierCode = 'SDS';
            }
        });
    }

    connect() {
        if (this.checkErr()) {
            // if (this.connectEInvoice.supplierCode === 'SDS') {
            this.eInvoiceService.connect(this.connectEInvoice).subscribe((res: HttpResponse<IResponeSds>) => {
                if (res.body.status === 2) {
                    this.activeModal.dismiss('closed');
                    this.callEventAfterConnectSuccess();
                    this.toastr.success('Kết nối thành công', this.translate.instant('ebwebApp.mCReceipt.home.message'));
                } else {
                    this.toastr.error(
                        res.body.message ? res.body.message : 'Có lỗi xảy ra khi kết nối!',
                        this.translate.instant('ebwebApp.mCReceipt.error.error')
                    );
                }
            });
            /*} else {
                if (this.connectEInvoice.supplierCode) {
                    this.toastr.warning('Chưa phát triển!', this.translate.instant('ebwebApp.mCReceipt.error.error'));
                }
            }*/
        }
    }

    checkErr() {
        if (
            !this.connectEInvoice.supplierCode ||
            !this.connectEInvoice.path ||
            !this.connectEInvoice.userName ||
            !this.connectEInvoice.password
        ) {
            this.toastr.error('Chưa nhập đủ các trường bắt buộc', this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }
        return true;
    }

    callEventAfterConnectSuccess() {
        this.eventManager.broadcast({
            name: 'connectEInvocieSuccess',
            content: {}
        });
    }

    selectSupplier() {
        const sp: ISupplier = this.suppliers.find(n => n.supplierServiceCode === this.connectEInvoice.supplierCode);
        if (sp) {
            this.connectEInvoice.path = sp.pathAccess;
        } else {
            this.connectEInvoice.path = null;
        }
    }
}
