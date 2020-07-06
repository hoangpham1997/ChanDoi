import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { NgbActiveModal, NgbDateAdapter, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { EbLoginModalComponent, EbwebSharedCommonModule, EbwebSharedLibsModule, HasAnyAuthorityDirective } from './';
import { ComboBoxComponent } from 'app/combobox/combo-box.component';
import { ComboBoxPipe } from 'app/combobox/combo-box.pipe';
import { NgbDateFRParserFormatter } from 'app/shared/util/date-parser';
import { ToastrModule } from 'ngx-toastr';
import { EbRefModalComponent } from 'app/shared/modal/ref/ref.component';
import { EbContextMenuModule } from 'app/shared/context-menu/contex-menu.module';
import { CurrencyMaskDirective } from 'app/shared/directive/currency-input/currency.directive';
import { EbCurrencyPipe } from 'app/shared/directive/currency-input/currency.pipe';
import { NgxMaskModule } from 'ngx-mask';
import { DatePickerComponent } from 'app/shared/date-picker/date-picker.component';
import { ConfirmLeaveComponent } from 'app/shared/can-deactive-guard/confirm-leave.component';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { EbExportIncoiceModalComponent } from 'app/shared/export-invoice/export-invoice.component';
import { EbDiscountReturnModalComponent } from 'app/shared/discount-return/discount-return.component';
import { EbAccountingObjectComboboxComponent } from 'app/shared/danh-muc-combobox/khach-hang-nha-cung-cap/khachHang-NhaCungCap.component';
import { MaterialGoodsSpecialTaxGroupCoboboxComponent } from 'app/shared/danh-muc-combobox/nhom-hhdv-chiu-thue-ttdb/material-goods-special-tax-group-update.component';
import { EbEmployeeComboboxComponent } from 'app/shared/danh-muc-combobox/nhan-vien/nhan-vien.component';
import { CreditCardComboboxComponent } from 'app/shared/danh-muc-combobox/the-tin-dung/credit-card-update.component';
import { MaterialGoodsCategoryComboboxComponent } from 'app/shared/danh-muc-combobox/loai-vat-tu-hang-hoa/material-goods-category-update.component';
import { MaterialGoodsComboboxComponent } from 'app/shared/danh-muc-combobox/vat-tu-hang-hoa/material-goods-update.component';
import { CurrencyComboboxComponent } from 'app/shared/danh-muc-combobox/loai-tien/currency-update.component';
import { CostSetComboboxComponent } from 'app/shared/danh-muc-combobox/doi-tuong-tap-hop-chi-phi/cost-set-update.component';
import { BankComboboxComponent } from 'app/shared/danh-muc-combobox/ngan-hang/bank-update.component';
import { EbBankAccountDetailComboboxComponent } from 'app/shared/danh-muc-combobox/bank-account-detail/bank-account-detail.component';
import { RepositoryComboboxComponent } from 'app/shared/danh-muc-combobox/repository/repository.component';
import { UnitComboboxComponent } from 'app/shared/danh-muc-combobox/unit/unit.component';
import { PpOrderModalComponent } from 'app/shared/modal/pp-order/pp-order-modal.component';
import { ViewLiabilitiesComponent } from 'app/shared/modal/view-liabilities/view-liabilities.component';
import { DiscountAllocationModalComponent } from 'app/shared/modal/discount-allocation/discount-allocation-modal.component';
import { EbSaInvoiceModalComponent } from 'app/shared/modal/sa-invoice/sa-invoice.component';
import { CostAllocationModalComponent } from 'app/shared/modal/cost-allocation/cost-allocation-modal.component';
import { EbSaQuoteModalComponent } from 'app/shared/modal/sa-quote/sa-quote.component';
import { DialogDeleteComponent } from 'app/shared/modal/dialog-delete/dialog-delete.component';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { EbStatisticsCodeComboboxComponent } from 'app/shared/danh-muc-combobox/ma-thong-ke/ma-thong-ke';
import { EbEMContractComboboxComponent } from 'app/shared/danh-muc-combobox/hop-dong/hop-dong';
import { EbBudgetItemComboboxComponent } from 'app/shared/danh-muc-combobox/muc-thu-chi/muc-thu-chi.component';
import { CostVouchersModalComponent } from 'app/shared/modal/cost-vouchers/cost-vouchers-modal.component';
import { TreeViewComponent, TreeViewItemComponent } from 'app/shared/tree-combo-box/tree-combo-box.component';
import { CheckService } from 'app/shared/tree-combo-box/check-service';
import { AutoPrincipleComboboxComponent } from 'app/shared/danh-muc-combobox/dinh-khoan-tu-dong/auto-principle-update.component';
import { InputHintComponent } from 'app/input-hint/input-hint.component';
import { EbViewPdfEInvocieComponent } from 'app/shared/modal/show-pdf-e-invocie/eb-view-pdf-e-invocie.component';
import { ConnnectEInvoiceComponent } from 'app/hoa-don-dien-tu/ket_noi_hoa_don_dien_tu/ket_noi_hoa_don_dien_tu';
import { EbSaOrderModalComponent } from 'app/shared/modal/sa-order/sa-order.component';
import { SaReturnModalComponent } from 'app/shared/modal/sa-return/sa-return-modal.component';
import { UserModalComponent } from 'app/shared/modal/user/user.modal.component';
import { EbGroupModalComponent } from 'app/shared/modal/eb-group/eb-group-modal.component';
import { NumericDirective } from 'app/shared/numberic/numeric.directive';
import { EbPrepaidExpenseVoucherModalComponent } from 'app/shared/prepaid-expense-voucher/prepaid-expense-voucher.component';
import { EmContractModalComponent } from 'app/shared/modal/em-contract/em-contract-modal.component';
import { PpInvoiceModalComponent } from 'app/shared/modal/pp-invoice/pp-invoice-modal.component';
import { RsOutwardModalComponent } from 'app/shared/modal/rs-outward/rs-outward-modal.component';
import { RsTranfersModalComponent } from 'app/shared/modal/rs-tranfers/rs-tranfers-modal.component';
import { EbMaterialGoodsModalComponent } from './modal/material-goods/material-goods.component';
import { ColorPickerModule } from 'app/shared/color-picker/color-picker.module';
import { EbOrganizationUnitModalComponent } from 'app/shared/modal/eb-organization-unit/eb-organization-unit-modal.component';
import { PpDiscountReturnModalComponent } from 'app/shared/modal/pp-discount-return/pp-discount-return-modal.component';
import { MaterialQuantumModalComponent } from 'app/shared/modal/material-quantum/material-quantum-modal.component';

import { SignaturesPopupComponent } from 'app/hoa-don-dien-tu/danh_sach_hoa_don/signatures-popup.component';
import { TreeGridComponent, TreeGridItemComponent } from 'app/shared/tree-grid/tree-grid.component';
import { EbPrepaidExpenseCurrentBookModalComponent } from 'app/shared/prepaid-expense-current-book/prepaid-expense-current-book.component';
import { CalculateOWRepositoryComponent } from 'app/kho/tinh_gia_xuat_kho/tinh-gia-xuat-kho.component';
import { SaInvoiceOutwardModalComponent } from 'app/shared/modal/sa-invoice-outward/sa-invoice-outward-modal.component';
import { HasSomeAuthorityDirective } from 'app/shared/auth/has-some-authority.directive';
import { KhoaSoKyKeToanComponent } from 'app/tonghop/khoa-so-ky-ke-toan/khoa-so-ky-ke-toan.component';
import { XuLyChungTuComponent } from 'app/tonghop/khoa-so-ky-ke-toan/xu-ly-chung-tu.component';
import { BoKhoaSoKyKeToanComponent } from 'app/tonghop/bo-khoa-so-ky-ke-toan/bo-khoa-so-ky-ke-toan.component';
import { EbSelectMaterialGoodsModalComponent } from 'app/shared/modal/select-material-goods/select-material-goods.component';
import { HasSomeAuthorityAndConditionDirective } from 'app/shared/auth/has-some-authority-and-condition.directive';
import { HasSomeAuthorityOrConditionDirective } from 'app/shared/auth/has-some-authority-or-condition.directive';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { DeleteResultComponent } from 'app/shared/modal/delete-result/delete-result.component';
import { CongNoPhaiTraComponent } from 'app/bao-cao/mua-hang/cong-no-phai-tra/cong-no-phai-tra.component';
import { CheckTreeGridService } from 'app/shared/tree-grid/check-tree-grid-service';
import { ViewBaoCaoHddtComponent } from 'app/bao-cao/hoa-don-dien-tu/view/view-bao-cao-hddt.component';
import { PhanBoChiPhiTraTruocComponent } from 'app/bao-cao/tong-hop/phan-bo-chi-phi-tra-truoc/phan-bo-chi-phi-tra-truoc.component';
import { CongNoPhaiThuComponent } from 'app/bao-cao/ban-hang/cong-no-phai-thu/cong-no-phai-thu.component';
import { BaoCaoKetQuaComponent } from 'app/bao-cao/bao-cao-tai-chinh/bao-cao-ket-qua/bao-cao-ket-qua.component';
import { EbPhongBanComboboxComponent } from 'app/shared/danh-muc-combobox/phong-ban/phong-ban.component';
import { SoTienGuiNganHangComponent } from 'app/bao-cao/ngan-hang/so-tien-gui-ngan-hang/so-tien-gui-ngan-hang.component';
import { SoQuyTienMatComponent } from 'app/bao-cao/quy/so-quy-tien-mat/so-quy-tien-mat.component';
import { KhoanMucChiPhiListComboboxComponent } from 'app/shared/danh-muc-combobox/khoan-muc-chi-phi/khoan-muc-chi-phi-list-update.component';
import { SoNhatKyMuaHangComponent } from 'app/bao-cao/mua-hang/so-nhat-ky-mua-hang/so-nhat-ky-mua-hang.component';
import { SoChiTietMuaHangComponent } from 'app/bao-cao/mua-hang/so-chi-tiet-mua-hang/so-chi-tiet-mua-hang.component';
import { TheKhoComponent } from 'app/bao-cao/kho/the-kho/the-kho.component';
import { BankResultComponent } from 'app/shared/modal/bank/bank-result.component';
import { MaterialGoodsResultComponent } from 'app/shared/modal/material-goods-1/bank-result.component';
import { DeleteMultipleLinesComponent } from 'app/shared/modal/delete-multiple-lines/delete-multiple-lines.component';
import { PrepaidExpensesComponent } from 'app/shared/modal/prepaid-expenses/prepaid-expenses.component';
import { ErrorVouchersComponent } from 'app/shared/modal/error-vouchers/error-vouchers.component';
import { CheckPasswordModalComponent } from 'app/shared/modal/checkPassword/checkPassword.modal.component';
import { DeleteMultipleLinesUnitComponent } from 'app/shared/modal/delete-multiple-line-unit/delete-multiple-lines-unit.component';
import { DeleteMultipleLinesRepositoryComponent } from 'app/shared/modal/delete-multiple-line-repository/delete-multiple-lines-repository.component';
import { DeleteMultipleLinesBankAccountDetailsComponent } from 'app/shared/modal/delete-multiple-line-bank-account-details/delete-multiple-lines-bank-account-details.component';
import { ImportExcelDanhMucComponent } from 'app/shared/modal/import-excel-danh-muc/import-excel-danh-muc.component';
import { EbTransportMethodComboboxComponent } from 'app/shared/danh-muc-combobox/phuong-thuc-van-chuyen/phuong-thuc-van-chuyen';
import { EbMaterialGoodsSpecificationsModalComponent } from 'app/shared/modal/material-goods-specifications/material-goods-specifications.component';
import { TheTinhGiaThanhComponent } from 'app/bao-cao/gia-thanh/the-tinh-gia-thanh/the-tinh-gia-thanh.component';

@NgModule({
    imports: [EbwebSharedLibsModule, EbwebSharedCommonModule, ToastrModule.forRoot(), NgxMaskModule.forRoot(), EbContextMenuModule],
    declarations: [
        KhoanMucChiPhiListComboboxComponent,
        EbLoginModalComponent,
        EbRefModalComponent,
        BankResultComponent,
        MaterialGoodsResultComponent,
        SaInvoiceOutwardModalComponent,
        EbSelectMaterialGoodsModalComponent,
        EbSaInvoiceModalComponent,
        EbSaQuoteModalComponent,
        EbSaOrderModalComponent,
        EbMaterialGoodsModalComponent,
        PpOrderModalComponent,
        SaReturnModalComponent,
        EmContractModalComponent,
        PpInvoiceModalComponent,
        RsOutwardModalComponent,
        ImportExcelDanhMucComponent,
        RsTranfersModalComponent,
        DeleteMultipleLinesComponent,
        DeleteMultipleLinesUnitComponent,
        DeleteMultipleLinesRepositoryComponent,
        DialogDeleteComponent,
        ViewLiabilitiesComponent,
        DiscountAllocationModalComponent,
        EbAccountingObjectComboboxComponent,
        MaterialGoodsSpecialTaxGroupCoboboxComponent,
        EbEmployeeComboboxComponent,
        DeleteMultipleLinesBankAccountDetailsComponent,
        AutoPrincipleComboboxComponent,
        CreditCardComboboxComponent,
        EbPhongBanComboboxComponent,
        MaterialGoodsCategoryComboboxComponent,
        MaterialGoodsComboboxComponent,
        BankComboboxComponent,
        CurrencyComboboxComponent,
        CostSetComboboxComponent,
        RepositoryComboboxComponent,
        UnitComboboxComponent,
        EbBankAccountDetailComboboxComponent,
        EbEMContractComboboxComponent,
        EbStatisticsCodeComboboxComponent,
        EbBudgetItemComboboxComponent,
        HasAnyAuthorityDirective,
        HasSomeAuthorityDirective,
        HasSomeAuthorityAndConditionDirective,
        HasSomeAuthorityOrConditionDirective,
        NumericDirective,
        ComboBoxComponent,
        InputHintComponent,
        DatePickerComponent,
        TreeViewComponent,
        TreeViewItemComponent,
        ComboBoxPipe,
        EbCurrencyPipe,
        CheckPasswordModalComponent,
        EbExportIncoiceModalComponent,
        CurrencyMaskDirective,
        EbDiscountReturnModalComponent,
        ConfirmLeaveComponent,
        CostAllocationModalComponent,
        EbReportPdfPopupComponent,
        EbViewPdfEInvocieComponent,
        CostVouchersModalComponent,
        ConnnectEInvoiceComponent,
        UserModalComponent,
        EbGroupModalComponent,
        EbPrepaidExpenseVoucherModalComponent,
        EbPrepaidExpenseCurrentBookModalComponent,
        CalculateOWRepositoryComponent,
        EbOrganizationUnitModalComponent,
        PpDiscountReturnModalComponent,
        MaterialQuantumModalComponent,
        TreeGridComponent,
        TreeGridItemComponent,
        SignaturesPopupComponent,
        KhoaSoKyKeToanComponent,
        XuLyChungTuComponent,
        BoKhoaSoKyKeToanComponent,
        HandlingResultComponent,
        DeleteResultComponent,
        CongNoPhaiTraComponent,
        CongNoPhaiThuComponent,
        PhanBoChiPhiTraTruocComponent,
        ViewBaoCaoHddtComponent,
        BaoCaoKetQuaComponent,
        SoTienGuiNganHangComponent,
        SoQuyTienMatComponent,
        SoNhatKyMuaHangComponent,
        SoChiTietMuaHangComponent,
        TheKhoComponent,
        TheTinhGiaThanhComponent,
        PrepaidExpensesComponent,
        ErrorVouchersComponent,
        EbTransportMethodComboboxComponent,
        EbMaterialGoodsSpecificationsModalComponent
    ],
    providers: [
        { provide: NgbDateAdapter, useClass: NgbDateMomentAdapter },
        { provide: NgbDateParserFormatter, useClass: NgbDateFRParserFormatter },
        CheckService,
        CheckTreeGridService,
        CanDeactiveGuardService,
        NgbActiveModal
    ],
    entryComponents: [
        KhoanMucChiPhiListComboboxComponent,
        EbLoginModalComponent,
        EbRefModalComponent,
        CheckPasswordModalComponent,
        EbSaInvoiceModalComponent,
        EbSaQuoteModalComponent,
        EbSaOrderModalComponent,
        MaterialGoodsResultComponent,
        EbSelectMaterialGoodsModalComponent,
        SaInvoiceOutwardModalComponent,
        EbMaterialGoodsModalComponent,
        PpOrderModalComponent,
        SaReturnModalComponent,
        DeleteMultipleLinesComponent,
        EmContractModalComponent,
        BankResultComponent,
        PpInvoiceModalComponent,
        RsOutwardModalComponent,
        ImportExcelDanhMucComponent,
        DeleteMultipleLinesBankAccountDetailsComponent,
        RsTranfersModalComponent,
        DialogDeleteComponent,
        DeleteMultipleLinesRepositoryComponent,
        ViewLiabilitiesComponent,
        BankComboboxComponent,
        UnitComboboxComponent,
        CurrencyComboboxComponent,
        CostSetComboboxComponent,
        DiscountAllocationModalComponent,
        EbAccountingObjectComboboxComponent,
        MaterialGoodsSpecialTaxGroupCoboboxComponent,
        EbPhongBanComboboxComponent,
        MaterialGoodsCategoryComboboxComponent,
        EbEmployeeComboboxComponent,
        AutoPrincipleComboboxComponent,
        CreditCardComboboxComponent,
        MaterialGoodsComboboxComponent,
        RepositoryComboboxComponent,
        EbBankAccountDetailComboboxComponent,
        EbEMContractComboboxComponent,
        EbStatisticsCodeComboboxComponent,
        EbBudgetItemComboboxComponent,
        EbExportIncoiceModalComponent,
        EbDiscountReturnModalComponent,
        ConfirmLeaveComponent,
        CostAllocationModalComponent,
        EbReportPdfPopupComponent,
        EbViewPdfEInvocieComponent,
        CostVouchersModalComponent,
        ConnnectEInvoiceComponent,
        UserModalComponent,
        EbGroupModalComponent,
        EbPrepaidExpenseVoucherModalComponent,
        EbPrepaidExpenseCurrentBookModalComponent,
        CalculateOWRepositoryComponent,
        EbOrganizationUnitModalComponent,
        PpDiscountReturnModalComponent,
        MaterialQuantumModalComponent,
        TreeGridComponent,
        TreeGridItemComponent,
        SignaturesPopupComponent,
        KhoaSoKyKeToanComponent,
        XuLyChungTuComponent,
        BoKhoaSoKyKeToanComponent,
        HandlingResultComponent,
        DeleteResultComponent,
        CongNoPhaiTraComponent,
        CongNoPhaiThuComponent,
        PhanBoChiPhiTraTruocComponent,
        CongNoPhaiThuComponent,
        ViewBaoCaoHddtComponent,
        BaoCaoKetQuaComponent,
        SoTienGuiNganHangComponent,
        SoQuyTienMatComponent,
        SoNhatKyMuaHangComponent,
        SoChiTietMuaHangComponent,
        TheKhoComponent,
        TheTinhGiaThanhComponent,
        PrepaidExpensesComponent,
        ErrorVouchersComponent,
        DeleteMultipleLinesUnitComponent,
        EbTransportMethodComboboxComponent,
        EbMaterialGoodsSpecificationsModalComponent
    ],
    exports: [
        KhoanMucChiPhiListComboboxComponent,
        EbwebSharedCommonModule,
        EbLoginModalComponent,
        EbRefModalComponent,
        EbSaQuoteModalComponent,
        CheckPasswordModalComponent,
        BankResultComponent,
        EbSaOrderModalComponent,
        DeleteMultipleLinesRepositoryComponent,
        EbMaterialGoodsModalComponent,
        EbSelectMaterialGoodsModalComponent,
        SaInvoiceOutwardModalComponent,
        PpOrderModalComponent,
        MaterialGoodsResultComponent,
        DeleteMultipleLinesUnitComponent,
        DeleteMultipleLinesComponent,
        SaReturnModalComponent,
        EmContractModalComponent,
        PpInvoiceModalComponent,
        RsOutwardModalComponent,
        ImportExcelDanhMucComponent,
        RsTranfersModalComponent,
        DialogDeleteComponent,
        ViewLiabilitiesComponent,
        DiscountAllocationModalComponent,
        EbAccountingObjectComboboxComponent,
        MaterialGoodsSpecialTaxGroupCoboboxComponent,
        DeleteMultipleLinesBankAccountDetailsComponent,
        EbPhongBanComboboxComponent,
        MaterialGoodsCategoryComboboxComponent,
        EbEmployeeComboboxComponent,
        AutoPrincipleComboboxComponent,
        UnitComboboxComponent,
        BankComboboxComponent,
        CurrencyComboboxComponent,
        CostSetComboboxComponent,
        CreditCardComboboxComponent,
        MaterialGoodsComboboxComponent,
        EbBankAccountDetailComboboxComponent,
        EbEMContractComboboxComponent,
        EbStatisticsCodeComboboxComponent,
        EbBudgetItemComboboxComponent,
        EbExportIncoiceModalComponent,
        HasAnyAuthorityDirective,
        HasSomeAuthorityDirective,
        HasSomeAuthorityAndConditionDirective,
        HasSomeAuthorityOrConditionDirective,
        NumericDirective,
        ComboBoxComponent,
        InputHintComponent,
        DatePickerComponent,
        TreeViewComponent,
        TreeViewItemComponent,
        CurrencyMaskDirective,
        ComboBoxPipe,
        EbCurrencyPipe,
        EbContextMenuModule,
        EbDiscountReturnModalComponent,
        ConfirmLeaveComponent,
        CostAllocationModalComponent,
        EbReportPdfPopupComponent,
        EbViewPdfEInvocieComponent,
        CostVouchersModalComponent,
        ConnnectEInvoiceComponent,
        UserModalComponent,
        EbGroupModalComponent,
        EbPrepaidExpenseVoucherModalComponent,
        EbPrepaidExpenseCurrentBookModalComponent,
        CalculateOWRepositoryComponent,
        EbOrganizationUnitModalComponent,
        PpDiscountReturnModalComponent,
        MaterialQuantumModalComponent,
        TreeGridComponent,
        TreeGridItemComponent,
        ColorPickerModule,
        EbOrganizationUnitModalComponent,
        SignaturesPopupComponent,
        KhoaSoKyKeToanComponent,
        XuLyChungTuComponent,
        BoKhoaSoKyKeToanComponent,
        HandlingResultComponent,
        DeleteResultComponent,
        CongNoPhaiTraComponent,
        CongNoPhaiThuComponent,
        PhanBoChiPhiTraTruocComponent,
        ViewBaoCaoHddtComponent,
        BaoCaoKetQuaComponent,
        SoTienGuiNganHangComponent,
        SoQuyTienMatComponent,
        SoNhatKyMuaHangComponent,
        SoChiTietMuaHangComponent,
        TheKhoComponent,
        TheTinhGiaThanhComponent,
        PrepaidExpensesComponent,
        ErrorVouchersComponent,
        EbTransportMethodComboboxComponent,
        EbMaterialGoodsSpecificationsModalComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebSharedModule {}
