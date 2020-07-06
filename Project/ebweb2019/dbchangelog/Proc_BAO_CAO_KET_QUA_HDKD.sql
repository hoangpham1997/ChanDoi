/*
-- =============================================
-- Author:		<chuongnv>
-- Create date: <18-02-2020>
-- Description:	<Lấy dữ liệu báo cáo kết quả hđkd>
-- =============================================
*/
CREATE PROCEDURE [dbo].[Proc_BAO_CAO_KET_QUA_HDKD](@CompanyID UNIQUEIDENTIFIER,
                                                   @IsWorkingWithManagementBook BIT,
                                                   @FromDate DATETIME,
                                                   @ToDate DATETIME,
                                                   @PrevFromDate DATETIME,
                                                   @PrevToDate DATETIME,
                                                   @IncludeDependentBranch BIT)
AS
BEGIN

    DECLARE
        @Result TABLE
                    (
                        ItemID          UNIQUEIDENTIFIER,
                        ItemCode        NVARCHAR(25),
                        ItemName        NVARCHAR(512),
                        ItemNameEnglish NVARCHAR(512),
                        ItemIndex       INT,
                        Description     NVARCHAR(512),
                        FormulaType     INT,
                        FormulaFrontEnd NVARCHAR(MAX),
                        Formula         XML,
                        Hidden          BIT,
                        IsBold          BIT,
                        IsItalic        BIT,
                        Amount          DECIMAL(25, 4),
                        PrevAmount      DECIMAL(25, 4)
                    )
    DECLARE
        @AccountingSystem INT
    SET @AccountingSystem = 15 --- Áp dụng theo QD 15/48
    DECLARE
        @ReportID NVARCHAR(100)
    SET @ReportID = '2'
    /*báo cáo kết quả HĐ kinh doanh*/
    -- Tinh toan lay lai nam truoc
    set @PrevToDate = DATEADD(DD, -1, @FromDate)
    declare
        @CalPrevDate nvarchar(20)
    declare
        @CalPrevMonth nvarchar(20)
    declare
        @CalPrevDay nvarchar(20)
    set @CalPrevDate =
                CAST(day(@FromDate) AS varchar) + CAST(month(@FromDate) AS varchar) + CAST(day(@ToDate) AS varchar) +
                CAST(month(@ToDate) AS varchar)
    set @CalPrevMonth = CAST(month(@FromDate) AS varchar) + CAST(month(@ToDate) AS varchar)
    set @CalPrevDay = CAST(day(@FromDate) AS varchar) + CAST(day(@ToDate) AS varchar)
    if @CalPrevDate = '113112'
        set @PrevFromDate = convert(DATETIME, dbo.ctod('D', dbo.godtos('M', -12, @FromDate)), 103)
    else
        if (@CalPrevMonth in (55, 77, 1010, 1212) and @CalPrevDay in (131))
            set @PrevFromDate = DATEADD(DD, -30, @FromDate)
        else
            if (@CalPrevMonth in (11, 22, 88, 44, 66, 99, 1111) and @CalPrevDay in (131, 130))
                set @PrevFromDate = DATEADD(DD, -31, @FromDate)
            else
                if (@CalPrevMonth in (33) and @CalPrevDay in (131) and CAST(year(@FromDate) AS varchar) % 4 = 0)
                    set @PrevFromDate = DATEADD(DD, -29, @FromDate)
                else
                    if (@CalPrevMonth in (33) and @CalPrevDay in (131) and CAST(year(@FromDate) AS varchar) % 4 <> 0)
                        set @PrevFromDate = DATEADD(DD, -28, @FromDate)
                    else
                        if ((@CalPrevMonth in (13, 1012) and @CalPrevDay = '131') or
                            (@CalPrevMonth in (46, 79) and @CalPrevDay = '130'))
                            set @PrevFromDate = DATEADD(QQ, -1, @FromDate)
                        else
                            set @PrevFromDate = DATEADD(DD, -1 - DATEDIFF(day, @FromDate, @ToDate), @FromDate)

    -- Bảng tạm các chỉ tiêu chi tiết
    DECLARE
        @tblItem TABLE
                 (
                     ItemID                     UNIQUEIDENTIFIER,
                     ItemName                   NVARCHAR(255),
                     OperationSign              INT,
                     OperandString              NVARCHAR(512),
                     AccountNumber              VARCHAR(25),
                     CorrespondingAccountNumber VARCHAR(25),
                     IsDetailGreaterThanZero    BIT --Có lấy giá trị lớn hơn 0 không?
                 )

    INSERT @tblItem
    SELECT ItemID,
           ItemName,
           x.r.value('@OperationSign', 'INT'),
           x.r.value('@OperandString', 'nvarchar(512)'),
           x.r.value('@AccountNumber', 'nvarchar(25)') + '%',
           CASE
               WHEN x.r.value('@CorrespondingAccountNumber',
                              'nvarchar(25)') <> ''
                   THEN x.r.value('@CorrespondingAccountNumber',
                                  'nvarchar(25)') + '%'
               ELSE ''
               END,
           CASE
               WHEN FormulaType = 0 THEN CAST(0 AS BIT)
               ELSE CAST(1 AS BIT)
               END
    FROM FRTemplate
             CROSS APPLY Formula.nodes('/root/DetailFormula') AS x (r)
    WHERE AccountingSystem = @AccountingSystem
      AND ReportID = @ReportID
      AND (FormulaType = 0/*chỉ tiêu chi tiết*/
        OR FormulaType = 2/*chỉ tiêu chi tiết chỉ được lấy số liệu khi kết quả>0*/
        )
      AND Formula IS NOT NULL
    --/*chỉ lấy các chỉ tiêu có công thức*/
    --and itemcode not in ('31.1', '31.2', '32.1' , '32.2')

    --Bảng tạm lấy ra dữ liệu cho các tài khoản và chi tiết theo nghiệp vụ thanh lý TSCĐ.
    DECLARE
        @Balance TABLE
                 (
                     AccountNumber                NVARCHAR(25),
                     CorrespondingAccountNumber   NVARCHAR(25),
                     PostedDate                   DATETIME,
                     CreditAmount                 DECIMAL(25, 4),
                     DebitAmount                  DECIMAL(25, 4),
                     CreditAmountDetailBy         DECIMAL(25, 4),
                     DebitAmountDetailBy          DECIMAL(25, 4),
                     IsDetailBy                   BIT,
                     CreditAmountByBussinessType0 DECIMAL(25, 4),/*Có Chi tiết theo loại  0-Chiết khấu thương mai*/
                     CreditAmountByBussinessType1 DECIMAL(25, 4),/*Có Chi tiết theo loại  1 - Giảm giá hàng bán*/
                     CreditAmountByBussinessType2 DECIMAL(25, 4),/*Có Chi tiết theo loại   2- trả lại hàng bán*/
                     DebitAmountByBussinessType0  DECIMAL(25, 4),/*Nợ Chi tiết theo loại  0-Chiết khấu thương mai*/
                     DebitAmountByBussinessType1  DECIMAL(25, 4),/*Nợ Chi tiết theo loại  1 - Giảm giá hàng bán*/
                     DebitAmountByBussinessType2  DECIMAL(25, 4) /*Nợ Chi tiết theo loại   2- trả lại hàng bán*/
                 )
