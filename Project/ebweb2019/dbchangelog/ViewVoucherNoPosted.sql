-- Author anmt
CREATE VIEW [dbo].[ViewVoucherNoPosted]
AS
    SELECT NEWID()          AS ID,
           dbo.MCPayment.ID AS RefID,
           dbo.MCPayment.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.MCPayment.CompanyID,
           dbo.MCPayment.BranchID,
           dbo.MCPayment.TypeLedger,
           dbo.MCPayment.NoMBook,
           dbo.MCPayment.NoFBook,
           dbo.MCPayment.Date,
           dbo.MCPayment.PostedDate,
           dbo.MCPayment.CurrencyID,
           dbo.MCPayment.Reason,
           dbo.MCPayment.AccountingObjectID,
           dbo.MCPayment.EmployeeID,
           dbo.MCPayment.Recorded,
           dbo.MCPayment.TotalAmount,
           dbo.MCPayment.TotalAmountOriginal,
           'MCPayment'      AS RefTable
    FROM dbo.MCPayment
             INNER JOIN
         dbo.Type ON dbo.MCPayment.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
    UNION ALL
    SELECT NEWID()          AS ID,
           dbo.MCReceipt.ID AS RefID,
           dbo.MCReceipt.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.MCReceipt.CompanyID,
           dbo.MCReceipt.BranchID,
           dbo.MCReceipt.TypeLedger,
           dbo.MCReceipt.NoMBook,
           dbo.MCReceipt.NoFBook,
           dbo.MCReceipt.Date,
           dbo.MCReceipt.PostedDate,
           dbo.MCReceipt.CurrencyID,
           dbo.MCReceipt.Reason,
           dbo.MCReceipt.AccountingObjectID,
           dbo.MCReceipt.EmployeeID,
           dbo.MCReceipt.Recorded,
           dbo.MCReceipt.TotalAmount,
           dbo.MCReceipt.TotalAmountOriginal,
           'MCReceipt'      AS RefTable
    FROM dbo.MCReceipt
             INNER JOIN
         dbo.Type ON dbo.MCReceipt.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()              AS ID,
           dbo.MBTellerPaper.ID AS RefID,
           dbo.MBTellerPaper.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.MBTellerPaper.CompanyID,
           dbo.MBTellerPaper.BranchID,
           dbo.MBTellerPaper.TypeLedger,
           dbo.MBTellerPaper.NoMBook,
           dbo.MBTellerPaper.NoFBook,
           dbo.MBTellerPaper.Date,
           dbo.MBTellerPaper.PostedDate,
           dbo.MBTellerPaper.CurrencyID,
           dbo.MBTellerPaper.Reason,
           dbo.MBTellerPaper.AccountingObjectID,
           dbo.MBTellerPaper.EmployeeID,
           dbo.MBTellerPaper.Recorded,
           dbo.MBTellerPaper.TotalAmount,
           dbo.MBTellerPaper.TotalAmountOriginal,
           'MBTellerPaper'      AS RefTable
    FROM dbo.MBTellerPaper
             INNER JOIN
         dbo.Type ON dbo.MBTellerPaper.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()             AS ID,
           dbo.MBCreditCard.ID AS RefID,
           dbo.MBCreditCard.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.MBCreditCard.CompanyID,
           dbo.MBCreditCard.BranchID,
           dbo.MBCreditCard.TypeLedger,
           dbo.MBCreditCard.NoMBook,
           dbo.MBCreditCard.NoFBook,
           dbo.MBCreditCard.Date,
           dbo.MBCreditCard.PostedDate,
           dbo.MBCreditCard.CurrencyID,
           dbo.MBCreditCard.Reason,
           dbo.MBCreditCard.AccountingObjectID,
           dbo.MBCreditCard.EmployeeID,
           dbo.MBCreditCard.Recorded,
           dbo.MBCreditCard.TotalAmount,
           dbo.MBCreditCard.TotalAmountOriginal,
           'MBCreditCard'      AS RefTable
    FROM dbo.MBCreditCard
             INNER JOIN
         dbo.Type ON dbo.MBCreditCard.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()          AS ID,
           dbo.MBDeposit.ID AS RefID,
           dbo.MBDeposit.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.MBDeposit.CompanyID,
           dbo.MBDeposit.BranchID,
           dbo.MBDeposit.TypeLedger,
           dbo.MBDeposit.NoMBook,
           dbo.MBDeposit.NoFBook,
           dbo.MBDeposit.Date,
           dbo.MBDeposit.PostedDate,
           dbo.MBDeposit.CurrencyID,
           dbo.MBDeposit.Reason,
           dbo.MBDeposit.AccountingObjectID,
           dbo.MBDeposit.EmployeeID,
           dbo.MBDeposit.Recorded,
           dbo.MBDeposit.TotalAmount,
           dbo.MBDeposit.TotalAmountOriginal,
           'MBDeposit'      AS RefTable
    FROM dbo.MBDeposit
             INNER JOIN
         dbo.Type ON dbo.MBDeposit.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()                           AS ID,
           dbo.MBInternalTransfer.ID         AS RefID,
           dbo.MBInternalTransfer.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.MBInternalTransfer.CompanyID,
           dbo.MBInternalTransfer.BranchID,
           dbo.MBInternalTransfer.TypeLedger,
           dbo.MBInternalTransfer.NoMBook,
           dbo.MBInternalTransfer.NoFBook,
           dbo.MBInternalTransfer.Date,
           dbo.MBInternalTransfer.PostedDate,
           dbo.EbOrganizationUnit.CurrencyID as CurrencyID,
           dbo.MBInternalTransfer.Reason,
           NULL                              as AccountingObjectID,
           dbo.MBInternalTransfer.EmployeeID,
           dbo.MBInternalTransfer.Recorded,
           dbo.MBInternalTransfer.TotalAmount,
           dbo.MBInternalTransfer.TotalAmountOriginal,
           'MBInternalTransfer'              AS RefTable
    FROM dbo.MBInternalTransfer
             INNER JOIN
         dbo.Type ON dbo.MBInternalTransfer.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
             INNER JOIN
         dbo.EbOrganizationUnit ON dbo.MBInternalTransfer.CompanyID = dbo.EbOrganizationUnit.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()          AS ID,
           dbo.PPInvoice.ID AS RefID,
           dbo.PPInvoice.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.PPInvoice.CompanyID,
           dbo.PPInvoice.BranchID,
           dbo.PPInvoice.TypeLedger,
           dbo.PPInvoice.NoMBook,
           dbo.PPInvoice.NoFBook,
           dbo.PPInvoice.Date,
           dbo.PPInvoice.PostedDate,
           dbo.PPInvoice.CurrencyID,
           dbo.PPInvoice.Reason,
           dbo.PPInvoice.AccountingObjectID,
           dbo.PPInvoice.EmployeeID,
           dbo.PPInvoice.Recorded,
           dbo.PPInvoice.TotalAmount,
           dbo.PPInvoice.TotalAmountOriginal,
           'PPInvoice'      AS RefTable
    FROM dbo.PPInvoice
             INNER JOIN
         dbo.Type ON dbo.PPInvoice.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()          AS ID,
           dbo.PPService.ID AS RefID,
           dbo.PPService.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.PPService.CompanyID,
           dbo.PPService.BranchID,
           dbo.PPService.TypeLedger,
           dbo.PPService.NoMBook,
           dbo.PPService.NoFBook,
           dbo.PPService.Date,
           dbo.PPService.PostedDate,
           dbo.PPService.CurrencyID,
           dbo.PPService.Reason,
           dbo.PPService.AccountingObjectID,
           dbo.PPService.EmployeeID,
           dbo.PPService.Recorded,
           dbo.PPService.TotalAmount,
           dbo.PPService.TotalAmountOriginal,
           'PPService'      AS RefTable
    FROM dbo.PPService
             INNER JOIN
         dbo.Type ON dbo.PPService.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()                 AS ID,
           dbo.PPDiscountReturn.ID AS RefID,
           dbo.PPDiscountReturn.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.PPDiscountReturn.CompanyID,
           dbo.PPDiscountReturn.BranchID,
           dbo.PPDiscountReturn.TypeLedger,
           dbo.PPDiscountReturn.NoMBook,
           dbo.PPDiscountReturn.NoFBook,
           dbo.PPDiscountReturn.Date,
           dbo.PPDiscountReturn.PostedDate,
           dbo.PPDiscountReturn.CurrencyID,
           dbo.PPDiscountReturn.Reason,
           dbo.PPDiscountReturn.AccountingObjectID,
           dbo.PPDiscountReturn.EmployeeID,
           dbo.PPDiscountReturn.Recorded,
           dbo.PPDiscountReturn.TotalAmount,
           dbo.PPDiscountReturn.TotalAmountOriginal,
           'PPDiscountReturn'      AS RefTable
    FROM dbo.PPDiscountReturn
             INNER JOIN
         dbo.Type ON dbo.PPDiscountReturn.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()          AS ID,
           dbo.SAInvoice.ID AS RefID,
           dbo.SAInvoice.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.SAInvoice.CompanyID,
           dbo.SAInvoice.BranchID,
           dbo.SAInvoice.TypeLedger,
           dbo.SAInvoice.NoMBook,
           dbo.SAInvoice.NoFBook,
           dbo.SAInvoice.Date,
           dbo.SAInvoice.PostedDate,
           dbo.SAInvoice.CurrencyID,
           dbo.SAInvoice.Reason,
           dbo.SAInvoice.AccountingObjectID,
           dbo.SAInvoice.EmployeeID,
           dbo.SAInvoice.Recorded,
           dbo.SAInvoice.TotalAmount,
           dbo.SAInvoice.TotalAmountOriginal,
           'SAInvoice'      AS RefTable
    FROM dbo.SAInvoice
             INNER JOIN
         dbo.Type ON dbo.SAInvoice.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()         AS ID,
           dbo.SAReturn.ID AS RefID,
           dbo.SAReturn.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.SAReturn.CompanyID,
           dbo.SAReturn.BranchID,
           dbo.SAReturn.TypeLedger,
           dbo.SAReturn.NoMBook,
           dbo.SAReturn.NoFBook,
           dbo.SAReturn.Date,
           dbo.SAReturn.PostedDate,
           dbo.SAReturn.CurrencyID,
           dbo.SAReturn.Reason,
           dbo.SAReturn.AccountingObjectID,
           dbo.SAReturn.EmployeeID,
           dbo.SAReturn.Recorded,
           dbo.SAReturn.TotalAmount,
           dbo.SAReturn.TotalAmountOriginal,
           'SAReturn'      AS RefTable
    FROM dbo.SAReturn
             INNER JOIN
         dbo.Type ON dbo.SAReturn.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()                AS ID,
           dbo.RSInwardOutward.ID AS RefID,
           dbo.RSInwardOutward.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.RSInwardOutward.CompanyID,
           dbo.RSInwardOutward.BranchID,
           dbo.RSInwardOutward.TypeLedger,
           dbo.RSInwardOutward.NoMBook,
           dbo.RSInwardOutward.NoFBook,
           dbo.RSInwardOutward.Date,
           dbo.RSInwardOutward.PostedDate,
           dbo.RSInwardOutward.CurrencyID,
           dbo.RSInwardOutward.Reason,
           dbo.RSInwardOutward.AccountingObjectID,
           dbo.RSInwardOutward.EmployeeID,
           dbo.RSInwardOutward.Recorded,
           dbo.RSInwardOutward.TotalAmount,
           dbo.RSInwardOutward.TotalAmountOriginal,
           'RSInwardOutward'      AS RefTable
    FROM dbo.RSInwardOutward
             INNER JOIN
         dbo.Type ON dbo.RSInwardOutward.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()                           AS ID,
           dbo.RSTransfer.ID                 AS RefID,
           dbo.RSTransfer.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.RSTransfer.CompanyID,
           dbo.RSTransfer.BranchID,
           dbo.RSTransfer.TypeLedger,
           dbo.RSTransfer.NoMBook,
           dbo.RSTransfer.NoFBook,
           dbo.RSTransfer.Date,
           dbo.RSTransfer.PostedDate,
           dbo.EbOrganizationUnit.CurrencyID as CurrencyID,
           dbo.RSTransfer.MobilizationOrderFor  Reason,
           dbo.RSTransfer.AccountingObjectID,
           NULL                              as EmployeeID,
           dbo.RSTransfer.Recorded,
           dbo.RSTransfer.TotalAmount,
           dbo.RSTransfer.TotalAmountOriginal,
           'RSTransfer'                      AS RefTable
    FROM dbo.RSTransfer
             INNER JOIN
         dbo.Type ON dbo.RSTransfer.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
             INNER JOIN
         dbo.EbOrganizationUnit ON dbo.RSTransfer.CompanyID = dbo.EbOrganizationUnit.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()            AS ID,
           dbo.TIIncrement.ID AS RefID,
           dbo.TIIncrement.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.TIIncrement.CompanyID,
           dbo.TIIncrement.BranchID,
           dbo.TIIncrement.TypeLedger,
           dbo.TIIncrement.NoMBook,
           dbo.TIIncrement.NoFBook,
           dbo.TIIncrement.Date,
           NULL as PostedDate,
           NULL as CurrencyID,
           dbo.TIIncrement.Reason,
           NULL as AccountingObjectID,
           NULL as EmployeeID,
           dbo.TIIncrement.Recorded,
           dbo.TIIncrement.TotalAmount,
           NULL as TotalAmountOriginal,
           'TIIncrement'      AS RefTable
    FROM dbo.TIIncrement
             INNER JOIN
         dbo.Type ON dbo.TIIncrement.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()                           AS ID,
           dbo.TIAllocation.ID               AS RefID,
           dbo.TIAllocation.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.TIAllocation.CompanyID,
           dbo.TIAllocation.BranchID,
           dbo.TIAllocation.TypeLedger,
           dbo.TIAllocation.NoMBook,
           dbo.TIAllocation.NoFBook,
           dbo.TIAllocation.Date,
           dbo.TIAllocation.PostedDate,
           dbo.EbOrganizationUnit.CurrencyID as CurrencyID,
           dbo.TIAllocation.Reason,
           NULL                              as AccountingObjectID,
           NULL                              as EmployeeID,
           dbo.TIAllocation.Recorded,
           dbo.TIAllocation.TotalAmount,
           dbo.TIAllocation.TotalAmount      as TotalAmountOriginal,
           'TIAllocation'                    AS RefTable
    FROM dbo.TIAllocation
             INNER JOIN
         dbo.Type ON dbo.TIAllocation.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
             INNER JOIN
         dbo.EbOrganizationUnit ON dbo.TIAllocation.CompanyID = dbo.EbOrganizationUnit.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()            AS ID,
           dbo.FAIncrement.ID AS RefID,
           dbo.FAIncrement.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.FAIncrement.CompanyID,
           dbo.FAIncrement.BranchID,
           dbo.FAIncrement.TypeLedger,
           dbo.FAIncrement.NoMBook,
           dbo.FAIncrement.NoFBook,
           dbo.FAIncrement.Date,
           NULL as PostedDate,
           NULL as CurrencyID,
           dbo.FAIncrement.Reason,
           NULL as AccountingObjectID,
           NULL as EmployeeID,
           dbo.FAIncrement.Recorded,
           dbo.FAIncrement.TotalAmount,
           NULL as TotalAmountOriginal,
           'FAIncrement'      AS RefTable
    FROM dbo.FAIncrement
             INNER JOIN
         dbo.Type ON dbo.FAIncrement.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()                           AS ID,
           dbo.FADecrement.ID                AS RefID,
           dbo.FADecrement.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.FADecrement.CompanyID,
           dbo.FADecrement.BranchID,
           dbo.FADecrement.TypeLedger,
           dbo.FADecrement.NoMBook,
           dbo.FADecrement.NoFBook,
           dbo.FADecrement.Date,
           dbo.FADecrement.PostedDate,
           dbo.EbOrganizationUnit.CurrencyID as CurrencyID,
           dbo.FADecrement.Reason,
           NULL                              as AccountingObjectID,
           NULL                              as EmployeeID,
           dbo.FADecrement.Recorded,
           dbo.FADecrement.TotalAmount,
           dbo.FADecrement.TotalAmount       as TotalAmountOriginal,
           'FADecrement'                     AS RefTable
    FROM dbo.FADecrement
             INNER JOIN
         dbo.Type ON dbo.FADecrement.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
             INNER JOIN
         dbo.EbOrganizationUnit ON dbo.FADecrement.CompanyID = dbo.EbOrganizationUnit.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()                           AS ID,
           dbo.FAAdjustment.ID               AS RefID,
           dbo.FAAdjustment.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.FAAdjustment.CompanyID,
           dbo.FAAdjustment.BranchID,
           dbo.FAAdjustment.TypeLedger,
           dbo.FAAdjustment.NoMBook,
           dbo.FAAdjustment.NoFBook,
           dbo.FAAdjustment.Date,
           dbo.FAAdjustment.PostedDate,
           dbo.EbOrganizationUnit.CurrencyID as CurrencyID,
           dbo.FAAdjustment.Reason,
           NULL                              as AccountingObjectID,
           NULL                              as EmployeeID,
           dbo.FAAdjustment.Recorded,
           0                                 as TotalAmount,
           0                                 as TotalAmountOriginal,
           'FAAdjustment'                    AS RefTable
    FROM dbo.FAAdjustment
             INNER JOIN
         dbo.Type ON dbo.FAAdjustment.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
             INNER JOIN
         dbo.EbOrganizationUnit ON dbo.FAAdjustment.CompanyID = dbo.EbOrganizationUnit.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()                           AS ID,
           dbo.FADepreciation.ID             AS RefID,
           dbo.FADepreciation.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.FADepreciation.CompanyID,
           dbo.FADepreciation.BranchID,
           dbo.FADepreciation.TypeLedger,
           dbo.FADepreciation.NoMBook,
           dbo.FADepreciation.NoFBook,
           dbo.FADepreciation.Date,
           dbo.FADepreciation.PostedDate,
           dbo.EbOrganizationUnit.CurrencyID as CurrencyID,
           dbo.FADepreciation.Reason,
           NULL                              as AccountingObjectID,
           NULL                              as EmployeeID,
           dbo.FADepreciation.Recorded,
           dbo.FADepreciation.TotalAmount,
           dbo.FADepreciation.TotalAmountOriginal,
           'FADepreciation'                  AS RefTable
    FROM dbo.FADepreciation
             INNER JOIN
         dbo.Type ON dbo.FADepreciation.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
             INNER JOIN
         dbo.EbOrganizationUnit ON dbo.FADepreciation.CompanyID = dbo.EbOrganizationUnit.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()              AS ID,
           dbo.GOtherVoucher.ID AS RefID,
           dbo.GOtherVoucher.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.GOtherVoucher.CompanyID,
           dbo.GOtherVoucher.BranchID,
           dbo.GOtherVoucher.TypeLedger,
           dbo.GOtherVoucher.NoMBook,
           dbo.GOtherVoucher.NoFBook,
           dbo.GOtherVoucher.Date,
           dbo.GOtherVoucher.PostedDate,
           dbo.GOtherVoucher.CurrencyID,
           dbo.GOtherVoucher.Reason,
           NULL                 as AccountingObjectID,
           NULL                 as EmployeeID,
           dbo.GOtherVoucher.Recorded,
           dbo.GOtherVoucher.TotalAmount,
           dbo.GOtherVoucher.TotalAmountOriginal,
           'GOtherVoucher'      AS RefTable
    FROM dbo.GOtherVoucher
             INNER JOIN
         dbo.Type ON dbo.GOtherVoucher.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
