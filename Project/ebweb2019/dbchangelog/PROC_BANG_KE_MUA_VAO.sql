ALTER PROCEDURE [dbo].[PROC_BANG_KE_MUA_VAO]
    (
        @CompanyId UNIQUEIDENTIFIER ,
        @FromDate DATETIME,
        @ToDate DATETIME ,
        @IsSimilarBranch BIT ,
        @TypeLedger BIT,
        @IsBill BIT = 0,
        @isDependent BIT = 0
        )
    AS
    BEGIN
        -- SET NOCOUNT ON added to prevent extra result sets from
        SET NOCOUNT ON;
        --Proc_GetBkeInv_Buy null,0,'1/1/2019','12/31/2019',0
/*add by cuongpv de xu ly cach thong nhat lam tron*/
        DECLARE @tbDataLocal TABLE
                             (
                                 goodsServicePurchaseCode NVARCHAR(25) COLLATE SQL_Latin1_General_CP1_CI_AS,
                                 goodsServicePurchaseName NVARCHAR(512) COLLATE SQL_Latin1_General_CP1_CI_AS,
                                 invoiceNo                NVARCHAR(25) COLLATE SQL_Latin1_General_CP1_CI_AS,
                                 invoiceDate              DATETIME,
                                 accountingObjectName     NVARCHAR(512) COLLATE SQL_Latin1_General_CP1_CI_AS,
                                 companyTaxCode           NVARCHAR(50) COLLATE SQL_Latin1_General_CP1_CI_AS,
                                 description              NVARCHAR(512),
                                 amount                   decimal(25, 0),
                                 vatRate                  decimal(25, 10),
                                 vatAmount                decimal(25, 0),
                                 vatAccount               NVARCHAR(25) COLLATE SQL_Latin1_General_CP1_CI_AS,
                                 flag                     int,
                                 AccountingObjectID       UNIQUEIDENTIFIER,
                                 RefID                    UNIQUEIDENTIFIER,
                                 TypeID                   int,
                                 OrderPriority            int
                             )
/*end add by cuongpv*/
        if @IsSimilarBranch = 0
            Begin
                /*add by cuongpv*/
                --lay du lieu tu DB da lam tron tung dong
                if @IsBill = 1
                    BEGIN
                        INSERT INTO @tbDataLocal
                            /*end add by cuongpv*/
                            --Mua hàng --> Nhận hóa đơn mua hàng
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo                   as invoiceNo,
                               InvoiceDate                 as invoiceDate,
                               a.AccountingObjectName      as accountingObjectName,
                               CompanyTaxCode              as companyTaxCode,
                               b.Description               as description,
                               b.Amount - b.DiscountAmount as amount,
                               VATRate                     as vatRate,
                               VATAmount                   as vatAmount,
                               VATAccount                  as vatAccount,
                               1                              flag,
                               b.AccountingObjectID        as AccountingObjectID
                                ,
                               a.ID                        as RefID,
                               a.TypeID                    as TypeID,
                               b.OrderPriority
                        from PPInvoice a
                                 join PPInvoiceDetail b on a.ID = b.PPInvoiceID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                        union all
                        --Trả lại hàng mua, Giảm giá hàng mua --> Cty phải xuất hóa đơn cho người bán --> Giá trị trên mẫu báo cáo là số tiền âm (Mẫu báo cáo theo hình ở dưới)
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               a.AccountingObjectName as accountingObjectName,
                               CompanyTaxCode         as companyTaxCode,
                               b.Description          as description,
                               (0 - Amount)           as GiatriHH_chuathue,
                               VATRate                as vatRate,
                               (0 - VATAmount)        as vatAmount,
                               VATAccount             as vatAccount,
                               -1                        flag,
                               b.AccountingObjectID   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from PPDiscountReturn a
                                 join PPDiscountReturnDetail b on a.ID = b.PPDiscountReturnID
                                 join GoodsServicePurchase c on c.ID = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                        union all
                        --Hoá đơn mua dịch vụ
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo                   as invoiceNo,
                               InvoiceDate                 as invoiceDate,
                               a.AccountingObjectName      as accountingObjectName,
                               a.CompanyTaxCode            as companyTaxCode,
                               b.Description               as description,
                               b.Amount - b.DiscountAmount as amount,
                               VATRate                     as vatRate,
                               VATAmount                   as vatAmount,
                               VATAccount                  as vatAccount,
                               1                              flag,
                               b.AccountingObjectID        as AccountingObjectID
                                ,
                               a.ID                        as RefID,
                               a.TypeID                    as TypeID,
                               b.OrderPriority
                        from PPService a
                                 join PPServiceDetail b on a.ID = b.PPServiceID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
-- 		union all
-- 		--hóa đơn mua TSCĐ tại module TSCĐ (màn Mua và ghi tăng TSCĐ) edit by cuongpv 20191114: (Amount - DiscountAmount) as amount
-- 		select c.goodsServicePurchaseCode,c.goodsServicePurchaseName, InvoiceNo as invoiceNo, InvoiceDate as invoiceDate, a.AccountingObjectName as accountingObjectName, null as companyTaxCode,
-- 		b.Description as description, (Amount - DiscountAmount) as amount, VATRate as vatRate, VATAmount as vatAmount, VATAccount as vatAccount,1 flag, b.AccountingObjectID as AccountingObjectID
-- 		, a.ID as RefID, a.TypeID as TypeID, b.OrderPriority
-- 		from FAIncrement a join FAIncrementDetail b on a.ID = b.FAIncrementID
-- 		join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
-- 		where a.PostedDate between @FromDate and @ToDate and Recorded = 1 and a.TypeID not in (510) and a.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent)) and a.TypeLedger in (@TypeLedger, 2)
-- 		union all
-- 		-- hóa đơn mua CCDC tại module CCDC (màn Mua và ghi tăng CCDC)
-- 		select c.goodsServicePurchaseCode,c.goodsServicePurchaseName, InvoiceNo as invoiceNo, InvoiceDate as invoiceDate, a.AccountingObjectName as accountingObjectName, null as companyTaxCode,
-- 		b.Description as description, (Amount - DiscountAmount) as amount, VATRate as vatRate, VATAmount as vatAmount, VATAccount as vatAccount	,1 flag, b.AccountingObjectID as AccountingObjectID
-- 		, a.ID as RefID, a.TypeID as TypeID, b.OrderPriority
-- 		from TIIncrement a join TIIncrementDetail b on a.ID = b.TIIncrementID
-- 		join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
-- 		where a.PostedDate between @FromDate and @ToDate and Recorded = 1 and a.TypeID not in (907) and a.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent)) and a.TypeLedger in (@TypeLedger, 2)
                        union all
                        --1.	CHỨNG TỪ NGHIỆP VỤ KHÁC
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               b.AccountingObjectName as accountingObjectName,
                               b.TaxCode              as companyTaxCode,
                               b.Description          as description,
                               PretaxAmount           as amount,
                               VATRate                as vatRate,
                               VATAmount              as vatAmount,
                               VATAccount             as vatAccount,
                               1                         flag,
                               NULL                   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from GOtherVoucher a
                                 join GOtherVoucherDetailTax b on a.ID = b.GOtherVoucherID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                        union all
                        --2.	PHIẾU CHI
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               b.AccountingObjectName as accountingObjectName,
                               b.TaxCode              as companyTaxCode,
                               b.Description          as description,
                               PretaxAmount           as amount,
                               VATRate                as vatRate,
                               b.VATAmount            as vatAmount,
                               VATAccount             as vatAccount,
                               1                         flag,
                               NULL                   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from MCPayment a
                                 join MCPaymentDetailTax b on a.ID = b.MCPaymentID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and a.Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                        union all
                        --3.	UNC/SÉC CK/SÉC TM
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               b.AccountingObjectName as accountingObjectName,
                               b.TaxCode              as companyTaxCode,
                               b.Description          as description,
                               PretaxAmount           as amount,
                               VATRate                as vatRate,
                               b.VATAmount            as vatAmount,
                               VATAccount             as vatAccount,
                               1                         flag,
                               NULL                   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from MBTellerPaper a
                                 join MBTellerPaperDetailTax b on a.ID = b.MBTellerPaperID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and a.Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                        union all
                        --4.	THẺ TÍN DỤNG
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               b.AccountingObjectName as accountingObjectName,
                               b.TaxCode              as companyTaxCode,
                               b.Description          as description,
                               PretaxAmount           as amount,
                               VATRate                as vatRate,
                               b.VATAmount            as vatAmount,
                               VATAccount             as vatAccount,
                               1                         flag,
                               NULL                   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from MBCreditCard a
                                 join MBCreditCardDetailTax b on a.ID = b.MBCreditCardID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and a.Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2);
                    end
                else
                    BEGIN
                        INSERT INTO @tbDataLocal
                            /*end add by cuongpv*/
                            --Mua hàng --> Nhận hóa đơn mua hàng
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo                   as invoiceNo,
                               InvoiceDate                 as invoiceDate,
                               a.AccountingObjectName      as accountingObjectName,
                               CompanyTaxCode              as companyTaxCode,
                               b.Description               as description,
                               b.Amount - b.DiscountAmount as amount,
                               VATRate                     as vatRate,
                               VATAmount                   as vatAmount,
                               VATAccount                  as vatAccount,
                               1                              flag,
                               b.AccountingObjectID        as AccountingObjectID
                                ,
                               a.ID                        as RefID,
                               a.TypeID                    as TypeID,
                               b.OrderPriority
                        from PPInvoice a
                                 join PPInvoiceDetail b on a.ID = b.PPInvoiceID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                          and b.invoiceNo is not null
                          and b.invoiceDate is not null
                        union all
                        --Trả lại hàng mua, Giảm giá hàng mua --> Cty phải xuất hóa đơn cho người bán --> Giá trị trên mẫu báo cáo là số tiền âm (Mẫu báo cáo theo hình ở dưới)
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               a.AccountingObjectName as accountingObjectName,
                               CompanyTaxCode         as companyTaxCode,
                               b.Description          as description,
                               (0 - Amount)           as GiatriHH_chuathue,
                               VATRate                as vatRate,
                               (0 - VATAmount)        as vatAmount,
                               VATAccount             as vatAccount,
                               -1                        flag,
                               b.AccountingObjectID   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from PPDiscountReturn a
                                 join PPDiscountReturnDetail b on a.ID = b.PPDiscountReturnID
                                 join GoodsServicePurchase c on c.ID = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                          and a.invoiceNo is not null
                          and a.invoiceDate is not null
                        union all
                        --Hoá đơn mua dịch vụ
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo                   as invoiceNo,
                               InvoiceDate                 as invoiceDate,
                               a.AccountingObjectName      as accountingObjectName,
                               a.CompanyTaxCode            as companyTaxCode,
                               b.Description               as description,
                               b.Amount - b.DiscountAmount as amount,
                               VATRate                     as vatRate,
                               VATAmount                   as vatAmount,
                               VATAccount                  as vatAccount,
                               1                              flag,
                               b.AccountingObjectID        as AccountingObjectID
                                ,
                               a.ID                        as RefID,
                               a.TypeID                    as TypeID,
                               b.OrderPriority
                        from PPService a
                                 join PPServiceDetail b on a.ID = b.PPServiceID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                          and b.invoiceNo is not null
                          and b.invoiceDate is not null
