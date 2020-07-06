-- =============================================
-- Description:	So theo doi THCP
-- Proc_SoTheoDoiMaThongKeTheoKhoanMucChiPhi '2020-06-01','2020-06-06','d20ac97c-9479-3046-b77a-e4cc7e8e38b1,a0cf9748-718a-9045-9355-c662969c09c4,abd1ca9e-a35f-7e4c-8f9f-4ead01637142,7bc1ee9f-9b8c-8a47-89da-5596a3484441,53b76074-0c50-224f-8213-67ac1545f9fb,'
-- ,'3c688f95-30b0-1d46-a2d9-ca4ca0d7bd75-c2b0-624d-bc36-eaafcd7b1149,6f7c761c-a87f-934b-90ea-0e4efef3e687,68a99a61-6e8b-6a45-bbe0-cde74181fa7a,be14c693-7833-4b40-8f9c-34c01f972b27,16ecdab3-3b33-404e-82d1-c08387e8fda5,ffca06f7-d5a3-d348-ab62-d1f9514330c1,dfaf95e6-0dcd-4a43-8a89-25585337d7f8,124c0bd4-f87f-d946-be1d-1ff7ac0c7925,2ecfbb10-9752-6f43-9ce6-253d63e3b09b,35aaa4a9-cdea-6747-95dd-8d943a1798ce,98d1f208-2ce8-5543-b611-b7b1105fff8a,30fa3891-b3a8-9f47-b2b4-a506393bbd06,f18b7e16-92ae-8444-9e36-be14cd67d5de,84f78fec-2625-a341-a678-7b392b16fb5b,93d1aee5-2c49-b74f-b97b-0700ee6213b5,0e808385-ab31-0749-9710-57a7d8b266ad,',
-- 'CAA09998-422C-C742-BF50-F26B2548BAEE', false, 0
-- =============================================
alter PROCEDURE [dbo].[Proc_SoTheoDoiMaThongKeTheoKhoanMucChiPhi]
	-- Add the parameters for the stored procedure here
	@FromDate DATETIME,
    @ToDate DATETIME,
    @StatisticsCodeID  NVARCHAR(MAX),
	@ExpenseItemID  NVARCHAR(MAX),
	@CompanyID UNIQUEIDENTIFIER,
    @IsDependent bit,
    @TypeLedger INT
AS
BEGIN
	DECLARE @temp TABLE(
			StatisticsCodeID uniqueidentifier,
			ExpenseItemID uniqueidentifier,
			SoDauKyNo money,
			SoDauKyCo money,
			SoPhatSinhNo money,
			SoPhatSinhCo money
	)

    DECLARE @tblListCompanyID TABLE
                                  (
                                      CompanyID UNIQUEIDENTIFIER
                                  )

     DECLARE @bigOrgID uniqueidentifier = (SELECT TOP (1) (CASE
                                               WHEN ParentID IS NOT NULL THEN ParentID
                                               ELSE ID END)
                           FROM EbOrganizationUnit
                           WHERE ID = @CompanyID)

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

	--data GL
	DECLARE @tbDataGL TABLE(
			ID uniqueidentifier,
			Date datetime,
			PostedDate datetime,
			No nvarchar(25),
			DebitAmount money,
			CreditAmount money,
			StatisticsCodeID uniqueidentifier,
			ExpenseItemID uniqueidentifier,
			CompanyID uniqueidentifier,
			OrderPriority int
		)

        INSERT INTO @tbDataGL
		SELECT GL.id,
		       GL.Date,
		       GL.PostedDate,
		       (CASE when @TypeLedger = 0 then GL.NoFBook else GL.NoMBook end) as No,
		       GL.DebitAmount,
		       GL.CreditAmount,
		       GL.StatisticsCodeID,
		       GL.ExpenseItemID,
		       GL.CompanyID,
		       GL.OrderPriority
		FROM dbo.GeneralLedger GL WHERE GL.PostedDate <= @ToDate
	    and GL.CompanyID in (select CompanyID from @tblListCompanyID)

		-------------------------------

	--bang tblStatisticsCodeID
	DECLARE @tblStatisticsCodeID TABLE
	(
	statisticsCodeID uniqueidentifier
	)

		 INSERT  INTO @tblStatisticsCodeID
         SELECT  TG.id
         FROM    StatisticsCode AS TG
                 LEFT JOIN dbo.Func_ConvertStringIntoTable_Nvarchar(@StatisticsCodeID,',') AS StatisticsCodeID ON TG.ID = StatisticsCodeID.Value
         WHERE  StatisticsCodeID.Value IS NOT NULL

	    INSERT INTO @tblStatisticsCodeID
	    SELECT TG.id
	    FROM StatisticsCode AS TG
	    WHERE TG.ParentID in (SELECT id from StatisticsCode sc where sc.id in (select statisticsCodeID from @tblStatisticsCodeID) and sc.ParentID is null)
	--bang ExpenseItemID
	DECLARE @tblListExpenseItemID TABLE
	(
	ExpenseItemID uniqueidentifier
	)

		 INSERT  INTO @tblListExpenseItemID
         SELECT  TG.id
         FROM    ExpenseItem AS TG
                 LEFT JOIN dbo.Func_ConvertStringIntoTable_Nvarchar(@ExpenseItemID,',') AS ExpenseItemID ON TG.ID = ExpenseItemID.Value
         WHERE  ExpenseItemID.Value IS NOT NULL

        INSERT INTO @tblListExpenseItemID
	    SELECT TG.id
	    FROM ExpenseItem AS TG
	    WHERE TG.ParentID in (SELECT id from ExpenseItem ei where ei.id in (select ExpenseItemID from @tblListExpenseItemID) and ei.ParentID is null)

     ----------------Insert Sodauky
		INSERT INTO @temp (StatisticsCodeID,ExpenseItemID ,SoDauKyNo)
		SELECT a.StatisticsCodeID,a.ExpenseItemID ,Sum(DebitAmount)
		FROM @tbDataGL a
		where StatisticsCodeID in (select distinct statisticsCodeID from @tblStatisticsCodeID)
		and ExpenseItemID in (select distinct ExpenseItemID from @tblListExpenseItemID)
		and DebitAmount > 0
		and Date < @FromDate
		and a.CompanyID in (select CompanyID from @tblListCompanyID)
		Group By a.StatisticsCodeID,a.ExpenseItemID

