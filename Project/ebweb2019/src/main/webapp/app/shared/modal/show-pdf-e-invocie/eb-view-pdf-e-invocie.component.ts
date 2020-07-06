import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { DomSanitizer } from '@angular/platform-browser';
import { JhiEventManager } from 'ng-jhipster';
import { IEInvoice } from 'app/shared/model/hoa-don-dien-tu/e-invoice.model';
import { Router } from '@angular/router';
import { EInvoiceService } from 'app/hoa-don-dien-tu/danh_sach_hoa_don/danh-sach-hoa-don.service';
import { HttpResponse } from '@angular/common/http';
import { CheckHDCD, EI_IDNhaCungCapDichVu, EINVOICE, NCCDV_EINVOICE, SignType, TCKHAC_SDTichHopHDDT } from 'app/app.constants';
import { ROLE } from 'app/role.constants';
import { ResponeSds } from 'app/shared/model/hoa-don-dien-tu/respone-sds';
import { Principal } from 'app/core';

@Component({
    selector: 'eb-view-pdf-e-invocie',
    templateUrl: './eb-view-pdf-e-invocie.component.html'
})
export class EbViewPdfEInvocieComponent implements OnInit {
    TYPE_HANG_MUA_TRA_LAI = 220;
    TYPE_BAN_HANG_CHUA_THU_TIEN = 320;
    TYPE_BAN_HANG_THU_TIEN_NGAY_TM = 321;
    TYPE_BAN_HANG_THU_TIEN_NGAY_CK = 322;
    TYPE_HANG_BAN_TRA_LAI = 330;
    credentials: any;
    modalData: any;
    filePdf: any;
    typeID: any;
    data: IEInvoice;
    with: any;
    height: any;
    disable: boolean;
    ROLE = ROLE;
    currentAccount: any;
    NCCDV: string;
    NCCDV_EINVOICE = NCCDV_EINVOICE;

    constructor(
        private sanitizer: DomSanitizer,
        public activeModal: NgbActiveModal,
        private router: Router,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private eInvoiceService: EInvoiceService
    ) {
        this.credentials = {};
    }

    ngOnInit() {
        this.with = window.innerWidth;
        this.height = window.innerHeight;
        const blob = new Blob([this.modalData.body], { type: 'application/pdf' });
        const fileURL = URL.createObjectURL(this.modalData.body);
        this.filePdf = this.sanitizer.bypassSecurityTrustResourceUrl(fileURL);
        this.disable =
            this.data.statusInvoice === 5 ||
            this.data.statusInvoice === 0 ||
            this.data.statusInvoice === 3 ||
            this.data.statusInvoice === 4 ||
            this.data.statusInvoice === 7 ||
            this.data.statusInvoice === 8 ||
            this.data.statusInvoice === 6;
        this.principal.identity().then(account => {
            this.currentAccount = account;
            if (this.currentAccount) {
                this.NCCDV = this.currentAccount.systemOption.find(x => x.code === EI_IDNhaCungCapDichVu).data;
            }
        });
    }

    download() {
        this.eventManager.broadcast({
            name: `export-excel-${this.typeID}`
        });
    }

    viewVoucher(einvoice: IEInvoice) {
        let url = '';
        if (einvoice.statusInvoice === EINVOICE.HOA_DON_HUY || einvoice.statusInvoice === EINVOICE.HOA_DON_BI_THAY_THE) {
            if (einvoice.id) {
                url = `/#/xuat-hoa-don/${einvoice.id}/edit/from-ref`;
                window.open(url, '_blank');
            }
        } else if (einvoice.statusInvoice === 1 && einvoice.iDReplaceInv) {
            this.eInvoiceService.getInformationVoucherByID({ id: einvoice.iDReplaceInv }).subscribe((res: HttpResponse<any>) => {
                switch (res.body.typeID) {
                    case this.TYPE_BAN_HANG_CHUA_THU_TIEN:
                        url = `/#/chung-tu-ban-hang/${res.body.id}/edit/from-einvoice`;
                        window.open(url, '_blank');
                        break;
                    case this.TYPE_BAN_HANG_THU_TIEN_NGAY_CK:
                    case this.TYPE_BAN_HANG_THU_TIEN_NGAY_TM:
                        url = `/#/chung-tu-ban-hang/${res.body.id}/edit/from-einvoice`;
                        window.open(url, '_blank');
                        break;
                    case this.TYPE_HANG_MUA_TRA_LAI:
                        url = `/#/mua-hang/tra-lai/${res.body.id}/edit/from-einvoice`;
                        window.open(url, '_blank');
                        break;
                    case this.TYPE_HANG_BAN_TRA_LAI:
                        url = `/#/ban-hang/tra-lai/${res.body.id}/edit/from-einvoice`;
                        window.open(url, '_blank');
                        break;
                    default:
                        if (einvoice.id) {
                            url = `/#/xuat-hoa-don/${einvoice.id}/edit/from-ref`;
                            window.open(url, '_blank');
                        }
                        break;
                }
            });
        } else {
            this.eInvoiceService.getInformationVoucherByID({ id: einvoice.id }).subscribe((res: HttpResponse<any>) => {
                switch (res.body.typeID) {
                    case this.TYPE_BAN_HANG_CHUA_THU_TIEN:
                        url = `/#/chung-tu-ban-hang/${res.body.id}/edit/from-einvoice`;
                        window.open(url, '_blank');
                        break;
                    case this.TYPE_BAN_HANG_THU_TIEN_NGAY_CK:
                    case this.TYPE_BAN_HANG_THU_TIEN_NGAY_TM:
                        url = `/#/chung-tu-ban-hang/${res.body.id}/edit/from-einvoice`;
                        window.open(url, '_blank');
                        break;
                    case this.TYPE_HANG_MUA_TRA_LAI:
                        url = `/#/mua-hang/tra-lai/${res.body.id}/edit/from-einvoice`;
                        window.open(url, '_blank');
                        break;
                    case this.TYPE_HANG_BAN_TRA_LAI:
                        url = `/#/ban-hang/tra-lai/${res.body.id}/edit/from-einvoice`;
                        window.open(url, '_blank');
                        break;
                    default:
                        if (einvoice.id) {
                            url = `/#/xuat-hoa-don/${einvoice.id}/edit/from-ref`;
                            window.open(url, '_blank');
                        }
                        break;
                }
            });
        }
    }

    thayTheHoaDon() {
        this.xulyHoaDon(0);
    }

    dieuChinhThongTin() {
        this.xulyHoaDon(4);
    }

    dieuChinhGiam() {
        this.xulyHoaDon(3);
    }

    dieuChinhTang() {
        this.xulyHoaDon(2);
    }

    xulyHoaDon(type: number) {
        if (this.data.id) {
            const url = `/#/xuat-hoa-don/${this.data.id}/edit/invoice-processing/${type}`;
            window.open(url, '_blank');
        }
    }
}