-- 		union all
-- 		--hóa đơn mua TSCĐ tại module TSCĐ (màn Mua và ghi tăng TSCĐ) edit by cuongpv 20191114: (Amount - DiscountAmount) as amount
-- 		select c.goodsServicePurchaseCode,c.goodsServicePurchaseName, InvoiceNo as invoiceNo, InvoiceDate as invoiceDate, a.AccountingObjectName as accountingObjectName, null as companyTaxCode,
-- 		b.Description as description, (Amount - DiscountAmount) as amount, VATRate as vatRate, VATAmount as vatAmount, VATAccount as vatAccount,1 flag, b.AccountingObjectID as AccountingObjectID
-- 		, a.ID as RefID, a.TypeID as TypeID, b.OrderPriority
-- 		from FAIncrement a join FAIncrementDetail b on a.ID = b.FAIncrementID
-- 		join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
-- 		where a.PostedDate between @FromDate and @ToDate and Recorded = 1 and a.TypeID not in (510) and a.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent)) and a.TypeLedger in (@TypeLedger, 2) and b.invoiceNo is not null and b.invoiceDate is not null
-- 		union all
-- 		-- hóa đơn mua CCDC tại module CCDC (màn Mua và ghi tăng CCDC)
-- 		select c.goodsServicePurchaseCode,c.goodsServicePurchaseName, InvoiceNo as invoiceNo, InvoiceDate as invoiceDate, a.AccountingObjectName as accountingObjectName, null as companyTaxCode,
-- 		b.Description as description, (Amount - DiscountAmount) as amount, VATRate as vatRate, VATAmount as vatAmount, VATAccount as vatAccount	,1 flag, b.AccountingObjectID as AccountingObjectID
-- 		, a.ID as RefID, a.TypeID as TypeID, b.OrderPriority
-- 		from TIIncrement a join TIIncrementDetail b on a.ID = b.TIIncrementID
-- 		join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
-- 		where a.PostedDate between @FromDate and @ToDate and Recorded = 1 and a.TypeID not in (907) and a.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent)) and a.TypeLedger in (@TypeLedger, 2) and b.invoiceNo is not null and b.invoiceDate is not null
                        union all
                        --1.	CHỨNG TỪ NGHIỆP VỤ KHÁC
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               b.AccountingObjectName as accountingObjectName,
                               b.TaxCode              as companyTaxCode,
                               b.Description          as description,
                               PretaxAmount           as amount,
                               VATRate                as vatRate,
                               VATAmount              as vatAmount,
                               VATAccount             as vatAccount,
                               1                         flag,
                               NULL                   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from GOtherVoucher a
                                 join GOtherVoucherDetailTax b on a.ID = b.GOtherVoucherID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                          and b.InvoiceNo is not null
                          and LEN(b.InvoiceNo) > 0
                          and b.InvoiceDate is not null
                        union all
                        --2.	PHIẾU CHI
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               b.AccountingObjectName as accountingObjectName,
                               b.TaxCode              as companyTaxCode,
                               b.Description          as description,
                               PretaxAmount           as amount,
                               VATRate                as vatRate,
                               b.VATAmount            as vatAmount,
                               VATAccount             as vatAccount,
                               1                         flag,
                               NULL                   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from MCPayment a
                                 join MCPaymentDetailTax b on a.ID = b.MCPaymentID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and a.Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                          and b.InvoiceNo is not null
                          and LEN(b.InvoiceNo) > 0
                          and b.InvoiceDate is not null
                        union all
                        --3.	UNC/SÉC CK/SÉC TM
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               b.AccountingObjectName as accountingObjectName,
                               b.TaxCode              as companyTaxCode,
                               b.Description          as description,
                               PretaxAmount           as amount,
                               VATRate                as vatRate,
                               b.VATAmount            as vatAmount,
                               VATAccount             as vatAccount,
                               1                         flag,
                               NULL                   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from MBTellerPaper a
                                 join MBTellerPaperDetailTax b on a.ID = b.MBTellerPaperID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and a.Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                          and b.InvoiceNo is not null
                          and LEN(b.InvoiceNo) > 0
                          and b.InvoiceDate is not null
                        union all
                        --4.	THẺ TÍN DỤNG
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               b.AccountingObjectName as accountingObjectName,
                               b.TaxCode              as companyTaxCode,
                               b.Description          as description,
                               PretaxAmount           as amount,
                               VATRate                as vatRate,
                               b.VATAmount            as vatAmount,
                               VATAccount             as vatAccount,
                               1                         flag,
                               NULL                   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from MBCreditCard a
                                 join MBCreditCardDetailTax b on a.ID = b.MBCreditCardID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and a.Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                          and b.InvoiceNo is not null
                          and LEN(b.InvoiceNo) > 0
                          and b.InvoiceDate is not null;
                    end

                /*edit by Hautv */
