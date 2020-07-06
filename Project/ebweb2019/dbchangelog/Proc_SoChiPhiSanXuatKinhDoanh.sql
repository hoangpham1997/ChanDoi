-- =============================================
-- Author:		namnh
-- Create date: 04/06/2020
-- Description:	Sổ chi phí sản xuất kinh doanh
-- PROC_SO_CHI_PHI_SAN_XUAT_KINH_DOANH 'CAA09998-422C-C742-BF50-F26B2548BAEE','623','06/01/2020','06/17/2020',0,',6231,6232,6233,6234,6237,6238,',',a90069f0-9ab1-8f40-a3b3-d9afd86f198c,7c98897a-8578-8e49-81ff-bf1706158603,b3f6796f-97e9-b744-b56c-323da287537c,e1c076ba-f090-c74c-9c83-ae1bbb9b0b4e,5bd7cbcd-589a-7f48-9f76-fef7d9f3552f,f8044ebc-c437-e549-bdf6-4cd641de8ba4,8cfcc3ec-116d-4344-9268-b15493a1d556,f0d70325-6e24-ba4b-b40a-a908607f313f,77b99361-d930-e447-a357-8ee6bf0de6f3,b62668b6-9d9c-894e-b7fe-9a872d341644,'
-- =============================================
    ALTER PROCEDURE [dbo].[PROC_SO_CHI_PHI_SAN_XUAT_KINH_DOANH](@CompanyID UNIQUEIDENTIFIER,
    @Account NVARCHAR(25),
    @FromDate DATETIME,
    @ToDate DATETIME,
    @TypeLedger INT,
    @ListAccountNumber NVARCHAR(MAX),
    @ListCostSetID NVARCHAR(MAX)
    --                                                       @IsDependent BIT,
--     @typeShowCurrency BIT
    )
    -- TypeLedger = 0 - Sổ tài chính
    -- TypeLedger = 1 - Sổ quản trị
    AS
    BEGIN
        -- bảng cố định lấy dữ liệu
        CREATE TABLE #LeftTable
        (
            RefID                UNIQUEIDENTIFIER,
            PostedDate           DATE,
            No                   NVARCHAR(25),
            Date                 DATE,
            Description          NVARCHAR(512),
            AccountCorresponding NVARCHAR(25),
            DebitAccount         NVARCHAR(25),
            CostSetID            UNIQUEIDENTIFIER,
            CostSetCode          NVARCHAR(25),
            CostSetName          NVARCHAR(512),
            Row                  INTEGER
        )

        -- bảng lấy tài khoản nợ  để xoay và số tiền
        CREATE TABLE #RightTable
        (
            RefID                UNIQUEIDENTIFIER,
            AccountCorresponding NVARCHAR(25),
            DebitAccount         NVARCHAR(25),
            CostSetID            UNIQUEIDENTIFIER,
            Amount               DECIMAL(25, 4)
        )


        DECLARE @tblListAccountNumber TABLE
                                      (
                                          AccountNumber NVARCHAR(25)
                                      )

        INSERT INTO @tblListAccountNumber
        SELECT TG.AccountNumber
        FROM AccountList AS TG
                 INNER JOIN Func_ConvertStringIntoTable_Nvarchar(@ListAccountNumber, ',') AS Account
                            ON TG.AccountNumber = Account.Value
        WHERE Account.Value IS NOT NULL
          AND CompanyID = (SELECT TOP (1) (CASE
                                               WHEN ParentID IS NOT NULL THEN ParentID
                                               ELSE ID END)
                           FROM EbOrganizationUnit
                           WHERE ID = @CompanyID)


        DECLARE @CostSet TABLE
                         (
                             ID UNIQUEIDENTIFIER
                         )

        INSERT INTO @CostSet
        SELECT TG.ID
        FROM CostSet AS TG
                 INNER JOIN Func_ConvertStringIntoTable_Nvarchar(@ListCostSetID, ',') AS CostSet
                            ON TG.ID = CostSet.Value
        WHERE CostSet.Value IS NOT NULL

        SELECT * into #GLDauKy FROM GeneralLedger WHERE PostedDate < @FromDate AND CompanyID = @CompanyID
        SELECT *
        into #GLTrongKy
        FROM GeneralLedger
        WHERE PostedDate >= @FromDate
          AND PostedDate <= @ToDate
          AND CompanyID = @CompanyID
          AND Account IN (SELECT AccountNumber FROM @tblListAccountNumber)


        INSERT INTO #LeftTable
        SELECT a.ID,
               a.PostedDate,
               CASE WHEN @TypeLedger = 0 THEN a.NoFBook ELSE a.NoMBook END as No,
               a.Date,
               a.Description,
               a.AccountCorresponding,
               a.Account,
               a.CostSetID,
               b.CostSetCode,
               b.CostSetName,
               3
        FROM GeneralLedger a
                 LEFT JOIN CostSet b ON a.CostSetID = b.ID
        WHERE a.CompanyID = @CompanyID
          AND a.Account IN (SELECT AccountNumber FROM @tblListAccountNumber)
          AND a.CostSetID IN (SELECT ID FROM @CostSet)
          AND (DebitAmount > 0 OR DebitAmount < 0)
          AND PostedDate >= @FromDate
          AND PostedDate <= @ToDate

        INSERT INTO #LeftTable
        SELECT c.ID,
               b.PostedDate,
               CASE WHEN @TypeLedger = 0 THEN b.NoFBook ELSE b.NoMBook END as No,
               b.Date,
               b.Description,
               b.AccountCorresponding,
               b.Account,
               c.CostSetID,
               d.CostSetCode,
               d.CostSetName,
               3
        FROM GeneralLedger b
                 LEFT JOIN CPAllocationGeneralExpense a ON a.RefDetailID = b.DetailID
                 LEFT JOIN CPAllocationGeneralExpenseDetail c ON a.ID = c.CPAllocationGeneralExpenseID
                 LEFT JOIN CostSet d ON b.CostSetID = d.ID
        WHERE b.CompanyID = @CompanyID
          AND c.AccountNumber IN (SELECT AccountNumber FROM @tblListAccountNumber)
          AND b.Account IN (SELECT AccountNumber FROM @tblListAccountNumber)
