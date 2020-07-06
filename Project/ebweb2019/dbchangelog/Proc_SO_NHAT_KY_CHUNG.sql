-- Author:		Hautv
-- Create date: <18/02/2020>
-- Description:	<Kế toán tổng hợp: Sổ nhật ký chung>
-- Proc_SO_NHAT_KY_CHUNG '1/1/2020','12/31/2020',0,1,'5A814271-A115-41D1-BA4D-C50BC0040482',1
-- =============================================
CREATE PROCEDURE [dbo].[Proc_SO_NHAT_KY_CHUNG] @FromDate DATETIME,
                                               @ToDate DATETIME,
                                               @GroupTheSameItem BIT,/*cộng gộp các bút toán giống nhau*/
                                               @IsShowAccumAmount BIT, -- Hiển thị số lũy kế kỳ trước chuyển sang
                                               @CompanyID uniqueidentifier, -- Hiển thị số lũy kế kỳ trước chuyển sang
                                               @IsFinancialBook BIT, -- Hiển thị số lũy kế kỳ trước chuyển sang,
                                               @isDependent BIT
AS
BEGIN

    DECLARE
        @PrevFromDate AS DATETIME
    SET @PrevFromDate = DATEADD(MILLISECOND, -10, @FromDate)

    -- Cộng gộp các bút toán giống nhau
    IF @GroupTheSameItem = 1
        BEGIN
            -- Lấy cộng trang trước chuyển sang
            select *
            into #t1
            from (
                     -- lấy dữ liệu cộng gộp
                     SELECT ROW_NUMBER() OVER ( ORDER BY gl.PostedDate, gl.Date
                         ,CASE WHEN GL.TypeID = 4012 THEN 1 ELSE 0 END -- Chứng từ Kết chuyển lãi lỗ xuống dưới
                         ,CASE WHEN @IsFinancialBook = 1 then GL.NoFBook else GL.NoMBook end desc , GL.TypeID ) AS RowNum,
                            CAST(1 AS BIT)                                                                      AS IsSummaryRow,
                            GL.[ReferenceID]                                                                    as ReferenceID,
                            GL.[TypeID]                                                                         as TypeID,
                            GL.[PostedDate],
                            GL.[Date],
                            CASE WHEN @IsFinancialBook = 1 then GL.NoFBook else GL.NoMBook end                  as No,
                            GL.[InvoiceDate],
                            GL.[InvoiceNo],
                            GL.Reason                                                                           as Description,
                         /*bổ sung trường diễn giải thông tin chung cr 137228*/
                            GL.Reason,
                            GL.[Account]                                                                        as AccountNumber,
                            GL.[AccountCorresponding]                                                           as CorrespondingAccountNumber,

                            SUM(GL.[DebitAmount])                                                               AS DebitAmount,
                            null                                                                                AS CreditAmount,
                            CASE
                                WHEN SUM(DebitAmount) <> 0
                                    THEN Gl.Account
                                    + AccountCorresponding
                                WHEN SUM(CreditAmount) <> 0
                                    THEN gl.AccountCorresponding
                                    + GL.Account
                                END                                                                             AS Sort,
                            0                                                                                   AS SortOrder,
                            0                                                                                   AS DetailPostOrder,
                            MAX(GL.OrderPriority)                                                               as OrderPriority,
                            SUM(DebitAmount)                                                                    as OrderTotal,
                            1                                                                                   AS OderType
                     FROM GeneralLedger GL
                              LEFT JOIN dbo.AccountingObject ao ON ao.ID = GL.AccountingObjectID
                     WHERE gl.PostedDate BETWEEN @FromDate AND @ToDate
                       AND ISNULL(gl.AccountCorresponding, '') <> ''
                       AND GL.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
                       and (GL.TypeLedger = 2
                         or GL.TypeLedger = case
                                                when @IsFinancialBook = 1 then
                                                    0
                                                else
                                                    1
                             end)
                     GROUP BY GL.[ReferenceID],
                              GL.[TypeID],
                              GL.[PostedDate],
                              GL.[Date],
                              CASE WHEN @IsFinancialBook = 1 then GL.NoFBook else GL.NoMBook end,
                              GL.[InvoiceDate],
                              GL.[InvoiceNo],
                              GL.[Reason],
                              GL.[Account],
                              GL.[AccountCorresponding]

                     HAVING SUM(GL.[DebitAmount]) <> 0
                     UNION ALL
                     /*add by cuongpv*/
                     SELECT ROW_NUMBER() OVER ( ORDER BY gl.PostedDate, gl.Date
                         ,CASE WHEN GL.TypeID = 4012 THEN 1 ELSE 0 END -- Chứng từ Kết chuyển lãi lỗ xuống dưới
                         , CASE WHEN @IsFinancialBook = 1 then GL.NoFBook else GL.NoMBook end desc , GL.TypeID ) AS RowNum,
                            CAST(1 AS BIT)                                                                       AS IsSummaryRow,
                            GL.[ReferenceID]                                                                     as ReferenceID,
                            GL.[TypeID]                                                                          as TypeID,--RefType
                            GL.[PostedDate],
                            GL.[Date],
                            CASE WHEN @IsFinancialBook = 1 then GL.NoFBook else GL.NoMBook end                   as No,
                            GL.[InvoiceDate],
                            GL.[InvoiceNo],
                            GL.Reason                                                                            as Description,
                         /*bổ sung trường diễn giải thông tin chung cr 137228*/
                            GL.Reason,
                            GL.[Account]                                                                         as AccountNumber,
                            GL.[AccountCorresponding]                                                            as CorrespondingAccountNumber,
                            null                                                                                 AS DebitAmount,
                            SUM(GL.[CreditAmount])                                                               AS CreditAmount,
                            CASE
                                WHEN SUM(DebitAmount) <> 0
                                    THEN Gl.Account
                                    + AccountCorresponding
                                WHEN SUM(CreditAmount) <> 0
                                    THEN gl.AccountCorresponding
                                    + GL.Account
                                END                                                                              AS Sort,
                            1                                                                                    AS SortOrder,
                            0                                                                                    AS DetailPostOrder,
                            MAX(GL.OrderPriority)                                                                as OrderPriority,
                            SUM(CreditAmount)                                                                    as OrderTotal,
                            1                                                                                    AS OderType
                     FROM GeneralLedger GL
                              LEFT JOIN dbo.AccountingObject ao ON ao.ID = GL.AccountingObjectID
                     WHERE gl.PostedDate BETWEEN @FromDate AND @ToDate
                       AND ISNULL(gl.AccountCorresponding, '') <> ''
                       AND GL.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
                       and (GL.TypeLedger = 2
                         or GL.TypeLedger = case
                                                when @IsFinancialBook = 1 then
                                                    0
                                                else
                                                    1
                             end)
                     GROUP BY GL.[ReferenceID],
                              GL.[TypeID],
                              GL.[PostedDate],
                              GL.[Date],
                              CASE WHEN @IsFinancialBook = 1 then GL.NoFBook else GL.NoMBook end,
                              GL.[InvoiceDate],
                              GL.[InvoiceNo],
                              GL.[Reason],
                              GL.[Account],
                              GL.[AccountCorresponding]
                     HAVING SUM(GL.[CreditAmount]) <> 0
                 ) t
            order by PostedDate, No, OrderPriority
        END
    ELSE
        BEGIN
            select *
            into #t2
            from (
                     -- Lấy dữ liệu chi tiết
                     SELECT ROW_NUMBER() OVER ( ORDER BY gl.PostedDate, gl.Date
                         ,CASE WHEN GL.TypeID = 4012 THEN 1 ELSE 0 END -- Chứng từ Kết chuyển lãi lỗ xuống dưới
                         , CASE WHEN @IsFinancialBook = 1 then GL.NoFBook else GL.NoMBook end desc , GL.TypeID ) AS RowNum,
                            CAST(1 AS BIT)                                                                       AS IsSummaryRow,
                            GL.[ReferenceID],
                            GL.[TypeID],
                            GL.[PostedDate]                                                                      as PostedDate,
                            GL.[Date],
                            CASE WHEN @IsFinancialBook = 1 then GL.NoFBook else GL.NoMBook end                   as No,
                            GL.[InvoiceDate],
                            GL.[InvoiceNo],
                            GL.Description,
                         /* bổ sung trường diễn giải thông tin chung cr 137228*/
                            GL.Reason,
                            GL.[Account]                                                                         as AccountNumber,
                            GL.[AccountCorresponding]                                                            as CorrespondingAccountNumber,
                            ao.AccountingObjectCode,
                            ao.AccountingObjectName,
                            GL.[DebitAmount],
                            GL.[CreditAmount],
                            N'Chi phí hợp lý'                                                                    AS UnResonableCost,
                            CASE
                                WHEN DebitAmount <> 0
                                    THEN Gl.Account
                                    + AccountCorresponding
                                    + CAST(DebitAmount AS NVARCHAR(22))
                                WHEN CreditAmount <> 0
                                    THEN gl.AccountCorresponding
                                    + GL.Account
                                    + CAST(CreditAmount AS NVARCHAR(22))
                                END                                                                              AS Sort,
                            0                                                                                    as SortOrder,
                            0                                                                                    as DetailPostOrder,
                            GL.OrderPriority                                                                     as OrderPriority,
                            1                                                                                    AS OderType

                     FROM GeneralLedger GL
                              LEFT JOIN dbo.AccountingObject ao ON ao.ID = GL.AccountingObjectID
                     WHERE gl.PostedDate BETWEEN @FromDate AND @ToDate
                       AND ISNULL(gl.AccountCorresponding,
                                  '') <> ''
                       AND (GL.[DebitAmount] <> 0
                         OR GL.[CreditAmount] <> 0
                         )
                       AND GL.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
                       and (GL.TypeLedger = 2
                         or GL.TypeLedger = case
                                                when @IsFinancialBook = 1 then
                                                    0
                                                else
                                                    1
                             end)
                 ) t
            order by t.PostedDate, No, t.OrderPriority
        END
    IF @IsShowAccumAmount = 1
        BEGIN
            if @GroupTheSameItem = 1
                begin
                    insert into #t1 (Description,
                                     Reason,
                                     DebitAmount,
                                     CreditAmount,
                                     SortOrder,
                                     DetailPostOrder,
                                     ReferenceID,
                                     TypeID,
                                     OderType)
                    select N'Số lũy kế kỳ trước chuyển sang' AS Description,
                           N'Số lũy kế kỳ trước chuyển sang' AS Reason,
                           SUM(DebitAmount)                  AS DebitAmount,
                           SUM(CreditAmount)                 AS CreditAmount,
                           0                                 as SortOrder,
                           0                                 as DetailPostOrder,
                           newid()                           as ReferenceID,
                           0                                 as TypeID,
                           0                                 AS OderType
                    from GeneralLedger GL
                    where GL.PostedDate < @FromDate
                      AND GL.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
                      and (GL.TypeLedger = 2
                        or GL.TypeLedger = case
                                               when @IsFinancialBook = 1 then
                                                   0
                                               else
                                                   1
                            end)
                      and TypeID not in (740, 741, 742)
                end
            else
                begin
                    insert into #t2 (Description,
                                     Reason,
                                     DebitAmount,
                                     CreditAmount,
                                     SortOrder,
                                     DetailPostOrder,
                                     UnResonableCost,
                                     ReferenceID,
                                     TypeID,
                                     OrderPriority,
                                     OderType)
                    select N'Số lũy kế kỳ trước chuyển sang' AS Description,
                           N'Số lũy kế kỳ trước chuyển sang' AS Reason,
                           SUM(DebitAmount)                  AS DebitAmount,
                           SUM(CreditAmount)                 AS CreditAmount,
                           0                                 as SortOrder,
                           0                                 as DetailPostOrder,
                           ''                                as UnResonableCost,
                           newid()                           as ReferenceID,
                           0                                 as TypeID,
                           -1                                as OrderPriority,
                           0                                 AS OderType
                    from GeneralLedger GL
                    where GL.PostedDate < @FromDate
                      AND GL.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
                      and (GL.TypeLedger = 2
                        or GL.TypeLedger = case
                                               when @IsFinancialBook = 1 then
                                                   0
                                               else
                                                   1
                            end)
                      and TypeID not in (740, 741, 742)
                end
        END
    IF @IsShowAccumAmount = 1
        BEGIN
            if @GroupTheSameItem = 1
                begin
                    select * from #t1 order by OderType, PostedDate, OrderPriority
                end
            else
                begin
                    select * from #t2 order by OderType, PostedDate, No, OrderPriority, SortOrder
                end
        END
    else
        begin
            if
                @GroupTheSameItem = 1
                begin
                    select * from #t1 order by PostedDate, OrderPriority
                end
            else
                begin
                    select * from #t2 order by PostedDate, No, OrderPriority, SortOrder
                end
        end

END
go