/*
1. Doanh thu bán hàng và cung cấp dịch vụ

PS Có TK 511 (không kể PSĐƯ N911/C511, không kể các nghiệp vụ: Chiết khấu thương mại (bán hàng),

 Giảm giá hàng bán, Trả lại hàng bán)
  – PS Nợ TK 511 (không kể PSĐƯ N511/911, không kể các nghiệp vụ: Chiết khấu thương mại (bán hàng),
  Giảm giá hàng bán, Trả lại hàng bán)
*/

    INSERT INTO @Balance
    SELECT GL.Account,
           GL.AccountCorresponding,
           GL.PostedDate,
           SUM(ISNULL(CreditAmount, 0)) AS CreditAmount,
           SUM(ISNULL(DebitAmount, 0))  AS DebitAmount,
           0                            AS CreditAmountDetailBy,
           0                            AS DebitAmountDetailBy,
           0,
           SUM(CASE
                   WHEN (GL.TypeID in ('320', '321', '322') and
                         gl.Account like '511%')
                       THEN ISNULL(CreditAmount, 0)
                   ELSE 0
               END)                     AS CreditAmountByBussinessType0,
           SUM(CASE
                   WHEN GL.TypeID = 340
                       THEN ISNULL(CreditAmount, 0)
                   ELSE 0
               END)                     AS CreditAmountByBussinessType1
            ,
           SUM(CASE
                   WHEN GL.TypeID = 330
                       THEN ISNULL(CreditAmount, 0)
                   ELSE 0
               END)                     AS CreditAmountByBussinessType2,
           SUM(CASE
                   WHEN (GL.TypeID in ('320', '321', '322') and
                         gl.Account like '511%')
                       THEN ISNULL(DebitAmount, 0)
                   ELSE 0
               END)                     AS DebitAmountByBussinessType0
            ,
           SUM(CASE
                   WHEN GL.TypeID = 340
                       THEN ISNULL(DebitAmount, 0)
                   ELSE 0
               END)                     AS DebitAmountByBussinessType1
            ,
           SUM(CASE
                   WHEN GL.TypeID = 330
                       THEN ISNULL(DebitAmount, 0)
                   ELSE 0
               END)                     AS DebitAmountByBussinessType2
    FROM GeneralLedger GL
    WHERE GL.CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch))
      AND (GL.TypeLedger = @IsWorkingWithManagementBook OR GL.TypeLedger = 2)
      AND PostedDate BETWEEN @PrevFromDate AND @ToDate
    GROUP BY GL.Account,
             GL.AccountCorresponding,
             GL.PostedDate
    OPTION ( RECOMPILE )

    -- Bảng tạm cho các chỉ tiêu
    DECLARE
        @tblMasterDetail TABLE
                         (
                             ItemID          UNIQUEIDENTIFIER,
                             DetailItemID    UNIQUEIDENTIFIER,
                             OperationSign   INT,
                             Grade           INT,
                             DebitAmount     DECIMAL(25, 4),
                             PrevDebitAmount DECIMAL(25, 4)
                         )
    -- Lấy dữ liệu cho các chỉ tiêu chi tiết
    INSERT INTO @tblMasterDetail
    SELECT I.ItemID,
           NULL,
           1,
           -1,
           CASE
               WHEN I.IsDetailGreaterThanZero = 0
                   OR (SUM(CASE
                               WHEN GL.PostedDate BETWEEN @FromDate AND @ToDate
                                   THEN (CASE
                                             WHEN I.OperandString = 'PhatsinhCO'
                                                 THEN GL.CreditAmount
                                             WHEN I.OperandString IN (
                                                                      'PhatsinhNO',
                                                                      'PhatsinhDU')
                                                 THEN GL.DebitAmount
                                             WHEN I.OperandString = 'PhatsinhNO_ChitietChietKhauThuongmai' THEN
                                                 GL.DebitAmountByBussinessType0
                                             WHEN I.OperandString = 'PhatsinhNO_ChitietGiamgiaHangBan' THEN
                                                 GL.DebitAmountByBussinessType1
                                             WHEN I.OperandString = 'PhatsinhNO_ChitietTralaiHangBan' THEN
                                                 GL.DebitAmountByBussinessType2
                                             WHEN I.OperandString = 'PhatsinhCO_ChitietChietKhauThuongmai1111' THEN
                                                 GL.CreditAmountByBussinessType0
                                             WHEN I.OperandString = 'PhatsinhCO_ChitietGiamgiaHangBan' THEN
                                                 GL.CreditAmountByBussinessType1
                                             WHEN I.OperandString = 'PhatsinhCO_ChitietTralaiHangBan' THEN
                                                 GL.CreditAmountByBussinessType2

                                             WHEN I.OperandString IN (
                                                 'PhatsinhCO_ChiTietThanhlyTSCD_BDSDT')
                                                 THEN GL.CreditAmount --GL.CreditAmountDetailBy
                                             WHEN I.OperandString IN (
                                                 'PhatsinhNO_ChiTietThanhlyTSCD_BDSDT')
                                                 THEN GL.DebitAmount --GL.DebitAmountDetailBy
                                             ELSE 0
                                   END) * I.OperationSign
                               ELSE 0
                       END)) > 0
                   THEN (SUM(CASE
                                 WHEN GL.PostedDate BETWEEN @FromDate AND @ToDate
                                     THEN (CASE
                                               WHEN I.OperandString = 'PhatsinhCO'
                                                   THEN GL.CreditAmount
                                               WHEN I.OperandString IN (
                                                                        'PhatsinhNO',
                                                                        'PhatsinhDU')
                                                   THEN GL.DebitAmount

                                               WHEN I.OperandString = 'PhatsinhNO_ChitietChietKhauThuongmai' THEN
                                                   GL.DebitAmountByBussinessType0
                                               WHEN I.OperandString = 'PhatsinhNO_ChitietGiamgiaHangBan' THEN
                                                   GL.DebitAmountByBussinessType1
                                               WHEN I.OperandString = 'PhatsinhNO_ChitietTralaiHangBan' THEN
                                                   GL.DebitAmountByBussinessType2
                                               WHEN I.OperandString = 'PhatsinhCO_ChitietChietKhauThuongmai1111' THEN
                                                   GL.CreditAmountByBussinessType0
                                               WHEN I.OperandString = 'PhatsinhCO_ChitietGiamgiaHangBan' THEN
                                                   GL.CreditAmountByBussinessType1
                                               WHEN I.OperandString = 'PhatsinhCO_ChitietTralaiHangBan' THEN
                                                   GL.CreditAmountByBussinessType2
                                               WHEN I.OperandString IN (
                                                   'PhatsinhCO_ChiTietThanhlyTSCD_BDSDT')
                                                   THEN GL.CreditAmount --GL.CreditAmountDetailBy
                                               WHEN I.OperandString IN (
                                                   'PhatsinhNO_ChiTietThanhlyTSCD_BDSDT')
                                                   THEN GL.DebitAmount --GL.DebitAmountDetailBy
                                               ELSE 0
                                     END) * I.OperationSign
                                 ELSE 0
                   END))
               ELSE 0
               END AS DebitAmount,
           CASE
               WHEN I.IsDetailGreaterThanZero = 0
                   OR (SUM(CASE
                               WHEN GL.PostedDate BETWEEN @PrevFromDate
                                   AND
                                   @PrevToDate
                                   THEN CASE
                                            WHEN I.OperandString = 'PhatsinhCO'
                                                THEN GL.CreditAmount
                                            WHEN I.OperandString IN (
                                                                     'PhatsinhNO',
                                                                     'PhatsinhDU')
                                                THEN GL.DebitAmount

                                            WHEN I.OperandString = 'PhatsinhNO_ChitietChietKhauThuongmai' THEN
                                                GL.DebitAmountByBussinessType0
                                            WHEN I.OperandString = 'PhatsinhNO_ChitietGiamgiaHangBan' THEN
                                                GL.DebitAmountByBussinessType1
                                            WHEN I.OperandString = 'PhatsinhNO_ChitietTralaiHangBan' THEN
                                                GL.DebitAmountByBussinessType2
                                            WHEN I.OperandString = 'PhatsinhCO_ChitietChietKhauThuongmai1111' THEN
                                                GL.CreditAmountByBussinessType0
                                            WHEN I.OperandString = 'PhatsinhCO_ChitietGiamgiaHangBan' THEN
                                                GL.CreditAmountByBussinessType1
                                            WHEN I.OperandString = 'PhatsinhCO_ChitietTralaiHangBan' THEN
                                                GL.CreditAmountByBussinessType2

                                            WHEN I.OperandString IN (
                                                'PhatsinhCO_ChiTietThanhlyTSCD_BDSDT')
                                                THEN GL.CreditAmount --GL.CreditAmountDetailBy
                                            WHEN I.OperandString IN (
                                                'PhatsinhNO_ChiTietThanhlyTSCD_BDSDT')
                                                THEN GL.DebitAmount --GL.DebitAmountDetailBy
                                            ELSE 0
                                            END * I.OperationSign
                               ELSE 0
                       END)) > 0
                   THEN SUM(CASE
                                WHEN GL.PostedDate BETWEEN @PrevFromDate
                                    AND
                                    @PrevToDate
                                    THEN CASE
                                             WHEN I.OperandString = 'PhatsinhCO'
                                                 THEN GL.CreditAmount
                                             WHEN I.OperandString IN (
                                                                      'PhatsinhNO',
                                                                      'PhatsinhDU')
                                                 THEN GL.DebitAmount
                                             WHEN I.OperandString = 'PhatsinhNO_ChitietChietKhauThuongmai' THEN
                                                 GL.DebitAmountByBussinessType0
                                             WHEN I.OperandString = 'PhatsinhNO_ChitietGiamgiaHangBan' THEN
                                                 GL.DebitAmountByBussinessType1
                                             WHEN I.OperandString = 'PhatsinhNO_ChitietTralaiHangBan' THEN
                                                 GL.DebitAmountByBussinessType2
                                             WHEN I.OperandString = 'PhatsinhCO_ChitietChietKhauThuongmai1111' THEN
                                                 GL.CreditAmountByBussinessType0
                                             WHEN I.OperandString = 'PhatsinhCO_ChitietGiamgiaHangBan' THEN
                                                 GL.CreditAmountByBussinessType1
                                             WHEN I.OperandString = 'PhatsinhCO_ChitietTralaiHangBan' THEN
                                                 GL.CreditAmountByBussinessType2
                                             WHEN I.OperandString IN (
                                                 'PhatsinhCO_ChiTietThanhlyTSCD_BDSDT')
                                                 THEN GL.CreditAmount --GL.CreditAmountDetailBy
                                             WHEN I.OperandString IN (
                                                 'PhatsinhNO_ChiTietThanhlyTSCD_BDSDT')
                                                 THEN GL.DebitAmount --GL.DebitAmountDetailBy
                                             ELSE 0
                                             END * I.OperationSign
                                ELSE 0
                   END)
               ELSE 0
               END AS DebitAmount
    FROM @tblItem I
             INNER JOIN @Balance AS GL ON (GL.AccountNumber LIKE I.AccountNumber)
        AND (I.OperandString <> 'PhatsinhDU'
            OR (I.OperandString = 'PhatsinhDU'
                AND GL.CorrespondingAccountNumber LIKE I.CorrespondingAccountNumber
                 )
                                              )
    GROUP BY I.ItemID,
             I.IsDetailGreaterThanZero
    OPTION ( RECOMPILE )

    -- Lấy các chỉ tiêu tổng hợp
    INSERT @tblMasterDetail
    SELECT ItemID,
           x.r.value('@ItemID', 'UNIQUEIDENTIFIER'),
           x.r.value('@OperationSign', 'INT'),
           0,
           0.0,
           0.0
    FROM FRTemplate
             CROSS APPLY Formula.nodes('/root/MasterFormula') AS x (r)
    WHERE AccountingSystem = @AccountingSystem
      AND ReportID = @ReportID
      AND FormulaType = 1
      AND Formula IS NOT NULL;
    WITH V (ItemID, DetailItemID, DebitAmount, PrevDebitAmount, OperationSign)
             AS (SELECT ItemID,
                        DetailItemID,
                        DebitAmount,
                        PrevDebitAmount,
                        OperationSign
                 FROM @tblMasterDetail
                 WHERE Grade = -1
                 UNION ALL
                 SELECT B.ItemID,
                        B.DetailItemID,
                        V.DebitAmount,
                        V.PrevDebitAmount,
                        B.OperationSign * V.OperationSign AS OperationSign
                 FROM @tblMasterDetail B,
                      V
                 WHERE B.DetailItemID = V.ItemID
        )

    INSERT
    @Result
    SELECT FR.ItemID,
           FR.ItemCode,
           FR.ItemName,
           FR.ItemNameEnglish,
           FR.ItemIndex,
           FR.Description,
           FR.FormulaType,
           CASE
               WHEN FR.FormulaType = 1 THEN FR.FormulaFrontEnd
               ELSE ''
               END                 AS FormulaFrontEnd,
           CASE
               WHEN FR.FormulaType = 1 THEN FR.Formula
               ELSE NULL
               END                 AS Formula,
           FR.Hidden,
           FR.IsBold,
           FR.IsItalic,
           ISNULL(X.Amount, 0)     AS Amount,
           ISNULL(X.PrevAmount, 0) AS PrevAmount
    FROM (SELECT V.ItemID,
                 SUM(V.OperationSign * V.DebitAmount)     AS Amount,
                 SUM(V.OperationSign * V.PrevDebitAmount) AS PrevAmount
          FROM V
          GROUP BY ItemID
         ) AS X
             RIGHT JOIN dbo.FRTemplate FR ON FR.ItemID = X.ItemID
    WHERE AccountingSystem = @AccountingSystem
      AND ReportID = @ReportID
    ORDER BY ItemIndex

    SELECT *
    FROM @Result
    WHERE Hidden = 0
    order by ItemIndex
end
go

