---------------------------------------------------------------------------
--  Proc_SoTheoDoiMaThongKeTheoSoTaiKhoan '2020-06-01','2020-06-03','d20ac97c-9479-3046-b77a-e4cc7e8e38b1,a0cf9748-718a-9045-9355-c662969c09c4,abd1ca9e-a35f-7e4c-8f9f-4ead01637142,7bc1ee9f-9b8c-8a47-89da-5596a3484441,53b76074-0c50-224f-8213-67ac1545f9fb,',
--  '111',
--  'CAA09998-422C-C742-BF50-F26B2548BAEE', false, 0
----------------------------------------------------------------------------
alter PROCEDURE [dbo].[Proc_SoTheoDoiMaThongKeTheoSoTaiKhoan]
    @FromDate DATETIME,
    @ToDate DATETIME,
    @StatisticsCodeID  NVARCHAR(MAX),
	@Account NVARCHAR(MAX),
    @CompanyID UNIQUEIDENTIFIER,
    @IsDependent bit,
    @TypeLedger INT
AS
BEGIN
	DECLARE @tblSoTheoDoiTheoMTK TABLE(
			StatisticsCodeID uniqueidentifier,
			StatisticsCode  NVARCHAR(25),
			StatisticsCodeName NVARCHAR(512),
			NgayChungTu Date,
			NgayHachToan Date,
			 SoChungTu NCHAR(20),
			 DienGiai NVARCHAR(MAX),
			 TK NVARCHAR(50),
			 TKDoiUng NVARCHAR(50),
			 SoTienNo money,
			 SoTienCo money,
			 OrderPriority int,
			 RefID uniqueidentifier,
			 RefType int
			 )

    DECLARE @tblResult TABLE(
			StatisticsCodeID uniqueidentifier,
			StatisticsCode  NVARCHAR(25),
			StatisticsCodeName NVARCHAR(512),
			NgayChungTu Date,
			NgayHachToan Date,
			 SoChungTu NCHAR(20),
			 DienGiai NVARCHAR(MAX),
			 TK NVARCHAR(50),
			 TKDoiUng NVARCHAR(50),
			 SoTienNo money,
			 SoTienCo money,
			 OrderPriority int,
			 RefID uniqueidentifier,
			 RefType int,
			 TongNo money,
			 TongCo money
			 )

    DECLARE @tblListCompanyID TABLE
                                  (
                                      CompanyID UNIQUEIDENTIFIER
                                  )

	DECLARE @tblListStatisticsCodeID TABLE
	(
	StatisticsCodeID uniqueidentifier
	)
	DECLARE @tblListAccount TABLE
	(
	Account NVARCHAR(25)
	)
	DECLARE @tblAccountID TABLE
	(
	   ID uniqueidentifier
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

		 INSERT  INTO @tblListStatisticsCodeID
         SELECT  TG.id
         FROM    StatisticsCode AS TG
                 LEFT JOIN dbo.Func_ConvertStringIntoTable_Nvarchar(@StatisticsCodeID,',') AS StatisticCodeID ON TG.ID = StatisticCodeID.Value
         WHERE  StatisticCodeID.Value IS NOT NULL

	    INSERT INTO @tblListAccount
        SELECT TG.AccountNumber
        FROM AccountList AS TG
                 INNER JOIN Func_ConvertStringIntoTable_Nvarchar(@Account, ',') AS Account
                            ON TG.AccountNumber = Account.Value
        WHERE Account.Value IS NOT NULL

	    INSERT INTO @tblAccountID
	    SELECT al.ID FROM AccountList al
	    WHERE al.AccountNumber in (select tla.Account from @tblListAccount tla) and al.CompanyID = @bigOrgID and al.ParentAccountID is null

	    INSERT INTO @tblListAccount
	    SELECT al.AccountNumber FROM AccountList al
	    WHERE al.ParentAccountID in (select id from @tblAccountID)

		INSERT INTO @tblSoTheoDoiTheoMTK
		SELECT a.StatisticsCodeID,
		       b.StatisticsCode,
		       b.StatisticsCodeName ,
		       a.Date as NgayChungTu ,
		       a.PostedDate as NgayHachToan,
		       (CASE When @TypeLedger = 0 then NoFBook else NoMBook end) as SoChungTu,
		       Reason as DienGiai,
		       Account as TK,
		       AccountCorresponding as TKDoiUng,
		       DebitAmount as SoTienNo,
		       CreditAmount as SoTienCo,
		       a.OrderPriority,
		       a.ReferenceID as RefID,
		       a.TypeID as RefType
		FROM [GeneralLedger] a
		LEFT JOIN [StatisticsCode] b ON a.StatisticsCodeID = b.ID
		where StatisticsCodeID in (select StatisticsCodeID from @tblListStatisticsCodeID)   AND a.Account in (select distinct Account from @tblListAccount)
		and a.PostedDate >= @FromDate and a.PostedDate <= @ToDate
		and a.CompanyID in (select CompanyID from @tblListCompanyID)
		order by Date, a.OrderPriority

	    INSERT INTO @tblResult
	    select a.StatisticsCodeID,
		       a.StatisticsCode,
		       a.StatisticsCodeName ,
		       a.NgayChungTu ,
	           a.NgayHachToan,
		       a.SoChungTu,
		       a.DienGiai,
		       a.TK,
		       a.TKDoiUng,
		       a.SoTienNo,
		       a.SoTienCo,
		       a.OrderPriority,
		       a.RefID,
		       a.RefType,
	           (select sum(x.SoTienNo) from @tblSoTheoDoiTheoMTK x where x.StatisticsCode = a.StatisticsCode) as TongNo,
	           (select sum(x.SoTienCo) from @tblSoTheoDoiTheoMTK x where x.StatisticsCode = a.StatisticsCode) as TongCo
        from @tblSoTheoDoiTheoMTK a

		Select * From @tblResult order by StatisticsCode,NgayChungTu,NgayHachToan,SoChungTu,OrderPriority
END
go

