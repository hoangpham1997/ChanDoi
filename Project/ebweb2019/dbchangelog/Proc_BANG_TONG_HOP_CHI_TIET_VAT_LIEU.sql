/*
-- =============================================
-- Author:		<chuongnv>
-- Create date: <18-02-2020>
-- Description:	<Lấy dữ sổ chi tiết vật liệu>
-- Proc_THE_KHO
-- =============================================
*/
CREATE PROCEDURE [dbo].[Proc_BANG_TONG_HOP_CHI_TIET_VAT_LIEU] @CompanyID UNIQUEIDENTIFIER,
                                                              @FromDate DATETIME,
                                                              @ToDate DATETIME,
                                                              @IsWorkingWithManagementBook BIT,
                                                              @Account  NVARCHAR(MAX),
                                                              @IncludeDependentBranch BIT
AS
BEGIN
    -- SET NOCOUNT ON added to prevent extra result sets from
    -- interfering with SELECT statements.
    SET NOCOUNT ON;

        CREATE TABLE #tblAccountNumber
    (
        AccountNumber        NVARCHAR(20) COLLATE Latin1_General_CI_AS
    )

    INSERT #tblAccountNumber
    SELECT DISTINCT A.AccountNumber
    FROM dbo.Func_ConvertStringIntoTable_Nvarchar(@Account, ',') F
             INNER JOIN dbo.AccountList A ON A.AccountNumber LIKE (F.Value + '%')
    where A.CompanyID = @CompanyID

    CREATE TABLE #Result
    (
        MaterialGoodsID   UNIQUEIDENTIFIER,
        MaterialGoodsCode NVARCHAR(50) COLLATE Latin1_General_CI_AS,
        MaterialGoodsName NVARCHAR(255) COLLATE Latin1_General_CI_AS,
        Account           NVARCHAR(20) COLLATE Latin1_General_CI_AS,
        AmountOpening     MONEY,
        IWAmount          MONEY,
        OWAmount          MONEY,
        AmountClosing     MONEY,
    )

    INSERT INTO #Result
    SELECT RL.MaterialGoodsID,
           M.MaterialGoodsCode,
           M.MaterialGoodsName,
           RL.Account,
           SUM(CASE
                   WHEN RL.PostedDate < @FromDate THEN ISNULL(RL.IWAmount, 0) - ISNULL(RL.OWAmount, 0)
                   ELSE 0
               END),
           SUM(CASE
                   WHEN RL.PostedDate >= @FromDate THEN ISNULL(RL.IWAmount, 0)
                   ELSE 0
               END),
           SUM(CASE
                   WHEN RL.PostedDate >= @FromDate THEN ISNULL(RL.OWAmount, 0)
                   ELSE 0
               END),
           0 AS AmountClosing
    FROM dbo.RepositoryLedger RL
             INNER JOIN #tblAccountNumber A ON RL.Account = A.AccountNumber
             INNER JOIN dbo.MaterialGoods M ON M.ID = RL.MaterialGoodsID
    WHERE PostedDate <= @ToDate
      AND RL.CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch))
      AND (RL.TypeLedger = @IsWorkingWithManagementBook OR RL.TypeLedger = 2)
    GROUP BY RL.MaterialGoodsID, M.MaterialGoodsCode, M.MaterialGoodsName, RL.Account


-- Tính số tồn

    UPDATE #Result
    SET AmountClosing = (AmountOpening + IWAmount - OWAmount) WHERE 1 = 1

    SELECT * FROM #Result R
    WHERE OWAmount <> 0 OR IWAmount <> 0 OR AmountOpening <> 0 OR AmountClosing <> 0
    ORDER BY R.Account, R.MaterialGoodsCode
END
go

