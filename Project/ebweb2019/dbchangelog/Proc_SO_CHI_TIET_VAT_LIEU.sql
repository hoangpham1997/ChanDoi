/*
-- =============================================
-- Author:		<chuongnv>
-- Create date: <18-02-2020>
-- Description:	<Lấy dữ sổ chi tiết vật liệu>
-- Proc_SO_CHI_TIET_VAT_LIEU
-- =============================================
*/
CREATE PROCEDURE [dbo].[Proc_SO_CHI_TIET_VAT_LIEU] @CompanyID UNIQUEIDENTIFIER,
                                                   @IncludeDependentBranch BIT,
                                                   @FromDate DATETIME,
                                                   @ToDate DATETIME,
                                                   @UnitType INT,
                                                   @RepositoryID UNIQUEIDENTIFIER,
                                                   @ListMaterialGoodsID NVARCHAR(MAX),
                                                   @IsWorkingWithManagementBook BIT
AS
BEGIN
    -- SET NOCOUNT ON added to prevent extra result sets from
    -- interfering with SELECT statements.
    SET NOCOUNT ON;
    DECLARE
        @OPNDescription AS NVARCHAR(255);
    SET @OPNDescription = N'Số dư đầu kỳ'

    DECLARE
        @MainCurrencyID AS NVARCHAR(3)
    SET @MainCurrencyID = ISNULL((SELECT TOP 1 CurrencyID FROM EbOrganizationUnit WHERE ID = @CompanyID), 'VND')

    DECLARE
        @MaterialGoods AS TABLE
                          (
                              MaterialGoodsID   UNIQUEIDENTIFIER,
                              MaterialGoodsCode NVARCHAR(50),
                              MaterialGoodsName NVARCHAR(255),
                              UnitID            UNIQUEIDENTIFIER,
                              UnitName          NVARCHAR(20),
                              ConvertRate       DECIMAL(22, 16)
                          )
    INSERT INTO @MaterialGoods
    SELECT M.ID,
           M.MaterialGoodsCode,
           M.MaterialGoodsName,
           U.ID,
           U.UnitName,
           CASE
               WHEN @UnitType = 0
                   OR @UnitType IS NULL THEN 1
               WHEN IU.ConvertRate = 0 THEN 1
               WHEN IU.Formula = '*' THEN 1 / ISNULL(IU.ConvertRate, 1)
               ELSE
                   ISNULL(IU.ConvertRate, 1)
               END AS ConvertRate
    FROM dbo.MaterialGoods M
             INNER JOIN dbo.Func_ConvertStringIntoTable(@ListMaterialGoodsID, ',') F ON M.ID = F.Value
             LEFT JOIN dbo.MaterialGoodsConvertUnit IU ON M.ID = IU.MaterialGoodsID AND IU.OrderNumber = @UnitType
             LEFT JOIN dbo.Unit U ON ((@UnitType IS NULL OR @UnitType = 0) AND M.UnitID = U.ID)
        OR (@UnitType IS NOT NULL AND @UnitType <> 0 AND IU.UnitID = U.ID)
    WHERE U.ID IS NOT NULL
    --                         OR @UnitType = 0
