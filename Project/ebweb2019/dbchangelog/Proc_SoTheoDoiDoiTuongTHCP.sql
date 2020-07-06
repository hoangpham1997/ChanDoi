-- =============================================
-- Author:		dieunn
-- Create date: <06/12/2020>
-- Description:	So theo doi THCP theo khoan muc chi phi
-- Proc_SoTheoDoiDoiTuongTHCP @FromDate = '2020-06-01',@ToDate = '2020-06-15',@CostSetID = ',a90069f0-9ab1-8f40-a3b3-d9afd86f198c,7c98897a-8578-8e49-81ff-bf1706158603,b3f6796f-97e9-b744-b56c-323da287537c,e1c076ba-f090-c74c-9c83-ae1bbb9b0b4e,5bd7cbcd-589a-7f48-9f76-fef7d9f3552f,f8044ebc-c437-e549-bdf6-4cd641de8ba4,8cfcc3ec-116d-4344-9268-b15493a1d556,f0d70325-6e24-ba4b-b40a-a908607f313f,77b99361-d930-e447-a357-8ee6bf0de6f3,b62668b6-9d9c-894e-b7fe-9a872d341644,',@ExpenseItemID = ',a0d7bd75-c2b0-624d-bc36-eaafcd7b1149,68a99a61-6e8b-6a45-bbe0-cde74181fa7a,16ecdab3-3b33-404e-82d1-c08387e8fda5,dfaf95e6-0dcd-4a43-8a89-25585337d7f8,35aaa4a9-cdea-6747-95dd-8d943a1798ce,30fa3891-b3a8-9f47-b2b4-a506393bbd06,84f78fec-2625-a341-a678-7b392b16fb5b,6f7c761c-a87f-934b-90ea-0e4efef3e687,be14c693-7833-4b40-8f9c-34c01f972b27,ffca06f7-d5a3-d348-ab62-d1f9514330c1,124c0bd4-f87f-d946-be1d-1ff7ac0c7925,2ecfbb10-9752-6f43-9ce6-253d63e3b09b,98d1f208-2ce8-5543-b611-b7b1105fff8a,f18b7e16-92ae-8444-9e36-be14cd67d5de,93d1aee5-2c49-b74f-b97b-0700ee6213b5,0e808385-ab31-0749-9710-57a7d8b266ad,'
-- =============================================
create PROCEDURE [dbo].[Proc_SoTheoDoiDoiTuongTHCP]
    @FromDate DATETIME,
    @ToDate DATETIME,
    @CostSetID NVARCHAR(MAX),
    @ExpenseItemID NVARCHAR(MAX)
AS
BEGIN
    DECLARE @temp TABLE
                  (   CostSetID     uniqueidentifier,
                      ExpenseItemID uniqueidentifier,
                      SoDauKyNo     money,
                      SoDauKyCo     money,
                      SoPhatSinhNo  money,
                      SoPhatSinhCo  money
                  )

    --bang CostSetID
    DECLARE @tblListCostSetID TABLE
                              (
                                  CostSetID uniqueidentifier
                              )

    INSERT INTO @tblListCostSetID
    SELECT TG.id
    FROM CostSet AS TG
             LEFT JOIN dbo.Func_ConvertStringIntoTable_Nvarchar (@CostSetID, ',') AS CostSetID ON TG.ID = CostSetID.Value
    WHERE CostSetID.Value IS NOT NULL

    --bang ExpenseItemID
    DECLARE @tblListExpenseItemID TABLE
                                  (
                                      ExpenseItemID uniqueidentifier
                                  )

    INSERT INTO @tblListExpenseItemID
    SELECT TG.id
    FROM ExpenseItem AS TG
             LEFT JOIN dbo.Func_ConvertStringIntoTable_Nvarchar (@ExpenseItemID, ',') AS ExpenseItemID
                       ON TG.ID = ExpenseItemID.Value
    WHERE ExpenseItemID.Value IS NOT NULL

    ----------------Insert Sodauky
    INSERT INTO @temp (CostSetID, ExpenseItemID, SoDauKyNo)
    SELECT a.CostSetID, a.ExpenseItemID, Sum(DebitAmount)
    FROM GeneralLedger a
    where CostSetID in (select CostSetID from @tblListCostSetID)
      and ExpenseItemID in (select ExpenseItemID from @tblListExpenseItemID)
      and DebitAmount > 0
      and PostedDate < @FromDate --and Account like '111%'
    Group By a.CostSetID, a.ExpenseItemID

    INSERT INTO @temp (CostSetID, ExpenseItemID, SoDauKyCo)
    SELECT a.CostSetID, a.ExpenseItemID, Sum(CreditAmount)
    FROM GeneralLedger a
    where CostSetID in (select CostSetID from @tblListCostSetID)
      and ExpenseItemID in (select ExpenseItemID from @tblListExpenseItemID)
      and CreditAmount > 0
      and PostedDate < @FromDate
      and AccountCorresponding like ('133%')
    Group By a.CostSetID, a.ExpenseItemID
    ---

