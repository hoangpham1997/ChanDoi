package vn.softdreams.ebweb.config;

import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.softdreams.ebweb.domain.*;

import java.time.Duration;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = {  DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(vn.softdreams.ebweb.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(EbAuthority.class.getName(), jcacheConfiguration);
            cm.createCache(EbGroup.class.getName(), jcacheConfiguration);
            cm.createCache(EbGroupAuth.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.Unit.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.BudgetItem.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.OrganizationUnit.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.ContractState.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.MaterialGoods.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.MaterialGoodsResourceTaxGroup.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.MaterialGoodsAssembly.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.MaterialGoodsSpecialTaxGroup.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.Bank.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.PSSalaryTaxInsuranceRegulation.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.BankAccountDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.SalePriceGroup.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CreditCard.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TransportMethod.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.Repository.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.FixedAssetCategory.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.FixedAsset.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.FixedAssetAllocation.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.FixedAssetAccessories.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.FixedAssetDetails.class.getName(), jcacheConfiguration);

            cm.createCache(vn.softdreams.ebweb.domain.AccountingObjectGroup.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.PaymentClause.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.AccountingObjectBankAccount.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.AccountingObject.class.getName(), jcacheConfiguration);

            cm.createCache(vn.softdreams.ebweb.domain.AutoPrinciple.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.AccountDefault.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.AccountTransfer.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.AccountGroup.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.AccountList.class.getName(), jcacheConfiguration);
			cm.createCache(vn.softdreams.ebweb.domain.Currency.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CostSet.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CostSetMaterialGood.class.getName(), jcacheConfiguration);
			cm.createCache(vn.softdreams.ebweb.domain.PersonalSalaryTax.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TimeSheetSymbols.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.ExpenseItem.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.MaterialQuantum.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.MaterialQuantumDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.StatisticsCode.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.MaterialGoodsPurchasePrice.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.MaterialGoodsConvertUnit.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.MaterialGoodsSpecifications.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.SaleDiscountPolicy.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.Warranty.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.Tools.class.getName(), jcacheConfiguration);
            cm.createCache(MBDepositDetails.class.getName(), jcacheConfiguration);
            cm.createCache(MBDeposit.class.getName(), jcacheConfiguration);
            cm.createCache(MBInternalTransfer.class.getName(), jcacheConfiguration);
            cm.createCache(MBInternalTransferDetails.class.getName(), jcacheConfiguration);
            cm.createCache(MBInternalTransferTax.class.getName(), jcacheConfiguration);
            cm.createCache(MBInternalTransfer.class.getName() + ".mBInternalTransferDetails", jcacheConfiguration);
            cm.createCache(MBInternalTransfer.class.getName() + ".mBInternalTransferTaxes", jcacheConfiguration);
            cm.createCache(MCAudit.class.getName(), jcacheConfiguration);
            cm.createCache(MCAudit.class.getName() + ".mcAuditDetailMembers", jcacheConfiguration);
            cm.createCache(MCAudit.class.getName() + ".mcAuditDetails", jcacheConfiguration);
            cm.createCache(MBCreditCard.class.getName(), jcacheConfiguration);
            cm.createCache(MBCreditCardDetails.class.getName(), jcacheConfiguration);
            cm.createCache(MBCreditCardDetailTax.class.getName(), jcacheConfiguration);
            cm.createCache(MBCreditCardDetailVendor.class.getName(), jcacheConfiguration);
			cm.createCache(MCReceipt.class.getName(), jcacheConfiguration);
            cm.createCache(MCReceipt.class.getName() + ".mCReceiptDetails", jcacheConfiguration);
            cm.createCache(MCReceiptDetails.class.getName(), jcacheConfiguration);
            cm.createCache(MCReceipt.class.getName() + ".mCReceiptDetailCustomers", jcacheConfiguration);
            cm.createCache(MCReceipt.class.getName() + ".mCReceiptDetailTaxes", jcacheConfiguration);
            cm.createCache(MCReceiptDetailCustomer.class.getName(), jcacheConfiguration);
            cm.createCache(MCReceiptDetailTax.class.getName(), jcacheConfiguration);
            cm.createCache(MCAuditDetails.class.getName(), jcacheConfiguration);
            cm.createCache(MCAuditDetailMember.class.getName(), jcacheConfiguration);

            cm.createCache(vn.softdreams.ebweb.domain.GeneralLedger.class.getName(), jcacheConfiguration);
            cm.createCache(MBTellerPaper.class.getName(), jcacheConfiguration);
            cm.createCache(MBTellerPaperDetails.class.getName(), jcacheConfiguration);
            cm.createCache(MBTellerPaperDetailTax.class.getName(), jcacheConfiguration);
            cm.createCache(MBDepositDetailCustomer.class.getName(), jcacheConfiguration);
            cm.createCache(MBDepositDetailTax.class.getName(), jcacheConfiguration);
            cm.createCache(MCPayment.class.getName(), jcacheConfiguration);
            cm.createCache(MCPayment.class.getName() + ".mCPaymentDetails", jcacheConfiguration);
            cm.createCache(MCPayment.class.getName() + ".mCPaymentDetailInsurances", jcacheConfiguration);
            cm.createCache(MCPayment.class.getName() + ".mCPaymentDetailSalaries", jcacheConfiguration);
            cm.createCache(MCPayment.class.getName() + ".mCPaymentDetailTaxes", jcacheConfiguration);
            cm.createCache(MCPayment.class.getName() + ".mCPaymentDetailVendors", jcacheConfiguration);
            cm.createCache(MCPaymentDetails.class.getName(), jcacheConfiguration);
            cm.createCache(MCPaymentDetailTax.class.getName(), jcacheConfiguration);
            cm.createCache(MCPaymentDetailInsurance.class.getName(), jcacheConfiguration);
            cm.createCache(MCPaymentDetailVendor.class.getName(), jcacheConfiguration);
            cm.createCache(MCPaymentDetailSalary.class.getName(), jcacheConfiguration);
            cm.createCache(MBCreditCard.class.getName() + ".mBCreditCardDetails", jcacheConfiguration);
            cm.createCache(MBCreditCard.class.getName() + ".mBCreditCardDetailTaxes", jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.MaterialGoodsCategory.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.EMContract.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.SystemOption.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.GenCode.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.OrganizationUnitOptionReport.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.GoodsServicePurchase.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.PPInvoice.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.PPInvoiceDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.RSInwardOutward.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.PPDiscountReturn.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.PPDiscountReturnDetails.class.getName(), jcacheConfiguration);
            cm.createCache(PPOrder.class.getName(), jcacheConfiguration);
            cm.createCache(PPOrderDetail.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.SaBill.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.SaBillDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.SaBill.class.getName() + ".saBillDetails", jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.Template.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.InvoiceType.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.Type.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.SAOrder.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.SAOrderDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.IaPublishInvoice.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.IaPublishInvoiceDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.PPService.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.PPServiceDetail.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.SAQuote.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.SAQuoteDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.ViewVoucher.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.RefVoucher.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TypeGroup.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.SaReturn.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.SaReturn.class.getName() + ".saReturnDetails", jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.SaReturnDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.GOtherVoucher.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.GOtherVoucherDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.GOtherVoucherDetailTax.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.SAInvoice.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.SAInvoice.class.getName() + ".saInvoiceDetails", jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.SAInvoiceDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.RepositoryLedger.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.RSInwardOutWardDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CPProductQuantum.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CPPeriod.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CPAllocationGeneralExpense.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CPAllocationGeneralExpenseDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CPUncompleteDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CPResult.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CPExpenseList.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CPPeriodDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CPAllocationQuantum.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CPOPN.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.PPInvoiceDetailCost.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.PrepaidExpense.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.PrepaidExpenseAllocation.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.PrepaidExpenseVoucher.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.Supplier.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CareerGroup.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.IAReport.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.IAInvoiceTemplate.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.GOtherVoucherDetailExpense.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.GOtherVoucherDetailExpenseAllocation.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.IARegisterInvoiceDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.IARegisterInvoice.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.MBTellerPaperDetailVendor.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.EbPackage.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.BackupData.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CPUncomplete.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CPExpenseTranfer.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CPExpenseTranferDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TIAllocation.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TIAllocationAllocated.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TIAllocationDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TIAllocationPost.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TIAdjustment.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TITransferDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TIAdjustmentDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TITransfer.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TIAudit.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TIAuditDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TIAuditMemberDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TIDecrement.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TIDecrementDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TIIncrement.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TIIncrementDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.TIIncrementDetailRefVoucher.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.Toolledger.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.FixedAssetLedger.class.getName(), jcacheConfiguration);
            cm.createCache(FAIncrement.class.getName(), jcacheConfiguration);
            cm.createCache(FAIncrement.class.getName() + ".fAIncrementDetails", jcacheConfiguration);
            cm.createCache(FAIncrement.class.getName() + ".fAIncrementDetailRefVoucher", jcacheConfiguration);
            cm.createCache(FAIncrementDetailRefVoucher.class.getName(), jcacheConfiguration);
            cm.createCache(FAIncrementDetails.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CPAllocationRate.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CPAcceptance.class.getName(), jcacheConfiguration);
            cm.createCache(vn.softdreams.ebweb.domain.CPAcceptanceDetails.class.getName(), jcacheConfiguration);
            cm.createCache(FAAdjustment.class.getName(), jcacheConfiguration);
            cm.createCache(FAAdjustment.class.getName() + ".FAAdjustmentDetails", jcacheConfiguration);
            cm.createCache(FAAdjustment.class.getName() + ".FAAdjustmentMemberDetail", jcacheConfiguration);
            cm.createCache(FAAdjustment.class.getName() + ".FAAdjustmentDetailPost", jcacheConfiguration);
            cm.createCache(FAAdjustmentDetailPost.class.getName(), jcacheConfiguration);
            cm.createCache(FAAdjustmentDetails.class.getName(), jcacheConfiguration);
            cm.createCache(FAAdjustmentMemberDetail.class.getName(), jcacheConfiguration);
            cm.createCache(FAAudit.class.getName(), jcacheConfiguration);
            cm.createCache(FAAudit.class.getName() + ".FAAuditDetails", jcacheConfiguration);
            cm.createCache(FAAudit.class.getName() + ".FAAuditMemberDetail", jcacheConfiguration);
            cm.createCache(FAAuditDetails.class.getName(), jcacheConfiguration);
            cm.createCache(FAAuditMemberDetail.class.getName(), jcacheConfiguration);
            cm.createCache(FaDecrement.class.getName(), jcacheConfiguration);
            cm.createCache(FaDecrement.class.getName() + ".FADecrementDetails", jcacheConfiguration);
            cm.createCache(FaDecrementDetails.class.getName(), jcacheConfiguration);
            cm.createCache(FaDecrementDetails.class.getName() + ".FADecrementDetailPost", jcacheConfiguration);
            cm.createCache(FADecrementDetailPost.class.getName(), jcacheConfiguration);
            cm.createCache(FADepreciation.class.getName(), jcacheConfiguration);
            cm.createCache(FADepreciation.class.getName() + ".FADepreciationDetail", jcacheConfiguration);
            cm.createCache(FADepreciation.class.getName() + ".FADepreciationAllocation", jcacheConfiguration);
            cm.createCache(FADepreciation.class.getName() + ".FADepreciationPost", jcacheConfiguration);
            cm.createCache(FADepreciationAllocation.class.getName(), jcacheConfiguration);
            cm.createCache(FADepreciationDetail.class.getName(), jcacheConfiguration);
            cm.createCache(FADepreciationPost.class.getName(), jcacheConfiguration);
            cm.createCache(FaTransfer.class.getName(), jcacheConfiguration);
            cm.createCache(FaTransfer.class.getName() + ".FATransferDetail", jcacheConfiguration);
            cm.createCache(FATransferDetail.class.getName(), jcacheConfiguration);

            cm.createCache(vn.softdreams.ebweb.domain.MaterialGoodsSpecificationsLedger.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
