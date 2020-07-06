
IF OBJECT_ID ( 'PROC_BANG_KE_BAN_RA', 'P' ) IS NOT NULL
    DROP PROCEDURE PROC_BANG_KE_BAN_RA;
CREATE PROCEDURE [dbo].[PROC_BANG_KE_BAN_RA]
      (
      @CompanyId UNIQUEIDENTIFIER ,
      @FromDate DATETIME,
      @ToDate DATETIME ,
      @TypeLedger BIT,
      @IsSimilarBranch BIT  = 0,
      @IsBill BIT = 0,
      @isDependent BIT = 0
    )
AS
BEGIN
     -- SET NOCOUNT ON added to prevent extra result sets from
        SET NOCOUNT ON;
--Proc_GetBkeInv_Buy null,0,'1/1/2019','12/31/2019',0
/*add by cuongpv de xu ly cach thong nhat lam tron*/
	DECLARE @tbDataLocal TABLE(
		id UNIQUEIDENTIFIER,
		invoiceNo NVARCHAR(25) COLLATE SQL_Latin1_General_CP1_CI_AS,
		invoiceDate DATETIME,
		accountingObjectName NVARCHAR(512) COLLATE SQL_Latin1_General_CP1_CI_AS,
		companyTaxCode NVARCHAR(50) COLLATE SQL_Latin1_General_CP1_CI_AS,
		description NVARCHAR(512),
		amount decimal(25,0),
		vatAmount decimal(25,0),
		VATRate decimal(25,0),
		vatAccount NVARCHAR(25) COLLATE SQL_Latin1_General_CP1_CI_AS,
		orderPriority int
	)
Begin
    if @IsBill = 1
        begin
    INSERT INTO @tbDataLocal
    select  sab.id as id , sab.InvoiceNo as invoiceNo, sab.InvoiceDate as invoiceDate, sab.AccountingObjectName as accountingObjectName, sab.CompanyTaxCode as companyTaxCode,
           case when @IsSimilarBranch = 0  then sabd.Description else  sab.Reason end as description,case  when sab.TypeID in (353) then 0 - (sabd.Amount - sabd.DiscountAmount) else (sabd.Amount - sabd.DiscountAmount) end as amount,
           case  when sab.TypeID in (353) then 0 - sabd.VATAmount else sabd.VATAmount end as vatAmount,
           sabd.VATRate as VATRate,
           case when sab.TypeID = 351 then said.VATAccount
--             when sab.TypeID = 352 then ppdd.VATAccount
            when sab.TypeID = 353 then sard.VATAccount
            end as vatAccount,
           sabd.OrderPriority
            from  SABill sab join SABillDetail sabd on sab.ID = sabd.SABillID
            left join SAInvoiceDetail said on said.SABillDetailID =  sabd.ID
            left join SAInvoice sai on sai.ID = said.SAInvoiceID
--             left join PPDiscountReturnDetail ppdd on ppdd.SABillDetailID = sabd.ID
--             left join PPDiscountReturn ppd on ppdd.PPDiscountReturnID = ppd.ID
            left join SAReturnDetail sard on sard.SaBillDetailID = sabd.ID
            left join SAReturn sar on sar.id = sard.SAReturnID
            left join Type t on sab.TypeID = t.ID
            where sab.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
              and sab.TypeLedger in (@TypeLedger, 2) and sabd.VATRate is not null and sab.InvoiceDate between @FromDate and @ToDate
            and sab.TypeID != 352 and (sai.Recorded = 1 or sar.Recorded = 1 or sab.TypeID = 350)
    union all
    select  sai.id as id , null as invoiceNo, null as invoiceDate, sai.AccountingObjectName as accountingObjectName, sai.CompanyTaxCode as companyTaxCode,
            case when @IsSimilarBranch = 0  then said.Description else  sai.Reason end as description, said.Amount as amount,
           said.vatAmount as vatAmount,
           said.VATRate as VATRate,
           said.VATAccount as vatAccount,
           said.OrderPriority
    from SAInvoiceDetail said
    left join SAInvoice sai on said.SAInvoiceID = sai.ID
    where sai.invoiceNo is null and sai.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
              and sai.TypeLedger in (@TypeLedger, 2) and said.VATRate is not null and sai.Recorded = 1
    union all
    select  sar.ID as id , sar.InvoiceNo as invoiceNo, sar.InvoiceDate as invoiceDate, sar.AccountingObjectName as accountingObjectName, sar.CompanyTaxCode as companyTaxCode,
            case when @IsSimilarBranch = 0  then sard.Description else  sar.Reason end as description, 0 - sard.Amount as amount,
           sard.vatAmount as vatAmount,
           sard.VATRate as VATRate,
           sard.VATAccount as vatAccount,
           sard.OrderPriority
    from SAReturnDetail sard
    left join SAReturn sar on sard.SAReturnID = sar.ID
    where sar.TypeID = 330 and sar.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
              and sar.TypeLedger in (@TypeLedger, 2) and sard.VATRate is not null and sar.Recorded = 1
            SELECT * FROM @tbDataLocal a where a.vatAmount != 0 or a.amount != 0 order by VATRate, InvoiceDate, InvoiceNo, AccountingObjectName;
        end
    else
        begin
             INSERT INTO @tbDataLocal
    select  sab.id as id , sab.InvoiceNo as invoiceNo, sab.InvoiceDate as invoiceDate, sab.AccountingObjectName as accountingObjectName, sab.CompanyTaxCode as companyTaxCode,
            case when @IsSimilarBranch = 0  then sabd.Description else  sab.Reason end as description,
           case  when sab.TypeID in (353) then 0 - (sabd.Amount - sabd.DiscountAmount) else (sabd.Amount - sabd.DiscountAmount) end as amount,
           case  when sab.TypeID in (353) then 0 - sabd.VATAmount else sabd.VATAmount end as vatAmount,
           sabd.VATRate as VATRate,
           case when sab.TypeID = 351 then said.VATAccount
--             when sab.TypeID = 352 then ppdd.VATAccount
            when sab.TypeID = 353 then sard.VATAccount
            end as vatAccount,
           sabd.OrderPriority
            from  SABill sab join SABillDetail sabd on sab.ID = sabd.SABillID
            left join SAInvoiceDetail said on said.SABillDetailID =  sabd.ID
            left join SAInvoice sai on sai.ID = said.SAInvoiceID
--             left join PPDiscountReturnDetail ppdd on ppdd.SABillDetailID = sabd.ID
--             left join PPDiscountReturn ppd on ppdd.PPDiscountReturnID = ppd.ID
            left join SAReturnDetail sard on sard.SaBillDetailID = sabd.ID
            left join SAReturn sar on sar.id = sard.SAReturnID
            left join Type t on sab.TypeID = t.ID
            where sab.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
              and sab.TypeLedger in (@TypeLedger, 2) and sabd.VATRate is not null and sab.InvoiceDate between @FromDate and @ToDate
            and sab.TypeID != 352 and (sai.Recorded = 1 or sar.Recorded = 1 or (sab.TypeID = 350 and sab.InvoiceNo is not null and sab.InvoiceNo != ''))
            SELECT * FROM @tbDataLocal a where a.vatAmount != 0 or a.amount != 0 order by VATRate, InvoiceDate, InvoiceNo, AccountingObjectName;
        end

end
    end
go