-- 		UPDATE @tbDataLocal SET accountingObjectName = d.AccountingObjectName
-- 		FROM (select * from AccountingObject xca) d
-- 			where AccountingObjectID = d.ID;
                ------------

                /*edit by cuongpv order by c.goodsServicePurchaseCode, InvoiceDate, InvoiceNo -> SELECT * FROM @tbDataLocal order by goodsServicePurchaseCode, invoiceDate, invoiceNo;*/

                SELECT *
                FROM @tbDataLocal
                where (vatAmount is not null and vatAmount != 0)
                   or (amount is not null and amount != 0)
                order by goodsServicePurchaseCode, invoiceDate, invoiceNo;

            End
        else
            Begin
                /*add by cuongpv*/
                --lay du lieu tu DB da lam tron tung dong
                if @IsBill = 1
                    Begin
                        INSERT INTO @tbDataLocal
                            -- update by namnh 19/05/2020
                        select goodsServicePurchaseCode,
                               goodsServicePurchaseName,
                               invoiceNo,
                               invoiceDate,
                               accountingObjectName,
                               companyTaxCode,
                               description,
                               Sum(amount),
                               vatRate,
                               vatAmount,
                               vatAccount,
                               flag,
                               AccountingObjectID
                                ,
                               RefID,
                               TypeID,
                               0 as OrderPriority
                        FROM (
                                 /*end add by cuongpv*/
                                 --Mua hàng --> Nhận hóa đơn mua hàng
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo                 as invoiceNo,
                                        InvoiceDate               as invoiceDate,
                                        a.AccountingObjectName    as accountingObjectName,
                                        CompanyTaxCode            as companyTaxCode,
                                        a.Reason                  as description,
                                        (Amount - DiscountAmount) as amount,
                                        VATRate                   as vatRate,
                                        VATAmount                 as vatAmount,
                                        VATAccount                as vatAccount,
                                        1                            flag,
                                        b.AccountingObjectID      as AccountingObjectID
                                         ,
                                        a.ID                      as RefID,
                                        a.TypeID                  as TypeID,
                                        b.OrderPriority
                                 from PPInvoice a
                                          join PPInvoiceDetail b on a.ID = b.PPInvoiceID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                 union all
                                 --Trả lại hàng mua, Giảm giá hàng mua --> Cty phải xuất hóa đơn cho người bán --> Giá trị trên mẫu báo cáo là số tiền âm (Mẫu báo cáo theo hình ở dưới)
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        a.AccountingObjectName as accountingObjectName,
                                        CompanyTaxCode         as companyTaxCode,
                                        a.Reason               as description,
                                        (0 - Amount)           as GiatriHH_chuathue,
                                        VATRate                as vatRate,
                                        (0 - VATAmount)        as vatAmount,
                                        VATAccount             as vatAccount,
                                        -1                        flag,
                                        b.AccountingObjectID   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from PPDiscountReturn a
                                          join PPDiscountReturnDetail b on a.ID = b.PPDiscountReturnID
                                          join GoodsServicePurchase c on c.ID = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                 union all
                                 --Hoá đơn mua dịch vụ
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo               as invoiceNo,
                                        InvoiceDate             as invoiceDate,
                                        a.AccountingObjectName  as accountingObjectName,
                                        a.CompanyTaxCode        as companyTaxCode,
                                        a.Reason                as description,
                                        Amount - DiscountAmount as amount,
                                        VATRate                 as vatRate,
                                        VATAmount               as vatAmount,
                                        VATAccount              as vatAccount,
                                        1                          flag,
                                        b.AccountingObjectID    as AccountingObjectID
                                         ,
                                        a.ID                    as RefID,
                                        a.TypeID                as TypeID,
                                        b.OrderPriority
                                 from PPService a
                                          join PPServiceDetail b on a.ID = b.PPServiceID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
-- 		union all
-- 		--hóa đơn mua TSCĐ tại module TSCĐ (màn Mua và ghi tăng TSCĐ)
-- 		select c.goodsServicePurchaseCode,c.goodsServicePurchaseName, InvoiceNo as invoiceNo, InvoiceDate as invoiceDate, a.AccountingObjectName as accountingObjectName, null as companyTaxCode,
-- 		a.Reason as description, (Amount - DiscountAmount) as amount, VATRate as vatRate, VATAmount as vatAmount, VATAccount as vatAccount,1 flag, b.AccountingObjectID as AccountingObjectID
-- 		, a.ID as RefID, a.TypeID as TypeID, b.OrderPriority
-- 		from FAIncrement a join FAIncrementDetail b on a.ID = b.FAIncrementID
-- 		join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
-- 		where a.PostedDate between @FromDate and @ToDate and Recorded = 1 and a.TypeID not in (510) and a.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent)) and a.TypeLedger in (@TypeLedger, 2)
-- 		union all
-- 		-- hóa đơn mua CCDC tại module CCDC (màn Mua và ghi tăng CCDC)
-- 		select c.goodsServicePurchaseCode,c.goodsServicePurchaseName, InvoiceNo as invoiceNo, InvoiceDate as invoiceDate, a.AccountingObjectName as accountingObjectName, null as companyTaxCode,
-- 		a.Reason as description, (Amount - DiscountAmount) as amount, VATRate as vatRate, VATAmount as vatAmount, VATAccount as vatAccount	,1 flag, b.AccountingObjectID as AccountingObjectID
-- 		, a.ID as RefID, a.TypeID as TypeID, b.OrderPriority
-- 		from TIIncrement a join TIIncrementDetail b on a.ID = b.TIIncrementID
-- 		join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
-- 		where a.PostedDate between @FromDate and @ToDate and Recorded = 1 and a.TypeID not in (907) and a.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent)) and a.TypeLedger in (@TypeLedger, 2)
                                 union all
                                 --1.	CHỨNG TỪ NGHIỆP VỤ KHÁC
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        b.AccountingObjectName as accountingObjectName,
                                        b.TaxCode              as companyTaxCode,
                                        a.Reason               as description,
                                        PretaxAmount           as amount,
                                        VATRate                as vatRate,
                                        VATAmount              as vatAmount,
                                        VATAccount             as vatAccount,
                                        1                         flag,
                                        NULL                   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from GOtherVoucher a
                                          join GOtherVoucherDetailTax b on a.ID = b.GOtherVoucherID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                 union all
                                 --2.	PHIẾU CHI
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        b.AccountingObjectName as accountingObjectName,
                                        b.TaxCode              as companyTaxCode,
                                        a.Reason               as description,
                                        PretaxAmount           as amount,
                                        VATRate                as vatRate,
                                        b.VATAmount            as vatAmount,
                                        VATAccount             as vatAccount,
                                        1                         flag,
                                        NULL                   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from MCPayment a
                                          join MCPaymentDetailTax b on a.ID = b.MCPaymentID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and a.Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                 union all
                                 --3.	UNC/SÉC CK/SÉC TM
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        b.AccountingObjectName as accountingObjectName,
                                        b.TaxCode              as companyTaxCode,
                                        a.Reason               as description,
                                        PretaxAmount           as amount,
                                        VATRate                as vatRate,
                                        b.VATAmount            as vatAmount,
                                        VATAccount             as vatAccount,
                                        1                         flag,
                                        NULL                   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from MBTellerPaper a
                                          join MBTellerPaperDetailTax b on a.ID = b.MBTellerPaperID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and a.Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                 union all
                                 --4.	THẺ TÍN DỤNG
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        b.AccountingObjectName as accountingObjectName,
                                        b.TaxCode              as companyTaxCode,
                                        a.Reason               as description,
                                        PretaxAmount           as amount,
                                        VATRate                as vatRate,
                                        b.VATAmount            as vatAmount,
                                        VATAccount             as vatAccount,
                                        1                         flag,
                                        NULL                   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from MBCreditCard a
                                          join MBCreditCardDetailTax b on a.ID = b.MBCreditCardID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and a.Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                             ) as #RS
                        GROUP BY goodsServicePurchaseCode, goodsServicePurchaseName, invoiceNo, invoiceDate,
                                 accountingObjectName, companyTaxCode, description, vatRate, vatAmount, vatAccount,
                                 flag,
                                 AccountingObjectID, RefID, TypeID
                    end
                else
                    begin
                        INSERT INTO @tbDataLocal
                        -- update by namnh 19/05/2020
                        SELECT goodsServicePurchaseCode,
                               goodsServicePurchaseName,
                               invoiceNo,
                               invoiceDate,
                               accountingObjectName,
                               companyTaxCode,
                               description,
                               Sum(amount),
                               vatRate,
                               vatAmount,
                               vatAccount,
                               flag,
                               AccountingObjectID
                                ,
                               RefID,
                               TypeID,
                               0 as OrderPriority
                        FROM (
                                 /*end add by cuongpv*/
                                 --Mua hàng --> Nhận hóa đơn mua hàng
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo                 as invoiceNo,
                                        InvoiceDate               as invoiceDate,
                                        a.AccountingObjectName    as accountingObjectName,
                                        CompanyTaxCode            as companyTaxCode,
                                        a.Reason                  as description,
                                        (Amount - DiscountAmount) as amount,
                                        VATRate                   as vatRate,
                                        VATAmount                 as vatAmount,
                                        VATAccount                as vatAccount,
                                        1                            flag,
                                        b.AccountingObjectID      as AccountingObjectID
                                         ,
                                        a.ID                      as RefID,
                                        a.TypeID                  as TypeID,
                                        b.OrderPriority
                                 from PPInvoice a
                                          join PPInvoiceDetail b on a.ID = b.PPInvoiceID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                   and b.invoiceNo is not null
                                   and LEN(b.InvoiceNo) > 0
                                   and b.invoiceDate is not null
                                 union all
                                 --Trả lại hàng mua, Giảm giá hàng mua --> Cty phải xuất hóa đơn cho người bán --> Giá trị trên mẫu báo cáo là số tiền âm (Mẫu báo cáo theo hình ở dưới)
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        a.AccountingObjectName as accountingObjectName,
                                        CompanyTaxCode         as companyTaxCode,
                                        a.Reason               as description,
                                        (0 - Amount)           as GiatriHH_chuathue,
                                        VATRate                as vatRate,
                                        (0 - VATAmount)        as vatAmount,
                                        VATAccount             as vatAccount,
                                        -1                        flag,
                                        b.AccountingObjectID   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from PPDiscountReturn a
                                          join PPDiscountReturnDetail b on a.ID = b.PPDiscountReturnID
                                          join GoodsServicePurchase c on c.ID = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                   and a.invoiceNo is not null
                                   and LEN(a.InvoiceNo) > 0
                                   and a.invoiceDate is not null
                                 union all
                                 --Hoá đơn mua dịch vụ
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo               as invoiceNo,
                                        InvoiceDate             as invoiceDate,
                                        a.AccountingObjectName  as accountingObjectName,
                                        a.CompanyTaxCode        as companyTaxCode,
                                        a.Reason                as description,
                                        Amount - DiscountAmount as amount,
                                        VATRate                 as vatRate,
                                        VATAmount               as vatAmount,
                                        VATAccount              as vatAccount,
                                        1                          flag,
                                        b.AccountingObjectID    as AccountingObjectID
                                         ,
                                        a.ID                    as RefID,
                                        a.TypeID                as TypeID,
                                        b.OrderPriority
                                 from PPService a
                                          join PPServiceDetail b on a.ID = b.PPServiceID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                   and b.invoiceNo is not null
                                   and LEN(b.InvoiceNo) > 0
                                   and b.invoiceDate is not null
-- 		union all
-- 		--hóa đơn mua TSCĐ tại module TSCĐ (màn Mua và ghi tăng TSCĐ)
-- 		select c.goodsServicePurchaseCode,c.goodsServicePurchaseName, InvoiceNo as invoiceNo, InvoiceDate as invoiceDate, a.AccountingObjectName as accountingObjectName, null as companyTaxCode,
-- 		a.Reason as description, (Amount - DiscountAmount) as amount, VATRate as vatRate, VATAmount as vatAmount, VATAccount as vatAccount,1 flag, b.AccountingObjectID as AccountingObjectID
-- 		, a.ID as RefID, a.TypeID as TypeID, b.OrderPriority
-- 		from FAIncrement a join FAIncrementDetail b on a.ID = b.FAIncrementID
-- 		join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
-- 		where a.PostedDate between @FromDate and @ToDate and Recorded = 1 and a.TypeID not in (510) and a.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent)) and a.TypeLedger in (@TypeLedger, 2) and b.invoiceNo is not null and LEN(b.InvoiceNo) > 0 and b.invoiceDate is not null
-- 		union all
-- 		-- hóa đơn mua CCDC tại module CCDC (màn Mua và ghi tăng CCDC)
-- 		select c.goodsServicePurchaseCode,c.goodsServicePurchaseName, InvoiceNo as invoiceNo, InvoiceDate as invoiceDate, a.AccountingObjectName as accountingObjectName, null as companyTaxCode,
-- 		a.Reason as description, (Amount - DiscountAmount) as amount, VATRate as vatRate, VATAmount as vatAmount, VATAccount as vatAccount	,1 flag, b.AccountingObjectID as AccountingObjectID
-- 		, a.ID as RefID, a.TypeID as TypeID, b.OrderPriority
-- 		from TIIncrement a join TIIncrementDetail b on a.ID = b.TIIncrementID
-- 		join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
-- 		where a.PostedDate between @FromDate and @ToDate and Recorded = 1 and a.TypeID not in (907) and a.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent)) and a.TypeLedger in (@TypeLedger, 2) and b.invoiceNo is not null and LEN(b.InvoiceNo) > 0 and b.invoiceDate is not null
                                 union all
                                 --1.	CHỨNG TỪ NGHIỆP VỤ KHÁC
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        b.AccountingObjectName as accountingObjectName,
                                        b.TaxCode              as companyTaxCode,
                                        a.Reason               as description,
                                        PretaxAmount           as amount,
                                        VATRate                as vatRate,
                                        VATAmount              as vatAmount,
                                        VATAccount             as vatAccount,
                                        1                         flag,
                                        NULL                   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from GOtherVoucher a
                                          join GOtherVoucherDetailTax b on a.ID = b.GOtherVoucherID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                   and b.InvoiceNo is not null
                                   and LEN(b.InvoiceNo) > 0
                                   and b.InvoiceDate is not null
                                 union all
                                 --2.	PHIẾU CHI
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        b.AccountingObjectName as accountingObjectName,
                                        b.TaxCode              as companyTaxCode,
                                        a.Reason               as description,
                                        PretaxAmount           as amount,
                                        VATRate                as vatRate,
                                        b.VATAmount            as vatAmount,
                                        VATAccount             as vatAccount,
                                        1                         flag,
                                        NULL                   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from MCPayment a
                                          join MCPaymentDetailTax b on a.ID = b.MCPaymentID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and a.Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                   and b.InvoiceNo is not null
                                   and LEN(b.InvoiceNo) > 0
                                   and b.InvoiceDate is not null
                                 union all
                                 --3.	UNC/SÉC CK/SÉC TM
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        b.AccountingObjectName as accountingObjectName,
                                        b.TaxCode              as companyTaxCode,
                                        a.Reason               as description,
                                        PretaxAmount           as amount,
                                        VATRate                as vatRate,
                                        b.VATAmount            as vatAmount,
                                        VATAccount             as vatAccount,
                                        1                         flag,
                                        NULL                   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from MBTellerPaper a
                                          join MBTellerPaperDetailTax b on a.ID = b.MBTellerPaperID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and a.Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                   and b.InvoiceNo is not null
                                   and LEN(b.InvoiceNo) > 0
                                   and b.InvoiceDate is not null
                                 union all
                                 --4.	THẺ TÍN DỤNG
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        b.AccountingObjectName as accountingObjectName,
                                        b.TaxCode              as companyTaxCode,
                                        a.Reason               as description,
                                        PretaxAmount           as amount,
                                        VATRate                as vatRate,
                                        b.VATAmount            as vatAmount,
                                        VATAccount             as vatAccount,
                                        1                         flag,
                                        NULL                   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from MBCreditCard a
                                          join MBCreditCardDetailTax b on a.ID = b.MBCreditCardID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and a.Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                   and b.InvoiceNo is not null
                                   and LEN(b.InvoiceNo) > 0
                                   and b.InvoiceDate is not null
                             ) AS #RS
                        group by goodsServicePurchaseCode, goodsServicePurchaseName, invoiceNo, invoiceDate, accountingObjectName, companyTaxCode, description, vatRate, vatAmount, vatAccount, flag, AccountingObjectID, RefID, TypeID
                    end
                --Hautv edit b.Description thành a.Reason sửa lỗi cộng gộp tên mặt hàng

                /*edit by Hautv */