--                         OR IU.OrderNumber IS NOT NULL


    CREATE TABLE #Result
    (
        RowNum                     INT IDENTITY (1, 2)
            PRIMARY KEY,
        RefID                      UNIQUEIDENTIFIER,
        RefType                    INT,
        RepositoryID               UNIQUEIDENTIFIER,
        RepositoryCode             NVARCHAR(20) COLLATE Latin1_General_CI_AS,
        RepositoryName             NVARCHAR(128) COLLATE Latin1_General_CI_AS,
        MaterialGoodsID            UNIQUEIDENTIFIER,
        MaterialGoodsCode          NVARCHAR(50) COLLATE Latin1_General_CI_AS,
        MaterialGoodsName          NVARCHAR(255) COLLATE Latin1_General_CI_AS,
        PostedDate                 DATETIME,
        RefDate                    DATETIME,
        RefNo                      NVARCHAR(20) COLLATE Latin1_General_CI_AS,
        Reason                     NVARCHAR(255) COLLATE Latin1_General_CI_AS,
        CorrespondingAccountNumber NVARCHAR(20) COLLATE Latin1_General_CI_AS,
        UnitName                   NVARCHAR(20) COLLATE Latin1_General_CI_AS,
        CurrencyID                 NVARCHAR(3) COLLATE Latin1_General_CI_AS,
        UnitPrice                  DECIMAL(22, 8),
        InwardQuantity             DECIMAL(22, 8),
        InwardAmount               DECIMAL(25, 8),
        OutwardQuantity            DECIMAL(22, 8),
        OutwardAmount              DECIMAL(25, 8),
        ClosingQuantity            DECIMAL(22, 8),
        ClosingAmount              DECIMAL(25, 8),
        INRefOrder                 DATETIME,
        IsBold                     BIT,
        SortOrder                  INT,
        RownumTemp                 INT
    )


    IF @RepositoryID = '00000000-0000-0000-0000-000000000000'
        BEGIN
            INSERT INTO #Result
            SELECT           CASE
                         WHEN IL.PostedDate < @FromDate THEN NULL
                         ELSE IL.DetailID
                         END,
                     CASE
                         WHEN IL.PostedDate < @FromDate THEN 0
                         ELSE IL.TypeID
                         END,
                     IL.RepositoryID,
                     S.RepositoryCode,
                     S.RepositoryName,
                     I.MaterialGoodsID,
                     I.MaterialGoodsCode,
                     I.MaterialGoodsName,
                     CASE
                         WHEN IL.PostedDate < @FromDate THEN NULL
                         ELSE IL.PostedDate
                         END               AS PostedDate,
                     CASE
                         WHEN IL.PostedDate < @FromDate THEN NULL
                         ELSE IL.Date
                         END               AS RefDate,
                     CASE
                         WHEN IL.PostedDate < @FromDate THEN NULL
                         ELSE CASE
                                  WHEN @IsWorkingWithManagementBook = 0 THEN IL.NoFBook
                                  ELSE IL.NoMBook
                             END
                         END               AS RefNo,
                     CASE
                         WHEN IL.PostedDate < @FromDate THEN @OPNDescription
                         ELSE IL.Reason
                         END,
                     CASE
                         WHEN IL.PostedDate < @FromDate THEN NULL
                         ELSE IL.AccountCorresponding
                         END,
                     I.UnitName,
                     CASE
                         WHEN IL.PostedDate < @FromDate THEN @MainCurrencyID
                         ELSE ISNULL(CurrencyID, @MainCurrencyID)
                         END               AS CurrencyID,
                     CASE
                         WHEN IL.PostedDate < @FromDate THEN 0
                         WHEN I.UnitID = IL.UnitID THEN IL.UnitPrice
                         ELSE IL.MainUnitPrice / I.ConvertRate
                         END               AS UnitPrice,
                     SUM(CASE
                             WHEN IL.PostedDate < @FromDate THEN 0
                             WHEN IL.UnitID = I.UnitID THEN ISNULL(IL.IWQuantity, 0)
                             ELSE ISNULL(IL.MainIWQuantity, 0) * I.ConvertRate
                         END),
                     SUM(CASE
                             WHEN IL.PostedDate < @FromDate THEN 0
                             ELSE ISNULL(IL.IWAmount, 0)
                         END),
                     SUM(CASE
                             WHEN IL.PostedDate < @FromDate THEN 0
                             WHEN IL.UnitID = I.UnitID THEN ISNULL(IL.OWQuantity, 0)
                             ELSE ISNULL(IL.MainOWQuantity, 0) * I.ConvertRate
                         END),
                     SUM(CASE
                             WHEN IL.PostedDate < @FromDate THEN 0
                             ELSE ISNULL(IL.OWAmount, 0)
                         END),
                     SUM(CASE
                             WHEN IL.PostedDate >= @FromDate THEN 0
                             WHEN IL.UnitID = I.UnitID THEN (ISNULL(IL.IWQuantity, 0) - ISNULL(IL.OWQuantity, 0))
                             ELSE ((ISNULL(IL.MainIWQuantity, 0) - ISNULL(IL.MainOWQuantity, 0))) * I.ConvertRate
                         END),
                     SUM(CASE
                             WHEN IL.PostedDate >= @FromDate THEN 0
                             ELSE ISNULL(IL.IWAmount, 0) - ISNULL(IL.OWAmount, 0)
                         END),
                     MAX(IL.Date)          AS INRefOrder,
                     CASE
                         WHEN IL.PostedDate < @FromDate THEN 1
                         ELSE 0
                         END,
                     MAX(IL.OrderPriority) AS SortOrder,
        RownumTemp = 0 --Dùng để sắp xếp các dòng số dư đầu kỳ =0
    FROM dbo.RepositoryLedger IL
             INNER JOIN dbo.Repository S ON IL.RepositoryID = S.ID
             INNER JOIN @MaterialGoods I ON IL.MaterialGoodsID = I.MaterialGoodsID
             INNER JOIN dbo.EbOrganizationUnit OU ON OU.ID = IL.CompanyID
    WHERE PostedDate <= @ToDate
      AND OU.ID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch))
      AND (IL.TypeLedger = @IsWorkingWithManagementBook OR IL.TypeLedger = 2)
    GROUP BY CASE
                 WHEN IL.PostedDate < @FromDate THEN NULL
                 ELSE IL.DetailID
                 END,
             CASE
                 WHEN IL.PostedDate < @FromDate THEN 0
                 ELSE IL.TypeID
                 END,
             IL.RepositoryID,
             S.RepositoryCode,
             S.RepositoryName,
             I.MaterialGoodsID,
             I.MaterialGoodsCode,
             I.MaterialGoodsName,
             CASE
                 WHEN IL.PostedDate < @FromDate THEN NULL
                 ELSE IL.PostedDate
                 END,
             CASE
                 WHEN IL.PostedDate < @FromDate THEN NULL
                 ELSE IL.Date
                 END,
             CASE
                 WHEN IL.PostedDate < @FromDate THEN NULL
                 ELSE CASE
                          WHEN @IsWorkingWithManagementBook = 0 THEN IL.NoFBook
                          ELSE IL.NoMBook
                     END
                 END,
             CASE
                 WHEN IL.PostedDate < @FromDate THEN @OPNDescription
                 ELSE IL.Reason
                 END,
             CASE
                 WHEN IL.PostedDate < @FromDate THEN NULL
                 ELSE IL.AccountCorresponding
                 END,
             I.UnitName,
             CASE
                 WHEN IL.PostedDate < @FromDate THEN @MainCurrencyID
                 ELSE ISNULL(CurrencyID, @MainCurrencyID)
                 END,
             CASE
                 WHEN IL.PostedDate < @FromDate THEN 0
                 WHEN I.UnitID = IL.UnitID THEN IL.UnitPrice
                 ELSE IL.MainUnitPrice / I.ConvertRate
                 END,
             CASE
                 WHEN IL.PostedDate < @FromDate THEN 1
                 ELSE 0
                 END
    ORDER BY S.RepositoryCode,
             I.MaterialGoodsCode,
             RefDate,
             INRefOrder,
             RefNO,
             SortOrder
        END
    ELSE
        BEGIN
            INSERT INTO #Result
            SELECT           CASE
                         WHEN IL.PostedDate < @FromDate THEN NULL
                         ELSE IL.DetailID
                         END,
                     CASE
                         WHEN IL.PostedDate < @FromDate THEN 0
                         ELSE IL.TypeID
                         END,
                     IL.RepositoryID,
                     S.RepositoryCode,
                     S.RepositoryName,
                     I.MaterialGoodsID,
                     I.MaterialGoodsCode,
                     I.MaterialGoodsName,
                     CASE
                         WHEN IL.PostedDate < @FromDate THEN NULL
                         ELSE IL.PostedDate
                         END               AS PostedDate,
                     CASE
                         WHEN IL.PostedDate < @FromDate THEN NULL
                         ELSE IL.Date
                         END               AS RefDate,
                     CASE
                         WHEN IL.PostedDate < @FromDate THEN NULL
                         ELSE CASE
                                  WHEN @IsWorkingWithManagementBook = 0 THEN IL.NoFBook
                                  ELSE IL.NoMBook
                             END
                         END               AS RefNo,
                     CASE
                         WHEN IL.PostedDate < @FromDate THEN @OPNDescription
                         ELSE IL.Reason
                         END,
                     CASE
                         WHEN IL.PostedDate < @FromDate THEN NULL
                         ELSE IL.AccountCorresponding
                         END,
                     I.UnitName,
                     CASE
                         WHEN IL.PostedDate < @FromDate THEN @MainCurrencyID
                         ELSE ISNULL(CurrencyID, @MainCurrencyID)
                         END               AS CurrencyID,
                     CASE
                         WHEN IL.PostedDate < @FromDate THEN 0
                         WHEN I.UnitID = IL.UnitID THEN IL.UnitPrice
                         ELSE IL.MainUnitPrice / I.ConvertRate
                         END               AS UnitPrice,
                     SUM(CASE
                             WHEN IL.PostedDate < @FromDate THEN 0
                             WHEN IL.UnitID = I.UnitID THEN ISNULL(IL.IWQuantity, 0)
                             ELSE ISNULL(IL.MainIWQuantity, 0) * I.ConvertRate
                         END),
                     SUM(CASE
                             WHEN IL.PostedDate < @FromDate THEN 0
                             ELSE ISNULL(IL.IWAmount, 0)
                         END),
                     SUM(CASE
                             WHEN IL.PostedDate < @FromDate THEN 0
                             WHEN IL.UnitID = I.UnitID THEN ISNULL(IL.OWQuantity, 0)
                             ELSE ISNULL(IL.MainOWQuantity, 0) * I.ConvertRate
                         END),
                     SUM(CASE
                             WHEN IL.PostedDate < @FromDate THEN 0
                             ELSE ISNULL(IL.OWAmount, 0)
                         END),
                     SUM(CASE
                             WHEN IL.PostedDate >= @FromDate THEN 0
                             WHEN IL.UnitID = I.UnitID THEN (ISNULL(IL.IWQuantity, 0) - ISNULL(IL.OWQuantity, 0))
                             ELSE ((ISNULL(IL.MainIWQuantity, 0) - ISNULL(IL.MainOWQuantity, 0))) * I.ConvertRate
                         END),
                     SUM(CASE
                             WHEN IL.PostedDate >= @FromDate THEN 0
                             ELSE ISNULL(IL.IWAmount, 0) - ISNULL(IL.OWAmount, 0)
                         END),
                     MAX(IL.Date)          AS INRefOrder,
                     CASE
                         WHEN IL.PostedDate < @FromDate THEN 1
                         ELSE 0
                         END,
                     MAX(IL.OrderPriority) AS SortOrder,
        RownumTemp = 0 --Dùng để sắp xếp các dòng số dư đầu kỳ =0
    FROM dbo.RepositoryLedger IL
             INNER JOIN dbo.Repository S ON IL.RepositoryID = S.ID
             INNER JOIN @MaterialGoods I ON IL.MaterialGoodsID = I.MaterialGoodsID
             INNER JOIN dbo.EbOrganizationUnit OU ON OU.ID = IL.CompanyID
    WHERE PostedDate <= @ToDate
      AND  IL.RepositoryID = @RepositoryID
      AND OU.ID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch))
      AND (IL.TypeLedger = @IsWorkingWithManagementBook OR IL.TypeLedger = 2)
    GROUP BY CASE
                 WHEN IL.PostedDate < @FromDate THEN NULL
                 ELSE IL.DetailID
                 END,
             CASE
                 WHEN IL.PostedDate < @FromDate THEN 0
                 ELSE IL.TypeID
                 END,
             IL.RepositoryID,
             S.RepositoryCode,
             S.RepositoryName,
             I.MaterialGoodsID,
             I.MaterialGoodsCode,
             I.MaterialGoodsName,
             CASE
                 WHEN IL.PostedDate < @FromDate THEN NULL
                 ELSE IL.PostedDate
                 END,
             CASE
                 WHEN IL.PostedDate < @FromDate THEN NULL
                 ELSE IL.Date
                 END,
             CASE
                 WHEN IL.PostedDate < @FromDate THEN NULL
                 ELSE CASE
                          WHEN @IsWorkingWithManagementBook = 0 THEN IL.NoFBook
                          ELSE IL.NoMBook
                     END
                 END,
             CASE
                 WHEN IL.PostedDate < @FromDate THEN @OPNDescription
                 ELSE IL.Reason
                 END,
             CASE
                 WHEN IL.PostedDate < @FromDate THEN NULL
                 ELSE IL.AccountCorresponding
                 END,
             I.UnitName,
             CASE
                 WHEN IL.PostedDate < @FromDate THEN @MainCurrencyID
                 ELSE ISNULL(CurrencyID, @MainCurrencyID)
                 END,
             CASE
                 WHEN IL.PostedDate < @FromDate THEN 0
                 WHEN I.UnitID = IL.UnitID THEN IL.UnitPrice
                 ELSE IL.MainUnitPrice / I.ConvertRate
                 END,
             CASE
                 WHEN IL.PostedDate < @FromDate THEN 1
                 ELSE 0
                 END
    ORDER BY S.RepositoryCode,
             I.MaterialGoodsCode,
             RefDate,
             INRefOrder,
             RefNO,
             SortOrder
        END

