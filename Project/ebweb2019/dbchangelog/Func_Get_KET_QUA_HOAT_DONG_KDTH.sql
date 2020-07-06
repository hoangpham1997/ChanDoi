CREATE FUNCTION [dbo].[Func_Get_KET_QUA_HOAT_DONG_KDTH]
(
    @CompanyID UNIQUEIDENTIFIER,
	@FromDate DATETIME
	,@ToDate DATETIME
	,@PrevFromDate DATETIME
	,@PrevToDate DATETIME
	,@IsWorkingWithManagementBook BIT
	,@IncludeDependentBranch BIT
)
RETURNS
@Result TABLE
(
	ItemID UNIQUEIDENTIFIER
      ,ItemCode NVARCHAR(25)
      ,ItemName NVARCHAR(255)
      ,ItemNameEnglish NVARCHAR(255)
      ,ItemIndex INT
      ,Description NVARCHAR(255)
      ,FormulaType INT
      ,FormulaFrontEnd NVARCHAR(MAX)
      ,Formula XML
      ,Hidden BIT
      ,IsBold BIT
      ,IsItalic BIT
      --,SortOrder INT -- Thứ tự cho trường hợp các chỉ tiêu ngoài bảng cân đối kế toán
      ,Amount DECIMAL(25,4)
      ,PrevAmount  DECIMAL(25,4)
)
AS
BEGIN

	DECLARE @AccountingSystem INT
	SET @AccountingSystem = 15
  	DECLARE @ReportID NVARCHAR(100)
	SET @ReportID = '2'

	-- Bảng tạm các chỉ tiêu chi tiết
	DECLARE @tblItem TABLE
	(
		ItemID				UNIQUEIDENTIFIER
		,ItemName			NVARCHAR(255)
		,OperationSign		INT
		,OperandString		NVARCHAR(255)
		,AccountNumber		VARCHAR(25)
		,CorrespondingAccountNumber	VARCHAR(25)
	)

	INSERT @tblItem
	SELECT ItemID
		,ItemName
		, x.r.value('@OperationSign','INT')
		, x.r.value('@OperandString','nvarchar(255)')
		, x.r.value('@AccountNumber','nvarchar(25)') + '%'
		, CASE WHEN x.r.value('@CorrespondingAccountNumber','nvarchar(25)') <>''
			THEN x.r.value('@CorrespondingAccountNumber','nvarchar(25)') + '%'
			ELSE ''
		 END
	FROM FRTemplate
	CROSS APPLY Formula.nodes('/root/DetailFormula') AS x(r)
	WHERE AccountingSystem = @AccountingSystem
		  AND ReportID = @ReportID
		  AND FormulaType = 0 AND Formula IS NOT NULL

	-- Nếu là @AccountingSytem = 15 thì loại các tài khoản 512
	IF @AccountingSystem = 15
	BEGIN
		DELETE @tblItem
		WHERE ItemID IN ('78314FBE-D5FC-4AF6-BE5B-6EA822AA18F5','4941F012-C759-44CE-9C91-FC456AD9B36A') AND (AccountNumber = '512%' OR CorrespondingAccountNumber = '512%')
	END

	-- Bảng tạm cho các chỉ tiêu
	DECLARE @tblMasterDetail Table
		(
			ItemID UNIQUEIDENTIFIER
			,DetailItemID UNIQUEIDENTIFIER
			,OperationSign INT
			,Grade INT
			,DebitAmount DECIMAL(25,4)
			,PrevDebitAmount DECIMAL(25,4)
		)
	-- Lấy dữ liệu cho các chỉ tiêu chi tiết

	INSERT INTO @tblMasterDetail
	SELECT
		I.ItemID
		,NULL
		,1
		,-1 -- Là chỉ tiêu chi tiết
		,SUM(CASE WHEN GL.PostedDate BETWEEN @FromDate AND @ToDate THEN
					(CASE WHEN I.OperandString = 'PhatsinhCO' THEN GL.CreditAmount
					   WHEN I.OperandString IN ('PhatsinhNO','PhatsinhDU') THEN GL.DebitAmount
					   ELSE 0
				    END) * I.OperationSign
			   ELSE 0
			   END) As DebitAmount
		,SUM(CASE WHEN GL.PostedDate BETWEEN @PrevFromDate AND @PrevToDate THEN
					CASE WHEN I.OperandString = 'PhatsinhCO' THEN GL.CreditAmount
					   WHEN I.OperandString IN ('PhatsinhNO','PhatsinhDU') THEN GL.DebitAmount
					   ELSE 0
				    END * I.OperationSign
			   ELSE 0
			   END) As DebitAmount
		FROM @tblItem I
		INNER JOIN dbo.GeneralLedger GL ON GL.Account LIKE I.AccountNumber
		AND ( I.OperandString <> 'PhatsinhDU'
			  OR I.OperandString = 'PhatsinhDU' AND GL.AccountCorresponding LIKE I.CorrespondingAccountNumber
			)
		WHERE GL.PostedDate BETWEEN @PrevFromDate AND @ToDate
		  AND GL.CompanyID IN (SELECT id FROM Func_getCompany (@CompanyID, @IncludeDependentBranch))
			AND (GL.TypeLedger = @IsWorkingWithManagementBook OR GL.TypeLedger = 2)
		GROUP BY I.ItemID

	-- Lấy các chỉ tiêu tổng hợp
	INSERT @tblMasterDetail
	SELECT ItemID
	, x.r.value('@ItemID','UNIQUEIDENTIFIER')
	, x.r.value('@OperationSign','INT')
	, 0
	, 0.0
	, 0.0
	FROM FRTemplate
	CROSS APPLY Formula.nodes('/root/MasterFormula') AS x(r)
	WHERE AccountingSystem =  @AccountingSystem
		  AND ReportID = @ReportID
		  AND FormulaType = 1 AND Formula IS NOT NULL

	-- Cộng chỉ tiêu con lên chỉ tiêu cha
	;
	WITH V(ItemID,DetailItemID,DebitAmount,PrevDebitAmount,OperationSign)
		AS
		(
			SELECT ItemID,DetailItemID,DebitAmount, PrevDebitAmount, OperationSign
			FROM @tblMasterDetail WHERE Grade = -1
			UNION ALL
			SELECT B.ItemID
				, B.DetailItemID
				, V.DebitAmount
				, V.PrevDebitAmount
				, B.OperationSign * V.OperationSign AS OperationSign
			FROM @tblMasterDetail B, V
			WHERE B.DetailItemID = V.ItemID
		)

	-- Lấy dữ liệu in báo cáo
	INSERT @Result
	SELECT
		FR.ItemID
		, FR.ItemCode
		, FR.ItemName
		, FR.ItemNameEnglish
		, FR.ItemIndex
		, FR.Description
		, FR.FormulaType
		, CASE WHEN FR.FormulaType = 1 THEN FR.FormulaFrontEnd	ELSE ''	END AS FormulaFrontEnd
		, CASE WHEN FR.FormulaType = 1 THEN FR.Formula ELSE NULL END AS Formula
		, FR.Hidden
		, FR.IsBold
		, FR.IsItalic
		, ISNULL(X.Amount,0) AS Amount
		, ISNULL(X.PrevAmount,0) AS PrevAmount
		FROM
		(
			SELECT V.ItemID
				,SUM(V.OperationSign * V.DebitAmount)  AS Amount
				,SUM(V.OperationSign * V.PrevDebitAmount) AS PrevAmount
			FROM V
			GROUP BY ItemID
		) AS X
		RIGHT JOIN dbo.FRTemplate FR ON FR.ItemID = X.ItemID
	WHERE AccountingSystem = @AccountingSystem
		  AND ReportID = @ReportID
	Order by ItemIndex

	RETURN
END
go

