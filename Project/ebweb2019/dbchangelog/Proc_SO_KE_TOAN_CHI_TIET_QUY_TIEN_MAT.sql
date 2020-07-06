-- =============================================
-- Author:		namnh
-- Create date: 26/02/2020
-- Description:	Sổ quỹ tiền mặt khi in trong phân hệ quỹ
-- Proc_SO_KE_TOAN_CHI_TIET_QUY_TIEN_MAT 'DAB6D60B-64B5-8444-B811-BE29A9DC6738', '04/01/2020', '04/24/2020','VND',0, ',1111,1112,1113',1, 0
-- =============================================
    CREATE PROCEDURE [dbo].[Proc_SO_KE_TOAN_CHI_TIET_QUY_TIEN_MAT](@CompanyID UNIQUEIDENTIFIER,
    @FromDate DATETIME,
    @ToDate DATETIME,
    @CurrencyID NVARCHAR(3),
    @TypeLedger INT,
    @ListAccount NVARCHAR(3000),
    @GroupTheSameItem bit,
    @IsDependent bit,
    @typeShowCurrency bit)
    -- TypeLedger = 0 - Sổ tài chính
    -- TypeLedger = 1 - Sổ quản trị
    AS
    BEGIN

        CREATE TABLE #Result
        (
            RowNum               INT IDENTITY (1, 1)
                PRIMARY KEY,
            RefID                UNIQUEIDENTIFIER,
            Date                 DATETIME,
            PostedDate           DATETIME,
            ReceiptRefNo         NVARCHAR(20) COLLATE SQL_Latin1_General_CP1_CI_AS,
            PaymentRefNo         NVARCHAR(20) COLLATE SQL_Latin1_General_CP1_CI_AS,
            JournalMemo          NVARCHAR(512) COLLATE SQL_Latin1_General_CP1_CI_AS,
            Reason               NVARCHAR(512) COLLATE SQL_Latin1_General_CP1_CI_AS,
            Account              NVARCHAR(512) COLLATE SQL_Latin1_General_CP1_CI_AS,
            AccountCorresponding NVARCHAR(512) COLLATE SQL_Latin1_General_CP1_CI_AS,
            PhatSinhNo           MONEY,
            PhatSinhCo           MONEY,
            SoTon                MONEY,
            Note                 NVARCHAR(255) COLLATE SQL_Latin1_General_CP1_CI_AS,
            CAType               INT, -- 0: Phiếu thu, 1: Phiếu chi
            OrderPriority        INT,
            PositionOrder        INT, -- Sắp xếp để lên báo cáo 0: tồn đầu kỳ, 1: Phát sinh trong kỳ 2: Cộng nhóm
            TypeID               INT
        )

        CREATE TABLE #Result2
        (
            RowNum               INT IDENTITY (1, 1)
                PRIMARY KEY,
            RefID                UNIQUEIDENTIFIER,
            Date                 DATETIME,
            PostedDate           DATETIME,
            ReceiptRefNo         NVARCHAR(20) COLLATE SQL_Latin1_General_CP1_CI_AS,
            PaymentRefNo         NVARCHAR(20) COLLATE SQL_Latin1_General_CP1_CI_AS,
            JournalMemo          NVARCHAR(512) COLLATE SQL_Latin1_General_CP1_CI_AS,
            Reason               NVARCHAR(512) COLLATE SQL_Latin1_General_CP1_CI_AS,
            Account              NVARCHAR(512) COLLATE SQL_Latin1_General_CP1_CI_AS,
            AccountCorresponding NVARCHAR(512) COLLATE SQL_Latin1_General_CP1_CI_AS,
            PhatSinhNo           MONEY,
            PhatSinhCo           MONEY,
            SoTon                MONEY,
            Note                 NVARCHAR(255) COLLATE SQL_Latin1_General_CP1_CI_AS,
            CAType               INT, -- 0: Phiếu thu, 1: Phiếu chi
            OrderPriority        INT,
            PositionOrder        INT, -- Sắp xếp để lên báo cáo 0: tồn đầu kỳ, 1: Phát sinh trong kỳ 2: Cộng nhóm
            TypeID               INT
        )


        DECLARE @tbDataGL TABLE
                          (
                              ID                      UNIQUEIDENTIFIER,
                              CompanyID               uniqueidentifier,
                              BranchID                UNIQUEIDENTIFIER,
                              ReferenceID             UNIQUEIDENTIFIER,
                              DetailID                UNIQUEIDENTIFIER,
                              TypeID                  INT,
                              Date                    DATETIME,
                              PostedDate              DATETIME,
                              TypeLedger              INT,
                              NoFBook                 NVARCHAR(25),
                              NoMBook                 NVARCHAR(25),
                              InvoiceSeries           NVARCHAR(25),
                              InvoiceDate             DATETIME,
                              InvoiceNo               NVARCHAR(25),
                              Account                 NVARCHAR(25),
                              AccountCorresponding    NVARCHAR(25),
                              BankAccountDetailID     UNIQUEIDENTIFIER,
                              BankAccount             NVARCHAR(50),
                              BankName                NVARCHAR(512),
                              CurrencyID              NVARCHAR(3),
                              ExchangeRate            DECIMAL(25, 4),
                              DebitAmount             MONEY,
                              DebitAmountOriginal     MONEY,
                              CreditAmount            MONEY,
                              CreditAmountOriginal    MONEY,
                              Reason                  NVARCHAR(512),
                              Description             NVARCHAR(512),
                              AccountingObjectID      UNIQUEIDENTIFIER,
                              AccountingObjectCode    NVARCHAR(25),
                              AccountingObjectName    NVARCHAR(512),
                              AccountingObjectAddress NVARCHAR(512),
                              ContactName             NVARCHAR(512),
                              EmployeeID              UNIQUEIDENTIFIER,
                              EmployeeCode            NVARCHAR(25),
                              EmployeeName            NVARCHAR(512),
                              MaterialGoodsID         UNIQUEIDENTIFIER,
                              MaterialGoodsCode       NVARCHAR(25),
                              MaterialGoodsName       NVARCHAR(512),
                              RepositoryID            UNIQUEIDENTIFIER,
                              RepositoryCode          NVARCHAR(25),
                              RepositoryName          NVARCHAR(512),
                              UnitID                  UNIQUEIDENTIFIER,
                              Quantity                DECIMAL(25, 4),
                              UnitPrice               MONEY,
                              UnitPriceOriginal       MONEY,
                              MainUnitID              UNIQUEIDENTIFIER,
                              MainQuantity            DECIMAL(25, 4),
                              MainUnitPrice           MONEY,
                              MainConvertRate         DECIMAL(25, 4),
                              Formula                 NVARCHAR(25),
                              DepartmentID            UNIQUEIDENTIFIER,
                              ExpenseItemID           UNIQUEIDENTIFIER,
                              BudgetItemID            UNIQUEIDENTIFIER,
                              CostSetID               UNIQUEIDENTIFIER,
                              ContractID              UNIQUEIDENTIFIER,
                              StatisticsCodeID        UNIQUEIDENTIFIER,
                              RefDateTime             DATETIME,
                              OrderPriority           INT
                          )

        DECLARE @tblListAccount TABLE
                                (
                                    Account NVARCHAR(25)
                                )

        INSERT INTO @tblListAccount
        SELECT TG.AccountNumber
        FROM AccountList AS TG
                 INNER JOIN Func_ConvertStringIntoTable_Nvarchar(@ListAccount, ',') AS Account
                            ON TG.AccountNumber = Account.Value
        WHERE Account.Value IS NOT NULL
          AND CompanyID = @CompanyID

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


        DECLARE @MainCurrency NVARCHAR(3) = (SELECT CurrencyID FROM EbOrganizationUnit WHERE ID = @CompanyID)
        -- Lấy dữ liệu vào bảng theo điêu kiện loại tiền
        IF (@CurrencyID = @MainCurrency)
            BEGIN
                INSERT INTO @tbDataGL
                SELECT GL.*
                FROM dbo.GeneralLedger GL
                WHERE GL.PostedDate <= @ToDate
                  AND Account IN (SELECT * from @tblListAccount)
                  AND CompanyID IN (SELECT CompanyID FROM @tblListCompanyID)
                  AND (DebitAmount > 0 OR CreditAmount > 0)
                  AND (TypeLedger = 2 OR TypeLedger = @TypeLedger)
                ORDER BY CASE WHEN @TypeLedger = 0 THEN NoFBook ELSE NoMBook END
            END
            -- Trường hợp chọn loại tiền khác đồng tiền hạch toán
        ELSE
            BEGIN
                INSERT INTO @tbDataGL
                SELECT GL.*
                FROM dbo.GeneralLedger GL
                WHERE GL.PostedDate <= @ToDate
                  AND Account IN (SELECT * from @tblListAccount)
                  AND CompanyID IN (SELECT CompanyID FROM @tblListCompanyID)
                  AND ((CASE WHEN @typeShowCurrency = 1 THEN DebitAmount ELSE DebitAmountOriginal END) > 0 OR
                       (CASE WHEN @typeShowCurrency = 1 THEN CreditAmount ELSE CreditAmountOriginal END) > 0)
                  AND CurrencyID = @CurrencyID
                  AND (TypeLedger = 2 OR TypeLedger = @TypeLedger)
                ORDER BY CASE WHEN @TypeLedger = 0 THEN NoFBook ELSE NoMBook END
            END

        -- Lấy dữ liệu số dư đầu kì của từng tài khoản
        SELECT * into #tempAccount FROM @tblListAccount group by Account
        DECLARE @CountAcc INT = (SELECT COUNT(*) FROM #tempAccount)
        DECLARE @Account NVARCHAR(25)
        DECLARE @ThuDauKy MONEY
        DECLARE @ChiDauKy MONEY
        DECLARE @SoTonDauKy MONEY
        WHILE(@COUNTACC > 0)
            BEGIN
                SET @Account = (SELECT TOP 1 Account FROM #tempAccount)
                SET @ThuDauKy =
                        (SELECT SUM(CASE WHEN @typeShowCurrency = 1 THEN DebitAmount ELSE DebitAmountOriginal END)
                         FROM @tbDataGL
                         WHERE PostedDate < @FromDate
                           AND (CASE WHEN @typeShowCurrency = 1 THEN DebitAmount ELSE DebitAmountOriginal END) > 0
                           AND Account = @Account)
                SET @ChiDauKy = (SELECT SUM(CASE
                                                WHEN @typeShowCurrency = 1 THEN CreditAmount
                                                ELSE CreditAmountOriginal END)
                                 FROM @tbDataGL
                                 WHERE PostedDate < @FromDate
                                   AND (CASE
                                            WHEN @typeShowCurrency = 1 THEN CreditAmount
                                            ELSE CreditAmountOriginal END) > 0
                                   AND Account = @Account)
                SET @SoTonDauKy = (CASE WHEN @ThuDauKy <> 0 THEN @ThuDauKy ELSE 0 END) -
                                  (CASE WHEN @ChiDauKy <> 0 THEN @ChiDauKy ELSE 0 END)
                INSERT INTO #Result (RefID, Date, PostedDate, ReceiptRefNo, PaymentRefNo, JournalMemo, Reason, Account,
                                     AccountCorresponding, PhatSinhNo, PhatSinhCo, SoTon, Note, CAType, OrderPriority,
                                     PositionOrder, TypeID)
                SELECT NULL,
                       NULL,
                       NULL,
                       NULL,
                       NULL,
                       N'Số tồn đầu kỳ',
                       N'Số tồn đầu kỳ',
                       @Account,
                       NULL,
                       NULL,
                       NULL,
                       @SoTonDauKy,
                       NULL,
                       NULL,
                       NULL,
                       0,
                       NULL
                DELETE FROM #tempAccount WHERE Account = @Account
                SET @CountAcc = @CountAcc - 1
            END


        -- Thêm dữ liệu phát sinh trong kỳ
        INSERT INTO #Result (RefID, Date, PostedDate, ReceiptRefNo, PaymentRefNo, JournalMemo, Reason, Account,
                             AccountCorresponding, PhatSinhNo, PhatSinhCo, SoTon, Note, CAType, OrderPriority,
                             PositionOrder, TypeID)
        SELECT *
        FROM (SELECT ReferenceID                                                                           as RefID,
                     Date                                                                                  as Date,
                     PostedDate                                                                            as PostedDate,
                     CASE WHEN @TypeLedger = 0 THEN NoFBook ELSE NoMBook END                               as ReceiptRefNo,
                     NULL                                                                                  as PaymentRefNo,
                     Description                                                                           as JournalMemo,
                     Reason                                                                                as Reason,
                     Account                                                                               as Account,
                     AccountCorresponding                                                                  as AccountCorresponding,
                     (CASE WHEN @typeShowCurrency = 1 THEN DebitAmount ELSE DebitAmountOriginal END) as SoTienThu,
                     NULL                                                                                  as SoTienChi,
                     NULL                                                                                  as SoTon,
                     NULL                                                                                  as Note,
                     0                                                                                     as CAType,
                     OrderPriority                                                                         as OrderPriority,
                     1                                                                                     as PositionOrder,
                     TypeID                                                                                as TypeID
              FROM @tbDataGL
              WHERE (CASE WHEN @typeShowCurrency = 1 THEN DebitAmount ELSE DebitAmountOriginal END) > 0
                AND PostedDate >= @FromDate
                AND PostedDate <= @ToDate
              UNION ALL
              SELECT ReferenceID                                             as RefID,
                     Date                                                    as Date,
                     PostedDate                                              as PostedDate,
                     NULL                                                    as ReceiptRefNo,
                     CASE WHEN @TypeLedger = 0 THEN NoFBook ELSE NoMBook END as PaymentRefNo,
                     Description                                             as JournalMemo,
                     Reason                                                  as Reason,
                     Account                                                 as Account,
                     AccountCorresponding                                    as AccountCorresponding,
                     NULL                                                    as SoTienThu,
                     (CASE
                          WHEN @typeShowCurrency = 1 THEN CreditAmount
                          ELSE CreditAmountOriginal END)                     as SoTienChi,
                     NULL                                                    as SoTon,
                     NULL                                                    as Note,
                     1                                                       as CAType,
                     OrderPriority                                           as OrderPriority,
                     1                                                       as PositionOrder,
                     TypeID                                                  as TypeID
              FROM @tbDataGL
              WHERE (CASE
                         WHEN @typeShowCurrency = 1 THEN CreditAmount
                         ELSE CreditAmountOriginal END) > 0
                AND PostedDate >= @FromDate
                AND PostedDate <= @ToDate
             ) as #RS
        Order by Account, PostedDate, Date, OrderPriority, PositionOrder

        -- Check cộng gộp bút toán hay không
        IF (@GroupTheSameItem = 1)
            BEGIN
                INSERT INTO #Result2 (RefID, Date, PostedDate, ReceiptRefNo, PaymentRefNo, JournalMemo, Reason, Account,
                                      AccountCorresponding, PhatSinhNo, PhatSinhCo, SoTon, Note, CAType, OrderPriority,
                                      PositionOrder, TypeID)
                SELECT RefID,
                       Date,
                       PostedDate,
                       ReceiptRefNo,
                       PaymentRefNo,
                       Reason,
                       Reason,
                       Account,
                       AccountCorresponding,
                       SUM(PhatSinhNo),
                       SUM(PhatSinhCo),
                       SoTon,
                       Note,
                       CAType,
                       MAX(OrderPriority) as OrderPriority,
                       PositionOrder,
                       TypeID
                from #Result
                Group BY RefID, Date, PostedDate, ReceiptRefNo, PaymentRefNo, Reason, Account, AccountCorresponding,
                         SoTon, Note, CAType, PositionOrder, TypeID
                Order by Account, PostedDate, Date, OrderPriority, PositionOrder
            END
        ELSE
            BEGIN
                INSERT INTO #Result2 (RefID, Date, PostedDate, ReceiptRefNo, PaymentRefNo, JournalMemo, Reason, Account,
                                      AccountCorresponding, PhatSinhNo, PhatSinhCo, SoTon, Note, CAType, OrderPriority,
                                      PositionOrder, TypeID)
                SELECT RefID,
                       Date,
                       PostedDate,
                       ReceiptRefNo,
                       PaymentRefNo,
                       JournalMemo,
                       Reason,
                       Account,
                       AccountCorresponding,
                       PhatSinhNo,
                       PhatSinhCo,
                       SoTon,
                       Note,
                       CAType,
                       OrderPriority,
                       PositionOrder,
                       TypeID
                from #Result
                Order by Account, PostedDate, Date, OrderPriority, PositionOrder
            END
        -- Xử lý cột tồn theo luỹ kế
        DECLARE @ClosingQuantity AS DECIMAL(25, 8) ,
            @ClosingAmount AS DECIMAL(25, 8) ,
            @AccountNumber NVARCHAR(25)
        SELECT @ClosingAmount = 0,
               @ClosingQuantity = 0,
               @AccountNumber = ''

        UPDATE #Result2
        SET @ClosingQuantity = (CASE
                                    WHEN RefID IS NULL THEN SoTon
                                    WHEN @AccountNumber <> Account
                                        THEN ISNULL(PhatSinhNo, 0) - ISNULL(PhatSinhCo, 0)
                                    ELSE @ClosingQuantity + ISNULL(PhatSinhNo, 0) - ISNULL(PhatSinhCo, 0)
            END),
            SoTon            = @ClosingQuantity,
            @AccountNumber   = Account
        WHERE 1 = 1;
        --Thêm cộng nhóm vào cuối mỗi tài khoản
        SELECT Account into #temp3 FROM #Result2 GROUP BY Account
        SET @CountAcc = (SELECT COUNT(*) FROM (SELECT DISTINCT Account FROM #Result2) as #RS)
        WHILE (@CountAcc > 0)
            BEGIN
                SET @Account = (SELECT TOP 1 Account FROM #temp3)
                INSERT INTO #Result2 (RefID, Date, PostedDate, ReceiptRefNo, PaymentRefNo, JournalMemo, Reason, Account,
                                      AccountCorresponding, PhatSinhNo, PhatSinhCo, SoTon, Note, CAType, OrderPriority,
                                      PositionOrder, TypeID)
                SELECT NULL,
                       NULL,
                       NULL,
                       NULL,
                       NULL,
                       N'Cộng nhóm',
                       NULL,
                       @Account,
                       NULL,
                       SUM(PhatSinhNo),
                       SUM(PhatSinhCo),
                       (SELECT SoTon
                        From #Result2
                        WHERE RowNum = (SELECT MAX(RowNum) FROM #Result2 WHERE Account = @Account)),
                       NULL,
                       NULL,
                       NULL,
                       2,
                       NULL
                FROM #Result2
                WHERE
                   Account = @Account
                group by Account
                DELETE FROM #temp3 WHERE Account = @Account
                PRINT @CountAcc
                SET @CountAcc = @CountAcc - 1
            END
        SELECT *
        FROM #Result2
        Order by Account, PositionOrder, PostedDate, Date, OrderPriority
    END
go