-- 		UPDATE @tbDataLocal SET accountingObjectName = d.AccountingObjectName
-- 		FROM (select * from AccountingObject) d
-- 			where AccountingObjectID = d.ID;
                ------------
                select aa.goodsServicePurchaseCode,
                       aa.goodsServicePurchaseName,
                       aa.invoiceNo,
                       aa.invoiceDate,
                       aa.accountingObjectName,
                       aa.companyTaxCode,
                       aa.description,
                       sum(aa.amount)    amount,
                       aa.vatRate,
                       --sum(aa.vatRate) vatRate,
                       sum(aa.vatAmount) vatAmount,
                       aa.vatAccount,
                       aa.OrderPriority,
                       flag
                       --aa.TypeID,
                       --aa.RefID
                from (
                         select *
                         from @tbDataLocal
                         where (amount is not null and amount != 0) or (vatAmount is not null and vatAmount != 0)
                     ) aa
                     --update by namnh 19/05/2020
                WHERE goodsServicePurchaseCode IN ('1', '2', '3')
                group by goodsServicePurchaseCode,
                         goodsServicePurchaseName,
                         invoiceNo,
                         invoiceDate,
                         accountingObjectName,
                         companyTaxCode,
                         description,
                         vatAccount,
                         flag,
                         aa.vatRate,
                         aa.OrderPriority
                         --TypeID,
                         --RefID
                order by aa.goodsServicePurchaseCode, aa.invoiceDate, aa.invoiceNo
            End

    end
go

ALTER PROCEDURE [dbo].[PROC_BANG_KE_MUA_VAO]
    (
        @CompanyId UNIQUEIDENTIFIER ,
        @FromDate DATETIME,
        @ToDate DATETIME ,
        @IsSimilarBranch BIT ,
        @TypeLedger BIT,
        @IsBill BIT = 0,
        @isDependent BIT = 0
        )
    AS
    BEGIN
        -- SET NOCOUNT ON added to prevent extra result sets from
        SET NOCOUNT ON;
        --Proc_GetBkeInv_Buy null,0,'1/1/2019','12/31/2019',0
