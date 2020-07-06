/*
-- =============================================
-- Author:		<Hautv>
-- Create date: <20-02-2020>
-- Description:	<Sổ cái (hình thức nhật ký chung)>
-- S03a-DNN sổ nhật ký chung
-- Proc_SO_CAI_HT_NHAT_KY_CHUNG_S03b '01/01/2020 00:00:00', '12/31/2020 23:59:59', ',131,331,111', 0, '5A814271-A115-41D1-BA4D-C50BC0040482', 1, 0, 1
-- =============================================
*/
CREATE PROCEDURE Proc_SO_CAI_HT_NHAT_KY_CHUNG_S03b @FromDate DATETIME,
                                                   @ToDate DATETIME,
                                                   @AccountNumber NVARCHAR(MAX),
                                                   @IsSimilarSum BIT,
                                                   @CompanyID uniqueidentifier, -- Hiển thị số lũy kế kỳ trước chuyển sang
                                                   @IsFinancialBook BIT, -- Hiển thị số lũy kế kỳ trước chuyển sang
                                                   @AccountingType INT,
                                                   @Calcular BIT,
                                                   @isDependent BIT
AS
BEGIN
    SET NOCOUNT ON
    CREATE TABLE #Result
    (
        --KeyID NVARCHAR(100)COLLATE Latin1_General_CI_AS,
        ReferenceID                UNIQUEIDENTIFIER,
        RefType                    INT,
        PostedDate                 DATETIME,
        No                         NVARCHAR(25) COLLATE Latin1_General_CI_AS,
        Date                       DATETIME,
        InvDate                    DATETIME,
        InvNo                      NVARCHAR(MAX) COLLATE Latin1_General_CI_AS,
        JournalMemo                NVARCHAR(512) COLLATE Latin1_General_CI_AS,
        AccountNumber              NVARCHAR(25) COLLATE Latin1_General_CI_AS,
        DetailAccountNumber        NVARCHAR(25) COLLATE Latin1_General_CI_AS,
        AccountCategoryKind        INT,
        AccountName                NVARCHAR(512) COLLATE Latin1_General_CI_AS,
        CorrespondingAccountNumber NVARCHAR(25) COLLATE Latin1_General_CI_AS,
        DebitAmount                MONEY,
        CreditAmount               MONEY,
        OrderType                  INT,
        IsBold                     BIT,
        OrderNumber                int,
        OrderPriority              int,
    )
    CREATE TABLE #tblAccountNumber
    (
        AccountNumber        NVARCHAR(25) COLLATE Latin1_General_CI_AS PRIMARY KEY,
        AccountName          NVARCHAR(512) COLLATE Latin1_General_CI_AS,
        AccountNumberPercent NVARCHAR(25) COLLATE Latin1_General_CI_AS,
        AccountCategoryKind  INT,
        IsParent             BIT
    )


    INSERT #tblAccountNumber
    SELECT A1.AccountNumber,
           A1.AccountName,
           A1.AccountNumber + '%',
           A1.AccountGroupKind,
           A1.IsParentNode
    FROM dbo.Func_ConvertStringIntoTable_Nvarchar(@AccountNumber, ',') F
             INNER JOIN dbo.AccountList A1 ON A1.AccountNumber = F.Value
    where A1.AccountingType = @AccountingType
      and A1.CompanyID = @CompanyID

    IF @IsSimilarSum = 0 /*khong cong gop but ke toan*/
        Begin
            INSERT #Result
            (ReferenceID,
             RefType,
             PostedDate,
             No,
             Date,
             InvDate,
             InvNo,
             JournalMemo,
             AccountNumber,
             DetailAccountNumber,
             AccountName,
             CorrespondingAccountNumber,
             AccountCategoryKind,
             DebitAmount,
             CreditAmount,
             ORDERType,
             IsBold,
             OrderNumber,
             OrderPriority)
            SELECT GL.ReferenceID,
                   GL.TypeID,
                   GL.PostedDate,
                   CASE WHEN @IsFinancialBook = 1 then GL.NoFBook else GL.NoMBook end as No,
                   GL.Date,
                   Gl.InvoiceDate,
                   GL.InvoiceNo,
                   GL.Description                                                     AS JournalMemo,
                   AC.AccountNumber,
                   GL.Account                                                         AS DetailAccountNumber,
                   AC.AccountName,
                   GL.AccountCorresponding,
                   AC.AccountCategoryKind,
                   DebitAmount,
                   CreditAmount,
                   1                                                                  AS ORDERType,
                   0,
                   CASE
                       WHEN (AC.AccountNumber LIKE N'11%' OR
                             AC.AccountNumber LIKE N'15%' AND AC.AccountNumber NOT LIKE N'154%')
                           AND GL.DebitAmount <> 0
                           THEN 0
                       ELSE 1
                       END                                                            AS OrderNumber,
                   OrderPriority
            FROM GeneralLedger AS GL
                     INNER JOIN #tblAccountNumber AC ON GL.Account LIKE AC.AccountNumberPercent
            WHERE GL.PostedDate BETWEEN @FromDate AND @ToDate
              AND GL.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
              and (GL.TypeLedger = 2
                or GL.TypeLedger = case
                                       when @IsFinancialBook = 1 then
                                           0
                                       else
                                           1
                    end)
              AND (GL.DebitAmount <> 0
                OR GL.CreditAmount <> 0
                or GL.DebitAmountOriginal <> 0
                OR GL.CreditAmountOriginal <> 0
                )
            ORDER BY OrderPriority
        End
    Else
        Begin
            /*cong gop but toan thue*/
            INSERT #Result(ReferenceID, RefType, PostedDate, No, Date, InvDate, InvNo, JournalMemo,
                           AccountNumber,
                           DetailAccountNumber, AccountName, CorrespondingAccountNumber,
                           AccountCategoryKind, DebitAmount, CreditAmount, ORDERType, IsBold, OrderNumber,
                           OrderPriority)
            SELECT ReferenceID,
                   RefType,
                   PostedDate,
                   No,
                   Date,
                   InvDate,
                   InvNo,
                   JournalMemo,
                   AccountNumber,
                   DetailAccountNumber,
                   AccountName,
                   CorrespondingAccountNumber,
                   AccountCategoryKind,
                   DebitAmount,
                   CreditAmount,
                   ORDERType,
                   IsBold,
                   OrderNumber,
                   OrderPriority
            FROM (SELECT
                      --convert(nvarchar(100),GL.ID)  + '-' + AC.AccountNumber as KeyID,
                      GL.ReferenceID                                                     as ReferenceID,
                      GL.TypeID                                                          as RefType,
                      GL.PostedDate                                                      as PostedDate,
                      CASE WHEN @IsFinancialBook = 1 then GL.NoFBook else GL.NoMBook end as No,
                      GL.Date                                                            as Date,
                      Gl.InvoiceDate                                                     as InvDate,
                      GL.InvoiceNo                                                       as InvNo,
                      GL.Reason                                                          AS JournalMemo,
                      AC.AccountNumber                                                   as AccountNumber,
                      GL.Account                                                         AS DetailAccountNumber,
                      AC.AccountName                                                     as AccountName,
                      GL.AccountCorresponding                                            as CorrespondingAccountNumber,
                      AC.AccountCategoryKind                                             as AccountCategoryKind,
                      SUM(GL.DebitAmount)                                                as DebitAmount,
                      0                                                                  as CreditAmount,
                      1                                                                  AS ORDERType,
                      0                                                                  as IsBold,

                      CASE
                          WHEN (AC.AccountNumber LIKE N'11%' OR
                                AC.AccountNumber LIKE N'15%' AND AC.AccountNumber NOT LIKE N'154%')
                              AND SUM(GL.DebitAmount) <> 0
                              THEN 0
                          ELSE 1
                          END                                                            AS OrderNumber,
                      OrderPriority
                  FROM GeneralLedger AS GL
                           INNER JOIN #tblAccountNumber AC ON GL.Account LIKE AC.AccountNumberPercent
                  WHERE GL.PostedDate BETWEEN @FromDate AND @ToDate
                    AND GL.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
                    and (GL.TypeLedger = 2
                      or GL.TypeLedger = case
                                             when @IsFinancialBook = 1 then
                                                 0
                                             else
                                                 1
                          end)
                  GROUP BY --convert(nvarchar(100),GL.ID)  + '-' + AC.AccountNumber,
                           GL.ReferenceID,
                           GL.TypeID,
                           GL.PostedDate,
                           CASE WHEN @IsFinancialBook = 1 then GL.NoFBook else GL.NoMBook end,
                           GL.Date,
                           Gl.InvoiceDate,
                           GL.InvoiceNo,
                           GL.Reason,
                           AC.AccountNumber,
                           GL.Account,
                           AC.AccountName,
                           GL.AccountCorresponding,
                           AC.AccountCategoryKind,
                           OrderPriority
                  HAVING SUM(GL.DebitAmount) <> 0
                      OR SUM(GL.CreditAmount) <> 0

                  UNION ALL

                  SELECT
                      --convert(nvarchar(100),GL.ID)  + '-' + AC.AccountNumber as KeyID,
                      GL.ReferenceID                                                     as ReferenceID,
                      GL.TypeID                                                          as RefType,
                      GL.PostedDate                                                      as PostedDate,
                      CASE WHEN @IsFinancialBook = 1 then GL.NoFBook else GL.NoMBook end as No,
                      GL.Date                                                            as Date,
                      Gl.InvoiceDate                                                     as InvDate,
                      GL.InvoiceNo                                                       as InvNo,
                      GL.Reason                                                          AS JournalMemo,
                      AC.AccountNumber                                                   as AccountNumber,
                      GL.Account                                                         AS DetailAccountNumber,
                      AC.AccountName                                                     as AccountName,
                      GL.AccountCorresponding                                            as CorrespondingAccountNumber,
                      AC.AccountCategoryKind                                             as AccountCategoryKind,
                      0                                                                  as DebitAmount,
                      SUM(GL.CreditAmount)                                               as CreditAmount,
                      1                                                                  AS ORDERType,
                      0                                                                  as IsBold,
                      CASE
                          WHEN (AC.AccountNumber LIKE N'11%' OR
                                AC.AccountNumber LIKE N'15%' AND AC.AccountNumber NOT LIKE N'154%')
                              AND SUM(GL.CreditAmount) <> 0
                              THEN 0
                          ELSE 1
                          END                                                            AS OrderNumber,
                      OrderPriority
                  FROM GeneralLedger AS GL
                           INNER JOIN #tblAccountNumber AC ON GL.Account LIKE AC.AccountNumberPercent
                  WHERE GL.PostedDate BETWEEN @FromDate AND @ToDate
                    AND GL.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
                    and (GL.TypeLedger = 2
                      or GL.TypeLedger = case
                                             when @IsFinancialBook = 1 then
                                                 0
                                             else
                                                 1
                          end)
                  GROUP BY --convert(nvarchar(100),GL.ID)  + '-' + AC.AccountNumber,
                           GL.ReferenceID,
                           GL.TypeID,
                           GL.PostedDate,
                           CASE WHEN @IsFinancialBook = 1 then GL.NoFBook else GL.NoMBook end,
                           GL.Date,
                           Gl.InvoiceDate,
                           GL.InvoiceNo,
                           GL.Reason,
                           AC.AccountNumber,
                           GL.Account,
                           AC.AccountName,
                           GL.AccountCorresponding,
                           AC.AccountCategoryKind,
                           OrderPriority
                  HAVING SUM(GL.DebitAmount) <> 0
                      OR SUM(GL.CreditAmount) <> 0
                 ) T
            where T.DebitAmount
                <> 0
               OR T.CreditAmount
                <> 0
        End
    if @Calcular = 1
        begin
            SELECT AccountNumber,
                   AccountName,
                   IsParent,
                   SUM(OpenningDebitAmount)  AS OpenningDebitAmount,
                   SUM(OpenningCreditAmount) AS OpenningCreditAmount,
                   SUM(ClosingDebitAmount)   AS ClosingDebitAmount,
                   SUM(ClosingCreditAmount)  AS ClosingCreditAmount,
                   SUM(AccumDebitAmount)     AS AccumDebitAmount,
                   SUM(AccumCreditAmount)    AS AccumCreditAmount,
                   AccountCategoryKind
            FROM (
                     SELECT AC.AccountNumber,
                            AC.AccountName,
                            CASE
                                WHEN AC.AccountCategoryKind = 0
                                    THEN SUM(GL.DebitAmount - GL.CreditAmount)
                                WHEN ac.AccountCategoryKind = 2
                                    AND SUM(GL.DebitAmount - GL.CreditAmount) > 0
                                    THEN SUM(GL.DebitAmount - GL.CreditAmount)
                                ELSE 0
                                END AS OpenningDebitAmount,
                            CASE
                                WHEN AC.AccountCategoryKind = 1
                                    THEN SUM(GL.CreditAmount - GL.DebitAmount)
                                WHEN AC.AccountCategoryKind = 2
                                    AND SUM(GL.CreditAmount - GL.DebitAmount) > 0
                                    THEN SUM(GL.CreditAmount - GL.DebitAmount)
                                ELSE 0
                                END AS OpenningCreditAmount,
                            0       AS ClosingDebitAmount,
                            0       AS ClosingCreditAmount,
                            0       AS AccumDebitAmount,
                            0       AS AccumCreditAmount,
                            AC.IsParent,
                            AC.AccountCategoryKind
                     FROM GeneralLedger GL
                              INNER JOIN #tblAccountNumber AC ON GL.Account LIKE AC.AccountNumberPercent
                     WHERE GL.PostedDate < @FromDate
                       AND GL.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
                       and (GL.TypeLedger = 2
                         or GL.TypeLedger = case
                                                when @IsFinancialBook = 1 then
                                                    0
                                                else
                                                    1
                             end)
                     GROUP BY GL.BranchID,
                              AC.AccountNumber,
                              AC.AccountName,
                              AC.AccountCategoryKind,
                              AC.IsParent
                     UNION ALL
                     SELECT AC.AccountNumber,
                            AC.AccountName,
                            0       AS OpenningDebitAmount,
                            0       AS OpenningCreditAmount,
                            CASE
                                WHEN AC.AccountCategoryKind = 0
                                    THEN SUM(GL.DebitAmount - GL.CreditAmount)
                                WHEN ac.AccountCategoryKind = 2
                                    AND SUM(GL.DebitAmount - GL.CreditAmount) > 0
                                    THEN SUM(GL.DebitAmount - GL.CreditAmount)
                                ELSE 0
                                END AS ClosingDebitAmount,
                            CASE
                                WHEN AC.AccountCategoryKind = 1
                                    THEN SUM(GL.CreditAmount - GL.DebitAmount)
                                WHEN AC.AccountCategoryKind = 2
                                    AND SUM(GL.CreditAmount - GL.DebitAmount) > 0
                                    THEN SUM(GL.CreditAmount - GL.DebitAmount)
                                ELSE 0
                                END AS ClosingCreditAmount,
                            0       AS AccumDebitAmount,
                            0       AS AccumCreditAmount,
                            AC.IsParent,
                            AC.AccountCategoryKind
                     FROM GeneralLedger GL
                              INNER JOIN #tblAccountNumber AC ON GL.Account LIKE AC.AccountNumberPercent
                     WHERE GL.PostedDate <= @ToDate
                       AND GL.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
                       and (GL.TypeLedger = 2
                         or GL.TypeLedger = case
                                                when @IsFinancialBook = 1 then
                                                    0
                                                else
                                                    1
                             end)
                     GROUP BY AC.AccountNumber,
                              AC.AccountName,
                              AC.AccountCategoryKind,
                              AC.IsParent
                     UNION ALL
                     SELECT AC.AccountNumber,
                            AC.AccountName,
                            0                    AS OpenningDebitAmount,
                            0                    AS OpenningCreditAmount,
                            0                    AS ClosingDebitAmount,
                            0                    AS ClosingCreditAmount,
                            SUM(GL.DebitAmount)  AS AccumDebitAmount,
                            SUM(GL.CreditAmount) AS AccumCreditAmount,
                            AC.IsParent,
                            AC.AccountCategoryKind
                     FROM GeneralLedger GL
                              INNER JOIN #tblAccountNumber AC ON GL.Account LIKE AC.AccountNumberPercent
                     WHERE GL.PostedDate BETWEEN @FromDate AND @ToDate
                       AND GL.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
                       and (GL.TypeLedger = 2
                         or GL.TypeLedger = case
                                                when @IsFinancialBook = 1 then
                                                    0
                                                else
                                                    1
                             end)
                     GROUP BY AC.AccountNumber,
                              AC.AccountName,
                              AC.AccountCategoryKind,
                              AC.IsParent
                 ) AS A --, Account B
                 --where A.AccountNumber = B.AccountNumber
            GROUP BY AccountNumber,
                     AccountName,
                     IsParent,
                     AccountCategoryKind
            HAVING SUM(OpenningDebitAmount) <> 0
                OR SUM(OpenningCreditAmount) <> 0
                OR SUM(ClosingDebitAmount) <> 0
                OR SUM(ClosingCreditAmount) <> 0
                OR SUM(AccumDebitAmount) <> 0
                OR SUM(AccumCreditAmount) <> 0
            ORDER BY AccountNumber
        end
    else
        begin
            SELECT ROW_NUMBER()
                           OVER (ORDER BY AccountNumber,DetailAccountNumber, OrderType, PostedDate, Date,OrderNumber, No) AS RowNum,
                   --KeyID,
                   ReferenceID,
                   RefType,
                   PostedDate,
                   Date,
                   No,
                   InvDate,
                   InvNo,
                   JournalMemo,
                   AccountNumber,
                   DetailAccountNumber,
                   CorrespondingAccountNumber,
                   DebitAmount,
                   CreditAmount,
                   IsBold,
                   AccountCategoryKind
            FROM #Result
            ORDER BY AccountNumber, PostedDate, No, OrderPriority
        end

END
go

