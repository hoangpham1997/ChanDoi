CREATE PROCEDURE [dbo].[Proc_LUU_CHUYEN_TIEN_TE_GT] @CompanyID UNIQUEIDENTIFIER, @FromDate DATETIME,
                                                    @ToDate DATETIME,
                                                    @PrevFromDate DATETIME,
                                                    @PrevToDate DATETIME,
                                                    @IsWorkingWithManagementBook BIT,
                                                    @IncludeDependentBranch BIT
AS
BEGIN

    DECLARE
        @Result TABLE
                (
                    ItemID          UNIQUEIDENTIFIER,
                    ItemCode        NVARCHAR(25),
                    ItemName        NVARCHAR(255),
                    ItemNameEnglish NVARCHAR(255),
                    ItemIndex       INT,
                    Description     NVARCHAR(255),
                    FormulaType     INT,
                    FormulaFrontEnd NVARCHAR(MAX),
                    Formula         XML,
                    Hidden          BIT,
                    IsBold          BIT,
                    IsItalic        BIT,
                    Amount          DECIMAL(25, 4),
                    PrevAmount      DECIMAL(25, 4)
                )


    DECLARE @AccountingSystem INT
    SET @AccountingSystem = 15

    DECLARE @ReportID NVARCHAR(100)
    SET @ReportID = '6'

    DECLARE @ItemProfitBeforeTax UNIQUEIDENTIFIER

    SET @ItemProfitBeforeTax = (SELECT ItemID
                                FROM FRTemplate
                                WHERE ItemCode = '01'
                                  AND ReportID = 6
                                  AND AccountingSystem = @AccountingSystem
    )
    -- Lấy dữ liệu cho phần hoạt động

    DECLARE @tblItem TABLE
                     (
                         ItemID                     UNIQUEIDENTIFIER,
                         ItemIndex                  INT,
                         ItemName                   NVARCHAR(255),
                         OperationSign              INT,
                         OperandString              NVARCHAR(255),
                         AccountNumberPercent       NVARCHAR(25),
                         AccountNumber              NVARCHAR(25),
                         CorrespondingAccountNumber VARCHAR(25),
                         IsDetailByAO               INT,
                         AccountKind                INT,
                         ActivityID                 INT -- -1: Chỉ tiêu không chon HĐ. 0: HDSXKD, 1: DT, 2: TC
                     )

    INSERT @tblItem
    SELECT ItemID,
           ItemIndex,
           ItemName,
           x.r.value('@OperationSign', 'INT'),
           RTRIM(LTRIM(x.r.value('@OperandString',
                                 'nvarchar(255)'))),
           x.r.value('@AccountNumber', 'nvarchar(25)') + '%',
           x.r.value('@AccountNumber', 'nvarchar(25)'),
           CASE
               WHEN x.r.value('@CorrespondingAccountNumber',
                              'nvarchar(25)') <> ''
                   THEN x.r.value('@CorrespondingAccountNumber',
                                  'nvarchar(25)') + '%'
               ELSE ''
               END,
           CASE
               WHEN x.r.value('@OperandString', 'nvarchar(255)') LIKE '%ChiTietTheoTKvaDT'
                   OR x.r.value('@OperandString',
                                'nvarchar(255)') LIKE '%_TKvaDT'
                   THEN 1
               ELSE CASE
                        WHEN x.r.value('@OperandString',
                                       'nvarchar(255)') LIKE '%ChitietTheoTK'
                            OR x.r.value('@OperandString',
                                         'nvarchar(255)') LIKE '%_TK'
                            THEN 2
                        ELSE 0
                   END
               END,
           NULL,
           CASE
               WHEN x.r.value('@OperandString', 'nvarchar(255)') NOT LIKE N'%ChiTietTheoHD%'
                   THEN -1
               ELSE CASE
                        WHEN x.r.value('@OperandString',
                                       'nvarchar(255)') LIKE N'%ChiTietTheoHD_SXKD%'
                            THEN 0
                        WHEN x.r.value('@OperandString',
                                       'nvarchar(255)') LIKE N'%ChiTietTheoHD_DAUTU%'
                            THEN 1
                        ELSE 2
                   END
               END
    FROM dbo.FRTemplate
             CROSS APPLY Formula.nodes('/root/DetailFormula') AS x (r)
    WHERE AccountingSystem = @AccountingSystem
      AND ReportID = @ReportID
      AND FormulaType = 0
      AND Formula IS NOT NULL
      AND ItemID <> @ItemProfitBeforeTax
      AND x.r.value('@OperandString', 'nvarchar(255)') NOT IN (
                                                               'PhatsinhDU_ChiTietThanhlyTSCD_BDSDT',
                                                               'PhatsinhDU_ChiTietChiPhiLaiVayChiTraLaiVay',
                                                               'PhatsinhDU_ChitietTienChitraLaivay',
                                                               'PhatsinhNO_ChiTietThanhlyTSCD_BDSDT',
                                                               'PhatsinhNO_ChiTietDGLaiTSGVDT',
                                                               'PhatsinhNO_ChiTietBanThuHoiDTTC',
                                                               'PhatsinhNO_ChiTietChiPhiLaiVayChiTraLaiVay',
                                                               'PhatsinhCO_ChiTietThanhlyTSCD_BDSDT',
                                                               'PhatsinhCO_ChiTietDGLaiTSGVDT',
                                                               'PhatsinhCO_ChiTietBanThuHoiDTTC',
                                                               'PhatsinhCO_ChiTietLai')

    -- Thêm accountkind cho
    UPDATE @tblItem
    SET AccountKind = A.AccountGroupKind
    FROM dbo.AccountList A
    WHERE A.AccountNumber = [@tblItem].AccountNumber AND A.CompanyID = (SELECT TOP(1) (CASE WHEN ParentID IS NOT NULL THEN ParentID
        ELSE ID END) AS ID FROM EbOrganizationUnit WHERE ID = @CompanyID)

    -- Bảng tạm tài khoản Số dư
    DECLARE @AccountBalance TABLE
                            (
                                AccountNumber       NVARCHAR(20),
                                AccountCategoryKind INT,
                                IsDetailByAO        INT          -- 0: không chi tiết,1: chi tiết theo tk và đối tượng,2: chi tiết theo tài khoản
                                ,
                                ActivityID          INT          -- -1: không theo hoạt động, 0: SXKD, 1: Đầu tư, 2: Tài chính
                                ,
                                ItemAccountNumber   NVARCHAR(20) -- Công thức không chi tiết theo tài khoản thì lấy lên AccountNumber trên chỉ tiêu
                            )

    INSERT @AccountBalance
    SELECT A.AccountNumber,
           A.AccountGroupKind,
           B.IsDetailByAO,
           B.ActivityID,
           B.AccountNumber
    FROM dbo.AccountList A
             INNER JOIN @tblItem B ON A.AccountNumber LIKE B.AccountNumberPercent
        AND b.OperandString LIKE N'DU%'
    WHERE A.CompanyID = (SELECT TOP(1) (CASE WHEN ParentID IS NOT NULL THEN ParentID
        ELSE ID END) AS ID FROM EbOrganizationUnit WHERE ID = @CompanyID)
    GROUP BY A.AccountNumber,
             A.AccountGroupKind,
             B.IsDetailByAO,
             B.ActivityID,
             B.AccountNumber

    -- Bảng số dư để tính số dư chi tiết theo đối tượng, tài khoản
    DECLARE @Balance TABLE
                     (
                         AccountNumber        NVARCHAR(20),
                         AccountCategoryKind  INT,
                         AccountObjectID      UNIQUEIDENTIFIER,
                         IsDetailByAO         INT,
                         PrevOpenDebitAmount  DECIMAL(25, 4),
                         PrevOpenCreditAmount DECIMAL(25, 4),
                         OpenDebitAmount      DECIMAL(25, 4),
                         OpenCreditAmount     DECIMAL(25, 4),
                         CloseDebitAmount     DECIMAL(25, 4),
                         CloseCreditAmount    DECIMAL(25, 4),
                         BranchID             UNIQUEIDENTIFIER,
                         ActivityID           INT
                     )
    DECLARE @BalanceALL TABLE
                        (
                            AccountNumber        NVARCHAR(20),
                            AccountCategoryKind  INT,
                            AccountObjectID      UNIQUEIDENTIFIER,
                            IsDetailByAO         INT,
                            PrevOpenDebitAmount  DECIMAL(25, 4),
                            PrevOpenCreditAmount DECIMAL(25, 4),
                            OpenDebitAmount      DECIMAL(25, 4),
                            OpenCreditAmount     DECIMAL(25, 4),
                            CloseDebitAmount     DECIMAL(25, 4),
                            CloseCreditAmount    DECIMAL(25, 4),
                            BranchID             UNIQUEIDENTIFIER,
                            ActivityID           INT
                        )

    DECLARE @StartDate DATETIME
    SELECT TOP 1 @StartDate = CONVERT(DATETIME, StartDate, 103)
    FROM dbo.EbOrganizationUnit
    WHERE CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch))

    INSERT INTO @BalanceALL
    SELECT CASE
               WHEN A.IsDetailByAO = 0 THEN A.ItemAccountNumber
               ELSE GL.Account
               END,
           CASE
               WHEN A.IsDetailByAO = 0
                   AND A.ItemAccountNumber <> '11'
                   THEN A.AccountCategoryKind
               ELSE A.AccountCategoryKind
               END,
           CASE
               WHEN A.IsDetailByAO = 1 THEN GL.AccountingObjectID
               ELSE NULL
               END AS AccountObjectID,
           A.IsDetailByAO,
           SUM(CASE
                   WHEN PostedDate < @PrevFromDate
                       THEN DebitAmount - CreditAmount
                   ELSE 0
               END)   OpenDebitAmount,
           0,
           SUM(CASE
                   WHEN PostedDate < @FromDate
                       THEN DebitAmount - CreditAmount
                   ELSE 0
               END)   OpenDebitAmount,
           0,
           SUM(CASE
                   WHEN PostedDate <= @ToDate
                       THEN DebitAmount - CreditAmount
                   ELSE 0
               END)   CloseDebitAmount,
           0
           -- nếu TK chi tiết theo tài khoản và không bù trừ nợ có giữa các chi nhánh thì lấy lên branchID
            ,
           CASE
               WHEN A.IsDetailByAO = 1 THEN GL.BranchID
               ELSE NULL
               END AS BranchID,
           CASE A.ActivityID
               WHEN -1 THEN -1
               --nvtoan modify 30/09/2016: Nếu không có dữ liệu trong bảng AC mới lấy giá trị mặc định, còn lại phải lấy theo AC
               WHEN 0 THEN CASE
                               WHEN AC.RefDetailID IS NULL THEN 0
                               ELSE Ac.ActivityID
                   END
               ELSE AC.ActivityID
               --WHEN 0 THEN ISNULL(Ac.ActivityID,0)
               --ELSE AC.ActivityID
               END
    FROM dbo.GeneralLedger GL
             INNER JOIN @AccountBalance A ON GL.Account = A.AccountNumber
             INNER JOIN AccountList ACC ON A.AccountNumber = Acc.AccountNumber AND ACC.CompanyID = (SELECT TOP(1) (CASE WHEN ParentID IS NOT NULL THEN ParentID
        ELSE ID END) AS ID FROM EbOrganizationUnit WHERE ID = @CompanyID)
             LEFT JOIN dbo.FRB03ReportDetailActivity AC ON GL.DetailID = Ac.RefDetailID
        AND AC.IsPostToManagementBook = @IsWorkingWithManagementBook
             LEFT JOIN dbo.FRB03OPNDetailByActivity OPN ON GL.PostedDate < @StartDate--GL.RefNo = 'OPN'
        AND GL.Account = OPN.AccountNumber
        AND GL.TypeLedger = OPN.IsPostToManagementBook
        AND GL.BranchID = OPN.BranchID
        AND (ACC.DetailType like '%0%'
            OR A.IsDetailByAO IN (
                                  0, 2)
                                                               )
    WHERE PostedDate <= @ToDate
      AND GL.CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch))
      AND (GL.TypeLedger = @IsWorkingWithManagementBook OR GL.TypeLedger = 2)
      AND OPN.AccountNumber IS NULL
    GROUP BY CASE
                 WHEN A.IsDetailByAO = 0 THEN A.ItemAccountNumber
                 ELSE GL.Account
                 END,
             CASE
                 WHEN A.IsDetailByAO = 0
                     AND A.ItemAccountNumber <> '11'
                     THEN A.AccountCategoryKind
                 ELSE A.AccountCategoryKind
                 END,
             CASE
                 WHEN A.IsDetailByAO = 1 THEN GL.AccountingObjectID
                 ELSE NULL
                 END,
             A.IsDetailByAO
             -- nếu TK chi tiết theo tài khoản và không bù trừ nợ có giữa các chi nhánh thì lấy lên branchID
            ,
             CASE
                 WHEN A.IsDetailByAO = 1 THEN GL.BranchID
                 ELSE NULL
                 END,
             CASE A.ActivityID
                 WHEN -1 THEN -1
                 --nvtoan modify 30/09/2016: Nếu không có dữ liệu trong bảng AC mới lấy giá trị mặc định, còn lại phải lấy theo AC
                 WHEN 0 THEN CASE
                                 WHEN AC.RefDetailID IS NULL THEN 0
                                 ELSE Ac.ActivityID
                     END
                 ELSE AC.ActivityID
                 --WHEN 0 THEN ISNULL(Ac.ActivityID,0)
                 --ELSE AC.ActivityID
                 END
    OPTION ( RECOMPILE )

    DECLARE @OPNDate DATETIME
    SET @OPNDate = DATEADD(day, -1, @StartDate)

    DECLARE @tblActivity TABLE
                         (
                             ActivityID INT
                         )
    INSERT @tblActivity
    SELECT 0
    UNION ALL
    SELECT 1
    UNION ALL
    SELECT 2
    -- Lấy số dư đầu kỳ tách dữ liệu
    INSERT INTO @BalanceALL
    SELECT OPN.AccountNumber,
           A.AccountCategoryKind,
           NULL           AS AccountObjectID,
           A.IsDetailByAO AS IsDetailByAO,
           SUM(CASE
                   WHEN @PrevFromDate > @OPNDate
                       THEN CASE
                                WHEN OPN.IsDebitBalance = 1
                                    THEN CASE T.ActivityID
                                             WHEN 0
                                                 THEN OPN.BussinessAmount
                                             WHEN 1
                                                 THEN OPN.InvestmentAmount
                                             WHEN 2
                                                 THEN OPN.FinancialAmount
                                    END
                                ELSE 0
                       END
                   ELSE 0
               END)       AS PrevOpenDebitAmount,
           SUM(CASE
                   WHEN @PrevFromDate > @OPNDate
                       THEN CASE
                                WHEN OPN.IsDebitBalance = 0
                                    THEN CASE T.ActivityID
                                             WHEN 0
                                                 THEN OPN.BussinessAmount
                                             WHEN 1
                                                 THEN OPN.InvestmentAmount
                                             WHEN 2
                                                 THEN OPN.FinancialAmount
                                    END
                                ELSE 0
                       END
                   ELSE 0
               END)       AS PrevOpenCreditAmount,
           SUM(CASE
                   WHEN @FromDate > @OPNDate
                       THEN CASE
                                WHEN OPN.IsDebitBalance = 1
                                    THEN CASE T.ActivityID
                                             WHEN 0
                                                 THEN OPN.BussinessAmount
                                             WHEN 1
                                                 THEN OPN.InvestmentAmount
                                             WHEN 2
                                                 THEN OPN.FinancialAmount
                                    END
                                ELSE 0
                       END
                   ELSE 0
               END)       AS OpenDebitAmount,
           SUM(CASE
                   WHEN @FromDate > @OPNDate
                       THEN CASE
                                WHEN OPN.IsDebitBalance = 0
                                    THEN CASE T.ActivityID
                                             WHEN 0
                                                 THEN OPN.BussinessAmount
                                             WHEN 1
                                                 THEN OPN.InvestmentAmount
                                             WHEN 2
                                                 THEN OPN.FinancialAmount
                                    END
                                ELSE 0
                       END
                   ELSE 0
               END)       AS OpenCreditAmount,
           SUM(CASE
                   WHEN @ToDate >= @OPNDate
                       THEN CASE
                                WHEN OPN.IsDebitBalance = 1
                                    THEN CASE T.ActivityID
                                             WHEN 0
                                                 THEN OPN.BussinessAmount
                                             WHEN 1
                                                 THEN OPN.InvestmentAmount
                                             WHEN 2
                                                 THEN OPN.FinancialAmount
                                    END
                                ELSE 0
                       END
                   ELSE 0
               END)       AS CloseDebitAmount,
           SUM(CASE
                   WHEN @ToDate >= @OPNDate
                       THEN CASE
                                WHEN OPN.IsDebitBalance = 0
                                    THEN CASE T.ActivityID
                                             WHEN 0
                                                 THEN OPN.BussinessAmount
                                             WHEN 1
                                                 THEN OPN.InvestmentAmount
                                             WHEN 2
                                                 THEN OPN.FinancialAmount
                                    END
                                ELSE 0
                       END
                   ELSE 0
               END)       AS CloseCreditAmount
           -- nếu TK chi tiết theo tài khoản và không bù trừ nợ có giữa các chi nhánh thì lấy lên branchID
            ,
           NULL           AS BranchID,
           T.ActivityID   AS ActivityID -- Hoạt động kinh doanh
    FROM dbo.FRB03OPNDetailByActivity OPN
             INNER JOIN @AccountBalance A ON OPN.AccountNumber = A.AccountNumber
             INNER JOIN AccountList AC ON OPN.AccountNumber = AC.AccountNumber AND AC.CompanyID = (SELECT TOP(1) (CASE WHEN ParentID IS NOT NULL THEN ParentID
        ELSE ID END) AS ID FROM EbOrganizationUnit WHERE ID = @CompanyID),
         @tblActivity T
    WHERE OPN.IsPostToManagementBook = @IsWorkingWithManagementBook
      AND (AC.DetailType like '%0%'
        OR A.IsDetailByAO IN (0, 2)
        )
    GROUP BY OPN.AccountNumber,
             A.AccountCategoryKind,
             A.IsDetailByAO,
             T.ActivityID
    OPTION ( RECOMPILE )

    -- cộng gộp số dư lại
    INSERT INTO @Balance
    SELECT AccountNumber,
           AccountCategoryKind,
           AccountObjectID,
           IsDetailByAO,
           SUM(PrevOpenDebitAmount - PrevOpenCreditAmount),
           0,
           SUM(OpenDebitAmount - OpenCreditAmount),
           0,
           SUM(CloseDebitAmount - CloseCreditAmount),
           0,
           BranchID,
           ActivityID
    FROM @BalanceALL B
    GROUP BY AccountNumber,
             AccountCategoryKind,
             AccountObjectID,
             IsDetailByAO,
             BranchID,
             ActivityID
    HAVING SUM(PrevOpenDebitAmount - PrevOpenCreditAmount) <> 0
        OR SUM(OpenDebitAmount - OpenCreditAmount) <> 0
        OR SUM(CloseDebitAmount - CloseCreditAmount) <> 0


    UPDATE @Balance
    SET PrevOpenCreditAmount = -PrevOpenDebitAmount,
        PrevOpenDebitAmount  = 0
    WHERE AccountCategoryKind = 1
       OR (AccountCategoryKind NOT IN (0, 1)
        AND PrevOpenDebitAmount < 0
        )

    UPDATE @Balance
    SET OpenCreditAmount = -OpenDebitAmount,
        OpenDebitAmount  = 0
    WHERE AccountCategoryKind = 1
       OR (AccountCategoryKind NOT IN (0, 1)
        AND OpenDebitAmount < 0
        )

    UPDATE @Balance
    SET CloseCreditAmount = -CloseDebitAmount,
        CloseDebitAmount  = 0
    WHERE AccountCategoryKind = 1
       OR (AccountCategoryKind NOT IN (0, 1)
        AND CloseDebitAmount < 0
        )


    -- bảng chứa tài khoản nợ,và đối ứng xuất hiện ở bảng config
    DECLARE @tblDebitAccount TABLE
                             (
                                 AccountNumber NVARCHAR(20) PRIMARY KEY
                             )

    INSERT @tblDebitAccount
    SELECT DISTINCT A.AccountNumber
    FROM dbo.AccountList A
             INNER JOIN @tblItem B ON A.AccountNumber LIKE B.AccountNumberPercent
        AND (b.OperandString LIKE N'PhatsinhNO%'
            OR b.OperandString LIKE N'PhatsinhDU%'
                                          )
    WHERE A.CompanyID = (SELECT TOP(1) (CASE WHEN ParentID IS NOT NULL THEN ParentID
        ELSE ID END) AS ID FROM EbOrganizationUnit WHERE ID = @CompanyID)


    -- Bang tam chua du lieu cho các chỉ tiêu phát sinh đối ứng, phát sinh nợ, phát sinh có
    DECLARE @DebitBalance TABLE
                          (
                              AccountNumber              NVARCHAR(25),
                              CorrespondingAccountNumber NVARCHAR(25),
                              PrevAmount                 DECIMAL(25, 4),
                              Amount                     DECIMAL(25, 4),
                              ActivityID                 INT
                          )

    INSERT @DebitBalance
    SELECT GL.Account,
           GL.AccountCorresponding,
           SUM(CASE
                   WHEN PostedDate BETWEEN @PrevFromDate AND @PrevToDate
                       THEN DebitAmount
                   ELSE 0
               END),
           SUM(CASE
                   WHEN PostedDate BETWEEN @FromDate AND @ToDate
                       THEN DebitAmount
                   ELSE 0
               END),
           --nvtoan modify 30/09/2016: Nếu không có dữ liệu trong bảng AC mới lấy giá trị mặc định, còn lại phải lấy theo AC
           CASE
               WHEN AC.RefDetailID IS NULL THEN 0
               ELSE Ac.ActivityID
               END
           --ISNULL(Ac.ActivityID, 0)
    FROM dbo.GeneralLedger AS GL
             INNER JOIN @tblDebitAccount I ON (GL.Account = I.AccountNumber)
             LEFT JOIN dbo.FRB03ReportDetailActivity AC ON GL.DetailID = Ac.RefDetailID
        AND AC.IsPostToManagementBook = @IsWorkingWithManagementBook
    WHERE GL.PostedDate < @ToDate
      AND GL.CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch))
      AND (GL.TypeLedger = @IsWorkingWithManagementBook OR GL.TypeLedger = 2)
    GROUP BY GL.Account,
             GL.AccountCorresponding,
             --nvtoan modify 30/09/2016: Nếu không có dữ liệu trong bảng AC mới lấy giá trị mặc định, còn lại phải lấy theo AC
             CASE
                 WHEN AC.RefDetailID IS NULL THEN 0
                 ELSE Ac.ActivityID
                 END
             --ISNULL(Ac.ActivityID, 0)
    OPTION ( RECOMPILE )

    -- bảng chứa tài khoản Có xuất hiện ở bảng config
    DECLARE @tblCreditAccount TABLE
                              (
                                  AccountNumber NVARCHAR(20)
                              )
    INSERT @tblCreditAccount
    SELECT DISTINCT A.AccountNumber
    FROM dbo.AccountList A
             INNER JOIN @tblItem B ON A.AccountNumber LIKE B.AccountNumberPercent
        AND (b.OperandString LIKE N'PhatsinhCO%')
    WHERE A.CompanyID = (SELECT TOP(1) (CASE WHEN ParentID IS NOT NULL THEN ParentID
        ELSE ID END) AS ID FROM EbOrganizationUnit WHERE ID = @CompanyID)

    DECLARE @CreditBalance TABLE
                           (
                               AccountNumber NVARCHAR(25),
                               PrevAmount    DECIMAL(25, 4),
                               Amount        DECIMAL(25, 4),
                               ActivityID    INT
                           )
    INSERT @CreditBalance
    SELECT GL.Account,
           SUM(CASE
                   WHEN PostedDate BETWEEN @PrevFromDate AND @PrevToDate
                       THEN CreditAmount
                   ELSE 0
               END),
           SUM(CASE
                   WHEN PostedDate BETWEEN @FromDate AND @ToDate
                       THEN CreditAmount
                   ELSE 0
               END),
           --nvtoan modify 30/09/2016: Nếu không có dữ liệu trong bảng AC mới lấy giá trị mặc định, còn lại phải lấy theo AC
           CASE
               WHEN AC.RefDetailID IS NULL THEN 0
               ELSE Ac.ActivityID
               END
           --ISNULL(Ac.ActivityID, 0)
    FROM dbo.GeneralLedger AS GL
             INNER JOIN @tblCreditAccount I ON (GL.Account = I.AccountNumber)
             LEFT JOIN dbo.FRB03ReportDetailActivity AC ON GL.DetailID = Ac.RefDetailID
        AND AC.IsPostToManagementBook = @IsWorkingWithManagementBook
    WHERE GL.PostedDate < @ToDate
      AND GL.CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch))
      AND (GL.TypeLedger = @IsWorkingWithManagementBook OR GL.TypeLedger = 2)
    GROUP BY GL.Account,
             --nvtoan modify 30/09/2016: Nếu không có dữ liệu trong bảng AC mới lấy giá trị mặc định, còn lại phải lấy theo AC
             CASE
                 WHEN AC.RefDetailID IS NULL THEN 0
                 ELSE Ac.ActivityID
                 END
             --ISNULL(Ac.ActivityID, 0)
    OPTION ( RECOMPILE )

    -- Lấy dữ liệu cho phần nghiệp vụ
    DECLARE @tblItemBussines TABLE
                             (
                                 ItemID                     UNIQUEIDENTIFIER,
                                 ItemIndex                  INT,
                                 ItemName                   NVARCHAR(255),
                                 OperationSign              INT,
                                 OperandString              NVARCHAR(255),
                                 AccountNumberPercent       NVARCHAR(25),
                                 AccountNumber              NVARCHAR(25),
                                 CorrespondingAccountNumber VARCHAR(25),
                                 IsDetailByAO               INT,
                                 AccountKind                INT
                             )

    INSERT @tblItemBussines
    SELECT ItemID,
           ItemIndex,
           ItemName,
           x.r.value('@OperationSign', 'INT'),
           RTRIM(LTRIM(x.r.value('@OperandString',
                                 'nvarchar(255)'))),
           x.r.value('@AccountNumber', 'nvarchar(25)') + '%',
           x.r.value('@AccountNumber', 'nvarchar(25)'),
           CASE
               WHEN x.r.value('@CorrespondingAccountNumber',
                              'nvarchar(25)') <> ''
                   THEN x.r.value('@CorrespondingAccountNumber',
                                  'nvarchar(25)') + '%'
               ELSE ''
               END,
           CASE
               WHEN x.r.value('@OperandString', 'nvarchar(255)') LIKE '%ChiTietTheoTKvaDT'
                   OR x.r.value('@OperandString',
                                'nvarchar(255)') LIKE '%_TKvaDT'
                   THEN 1
               ELSE CASE
                        WHEN x.r.value('@OperandString',
                                       'nvarchar(255)') LIKE '%ChitietTheoTK'
                            OR x.r.value('@OperandString',
                                         'nvarchar(255)') LIKE '%_TK'
                            THEN 2
                        ELSE 0
                   END
               END,
           NULL
    FROM dbo.FRTemplate
             CROSS APPLY Formula.nodes('/root/DetailFormula') AS x (r)
    WHERE AccountingSystem = @AccountingSystem
      AND ReportID = @ReportID
      AND FormulaType = 0
      AND Formula IS NOT NULL
      AND ItemID <> @ItemProfitBeforeTax
      AND x.r.value('@OperandString', 'nvarchar(255)') IN (
                                                           'PhatsinhDU_ChiTietThanhlyTSCD_BDSDT',
                                                           'PhatsinhDU_ChiTietChiPhiLaiVayChiTraLaiVay',
                                                           'PhatsinhDU_ChitietTienChitraLaivay',
                                                           'PhatsinhNO_ChiTietThanhlyTSCD_BDSDT',
                                                           'PhatsinhNO_ChiTietDGLaiTSGVDT',
                                                           'PhatsinhNO_ChiTietBanThuHoiDTTC',
                                                           'PhatsinhNO_ChiTietChiPhiLaiVayChiTraLaiVay',
                                                           'PhatsinhCO_ChiTietThanhlyTSCD_BDSDT',
                                                           'PhatsinhCO_ChiTietDGLaiTSGVDT',
                                                           'PhatsinhCO_ChiTietBanThuHoiDTTC',
                                                           'PhatsinhCO_ChiTietLai')

    -- Thêm accountkind cho
    UPDATE @tblItemBussines
    SET AccountKind = A.AccountGroupKind
    FROM dbo.AccountList A
    WHERE A.AccountNumber = [@tblItemBussines].AccountNumber
    AND A.CompanyID = (SELECT TOP(1) (CASE WHEN ParentID IS NOT NULL THEN ParentID
        ELSE ID END) AS ID FROM EbOrganizationUnit WHERE ID = @CompanyID)

    -- bảng chứa tài khoản nợ, đối ứng xuất hiện ở bảng config

    DECLARE @tblDebitAccountBussines TABLE
                                     (
                                         AccountNumber NVARCHAR(20) PRIMARY KEY
                                     )

    INSERT @tblDebitAccountBussines
    SELECT DISTINCT A.AccountNumber
    FROM dbo.AccountList A
             INNER JOIN @tblItemBussines B ON A.AccountNumber LIKE B.AccountNumberPercent
        AND (b.OperandString LIKE N'PhatsinhNO%'
            OR b.OperandString LIKE N'PhatsinhDU%'
                                                  )
    WHERE A.CompanyID = (SELECT TOP(1) (CASE WHEN ParentID IS NOT NULL THEN ParentID
        ELSE ID END) AS ID FROM EbOrganizationUnit WHERE ID = @CompanyID)


    -- Bang tam chua du lieu cho các chỉ tiêu phát sinh đối ứng, phát sinh nợ, phát sinh có
    DECLARE @DebitBalanceBussiness TABLE
                                   (
                                       AccountNumber              NVARCHAR(25),
                                       CorrespondingAccountNumber NVARCHAR(25),
                                       PrevAmount                 DECIMAL(25, 4),
                                       Amount                     DECIMAL(25, 4),
                                       BussinessID                INT
                                   )

    INSERT @DebitBalanceBussiness
    SELECT GL.Account,
           GL.AccountCorresponding,
           SUM(CASE
                   WHEN PostedDate BETWEEN @PrevFromDate AND @PrevToDate
                       THEN DebitAmount
                   ELSE 0
               END),
           SUM(CASE
                   WHEN PostedDate BETWEEN @FromDate AND @ToDate
                       THEN DebitAmount
                   ELSE 0
               END),
           BussinessID
    FROM dbo.GeneralLedger AS GL
             INNER JOIN @tblDebitAccountBussines I ON (GL.Account = I.AccountNumber)
             INNER JOIN dbo.FRB03GTBussiness AB ON GL.DetailID = AB.RefDetailID
        AND AB.IsPostToManagementBook = @IsWorkingWithManagementBook
    WHERE GL.PostedDate < @ToDate
      AND GL.CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch))
      AND (GL.TypeLedger = @IsWorkingWithManagementBook OR GL.TypeLedger = 2)
    GROUP BY GL.Account,
             GL.AccountCorresponding,
             BussinessID
    OPTION ( RECOMPILE )


    -- bảng chứa tài khoản nợ, đối ứng xuất hiện ở bảng config


    DECLARE @tblCreditAccountBussines TABLE
                                      (
                                          AccountNumber NVARCHAR(20) PRIMARY KEY
                                      )
    INSERT @tblCreditAccountBussines
    SELECT DISTINCT A.AccountNumber
    FROM dbo.AccountList A
             INNER JOIN @tblItemBussines B ON A.AccountNumber LIKE B.AccountNumberPercent
        AND (b.OperandString LIKE N'PhatsinhCO%')
    WHERE A.CompanyID = (SELECT TOP(1) (CASE WHEN ParentID IS NOT NULL THEN ParentID
        ELSE ID END) AS ID FROM EbOrganizationUnit WHERE ID = @CompanyID)

    -- Bang tam chua du lieu cho các chỉ tiêu phát phát sinh có
    DECLARE @CreditBalanceBussiness TABLE
                                    (
                                        AccountNumber NVARCHAR(25),
                                        PrevAmount    DECIMAL(25, 4),
                                        Amount        DECIMAL(25, 4),
                                        BussinessID   INT
                                    )

    INSERT @CreditBalanceBussiness
    SELECT GL.Account,
           SUM(CASE
                   WHEN PostedDate BETWEEN @PrevFromDate AND @PrevToDate
                       THEN CreditAmount
                   ELSE 0
               END),
           SUM(CASE
                   WHEN PostedDate BETWEEN @FromDate AND @ToDate
                       THEN CreditAmount
                   ELSE 0
               END),
           BussinessID
    FROM dbo.GeneralLedger AS GL
             INNER JOIN @tblCreditAccountBussines I ON (GL.Account = I.AccountNumber)
             INNER JOIN dbo.FRB03GTBussiness AB ON GL.DetailID = AB.RefDetailID
        AND AB.IsPostToManagementBook = @IsWorkingWithManagementBook
    WHERE GL.PostedDate < @ToDate
      AND GL.CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch))
      AND (GL.TypeLedger = @IsWorkingWithManagementBook OR GL.TypeLedger = 2)
    GROUP BY GL.Account,
             BussinessID
    OPTION ( RECOMPILE )


    -- Bảng tạm cho các chỉ tiêu
    DECLARE @tblMasterDetail TABLE
                             (
                                 ItemID        UNIQUEIDENTIFIER,
                                 DetailItemID  UNIQUEIDENTIFIER,
                                 OperationSign INT,
                                 Grade         INT,
                                 PrevAmount    DECIMAL(25, 4),
                                 Amount        DECIMAL(25, 4)
                             )

    -- Bang tam chua du lieu cho các chỉ tiêu có Dư nợ

    INSERT INTO @tblMasterDetail
    SELECT I.ItemID,
           NULL,
           1,
           -1,
           SUM(I.PrevAmount),
           SUM(I.Amount)
    FROM (SELECT I.ItemID,
                 SUM(CASE
                         WHEN I.OperandString = 'PhatsinhDU'
                             OR I.OperandString = 'PhatsinhDUChiTietTheoHD_DAUTU'
                                  AND B.ActivityID = 1
                             OR I.OperandString = 'PhatsinhDUChiTietTheoHD_TAICHINH'
                                  AND B.ActivityID = 2
                             OR I.OperandString = 'PhatsinhDUChiTietTheoHD_SXKD'
                                  AND B.ActivityID = 0
                             THEN b.PrevAmount
                         ELSE 0
                         END * I.OperationSign) AS PrevAmount,
                 SUM(CASE
                         WHEN I.OperandString = 'PhatsinhDU'
                             OR I.OperandString = 'PhatsinhDUChiTietTheoHD_DAUTU'
                                  AND B.ActivityID = 1
                             OR I.OperandString = 'PhatsinhDUChiTietTheoHD_TAICHINH'
                                  AND B.ActivityID = 2
                             OR I.OperandString = 'PhatsinhDUChiTietTheoHD_SXKD'
                                  AND B.ActivityID = 0
                             THEN (b.Amount)
                         ELSE 0
                         END * I.OperationSign) AS Amount
          FROM @tblItem I
                   INNER JOIN @DebitBalance B ON (B.AccountNumber LIKE I.AccountNumberPercent)
              AND (B.CorrespondingAccountNumber LIKE I.CorrespondingAccountNumber)
          WHERE LEFT(I.OperandString, 10) = 'PhatsinhDU'
          GROUP BY I.ItemID

                   -- Lấy phát sinh Nợ
          UNION ALL
          SELECT I.ItemID,
                 SUM(CASE
                         WHEN I.OperandString = 'PhatsinhNO'
                             OR I.OperandString = 'PhatsinhNO_ChiTietTheoHD_SXKD'
                                  AND B.ActivityID = 0
                             THEN b.PrevAmount
                         ELSE 0
                         END * I.OperationSign) AS PrevAmount,
                 SUM(CASE
                         WHEN I.OperandString = 'PhatsinhNO'
                             OR I.OperandString = 'PhatsinhNO_ChiTietTheoHD_SXKD'
                                  AND B.ActivityID = 0
                             THEN b.Amount
                         ELSE 0
                         END * I.OperationSign) AS Amount
          FROM @tblItem I
                   INNER JOIN @DebitBalance B ON (B.AccountNumber LIKE I.AccountNumberPercent)
          WHERE LEFT(I.OperandString, 10) = 'PhatsinhNO'
          GROUP BY I.ItemID
          UNION ALL
          SELECT I.ItemID,
                 SUM(CASE
                         WHEN I.OperandString = 'PhatsinhCO'
                             OR I.OperandString = 'PhatsinhCO_ChiTietTheoHD_SXKD'
                                  AND B.ActivityID = 0
                             THEN b.PrevAmount
                         ELSE 0
                         END * I.OperationSign) AS PrevAmount,
                 SUM(CASE
                         WHEN I.OperandString = 'PhatsinhNO'
                             OR I.OperandString = 'PhatsinhCO_ChiTietTheoHD_SXKD'
                                  AND B.ActivityID = 0
                             THEN b.Amount
                         ELSE 0
                         END * I.OperationSign) AS Amount
          FROM @tblItem I
                   INNER JOIN @CreditBalance B ON (B.AccountNumber LIKE I.AccountNumberPercent)
          WHERE LEFT(I.OperandString, 10) = 'PhatsinhCO'
          GROUP BY I.ItemID

                   -- Lấy lên phát sinh nợ có theo nghiệp vụ
          UNION ALL
          SELECT I.ItemID,
                 SUM(CASE
                         WHEN I.OperandString = 'PhatsinhDU_ChiTietChiPhiLaiVayChiTraLaiVay'
                                  AND B.BussinessID = 5
                             OR I.OperandString = 'PhatsinhDU_ChiTietThanhlyTSCD_BDSDT'
                                  AND B.BussinessID = 0
                             THEN b.PrevAmount
                         ELSE 0
                         END * I.OperationSign) AS PrevAmount,
                 SUM(CASE
                         WHEN I.OperandString = 'PhatsinhDU_ChiTietChiPhiLaiVayChiTraLaiVay'
                                  AND B.BussinessID = 5
                             OR I.OperandString = 'PhatsinhDU_ChiTietThanhlyTSCD_BDSDT'
                                  AND B.BussinessID = 0
                             THEN (b.Amount)
                         ELSE 0
                         END * I.OperationSign) AS Amount
          FROM @tblItemBussines I
                   INNER JOIN @DebitBalanceBussiness B ON (B.AccountNumber LIKE I.AccountNumberPercent)
              AND (B.CorrespondingAccountNumber LIKE I.CorrespondingAccountNumber)
          WHERE LEFT(I.OperandString, 10) = 'PhatsinhDU'
          GROUP BY I.ItemID

                   -- Lấy phát sinh Nợ
          UNION ALL
          SELECT I.ItemID,
                 SUM(CASE
                         WHEN I.OperandString = 'PhatsinhNO_ChiTietThanhlyTSCD_BDSDT'
                                  AND B.BussinessID = 0
                             OR I.OperandString = 'PhatsinhNO_ChiTietDGLaiTSGVDT'
                                  AND B.BussinessID = 1
                             OR I.OperandString = 'PhatsinhNO_ChiTietBanThuHoiDTTC'
                                  AND B.BussinessID = 2
                             OR I.OperandString = 'PhatsinhNO_ChiTietChiPhiLaiVayChiTraLaiVay'
                                  AND B.BussinessID = 5
                             THEN b.PrevAmount
                         ELSE 0
                         END * I.OperationSign) AS PrevAmount,
                 SUM(CASE
                         WHEN I.OperandString = 'PhatsinhNO_ChiTietThanhlyTSCD_BDSDT'
                                  AND B.BussinessID = 0
                             OR I.OperandString = 'PhatsinhNO_ChiTietDGLaiTSGVDT'
                                  AND B.BussinessID = 1
                             OR I.OperandString = 'PhatsinhNO_ChiTietBanThuHoiDTTC'
                                  AND B.BussinessID = 2
                             OR I.OperandString = 'PhatsinhNO_ChiTietChiPhiLaiVayChiTraLaiVay'
                                  AND B.BussinessID = 5
                             THEN b.Amount
                         ELSE 0
                         END * I.OperationSign) AS Amount
          FROM @tblItemBussines I
                   INNER JOIN @DebitBalanceBussiness B ON (B.AccountNumber LIKE I.AccountNumberPercent)
          WHERE LEFT(I.OperandString, 10) = 'PhatsinhNO'
          GROUP BY I.ItemID
          UNION ALL
          SELECT I.ItemID,
                 SUM(CASE
                         WHEN I.OperandString = 'PhatsinhCO_ChiTietThanhlyTSCD_BDSDT'
                                  AND B.BussinessID = 0
                             OR I.OperandString = 'PhatsinhCO_ChiTietDGLaiTSGVDT'
                                  AND B.BussinessID = 1
                             OR I.OperandString = 'PhatsinhCO_ChiTietBanThuHoiDTTC'
                                  AND B.BussinessID = 2
                             OR I.OperandString = 'PhatsinhCO_ChiTietLai'
                                  AND B.BussinessID = 3
                             THEN b.PrevAmount
                         ELSE 0
                         END * I.OperationSign) AS PrevAmount,
                 SUM(CASE
                         WHEN I.OperandString = 'PhatsinhCO_ChiTietThanhlyTSCD_BDSDT'
                                  AND B.BussinessID = 0
                             OR I.OperandString = 'PhatsinhCO_ChiTietDGLaiTSGVDT'
                                  AND B.BussinessID = 1
                             OR I.OperandString = 'PhatsinhCO_ChiTietBanThuHoiDTTC'
                                  AND B.BussinessID = 2
                             OR I.OperandString = 'PhatsinhCO_ChiTietLai'
                                  AND B.BussinessID = 3
                             THEN b.Amount
                         ELSE 0
                         END * I.OperationSign) AS Amount
          FROM @tblItemBussines I
                   INNER JOIN @CreditBalanceBussiness B ON (B.AccountNumber LIKE I.AccountNumberPercent)
          WHERE LEFT(I.OperandString, 10) = 'PhatsinhCO'
          GROUP BY I.ItemID


                   -- Lấy số dư
          UNION ALL
          SELECT I.ItemID,
                 SUM((CASE
                          WHEN LEFT(I.OperandString, 6) = 'DUNODK'
                              THEN B.PrevOpenDebitAmount
                          WHEN LEFT(I.OperandString, 6) = 'DUCODK'
                              THEN B.PrevOpenCreditAmount
                          WHEN LEFT(I.OperandString, 6) = 'DUNOCK'
                              THEN B.OpenDebitAmount
                          WHEN LEFT(I.OperandString, 6) = 'DUCOCK'
                              THEN B.OpenCreditAmount
                          ELSE 0
                     END) * I.OperationSign) AS PrevAmount,
                 SUM((CASE
                          WHEN LEFT(I.OperandString, 6) = 'DUNODK'
                              THEN B.OpenDebitAmount
                          WHEN LEFT(I.OperandString, 6) = 'DUCODK'
                              THEN B.OpenCreditAmount
                          WHEN LEFT(I.OperandString, 6) = 'DUNOCK'
                              THEN B.CloseDebitAmount
                          WHEN LEFT(I.OperandString, 6) = 'DUCOCK'
                              THEN B.CloseCreditAmount
                          ELSE 0
                     END) * I.OperationSign) AS Amount
          FROM @tblItem I
                   INNER JOIN @Balance B ON B.AccountNumber LIKE I.AccountNumberPercent
              AND B.IsDetailByAO = I.IsDetailByAO
              AND I.ActivityID = B.ActivityID
          GROUP BY I.ItemID
         ) AS I
    GROUP BY I.ITemID

    -- Tổng lợi nhuận kế toán trước thuế
    DECLARE @B02ProfitBeforeTaxID UNIQUEIDENTIFIER
    SET @B02ProfitBeforeTaxID = (SELECT ItemId
                                 FROM dbo.FRTemplate
                                 WHERE ReportID = 2
                                   AND AccountingSystem = @AccountingSystem
                                   AND ItemCode = N'50'
    )

    INSERT INTO @tblMasterDetail
    SELECT @ItemProfitBeforeTax,
           NULL,
           1,
           -1,
           F.PrevAmount,
           F.Amount
    FROM dbo.Func_Get_KET_QUA_HOAT_DONG_KDTH(@CompanyID, @FromDate, @ToDate, @PrevFromDate, @PrevToDate,
                                             @IsWorkingWithManagementBook, @IncludeDependentBranch) F
    WHERE ItemID = @B02ProfitBeforeTaxID


    -- Lấy các chỉ tiêu tổng hợp
    INSERT @tblMasterDetail
    SELECT ItemID,
           x.r.value('@ItemID', 'NVARCHAR(100)'),
           x.r.value('@OperationSign', 'INT'),
           0,
           0.0,
           0.0
    FROM dbo.FRTemplate
             CROSS APPLY Formula.nodes('/root/MasterFormula') AS x (r)
    WHERE AccountingSystem = @AccountingSystem
      AND ReportID = @ReportID
      AND FormulaType = 1
      AND Formula IS NOT NULL

    -- Cộng chỉ tiêu con lên chỉ tiêu cha
    ;
    WITH V (ItemID, DetailItemID, PrevAmount, Amount, OperationSign)
             AS (SELECT ItemID,
                        DetailItemID,
                        PrevAmount,
                        Amount,
                        OperationSign
                 FROM @tblMasterDetail
                 WHERE Grade = -1
                 UNION ALL
                 SELECT B.ItemID,
                        B.DetailItemID,
                        V.PrevAmount,
                        V.Amount,
                        B.OperationSign * V.OperationSign AS OperationSign
                 FROM @tblMasterDetail B,
                      V
                 WHERE B.DetailItemID = V.ItemID
        )
         -- Lấy dữ liệu in báo cáo
    INSERT
    @Result
    SELECT FR.ItemID,
           FR.ItemCode,
           FR.ItemName,
           FR.ItemNameEnglish,
           FR.ItemIndex,
           FR.Description,
           FR.FormulaType,
           CASE
               WHEN FR.FormulaType = 1 THEN FR.FormulaFrontEnd
               ELSE ''
               END AS FormulaFrontEnd,
           CASE
               WHEN FR.FormulaType = 1 THEN FR.Formula
               ELSE NULL
               END AS Formula,
           FR.Hidden,
           FR.IsBold,
           FR.IsItalic,
           CASE
               WHEN FR.Formula IS NOT NULL THEN ISNULL(X.Amount, 0)
               ELSE X.Amount
               END AS Amount,
           CASE
               WHEN FR.Formula IS NOT NULL THEN ISNULL(X.PrevAmount, 0)
               ELSE X.PrevAmount
               END AS PrevAmount
    FROM (SELECT V.ItemID,
                 SUM(V.OperationSign * V.Amount)     AS Amount,
                 SUM(V.OperationSign * V.PrevAmount) AS PrevAmount
          FROM V
          GROUP BY ItemID
         ) AS X
             RIGHT JOIN dbo.FRTemplate FR ON FR.ItemID = X.ItemID
    WHERE AccountingSystem = @AccountingSystem
      AND ReportID = @ReportID
    ORDER BY ItemIndex

    -- Lấy dữ liệu từ sổ cái in báo cáo
    SELECT FR.ItemID,
           FR.ItemCode,
           FR.ItemName,
           ISNULL(FR.ItemNameEnglish, FR.ItemName) AS ItemNameEnglish,
           FR.ItemIndex,
           FR.Description,
           FR.FormulaType,
           FR.FormulaFrontEnd,
           FR.Formula,
           FR.Hidden,
           FR.IsBold,
           FR.IsItalic,
           ISNULL(FR.Amount, 0)                    as Amount,
           ISNULL(FR.PrevAmount, 0)                as PrevAmount
    FROM @Result FR
    WHERE FR.Hidden = 0
    ORDER BY ItemIndex
END
go

