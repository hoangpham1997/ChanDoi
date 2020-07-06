-- Author : Hautv
-- crate : 2020-03-17
-- Backup dữ liệu theo companyID (Tất cả dữ liệu hiện tại)
-- sp_generate_insertscripts_backup @CompanyID = 'DAB6D60B-64B5-8444-B811-BE29A9DC6738'
-- sp_generate_insertscripts_backup @CompanyID = '5A814271-A115-41D1-BA4D-C50BC0040482'
CREATE PROCEDURE sp_generate_insertscripts_backup(@CompanyID uniqueidentifier)
AS
begin
    SET NOCOUNT ON

    declare
        @ResultDelete as table
                         (
                             Insert_Scripts nvarchar(MAX)
                         )
    declare
        @ResultDeleteDetail as table
                               (
                                   Insert_Scripts nvarchar(MAX)
                               )
    declare
        @Result as table
                   (
                       RowNum         BIGINT PRIMARY KEY IDENTITY (1, 1),
                       Insert_Scripts nvarchar(MAX)
                   )
    declare
        @ResultAfterOrder as table
                             (
                                 RowNum         BIGINT PRIMARY KEY IDENTITY (1, 1),
                                 Insert_Scripts nvarchar(MAX)
                             )

    DECLARE
        @TABLE_NAME  VARCHAR(250),
        @CSV_COLUMN  VARCHAR(MAX),
        @QUOTED_DATA VARCHAR(MAX),
        @TEXT        VARCHAR(MAX) = '',
        @FILTER      VARCHAR(MAX),
        @ORDER       VARCHAR(250)
    declare
        @AllMyTable as table
                       (
                           RowNum             INT PRIMARY KEY IDENTITY (1, 1),
                           NameTable          nvarchar(100),
                           NameTableReference nvarchar(MAX)
                       )
    -- Tổng hợp dữ liệu các bảng cần backup
    Insert into @AllMyTable(NameTable, NameTableReference) values ('AccountDefault', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('AccountingObject', 'AccountingObjectBankAccount');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('AccountingObjectGroup', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('AccountList', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('AccountTransfer', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('AutoPrinciple', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('B09Report', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('Bank', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('BankAccountDetail', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('BudgetItem', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('ContractState', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('CostSet', 'CostSetMaterialGoods');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('CPAllocationQuantum', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('CPExpenseTranfer', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('CPOPN', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('CPPeriod', 'CPPeriodDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('CPProductQuantum', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('CreditCard', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('Currency', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('EbGroup', 'EbGroupAuth');
    --         Insert into @AllMyTable(NameTable, NameTableReference) values ('EbOrganizationUnit', NULL);  -- backup thep parentID - đã thêm
    Insert into @AllMyTable(NameTable, NameTableReference) values ('EbUserGroup', NULL);
--         Insert into @AllMyTable(NameTable, NameTableReference) values ('EbUserPackage', NULL); -- Bảng gói người dùng
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('EMContract', 'EMContractDetail'); -- Lỗi ID đặc biệt - đã thêm ở while if()
    Insert into @AllMyTable(NameTable, NameTableReference) values ('ExceptVoucher', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('ExpenseItem', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('FAAdjustment', 'FAAdjustmentDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('FAAudit', 'FAAuditDetail,FAAuditMemberDetail');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('FADecrement', 'FADecrementDetail'); --  FADecrementDetailPost đặc biệt
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('FADepreciation', 'FADepreciationDetail,FADepreciationPost');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('FAIncrement', 'FAIncrementDetail,FAIncrementDetailRefVoucher');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('FAInit', 'FAInitDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('FATransfer', 'FATransferDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('FinancialReport', 'FinancialReportDetail');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('FixedAsset', 'FixedAssetAccessories,FixedAssetAllocation,FixedAssetDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('FixedAssetCategory', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('FixedAssetLedger', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('GenCode', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('GeneralLedger', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('GOtherVoucher',
            'GOtherVoucherDetail,GotherVoucherDetailDebtPayment,GotherVoucherDetailExcept,GotherVoucherDetailExpense,GOtherVoucherDetailExpenseAllocation,GOtherVoucherDetailForeignCurrency,GotherVoucherDetailTax');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('GvoucherList', 'GvoucherListDetail');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('IAAdjustAnnouncement', 'IAAdjustAnnouncementDetail,IAAdjustAnnouncementDetailListInvoice');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('IADeletedInvoice', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('IADestructionInvoice', 'IADestructionInvoiceDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('IALostInvoice', 'IALostInvoiceDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('IAPublishInvoice', 'IAPublishInvoiceDetail');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('IARegisterInvoice', 'IARegisterInvoiceDetail'); -- lỗi varbinary(max)
    Insert into @AllMyTable(NameTable, NameTableReference) values ('IAReport', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('MaterialGoods',
            'MaterialGoodsAssembly,MaterialGoodsConvertUnit,MaterialGoodsPurchasePrice,MaterialGoodsSpecifications,SaleDiscountPolicy');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('MaterialGoodsCategory', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('MaterialGoodsResourceTaxGroup', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('MaterialGoodsSpecialTaxGroup', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('MaterialQuantum', 'MaterialQuantumDetail');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('MBCreditCard', 'MBCreditCardDetail,MBCreditCardDetailTax,MBCreditCardDetailVendor');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('MBDeposit', 'MBDepositDetail,MBDepositDetailCustomer,MBDepositDetailTax');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('MBInternalTransfer', 'MBInternalTransferDetail');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('MBTellerPaper',
            'MBTellerPaperDetail,MBTellerPaperDetailInsurance,MBTellerPaperDetailSalary,MBTellerPaperDetailTax,MBTellerPaperDetailVendor');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('MCAudit', 'MCAuditDetail,MCAuditDetailMember');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('MCPayment',
            'MCPaymentDetail,MCPaymentDetailInsurance,MCPaymentDetailSalary,MCPaymentDetailTax,MCPaymentDetailVendor');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('MCReceipt', 'MCReceiptDetail,MCReceiptDetailCustomer,MCReceiptDetailTax');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('OPAccount', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('OPMaterialGoods', NULL);
--         Insert into @AllMyTable(NameTable, NameTableReference) values ('OrganizationUnitOptionReport', NULL);  trường hợp đặc biệt -đã thêm
    Insert into @AllMyTable(NameTable, NameTableReference) values ('PaymentClause', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('PersonalSalaryTax', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('PPDiscountReturn', 'PPDiscountReturnDetail');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('PPInvoice', 'PPInvoiceDetail,PPInvoiceDetailCost'); -- PPInvoiceDetailCost trường hợp đặc biệt - đã thêm if
    Insert into @AllMyTable(NameTable, NameTableReference) values ('PPOrder', 'PPOrderDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('PPService', 'PPServiceDetail');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('PrepaidExpense', 'PrepaidExpenseAllocation,PrepaidExpenseVoucher');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('PSSalarySheet', 'PSSalarySheetDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('PSSalaryTaxInsuranceRegulation', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('PSTimeSheet', 'PSTimeSheetDetail');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('PSTimeSheetSummary', 'PSTimeSheetSummaryDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('RefVoucher', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('Repository', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('RepositoryLedger', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('RSAssemblyDismantlement', 'RSAssemblyDismantlementDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('RSInwardOutward', 'RSInwardOutwardDetail');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('RSProductionOrder', 'RSProductionOrderDetail'); --RSProductionOrderQuantumn bảng đặc biệt
    Insert into @AllMyTable(NameTable, NameTableReference) values ('RSTransfer', 'RSTransferDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('SABill', 'SABillDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('SAInvoice', 'SAInvoiceDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('SalePriceGroup', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('SAOrder', 'SAOrderDetail');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('SAPolicyPriceSetting', 'SAPolicyPriceTable,SAPolicySalePriceGroup'); --SAPolicySalePriceCustomer
    Insert into @AllMyTable(NameTable, NameTableReference) values ('SAQuote', 'SAQuoteDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('SAReturn', 'SAReturnDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('StatisticsCode', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('SystemOption', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('TIAdjustment', 'TIAdjustmentDetail');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('TIAllocation', 'TIAllocationAllocated,TIAllocationDetail,TIAllocationPost');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('TIAudit', 'TIAuditDetail,TIAuditMemberDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('TIDecrement', 'TIDecrementDetail');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('TIIncrement', 'TIIncrementDetail,TIIncrementDetailRefVoucher');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('TimeSheetSymbols', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('TITransfer', 'TITransferDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('TM01GTGT', 'TM01GTGTAdjust,TM01GTGTDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('TM01TAIN', 'TM01TAINAdjust,TM01TAINDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('TM02GTGT', 'TM02GTGTAdjust,TM02GTGTDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('TM02TAIN', 'TM02TAINAdjust,TM02TAINDetail');
    Insert into @AllMyTable(NameTable, NameTableReference)
    values ('TM03TNDN', 'TM031ATNDN,TM032ATNDN,TM03TNDNAdjust,TM03TNDNDetail,TM03TNDNDocument');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('TM04GTGT', 'TM04GTGTAdjust,TM04GTGTDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('TMDeclaration', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('ToolLedger', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('Tools', 'ToolsDetail');
    Insert into @AllMyTable(NameTable, NameTableReference) values ('TransportMethod', NULL);
    Insert into @AllMyTable(NameTable, NameTableReference) values ('Unit', NULL);
--     Insert into @AllMyTable(NameTable, NameTableReference) values ('VoucherPatternsReport', NULL); bảng đã xóa
    Insert into @AllMyTable(NameTable, NameTableReference) values ('Warranty', NULL);


    set @FILTER = ' where companyID = ' + CHAR(39) + convert(nvarchar(100), @CompanyID) + CHAR(39)

    --     chạy tất cả các bảng
    DECLARE
        @i INT = 0;
    DECLARE
        @count INT = (SELECT COUNT(*)
                      FROM @AllMyTable)
    WHILE @i < @count
    BEGIN
        SET @i = @i + 1;
        SET @TABLE_NAME = (select NameTable from @AllMyTable where RowNum = @i);
        INSERT into @ResultDelete
        values ('DELETE FROM ' + @TABLE_NAME + @FILTER);
        if ((SELECT COUNT(*)
             FROM INFORMATION_SCHEMA.COLUMNS
             WHERE TABLE_NAME = @TABLE_NAME
               and COLUMN_NAME = 'OrderPriority') = 1
            )
            begin
                set @ORDER = ' order by OrderPriority'
            end
        else
            begin
                set @ORDER = ''
            end
        PRINT @FILTER + @ORDER + ' --- filter of Parent ' + @TABLE_NAME
        SELECT @CSV_COLUMN = STUFF -- lấy string tên các cột của bảng [],[],[]
            (
                (
                    SELECT ',[' + NAME + ']'
                    FROM sys.all_columns
                    WHERE OBJECT_ID = OBJECT_ID(@TABLE_NAME)
                      AND is_identity != 1 FOR XML PATH ('')
                ), 1, 1, ''
            )

        SELECT @QUOTED_DATA = STUFF -- Lấy string dữ liệu tương ứng với các cột
            (
                (
                    SELECT case
                               when system_type_id = 231 then ' case when ' + NAME +
                                                              ' is null then ''NULL'' else' + --xử lý nvarchar
                                                              ' char(78) + char(39) + ' + 'REPLACE(' + name + ', CHAR(39), char(39) + char(39))' + ' + char(39) end' +
                                                              '+'',''' +
                                                              '+'
                               when system_type_id = 165 then '''NULL''' + -- xử lý varbinary(max) gán NULL
                                                              '+'',''' +
                                                              '+'
                               else
                                       ' ISNULL(QUOTENAME(' + 'REPLACE(' + name + ', CHAR(39), char(39) + char(39))' + ',' + QUOTENAME('''', '''''') + '),' +
                                       '''NULL''' +
                                       ')+'',''' +
                                       '+'
                               end
                    FROM sys.all_columns
                    WHERE OBJECT_ID = OBJECT_ID(@TABLE_NAME)
                      AND is_identity != 1 FOR XML PATH ('')
                ), 1, 1, ''
            )
        DECLARE
            @subText nvarchar(max);

        SELECT @subText = 'SELECT ''INSERT INTO ' + @TABLE_NAME + '(' + @CSV_COLUMN + ')VALUES(''' + '+' +
                          SUBSTRING(@QUOTED_DATA, 1, LEN(@QUOTED_DATA) - 5) + '+' + ''')''' +
                          ' Insert_Scripts FROM ' +
                          @TABLE_NAME + @FILTER + @ORDER;
        insert into @Result EXECUTE (@subText)

--             bảng liên quan
        DECLARE
            @TableRefString nvarchar(200) = (select NameTableReference from @AllMyTable where RowNum = @i)
        if (@TableRefString is not null)
            begin
                declare
                    @tableRef as table
                                 (
                                     RowNum    INT,
                                     NameTable nvarchar(MAX)
                                 )
                insert into @tableRef
                select ROW_NUMBER() over (order by Value), f.Value
                from Func_ConvertStringIntoTable_Nvarchar(@TableRefString, ',') f
                DECLARE
                    @filterRef nvarchar(max)
                DECLARE
                    @orderRef nvarchar(250)
                DECLARE
                    @TABLE_NAME_Ref nvarchar(250)
                DECLARE
                    @iRef INT = 0
                set @iRef = 0
                DECLARE
                    @countRef INT = (SELECT COUNT(*)
                                     FROM @tableRef)
                WHILE @iRef < @countRef
                begin
                    SET @iRef = @iRef + 1;
                    SET @TABLE_NAME_Ref = (select NameTable from @tableRef where RowNum = @iRef);
                    /*set @filterRef = ' where ' + @TABLE_NAME + 'ID' + ' in ' + CHAR(39) +
                                     convert(nvarchar(100), @CompanyID) + CHAR(39);*/
                    PRINT convert(nvarchar(100), @TABLE_NAME) + ' ' + convert(nvarchar(100), @iRef) + ' ' +
                          convert(nvarchar(100), @TABLE_NAME_Ref);
                    if (@TABLE_NAME_Ref = 'EMContractDetail')
                        begin
                            set @filterRef = ' where ' + 'ContractID' + ' in ' +
                                             '(select ID from ' + @TABLE_NAME +
                                             ' where CompanyID = ' + CHAR(39) + convert(nvarchar(100), @CompanyID) +
                                             CHAR(39) + ')';
                        end
                    else
                        if (@TABLE_NAME_Ref = 'PPInvoiceDetailCost')
                            begin
                                set @filterRef = ' where ' + 'RefID' + ' in ' +
                                                 '(select ID from ' + @TABLE_NAME +
                                                 ' where CompanyID = ' + CHAR(39) + convert(nvarchar(100), @CompanyID) +
                                                 CHAR(39) + ')';
                            end
                        else
                            if (@TABLE_NAME_Ref = 'EbGroupAuth')
                                begin
                                    set @filterRef = ' where ' + 'GroupId' + ' in ' +
                                                     '(select ID from ' + @TABLE_NAME +
                                                     ' where CompanyID = ' + CHAR(39) +
                                                     convert(nvarchar(100), @CompanyID) +
                                                     CHAR(39) + ')';
                                end
                            else
                                begin
                                    set @filterRef = ' where ' + @TABLE_NAME + 'ID' + ' in ' +
                                                     '(select ID from ' + @TABLE_NAME +
                                                     ' where CompanyID = ' + CHAR(39) +
                                                     convert(nvarchar(100), @CompanyID) +
                                                     CHAR(39) + ')';
                                end
                    INSERT into @ResultDeleteDetail
                    values ('DELETE FROM ' + @TABLE_NAME_Ref + @filterRef);
                    if ((SELECT COUNT(*)
                         FROM INFORMATION_SCHEMA.COLUMNS
                         WHERE TABLE_NAME = @TABLE_NAME_Ref
                           and COLUMN_NAME = 'OrderPriority') = 1
                        )
                        begin
                            set @orderRef = ' order by OrderPriority'
                        end
                    else
                        begin
                            set @orderRef = ''
                        end
                    PRINT @filterRef + @orderRef + ' --- filter of detail ' + @TABLE_NAME_Ref
                    SELECT @CSV_COLUMN = STUFF
                        (
                            (
                                SELECT ',[' + NAME + ']'
                                FROM sys.all_columns
                                WHERE OBJECT_ID = OBJECT_ID(@TABLE_NAME_Ref)
                                  AND is_identity != 1 FOR XML PATH ('')
                            ), 1, 1, ''
                        )
                    -- 'REPLACE(' + name + ', CHAR(39), char(39) + char(39))' xử lý ký tự đặc biệt '''
                    SELECT @QUOTED_DATA = STUFF
                        (
                            (
                                SELECT case
                                           when system_type_id = 231 then ' case when ' + NAME + -- xử lý nvarchar
                                                                          ' is null then ''NULL'' else' +
                                                                          ' char(78) + char(39) + ' + 'REPLACE(' + name + ', CHAR(39), char(39) + char(39))' +
                                                                          ' + char(39) end' +
                                                                          '+'',''' +
                                                                          '+'
                                           when system_type_id = 165 then '''NULL''' + -- xử lý varbinary(max) gán NULL
                                                                          '+'',''' +
                                                                          '+'
                                           else
                                                   ' ISNULL(QUOTENAME(' + 'REPLACE(' + name + ', CHAR(39), char(39) + char(39))' + ',' + QUOTENAME('''', '''''') +
                                                   '),' +
                                                   '''NULL''' +
                                                   ')+'',''' +
                                                   '+'
                                           end
                                FROM sys.all_columns
                                WHERE OBJECT_ID = OBJECT_ID(@TABLE_NAME_Ref)
                                  AND is_identity != 1 FOR XML PATH ('')
                            ), 1, 1, ''
                        )
                    DECLARE
                        @subTextRef nvarchar(max);

                    SELECT @subTextRef =
                           'SELECT ''INSERT INTO ' + @TABLE_NAME_Ref + '(' + @CSV_COLUMN + ')VALUES(''' + '+' +
                           SUBSTRING(@QUOTED_DATA, 1, LEN(@QUOTED_DATA) - 5) + '+' + ''')''' +
                           ' Insert_Scripts FROM ' +
                           @TABLE_NAME_Ref + @filterRef + @orderRef;
                    insert into @Result EXECUTE (@subTextRef)
                end
                DELETE FROM @tableRef where 1 = 1
            end

    END
    /**
          Một số bảng đặc biệt
          **/
    -- Bảng OrganizationUnitOptionReport
    set @TABLE_NAME = 'OrganizationUnitOptionReport'
    set @FILTER = ' where organizationunitid = ' + CHAR(39) + convert(nvarchar(100), @CompanyID) + CHAR(39)
    INSERT into @ResultDelete
    values ('DELETE FROM ' + @TABLE_NAME + @FILTER);

    SELECT @CSV_COLUMN = STUFF
        (
            (
                SELECT ',[' + NAME + ']'
                FROM sys.all_columns
                WHERE OBJECT_ID = OBJECT_ID(@TABLE_NAME)
                  AND is_identity != 1 FOR XML PATH ('')
            ), 1, 1, ''
        )

    SELECT @QUOTED_DATA = STUFF
        (
            (
                SELECT case
                           when system_type_id = 231 then ' case when ' + NAME +
                                                          ' is null then ''NULL'' else' + -- xử lý nvarchar
                                                          ' char(78) + char(39) + ' + 'REPLACE(' + name + ', CHAR(39), char(39) + char(39))' + ' + char(39) end' +
                                                          '+'',''' +
                                                          '+'
                           when system_type_id = 165 then '''NULL''' + -- xử lý varbinary(max) gán NULL
                                                          '+'',''' +
                                                          '+'
                           else
                                   ' ISNULL(QUOTENAME(' + 'REPLACE(' + name + ', CHAR(39), char(39) + char(39))' + ',' + QUOTENAME('''', '''''') + '),' +
                                   '''NULL''' +
                                   ')+'',''' +
                                   '+'
                           end
                FROM sys.all_columns
                WHERE OBJECT_ID = OBJECT_ID(@TABLE_NAME)
                  AND is_identity != 1 FOR XML PATH ('')
            ), 1, 1, ''
        )
    DECLARE
        @subTextSpecial nvarchar(max);
    SELECT @subTextSpecial = 'SELECT ''INSERT INTO ' + @TABLE_NAME + '(' + @CSV_COLUMN + ')VALUES(''' + '+' +
                             SUBSTRING(@QUOTED_DATA, 1, LEN(@QUOTED_DATA) - 5) + '+' + ''')''' +
                             ' Insert_Scripts FROM ' +
                             @TABLE_NAME + @FILTER;
    insert into @Result EXECUTE (@subTextSpecial)
    -- Bảng EbOrganizationUnitGroup
    set @TABLE_NAME = 'EbOrganizationUnitGroup'
    set @FILTER = ' where OrgId = ' + CHAR(39) + convert(nvarchar(100), @CompanyID) + CHAR(39)
    INSERT into @ResultDelete
    values ('DELETE FROM ' + @TABLE_NAME + @FILTER);
    SELECT @CSV_COLUMN = STUFF
        (
            (
                SELECT ',[' + NAME + ']'
                FROM sys.all_columns
                WHERE OBJECT_ID = OBJECT_ID(@TABLE_NAME)
                  AND is_identity != 1 FOR XML PATH ('')
            ), 1, 1, ''
        )

    SELECT @QUOTED_DATA = STUFF
        (
            (
                SELECT case
                           when system_type_id = 231 then ' case when ' + NAME +
                                                          ' is null then ''NULL'' else' + -- xử lý nvarchar
                                                          ' char(78) + char(39) + ' + 'REPLACE(' + name + ', CHAR(39), char(39) + char(39))' + ' + char(39) end' +
                                                          '+'',''' +
                                                          '+'
                           when system_type_id = 165 then '''NULL''' + -- xử lý varbinary(max) gán NULL
                                                          '+'',''' +
                                                          '+'
                           else
                                   ' ISNULL(QUOTENAME(' + 'REPLACE(' + name + ', CHAR(39), char(39) + char(39))' + ',' + QUOTENAME('''', '''''') + '),' +
                                   '''NULL''' +
                                   ')+'',''' +
                                   '+'
                           end
                FROM sys.all_columns
                WHERE OBJECT_ID = OBJECT_ID(@TABLE_NAME)
                  AND is_identity != 1 FOR XML PATH ('')
            ), 1, 1, ''
        )

    SELECT @subTextSpecial = 'SELECT ''INSERT INTO ' + @TABLE_NAME + '(' + @CSV_COLUMN + ')VALUES(''' + '+' +
                             SUBSTRING(@QUOTED_DATA, 1, LEN(@QUOTED_DATA) - 5) + '+' + ''')''' +
                             ' Insert_Scripts FROM ' +
                             @TABLE_NAME + @FILTER;
    insert into @Result EXECUTE (@subTextSpecial)
    -- Bảng EbUserOrganizationUnit
    set @TABLE_NAME = 'EbUserOrganizationUnit'
    set @FILTER = ' where OrgId = ' + CHAR(39) + convert(nvarchar(100), @CompanyID) + CHAR(39)
    INSERT into @ResultDelete
    values ('DELETE FROM ' + @TABLE_NAME + @FILTER);
    SELECT @CSV_COLUMN = STUFF
        (
            (
                SELECT ',[' + NAME + ']'
                FROM sys.all_columns
                WHERE OBJECT_ID = OBJECT_ID(@TABLE_NAME)
                  AND is_identity != 1 FOR XML PATH ('')
            ), 1, 1, ''
        )

    SELECT @QUOTED_DATA = STUFF
        (
            (
                SELECT case
                           when system_type_id = 231 then ' case when ' + NAME +
                                                          ' is null then ''NULL'' else' + --xử lý nvarchar
                                                          ' char(78) + char(39) + ' + 'REPLACE(' + name + ', CHAR(39), char(39) + char(39))' + ' + char(39) end' +
                                                          '+'',''' +
                                                          '+'
                           when system_type_id = 165 then '''NULL''' + -- xử lý varbinary(max) gán NULL
                                                          '+'',''' +
                                                          '+'
                           else
                                   ' ISNULL(QUOTENAME(' + 'REPLACE(' + name + ', CHAR(39), char(39) + char(39))' + ',' + QUOTENAME('''', '''''') + '),' +
                                   '''NULL''' +
                                   ')+'',''' +
                                   '+'
                           end
                FROM sys.all_columns
                WHERE OBJECT_ID = OBJECT_ID(@TABLE_NAME)
                  AND is_identity != 1 FOR XML PATH ('')
            ), 1, 1, ''
        )

    SELECT @subTextSpecial = 'SELECT ''INSERT INTO ' + @TABLE_NAME + '(' + @CSV_COLUMN + ')VALUES(''' + '+' +
                             SUBSTRING(@QUOTED_DATA, 1, LEN(@QUOTED_DATA) - 5) + '+' + ''')''' +
                             ' Insert_Scripts FROM ' +
                             @TABLE_NAME + @FILTER;
    insert into @Result EXECUTE (@subTextSpecial)

    --     insert @ResultAfterOrder select Insert_Scripts from @Result order by RowNum
    /*SET NOCOUNT OFF
    select '-- Delete detail'
    union all
    select *
    from @ResultDeleteDetail
    union all
    select '-- Delete parent'
    union all
    select *
    from @ResultDelete
    union all
    select '-- Insert data'
    union all
    select Insert_Scripts
    from @Result*/
    insert into @ResultAfterOrder(Insert_Scripts) values ('-- Delete detail')
    insert into @ResultAfterOrder(Insert_Scripts) select Insert_Scripts from @ResultDeleteDetail
    insert into @ResultAfterOrder(Insert_Scripts) values ('-- Delete parent')
    insert into @ResultAfterOrder(Insert_Scripts) select Insert_Scripts from @ResultDelete
    insert into @ResultAfterOrder(Insert_Scripts) values ('-- Insert data')
    insert into @ResultAfterOrder(Insert_Scripts) select Insert_Scripts from @Result order by RowNum
    SET NOCOUNT OFF
    select Insert_Scripts
    from @ResultAfterOrder
    order by RowNum
end
go

