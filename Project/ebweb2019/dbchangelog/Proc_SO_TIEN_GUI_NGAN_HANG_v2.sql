-- =============================================
-- Author:		namnh
-- Create date: 26/02/2020
-- Description:	Sổ tiền gửi ngân hàng
-- Proc_SO_TIEN_GUI_NGAN_HANG 'DAB6D60B-64B5-8444-B811-BE29A9DC6738', '02/01/2020', '03/04/2020',',1121,1122,1123,','VND',',3a0ed558-d852-4e48-adfd-5e07a9a4f903,26096b61-79aa-b444-99ef-63b588b3b0f5,e98d8554-8feb-7a42-9e7f-6e3d4a36d32e,12c3f4dd-d22c-8242-84bf-cd3ef6d5aea1,',1,0,1
-- =============================================
    ALTER PROCEDURE [dbo].[Proc_SO_TIEN_GUI_NGAN_HANG](@CompanyID UNIQUEIDENTIFIER,
    @FromDate DATETIME,
    @ToDate DATETIME,
    @ListAccountNumber NVARCHAR(128),
    @CurrencyID NVARCHAR(3),
    @ListBankAccountDetailID NVARCHAR(3000),
    @GroupTheSameItem bit,
    @TypeLedger INT,
    @IsDependent bit)
    AS
    BEGIN


        CREATE TABLE #Temp
        (
            RowNum               INT IDENTITY (1, 1)
                PRIMARY KEY,
            BankAccountDetailID  UNIQUEIDENTIFIER,
            BankAccount          NVARCHAR(50),
            BankName             NVARCHAR(512),
            PostedDate           DATETIME,
            Date                 DATETIME,
            No                   NVARCHAR(25),
            JournalMemo          NVARCHAR(512),
            Reason               NVARCHAR(512),
            AccountCorresponding NVARCHAR(25),
            SoThu                MONEY,
            SoChi                MONEY,
            SoTon                MONEY,
            OrderPriority        INT,
            PositionOrder        INT, -- Sắp xếp để lên báo cáo 0: Thông tin ngân hàng, 1: Số dư đầu kỳ, 2: Phát sinh trong kỳ, 3: Cộng nhóm
            TypeID               INT,
            RefID                UNIQUEIDENTIFIER
        )

        CREATE TABLE #Temp2
        (
            RowNum               INT IDENTITY (1, 1)
                PRIMARY KEY,
            BankAccountDetailID  UNIQUEIDENTIFIER,
            BankAccount          NVARCHAR(50),
            BankName             NVARCHAR(512),
            PostedDate           DATETIME,
            Date                 DATETIME,
            No                   NVARCHAR(25),
            JournalMemo          NVARCHAR(512),
            Reason               NVARCHAR(512),
            AccountCorresponding NVARCHAR(25),
            SoThu                MONEY,
            SoChi                MONEY,
            SoTon                MONEY,
            OrderPriority        INT,
            PositionOrder        INT, -- Sắp xếp để lên báo cáo 0: Thông tin ngân hàng, 1: Số dư đầu kỳ, 2: Phát sinh trong kỳ, 3: Cộng nhóm
            TypeID               INT,
            RefID                UNIQUEIDENTIFIER
        )

        CREATE TABLE #Result
        (
            RowNum               INT IDENTITY (1, 1)
                PRIMARY KEY,
            BankAccountDetailID  UNIQUEIDENTIFIER,
            BankAccount          NVARCHAR(50),
            BankName             NVARCHAR(512),
            PostedDate           DATETIME,
            Date                 DATETIME,
            No                   NVARCHAR(25),
            JournalMemo          NVARCHAR(512),
            Reason               NVARCHAR(512),
            AccountCorresponding NVARCHAR(25),
            SoThu                MONEY,
            SoChi                MONEY,
            SoTon                MONEY,
            OrderPriority        INT,
            PositionOrder        INT, -- Sắp xếp để lên báo cáo 0: Thông tin ngân hàng, 1: Số dư đầu kỳ, 2: Phát sinh trong kỳ, 3: Cộng nhóm
            TypeID               INT,
            RefID                UNIQUEIDENTIFIER
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

        DECLARE @tblListBankAccountDetail TABLE
                                          (
                                              BankAccountDetailID UNIQUEIDENTIFIER
                                          )

        INSERT INTO @tblListBankAccountDetail
        SELECT TG.ID
        FROM BankAccountDetail AS TG
                 INNER JOIN Func_ConvertStringIntoTable_Nvarchar(@ListBankAccountDetailID, ',') AS BankAccountDetail
                            ON TG.ID = BankAccountDetail.Value
        WHERE BankAccountDetail.Value IS NOT NULL
          AND CompanyID = @CompanyID

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
                  AND Account IN (SELECT AccountNumber FROM @tblListAccountNumber)
                  AND CompanyID IN (SELECT CompanyID FROM @tblListCompanyID)
                  AND (DebitAmount > 0 OR CreditAmount > 0)
                  AND (TypeLedger = 2 OR TypeLedger = @TypeLedger)
                  AND BankAccountDetailID IN (SELECT BankAccountDetailID FROM @tblListBankAccountDetail)
                ORDER BY OrderPriority
            END
        ELSE
            BEGIN
                INSERT INTO @tbDataGL
                SELECT GL.*
                FROM dbo.GeneralLedger GL
                WHERE GL.PostedDate <= @ToDate
                  AND Account IN (SELECT AccountNumber FROM @tblListAccountNumber)
                  AND CompanyID IN (SELECT CompanyID FROM @tblListCompanyID)
                  AND (DebitAmountOriginal > 0 OR CreditAmountOriginal > 0)
                  AND (TypeLedger = 2 OR TypeLedger = @TypeLedger)
                  AND BankAccountDetailID IN (SELECT BankAccountDetailID FROM @tblListBankAccountDetail)
                  AND CurrencyID = @CurrencyID
                ORDER BY OrderPriority
            END


        INSERT INTO #Temp2 (BankAccountDetailID, BankAccount, BankName, PostedDate, Date, No, JournalMemo, Reason,
                            AccountCorresponding, SoThu, SoChi, SoTon, OrderPriority, PositionOrder, TypeID, RefID)
        SELECT *
        FROM (SELECT BankAccountDetailID                                                                 as BankAccountDetailID,
                     BankAccount                                                                         as BankAccount,
                     BankName                                                                            as BankName,
                     PostedDate                                                                          as PostedDate,
                     Date                                                                                as Date,
                     (CASE WHEN @TypeLedger = 0 THEN NoFBook ELSE NoMBook END)                           as No,
                     Description                                                                         as JournalMemo,
                     Reason                                                                              as Reason,
                     AccountCorresponding                                                                as AccounntCorresponding,
                     CASE WHEN @CurrencyID = @MainCurrency THEN DebitAmount ELSE DebitAmountOriginal END as SoThu,
                     NULL                                                                                as SoChi,
                     NULL                                                                                as SoTon,
                     OrderPriority                                                                       as OrderPriority,
                     2                                                                                   as PositionOrder,
                     TypeID                                                                              as TypeID,
                     ReferenceID                                                                         as RefID
              FROM @tbDataGL
              WHERE PostedDate >= @FromDate
                AND PostedDate <= @ToDate
                AND (CASE WHEN @CurrencyID = @MainCurrency THEN DebitAmount ELSE DebitAmountOriginal END) > 0
              UNION ALL
              SELECT BankAccountDetailID                                                                   as BankAccountDetailID,
                     BankAccount                                                                           as BankAccount,
                     BankName                                                                              as BankName,
                     PostedDate                                                                            as PostedDate,
                     Date                                                                                  as Date,
                     (CASE WHEN @TypeLedger = 0 THEN NoFBook ELSE NoMBook END)                             as No,
                     Description                                                                           as JournalMemo,
                     Reason                                                                                as Reason,
                     AccountCorresponding                                                                  as AccounntCorresponding,
                     NULL                                                                                  as SoThu,
                     CASE WHEN @CurrencyID = @MainCurrency THEN CreditAmount ELSE CreditAmountOriginal END as SoChi,
                     NULL                                                                                  as SoTon,
                     OrderPriority                                                                         as OrderPriority,
                     2                                                                                     as PositionOrder,
                     TypeID                                                                                as TypeID,
                     ReferenceID                                                                           as RefID
              FROM @tbDataGL
              WHERE PostedDate >= @FromDate
                AND PostedDate <= @ToDate
                AND (CASE
                         WHEN @CurrencyID = @MainCurrency THEN CreditAmount
                         ELSE CreditAmountOriginal END) > 0) as #RS
        ORDER BY OrderPriority

        IF (@GroupTheSameItem = 0)
            BEGIN
                INSERT INTO #Temp (BankAccountDetailID, BankAccount, BankName, PostedDate, Date, No, JournalMemo,
                                   Reason,
                                   AccountCorresponding, SoThu, SoChi, SoTon, OrderPriority, PositionOrder, TypeID,
                                   RefID)
                SELECT BankAccountDetailID,
                       BankAccount,
                       BankName,
                       PostedDate,
                       Date,
                       No,
                       JournalMemo,
                       Reason,
                       AccountCorresponding,
                       SoThu,
                       SoChi,
                       SoTon,
                       OrderPriority,
                       PositionOrder,
                       TypeID,
                       RefID
                from #Temp2
                ORDER BY BankAccountDetailID, PositionOrder, PostedDate, Date, No, OrderPriority
            END
        ELSE
            BEGIN
                INSERT INTO #Temp (BankAccountDetailID, BankAccount, BankName, PostedDate, Date, No, JournalMemo,
                                   Reason,
                                   AccountCorresponding, SoThu, SoChi, SoTon, OrderPriority, PositionOrder, TypeID,
                                   RefID)
                SELECT BankAccountDetailID,
                       BankAccount,
                       BankName,
                       PostedDate,
                       Date,
                       No,
                       Reason,
                       Reason,
                       AccountCorresponding,
                       SUM(SoThu)         as SoThu,
                       SUM(SoChi)         as SoChi,
                       SUM(SoTon)         as SoTon,
                       MAX(OrderPriority) as OrderPriority,
                       PositionOrder,
                       TypeID,
                       RefID
                from #Temp2
                GROUP BY BankAccountDetailID, BankAccount, BankName, PostedDate, Date, No, Reason,
                         AccountCorresponding, PositionOrder, TypeID, RefID
                ORDER BY BankAccountDetailID, PositionOrder, PostedDate, Date, No, OrderPriority
            END

        DECLARE @BankAccountDetailID UNIQUEIDENTIFIER
        DECLARE @COUNT INT = (SELECT COUNT(*)
                              FROM (SELECT DISTINCT(BankAccountDetailID) FROM @tblListBankAccountDetail) as #listBank)
        SELECT DISTINCT (BankAccountDetailID) as BankAccountDetailID
        into #tempBankAccountDetail
        FROM @tblListBankAccountDetail
        DECLARE @tempBankAccountDetailID UNIQUEIDENTIFIER, @SoThu MONEY, @SoChi MONEY, @SoTon MONEY
        WHILE(@COUNT > 0)
            BEGIN
                SET @BankAccountDetailID = (SELECT TOP 1 BankAccountDetailID FROM #tempBankAccountDetail)
                INSERT INTO #Temp (BankAccountDetailID, BankAccount, BankName, PostedDate, Date, No, JournalMemo,
                                   AccountCorresponding, SoThu, SoChi, SoTon, OrderPriority, PositionOrder, TypeID,
                                   RefID)
                SELECT *
                FROM (SELECT BankAccountDetailID                                     as BankAccountDetail,
                             BankAccount                                             as BankAccount,
                             BankName                                                as BankName,
                             NULL                                                    as PostedDate,
                             NULL                                                    as Date,
                             NULL                                                    as No,
                             N'Tài khoản ngân hàng: ' + BankAccount + ' ' + BankName as JournalMemo,
                             NULL                                                    as AccountCorresponding,
                             NULL                                                    as SoThu,
                             NULL                                                    as SoChi,
                             NULL                                                    as Soton,
                             NULL                                                    as OrderPriority,
                             0                                                       as PositionOrder,
                             NULL                                                    as TypeID,
                             NULL                                                    as RefID
                      FROM @tbDataGL
                      WHERE BankAccountDetailID = @BankAccountDetailID
                      GROUP BY BankAccountDetailID, BankAccount, BankName
                      UNION ALL
                      SELECT BankAccountDetailID as BankAccountDetail,
                             BankAccount         as BankAccount,
                             BankName            as BankName,
                             NULL                as PostedDate,
                             NULL                as Date,
                             NULL                as No,
                             N'Số dư đầu kỳ'     as JournalMemo,
                             NULL                as AccountCorresponding,
                             NULL                as SoThu,
                             NULL                as SoChi,
                             NULL                as Soton,
                             NULL                as OrderPriority,
                             1                   as PositionOrder,
                             NULL                as TypeID,
                             NULL                as RefID
                      FROM @tbDataGL a
                      WHERE BankAccountDetailID = @BankAccountDetailID
                      GROUP BY BankAccountDetailID, BankAccount, BankName
                      UNION ALL
                      SELECT BankAccountDetailID                           as BankAccountDetail,
                             BankAccount                                   as BankAccount,
                             BankName                                      as BankName,
                             NULL                                          as PostedDate,
                             NULL                                          as Date,
                             NULL                                          as No,
                             N'Cộng nhóm: ' + BankAccount + ' ' + BankName as JournalMemo,
                             NULL                                          as AccountCorresponding,
                             NULL                                          as SoThu,
                             NULL                                          as SoChi,
                             NULL                                          as Soton,
                             NULL                                          as OrderPriority,
                             3                                             as PositionOrder,
                             NULL                                          as TypeID,
                             NULL                                          as RefID
                      FROM @tbDataGL a
                      WHERE BankAccountDetailID = @BankAccountDetailID
                      GROUP BY BankAccountDetailID, BankAccount, BankName) as #RS
                UPDATE #Temp
                SET SoTon = ISNULL(CASE
                                       WHEN @CurrencyID = @MainCurrency THEN (ISNULL((SELECT SUM(DebitAmount)
                                                                                      FROM @tbDataGL
                                                                                      WHERE BankAccountDetailID = @BankAccountDetailID
                                                                                        AND PostedDate < @FromDate
                                                                                        AND DebitAmount > 0), 0) -
                                                                              ISNULL((SELECT SUM(CreditAmount)
                                                                                      FROM @tbDataGL
                                                                                      WHERE BankAccountDetailID = @BankAccountDetailID
                                                                                        AND PostedDate < @FromDate
                                                                                        AND CreditAmount > 0), 0))
                                       ELSE (ISNULL((SELECT SUM(DebitAmountOriginal)
                                                     FROM @tbDataGL
                                                     WHERE BankAccountDetailID = @BankAccountDetailID
                                                       AND PostedDate < @FromDate
                                                       AND DebitAmountOriginal > 0), 0) -
                                             ISNULL((SELECT SUM(CreditAmountOriginal)
                                                     FROM @tbDataGL
                                                     WHERE BankAccountDetailID = @BankAccountDetailID
                                                       AND PostedDate < @FromDate
                                                       AND CreditAmountOriginal > 0), 0)) END, 0)
                WHERE PositionOrder = 1
                  AND BankAccountDetailID = @BankAccountDetailID
                DELETE FROM #tempBankAccountDetail WHERE BankAccountDetailID = @BankAccountDetailID
                SET @COUNT = @COUNT - 1
            END

        INSERT INTO #Result(BankAccountDetailID, BankAccount, BankName, PostedDate, Date, No, JournalMemo,
                            AccountCorresponding, SoThu, SoChi, SoTon, OrderPriority, PositionOrder, TypeID, RefID)
        SELECT BankAccountDetailID,
               BankAccount,
               BankName,
               PostedDate,
               Date,
               No,
               JournalMemo,
               AccountCorresponding,
               SoThu,
               SoChi,
               SoTon,
               OrderPriority,
               PositionOrder,
               TypeID,
               RefID
        from #Temp
        ORDER by BankAccountDetailID, PositionOrder, PostedDate, Date, No, OrderPriority

        -- Xử lý cột tồn theo luỹ kế
        DECLARE @ClosingQuantity AS DECIMAL(25, 8) ,
            @ClosingAmount AS DECIMAL(25, 8) ,
            @BankAccountDetailID2 UNIQUEIDENTIFIER
        SELECT @ClosingAmount = 0,
               @ClosingQuantity = 0,
               @BankAccountDetailID2 = NEWID()

        UPDATE #Result
        SET @ClosingQuantity      = (CASE
                                         WHEN PositionOrder = 1 THEN SoTon
                                         WHEN @BankAccountDetailID2 <> BankAccountDetailID
                                             THEN ISNULL(SoThu, 0) - ISNULL(SoChi, 0)
                                         ELSE @ClosingQuantity + ISNULL(SoThu, 0) - ISNULL(SoChi, 0)
            END),
            SoTon                 = @ClosingQuantity,
            @BankAccountDetailID2 = BankAccountDetailID
        WHERE PositionOrder > 0
          AND PositionOrder < 3

        SET @COUNT = (SELECT COUNT(*) FROM (SELECT DISTINCT(BankAccountDetailID) FROM #Temp) as #listBank)
        DECLARE @SoTonCuoi MONEY, @BankAccount NVARCHAR(25), @BankName NVARCHAR(512)
        WHILE(@COUNT > 0)
            BEGIN
                SET @BankAccountDetailID2 = (SELECT TOP 1 BankAccountDetailID FROM #Temp)
                SET @SoTonCuoi = (SELECT SoTon
                                  FROM #Result
                                  WHERE RowNum = (SELECT MAX(RowNum)
                                                  FROM #Result
                                                  WHERE BankAccountDetailID = @BankAccountDetailID2
                                                    AND PositionOrder < 3))

                UPDATE #Result
                SET SoThu = ISNULL((SELECT SUM(SoThu)
                                    FROM #Result
                                    WHERE PositionOrder = 2
                                      AND BankAccountDetailID = @BankAccountDetailID2), 0),
                    SoChi = ISNULL((SELECT SUM(SoChi)
                                    FROM #Result
                                    WHERE PositionOrder = 2
                                      AND BankAccountDetailID = @BankAccountDetailID2), 0),
                    SoTon = ISNULL(@SoTonCuoi, 0)
                WHERE BankAccountDetailID = @BankAccountDetailID2
                  AND PositionOrder = 3
                DELETE FROM #Temp WHERE BankAccountDetailID = @BankAccountDetailID2
                SET @COUNT = @COUNT - 1
            END
        SELECT * from #Result ORDER BY BankAccountDetailID, PositionOrder, PostedDate, Date, No, OrderPriority
    END
go

