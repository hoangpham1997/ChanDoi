/*
-- =============================================
-- Author:		<namnh>
-- Create date: <12-04-2020>
-- Description:	<Check data khi chuyển từ dùng riêng -> dùng chung trong tuỳ chọn >
-- =============================================
*/
-- [CheckGeneralUse] '09FE5FE9-9BCC-D242-A9F1-9F2EA9A78D00',',09FE5FE9-9BCC-D242-A9F1-9F2EA9A78D00,2678689F-7005-E343-A01C-033056C3A89A,7D51BF1D-5525-DC4A-99F3-11B59469F2BB,D63AF0FD-0D60-3743-8A45-125ED24366CB,BEB343BA-EEE4-0E45-8A72-28971E2F89CB,B4F2505D-C568-EB4D-BBD1-4C433637AE1C,2CA85112-BF40-614F-8881-510BC9EDAD55,761D463F-7068-E848-AF53-56DB1D03A87C,71232EAA-6A9A-1F4D-B31B-9D481729A823,813AE293-DE4C-0741-8ED5-A23D9BEFBC2B,B7E3860A-36F2-CA4D-BD3D-D40194C3E8B9,F5706A07-E95B-E94F-8A52-E5379EB150B0,6210A213-92A6-B745-8E5D-F4E914943248,', ',1,2,3,4,6,7,8,'
    ALTER proc [dbo].[CheckGeneralUse] @CompanyID UNIQUEIDENTIFIER, @listCompanyID NVARCHAR(3000), @listCheck NVARCHAR(128)
    as
    begin
        DECLARE @tblListCheck TABLE
                              (
                                  id INT
                              )
        INSERT INTO @tblListCheck
        SELECT ListCheck.Value
        FROM Func_ConvertStringIntoTable_Nvarchar(@listCheck, ',') AS ListCheck
        WHERE ListCheck.Value IS NOT NULL
        DECLARE @tblListCompanyID TABLE
                                  (
                                      orgID UNIQUEIDENTIFIER
                                  )
                INSERT INTO @tblListCompanyID
        SELECT TG.ID
        FROM EbOrganizationUnit AS TG
                 INNER JOIN Func_ConvertStringIntoTable_Nvarchar(@listCompanyID, ',') AS Company
                            ON TG.ID = Company.Value
        WHERE Company.Value IS NOT NULL
        SELECT Code, ID, Name, CompanyID, Type, NameCategory, row_num
        into #tg1
        FROM (SELECT id                                                                                  as ID
                   , AccountingObjectCode                                                                as Code
                   , AccountingObjectName                                                                as Name
                   , CompanyID                                                                           as CompanyID
                   , 1                                                                                   as Type
                   , CASE
                         WHEN ObjectType = 0 AND IsEmployee = 0 THEN N'Nhà cung cấp'
                         WHEN ObjectType = 0 AND IsEmployee = 1 THEN N'Nhà cung cấp/Nhân viên'
                         WHEN ObjectType = 1 AND IsEmployee = 0 THEN N'Khách hàng'
                         WHEN ObjectType = 1 AND IsEmployee = 1 THEN N'Khách hàng/Nhân viên'
                         WHEN ObjectType = 2 AND IsEmployee = 0 THEN N'Khách hàng/Nhà cung cấp'
                         WHEN ObjectType = 2 AND IsEmployee = 1 THEN N'Khách hàng/Nhà cung cấp/Nhân viên'
                         WHEN ObjectType IS NULL AND IsEmployee = 1 THEN N'Nhân viên'
                         WHEN ObjectType = 3 AND IsEmployee = 0 THEN N'Khác'
                         WHEN ObjectType = 3 AND IsEmployee = 1 THEN N'Nhân viên/Khác'
                         ELSE '' END                                                                     as NameCategory
                   , row_number() OVER (PARTITION BY AccountingObjectCode order by AccountingObjectCode) AS row_num
              from AccountingObject
              where CompanyID in (SELECT * from @tblListCompanyID)
              UNION ALL
              SELECT id                                                                      as ID
                   , FixedAssetCode                                                          as Code
                   , FixedAssetName                                                          as Name
                   , CompanyID                                                               as CompanyID
                   , 2                                                                       as Type
                   , N'Tài sản cố định'                                                      as NameCategory
                   , row_number() OVER (PARTITION BY FixedAssetCode order by FixedAssetCode) AS row_num
              from FixedAsset
              where CompanyID in (SELECT * from @tblListCompanyID)
              UNION ALL
              SELECT id                                                          as ID
                   , ToolCode                                                    as Code
                   , ToolName                                                    as Name
                   , CompanyID                                                   as CompanyID
                   , 3                                                           as Type
                   , N'Công cụ dụng cụ'                                          as NameCategory
                   , row_number() OVER (PARTITION BY ToolCode order by ToolCode) AS row_num
              from Tools
              where CompanyID in (SELECT * from @tblListCompanyID)
              UNION ALL
              SELECT id                                                                as ID
                   , BankAccount                                                       as Code
                   , BankName                                                          as Name
                   , CompanyID                                                         as CompanyID
                   , 4                                                                 as Type
                   , N'Tài khoản ngân hàng'                                            as NameCategory
                   , row_number() OVER (PARTITION BY BankAccount order by BankAccount) AS row_num
              from BankAccountDetail
              where CompanyID in (SELECT * from @tblListCompanyID)
              UNION ALL
              SELECT id                                                                            as ID
                   , MaterialGoodsCode                                                             as Code
                   , MaterialGoodsName                                                             as Name
                   , CompanyID                                                                     as CompanyID
                   , 5                                                                             as Type
                   , N'Vật tư hàng hoá'                                                            as NameCategory
                   , row_number() OVER (PARTITION BY MaterialGoodsCode order by MaterialGoodsCode) AS row_num
              from MaterialGoods
              where CompanyID in (SELECT * from @tblListCompanyID)
              UNION ALL
              SELECT id                                                                      as ID
                   , RepositoryCode                                                          as Code
                   , RepositoryName                                                          as Name
                   , CompanyID                                                               as CompanyID
                   , 6                                                                       as Type
                   , N'Kho'                                                                  as NameCategory
                   , row_number() OVER (PARTITION BY RepositoryCode order by RepositoryCode) AS row_num
              from Repository
              where CompanyID in (SELECT * from @tblListCompanyID)
              UNION ALL
              SELECT id                                                                as ID
                   , CostSetCode                                                       as Code
                   , CostSetName                                                       as Name
                   , CompanyID                                                         as CompanyID
                   , 7                                                                 as Type
                   , N'Đối tượng tập hợp chi phí'                                                as NameCategory
                   , row_number() OVER (PARTITION BY CostSetCode order by CostSetCode) AS row_num
              from CostSet
              where CompanyID in (SELECT * from @tblListCompanyID)
              UNION ALL
              SELECT id                                                                          as ID
                   , CreditCardNumber                                                            as Code
                   , ''                                                                          as Name
                   , CompanyID                                                                   as CompanyID
                   , 8                                                                           as Type
                   , N'Thẻ tín dụng'                                                             as NameCategory
                   , row_number() OVER (PARTITION BY CreditCardNumber order by CreditCardNumber) AS row_num
              from CreditCard
              where CompanyID in (SELECT * from @tblListCompanyID)) as #RS
        WHERE Type IN (SELECT id FROM @tblListCheck)
        ORDER BY Type
        SELECT * from #tg1 WHERE Code IN (SELECT code from #tg1 WHERE row_num > 1) AND CompanyID = @CompanyID
        drop table #tg1
    end
go