/*add by cuongpv de xu ly cach thong nhat lam tron*/
        DECLARE @tbDataLocal TABLE
                             (
                                 goodsServicePurchaseCode NVARCHAR(25) COLLATE SQL_Latin1_General_CP1_CI_AS,
                                 goodsServicePurchaseName NVARCHAR(512) COLLATE SQL_Latin1_General_CP1_CI_AS,
                                 invoiceNo                NVARCHAR(25) COLLATE SQL_Latin1_General_CP1_CI_AS,
                                 invoiceDate              DATETIME,
                                 accountingObjectName     NVARCHAR(512) COLLATE SQL_Latin1_General_CP1_CI_AS,
                                 companyTaxCode           NVARCHAR(50) COLLATE SQL_Latin1_General_CP1_CI_AS,
                                 description              NVARCHAR(512),
                                 amount                   decimal(25, 0),
                                 vatRate                  decimal(25, 10),
                                 vatAmount                decimal(25, 0),
                                 vatAccount               NVARCHAR(25) COLLATE SQL_Latin1_General_CP1_CI_AS,
                                 flag                     int,
                                 AccountingObjectID       UNIQUEIDENTIFIER,
                                 RefID                    UNIQUEIDENTIFIER,
                                 TypeID                   int,
                                 OrderPriority            int
                             )
/*end add by cuongpv*/
        if @IsSimilarBranch = 0
            Begin
                /*add by cuongpv*/
                --lay du lieu tu DB da lam tron tung dong
                if @IsBill = 1
                    BEGIN
                        INSERT INTO @tbDataLocal
                            /*end add by cuongpv*/
                            --Mua hàng --> Nhận hóa đơn mua hàng
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo                   as invoiceNo,
                               InvoiceDate                 as invoiceDate,
                               a.AccountingObjectName      as accountingObjectName,
                               CompanyTaxCode              as companyTaxCode,
                               b.Description               as description,
                               b.Amount - b.DiscountAmount as amount,
                               VATRate                     as vatRate,
                               VATAmount                   as vatAmount,
                               VATAccount                  as vatAccount,
                               1                              flag,
                               b.AccountingObjectID        as AccountingObjectID
                                ,
                               a.ID                        as RefID,
                               a.TypeID                    as TypeID,
                               b.OrderPriority
                        from PPInvoice a
                                 join PPInvoiceDetail b on a.ID = b.PPInvoiceID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                        union all
                        --Trả lại hàng mua, Giảm giá hàng mua --> Cty phải xuất hóa đơn cho người bán --> Giá trị trên mẫu báo cáo là số tiền âm (Mẫu báo cáo theo hình ở dưới)
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               a.AccountingObjectName as accountingObjectName,
                               CompanyTaxCode         as companyTaxCode,
                               b.Description          as description,
                               (0 - Amount)           as GiatriHH_chuathue,
                               VATRate                as vatRate,
                               (0 - VATAmount)        as vatAmount,
                               VATAccount             as vatAccount,
                               -1                        flag,
                               b.AccountingObjectID   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from PPDiscountReturn a
                                 join PPDiscountReturnDetail b on a.ID = b.PPDiscountReturnID
                                 join GoodsServicePurchase c on c.ID = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                        union all
                        --Hoá đơn mua dịch vụ
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo                   as invoiceNo,
                               InvoiceDate                 as invoiceDate,
                               a.AccountingObjectName      as accountingObjectName,
                               a.CompanyTaxCode            as companyTaxCode,
                               b.Description               as description,
                               b.Amount - b.DiscountAmount as amount,
                               VATRate                     as vatRate,
                               VATAmount                   as vatAmount,
                               VATAccount                  as vatAccount,
                               1                              flag,
                               b.AccountingObjectID        as AccountingObjectID
                                ,
                               a.ID                        as RefID,
                               a.TypeID                    as TypeID,
                               b.OrderPriority
                        from PPService a
                                 join PPServiceDetail b on a.ID = b.PPServiceID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
-- 		union all
-- 		--hóa đơn mua TSCĐ tại module TSCĐ (màn Mua và ghi tăng TSCĐ) edit by cuongpv 20191114: (Amount - DiscountAmount) as amount
-- 		select c.goodsServicePurchaseCode,c.goodsServicePurchaseName, InvoiceNo as invoiceNo, InvoiceDate as invoiceDate, a.AccountingObjectName as accountingObjectName, null as companyTaxCode,
-- 		b.Description as description, (Amount - DiscountAmount) as amount, VATRate as vatRate, VATAmount as vatAmount, VATAccount as vatAccount,1 flag, b.AccountingObjectID as AccountingObjectID
-- 		, a.ID as RefID, a.TypeID as TypeID, b.OrderPriority
-- 		from FAIncrement a join FAIncrementDetail b on a.ID = b.FAIncrementID
-- 		join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
-- 		where a.PostedDate between @FromDate and @ToDate and Recorded = 1 and a.TypeID not in (510) and a.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent)) and a.TypeLedger in (@TypeLedger, 2)
-- 		union all
-- 		-- hóa đơn mua CCDC tại module CCDC (màn Mua và ghi tăng CCDC)
-- 		select c.goodsServicePurchaseCode,c.goodsServicePurchaseName, InvoiceNo as invoiceNo, InvoiceDate as invoiceDate, a.AccountingObjectName as accountingObjectName, null as companyTaxCode,
-- 		b.Description as description, (Amount - DiscountAmount) as amount, VATRate as vatRate, VATAmount as vatAmount, VATAccount as vatAccount	,1 flag, b.AccountingObjectID as AccountingObjectID
-- 		, a.ID as RefID, a.TypeID as TypeID, b.OrderPriority
-- 		from TIIncrement a join TIIncrementDetail b on a.ID = b.TIIncrementID
-- 		join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
-- 		where a.PostedDate between @FromDate and @ToDate and Recorded = 1 and a.TypeID not in (907) and a.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent)) and a.TypeLedger in (@TypeLedger, 2)
                        union all
                        --1.	CHỨNG TỪ NGHIỆP VỤ KHÁC
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               b.AccountingObjectName as accountingObjectName,
                               b.TaxCode              as companyTaxCode,
                               b.Description          as description,
                               PretaxAmount           as amount,
                               VATRate                as vatRate,
                               VATAmount              as vatAmount,
                               VATAccount             as vatAccount,
                               1                         flag,
                               NULL                   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from GOtherVoucher a
                                 join GOtherVoucherDetailTax b on a.ID = b.GOtherVoucherID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                        union all
                        --2.	PHIẾU CHI
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               b.AccountingObjectName as accountingObjectName,
                               b.TaxCode              as companyTaxCode,
                               b.Description          as description,
                               PretaxAmount           as amount,
                               VATRate                as vatRate,
                               b.VATAmount            as vatAmount,
                               VATAccount             as vatAccount,
                               1                         flag,
                               NULL                   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from MCPayment a
                                 join MCPaymentDetailTax b on a.ID = b.MCPaymentID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and a.Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                        union all
                        --3.	UNC/SÉC CK/SÉC TM
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               b.AccountingObjectName as accountingObjectName,
                               b.TaxCode              as companyTaxCode,
                               b.Description          as description,
                               PretaxAmount           as amount,
                               VATRate                as vatRate,
                               b.VATAmount            as vatAmount,
                               VATAccount             as vatAccount,
                               1                         flag,
                               NULL                   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from MBTellerPaper a
                                 join MBTellerPaperDetailTax b on a.ID = b.MBTellerPaperID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and a.Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                        union all
                        --4.	THẺ TÍN DỤNG
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               b.AccountingObjectName as accountingObjectName,
                               b.TaxCode              as companyTaxCode,
                               b.Description          as description,
                               PretaxAmount           as amount,
                               VATRate                as vatRate,
                               b.VATAmount            as vatAmount,
                               VATAccount             as vatAccount,
                               1                         flag,
                               NULL                   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from MBCreditCard a
                                 join MBCreditCardDetailTax b on a.ID = b.MBCreditCardID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and a.Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2);
                    end
                else
                    BEGIN
                        INSERT INTO @tbDataLocal
                            /*end add by cuongpv*/
                            --Mua hàng --> Nhận hóa đơn mua hàng
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo                   as invoiceNo,
                               InvoiceDate                 as invoiceDate,
                               a.AccountingObjectName      as accountingObjectName,
                               CompanyTaxCode              as companyTaxCode,
                               b.Description               as description,
                               b.Amount - b.DiscountAmount as amount,
                               VATRate                     as vatRate,
                               VATAmount                   as vatAmount,
                               VATAccount                  as vatAccount,
                               1                              flag,
                               b.AccountingObjectID        as AccountingObjectID
                                ,
                               a.ID                        as RefID,
                               a.TypeID                    as TypeID,
                               b.OrderPriority
                        from PPInvoice a
                                 join PPInvoiceDetail b on a.ID = b.PPInvoiceID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                          and b.invoiceNo is not null
                          and b.invoiceDate is not null
                        union all
                        --Trả lại hàng mua, Giảm giá hàng mua --> Cty phải xuất hóa đơn cho người bán --> Giá trị trên mẫu báo cáo là số tiền âm (Mẫu báo cáo theo hình ở dưới)
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               a.AccountingObjectName as accountingObjectName,
                               CompanyTaxCode         as companyTaxCode,
                               b.Description          as description,
                               (0 - Amount)           as GiatriHH_chuathue,
                               VATRate                as vatRate,
                               (0 - VATAmount)        as vatAmount,
                               VATAccount             as vatAccount,
                               -1                        flag,
                               b.AccountingObjectID   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from PPDiscountReturn a
                                 join PPDiscountReturnDetail b on a.ID = b.PPDiscountReturnID
                                 join GoodsServicePurchase c on c.ID = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                          and a.invoiceNo is not null
                          and a.invoiceDate is not null
                        union all
                        --Hoá đơn mua dịch vụ
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo                   as invoiceNo,
                               InvoiceDate                 as invoiceDate,
                               a.AccountingObjectName      as accountingObjectName,
                               a.CompanyTaxCode            as companyTaxCode,
                               b.Description               as description,
                               b.Amount - b.DiscountAmount as amount,
                               VATRate                     as vatRate,
                               VATAmount                   as vatAmount,
                               VATAccount                  as vatAccount,
                               1                              flag,
                               b.AccountingObjectID        as AccountingObjectID
                                ,
                               a.ID                        as RefID,
                               a.TypeID                    as TypeID,
                               b.OrderPriority
                        from PPService a
                                 join PPServiceDetail b on a.ID = b.PPServiceID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                          and b.invoiceNo is not null
                          and b.invoiceDate is not null
