IF OBJECT_ID ( 'Proc_CHI_TIET_BAN_HANG', 'P' ) IS NOT NULL
    DROP PROCEDURE Proc_CHI_TIET_BAN_HANG;
/*=================================================================================================
 * Created By:		congnd
 * Created Date:	25/12/2017
 * Description:		Lay du lieu cho bao cao << Sổ chi tiết bán hàng >>
 * Edit by: Cuongpv
 * Edit Description: Lay du lieu day du cac truong hop cho bao cao <<So chi tiet ban hang>>
  -- EXEC [Proc_CHI_TIET_BAN_HANG] @FromDate = '2019-03-01', @ToDate = '2020-03-09',@AccountObjectID = null,
  @MaterialGoods = null, @CompanyID =  '5A814271-A115-41D1-BA4D-C50BC0040482', @IsMBook = false;
===================================================================================================== */
    CREATE PROCEDURE [dbo].[Proc_CHI_TIET_BAN_HANG]
    @FromDate DATETIME,
        @ToDate DATETIME,
        @AccountObjectID AS NVARCHAR(MAX),
        @MaterialGoods as NVARCHAR(MAX),
        @CompanyID UNIQUEIDENTIFIER,
        @IsMBook BIT = 0,
        @isDependent BIT = 0
    as
    begin
        CREATE TABLE #tblListAccountObjectID
        (
            AccountObjectID   UNIQUEIDENTIFIER,
            AccountObjectCode NVARCHAR(25)
                COLLATE SQL_Latin1_General_CP1_CI_AS,
            AccountObjectName NVARCHAR(512)
                COLLATE SQL_Latin1_General_CP1_CI_AS
        )
        CREATE TABLE #tblListMaterialGoods
        (
            MaterialGoodsID   UNIQUEIDENTIFIER,
            MaterialGoodsCode NVARCHAR(25)
                COLLATE SQL_Latin1_General_CP1_CI_AS,
            MaterialGoodsName NVARCHAR(512)
                COLLATE SQL_Latin1_General_CP1_CI_AS
        )
        INSERT INTO #tblListAccountObjectID
        SELECT AO.ID,
               ao.AccountingObjectCode,
               ao.AccountingObjectName
        FROM dbo.Func_ConvertStringIntoTable_Nvarchar(@AccountObjectID,
                                                      ',') tblAccountObjectSelected
                 INNER JOIN dbo.AccountingObject AO ON AO.ID = tblAccountObjectSelected.Value
        INSERT INTO #tblListMaterialGoods
        SELECT MG.ID,
               MG.MaterialGoodsCode,
               MG.MaterialGoodsName
        FROM dbo.Func_ConvertStringIntoTable_Nvarchar(@MaterialGoods,
                                                      ',') tblAccountObjectSelected
                 INNER JOIN dbo.MaterialGoods MG ON MG.ID = tblAccountObjectSelected.Value
        /*Add by cuongpv de sua cach lam tron*/
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
                              DebitAmount          decimal(25, 0),
                              DebitAmountOriginal  decimal(25, 2),
                              CreditAmount         decimal(25, 0),
                              CreditAmountOriginal decimal(25, 2),
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
        FROM GeneralLedger GL
        WHERE GL.PostedDate between @FromDate
            and @ToDate
          and GL.Companyid in (select id from Func_getCompany (@CompanyID, @isDependent))
          and GL.TypeLedger in (@IsMBook, 2)
        /*end add by cuongpv*/

        /*add by cuongpv*/
        DECLARE @tbLuuTru TABLE
                          (
                              ngayCTu           datetime,
                              ngayHachToan      datetime,
                              soHieu            nvarchar(25),
                              ngayHoaDon        datetime,
                              soHoaDon          nvarchar(25),
                              dienGiai          nvarchar(512),
                              tkDoiUng          nvarchar(25),
                              dvt               nvarchar(25),
                              soLuong           decimal(25, 10),
                              donGia            money,
                              thanhTien         decimal(25, 0),
                              thue              decimal(25, 0),
                              khac              decimal(25, 0),
                              materialGoodsName nvarchar(512),
                              materialGoodsCode nvarchar(512),
                              materialGoodsID   nvarchar(512),
                              refID             uniqueidentifier,
                              typeID            int
                              --OrderPriority int
                          )

        INSERT INTO @tbLuuTru
        SELECT ngayCTu,
               ngayHachToan,
               soHieu,
               ngayHoaDon,
               soHoaDon,
               dienGiai,
               tkDoiUng,
               dvt,
               soLuong,
               donGia,
               SUM(thanhTien) as thanhTien,
               SUM(thue)      as thue,
               SUM(khac)      as khac,
               materialGoodsName,
               materialGoodsCode,
               materialGoodsID,
               refID,
               typeID
        FROM (
                 --Lấy dữ liệu Doanh thu (Số lượng, đơn giá, thành tiền) từ chứng từ bán hàng
                 /*end add by cuongpv*/
                 /*lay hoa don ban hang*/
                 select a.Date               as ngayCTu,
                        a.PostedDate         as ngayHachToan,
                        a.No                 as soHieu,
                        a.InvoiceDate        as ngayHoaDon,
                        a.InvoiceNo          as soHoaDon,
                        d.reason             as dienGiai,
                        accountCorresponding as tkDoiUng,
                        e.UnitName           as dvt,
                        b.Quantity           as soLuong,
                        b.UnitPrice          as donGia,
                        sum(b.amount)        as thanhTien,
                        0                    as thue,
                        0                    as khac,
                        c.MaterialGoodsName,
                        c.MaterialGoodsCode,
                        c.id                 as materialGoodsID,
                        a.OrderPriority,
                        a.ReferenceID        as refID,
                        a.TypeID
                 from @tbDataGL a
                          left join SAInvoiceDetail b
                                    on a.DetailID = b.ID /*edit by cuongpv GeneralLedger -> @tbDataGL*/
                          left join MaterialGoods c on b.MaterialGoodsID = c.ID
                          left join SAInvoice d on d.ID = b.SAInvoiceID
                          left join Unit e on b.UnitID = e.ID
                 where a.PostedDate between @FromDate and @ToDate
                   and a.Account like '511%'
                   and a.Typeid not in (330, 340)
                   and (@AccountObjectID is null OR
                        (a.AccountingObjectID in (select AccountObjectID from #tblListAccountObjectID)))
                   and (@MaterialGoods is null OR
                        (b.MaterialGoodsID in (select MaterialGoodsID from #tblListMaterialGoods)))

                 group by a.Date, a.PostedDate, a.No, a.InvoiceDate, a.InvoiceNo,
                          accountCorresponding, /*edit by cuonpv bo: " , Sreason "*/
                          e.UnitName, b.Quantity, b.UnitPrice, c.ID, c.MaterialGoodsName, d.reason, a.OrderPriority,
                          a.ReferenceID, a.TypeID, c.MaterialGoodsCode, c.id
                 Union all
                 --Lấy dữ liệu Các khoản tính trừ (Thuế) từ chứng từ bán hàng, lấy thuế GTGT hiển thị cùng dòng với dòng lấy doanh thu
                 select a.Date               as ngayCTu,
                        a.PostedDate         as ngayHachToan,
                        a.No                 as soHieu,
                        a.InvoiceDate        as ngayHoaDon,
                        a.InvoiceNo          as soHoaDon,
                        d.reason             as dienGiai,
                        accountCorresponding as tkDoiUng,
                        e.UnitName           as dvt,
                        b.Quantity           as soLuong,
                        b.UnitPrice          as donGia,
                        0                    as thanhTien,
                        sum(CreditAmount)    as thue,
                        0                    as khac,
                        c.MaterialGoodsName,
                        c.MaterialGoodsCode,
                        c.id                 as materialGoodsID,
                        a.OrderPriority,
                        a.ReferenceID        as refID,
                        a.TypeID
                 from @tbDataGL a
                          left join SAInvoiceDetail b on a.DetailID = b.ID
                          left join MaterialGoods c on b.MaterialGoodsID = c.ID
                          left join SAInvoice d on d.ID = b.SAInvoiceID
                          left join Unit e on b.UnitID = e.ID
                 where a.PostedDate between @FromDate and @ToDate
                   and a.Account like '3331%'
                   and accountCorresponding like '511%'
                   and (a.TypeID not in (330, 340))
                   and (@AccountObjectID is null OR
                        (a.AccountingObjectID in (select AccountObjectID from #tblListAccountObjectID)))
                   and (@MaterialGoods is null OR
                        (b.MaterialGoodsID in (select MaterialGoodsID from #tblListMaterialGoods)))
                 group by a.Date, a.PostedDate, a.No, a.InvoiceDate, a.InvoiceNo, accountCorresponding,
                          e.UnitName, b.Quantity, b.UnitPrice, c.ID, c.MaterialGoodsName, d.reason, a.OrderPriority,
                          a.ReferenceID, a.TypeID, c.MaterialGoodsCode, c.id
                 Union all
                 --Lấy dữ liệu Các khoản tính trừ (Thuế) từ chứng từ bán hàng, lấy thuế xuất khẩu hiển thị cùng dòng với dòng lấy doanh thu
                 select a.Date               as ngayCTu,
                        a.PostedDate         as ngayHachToan,
                        a.No                 as soHieu,
                        a.InvoiceDate        as ngayHoaDon,
                        a.InvoiceNo          as soHoaDon,
                        d.reason             as dienGiai,
                        accountCorresponding as tkDoiUng,
                        e.UnitName           as dvt,
                        b.Quantity           as soLuong,
                        b.UnitPrice          as donGia,
                        0                    as thanhTien,
                        sum(CreditAmount)    as thue,
                        0                    as khac,
                        c.MaterialGoodsName,
                        c.MaterialGoodsCode,
                        c.id                 as materialGoodsID,
                        a.OrderPriority,
                        a.ReferenceID        as refID,
                        a.TypeID
                 from @tbDataGL a
                          left join SAInvoiceDetail b on a.DetailID = b.ID
                          left join MaterialGoods c on b.MaterialGoodsID = c.ID
                          left join SAInvoice d on d.ID = b.SAInvoiceID
                          left join Unit e on b.UnitID = e.ID
                 where a.PostedDate between @FromDate and @ToDate
                   and a.Account like '3333%'
                   and (a.TypeID not in (330, 340))
                   and (@AccountObjectID is null OR
                        (a.AccountingObjectID in (select AccountObjectID from #tblListAccountObjectID)))
                   and (@MaterialGoods is null OR
                        (b.MaterialGoodsID in (select MaterialGoodsID from #tblListMaterialGoods)))
                 group by a.Date, a.PostedDate, a.No, a.InvoiceDate, a.InvoiceNo, accountCorresponding,
                          e.UnitName, b.Quantity, b.UnitPrice, c.ID, c.MaterialGoodsName, d.reason, a.OrderPriority,
                          a.ReferenceID, a.TypeID, c.MaterialGoodsCode, c.id
                 Union all
                 --Lấy dữ liệu Các khoản tính trừ (Khác) từ chứng từ bán hàng, hiển thị cùng dòng với dòng lấy doanh thu
                 select a.Date               as ngayCTu,
                        a.PostedDate         as ngayHachToan,
                        a.No                 as soHieu,
                        a.InvoiceDate        as ngayHoaDon,
                        a.InvoiceNo          as soHoaDon,
                        d.reason             as dienGiai,
                        accountCorresponding as tkDoiUng,
                        e.UnitName           as dvt,
                        b.Quantity           as soLuong,
                        b.UnitPrice          as donGia,
                        0                    as thanhTien,
                        0                    as thue,
                        sum(DebitAmount)     as khac,
                        c.MaterialGoodsName,
                        c.MaterialGoodsCode,
                        c.id                 as materialGoodsID,
                        a.OrderPriority,
                        a.ReferenceID        as refID,
                        a.TypeID
                 from @tbDataGL a
                          left join SAInvoiceDetail b on a.DetailID = b.ID
                          left join MaterialGoods c on b.MaterialGoodsID = c.ID
                          left join SAInvoice d on d.ID = b.SAInvoiceID
                          left join Unit e on b.UnitID = e.ID
                 where a.PostedDate between @FromDate and @ToDate
                   and a.Account like '521%'
                   and (a.TypeID not in (330, 340))
                   and (@AccountObjectID is null OR
                        (a.AccountingObjectID in (select AccountObjectID from #tblListAccountObjectID)))
                   and (@MaterialGoods is null OR
                        (b.MaterialGoodsID in (select MaterialGoodsID from #tblListMaterialGoods)))
                 group by a.Date, a.PostedDate, a.No, a.InvoiceDate, a.InvoiceNo, accountCorresponding,
                          e.UnitName, b.Quantity, b.UnitPrice, c.ID, c.MaterialGoodsName, d.reason, a.OrderPriority,
                          a.ReferenceID, a.TypeID, c.MaterialGoodsCode, c.id
             ) SA
        GROUP BY ngayCTu, ngayHachToan, soHieu, ngayHoaDon, soHoaDon, dienGiai, tkDoiUng, dvt, soLuong, donGia,
                 MaterialGoodsName, refID, TypeID, materialGoodsCode, materialGoodsID
            /*add by cuongpv*/

        UNION ALL
        /*lay gia tri tra lai giam gia*/
        SELECT ngayCTu,
               ngayHachToan,
               soHieu,
               ngayHoaDon,
               soHoaDon,
               dienGiai,
               tkDoiUng,
               dvt,
               soLuong,
               donGia,
               SUM(thanhTien) as thanhTien,
               SUM(thue)      as thue,
               SUM(khac)      as khac,
               materialGoodsName,
               materialGoodsCode,
               materialGoodsID,
               refID,
               typeID
        FROM (
                 /*lay dong gia tri SAInvoice*/
                 /*lay hoa don ban hang*/
                 select a.Date               as ngayCTu,
                        a.PostedDate         as ngayHachToan,
                        a.No                 as soHieu,
                        a.InvoiceDate        as ngayHoaDon,
                        a.InvoiceNo          as soHoaDon,
                        d.reason             as dienGiai,
                        accountCorresponding as tkDoiUng,
                        e.UnitName           as dvt,
                        b.Quantity           as soLuong,
                        b.UnitPrice          as donGia,
                        sum(b.amount)        as thanhTien,
                        0                    as thue,
                        0                    as khac,
                        c.MaterialGoodsName,
                        c.MaterialGoodsCode,
                        c.id                 as materialGoodsID,
                        a.OrderPriority,
                        a.ReferenceID        as refID,
                        a.TypeID
                 from @tbDataGL a
                          left join SAReturnDetail SAR on SAR.ID = a.DetailID
                          join SAInvoiceDetail b on b.ID = SAR.SAInvoiceDetailID
                          left join MaterialGoods c on b.MaterialGoodsID = c.ID
                          left join SAInvoice d on d.ID = b.SAInvoiceID
                          left join Unit e on b.UnitID = e.ID
                 where a.PostedDate between @FromDate and @ToDate
                   and a.Account like '521%'
                   and (a.TypeID in (330, 340))
--                    and (@AccountObjectID is null OR
--                         (a.AccountingObjectID in (select AccountObjectID from #tblListAccountObjectID)))
--                    and (@MaterialGoods is null OR
--                         (b.MaterialGoodsID in (select MaterialGoodsID from #tblListMaterialGoods)))
                 group by a.Date, a.PostedDate, a.No, a.InvoiceDate, a.InvoiceNo, accountCorresponding,
                          e.UnitName, b.Quantity, b.UnitPrice, c.ID, c.MaterialGoodsName, d.reason, a.OrderPriority,
                          a.ReferenceID, a.TypeID, c.MaterialGoodsCode, c.id
             ) GG
        GROUP BY ngayCTu, ngayHachToan, soHieu, ngayHoaDon, soHoaDon, dienGiai, tkDoiUng, dvt, soLuong, donGia,
                 MaterialGoodsName, refID, TypeID, materialGoodsCode, materialGoodsID


--         SELECT * FROM @tbLuuTru ORDER BY MaterialGoodsName, ngayHachToan, soHieu
        select a.ngayCTu,
               a.ngayHachToan,
               a.soHieu,
               a.ngayHoaDon,
               a.soHoaDon,
               a.dienGiai,
               a.tkDoiUng,
               a.dvt,
               a.soLuong,
               a.donGia,
               a.thanhTien,
               a.thue,
               a.khac,
               a.materialGoodsName,
               a.materialGoodsCode,
               a.materialGoodsID,
               a.refID,
               a.typeID,
               a.giaVon,
               a.tongSoLuong,
               a.tongThanhTien,
               a.tongThue,
               a.tongKhac,
               (isnull(a.tongThanhTien, 0) - isnull(a.tongThue, 0) - isnull(a.tongKhac, 0)) as doanhThuThuan,
               (isnull(a.tongThanhTien, 0) - isnull(a.tongThue, 0) - isnull(a.tongKhac, 0) - isnull(a.giaVon, 0)) as laiGop
        from (
                 SELECT ngayCTu,
                        ngayHachToan,
                        soHieu,
                        ngayHoaDon,
                        soHoaDon,
                        dienGiai,
                        tkDoiUng,
                        dvt,
                        soLuong,
                        donGia,
                        thanhTien,
                        thue,
                        khac,
                        materialGoodsName,
                        materialGoodsCode,
                        materialGoodsID,
                        refID,
                        typeID,
                        (select sum(ISNULL(debitamount, 0))
                         from GeneralLedger gl
                         where Account like '632%'
                           and gl.PostedDate between @FromDate and @ToDate
                           and tb.materialGoodsID = gl.MaterialGoodsID
                           and gl.TypeLedger in (@IsMBook, 2)
                           and gl.AccountingObjectID in
                               (select AccountObjectID from #tblListAccountObjectID)) as giaVon,
                        (select sum(tb1.soLuong)
                         from @tbLuuTru tb1
                         where tb.materialGoodsID = tb1.materialGoodsID)              as tongSoLuong,
                        (select sum(tb1.thanhTien)
                         from @tbLuuTru tb1
                         where tb.materialGoodsID = tb1.materialGoodsID)              as tongThanhTien,
                        (select sum(tb1.thue)
                         from @tbLuuTru tb1
                         where tb.materialGoodsID = tb1.materialGoodsID)              as tongThue,
                        (select sum(tb1.khac)
                         from @tbLuuTru tb1
                         where tb.materialGoodsID = tb1.materialGoodsID)              as tongKhac
                 FROM @tbLuuTru tb
             ) a
        ORDER BY MaterialGoodsName, ngayHachToan, soHieu

        --, OrderPriority
        /*end add by cuongpv*/

        --select  isnull(sum(a.debitAmount),0) Sum_Gia_Goc, c.MaterialGoodsCode,c.ID  as InventoryItemID
        -- from GeneralLedger a
        --join SAInvoiceDetail b on a.ReferenceID = b.SAInvoiceID
        --join MaterialGoods c on c.ID = b.MaterialGoodsID
        -- where PostedDate between @FromDate and @ToDate and Account = '632' and c.ID in (select InventoryItemID from #tblListInventoryItemID)
        -- group by c.MaterialGoodscode,c.ID;

        /*add by cuongpv*/
--     SELECT SUM(g.Sum_Gia_Goc - Sum_Gia_TLGG) as Sum_Gia_Goc, g.MaterialGoodsCode, g.InventoryItemID
--     FROM (/*end add by cuongpv*/
--              select isnull(sum(OWAmount), 0) Sum_Gia_Goc, 0 as Sum_Gia_TLGG, b.MaterialGoodsCode, b.ID InventoryItemID
--              from SAInvoiceDetail a,
--                   MaterialGoods b
--              where exists(select InventoryItemID from #tblListInventoryItemID where InventoryItemID = a.MaterialGoodsID)
--                and exists(select ReferenceID
--                           from @tbDataGL
--                           where account like '632%'
--                             and PostedDate between @FromDate and @ToDate
--                             and ReferenceID = a.SAinvoiceID)/*edit by cuonpv: GeneralLedger -> @tbDataGL;account = '632' -> account like '632%'*/
--                and a.MaterialGoodsID = b.ID
--              group by b.ID, b.MaterialGoodsCode, OrderPriority
--                       --Order by OrderPriority /*comment by cuongpv*/
--              union all
--              select 0                        as Sum_Gia_Goc,
--                     isnull(sum(OWAmount), 0) as Sum_Gia_TLGG,
--                     b.MaterialGoodsCode,
--                     b.ID                        InventoryItemID
--              from SAReturnDetail a,
--                   MaterialGoods b
--              where exists(select InventoryItemID from #tblListInventoryItemID where InventoryItemID = a.MaterialGoodsID)
--                and exists(select ReferenceID
--                           from @tbDataGL
--                           where account like '632%'
--                             and PostedDate between @FromDate and @ToDate
--                             and DetailID = a.ID)
--                and a.MaterialGoodsID = b.ID
--              group by b.ID, b.MaterialGoodsCode, OrderPriority
--              /*add by cuongpv*/
--          ) g
--     GROUP BY g.MaterialGoodsCode, g.InventoryItemID
--     ORDER BY MaterialGoodsCode
        /*end add by cuongpv*/


    end
go