--           AND b.CostSetID IN (SELECT ID FROM @CostSet)
          AND c.CostSetID IN (SELECT ID FROM @CostSet)
          AND (c.AllocatedAmount > 0 OR c.AllocatedAmount < 0)
          AND b.PostedDate >= @FromDate
          AND b.PostedDate <= @ToDate


        INSERT INTO #RightTable
        SELECT ID,
               AccountCorresponding,
               Account,
               CostSetID,
               DebitAmount
        FROM GeneralLedger
        WHERE CompanyID = @CompanyID
          AND Account IN (SELECT AccountNumber FROM @tblListAccountNumber)
          AND CostSetID IN (SELECT ID FROM @CostSet)
          AND (DebitAmount > 0 OR DebitAmount < 0)
          AND PostedDate >= @FromDate
          AND PostedDate <= @ToDate


        INSERT INTO #RightTable
        SELECT c.ID,
               b.AccountCorresponding,
               b.Account,
               c.CostSetID,
               c.AllocatedAmount
           FROM GeneralLedger b
                 LEFT JOIN CPAllocationGeneralExpense a ON a.RefDetailID = b.DetailID
                 LEFT JOIN CPAllocationGeneralExpenseDetail c ON a.ID = c.CPAllocationGeneralExpenseID
                 LEFT JOIN CostSet d ON b.CostSetID = d.ID
        WHERE b.CompanyID = @CompanyID
          AND c.AccountNumber IN (SELECT AccountNumber FROM @tblListAccountNumber)
          AND b.Account IN (SELECT AccountNumber FROM @tblListAccountNumber)