-- Tính số tồn
    DECLARE @ClosingQuantity AS DECIMAL(25, 8) ,
        @ClosingAmount AS DECIMAL(25, 8) ,
        @RowStockID UNIQUEIDENTIFIER ,
        @RowMaterialGoodsID UNIQUEIDENTIFIER
    SELECT @ClosingAmount = 0,
           @ClosingQuantity = 0,
           @RowStockID = NEWID(),
           @RowMaterialGoodsID = NEWID()

    UPDATE #Result
    SET @ClosingAmount      = (CASE
                                   WHEN RefID IS NULL THEN ClosingAmount
                                   WHEN @RowStockID <> RepositoryID
                                       OR @RowMaterialGoodsID <> MaterialGoodsID
                                       THEN ISNULL(InwardAmount, 0) - ISNULL(OutwardAmount, 0)
                                   ELSE @ClosingAmount + ISNULL(InwardAmount, 0) - ISNULL(OutwardAmount, 0)
        END),
        @ClosingQuantity    = (CASE
                                   WHEN RefID IS NULL THEN ClosingQuantity
                                   WHEN @RowStockID <> RepositoryID
                                       OR @RowMaterialGoodsID <> MaterialGoodsID
                                       THEN ISNULL(InwardQuantity, 0) - ISNULL(OutwardQuantity, 0)
                                   ELSE @ClosingQuantity + ISNULL(InwardQuantity, 0) - ISNULL(OutwardQuantity, 0)
            END),
        ClosingAmount       = @ClosingAmount,
        ClosingQuantity     = @ClosingQuantity,
        @RowStockID         = RepositoryID,
        @RowMaterialGoodsID = MaterialGoodsID;

    DECLARE @MaterialGoodsResult AS TABLE
                                    (
                                        RepositoryID      UNIQUEIDENTIFIER,
                                        RepositoryCode    NVARCHAR(20) COLLATE Latin1_General_CI_AS,
                                        RepositoryName    NVARCHAR(128) COLLATE Latin1_General_CI_AS,
                                        MaterialGoodsID   UNIQUEIDENTIFIER,
                                        MaterialGoodsCode NVARCHAR(50) COLLATE Latin1_General_CI_AS,
                                        MaterialGoodsName NVARCHAR(255) COLLATE Latin1_General_CI_AS,
                                        RowNum            INT
                                    )

    INSERT INTO @MaterialGoodsResult
    SELECT R.RepositoryID,
           RepositoryCode,
           RepositoryName,
           R.MaterialGoodsID,
           MaterialGoodsCode,
           MaterialGoodsName,
           MIN(RowNum) - 1
    FROM #Result R
    GROUP BY R.RepositoryID,
             RepositoryCode,
             RepositoryName,
             R.MaterialGoodsID,
             MaterialGoodsCode,
             MaterialGoodsName

    --Lưu lại số thứ tự hiện thời

    UPDATE #Result
    SET RowNumTemp = RowNum;

    INSERT INTO #Result
    (RefID,
     RefType,
     RepositoryID,
     RepositoryCode,
     RepositoryName,
     MaterialGoodsID,
     MaterialGoodsCode,
     MaterialGoodsName,
     PostedDate,
     RefDate,
     RefNo,
     Reason,
     CorrespondingAccountNumber,
     UnitName,
     CurrencyID,
     UnitPrice,
     InwardQuantity,
     InwardAmount,
     OutwardQuantity,
     OutwardAmount,
     ClosingQuantity,
     ClosingAmount,
     INRefOrder,
     IsBold,
     SortOrder,
     RowNumTemp)
    SELECT RefID                      = NULL,
           RefType                    = 0,
           RepositoryID,
           RepositoryCode,
           RepositoryName,
           MaterialGoodsID,
           MaterialGoodsCode,
           MaterialGoodsName,
           PostedDate                 = NULL,
           RefDate                    = NULL,
           RefNo                      = NULL,
           Reason                     = @OPNDescription,
           CorrespondingAccountNumber = NULL,
           UnitName                   = NULL,
           CurrencyID                 = NULL,
           UnitPrice                  = CAST(0 AS DECIMAL(20, 6)),
           InwardQuantity             = CAST(0 AS DECIMAL(22, 8)),
           InwardAmount               = CAST(0 AS DECIMAL(18, 4)),
           OutwardQuantity            = CAST(0 AS DECIMAL(22, 8)),
           OutwardAmount              = CAST(0 AS DECIMAL(18, 4)),
           ClosingQuantity            = CAST(0 AS DECIMAL(22, 8)),
           ClosingAmount              = CAST(0 AS DECIMAL(18, 4)),
           INRefOrder                 = GETDATE(),
           IsBold                     = 0,
           SortOrder                  = 0,
           I.RowNum
    FROM @MaterialGoodsResult I
    WHERE NOT EXISTS(SELECT 1
                     FROM #Result R
                     WHERE R.RepositoryID = I.RepositoryID
                       AND R.MaterialGoodsID = I.MaterialGoodsID
                       AND R.RefID IS NULL);

    SELECT RefID,
           RefType,
           R.RepositoryID,
           RepositoryCode,
           RepositoryName,
           MaterialGoodsID,
           MaterialGoodsCode,
           MaterialGoodsName,
           PostedDate,
           RefDate,
           RefNo,
           Reason,
           CorrespondingAccountNumber,
           UnitName,
           ISNULL(CASE
                      WHEN RefID IS NULL
                          AND ClosingQuantity <> 0 THEN ClosingAmount / ClosingQuantity
                      WHEN RefID IS NOT NULL THEN CASE
                                                      WHEN Reftype IN (220)
                                                          AND CurrencyID <> @MainCurrencyID THEN CASE
                                                                                                     WHEN InwardQuantity > 0
                                                                                                         THEN InwardAmount / InwardQuantity
                                                                                                     WHEN OutwardQuantity > 0
                                                                                                         THEN OutwardAmount / OutwardQuantity
                                                                                                     ELSE 0
                                                          END
                                                      ELSE UNitPrice
                          END
                      END, 0)                            AS UnitPrice,
           ISNULL(InwardQuantity, 0)                     AS InwardQuantity,
           ISNULL(InwardAmount, 0)                       AS InwardAmount,
           ISNULL(OutwardQuantity, 0)                    AS OutwardQuantity,
           ISNULL(OutwardAmount, 0)                      AS OutwardAmount,
           ISNULL(ClosingQuantity, 0)                    AS ClosingQuantity,
           ISNULL(ClosingAmount, 0)                      AS ClosingAmount,
           ROW_NUMBER() OVER ( ORDER BY RowNumTemp ASC ) AS RowNum,--Làm lại rowNum cho người dùng trên báo cáo
           INRefOrder,
           SortOrder,
           (CASE
                WHEN RefID IS NULL
                    AND ClosingAmount = 0
                    AND ClosingQuantity = 0 THEN CAST(0 AS BIT)
                ELSE CAST(1 AS BIT)
               END)                                      AS IsShowOnReport --Dùng để đánh dấu xem có hiển thị lên báo cáo hay không? Sổ chi tiết vật liệu, công cụ, hàng hóa=> Lỗi đang lấy lên dòng số dư đầu kỳ trường hợp kỳ trước có phát sinh nhập xuất
    FROM #Result R
    WHERE (InwardQuantity <> 0
        OR InwardAmount <> 0
        OR OutwardQuantity <> 0
        OR OutwardAmount <> 0
        OR ClosingQuantity <> 0
        OR ClosingAmount <> 0
              )
    ORDER BY RowNumTemp;
END
go

