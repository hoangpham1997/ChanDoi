-- =============================================
-- Author:		namnh
-- Create date: 27/05/2020
-- Description:	Tổng họp chi phí theo khoản mục chi phí
-- Proc_TONG_HOP_CHI_PHI_THEO_KMCP '09FE5FE9-9BCC-D242-A9F1-9F2EA9A78D00', '01/01/2020', '12/31/2020', 0,',632,635,641,6411,6412,6413,6414,6415,6417,6418,642,6421,6422,6423,6424,6425,6426,6427,6428,811,',',df6aef78-bc30-bb41-9395-8ee73c7fbe97,333ab257-ccfd-1a41-9f3c-672c55a9270f,',0
-- =============================================
    ALTER PROCEDURE [dbo].[Proc_TONG_HOP_CHI_PHI_THEO_KMCP](@CompanyID UNIQUEIDENTIFIER,
    @FromDate DATETIME,
    @ToDate DATETIME,
    @TypeLedger INT,
    @ListAccountNumber NVARCHAR(3000),
    @ListExpenseItem NVARCHAR(3000),
    @IsDependent BIT)
    -- TypeLedger = 0 - Sổ tài chính
    -- TypeLedger = 1 - Sổ quản trị
    AS
    BEGIN

        DECLARE @tblListCompanyID TABLE
                                  (
                                      CompanyID UNIQUEIDENTIFIER
                                  )
        IF (@isDependent = 1)
            BEGIN
                INSERT INTO @tblListCompanyID
                SELECT ID
                FROM EbOrganizationUnit
                WHERE (ID = @CompanyID)
                   OR (ParentID = @CompanyID AND UnitType = 1 AND AccType = 0)
            END
        ELSE
            BEGIN
                INSERT INTO @tblListCompanyID
                SELECT ID
                FROM EbOrganizationUnit
                WHERE ID = @CompanyID
            END

        DECLARE @tblListAccountNumber TABLE
                                      (
                                          AccountNumber NVARCHAR(25)
                                      )

        INSERT INTO @tblListAccountNumber
        SELECT TG.AccountNumber
        FROM AccountList AS TG
                 INNER JOIN Func_ConvertStringIntoTable_Nvarchar(@ListAccountNumber, ',') AS Account
                            ON TG.AccountNumber = Account.Value
        WHERE Account.Value IS NOT NULL
          AND CompanyID = (SELECT
                           TOP (1)
                           (CASE
                                WHEN ParentID IS NOT NULL THEN ParentID
                                ELSE ID END)
                           FROM EbOrganizationUnit
                           WHERE ID = @CompanyID)

        DECLARE @tblListExpenseItem TABLE
                                    (
                                        ID UNIQUEIDENTIFIER
                                    )

        INSERT INTO @tblListExpenseItem
        SELECT TG.ID
        FROM ExpenseItem AS TG
                 INNER JOIN Func_ConvertStringIntoTable_Nvarchar(@ListExpenseItem, ',') AS ExpenseItem
                            ON TG.ID = ExpenseItem.Value
        WHERE ExpenseItem.Value IS NOT NULL
          AND CompanyID = @CompanyID

        SELECT a.ExpenseItemID                 as expenseItemID,
               b.ExpenseItemCode               as expenseItemCode,
               b.ExpenseItemName               as expenseItemName,
               Account                         as account,
               SUM(DebitAmount - CreditAmount) as amount
        from GeneralLedger a
                 LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
        WHERE a.Account IN (SELECT AccountNumber FROM @tblListAccountNumber)
          AND a.ExpenseItemID IN (SELECT * from @tblListExpenseItem)
          AND a.PostedDate >= @FromDate
          AND a.PostedDate <= @ToDate
          AND (a.TypeLedger = 2 OR a.TypeLedger = @TypeLedger)
          AND a.CompanyID IN (SELECT CompanyID FROM @tblListCompanyID)
          AND a.ExpenseItemID IS NOT NULL
        group by a.ExpenseItemID, b.ExpenseItemCode, b.ExpenseItemName, a.Account
        ORDER BY a.Account, b.ExpenseItemCode
    END
go

