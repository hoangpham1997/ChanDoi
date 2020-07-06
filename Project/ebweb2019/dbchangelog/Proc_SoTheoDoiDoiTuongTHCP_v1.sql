USE [KT_2019]
GO
/****** Object:  StoredProcedure [dbo].[Proc_SoTheoDoiDoiTuongTHCP]    Script Date: 5/27/2020 3:05:34 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		MrAn
-- Create date: <Create Date,,>
-- Description:	So theo doi THCP
-- Proc_SoTheoDoiDoiTuongTHCP '01/01/2019 00:00:00','12/31/2019 23:59:59',',3b3456f0-2289-4011-9f2d-c1430aa4f66e,d2450a4f-4c25-46c2-ad72-fa100f6326fa,7dea4b71-19e8-4952-9f5a-ee4596cc340c,2467b0c5-4bb4-419f-a561-9f654c73c324,e7d99d06-b8b6-4b31-9a8a-a3831b499367,40d13a33-27c1-4f4e-b1f1-969933f5b9c2,',',3388b665-7eca-4b51-9aba-0e13d8fcaf2a,26ef6fc0-6a31-454c-b762-8445a3de9ce9,d15b2f40-b8eb-44c5-ae4f-8a3ead0ad845,'
-- =============================================
ALTER PROCEDURE [dbo].[Proc_SoTheoDoiDoiTuongTHCP]
	-- Add the parameters for the stored procedure here
	@FromDate DATETIME,
    @ToDate DATETIME,
    @CostSetID  NVARCHAR(MAX),
	@ExpenseItemID  NVARCHAR(MAX)
AS
BEGIN
	DECLARE @temp TABLE(
			CostSetID uniqueidentifier,
			ExpenseItemID uniqueidentifier,	 
			SoDauKyNo decimal(25,0),
			SoDauKyCo decimal(25,0),
			SoPhatSinhNo decimal(25,0),
			SoPhatSinhCo decimal(25,0)
	)

	--data GL
	DECLARE @tbDataGL TABLE(
			ID uniqueidentifier,
			BranchID uniqueidentifier,
			ReferenceID uniqueidentifier,
			TypeID int,
			Date datetime,
			PostedDate datetime,
			No nvarchar(25),
			InvoiceDate datetime,
			InvoiceNo nvarchar(25),
			Account nvarchar(25),
			AccountCorresponding nvarchar(25),
			BankAccountDetailID uniqueidentifier,
			CurrencyID nvarchar(3),
			ExchangeRate decimal(25, 10),
			DebitAmount decimal(25,0),
			DebitAmountOriginal decimal(25,2),
			CreditAmount decimal(25,0),
			CreditAmountOriginal decimal(25,2),
			Reason nvarchar(512),
			Description nvarchar(512),
			VATDescription nvarchar(512),
			AccountingObjectID uniqueidentifier,
			EmployeeID uniqueidentifier,
			BudgetItemID uniqueidentifier,
			CostSetID uniqueidentifier,
			ContractID uniqueidentifier,
			StatisticsCodeID uniqueidentifier,
			InvoiceSeries nvarchar(25),
			ContactName nvarchar(512),
			DetailID uniqueidentifier,
			RefNo nvarchar(25),
			RefDate datetime,
			DepartmentID uniqueidentifier,
			ExpenseItemID uniqueidentifier,
			OrderPriority int,
			IsIrrationalCost bit
		)

		INSERT INTO @tbDataGL
		SELECT GL.* FROM dbo.GeneralLedger GL WHERE GL.PostedDate <= @ToDate
		-------------------------------

	--bang CostSetID
	DECLARE @tblListCostSetID TABLE
	(
	CostSetID uniqueidentifier
	)

		 INSERT  INTO @tblListCostSetID
         SELECT  TG.id
         FROM    CostSet AS TG
                 LEFT JOIN dbo.Func_ConvertStringIntoTable(@CostSetID,',') AS CostSetID ON TG.ID = CostSetID.Value
         WHERE  CostSetID.Value IS NOT NULL
	--bang ExpenseItemID
	DECLARE @tblListExpenseItemID TABLE
	(
	ExpenseItemID uniqueidentifier
	)

		 INSERT  INTO @tblListExpenseItemID
         SELECT  TG.id
         FROM    ExpenseItem AS TG
                 LEFT JOIN dbo.Func_ConvertStringIntoTable(@ExpenseItemID,',') AS ExpenseItemID ON TG.ID = ExpenseItemID.Value
         WHERE  ExpenseItemID.Value IS NOT NULL



     ----------------Insert Sodauky                        			
		INSERT INTO @temp (CostSetID,ExpenseItemID ,SoDauKyNo)
		SELECT a.CostSetID,a.ExpenseItemID ,Sum(DebitAmount)
		FROM @tbDataGL a 
		where CostSetID in (select CostSetID from @tblListCostSetID)
		and ExpenseItemID in (select ExpenseItemID from @tblListExpenseItemID)
		and DebitAmount > 0
		and Date < @FromDate --and Account like '111%'
		Group By a.CostSetID,a.ExpenseItemID

	 INSERT INTO @temp (CostSetID,ExpenseItemID ,SoDauKyCo)
		SELECT a.CostSetID,a.ExpenseItemID ,Sum(CreditAmount)
		FROM @tbDataGL a
		where CostSetID in (select CostSetID from @tblListCostSetID)
		and ExpenseItemID in (select ExpenseItemID from @tblListExpenseItemID)
		and CreditAmount > 0 and Date < @FromDate and AccountCorresponding like ('133%')
		Group By a.CostSetID,a.ExpenseItemID
		---

		--select * from @temp
		 ----------------Insert SoPhatSinh                        			
		INSERT INTO @temp (CostSetID,ExpenseItemID ,SoPhatSinhNo)
		SELECT a.CostSetID,a.ExpenseItemID ,Sum(DebitAmount)
		FROM @tbDataGL a 
		where CostSetID in (select CostSetID from @tblListCostSetID)
		and ExpenseItemID in (select ExpenseItemID from @tblListExpenseItemID)
		and DebitAmount > 0
		and Date >= @FromDate and Date <= @ToDate --and AccountCorresponding in ('1331%')
		Group By a.CostSetID,a.ExpenseItemID

	  INSERT INTO @temp (CostSetID,ExpenseItemID ,SoPhatSinhCo)
		SELECT a.CostSetID,a.ExpenseItemID ,Sum(CreditAmount)
		FROM @tbDataGL a
		where CostSetID in (select CostSetID from @tblListCostSetID)
		and ExpenseItemID in (select ExpenseItemID from @tblListExpenseItemID)
		and CreditAmount > 0 and Date >= @FromDate and Date <= @ToDate and AccountCorresponding like ('133%')
		Group By a.CostSetID,a.ExpenseItemID

		--select * from @temp

		---------Tao bang
	DECLARE @tblSoTheoDoiDTTHCP TABLE(
			CostSetID uniqueidentifier,
			CostSetCode  NVARCHAR(25),
			CostSetName NVARCHAR(512),
			ExpenseItemID uniqueidentifier,
			ExpenseItemCode  NVARCHAR(25),
			ExpenseItemName NVARCHAR(512),	 
			SoDauKy decimal(25,0),
			SoPhatSinh decimal(25,0),
			LuyKeCuoiKy decimal(25,0)
	)
	--Insert vao @tblSoTheoDoiDTTHCP
	INSERT INTO @tblSoTheoDoiDTTHCP (CostSetID,ExpenseItemID ,SoDauKy,SoPhatSinh)
		SELECT a.CostSetID,a.ExpenseItemID, Sum(ISNULL(a.SoDauKyNo,0) - ISNULL(a.SoDauKyCo,0)),Sum(ISNULL(a.SoPhatSinhNo,0) - ISNULL(a.SoPhatSinhCo,0))
		FROM @temp a 
		Group By a.CostSetID,a.ExpenseItemID

	------Update @tblSoTheoDoiDTTHCP
	
	UPDATE @tblSoTheoDoiDTTHCP SET CostSetCode = k.CostSetCode, CostSetName = k.CostSetName
	FROM (select ID,CostSetCode,CostSetName from CostSet) k
	WHERE CostSetID = k.ID
	--
	
	UPDATE @tblSoTheoDoiDTTHCP SET ExpenseItemCode = k.ExpenseItemCode, ExpenseItemName = k.ExpenseItemName
	FROM (select ID,ExpenseItemCode,ExpenseItemName from ExpenseItem) k
	WHERE ExpenseItemID = k.ID
	-----Select

		Select CostSetID,CostSetCode,CostSetName,ExpenseItemID, ExpenseItemCode, ExpenseItemName,ISNULL(SoDauKy,0) as SoDauKy,ISNULL(SoPhatSinh,0) as SoPhatSinh,ISNULL(SoDauKy,0)+ISNULL(SoPhatSinh,0) as LuyKeCuoiKy
		From @tblSoTheoDoiDTTHCP 
		order by CostSetCode

END
