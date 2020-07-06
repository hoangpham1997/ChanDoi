-- Author Hautv
    CREATE VIEW [dbo].[ViewVoucherNoForCloseBook]
    AS
        select temp.ID,
               temp.RefID,
               temp.TypeID,
               temp.TypeGroupID,
               temp.TypeGroupName,
               temp.CompanyID,
               temp.BranchID,
               temp.TypeLedger,
               temp.NoMBook,
               temp.NoFBook,
               temp.Date,
               temp.PostedDate,
               temp.InvoiceSeries,
               temp.InvoiceDate,
               temp.InvoiceNo,
               temp.BankAccountDetailID,
               temp.BankAccount,
               temp.BankName,
               temp.CurrencyID,
               temp.ExchangeRate,
               temp.Reason,
               temp.Description,
               temp.AccountingObjectID,
               temp.AccountingObjectCode,
               temp.AccountingObjectName,
               temp.AccountingObjectAddress,
               temp.ContactName,
               temp.EmployeeID,
               temp.EmployeeCode,
               temp.EmployeeName,
               temp.MCReceiptID,
               temp.MBDepositID,
               temp.PaymentVoucherID,
               temp.RSInwardOutwardID,
               temp.Recorded,
               temp.TotalAmount,
               temp.TotalAmountOriginal,
               temp.Exported,
               temp.IsDeliveryVoucher,
               temp.RefDateTime,
               temp.storedInRepository,
               temp.IsImportPurchase,
               temp.InvoiceForm,
               temp.RefTable
        from (SELECT *, row_number() over (partition by b.RefID order by b.NoFBook desc) as roworder
              from (SELECT NEWID()                AS ID,
                           a.ID                   AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           a.PostedDate,
                           NULL                   as InvoiceSeries,
                           NULL                   as InvoiceDate,
                           NULL                   as InvoiceNo,
                           NULL                   as BankAccountDetailID,
                           NULL                   as BankAccount,
                           NULL                   as BankName,
                           a.CurrencyID,
                           a.ExchangeRate,
                           a.Reason,
                           NULL                   as Description,
                           a.AccountingObjectID,
                           d.AccountingObjectCode,
                           a.AccountingObjectName,
                           a.AccountingObjectAddress,
                           a.Receiver             as ContactName,
                           a.EmployeeID,
                           e.AccountingObjectCode as EmployeeCode,
                           e.AccountingObjectName as EmployeeName,
                           NULL                   as MCReceiptID,
                           NULL                   as MBDepositID,
                           NULL                   as PaymentVoucherID,
                           NULL                   as RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           a.TotalAmountOriginal,
                           NULL                   as Exported,
                           NULL                   as IsDeliveryVoucher,
                           NULL                   as RefDateTime,
                           NULL                   as storedInRepository,
                           null                   as IsImportPurchase,
                           NULL                   as InvoiceForm,
                           'MCPayment'            AS RefTable
                    FROM MCPayment a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN MCPaymentDetail b on a.ID = b.MCPaymentID
                             left join TypeGroup c on TypeGroupID = c.ID
                             left join AccountingObject d on a.AccountingObjectID = d.ID
                             left join AccountingObject e on a.EmployeeID = e.ID
                    UNION ALL
                    SELECT NEWID()                AS ID,
                           a.ID                   AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           a.PostedDate,
                           NULL                   as InvoiceSeries,
                           NULL                   as InvoiceDate,
                           NULL                   as InvoiceNo,
                           NULL                   as BankAccountDetailID,
                           NULL                   as BankAccount,
                           NULL                   as BankName,
                           a.CurrencyID,
                           a.ExchangeRate,
                           a.Reason,
                           NULL                   as Description,
                           a.AccountingObjectID,
                           d.AccountingObjectCode,
                           a.AccountingObjectName,
                           a.AccountingObjectAddress,
                           a.Payers               as ContactName,
                           a.EmployeeID,
                           e.AccountingObjectCode as EmployeeCode,
                           e.AccountingObjectName as EmployeeName,
                           NULL                   as MCReceiptID,
                           NULL                   as MBDepositID,
                           NULL                   as PaymentVoucherID,
                           NULL                   as RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           a.TotalAmountOriginal,
                           a.Exported,
                           NULL                   as IsDeliveryVoucher,
                           NULL                   as RefDateTime,
                           NULL                   as storedInRepository,
                           null                   as IsImportPurchase,
                           NULL                   as InvoiceForm,
                           'MCReceipt'            AS RefTable
                    FROM dbo.MCReceipt a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN MCReceiptDetail b on a.ID = b.MCReceiptID
                             left join TypeGroup c on TypeGroupID = c.ID
                             left join AccountingObject d on a.AccountingObjectID = d.ID
                             left join AccountingObject e on a.EmployeeID = e.ID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()                AS ID,
                           a.ID                   AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           a.PostedDate,
                           NULL                   as InvoiceSeries,
                           NULL                   as InvoiceDate,
                           NULL                   as InvoiceNo,
                           a.BankAccountDetailID,
                           ba.BankAccount,
                           a.BankName,
                           a.CurrencyID,
                           a.ExchangeRate,
                           a.Reason,
                           NULL                   as Description,
                           a.AccountingObjectID,
                           d.AccountingObjectCode,
                           a.AccountingObjectName,
                           a.AccountingObjectAddress,
                           d.ContactName          as ContactName,
                           a.EmployeeID,
                           e.AccountingObjectCode as EmployeeCode,
                           e.AccountingObjectName as EmployeeName,
                           NULL                   as MCReceiptID,
                           NULL                   as MBDepositID,
                           NULL                   as PaymentVoucherID,
                           NULL                   as RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           a.TotalAmountOriginal,
                           NULL                   as Exported,
                           NULL                   as IsDeliveryVoucher,
                           NULL                   as RefDateTime,
                           NULL                   as storedInRepository,
                           null                   as IsImportPurchase,
                           NULL                   as InvoiceForm,
                           'MBTellerPaper'        AS RefTable
                    FROM dbo.MBTellerPaper a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN MBTellerPaperDetail b on a.ID = b.MBTellerPaperID
                             left join TypeGroup c on TypeGroupID = c.ID
                             left join BankAccountDetail ba on ba.ID = a.BankAccountDetailID
                             left join AccountingObject d on d.ID = a.AccountingObjectID
                             left join AccountingObject e on e.ID = a.EmployeeID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()                AS ID,
                           a.ID                   AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           a.PostedDate,
                           NULL                   as InvoiceSeries,
                           NULL                   as InvoiceDate,
                           NULL                   as InvoiceNo,
                           a.CreditCardID         as BankAccountDetailID,
                           cr.CreditCardNumber    as BankAccount,
                           ba.BankName            as BankName,
                           a.CurrencyID,
                           a.ExchangeRate,
                           a.Reason,
                           NULL                   as Description,
                           a.AccountingObjectID,
                           d.AccountingObjectCode,
                           a.AccountingObjectName,
                           a.AccountingObjectAddress,
                           NULL                   as ContactName,
                           a.EmployeeID,
                           e.AccountingObjectCode as EmployeeCode,
                           e.AccountingObjectName as EmployeeName,
                           NULL                   as MCReceiptID,
                           NULL                   as MBDepositID,
                           NULL                   as PaymentVoucherID,
                           NULL                   as RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           a.TotalAmountOriginal,
                           NULL                   as Exported,
                           NULL                   as IsDeliveryVoucher,
                           NULL                   as RefDateTime,
                           NULL                   as storedInRepository,
                           null                   as IsImportPurchase,
                           NULL                   as InvoiceForm,
                           'MBCreditCard'         AS RefTable
                    FROM dbo.MBCreditCard a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN MBCreditCardDetail b on a.ID = b.MBCreditCardID
                             left join TypeGroup c on TypeGroupID = c.ID
                             left join AccountingObject d on a.ID = a.AccountingObjectID
                             left join AccountingObject e on e.ID = a.EmployeeID
                             left join CreditCard cr on a.CreditCardID = cr.ID
                             left join Bank ba on ba.ID = cr.BankIDIssueCard
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()                AS ID,
                           a.ID                   AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           a.PostedDate,
                           NULL                   as InvoiceSeries,
                           NULL                   as InvoiceDate,
                           NULL                   as InvoiceNo,
                           a.BankAccountDetailID,
                           ba.BankAccount,
                           a.BankName,
                           a.CurrencyID,
                           a.ExchangeRate,
                           a.Reason,
                           NULL                   as Description,
                           a.AccountingObjectID,
                           d.AccountingObjectCode,
                           a.AccountingObjectName,
                           a.AccountingObjectAddress,
                           NULL                   as ContactName,
                           a.EmployeeID,
                           e.AccountingObjectCode as EmployeeCode,
                           e.AccountingObjectName as EmployeeName,
                           NULL                   as MCReceiptID,
                           NULL                   as MBDepositID,
                           NULL                   as PaymentVoucherID,
                           NULL                   as RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           a.TotalAmountOriginal,
                           a.Exported,
                           NULL                   as IsDeliveryVoucher,
                           NULL                   as RefDateTime,
                           NULL                   as storedInRepository,
                           null                   as IsImportPurchase,
                           NULL                   as InvoiceForm,
                           'MBDeposit'            AS RefTable
                    FROM dbo.MBDeposit a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN MBDepositDetail b on a.ID = b.MBDepositID
                             left join TypeGroup c on TypeGroupID = c.ID
                             left join AccountingObject d on d.ID = a.AccountingObjectID
                             left join AccountingObject e on e.ID = a.EmployeeID
                             left join BankAccountDetail ba on ba.ID = a.BankAccountDetailID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()                AS ID,
                           a.ID                   AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           a.PostedDate,
                           NULL                   as InvoiceSeries,
                           NULL                   as InvoiceDate,
                           NULL                   as InvoiceNo,
                           NULL                   as BankAccountDetailID,
                           NULL                   as BankAccount,
                           NULL                   as BankName,
                           NULL                   as CurrencyID,
                           NULL                   as ExchangeRate,
                           a.Reason,
                           NULL                   as Description,
                           NULL                   as AccountingObjectID,
                           NULL                   as AccountingObjectCode,
                           NULL                   as AccountingObjectName,
                           NULL                   as AccountingObjectAddress,
                           NULL                   as ContactName,
                           a.EmployeeID,
                           e.AccountingObjectCode as EmployeeCode,
                           e.AccountingObjectName as EmployeeName,
                           NULL                   as MCReceiptID,
                           NULL                   as MBDepositID,
                           NULL                   as PaymentVoucherID,
                           NULL                   as RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           a.TotalAmountOriginal,
                           NULL                   as Exported,
                           NULL                   as IsDeliveryVoucher,
                           NULL                   as RefDateTime,
                           NULL                   as storedInRepository,
                           null                   as IsImportPurchase,
                           NULL                   as InvoiceForm,
                           'MBInternalTransfer'   AS RefTable
                    FROM dbo.MBInternalTransfer a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN MBInternalTransferDetail b
                                        on a.ID = b.MBInternalTransferID
                             left join TypeGroup c on TypeGroupID = c.ID
                             left join AccountingObject e on e.ID = a.EmployeeID
                             INNER JOIN
                         dbo.EbOrganizationUnit ON a.CompanyID = dbo.EbOrganizationUnit.ID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()                AS ID,
                           a.ID                   AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           case
                               when a.TypeID = 211 then mcp.NoMBook
                               when a.TypeID in (212, 213, 215) then mbt.NoMBook
                               when a.TypeID = 214 then mbc.NoMBook
                               else a.NoMBook
                               end                as NoMBook,
                           case
                               when a.TypeID = 211 then mcp.NoFBook
                               when a.TypeID in (212, 213, 215) then mbt.NoFBook
                               when a.TypeID = 214 then mbc.NoFBook
                               else a.NoFBook
                               end                as NoFBook,
                           a.Date,
                           a.PostedDate,
                           NULL                   as InvoiceSeries,
                           NULL                   as InvoiceDate,
                           NULL                   as InvoiceNo,
                           case
                               when a.TypeID in (212, 213, 215) then mbt.BankAccountDetailID
                               when a.TypeID = 214 then cc.ID
                               end                as BankAccountDetailID,
                           case
                               when a.TypeID in (212, 213, 215) then bad.BankAccount
                               when a.TypeID = 214 then cc.CreditCardNumber
                               end                as BankAccount,
                           case
                               when a.TypeID in (212, 213, 215) then bad.BankName
                               when a.TypeID = 214 then ba.BankName
                               end                as BankName,
                           a.CurrencyID,
                           a.ExchangeRate,
                           a.Reason,
                           b.Description          as Description,
                           a.AccountingObjectID,
                           d.AccountingObjectCode,
                           a.AccountingObjectName,
                           a.AccountingObjectAddress,
                           a.ContactName,
                           a.EmployeeID,
                           e.AccountingObjectCode as EmployeeCode,
                           e.AccountingObjectName as EmployeeName,
                           NULL                   as MCReceiptID,
                           NULL                   as MBDepositID,
                           a.PaymentVoucherID,
                           a.RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           a.TotalAmountOriginal,
                           NULL                   as Exported,
                           NULL                   as IsDeliveryVoucher,
                           a.Date                 as RefDateTime,
                           a.StoredInRepository   as storedInRepository,
                           a.IsImportPurchase     as IsImportPurchase,
                           NULL                   as InvoiceForm,
                           'PPInvoice'            AS RefTable
                    FROM dbo.PPInvoice a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN PPInvoiceDetail b on a.ID = b.PPInvoiceID
                             left join TypeGroup c on TypeGroupID = c.ID
                             left join AccountingObject d on d.ID = a.AccountingObjectID
                             left join AccountingObject e on e.ID = a.EmployeeID
                             left join MCPayment mcp on a.PaymentVoucherID = mcp.ID
                             left join MBTellerPaper mbt on a.PaymentVoucherID = mbt.ID
                             left join MBCreditCard mbc on a.PaymentVoucherID = mbc.ID
                             left join BankAccountDetail bad on bad.id = mbt.BankAccountDetailID
                             left join CreditCard cc on upper(cc.creditCardNumber) = upper(mbc.CreditCardNumber) and
                                                        cc.CompanyID = a.CompanyID
                             left join Bank ba on ba.id = cc.BankIDIssueCard
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()                AS ID,
                           b.PPServiceID          AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           case
                               when a.TypeID = 241 then mcp.NoMBook
                               when a.TypeID in (242, 243, 245) then mbt.NoMBook
                               when a.TypeID = 244 then mbc.NoMBook
                               else a.NoMBook
                               end                as NoMBook,
                           case
                               when a.TypeID = 241 then mcp.NoFBook
                               when a.TypeID in (242, 243, 245) then mbt.NoFBook
                               when a.TypeID = 244 then mbc.NoFBook
                               else a.NoFBook
                               end                as NoFBook,
                           a.Date,
                           a.PostedDate,
                           NULL                   as InvoiceSeries,
                           NULL                   as InvoiceDate,
                           NULL                   as InvoiceNo,
                           case
                               when a.TypeID in (242, 243, 245) then mbt.BankAccountDetailID
                               when a.TypeID = 244 then cc.ID
                               end                as BankAccountDetailID,
                           case
                               when a.TypeID in (242, 243, 245) then bad.BankAccount
                               when a.TypeID = 244 then cc.CreditCardNumber
                               end                as BankAccount,
                           case
                               when a.TypeID in (242, 243, 245) then bad.BankName
                               when a.TypeID = 244 then ba.BankName
                               end                as BankName,
                           a.CurrencyID,
                           a.ExchangeRate,
                           a.Reason,
                           b.Description          as Description,
                           a.AccountingObjectID,
                           d.AccountingObjectCode,
                           d.AccountingObjectName,
                           d.AccountingObjectAddress,
                           d.ContactName          as ContactName,
                           a.EmployeeID,
                           e.AccountingObjectCode as EmployeeCode,
                           e.AccountingObjectName as EmployeeName,
                           NULL                   as MCReceiptID,
                           NULL                   as MBDepositID,
                           a.PaymentVoucherID,
                           NULL                   as RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           a.TotalAmountOriginal,
                           NULL                   as Exported,
                           NULL                   as IsDeliveryVoucher,
                           a.Date                 as RefDateTime,
                           NULL                   as storedInRepository,
                           null                   as IsImportPurchase,
                           NULL                   as InvoiceForm,
                           'PPService'            AS RefTable
                    FROM dbo.PPService a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN PPServiceDetail b on a.ID = b.PPServiceID
                             left join TypeGroup c on TypeGroupID = c.ID
                             left join AccountingObject d on d.ID = a.AccountingObjectID
                             left join AccountingObject e on e.ID = a.EmployeeID
                             left join MCPayment mcp on a.PaymentVoucherID = mcp.ID
                             left join MBTellerPaper mbt on a.PaymentVoucherID = mbt.ID
                             left join MBCreditCard mbc on a.PaymentVoucherID = mbc.ID
                             left join BankAccountDetail bad on bad.id = mbt.BankAccountDetailID
                             left join CreditCard cc on upper(cc.creditCardNumber) = upper(mbc.CreditCardNumber) and
                                                        cc.CompanyID = a.CompanyID
                             left join Bank ba on ba.id = cc.BankIDIssueCard
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()                AS ID,
                           a.ID                   AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           a.PostedDate,
                           a.InvoiceSeries,
                           a.InvoiceDate,
                           a.InvoiceNo,
                           NULL                   as BankAccountDetailID,
                           NULL                   as BankAccount,
                           NULL                   as BankName,
                           a.CurrencyID,
                           a.ExchangeRate,
                           a.Reason,
                           NULL                   as Description,
                           a.AccountingObjectID,
                           d.AccountingObjectCode,
                           a.AccountingObjectName,
                           a.AccountingObjectAddress,
                           a.ContactName,
                           a.EmployeeID,
                           e.AccountingObjectCode as EmployeeCode,
                           e.AccountingObjectName as EmployeeName,
                           NULL                   as MCReceiptID,
                           NULL                   as MBDepositID,
                           NULL                   as PaymentVoucherID,
                           a.RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           a.TotalAmountOriginal,
                           NULL                   as Exported,
                           a.IsDeliveryVoucher,
                           NULL                   as RefDateTime,
                           NULL                   as storedInRepository,
                           null                   as IsImportPurchase,
                           a.InvoiceForm,
                           'PPDiscountReturn'     AS RefTable
                    FROM dbo.PPDiscountReturn a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN PPDiscountReturnDetail b
                                        on a.ID = b.PPDiscountReturnID
                             left join TypeGroup c on TypeGroupID = c.ID
                             left join AccountingObject d on d.ID = a.AccountingObjectID
                             left join AccountingObject e on e.ID = a.EmployeeID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()                AS ID,
                           a.ID                   AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           a.PostedDate,
                           a.InvoiceSeries,
                           a.InvoiceDate,
                           a.InvoiceNo,
                           NULL                   as BankAccountDetailID,
                           NULL                   as BankAccount,
                           NULL                   as BankName,
                           a.CurrencyID,
                           a.ExchangeRate,
                           a.Reason,
                           NULL                   as Description,
                           a.AccountingObjectID,
                           d.AccountingObjectCode,
                           a.AccountingObjectName,
                           a.AccountingObjectAddress,
                           a.ContactName,
                           a.EmployeeID,
                           e.AccountingObjectCode as EmployeeCode,
                           e.AccountingObjectName as EmployeeName,
                           a.MCReceiptID,
                           a.MBDepositID,
                           a.PaymentVoucherID,
                           a.RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           a.TotalAmountOriginal,
                           a.Exported,
                           a.IsDeliveryVoucher,

                           NULL                   as RefDateTime,
                           NULL                   as storedInRepository,
                           null                   as IsImportPurchase,
                           a.InvoiceForm,
                           'SAInvoice'            AS RefTable
                    FROM dbo.SAInvoice a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN SAInvoiceDetail b on a.ID = b.SAInvoiceID
                             left join TypeGroup on TypeGroupID = TypeGroup.ID
                             left join AccountingObject d on d.ID = a.AccountingObjectID
                             left join AccountingObject e on e.ID = a.EmployeeID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()                AS ID,
                           a.ID                   AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           a.PostedDate,
                           a.InvoiceSeries,
                           a.InvoiceDate,
                           a.InvoiceNo,
                           NULL                   as BankAccountDetailID,
                           NULL                   as BankAccount,
                           NULL                   as BankName,
                           a.CurrencyID,
                           a.ExchangeRate,
                           a.Reason,
                           NULL                   as Description,
                           a.AccountingObjectID,
                           d.AccountingObjectCode,
                           a.AccountingObjectName,
                           a.AccountingObjectAddress,
                           a.ContactName,
                           a.EmployeeID,
                           e.AccountingObjectCode as EmployeeCode,
                           e.AccountingObjectName as EmployeeName,
                           NULL                   as MCReceiptID,
                           NULL                   as MBDepositID,
                           NULL                   as PaymentVoucherID,
                           a.RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           a.TotalAmountOriginal,
                           NULL                   as Exported,
                           a.IsDeliveryVoucher,
                           NULL                   as RefDateTime,
                           NULL                   as storedInRepository,
                           null                   as IsImportPurchase,
                           a.InvoiceForm,
                           'SAReturn'             AS RefTable
                    FROM dbo.SAReturn a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN SAReturnDetail b on a.ID = b.SAReturnID
                             left join TypeGroup on TypeGroupID = TypeGroup.ID
                             left join AccountingObject d on d.ID = a.AccountingObjectID
                             left join AccountingObject e on e.ID = a.EmployeeID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()                AS ID,
                           a.ID                   AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           a.PostedDate,
                           NULL                   as InvoiceSeries,
                           NULL                   as InvoiceDate,
                           NULL                   as InvoiceNo,
                           NULL                   as BankAccountDetailID,
                           NULL                   as BankAccount,
                           NULL                   as BankName,
                           a.CurrencyID,
                           a.ExchangeRate,
                           a.Reason,
                           NULL                   as Description,
                           a.AccountingObjectID,
                           a.AccountingObjectCode,
                           a.AccountingObjectName,
                           a.AccountingObjectAddress,
                           a.ContactName,
                           a.EmployeeID,
                           e.AccountingObjectCode as EmployeeCode,
                           e.AccountingObjectName as EmployeeName,
                           NULL                   as MCReceiptID,
                           NULL                   as MBDepositID,
                           NULL                   as PaymentVoucherID,
                           NULL                   as RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           a.TotalAmountOriginal,
                           a.Exported,
                           NULL                   as IsDeliveryVoucher,
                           NULL                   as RefDateTime,
                           NULL                   as storedInRepository,
                           null                   as IsImportPurchase,
                           NULL                   as InvoiceForm,
                           'RSInwardOutward'      AS RefTable
                    FROM dbo.RSInwardOutward a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN RSInwardOutwardDetail b on a.ID = b.RSInwardOutwardID
                             left join TypeGroup c on TypeGroupID = c.ID
                             left join AccountingObject e on e.ID = a.EmployeeID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()      AS ID,
                           a.ID         AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           a.PostedDate,
                           a.InvoiceSeries,
                           NULL         as InvoiceDate,
                           a.InvoiceNo,
                           NULL         as BankAccountDetailID,
                           NULL         as BankAccount,
                           NULL         as BankName,
                           NULL         as CurrencyID,
                           NULL         as ExchangeRate,
                           a.Reason,
                           NULL         as Description,
                           a.AccountingObjectID,
                           d.AccountingObjectCode,
                           a.AccountingObjectName,
                           d.AccountingObjectAddress,
                           NULL         as ContactName,
                           a.EmployeeID,
                           e.AccountingObjectCode as EmployeeCode,
                           e.AccountingObjectName as EmployeeName,
                           NULL         as MCReceiptID,
                           NULL         as MBDepositID,
                           NULL         as PaymentVoucherID,
                           NULL         as RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           a.TotalAmountOriginal,
                           NULL         as Exported,
                           NULL         as IsDeliveryVoucher,
                           NULL         as RefDateTime,
                           NULL         as storedInRepository,
                           null         as IsImportPurchase,
                           NULL         as InvoiceForm,
                           'RSTransfer' AS RefTable
                    FROM dbo.RSTransfer a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN RSTransferDetail b on a.ID = b.RSTransferID
                             left join TypeGroup on TypeGroupID = TypeGroup.ID
                             left join AccountingObject d on d.ID = a.AccountingObjectID
                             left join AccountingObject e on e.ID = a.EmployeeID
                             INNER JOIN
                         dbo.EbOrganizationUnit ON a.CompanyID = dbo.EbOrganizationUnit.ID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()       AS ID,
                           a.ID          AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           NULL          as PostedDate,
                           NULL          as InvoiceSeries,
                           NULL          as InvoiceDate,
                           NULL          as InvoiceNo,
                           NULL          as BankAccountDetailID,
                           NULL          as BankAccount,
                           NULL          as BankName,
                           NULL          as CurrencyID,
                           NULL          as ExchangeRate,
                           a.Reason,
                           NULL          as Description,
                           NULL          as AccountingObjectID,
                           NULL          as AccountingObjectCode,
                           NULL          as AccountingObjectName,
                           NULL          as AccountingObjectAddress,
                           NULL          as ContactName,
                           NULL          as EmployeeID,
                           NULL          as EmployeeCode,
                           NULL          as EmployeeName,
                           NULL          as MCReceiptID,
                           NULL          as MBDepositID,
                           NULL          as vPaymentVoucherID,
                           NULL          as RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           NULL          as TotalAmountOriginal,
                           NULL          as Exported,
                           NULL          as IsDeliveryVoucher,
                           NULL          as RefDateTime,
                           NULL          as storedInRepository,
                           null          as IsImportPurchase,
                           NULL          as InvoiceForm,
                           'TIIncrement' AS RefTable
                    FROM dbo.TIIncrement a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN TIIncrementDetail b on a.ID = b.TIIncrementID
                             left join TypeGroup c on TypeGroupID = c.ID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()       AS ID,
                           a.ID          AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           NULL          as PostedDate,
                           NULL          as InvoiceSeries,
                           NULL          as InvoiceDate,
                           NULL          as InvoiceNo,
                           NULL          as BankAccountDetailID,
                           NULL          as BankAccount,
                           NULL          as BankName,
                           NULL          as CurrencyID,
                           NULL          as ExchangeRate,
                           a.Reason,
                           NULL          as Description,
                           NULL          as AccountingObjectID,
                           NULL          as AccountingObjectCode,
                           NULL          as AccountingObjectName,
                           NULL          as AccountingObjectAddress,
                           NULL          as ContactName,
                           NULL          as EmployeeID,
                           NULL          as EmployeeCode,
                           NULL          as EmployeeName,
                           NULL          as MCReceiptID,
                           NULL          as MBDepositID,
                           NULL          as PaymentVoucherID,
                           NULL          as RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           NULL          as TotalAmountOriginal,
                           NULL          as Exported,
                           NULL          as IsDeliveryVoucher,
                           NULL          as RefDateTime,
                           NULL          as storedInRepository,
                           null          as IsImportPurchase,
                           NULL          as InvoiceForm,
                           'TIDecrement' AS RefTable
                    FROM dbo.TIDecrement a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN TIDecrementDetail b on a.ID = b.TIDecrementID
                             left join TypeGroup c on TypeGroupID = c.ID
                             INNER JOIN
                         dbo.EbOrganizationUnit ON a.CompanyID = dbo.EbOrganizationUnit.ID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()        AS ID,
                           a.ID           AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           a.PostedDate,
                           NULL           as InvoiceSeries,
                           NULL           as InvoiceDate,
                           NULL           as InvoiceNo,
                           NULL           as BankAccountDetailID,
                           NULL           as BankAccount,
                           NULL           as BankName,
                           NULL           as CurrencyID,
                           NULL           as ExchangeRate,
                           a.Reason,
                           NULL           as Description,
                           NULL           as AccountingObjectID,
                           NULL           as AccountingObjectCode,
                           NULL           as AccountingObjectName,
                           NULL           as AccountingObjectAddress,
                           NULL           as ContactName,
                           NULL           as EmployeeID,
                           NULL           as EmployeeCode,
                           NULL           as EmployeeName,
                           NULL           as MCReceiptID,
                           NULL           as MBDepositID,
                           NULL           as PaymentVoucherID,
                           NULL           as RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           NULL           as TotalAmountOriginal,
                           NULL           as Exported,
                           NULL           as IsDeliveryVoucher,
                           NULL           as RefDateTime,
                           NULL           as storedInRepository,
                           null           as IsImportPurchase,
                           NULL           as InvoiceForm,
                           'TIAdjustment' AS RefTable
                    FROM dbo.TIAdjustment a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN TIAdjustmentDetail b on a.ID = b.TIAdjustmentID
                             left join TypeGroup c on TypeGroupID = c.ID
                             INNER JOIN
                         dbo.EbOrganizationUnit ON a.CompanyID = dbo.EbOrganizationUnit.ID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()      AS ID,
                           a.ID         AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           NULL         as PostedDate,
                           NULL         as InvoiceSeries,
                           NULL         as InvoiceDate,
                           NULL         as InvoiceNo,
                           NULL         as BankAccountDetailID,
                           NULL         as BankAccount,
                           NULL         as BankName,
                           NULL         as CurrencyID,
                           NULL         as ExchangeRate,
                           a.Reason,
                           NULL         as Description,
                           NULL         as AccountingObjectID,
                           NULL         as AccountingObjectCode,
                           NULL         as AccountingObjectName,
                           NULL         as AccountingObjectAddress,
                           NULL         as ContactName,
                           NULL         as EmployeeID,
                           NULL         as EmployeeCode,
                           NULL         as EmployeeName,
                           NULL         as MCReceiptID,
                           NULL         as MBDepositID,
                           NULL         as PaymentVoucherID,
                           NULL         as RSInwardOutwardID,
                           a.Recorded,
                           NULL         as TotalAmount,
                           NULL         as TotalAmountOriginal,
                           NULL         as Exported,
                           NULL         as IsDeliveryVoucher,
                           NULL         as RefDateTime,
                           NULL         as storedInRepository,
                           null         as IsImportPurchase,
                           NULL         as InvoiceForm,
                           'TITransfer' AS RefTable
                    FROM dbo.TITransfer a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN TITransferDetail b on a.ID = b.TITransferID
                             left join TypeGroup c on TypeGroupID = c.ID
                             INNER JOIN
                         dbo.EbOrganizationUnit ON a.CompanyID = dbo.EbOrganizationUnit.ID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()        AS ID,
                           a.ID           AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           a.PostedDate,
                           NULL           as InvoiceSeries,
                           NULL           as InvoiceDate,
                           NULL           as InvoiceNo,
                           NULL           as BankAccountDetailID,
                           NULL           as BankAccount,
                           NULL           as BankName,
                           NULL           as CurrencyID,
                           NULL           as ExchangeRate,
                           a.Reason,
                           NULL           as Description,
                           NULL           as AccountingObjectID,
                           NULL           as AccountingObjectCode,
                           NULL           as AccountingObjectName,
                           NULL           as AccountingObjectAddress,
                           NULL           as ContactName,
                           NULL           as EmployeeID,
                           NULL           as EmployeeCode,
                           NULL           as EmployeeName,
                           NULL           as MCReceiptID,
                           NULL           as MBDepositID,
                           NULL           as PaymentVoucherID,
                           NULL           as RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           NULL           as TotalAmountOriginal,
                           NULL           as Exported,
                           NULL           as IsDeliveryVoucher,
                           NULL           as RefDateTime,
                           NULL           as storedInRepository,
                           null           as IsImportPurchase,
                           NULL           as InvoiceForm,
                           'TIAllocation' AS RefTable
                    FROM dbo.TIAllocation a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN TIAllocationDetail b on a.ID = b.TIAllocationID
                             left join TypeGroup c on TypeGroupID = c.ID
                             INNER JOIN
                         dbo.EbOrganizationUnit ON a.CompanyID = dbo.EbOrganizationUnit.ID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()       AS ID,
                           a.ID          AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           NULL          as PostedDate,
                           NULL          as InvoiceSeries,
                           NULL          as InvoiceDate,
                           NULL          as InvoiceNo,
                           NULL          as BankAccountDetailID,
                           NULL          as BankAccount,
                           NULL          as BankName,
                           NULL          as CurrencyID,
                           NULL          as ExchangeRate,
                           a.Reason,
                           NULL          as Description,
                           NULL          as AccountingObjectID,
                           NULL          as AccountingObjectCode,
                           NULL          as AccountingObjectName,
                           NULL          as AccountingObjectAddress,
                           NULL          as ContactName,
                           NULL          as EmployeeID,
                           NULL          as EmployeeCode,
                           NULL          as EmployeeName,
                           NULL          as MCReceiptID,
                           NULL          as MBDepositID,
                           NULL          as PaymentVoucherID,
                           NULL          as RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           NULL          as TotalAmountOriginal,
                           NULL          as Exported,
                           NULL          as IsDeliveryVoucher,
                           NULL          as RefDateTime,
                           NULL          as storedInRepository,
                           null          as IsImportPurchase,
                           NULL          as InvoiceForm,
                           'FAIncrement' AS RefTable
                    FROM dbo.FAIncrement a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN FAIncrementDetail b on a.ID = b.FAIncrementID
                             left join TypeGroup c on TypeGroupID = c.ID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()       AS ID,
                           a.ID          AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           a.PostedDate,
                           NULL          as InvoiceSeries,
                           NULL          as InvoiceDate,
                           NULL          as InvoiceNo,
                           NULL          as BankAccountDetailID,
                           NULL          as BankAccount,
                           NULL          as BankName,
                           NULL          as CurrencyID,
                           NULL          as ExchangeRate,
                           a.Reason,
                           NULL          as Description,
                           NULL          as AccountingObjectID,
                           NULL          as AccountingObjectCode,
                           NULL          as AccountingObjectName,
                           NULL          as AccountingObjectAddress,
                           NULL          as ContactName,
                           NULL          as EmployeeID,
                           NULL          as EmployeeCode,
                           NULL          as EmployeeName,
                           NULL          as MCReceiptID,
                           NULL          as MBDepositID,
                           NULL          as PaymentVoucherID,
                           NULL          as RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           NULL          as TotalAmountOriginal,
                           NULL          as Exported,
                           NULL          as IsDeliveryVoucher,
                           NULL          as RefDateTime,
                           NULL          as storedInRepository,
                           null          as IsImportPurchase,
                           NULL          as InvoiceForm,
                           'FADecrement' AS RefTable
                    FROM dbo.FADecrement a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN FADecrementDetail b on a.ID = b.FADecrementID
                             left join TypeGroup c on TypeGroupID = c.ID
                             INNER JOIN
                         dbo.EbOrganizationUnit ON a.CompanyID = dbo.EbOrganizationUnit.ID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()        AS ID,
                           a.ID           AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           a.PostedDate,
                           NULL           as InvoiceSeries,
                           NULL           as InvoiceDate,
                           NULL           as InvoiceNo,
                           NULL           as BankAccountDetailID,
                           NULL           as BankAccount,
                           NULL           as BankName,
                           NULL           as CurrencyID,
                           NULL           as ExchangeRate,
                           a.Reason,
                           NULL           as Description,
                           NULL           as AccountingObjectID,
                           NULL           as AccountingObjectCode,
                           NULL           as AccountingObjectName,
                           NULL           as AccountingObjectAddress,
                           NULL           as ContactName,
                           NULL           as EmployeeID,
                           NULL           as EmployeeCode,
                           NULL           as EmployeeName,
                           NULL           as MCReceiptID,
                           NULL           as MBDepositID,
                           NULL           as PaymentVoucherID,
                           NULL           as RSInwardOutwardID,
                           a.Recorded,
                           NULL           as TotalAmount,
                           NULL           as TotalAmountOriginal,
                           NULL           as Exported,
                           NULL           as IsDeliveryVoucher,
                           NULL           as RefDateTime,
                           NULL           as storedInRepository,
                           null           as IsImportPurchase,
                           NULL           as InvoiceForm,
                           'FAAdjustment' AS RefTable
                    FROM dbo.FAAdjustment a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN FAAdjustmentDetail b on a.ID = b.FAAdjustmentID
                             left join TypeGroup c on TypeGroupID = c.ID
                             INNER JOIN
                         dbo.EbOrganizationUnit ON a.CompanyID = dbo.EbOrganizationUnit.ID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()      AS ID,
                           a.ID         AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           NULL         as PostedDate,
                           NULL         as InvoiceSeries,
                           NULL         as InvoiceDate,
                           NULL         as InvoiceNo,
                           NULL         as BankAccountDetailID,
                           NULL         as BankAccount,
                           NULL         as BankName,
                           NULL         as CurrencyID,
                           NULL         as ExchangeRate,
                           a.Reason,
                           NULL         as Description,
                           NULL         as AccountingObjectID,
                           NULL         as AccountingObjectCode,
                           NULL         as AccountingObjectName,
                           NULL         as AccountingObjectAddress,
                           NULL         as ContactName,
                           NULL         as EmployeeID,
                           NULL         as EmployeeCode,
                           NULL         as EmployeeName,
                           NULL         as MCReceiptID,
                           NULL         as MBDepositID,
                           NULL         as PaymentVoucherID,
                           NULL         as RSInwardOutwardID,
                           a.Recorded,
                           NULL         as TotalAmount,
                           NULL         as TotalAmountOriginal,
                           NULL         as Exported,
                           NULL         as IsDeliveryVoucher,
                           NULL         as RefDateTime,
                           NULL         as storedInRepository,
                           null         as IsImportPurchase,
                           NULL         as InvoiceForm,
                           'FATransfer' AS RefTable
                    FROM dbo.FATransfer a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN FATransferDetail b on a.ID = b.FATransferID
                             left join TypeGroup c on TypeGroupID = c.ID
                             INNER JOIN
                         dbo.EbOrganizationUnit ON a.CompanyID = dbo.EbOrganizationUnit.ID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()          AS ID,
                           a.ID             AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           a.PostedDate,
                           NULL             as InvoiceSeries,
                           NULL             as InvoiceDate,
                           NULL             as InvoiceNo,
                           NULL             as BankAccountDetailID,
                           NULL             as BankAccount,
                           NULL             as BankName,
                           NULL             as CurrencyID,
                           NULL             as ExchangeRate,
                           a.Reason,
                           NULL             as Description,
                           NULL             as AccountingObjectID,
                           NULL             as AccountingObjectCode,
                           NULL             as AccountingObjectName,
                           NULL             as AccountingObjectAddress,
                           NULL             as ContactName,
                           NULL             as EmployeeID,
                           NULL             as EmployeeCode,
                           NULL             as EmployeeName,
                           NULL             as MCReceiptID,
                           NULL             as MBDepositID,
                           NULL             as PaymentVoucherID,
                           NULL             as RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           a.TotalAmountOriginal,
                           NULL             as Exported,
                           NULL             as IsDeliveryVoucher,
                           NULL             as RefDateTime,
                           NULL             as storedInRepository,
                           null             as IsImportPurchase,
                           NULL             as InvoiceForm,
                           'FADepreciation' AS RefTable
                    FROM dbo.FADepreciation a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN FADepreciationDetail b on a.ID = b.FADepreciationID
                             left join TypeGroup c on TypeGroupID = c.ID
                             INNER JOIN
                         dbo.EbOrganizationUnit ON a.CompanyID = dbo.EbOrganizationUnit.ID
/* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()         AS ID,
                           a.ID            AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           a.PostedDate,
                           NULL            as InvoiceSeries,
                           NULL            as InvoiceDate,
                           NULL            as InvoiceNo,
                           NULL            as BankAccountDetailID,
                           NULL            as BankAccount,
                           NULL            as BankName,
                           a.CurrencyID,
                           a.ExchangeRate,
                           a.Reason,
                           NULL            as Description,
                           NULL            as AccountingObjectID,
                           NULL            as AccountingObjectCode,
                           NULL            as AccountingObjectName,
                           NULL            as AccountingObjectAddress,
                           NULL            as ContactName,
                           NULL            as EmployeeID,
                           NULL            as EmployeeCode,
                           NULL            as EmployeeName,
                           NULL            as MCReceiptID,
                           NULL            as MBDepositID,
                           NULL            as PaymentVoucherID,
                           NULL            as RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           a.TotalAmountOriginal,
                           NULL            as Exported,
                           NULL            as IsDeliveryVoucher,
                           NULL            as RefDateTime,
                           NULL            as storedInRepository,
                           null            as IsImportPurchase,
                           NULL            as InvoiceForm,
                           'GOtherVoucher' AS RefTable
                    FROM dbo.GOtherVoucher a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN GOtherVoucherDetail b on a.ID = b.GOtherVoucherID
                             left join TypeGroup c on TypeGroupID = c.ID
                             /* =======================================================================*/
                    UNION ALL
                    SELECT NEWID()         AS ID,
                           a.ID            AS RefID,
                           a.TypeID,
                           dbo.Type.TypeGroupID,
                           TypeGroupName,
                           a.CompanyID,
                           a.BranchID,
                           a.TypeLedger,
                           a.NoMBook,
                           a.NoFBook,
                           a.Date,
                           a.PostedDate,
                           NULL            as InvoiceSeries,
                           NULL            as InvoiceDate,
                           NULL            as InvoiceNo,
                           NULL            as BankAccountDetailID,
                           NULL            as BankAccount,
                           NULL            as BankName,
                           null as CurrencyID,
                           null as ExchangeRate,
                           a.Reason,
                           NULL            as Description,
                           NULL            as AccountingObjectID,
                           NULL            as AccountingObjectCode,
                           NULL            as AccountingObjectName,
                           NULL            as AccountingObjectAddress,
                           NULL            as ContactName,
                           NULL            as EmployeeID,
                           NULL            as EmployeeCode,
                           NULL            as EmployeeName,
                           NULL            as MCReceiptID,
                           NULL            as MBDepositID,
                           NULL            as PaymentVoucherID,
                           NULL            as RSInwardOutwardID,
                           a.Recorded,
                           a.TotalAmount,
                           a.TotalAmountOriginal,
                           NULL            as Exported,
                           NULL            as IsDeliveryVoucher,
                           NULL            as RefDateTime,
                           NULL            as storedInRepository,
                           null            as IsImportPurchase,
                           NULL            as InvoiceForm,
                           'CPExpenseTranfer' AS RefTable
                    FROM dbo.CPExpenseTranfer a
                             INNER JOIN
                         dbo.Type ON a.TypeID = dbo.Type.ID
                             INNER JOIN CPExpenseTranferDetail b on a.ID = b.CPExpenseTranferID
                             left join TypeGroup c on TypeGroupID = c.ID) b) temp
        where roworder = 1
go