-- 	 INSERT INTO @temp (StatisticsCodeID,ExpenseItemID ,SoDauKyCo)
-- 		SELECT a.StatisticsCodeID,a.ExpenseItemID ,Sum(CreditAmount)
-- 		FROM @tbDataGL a
-- 		where StatisticsCodeID in (select StatisticsCodeID from @tblStatisticsCodeID)
-- 		and ExpenseItemID in (select ExpenseItemID from @tblListExpenseItemID)
-- 		and CreditAmount > 0 and Date < @FromDate
-- 	    and a.CompanyID in (select CompanyID from @tblListCompanyID)
-- 		Group By a.StatisticsCodeID,a.ExpenseItemID
		---

		--select * from @temp
		 ----------------Insert SoPhatSinh
		INSERT INTO @temp (StatisticsCodeID,ExpenseItemID ,SoPhatSinhNo)
		SELECT a.StatisticsCodeID,a.ExpenseItemID ,Sum(DebitAmount)
		FROM @tbDataGL a
		where StatisticsCodeID in (select distinct StatisticsCodeID from @tblStatisticsCodeID)
		and ExpenseItemID in (select distinct ExpenseItemID from @tblListExpenseItemID)
		and DebitAmount > 0
		and Date >= @FromDate and Date <= @ToDate
		and a.CompanyID in (select CompanyID from @tblListCompanyID)
		Group By a.StatisticsCodeID,a.ExpenseItemID

-- 	  INSERT INTO @temp (StatisticsCodeID,ExpenseItemID ,SoPhatSinhCo)
-- 		SELECT a.StatisticsCodeID,a.ExpenseItemID ,Sum(CreditAmount)
-- 		FROM @tbDataGL a
-- 		where StatisticsCodeID in (select StatisticsCodeID from @tblStatisticsCodeID)
-- 		and ExpenseItemID in (select ExpenseItemID from @tblListExpenseItemID)
-- 		and CreditAmount > 0 and Date >= @FromDate and Date <= @ToDate
-- 	    and a.CompanyID in (select CompanyID from @tblListCompanyID)
-- 		Group By a.StatisticsCodeID,a.ExpenseItemID

		--select * from @temp

		---------Tao bang
	DECLARE @tblSoTheoDoiMaThongKe TABLE(
			StatisticsCodeID uniqueidentifier,
			StatisticsCode  NVARCHAR(25),
			StatisticsCodeName NVARCHAR(512),
			ExpenseItemID uniqueidentifier,
			ExpenseItemCode  NVARCHAR(25),
			ExpenseItemName NVARCHAR(512),
			SoDauKy money,
			SoPhatSinh money,
			LuyKeCuoiKy money,
			TongSoDauKy money,
			TongSoPhatSinh money,
			TongLuyKeCuoiKy money
	)
	--Insert vao @tblSoTheoDoiMaThongKe
	INSERT INTO @tblSoTheoDoiMaThongKe (StatisticsCodeID,ExpenseItemID ,SoDauKy,SoPhatSinh)
		SELECT a.StatisticsCodeID,a.ExpenseItemID, Sum(ISNULL(a.SoDauKyNo,0)),Sum(ISNULL(a.SoPhatSinhNo,0))
		FROM @temp a
		Group By a.StatisticsCodeID,a.ExpenseItemID

	------Update @tblSoTheoDoiMaThongKe

	UPDATE @tblSoTheoDoiMaThongKe SET StatisticsCode = k.StatisticsCode, StatisticsCodeName = k.StatisticsCodeName
	FROM (select ID,StatisticsCode,StatisticsCodeName from StatisticsCode) k
	WHERE StatisticsCodeID = k.ID
	--

	UPDATE @tblSoTheoDoiMaThongKe SET ExpenseItemCode = k.ExpenseItemCode, ExpenseItemName = k.ExpenseItemName
	FROM (select ID,ExpenseItemCode,ExpenseItemName from ExpenseItem) k
	WHERE ExpenseItemID = k.ID
	-----Select

    Select StatisticsCodeID,
           StatisticsCode,
           StatisticsCodeName,
           ExpenseItemID,
           ExpenseItemCode,
           ExpenseItemName,
           ISNULL(SoDauKy,0) as SoDauKy,
           ISNULL(SoPhatSinh,0) as SoPhatSinh,
           ISNULL(SoDauKy,0)+ISNULL(SoPhatSinh,0) as LuyKeCuoiKy,
           (select sum(ISNULL(x.SoDauKy,0)) from @tblSoTheoDoiMaThongKe x where x.StatisticsCode = a.StatisticsCode) as TongSoDauKy,
           (select sum(ISNULL(x.SoPhatSinh,0)) from @tblSoTheoDoiMaThongKe x where x.StatisticsCode = a.StatisticsCode) as TongSoPhatSinh,
           (select sum(ISNULL(x.SoDauKy,0)+ISNULL(x.SoPhatSinh,0)) from @tblSoTheoDoiMaThongKe x where x.StatisticsCode = a.StatisticsCode) as TongLuyKeCuoiKy
	From @tblSoTheoDoiMaThongKe a
	order by StatisticsCode

END
go

