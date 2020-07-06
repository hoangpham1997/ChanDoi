import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EbAccountingObjectComboboxComponent } from 'app/shared/danh-muc-combobox/khach-hang-nha-cung-cap/khachHang-NhaCungCap.component';
import { EbEmployeeComboboxComponent } from 'app/shared/danh-muc-combobox/nhan-vien/nhan-vien.component';
import { EbBankAccountDetailComboboxComponent } from 'app/shared/danh-muc-combobox/bank-account-detail/bank-account-detail.component';
import { CategoryName } from 'app/app.constants';
import { EbStatisticsCodeComboboxComponent } from 'app/shared/danh-muc-combobox/ma-thong-ke/ma-thong-ke';
import { AutoPrincipleComboboxComponent } from 'app/shared/danh-muc-combobox/dinh-khoan-tu-dong/auto-principle-update.component';
import { EbEMContractComboboxComponent } from 'app/shared/danh-muc-combobox/hop-dong/hop-dong';
import { EbBudgetItemComboboxComponent } from 'app/shared/danh-muc-combobox/muc-thu-chi/muc-thu-chi.component';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { CreditCardComboboxComponent } from 'app/shared/danh-muc-combobox/the-tin-dung/credit-card-update.component';
import { MaterialGoodsCategoryComboboxComponent } from 'app/shared/danh-muc-combobox/loai-vat-tu-hang-hoa/material-goods-category-update.component';
import { MaterialGoodsComboboxComponent } from 'app/shared/danh-muc-combobox/vat-tu-hang-hoa/material-goods-update.component';
import { CurrencyComboboxComponent } from 'app/shared/danh-muc-combobox/loai-tien/currency-update.component';
import { CostSetComboboxComponent } from 'app/shared/danh-muc-combobox/doi-tuong-tap-hop-chi-phi/cost-set-update.component';
import { BankComboboxComponent } from 'app/shared/danh-muc-combobox/ngan-hang/bank-update.component';
import { MaterialGoodsSpecialTaxGroupCoboboxComponent } from 'app/shared/danh-muc-combobox/nhom-hhdv-chiu-thue-ttdb/material-goods-special-tax-group-update.component';
import { UnitComboboxComponent } from 'app/shared/danh-muc-combobox/unit/unit.component';
import { RepositoryComboboxComponent } from 'app/shared/danh-muc-combobox/repository/repository.component';
import { EbPhongBanComboboxComponent } from 'app/shared/danh-muc-combobox/phong-ban/phong-ban.component';
import { KhoanMucChiPhiListComboboxComponent } from 'app/shared/danh-muc-combobox/khoan-muc-chi-phi/khoan-muc-chi-phi-list-update.component';
import { EbTransportMethodComboboxComponent } from 'app/shared/danh-muc-combobox/phuong-thuc-van-chuyen/phuong-thuc-van-chuyen';

@Injectable({ providedIn: 'root' })
export class ComboboxModalService {
    public isOpen = false;

    constructor(private modalService: NgbModal) {}

