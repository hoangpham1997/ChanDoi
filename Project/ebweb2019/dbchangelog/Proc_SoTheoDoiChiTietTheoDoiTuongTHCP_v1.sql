USE [KT_2019]
GO
/****** Object:  StoredProcedure [dbo].[Proc_SoTheoDoiChiTietTheoDoiTuongTHCP]    Script Date: 5/15/2020 1:45:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- Proc_SoTheoDoiChiTietTheoDoiTuongTHCP '01/01/2019','12/31/2019',',4d27911b-a16a-4712-befd-6a9f882f7fb7,d313efab-6a1e-45f2-aa7d-dfdad5dff076,d0b50fe8-a69c-4d2d-90f4-9df786f77f9e,07a8c203-1660-4c2e-9f5d-4744c2dbcf46,cf79b178-b37e-4a15-95b1-a2928bd7320b,8eea07de-2678-42b9-9249-4f681f60233c,4224305e-6b9f-4aad-9473-37cdf6f5df45,f071180f-02d8-4421-a97b-a78f11725061,7b17eb71-60e4-4279-944f-e46addf2a6b2,27af706f-50a1-449a-b4e5-d8824e48f103,a575197f-8e91-4010-8c0e-b13415f654f2,dd910d08-0683-4532-9a58-f6d454a3ad6e,7a0cde61-8697-4c54-9986-650df61fc546,fc995fe8-a82d-4a77-94ac-4ea77c2204f3,99239f53-7d20-40b0-9e1a-e574cc4ea90a,0bac2a3e-e96e-47e1-a057-22778e325f49,148bbc81-e3c7-4169-97a3-a3c536082b3a,db1214cf-a735-4b65-b059-cc340b0500fd,9372f1a8-6fae-473b-bd2c-65e6549df181,b7d6c705-34d6-4555-834a-b5c41f0a5562,63bbe6ab-8c4f-4b34-85a6-a09ac3a2f9af,f59a83da-1a9f-4166-b506-334383ee5d13,b15e670e-d0b4-4e4c-a622-f878cb4d7561,14eb4576-089c-4e0c-b94d-21e27244d5c4,e3eb234a-ac6e-4b64-91b0-67b3dd72d1bb,5fbb9762-f9cc-400e-838d-ed1b39713880,fafde6c8-4918-4f47-9fa3-4e16320ef532,88950655-89e4-4f9d-a416-d49e2ed2df1b,',',111,1111,1112,112,1121,1122,121,128,1281,1288,131,133,1331,1332,136,1361,1368,138,1381,1386,1388,141,151,152,153,154,155,156,157,211,2111,2112,2113,214,2141,2142,2143,2147,217,228,2281,2288,229,2291,2292,2293,2294,241,2411,2412,2413,242,331,333,3331,33311,33312,3332,3333,3334,3335,3336,3337,3338,33381,33382,3339,334,335,336,3361,3368,338,3381,3382,3383,3384,3385,3386,3387,3388,341,3411,3412,352,3521,3522,3524,353,3531,3532,3533,3534,356,3561,3562,411,4111,4112,4118,413,418,419,421,4211,4212,511,5111,5112,5113,5118,515,611,631,632,635,642,6421,6422,711,811,821,911,'
ALTER PROCEDURE [dbo].[Proc_SoTheoDoiChiTietTheoDoiTuongTHCP]
    @FromDate DATETIME,
    @ToDate DATETIME,
    @CostSetID  NVARCHAR(MAX),
	@Account NVARCHAR(MAX)
AS
BEGIN     
	DECLARE @tblSoTheoDoiTheoDT TABLE(
			CostSetID uniqueidentifier,
			RefID uniqueidentifier,
			TypeID int,
			CostSetCode  NVARCHAR(25),
			CostSetName NVARCHAR(512),
			NgayChungTu Date,
			 SoChungTu NCHAR(20),
			 DienGiai NVARCHAR(MAX),
			 TK NVARCHAR(50),
			 TKDoiUng NVARCHAR(50),
			 SoTienNo decimal(25,0),
			 SoTienCo decimal(25,0),
			 OrderPriority int)
	
	
	DECLARE @tblListCostSetID TABLE
	(
	CostSetID uniqueidentifier
	)
	DECLARE @tblListAccount TABLE
	(
	Account NVARCHAR(25)
	)


		 INSERT  INTO @tblListCostSetID
         SELECT  TG.id
         FROM    CostSet AS TG
                 LEFT JOIN dbo.Func_ConvertStringIntoTable(@CostSetID,',') AS CostSetID ON TG.ID = CostSetID.Value
         WHERE  CostSetID.Value IS NOT NULL

	
		INSERT  INTO @tblListAccount
         SELECT  TG.AccountNumber
         FROM    Account AS TG
                 LEFT JOIN dbo.Func_SplitString(@Account,',') AS Account ON TG.AccountNumber = Account.splitdata
         WHERE  Account.splitdata IS NOT NULL
		
                              			
		INSERT INTO @tblSoTheoDoiTheoDT
		SELECT a.CostSetID, a.ReferenceID as RefID, a.TypeID,b.CostSetCode, b.CostSetName ,Date as NgayChungTu , No as SoChungTu, Reason as DienGiai, Account as TK, AccountCorresponding as TKDoiUng, DebitAmount as SoTienNo, CreditAmount as SoTienCo, a.OrderPriority
		FROM [GeneralLedger] a 
		LEFT JOIN [CostSet] b ON a.CostSetID = b.ID
		where CostSetID in (select CostSetID from @tblListCostSetID) AND a.Account in (select Account from @tblListAccount)
		and Date > = @FromDate and Date < = @ToDate
		order by Date, a.OrderPriority

		Select CostSetID,RefID,TypeID,CostSetCode,CostSetName,NgayChungTu,SoChungTu,DienGiai,TK,TKDoiUng,SoTienNo,SoTienCo From @tblSoTheoDoiTheoDT order by NgayChungTu,SoChungTu,OrderPriority
END






