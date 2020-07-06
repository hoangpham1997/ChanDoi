-- =============================================
-- Author:		namnh
-- Create date: 21/02/2020
-- Description:	Sổ quỹ tiền mặt khi in trong phân hệ quỹ
-- Proc_SO_QUY_TIEN_MAT '7EE38755-5D68-0143-8B97-6C3FFB45EB15', '02/01/2020', '02/29/2020','', 0, 1
-- =============================================
    ALTER PROCEDURE [dbo].[Proc_SO_QUY_TIEN_MAT](@CompanyID UNIQUEIDENTIFIER,
    @FromDate DATETIME,
    @ToDate DATETIME,
    @CurrencyID NVARCHAR(3),
    @TypeLedger INT,
    @isDependent BIT)
    -- TypeLedger = 0 - Sổ tài chính
    -- TypeLedger = 1 - Sổ quản trị
    AS
    BEGIN

        CREATE TABLE #Result
        (
            RowNum                   INT IDENTITY (1, 1)
                PRIMARY KEY,
            RefID                    UNIQUEIDENTIFIER,
            PostedDate               DATETIME,
            Date                     DATETIME,
            ReceiptRefNo             NVARCHAR(20) COLLATE SQL_Latin1_General_CP1_CI_AS,
            PaymentRefNo             NVARCHAR(20) COLLATE SQL_Latin1_General_CP1_CI_AS,
            JournalMemo              NVARCHAR(512) COLLATE SQL_Latin1_General_CP1_CI_AS,
            TotalReceiptFBCurrencyID MONEY,
            TotalPaymentFBCurrencyID MONEY,
            ClosingFBCurrencyID      MONEY,
            Note                     NVARCHAR(255) COLLATE SQL_Latin1_General_CP1_CI_AS,
            CAType                   INT, -- 0: Phiếu thu, 1: Phiếu chi
            OrderPriority            int,
            TypeID                   int
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
                  AND Account = '1111'
                  AND CompanyID IN (SELECT CompanyID FROM @tblListCompanyID)
                  AND (DebitAmount > 0 OR CreditAmount > 0)
            END
            -- Trường hợp chọn loại tiền = vàng tiền tệ
        ELSE
            IF (@CurrencyID = 'XAU')
                BEGIN
                    INSERT INTO @tbDataGL
                    SELECT GL.*
                    FROM dbo.GeneralLedger GL
                    WHERE GL.PostedDate <= @ToDate
                      AND Account LIKE '1113'
                      AND CompanyID IN (SELECT CompanyID FROM @tblListCompanyID)
                      AND CurrencyID = @CurrencyID
                      AND (DebitAmountOriginal > 0 OR CreditAmountOriginal > 0)
                END
                -- Loại tiền khác đồng tiền hạch toán
            ELSE
                BEGIN
                    INSERT INTO @tbDataGL
                    SELECT GL.*
                    FROM dbo.GeneralLedger GL
                    WHERE GL.PostedDate <= @ToDate
                      AND Account = '1112'
                      AND CompanyID IN (SELECT CompanyID FROM @tblListCompanyID)
                      AND (DebitAmountOriginal > 0 OR CreditAmountOriginal > 0)
                      AND CurrencyID = @CurrencyID
                END
        -- Xử lý số dư đầu kỳ
        DECLARE @ThuDauKy DECIMAL(25, 4);
        DECLARE @ChiDauKy DECIMAL(25, 4);
        DECLARE @DuDauKy DECIMAL(25, 4);
        IF (@CurrencyID = @MainCurrency)
            BEGIN
                SET @ThuDauKy =
                        (SELECT SUM(DebitAmount) FROM @tbDataGL WHERE DebitAmount > 0 AND PostedDate < @FromDate)
                IF (@ThuDauKy IS NULL)
                    BEGIN
                        SET @ThuDauKy = 0
                    END
                SET @ChiDauKy =
                        (SELECT SUM(CreditAmount) FROM @tbDataGL WHERE CreditAmount > 0 AND PostedDate < @FromDate)
                IF (@ChiDauKy IS NULL)
                    BEGIN
                        SET @ChiDauKy = 0
                    END
                SET @DuDauKy = @ThuDauKy - @ChiDauKy
                IF (@DuDauKy IS NULL)
                    BEGIN
                        SET @DuDauKy = 0
                    END
            END
        ELSE
            BEGIN
                SET @ThuDauKy =
                        (SELECT SUM(DebitAmountOriginal)
                         FROM @tbDataGL
                         WHERE DebitAmountOriginal > 0
                           AND PostedDate < @FromDate)
                IF (@ThuDauKy IS NULL)
                    BEGIN
                        SET @ThuDauKy = 0
                    END
                SET @ChiDauKy =
                        (SELECT SUM(CreditAmountOriginal)
                         FROM @tbDataGL
                         WHERE CreditAmountOriginal > 0
                           AND PostedDate < @FromDate)
                IF (@ChiDauKy IS NULL)
                    BEGIN
                        SET @ChiDauKy = 0
                    END
                SET @DuDauKy = @ThuDauKy - @ChiDauKy
                IF (@DuDauKy IS NULL)
                    BEGIN
                        SET @DuDauKy = 0
                    END
            END

        INSERT INTO #Result (RefID, PostedDate, Date, ReceiptRefNo, PaymentRefNo, JournalMemo,
                             TotalReceiptFBCurrencyID, TotalPaymentFBCurrencyID, ClosingFBCurrencyID, Note,
                             OrderPriority,TypeID)
        SELECT NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               N'Số dư đầu kỳ',
               NULL,
               NULL,
               @DuDauKy,
               NULL,
               NULL,
               NULL
        -- Thêm dữ liệu Thu chi vào #Result
        IF (@TypeLedger = 0)
            BEGIN
                INSERT INTO #Result (RefID, PostedDate, Date, ReceiptRefNo, PaymentRefNo, JournalMemo,
                                     TotalReceiptFBCurrencyID, TotalPaymentFBCurrencyID, ClosingFBCurrencyID, Note,
                                     CAType,
                                     OrderPriority,TypeID)
                SELECT *
                FROM (SELECT ReferenceID   as RefID,
                             PostedDate    as PostedDate,
                             Date          as Date,
                             NoFBook       as ReceiptRefNo,
                             NULL          as PaymentRefNo,
                             Description   as JournalMemo,
                             CASE
                                 WHEN @CurrencyID = @MainCurrency THEN DebitAmount
                                 ELSE DebitAmountOriginal END
                                           as TotalReceiptFBCurrencyID,
                             NULL          as TotalPaymentFBCurrencyID,
                             NULL          as ClosingFBCurrencyID,
                             NULL          as Note,
                             0             as CAType,
                             OrderPriority as OrderPriority,
                             TypeID as TypeID
                      FROM @tbDataGL
                      WHERE (CASE
                                 WHEN @CurrencyID = @MainCurrency THEN DebitAmount
                                 ELSE DebitAmountOriginal END) > 0
                        AND PostedDate >= @FromDate
                        AND PostedDate <= @ToDate
                        AND (TypeLedger = 0 OR TypeLedger = 2)
                      UNION ALL
                      SELECT ReferenceID   as RefID,
                             PostedDate    as PostedDate,
                             Date          as Date,
                             NULL          as ReceiptRefNo,
                             NoFBook       as PaymentRefNo,
                             Description   as JournalMemo,
                             NULL          as TotalReceiptFBCurrencyID,
                             CASE
                                 WHEN @CurrencyID = @MainCurrency THEN CreditAmount
                                 ELSE CreditAmountOriginal END
                                           as TotalPaymentFBCurrencyID,
                             NULL             ClosingFBCurrencyID,
                             NULL          as Note,
                             1             as CAType,
                             OrderPriority as OrderPriority,
                             TypeID as TypeID
                      FROM @tbDataGL
                      WHERE (CASE
                                 WHEN @CurrencyID = @MainCurrency THEN CreditAmount
                                 ELSE CreditAmountOriginal END) > 0
                        AND PostedDate >= @FromDate
                        AND PostedDate <= @ToDate
                        AND (TypeLedger = 0 OR TypeLedger = 2)
                     ) as #rs
                ORDER BY PostedDate, Date, OrderPriority
            END
        ELSE
            BEGIN
                INSERT INTO #Result (RefID, PostedDate, Date, ReceiptRefNo, PaymentRefNo, JournalMemo,
                                     TotalReceiptFBCurrencyID, TotalPaymentFBCurrencyID, ClosingFBCurrencyID, Note,
                                     CAType,
                                     OrderPriority,TypeID)
                SELECT *
                FROM (SELECT ReferenceID   as RefID,
                             PostedDate    as PostedDate,
                             Date          as Date,
                             NoMBook       as ReceiptRefNo,
                             NULL          as PaymentRefNo,
                             Description   as JournalMemo,
                             CASE
                                 WHEN @CurrencyID = @MainCurrency THEN DebitAmount
                                 ELSE DebitAmountOriginal END
                                           as TotalReceiptFBCurrencyID,
                             NULL          as TotalPaymentFBCurrencyID,
                             NULL          as ClosingFBCurrencyID,
                             NULL          as Note,
                             0             as CAType,
                             OrderPriority as OrderPriority,
                             TypeID as TypeID
                      FROM @tbDataGL
                      WHERE (CASE
                                 WHEN @CurrencyID = @MainCurrency THEN DebitAmount
                                 ELSE DebitAmountOriginal END) > 0
                        AND PostedDate > @FromDate
                        AND PostedDate < @ToDate
                        AND (TypeLedger = 1 OR TypeLedger = 2)
                      UNION ALL
                      SELECT ReferenceID   as RefID,
                             PostedDate    as PostedDate,
                             Date          as Date,
                             NULL          as ReceiptRefNo,
                             NoMBook       as PaymentRefNo,
                             Description   as JournalMemo,
                             NULL          as TotalReceiptFBCurrencyID,
                             CASE
                                 WHEN @CurrencyID = @MainCurrency THEN CreditAmount
                                 ELSE CreditAmountOriginal END
                                           as TotalPaymentFBCurrencyID,
                             NULL          as ClosingFBCurrenncyID,
                             NULL          as Note,
                             1             as CAType,
                             OrderPriority as OrderPriority,
                             TypeID as TypeID
                      FROM @tbDataGL
                      WHERE (CASE
                                 WHEN @CurrencyID = @MainCurrency THEN CreditAmount
                                 ELSE CreditAmountOriginal END) > 0
                        AND PostedDate > @FromDate
                        AND PostedDate < @ToDate
                        AND (TypeLedger = 1 OR TypeLedger = 2)
                     ) as #rs
                ORDER BY PostedDate, Date, OrderPriority
            END

        -- Xử lý cột tồn theo luỹ kế
        DECLARE @TEMP DECIMAL(25, 4)
        DECLARE @ROWNUM INT
        DECLARE @COUNT INT = (SELECT COUNT(*) FROM #Result WHERE RowNum > 1)
        DECLARE @CAType INT
        SELECT * into #temp FROM #Result WHERE RowNum > 1 ORDER BY PostedDate, Date, OrderPriority
        WHILE(@COUNT > 0)
            BEGIN
                SET @CAType = (SELECT TOP 1 CAType FROM #temp)
                IF (@CAType = 1)
                    BEGIN
                        SET @TEMP = (SELECT TOP 1 TotalPaymentFBCurrencyID FROM #temp)
                        SET @ROWNUM = (SELECT TOP 1 RowNum FROM #temp)
                        SET @DuDauKy = @DuDauKy - @TEMP
                        UPDATE #Result SET ClosingFBCurrencyID = @DuDauKy WHERE RowNum = @ROWNUM
                        DELETE FROM #temp WHERE RowNum = @ROWNUM
                    END
                ELSE
                    BEGIN
                        SET @TEMP = (SELECT TOP 1 TotalReceiptFBCurrencyID FROM #temp)
                        SET @ROWNUM = (SELECT TOP 1 RowNum FROM #temp)
                        SET @DuDauKy = @DuDauKy + @TEMP
                        UPDATE #Result SET ClosingFBCurrencyID = @DuDauKy WHERE RowNum = @ROWNUM
                        DELETE FROM #temp WHERE RowNum = @ROWNUM
                    END
                SET @COUNT = @COUNT - 1;
            END
        SELECT * from #Result ORDER BY RowNum

    END
go

-- =============================================
-- Author:		namnh
-- Create date: 21/02/2020
-- Description:	Sổ quỹ tiền mặt khi in trong phân hệ quỹ
-- Proc_SO_QUY_TIEN_MAT '7EE38755-5D68-0143-8B97-6C3FFB45EB15', '02/01/2020', '02/29/2020','', 0, 1
-- =============================================
    ALTER PROCEDURE [dbo].[Proc_SO_QUY_TIEN_MAT](@CompanyID UNIQUEIDENTIFIER,
    @FromDate DATETIME,
    @ToDate DATETIME,
    @CurrencyID NVARCHAR(3),
    @TypeLedger INT,
    @isDependent BIT)
    -- TypeLedger = 0 - Sổ tài chính
    -- TypeLedger = 1 - Sổ quản trị
    AS
    BEGIN

        CREATE TABLE #Result
        (
            RowNum                   INT IDENTITY (1, 1)
                PRIMARY KEY,
            RefID                    UNIQUEIDENTIFIER,
            PostedDate               DATETIME,
            Date                     DATETIME,
            ReceiptRefNo             NVARCHAR(20) COLLATE SQL_Latin1_General_CP1_CI_AS,
            PaymentRefNo             NVARCHAR(20) COLLATE SQL_Latin1_General_CP1_CI_AS,
            JournalMemo              NVARCHAR(512) COLLATE SQL_Latin1_General_CP1_CI_AS,
            TotalReceiptFBCurrencyID MONEY,
            TotalPaymentFBCurrencyID MONEY,
            ClosingFBCurrencyID      MONEY,
            Note                     NVARCHAR(255) COLLATE SQL_Latin1_General_CP1_CI_AS,
            CAType                   INT, -- 0: Phiếu thu, 1: Phiếu chi
            OrderPriority            int,
            TypeID                   int
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
                  AND Account = '1111'
                  AND CompanyID IN (SELECT CompanyID FROM @tblListCompanyID)
                  AND (DebitAmount > 0 OR CreditAmount > 0)
            END
            -- Trường hợp chọn loại tiền = vàng tiền tệ
        ELSE
            IF (@CurrencyID = 'XAU')
                BEGIN
                    INSERT INTO @tbDataGL
                    SELECT GL.*
                    FROM dbo.GeneralLedger GL
                    WHERE GL.PostedDate <= @ToDate
                      AND Account LIKE '1113'
                      AND CompanyID IN (SELECT CompanyID FROM @tblListCompanyID)
                      AND CurrencyID = @CurrencyID
                      AND (DebitAmountOriginal > 0 OR CreditAmountOriginal > 0)
                END
                -- Loại tiền khác đồng tiền hạch toán
            ELSE
                BEGIN
                    INSERT INTO @tbDataGL
                    SELECT GL.*
                    FROM dbo.GeneralLedger GL
                    WHERE GL.PostedDate <= @ToDate
                      AND Account = '1112'
                      AND CompanyID IN (SELECT CompanyID FROM @tblListCompanyID)
                      AND (DebitAmountOriginal > 0 OR CreditAmountOriginal > 0)
                      AND CurrencyID = @CurrencyID
                END
        -- Xử lý số dư đầu kỳ
        DECLARE @ThuDauKy DECIMAL(25, 4);
        DECLARE @ChiDauKy DECIMAL(25, 4);
        DECLARE @DuDauKy DECIMAL(25, 4);
        IF (@CurrencyID = @MainCurrency)
            BEGIN
                SET @ThuDauKy =
                        (SELECT SUM(DebitAmount) FROM @tbDataGL WHERE DebitAmount > 0 AND PostedDate < @FromDate)
                IF (@ThuDauKy IS NULL)
                    BEGIN
                        SET @ThuDauKy = 0
                    END
                SET @ChiDauKy =
                        (SELECT SUM(CreditAmount) FROM @tbDataGL WHERE CreditAmount > 0 AND PostedDate < @FromDate)
                IF (@ChiDauKy IS NULL)
                    BEGIN
                        SET @ChiDauKy = 0
                    END
                SET @DuDauKy = @ThuDauKy - @ChiDauKy
                IF (@DuDauKy IS NULL)
                    BEGIN
                        SET @DuDauKy = 0
                    END
            END
        ELSE
            BEGIN
                SET @ThuDauKy =
                        (SELECT SUM(DebitAmountOriginal)
                         FROM @tbDataGL
                         WHERE DebitAmountOriginal > 0
                           AND PostedDate < @FromDate)
                IF (@ThuDauKy IS NULL)
                    BEGIN
                        SET @ThuDauKy = 0
                    END
                SET @ChiDauKy =
                        (SELECT SUM(CreditAmountOriginal)
                         FROM @tbDataGL
                         WHERE CreditAmountOriginal > 0
                           AND PostedDate < @FromDate)
                IF (@ChiDauKy IS NULL)
                    BEGIN
                        SET @ChiDauKy = 0
                    END
                SET @DuDauKy = @ThuDauKy - @ChiDauKy
                IF (@DuDauKy IS NULL)
                    BEGIN
                        SET @DuDauKy = 0
                    END
            END

        INSERT INTO #Result (RefID, PostedDate, Date, ReceiptRefNo, PaymentRefNo, JournalMemo,
                             TotalReceiptFBCurrencyID, TotalPaymentFBCurrencyID, ClosingFBCurrencyID, Note,
                             OrderPriority,TypeID)
        SELECT NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               N'Số dư đầu kỳ',
               NULL,
               NULL,
               @DuDauKy,
               NULL,
               NULL,
               NULL
        -- Thêm dữ liệu Thu chi vào #Result
        IF (@TypeLedger = 0)
            BEGIN
                INSERT INTO #Result (RefID, PostedDate, Date, ReceiptRefNo, PaymentRefNo, JournalMemo,
                                     TotalReceiptFBCurrencyID, TotalPaymentFBCurrencyID, ClosingFBCurrencyID, Note,
                                     CAType,
                                     OrderPriority,TypeID)
                SELECT *
                FROM (SELECT ReferenceID   as RefID,
                             PostedDate    as PostedDate,
                             Date          as Date,
                             NoFBook       as ReceiptRefNo,
                             NULL          as PaymentRefNo,
                             Description   as JournalMemo,
                             CASE
                                 WHEN @CurrencyID = @MainCurrency THEN DebitAmount
                                 ELSE DebitAmountOriginal END
                                           as TotalReceiptFBCurrencyID,
                             NULL          as TotalPaymentFBCurrencyID,
                             NULL          as ClosingFBCurrencyID,
                             NULL          as Note,
                             0             as CAType,
                             OrderPriority as OrderPriority,
                             TypeID as TypeID
                      FROM @tbDataGL
                      WHERE (CASE
                                 WHEN @CurrencyID = @MainCurrency THEN DebitAmount
                                 ELSE DebitAmountOriginal END) > 0
                        AND PostedDate >= @FromDate
                        AND PostedDate <= @ToDate
                        AND (TypeLedger = 0 OR TypeLedger = 2)
                      UNION ALL
                      SELECT ReferenceID   as RefID,
                             PostedDate    as PostedDate,
                             Date          as Date,
                             NULL          as ReceiptRefNo,
                             NoFBook       as PaymentRefNo,
                             Description   as JournalMemo,
                             NULL          as TotalReceiptFBCurrencyID,
                             CASE
                                 WHEN @CurrencyID = @MainCurrency THEN CreditAmount
                                 ELSE CreditAmountOriginal END
                                           as TotalPaymentFBCurrencyID,
                             NULL             ClosingFBCurrencyID,
                             NULL          as Note,
                             1             as CAType,
                             OrderPriority as OrderPriority,
                             TypeID as TypeID
                      FROM @tbDataGL
                      WHERE (CASE
                                 WHEN @CurrencyID = @MainCurrency THEN CreditAmount
                                 ELSE CreditAmountOriginal END) > 0
                        AND PostedDate >= @FromDate
                        AND PostedDate <= @ToDate
                        AND (TypeLedger = 0 OR TypeLedger = 2)
                     ) as #rs
                ORDER BY PostedDate, Date, OrderPriority
            END
        ELSE
            BEGIN
                INSERT INTO #Result (RefID, PostedDate, Date, ReceiptRefNo, PaymentRefNo, JournalMemo,
                                     TotalReceiptFBCurrencyID, TotalPaymentFBCurrencyID, ClosingFBCurrencyID, Note,
                                     CAType,
                                     OrderPriority,TypeID)
                SELECT *
                FROM (SELECT ReferenceID   as RefID,
                             PostedDate    as PostedDate,
                             Date          as Date,
                             NoMBook       as ReceiptRefNo,
                             NULL          as PaymentRefNo,
                             Description   as JournalMemo,
                             CASE
                                 WHEN @CurrencyID = @MainCurrency THEN DebitAmount
                                 ELSE DebitAmountOriginal END
                                           as TotalReceiptFBCurrencyID,
                             NULL          as TotalPaymentFBCurrencyID,
                             NULL          as ClosingFBCurrencyID,
                             NULL          as Note,
                             0             as CAType,
                             OrderPriority as OrderPriority,
                             TypeID as TypeID
                      FROM @tbDataGL
                      WHERE (CASE
                                 WHEN @CurrencyID = @MainCurrency THEN DebitAmount
                                 ELSE DebitAmountOriginal END) > 0
                        AND PostedDate > @FromDate
                        AND PostedDate < @ToDate
                        AND (TypeLedger = 1 OR TypeLedger = 2)
                      UNION ALL
                      SELECT ReferenceID   as RefID,
                             PostedDate    as PostedDate,
                             Date          as Date,
                             NULL          as ReceiptRefNo,
                             NoMBook       as PaymentRefNo,
                             Description   as JournalMemo,
                             NULL          as TotalReceiptFBCurrencyID,
                             CASE
                                 WHEN @CurrencyID = @MainCurrency THEN CreditAmount
                                 ELSE CreditAmountOriginal END
                                           as TotalPaymentFBCurrencyID,
                             NULL          as ClosingFBCurrenncyID,
                             NULL          as Note,
                             1             as CAType,
                             OrderPriority as OrderPriority,
                             TypeID as TypeID
                      FROM @tbDataGL
                      WHERE (CASE
                                 WHEN @CurrencyID = @MainCurrency THEN CreditAmount
                                 ELSE CreditAmountOriginal END) > 0
                        AND PostedDate > @FromDate
                        AND PostedDate < @ToDate
                        AND (TypeLedger = 1 OR TypeLedger = 2)
                     ) as #rs
                ORDER BY PostedDate, Date, OrderPriority
            END

        -- Xử lý cột tồn theo luỹ kế
        DECLARE @TEMP DECIMAL(25, 4)
        DECLARE @ROWNUM INT
        DECLARE @COUNT INT = (SELECT COUNT(*) FROM #Result WHERE RowNum > 1)
        DECLARE @CAType INT
        SELECT * into #temp FROM #Result WHERE RowNum > 1 ORDER BY PostedDate, Date, OrderPriority
        WHILE(@COUNT > 0)
            BEGIN
                SET @CAType = (SELECT TOP 1 CAType FROM #temp)
                IF (@CAType = 1)
                    BEGIN
                        SET @TEMP = (SELECT TOP 1 TotalPaymentFBCurrencyID FROM #temp)
                        SET @ROWNUM = (SELECT TOP 1 RowNum FROM #temp)
                        SET @DuDauKy = @DuDauKy - @TEMP
                        UPDATE #Result SET ClosingFBCurrencyID = @DuDauKy WHERE RowNum = @ROWNUM
                        DELETE FROM #temp WHERE RowNum = @ROWNUM
                    END
                ELSE
                    BEGIN
                        SET @TEMP = (SELECT TOP 1 TotalReceiptFBCurrencyID FROM #temp)
                        SET @ROWNUM = (SELECT TOP 1 RowNum FROM #temp)
                        SET @DuDauKy = @DuDauKy + @TEMP
                        UPDATE #Result SET ClosingFBCurrencyID = @DuDauKy WHERE RowNum = @ROWNUM
                        DELETE FROM #temp WHERE RowNum = @ROWNUM
                    END
                SET @COUNT = @COUNT - 1;
            END
        SELECT * from #Result ORDER BY RowNum

    END
go