    open(data, typeObject?, isCostSet?): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        let modalRef;
        if (data === CategoryName.DOI_TUONG) {
            modalRef = this.modalService.open(EbAccountingObjectComboboxComponent, {
                size: 'lg',
                windowClass: 'width-80 height-90',
                backdrop: 'static'
            });
            const acc: IAccountingObject = {};
            acc.objectType = 1;
            const type = 1;
            modalRef.componentInstance.type = type;
            modalRef.componentInstance.data = acc;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.KHACH_HANG) {
            modalRef = this.modalService.open(EbAccountingObjectComboboxComponent, {
                size: 'lg',
                windowClass: 'width-80 height-90',
                backdrop: 'static'
            });
            const acc: IAccountingObject = {};
            acc.objectType = 1;
            const type = 1;
            modalRef.componentInstance.data = acc;
            modalRef.componentInstance.type = type;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.NHA_CUNG_CAP) {
            modalRef = this.modalService.open(EbAccountingObjectComboboxComponent, {
                size: 'lg',
                windowClass: 'width-80 height-90',
                backdrop: 'static'
            });
            const acc: IAccountingObject = {};
            acc.objectType = 0;
            const type = 0;
            modalRef.componentInstance.data = acc;
            modalRef.componentInstance.type = type;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.NHAN_VIEN) {
            modalRef = this.modalService.open(EbEmployeeComboboxComponent, {
                size: 'lg',
                windowClass: 'width-80',
                backdrop: 'static'
            });
            if (typeObject) {
                modalRef.componentInstance.typeObject = typeObject;
            }
            modalRef.componentInstance.data = data;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.DON_VI_TINH) {
            modalRef = this.modalService.open(UnitComboboxComponent, {
                size: 'lg',
                windowClass: 'width-80',
                backdrop: 'static'
            });
            modalRef.componentInstance.data = data;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.KHO) {
            modalRef = this.modalService.open(RepositoryComboboxComponent, {
                size: 'lg',
                windowClass: 'width-80',
                backdrop: 'static'
            });
            modalRef.componentInstance.data = data;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.DINH_KHOAN_TU_DONG) {
            modalRef = this.modalService.open(AutoPrincipleComboboxComponent, {
                size: 'lg',
                windowClass: 'width-80',
                backdrop: 'static'
            });
            modalRef.componentInstance.data = data;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.TAI_KHOAN_NGAN_HANG) {
            modalRef = this.modalService.open(EbBankAccountDetailComboboxComponent, {
                size: 'lg',
                windowClass: 'height-30',
                backdrop: 'static'
            });
            modalRef.componentInstance.data = data;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.MA_THONG_KE) {
            modalRef = this.modalService.open(EbStatisticsCodeComboboxComponent, {
                size: 'lg',
                windowClass: 'height-30',
                backdrop: 'static'
            });
            modalRef.componentInstance.data = data;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.HOP_DONG) {
            modalRef = this.modalService.open(EbEMContractComboboxComponent, {
                size: 'lg',
                windowClass: 'height-30',
                backdrop: 'static'
            });
            modalRef.componentInstance.data = data;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.THE_TIN_DUNG) {
            modalRef = this.modalService.open(CreditCardComboboxComponent, {
                size: 'lg',
                windowClass: 'height-30',
                backdrop: 'static'
            });
            modalRef.componentInstance.data = data;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.KHOAN_MUC_CHI_PHI) {
            modalRef = this.modalService.open(KhoanMucChiPhiListComboboxComponent, {
                size: 'lg',
                windowClass: 'height-30',
                backdrop: 'static'
            });
            modalRef.componentInstance.data = data;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.LOAI_VAT_TU_HANG_HOA) {
            modalRef = this.modalService.open(MaterialGoodsCategoryComboboxComponent, {
                size: 'lg',
                windowClass: 'height-30',
                backdrop: 'static'
            });
            modalRef.componentInstance.data = data;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.VAT_TU_HANG_HOA) {
            modalRef = this.modalService.open(MaterialGoodsComboboxComponent, {
                size: 'lg',
                windowClass: 'height-30',
                backdrop: 'static'
            });
            modalRef.componentInstance.isCostSet = isCostSet;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.VAT_TU_HANG_HOA_PP_SERVICE) {
            modalRef = this.modalService.open(MaterialGoodsComboboxComponent, {
                size: 'lg',
                windowClass: 'height-30',
                backdrop: 'static'
            });
            modalRef.componentInstance.isPPService = true;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.LOAI_TIEN) {
            modalRef = this.modalService.open(CurrencyComboboxComponent, {
                size: 'lg',
                windowClass: 'height-30',
                backdrop: 'static'
            });
            modalRef.componentInstance.data = data;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.DOI_TUONG_TAP_HOP_CHI_PHI) {
            modalRef = this.modalService.open(CostSetComboboxComponent, {
                size: 'lg',
                windowClass: 'height-30',
                backdrop: 'static'
            });
            // modalRef.componentInstance.data = data;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.NGAN_HANG) {
            modalRef = this.modalService.open(BankComboboxComponent, {
                size: 'lg',
                windowClass: 'height-30',
                backdrop: 'static'
            });
            modalRef.componentInstance.data = data;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.NHOM_HHDV_CHIU_THUE_TTĐB) {
            modalRef = this.modalService.open(MaterialGoodsSpecialTaxGroupCoboboxComponent, {
                size: 'lg',
                windowClass: 'height-30',
                backdrop: 'static'
            });
            modalRef.componentInstance.data = data;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.MUC_THU_CHI) {
            modalRef = this.modalService.open(EbBudgetItemComboboxComponent, {
                size: 'lg',
                windowClass: 'height-30',
                backdrop: 'static'
            });
            modalRef.componentInstance.data = data;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.PHONG_BAN) {
            modalRef = this.modalService.open(EbPhongBanComboboxComponent, {
                size: 'lg',
                windowClass: 'height-30',
                backdrop: 'static'
            });
            modalRef.componentInstance.data = data;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        } else if (data === CategoryName.PHUONG_THUC_VAN_CHUYEN) {
            modalRef = this.modalService.open(EbTransportMethodComboboxComponent, {
                size: 'lg',
                windowClass: 'height-30',
                backdrop: 'static'
            });
            modalRef.componentInstance.data = data;
            modalRef.result.then(
                result => {
                    this.isOpen = false;
                },
                reason => {
                    this.isOpen = false;
                }
            );
        }
        return modalRef;
    }
}
