-- =============================================
-- Author:		namnh
-- Create date: 21/02/2020
-- Description:	Bảng kê số dư ngân hàng khi in trong phân hệ ngân hàng
-- Proc_BANG_KE_SO_DU_NGAN_HANG 'DAB6D60B-64B5-8444-B811-BE29A9DC6738', '02/01/2020', '02/25/2020','USD', 0,',1121,'
-- =============================================
CREATE PROCEDURE [dbo].[Proc_BANG_KE_SO_DU_NGAN_HANG](@CompanyID UNIQUEIDENTIFIER,
                                                      @FromDate DATETIME,
                                                      @ToDate DATETIME,
                                                      @CurrencyID NVARCHAR(3),
                                                      @TypeLedger INT,
                                                      @ListAccountNumber NVARCHAR(3000),
                                                      @IsDependent BIT,
                                                      @typeShowCurrency BIT)
    -- TypeLedger = 0 - Sổ tài chính
    -- TypeLedger = 1 - Sổ quản trị
AS
BEGIN

    CREATE TABLE #Temp
    (
        RowNum              INT IDENTITY (1, 1)
            PRIMARY KEY,
        BankAccountDetailID UNIQUEIDENTIFIER,
        TaiKhoanNganHang    NVARCHAR(25),
        TenNganHang         NVARCHAR(512),
        ChiNhanh            NVARCHAR(512),
        SoDuDauKy           MONEY,
        PhatSinhNo          MONEY,
        PhatSinhCo          MONEY,
        SoDuCuoiKy          MONEY
    )

    CREATE TABLE #Result
    (
        RowNum              INT IDENTITY (1, 1)
            PRIMARY KEY,
        BankAccountDetailID UNIQUEIDENTIFIER,
        TaiKhoanNganHang    NVARCHAR(25),
        TenNganHang         NVARCHAR(512),
        ChiNhanh            NVARCHAR(512),
        SoDuDauKy           MONEY,
        PhatSinhNo          MONEY,
        PhatSinhCo          MONEY,
        SoDuCuoiKy          MONEY
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
    CREATE TABLE #TinhSoDu
    (
        BankAccountDetailID UNIQUEIDENTIFIER,
        SoDuDauKy           MONEY
    )

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
    DECLARE @CompanyName NVARCHAR(512) = (SELECT OrganizationUnitName FROM EbOrganizationUnit WHERE ID = @CompanyID)
    -- Lấy dữ liệu vào bảng theo điêu kiện loại tiền
    INSERT INTO @tbDataGL
    SELECT GL.*
    FROM dbo.GeneralLedger GL
    WHERE GL.PostedDate <= @ToDate
      AND Account IN (SELECT AccountNumber from @tblListAccountNumber)
      AND CompanyID IN (SELECT CompanyID FROM @tblListCompanyID)
      AND ((CASE WHEN @typeShowCurrency = 1 THEN DebitAmount ELSE DebitAmountOriginal END) > 0 OR
           (CASE WHEN @typeShowCurrency = 1 THEN CreditAmount ELSE CreditAmountOriginal END) > 0)
      AND (TypeLedger = 2 OR TypeLedger = @TypeLedger)
    ORDER BY OrderPriority

    IF (@CurrencyID = @MainCurrency)
        BEGIN
            INSERT INTO #Temp (BankAccountDetailID, TaiKhoanNganHang, TenNganHang, ChiNhanh, SoDuDauKy,
                               PhatSinhNo, PhatSinhCo,
                               SoDuCuoiKy)
            SELECT BankAccountDetailID as BankAcccountDetailID,
                   BankAccount         as TaiKhoanNganHang,
                   BankName            as TenNganHang,
                   @CompanyName        as CompanyName,
                   NULL                as SoDuDauKy,
                   Sum(DebitAmount)    as PhatSinhNo,
                   Sum(CreditAmount)   as PhatSinhNo,
                   NULL                as SoDuCuoiKy
            FROM @tbDataGL
            WHERE Account IN (SELECT AccountNumber From @tblListAccountNumber)
              AND PostedDate >= @FromDate
              AND PostedDate <= @ToDate
