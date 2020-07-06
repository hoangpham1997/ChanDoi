/*
-- =============================================
-- Author:		<namnh>
-- Create date: <12-04-2020>
-- Description:	<Check data khi chuyển từ dùng chung -> dùng riêng trong tuỳ chọn >
-- =============================================
*/
-- [CheckPrivateUse] ',7EE38755-5D68-0143-8B97-6C3FFB45EB15,A4E11A0A-5BD5-9546-AC91-044B26479BB2,9CFFAE11-DE2D-FE47-B1C9-3DF7E25ED8CB,CE570747-8804-2A4A-968A-4F0DF88A6CBA,81D29D22-296F-FE4A-85AB-8604F29AA489,C24C24A3-8C18-D042-A9AE-B2A52BE19F0F,40D44746-DDB9-3448-AEDE-F52F70D018CB,', ',1,2,3,4,6,7,8,'
-- [CheckPrivateUse] ',D5118CBF-5E60-0744-9FEC-695956D80173,BCC06C46-524B-EC44-8989-428CB31667EF,8F4E46DC-59A5-B049-9E62-8F0071FB1BA0,4AE2CA4F-42E4-2F4E-87D5-BE90D0961FF2,31703638-1947-7141-B895-DF3896C7BFDF,8BFD8A98-7D95-7241-BE7D-F152273B4F8B,6E5C460E-D276-8B4A-9A46-F2CC5155DD57,7B885FCC-2218-E94F-8505-F91107D351E2,',',1,2,3,4,5,6,7,8,'

    ALTER proc [dbo].[CheckPrivateUse] @CompanyID NVARCHAR(3000), @listCheck NVARCHAR(128)
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
                 INNER JOIN Func_ConvertStringIntoTable_Nvarchar(@CompanyID, ',') AS Company
                            ON TG.ID = Company.Value
        WHERE Company.Value IS NOT NULL
        DECLARE @tblListCategory TABLE
                                 (
                                     ID        UNIQUEIDENTIFIER,
                                     CompanyID UNIQUEIDENTIFIER,
                                     Type      INT,
                                     Code      NVARCHAR(128)
                                 )
        DECLARE @tblListVoucherUse TABLE
                                   (
                                       ID        UNIQUEIDENTIFIER,
                                       CompanyID UNIQUEIDENTIFIER,
                                       RefID     UNIQUEIDENTIFIER,
                                       nameTable NVARCHAR(128)
                                   )
        INSERT INTO @tblListCategory
        SELECT *
        FROM (SELECT ID, CompanyID, 1 as Type, AccountingObjectCode as Code
              FROM AccountingObject
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
              UNION ALL
              SELECT ID, CompanyID, 2 as Type, FixedAssetCode as Code
              FROM FixedAsset
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
              UNION ALL
              SELECT ID, CompanyID, 3 as Type, ToolCode as Code
              FROM Tools
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
              UNION ALL
              SELECT ID, CompanyID, 4 as Type, BankAccount as Code
              FROM BankAccountDetail
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
              UNION ALL
              SELECT ID, CompanyID, 5 as Type, MaterialGoodsCode as Code
              FROM MaterialGoods
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
              UNION ALL
              SELECT ID, CompanyID, 6 as Type, RepositoryCode as Code
              FROM Repository
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
              UNION ALL
              SELECT ID, CompanyID, 7 as Type, CostSetCode as Code
              FROM CostSet
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
              UNION ALL
              SELECT ID, CompanyID, 8 as Type, CreditCardNumber as Code
              FROM CreditCard
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)) as #RS1


        INSERT INTO @tblListVoucherUse
        SELECT *
        FROM (SELECT b.AccountingObjectID as ID, CompanyID as CompanyID, a.ID as RefID, a.RefTable as NameTable
              FROM ViewVoucherNoForCloseBook a
                       LEFT JOIN ViewVoucherNoDetailForClosebook b ON a.RefID = b.RefParentID
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
              UNION ALL
              SELECT b.BankAccountDetailID as ID, CompanyID as CompanyID, a.ID as RefID, a.RefTable as NameTable
              FROM ViewVoucherNoForCloseBook a
                       LEFT JOIN ViewVoucherNoDetailForClosebook b ON a.RefID = b.RefParentID
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
              UNION ALL
              SELECT b.MaterialGoodsID as ID, CompanyID as CompanyID, a.ID as RefID, a.RefTable as NameTable
              FROM ViewVoucherNoForCloseBook a
                       LEFT JOIN ViewVoucherNoDetailForClosebook b ON a.RefID = b.RefParentID
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
              UNION ALL
              SELECT b.RepositoryID as ID, CompanyID as CompanyID, a.ID as RefID, a.RefTable as NameTable
              FROM ViewVoucherNoForCloseBook a
                       LEFT JOIN ViewVoucherNoDetailForClosebook b ON a.RefID = b.RefParentID
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
              UNION ALL
              SELECT b.CostSetID as ID, CompanyID as CompanyID, a.ID as RefID, a.RefTable as NameTable
              FROM ViewVoucherNoForCloseBook a
                       LEFT JOIN ViewVoucherNoDetailForClosebook b ON a.RefID = b.RefParentID
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
              UNION ALL
              SELECT a.AccountingObjectID as ID, CompanyID as CompanyID, a.ID as RefID, a.RefTable as NameTable
              FROM ViewVoucherNoForCloseBook a
                       LEFT JOIN ViewVoucherNoDetailForClosebook b ON a.RefID = b.RefParentID
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
              UNION ALL
              SELECT a.BankAccountDetailID as ID, CompanyID as CompanyID, a.ID as RefID, a.RefTable as NameTable
              FROM ViewVoucherNoForCloseBook a
                       LEFT JOIN ViewVoucherNoDetailForClosebook b ON a.RefID = b.RefParentID
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
              UNION ALL
              SELECT BankAccountDetailID as ID, organizationunitid as CompanyID, ID as RefID, 'OrganizationUnitOptionReport' as NameTable
              FROM OrganizationUnitOptionReport
              WHERE organizationunitid IN (SELECT orgID from @tblListCompanyID)
                AND BankAccountDetailID IS NOT NULL
              UNION ALL
              SELECT ObjectID, CompanyID, ID as RefID, 'MaterialQuantum' as NameTable
              FROM MaterialQuantum
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
              UNION ALL
              SELECT b.MaterialGoodsID, a.CompanyID, a.ID as RefID, 'MaterialQuantumDetails' as NameTable
              FROM MaterialQuantum a
                       LEFT JOIN MaterialQuantumDetail b ON a.ID = b.MaterialQuantumID
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
              UNION ALL
              SELECT RepositoryID, CompanyID, ID as RefID, 'MaterialGoods' as NameTable
              FROM MaterialGoods
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
                AND RepositoryID IS NOT NULL
              UNION ALL
              SELECT a.MaterialAssemblyID, b.CompanyID, a.ID as RefID, 'MaterialGoodsAssembly' as NameTable
              FROM MaterialGoodsAssembly a
                       LEFT JOIN MaterialGoods b ON a.MaterialGoodsID = b.ID
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
                AND a.MaterialAssemblyID IS NOT NULL
              UNION ALL
              SELECT b.MaterialGoodsID, a.CompanyID, a.ID as RefID, 'CostSet' as NameTable
              FROM CostSet a
                       LEFT JOIN CostSetMaterialGoods b ON a.ID = b.CostSetID
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
                AND b.MaterialGoodsID IS NOT NULL
              UNION ALL
              SELECT MaterialGoodsID as ID, CompanyID as CompanyID, RefID as RefID, 'TableNotHasRecord' as NameTable
              FROM ViewVoucherNoForTableNotHasRecord
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
                AND MaterialGoodsID IS NOT NULL
              UNION ALL
              SELECT AccountingObjectID as ID, CompanyID as CompanyID, RefID as RefID, 'TableNotHasRecord' as NameTable
              FROM ViewVoucherNoForTableNotHasRecord
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
                AND MaterialGoodsID IS NOT NULL
              UNION ALL
              SELECT EmployeeID as ID, CompanyID as CompanyID, RefID as RefID, 'TableNotHasRecord' as NameTable
              FROM ViewVoucherNoForTableNotHasRecord
              WHERE CompanyID IN (SELECT orgID from @tblListCompanyID)
                AND MaterialGoodsID IS NOT NULL
             ) as #RS2
        WHERE ID IS NOT NULL
        SELECT COUNT(*)
        from @tblListVoucherUse a
                 LEFT JOIN @tblListCategory b ON a.ID = b.ID
        WHERE a.CompanyID <> b.CompanyID
          AND b.Type IN (SELECT id FROM @tblListCheck)
    end
GO