--     select * from @temp
--     ----------------Insert SoPhatSinh
    INSERT INTO @temp (CostSetID, ExpenseItemID, SoPhatSinhNo)
    SELECT a.CostSetID, a.ExpenseItemID, Sum(DebitAmount)
    FROM GeneralLedger a
    where CostSetID in (select CostSetID from @tblListCostSetID)
      and ExpenseItemID in (select ExpenseItemID from @tblListExpenseItemID)
      and DebitAmount > 0
      and PostedDate >= @FromDate
      and PostedDate <= @ToDate --and AccountCorresponding in ('1331%')
    Group By a.CostSetID, a.ExpenseItemID

    INSERT INTO @temp (CostSetID, ExpenseItemID, SoPhatSinhCo)
    SELECT a.CostSetID, a.ExpenseItemID, Sum(CreditAmount)
    FROM GeneralLedger a
    where CostSetID in (select CostSetID from @tblListCostSetID)
      and ExpenseItemID in (select ExpenseItemID from @tblListExpenseItemID)
      and CreditAmount > 0
      and PostedDate >= @FromDate
      and PostedDate <= @ToDate
      and AccountCorresponding like ('133%')
    Group By a.CostSetID, a.ExpenseItemID

--     select * from @temp
--
--     ---------Tao bang
    DECLARE @tblSoTheoDoiDTTHCP TABLE
                                (
                                    CostSetID       uniqueidentifier,
                                    CostSetCode     NVARCHAR(25),
                                    CostSetName     NVARCHAR(512),
                                    ExpenseItemID   uniqueidentifier,
                                    ExpenseItemCode NVARCHAR(25),
                                    ExpenseItemName NVARCHAR(512),
                                    SoDauKy         money,
                                    SoPhatSinh      money,
                                    LuyKeCuoiKy     money
                                )
    --Insert vao @tblSoTheoDoiDTTHCP
    INSERT INTO @tblSoTheoDoiDTTHCP (CostSetID, ExpenseItemID, SoDauKy, SoPhatSinh)
    SELECT
           a.CostSetID,
           a.ExpenseItemID,
           Sum(ISNULL(a.SoDauKyNo, 0) - ISNULL(a.SoDauKyCo, 0)),
           Sum(ISNULL(a.SoPhatSinhNo, 0) - ISNULL(a.SoPhatSinhCo, 0))
    FROM @temp a
    Group By a.CostSetID, a.ExpenseItemID

    ------Update @tblSoTheoDoiDTTHCP

    UPDATE @tblSoTheoDoiDTTHCP
    SET CostSetCode = k.CostSetCode,
        CostSetName = k.CostSetName
    FROM (select ID, CostSetCode, CostSetName from CostSet) k
    WHERE CostSetID = k.ID
    --

    UPDATE @tblSoTheoDoiDTTHCP
    SET ExpenseItemCode = k.ExpenseItemCode,
        ExpenseItemName = k.ExpenseItemName
    FROM (select ID, ExpenseItemCode, ExpenseItemName from ExpenseItem) k
    WHERE ExpenseItemID = k.ID
    -----Select

    Select
           CostSetID,
           CostSetCode,
           CostSetName,
           ExpenseItemID,
           ExpenseItemCode,
           ExpenseItemName,
           ISNULL(SoDauKy, 0)                         as SoDauKy,
           ISNULL(SoPhatSinh, 0)                      as SoPhatSinh,
           ISNULL(SoDauKy, 0) + ISNULL(SoPhatSinh, 0) as LuyKeCuoiKy
    From @tblSoTheoDoiDTTHCP
    order by CostSetCode,ExpenseItemCode

END
go

