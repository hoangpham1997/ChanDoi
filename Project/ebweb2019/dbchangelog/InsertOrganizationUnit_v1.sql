-- =============================================
-- Author:		namnh
-- Create date: 21/02/2020
-- Description:	Insert dữ liệu khi tạo cơ cấu tổ chức
-- [InsertOrganizationUnit] '8B1A5AD9-DBBB-C341-B8EF-D40A45FF5F3F', 'VND'
-- =============================================
ALTER proc [dbo].[InsertOrganizationUnit] @companyID UNIQUEIDENTIFIER, @mainCurrency NVARCHAR(3), @accType INTEGER, @IDParent UNIQUEIDENTIFIER
as
begin
    DECLARE @ParentAccountID UNIQUEIDENTIFIER
    DECLARE @AccountNumber NVARCHAR(25)
    DECLARE @LikeAccountNumber NVARCHAR(128)
    DECLARE @Count INT

    --ADD DATA SYSTEM OPTION
    INSERT INTO SystemOption(CompanyID, BranchID, Code, Name, Type, Data, DefaultData, Note, IsSecurity)
    SELECT @companyID,
           BranchID,
           Code,
           Name,
           Type,
           Data,
           DefaultData,
           Note,
           IsSecurity
    FROM SystemOption
    WHERE CompanyID IS NULL
    Order by ID
    --END ADD SYSTEM OPTION

    IF (@IDParent IS NOT NULL)
        BEGIN
            UPDATE SystemOption
            SET Data = (SELECT Data FROM SystemOption WHERE CompanyID = @IDParent AND Code = 'TCKHAC_SDDMDoiTuong')
            WHERE CompanyID = @companyID AND Code = 'TCKHAC_SDDMDoiTuong'
            UPDATE SystemOption
            SET Data = (SELECT Data FROM SystemOption WHERE CompanyID = @IDParent AND Code = 'TCKHAC_SDDMVTHH')
            WHERE CompanyID = @companyID AND Code = 'TCKHAC_SDDMVTHH'
            UPDATE SystemOption
            SET Data = (SELECT Data FROM SystemOption WHERE CompanyID = @IDParent AND Code = 'TCKHAC_SDDMKho')
            WHERE CompanyID = @companyID AND Code = 'TCKHAC_SDDMKho'
            UPDATE SystemOption
            SET Data = (SELECT Data FROM SystemOption WHERE CompanyID = @IDParent AND Code = 'TCKHAC_SDDMCCDC')
            WHERE CompanyID = @companyID AND Code = 'TCKHAC_SDDMCCDC'
            UPDATE SystemOption
            SET Data = (SELECT Data FROM SystemOption WHERE CompanyID = @IDParent AND Code = N'TCKHAC_SDDMTSCĐ')
            WHERE CompanyID = @companyID AND Code = 'TCKHAC_SDDMTSCĐ'
            UPDATE SystemOption
            SET Data = (SELECT Data FROM SystemOption WHERE CompanyID = @IDParent AND Code = N'TCKHAC_SDDMĐTTHCP')
            WHERE CompanyID = @companyID AND Code = 'TCKHAC_SDDMĐTTHCP'
            UPDATE SystemOption
            SET Data = (SELECT Data FROM SystemOption WHERE CompanyID = @IDParent AND Code = N'TCKHAC_SDDMTKNH')
            WHERE CompanyID = @companyID AND Code = 'TCKHAC_SDDMTKNH'
            UPDATE SystemOption
            SET Data = (SELECT Data FROM SystemOption WHERE CompanyID = @IDParent AND Code = N'TCKHAC_SDDMTheTD')
            WHERE CompanyID = @companyID AND Code = 'TCKHAC_SDDMTheTD'
        end

    --ADD DATA PERSONAL SALARY TAX
    INSERT INTO PersonalSalaryTax(ID, CompanyID, BranchID, PersonalSalaryTaxGrade, SalaryType, TaxRate, FromAmount,
                                  ToAmount, IsActive)
    SELECT NEWID(),
           @companyID,
           BranchID,
           PersonalSalaryTaxGrade,
           SalaryType,
           TaxRate,
           FromAmount,
           ToAmount,
           IsActive
    FROM PersonalSalaryTax
    WHERE CompanyID IS NULL
    --END ADD PERSONAL SALARY TAX

    --ADD DATA TIME SHEET SYMBOLS
    INSERT INTO TimeSheetSymbols(ID, CompanyID, BranchID, TimeSheetSymbolsCode, TimeSheetSymbolsName, SalaryRate,
                                 IsDefault, IsHalfDayDefault, IsHalfDay, OverTimeSymbol, IsOverTime, IsActive,
                                 IsSecurity)
    SELECT NEWID(),
           @companyID,
           BranchID,
           TimeSheetSymbolsCode,
           TimeSheetSymbolsName,
           SalaryRate,
           IsDefault,
           IsHalfDayDefault,
           IsHalfDay,
           OverTimeSymbol,
           IsOverTime,
           IsActive,
           IsSecurity
    FROM TimeSheetSymbols
    WHERE CompanyID IS NULL
    --END ADD TIME SHEET SYMBOLS
    -- Namnh Update Lại ParentID các bảng 30/1
    --ADD DATA FIXED ASSET CATEGORY
    INSERT INTO FixedAssetCategory(ID, CompanyID, BranchID, FixedAssetCategoryCode, FixedAssetCategoryName,
                                   Description,
                                   ParentID, IsParentNode, OrderFixCode, Grade, UsedTime, DepreciationRate,
                                   OriginalPriceAccount, DepreciationAccount, ExpenditureAccount, IsActive)
    SELECT NEWID(),
           @companyID,
           BranchID,
           FixedAssetCategoryCode,
           FixedAssetCategoryName,
           Description,
           ParentID,
           IsParentNode,
           OrderFixCode,
           Grade,
           UsedTime,
           DepreciationRate,
           OriginalPriceAccount,
           DepreciationAccount,
           ExpenditureAccount,
           IsActive
    FROM FixedAssetCategory
    WHERE CompanyID IS NULL
    SELECT *
    into #tableParentFixedAssetCategory
    from FixedAssetCategory
    WHERE CompanyID = @companyID
      AND IsParentNode = 1;
    DECLARE @ParentID UNIQUEIDENTIFIER
    DECLARE @FixedAssetCategoryCode NVARCHAR(25)
    DECLARE @LikeFixedAssetCategoryCode NVARCHAR(128)
