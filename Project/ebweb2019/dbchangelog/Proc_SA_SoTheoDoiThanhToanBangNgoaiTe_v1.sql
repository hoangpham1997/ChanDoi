USE [KT_2019]
GO
/****** Object:  StoredProcedure [dbo].[Proc_SA_SoTheoDoiThanhToanBangNgoaiTe]    Script Date: 6/3/2020 11:35:58 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
-- =============================================
-- Author:		Cuongpv
-- Create date: 05.04.2019
-- Description:	Báo cáo Sổ theo dõi thanh toán bằng ngoại tệ
-- Proc_SA_SoTheoDoiThanhToanBangNgoaiTe '1/1/2017','12/31/2019','USD','all',',c2ba9033-f228-47e7-b607-2fe4074cb219,20d019ed-ee4a-4d04-9bd3-c8f63afdb49f,638e85a0-9caa-4277-b032-7aeaad0d2e19,6954fd1c-3740-47b8-8ca9-1f32b5986955,ad56c323-1fee-47bd-8559-5a1955177172,de053917-f92b-4c9a-8d81-e280f55f5bc9,cde65ffe-9acb-4450-815d-a892ca58e641,0637c2d0-d029-456e-9342-973002424ee3,a069c311-9412-4d3f-91a3-49719f77e4da,577fa146-cef4-48f1-b7d5-25373e026453,2c82eec7-28ff-4079-b84d-101cf5e2386e,832835d4-d2b4-4dc8-b073-b58a08e87ce1,939b5542-ff9a-4b06-aba5-2a6bebad7a8a,30c12d41-17e1-445e-9f80-93dc30a401e2,70cc3371-753a-4e6e-910f-1a6f822534a8,b4972008-f6ed-44e1-8ad9-3309913b5419,2689a990-56d1-4953-80cb-301397168aad,aae4e03b-66dd-4f75-8b9b-51b909bc6791,68f33502-0ce2-44af-890c-d82ad8869826,7147c79f-b4d8-415f-80ac-b96a8118bd55,f5919bc0-a233-49fa-a9b6-b2cb75145370,bd65fccf-8420-4d15-95f1-8c54f5779c3c,b3252cde-540b-4d37-ba08-0ce71d44a394,234d775b-3d29-47c9-828b-f5add5476098,c5b6450f-3d7b-4953-9853-79e9ce2f93d9,db1ec87f-71f4-4d7d-85f5-9e3e0b06c273,af32924d-1ac5-4148-b1f2-ecc2eb41c8d7,66cdf760-f460-4e91-a1d2-024fe1448cb4,553f3b67-8e33-4e23-863b-8e0cb5aa6198,022acefb-d45a-47a1-9614-697ad6bfab47,a594193a-ae19-4261-8d6f-6ea182aa620a,d8c93535-569d-4397-9ea3-e2649059e645,58bf7085-8b8c-4bf1-b7bc-e3231fc0e3c6,2a61c86d-711e-4b44-8e57-b26fb8a96b36,c6505142-fb16-4b74-8a43-75068200d720,64c2a66f-6001-4b65-9689-1c3c78b90db7,36902469-2b76-4593-9017-ce0d7e983e56,a1b8abcd-f0a5-4ca5-9695-0019ee9a1b32,5ceccde9-7599-4c8a-a784-8b7f1d114394,4a5b7d92-43b9-4126-bbca-5b24b3bcd077,a41ae830-c599-4923-8d69-fd68f04032d2,b795cb1e-4b29-4bd4-b5ce-79a481424813,0da8f1a9-62b1-437d-b3e2-a3ebc10e6b14,b1b42874-2b17-48b2-9b7d-a2f7acf15ebb,c4168c3c-b968-43fa-ac7c-10072a4cf284,'
-- =============================================
*/
ALTER PROCEDURE [dbo].[Proc_SA_SoTheoDoiThanhToanBangNgoaiTe]
    @FromDate DATETIME,
    @ToDate DATETIME,
    @TypeMoney NVARCHAR(3),
    @Account NVARCHAR(25),
    @AccountObjectID AS NVARCHAR(MAX)
