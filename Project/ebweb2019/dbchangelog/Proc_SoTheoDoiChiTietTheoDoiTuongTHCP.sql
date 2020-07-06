-- =============================================
-- Author:		dieunn
-- Create date: <06/12/2020>
-- Description:	So theo doi THCP theo tai khoan
-- Proc_SoTheoDoiChiTietTheoDoiTuongTHCP @FromDate = '2020-01-01',@ToDate = '2020-12-12',@CostSetID = ',c67c2603-f75d-8246-b2c7-8488ad091652,a90069f0-9ab1-8f40-a3b3-d9afd86f198c,7c98897a-8578-8e49-81ff-bf1706158603,b3f6796f-97e9-b744-b56c-323da287537c,e1c076ba-f090-c74c-9c83-ae1bbb9b0b4e,5bd7cbcd-589a-7f48-9f76-fef7d9f3552f,f8044ebc-c437-e549-bdf6-4cd641de8ba4,8cfcc3ec-116d-4344-9268-b15493a1d556,f0d70325-6e24-ba4b-b40a-a908607f313f,77b99361-d930-e447-a357-8ee6bf0de6f3,b62668b6-9d9c-894e-b7fe-9a872d341644,',@Account ='152'
-- =============================================

create PROCEDURE [dbo].[Proc_SoTheoDoiChiTietTheoDoiTuongTHCP] @FromDate DATETIME, @ToDate DATETIME,
                                                               @CostSetID NVARCHAR(MAX), @Account NVARCHAR(MAX),
                                                               @PhienLamViec INT
AS
BEGIN

    DECLARE @tblSoTheoDoiTheoDT TABLE
                                (
                                    CostSetID     UNIQUEIDENTIFIER,
                                    RefID         UNIQUEIDENTIFIER,
                                    TypeID        INT,
                                    CostSetCode   NVARCHAR(25),
                                    CostSetName   NVARCHAR(512),
                                    NgayHoachToan DATETIME,
                                    NgayChungTu   DATETIME,
                                    No            NVARCHAR(25),
                                    DienGiai      NVARCHAR(MAX),
                                    TK            NVARCHAR(50),
                                    TKDoiUng      NVARCHAR(50),
                                    SoTienNo      money,
                                    SoTienCo      money,
                                    OrderPriority INT
                                )


    DECLARE @tblListCostSetID TABLE
                              (
                                  CostSetID UNIQUEIDENTIFIER
                              )
    DECLARE @tblListAccount TABLE
                            (
                                Account NVARCHAR(MAX)
                            )


    INSERT INTO @tblListCostSetID
    SELECT TG.id as CostSetID
    FROM CostSet AS TG
             LEFT JOIN dbo.Func_ConvertStringIntoTable_Nvarchar(@CostSetID, ',') AS CostSetID
                       ON TG.ID = CostSetID.Value
    WHERE CostSetID.Value IS NOT NULL

    INSERT INTO @tblListAccount
    SELECT TG.AccountNumber AS Account
    FROM AccountList AS TG
             LEFT JOIN dbo.Func_ConvertStringIntoTable_Nvarchar(@Account, ',') AS A
                       ON TG.AccountNumber like (A.Value + '%')
    WHERE A.Value IS NOT NULL
    group by TG.AccountNumber
    order by Tg.AccountNumber

    INSERT INTO @tblSoTheoDoiTheoDT
    SELECT a.CostSetID,
           a.ReferenceID         as RefID,
           a.TypeID,
           b.CostSetCode,
           b.CostSetName,
           a.PostedDate          as NgayHoachToan,
           a.Date                as NgayChungTu,
           CASE
               WHEN @PhienLamViec = 0 THEN a.NoFBook
               ELSE
                   a.NoMBook END as No,
           Reason                as DienGiai,
           a.Account             as TK,
           AccountCorresponding  as TKDoiUng,
           DebitAmount           as SoTienNo,
           CreditAmount          as SoTienCo,
           a.OrderPriority

    FROM [GeneralLedger] a
             LEFT JOIN [CostSet] b ON a.CostSetID = b.ID
    WHERE (a.Date BETWEEN @FromDate AND @ToDate)
      AND a.CostSetID in (select c.CostSetID from @tblListCostSetID c)
      AND a.Account in (select d.Account from @tblListAccount d)
      AND (a.DebitAmount != 0 OR a.CreditAmount != 0)
    ORDER BY a.CostSetID, a.PostedDate, No, TK, TKDoiUng DESC, a.OrderPriority


    SELECT RefID,
           TypeID,
           CostSetCode,
           CostSetName,
           NgayChungTu   AS Date,
           NgayHoachToan AS DateHT,
           No,
           DienGiai      AS Reason,
           TK            AS Account,
           TKDoiUng      AS AccountCorresponding,
           SoTienNo      AS DebitAmount,
           SoTienCo      AS CreditAmount,
           OrderPriority
    FROM @tblSoTheoDoiTheoDT
    ORDER BY CostSetCode, Date, No, OrderPriority
END
go

