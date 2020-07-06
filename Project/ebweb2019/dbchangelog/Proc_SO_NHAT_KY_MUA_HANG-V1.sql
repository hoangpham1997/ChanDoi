/*
-- =============================================
-- Edit by:   congnd
-- Edit date: 03.05.2019
-- Description: Báo cáo sổ nhật ký mua hàng
-- EXEC EXEC [Proc_SO_NHAT_KY_MUA_HANG] @FromDate = '2020-03-01', @ToDate = '2020-03-09', @IsNotPaid = false, @CompanyID = '5A814271-A115-41D1-BA4D-C50BC0040482', @IsMBook = false;
-- =============================================
*/
CREATE PROCEDURE [dbo].[Proc_SO_NHAT_KY_MUA_HANG] @FromDate nvarchar(25),
    @ToDate nvarchar(25),
    @IsNotPaid BIT = 0,
    @CompanyID UNIQUEIDENTIFIER = NULL,
    @IsMBook BIT = 0,
    @isDependent BIT = 0
    AS
    BEGIN

        DECLARE @tbDataGL TABLE
                          (
                              ID                   uniqueidentifier,
                              CompanyID            uniqueidentifier,
                              ReferenceID          uniqueidentifier,
                              TypeID               int,
                              Date                 datetime,
                              PostedDate           datetime,
                              No                   nvarchar(25),
                              InvoiceDate          datetime,
                              InvoiceNo            nvarchar(25),
                              Account              nvarchar(25),
                              AccountCorresponding nvarchar(25),
                              BankAccountDetailID  uniqueidentifier,
                              CurrencyID           nvarchar(3),
                              ExchangeRate         decimal(25, 10),
                              DebitAmount          money,
                              DebitAmountOriginal  money,
                              CreditAmount         money,
                              CreditAmountOriginal money,
                              Reason               nvarchar(512),
                              Description          nvarchar(512),
                              AccountingObjectID   uniqueidentifier,
                              EmployeeID           uniqueidentifier,
                              BudgetItemID         uniqueidentifier,
                              CostSetID            uniqueidentifier,
                              ContractID           uniqueidentifier,
                              StatisticsCodeID     uniqueidentifier,
                              InvoiceSeries        nvarchar(25),
                              ContactName          nvarchar(512),
                              DetailID             uniqueidentifier,
                              DepartmentID         uniqueidentifier,
                              ExpenseItemID        uniqueidentifier,
                              OrderPriority        int
                          )


        if (@IsNotPaid = 0)
            Begin
                /*Add by cuongpv de sua cach lam tron*/

                INSERT INTO @tbDataGL(ID,
                                      CompanyID,
                                      ReferenceID,
                                      TypeID,
                                      Date,
                                      PostedDate,
                                      No,
                                      InvoiceDate,
                                      InvoiceNo,
                                      Account,
                                      AccountCorresponding,
                                      BankAccountDetailID,
                                      CurrencyID,
                                      ExchangeRate,
                                      DebitAmount,
                                      DebitAmountOriginal,
                                      CreditAmount,
                                      CreditAmountOriginal,
                                      Reason,
                                      Description,
                                      AccountingObjectID,
                                      EmployeeID,
                                      BudgetItemID,
                                      CostSetID,
                                      ContractID,
                                      StatisticsCodeID,
                                      InvoiceSeries,
                                      ContactName,
                                      DetailID,
                                      DepartmentID,
                                      ExpenseItemID,
                                      OrderPriority)
                SELECT GL.ID,
                       GL.CompanyID,
                       GL.ReferenceID,
                       GL.TypeID,
                       GL.Date,
                       GL.PostedDate,
                       CASE WHEN @IsMBook = 1 then GL.NoMBook else GL.NoFBook end as no,
                       GL.InvoiceDate,
                       GL.InvoiceNo,
                       GL.Account,
                       GL.AccountCorresponding,
                       GL.BankAccountDetailID,
                       GL.CurrencyID,
                       GL.ExchangeRate,
                       GL.DebitAmount,
                       GL.DebitAmountOriginal,
                       GL.CreditAmount,
                       GL.CreditAmountOriginal,
                       GL.Reason,
                       GL.Description,
                       GL.AccountingObjectID,
                       GL.EmployeeID,
                       GL.BudgetItemID,
                       GL.CostSetID,
                       GL.ContractID,
                       GL.StatisticsCodeID,
                       GL.InvoiceSeries,
                       GL.ContactName,
                       GL.DetailID,
                       GL.DepartmentID,
                       GL.ExpenseItemID,
                       GL.OrderPriority

                FROM dbo.GeneralLedger GL
                WHERE GL.PostedDate BETWEEN @FromDate AND @ToDate
                  and GL.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
                  and GL.TypeLedger in (@IsMBook, 2)
                /*end add by cuongpv*/

                select ngayGhiSo,
                       soCTu,
                       ngayCTu,
                       dienGiai,
                       sum(hangHoa)                                                                          as hangHoa,
                       CASE
                           WHEN TypeID in (211, 212, 213, 215, 214) then null
                           else sum(phaiTraNguoiBan) end                                                     as phaiTraNguoiBan,
                       sum(nguyenVatLieu)                                                                    as nguyenVatLieu,
                       sum(SoTien)                                                                           as SoTien,
                       account,
                       ReferenceID                                                                           as referenceID,
                       TypeID                                                                                as typeID
                from (
                         --             -Điều kiện để lấy giá trị Trường Hàng hóa (TK nợ = 156)
                         select a.PostedDate     as ngayGhiSo,
                                a.No             as soCTu,
                                a.Date           as ngayCTu,
                                b.Description    as dienGiai,
                                sum(DebitAmount) as hangHoa,
                                sum(DebitAmount) as phaiTraNguoiBan,
                                0                as nguyenVatLieu,
                                0                as SoTien,
                                null             as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join PPInvoiceDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account like '156%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                         union all
                         select a.PostedDate     as ngayGhiSo,
                                a.No             as soCTu,
                                a.Date           as ngayCTu,
                                b.Description    as dienGiai,
                                sum(DebitAmount) as hangHoa,
                                sum(DebitAmount) as phaiTraNguoiBan,
                                0                as nguyenVatLieu,
                                0                as SoTien,
                                null             as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join PPServiceDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account like '156%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                         union all
                         select a.PostedDate     as ngayGhiSo,
                                a.No             as soCTu,
                                a.Date           as ngayCTu,
                                b.Description    as dienGiai,
                                sum(DebitAmount) as hangHoa,
                                sum(DebitAmount) as phaiTraNguoiBan,
                                0                as nguyenVatLieu,
                                0                as SoTien,
                                null             as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join TIIncrementDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account like '156%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID

                         union all

                         select a.PostedDate      as ngayGhiSo,
                                a.No              as soCTu,
                                a.Date            as ngayCTu,
                                b.Description     as dienGiai,
                                0 - sum(CreditAmount) as hangHoa,
                                0 - sum(CreditAmount) as phaiTraNguoiBan,
                                0                 as nguyenVatLieu,
                                0                 as SoTien,
                                null              as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join PPDiscountReturnDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account like '156%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID

                         union all
                         ---Điều kiện để lấy giá trị Trường Nguyên liệu, vật liệu (TK nợ = 152)

                         select a.PostedDate     as ngayGhiSo,
                                a.No             as soCTu,
                                a.Date           as ngayCTu,
                                b.Description    as dienGiai,
                                0                as hangHoa,
                                sum(DebitAmount) as phaiTraNguoiBan,
                                sum(DebitAmount) as nguyenVatLieu,
                                0                as SoTien,
                                null             as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join PPInvoiceDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account like '152%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                         union all
                         select a.PostedDate     as ngayGhiSo,
                                a.No             as soCTu,
                                a.Date           as ngayCTu,
                                b.Description    as dienGiai,
                                0                as hangHoa,
                                sum(DebitAmount) as phaiTraNguoiBan,
                                sum(DebitAmount) as nguyenVatLieu,
                                0                as SoTien,
                                null             as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join PPServiceDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account like '152%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                         union all
                         select a.PostedDate     as ngayGhiSo,
                                a.No             as soCTu,
                                a.Date           as ngayCTu,
                                b.Description    as dienGiai,
                                0                as hangHoa,
                                sum(DebitAmount) as phaiTraNguoiBan,
                                sum(DebitAmount) as nguyenVatLieu,
                                0                as SoTien,
                                null             as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join TIIncrementDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account like '152%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                         union all
                         select a.PostedDate      as ngayGhiSo,
                                a.No              as soCTu,
                                a.Date            as ngayCTu,
                                b.Description     as dienGiai,
                                0                 as hangHoa,
                                0 - sum(CreditAmount) as phaiTraNguoiBan,
                                0 - sum(CreditAmount) as nguyenVatLieu,
                                0                 as SoTien,
                                null              as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join PPDiscountReturnDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account like '152%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                         union all
                         ---Điều kiện để lấy Tài khoản khác

                         select a.PostedDate     as ngayGhiSo,
                                a.No             as soCTu,
                                a.Date           as ngayCTu,
                                b.Description    as dienGiai,
                                0                as hangHoa,
                                sum(DebitAmount) as phaiTraNguoiBan,
                                0                as nguyenVatLieu,
                                sum(DebitAmount) as SoTien,
                                a.Account        as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join PPInvoiceDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account not like '156%'
                           and a.Account not like '152%'
                           and a.Account not like '3331%'
                           and a.Account not like '133%'
                           and a.Account not like '3333%'
                           and a.Account not like '3332%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                           and a.AccountCorresponding not like '3331%'
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                         union all
                         select a.PostedDate     as ngayGhiSo,
                                a.No             as soCTu,
                                a.Date           as ngayCTu,
                                b.Description    as dienGiai,
                                0                as hangHoa,
                                sum(DebitAmount) as phaiTraNguoiBan,
                                0                as nguyenVatLieu,
                                sum(DebitAmount) as SoTien,
                                a.Account        as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join PPServiceDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account not like '156%'
                           and a.Account not like '152%'
                           and a.Account not like '3331%'
                           and a.Account not like '133%'
                           and a.Account not like '3333%'
                           and a.Account not like '3332%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                           and a.AccountCorresponding not like '3331%'
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                         union all
                         select a.PostedDate     as ngayGhiSo,
                                a.No             as soCTu,
                                a.Date           as ngayCTu,
                                b.Description    as dienGiai,
                                0                as hangHoa,
                                sum(DebitAmount) as phaiTraNguoiBan,
                                0                as nguyenVatLieu,
                                sum(DebitAmount) as SoTien,
                                a.Account        as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join TIIncrementDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account not like '156%'
                           and a.Account not like '152%'
                           and a.Account not like '3331%'
                           and a.Account not like '133%'
                           and a.Account not like '3333%'
                           and a.Account not like '3332%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                           and a.AccountCorresponding not like '3331%'
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                         union all
                         select a.PostedDate      as ngayGhiSo,
                                a.No              as soCTu,
                                a.Date            as ngayCTu,
                                b.Description     as dienGiai,
                                0                 as hangHoa,
                                0 - sum(CreditAmount) as phaiTraNguoiBan,
                                0                 as nguyenVatLieu,
                                0 - sum(CreditAmount) as SoTien,
                                a.Account         as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join PPDiscountReturnDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account not like '156%'
                           and a.Account not like '152%'
                           and a.Account not like '3331%'
                           and a.Account not like '133%'
                           and a.Account not like '3333%'
                           and a.Account not like '3332%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                           and a.AccountCorresponding not like '3331%'
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                     ) t
                where (hangHoa != 0 or phaiTraNguoiBan != 0 or nguyenVatLieu != 0 or SoTien != 0)

                group by ngayGhiSo,
                         soCTu,
                         ngayCTu,
                         dienGiai,
                         account,
                         ReferenceID,
                         TypeID
                order by ngayGhiSo, soCTu

            END
        else
            Begin

                INSERT INTO @tbDataGL(ID,
                                      CompanyID,
                                      ReferenceID,
                                      TypeID,
                                      Date,
                                      PostedDate,
                                      No,
                                      InvoiceDate,
                                      InvoiceNo,
                                      Account,
                                      AccountCorresponding,
                                      BankAccountDetailID,
                                      CurrencyID,
                                      ExchangeRate,
                                      DebitAmount,
                                      DebitAmountOriginal,
                                      CreditAmount,
                                      CreditAmountOriginal,
                                      Reason,
                                      Description,
                                      AccountingObjectID,
                                      EmployeeID,
                                      BudgetItemID,
                                      CostSetID,
                                      ContractID,
                                      StatisticsCodeID,
                                      InvoiceSeries,
                                      ContactName,
                                      DetailID,
                                      DepartmentID,
                                      ExpenseItemID,
                                      OrderPriority)
                SELECT GL.ID,
                       GL.CompanyID,
                       GL.ReferenceID,
                       GL.TypeID,
                       GL.Date,
                       GL.PostedDate,
                       CASE WHEN @IsMBook = 1 then GL.NoMBook else GL.NoFBook end as no,
                       GL.InvoiceDate,
                       GL.InvoiceNo,
                       GL.Account,
                       GL.AccountCorresponding,
                       GL.BankAccountDetailID,
                       GL.CurrencyID,
                       GL.ExchangeRate,
                       GL.DebitAmount,
                       GL.DebitAmountOriginal,
                       GL.CreditAmount,
                       GL.CreditAmountOriginal,
                       GL.Reason,
                       GL.Description,
                       GL.AccountingObjectID,
                       GL.EmployeeID,
                       GL.BudgetItemID,
                       GL.CostSetID,
                       GL.ContractID,
                       GL.StatisticsCodeID,
                       GL.InvoiceSeries,
                       GL.ContactName,
                       GL.DetailID,
                       GL.DepartmentID,
                       GL.ExpenseItemID,
                       GL.OrderPriority
                FROM dbo.GeneralLedger GL
                WHERE GL.PostedDate between @FromDate and @ToDate
                  and GL.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
                  and GL.TypeLedger in (@IsMBook, 2)
                /*end add by cuongpv*/


                select ngayGhiSo,
                       soCTu,
                       ngayCTu,
                       dienGiai,
                       sum(hangHoa)                                                                          as hangHoa,
                       CASE
                           WHEN TypeID in (211, 212, 213, 215, 214) then null
                           else sum(phaiTraNguoiBan) end                                                     as phaiTraNguoiBan,
                       sum(nguyenVatLieu)                                                                    as nguyenVatLieu,
                       sum(SoTien)                                                                           as SoTien,
                       account,
                       ReferenceID                                                                           as referenceID,
                       TypeID                                                                                as typeID

                from (
                         ---Điều kiện để lấy giá trị Trường Hàng hóa (TK nợ = 156)
                         select a.PostedDate     as ngayGhiSo,
                                a.No             as soCTu,
                                a.Date           as ngayCTu,
                                b.Description    as dienGiai,
                                sum(DebitAmount) as hangHoa,
                                sum(DebitAmount) as phaiTraNguoiBan,
                                0                as nguyenVatLieu,
                                0                as SoTien,
                                null             as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join PPInvoiceDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account like '156%'
                           and a.AccountCorresponding like '331%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                           and a.TypeID in (210, 240, 430, 500)
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                         union all
                         select a.PostedDate     as ngayGhiSo,
                                a.No             as soCTu,
                                a.Date           as ngayCTu,
                                b.Description    as dienGiai,
                                sum(DebitAmount) as hangHoa,
                                sum(DebitAmount) as phaiTraNguoiBan,
                                0                as nguyenVatLieu,
                                0                as SoTien,
                                null             as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join PPServiceDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account like '156%'
                           and a.AccountCorresponding like '331%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                           and a.TypeID in (210, 240, 430, 500)
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                         union all
                         select a.PostedDate     as ngayGhiSo,
                                a.No             as soCTu,
                                a.Date           as ngayCTu,
                                b.Description    as dienGiai,
                                sum(DebitAmount) as hangHoa,
                                sum(DebitAmount) as phaiTraNguoiBan,
                                0                as nguyenVatLieu,
                                0                as SoTien,
                                null             as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join TIIncrementDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account like '156%'
                           and a.AccountCorresponding like '331%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                           and a.TypeID in (210, 240, 430, 500)
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID

                         union all
                         select a.PostedDate      as ngayGhiSo,
                                a.No              as soCTu,
                                a.Date            as ngayCTu,
                                b.Description     as dienGiai,
                                0 - sum(CreditAmount) as hangHoa,
                                0 - sum(CreditAmount) as phaiTraNguoiBan,
                                0                 as nguyenVatLieu,
                                0                 as SoTien,
                                null              as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join PPDiscountReturnDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account like '156%'
                           and a.AccountCorresponding like '331%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                           and a.TypeID in (210, 240, 430, 500)
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID

                         union all
                         ---Điều kiện để lấy giá trị Trường Nguyên liệu, vật liệu (TK nợ = 152)

                         select a.PostedDate     as ngayGhiSo,
                                a.No             as soCTu,
                                a.Date           as ngayCTu,
                                b.Description    as dienGiai,
                                0                as hangHoa,
                                sum(DebitAmount) as phaiTraNguoiBan,
                                sum(DebitAmount) as nguyenVatLieu,
                                0                as SoTien,
                                null             as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join PPInvoiceDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account like '152%'
                           and a.AccountCorresponding like '331%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                           and a.TypeID in (210, 240, 430, 500)
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                         union all
                         select a.PostedDate     as ngayGhiSo,
                                a.No             as soCTu,
                                a.Date           as ngayCTu,
                                b.Description    as dienGiai,
                                0                as hangHoa,
                                sum(DebitAmount) as phaiTraNguoiBan,
                                sum(DebitAmount) as nguyenVatLieu,
                                0                as SoTien,
                                null             as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join PPServiceDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account like '152%'
                           and a.AccountCorresponding like '331%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                           and a.TypeID in (210, 240, 430, 500)
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                         union all
                         select a.PostedDate     as ngayGhiSo,
                                a.No             as soCTu,
                                a.Date           as ngayCTu,
                                b.Description    as dienGiai,
                                0                as hangHoa,
                                sum(DebitAmount) as phaiTraNguoiBan,
                                sum(DebitAmount) as nguyenVatLieu,
                                0                as SoTien,
                                null             as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join TIIncrementDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account like '152%'
                           and a.AccountCorresponding like '331%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                           and a.TypeID in (210, 240, 430, 500)
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                         union all
                         select a.PostedDate      as ngayGhiSo,
                                a.No              as soCTu,
                                a.Date            as ngayCTu,
                                b.Description     as dienGiai,
                                0                 as hangHoa,
                                0 - sum(CreditAmount) as phaiTraNguoiBan,
                                0 - sum(CreditAmount) as nguyenVatLieu,
                                0                 as SoTien,
                                null              as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join PPDiscountReturnDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.Account like '152%'
                           and a.AccountCorresponding like '331%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                           and a.TypeID in (210, 240, 430, 500)
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                         union all
                         ---Điều kiện để lấy Tài khoản khác

                         select a.PostedDate     as ngayGhiSo,
                                a.No             as soCTu,
                                a.Date           as ngayCTu,
                                b.Description    as dienGiai,
                                0                as hangHoa,
                                sum(DebitAmount) as phaiTraNguoiBan,
                                0                as nguyenVatLieu,
                                sum(DebitAmount) as SoTien,
                                a.Account        as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join PPInvoiceDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.AccountCorresponding like '331%'
                           and a.Account not like '156%'
                           and a.Account not like '152%'
                           and a.Account not like '3331%'
                           and a.Account not like '133%'
                           and a.Account not like '3333%'
                           and a.Account not like '3332%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                           and a.AccountCorresponding not like '3331%'
                           and a.TypeID in (210, 240, 430, 500)
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                         union all
                         select a.PostedDate     as ngayGhiSo,
                                a.No             as soCTu,
                                a.Date           as ngayCTu,
                                b.Description    as dienGiai,
                                0                as hangHoa,
                                sum(DebitAmount) as phaiTraNguoiBan,
                                0                as nguyenVatLieu,
                                sum(DebitAmount) as SoTien,
                                a.Account        as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join PPServiceDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.AccountCorresponding like '331%'
                           and a.Account not like '156%'
                           and a.Account not like '152%'
                           and a.Account not like '3331%'
                           and a.Account not like '133%'
                           and a.Account not like '3333%'
                           and a.Account not like '3332%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                           and a.AccountCorresponding not like '3331%'
                           and a.TypeID in (210, 240, 430, 500)
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                         union all
                         select a.PostedDate     as ngayGhiSo,
                                a.No             as soCTu,
                                a.Date           as ngayCTu,
                                b.Description    as dienGiai,
                                0                as hangHoa,
                                sum(DebitAmount) as phaiTraNguoiBan,
                                0                as nguyenVatLieu,
                                sum(DebitAmount) as SoTien,
                                a.Account        as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join TIIncrementDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.AccountCorresponding like '331%'
                           and a.Account not like '156%'
                           and a.Account not like '152%'
                           and a.Account not like '3331%'
                           and a.Account not like '133%'
                           and a.Account not like '3333%'
                           and a.Account not like '3332%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                           and a.AccountCorresponding not like '3331%'
                           and a.TypeID in (210, 240, 430, 500)
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                         union all
                         select a.PostedDate      as ngayGhiSo,
                                a.No              as soCTu,
                                a.Date            as ngayCTu,
                                b.Description     as dienGiai,
                                0                 as hangHoa,
                                0 - sum(CreditAmount) as phaiTraNguoiBan,
                                0                 as nguyenVatLieu,
                                0 - sum(CreditAmount) as SoTien,
                                a.Account         as account,
                                a.OrderPriority,
                                a.referenceID,
                                a.typeID
                         from @tbDataGL a
                                  join PPDiscountReturnDetail b on a.DetailID = b.ID
                         where a.PostedDate BETWEEN @FromDate AND @ToDate
                           and a.AccountCorresponding like '331%'
                           and a.Account not like '156%'
                           and a.Account not like '152%'
                           and a.Account not like '3331%'
                           and a.Account not like '133%'
                           and a.Account not like '3333%'
                           and a.Account not like '3332%'
                           and a.AccountCorresponding not like '3333%'
                           and a.AccountCorresponding not like '3332%'
                           and a.AccountCorresponding not like '133%'
                           and a.AccountCorresponding not like '3331%'
                           and a.TypeID in (210, 240, 430, 500)
                         group by a.PostedDate, a.date, a.No, b.Description, a.OrderPriority, a.Account, a.ReferenceID, a.TypeID
                     ) t
                where (hangHoa != 0 or phaiTraNguoiBan != 0 or nguyenVatLieu != 0 or SoTien != 0)
                group by ngayGhiSo,
                         soCTu,
                         ngayCTu,
                         dienGiai,
                         account,
                         ReferenceID,
                         TypeID
                order by ngayGhiSo, soCTu

            END

    END
go