-- 		union all
-- 		--hóa đơn mua TSCĐ tại module TSCĐ (màn Mua và ghi tăng TSCĐ) edit by cuongpv 20191114: (Amount - DiscountAmount) as amount
-- 		select c.goodsServicePurchaseCode,c.goodsServicePurchaseName, InvoiceNo as invoiceNo, InvoiceDate as invoiceDate, a.AccountingObjectName as accountingObjectName, null as companyTaxCode,
-- 		b.Description as description, (Amount - DiscountAmount) as amount, VATRate as vatRate, VATAmount as vatAmount, VATAccount as vatAccount,1 flag, b.AccountingObjectID as AccountingObjectID
-- 		, a.ID as RefID, a.TypeID as TypeID, b.OrderPriority
-- 		from FAIncrement a join FAIncrementDetail b on a.ID = b.FAIncrementID
-- 		join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
-- 		where a.PostedDate between @FromDate and @ToDate and Recorded = 1 and a.TypeID not in (510) and a.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent)) and a.TypeLedger in (@TypeLedger, 2) and b.invoiceNo is not null and b.invoiceDate is not null
-- 		union all
-- 		-- hóa đơn mua CCDC tại module CCDC (màn Mua và ghi tăng CCDC)
-- 		select c.goodsServicePurchaseCode,c.goodsServicePurchaseName, InvoiceNo as invoiceNo, InvoiceDate as invoiceDate, a.AccountingObjectName as accountingObjectName, null as companyTaxCode,
-- 		b.Description as description, (Amount - DiscountAmount) as amount, VATRate as vatRate, VATAmount as vatAmount, VATAccount as vatAccount	,1 flag, b.AccountingObjectID as AccountingObjectID
-- 		, a.ID as RefID, a.TypeID as TypeID, b.OrderPriority
-- 		from TIIncrement a join TIIncrementDetail b on a.ID = b.TIIncrementID
-- 		join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
-- 		where a.PostedDate between @FromDate and @ToDate and Recorded = 1 and a.TypeID not in (907) and a.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent)) and a.TypeLedger in (@TypeLedger, 2) and b.invoiceNo is not null and b.invoiceDate is not null
                        union all
                        --1.	CHỨNG TỪ NGHIỆP VỤ KHÁC
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               b.AccountingObjectName as accountingObjectName,
                               b.TaxCode              as companyTaxCode,
                               b.Description          as description,
                               PretaxAmount           as amount,
                               VATRate                as vatRate,
                               VATAmount              as vatAmount,
                               VATAccount             as vatAccount,
                               1                         flag,
                               NULL                   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from GOtherVoucher a
                                 join GOtherVoucherDetailTax b on a.ID = b.GOtherVoucherID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                          and b.InvoiceNo is not null
                          and LEN(b.InvoiceNo) > 0
                          and b.InvoiceDate is not null
                        union all
                        --2.	PHIẾU CHI
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               b.AccountingObjectName as accountingObjectName,
                               b.TaxCode              as companyTaxCode,
                               b.Description          as description,
                               PretaxAmount           as amount,
                               VATRate                as vatRate,
                               b.VATAmount            as vatAmount,
                               VATAccount             as vatAccount,
                               1                         flag,
                               NULL                   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from MCPayment a
                                 join MCPaymentDetailTax b on a.ID = b.MCPaymentID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and a.Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                          and b.InvoiceNo is not null
                          and LEN(b.InvoiceNo) > 0
                          and b.InvoiceDate is not null
                        union all
                        --3.	UNC/SÉC CK/SÉC TM
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               b.AccountingObjectName as accountingObjectName,
                               b.TaxCode              as companyTaxCode,
                               b.Description          as description,
                               PretaxAmount           as amount,
                               VATRate                as vatRate,
                               b.VATAmount            as vatAmount,
                               VATAccount             as vatAccount,
                               1                         flag,
                               NULL                   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from MBTellerPaper a
                                 join MBTellerPaperDetailTax b on a.ID = b.MBTellerPaperID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and a.Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                          and b.InvoiceNo is not null
                          and LEN(b.InvoiceNo) > 0
                          and b.InvoiceDate is not null
                        union all
                        --4.	THẺ TÍN DỤNG
                        select c.goodsServicePurchaseCode,
                               c.goodsServicePurchaseName,
                               InvoiceNo              as invoiceNo,
                               InvoiceDate            as invoiceDate,
                               b.AccountingObjectName as accountingObjectName,
                               b.TaxCode              as companyTaxCode,
                               b.Description          as description,
                               PretaxAmount           as amount,
                               VATRate                as vatRate,
                               b.VATAmount            as vatAmount,
                               VATAccount             as vatAccount,
                               1                         flag,
                               NULL                   as AccountingObjectID
                                ,
                               a.ID                   as RefID,
                               a.TypeID               as TypeID,
                               b.OrderPriority
                        from MBCreditCard a
                                 join MBCreditCardDetailTax b on a.ID = b.MBCreditCardID
                                 join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                        where a.PostedDate between @FromDate and @ToDate
                          and a.Recorded = 1
                          and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                          and a.TypeLedger in (@TypeLedger, 2)
                          and b.InvoiceNo is not null
                          and LEN(b.InvoiceNo) > 0
                          and b.InvoiceDate is not null;
                    end

                /*edit by Hautv */
