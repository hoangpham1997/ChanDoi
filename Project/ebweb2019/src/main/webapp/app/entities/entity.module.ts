import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';

import { EbwebUnitModule } from '../danhmuc/unit/unit.module';
import { EbwebBudgetItemModule } from './budget-item/budget-item.module';
import { EbwebOrganizationUnitModule } from '../danhmuc/organization-unit/organization-unit.module';
import { EbwebContractStateModule } from './contract-state/contract-state.module';
import { EbwebMaterialGoodsModule } from '../danhmuc/material-goods/material-goods.module';
import { EbwebMaterialGoodsResourceTaxGroupModule } from './material-goods-resource-tax-group/material-goods-resource-tax-group.module';
import { EbwebMaterialGoodsAssemblyModule } from './material-goods-assembly/material-goods-assembly.module';
import { EbwebMaterialGoodsSpecialTaxGroupModule } from '../danhmuc/material-goods-special-tax-group/material-goods-special-tax-group.module';

import { EbwebPSSalaryTaxInsuranceRegulationModule } from './ps-salary-tax-insurance-regulation/ps-salary-tax-insurance-regulation.module';
import { EbwebAccountingObjectModule } from '../danhmuc/accounting-object/accounting-object.module';
import { EbwebSalePriceGroupModule } from './sale-price-group/sale-price-group.module';
import { EbwebAccountingObjectGroupModule } from '../danhmuc/accounting-object-group/accounting-object-group.module';
import { EbwebTransportMethodModule } from './transport-method/transport-method.module';
import { EbwebPaymentClauseModule } from './payment-clause/payment-clause.module';
import { EbwebAccountingObjectBankAccountModule } from './accounting-object-bank-account/accounting-object-bank-account.module';
import { EbwebRepositoryModule } from '../danhmuc/repository/repository.module';
import { EbwebCreditCardModule } from '../danhmuc/credit-card/credit-card.module';
import { EbwebFixedAssetCategoryModule } from './fixed-asset-category/fixed-asset-category.module';
import { EbwebFixedAssetModule } from './fixed-asset/fixed-asset.module';
import { EbwebFixedAssetAllocationModule } from './fixed-asset-allocation/fixed-asset-allocation.module';
import { EbwebFixedAssetAccessoriesModule } from './fixed-asset-accessories/fixed-asset-accessories.module';
import { EbwebFixedAssetDetailsModule } from './fixed-asset-details/fixed-asset-details.module';
import { EbwebAutoPrincipleModule } from '../danhmuc/auto-principle/auto-principle.module';
import { EbwebAccountDefaultModule } from '../danhmuc/account-default/account-default.module';
import { EbwebAccountTransferModule } from '../danhmuc/account-transfer/account-transfer.module';

import { EbwebCurrencyModule } from '../danhmuc/currency/currency.module';
import { EbwebCostSetModule } from './cost-set/cost-set.module';
import { EbwebCostSetMaterialGoodModule } from './cost-set-material-good/cost-set-material-good.module';

import { EbwebPersonalSalaryTaxModule } from './personal-salary-tax/personal-salary-tax.module';
import { EbwebTimeSheetSymbolsModule } from './time-sheet-symbols/time-sheet-symbols.module';
import { EbwebExpenseItemModule } from './expense-item/expense-item.module';
import { EbwebMaterialQuantumModule } from '../danhmuc/dinh-muc-nguyen-vat-lieu/material-quantum/material-quantum.module';
import { EbwebMaterialQuantumDetailsModule } from '../danhmuc/dinh-muc-nguyen-vat-lieu/material-quantum-details/material-quantum-details.module';
import { EbwebStatisticsCodeModule } from './statistics-code/statistics-code.module';
import { EbwebMaterialGoodsPurchasePriceModule } from './material-goods-purchase-price/material-goods-purchase-price.module';
import { EbwebMaterialGoodsConvertUnitModule } from './material-goods-convert-unit/material-goods-convert-unit.module';
import { EbwebMaterialGoodsSpecificationsModule } from './material-goods-specifications/material-goods-specifications.module';
import { EbwebSaleDiscountPolicyModule } from './sale-discount-policy/sale-discount-policy.module';
import { EbwebWarrantyModule } from './warranty/warranty.module';
import { EbwebToolsModule } from './tools/tools.module';