--     DECLARE @Count INT
    SET @Count = (SELECT COUNT(*) FROM #tableParentFixedAssetCategory)
    WHILE(@Count > 0)
        BEGIN
            SET @FixedAssetCategoryCode = (SELECT
                                           TOP 1
                                           FixedAssetCategoryCode
                                           FROM #tableParentFixedAssetCategory
                                           ORDER BY FixedAssetCategoryCode)
            SET @LikeFixedAssetCategoryCode = (SELECT
                                               TOP 1
                                               FixedAssetCategoryCode
                                               FROM #tableParentFixedAssetCategory
                                               ORDER BY FixedAssetCategoryCode) + '%'
            SET @ParentID = (SELECT TOP 1 ID FROM #tableParentFixedAssetCategory ORDER BY FixedAssetCategoryCode)
            UPDATE FixedAssetCategory
            SET ParentID = @ParentID
            WHERE CompanyID = @companyID
              AND FixedAssetCategoryCode LIKE @LikeFixedAssetCategoryCode
              AND FixedAssetCategoryCode <> @FixedAssetCategoryCode
            DELETE
            FROM #tableParentFixedAssetCategory
            WHERE FixedAssetCategoryCode = @FixedAssetCategoryCode
              AND ID = @ParentID
            SET @Count = @Count - 1
        end
    --END ADD FIXED ASSET CATEGORY

    --ADD DATA Material Goods Resource Tax Group
    INSERT INTO MaterialGoodsResourceTaxGroup(ID, CompanyID, BranchID, MaterialGoodsResourceTaxGroupCode,
                                              MaterialGoodsResourceTaxGroupName, UnitID, TaxRate, OrderFixCode,
                                              ParentID, IsParentNode, Grade, IsActive, IsSecurity)
    SELECT NEWID(),
           @companyID,
           BranchID,
           MaterialGoodsResourceTaxGroupCode,
           MaterialGoodsResourceTaxGroupName,
           UnitID,
           TaxRate,
           OrderFixCode,
           NULL,
           IsParentNode,
           Grade,
           IsActive,
           IsSecurity
    FROM MaterialGoodsResourceTaxGroup
    WHERE CompanyID IS NULL
    SELECT *
    into #tableParentMaterialGoodsResourceTaxGroup
    from MaterialGoodsResourceTaxGroup
    WHERE CompanyID = @companyID
      AND IsParentNode = 1;
    DECLARE @MaterialGoodsResourceTaxGroupCode NVARCHAR(25)
    DECLARE @LikeMaterialGoodsResourceTaxGroupCode NVARCHAR(128)
--     DECLARE @Count INT
    SET @Count = (SELECT COUNT(*) FROM #tableParentMaterialGoodsResourceTaxGroup)
    WHILE(@Count > 0)
        BEGIN
            SET @MaterialGoodsResourceTaxGroupCode = (SELECT
                                                      TOP 1
                                                      MaterialGoodsResourceTaxGroupCode
                                                      FROM #tableParentMaterialGoodsResourceTaxGroup
                                                      ORDER BY MaterialGoodsResourceTaxGroupCode)
            SET @LikeMaterialGoodsResourceTaxGroupCode = (SELECT
                                                          TOP 1
                                                          MaterialGoodsResourceTaxGroupCode
                                                          FROM #tableParentMaterialGoodsResourceTaxGroup
                                                          ORDER BY MaterialGoodsResourceTaxGroupCode) + '%'
            SET @ParentID = (SELECT
                             TOP 1
                             ID
                             FROM #tableParentMaterialGoodsResourceTaxGroup
                             ORDER BY MaterialGoodsResourceTaxGroupCode)
            UPDATE MaterialGoodsResourceTaxGroup
            SET ParentID = @ParentID
            WHERE CompanyID = @companyID
              AND MaterialGoodsResourceTaxGroupCode LIKE @LikeMaterialGoodsResourceTaxGroupCode
              AND MaterialGoodsResourceTaxGroupCode <> @MaterialGoodsResourceTaxGroupCode
            DELETE
            FROM #tableParentMaterialGoodsResourceTaxGroup
            WHERE MaterialGoodsResourceTaxGroupCode = @MaterialGoodsResourceTaxGroupCode
              AND ID = @ParentID
            SET @Count = @Count - 1
        end
    --END ADD Material Goods Resource Tax Group
    -- End Namnh Update Lại ParentID các bảng 30/1
    --ADD DATA Contract State
    INSERT INTO ContractState(CompanyID, BranchID, ContractStateName, Description, IsSecurity)
    SELECT @companyID,
           BranchID,
           ContractStateName,
           Description,
           IsSecurity
    FROM ContractState
    WHERE CompanyID IS NULL
    --END ADD Contract State

    --ADD DATA Gen Code
    INSERT INTO GenCode(ID, CompanyID, BranchID, DisplayOnBook, TypeGroupID, TypeGroupName, Prefix, CurrentValue,
                        Suffix, Length, IsSecurity)
    SELECT NEWID(),
           @companyID,
           BranchID,
           DisplayOnBook,
           TypeGroupID,
           TypeGroupName,
           Prefix,
           CurrentValue,
           Suffix,
           Length,
           IsSecurity
    FROM GenCode
    WHERE CompanyID IS NULL
    --END ADD Gen Code

    --ADD DATA Warranty
    INSERT INTO Warranty(CompanyID, BranchID, WarrantyTime, WarrantyName, Description, IsActive)
    SELECT @companyID,
           BranchID,
           WarrantyTime,
           WarrantyName,
           Description,
           IsActive
    FROM Warranty
    WHERE CompanyID IS NULL
    --END ADD Warranty
    --ADD DATA EbGroup
    INSERT INTO EbGroup(ID, Code, Name, Description, IsSystem, CompanyID)
    SELECT NEWID(),
           Code,
           Name,
           Description,
           IsSystem,
           @companyID
    FROM EbGroup
    WHERE CompanyID IS NULL
    --END ADD EbGroup

    --Add Data EbGroupAuth
    SELECT ID, Code into #newEbGroup From EbGroup WHERE CompanyID = @companyID
    SELECT ID, Code into #oldEbGroup From EbGroup WHERE CompanyID IS NULL
    DECLARE @codeGroup NVARCHAR(512)
    DECLARE @idOldGroup UNIQUEIDENTIFIER
    DECLARE @idNewGroup UNIQUEIDENTIFIER
    SET @Count = (SELECT COUNT(*) from #newEbGroup)
    WHILE(@Count > 0)
        BEGIN
            SET @codeGroup = (SELECT TOP 1 Code FROM #newEbGroup ORDER BY Code)
            SET @idNewGroup = (SELECT TOP 1 ID FROM #newEbGroup ORDER BY Code)
            SET @idOldGroup = (SELECT TOP 1 ID FROM #oldEbGroup ORDER BY Code)
            INSERT INTO EbGroupAuth
            SELECT null, AuthorityId, @idNewGroup
            FROM EbGroupAuth a
                     LEFT JOIN EbGroup b ON a.GroupId = b.ID
            WHERE b.CompanyID IS NULL
              AND a.GroupId = @idOldGroup
            DELETE FROM #newEbGroup WHERE ID = @idNewGroup
            DELETE FROM #oldEbGroup WHERE ID = @idOldGroup
            SET @Count = @Count - 1
        END
    -- End Add EbGroupAuth

    --ADD DATA UNIT
    INSERT INTO Unit(ID, CompanyID, BranchID, UnitName, UnitDescription, IsActive)
    SELECT NEWID(),
           @companyID,
           BranchID,
           UnitName,
           UnitDescription,
           IsActive
    FROM Unit
    WHERE CompanyID IS NULL
    --END ADD UNIT

    IF (@accType = 1)
        BEGIN
            -- ADD DATA ACCOUNT LIST
            INSERT INTO AccountList (ID, CompanyID, BranchID, AccountingType, AccountNumber, AccountName,
                                     AccountNameGlobal,
                                     Description, IsParentNode, Grade, AccountGroupKind, DetailType, IsActive,
                                     DetailByAccountObject, IsForeignCurrency)
            SELECT NEWID(),
                   @companyID,
                   BranchID,
                   AccountingType,
                   AccountNumber,
                   AccountName,
                   AccountNameGlobal,
                   Description,
                   IsParentNode,
                   Grade,
                   AccountGroupKind,
                   DetailType,
                   IsActive,
                   DetailByAccountObject,
                   IsForeignCurrency
            FROM AccountList
            WHERE CompanyID IS NULL
            ORDER BY AccountNumber
            SELECT *
            into #tableParentAccountList
            from AccountList
            WHERE CompanyID = @companyID
              AND IsParentNode = 1;
            SET @Count = (SELECT COUNT(*) FROM #tableParentAccountList)
            WHILE(@Count > 0)
                BEGIN
                    SET @AccountNumber =
                            (SELECT TOP 1 AccountNumber FROM #tableParentAccountList ORDER BY AccountNumber)
                    SET @LikeAccountNumber =
                                (SELECT TOP 1 AccountNumber FROM #tableParentAccountList ORDER BY AccountNumber) +
                                '%'
                    SET @ParentAccountID = (SELECT TOP 1 ID FROM #tableParentAccountList ORDER BY AccountNumber)
                    UPDATE AccountList
                    SET ParentAccountID = @ParentAccountID
                    WHERE CompanyID = @companyID
                      AND AccountNumber LIKE @LikeAccountNumber
                      AND AccountNumber <> @AccountNumber
                    DELETE
                    FROM #tableParentAccountList
                    WHERE AccountNumber = @AccountNumber
                      AND ID = @ParentAccountID
                    SET @Count = @Count - 1
                end
            --     SELECT * from AccountList WHERE CompanyID = @companyID ORDER BY AccountNumber
            --END ADD ACCOUNT LIST


            --ADD DATA ACCOUNT DEFAULT
            INSERT INTO AccountDefault(ID, CompanyID, BranchID, AccountingType, TypeID, ColumnName, ColumnCaption,
                                       FilterAccount,
                                       DefaultAccount, ReduceAccount, PPType, OrderPriority)
            SELECT NEWID(),
                   @companyID,
                   BranchID,
                   AccountingType,
                   TypeID,
                   ColumnName,
                   ColumnCaption,
                   FilterAccount,
                   DefaultAccount,
                   ReduceAccount,
                   PPType,
                   OrderPriority
            FROM AccountDefault
            WHERE CompanyID IS NULL
            --END ADD ACCOUNT DEFAULT


            --ADD DATA ACCOUNT TRANSFER
            INSERT INTO AccountTransfer(ID, CompanyID, AccountingType, AccountTransferCode, AccountTransferOrder,
                                        Description,
                                        FromAccount, ToAccount, FromAccountData, DebitAccount, CreditAccount,
                                        IsActive,
                                        IsSecurity)
            SELECT NEWID(),
                   @companyID,
                   AccountingType,
                   AccountTransferCode,
                   AccountTransferOrder,
                   Description,
                   FromAccount,
                   ToAccount,
                   FromAccountData,
                   DebitAccount,
                   CreditAccount,
                   IsActive,
                   IsSecurity
            FROM AccountTransfer
            WHERE CompanyID IS NULL
            --END ADD ACCOUNT TRANSFER

            --ADD DATA AUTO PRINCIPLE
            INSERT INTO AutoPrinciple(ID, CompanyID, BranchID, AccountingType, AutoPrincipleName, TypeID,
                                      DebitAccount,
                                      CreditAccount, Description, IsActive, NumberAttach)
            SELECT NEWID(),
                   @companyID,
                   BranchID,
                   AccountingType,
                   AutoPrincipleName,
                   TypeID,
                   DebitAccount,
                   CreditAccount,
                   Description,
                   IsActive,
                   NumberAttach
            FROM AutoPrinciple
            WHERE CompanyID IS NULL
            --END ADD AUTO PRINCIPLE

            --ADD DATA BANK
            INSERT INTO Bank(ID, CompanyID, BranchID, BankCode, BankName, BankNameRepresent, Address, Description,
                             IsActive)
            SELECT NEWID(),
                   @companyID,
                   BranchID,
                   BankCode,
                   BankName,
                   BankNameRepresent,
                   Address,
                   Description,
                   IsActive
            FROM Bank
            WHERE CompanyID IS NULL
            --END ADD BANK

            --ADD DATA CURRENCY
            INSERT INTO Currency(ID, CompanyID, BranchID, CurrencyCode, CurrencyName, ExchangeRate, IsActive,
                                 Formula)
            SELECT NEWID(),
                   @companyID,
                   BranchID,
                   CurrencyCode,
                   CurrencyName,
                   1,
                   1,
                   Formula
            FROM Currency
            WHERE CompanyID IS NULL
              AND CurrencyCode = @mainCurrency

            INSERT INTO Currency(ID, CompanyID, BranchID, CurrencyCode, CurrencyName, ExchangeRate, IsActive,
                                 Formula)
            SELECT NEWID(),
                   @companyID,
                   BranchID,
                   CurrencyCode,
                   CurrencyName,
                   ExchangeRate,
                   IsActive,
                   Formula
            FROM Currency
            WHERE CompanyID IS NULL
              AND CurrencyCode <> @mainCurrency
            --edit 21/1
            --UPDATE Currency SET ExchangeRate = 1 WHERE CompanyID = @companyID AND CurrencyCode = @MainCurrency
            --END ADD CURRENCY

            --ADD DATA Material Goods Special Tax Group
            INSERT INTO MaterialGoodsSpecialTaxGroup(ID, CompanyID, BranchID, MaterialGoodsSpecialTaxGroupCode,
                                                     MaterialGoodsSpecialTaxGroupName, TaxRate, UnitID, OrderFixCode,
                                                     ParentID,
                                                     IsParentNode, Grade, IsActive, IsSecurity)
            SELECT NEWID(),
                   @companyID,
                   BranchID,
                   MaterialGoodsSpecialTaxGroupCode,
                   MaterialGoodsSpecialTaxGroupName,
                   TaxRate,
                   UnitID,
                   OrderFixCode,
                   NULL,
                   IsParentNode,
                   Grade,
                   IsActive,
                   IsSecurity
            FROM MaterialGoodsSpecialTaxGroup
            WHERE CompanyID IS NULL
            SELECT *
            into #tableParentMaterialGoodsSpecialTaxGroup
            from MaterialGoodsSpecialTaxGroup
            WHERE CompanyID = @companyID
              AND IsParentNode = 1;
            DECLARE @MaterialGoodsSpecialTaxGroupCode NVARCHAR(25)
            DECLARE @LikeMaterialGoodsSpecialTaxGroupCode NVARCHAR(128)
--     DECLARE @Count INT
            SET @Count = (SELECT COUNT(*) FROM #tableParentMaterialGoodsSpecialTaxGroup)
            WHILE(@Count > 0)
                BEGIN
                    SET @MaterialGoodsSpecialTaxGroupCode = (SELECT
                                                             TOP 1
                                                             MaterialGoodsSpecialTaxGroupCode
                                                             FROM #tableParentMaterialGoodsSpecialTaxGroup
                                                             ORDER BY MaterialGoodsSpecialTaxGroupCode)
                    SET @LikeMaterialGoodsSpecialTaxGroupCode = (SELECT
                                                                 TOP 1
                                                                 MaterialGoodsSpecialTaxGroupCode
                                                                 FROM #tableParentMaterialGoodsSpecialTaxGroup
                                                                 ORDER BY MaterialGoodsSpecialTaxGroupCode) + '%'
                    SET @ParentID = (SELECT
                                     TOP 1
                                     ID
                                     FROM #tableParentMaterialGoodsSpecialTaxGroup
                                     ORDER BY MaterialGoodsSpecialTaxGroupCode)
                    UPDATE MaterialGoodsSpecialTaxGroup
                    SET ParentID = @ParentID
                    WHERE CompanyID = @companyID
                      AND MaterialGoodsSpecialTaxGroupCode LIKE @LikeMaterialGoodsSpecialTaxGroupCode
                      AND MaterialGoodsSpecialTaxGroupCode <> @MaterialGoodsSpecialTaxGroupCode
                    DELETE
                    FROM #tableParentMaterialGoodsSpecialTaxGroup
                    WHERE MaterialGoodsSpecialTaxGroupCode = @MaterialGoodsSpecialTaxGroupCode
                      AND ID = @ParentID
                    SET @Count = @Count - 1
                end
            --END ADD  Material Goods Special Tax Group
        END

    drop table #newEbGroup
    drop table #oldEbGroup
end
go