-- 		UPDATE @tbDataLocal SET accountingObjectName = d.AccountingObjectName
-- 		FROM (select * from AccountingObject xca) d
-- 			where AccountingObjectID = d.ID;
                ------------

                /*edit by cuongpv order by c.goodsServicePurchaseCode, InvoiceDate, InvoiceNo -> SELECT * FROM @tbDataLocal order by goodsServicePurchaseCode, invoiceDate, invoiceNo;*/

                SELECT *
                FROM @tbDataLocal
                where (vatAmount is not null and vatAmount != 0)
                   or (amount is not null and amount != 0)
                order by goodsServicePurchaseCode, invoiceDate, invoiceNo;

            End
        else
            Begin
                /*add by cuongpv*/
                --lay du lieu tu DB da lam tron tung dong
                if @IsBill = 1
                    Begin
                        INSERT INTO @tbDataLocal
                            -- update by namnh 19/05/2020
                        select goodsServicePurchaseCode,
                               goodsServicePurchaseName,
                               invoiceNo,
                               invoiceDate,
                               accountingObjectName,
                               companyTaxCode,
                               description,
                               Sum(amount),
                               vatRate,
                               vatAmount,
                               vatAccount,
                               flag,
                               AccountingObjectID
                                ,
                               RefID,
                               TypeID,
                               0 as OrderPriority
                        FROM (
                                 /*end add by cuongpv*/
                                 --Mua hàng --> Nhận hóa đơn mua hàng
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo                 as invoiceNo,
                                        InvoiceDate               as invoiceDate,
                                        a.AccountingObjectName    as accountingObjectName,
                                        CompanyTaxCode            as companyTaxCode,
                                        a.Reason                  as description,
                                        (Amount - DiscountAmount) as amount,
                                        VATRate                   as vatRate,
                                        VATAmount                 as vatAmount,
                                        VATAccount                as vatAccount,
                                        1                            flag,
                                        b.AccountingObjectID      as AccountingObjectID
                                         ,
                                        a.ID                      as RefID,
                                        a.TypeID                  as TypeID,
                                        b.OrderPriority
                                 from PPInvoice a
                                          join PPInvoiceDetail b on a.ID = b.PPInvoiceID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                 union all
                                 --Trả lại hàng mua, Giảm giá hàng mua --> Cty phải xuất hóa đơn cho người bán --> Giá trị trên mẫu báo cáo là số tiền âm (Mẫu báo cáo theo hình ở dưới)
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        a.AccountingObjectName as accountingObjectName,
                                        CompanyTaxCode         as companyTaxCode,
                                        a.Reason               as description,
                                        (0 - Amount)           as GiatriHH_chuathue,
                                        VATRate                as vatRate,
                                        (0 - VATAmount)        as vatAmount,
                                        VATAccount             as vatAccount,
                                        -1                        flag,
                                        b.AccountingObjectID   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from PPDiscountReturn a
                                          join PPDiscountReturnDetail b on a.ID = b.PPDiscountReturnID
                                          join GoodsServicePurchase c on c.ID = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                 union all
                                 --Hoá đơn mua dịch vụ
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo               as invoiceNo,
                                        InvoiceDate             as invoiceDate,
                                        a.AccountingObjectName  as accountingObjectName,
                                        a.CompanyTaxCode        as companyTaxCode,
                                        a.Reason                as description,
                                        Amount - DiscountAmount as amount,
                                        VATRate                 as vatRate,
                                        VATAmount               as vatAmount,
                                        VATAccount              as vatAccount,
                                        1                          flag,
                                        b.AccountingObjectID    as AccountingObjectID
                                         ,
                                        a.ID                    as RefID,
                                        a.TypeID                as TypeID,
                                        b.OrderPriority
                                 from PPService a
                                          join PPServiceDetail b on a.ID = b.PPServiceID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
-- 		union all
-- 		--hóa đơn mua TSCĐ tại module TSCĐ (màn Mua và ghi tăng TSCĐ)
-- 		select c.goodsServicePurchaseCode,c.goodsServicePurchaseName, InvoiceNo as invoiceNo, InvoiceDate as invoiceDate, a.AccountingObjectName as accountingObjectName, null as companyTaxCode,
-- 		a.Reason as description, (Amount - DiscountAmount) as amount, VATRate as vatRate, VATAmount as vatAmount, VATAccount as vatAccount,1 flag, b.AccountingObjectID as AccountingObjectID
-- 		, a.ID as RefID, a.TypeID as TypeID, b.OrderPriority
-- 		from FAIncrement a join FAIncrementDetail b on a.ID = b.FAIncrementID
-- 		join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
-- 		where a.PostedDate between @FromDate and @ToDate and Recorded = 1 and a.TypeID not in (510) and a.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent)) and a.TypeLedger in (@TypeLedger, 2)
-- 		union all
-- 		-- hóa đơn mua CCDC tại module CCDC (màn Mua và ghi tăng CCDC)
-- 		select c.goodsServicePurchaseCode,c.goodsServicePurchaseName, InvoiceNo as invoiceNo, InvoiceDate as invoiceDate, a.AccountingObjectName as accountingObjectName, null as companyTaxCode,
-- 		a.Reason as description, (Amount - DiscountAmount) as amount, VATRate as vatRate, VATAmount as vatAmount, VATAccount as vatAccount	,1 flag, b.AccountingObjectID as AccountingObjectID
-- 		, a.ID as RefID, a.TypeID as TypeID, b.OrderPriority
-- 		from TIIncrement a join TIIncrementDetail b on a.ID = b.TIIncrementID
-- 		join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
-- 		where a.PostedDate between @FromDate and @ToDate and Recorded = 1 and a.TypeID not in (907) and a.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent)) and a.TypeLedger in (@TypeLedger, 2)
                                 union all
                                 --1.	CHỨNG TỪ NGHIỆP VỤ KHÁC
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        b.AccountingObjectName as accountingObjectName,
                                        b.TaxCode              as companyTaxCode,
                                        a.Reason               as description,
                                        PretaxAmount           as amount,
                                        VATRate                as vatRate,
                                        VATAmount              as vatAmount,
                                        VATAccount             as vatAccount,
                                        1                         flag,
                                        NULL                   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from GOtherVoucher a
                                          join GOtherVoucherDetailTax b on a.ID = b.GOtherVoucherID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                 union all
                                 --2.	PHIẾU CHI
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        b.AccountingObjectName as accountingObjectName,
                                        b.TaxCode              as companyTaxCode,
                                        a.Reason               as description,
                                        PretaxAmount           as amount,
                                        VATRate                as vatRate,
                                        b.VATAmount            as vatAmount,
                                        VATAccount             as vatAccount,
                                        1                         flag,
                                        NULL                   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from MCPayment a
                                          join MCPaymentDetailTax b on a.ID = b.MCPaymentID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and a.Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                 union all
                                 --3.	UNC/SÉC CK/SÉC TM
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        b.AccountingObjectName as accountingObjectName,
                                        b.TaxCode              as companyTaxCode,
                                        a.Reason               as description,
                                        PretaxAmount           as amount,
                                        VATRate                as vatRate,
                                        b.VATAmount            as vatAmount,
                                        VATAccount             as vatAccount,
                                        1                         flag,
                                        NULL                   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from MBTellerPaper a
                                          join MBTellerPaperDetailTax b on a.ID = b.MBTellerPaperID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and a.Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                 union all
                                 --4.	THẺ TÍN DỤNG
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        b.AccountingObjectName as accountingObjectName,
                                        b.TaxCode              as companyTaxCode,
                                        a.Reason               as description,
                                        PretaxAmount           as amount,
                                        VATRate                as vatRate,
                                        b.VATAmount            as vatAmount,
                                        VATAccount             as vatAccount,
                                        1                         flag,
                                        NULL                   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from MBCreditCard a
                                          join MBCreditCardDetailTax b on a.ID = b.MBCreditCardID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and a.Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                             ) as #RS
                        GROUP BY goodsServicePurchaseCode, goodsServicePurchaseName, invoiceNo, invoiceDate,
                                 accountingObjectName, companyTaxCode, description, vatRate, vatAmount, vatAccount,
                                 flag,
                                 AccountingObjectID, RefID, TypeID
                    end
                else
                    begin
                        INSERT INTO @tbDataLocal
                        -- update by namnh 19/05/2020
                        SELECT goodsServicePurchaseCode,
                               goodsServicePurchaseName,
                               invoiceNo,
                               invoiceDate,
                               accountingObjectName,
                               companyTaxCode,
                               description,
                               Sum(amount),
                               vatRate,
                               vatAmount,
                               vatAccount,
                               flag,
                               AccountingObjectID
                                ,
                               RefID,
                               TypeID,
                               0 as OrderPriority
                        FROM (
                                 /*end add by cuongpv*/
                                 --Mua hàng --> Nhận hóa đơn mua hàng
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo                 as invoiceNo,
                                        InvoiceDate               as invoiceDate,
                                        a.AccountingObjectName    as accountingObjectName,
                                        CompanyTaxCode            as companyTaxCode,
                                        a.Reason                  as description,
                                        (Amount - DiscountAmount) as amount,
                                        VATRate                   as vatRate,
                                        VATAmount                 as vatAmount,
                                        VATAccount                as vatAccount,
                                        1                            flag,
                                        b.AccountingObjectID      as AccountingObjectID
                                         ,
                                        a.ID                      as RefID,
                                        a.TypeID                  as TypeID,
                                        b.OrderPriority
                                 from PPInvoice a
                                          join PPInvoiceDetail b on a.ID = b.PPInvoiceID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                   and b.invoiceNo is not null
                                   and LEN(b.InvoiceNo) > 0
                                   and b.invoiceDate is not null
                                 union all
                                 --Trả lại hàng mua, Giảm giá hàng mua --> Cty phải xuất hóa đơn cho người bán --> Giá trị trên mẫu báo cáo là số tiền âm (Mẫu báo cáo theo hình ở dưới)
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        a.AccountingObjectName as accountingObjectName,
                                        CompanyTaxCode         as companyTaxCode,
                                        a.Reason               as description,
                                        (0 - Amount)           as GiatriHH_chuathue,
                                        VATRate                as vatRate,
                                        (0 - VATAmount)        as vatAmount,
                                        VATAccount             as vatAccount,
                                        -1                        flag,
                                        b.AccountingObjectID   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from PPDiscountReturn a
                                          join PPDiscountReturnDetail b on a.ID = b.PPDiscountReturnID
                                          join GoodsServicePurchase c on c.ID = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                   and a.invoiceNo is not null
                                   and LEN(a.InvoiceNo) > 0
                                   and a.invoiceDate is not null
                                 union all
                                 --Hoá đơn mua dịch vụ
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo               as invoiceNo,
                                        InvoiceDate             as invoiceDate,
                                        a.AccountingObjectName  as accountingObjectName,
                                        a.CompanyTaxCode        as companyTaxCode,
                                        a.Reason                as description,
                                        Amount - DiscountAmount as amount,
                                        VATRate                 as vatRate,
                                        VATAmount               as vatAmount,
                                        VATAccount              as vatAccount,
                                        1                          flag,
                                        b.AccountingObjectID    as AccountingObjectID
                                         ,
                                        a.ID                    as RefID,
                                        a.TypeID                as TypeID,
                                        b.OrderPriority
                                 from PPService a
                                          join PPServiceDetail b on a.ID = b.PPServiceID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                   and b.invoiceNo is not null
                                   and LEN(b.InvoiceNo) > 0
                                   and b.invoiceDate is not null
-- 		union all
-- 		--hóa đơn mua TSCĐ tại module TSCĐ (màn Mua và ghi tăng TSCĐ)
-- 		select c.goodsServicePurchaseCode,c.goodsServicePurchaseName, InvoiceNo as invoiceNo, InvoiceDate as invoiceDate, a.AccountingObjectName as accountingObjectName, null as companyTaxCode,
-- 		a.Reason as description, (Amount - DiscountAmount) as amount, VATRate as vatRate, VATAmount as vatAmount, VATAccount as vatAccount,1 flag, b.AccountingObjectID as AccountingObjectID
-- 		, a.ID as RefID, a.TypeID as TypeID, b.OrderPriority
-- 		from FAIncrement a join FAIncrementDetail b on a.ID = b.FAIncrementID
-- 		join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
-- 		where a.PostedDate between @FromDate and @ToDate and Recorded = 1 and a.TypeID not in (510) and a.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent)) and a.TypeLedger in (@TypeLedger, 2) and b.invoiceNo is not null and LEN(b.InvoiceNo) > 0 and b.invoiceDate is not null
-- 		union all
-- 		-- hóa đơn mua CCDC tại module CCDC (màn Mua và ghi tăng CCDC)
-- 		select c.goodsServicePurchaseCode,c.goodsServicePurchaseName, InvoiceNo as invoiceNo, InvoiceDate as invoiceDate, a.AccountingObjectName as accountingObjectName, null as companyTaxCode,
-- 		a.Reason as description, (Amount - DiscountAmount) as amount, VATRate as vatRate, VATAmount as vatAmount, VATAccount as vatAccount	,1 flag, b.AccountingObjectID as AccountingObjectID
-- 		, a.ID as RefID, a.TypeID as TypeID, b.OrderPriority
-- 		from TIIncrement a join TIIncrementDetail b on a.ID = b.TIIncrementID
-- 		join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
-- 		where a.PostedDate between @FromDate and @ToDate and Recorded = 1 and a.TypeID not in (907) and a.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent)) and a.TypeLedger in (@TypeLedger, 2) and b.invoiceNo is not null and LEN(b.InvoiceNo) > 0 and b.invoiceDate is not null
                                 union all
                                 --1.	CHỨNG TỪ NGHIỆP VỤ KHÁC
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        b.AccountingObjectName as accountingObjectName,
                                        b.TaxCode              as companyTaxCode,
                                        a.Reason               as description,
                                        PretaxAmount           as amount,
                                        VATRate                as vatRate,
                                        VATAmount              as vatAmount,
                                        VATAccount             as vatAccount,
                                        1                         flag,
                                        NULL                   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from GOtherVoucher a
                                          join GOtherVoucherDetailTax b on a.ID = b.GOtherVoucherID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                   and b.InvoiceNo is not null
                                   and LEN(b.InvoiceNo) > 0
                                   and b.InvoiceDate is not null
                                 union all
                                 --2.	PHIẾU CHI
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        b.AccountingObjectName as accountingObjectName,
                                        b.TaxCode              as companyTaxCode,
                                        a.Reason               as description,
                                        PretaxAmount           as amount,
                                        VATRate                as vatRate,
                                        b.VATAmount            as vatAmount,
                                        VATAccount             as vatAccount,
                                        1                         flag,
                                        NULL                   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from MCPayment a
                                          join MCPaymentDetailTax b on a.ID = b.MCPaymentID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and a.Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                   and b.InvoiceNo is not null
                                   and LEN(b.InvoiceNo) > 0
                                   and b.InvoiceDate is not null
                                 union all
                                 --3.	UNC/SÉC CK/SÉC TM
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        b.AccountingObjectName as accountingObjectName,
                                        b.TaxCode              as companyTaxCode,
                                        a.Reason               as description,
                                        PretaxAmount           as amount,
                                        VATRate                as vatRate,
                                        b.VATAmount            as vatAmount,
                                        VATAccount             as vatAccount,
                                        1                         flag,
                                        NULL                   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from MBTellerPaper a
                                          join MBTellerPaperDetailTax b on a.ID = b.MBTellerPaperID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and a.Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                   and b.InvoiceNo is not null
                                   and LEN(b.InvoiceNo) > 0
                                   and b.InvoiceDate is not null
                                 union all
                                 --4.	THẺ TÍN DỤNG
                                 select c.goodsServicePurchaseCode,
                                        c.goodsServicePurchaseName,
                                        InvoiceNo              as invoiceNo,
                                        InvoiceDate            as invoiceDate,
                                        b.AccountingObjectName as accountingObjectName,
                                        b.TaxCode              as companyTaxCode,
                                        a.Reason               as description,
                                        PretaxAmount           as amount,
                                        VATRate                as vatRate,
                                        b.VATAmount            as vatAmount,
                                        VATAccount             as vatAccount,
                                        1                         flag,
                                        NULL                   as AccountingObjectID
                                         ,
                                        a.ID                   as RefID,
                                        a.TypeID               as TypeID,
                                        b.OrderPriority
                                 from MBCreditCard a
                                          join MBCreditCardDetailTax b on a.ID = b.MBCreditCardID
                                          join GoodsServicePurchase c on c.id = b.GoodsServicePurchaseID
                                 where a.PostedDate between @FromDate and @ToDate
                                   and a.Recorded = 1
                                   and a.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                                   and a.TypeLedger in (@TypeLedger, 2)
                                   and b.InvoiceNo is not null
                                   and LEN(b.InvoiceNo) > 0
                                   and b.InvoiceDate is not null
                             ) AS #RS
                        group by goodsServicePurchaseCode, goodsServicePurchaseName, invoiceNo, invoiceDate, accountingObjectName, companyTaxCode, description, vatRate, vatAmount, vatAccount, flag, AccountingObjectID, RefID, TypeID
                    end
                --Hautv edit b.Description thành a.Reason sửa lỗi cộng gộp tên mặt hàng

                /*edit by Hautv */
