/*
-- =============================================
-- Author:		congnd
-- Create date: 28.02.2020
-- Description:	<Mua hàng: Lấy số liệu cho báo cáo Sổ nhật ký bán hàng
-- EXEC [Proc_SO_NHAT_KY_BAN_HANG] @FromDate = '2020-03-01', @ToDate = '2020-03-09', @IsNotPaid = false, @CompanyID =  '5A814271-A115-41D1-BA4D-C50BC0040482', @IsMBook = false;
-- =============================================
*/
    CREATE PROCEDURE [dbo].[Proc_SO_NHAT_KY_BAN_HANG] @FromDate nvarchar(25),
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

                select detailID,
                       typeID,
                       postedDate,-- ngày ht
                       refDate,-- ngày ctu
                       refNo,-- so ctu
                       invoiceDate,-- ngày hđ
                       invoiceNo, -- số hđ
                       description,
                       refID,
                       nameCustomer,
                       sum(doanhThuHangHoa)                                      as doanhThuHangHoa,-- doanh thu hàng hóa
                       sum(doanhThuThanhPham)                                    as doanhThuThanhPham,
                       sum(doanhThuDichVu)                                       as doanhThuDichVu,
                       sum(doanhThuTroCap)                                       as doanhThuTroCap,
                       sum(doanhThuBDSDautu)                                     as doanhThuBDSDautu,
                       sum(doanhThuKhac)                                         as doanhThuKhac,
                       sum(chietKhau)                                            as chietKhau,
                       sum(giaTriTraLai)                                         as giaTriTraLai,
                       sum(giaTriGiamGia)                                        as giaTriGiamGia,
                       (DoanhThuHangHoa + DoanhThuThanhPham + DoanhThuDichVu + DoanhThuTroCap + DoanhThuBDSDautu +
                        DoanhThuKhac)                                            as sum,
                       (DoanhThuHangHoa + DoanhThuThanhPham + DoanhThuDichVu + DoanhThuTroCap + DoanhThuBDSDautu +
                        DoanhThuKhac - ChietKhau - GiaTriTraLai - GiaTriGiamGia) as doanhThuThuan
                from (
                         --Lấy doanh thu hàng hóa
                         select a.DetailID,
                                a.date                 as RefDate,
                                a.PostedDate,
                                a.No                   as RefNo,
                                a.InvoiceDate,
                                a.InvoiceNo,
                                a.Reason               as Description,
                                a.TypeID,
                                a.ReferenceID          as RefID,
                                e.accountingobjectname as NameCustomer,
                                a.CreditAmount         as DoanhThuHangHoa,
                                0                      as DoanhThuThanhPham,
                                0                      as DoanhThuDichVu,
                                0                      as DoanhThuTroCap,
                                0                      as DoanhThuBDSDautu,
                                0                      as DoanhThuKhac,
                                0                      as ChietKhau,
                                0                      as GiaTriTraLai,
                                0                      as GiaTriGiamGia
                         from @tbDataGL a
                                  join SAInvoiceDetail b on a.DetailID = b.ID
                                  left join MaterialGoods c on b.MaterialGoodsID = c.ID
                                  join SAInvoice d on a.ReferenceID = d.ID
                                  left join AccountingObject e on a.AccountingObjectID = e.ID
                         where a.Account like '5111%'
                           and a.PostedDate BETWEEN @FromDate AND @ToDate
                         group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
                                  e.accountingobjectname, a.TypeID, a.ReferenceID, a.CreditAmount, a.CreditAmount
                         union all
                         --Lấy doanh thu thành phẩm
                         select a.DetailID,
                                a.date                 as RefDate,
                                a.PostedDate,
                                a.No                   as RefNo,
                                a.InvoiceDate,
                                a.InvoiceNo,
                                a.Reason               as Description,
                                a.TypeID,
                                a.ReferenceID          as RefID,
                                e.accountingobjectname as NameCustomer,
                                0                      as DoanhThuHangHoa,
                                a.CreditAmount         as DoanhThuThanhPham,
                                0                      as DoanhThuDichVu,
                                0                      as DoanhThuTroCap,
                                0                      as DoanhThuBDSDautu,
                                0                      as DoanhThuKhac,
                                0                      as ChietKhau,
                                0                      as GiaTriTraLai,
                                0                      as GiaTriGiamGia
                         from @tbDataGL a
                                  join SAInvoiceDetail b on a.DetailID = b.ID
                                  left join MaterialGoods c on b.MaterialGoodsID = c.ID
                                  join SAInvoice d on a.ReferenceID = d.ID
                                  left join AccountingObject e on a.AccountingObjectID = e.ID
                         where a.Account like '5112%'
                           and a.PostedDate BETWEEN @FromDate AND @ToDate
                         group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
                                  e.accountingobjectname, a.TypeID, a.ReferenceID, a.CreditAmount
                         union all
                         --Lấy doanh thu dịch vụ
                         select a.DetailID,
                                a.date                 as RefDate,
                                a.PostedDate,
                                a.No                   as RefNo,
                                a.InvoiceDate,
                                a.InvoiceNo,
                                a.Reason               as Description,
                                a.TypeID,
                                a.ReferenceID          as RefID,
                                e.accountingobjectname as NameCustomer,
                                0                      as DoanhThuHangHoa,
                                0                      as DoanhThuThanhPham,
                                a.CreditAmount         as DoanhThuDichVu,
                                0                      as DoanhThuTroCap,
                                0                      as DoanhThuBDSDautu,
                                0                      as DoanhThuKhac,
                                0                      as ChietKhau,
                                0                      as GiaTriTraLai,
                                0                      as GiaTriGiamGia
                         from @tbDataGL a
                                  join SAInvoiceDetail b on a.DetailID = b.ID
                                  left join MaterialGoods c on b.MaterialGoodsID = c.ID
                                  join SAInvoice d on a.ReferenceID = d.ID
                                  left join AccountingObject e on a.AccountingObjectID = e.ID
                         where a.Account like '5113%'
                           and a.PostedDate BETWEEN @FromDate AND @ToDate
                         group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
                                  e.accountingobjectname, a.TypeID, a.ReferenceID, a.CreditAmount
                         union all
                         --Lấy doanh thu trợ cấp trợ giá
                         select a.DetailID,
                                a.date                 as RefDate,
                                a.PostedDate,
                                a.No                   as RefNo,
                                a.InvoiceDate,
                                a.InvoiceNo,
                                a.Reason               as Description,
                                a.TypeID,
                                a.ReferenceID          as RefID,
                                e.accountingobjectname as NameCustomer,
                                0                      as DoanhThuHangHoa,
                                0                      as DoanhThuThanhPham,
                                0                      as DoanhThuDichVu,
                                a.CreditAmount         as DoanhThuTroCap,
                                0                      as DoanhThuBDSDautu,
                                0                      as DoanhThuKhac,
                                0                      as ChietKhau,
                                0                      as GiaTriTraLai,
                                0                      as GiaTriGiamGia
                         from @tbDataGL a
                                  join SAInvoiceDetail b on a.DetailID = b.ID
                                  left join MaterialGoods c on b.MaterialGoodsID = c.ID
                                  join SAInvoice d on a.ReferenceID = d.ID
                                  left join AccountingObject e on a.AccountingObjectID = e.ID
                         where a.Account like '5114%'
                           and a.PostedDate BETWEEN @FromDate AND @ToDate
                         group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
                                  e.accountingobjectname, a.TypeID, a.ReferenceID, a.CreditAmount
                         union all
                         --Lấy doanh thu BDS đầu tư
                         select a.DetailID,
                                a.date                 as RefDate,
                                a.PostedDate,
                                a.No                   as RefNo,
                                a.InvoiceDate,
                                a.InvoiceNo,
                                a.Reason               as Description,
                                a.TypeID,
                                a.ReferenceID          as RefID,
                                e.accountingobjectname as NameCustomer,
                                0                      as DoanhThuHangHoa,
                                0                      as DoanhThuThanhPham,
                                0                      as DoanhThuDichVu,
                                0                      as DoanhThuTroCap,
                                a.CreditAmount         as DoanhThuBDSDautu,
                                0                      as DoanhThuKhac,
                                0                      as ChietKhau,
                                0                      as GiaTriTraLai,
                                0                      as GiaTriGiamGia
                         from @tbDataGL a
                                  join SAInvoiceDetail b on a.DetailID = b.ID
                                  left join MaterialGoods c on b.MaterialGoodsID = c.ID
                                  join SAInvoice d on a.ReferenceID = d.ID
                                  left join AccountingObject e on a.AccountingObjectID = e.ID
                         where a.Account like '5117%'
                           and a.PostedDate BETWEEN @FromDate AND @ToDate
                         group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
                                  e.accountingobjectname, a.TypeID, a.ReferenceID, a.CreditAmount
                         union all
                         --Lấy doanh thu khác
                         select a.DetailID,
                                a.date                 as RefDate,
                                a.PostedDate,
                                a.No                   as RefNo,
                                a.InvoiceDate,
                                a.InvoiceNo,
                                a.Reason               as Description,
                                a.TypeID,
                                a.ReferenceID          as RefID,
                                e.accountingobjectname as NameCustomer,
                                0                      as DoanhThuHangHoa,
                                0                      as DoanhThuThanhPham,
                                0                      as DoanhThuDichVu,
                                0                      as DoanhThuTroCap,
                                0                      as DoanhThuBDSDautu,
                                a.CreditAmount         as DoanhThuKhac,
                                0                      as ChietKhau,
                                0                      as GiaTriTraLai,
                                0                      as GiaTriGiamGia
                         from @tbDataGL a
                                  join SAInvoiceDetail b on a.DetailID = b.ID
                                  left join MaterialGoods c on b.MaterialGoodsID = c.ID
                                  join SAInvoice d on a.ReferenceID = d.ID
                                  left join AccountingObject e on a.AccountingObjectID = e.ID
                         where a.Account like '5118%'
                           and a.PostedDate BETWEEN @FromDate AND @ToDate
                         group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
                                  e.accountingobjectname, a.TypeID, a.ReferenceID, a.CreditAmount
                         union all
                         --Lấy chiết khấu của doanh thu
                         select a.DetailID,
                                a.date                 as RefDate,
                                a.PostedDate,
                                a.No                   as RefNo,
                                a.InvoiceDate,
                                a.InvoiceNo,
                                a.Reason               as Description,
                                a.TypeID,
                                a.ReferenceID          as RefID,
                                e.accountingobjectname as NameCustomer,
                                0                      as DoanhThuHangHoa,
                                0                      as DoanhThuThanhPham,
                                0                      as DoanhThuDichVu,
                                0                      as DoanhThuTroCap,
                                0                      as DoanhThuBDSDautu,
                                0                      as DoanhThuKhac,
                                a.DebitAmount          as ChietKhau,
                                0                      as GiaTriTraLai,
                                0                      as GiaTriGiamGia
                         from @tbDataGL a
                                  join SAInvoiceDetail b on a.DetailID = b.ID
                                  left join MaterialGoods c on b.MaterialGoodsID = c.ID
                                  join SAInvoice d on a.ReferenceID = d.ID
                                  left join AccountingObject e on a.AccountingObjectID = e.ID
                         where a.Account like '5211%'
                           and a.PostedDate BETWEEN @FromDate AND @ToDate
                         group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
                                  e.accountingobjectname, a.TypeID, a.ReferenceID, a.DebitAmount
                         union all
                         --Lấy giá trị trả lại
                         select a.DetailID,
                                a.date                 as RefDate,
                                a.PostedDate,
                                a.No                   as RefNo,
                                a.InvoiceDate,
                                a.InvoiceNo,
                                a.Reason               as Description,
                                a.TypeID,
                                a.ReferenceID          as RefID,
                                e.accountingobjectname as NameCustomer,
                                0                      as DoanhThuHangHoa,
                                0                      as DoanhThuThanhPham,
                                0                      as DoanhThuDichVu,
                                0                      as DoanhThuTroCap,
                                0                      as DoanhThuBDSDautu,
                                0                      as DoanhThuKhac,
                                0                      as ChietKhau,
                                a.DebitAmount          as GiaTriTraLai,
                                0                      as GiaTriGiamGia
                         from @tbDataGL a
                                  join SAReturnDetail b on a.DetailID = b.ID
                                  left join MaterialGoods c on b.MaterialGoodsID = c.ID
                                  join SAReturn d on a.ReferenceID = d.ID
                                  left join AccountingObject e on a.AccountingObjectID = e.ID
                         where a.Account like '5212%'
                           and a.typeid = 330
                           and a.PostedDate BETWEEN @FromDate AND @ToDate
                         group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
                                  e.accountingobjectname, a.TypeID, a.ReferenceID, a.DebitAmount
                         union all
                         --Lấy giá trị giảm giá
                         select a.DetailID,
                                a.date                 as RefDate,
                                a.PostedDate,
                                a.No                   as RefNo,
                                a.InvoiceDate,
                                a.InvoiceNo,
                                a.Reason               as Description,
                                a.TypeID,
                                a.ReferenceID          as RefID,
                                e.accountingobjectname as NameCustomer,
                                0                      as DoanhThuHangHoa,
                                0                      as DoanhThuThanhPham,
                                0                      as DoanhThuDichVu,
                                0                      as DoanhThuTroCap,
                                0                      as DoanhThuBDSDautu,
                                0                      as DoanhThuKhac,
                                0                      as ChietKhau,
                                0                      as GiaTriTraLai,
                                a.DebitAmount          as GiaTriGiamGia
                         from @tbDataGL a
                                  join SAReturnDetail b on a.DetailID = b.ID
                                  left join MaterialGoods c on b.MaterialGoodsID = c.ID
                                  join SAReturn d on a.ReferenceID = d.ID
                                  left join AccountingObject e on a.AccountingObjectID = e.ID
                         where a.Account like '5213%'
                           and a.typeid = 340
                           and a.PostedDate BETWEEN @FromDate AND @ToDate
                         group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
                                  e.accountingobjectname, a.TypeID, a.ReferenceID, a.DebitAmount
                         union all
                         --Lấy chiết khấu trả lại
                         select a.DetailID,
                                a.date                 as RefDate,
                                a.PostedDate,
                                a.No                   as RefNo,
                                a.InvoiceDate,
                                a.InvoiceNo,
                                a.Reason               as Description,
                                a.TypeID,
                                a.ReferenceID          as RefID,
                                e.accountingobjectname as NameCustomer,
                                0                      as DoanhThuHangHoa,
                                0                      as DoanhThuThanhPham,
                                0                      as DoanhThuDichVu,
                                0                      as DoanhThuTroCap,
                                0                      as DoanhThuBDSDautu,
                                0                      as DoanhThuKhac,
                                (0 - a.CreditAmount)   as ChietKhau,
                                0                      as GiaTriTraLai,
                                0                      as GiaTriGiamGia

                         from @tbDataGL a
                                  join SAReturnDetail b on a.DetailID = b.ID
                                  left join MaterialGoods c on b.MaterialGoodsID = c.ID
                                  join SAReturn d on a.ReferenceID = d.ID
                                  left join AccountingObject e on a.AccountingObjectID = e.ID
                         where a.Account like '5211%'
                           and a.typeid = 330
                           and a.PostedDate BETWEEN @FromDate AND @ToDate
                         group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
                                  e.accountingobjectname, a.TypeID, a.ReferenceID, a.CreditAmount
                         union all
                         --Lấy chiết khấu giảm giá
                         select a.DetailID,
                                a.date                 as RefDate,
                                a.PostedDate,
                                a.No                   as RefNo,
                                a.InvoiceDate,
                                a.InvoiceNo,
                                a.Reason               as Description,
                                a.TypeID,
                                a.ReferenceID          as RefID,
                                e.accountingobjectname as NameCustomer,
                                0                      as DoanhThuHangHoa,
                                0                      as DoanhThuThanhPham,
                                0                      as DoanhThuDichVu,
                                0                      as DoanhThuTroCap,
                                0                      as DoanhThuBDSDautu,
                                0                      as DoanhThuKhac,
                                (0 - a.CreditAmount)   as ChietKhau,
                                0                      as GiaTriTraLai,
                                0                      as GiaTriGiamGia

                         from @tbDataGL a
                                  join SAReturnDetail b on a.DetailID = b.ID
                                  left join MaterialGoods c on b.MaterialGoodsID = c.ID
                                  join SAReturn d on a.ReferenceID = d.ID
                                  left join AccountingObject e on a.AccountingObjectID = e.ID
                         where a.Account like '5211%'
                           and a.typeid = 340
                           and a.PostedDate BETWEEN @FromDate AND @ToDate
                         group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
                                  e.accountingobjectname, a.TypeID, a.ReferenceID, a.CreditAmount
                     ) t
                where (doanhThuHangHoa != 0
                    or doanhThuThanhPham != 0
                    or doanhThuDichVu != 0
                    or doanhThuTroCap != 0
                    or doanhThuBDSDautu != 0
                    or doanhThuKhac != 0
                    or chietKhau != 0
                    or giaTriTraLai != 0
                    or giaTriGiamGia != 0)
                group by detailID,
                         typeID,
                         postedDate,-- ngày ht
                         refDate,-- ngày ctu
                         refNo,-- so ctu
                         invoiceDate,-- ngày hđ
                         invoiceNo, -- số hđ
                         description,
                         refID,
                         nameCustomer,
                         doanhThuHangHoa,-- doanh thu hàng hóa
                         doanhThuThanhPham,
                         doanhThuDichVu,
                         doanhThuTroCap,
                         doanhThuBDSDautu,
                         doanhThuKhac,
                         chietKhau,
                         giaTriTraLai,
                         giaTriGiamGia

                ORDER BY PostedDate,
                         RefDate, RefNo

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
                       GL.NoFBook as no,
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
                select detailID,
                       typeID,
                       postedDate,-- ngày ht
                       refDate,-- ngày ctu
                       refNo,-- so ctu
                       invoiceDate,-- ngày hđ
                       invoiceNo, -- số hđ
                       description,
                       refID,
                       nameCustomer,
                       sum(doanhThuHangHoa)                                      as doanhThuHangHoa,-- doanh thu hàng hóa
                       sum(doanhThuThanhPham)                                    as doanhThuThanhPham,
                       sum(doanhThuDichVu)                                       as doanhThuDichVu,
                       sum(doanhThuTroCap)                                       as doanhThuTroCap,
                       sum(doanhThuBDSDautu)                                     as doanhThuBDSDautu,
                       sum(doanhThuKhac)                                         as doanhThuKhac,
                       sum(chietKhau)                                            as chietKhau,
                       sum(giaTriTraLai)                                         as giaTriTraLai,
                       sum(giaTriGiamGia)                                        as giaTriGiamGia,
                       (DoanhThuHangHoa + DoanhThuThanhPham + DoanhThuDichVu + DoanhThuTroCap + DoanhThuBDSDautu +
                        DoanhThuKhac)                                            as sum,
                       (DoanhThuHangHoa + DoanhThuThanhPham + DoanhThuDichVu + DoanhThuTroCap + DoanhThuBDSDautu +
                        DoanhThuKhac - ChietKhau - GiaTriTraLai - GiaTriGiamGia) as doanhThuThuan
                from (
                         --Lấy doanh thu hàng hóa
                         select a.DetailID,
                                a.date                 as RefDate,
                                a.PostedDate,
                                a.No                   as RefNo,
                                a.InvoiceDate,
                                a.InvoiceNo,
                                a.Reason               as Description,
                                a.TypeID,
                                a.ReferenceID          as RefID,
                                e.accountingobjectname as NameCustomer,
                                a.CreditAmount         as DoanhThuHangHoa,
                                0                      as DoanhThuThanhPham,
                                0                      as DoanhThuDichVu,
                                0                      as DoanhThuTroCap,
                                0                      as DoanhThuBDSDautu,
                                0                      as DoanhThuKhac,
                                0                      as ChietKhau,
                                0                      as GiaTriTraLai,
                                0                      as GiaTriGiamGia
                         from @tbDataGL a
                                  join SAInvoiceDetail b on a.DetailID = b.ID
                                  left join MaterialGoods c on b.MaterialGoodsID = c.ID
                                  join SAInvoice d on a.ReferenceID = d.ID
                                  left join AccountingObject e on a.AccountingObjectID = e.ID
                         where a.Account like '5111%'
                           and a.AccountCorresponding like '131%'
                           and a.TypeID in (320)
                           and a.PostedDate BETWEEN @FromDate AND @ToDate
                         group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
                                  e.accountingobjectname, a.TypeID, a.ReferenceID, a.CreditAmount, a.CreditAmount
                         union all
                         --Lấy doanh thu thành phẩm
                         select a.DetailID,
                                a.date                 as RefDate,
                                a.PostedDate,
                                a.No                   as RefNo,
                                a.InvoiceDate,
                                a.InvoiceNo,
                                a.Reason               as Description,
                                a.TypeID,
                                a.ReferenceID          as RefID,
                                e.accountingobjectname as NameCustomer,
                                0                      as DoanhThuHangHoa,
                                a.CreditAmount         as DoanhThuThanhPham,
                                0                      as DoanhThuDichVu,
                                0                      as DoanhThuTroCap,
                                0                      as DoanhThuBDSDautu,
                                0                      as DoanhThuKhac,
                                0                      as ChietKhau,
                                0                      as GiaTriTraLai,
                                0                      as GiaTriGiamGia
                         from @tbDataGL a
                                  join SAInvoiceDetail b on a.DetailID = b.ID
                                  left join MaterialGoods c on b.MaterialGoodsID = c.ID
                                  join SAInvoice d on a.ReferenceID = d.ID
                                  left join AccountingObject e on a.AccountingObjectID = e.ID
                         where a.Account like '5112%'
                           and a.AccountCorresponding like '131%'
                           and a.TypeID in (320)
                           and a.PostedDate BETWEEN @FromDate AND @ToDate
                         group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
                                  e.accountingobjectname, a.TypeID, a.ReferenceID, a.CreditAmount
                         union all
                         --Lấy doanh thu dịch vụ
                         select a.DetailID,
                                a.date                 as RefDate,
                                a.PostedDate,
                                a.No                   as RefNo,
                                a.InvoiceDate,
                                a.InvoiceNo,
                                a.Reason               as Description,
                                a.TypeID,
                                a.ReferenceID          as RefID,
                                e.accountingobjectname as NameCustomer,
                                0                      as DoanhThuHangHoa,
                                0                      as DoanhThuThanhPham,
                                a.CreditAmount         as DoanhThuDichVu,
                                0                      as DoanhThuTroCap,
                                0                      as DoanhThuBDSDautu,
                                0                      as DoanhThuKhac,
                                0                      as ChietKhau,
                                0                      as GiaTriTraLai,
                                0                      as GiaTriGiamGia
                         from @tbDataGL a
                                  join SAInvoiceDetail b on a.DetailID = b.ID
                                  left join MaterialGoods c on b.MaterialGoodsID = c.ID
                                  join SAInvoice d on a.ReferenceID = d.ID
                                  left join AccountingObject e on a.AccountingObjectID = e.ID
                         where a.Account like '5113%'
                           and a.AccountCorresponding like '131%'
                           and a.TypeID in (320)
                           and a.PostedDate BETWEEN @FromDate AND @ToDate
                         group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
                                  e.accountingobjectname, a.TypeID, a.ReferenceID, a.CreditAmount
                         union all
                         --Lấy doanh thu trợ cấp trợ giá
                         select a.DetailID,
                                a.date                 as RefDate,
                                a.PostedDate,
                                a.No                   as RefNo,
                                a.InvoiceDate,
                                a.InvoiceNo,
                                a.Reason               as Description,
                                a.TypeID,
                                a.ReferenceID          as RefID,
                                e.accountingobjectname as NameCustomer,
                                0                      as DoanhThuHangHoa,
                                0                      as DoanhThuThanhPham,
                                0                      as DoanhThuDichVu,
                                a.CreditAmount         as DoanhThuTroCap,
                                0                      as DoanhThuBDSDautu,
                                0                      as DoanhThuKhac,
                                0                      as ChietKhau,
                                0                      as GiaTriTraLai,
                                0                      as GiaTriGiamGia
                         from @tbDataGL a
                                  join SAInvoiceDetail b on a.DetailID = b.ID
                                  left join MaterialGoods c on b.MaterialGoodsID = c.ID
                                  join SAInvoice d on a.ReferenceID = d.ID
                                  left join AccountingObject e on a.AccountingObjectID = e.ID
                         where a.Account like '5114%'
                           and a.AccountCorresponding like '131%'
                           and a.TypeID in (320)
                           and a.PostedDate BETWEEN @FromDate AND @ToDate
                         group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
                                  e.accountingobjectname, a.TypeID, a.ReferenceID, a.CreditAmount
                         union all
                         --Lấy doanh thu BDS đầu tư
                         select a.DetailID,
                                a.date                 as RefDate,
                                a.PostedDate,
                                a.No                   as RefNo,
                                a.InvoiceDate,
                                a.InvoiceNo,
                                a.Reason               as Description,
                                a.TypeID,
                                a.ReferenceID          as RefID,
                                e.accountingobjectname as NameCustomer,
                                0                      as DoanhThuHangHoa,
                                0                      as DoanhThuThanhPham,
                                0                      as DoanhThuDichVu,
                                0                      as DoanhThuTroCap,
                                a.CreditAmount         as DoanhThuBDSDautu,
                                0                      as DoanhThuKhac,
                                0                      as ChietKhau,
                                0                      as GiaTriTraLai,
                                0                      as GiaTriGiamGia
                         from @tbDataGL a
                                  join SAInvoiceDetail b on a.DetailID = b.ID
                                  left join MaterialGoods c on b.MaterialGoodsID = c.ID
                                  join SAInvoice d on a.ReferenceID = d.ID
                                  left join AccountingObject e on a.AccountingObjectID = e.ID
                         where a.Account like '5117%'
                           and a.AccountCorresponding like '131%'
                           and a.TypeID in (320)
                           and a.PostedDate BETWEEN @FromDate AND @ToDate
                         group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
                                  e.accountingobjectname, a.TypeID, a.ReferenceID, a.CreditAmount
                         union all
                         --Lấy doanh thu khác
                         select a.DetailID,
                                a.date                 as RefDate,
                                a.PostedDate,
                                a.No                   as RefNo,
                                a.InvoiceDate,
                                a.InvoiceNo,
                                a.Reason               as Description,
                                a.TypeID,
                                a.ReferenceID          as RefID,
                                e.accountingobjectname as NameCustomer,
                                0                      as DoanhThuHangHoa,
                                0                      as DoanhThuThanhPham,
                                0                      as DoanhThuDichVu,
                                0                      as DoanhThuTroCap,
                                0                      as DoanhThuBDSDautu,
                                a.CreditAmount         as DoanhThuKhac,
                                0                      as ChietKhau,
                                0                      as GiaTriTraLai,
                                0                      as GiaTriGiamGia
                         from @tbDataGL a
                                  join SAInvoiceDetail b on a.DetailID = b.ID
                                  left join MaterialGoods c on b.MaterialGoodsID = c.ID
                                  join SAInvoice d on a.ReferenceID = d.ID
                                  left join AccountingObject e on a.AccountingObjectID = e.ID
                         where a.Account like '5118%'
                           and a.AccountCorresponding like '131%'
                           and a.TypeID in (320)
                           and a.PostedDate BETWEEN @FromDate AND @ToDate
                         group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
                                  e.accountingobjectname, a.TypeID, a.ReferenceID, a.CreditAmount
                         union all
                         --Lấy chiết khấu của doanh thu
                         select a.DetailID,
                                a.date                 as RefDate,
                                a.PostedDate,
                                a.No                   as RefNo,
                                a.InvoiceDate,
                                a.InvoiceNo,
                                a.Reason               as Description,
                                a.TypeID,
                                a.ReferenceID          as RefID,
                                e.accountingobjectname as NameCustomer,
                                0                      as DoanhThuHangHoa,
                                0                      as DoanhThuThanhPham,
                                0                      as DoanhThuDichVu,
                                0                      as DoanhThuTroCap,
                                0                      as DoanhThuBDSDautu,
                                0                      as DoanhThuKhac,
                                a.DebitAmount          as ChietKhau,
                                0                      as GiaTriTraLai,
                                0                      as GiaTriGiamGia
                         from @tbDataGL a
                                  join SAInvoiceDetail b on a.DetailID = b.ID
                                  left join MaterialGoods c on b.MaterialGoodsID = c.ID
                                  join SAInvoice d on a.ReferenceID = d.ID
                                  left join AccountingObject e on a.AccountingObjectID = e.ID
                         where a.Account like '5211%'
                           and a.AccountCorresponding like '131%'
                           and a.TypeID in (320)
                           and a.PostedDate BETWEEN @FromDate AND @ToDate
                         group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
                                  e.accountingobjectname, a.TypeID, a.ReferenceID, a.DebitAmount
--                          union all
                         --Lấy giá trị trả lại
--                          select a.DetailID,
--                                 a.date                 as RefDate,
--                                 a.PostedDate,
--                                 a.No                   as RefNo,
--                                 a.InvoiceDate,
--                                 a.InvoiceNo,
--                                 a.Reason               as Description,
--                                 a.TypeID,
--                                 a.ReferenceID          as RefID,
--                                 e.accountingobjectname as NameCustomer,
--                                 0                      as DoanhThuHangHoa,
--                                 0                      as DoanhThuThanhPham,
--                                 0                      as DoanhThuDichVu,
--                                 0                      as DoanhThuTroCap,
--                                 0                      as DoanhThuBDSDautu,
--                                 0                      as DoanhThuKhac,
--                                 0                      as ChietKhau,
--                                 a.DebitAmount          as GiaTriTraLai,
--                                 0                      as GiaTriGiamGia
--                          from @tbDataGL a
--                                   join SAReturnDetail b on a.DetailID = b.ID
--                                   left join MaterialGoods c on b.MaterialGoodsID = c.ID
--                                   join SAReturn d on a.ReferenceID = d.ID
--                                   left join AccountingObject e on a.AccountingObjectID = e.ID
--                          where a.Account like '5212%'
--                            and a.typeid = 330
--                            and a.PostedDate BETWEEN @FromDate AND @ToDate
--                          group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
--                                   e.accountingobjectname, a.TypeID, a.ReferenceID, a.DebitAmount
--                          union all
                         --Lấy giá trị giảm giá
--                          select a.DetailID,
--                                 a.date                 as RefDate,
--                                 a.PostedDate,
--                                 a.No                   as RefNo,
--                                 a.InvoiceDate,
--                                 a.InvoiceNo,
--                                 a.Reason               as Description,
--                                 a.TypeID,
--                                 a.ReferenceID          as RefID,
--                                 e.accountingobjectname as NameCustomer,
--                                 0                      as DoanhThuHangHoa,
--                                 0                      as DoanhThuThanhPham,
--                                 0                      as DoanhThuDichVu,
--                                 0                      as DoanhThuTroCap,
--                                 0                      as DoanhThuBDSDautu,
--                                 0                      as DoanhThuKhac,
--                                 0                      as ChietKhau,
--                                 0                      as GiaTriTraLai,
--                                 a.DebitAmount          as GiaTriGiamGia
--                          from @tbDataGL a
--                                   join SAReturnDetail b on a.DetailID = b.ID
--                                   left join MaterialGoods c on b.MaterialGoodsID = c.ID
--                                   join SAReturn d on a.ReferenceID = d.ID
--                                   left join AccountingObject e on a.AccountingObjectID = e.ID
--                          where a.Account like '5213%'
--                            and a.typeid = 340
--                            and a.PostedDate BETWEEN @FromDate AND @ToDate
--                          group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
--                                   e.accountingobjectname, a.TypeID, a.ReferenceID, a.DebitAmount
--                          union all
                         --Lấy chiết khấu trả lại
--                          select a.DetailID,
--                                 a.date                 as RefDate,
--                                 a.PostedDate,
--                                 a.No                   as RefNo,
--                                 a.InvoiceDate,
--                                 a.InvoiceNo,
--                                 a.Reason               as Description,
--                                 a.TypeID,
--                                 a.ReferenceID          as RefID,
--                                 e.accountingobjectname as NameCustomer,
--                                 0                      as DoanhThuHangHoa,
--                                 0                      as DoanhThuThanhPham,
--                                 0                      as DoanhThuDichVu,
--                                 0                      as DoanhThuTroCap,
--                                 0                      as DoanhThuBDSDautu,
--                                 0                      as DoanhThuKhac,
--                                 (0 - a.CreditAmount)   as ChietKhau,
--                                 0                      as GiaTriTraLai,
--                                 0                      as GiaTriGiamGia
--
--                          from @tbDataGL a
--                                   join SAReturnDetail b on a.DetailID = b.ID
--                                   left join MaterialGoods c on b.MaterialGoodsID = c.ID
--                                   join SAReturn d on a.ReferenceID = d.ID
--                                   left join AccountingObject e on a.AccountingObjectID = e.ID
--                          where a.Account like '5211%'
--                            and a.typeid = 330
--                            and a.PostedDate BETWEEN @FromDate AND @ToDate
--                          group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
--                                   e.accountingobjectname, a.TypeID, a.ReferenceID, a.CreditAmount
--                          union all
--                          --Lấy chiết khấu giảm giá
--                          select a.DetailID,
--                                 a.date                 as RefDate,
--                                 a.PostedDate,
--                                 a.No                   as RefNo,
--                                 a.InvoiceDate,
--                                 a.InvoiceNo,
--                                 a.Reason               as Description,
--                                 a.TypeID,
--                                 a.ReferenceID          as RefID,
--                                 e.accountingobjectname as NameCustomer,
--                                 0                      as DoanhThuHangHoa,
--                                 0                      as DoanhThuThanhPham,
--                                 0                      as DoanhThuDichVu,
--                                 0                      as DoanhThuTroCap,
--                                 0                      as DoanhThuBDSDautu,
--                                 0                      as DoanhThuKhac,
--                                 (0 - a.CreditAmount)   as ChietKhau,
--                                 0                      as GiaTriTraLai,
--                                 0                      as GiaTriGiamGia
--
--                          from @tbDataGL a
--                                   join SAReturnDetail b on a.DetailID = b.ID
--                                   left join MaterialGoods c on b.MaterialGoodsID = c.ID
--                                   join SAReturn d on a.ReferenceID = d.ID
--                                   left join AccountingObject e on a.AccountingObjectID = e.ID
--                          where a.Account like '5211%'
--                            and a.typeid = 340
--                            and a.PostedDate BETWEEN @FromDate AND @ToDate
--                          group by a.DetailID, a.Date, a.PostedDate, a.no, a.InvoiceDate, a.InvoiceNo, a.Reason,
--                                   e.accountingobjectname, a.TypeID, a.ReferenceID, a.CreditAmount
                     ) t
                group by detailID,
                         typeID,
                         postedDate,-- ngày ht
                         refDate,-- ngày ctu
                         refNo,-- so ctu
                         invoiceDate,-- ngày hđ
                         invoiceNo, -- số hđ
                         description,
                         refID,
                         nameCustomer,
                         doanhThuHangHoa,-- doanh thu hàng hóa
                         doanhThuThanhPham,
                         doanhThuDichVu,
                         doanhThuTroCap,
                         doanhThuBDSDautu,
                         doanhThuKhac,
                         chietKhau,
                         giaTriTraLai,
                         giaTriGiamGia

                ORDER BY PostedDate,
                         RefDate, RefNo
            END
    END
go