/* =======================================================================*/
    UNION ALL
    SELECT NEWID()                      AS   ID,
           dbo.CPExpenseTranfer.ID          AS   RefID,
           dbo.CPExpenseTranfer.TypeID,
           dbo.Type.TypeGroupID,
           TypeGroupName,
           dbo.CPExpenseTranfer.CompanyID,
           dbo.CPExpenseTranfer.BranchID,
           dbo.CPExpenseTranfer.TypeLedger,
           dbo.CPExpenseTranfer.NoMBook,
           dbo.CPExpenseTranfer.NoFBook,
           dbo.CPExpenseTranfer.Date,
           dbo.CPExpenseTranfer.PostedDate,
           dbo.EbOrganizationUnit.CurrencyID CurrencyID,
           dbo.CPExpenseTranfer.Reason,
           NULL                         as   AccountingObjectID,
           NULL                         as   EmployeeID,
           dbo.CPExpenseTranfer.Recorded,
           dbo.CPExpenseTranfer.TotalAmount,
           dbo.CPExpenseTranfer.TotalAmountOriginal,
           'CPExpenseTranfer'               AS   RefTable
    FROM dbo.CPExpenseTranfer
             INNER JOIN
         dbo.Type ON dbo.CPExpenseTranfer.TypeID = dbo.Type.ID
             left join TypeGroup on TypeGroupID = TypeGroup.ID
             INNER JOIN
         dbo.EbOrganizationUnit ON dbo.CPExpenseTranfer.CompanyID = dbo.EbOrganizationUnit.ID
go

