/*
-- =============================================
-- Author:		<chuongnv>
-- Create date: <18-02-2020>
-- Description:	<Lấy dữ liệu bảng cân đối tài khoản>
-- =============================================
*/
CREATE FUNCTION [dbo].[Func_Get_BANG_CAN_DOI_TAI_KHOAN](@CompanyID UNIQUEIDENTIFIER,
                                                        @FromDate DATETIME,
                                                        @ToDate DATETIME,
                                                        @MaxAccountGrade INT,
                                                        @IsBalanceBothSide BIT,
                                                        @IsWorkingWithManagementBook BIT,
                                                        @IncludeDependentBranch BIT)
    RETURNS @Result TABLE
                    (
                        AccountID             UNIQUEIDENTIFIER,
                        DetailByAccountObject BIT,
                        AccountObjectType     INT,
                        AccountNumber         NVARCHAR(50),
                        AccountName           NVARCHAR(512),
                        AccountNameEnglish    NVARCHAR(512),
                        AccountCategoryKind   INT,
                        IsParent              BIT,
                        ParentID              UNIQUEIDENTIFIER,
                        Grade                 INT,
                        AccountKind           INT,
                        SortOrder             INT,
                        OpeningDebitAmount    DECIMAL(25, 4),
                        OpeningCreditAmount   DECIMAL(25, 4),
                        DebitAmount           DECIMAL(25, 4),
                        CreditAmount          DECIMAL(25, 4),
                        DebitAmountAccum      DECIMAL(25, 4),
                        CreditAmountAccum     DECIMAL(25, 4),
                        ClosingDebitAmount    DECIMAL(25, 4),
                        ClosingCreditAmount   DECIMAL(25, 4)
                    )