AS
BEGIN          
	DECLARE @sqlexc nvarchar(max)
	set @sqlexc = N''
	/*Add by cuongpv de sua cach lam tron*/
	select @sqlexc = @sqlexc + N' DECLARE @tbDataGL TABLE('
	select @sqlexc = @sqlexc + N' 	ID uniqueidentifier,'
	select @sqlexc = @sqlexc + N' 	BranchID uniqueidentifier,'
	select @sqlexc = @sqlexc + N' 	ReferenceID uniqueidentifier,'
	select @sqlexc = @sqlexc + N' 	TypeID int,'
	select @sqlexc = @sqlexc + N' 	Date datetime,'
	select @sqlexc = @sqlexc + N' 	PostedDate datetime,'
	select @sqlexc = @sqlexc + N' 	No nvarchar(25),'
	select @sqlexc = @sqlexc + N' 	InvoiceDate datetime,'
	select @sqlexc = @sqlexc + N' 	InvoiceNo nvarchar(25),'
	select @sqlexc = @sqlexc + N' 	Account nvarchar(25),'
	select @sqlexc = @sqlexc + N' 	AccountCorresponding nvarchar(25),'
	select @sqlexc = @sqlexc + N' 	BankAccountDetailID uniqueidentifier,'
	select @sqlexc = @sqlexc + N' 	CurrencyID nvarchar(3),'
	select @sqlexc = @sqlexc + N' 	ExchangeRate decimal(25, 10),'
	select @sqlexc = @sqlexc + N' 	DebitAmount decimal(25,0),'
	select @sqlexc = @sqlexc + N' 	DebitAmountOriginal decimal(25,2),'
	select @sqlexc = @sqlexc + N' 	CreditAmount decimal(25,0),'
	select @sqlexc = @sqlexc + N' 	CreditAmountOriginal decimal(25,2),'
	select @sqlexc = @sqlexc + N' 	Reason nvarchar(512),'
	select @sqlexc = @sqlexc + N' 	Description nvarchar(512),'
	select @sqlexc = @sqlexc + N' 	VATDescription nvarchar(512),'
	select @sqlexc = @sqlexc + N' 	AccountingObjectID uniqueidentifier,'
	select @sqlexc = @sqlexc + N' 	EmployeeID uniqueidentifier,'
	select @sqlexc = @sqlexc + N' 	BudgetItemID uniqueidentifier,'
	select @sqlexc = @sqlexc + N' 	CostSetID uniqueidentifier,'
	select @sqlexc = @sqlexc + N' 	ContractID uniqueidentifier,'
	select @sqlexc = @sqlexc + N' 	StatisticsCodeID uniqueidentifier,'
	select @sqlexc = @sqlexc + N' 	InvoiceSeries nvarchar(25),'
	select @sqlexc = @sqlexc + N' 	ContactName nvarchar(512),'
	select @sqlexc = @sqlexc + N' 	DetailID uniqueidentifier,'
	select @sqlexc = @sqlexc + N' 	RefNo nvarchar(25),'
	select @sqlexc = @sqlexc + N' 	RefDate datetime,'
	select @sqlexc = @sqlexc + N' 	DepartmentID uniqueidentifier,'
	select @sqlexc = @sqlexc + N' 	ExpenseItemID uniqueidentifier,'
	select @sqlexc = @sqlexc + N' 	OrderPriority int,'
	select @sqlexc = @sqlexc + N' 	IsIrrationalCost bit'
	select @sqlexc = @sqlexc + N' )'

	select @sqlexc = @sqlexc + N' INSERT INTO @tbDataGL'
	select @sqlexc = @sqlexc + N' SELECT GL.* FROM dbo.GeneralLedger GL WHERE GL.PostedDate <= '''+CONVERT(nvarchar(25),@ToDate,101)+''''
		/*end add by cuongpv*/
	--lay danh sach cac @AccountObjectID duoc chon
	set @sqlexc = @sqlexc + N' DECLARE @tbAccountObjectID TABLE('
	set @sqlexc = @sqlexc + N'	AccountingObjectID UNIQUEIDENTIFIER'
	set @sqlexc = @sqlexc + N')'
	
	set @sqlexc = @sqlexc + N' INSERT INTO @tbAccountObjectID'
	set @sqlexc = @sqlexc + N' SELECT tblAccOjectSelect.Value as AccountingObjectID'
	set @sqlexc = @sqlexc + N'	FROM dbo.Func_ConvertStringIntoTable('''+@AccountObjectID+''','','') tblAccOjectSelect'
	set @sqlexc = @sqlexc + N'	WHERE tblAccOjectSelect.Value in (SELECT ID FROM AccountingObject)'
	
	--lay so du dau ky
	select @sqlexc = @sqlexc + N' DECLARE @tbDataDauKy TABLE('
	select @sqlexc = @sqlexc + N' AccountingObjectID UNIQUEIDENTIFIER,'
	select @sqlexc = @sqlexc + N' Account nvarchar(25),'
	select @sqlexc = @sqlexc + N' SoDuDauKySoTien money,'
	select @sqlexc = @sqlexc + N' SoDuDauKyQuyDoi money'
	select @sqlexc = @sqlexc + N')'
	
	select @sqlexc = @sqlexc + N' INSERT INTO @tbDataDauKy(AccountingObjectID, Account, SoDuDauKySoTien, SoDuDauKyQuyDoi)'
	select @sqlexc = @sqlexc + N' SELECT GL.AccountingObjectID, GL.Account, SUM(GL.DebitAmountOriginal - GL.CreditAmountOriginal) as SoDuDauKySoTien,'
	select @sqlexc = @sqlexc + N' SUM(GL.DebitAmount - GL.CreditAmount) as SoDuDauKyQuyDoi'
	select @sqlexc = @sqlexc + N' FROM @tbDataGL GL'
	select @sqlexc = @sqlexc + N' WHERE (GL.AccountingObjectID in (Select AccountingObjectID from @tbAccountObjectID))'
	select @sqlexc = @sqlexc + N' AND GL.PostedDate < '''+CONVERT(nvarchar(25),@FromDate,101)+''' AND (GL.CurrencyID = '''+@TypeMoney+''')'
	
	if(@Account='all')
	begin
		set @sqlexc = @sqlexc + N' AND ((GL.Account like ''131%'') OR (GL.Account like ''141%'') OR (GL.Account like ''331%''))'
	end
	else
	begin
		set @sqlexc = @sqlexc + N' AND (GL.Account like '''+@Account+'%'')'
	end
	
	select @sqlexc = @sqlexc + N' GROUP BY GL.AccountingObjectID, GL.Account'
	
	--select @sqlexc = @sqlexc + ' select * from @tbDataDauKy'
	
	--Lay Du Lieu
	set @sqlexc = @sqlexc + N' DECLARE @tbDataReturn TABLE('
	set @sqlexc = @sqlexc + N'	stt bigint,'
	set @sqlexc = @sqlexc + N'	IdGroup smallint,'
	set @sqlexc = @sqlexc + N'	AccountingObjectID UNIQUEIDENTIFIER,'
	set @sqlexc = @sqlexc + N'	AccountingObjectName nvarchar(512),'
	set @sqlexc = @sqlexc + N'	Account nvarchar(25),'
	set @sqlexc = @sqlexc + N'	NgayHoachToan Date,'--PostedDate 
	set @sqlexc = @sqlexc + N'	NgayChungTu Date,'--Date
	set @sqlexc = @sqlexc + N'	SoChungTu nvarchar(25),'--No
	set @sqlexc = @sqlexc + N'	DienGiai nvarchar(512),' --Description 
	set @sqlexc = @sqlexc + N'	TKDoiUng nvarchar(512),'--AccountCorresponding 
	set @sqlexc = @sqlexc + N'	TyGiaHoiDoai decimal(25,10),'--ExchangeRate 
	set @sqlexc = @sqlexc + N'	PSNSoTien money,'--DebitAmountOriginal
	set @sqlexc = @sqlexc + N'	PSNQuyDoi money,' --DebitAmount
	set @sqlexc = @sqlexc + N'	PSCSoTien money,' --CreditAmountOriginal
	set @sqlexc = @sqlexc + N'	PSCQuyDoi money,' --CreditAmount
	set @sqlexc = @sqlexc + N'	DuNoSoTien money,'
	set @sqlexc = @sqlexc + N'	DuNoQuyDoi money,'
	set @sqlexc = @sqlexc + N'	DuCoSoTien money,'
	set @sqlexc = @sqlexc + N'	DuCoQuyDoi money,'
	set @sqlexc = @sqlexc + N'	TonSoTien money,'--so du ton cong don so tien
	set @sqlexc = @sqlexc + N'	TonQuyDoi money,'--so du ton cong don quy doi
	set @sqlexc = @sqlexc + N'	OrderPriority int,'
	set @sqlexc = @sqlexc + N'	RefID UNIQUEIDENTIFIER,'
	set @sqlexc = @sqlexc + N'	RefType int'
	set @sqlexc = @sqlexc + N')'
	
	--lay du lieu tu GL
	set @sqlexc = @sqlexc + N' DECLARE @IdGroup1 smallint'
	set @sqlexc = @sqlexc + N' set @IdGroup1 = 5;'
	
	set @sqlexc = @sqlexc + N' INSERT INTO @tbDataReturn(stt,IdGroup, AccountingObjectID, Account, NgayHoachToan, NgayChungTu, SoChungTu, DienGiai, TKDoiUng, TyGiaHoiDoai,'
	set @sqlexc = @sqlexc + N' PSNSoTien, PSNQuyDoi, PSCSoTien, PSCQuyDoi,OrderPriority,RefID,RefType)'
	set @sqlexc = @sqlexc + N' SELECT ROW_NUMBER() OVER(ORDER BY GL.AccountingObjectID, GL.Account, GL.PostedDate) as stt, @IdGroup1 as IdGroup, GL.AccountingObjectID, Gl.Account, GL.PostedDate as NgayHoachToan, GL.Date as NgayChungTu, GL.No as SoChungTu,'
	set @sqlexc = @sqlexc + N' GL.Description as DienGiai, GL.AccountCorresponding as TKDoiUng, GL.ExchangeRate as TyGiaHoiDoai, GL.DebitAmountOriginal as PSNSoTien,'
	set @sqlexc = @sqlexc + N' GL.DebitAmount as PSNQuyDoi, Gl.CreditAmountOriginal as PSCSoTien, GL.CreditAmount as PSCQuyDoi,GL.OrderPriority as OrderPriority,GL.ReferenceID as RefID, GL.TypeID as RefType'
	set @sqlexc = @sqlexc + N' FROM @tbDataGL GL'
	set @sqlexc = @sqlexc + N' WHERE (GL.PostedDate Between '''+CONVERT(nvarchar(25),@FromDate,101)+''' And '''+CONVERT(nvarchar(25),@ToDate,101)+''') AND (GL.CurrencyID = '''+@TypeMoney+''')'
	set @sqlexc = @sqlexc + N' AND (GL.AccountingObjectID in (Select AccountingObjectID from @tbAccountObjectID))'
	
	--lay du lieu tat ca tai khoan dau 131, 141, 331
	if(@Account='all')
	begin
		set @sqlexc = @sqlexc + N' AND ((GL.Account like ''131%'') OR (GL.Account like ''141%'') OR (GL.Account like ''331%''))'
	end
	else
	begin
		set @sqlexc = @sqlexc + N' AND (GL.Account like '''+@Account+'%'')'
	end
	
	--insert so du dau ky vao, ke ca cac tai khoan ko co phat sinh trong ky nay vao @tbDataReturn
	set @sqlexc = @sqlexc + N' DECLARE @stt1 smallint'
	set @sqlexc = @sqlexc + N' set @stt1 = 0;'
	
	--set @sqlexc = @sqlexc + N' INSERT INTO @tbDataReturn(stt,IdGroup, AccountingObjectID, Account, DienGiai, TonSoTien, TonQuyDoi)'
	--set @sqlexc = @sqlexc + N' SELECT @stt1, @IdGroup1, AccountingObjectID, Account, N''Số dư đầu kỳ'''
	--set @sqlexc = @sqlexc + N' ,SoDuDauKySoTien, SoDuDauKyQuyDoi'
	--set @sqlexc = @sqlexc + N' FROM @tbDataDauKy'
	
	set @sqlexc = @sqlexc + N' Declare @SoDuDauKySoTien money'
	set @sqlexc = @sqlexc + N' Set @SoDuDauKySoTien = 0'
	set @sqlexc = @sqlexc + N' Declare @SoDuDauKyQuyDoi money'
	set @sqlexc = @sqlexc + N' Set @SoDuDauKyQuyDoi = 0'
	
	set @sqlexc = @sqlexc + N' INSERT INTO @tbDataReturn(stt,IdGroup, AccountingObjectID, Account, DienGiai, TonSoTien, TonQuyDoi)'
	set @sqlexc = @sqlexc + N' SELECT @stt1, @IdGroup1, AccountingObjectID, Account, N''Số dư đầu kỳ'''
	set @sqlexc = @sqlexc + N' ,@SoDuDauKySoTien, @SoDuDauKyQuyDoi'
	set @sqlexc = @sqlexc + N' FROM @tbDataReturn'
	set @sqlexc = @sqlexc + N' GROUP BY AccountingObjectID, Account'
	
	--Update so du dau ky vao @tbDataReturn {Neu da co AccountingObjectID, Account thi Update, nguoc lai thi insert}
	set @sqlexc = @sqlexc + N' DECLARE @iIDDauKy UNIQUEIDENTIFIER'
	set @sqlexc = @sqlexc + N' DECLARE @iAccountDauKy NVARCHAR(25)'
	set @sqlexc = @sqlexc + N' DECLARE @iTonSoTienDauKy money'
	set @sqlexc = @sqlexc + N' DECLARE @iTonQuyDoiDauKy money'
	
	set @sqlexc = @sqlexc + N' DECLARE cursorDauKy CURSOR FOR'
	set @sqlexc = @sqlexc + N' SELECT AccountingObjectID, Account, SoDuDauKySoTien, SoDuDauKyQuyDoi FROM @tbDataDauKy'
	set @sqlexc = @sqlexc + N' OPEN cursorDauKy'
	set @sqlexc = @sqlexc + N' FETCH NEXT FROM cursorDauKy INTO @iIDDauKy, @iAccountDauKy, @iTonSoTienDauKy, @iTonQuyDoiDauKy'
	set @sqlexc = @sqlexc + N' WHILE @@FETCH_STATUS = 0'
	set @sqlexc = @sqlexc + N' BEGIN'
		set @sqlexc = @sqlexc + N' Declare @countCheck int'
		set @sqlexc = @sqlexc + N' set @countCheck = (select count(AccountingObjectID) from @tbDataReturn where AccountingObjectID = @iIDDauKy and Account = @iAccountDauKy)'
		
		set @sqlexc = @sqlexc + N' If(@countCheck > 0)'
		set @sqlexc = @sqlexc + N' Begin'
			set @sqlexc = @sqlexc + N' Update @tbDataReturn Set TonSoTien = @iTonSoTienDauKy, TonQuyDoi = @iTonQuyDoiDauKy' 
			set @sqlexc = @sqlexc + N' where AccountingObjectID = @iIDDauKy and Account = @iAccountDauKy'
		set @sqlexc = @sqlexc + N' End'
		set @sqlexc = @sqlexc + N' Else'
		set @sqlexc = @sqlexc + N' Begin'
			set @sqlexc = @sqlexc + N' Insert Into @tbDataReturn(stt,IdGroup, AccountingObjectID, Account, DienGiai, TonSoTien, TonQuyDoi)'
			set @sqlexc = @sqlexc + N' Values(@stt1, @IdGroup1, @iIDDauKy, @iAccountDauKy, N''Số dư đầu kỳ'', @iTonSoTienDauKy, @iTonQuyDoiDauKy)'
		set @sqlexc = @sqlexc + N' End'
		set @sqlexc = @sqlexc + N' FETCH NEXT FROM cursorDauKy INTO @iIDDauKy, @iAccountDauKy, @iTonSoTienDauKy, @iTonQuyDoiDauKy'
	set @sqlexc = @sqlexc + N' END'
	set @sqlexc = @sqlexc + N' CLOSE cursorDauKy'
	set @sqlexc = @sqlexc + N' DEALLOCATE cursorDauKy'
	
	--set @sqlexc = @sqlexc + N' UPDATE @tbDataReturn SET TonSoTien = a.SoDuDauKySoTien, TonQuyDoi = a.SoDuDauKyQuyDoi'
	--set @sqlexc = @sqlexc + N' FROM (select AccountingObjectID as AOID, Account as Acc, SoDuDauKySoTien, SoDuDauKyQuyDoi from @tbDataDauKy) a'
	--set @sqlexc = @sqlexc + N' WHERE AccountingObjectID = a.AOID AND Account = a.Acc AND stt = 0'
	
	--su dung contro duyet tung dong va tinh gia tri so du luy ke
	set @sqlexc = @sqlexc + N' DECLARE @iID UNIQUEIDENTIFIER'
	set @sqlexc = @sqlexc + N' DECLARE @iAccount NVARCHAR(25)'
	set @sqlexc = @sqlexc + N' DECLARE @iTonSoTien money'
	set @sqlexc = @sqlexc + N' DECLARE @iTonQuyDoi money'
	
	set @sqlexc = @sqlexc + N' DECLARE cursorTon CURSOR FOR'
	set @sqlexc = @sqlexc + N' SELECT AccountingObjectID, Account, TonSoTien, TonQuyDoi FROM @tbDataReturn WHERE stt = 0'
	set @sqlexc = @sqlexc + N' OPEN cursorTon'
	set @sqlexc = @sqlexc + N' FETCH NEXT FROM cursorTon INTO @iID, @iAccount, @iTonSoTien, @iTonQuyDoi'
	set @sqlexc = @sqlexc + N' WHILE @@FETCH_STATUS = 0'
	set @sqlexc = @sqlexc + N' BEGIN'
		set @sqlexc = @sqlexc + N' Declare @tonSoTien money'
		set @sqlexc = @sqlexc + N' set @tonSoTien = ISNULL(@iTonSoTien,0)'
		set @sqlexc = @sqlexc + N' Declare @tonQuyDoi money'
		set @sqlexc = @sqlexc + N' set @tonQuyDoi = ISNULL(@iTonQuyDoi,0)'
		
		set @sqlexc = @sqlexc + N' Declare @minStt bigint'
		set @sqlexc = @sqlexc + N' set @minStt = (select MIN(stt) from @tbDataReturn where AccountingObjectID = @iID and Account = @iAccount and stt > 0)'
		set @sqlexc = @sqlexc + N' Declare @maxStt bigint'
		set @sqlexc = @sqlexc + N' set @maxStt = (select MAX(stt) from @tbDataReturn where AccountingObjectID = @iID and Account = @iAccount and stt > 0)'
		set @sqlexc = @sqlexc + N' WHILE @minStt <= @maxStt'
		set @sqlexc = @sqlexc + N' Begin'
			set @sqlexc = @sqlexc + N' set @tonSoTien = @tonSoTien + ISNULL((select PSNSoTien from @tbDataReturn where stt = @minStt),0) - ISNULL((select PSCSoTien from @tbDataReturn where stt = @minStt),0)'
			set @sqlexc = @sqlexc + N' set @tonQuyDoi = @tonQuyDoi + ISNULL((select PSNQuyDoi from @tbDataReturn where stt = @minStt),0) - ISNULL((select PSCQuyDoi from @tbDataReturn where stt = @minStt),0)'
			set @sqlexc = @sqlexc + N' UPDATE @tbDataReturn SET TonSoTien = @tonSoTien, TonQuyDoi = @tonQuyDoi WHERE stt = @minStt'
			set @sqlexc = @sqlexc + N' set @minStt = @minStt + 1'
		set @sqlexc = @sqlexc + N' End'
		set @sqlexc = @sqlexc + N' FETCH NEXT FROM cursorTon INTO @iID, @iAccount, @iTonSoTien, @iTonQuyDoi'
	set @sqlexc = @sqlexc + N' END'
	set @sqlexc = @sqlexc + N' CLOSE cursorTon'
	set @sqlexc = @sqlexc + N' DEALLOCATE cursorTon'
	
	--lay du lieu nhom theo Account
	set @sqlexc = @sqlexc + N' DECLARE @IdGroup3 smallint'
	set @sqlexc = @sqlexc + N' SET @IdGroup3 = 3'
	
	set @sqlexc = @sqlexc + N' INSERT INTO @tbDataReturn(IdGroup, AccountingObjectID, Account, PSNSoTien, PSNQuyDoi, PSCSoTien, PSCQuyDoi)'
	set @sqlexc = @sqlexc + N' SELECT @IdGroup3 as IdGroup, AccountingObjectID, Account, SUM(PSNSoTien) as PSNSoTien, SUM(PSNQuyDoi) as PSNQuyDoi'
	set @sqlexc = @sqlexc + N' , SUM(PSCSoTien) as PSCSoTien, SUM(PSCQuyDoi) as PSCQuyDoi'
	set @sqlexc = @sqlexc + N' FROM @tbDataReturn WHERE IdGroup = 5'
	set @sqlexc = @sqlexc + N' GROUP BY AccountingObjectID, Account'
	
	set @sqlexc = @sqlexc + N' DECLARE @iID1 UNIQUEIDENTIFIER'
	set @sqlexc = @sqlexc + N' DECLARE @iAccount1 NVARCHAR(25)'
	
	set @sqlexc = @sqlexc + N' DECLARE cursorTon1 CURSOR FOR'
	set @sqlexc = @sqlexc + N' SELECT AccountingObjectID, Account FROM @tbDataReturn WHERE stt = 0'
	set @sqlexc = @sqlexc + N' OPEN cursorTon1'
	set @sqlexc = @sqlexc + N' FETCH NEXT FROM cursorTon1 INTO @iID1, @iAccount1'
	set @sqlexc = @sqlexc + N' WHILE @@FETCH_STATUS = 0'
	set @sqlexc = @sqlexc + N' BEGIN'
		set @sqlexc = @sqlexc + N' Declare @maxStt1 bigint'
		set @sqlexc = @sqlexc + N' set @maxStt1 = (select MAX(stt) from @tbDataReturn where AccountingObjectID = @iID1 and Account = @iAccount1 and stt > 0)'
		set @sqlexc = @sqlexc + N' If(@maxStt1 > 0)'
		set @sqlexc = @sqlexc + N' Begin'
			set @sqlexc = @sqlexc + N' UPDATE @tbDataReturn SET TonSoTien = (select TonSoTien from @tbDataReturn where stt = @maxStt1)'
			set @sqlexc = @sqlexc + N' ,TonQuyDoi = (select TonQuyDoi from @tbDataReturn where stt = @maxStt1) WHERE AccountingObjectID = @iID1'
			set @sqlexc = @sqlexc + N' AND Account = @iAccount1 AND IdGroup = 3'
		set @sqlexc = @sqlexc + N' End'
		set @sqlexc = @sqlexc + N' Else'
		set @sqlexc = @sqlexc + N' Begin'
			set @sqlexc = @sqlexc + N' UPDATE @tbDataReturn SET TonSoTien = (select TonSoTien from @tbDataReturn where stt = 0 and AccountingObjectID = @iID1 and Account = @iAccount1)'
			set @sqlexc = @sqlexc + N' ,TonQuyDoi = (select TonQuyDoi from @tbDataReturn where stt = 0 and AccountingObjectID = @iID1 and Account = @iAccount1) WHERE AccountingObjectID = @iID1'
			set @sqlexc = @sqlexc + N' AND Account = @iAccount1 AND IdGroup = 3'
		set @sqlexc = @sqlexc + N' End'
		set @sqlexc = @sqlexc + N' FETCH NEXT FROM cursorTon1 INTO @iID1, @iAccount1'
	set @sqlexc = @sqlexc + N' END'
	set @sqlexc = @sqlexc + N' CLOSE cursorTon1'
	set @sqlexc = @sqlexc + N' DEALLOCATE cursorTon1'
	
	set @sqlexc = @sqlexc + N' UPDATE @tbDataReturn SET DuNoSoTien = TonSoTien WHERE TonSoTien > 0'
	set @sqlexc = @sqlexc + N' UPDATE @tbDataReturn SET DuCoSoTien = TonSoTien WHERE TonSoTien < 0'
	
	set @sqlexc = @sqlexc + N' UPDATE @tbDataReturn SET DuNoQuyDoi = TonQuyDoi WHERE TonQuyDoi > 0'
	set @sqlexc = @sqlexc + N' UPDATE @tbDataReturn SET DuCoQuyDoi = TonQuyDoi WHERE TonQuyDoi < 0'
	
	set @sqlexc = @sqlexc + N' UPDATE @tbDataReturn SET DuCoSoTien = (-1 * DuCoSoTien) WHERE DuCoSoTien < 0'
	set @sqlexc = @sqlexc + N' UPDATE @tbDataReturn SET DuCoQuyDoi = (-1 * DuCoQuyDoi) WHERE DuCoQuyDoi < 0'
	
	--lay du lieu nhom theo Doi Tuong Ke Toan
	set @sqlexc = @sqlexc + N' DECLARE @IdGroup2 smallint'
	set @sqlexc = @sqlexc + N' SET @IdGroup2 = 1'
	
	set @sqlexc = @sqlexc + N' INSERT INTO @tbDataReturn(IdGroup, AccountingObjectID, PSNSoTien, PSNQuyDoi, PSCSoTien, PSCQuyDoi'
	set @sqlexc = @sqlexc + N' , DuNoSoTien, DuNoQuyDoi, DuCoSoTien, DuCoQuyDoi)'
	set @sqlexc = @sqlexc + N' SELECT @IdGroup2 as IdGroup, AccountingObjectID, SUM(PSNSoTien) as PSNSoTien, SUM(PSNQuyDoi) as PSNQuyDoi'
	set @sqlexc = @sqlexc + N' , SUM(PSCSoTien) as PSCSoTien, SUM(PSCQuyDoi) as PSCQuyDoi, SUM(DuNoSoTien) as DuNoSoTien'
	set @sqlexc = @sqlexc + N' , SUM(DuNoQuyDoi) as DuNoQuyDoi, SUM(DuCoSoTien) as DuCoSoTien, SUM(DuCoQuyDoi) as DuCoQuyDoi'
	set @sqlexc = @sqlexc + N' FROM @tbDataReturn WHERE IdGroup = 3'
	set @sqlexc = @sqlexc + N' GROUP BY AccountingObjectID'
	
	set @sqlexc = @sqlexc + N' UPDATE @tbDataReturn SET AccountingObjectName = AJ.AccountingObjectName'
	set @sqlexc = @sqlexc + N' FROM ( select ID,AccountingObjectName from AccountingObject) AJ'
	set @sqlexc = @sqlexc + N' where AccountingObjectID = AJ.ID'
	
	set @sqlexc = @sqlexc + N' SELECT stt,IdGroup, AccountingObjectID, AccountingObjectName, Account, NgayHoachToan, NgayChungTu, SoChungTu, DienGiai, TKDoiUng, TyGiaHoiDoai'
	set @sqlexc = @sqlexc + N',PSNSoTien, PSNQuyDoi, PSCSoTien, PSCQuyDoi, DuNoSoTien, DuNoQuyDoi, DuCoSoTien, DuCoQuyDoi,RefID,RefType'
	set @sqlexc = @sqlexc + N' FROM @tbDataReturn'
	set @sqlexc = @sqlexc + N' WHERE (ISNULL(PSNSoTien,0) > 0) OR (ISNULL(PSNQuyDoi,0) > 0) OR (ISNULL(PSCSoTien,0) > 0) OR (ISNULL(PSCQuyDoi,0) > 0)'
	set @sqlexc = @sqlexc + N' OR (ISNULL(DuNoSoTien,0) > 0) OR (ISNULL(DuNoQuyDoi,0) > 0) OR (ISNULL(DuCoSoTien,0) > 0) OR (ISNULL(DuCoQuyDoi,0) > 0)'
	set @sqlexc = @sqlexc + N' ORDER BY AccountingObjectID, OrderPriority, Account, IdGroup, stt'
	
	--select @sqlexc
	
	EXECUTE sp_executesql @sqlexc
END