--               AND CompanyID = @CompanyID
            GROUP BY BankAccountDetailID, BankAccount, BankName
            UNION ALL
            SELECT BankAccountDetailID                    as BankAccountDetailID,
                   BankAccount,
                   BankName,
                   @CompanyName,
                   (SUM(DebitAmount) - SUM(CreditAmount)) as SoDuDauKy,
                   NULL,
                   NULL,
                   NULL
            FROM @tbDataGL
            WHERE Account IN (SELECT AccountNumber From @tblListAccountNumber)
              AND PostedDate < @FromDate
--               AND CompanyID = @CompanyID
            GROUP BY BankAccountDetailID, BankAccount, BankName
        END
    ELSE
        BEGIN
            INSERT INTO #Temp (BankAccountDetailID, TaiKhoanNganHang, TenNganHang, ChiNhanh, SoDuDauKy,
                               PhatSinhNo, PhatSinhCo,
                               SoDuCuoiKy)
            SELECT BankAccountDetailID       as BankAccountDetailID,
                   BankAccount               as TaiKhoanNganHang,
                   BankName                  as TenNganHang,
                   @CompanyName              as CompanyName,
                   NULL                      as SoDuDauKy,
                   Sum(CASE WHEN @typeShowCurrency = 1 THEN DebitAmount ELSE DebitAmountOriginal END)  as PhatSinhNo,
                   Sum(CASE WHEN @typeShowCurrency = 1 THEN CreditAmount ELSE CreditAmountOriginal END) as PhatSinhNo,
                   NULL                      as SoDuCuoiKy
            FROM @tbDataGL
            WHERE Account IN (SELECT AccountNumber From @tblListAccountNumber)
              AND PostedDate >= @FromDate
              AND PostedDate <= @ToDate
--               AND CompanyID = @CompanyID
              AND CurrencyID = @CurrencyID
            GROUP BY BankAccountDetailID, BankAccount, BankName
            UNION ALL
            SELECT BankAccountDetailID                                    as BankAccountDetailID,
                   BankAccount,
                   BankName,
                   @CompanyName,
                   (SUM(CASE WHEN @typeShowCurrency = 1 THEN DebitAmount ELSE DebitAmountOriginal END) - SUM(CASE WHEN @typeShowCurrency = 1 THEN CreditAmount ELSE CreditAmountOriginal END)) as SoDuDauKy,
                   NULL,
                   NULL,
                   NULL
            FROM @tbDataGL
            WHERE Account IN (SELECT AccountNumber From @tblListAccountNumber)
              AND PostedDate < @FromDate
--               AND CompanyID = @CompanyID
              AND CurrencyID = @CurrencyID
            GROUP BY BankAccountDetailID, BankAccount, BankName
        END
    INSERT INTO #Result (BankAccountDetailID, TaiKhoanNganHang, TenNganHang, ChiNhanh, SoDuDauKy, PhatSinhNo,
                         PhatSinhCo, SoDuCuoiKy)
    SELECT BankAccountDetailID as BankAccountDetailID,
           TaiKhoanNganHang    as BankAccount,
           TenNganHang         as BankName,
           ChiNhanh            as ChiNhanh,
           SUM(SoDuDauKy)      as SoDuDauKy,
           SUM(PhatSinhNo)     as PhatSinhNo,
           SUM(PhatSinhCo)     as PhatSinhCo,
           NULL
    FROM #Temp
    GROUP BY BankAccountDetailID, TaiKhoanNganHang, TenNganHang, ChiNhanh

    UPDATE #Result
    SET SoDuCuoiKy = (CASE WHEN SoDuDauKy IS NULL THEN 0 ELSE SoDuDauKy END) +
                     (CASE WHEN PhatSinhNo IS NULL THEN 0 ELSE PhatSinhNo END) -
                     (CASE WHEN PhatSinhCo IS NULL THEN 0 ELSE PhatSinhCo END)
    WHERE 1 = 1
    SELECT * from #Result ORDER BY RowNum
END
go

