-- =============================================
-- Author:		namnh
-- Create date: 02/06/2020
-- Description:	Thẻ tính giá thành
-- Proc_THE_TINH_GIA_THANH 'CAA09998-422C-C742-BF50-F26B2548BAEE',',5b3b6ef6-8c76-3543-950b-e962fbb0fb16,f935773d-2f8f-544f-b4ca-96b439875b3e,f935773d-2f8f-544f-b4ca-96b439875b3e,e5a3bcdd-8726-0f4d-b2f9-b68084175931,5b3b6ef6-8c76-3543-950b-e962fbb0fb16,2a449478-3660-b840-bb88-5019f77af242,,',',c67c2603-f75d-8246-b2c7-8488ad091652,f8044ebc-c437-e549-bdf6-4cd641de8ba4,c67c2603-f75d-8246-b2c7-8488ad091652,f8044ebc-c437-e549-bdf6-4cd641de8ba4,8cfcc3ec-116d-4344-9268-b15493a1d556,8cfcc3ec-116d-4344-9268-b15493a1d556,', '2D2956D7-575C-A842-8CF8-EB368DF4A465', 0,0
-- =============================================
    ALTER PROCEDURE [dbo].[Proc_THE_TINH_GIA_THANH](@CompanyID UNIQUEIDENTIFIER,
    @ListMaterialGoodsID NVARCHAR(3000),
    @ListCostSetID NVARCHAR(3000),
    @CPPeriodID UNIQUEIDENTIFIER,
    @TypeLedger INT,
    @TypeMethod INT)
    -- TypeLedger = 0 - Sổ tài chính
    -- TypeLedger = 1 - Sổ quản trị
    AS
    BEGIN

        DECLARE @CostSetMaterialGoods TABLE
                                      (
                                          MaterialGoodsID UNIQUEIDENTIFIER,
                                          CostSetID       UNIQUEIDENTIFIER
                                      )


        CREATE TABLE #Result
        (
            CostsetID          UNIQUEIDENTIFIER,
            QuantumID          UNIQUEIDENTIFIER,
            Target             NVARCHAR(512),
            TotalAmount        DECIMAL(25, 4),
            TotalAmountNLVL    DECIMAL(25, 4),
            TotalAmountNCTT    DECIMAL(25, 4),
            TotalAmountCPSDMTC DECIMAL(25, 4),
            TotalAmountCPSXC   DECIMAL(25, 4),
            Row                INT
        )


        DECLARE @CostSet TABLE
                         (
                             ID UNIQUEIDENTIFIER
                         )

        INSERT INTO @CostSet
        SELECT TG.ID
        FROM CostSet AS TG
                 INNER JOIN Func_ConvertStringIntoTable_Nvarchar(@ListCostSetID, ',') AS CostSet
                            ON TG.ID = CostSet.Value
        WHERE CostSet.Value IS NOT NULL
          AND CompanyID = @CompanyID


        DECLARE @MaterialGoods TABLE
                               (
                                   ID UNIQUEIDENTIFIER
                               )

        INSERT INTO @MaterialGoods
        SELECT TG.ID
        FROM MaterialGoods AS TG
                 INNER JOIN Func_ConvertStringIntoTable_Nvarchar(@ListMaterialGoodsID, ',') AS MaterialGoods
                            ON TG.ID = MaterialGoods.Value
        WHERE MaterialGoods.Value IS NOT NULL
          AND CompanyID = @CompanyID

        IF (@TypeMethod <> 5)
            BEGIN
                INSERT INTO @CostSetMaterialGoods
                SELECT a.MaterialGoodsID, CostSetID
                FROM CostSetMaterialGoods a
                         LEFT JOIN CostSet b ON a.CostSetID = b.ID
                         LEFT JOIN MaterialGoods c ON a.MaterialGoodsID = c.ID
                WHERE a.MaterialGoodsID IN (SELECT ID FROM @MaterialGoods)
                  AND CostSetID IN (SELECT ID FROM @CostSet)
                  AND b.CompanyID = @CompanyID
                  AND c.CompanyID = @CompanyID

                DECLARE @MaterialGoodsID UNIQUEIDENTIFIER
                DECLARE @CostSetID UNIQUEIDENTIFIER
                DECLARE @COUNT INT = (SELECT COUNT(*) FROM @CostSetMaterialGoods)

                WHILE(@COUNT > 0)
                    BEGIN
                        SET @MaterialGoodsID = (SELECT TOP 1 MaterialGoodsID FROM @CostSetMaterialGoods)
                        SET @CostSetID = (SELECT TOP 1 CostSetID FROM @CostSetMaterialGoods)
                        DECLARE @ToDate DATE = (SELECT ToDate
                                                FROM CPPeriod a
                                                         LEFT JOIN CPPeriodDetail b ON a.ID = b.CPPeriodID
                                                WHERE a.ID = @CPPeriodID
                                                  AND a.CompanyID = @CompanyID
                                                  AND b.CostSetID = @CostSetID
                                                  AND b.CPPeriodID = @CPPeriodID)
                        DECLARE @FromDate DATE = (SELECT FromDate
                                                  FROM CPPeriod a
                                                           LEFT JOIN CPPeriodDetail b ON a.ID = b.CPPeriodID
                                                  WHERE a.ID = @CPPeriodID
                                                    AND a.CompanyID = @CompanyID
                                                    AND b.CostSetID = @CostSetID
                                                    AND b.CPPeriodID = @CPPeriodID)
                        DECLARE @MinToDate DATE = (SELECT MIN(ToDate)
                                                   FROM CPPeriod a
                                                            LEFT JOIN CPPeriodDetail b ON a.ID = b.CPPeriodID
                                                   WHERE a.CompanyID = @CompanyID
                                                     AND b.CostSetID = @CostSetID)
                        DECLARE @PreviousToDate DATE = (SELECT TOP 1 ToDate
                                                        FROM CPPeriod a
                                                                 LEFT JOIN CPPeriodDetail b ON a.ID = b.CPPeriodID
                                                        WHERE ToDate < @FromDate
                                                          AND b.CostSetID = @CostSetID
                                                          AND CompanyID = @CompanyID)
                        DECLARE @PreviousCPPeriodID UNIQUEIDENTIFIER = (SELECT ID
                                                                        FROM CPPeriod
                                                                        WHERE ToDate = @PreviousToDate
                                                                          AND CompanyID = @CompanyID)
                        -- Insert dữ liệu chỉ tiêu 1
                        IF (@ToDate > @MinToDate)
                            BEGIN
                                INSERT INTO #Result
                                SELECT a.CostSetID                                       as CostSetID,
                                       c.QuantumID                                       as QuantumID,
                                       N'1. Chi phí SXKD dở dang đầu kỳ'                 as Target,
                                       a.TotalCostAmount                                 as TotalAmount,
                                       a.DirectMatetialAmount                            as TotalAmountNLVL,
                                       a.DirectLaborAmount                               as TotalAmountNCTT,
                                       (a.MachineMatetialAmount + a.MachineLaborAmount + a.MachineToolsAmount +
                                        a.MachineDepreciationAmount +
                                        a.MachineServiceAmount + a.MachineGeneralAmount) as TotalAmountCPSDMTC,
                                       (a.GeneralMatetialAmount + a.GeneralLaborAmount + a.GeneralToolsAmount +
                                        a.GeneralDepreciationAmount +
                                        a.GeneralServiceAmount + a.OtherGeneralAmount)   as TotalAmountCPSXC,
                                       1                                                 as Row
                                FROM CPUncomplete c
                                         LEFT JOIN CPPeriod b ON b.ID = c.CPPeriodID
                                         LEFT JOIN CPUncompleteDetail a
                                                   ON a.CPPeriodID = b.ID
                                WHERE a.CostSetID = @CostSetID
                                  AND b.CompanyID = @CompanyID
                                  AND a.CPPeriodID = @PreviousCPPeriodID
                                  AND c.CPPeriodID = @PreviousCPPeriodID
                                  AND c.QuantumID = @MaterialGoodsID
                            END
                        ELSE
                            BEGIN
                                INSERT INTO #Result
                                SELECT a.CostSetID                                          as CostSetID,
                                       b.MaterialGoodsID                                    as QuantumID,
                                       CASE
                                           WHEN @TypeMethod = 0 OR @TypeMethod = 1 OR @TypeMethod = 2
                                               THEN N'1. Chi phí SXKD dở dang đầu kỳ'
                                           ELSE N'1. Lũy kế chi phí phát sinh kỳ trước' END as Target,
                                       a.TotalCostAmount                                    as TotalAmount,
                                       a.DirectMaterialAmount                               as TotalAmountNLVL,
                                       a.DirectLaborAmount                                  as TotalAmountNCTT,
                                       (a.MachineMaterialAmount + a.MachineLaborAmount + a.MachineToolsAmount +
                                        a.MachineDepreciationAmount +
                                        a.MachineServiceAmount + a.MachineGeneralAmount)    as TotalAmountCPSDMTC,
                                       (a.GeneralMaterialAmount + a.GeneralLaborAmount + a.GeneralToolsAmount +
                                        a.GeneralDepreciationAmount +
                                        a.GeneralServiceAmount + a.OtherGeneralAmount)      as TotalAmountCPSXC,
                                       1                                                    as Row
                                FROM @CostSetMaterialGoods b
                                         LEFT JOIN MaterialGoods c ON c.ID = b.MaterialGoodsID
                                         LEFT JOIN CPOPN a ON b.CostSetID = a.CostSetID
                                WHERE a.CostSetID = @CostSetID
                                  AND a.CompanyID = @CompanyID
                                  AND c.CompanyID = @CompanyID
                                  AND b.MaterialGoodsID = @MaterialGoodsID
                            END

                        -- Insert dữ liệu chỉ tiêu 2
                        DECLARE @TotalAmount DECIMAL(25, 4)
                        DECLARE @TotalAmountNLVL DECIMAL(25, 4) = ISNULL((ISNULL((SELECT Sum(Amount)
                                                                                  FROM CPExpenseList a
                                                                                  WHERE a.TypeVoucher = 0
                                                                                    AND a.AccountNumber = '621'
                                                                                    AND a.CostSetID = @CostSetID
                                                                                    AND a.CPPeriodID = @CPPeriodID),
                                                                                 0) +
                                                                          ISNULL((SELECT Sum(Amount)
                                                                                  FROM CPExpenseList a
                                                                                           LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                  WHERE a.TypeVoucher = 0
                                                                                    AND b.ExpenseType = 0
                                                                                    AND a.AccountNumber = '154'
                                                                                    AND a.CostSetID = @CostSetID
                                                                                    AND a.CPPeriodID = @CPPeriodID),
                                                                                 0) +
                                                                          ISNULL((SELECT SUM(a.AllocatedAmount)
                                                                                  FROM CPAllocationGeneralExpenseDetail a
                                                                                           LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                  WHERE b.ExpenseType = 0
                                                                                    AND a.CostSetID = @CostSetID
                                                                                    AND a.CPPeriodID = @CPPeriodID
                                                                                    AND a.AccountNumber = '154'), 0) +
                                                                          ISNULL((SELECT SUM(a.AllocatedAmount)
                                                                                  FROM CPAllocationGeneralExpenseDetail a
                                                                                           LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                  WHERE a.CostSetID = @CostSetID
                                                                                    AND a.CPPeriodID = @CPPeriodID
                                                                                    AND a.AccountNumber = '621'), 0) -
                                                                          ISNULL((SELECT Sum(Amount)
                                                                                  FROM CPExpenseList a
                                                                                           LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                  WHERE a.TypeVoucher = 1
                                                                                    AND b.ExpenseType = 0
                                                                                    AND a.AccountNumber = '154'
                                                                                    AND a.CostSetID = @CostSetID
                                                                                    AND a.CPPeriodID = @CPPeriodID),
                                                                                 0) -
                                                                          ISNULL((SELECT Sum(Amount)
                                                                                  FROM CPExpenseList a
                                                                                  WHERE a.TypeVoucher = 1
                                                                                    AND a.AccountNumber = '621'
                                                                                    AND a.CostSetID = @CostSetID
                                                                                    AND a.CPPeriodID = @CPPeriodID),
                                                                                 0)), 0)
                        DECLARE @TotalAmountNCTT DECIMAL(25, 4) = ISNULL(ISNULL((SELECT Sum(Amount)
                                                                                 FROM CPExpenseList a
                                                                                 WHERE a.TypeVoucher = 0
                                                                                   AND a.AccountNumber = '622'
                                                                                   AND a.CostSetID = @CostSetID
                                                                                   AND a.CPPeriodID = @CPPeriodID), 0) +
                                                                         (ISNULL((SELECT Sum(Amount)
                                                                                  FROM CPExpenseList a
                                                                                           LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                  WHERE a.TypeVoucher = 0
                                                                                    AND b.ExpenseType = 1
                                                                                    AND a.AccountNumber = '154'
                                                                                    AND a.CostSetID = @CostSetID
                                                                                    AND a.CPPeriodID = @CPPeriodID),
                                                                                 0) +
                                                                          ISNULL((SELECT SUM(a.AllocatedAmount)
                                                                                  FROM CPAllocationGeneralExpenseDetail a
                                                                                           LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                  WHERE b.ExpenseType = 1
                                                                                    AND a.CostSetID = @CostSetID
                                                                                    AND a.CPPeriodID = @CPPeriodID
                                                                                    AND a.AccountNumber = '154'), 0) +
                                                                          ISNULL((SELECT SUM(a.AllocatedAmount)
                                                                                  FROM CPAllocationGeneralExpenseDetail a
                                                                                           LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                  WHERE a.CostSetID = @CostSetID
                                                                                    AND a.CPPeriodID = @CPPeriodID
                                                                                    AND a.AccountNumber = '622'), 0) -
                                                                          ISNULL((SELECT Sum(Amount)
                                                                                  FROM CPExpenseList a
                                                                                           LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                  WHERE a.TypeVoucher = 1
                                                                                    AND b.ExpenseType = 1
                                                                                    AND a.AccountNumber = '154'
                                                                                    AND a.CostSetID = @CostSetID
                                                                                    AND a.CPPeriodID = @CPPeriodID),
                                                                                 0) -
                                                                          ISNULL((SELECT Sum(Amount)
                                                                                  FROM CPExpenseList a
                                                                                  WHERE a.TypeVoucher = 1
                                                                                    AND a.AccountNumber = '622'
                                                                                    AND a.CostSetID = @CostSetID
                                                                                    AND a.CPPeriodID = @CPPeriodID),
                                                                                 0)), 0)
                        DECLARE @TotalAmountCPSDMTC DECIMAL(25, 4) = ISNULL((ISNULL((SELECT Sum(Amount)
                                                                                     FROM CPExpenseList a
                                                                                              LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                     WHERE a.TypeVoucher = 0
                                                                                       AND a.AccountNumber = '623'
                                                                                       AND a.CostSetID = @CostSetID
                                                                                       AND a.CPPeriodID = @CPPeriodID),
                                                                                    0) +
                                                                             ISNULL((SELECT Sum(Amount)
                                                                                     FROM CPExpenseList a
                                                                                              LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                     WHERE a.TypeVoucher = 0
                                                                                       AND b.ExpenseType IN (2, 3, 4, 5, 6, 7)
                                                                                       AND a.AccountNumber = '154'
                                                                                       AND a.CostSetID = @CostSetID
                                                                                       AND a.CPPeriodID = @CPPeriodID),
                                                                                    0) +
                                                                             ISNULL((SELECT SUM(a.AllocatedAmount)
                                                                                     FROM CPAllocationGeneralExpenseDetail a
                                                                                              LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                     WHERE b.ExpenseType IN (2, 3, 4, 5, 6, 7)
                                                                                       AND a.CostSetID = @CostSetID
                                                                                       AND a.CPPeriodID = @CPPeriodID
                                                                                       AND a.AccountNumber = '154'),
                                                                                    0) +
                                                                             ISNULL((SELECT SUM(a.AllocatedAmount)
                                                                                     FROM CPAllocationGeneralExpenseDetail a
                                                                                              LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                     WHERE a.CostSetID = @CostSetID
                                                                                       AND a.CPPeriodID = @CPPeriodID
                                                                                       AND a.AccountNumber = '623'),
                                                                                    0) -
                                                                             ISNULL((SELECT Sum(Amount)
                                                                                     FROM CPExpenseList a
                                                                                              LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                     WHERE a.TypeVoucher = 1
                                                                                       AND b.ExpenseType IN (2, 3, 4, 5, 6, 7)
                                                                                       AND a.AccountNumber = '154'
                                                                                       AND a.CostSetID = @CostSetID
                                                                                       AND a.CPPeriodID = @CPPeriodID),
                                                                                    0) -
                                                                             ISNULL((SELECT Sum(Amount)
                                                                                     FROM CPExpenseList a
                                                                                              LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                     WHERE a.TypeVoucher = 1
                                                                                       AND a.AccountNumber = '623'
                                                                                       AND a.CostSetID = @CostSetID
                                                                                       AND a.CPPeriodID = @CPPeriodID),
                                                                                    0)), 0)
                        DECLARE @TotalAmountCPSXC DECIMAL(25, 4) = ISNULL((ISNULL((SELECT Sum(Amount)
                                                                                   FROM CPExpenseList a
                                                                                            LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                   WHERE a.TypeVoucher = 0
                                                                                     AND a.AccountNumber = '627'
                                                                                     AND a.CostSetID = @CostSetID
                                                                                     AND a.CPPeriodID = @CPPeriodID),
                                                                                  0) +
                                                                           ISNULL((SELECT Sum(Amount)
                                                                                   FROM CPExpenseList a
                                                                                            LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                   WHERE a.TypeVoucher = 0
                                                                                     AND b.ExpenseType IN (8, 9, 10, 11, 12, 13)
                                                                                     AND a.AccountNumber = '154'
                                                                                     AND a.CostSetID = @CostSetID
                                                                                     AND a.CPPeriodID = @CPPeriodID),
                                                                                  0) +
                                                                           ISNULL((SELECT SUM(a.AllocatedAmount)
                                                                                   FROM CPAllocationGeneralExpenseDetail a
                                                                                            LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                   WHERE b.ExpenseType IN (8, 9, 10, 11, 12, 13)
                                                                                     AND a.CostSetID = @CostSetID
                                                                                     AND a.CPPeriodID = @CPPeriodID
                                                                                     AND a.AccountNumber = '154'), 0) +
                                                                           ISNULL((SELECT SUM(a.AllocatedAmount)
                                                                                   FROM CPAllocationGeneralExpenseDetail a
                                                                                            LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                   WHERE a.CostSetID = @CostSetID
                                                                                     AND a.CPPeriodID = @CPPeriodID
                                                                                     AND a.AccountNumber = '627'), 0) -
                                                                           ISNULL((SELECT Sum(Amount)
                                                                                   FROM CPExpenseList a
                                                                                            LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                   WHERE a.TypeVoucher = 1
                                                                                     AND b.ExpenseType IN (8, 9, 10, 11, 12, 13)
                                                                                     AND a.AccountNumber = '154'
                                                                                     AND a.CostSetID = @CostSetID
                                                                                     AND a.CPPeriodID = @CPPeriodID),
                                                                                  0) -
                                                                           ISNULL((SELECT Sum(Amount)
                                                                                   FROM CPExpenseList a
                                                                                            LEFT JOIN ExpenseItem b ON a.ExpenseItemID = b.ID
                                                                                   WHERE a.TypeVoucher = 1
                                                                                     AND a.AccountNumber = '627'
                                                                                     AND a.CostSetID = @CostSetID
                                                                                     AND a.CPPeriodID = @CPPeriodID),
                                                                                  0)), 0)
                        SET @TotalAmount = @TotalAmountNLVL + @TotalAmountNCTT + @TotalAmountCPSDMTC + @TotalAmountCPSXC

                        INSERT INTO #Result
                        SELECT CostSetID                                       as CostSetID,
                               MaterialGoodsID                                 as MaterialGoodsID,
                               CASE
                                   WHEN @TypeMethod = 0 OR @TypeMethod = 1 OR @TypeMethod = 2
                                       THEN N'2. Chi phí SXKD phát sinh trong kỳ'
                                   ELSE N'Chi phí SXKD phát sinh trong kỳ' END as Target,
                               @TotalAmount                                    as TotalAmount,
                               @TotalAmountNLVL                                as TotalAmountNLVL,
                               @TotalAmountNCTT                                as TotalAmountNCTT,
                               @TotalAmountCPSDMTC                             as TotalAmountCPSDMTC,
                               @TotalAmountCPSXC                               as TotalAmountCPSXC,
                               2
                        FROM @CostSetMaterialGoods
                        WHERE MaterialGoodsID = @MaterialGoodsID
                          AND CostSetID = @CostSetID
                          AND MaterialGoodsID IN (SELECT MaterialGoodsID FROM #Result)
                          AND CostSetID IN (SELECT CostSetID FROM #Result)

                        -- Insert dữ liệu chỉ tiêu 3
                        INSERT INTO #Result
                        SELECT a.CostSetID                                       as CostSetID,
                               a.MaterialGoodsID                                 as QuantumID,
                               CASE
                                   WHEN @TypeMethod = 0 OR @TypeMethod = 1 OR @TypeMethod = 2
                                       THEN N'3. Giá thành sản phẩm, dịch vụ trong kỳ'
                                   ELSE N'3. Tổng chi phí' END                   as Target,
                               a.TotalCostAmount                                 as TotalAmount,
                               a.DirectMatetialAmount                            as TotalAmountNLVL,
                               a.DirectLaborAmount                               as TotalAmountNCTT,
                               (a.MachineMatetialAmount + a.MachineLaborAmount + a.MachineToolsAmount +
                                a.MachineDepreciationAmount +
                                a.MachineServiceAmount + a.MachineGeneralAmount) as TotalAmountCPSDMTC,
                               (a.GeneralMatetialAmount + a.GeneralLaborAmount + a.GeneralToolsAmount +
                                a.GeneralDepreciationAmount +
                                a.GeneralServiceAmount + a.OtherGeneralAmount)   as TotalAmountCPSXC,
                               3                                                 as Row
                        FROM CPResult a
                                 LEFT JOIN CPPeriod b ON a.CPPeriodID = b.ID
                        WHERE a.CostSetID = @CostSetID
                          AND a.MaterialGoodsID = @MaterialGoodsID
                          AND b.CompanyID = @CompanyID

                        IF (@TypeMethod = 0 OR @TypeMethod = 1 OR @TypeMethod = 2)
                            BEGIN
                                -- Insert dữ liệu chỉ tiêu 4
                                INSERT INTO #Result
                                SELECT a.CostSetID                                       as CostSetID,
                                       c.QuantumID                                       as QuantumID,
                                       N'4. Chi phí SXKD dở dang cuối kỳ '               as Target,
                                       a.TotalCostAmount                                 as TotalAmount,
                                       a.DirectMatetialAmount                            as TotalAmountNLVL,
                                       a.DirectLaborAmount                               as TotalAmountNCTT,
                                       (a.MachineMatetialAmount + a.MachineLaborAmount + a.MachineToolsAmount +
                                        a.MachineDepreciationAmount +
                                        a.MachineServiceAmount + a.MachineGeneralAmount) as TotalAmountCPSDMTC,
                                       (a.GeneralMatetialAmount + a.GeneralLaborAmount + a.GeneralToolsAmount +
                                        a.GeneralDepreciationAmount +
                                        a.GeneralServiceAmount + a.OtherGeneralAmount)   as TotalAmountCPSXC,
                                       4                                                 as Row
                                FROM CPUncompleteDetail a
                                         LEFT JOIN CPUncomplete c ON a.CostSetID = c.CostSetID
                                         LEFT JOIN CPPeriod b ON b.ID = a.CPPeriodID
                                WHERE a.CostSetID = @CostSetID
                                  AND c.QuantumID = @MaterialGoodsID
                                  AND b.CompanyID = @CompanyID
                                  AND a.CPPeriodID = @CPPeriodID
                                  AND c.CPPeriodID = @CPPeriodID
                                  AND b.ID = @CPPeriodID
                            END
                        ELSE
                            IF (@TypeMethod = 3 OR @TypeMethod = 4)
                                BEGIN
                                    -- Insert dữ liệu chỉ tiêu 4
                                    INSERT INTO #Result
                                    SELECT a.CostSetID                   as CostSetID,
                                           @MaterialGoodsID              as QuantumID,
                                           N'4. Số nghiệm thu trong kỳ ' as Target,
                                           a.TotalAcceptedAmount         as TotalAmount,
                                           null                          as TotalAmountNLVL,
                                           null                          as TotalAmountNCTT,
                                           null                          as TotalAmountCPSDMTC,
                                           null                          as TotalAmountCPSXC,
                                           4                             as Row
                                    FROM CPAcceptanceDetail a
                                    WHERE a.CostSetID = @CostSetID
                                      AND CPPeriodID = @CPPeriodID

                                    -- Insert dữ liệu chỉ tiêu 5
                                    INSERT INTO #Result
                                    SELECT a.CostSetID                      as CostSetID,
                                           @MaterialGoodsID                 as QuantumID,
                                           N'5. Số chưa nghiệm thu cuối kỳ' as Target,
                                           a.TotalAcceptedAmount            as TotalAmount,
                                           null                             as TotalAmountNLVL,
                                           null                             as TotalAmountNCTT,
                                           null                             as TotalAmountCPSDMTC,
                                           null                             as TotalAmountCPSXC,
                                           5                                as Row
                                    FROM CPAcceptanceDetail a
                                    WHERE a.CostSetID = @CostSetID
                                      AND CPPeriodID = @CPPeriodID

                                END
                        DELETE
                        FROM @CostSetMaterialGoods
                        WHERE MaterialGoodsID = @MaterialGoodsID
                          AND CostSetID = @CostSetID
                        SET @COUNT = @COUNT - 1
                    END
            END


        SELECT a.CostsetID          as CostSetID,
               c.CostSetCode        as CostSetCode,
               c.CostSetName        as CostSetName,
               a.QuantumID          as QuantumID,
               b.MaterialGoodsCode  as QuantumCode,
               b.MaterialGoodsName  as QuantumName,
               a.Target             as Target,
               a.TotalAmount        as TotalAmount,
               a.TotalAmountNLVL    as TotalAmountNLVL,
               a.TotalAmountNCTT    as TotalAmountNCTT,
               a.TotalAmountCPSDMTC as TotalAmountCPSDMTC,
               a.TotalAmountCPSXC   as TotalAmountCPSXC,
               a.Row                as Row
        from #Result a
                 LEFT JOIN MaterialGoods b ON a.QuantumID = b.ID
                 LEFT JOIN CostSet c ON a.CostsetID = c.ID
        WHERE b.CompanyID = @CompanyID
        ORDER BY a.CostsetID, a.QuantumID, a.Row
    END
go