AS
BEGIN
    /*ngày bắt đầu chương trình*/
    DECLARE
        @StartDate DATETIME
    SELECT TOP 1 @StartDate = CONVERT(DATETIME, StartDate, 103)
    FROM dbo.EbOrganizationUnit
    WHERE ID = @CompanyID
    IF @IsBalanceBothSide = 1
        BEGIN
            DECLARE
                @GeneralLedger TABLE
                               (
                                   AccountNumber         NVARCHAR(20),
                                   AccountObjectID       UNIQUEIDENTIFIER,
                                   CurrencyID            NVARCHAR(3),
                                   OpenningDebitAmount   DECIMAL(25, 4),
                                   DebitAmount           DECIMAL(25, 4),
                                   CreditAmount          DECIMAL(25, 4),
                                   DebitAmountAccum      DECIMAL(25, 4),
                                   CreditAmountAccum     DECIMAL(25, 4),
                                   OpenningDebitAmountOC DECIMAL(25, 4),
                                   DebitAmountOC         DECIMAL(25, 4),
                                   CreditAmountOC        DECIMAL(25, 4),
                                   DebitAmountOCAccum    DECIMAL(25, 4),
                                   CreditAmountOCAccum   DECIMAL(25, 4)
                               )
            INSERT INTO @GeneralLedger
            SELECT G.*
            FROM (
                     SELECT Account,
                            AccountingObjectID,
                            GL.CurrencyID,
                            SUM(CASE
                                    WHEN GL.PostedDate < @FromDate THEN GL.DebitAmount - GL.CreditAmount
                                    ELSE 0
                                END) AS OpenningDebitAmount,
                            SUM(CASE
                                    WHEN GL.PostedDate BETWEEN @FromDate AND @ToDate THEN (GL.DebitAmount)
                                    ELSE 0
                                END) AS DebitAmount,
                            SUM(CASE
                                    WHEN GL.PostedDate BETWEEN @FromDate AND @ToDate THEN (GL.CreditAmount)
                                    ELSE 0
                                END) AS CreditAmount,
                            SUM(CASE
                                    WHEN GL.PostedDate BETWEEN @StartDate AND @ToDate THEN (GL.DebitAmount)
                                    ELSE 0
                                END) AS DebitAmountAccum,
                            SUM(CASE
                                    WHEN GL.PostedDate BETWEEN @StartDate AND @ToDate THEN (GL.CreditAmount)
                                    ELSE 0
                                END) AS CreditAmountAccum,
                            SUM(CASE
                                    WHEN GL.PostedDate < @FromDate THEN GL.DebitAmountOriginal - GL.CreditAmountOriginal
                                    ELSE 0
                                END) AS OpenningDebitAmountOC,
                            SUM(CASE
                                    WHEN GL.PostedDate BETWEEN @FromDate AND @ToDate THEN (GL.DebitAmountOriginal)
                                    ELSE 0
                                END) AS DebitAmountOC,
                            SUM(CASE
                                    WHEN GL.PostedDate BETWEEN @FromDate AND @ToDate THEN (GL.CreditAmountOriginal)
                                    ELSE 0
                                END) AS CreditAmountOC,
                            SUM(CASE
                                    WHEN GL.PostedDate BETWEEN @StartDate AND @ToDate THEN (GL.DebitAmountOriginal)
                                    ELSE 0
                                END) AS DebitAmountOCAccum,
                            SUM(CASE
                                    WHEN GL.PostedDate BETWEEN @StartDate AND @ToDate THEN (GL.CreditAmountOriginal)
                                    ELSE 0
                                END) AS CreditAmountOCAccum
                     FROM dbo.GeneralLedger AS GL
                     WHERE GL.PostedDate <= @ToDate
                       AND GL.CompanyID IN (SELECT id FROM Func_getCompany(@CompanyID, @IncludeDependentBranch))
                       AND (GL.TypeLedger = @IsWorkingWithManagementBook OR GL.TypeLedger = 2)
                     GROUP BY Account,
                              AccountingObjectID,
                              GL.CurrencyID
                 ) G

            DECLARE
                @Data TABLE
                      (
                          AccountNumber       NVARCHAR(50),
                          AccountObjectID     UNIQUEIDENTIFIER,
                          CurrencyID          NVARCHAR(20),
                          SortOrder           INT,
                          OpeningDebitAmount  DECIMAL(25, 4),
                          OpeningCreditAmount DECIMAL(25, 4),
                          DebitAmount         DECIMAL(25, 4),
                          CreditAmount        DECIMAL(25, 4),
                          DebitAmountAccum    DECIMAL(25, 4),/*Nợ lũy kế*/
                          CreditAmountAccum   DECIMAL(25, 4),/*có lũy kế*/
                          ClosingDebitAmount  DECIMAL(25, 4),
                          ClosingCreditAmount DECIMAL(25, 4)
                      )

            INSERT @Data
            SELECT F.AccountNumber,
                   null,
                   '',
                   0,
                   SUM(CASE
                           WHEN F.AccountCategoryKind = 0
                               OR (
                                        F.AccountCategoryKind NOT IN (0, 1)
                                        AND F.OpenningDebitAmount > 0
                                    ) THEN F.OpenningDebitAmount
                           ELSE 0
                       END)                 AS OpenningDebitAmount,
                   SUM(CASE
                           WHEN F.AccountCategoryKind = 1
                               OR (
                                        F.AccountCategoryKind NOT IN (0, 1)
                                        AND F.OpenningDebitAmount < 0
                                    ) THEN -1 * F.OpenningDebitAmount
                           ELSE 0
                       END)                 AS OpenningCreditAmount,
                   SUM(F.DebitAmount)       AS DebitAmount,
                   SUM(F.CreditAmount)      AS CreditAmount,
                   SUM(F.DebitAmountAccum)  AS DebitAmountAccum,
                   SUM(F.CreditAmountAccum) AS CreditAmountAccum,
                   SUM(CASE
                           WHEN F.AccountCategoryKind = 0
                               OR (
                                        F.AccountCategoryKind NOT IN (0, 1)
                                        AND F.OpenningDebitAmount + F.DebitAmount - F.CreditAmount > 0
                                    ) THEN F.OpenningDebitAmount + F.DebitAmount - F.CreditAmount
                           ELSE 0
                       END)                 AS ClosingDebitAmount,
                   SUM(CASE
                           WHEN F.AccountCategoryKind = 1
                               OR (
                                        F.AccountCategoryKind NOT IN (0, 1)
                                        AND F.OpenningDebitAmount + F.DebitAmount - F.CreditAmount < 0
                                    ) THEN F.CreditAmount - F.OpenningDebitAmount - F.DebitAmount
                           ELSE 0
                       END)                 AS ClosingCreditAmount
            FROM (
                     SELECT A.AccountNumber,
                            SUM(OpenningDebitAmount) AS OpenningDebitAmount,
                            SUM(DebitAmount)         AS DebitAmount,
                            SUM(CreditAmount)        AS CreditAmount,
                            SUM(DebitAmountAccum)    AS DebitAmountAccum,
                            SUM(CreditAmountAccum)   AS CreditAmountAccum,
                            CASE
                                WHEN (A.DetailType LIKE '%0%' OR A.DetailType LIKE '%1%' OR A.DetailType LIKE '%2%')
                                    THEN GL.AccountObjectID
                                ELSE NULL
                                END                  AS AccountObjectID,
                            A.AccountGroupKind          AccountCategoryKind
                     FROM @GeneralLedger AS GL
                              INNER JOIN dbo.AccountList A ON GL.AccountNumber = A.AccountNumber
                     WHERE GL.AccountNumber NOT LIKE '0%'
                       AND A.CompanyID = (SELECT TOP (1) (CASE
                                                              WHEN ParentID IS NOT NULL THEN ParentID
                                                              ELSE ID END)
                                          FROM EbOrganizationUnit
                                          WHERE ID = @CompanyID)
                       AND A.IsParentNode = 0
                     GROUP BY A.AccountNumber,
                              A.AccountGroupKind,
                              CASE
                                  WHEN (A.DetailType LIKE '%0%' OR A.DetailType LIKE '%1%' OR A.DetailType LIKE '%2%')
                                      THEN GL.AccountObjectID
                                  ELSE NULL
                                  END
                     HAVING SUM(OpenningDebitAmount) <> 0
                         OR SUM(DebitAmount) <> 0
                         OR SUM(CreditAmount) <> 0
                 ) AS F
            GROUP BY F.AccountNumber,
                     F.AccountCategoryKind,
                     F.AccountObjectID
            OPTION ( RECOMPILE )
            INSERT @Result
            SELECT A.ID,
                   null,
                   null,
                   A.AccountNumber,
                   CASE
                       WHEN D.CurrencyID <> '' THEN D.CurrencyID
                       ELSE A.AccountName
                       END,
                   CASE
                       WHEN D.CurrencyID <> '' THEN D.CurrencyID
                       ELSE A.AccountNameGlobal
                       END,
                   A.AccountGroupKind,
                   A.IsParentNode,
                   A.ParentAccountID,
                   A.Grade,
                   0 AS AccountKind,
                   D.SortOrder,
                   SUM(D.OpeningDebitAmount),
                   SUM(D.OpeningCreditAmount),
                   SUM(D.DebitAmount),
                   SUM(D.CreditAmount),
                   SUM(D.DebitAmountAccum),
                   SUM(D.CreditAmountAccum),
                   SUM(D.ClosingDebitAmount),
                   SUM(D.ClosingCreditAmount)
            FROM @Data D
                     INNER JOIN dbo.AccountList A ON D.AccountNumber LIKE A.AccountNumber + '%'
            WHERE A.Grade <= @MaxAccountGrade
              AND A.CompanyID = (SELECT TOP (1) (CASE
                                                     WHEN ParentID IS NOT NULL THEN ParentID
                                                     ELSE ID END)
                                 FROM EbOrganizationUnit
                                 WHERE ID = @CompanyID)
            GROUP BY A.ID,
                     A.AccountNumber,
                     CASE
                         WHEN D.CurrencyID <> '' THEN D.CurrencyID
                         ELSE A.AccountName
                         END,
                     CASE
                         WHEN D.CurrencyID <> '' THEN D.CurrencyID
                         ELSE A.AccountNameGlobal
                         END,
                     A.AccountGroupKind,
                     A.IsParentNode,
                     A.ParentAccountID,
                     A.Grade,
                     D.SortOrder
            HAVING SUM(D.OpeningDebitAmount) <> 0
                OR SUM(D.OpeningCreditAmount) <> 0
                OR SUM(D.DebitAmount) <> 0
                OR SUM(D.CreditAmount) <> 0
                OR SUM(D.ClosingDebitAmount) <> 0
                OR SUM(D.ClosingCreditAmount) <> 0
            OPTION ( RECOMPILE )
        END
    ELSE
        BEGIN
            DECLARE
                @GeneralLedgerP1 TABLE
                                 (
                                     AccountNumber       NVARCHAR(20),
                                     OpenningDebitAmount DECIMAL(25, 4),
                                     DebitAmount         DECIMAL(25, 4),
                                     CreditAmount        DECIMAL(25, 4),
                                     DebitAmountAccum    DECIMAL(25, 4),
                                     CreditAmountAccum   DECIMAL(25, 4)
                                 )
            INSERT INTO @GeneralLedgerP1
            SELECT Account,
                   SUM(CASE
                           WHEN GL.PostedDate < @FromDate THEN GL.DebitAmount - GL.CreditAmount
                           ELSE 0
                       END) AS OpenningDebitAmount,
                   SUM(CASE
                           WHEN GL.PostedDate BETWEEN @FromDate AND @ToDate THEN (GL.DebitAmount)
                           ELSE 0
                       END) AS DebitAmount,
                   SUM(CASE
                           WHEN GL.PostedDate BETWEEN @FromDate AND @ToDate THEN (GL.CreditAmount)
                           ELSE 0
                       END) AS CreditAmount,
                   SUM(CASE
                           WHEN GL.PostedDate BETWEEN @StartDate AND @ToDate THEN (GL.DebitAmount)
                           ELSE 0
                       END) AS DebitAmountAccum,
                   SUM(CASE
                           WHEN GL.PostedDate BETWEEN @StartDate AND @ToDate THEN (GL.CreditAmount)
                           ELSE 0
                       END) AS CreditAmountAccum
            FROM dbo.GeneralLedger AS GL /*edit by cuongpv dbo.GeneralLedger -> @tbDataGL*/
            WHERE GL.PostedDate <= @ToDate
              AND GL.CompanyID IN (SELECT id FROM Func_getCompany(@CompanyID, @IncludeDependentBranch))
              AND (GL.TypeLedger = @IsWorkingWithManagementBook OR GL.TypeLedger = 2)
            GROUP BY Account
            INSERT @Result
            SELECT A.ID,
                   null                     DetailByAccountObject,
                   null                     AccountObjectType,
                   A.AccountNumber,
                   CASE
                       WHEN F.CurrencyID <> '' THEN F.CurrencyID
                       ELSE A.AccountName
                       END,
                   CASE
                       WHEN F.CurrencyID <> '' THEN F.CurrencyID
                       ELSE A.AccountNameGlobal
                       END,
                   A.AccountGroupKind,
                   A.IsParentNode,
                   A.ParentAccountID,
                   A.Grade               AS Grade,
                   F.AccountKind,
                   F.SortOrder,
                   (CASE
                        WHEN A.AccountGroupKind = 0
                            OR (
                                     A.AccountGroupKind NOT IN (0, 1)
                                     AND F.OpenningDebitAmount > 0
                                 ) THEN F.OpenningDebitAmount
                        ELSE 0
                       END)              AS OpenningDebitAmount,
                   (CASE
                        WHEN A.AccountGroupKind = 1
                            OR (
                                     A.AccountGroupKind NOT IN (0, 1)
                                     AND F.OpenningDebitAmount < 0
                                 ) THEN -1 * F.OpenningDebitAmount
                        ELSE 0
                       END)              AS OpenningCreditAmount,
                   (F.DebitAmount)       AS DebitAmount,
                   (F.CreditAmount)      AS CreditAmount,
                   (F.DebitAmountAccum)  AS DebitAmountAccum,
                   (F.CreditAmountAccum) AS CreditAmountAccum,
                   (CASE
                        WHEN A.AccountGroupKind = 0
                            OR (
                                     A.AccountGroupKind NOT IN (0, 1)
                                     AND F.OpenningDebitAmount + F.DebitAmount - F.CreditAmount > 0
                                 ) THEN F.OpenningDebitAmount + F.DebitAmount - F.CreditAmount
                        ELSE 0
                       END)              AS OpenningDebitAmount,
                   (CASE
                        WHEN A.AccountGroupKind = 1
                            OR (
                                     A.AccountGroupKind NOT IN (0, 1)
                                     AND F.OpenningDebitAmount + F.DebitAmount - F.CreditAmount < 0
                                 ) THEN F.CreditAmount - F.OpenningDebitAmount - F.DebitAmount
                        ELSE 0
                       END)              AS OpenningCreditAmount
            FROM (
                     SELECT A.ID,
                            null                        DetailByAccountObject,
                            null                        AccountObjectType,
                            A.AccountNumber,
                            ''                       AS CurrencyID,
                            0                        AS SortOrder,
                            SUM(OpenningDebitAmount) AS OpenningDebitAmount,
                            SUM(DebitAmount)         AS DebitAmount,
                            SUM(CreditAmount)        AS CreditAmount,
                            SUM(DebitAmountAccum)    AS DebitAmountAccum,
                            SUM(CreditAmountAccum)   AS CreditAmountAccum,
                            0                        AS AccountKind
                     FROM @GeneralLedgerP1 AS GL
                              INNER JOIN dbo.AccountList A ON GL.AccountNumber LIKE A.AccountNumber + '%'
                     WHERE A.Grade <= @MaxAccountGrade
                       AND A.CompanyID = (SELECT TOP (1) (CASE
                                                              WHEN ParentID IS NOT NULL THEN ParentID
                                                              ELSE ID END)
                                          FROM EbOrganizationUnit
                                          WHERE ID = @CompanyID)
                       AND (
                             OpenningDebitAmount <> 0
                             OR DebitAmount <> 0
                             OR CreditAmount <> 0
                         )
                     GROUP BY A.ID,
                              A.AccountNumber
                 ) AS F
                     INNER JOIN AccountList A ON F.AccountNumber = A.AccountNumber
            WHERE A.CompanyID = (SELECT TOP (1) (CASE
                                                     WHEN ParentID IS NOT NULL THEN ParentID
                                                     ELSE ID END)
                                 FROM EbOrganizationUnit
                                 WHERE ID = @CompanyID)
        END
    RETURN
END
go

