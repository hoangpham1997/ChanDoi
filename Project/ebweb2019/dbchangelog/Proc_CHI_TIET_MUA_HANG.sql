/*
-- =============================================
-- Edit by:		congnd
-- Edit date: 03.05.2019
-- Description:	Báo cáo sổ chi tiết mua hàng
-- EXEC [Proc_CHI_TIET_MUA_HANG] @FromDate = '2019-03-01', @ToDate = '2020-03-09', @AccountObjectID = null, @MaterialGoods = null, @CompanyID = '5A814271-A115-41D1-BA4D-C50BC0040482', @IsMBook = false, @EmployeeID = null, @isDependent = false;
-- =============================================
*/
    CREATE procedure [dbo].[Proc_CHI_TIET_MUA_HANG]
    @FromDate DATETIME,
    @ToDate DATETIME,
    @AccountObjectID AS NVARCHAR(MAX),
    @MaterialGoods as NVARCHAR(MAX),
    @CompanyID UNIQUEIDENTIFIER,
    @IsMBook BIT = 0,
    @EmployeeID UNIQUEIDENTIFIER,
    @isDependent BIT = 0
    as
    begin
        SET @EmployeeID = (CASE WHEN @EmployeeID = '00000000-0000-0000-0000-000000000000' THEN NULL
            ELSE @EmployeeID end )
        CREATE TABLE #tblListAccountObjectID
        (
            AccountObjectID   UNIQUEIDENTIFIER,
            AccountObjectCode NVARCHAR(100)
                COLLATE SQL_Latin1_General_CP1_CI_AS,
            AccountObjectName NVARCHAR(512)
                COLLATE SQL_Latin1_General_CP1_CI_AS
        )
        CREATE TABLE #tblListMaterialGoods
        (
            MaterialGoodsID   UNIQUEIDENTIFIER,
            MaterialGoodsCode NVARCHAR(100)
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
        /*select * from #tblListMaterialGoods
        select * from #tblListAccountObjectID*/
        /*Tao bang tam luu du lieu ra bao cao*/
        DECLARE @tbDataResult TABLE
                              (
                                  AccountObjectID UNIQUEIDENTIFIER,
                                  MaKH            NVARCHAR(25),  --AccountObjectCode
                                  TenKH           NVARCHAR(512), --AccountObjectName
                                  NgayHachToan    DATE,
                                  NgayCTu         DATE,
                                  SoCTu           NVARCHAR(25),
                                  SoHoaDon        NVARCHAR(25),
                                  NgayHoaDon      DATE,
                                  MaterialGoodsID UNIQUEIDENTIFIER,
                                  Mahang          NVARCHAR(50),  --MaterialGoodsCode
                                  Tenhang         NVARCHAR(512), --MaterialGoodsName
                                  DVT             NVARCHAR(25),  --Unit
                                  SoLuongMua      DECIMAL(25, 10),
                                  DonGia          MONEY,
                                  GiaTriMua       money,
                                  ChietKhau       money,
                                  SoLuongTraLai   money,
                                  GiaTriTraLai    money,
                                  GiaTriGiamGia   money,
                                  TypeMG          NVARCHAR(1),
                                  RefID           UNIQUEIDENTIFIER,
                                  TypeID          int
                              )


        BEGIN
            /*lay gia tri PPInvoice vao @tbDataResult*/
--             Lấy SL mua/đơn giá/giá trị mua/chiết khấu từ Mua hàng
            INSERT INTO @tbDataResult(AccountObjectID, NgayHachToan, NgayCTu, SoCTu, SoHoaDon, NgayHoaDon,
                                      MaterialGoodsID, DVT, SoLuongMua, DonGia, GiaTriMua, ChietKhau, TypeMG, RefID,
                                      TypeID, SoLuongTraLai, GiaTriTraLai, GiaTriGiamGia)
            SELECT a.AccountingObjectID                                     as AccountObjectID,
                   a.Date                                                   as NgayHachToan,
                   a.PostedDate                                             as NgayCTu,
                   CASE WHEN @IsMBook = 1 then a.NoMBook else a.NoFBook end as SoCTu,
                   b.InvoiceNo                                              as SoHoaDon,
                   b.InvoiceDate                                            as NgayHoaDon,
                   b.MaterialGoodsID                                        as MaterialGoodsID,
                   e.UnitName                                               as DVT,
                   b.Quantity                                               as SoLuongMua,
                   b.UnitPrice                                              as DonGia,
                   b.Amount                                                 as GiaTriMua,
                   b.DiscountAmount                                         as ChietKhau,
                   'I'                                                      as TypeMG,
                   a.ID                                                     as RefID,
                   a.TypeID,
                   (select sum(COALESCE(ppdrt.Quantity, 0)) from PPDiscountReturnDetail ppdrt inner join PPDiscountReturn ppdr on ppdrt.PPDiscountReturnID = ppdr.id where ppdrt.PPInvoiceDetailID = b.id and ppdr.TypeID = 220)       as SoLuongTraLai,
                   (select sum(COALESCE(ppdrt.Amount, 0)) from PPDiscountReturnDetail ppdrt inner join PPDiscountReturn ppdr on ppdrt.PPDiscountReturnID = ppdr.id where ppdrt.PPInvoiceDetailID = b.id and ppdr.TypeID = 220)          as GiaTriTraLai,
                   (select sum(COALESCE(ppdrt.Amount, 0)) from PPDiscountReturnDetail ppdrt inner join PPDiscountReturn ppdr on ppdrt.PPDiscountReturnID = ppdr.id where ppdrt.PPInvoiceDetailID = b.id and ppdr.TypeID = 230)          as GiaTriGiamGia
            FROM PPInvoice a
                     join PPInvoiceDetail b ON a.id = b.PPInvoiceID
                     left join MaterialGoods c on c.id = b.MaterialGoodsID
                     left join AccountingObject d on d.ID = a.AccountingObjectID
                     left join Unit e on e.id = b.UnitID
            WHERE a.PostedDate between @FromDate and @ToDate
              and a.Recorded = 1
              and a.Companyid in (select id from Func_getCompany (@CompanyID, @isDependent))
              and a.TypeLedger in (@IsMBook, 2)
              and (@AccountObjectID is null OR
                   (a.AccountingObjectID in (select AccountObjectID from #tblListAccountObjectID)))
              and (@MaterialGoods is null OR
                   (b.MaterialGoodsID in (select MaterialGoodsID from #tblListMaterialGoods)))
              and (@EmployeeID is null OR a.EmployeeID = @EmployeeID)


            /*Lay gia tri PPService vao @tbDataResult*/
--             Lấy SL mua/đơn giá/giá trị mua/chiết khấu từ Mua dịch vụ
            INSERT INTO @tbDataResult(AccountObjectID, NgayHachToan, NgayCTu, SoCTu, SoHoaDon, NgayHoaDon,
                                      MaterialGoodsID, DVT, SoLuongMua, DonGia, GiaTriMua, ChietKhau, TypeMG, RefID,
                                      TypeID)
            SELECT a.AccountingObjectID                                     as AccountObjectID,
                   a.Date                                                   as NgayHachToan,
                   a.PostedDate                                             as NgayCTu,
                   CASE WHEN @IsMBook = 1 then a.NoMBook else a.NoFBook end as SoCTu,
                   b.InvoiceNo                                              as SoHoaDon,
                   b.InvoiceDate                                            as NgayHoaDon,
                   b.MaterialGoodsID                                        as MaterialGoodsID,
                   e.UnitName                                               as DVT,
                   b.Quantity                                               as SoLuongMua,
                   b.UnitPrice                                              as DonGia,
                   b.Amount                                                 as GiaTriMua,
                   b.DiscountAmount                                         as ChietKhau,
                   'S'                                                      as TypeMG,
                   a.ID                                                     as RefID,
                   a.TypeID
            FROM PPService a
                     JOIN PPServiceDetail b ON a.ID = b.PPServiceID
                     left join MaterialGoods c on c.id = b.MaterialGoodsID
                     left join AccountingObject d on d.ID = a.AccountingObjectID
                     left join Unit e on e.id = b.UnitID
            WHERE a.PostedDate between @FromDate and @ToDate
              and a.Recorded = 1
              and a.Companyid in (select id from Func_getCompany (@CompanyID, @isDependent))
              and a.TypeLedger in (@IsMBook, 2)
              and (@AccountObjectID is null OR
                   (a.AccountingObjectID in (select AccountObjectID from #tblListAccountObjectID)))
              and (@MaterialGoods is null OR
                   (b.MaterialGoodsID in (select MaterialGoodsID from #tblListMaterialGoods)))
              and (@EmployeeID is null OR a.EmployeeID = @EmployeeID)

            --             /*Update DVT cua cac gia tri lay tu PPService*/
--             UPDATE @tbDataResult
--             SET DVT = MS.DVT
--             FROM (
--                      SELECT ID as MSID, Unit as DVT
--                      FROM dbo.MaterialGoods
--                  ) MS
--             WHERE MaterialGoodsID = MS.MSID
--               AND TypeMG = 'S'


            /*Lay gia tri Tra lai vao @tbDataResult*/
--             Lấy đơn giá/số lượng trả lại/giá trị trả lại từ hàng mua trả lại
            INSERT INTO @tbDataResult(AccountObjectID, NgayHachToan, NgayCTu, SoCTu, SoHoaDon, NgayHoaDon,
                                      MaterialGoodsID, DVT, DonGia, SoLuongTraLai, GiaTriTraLai, TypeMG, RefID, TypeID)
            SELECT a.AccountingObjectID                                     as AccountObjectID,
                   a.Date                                                   as NgayHachToan,
                   a.PostedDate                                             as NgayCTu,
                   CASE WHEN @IsMBook = 1 then a.NoMBook else a.NoFBook end as SoCTu,
                   a.InvoiceNo                                              as SoHoaDon,
                   a.InvoiceDate                                            as NgayHoaDon,
                   b.MaterialGoodsID                                        as MaterialGoodsID,
                   e.UnitName                                               as DVT,
                   b.UnitPrice                                              as DonGia,
                   b.Quantity                                               as SoLuongTraLai,
                   b.Amount                                                 as GiaTriTraLai,
                   'T'                                                      as TypeMG,
                   a.ID                                                     as RefID,
                   a.TypeID
            FROM PPDiscountReturn a
                     JOIN PPDiscountReturnDetail b ON a.ID = b.PPDiscountReturnID
                     left join MaterialGoods c on c.id = b.MaterialGoodsID
                     left join AccountingObject d on d.ID = a.AccountingObjectID
                     left join Unit e on e.id = b.UnitID
                     left join PPInvoice ppi on b.PPInvoiceID = ppi.ID
            WHERE a.PostedDate between @FromDate and @ToDate
              and a.TypeID = 220
              and a.Recorded = 1
              and a.Companyid in (select id from Func_getCompany (@CompanyID, @isDependent))
              and a.TypeLedger in (@IsMBook, 2)
              and (@AccountObjectID is null OR
                   (a.AccountingObjectID in (select AccountObjectID from #tblListAccountObjectID)))
              and (@MaterialGoods is null OR
                   (b.MaterialGoodsID in (select MaterialGoodsID from #tblListMaterialGoods)))
              and (@EmployeeID is null OR a.EmployeeID = @EmployeeID)
              and ppi.ID is null


            /*lay gia tri giam gia vao @tbDataResult*/
--             Lấy giá trị giảm giá từ hàng mua giảm giá
            INSERT INTO @tbDataResult(AccountObjectID, NgayHachToan, NgayCTu, SoCTu, SoHoaDon, NgayHoaDon,
                                      MaterialGoodsID, DVT, GiaTriGiamGia, TypeMG, RefID, TypeID)
            select a.AccountingObjectID                                     as AccountObjectID,
                   a.Date                                                   as NgayHachToan,
                   a.PostedDate                                             as NgayCTu,
                   CASE WHEN @IsMBook = 1 then a.NoMBook else a.NoFBook end as SoCTu,
                   InvoiceNo                                                as SoHoaDon,
                   InvoiceDate                                              as NgayHoaDon,
                   b.MaterialGoodsID                                        as MaterialGoodsID,
                   e.UnitName                                               as DVT,
                   b.Amount                                                 as GiaTriGiamGia,
                   'G'                                                      as TypeMG,
                   a.ID                                                     as RefID,
                   a.TypeID
            from PPDiscountReturn a
                     JOIN PPDiscountReturnDetail b ON a.ID = b.PPDiscountReturnID
                     left join MaterialGoods c on c.id = b.MaterialGoodsID
                     left join AccountingObject d on d.ID = a.AccountingObjectID
                     left join Unit e on e.id = b.UnitID
                     left join PPInvoice ppi on b.PPInvoiceID = ppi.ID
            where a.PostedDate between @FromDate and @ToDate
              and a.TypeID = 230
              and a.Companyid in (select id from Func_getCompany (@CompanyID, @isDependent))
              and a.TypeLedger in (@IsMBook, 2)
              and a.Recorded = 1
              and (@AccountObjectID is null OR
                   (a.AccountingObjectID in (select AccountObjectID from #tblListAccountObjectID)))
              and (@MaterialGoods is null OR
                   (b.MaterialGoodsID in (select MaterialGoodsID from #tblListMaterialGoods)))
              and (@EmployeeID is null OR a.EmployeeID = @EmployeeID)
              and ppi.ID is null
        END


        /*Update makh, tenkh, mahang, ten hang vao @tbDataResult*/
        UPDATE @tbDataResult
        SET MaKH  = AO.MaKH,
            TenKH = AO.TenKH
        FROM (
                 SELECT ID as AOID, AccountingObjectCode as MaKH, AccountingObjectName as TenKH
                 FROM dbo.AccountingObject
             ) AO
        WHERE AccountObjectID = AO.AOID

        UPDATE @tbDataResult
        SET Mahang  = M.Mahang,
            Tenhang = M.Tenhang
        FROM (
                 SELECT ID as MID, MaterialGoodsCode as Mahang, MaterialGoodsName as Tenhang FROM dbo.MaterialGoods
             ) M
        WHERE MaterialGoodsID = M.MID

        /*lay du lieu ra bao cao*/
        SELECT maKH,
               tenKH,
               ngayHachToan,
               ngayCTu,
               soCTu,
               soHoaDon,
               ngayHoaDon,
               mahang,
               tenhang,
               dvt,
               soLuongMua,
               donGia,
               giaTriMua,
               chietKhau,
               soLuongTraLai,
               giaTriTraLai,
               giaTriGiamGia,
               refID,
               typeID
        FROM @tbDataResult
        where (SoLuongMua != 0 or DonGia != 0 or GiaTriMua != 0 or ChietKhau != 0 or SoLuongTraLai != 0 or GiaTriTraLai != 0 or GiaTriGiamGia != 0)
        order by ngayHachToan, SoCTu
    end
go