--           AND b.CostSetID IN (SELECT ID FROM @CostSet)
          AND c.CostSetID IN (SELECT ID FROM @CostSet)
          AND (c.AllocatedAmount > 0 OR c.AllocatedAmount < 0)
          AND b.PostedDate >= @FromDate
          AND b.PostedDate <= @ToDate


        DECLARE @sql NVARCHAR(MAX) = ''
        DECLARE @sqlresult NVARCHAR(MAX) = ''
        DECLARE @COUNT INT = (SELECT COUNT(*) FROM @tblListAccountNumber)
        DECLARE @TotalAccountCorresponding DECIMAL(25, 4) = (SELECT SUM(CreditAmount) FROM #GLTrongKy)
        DECLARE @AccountNumber NVARCHAR(25)
        DECLARE @CostSetID UNIQUEIDENTIFIER
        DECLARE @selectHeaderAccount NVARCHAR(MAX)
        DECLARE @strAccountNumber NVARCHAR(MAX)
        SET @selectHeaderAccount = ''
        DECLARE @selectSumRow4 NVARCHAR(MAX) -- dòng cộng số phát sinh trong kỳ
        DECLARE @selectSumRow5 NVARCHAR(MAX) -- dòng ghi có tài khoản
        DECLARE @selectSumRow1 NVARCHAR(MAX) -- dòng số dư đầu kỳ
        DECLARE @selectSumRow6 NVARCHAR(MAX)-- dòng số dư cuối kỳ
        SET @selectSumRow4 = ''
        SET @selectSumRow5 = ''
        SET @selectSumRow1 = ''
        SET @selectSumRow6 = ''
        SET @strAccountNumber = ''
        WHILE(@COUNT > 0)
            BEGIN
                SET @AccountNumber = (SELECT TOP 1 AccountNumber FROM @tblListAccountNumber ORDER BY AccountNumber)
                IF (@COUNT > 1)
                    BEGIN
                        SET @sql = @sql + '[' + @AccountNumber + '],'
                        SET @sqlresult = @sqlresult + 'SUM([' + @AccountNumber + ']) as [' + @AccountNumber + '],'
                        SET @selectHeaderAccount += @selectHeaderAccount + @AccountNumber + ','
                        SET @selectSumRow4 += 'SUM(' + '[' + @AccountNumber + ']' + ') as ' + '[' + @AccountNumber +
                                              ']' +
                                              ', '
                        SET @selectSumRow5 += ' (SELECT SUM(CreditAmount) FROM #GLTrongKy WHERE CompanyID = ' + '''' +
                                              CAST(@CompanyID as NVARCHAR(50)) + '''' + ' AND Account = ' + '''' +
                                              @AccountNumber + '''' + ' AND CostSetID = @CostSetID ),'
                        SET @selectSumRow1 +=
                                ' (SELECT (SUM(DebitAmount) - SUM(CreditAmount)) FROM #GLDauKy WHERE CompanyID = ' +
                                '''' +
                                CAST(@CompanyID as NVARCHAR(50)) + '''' + ' AND Account = ' + '''' +
                                @AccountNumber + '''' + '  AND CostSetID = @CostSetID ),'
                        SET @selectSumRow6 += ' (ISNULL(( SELECT TOP 1 ' + '([' + @AccountNumber + ']) as ' + '[' +
                                              @AccountNumber +
                                              ']' +
                                              ' FROM #tg1 WHERE Row = 1 AND CostSetID = @CostSetID), 0) + ' +
                                              'ISNULL(( SELECT TOP 1 ' + '[' + @AccountNumber + ']' +
                                              ' FROM #tg1 WHERE Row = 4 AND CostSetID = @CostSetID),0) - ' +
                                              'ISNULL(( SELECT TOP 1 ' + '[' + @AccountNumber + ']' +
                                              ' FROM #tg1 WHERE Row = 5 AND CostSetID = @CostSetID),0)), '
                        SET @strAccountNumber += '''' + @AccountNumber + '''' + ', '
                    end
                ELSE
                    BEGIN
                        SET @sql = @sql + '[' + @AccountNumber + ']'
                        SET @sqlresult = @sqlresult + 'SUM([' + @AccountNumber + ']) as [' + @AccountNumber + ']'
                        SET @selectHeaderAccount += @selectHeaderAccount + @AccountNumber
                        SET @selectSumRow4 += 'SUM(' + '[' + @AccountNumber + ']' + ') as ' + '[' + @AccountNumber + ']'
                        SET @selectSumRow5 += ' (SELECT SUM(CreditAmount) FROM #GLTrongKy WHERE CompanyID = ' + '''' +
                                              CAST(@CompanyID as NVARCHAR(50)) + '''' + ' AND Account = ' + '''' +
                                              @AccountNumber + '''' + '  AND CostSetID = @CostSetID )'
                        SET @selectSumRow1 +=
                                ' (SELECT (SUM(DebitAmount) - Sum(CreditAmount)) FROM #GLDauKy WHERE CompanyID = ' +
                                '''' +
                                CAST(@CompanyID as NVARCHAR(50)) + '''' + ' AND Account = ' + '''' +
                                @AccountNumber + '''' + '  AND CostSetID = @CostSetID )'
                        SET @selectSumRow6 += ' (ISNULL(( SELECT TOP 1 ' + '([' + @AccountNumber + ']) as ' + '[' +
                                              @AccountNumber +
                                              ']' +
                                              ' FROM #tg1 WHERE Row = 1 AND CostSetID = @CostSetID),0) + ' +
                                              'ISNULL(( SELECT TOP 1 ' + '[' + @AccountNumber + ']' +
                                              ' FROM #tg1 WHERE Row = 4 AND CostSetID = @CostSetID),0) - ' +
                                              'ISNULL(( SELECT TOP 1 ' + '[' + @AccountNumber + ']' +
                                              ' FROM #tg1 WHERE Row = 5 AND CostSetID = @CostSetID),0)) '
                        SET @strAccountNumber += '''' + @AccountNumber + ''''
                    END
                DELETE FROM @tblListAccountNumber WHERE AccountNumber = @AccountNumber
                SET @COUNT = @COUNT - 1
            END

        DECLARE @strCostSetID NVARCHAR(MAX)
        SELECT DISTINCT(ID) as ID into #tblListCostSet FROM @CostSet
        SET @COUNT = (SELECT COUNT(*) FROM #tblListCostSet)
        SET @strCostSetID = ''

        WHILE (@COUNT > 0)
            BEGIN
                SET @CostSetID = (SELECT TOP 1 ID FROM #tblListCostSet)
                IF (@COUNT > 1)
                    BEGIN
                        SET @strCostSetID += '''' + CAST(@CostSetID as NVARCHAR(50)) + '''' + ', '
                    END
                ELSE
                    BEGIN
                        SET @strCostSetID += '''' + CAST(@CostSetID as NVARCHAR(50)) + ''''

                    end
                DELETE FROM #tblListCostSet WHERE ID = @CostSetID
                SET @COUNT = @COUNT - 1
            END
        print @strAccountNumber
        print @strCostSetID
        --

        DECLARE @selectSql NVARCHAR(MAX) =
                'SELECT a.RefID, a.PostedDate, a.No, a.Date, a.Description, a.AccountCorresponding, a.DebitAccount, a.CostSetID, a.CostSetCode, a.CostSetName, ' +
                @sql + ', a.Row into #tg1 FROM '

        DECLARE @result NVARCHAR(MAX) = @selectSql + ' #LeftTable a
                 LEFT JOIN
             (SELECT *
              FROM (SELECT RefID, CostSetID, DebitAccount, Amount
                    FROM #RightTable) AS BangNguon
                       PIVOT
                       (
                       SUM(Amount)
                       FOR DebitAccount IN (' + @sql + ')
                       ) AS BangChuyen) #RS ON a.RefID = #RS.RefID Order by a.CostSetID, a.AccountCorresponding, a.postedDate, a.Date '
--         SET @result = '';
        SET @result += ' DECLARE @COUNT INT '
        SET @result += ' DECLARE @CostSetID UNIQUEIDENTIFIER '
        SET @result +=
                ' SELECT DISTINCT(CostSetID) into #costSetTemp FROM GeneralLedger WHERE CostSetID IN (' +
                @strCostSetID +
                ' ) AND Account IN (' + @strAccountNumber + ')' +
                ' AND (DebitAmount > 0 OR CreditAmount > 0) AND PostedDate <= ''' +
                CONVERT(nvarchar(10), @ToDate, 101) +
                ''' AND CostSetID IS NOT NULL AND CompanyID = ' + '''' +
                CAST(@CompanyID as NVARCHAR(50)) + ''''
        SET @result += ' SET @COUNT = (SELECT COUNT(*) from #costSetTemp) '
        SET @result += ' WHILE (@COUNT >0) '
        SET @result += ' BEGIN '
        SET @result += ' SET @CostSetID = (SELECT TOP 1 CostSetID FROM #costSetTemp) '
        SET @result += N' INSERT INTO #tg1 (CostSetID, Description, Row) VALUES (@CostSetID, N''Số phát sinh trong kỳ'',2) '
        SET @result +=
                N' INSERT INTO #tg1 SELECT null, null, null, null, N''Cộng số phát sinh trong kỳ'', null, null, @CostSetID, null, null, ' +
                @selectSumRow4 + ',4 FROM #tg1 WHERE CostSetID = @CostSetID '
        SET @result +=
                N' INSERT INTO #tg1 SELECT null, null, null, null, N''Số dư đầu kỳ'', null, null, @CostSetID, null, null, ' +
                @selectSumRow1 + ',1 '
        SET @result +=
                N' INSERT INTO #tg1 SELECT null, null, null, null, N''Ghi có TK: ' + @Account +
                ' '' , null, null, @CostSetID, null, null, ' +
                @selectSumRow5 + ',5 '
        SET @result +=
                N' INSERT INTO #tg1 SELECT null, null, null, null, N''Số dư cuối kỳ ' +
                ' '' , null, null, @CostSetID, null, null, ' +
                @selectSumRow6 + ',6 '


        SET @result += ' DELETE FROM #costSetTemp WHERE CostSetID = @CostSetID '
        SET @result += ' SET @COUNT = @COUNT - 1 '
        SET @result += ' END '
        --         SET @result += ' SET @COUNT = (SELECT COUNT(*) FROM (SELECT DISTINCT CostSetID FROM #tg1))) '
--         SET @result += N' INSERT INTO #tg1 (Description, Row) VALUES (N''Số dư đầu kỳ '',2)'
        SET @result +=
                'SELECT  null as RefID, a.PostedDate, a.No, a.Date, a.Description, a.AccountCorresponding, a.DebitAccount, a.CostSetID, b.CostSetCode, b.CostSetName, ' +
                @sqlresult + ', a.Row ' +
                'from #tg1 a LEFT JOIN CostSet b ON a.CostSetID = b.ID group by  a.PostedDate, a.No, a.Date, a.Description, a.AccountCorresponding, a.DebitAccount, a.CostSetID, b.CostSetCode, b.CostSetName, a.Row Order by CostSetID, Row '

        exec (@result)
        drop table #LeftTable
        drop table #RightTable
    END
go

