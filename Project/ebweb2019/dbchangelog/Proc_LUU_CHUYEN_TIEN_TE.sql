/*
-- =============================================
-- Author:		<chuongnv>
-- Create date: <18-02-2020>
-- Description:	<Lấy dữ liệu lưu chuyển tiền tệ>
-- =============================================
*/
CREATE PROCEDURE [dbo].[Proc_LUU_CHUYEN_TIEN_TE](@CompanyID UNIQUEIDENTIFIER,
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

    DECLARE @AccountingSystem INT
    SET @AccountingSystem = 15

    -- Tinh toan lay lai nam truoc
    set @PrevToDate = DATEADD(DD, -1, @FromDate)
    /*add by cuongpv*/
    set @PrevToDate = DATEADD(hh, 23, @PrevToDate)
    set @PrevToDate = DATEADD(mi, 59, @PrevToDate)
    set @PrevToDate = DATEADD(ss, 59, @PrevToDate)
    /*end add by cuongpv*/
    declare @CalPrevDate nvarchar(20)
    declare @CalPrevMonth nvarchar(20)
    declare @CalPrevDay nvarchar(20)
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
            if (@CalPrevMonth in (11, 88, 44, 66, 99, 1111) and @CalPrevDay in (131, 130))/*edit by cuongpv: @CalPrevMonth in (11,22,88,44,66,99,1111) -> @CalPrevMonth in (11,88,44,66,99,1111)*/
                set @PrevFromDate = DATEADD(DD, -31, @FromDate)
            else
                if (@CalPrevMonth in (22) and @CalPrevDay in (128, 129)) /*add by cuongpv*/
                    set @PrevFromDate = DATEADD(DD, -31, @FromDate) /*add by cuongpv*/
                else
                    if (@CalPrevMonth in (33) and @CalPrevDay in (131) and CAST(year(@FromDate) AS varchar) % 4 = 0)
                        set @PrevFromDate = DATEADD(DD, -29, @FromDate)
                    else
                        if (@CalPrevMonth in (33) and @CalPrevDay in (131) and
                            CAST(year(@FromDate) AS varchar) % 4 <> 0)
                            set @PrevFromDate = DATEADD(DD, -28, @FromDate)
                        else
                            if ((@CalPrevMonth in (13, 1012) and @CalPrevDay = '131') or
                                (@CalPrevMonth in (46, 79) and @CalPrevDay = '130'))
                                set @PrevFromDate = DATEADD(QQ, -1, @FromDate)
                            else
                                set @PrevFromDate = DATEADD(DD, -1 - DATEDIFF(day, @FromDate, @ToDate), @FromDate)

    DECLARE @ReportID NVARCHAR(100)
    SET @ReportID = '3'

    DECLARE @ItemBeginingCash UNIQUEIDENTIFIER

    SET @ItemBeginingCash = (Select ItemID
                             From FRTemplate
                             Where ItemCode = '60' AND ReportID = 3 AND AccountingSystem = @AccountingSystem)

    DECLARE @tblItem TABLE
                     (
                         ItemID                     UNIQUEIDENTIFIER,
                         ItemIndex                  INT,
                         ItemName                   NVARCHAR(512),
                         OperationSign              INT,
                         OperandString              NVARCHAR(512),
                         AccountNumber              NVARCHAR(25),
                         AccountNumberPercent       NVARCHAR(25),
                         CorrespondingAccountNumber VARCHAR(25)
                     )

    INSERT @tblItem
    SELECT ItemID
         , ItemIndex
         , ItemName
         , x.r.value('@OperationSign', 'INT')
         , x.r.value('@OperandString', 'nvarchar(512)')
         , x.r.value('@AccountNumber', 'nvarchar(25)')
         , x.r.value('@AccountNumber', 'nvarchar(25)') + '%'
         , CASE
               WHEN x.r.value('@CorrespondingAccountNumber', 'nvarchar(25)') <> ''
                   THEN x.r.value('@CorrespondingAccountNumber', 'nvarchar(25)') + '%'
               ELSE ''
        END
    FROM dbo.FRTemplate
             CROSS APPLY Formula.nodes('/root/DetailFormula') AS x(r)
    WHERE AccountingSystem = @AccountingSystem
      AND ReportID = @ReportID
      AND FormulaType = 0
      AND Formula IS NOT NULL

    --Bảng tạm lấy ra dữ liệu cho các tài khoản và hoạt động.
    DECLARE @Balance TABLE
                     (
                         AccountNumber              NVARCHAR(25),
                         CorrespondingAccountNumber NVARCHAR(25),
                         PrevBusinessAmount         DECIMAL(25, 4) -- HĐSXKD
                         ,
                         BusinessAmount             DECIMAL(25, 4),
                         PrevInvestmentAmount       DECIMAL(25, 4) -- HDĐT
                         ,
                         InvestmentAmount           DECIMAL(25, 4),
                         PrevFinancialAmount        DECIMAL(25, 4) -- HĐ TC
                         ,
                         FinancialAmount            DECIMAL(25, 4)
                     )

    INSERT INTO @Balance
    SELECT GL.Account
         , GL.AccountCorresponding
         , SUM(CASE
                   WHEN PostedDate BETWEEN @PrevFromDate AND @PrevToDate AND
                        (AC.ActivityID IS NULL OR AC.ActivityID = 0)
                       THEN DebitAmount
                   ELSE 0 END) AS PrevBusinessAmount
         , SUM(CASE
                   WHEN PostedDate BETWEEN @FromDate AND @ToDate AND (AC.ActivityID IS NULL OR AC.ActivityID = 0)
                       THEN DebitAmount
                   ELSE 0 END) AS BusinessAmount
         , SUM(CASE
                   WHEN PostedDate BETWEEN @PrevFromDate AND @PrevToDate AND AC.ActivityID = 1
                       THEN DebitAmount
                   ELSE 0 END) AS PrevInvestmentAmount
         , SUM(CASE
                   WHEN PostedDate BETWEEN @FromDate AND @ToDate AND AC.ActivityID = 1
                       THEN DebitAmount
                   ELSE 0 END) AS InvestmentAmount
         , SUM(CASE
                   WHEN PostedDate BETWEEN @PrevFromDate AND @PrevToDate AND AC.ActivityID = 2
                       THEN DebitAmount
                   ELSE 0 END) AS PrevFinancialAmount
         , SUM(CASE
                   WHEN PostedDate BETWEEN @FromDate AND @ToDate AND AC.ActivityID = 2
                       THEN DebitAmount
                   ELSE 0 END) AS FinancialAmount
    FROM dbo.GeneralLedger GL
             INNER JOIN dbo.AccountList A ON GL.Account = A.AccountNumber AND A.CompanyID = (SELECT TOP(1) (CASE WHEN ParentID IS NOT NULL THEN ParentID
        ELSE ID END) AS ID FROM EbOrganizationUnit WHERE ID = @CompanyID)
             LEFT JOIN [dbo].[FRB03ReportDetailActivity] AC ON GL.DetailID = Ac.RefDetailID
                AND GL.TypeLedger = AC.IsPostToManagementBook
        /*Khi JOIN thêm điều kiện sổ*/
    WHERE PostedDate BETWEEN @PrevFromDate AND @ToDate AND GL.CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch)) AND (GL.TypeLedger = @IsWorkingWithManagementBook OR GL.TypeLedger = 2)
      and (GL.NoFBook <> 'OPN' OR GL.NoMBook <> 'OPN') -- haipl add 02012019
    GROUP BY GL.Account
           , GL.AccountCorresponding
    HAVING SUM(CASE
                   WHEN PostedDate BETWEEN @PrevFromDate AND @PrevToDate AND
                        (AC.ActivityID IS NULL OR AC.ActivityID = 0)
                       THEN DebitAmount
                   ELSE 0 END) <> 0
        OR SUM(CASE
                   WHEN PostedDate BETWEEN @FromDate AND @ToDate AND (AC.ActivityID IS NULL OR AC.ActivityID = 0)
                       THEN DebitAmount
                   ELSE 0 END) <> 0
        OR SUM(CASE
                   WHEN PostedDate BETWEEN @PrevFromDate AND @PrevToDate AND AC.ActivityID = 1
                       THEN DebitAmount
                   ELSE 0 END) <> 0
        OR SUM(CASE
                   WHEN PostedDate BETWEEN @FromDate AND @ToDate AND AC.ActivityID = 1
                       THEN DebitAmount
                   ELSE 0 END) <> 0
        OR SUM(CASE
                   WHEN PostedDate BETWEEN @PrevFromDate AND @PrevToDate AND AC.ActivityID = 2
                       THEN DebitAmount
                   ELSE 0 END) <> 0
        OR SUM(CASE
                   WHEN PostedDate BETWEEN @FromDate AND @ToDate AND AC.ActivityID = 2
                       THEN DebitAmount
                   ELSE 0 END) <> 0
    -- Bang tam chua du lieu cho các chỉ tiêu có Dư nợ

    DECLARE @DebitBalance TABLE
                          (
                              ItemID          UNIQUEIDENTIFIER,
                              OperationSign   INT,
                              AccountNumber   NVARCHAR(25),
                              AccountKind     INT,
                              PrevDebitAmount Decimal(25, 4),
                              DebitAmount     Decimal(25, 4)
                          )

    INSERT @DebitBalance
    SELECT I.ItemID
         , I.OperationSign
         , I.AccountNumber
         , A.AccountGroupKind
         , SUM(CASE WHEN GL.PostedDate < @PrevFromDate THEN GL.DebitAmount - GL.CreditAmount ELSE 0 END)
         , SUM(GL.DebitAmount - GL.CreditAmount)
    FROM dbo.GeneralLedger AS GL /*edit by cuongpv dbo.GeneralLedger -> @tbDataGL*/
             INNER JOIN @tblItem I ON GL.Account LIKE I.AccountNumberPercent
             LEFT JOIN dbo.AccountList A ON A.AccountNumber = I.AccountNumber AND A.CompanyID = (SELECT TOP(1) (CASE WHEN ParentID IS NOT NULL THEN ParentID
        ELSE ID END) AS ID FROM EbOrganizationUnit WHERE ID = @CompanyID)
    WHERE GL.PostedDate < @FromDate AND GL.CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch)) AND (GL.TypeLedger = @IsWorkingWithManagementBook OR GL.TypeLedger = 2)
      AND I.OperandString = 'DUNO'
      AND I.ItemID = @ItemBeginingCash
    Group By I.ItemID
           , I.OperationSign
           , I.AccountNumber
           , A.AccountGroupKind

           -- Neu chi tieu khac chi tieu tien dau ky thi van lay binh thuong
    UNION ALL
    SELECT I.ItemID
         , I.OperationSign
         , I.AccountNumber
         , A.AccountGroupKind
         , SUM(CASE WHEN GL.PostedDate <= @PrevToDate THEN GL.DebitAmount - GL.CreditAmount ELSE 0 END)
         , SUM(GL.DebitAmount - GL.CreditAmount)
    FROM dbo.GeneralLedger AS GL /*edit by cuongpv dbo.GeneralLedger -> @tbDataGL*/
             INNER JOIN @tblItem I ON GL.Account LIKE I.AccountNumberPercent
             LEFT JOIN dbo.AccountList A ON A.AccountNumber = I.AccountNumber AND A.CompanyID = (SELECT TOP(1) (CASE WHEN ParentID IS NOT NULL THEN ParentID
        ELSE ID END) AS ID FROM EbOrganizationUnit WHERE ID = @CompanyID)
    WHERE GL.PostedDate <= @ToDate AND GL.CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch)) AND (GL.TypeLedger = @IsWorkingWithManagementBook OR GL.TypeLedger = 2)
      AND I.OperandString = 'DUNO'
      AND I.ItemID <> @ItemBeginingCash
    Group By I.ItemID
           , I.OperationSign
           , I.AccountNumber
           , A.AccountGroupKind


    UPDATE @DebitBalance
    SET PrevDebitAmount = 0
    WHERE AccountKind = 1
       OR (AccountKind NOT IN (0, 1) AND PrevDebitAmount < 0)

    UPDATE @DebitBalance
    SET DebitAmount = 0
    WHERE AccountKind = 1
       OR (AccountKind NOT IN (0, 1) AND DebitAmount < 0)

    -- Bảng tạm cho các chỉ tiêu
    DECLARE @tblMasterDetail Table
                             (
                                 ItemID        UNIQUEIDENTIFIER,
                                 DetailItemID  UNIQUEIDENTIFIER,
                                 OperationSign INT,
                                 Grade         INT,
                                 PrevAmount    DECIMAL(25, 4),
                                 Amount        DECIMAL(25, 4)
                             )

    -- Bang tam chua du lieu cho các chỉ tiêu có Dư nợ

    INSERT INTO @tblMasterDetail
    SELECT I.ItemID
         , NULL
         , 1
         , -1
         , SUM(I.PrevAmount)
         , SUM(I.Amount)
    FROM (SELECT I.ItemID
               , SUM(CASE
                         WHEN I.OperandString = 'PhatsinhDU'
                             THEN b.PrevBusinessAmount + b.PrevInvestmentAmount + b.PrevFinancialAmount
                         WHEN I.OperandString = 'PhatsinhDUChiTietTheoHD_DAUTU' THEN b.PrevInvestmentAmount
                         WHEN I.OperandString = 'PhatsinhDUChiTietTheoHD_TAICHINH' THEN b.PrevFinancialAmount
                         WHEN I.OperandString = 'PhatsinhDUChiTietTheoHD_SXKD' THEN b.PrevBusinessAmount
                         END * I.OperationSign) AS PrevAmount
               , SUM(CASE
                         WHEN I.OperandString = 'PhatsinhDU'
                             THEN (b.BusinessAmount + b.InvestmentAmount + b.FinancialAmount)
                         WHEN I.OperandString = 'PhatsinhDUChiTietTheoHD_DAUTU' THEN (b.InvestmentAmount)
                         WHEN I.OperandString = 'PhatsinhDUChiTietTheoHD_TAICHINH' THEN (b.FinancialAmount)
                         WHEN I.OperandString = 'PhatsinhDUChiTietTheoHD_SXKD' THEN (b.BusinessAmount)
                         END * I.OperationSign) AS Amount
          FROM @tblItem I
                   INNER JOIN @Balance B
                              ON (B.AccountNumber like I.AccountNumberPercent)
                                  AND (B.CorrespondingAccountNumber like I.CorrespondingAccountNumber)
          WHERE I.OperandString IN ('PhatsinhDU', 'PhatsinhDUChiTietTheoHD_DAUTU', 'PhatsinhDUChiTietTheoHD_TAICHINH',
                                    'PhatsinhDUChiTietTheoHD_SXKD')
          GROUP BY I.ItemID
          UNION ALL
          SELECT I.ItemID
               , SUM((b.PrevBusinessAmount + b.PrevInvestmentAmount + b.PrevFinancialAmount) *
                     I.OperationSign)                                                               AS PrevAmount
               , SUM((b.BusinessAmount + b.InvestmentAmount + b.FinancialAmount) * I.OperationSign) AS Amount
          FROM @tblItem I
                   INNER JOIN @Balance B
                              ON (B.AccountNumber like I.AccountNumberPercent)
          WHERE I.OperandString = 'PhatsinhNO'
          GROUP BY I.ItemID
          UNION ALL
          SELECT I.ItemID
               , SUM((b.PrevBusinessAmount + b.PrevInvestmentAmount + b.PrevFinancialAmount) *
                     I.OperationSign)                                                               AS PrevAmount
               , SUM((b.BusinessAmount + b.InvestmentAmount + b.FinancialAmount) * I.OperationSign) AS Amount
          FROM @tblItem I
                   INNER JOIN @Balance B
                              ON (B.CorrespondingAccountNumber like I.AccountNumberPercent)
          WHERE I.OperandString = 'PhatsinhCO'

          GROUP BY I.ItemID

          UNION ALL
          SELECT I.ItemID
               , I.PrevDebitAmount * I.OperationSign
               , I.DebitAmount * I.OperationSign
          FROM @DebitBalance I
         ) AS I
    Group By I.ITemID


    -- Update dữ liệu đầu năm của chỉ tiêu 60 (tiền đầu năm)


    -- Lấy các chỉ tiêu tổng hợp
    INSERT @tblMasterDetail
    SELECT ItemID
         , x.r.value('@ItemID', 'NVARCHAR(100)')
         , x.r.value('@OperationSign', 'INT')
         , 0
         , 0.0
         , 0.0
    FROM dbo.FRTemplate
             CROSS APPLY Formula.nodes('/root/MasterFormula') AS x(r)
    WHERE AccountingSystem = @AccountingSystem
      AND ReportID = @ReportID
      AND FormulaType = 1
      AND Formula IS NOT NULL

    -- Cộng chỉ tiêu con lên chỉ tiêu cha
    ;
    WITH V(ItemID, DetailItemID, PrevAmount, Amount, OperationSign)
             AS
             (
                 SELECT ItemID, DetailItemID, PrevAmount, Amount, OperationSign
                 FROM @tblMasterDetail
                 WHERE Grade = -1
                 UNION ALL
                 SELECT B.ItemID
                      , B.DetailItemID
                      , V.PrevAmount
                      , V.Amount
                      , B.OperationSign * V.OperationSign AS OperationSign
                 FROM @tblMasterDetail B,
                      V
                 WHERE B.DetailItemID = V.ItemID
             )

         -- Lấy dữ liệu in báo cáo
    INSERT
    @Result
    SELECT FR.ItemID
         , FR.ItemCode
         , FR.ItemName
         , FR.ItemNameEnglish
         , FR.ItemIndex
         , FR.Description
         , FR.FormulaType
         , CASE WHEN FR.FormulaType = 1 THEN FR.FormulaFrontEnd ELSE '' END AS FormulaFrontEnd
         , CASE WHEN FR.FormulaType = 1 THEN FR.Formula ELSE NULL END       AS Formula
         , FR.Hidden
         , FR.IsBold
         , FR.IsItalic
         , CASE
               WHEN FR.Formula IS NOT NULL THEN ISNULL(X.Amount, 0)
               ELSE X.Amount
        END                                                                 AS Amount
         , CASE
               WHEN FR.Formula IS NOT NULL THEN ISNULL(X.PrevAmount, 0)
               ELSE X.PrevAmount
        END                                                                 AS PrevAmount
    FROM (
             SELECT V.ItemID
                  , SUM(V.OperationSign * V.Amount)     AS Amount
                  , SUM(V.OperationSign * V.PrevAmount) AS PrevAmount
             FROM V
             GROUP BY ItemID
         ) AS X
             RIGHT JOIN dbo.FRTemplate FR ON FR.ItemID = X.ItemID
    WHERE AccountingSystem = @AccountingSystem
      AND ReportID = @ReportID
      and Hidden = 0 -- Haipl cmt 02012019
    Order by ItemIndex

    SELECT FR.ItemID,
           FR.ItemCode,
           FR.ItemName,
           ISNULL(FR.ItemNameEnglish, FR.ItemName) AS ItemNameEnglish,
           FR.ItemIndex,
           FR.Description,
           FR.FormulaType,
           FR.FormulaFrontEnd,
           FR.Formula,
           FR.Hidden,
           FR.IsBold,
           FR.IsItalic,
           ISNULL(FR.Amount, 0)                    as Amount,
           ISNULL(FR.PrevAmount, 0)                as PrevAmount
    FROM @Result FR
    WHERE FR.Hidden = 0
    ORDER BY ItemIndex
end
go

