/*
-- =============================================
-- Author:		<chuongnv>
-- Create date: <18-02-2020>
-- Description:	<Lấy dữ liệu bảng cân đối kế toán>
-- Proc_BANG_CAN_DOI_KE_TOAN '5A814271-A115-41D1-BA4D-C50BC0040482', 1, '01/01/2019 00:00:00', '12/31/2020 23:59:59', 0, 0, '01/01/2019 00:00:00', '12/31/2020 23:59:59'
-- =============================================
*/
CREATE PROCEDURE [dbo].[Proc_BANG_CAN_DOI_KE_TOAN](@CompanyID UNIQUEIDENTIFIER,
                                                   @TypeLedger INT,
                                                   @FromDate DATETIME,
                                                   @ToDate DATETIME,
                                                   @IncludeDependentBranch BIT,
                                                   @isPrintByYear BIT,
                                                   @PrevFromDate DATETIME,
                                                   @PrevToDate DATETIME)
AS
BEGIN

    DECLARE
        @Result TABLE
                (
                    ItemID          UNIQUEIDENTIFIER,
                    ItemCode        NVARCHAR(25),
                    ItemName        NVARCHAR(512),
                    ItemNameEnglish NVARCHAR(512),
                    ItemIndex       INT,
                    Description     NVARCHAR(512),
                    FormulaType     INT,
                    FormulaFrontEnd NVARCHAR(MAX),
                    Formula         XML,
                    Hidden          BIT,
                    IsBold          BIT,
                    IsItalic        BIT,
                    Category        INT,
                    SortOrder       INT,
                    Amount          DECIMAL(25, 4),
                    PrevAmount      DECIMAL(25, 4)
                )

    DECLARE
        @AccountingSystem INT
    SET @AccountingSystem = 15
    DECLARE
        @ReportID NVARCHAR(100)
    SET @ReportID = '1'
    DECLARE
        @ItemForeignCurrency UNIQUEIDENTIFIER
    DECLARE
        @ItemIndex INT
    DECLARE
        @MainCurrency NVARCHAR(3)
    SET @ItemForeignCurrency = '00000000-0000-0000-0000-000000000000'

    SET @ItemIndex = (SELECT TOP 1 ItemIndex
                      FROM FRTemplate
                      WHERE ItemID = @ItemForeignCurrency)
    SET @MainCurrency = ISNULL((SELECT TOP 1 CurrencyID FROM EbOrganizationUnit WHERE ID = @CompanyID), 'VND')
    DECLARE
        @tblItem TABLE
                 (
                     ItemID                     UNIQUEIDENTIFIER,
                     ItemIndex                  INT,
                     ItemName                   NVARCHAR(512),
                     OperationSign              INT,
                     OperandString              NVARCHAR(512),
                     AccountNumberPercent       NVARCHAR(25),
                     AccountNumber              NVARCHAR(25),
                     CorrespondingAccountNumber VARCHAR(25),
                     IsDetailByAO               INT,
                     AccountKind                INT
                 )

    INSERT @tblItem
    SELECT ItemID,
           ItemIndex,
           ItemName,
           x.r.value('@OperationSign', 'INT'),
           RTRIM(LTRIM(x.r.value('@OperandString',
                                 'nvarchar(512)'))),
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
               WHEN x.r.value('@OperandString',
                              'nvarchar(512)') LIKE '%ChitietTheoTKvaDoituong'
                   THEN 1
               ELSE CASE
                        WHEN x.r.value('@OperandString',
                                       'nvarchar(512)') LIKE '%ChitietTheoTK'
                            THEN 2
                        ELSE 0
                   END
               END,
           NULL
    FROM dbo.FRTemplate
             CROSS APPLY Formula.nodes('/root/DetailFormula')
        AS x (r)
    WHERE AccountingSystem = @AccountingSystem
      AND ReportID = @ReportID
      AND FormulaType = 0
      AND Formula IS NOT NULL
      AND ItemID <> @ItemForeignCurrency

    UPDATE @tblItem
    SET AccountKind = A.AccountGroupKind
    FROM dbo.AccountList A
    WHERE A.AccountNumber = [@tblItem].AccountNumber
      AND A.CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch))
      AND A.AccountingType = 0

    DECLARE
        @AccountBalance TABLE
                        (
                            AccountNumber       NVARCHAR(20),
                            AccountCategoryKind INT,
                            IsDetailByAO        INT
                        )

    INSERT @AccountBalance
    SELECT DISTINCT A.AccountNumber,
                    A.AccountGroupKind,
                    B.IsDetailByAO
    FROM dbo.AccountList A
             INNER JOIN @tblItem B ON A.AccountNumber LIKE B.AccountNumberPercent
    WHERE A.CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch))
      AND A.AccountingType = 0

    DECLARE
        @Balance TABLE
                 (
                     AccountNumber       NVARCHAR(20),
                     AccountCategoryKind INT,
                     AccountObjectID     UNIQUEIDENTIFIER,
                     IsDetailByAO        INT,
                     OpeningDebit        DECIMAL(25, 4),
                     OpeningCredit       DECIMAL(25, 4),
                     ClosingDebit        DECIMAL(25, 4),
                     ClosingCredit       DECIMAL(25, 4),
                     BranchID            UNIQUEIDENTIFIER
                 )
    DECLARE
        @GeneralLedger TABLE
                       (
                           AccountNumber   NVARCHAR(20),
                           AccountObjectID UNIQUEIDENTIFIER,
                           BranchID        UNIQUEIDENTIFIER,
                           IsOPN           BIT,
                           DebitAmount     DECIMAL(25, 4),
                           CreditAmount    DECIMAL(25, 4)
                       )

    INSERT INTO @GeneralLedger
    (AccountNumber,
     AccountObjectID,
     BranchID,
     IsOPN,
     DebitAmount,
     CreditAmount)
    SELECT GL.Account,
           GL.AccountingObjectID,
           GL.BranchID,
           CASE
               WHEN GL.PostedDate < @FromDate THEN 1
               ELSE 0
               END           AS IsOPN,
           SUM(DebitAmount)  AS DebitAmount,
           SUM(CreditAmount) AS CreditAmount
    FROM dbo.GeneralLedger GL
    WHERE GL.PostedDate <= @ToDate
      AND GL.CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch))
      AND (GL.TypeLedger = @TypeLedger OR GL.TypeLedger = 2)
    GROUP BY GL.Account,
             GL.AccountingObjectID,
             GL.BranchID,
             CASE
                 WHEN GL.PostedDate < @FromDate THEN 1
                 ELSE 0
                 END


    DECLARE
        @StartDate DATETIME
    SET @StartDate = @FromDate

    DECLARE
        @GeneralLedger421 TABLE
                          (
                              OperandString NVARCHAR(255),
                              ClosingAmount DECIMAL(25, 4),
                              OpeningAmount DECIMAL(25, 4)
                          )


    /*


Đối với báo cáo kỳ khác năm:
*Cuối kỳ: (Dư Có – Dư Nợ) TK 4211 trên sổ cái tính đến Từ ngày -1 + (Dư Có – Dư Nợ) TK 4212 trên sổ cái tính đến Từ ngày -1
* Đầu kỳ:
(Dư Có – Dư Nợ) TK 4211 trên sổ cái tính đến từ ngày -1 của kỳ trước liền kề+ (Dư Có – Dư Nợ) TK 4212 trên sổ cái tính đến Từ ngày -1 của kỳ trước liền kề.
→ Nếu Từ ngày của kỳ trùng với ngày bắt đầu kỳ kế toán năm (theo năm tài chính trên hệ thống,
VD: kỳ năm tài chính bắt đầu từ 01/10 thì ngày bắt đầu kỳ kế toán năm sẽ là 01/10) thì đầu kỳ sẽ lấy theo công thức: Dư Có TK 4211 – Dư Nợ TK 4211 trên Sổ cái tính đến Từ ngày -1

     */
    /*- Thêm điều kiện chọn theo năm hoặc khác năm.*/
    INSERT INTO @GeneralLedger421
    (OperandString,
     ClosingAmount,
     OpeningAmount)
    SELECT N'Solieuchitieu421a_Ky_khac_nam' AS AccountNumber,
           ISNULL(SUM(CASE
                          WHEN GL.Account LIKE '4211%'
                              AND GL.PostedDate < @FromDate
                              THEN CreditAmount - DebitAmount
                          ELSE 0
               END), 0)
               + ISNULL(SUM(CASE
                                WHEN GL.Account LIKE '4212%'
                                    AND GL.PostedDate < @FromDate
                                    THEN CreditAmount - DebitAmount
                                ELSE 0
               END), 0)
                                            AS ClosingAmount,

           ISNULL(SUM(
                          CASE
                              WHEN DAY(@StartDate) = DAY(@FromDate) AND MONTH(@StartDate) = MONTH(@FromDate) THEN 0
                              ELSE
                                  CASE
                                      WHEN GL.Account LIKE '4211%'
                                          AND GL.PostedDate < @PrevFromDate
                                          THEN CreditAmount - DebitAmount
                                      ELSE 0
                                      END
                              end
                      ), 0)
               + SUM(CASE
                         WHEN DAY(@StartDate) = DAY(@FromDate) AND MONTH(@StartDate) = MONTH(@FromDate) THEN 0
                         ELSE CASE
                                  WHEN GL.Account LIKE '4212%'
                                      AND GL.PostedDate < @PrevFromDate
                                      THEN CreditAmount - DebitAmount
                                  ELSE 0
                             END
               END)
               + ISNULL(SUM(CASE
                                WHEN DAY(@StartDate) = DAY(@FromDate) AND MONTH(@StartDate) = MONTH(@FromDate)
                                    AND GL.Account LIKE '4211%'
                                    THEN CASE WHEN GL.PostedDate < @FromDate then CreditAmount - DebitAmount ELSE 0 end
                                ELSE 0
               END)
               , 0)                         AS OpeningAmount

    FROM dbo.GeneralLedger GL
    WHERE GL.PostedDate <= @ToDate
      AND GL.CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch))
      AND (GL.TypeLedger = @TypeLedger OR GL.TypeLedger = 2)
      AND gl.Account LIKE '421%'

    /*


Đối với báo cáo kỳ khác năm:
* Cuối kỳ: PS Có TK 4212 trên sổ cái trong kỳ báo cáo (không bao gồm ĐU N4211/Có TK 4212) - PSN TK 4212 trên sổ cái trong kỳ báo cáo (không bao gồm ĐU N4212/Có TK 4211)
* Đầu kỳ:
PS Có TK 4212 trên sổ cái trong kỳ trước liền kề (không bao gồm ĐU N4211/Có TK 4212) – PS Nợ TK 4212 trên sổ cái trong kỳ trước liền kề (không bao gồm ĐU N4212/Có TK 4211)
→ Nếu Từ ngày của kỳ trùng với ngày bắt đầu kỳ kế toán năm (theo năm tài chính trên hệ thống, VD: kỳ năm tài chính bắt đầu từ 01/10 thì ngày bắt đầu kỳ kế toán năm sẽ là 01/10) thì đầu kỳ sẽ lấy theo công thức: Dư Có TK 4212 – Dư Nợ TK 4212 trên Sổ cái tính đến Từ ngày -1

    */
    /*- Thêm điều kiện chọn theo năm hoặc khác năm.*/

    INSERT INTO @GeneralLedger421
    (OperandString,
     ClosingAmount,
     OpeningAmount)
    SELECT N'Solieuchitieu421b_Ky_khac_nam' AS AccountNumber,
           ISNULL(SUM(CASE
                          WHEN GL.Account LIKE '4212%'
                              AND GL.PostedDate BETWEEN @fromdate AND @ToDate
                              THEN (CASE
                                        WHEN GL.AccountCorresponding LIKE N'4211%'
                                            THEN 0
                                        ELSE CreditAmount
                              END)
                          ELSE 0
               END), 0)
               - ISNULL(SUM(CASE
                                WHEN GL.Account LIKE '4212%'
                                    AND GL.PostedDate BETWEEN @Fromdate AND @ToDate
                                    THEN (CASE
                                              WHEN GL.AccountCorresponding LIKE N'4211%'
                                                  THEN 0
                                              ELSE DebitAmount
                                    END)
                                ELSE 0
               END), 0)                     AS ClosingAmount,
        /** Đầu kỳ:
PS Có TK 4212 trên sổ cái trong kỳ trước liền kề (không bao gồm ĐU N4211/Có TK 4212) – PS Nợ TK 4212 trên sổ cái trong kỳ trước liền kề (không bao gồm ĐU N4212/Có TK 4211)
→ Nếu Từ ngày của kỳ trùng với ngày bắt đầu kỳ kế toán năm (theo năm tài chính trên hệ thống, VD: kỳ năm tài chính bắt đầu từ 01/10 thì ngày bắt đầu kỳ kế toán năm sẽ là 01/10)
thì đầu kỳ sẽ lấy theo công thức: Dư Có TK 4212 – Dư Nợ TK 4212 trên Sổ cái tính đến Từ ngày -1
*/
           ISNULL(SUM(CASE
                          WHEN DAY(@StartDate) = DAY(@FromDate) AND MONTH(@StartDate) = MONTH(@FromDate) THEN 0
                          ELSE (CASE
                                    WHEN GL.Account LIKE '4212%'
                                        AND GL.PostedDate BETWEEN @PrevFromDate
                                             AND
                                             @PrevToDate
                                        THEN (CASE
                                                  WHEN GL.AccountCorresponding LIKE N'4211%'
                                                      THEN 0
                                                  ELSE CreditAmount
                                        END)
                                    ELSE 0
                              END)
               END), 0)
               - ISNULL(SUM(CASE
                                WHEN DAY(@StartDate) = DAY(@FromDate) AND MONTH(@StartDate) = MONTH(@FromDate) THEN 0
                                ELSE CASE
                                         WHEN GL.Account LIKE '4212%'
                                             AND GL.PostedDate BETWEEN @PrevFromDate
                                                  AND
                                                  @PrevToDate
                                             THEN (CASE
                                                       WHEN GL.AccountCorresponding LIKE N'4211%'
                                                           THEN 0
                                                       ELSE DebitAmount
                                             END)
                                         ELSE 0
                                    END
               END), 0)
               + /*nếu không  là kỳ đầu tiên
		→ Nếu Từ ngày của kỳ trùng với ngày bắt đầu kỳ kế toán năm (theo năm tài chính trên hệ thống,
		VD: kỳ năm tài chính bắt đầu từ 01/10 thì ngày bắt đầu kỳ kế toán năm sẽ là 01/10) thì đầu kỳ sẽ lấy theo công thức: Dư Có TK 4212 – Dư Nợ TK 4212 trên Sổ cái tính đến Từ ngày -1
		*/
           ISNULL(SUM(CASE
                          WHEN DAY(@StartDate) = DAY(@FromDate) AND MONTH(@StartDate) = MONTH(@FromDate)
                              AND GL.Account LIKE '4212%'
                              THEN CASE WHEN GL.PostedDate < @FromDate then CreditAmount - DebitAmount ELSE 0 end
                          ELSE 0
               END), 0)                     AS OpeningAmount
    FROM dbo.GeneralLedger GL
    WHERE GL.PostedDate <= @ToDate
      AND GL.CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch))
      AND (GL.TypeLedger = @TypeLedger OR GL.TypeLedger = 2)
      AND gl.Account LIKE '421%'

    INSERT INTO @Balance
    SELECT GL.AccountNumber,
           A.AccountCategoryKind,
           CASE
               WHEN A.IsDetailByAO = 1
                   THEN GL.AccountObjectID
               ELSE NULL
               END AS                      AccountObjectID,
           A.IsDetailByAO,
           SUM(CASE
                   WHEN GL.IsOPN = 1
                       THEN DebitAmount - CreditAmount
                   ELSE 0
               END)                        OpeningDebit,
           0,
           SUM(DebitAmount - CreditAmount) ClosingDebit,
           0
            ,
           CASE
               WHEN A.IsDetailByAO = 1
                   AND @IncludeDependentBranch = 0
                   THEN GL.BranchID
               ELSE NULL
               END AS                      BranchID
    FROM @GeneralLedger GL
             INNER JOIN @AccountBalance A ON GL.AccountNumber = A.AccountNumber
    GROUP BY GL.AccountNumber,
             A.AccountCategoryKind,
             CASE
                 WHEN A.IsDetailByAO = 1
                     THEN GL.AccountObjectID
                 ELSE NULL
                 END,
             A.IsDetailByAO
            ,
             CASE
                 WHEN A.IsDetailByAO = 1
                     AND @IncludeDependentBranch = 0
                     THEN GL.BranchID
                 ELSE NULL
                 END
    OPTION ( RECOMPILE )
    UPDATE @Balance
    SET OpeningCredit = -OpeningDebit,
        OpeningDebit  = 0
    WHERE AccountCategoryKind = 1
       OR (AccountCategoryKind NOT IN (0, 1)
        AND OpeningDebit < 0
        )

    UPDATE @Balance
    SET ClosingCredit = -ClosingDebit,
        ClosingDebit  = 0
    WHERE AccountCategoryKind = 1
       OR (AccountCategoryKind NOT IN (0, 1)
        AND ClosingDebit < 0
        )

    DECLARE
        @tblMasterDetail TABLE
                         (
                             ItemID        UNIQUEIDENTIFIER,
                             DetailItemID  UNIQUEIDENTIFIER,
                             OperationSign INT,
                             Grade         INT,
                             OpeningAmount DECIMAL(25, 4),
                             ClosingAmount DECIMAL(25, 4)
                         )


    INSERT INTO @tblMasterDetail
    SELECT I.ItemID,
           NULL,
           1,
           -1
            ,
           SUM((CASE I.OperandString
                    WHEN 'DUNO'
                        THEN
                        CASE I.AccountKind
                            WHEN 1 THEN 0
                            WHEN 0
                                THEN I.OpeningDebit
                                - I.OpeningCredit
                            ELSE CASE
                                     WHEN I.OpeningDebit
                                              - I.OpeningCredit > 0
                                         THEN I.OpeningDebit
                                         - I.OpeningCredit
                                     ELSE 0
                                END
                            END
               /*- Thêm điều kiện chọn theo năm hoặc khác năm.*/
                    WHEN 'DUNO_ky_nam'
                        THEN
                        CASE
                            WHEN @isPrintByYear = 1
                                THEN CASE I.AccountKind
                                         WHEN 1 THEN 0
                                         WHEN 0
                                             THEN I.OpeningDebit
                                             - I.OpeningCredit
                                         ELSE CASE
                                                  WHEN I.OpeningDebit
                                                           - I.OpeningCredit > 0
                                                      THEN I.OpeningDebit
                                                      - I.OpeningCredit
                                                  ELSE 0
                                             END
                                END
                            ELSE 0
                            END
                    WHEN 'DUCO'
                        THEN CASE I.AccountKind
                                 WHEN 0 THEN 0
                                 WHEN 1
                                     THEN I.OpeningCredit
                                     - I.OpeningDebit
                                 ELSE CASE
                                          WHEN I.OpeningCredit
                                                   - I.OpeningDebit > 0
                                              THEN I.OpeningCredit
                                              - I.OpeningDebit
                                          ELSE 0
                                     END
                        END
               /* Thêm điều kiện chọn theo năm hoặc khác năm.*/
                    WHEN 'DUCO_ky_nam'
                        THEN CASE
                                 WHEN @isPrintByYear = 1
                                     THEN CASE I.AccountKind
                                              WHEN 0 THEN 0
                                              WHEN 1
                                                  THEN I.OpeningCredit
                                                  - I.OpeningDebit
                                              ELSE CASE
                                                       WHEN I.OpeningCredit
                                                                - I.OpeningDebit > 0
                                                           THEN I.OpeningCredit
                                                           - I.OpeningDebit
                                                       ELSE 0
                                                  END
                                     END
                                 ELSE 0
                        END
                    WHEN 'Solieuchitieu421a_Ky_khac_nam'
                        THEN CASE
                                 WHEN @isPrintByYear = 0
                                     THEN C.OpeningAmount
                                 ELSE 0
                        END
                    WHEN 'Solieuchitieu421b_Ky_khac_nam'
                        THEN CASE
                                 WHEN @isPrintByYear = 0
                                     THEN C.OpeningAmount
                                 ELSE 0
                        END
               /* - Thêm điều kiện chọn theo năm hoặc khác năm.*/
                    ELSE CASE
                             WHEN LEFT(I.OperandString, 4) = 'DUNO'
                                 THEN I.OpeningDebit
                             ELSE I.OpeningCredit
                        END
               END) * I.OperationSign) AS OpeningAmount,
           SUM((CASE I.OperandString
                    WHEN 'DUNO'
                        THEN CASE I.Accountkind
                                 WHEN 1 THEN 0
                                 WHEN 0
                                     THEN I.ClosingDebit
                                     - I.ClosingCredit
                                 ELSE CASE
                                          WHEN I.ClosingDebit
                                                   - I.ClosingCredit > 0
                                              THEN I.ClosingDebit
                                              - I.ClosingCredit
                                          ELSE 0
                                     END
                        END
               /*- Thêm điều kiện chọn theo năm hoặc khác năm.*/
                    WHEN 'DUNO_ky_nam'
                        THEN CASE
                                 WHEN @isPrintByYear = 1
                                     THEN CASE I.Accountkind
                                              WHEN 1 THEN 0
                                              WHEN 0
                                                  THEN I.ClosingDebit
                                                  - I.ClosingCredit
                                              ELSE CASE
                                                       WHEN I.ClosingDebit
                                                                - I.ClosingCredit > 0
                                                           THEN I.ClosingDebit
                                                           - I.ClosingCredit
                                                       ELSE 0
                                                  END
                                     END
                                 ELSE 0
                        END
                    WHEN 'DUCO'
                        THEN CASE I.AccountKind
                                 WHEN 0 THEN 0
                                 WHEN 1
                                     THEN I.ClosingCredit
                                     - I.ClosingDebit
                                 ELSE CASE
                                          WHEN I.ClosingCredit
                                                   - I.ClosingDebit > 0
                                              THEN I.ClosingCredit
                                              - I.ClosingDebit
                                          ELSE 0
                                     END
                        END
               /*- Thêm điều kiện chọn theo năm hoặc khác năm.*/
                    WHEN 'DUCO_ky_nam'
                        THEN CASE
                                 WHEN @isPrintByYear = 1
                                     THEN CASE I.AccountKind
                                              WHEN 0 THEN 0
                                              WHEN 1
                                                  THEN I.ClosingCredit
                                                  - I.ClosingDebit
                                              ELSE CASE
                                                       WHEN I.ClosingCredit
                                                                - I.ClosingDebit > 0
                                                           THEN I.ClosingCredit
                                                           - I.ClosingDebit
                                                       ELSE 0
                                                  END
                                     END
                                 ELSE 0
                        END
                    WHEN 'Solieuchitieu421a_Ky_khac_nam'
                        THEN CASE
                                 WHEN @isPrintByYear = 0
                                     THEN C.ClosingAmount
                                 ELSE 0
                        END
                    WHEN 'Solieuchitieu421b_Ky_khac_nam'
                        THEN CASE
                                 WHEN @isPrintByYear = 0
                                     THEN C.ClosingAmount
                                 ELSE 0
                        END
               /* - Thêm điều kiện chọn theo năm hoặc khác năm.*/
                    ELSE CASE
                             WHEN LEFT(I.OperandString, 4) = 'DUNO'
                                 THEN I.ClosingDebit
                             ELSE I.ClosingCredit
                        END
               END) * I.OperationSign) AS ClosingAmount
    FROM (SELECT I.ItemID,
                 I.OperandString,
                 I.OperationSign,
                 I.AccountNumber,
                 I.AccountKind,
                 SUM(B.OpeningDebit)  AS OpeningDebit,
                 SUM(B.OpeningCredit) AS OpeningCredit,
                 SUM(B.ClosingDebit)  AS ClosingDebit,
                 SUM(B.ClosingCredit) AS ClosingCredit
          FROM @tblItem I
                   INNER JOIN @Balance B ON B.AccountNumber LIKE I.AccountNumberPercent
              AND B.IsDetailByAO = I.IsDetailByAO
          GROUP BY I.ItemID,
                   I.OperandString,
                   I.OperationSign,
                   I.AccountNumber,
                   I.AccountKind
         ) I
             /*- Thêm điều kiện chọn theo năm hoặc khác năm.*/
             LEFT JOIN @GeneralLedger421 C ON I.OperandString = C.OperandString
    GROUP BY I.ItemID

    INSERT @tblMasterDetail
    SELECT ItemID,
           x.r.value('@ItemID', 'NVARCHAR(100)'),
           x.r.value('@OperationSign', 'INT'),
           0,
           0.0,
           0.0
    FROM dbo.FRTemplate
             CROSS APPLY Formula.nodes('/root/MasterFormula')
        AS x (r)
    WHERE AccountingSystem = @AccountingSystem
      AND ReportID = @ReportID
      AND FormulaType = 1
      AND Formula IS NOT NULL;
    WITH V (ItemID, DetailItemID, OpeningAmount, ClosingAmount, OperationSign)
             AS (SELECT ItemID,
                        DetailItemID,
                        OpeningAmount,
                        ClosingAmount,
                        OperationSign
                 FROM @tblMasterDetail
                 WHERE Grade = -1
                 UNION ALL
                 SELECT B.ItemID,
                        B.DetailItemID,
                        V.OpeningAmount,
                        V.ClosingAmount,
                        B.OperationSign * V.OperationSign AS OperationSign
                 FROM @tblMasterDetail B,
                      V
                 WHERE B.DetailItemID = V.ItemID
        )
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
               END                 AS FormulaFrontEnd,
           CASE
               WHEN FR.FormulaType = 1 THEN FR.Formula
               ELSE NULL
               END                 AS Formula,
           FR.Hidden,
           FR.IsBold,
           FR.IsItalic,
           FR.Category,
           -1                      AS SortOrder,
           ISNULL(X.Amount, 0)     AS Amount,
           ISNULL(X.PrevAmount, 0) AS PrevAmount
    FROM (SELECT ItemID,
                 SUM(V.OperationSign * V.OpeningAmount) AS PrevAmount,
                 SUM(V.OperationSign * V.ClosingAmount) AS Amount
          FROM V
          GROUP BY ItemID
         ) AS X
             RIGHT JOIN FRTemplate FR ON FR.ItemID = X.ItemID
    WHERE AccountingSystem = @AccountingSystem
      AND ReportID = @ReportID


    SELECT *
    FROM @Result
    order by ItemIndex
end
go

