/*
*Author Hautv
* Sổ chi tiết các tài khoản
*    Proc_SO_CHI_TIET_CAC_TAI_KHOAN '2020-01-01', '2020-12-30', USD, ',131,132,331', 0, 1, '5A814271-A115-41D1-BA4D-C50BC0040482', 1, 0
*/
CREATE PROCEDURE [dbo].[Proc_SO_CHI_TIET_CAC_TAI_KHOAN] @FromDate DATETIME,
                                                        @ToDate DATETIME,
                                                        @CurrencyID NVARCHAR(3),
                                                        @AccountNumber NVARCHAR(MAX),
                                                        @GroupTheSameItem BIT,
                                                        @IsVietNamese bit,
                                                        @CompanyID uniqueidentifier, -- Hiển thị số lũy kế kỳ trước chuyển sang
                                                        @IsFinancialBook BIT, -- Hiển thị số lũy kế kỳ trước chuyển sang
                                                        @AccountingType INT,
                                                        @isDependent BIT
AS
BEGIN
    DECLARE
        @CurrencyIDEbOrganizationUnit nvarchar(3) = (select CurrencyID from EbOrganizationUnit where id = @CompanyID)
    SET NOCOUNT ON
    --DECLARE @Result TABLE
    CREATE TABLE #Result
    (
        RowNum                       INT PRIMARY KEY
            IDENTITY (1, 1),
        KeyID                        NVARCHAR(100) COLLATE Latin1_General_CI_AS,
        ReferenceID                  UNIQUEIDENTIFIER,
        TypeID                       INT,
        PostedDate                   DATETIME,
        No                           NVARCHAR(50) COLLATE Latin1_General_CI_AS,
        Date                         DATETIME,
        JournalMemo                  NVARCHAR(500) COLLATE Latin1_General_CI_AS,
        AccountNumber                NVARCHAR(50) COLLATE Latin1_General_CI_AS,
        AccountCategoryKind          INT,
        AccountName                  NVARCHAR(500) COLLATE Latin1_General_CI_AS,
        AccountNameWithAccountNumber NVARCHAR(500) COLLATE Latin1_General_CI_AS,
        AccountCorresponding         NVARCHAR(50) COLLATE Latin1_General_CI_AS,
        DebitAmountOriginal          MONEY, -- Phát sinh nợ
        DebitAmount                  MONEY, --Phát sinh nợ quy đổi
        CreditAmountOriginal         MONEY, -- Phát sinh Có
        CreditAmount                 MONEY, -- Phát sinh Có quy đổi
        ClosingDebitAmountOriginal   MONEY, --Dư nợ
        ClosingDebitAmount           MONEY, -- Dư nợ quy đổi
        ClosingCreditAmountOriginal  MONEY, -- Dư có
        ClosingCreditAmount          MONEY, -- Dư có quy đổi
        OrderType                    INT,
        IsBold                       BIT,
        UnResonableCost              NVARCHAR(500) COLLATE Latin1_General_CI_AS,
        OrderNumber                  INT/*thứ tự để sắp xếp thu trước/chi sau*/
        ,
        OrderPriority                INT /* thứ tự sắp xếp trên chi tiết*/
    )
    ----------------------------------
    CREATE TABLE #Result1
    (
        RowNum                       INT IDENTITY (1, 1) PRIMARY KEY,
        KeyID                        NVARCHAR(100) COLLATE Latin1_General_CI_AS,
        ReferenceID                  UNIQUEIDENTIFIER,
        TypeID                       INT,
        PostedDate                   DATETIME,
        No                           NVARCHAR(50) COLLATE Latin1_General_CI_AS,
        Date                         DATETIME,
        JournalMemo                  NVARCHAR(500) COLLATE Latin1_General_CI_AS,
        AccountNumber                NVARCHAR(50) COLLATE Latin1_General_CI_AS,
        AccountCategoryKind          INT,
        AccountName                  NVARCHAR(500) COLLATE Latin1_General_CI_AS,
        AccountNameWithAccountNumber NVARCHAR(500) COLLATE Latin1_General_CI_AS,
        AccountCorresponding         NVARCHAR(50) COLLATE Latin1_General_CI_AS,
        DebitAmountOriginal          MONEY, -- Phát sinh nợ
        DebitAmount                  MONEY, --Phát sinh nợ quy đổi
        CreditAmountOriginal         MONEY, -- Phát sinh Có
        CreditAmount                 MONEY, -- Phát sinh Có quy đổi
        ClosingDebitAmountOriginal   MONEY, --Dư nợ
        ClosingDebitAmount           MONEY, -- Dư nợ quy đổi
        ClosingCreditAmountOriginal  MONEY, -- Dư có
        ClosingCreditAmount          MONEY, -- Dư có quy đổi
        OrderType                    INT,
        IsBold                       BIT,
        --Bổ sung các cột Customfield
        UnResonableCost              NVARCHAR(500) COLLATE Latin1_General_CI_AS,
        OrderNumber                  INT/*thứ tự để sắp xếp thu trước/chi sau*/
        ,
        OrderPriority                INT /* thứ tự sắp xếp trên chi tiết*/
    )

    -- Bảng Account Number
    CREATE TABLE #tblAccountNumber
    (
        AccountNumber        NVARCHAR(20) COLLATE Latin1_General_CI_AS PRIMARY KEY,
        AccountName          NVARCHAR(500) COLLATE Latin1_General_CI_AS,
        AccountNameEnglish   NVARCHAR(500) COLLATE Latin1_General_CI_AS,
        AccountNumberPercent NVARCHAR(25)
            COLLATE Latin1_General_CI_AS,
        AccountCategoryKind  INT
    )

    INSERT #tblAccountNumber
    SELECT A.AccountNumber,
           A.AccountName,
           A.AccountNameGlobal,
           A.AccountNumber,
           A.AccountGroupKind
    FROM dbo.Func_ConvertStringIntoTable_Nvarchar(@AccountNumber, ',') F
             INNER JOIN dbo.AccountList A ON A.AccountNumber = F.Value
    where A.CompanyID = @CompanyID
      and A.AccountingType = @AccountingType

    -- Lấy số dư đầu kỳ cho các tài khoản
    INSERT #Result
    (KeyID,
     AccountNumber,
     AccountName,
     AccountNameWithAccountNumber,
     AccountCategoryKind,
     JournalMemo,
     OrderType,
     IsBold,
     ClosingDebitAmount,
     ClosingDebitAmountOriginal,
     ClosingCreditAmount,
     ClosingCreditAmountOriginal)
    SELECT N'SDDK' + AccountNumber,
           AccountNumber,
           AccountName,
           AccountNameWithAccountNumber,
           AccountCategoryKind,
           N'Số dư đầu kỳ',
           0,
           1,
           CASE
               WHEN AccountCategoryKind = 0 THEN OpenAmount
               WHEN AccountCategoryKind = 1 THEN $0
               ELSE CASE
                        WHEN OpenAmount > 0 THEN OpenAmount
                        ELSE $0
                   END
               END AS ClosingDebitAmount,
           CASE
               WHEN AccountCategoryKind = 0 THEN OpenAmountOC
               WHEN AccountCategoryKind = 1 THEN $0
               ELSE CASE
                        WHEN OpenAmountOC > 0 THEN OpenAmountOC
                        ELSE $0
                   END
               END AS ClosingDebitAmountOriginal,
           CASE
               WHEN AccountCategoryKind = 1 THEN -1 * OpenAmount
               WHEN AccountCategoryKind = 0 THEN $0
               ELSE CASE
                        WHEN OpenAmount < 0
                            THEN -1 * OpenAmount
                        ELSE $0
                   END
               END AS ClosingCreditAmount,
           CASE
               WHEN AccountCategoryKind = 1
                   THEN -1 * OpenAmountOC
               WHEN AccountCategoryKind = 0 THEN $0
               ELSE CASE
                        WHEN OpenAmountOC < 0
                            THEN -1 * OpenAmountOC
                        ELSE $0
                   END
               END AS ClosingCreditAmountOriginal
    FROM (SELECT AC.AccountNumber,
                 AC.AccountName,
                 AC.AccountNumber + '-' + CASE @IsVietNamese
                                              WHEN 1 THEN AC.AccountName
                                              ELSE AC.AccountNameEngLish END AS AccountNameWithAccountNumber,
                 AC.AccountCategoryKind,
                 SUM(GL.DebitAmount - GL.CreditAmount)                       AS OpenAmount,
                 SUM(GL.DebitAmountOriginal - GL.CreditAmountOriginal)       AS OpenAmountOC
          FROM dbo.GeneralLedger AS GL
                   INNER JOIN #tblAccountNumber AC ON GL.Account LIKE AC.AccountNumberPercent
          WHERE (GL.PostedDate < @FromDate)
            AND case when @CurrencyIDEbOrganizationUnit <> @CurrencyID then GL.CurrencyID else 1 end =
                case when @CurrencyIDEbOrganizationUnit <> @CurrencyID then @CurrencyID else 1 end
            AND GL.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
            and (GL.TypeLedger = 2
              or GL.TypeLedger = case
                                     when @IsFinancialBook = 1 then
                                         0
                                     else
                                         1
                  end)
          GROUP BY AC.AccountNumber,
                   AC.AccountName,
                   AC.AccountNameEnglish,
                   AC.AccountCategoryKind
         ) AS OT
    WHERE OT.OpenAmount <> 0
       OR OT.OpenAmountOC <> 0
    --PRINT  'Lay DL PS trong ky: ' + CONVERT (NVARCHAR(500), GETDATE() ,109)

    --Lấy dữ liệu trong kỳ
    IF @GroupTheSameItem = 0 -- Không cộng gộp dl
        INSERT #Result
        (KeyID,
         ReferenceID,
         TypeID,
         PostedDate,
         No,
         Date,
         JournalMemo,
         AccountNumber,
         AccountName,
         AccountNameWithAccountNumber,
         AccountCorresponding,
         AccountCategoryKind,
         DebitAmount, -- Phát sinh nợ
         DebitAmountOriginal, -- Phát sinh nợ quy đổi
         CreditAmount, -- Phát sinh có
         CreditAmountOriginal, -- Phát sinh có quy đổi
         ClosingDebitAmount, -- Dư Nợ
         ClosingDebitAmountOriginal, -- Dư Nợ quy đổi
         ClosingCreditAmount, -- Dư Có
         ClosingCreditAmountOriginal, -- Dư Có quy đổi
         ORDERType,
         IsBold,
         UnResonableCost,
         OrderNumber /*thứ tự để sắp xếp thu trước/chi sau*/
            , OrderPriority)
        SELECT CAST(GL.ID AS NVARCHAR(MAX)) + '-' + CAST(AC.AccountNumber AS NVARCHAR(20)) AS KeyID,                       --( KeyID ,
               GL.ReferenceID                                                              AS ReferenceID,                 --  ReferenceID ,
               GL.TypeID                                                                   AS TypeID,                      --  TypeID ,
               GL.PostedDate                                                               AS PostedDate,                  --  PostedDate ,
               CASE WHEN @IsFinancialBook = 1 then GL.NoFBook else GL.NoMBook end          as No,                          --  No ,
               GL.Date                                                                     AS Date,                        --  Date ,
               GL.Description                                                              AS JournalMemo,                 --  JournalMemo ,
               AC.AccountNumber                                                            AS AccountNumber,               --  AccountNumber ,
               AC.AccountName                                                              AS AccountName,
               AC.AccountNumber + '-' + CASE @IsVietNamese
                                            WHEN 1 THEN AC.AccountName
                                            ELSE AC.AccountNameEngLish END                 AS AccountNameWithAccountNumber,
               --  AccountName ,
               GL.AccountCorresponding                                                     AS AccountCorresponding,        --  AccountCorresponding ,
               AC.AccountCategoryKind                                                      AS AccountCategoryKind,         --  AccountCategoryKind ,
               GL.DebitAmount                                                              AS DebitAmount,                 --  DebitAmount , -- Phát sinh nợ
               GL.DebitAmountOriginal                                                      AS DebitAmountOriginal,         --  DebitAmountOriginal , -- Phát sinh nợ quy đổi
               GL.CreditAmount                                                             AS CreditAmount,                --  CreditAmount , -- Phát sinh có
               GL.CreditAmountOriginal                                                     AS CreditAmountOriginal,        --  CreditAmountOriginal , -- Phát sinh có quy đổi
               ISNULL(R.ClosingDebitAmount, 0)                                             AS ClosingDebitAmount,          --  ClosingDebitAmount , -- Dư Nợ
               ISNULL(R.ClosingDebitAmountOriginal, 0)                                     AS ClosingDebitAmountOriginal,  --  ClosingDebitAmountOriginal , -- Dư Nợ quy đổi
               ISNULL(R.ClosingCreditAmount, 0)                                            AS ClosingCreditAmount,         --  ClosingCreditAmount , -- Dư Có
               ISNULL(R.ClosingCreditAmountOriginal, 0)                                    AS ClosingCreditAmountOriginal, --  ClosingCreditAmountOriginal , -- Dư Có quy đổi
               1                                                                           as ORDERType,                   --  ORDERType ,
               0                                                                           as IsBold,                      --  IsBold ,
            /*(CASE
                 WHEN GL.UnResonableCost = 1 THEN N'Chi phí không hợp lý'
                 WHEN GL.UnResonableCost = 0
                     THEN N'Chi phí hợp lý'
                 ELSE ''
                END) */
               ''                                                                          AS UnResonableCost,             --OrderNumber /*thứ tự để sắp xếp thu trước/chi sau*/
               CASE
                   WHEN ((GL.Account LIKE N'11%' OR GL.Account LIKE N'15%' AND gl.Account NOT LIKE N'154%')
                       AND GL.DebitAmount <> 0) THEN 0
                   ELSE 1 END                                                              AS OrderNumber
                ,
               GL.OrderPriority
        FROM dbo.GeneralLedger AS GL
                 LEFT JOIN #Result AS R ON (GL.Account = R.AccountNumber
            AND R.KeyID = N'SDDK'
                + GL.Account
            )
                 INNER JOIN #tblAccountNumber AC ON GL.Account LIKE AC.AccountNumberPercent
        WHERE GL.PostedDate BETWEEN @FromDate AND @ToDate
          AND (GL.DebitAmountOriginal <> 0
            OR GL.CreditAmountOriginal <> 0
            OR GL.DebitAmount <> 0
            OR GL.CreditAmount <> 0
            )
          AND case when @CurrencyIDEbOrganizationUnit <> @CurrencyID then GL.CurrencyID else 1 end =
              case when @CurrencyIDEbOrganizationUnit <> @CurrencyID then @CurrencyID else 1 end
          AND GL.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
          and (GL.TypeLedger = 2
            or GL.TypeLedger = case
                                   when @IsFinancialBook = 1 then
                                       0
                                   else
                                       1
                end)
        order by GL.OrderPriority
    ELSE --Cộng gộp các bút toán giống nhau
        INSERT INTO #Result
        (KeyID,
         ReferenceID,
         TypeID,
         PostedDate,
         No,
         Date,
         JournalMemo,
         AccountNumber,
         AccountName,
         AccountNameWithAccountNumber,
         AccountCorresponding,
         AccountCategoryKind,
         DebitAmount,
         DebitAmountOriginal,
         CreditAmount,
         CreditAmountOriginal,
         ClosingDebitAmount, -- Dư Nợ
         ClosingDebitAmountOriginal, -- Dư Nợ quy đổi
         ClosingCreditAmount, -- Dư Có
         ClosingCreditAmountOriginal, -- Dư Có quy đổi
         ORDERType,
         IsBold,
         OrderNumber /*thứ tự để sắp xếp thu trước/chi sau*/
        )
        SELECT RSNS.KeyID,
               RSNS.ReferenceID,
               RSNS.TypeID,
               RSNS.PostedDate,
               RSNS.No,
               RSNS.Date,
               RSNS.JournalMemo,
               RSNS.AccountNumber,
               RSNS.AccountName,
               RSNS.AccountNameWithAccountNumber,
               RSNS.AccountCorresponding,
               RSNS.AccountCategoryKind,
               RSNS.DebitAmount,
               RSNS.DebitAmountOriginal,
               RSNS.CreditAmount,
               RSNS.CreditAmountOriginal,
               RSNS.ClosingDebitAmount,          -- Dư Nợ
               RSNS.ClosingDebitAmountOriginal,  -- Dư Nợ quy đổi
               RSNS.ClosingCreditAmount,         -- Dư Có
               RSNS.ClosingCreditAmountOriginal, -- Dư Có quy đổi
               1 AS ORDERType,
               0 AS IsBold,
               RSNS.OrderNumber /*thứ tự để sắp xếp thu trước/chi sau*/
        FROM (SELECT CAST(MAX(GL.ID) AS NVARCHAR(MAX))
                         + '-' + AC.AccountNumber                                       AS KeyID,
                     GL.ReferenceID                                                     AS ReferenceID,
                     GL.TypeID                                                          AS TypeID,
                     GL.PostedDate                                                      AS PostedDate,
                     CASE WHEN @IsFinancialBook = 1 then GL.NoFBook else GL.NoMBook end as No,
                     GL.Date                                                            AS Date,
                     GL.Reason                                                          AS JournalMemo,
                     AC.AccountNumber                                                   AS AccountNumber,
                     AC.AccountName                                                     AS AccountName,
                     AC.AccountNumber + '-' + CASE @IsVietNamese
                                                  WHEN 1 THEN AC.AccountName
                                                  ELSE AC.AccountNameEngLish END        AS AccountNameWithAccountNumber,
                     GL.AccountCorresponding                                            AS AccountCorresponding,
                     AC.AccountCategoryKind                                             AS AccountCategoryKind,
                     SUM(GL.DebitAmount)                                                AS DebitAmount,
                     SUM(GL.DebitAmountOriginal)                                        AS DebitAmountOriginal,
                     SUM(GL.CreditAmount)                                               AS CreditAmount,
                     SUM(GL.CreditAmountOriginal)                                       AS CreditAmountOriginal,
                     ISNULL(R.ClosingDebitAmount, 0)                                    AS ClosingDebitAmount,
                     ISNULL(R.ClosingDebitAmountOriginal, 0)                            AS ClosingDebitAmountOriginal,
                     ISNULL(R.ClosingCreditAmount, 0)                                   AS ClosingCreditAmount,
                     ISNULL(R.ClosingCreditAmountOriginal, 0)                           AS ClosingCreditAmountOriginal,
                     --Cộng gộp thì set các thông tin bổ sung = NULL
                     CASE
                         WHEN (AC.AccountNumber LIKE N'11%' OR
                               AC.AccountNumber LIKE N'15%' AND AC.AccountNumber NOT LIKE N'154%')
                             AND SUM(GL.DebitAmount) <> 0
                             THEN 0
                         ELSE 1
                         END                                                            AS OrderNumber,
                     MAX(GL.OrderPriority)                                              as OrderPriority
              FROM dbo.GeneralLedger AS GL
                       LEFT JOIN #Result AS R ON (GL.Account = R.AccountNumber
                  AND R.KeyID = N'SDDK'
                      + GL.Account
                  )
                       INNER JOIN #tblAccountNumber AC ON GL.Account LIKE AC.AccountNumberPercent
                   --LEFT JOIN dbo.CustomFieldLedger AS CFL ON GL.RefDetailID = CFL.RefDetailID
                   --                      AND GL.IsPostToManagementBook = CFL.IsPostToManagementBook
              WHERE (GL.PostedDate BETWEEN @FromDate AND @ToDate)
                AND case when @CurrencyIDEbOrganizationUnit <> @CurrencyID then GL.CurrencyID else 1 end =
                    case when @CurrencyIDEbOrganizationUnit <> @CurrencyID then @CurrencyID else 1 end
                AND GL.CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                and (GL.TypeLedger = 2
                  or GL.TypeLedger = case
                                         when @IsFinancialBook = 1 then
                                             0
                                         else
                                             1
                      end)
              GROUP BY GL.ReferenceID,
                       GL.TypeID,
                       GL.PostedDate,
                       CASE WHEN @IsFinancialBook = 1 then GL.NoFBook else GL.NoMBook end,
                       GL.Date,
                       GL.Reason,
                       AC.AccountNumber,
                       AC.AccountName,
                       AC.AccountNameEngLish,
                       GL.AccountCorresponding,
                       AC.AccountCategoryKind,
                       R.ClosingDebitAmount,
                       R.ClosingDebitAmountOriginal,
                       R.ClosingCreditAmount,
                       R.ClosingCreditAmountOriginal
              HAVING SUM(GL.DebitAmountOriginal) <> 0
                  OR SUM(GL.CreditAmountOriginal) <> 0
                  OR SUM(GL.DebitAmount) <> 0
                  OR SUM(GL.CreditAmount) <> 0
             ) AS RSNS
        GROUP BY RSNS.KeyID,
                 RSNS.ReferenceID,
                 RSNS.TypeID,
                 RSNS.PostedDate,
                 RSNS.No,
                 RSNS.Date,
                 RSNS.JournalMemo,
                 RSNS.AccountNumber,
                 RSNS.AccountName,
                 RSNS.AccountNameWithAccountNumber,
                 RSNS.AccountCorresponding,
                 RSNS.AccountCategoryKind,
                 RSNS.DebitAmount,
                 RSNS.DebitAmountOriginal,
                 RSNS.CreditAmount,
                 RSNS.CreditAmountOriginal,
                 RSNS.ClosingDebitAmount,          -- Dư Nợ
                 RSNS.ClosingDebitAmountOriginal,  -- Dư Nợ quy đổi
                 RSNS.ClosingCreditAmount,         -- Dư Có
                 RSNS.ClosingCreditAmountOriginal, -- Dư Có quy đổi
                 RSNS.OrderNumber
        order by MAX(RSNS.OrderPriority)
    -- Thêm dòng tổng cộng
    INSERT #Result
    (KeyID,
     AccountNumber,
     AccountName,
     AccountNameWithAccountNumber,
     AccountCategoryKind,
     JournalMemo,
     OrderType,
     IsBold,
     DebitAmount,
     DebitAmountOriginal,
     CreditAmount,
     CreditAmountOriginal)
    SELECT N'TC' + AccountNumber,
           AccountNumber,
           AccountName,
           AccountNameWithAccountNumber,
           AccountCategoryKind,
           N'Cộng',
           2,
           1,
           SUM(DebitAmount),
           SUM(DebitAmountOriginal),
           SUM(CreditAmount),
           SUM(CreditAmountOriginal)
    FROM #Result
    WHERE OrderType = 1
    GROUP BY AccountNumber,
             AccountName,
             AccountNameWithAccountNumber,
             AccountCategoryKind
    -- Thêm dòng Số dư cuối kỳ

    INSERT #Result
    (KeyID,
     AccountNumber,
     AccountName,
     AccountNameWithAccountNumber,
     AccountCategoryKind,
     JournalMemo,
     OrderType,
     IsBold,
     ClosingDebitAmount, -- Dư Nợ
     ClosingDebitAmountOriginal, -- Dư Nợ quy đổi
     ClosingCreditAmount, -- Dư Có
     ClosingCreditAmountOriginal -- Dư Có quy đổi
    )
    SELECT N'SDCK' + AccountNumber,
           AccountNumber,
           AccountName,
           AccountNameWithAccountNumber,
           AccountCategoryKind,
           N'Số dư cuối kỳ',
           3,
           1,
           CASE
               WHEN AccountCategoryKind = 0 THEN CloseAmount
               WHEN AccountCategoryKind = 1 THEN $0
               ELSE CASE
                        WHEN CloseAmount > 0 THEN CloseAmount
                        ELSE $0
                   END
               END AS DebitBalance,
           CASE
               WHEN AccountCategoryKind = 0 THEN CloseAmountOC
               WHEN AccountCategoryKind = 1 THEN $0
               ELSE CASE
                        WHEN CloseAmountOC > 0
                            THEN CloseAmountOC
                        ELSE $0
                   END
               END AS DebitBalanceOC,
           CASE
               WHEN AccountCategoryKind = 1
                   THEN -1 * CloseAmount
               WHEN AccountCategoryKind = 0 THEN $0
               ELSE CASE
                        WHEN CloseAmount < 0
                            THEN -1 * CloseAmount
                        ELSE $0
                   END
               END AS CreditBalance,
           CASE
               WHEN AccountCategoryKind = 1
                   THEN -1 * CloseAmountOC
               WHEN AccountCategoryKind = 0 THEN $0
               ELSE CASE
                        WHEN CloseAmountOC < 0
                            THEN -1 * CloseAmountOC
                        ELSE $0
                   END
               END AS CreditBalanceOC
    FROM (SELECT AccountNumber,
                 AccountName,
                 AccountNameWithAccountNumber,
                 AccountCategoryKind,
                 (ISNULL(SUM(ClosingDebitAmount
                     - ClosingCreditAmount), 0)
                     + ISNULL(SUM(DebitAmount - CreditAmount),
                              0))                     AS CloseAmount,
                 (ISNULL(SUM(ClosingDebitAmountOriginal
                     - ClosingCreditAmountOriginal), 0)
                     + ISNULL(SUM(DebitAmountOriginal
                         - CreditAmountOriginal), 0)) AS CloseAmountOC
          FROM #Result
          WHERE OrderType IN (0, 2)
          GROUP BY AccountNumber,
                   AccountName,
                   AccountNameWithAccountNumber,
                   AccountCategoryKind
         ) OT


    DECLARE
        @CloseAmountOC AS  DECIMAL(30, 10) ,
        @CloseAmount AS    DECIMAL(30, 10) ,
        @AccountNumber_tmp NVARCHAR(20)
    SELECT @CloseAmountOC = 0,
           @CloseAmount = 0,
           @AccountNumber_tmp = N''

    /*Thêm đoạn này by hoant 22.04.2015 do là các bạn làm trước đã inser theo kiểu ko thể sort để cộng đuổi nên phải chế ra đoạn này*/
    INSERT INTO #Result1
    (KeyID,
     ReferenceID,
     TypeID,
     PostedDate,
     No,
     Date,
     JournalMemo,
     AccountNumber,
     AccountCategoryKind,
     AccountName,
     AccountNameWithAccountNumber,
     AccountCorresponding,
     DebitAmountOriginal,
     DebitAmount,
     CreditAmountOriginal,
     CreditAmount,
     ClosingDebitAmountOriginal,
     ClosingDebitAmount,
     ClosingCreditAmountOriginal,
     ClosingCreditAmount,
     OrderType,
     IsBold,
     UnResonableCost,
     OrderNumber
        , OrderPriority)
    SELECT KeyID,
           ReferenceID,
           TypeID,
           PostedDate,
           No,
           Date,
           JournalMemo,
           AccountNumber,
           AccountCategoryKind,
           AccountName,
           AccountNameWithAccountNumber,
           AccountCorresponding,
           DebitAmountOriginal,
           DebitAmount,
           CreditAmountOriginal,
           CreditAmount,
           ClosingDebitAmountOriginal,
           ClosingDebitAmount,
           ClosingCreditAmountOriginal,
           ClosingCreditAmount,
           OrderType,
           IsBold,
           UnResonableCost,
           OrderNumber
            ,
           OrderPriority
    FROM #Result
    ORDER BY AccountNumber,
             OrderType,
             PostedDate,
             Date,
             OrderNumber,
             No


    UPDATE #Result1
    SET @CloseAmountOC              = (CASE
                                           WHEN OrderType = 0
                                               THEN (CASE
                                                         WHEN ClosingDebitAmountOriginal = 0
                                                             THEN ClosingCreditAmountOriginal
                                                         ELSE -1
                                                             * ISNULL(ClosingDebitAmountOriginal, $0)
                                               END)
                                           WHEN @AccountNumber_tmp <> AccountNumber
                                               THEN ISNULL(CreditAmountOriginal, 0) - ISNULL(DebitAmountOriginal, $0)
                                           ELSE @CloseAmountOC + ISNULL(CreditAmountOriginal, $0)
                                               - ISNULL(DebitAmountOriginal, $0)
        END),
        ClosingDebitAmountOriginal  = (CASE
                                           WHEN AccountCategoryKind = 0
                                               THEN -1 * @CloseAmountOC
                                           WHEN AccountCategoryKind = 1
                                               THEN $0
                                           ELSE CASE
                                                    WHEN @CloseAmountOC < 0
                                                        THEN -1
                                                        * @CloseAmountOC
                                                    ELSE $0
                                               END
            END),
        ClosingCreditAmountOriginal = (CASE
                                           WHEN AccountCategoryKind = 1
                                               THEN @CloseAmountOC
                                           WHEN AccountCategoryKind = 0
                                               THEN $0
                                           ELSE CASE
                                                    WHEN @CloseAmountOC > 0
                                                        THEN @CloseAmountOC
                                                    ELSE $0
                                               END
            END),
        @CloseAmount                = (CASE
                                           WHEN OrderType = 0
                                               THEN (CASE
                                                         WHEN ClosingDebitAmount = 0
                                                             THEN ClosingCreditAmount
                                                         ELSE -1 * ISNULL(ClosingDebitAmount, $0)
                                               END)
                                           WHEN @AccountNumber_tmp <> AccountNumber
                                               THEN ISNULL(CreditAmount, 0) - ISNULL(DebitAmount, $0)
                                           ELSE @CloseAmount + ISNULL(CreditAmount, $0)
                                               - ISNULL(DebitAmount, $0)
            END),
        ClosingDebitAmount          = (CASE
                                           WHEN AccountCategoryKind = 0
                                               THEN -1 * @CloseAmount
                                           WHEN AccountCategoryKind = 1
                                               THEN $0
                                           ELSE CASE
                                                    WHEN @CloseAmount < 0
                                                        THEN -1 * @CloseAmount
                                                    ELSE $0
                                               END
            END),
        ClosingCreditAmount         = (CASE
                                           WHEN AccountCategoryKind = 1
                                               THEN @CloseAmount
                                           WHEN AccountCategoryKind = 0
                                               THEN $0
                                           ELSE CASE
                                                    WHEN @CloseAmount > 0
                                                        THEN @CloseAmount
                                                    ELSE $0
                                               END
            END),
        @AccountNumber_tmp          = AccountNumber
    WHERE OrderType <> 3
      AND OrderType <> 2
    --ORDER BY AccountNumber, OrderType, PostedDate, Date, No

    SELECT GL.RowNum,
           GL.AccountNumber,
           GL.AccountNameWithAccountNumber,
           GL.PostedDate,
           GL.Date,
           GL.No,
           GL.ReferenceID,
           GL.TypeID,
           GL.JournalMemo,
           GL.AccountCorresponding,
           GL.DebitAmountOriginal,
           GL.DebitAmount,
           GL.CreditAmountOriginal,
           GL.CreditAmount,
           GL.ClosingDebitAmount,          -- Dư Nợ
           GL.ClosingDebitAmountOriginal,  -- Dư Nợ quy đổi
           GL.ClosingCreditAmount,         -- Dư Có
           GL.ClosingCreditAmountOriginal, -- Dư Có quy đổi
           CASE
               WHEN GL.OrderType = 1 THEN GL.DebitAmountOriginal
               ELSE 0
               END AS DebitAmountOriginal_OnlyDetail,
           CASE
               WHEN GL.OrderType = 1 THEN GL.DebitAmount
               ELSE 0
               END AS DebitAmount_OnlyDetail,
           CASE
               WHEN GL.OrderType = 1 THEN GL.CreditAmountOriginal
               ELSE 0
               END AS CreditAmountOriginal_OnlyDetail,
           CASE
               WHEN GL.OrderType = 1 THEN GL.CreditAmount
               ELSE 0
               END AS CreditAmount_OnlyDetail,
           GL.IsBold,
           UnResonableCost,
           OrderNumber
    FROM #Result1 GL
    ORDER BY rowNUm

    DROP TABLE #tblAccountNumber
    DROP TABLE #Result
    DROP TABLE #Result1
END
go