import { EbwebMBDepositModule } from '../TienMatNganHang/BaoCo/mb-deposit/mb-deposit.module';
import { EbwebMBInternalTransferModule } from '../TienMatNganHang/mb-internal-transfer/mb-internal-transfer.module';
import { EbwebMCReceiptModule } from '../TienMatNganHang/phieu-thu/mc-receipt/mc-receipt.module';
import { EbwebMBInternalTransferDetailsModule } from '../TienMatNganHang/mb-internal-transfer-details/mb-internal-transfer-details.module';
import { EbwebMBInternalTransferTaxModule } from '../TienMatNganHang/mb-internal-transfer-tax/mb-internal-transfer-tax.module';
import { EbwebMCAuditModule } from '../TienMatNganHang/kiem_ke_quy/mc-audit.module';
import { EbwebMBCreditCardModule } from '../TienMatNganHang/TheTinDung/mb-credit-card/mb-credit-card.module';
import { EbwebGeneralLedgerModule } from './general-ledger/general-ledger.module';
import { EbwebMCPaymentModule } from '../TienMatNganHang/phieu-chi/mc-payment/mc-payment.module';
import { EbwebMBTellerPaperModule } from '../TienMatNganHang/BaoNo/mb-teller-paper/mb-teller-paper.module';
import { EbwebMBTellerPaperDetailsModule } from '../TienMatNganHang/BaoNo/mb-teller-paper-details/mb-teller-paper-details.module';
import { EbwebMBTellerPaperDetailTaxModule } from '../TienMatNganHang/BaoNo/mb-teller-paper-detail-tax/mb-teller-paper-detail-tax.module';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { EbwebMaterialGoodsCategoryModule } from 'app/danhmuc/material-goods-category/material-goods-category.module';
import { EbwebEMContractModule } from 'app/entities/em-contract/em-contract.module';
import { EbwebSystemOptionModule } from 'app/he-thong/system-option/system-option.module';
import { EbwebGenCodeModule } from './gen-code/gen-code.module';
import { EbwebGoodsServicePurchaseModule } from './goods-service-purchase/goods-service-purchase.module';
import { EbwebPPInvoiceDetailsModule } from './pp-invoice-details/pp-invoice-details.module';
import { EbwebPPDiscountReturnModule } from '../muahang/hang_mua_tra_lai_giam_gia/pp-discount-return/pp-discount-return.module';
import { EbwebPPDiscountReturnDetailsModule } from './pp-discount-return-details/pp-discount-return-details.module';
import { EbwebPporderModule } from './pporder/pporder.module';
import { EbwebPporderdetailModule } from './pporderdetail/pporderdetail.module';
import { EbwebNhanHoaDonModule } from 'app/muahang/nhan_hoa_don/nhan-hoa-don.module';
import { EbwebTypeModule } from './type/type.module';
import { EbwebSAOrderModule } from '../ban-hang/don_dat_hang/sa-order/sa-order.module';
import { EbwebGOtherVoucherModule } from '../tonghop/chung_tu_nghiep_vu_khac/g-other-voucher.module';
import { EbwebRepositoryLedgerModule } from './repository-ledger/repository-ledger.module';
import { EbwebCPUncompleteDetailsModule } from './cp-uncomplete-details/cp-uncomplete-details.module';
import { EbwebCPResultModule } from './cp-result/cp-result.module';
import { EbwebCPAllocationQuantumModule } from '../giathanh/dinh_muc_phan_bo_chi_phi/cp-allocation-quantum.module';
import { EbwebChiPhiTraTruocModule } from 'app/tonghop/chi_phi_tra_truoc/chi-phi-tra-truoc.module';
import { EbwebCPOPNModule } from '../giathanh/chi_phi_do_dang_dau_ky/cpopn.module';
import { EbwebPrepaidExpenseModule } from './prepaid-expense/prepaid-expense.module';
import { EbwebPrepaidExpenseAllocationModule } from './prepaid-expense-allocation/prepaid-expense-allocation.module';
import { EbwebPrepaidExpenseVoucherModule } from './prepaid-expense-voucher/prepaid-expense-voucher.module';
import { EbwebBankAccountDetailsModule } from 'app/danhmuc/bank-account-details/bank-account-details.module';
import { EbwebBankModule } from 'app/danhmuc/bank/bank.module';
import { EbwebPhanBoChiPhiTraTruocModule } from 'app/tonghop/phan_bo_chi_phi_tra_truoc/phan-bo-chi-phi-tra-truoc.module';
import { EbwebGOtherVoucherDetailExpenseModule } from './g-other-voucher-detail-expense/g-other-voucher-detail-expense.module';
import { EbwebGOtherVoucherDetailExpenseAllocationModule } from './g-other-voucher-detail-expense-allocation/g-other-voucher-detail-expense-allocation.module';
import { EbwebPhanBoCCDCModule } from 'app/congcudungcu/phan-bo-ccdc/phan-bo-ccdc.module';
import { EbwebGhiTangModule } from 'app/congcudungcu/ghi-tang-ccdc-khac/ghi-tang.module';

