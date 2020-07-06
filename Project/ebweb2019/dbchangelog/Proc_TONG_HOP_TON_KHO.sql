/*
-- =============================================
-- Author:		<chuongnv>
-- Create date: <18-02-2020>
-- Description:	<Lấy dữ sổ chi tiết vật liệu>
-- Proc_TONG_HOP_TON_KHO
-- =============================================
*/
CREATE PROCEDURE [dbo].[Proc_TONG_HOP_TON_KHO] @FromDate DATETIME,
                                               @Todate DATETIME,
                                               @StockID NVARCHAR(MAX),
                                               @CompanyID UNIQUEIDENTIFIER,
                                               @UnitType INT,
                                               @IsWorkingWithManagementBook BIT,
                                               @MaterialGoodsCategoryID UNIQUEIDENTIFIER,
                                               @IncludeDependentBranch BIT
AS
BEGIN
    SET NOCOUNT ON
    DECLARE
        @Stock AS TABLE
                  (
                      RepositoryID   UNIQUEIDENTIFIER,
                      RepositoryCode NVARCHAR(20),
                      RepositoryName NVARCHAR(255)
                  )
    INSERT INTO @Stock
    SELECT ID,
           RepositoryCode,
           RepositoryName
    FROM dbo.Repository S
             INNER JOIN dbo.Func_ConvertStringIntoTable(@StockID, ',') F ON S.ID = F.Value

    CREATE TABLE #Result
    (
        RepositoryID      UNIQUEIDENTIFIER,
        RepositoryCode    NVARCHAR(20),
        RepositoryName    NVARCHAR(255),
        MaterialGoodsID   UNIQUEIDENTIFIER,
        MaterialGoodsCode NVARCHAR(25),
        MaterialGoodsName NVARCHAR(255),
        UnitName          NVARCHAR(25),
        OpeningQuantity   DECIMAL(22, 8),
        OpeningAmount     MONEY,
        IWQuantity        DECIMAL(22, 8),
        IWAmount          MONEY,
        OWQuantity        DECIMAL(22, 8),
        OWAmount          MONEY,
        ClosingQuantity   DECIMAL(22, 8),
        ClosingAmount     MONEY
    )

    IF @UnitType = 0
        BEGIN
            INSERT INTO #Result
            SELECT IL.RepositoryID,
                   S.RepositoryCode,
                   S.RepositoryName,
                   IL.MaterialGoodsID,
                   I.MaterialGoodsCode,
                   I.MaterialGoodsName,
                   UI.UnitName,
                   SUM(CASE
                           WHEN IL.PostedDate >= @FromDate THEN 0
                           WHEN IL.UnitID = IL.MainUnitID THEN ISNULL(IL.IWQuantity, 0) - ISNULL(IL.OWQuantity, 0)
                           ELSE (ISNULL(IL.MainIWQuantity, 0) - ISNULL(IL.MainOWQuantity, 0))
                       END) AS OpeningQuantity,
                   SUM(CASE
                           WHEN IL.PostedDate >= @FromDate THEN 0
                           ELSE ISNULL(IL.IWAmount, 0) - ISNULL(IL.OWAmount, 0)
                       END) AS OpeningAmount,
                   SUM(CASE
                           WHEN IL.PostedDate < @FromDate THEN 0
                           WHEN IL.UnitID = IL.MainUnitID THEN ISNULL(IL.IWQuantity, 0)
                           ELSE ISNULL(IL.MainIWQuantity, 0)
                       END),
                   SUM(CASE
                           WHEN IL.PostedDate < @FromDate THEN 0
                           ELSE ISNULL(IL.IWAmount, 0)
                       END),
                   SUM(CASE
                           WHEN IL.PostedDate < @FromDate THEN 0
                           WHEN IL.UnitID = IL.MainUnitID THEN ISNULL(IL.OWQuantity, 0)
                           ELSE ISNULL(IL.MainOWQuantity, 0)
                       END),
                   SUM(CASE
                           WHEN IL.PostedDate < @FromDate THEN 0
                           ELSE ISNULL(IL.OWAmount, 0)
                       END),
                   0        AS ClosingQuantity,
                   0        AS ClosingAmount
            FROM dbo.RepositoryLedger IL
                     INNER JOIN @Stock S ON IL.RepositoryID = S.RepositoryID
                     INNER JOIN dbo.MaterialGoods I ON IL.MaterialGoodsID = I.ID
                     LEFT JOIN dbo.Unit UI ON I.UnitID = UI.ID
            WHERE IL.PostedDate <= @Todate
              AND IL.CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch))
              AND (IL.MainUnitID = I.UnitID OR IL.MainUnitID IS NULL)
              AND (IL.TypeLedger = @IsWorkingWithManagementBook OR IL.TypeLedger = 2)
              AND I.MaterialGoodsType NOT IN (2, 4)
            GROUP BY IL.RepositoryID,
                     S.RepositoryCode,
                     S.RepositoryName,
                     IL.MaterialGoodsID,
                     I.MaterialGoodsCode,
                     I.MaterialGoodsName,
                     UI.UnitName
        END
    ELSE
        BEGIN
            INSERT INTO #Result
            SELECT IL.RepositoryID,
                   S.RepositoryCode,
                   S.RepositoryName,
                   IL.MaterialGoodsID,
                   I.MaterialGoodsCode,
                   I.MaterialGoodsName,
                   UI.UnitName,
                   SUM(CASE
                           WHEN IL.PostedDate >= @FromDate THEN 0
                           WHEN IL.UnitID = MC.UnitID THEN ISNULL(IL.IWQuantity, 0) - ISNULL(IL.OWQuantity, 0)
                           ELSE (CASE WHEN MC.Formula = '*' THEN (ISNULL(IL.MainIWQuantity, 0) - ISNULL(IL.MainOWQuantity, 0)) /
                                ISNULL(MC.ConvertRate, 1)
                               ELSE (ISNULL(IL.MainIWQuantity, 0) - ISNULL(IL.MainOWQuantity, 0)) *
                                ISNULL(MC.ConvertRate, 1) END)
                       END) AS OpeningQuantity,
                   SUM(CASE
                           WHEN IL.PostedDate >= @FromDate THEN 0
                           ELSE ISNULL(IL.IWAmount, 0) - ISNULL(IL.OWAmount, 0)
                       END) AS OpeningAmount,
                   SUM(CASE
                           WHEN IL.PostedDate < @FromDate THEN 0
                           WHEN IL.UnitID = MC.UnitID THEN ISNULL(IL.IWQuantity, 0)
                           ELSE (CASE WHEN MC.Formula = '*' THEN ISNULL(IL.MainIWQuantity, 0) / ISNULL(MC.ConvertRate, 1)
                               ELSE ISNULL(IL.MainIWQuantity, 0) * ISNULL(MC.ConvertRate, 1) END)
                       END),
                   SUM(CASE
                           WHEN IL.PostedDate < @FromDate THEN 0
                           ELSE ISNULL(IL.IWAmount, 0)
                       END),
                   SUM(CASE
                           WHEN IL.PostedDate < @FromDate THEN 0
                           WHEN IL.UnitID = MC.UnitID THEN ISNULL(IL.OWQuantity, 0)
                           ELSE (CASE WHEN MC.Formula = '*' THEN ISNULL(IL.MainOWQuantity, 0) / ISNULL(MC.ConvertRate, 1)
                               ELSE ISNULL(IL.MainOWQuantity, 0) * ISNULL(MC.ConvertRate, 1) END)
                       END),
                   SUM(CASE
                           WHEN IL.PostedDate < @FromDate THEN 0
                           ELSE ISNULL(IL.OWAmount, 0)
                       END),
                   0        AS ClosingQuantity,
                   0        AS ClosingAmount
            FROM dbo.RepositoryLedger IL
                     INNER JOIN @Stock S ON IL.RepositoryID = S.RepositoryID
                     INNER JOIN dbo.MaterialGoods I ON IL.MaterialGoodsID = I.ID
                     INNER JOIN dbo.MaterialGoodsConvertUnit MC
                                ON I.ID = MC.MaterialGoodsID AND MC.OrderNumber = @UnitType
                     LEFT JOIN dbo.Unit UI ON MC.UnitID = UI.ID
            WHERE IL.PostedDate <= @Todate
              AND IL.CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch))
              AND (IL.TypeLedger = @IsWorkingWithManagementBook OR IL.TypeLedger = 2)
              AND I.MaterialGoodsType NOT IN (2, 4)
            GROUP BY IL.RepositoryID,
                     S.RepositoryCode,
                     S.RepositoryName,
                     IL.MaterialGoodsID,
                     I.MaterialGoodsCode,
                     I.MaterialGoodsName,
                     UI.UnitName
        END

    UPDATE #Result
    SET ClosingAmount   = (OpeningAmount + IWAmount - OWAmount),
        ClosingQuantity = (OpeningQuantity + IWQuantity - OWQuantity)
    WHERE 1 = 1
    SELECT *
    FROM #Result
    WHERE OpeningQuantity <> 0
       OR OpeningAmount <> 0
       OR IWQuantity <> 0
       OR OWQuantity <> 0
       OR IWAmount <> 0
       OR OWAmount <> 0
       OR ClosingQuantity <> 0
       OR ClosingAmount <> 0
    ORDER BY RepositoryCode, MaterialGoodsCode
END
go

