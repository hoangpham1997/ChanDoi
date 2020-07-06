-- [CheckConstraintDeleteAccount] '111','6F7C9527-04CB-BC48-B7D6-6C31FDCB0069'
CREATE proc [dbo].[CheckConstraintDeleteAccount] @AccountNumber NVARCHAR(25), @CompanyID UNIQUEIDENTIFIER
as
begin
    DECLARE @COUNT INT = 0
    --         SET @COUNT = @COUNT + (SELECT COUNT(*) FROM AccountTransfer WHERE (FromAccount = @AccountNumber OR ToAccount = @AccountNumber) AND CompanyID = @CompanyID)
--         SET @COUNT = @COUNT + (SELECT COUNT(*) FROM AccountDefault WHERE (FilterAccount = @AccountNumber OR DefaultAccount = @AccountNumber OR ReduceAccount = @AccountNumber) AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT +
                 (SELECT COUNT(*) FROM EbOrganizationUnit WHERE CostAccount = @AccountNumber AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MaterialGoods
                           WHERE (ReponsitoryAccount = @AccountNumber OR ExpenseAccount = @AccountNumber OR
                                  RevenueAccount = @AccountNumber)
                             AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT +
                 (SELECT COUNT(*) FROM Tools WHERE AllocationAwaitAccount = @AccountNumber AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM ToolsDetail a
                                    LEFT JOIN Tools b ON a.ToolsID = b.ID
                           WHERE CostAccount = @AccountNumber
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM FixedAsset
                           WHERE (DepreciationAccount = @AccountNumber OR OriginalPriceAccount = @AccountNumber OR
                                  ExpenditureAccount = @AccountNumber)
                             AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM FixedAssetAllocation a
                                    LEFT JOIN FixedAsset b ON a.FixedAssetID = b.ID
                           WHERE CostAccount = @AccountNumber
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT +
                 (SELECT COUNT(*) FROM Repository WHERE DefaultAccount = @AccountNumber AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM FixedAssetCategory
                           WHERE (OriginalPriceAccount = @AccountNumber OR DepreciationAccount = @AccountNumber OR
                                  ExpenditureAccount = @AccountNumber)
                             AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT +
                 (SELECT COUNT(*) FROM OPAccount WHERE AccountNumber = @AccountNumber AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT +
                 (SELECT COUNT(*) FROM OPMaterialGoods WHERE AccountNumber = @AccountNumber AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MCReceiptDetail a
                                    LEFT JOIN MCReceipt b ON a.MCReceiptID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber OR
                                  CashOutDifferAccountFB = @AccountNumber OR CashOutDifferAccountMB = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MCReceiptDetailTax a
                                    LEFT JOIN MCReceipt b ON a.MCReceiptID = b.ID
                           WHERE VATAccount = @AccountNumber
                             AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MCReceiptDetailCustomer a
                                    LEFT JOIN MCReceipt b ON a.MCReceiptID = b.ID
                           WHERE (CreditAccount = @AccountNumber OR DiscountAccount = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MCPaymentDetail a
                                    LEFT JOIN MCPayment b ON a.MCPaymentID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber OR
                                  CashOutDifferAccountFB = @AccountNumber OR CashOutDifferAccountMB = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MCPaymentDetailInsurance a
                                    LEFT JOIN MCPayment b ON a.MCPaymentID = b.ID
                           WHERE (CreditAccount = @AccountNumber OR CreditAccount = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MCPaymentDetailTax a
                                    LEFT JOIN MCPayment b ON a.MCPaymentID = b.ID
                           WHERE VATAccount = @AccountNumber
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MCPaymentDetailVendor a
                                    LEFT JOIN MCPayment b ON a.MCPaymentID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR DiscountAccount = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MBTellerPaperDetail a
                                    LEFT JOIN MBTellerPaper b ON a.MBTellerPaperID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber OR
                                  CashOutDifferAccountFB = @AccountNumber OR CashOutDifferAccountMB = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MBTellerPaperDetailInsurance a
                                    LEFT JOIN MBTellerPaper b ON a.MBTellerPaperID = b.ID
                           WHERE (CreditAccount = @AccountNumber OR CreditAccount = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MBTellerPaperDetailTax a
                                    LEFT JOIN MBTellerPaper b ON a.MBTellerPaperID = b.ID
                           WHERE VATAccount = @AccountNumber
                             AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MBTellerPaperDetailVendor a
                                    LEFT JOIN MBTellerPaper b ON a.MBTellerPaperID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR DiscountAccount = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MBCreditCardDetail a
                                    LEFT JOIN MBCreditCard b ON a.MBCreditCardID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber OR
                                  CashOutDifferAccountFB = @AccountNumber OR CashOutDifferAccountMB = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MBCreditCardDetailTax a
                                    LEFT JOIN MBCreditCard b ON a.MBCreditCardID = b.ID
                           WHERE VATAccount = @AccountNumber
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MBCreditCardDetailVendor a
                                    LEFT JOIN MBCreditCard b ON a.MBCreditCardID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR DiscountAccount = @AccountNumber)
                             AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MBDepositDetail a
                                    LEFT JOIN MBDeposit b ON a.MBDepositID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MBDepositDetailTax a
                                    LEFT JOIN MBDeposit b ON a.MBDepositID = b.ID
                           WHERE VATAccount = @AccountNumber
                             AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MBDepositDetailCustomer a
                                    LEFT JOIN MBDeposit b ON a.MBDepositID = b.ID
                           WHERE (CreditAccount = @AccountNumber OR DiscountAccount = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM MBInternalTransferDetail a
                                    LEFT JOIN MBInternalTransfer b ON a.MBInternalTransferID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber OR
                                  CashOutDifferAccountFB = @AccountNumber OR CashOutDifferAccountMB = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM PPInvoiceDetail a
                                    LEFT JOIN PPInvoice b ON a.PPInvoiceID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber OR
                                  VATAccount = @AccountNumber OR DeductionDebitAccount = @AccountNumber OR
                                  ImportTaxAccount = @AccountNumber OR SpecialConsumeTaxAccount = @AccountNumber OR
                                  CashOutDifferAccountMB = @AccountNumber OR CashOutDifferAccountFB = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM PPServiceDetail a
                                    LEFT JOIN PPService b ON a.PPServiceID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber OR
                                  VATAccount = @AccountNumber OR DeductionDebitAccount = @AccountNumber OR
                                  CashOutDifferAccountMB = @AccountNumber OR CashOutDifferAccountFB = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM PPDiscountReturnDetail a
                                    LEFT JOIN PPDiscountReturn b ON a.PPDiscountReturnID = b.ID
                           WHERE (CreditAccount = @AccountNumber OR DebitAccount = @AccountNumber OR
                                  VATAccount = @AccountNumber OR DeductionDebitAccount = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*) FROM ExceptVoucher WHERE Account = @AccountNumber AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM SAInvoiceDetail a
                                    LEFT JOIN SAInvoice b ON a.SAInvoiceID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber OR
                                  DiscountAccount = @AccountNumber OR VATAccount = @AccountNumber OR
                                  DeductionDebitAccount = @AccountNumber OR RepositoryAccount = @AccountNumber OR
                                  CostAccount = @AccountNumber OR ExportTaxAmountAccount = @AccountNumber OR
                                  ExportTaxAccountCorresponding = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM SAReturnDetail a
                                    LEFT JOIN SAReturn b ON a.SAReturnID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber OR
                                  VATAccount = @AccountNumber OR DeductionDebitAccount = @AccountNumber OR
                                  DiscountAccount = @AccountNumber OR CostAccount = @AccountNumber OR
                                  RepositoryAccount = @AccountNumber OR CashOutDifferAccountFB = @AccountNumber OR
                                  CashOutDifferAccountMB = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM RSInwardOutwardDetail a
                                    LEFT JOIN RSInwardOutward b ON a.RSInwardOutwardID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM RSTransferDetail a
                                    LEFT JOIN RSTransfer b ON a.RSTransferID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM TIIncrementDetail a
                                    LEFT JOIN TIIncrement b ON a.TIIncrementID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber OR
                                  VATAccount = @AccountNumber OR DeductionDebitAccount = @AccountNumber OR
                                  ImportTaxAccount = @AccountNumber OR SpecialConsumeTaxAccount = @AccountNumber OR
                                  CashOutDifferAccountMB = @AccountNumber OR CashOutDifferAccountFB = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM TITransferDetail a
                                    LEFT JOIN TITransfer b ON a.TITransferID = b.ID
                           WHERE CostAccount = @AccountNumber
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM TIAllocationAllocated a
                                    LEFT JOIN TIAllocation b ON a.TIAllocationID = b.ID
                           WHERE CostAccount = @AccountNumber
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM TIAllocationPost a
                                    LEFT JOIN TIAllocation b ON a.TIAllocationID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM FAInit
                           WHERE (OriginalPriceAccount = @AccountNumber OR DepreciationAccount = @AccountNumber OR
                                  ExpenditureAccount = @AccountNumber)
                             AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM FAInitDetail a
                                    LEFT JOIN FAInit b ON a.FAInitID = b.ID
                           WHERE CostAccount = @AccountNumber
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM FAIncrementDetail a
                                    LEFT JOIN FAIncrement b ON a.FAIncrementID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber OR
                                  VATAccount = @AccountNumber OR DeductionDebitAccount = @AccountNumber OR
                                  ImportTaxAccount = @AccountNumber OR SpecialConsumeTaxAccount = @AccountNumber OR
                                  CashOutDifferAccountFB = @AccountNumber OR CashOutDifferAccountMB = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM FADecrementDetail a
                                    LEFT JOIN FADecrement b ON a.FADecrementID = b.ID
                           WHERE (OriginalPriceAccount = @AccountNumber OR DepreciationAccount = @AccountNumber OR
                                  ExpenditureAccount = @AccountNumber OR RemainingAccount = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM FADecrementDetailPost a
                                    LEFT JOIN FADecrementDetail b ON a.FADecrementDetailID = b.ID
                                    LEFT JOIN FADecrement c ON b.FADecrementID = c.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber)
                             AND c.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM FAAdjustmentDetail a
                                    LEFT JOIN FAAdjustment b ON a.FAAdjustmentID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM FATransferDetail a
                                    LEFT JOIN FATransfer b ON a.FATransferID = b.ID
                           WHERE CostAccount = @AccountNumber
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM FADepreciatioAllocation a
                                    LEFT JOIN FADepreciation b ON a.FADepreciationID = b.ID
                           WHERE CostAccount = @AccountNumber
                             AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM FADepreciationPost a
                                    LEFT JOIN FADepreciation b ON a.FADepreciationID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT +
                 (SELECT COUNT(*) FROM CPOPN WHERE UncompletedAccount = @AccountNumber AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM CPAllocationGeneralExpense a
                                    LEFT JOIN CPPeriod b ON a.CPPeriodID = b.ID
                           WHERE a.AccountNumber = @AccountNumber
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM CPAllocationGeneralExpenseDetail a
                                    LEFT JOIN CPAllocationGeneralExpense b ON a.CPAllocationGeneralExpenseID = b.ID
                                    LEFT JOIN CPPeriod c ON b.CPPeriodID = c.ID
                           WHERE a.AccountNumber = @AccountNumber
                             AND c.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM CPAcceptanceDetail a
                                    LEFT JOIN CPAcceptance b ON a.CPAcceptanceID = b.ID
                                    LEFT JOIN CPPeriod c ON b.CPPeriod = c.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber)
                             AND c.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM CPExpenseTranferDetail a
                                    LEFT JOIN CPExpenseTranfer b ON a.CPExpenseTranferID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM GOtherVoucherDetail a
                                    LEFT JOIN GOtherVoucher b ON A.GOtherVoucherID = b.ID
                           WHERE (DebitAccount = @AccountNumber OR CreditAccount = @AccountNumber OR
                                  CashOutDifferAccountMB = @AccountNumber OR CashOutDifferAccountFB = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM GotherVoucherDetailDebtPayment a
                                    LEFT JOIN GOtherVoucher b ON a.GOtherVoucherID = b.ID
                           WHERE a.AccountNumber = @AccountNumber
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM GOtherVoucherDetailForeignCurrency a
                                    LEFT JOIN GOtherVoucher b ON a.GOtherVoucherID = b.ID
                           WHERE AccountNumber = @AccountNumber
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM GOtherVoucherDetailExpenseAllocation a
                                    LEFT JOIN GOtherVoucher b ON a.GOtherVoucherID = b.ID
                           WHERE CostAccount = @AccountNumber
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM GotherVoucherDetailTax a
                                    LEFT JOIN GOtherVoucher b ON a.GOtherVoucherID = b.ID
                           WHERE VATAccount = @AccountNumber
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM GvoucherListDetail a
                                    LEFT JOIN GvoucherList b ON a.GVoucherListID = b.ID
                           WHERE (VoucherDebitAccount = @AccountNumber OR VoucherCreditAccount = @AccountNumber)
                             AND b.CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM GeneralLedger
                           WHERE (Account = @AccountNumber OR AccountCorresponding = @AccountNumber)
                             AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM RepositoryLedger
                           WHERE (Account = @AccountNumber OR AccountCorresponding = @AccountNumber)
                             AND CompanyID = @CompanyID)
    SET @COUNT = @COUNT + (SELECT COUNT(*)
                           FROM FixedAssetLedger
                           WHERE (DepreciationAccount = @AccountNumber OR OriginalPriceAccount = @AccountNumber)
                             AND CompanyID = @CompanyID)
    SELECT @COUNT
end
go