import { EbwebCPExpenseTranferModule } from '../giathanh/ket_chuyen_chi_phi/cp-expense-tranfer.module';
// import { EbwebTIAllocationModule } from './ti-allocation/ti-allocation.module';
// import { EbwebTIAllocationAllocatedModule } from './ti-allocation-allocated/ti-allocation-allocated.module';
// import { EbwebTIAllocationDetailsModule } from './ti-allocation-details/ti-allocation-details.module';
// import { EbwebTIAllocationPostModule } from './ti-allocation-post/ti-allocation-post.module';
// import { EbwebTIAdjustmentModule } from './ti-adjustment/ti-adjustment.module';
// import { EbwebTITransferDetailsModule } from './ti-transfer-details/ti-transfer-details.module';
// import { EbwebTIAdjustmentDetailsModule } from './ti-adjustment-details/ti-adjustment-details.module';
// import { EbwebTITransferModule } from './ti-transfer/ti-transfer.module';
// import { EbwebTIAuditModule } from './ti-audit/ti-audit.module';
// import { EbwebTIAuditDetailsModule } from './ti-audit-details/ti-audit-details.module';
// import { EbwebTIAuditMemberDetailsModule } from './ti-audit-member-details/ti-audit-member-details.module';
// import { EbwebTIDecrementModule } from './ti-decrement/ti-decrement.module';
// import { EbwebTIDecrementDetailsModule } from './ti-decrement-details/ti-decrement-details.module';
import { EbwebCPAllocationRateModule } from './cp-allocation-rate/cp-allocation-rate.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        EbwebUnitModule,
        EbwebBudgetItemModule,
        EbwebContractStateModule,
        EbwebMaterialGoodsResourceTaxGroupModule,
        EbwebMaterialGoodsSpecialTaxGroupModule,
        EbwebPSSalaryTaxInsuranceRegulationModule,
        EbwebSalePriceGroupModule,
        EbwebTransportMethodModule,
        EbwebBankModule,
        EbwebBankAccountDetailsModule,
        EbwebCreditCardModule,
        EbwebRepositoryModule,
        EbwebFixedAssetCategoryModule,
        EbwebFixedAssetModule,
        EbwebFixedAssetAllocationModule,
        EbwebFixedAssetAccessoriesModule,
        EbwebFixedAssetDetailsModule,
        EbwebAutoPrincipleModule,
        EbwebAccountDefaultModule,
        EbwebAccountTransferModule,
        EbwebCurrencyModule,
        EbwebCostSetModule,
        EbwebCostSetMaterialGoodModule,
        // tslint:disable-next-line:indent
        EbwebOrganizationUnitModule,
        EbwebMaterialGoodsModule,
        EbwebMaterialGoodsAssemblyModule,
        EbwebAccountingObjectModule,
        EbwebAccountingObjectGroupModule,
        EbwebPaymentClauseModule,
        EbwebAccountingObjectBankAccountModule,
        // tslint:disable-next-line:indent
        EbwebPersonalSalaryTaxModule,
        EbwebTimeSheetSymbolsModule,
        EbwebExpenseItemModule,
        EbwebMaterialQuantumModule,
        EbwebMaterialQuantumDetailsModule,
        EbwebStatisticsCodeModule,
        EbwebMaterialGoodsPurchasePriceModule,
        EbwebMaterialGoodsConvertUnitModule,
        EbwebMaterialGoodsSpecificationsModule,
        EbwebSaleDiscountPolicyModule,
        EbwebWarrantyModule,
        EbwebToolsModule,
        EbwebMBDepositModule,
        EbwebMBInternalTransferModule,
        EbwebMCAuditModule,
        EbwebMBInternalTransferDetailsModule,
        EbwebMBCreditCardModule,
        EbwebMBInternalTransferTaxModule,
        EbwebMCReceiptModule,
        EbwebGeneralLedgerModule,
        EbwebMCPaymentModule,
        EbwebMBTellerPaperModule,
        EbwebMBTellerPaperDetailsModule,
        EbwebMBTellerPaperDetailTaxModule,
        EbwebMaterialGoodsCategoryModule,
        EbwebEMContractModule,
        EbwebSystemOptionModule,
        EbwebGenCodeModule,
        EbwebGoodsServicePurchaseModule,
        EbwebPPInvoiceDetailsModule,
        EbwebPPDiscountReturnModule,
        EbwebPPDiscountReturnDetailsModule,
        EbwebPporderModule,
        EbwebPporderdetailModule,
        EbwebNhanHoaDonModule,
        EbwebTypeModule,
        EbwebSAOrderModule,
        EbwebGOtherVoucherModule,
        EbwebRepositoryLedgerModule,
        EbwebCPUncompleteDetailsModule,
        EbwebCPResultModule,
        EbwebCPAllocationQuantumModule,
        EbwebChiPhiTraTruocModule,
        EbwebCPOPNModule,
        EbwebPrepaidExpenseModule,
        EbwebPrepaidExpenseAllocationModule,
        EbwebPrepaidExpenseVoucherModule,
        EbwebPhanBoChiPhiTraTruocModule,
        EbwebGOtherVoucherDetailExpenseModule,
        EbwebGOtherVoucherDetailExpenseAllocationModule,
        EbwebCPExpenseTranferModule,
        EbwebGOtherVoucherDetailExpenseAllocationModule,
        EbwebPhanBoCCDCModule,
        // EbwebTIAllocationModule,
        // EbwebTIAllocationAllocatedModule,
        // EbwebTIAllocationDetailsModule,
        // EbwebTIAllocationPostModule,
        // EbwebTIAdjustmentModule,
        // EbwebTITransferDetailsModule,
        // EbwebTIAdjustmentDetailsModule,
        // EbwebTITransferModule,
        // EbwebTIAuditModule,
        // EbwebTIAuditDetailsModule,
        // EbwebTIAuditMemberDetailsModule,
        // EbwebTIDecrementModule,
        // EbwebTIDecrementDetailsModule,
        EbwebGhiTangModule,
        EbwebCPAllocationRateModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebEntityModule {}
