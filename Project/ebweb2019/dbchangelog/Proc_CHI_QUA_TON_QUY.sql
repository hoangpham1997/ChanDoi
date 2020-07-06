/*
-- =============================================
-- Author:		<chuongnv>
-- Create date: <18-02-2020>
-- Description:	<Lấy dữ sổ chi tiết vật liệu>
-- Proc_THE_KHO
-- =============================================
*/
CREATE PROCEDURE [dbo].[Proc_CHI_QUA_TON_QUY] @CompanyID UNIQUEIDENTIFIER,
                                              @TypeLedger INT,
                                              @Date DATETIME
AS
BEGIN
    DECLARE @tblAccount AS TABLE
                           (
                               Account             NVARCHAR(50),
                               CompanyID           UNIQUEIDENTIFIER,
                               TypeLedger          INT,
                               BankAccountDetailID UNIQUEIDENTIFIER
                           )
    DECLARE @tblAccountList AS TABLE
                               (
                                   AccountNumber NVARCHAR(50),
                                   CompanyID     UNIQUEIDENTIFIER
                               )
    INSERT INTO @tblAccountList
    SELECT AccountNumber,
           @CompanyID CompanyID
    FROM AccountList
    WHERE (CompanyID = (SELECT TOP (1) (CASE
                                           WHEN ParentID IS NOT NULL THEN ParentID
                                           ELSE ID END)
                       FROM EbOrganizationUnit
                       WHERE ID = @CompanyID))
      AND (AccountNumber LIKE N'111%' OR AccountNumber LIKE N'112%')
    IF @TypeLedger = 2
        BEGIN
            INSERT INTO @tblAccount
            SELECT AccountNumber,
                   CompanyID,
                   0,
                   null
            FROM @tblAccountList
            WHERE CompanyID = @CompanyID
              AND AccountNumber LIKE N'111%'

            INSERT INTO @tblAccount
            SELECT A.AccountNumber,
                   A.CompanyID,
                   0,
                   B.ID as BankAccountDetailID
            FROM @tblAccountList A
                     RIGHT JOIN BankAccountDetail B ON A.CompanyID = b.CompanyID
            WHERE A.CompanyID = @CompanyID
              AND AccountNumber LIKE N'112%'


            INSERT INTO @tblAccount
            SELECT AccountNumber,
                   CompanyID,
                   1,
                   null
            FROM @tblAccountList
            WHERE CompanyID = @CompanyID
              AND AccountNumber LIKE N'111%'

            INSERT INTO @tblAccount
            SELECT A.AccountNumber,
                   A.CompanyID,
                   1,
                   B.ID as BankAccountDetailID
            FROM @tblAccountList A
                     RIGHT JOIN BankAccountDetail B ON A.CompanyID = b.CompanyID
            WHERE A.CompanyID = @CompanyID
              AND AccountNumber LIKE N'112%'
        end
    ELSE
        BEGIN
            INSERT INTO @tblAccount
            SELECT AccountNumber,
                   CompanyID,
                   @TypeLedger,
                   null
            FROM @tblAccountList
            WHERE CompanyID = @CompanyID
              AND AccountNumber LIKE N'111%'

            INSERT INTO @tblAccount
            SELECT A.AccountNumber,
                   A.CompanyID,
                   @TypeLedger,
                   B.ID as BankAccountDetailID
            FROM @tblAccountList A
                     RIGHT JOIN BankAccountDetail B ON A.CompanyID = b.CompanyID
            WHERE A.CompanyID = @CompanyID
              AND AccountNumber LIKE N'112%'
        end


    DECLARE @tblResult AS TABLE
                          (
                              Account              NVARCHAR(50) COLLATE Latin1_General_CI_AS,
                              CompanyID            UNIQUEIDENTIFIER,
                              TypeLedger           INT,
                              DebitAmount          decimal(25, 4),
                              DebitAmountOriginal  decimal(25, 4),
                              CreditAmount         decimal(25, 4),
                              CreditAmountOriginal decimal(25, 4),
                              BankAccountDetailID  UNIQUEIDENTIFIER
                          )
    INSERT INTO @tblResult
    SELECT A.Account,
           A.CompanyID,
           A.TypeLedger,
           SUM(CASE
                   WHEN (A.CompanyID = GL.CompanyID AND (GL.TypeLedger = A.TypeLedger OR GL.TypeLedger = 2) AND
                         A.Account LIKE '111%') THEN ISNULL(GL.DebitAmount, 0)
                   WHEN (A.CompanyID = GL.CompanyID AND (GL.TypeLedger = A.TypeLedger OR GL.TypeLedger = 2) AND
                         GL.BankAccountDetailID = A.BankAccountDetailID) THEN ISNULL(GL.DebitAmount, 0)
                   ELSE 0 END)   AS DebitAmount,
           SUM(CASE
                   WHEN (A.CompanyID = GL.CompanyID AND (GL.TypeLedger = A.TypeLedger OR GL.TypeLedger = 2) AND
                         A.Account LIKE '111%') THEN ISNULL(GL.DebitAmountOriginal, 0)
                   WHEN (A.CompanyID = GL.CompanyID AND (GL.TypeLedger = A.TypeLedger OR GL.TypeLedger = 2) AND
                         GL.BankAccountDetailID = A.BankAccountDetailID) THEN ISNULL(GL.DebitAmountOriginal, 0)
                   ELSE 0 END)   AS DebitAmountOriginal,
           SUM(CASE
                   WHEN (A.CompanyID = GL.CompanyID AND (GL.TypeLedger = A.TypeLedger OR GL.TypeLedger = 2) AND
                         A.Account LIKE '111%') THEN ISNULL(GL.CreditAmount, 0)
                   WHEN (A.CompanyID = GL.CompanyID AND (GL.TypeLedger = A.TypeLedger OR GL.TypeLedger = 2) AND
                         GL.BankAccountDetailID = A.BankAccountDetailID) THEN ISNULL(GL.CreditAmount, 0)
                   ELSE 0 END)   AS CreditAmount,
           SUM(CASE
                   WHEN (A.CompanyID = GL.CompanyID AND (GL.TypeLedger = A.TypeLedger OR GL.TypeLedger = 2) AND
                         A.Account LIKE '111%') THEN ISNULL(GL.CreditAmountOriginal, 0)
                   WHEN (A.CompanyID = GL.CompanyID AND (GL.TypeLedger = A.TypeLedger OR GL.TypeLedger = 2) AND
                         GL.BankAccountDetailID = A.BankAccountDetailID) THEN ISNULL(GL.CreditAmountOriginal, 0)
                   ELSE 0 END)   AS CreditAmountOriginal,
           A.BankAccountDetailID AS BankAccountDetailID
    FROM @tblAccount A
             LEFT JOIN dbo.GeneralLedger GL ON A.Account = GL.Account
    WHERE GL.PostedDate <= @Date
    GROUP BY A.Account, A.CompanyID, A.TypeLedger, A.BankAccountDetailID

    SELECT * FROM @tblResult ORDER BY Account

END
go