-- 		UPDATE @tbDataLocal SET accountingObjectName = d.AccountingObjectName
-- 		FROM (select * from AccountingObject) d
-- 			where AccountingObjectID = d.ID;
                ------------
                select aa.goodsServicePurchaseCode,
                       aa.goodsServicePurchaseName,
                       aa.invoiceNo,
                       aa.invoiceDate,
                       aa.accountingObjectName,
                       aa.companyTaxCode,
                       aa.description,
                       sum(aa.amount)    amount,
                       aa.vatRate,
                       --sum(aa.vatRate) vatRate,
                       sum(aa.vatAmount) vatAmount,
                       aa.vatAccount,
                       aa.OrderPriority,
                       flag
                       --aa.TypeID,
                       --aa.RefID
                from (
                         select *
                         from @tbDataLocal
                         where (amount is not null and amount != 0) or (vatAmount is not null and vatAmount != 0)
                     ) aa
                     --update by namnh 19/05/2020
                WHERE goodsServicePurchaseCode IN ('1', '2', '3')
                group by goodsServicePurchaseCode,
                         goodsServicePurchaseName,
                         invoiceNo,
                         invoiceDate,
                         accountingObjectName,
                         companyTaxCode,
                         description,
                         vatAccount,
                         flag,
                         aa.vatRate,
                         aa.OrderPriority
                         --TypeID,
                         --RefID
                order by aa.goodsServicePurchaseCode, aa.invoiceDate, aa.invoiceNo
            End

    end
go

