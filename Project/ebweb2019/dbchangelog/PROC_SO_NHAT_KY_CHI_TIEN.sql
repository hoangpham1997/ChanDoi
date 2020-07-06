-- Author: PhuongHV'
-- Create date: 28/02/2020
-- Update by namnh: 19/05/2020
-- Description:
-- =============================================
ALTER PROCEDURE [dbo].[PROC_SO_NHAT_KY_CHI_TIEN]
    @FromDate DATETIME ,
    @ToDate DATETIME ,
    @CurrencyID NVARCHAR(10) ,
    @AccountNumber NVARCHAR(25) ,
    @BankAccountID UNIQUEIDENTIFIER ,
    @IsSimilarSum BIT, --Cộng gộp theo chứng từ
    @GetAmountOriginal BIT, -- lấy nguyên tệ hay quy đổi
    @CompanyID uniqueidentifier,
    @IsFinancialBook BIT,
    @isDependent BIT = 0
AS
    BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
        SET NOCOUNT ON;

		CREATE TABLE #Result
            (
              BranchName NVARCHAR(512)  COLLATE SQL_Latin1_General_CP1_CI_AS,
              RefType INT , --RefType
              RefID UNIQUEIDENTIFIER , --RefID
              PostedDate DATETIME , --PostedDate
              No NVARCHAR(22) COLLATE SQL_Latin1_General_CP1_CI_AS, --No
              Date DATETIME ,--Date
              [Description] NVARCHAR(512) COLLATE SQL_Latin1_General_CP1_CI_AS, --Description
              AccountNumber NVARCHAR(20) COLLATE SQL_Latin1_General_CP1_CI_AS,
              CorrespondingAccountNumber NVARCHAR(20) COLLATE SQL_Latin1_General_CP1_CI_AS, --CorrespondingAccountNumber
              Amount DECIMAL(19,4) , --Amount
              Col2 DECIMAL(19,4) DEFAULT 0 ,
              Col3 DECIMAL(19,4) DEFAULT 0 ,
              Col4 DECIMAL(19,4) DEFAULT 0 ,
              Col5 DECIMAL(19,4) DEFAULT 0 ,
              Col6 DECIMAL(19,4) DEFAULT 0 ,
              ColOtherAccount NVARCHAR(20) COLLATE SQL_Latin1_General_CP1_CI_AS,
              AccountNumberList NVARCHAR(120) COLLATE SQL_Latin1_General_CP1_CI_AS,
              /*Nhật ký thu tiền, chi tiền: Sắp xếp không đúng theo thứ tự nhập trên chứng từ */
              SortOrder INT,
              DetailPostOrder int
            )

         /*tài khoản ngân hàng khác - lấy các phát sinh không chọn tài khoản ngân hàng*/
        DECLARE @BankAccountOther UNIQUEIDENTIFIER
        SET @BankAccountOther = '12345678-2222-48B8-AE4B-5CF7FA7FB3F5'
		IF(@CurrencyID = 'VND')
		BEGIN
		    INSERT  #Result
                ( BranchName ,
                  RefType ,
                  RefID ,
                  PostedDate ,
                  No ,
                  Date ,
                  Description ,
                  AccountNumber ,
                  CorrespondingAccountNumber ,
                  Amount,
                    SortOrder,
                  DetailPostOrder
                )
                SELECT  null as BranchName ,
                        GL.TypeID ,
                        GL.ReferenceID ,
                        GL.PostedDate ,
                        CASE WHEN @IsFinancialBook = 1 then GL.NoFBook else GL.NoMBook end as No,
                        GL.RefDateTime ,
                        CASE WHEN @IsSimilarSum = 1 THEN GL.Reason
                             ELSE ISNULL(GL.[Description], GL.Reason)
                        END ,
                        @AccountNumber AS AccountNumber ,
                        GL.AccountCorresponding ,
                        GL.CreditAmount AS Amount,
                          /*-Nhật ký thu tiền, chi tiền: Sắp xếp không đúng theo thứ tự nhập trên chứng từ */
                        GL.OrderPriority,
                        null--GL.DetailPostOrder
                FROM     GeneralLedger AS GL /*edit by cuongpv dbo.GeneralLedger -> @tbDataGL*/
                WHERE   PostedDate BETWEEN @FromDate AND @ToDate
                        AND GL.Account LIKE @AccountNumber + '%'
                        AND GL.CreditAmount <> 0
                        and GL.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
                        AND ( @BankAccountID IS NULL
                              OR ( (GL.BankAccountDetailID = @BankAccountID
                                   AND @BankAccountID <> @BankAccountOther )
                                 )

                              OR ( (GL.BankAccountDetailID IS  NULL
                                   AND @BankAccountID = @BankAccountOther )
                                 )
                            )
		END
        ELSE
		BEGIN
        INSERT  #Result
                ( BranchName ,
                  RefType ,
                  RefID ,
                  PostedDate ,
                  No ,
                  Date ,
                  Description ,
                  AccountNumber ,
                  CorrespondingAccountNumber ,
                  Amount,
                    SortOrder,
                  DetailPostOrder
                )
                SELECT  null as BranchName ,
                        GL.TypeID ,
                        GL.ReferenceID ,
                        GL.PostedDate ,
                        CASE WHEN @IsFinancialBook = 1 then GL.NoFBook else GL.NoMBook end as No ,
                        GL.Date ,
                        CASE WHEN @IsSimilarSum = 1 THEN GL.Reason
                             ELSE ISNULL(GL.[Description], GL.Reason)
                        END ,
                        @AccountNumber AS AccountNumber ,
                        GL.AccountCorresponding ,
                        (CASE WHEN @GetAmountOriginal = 1 THEN GL.CreditAmount ELSE GL.CreditAmountOriginal END) AS Amount,
                          /*-Nhật ký thu tiền, chi tiền: Sắp xếp không đúng theo thứ tự nhập trên chứng từ */
                        GL.OrderPriority,
                        null--GL.DetailPostOrder
                FROM    GeneralLedger AS GL /*edit by cuongpv dbo.GeneralLedger -> @tbDataGL*/
                WHERE   PostedDate BETWEEN @FromDate AND @ToDate
                        AND GL.Account LIKE @AccountNumber + '%'
                        AND (CASE WHEN @GetAmountOriginal = 1 THEN GL.CreditAmount ELSE GL.CreditAmountOriginal END) <> 0
                        AND GL.CompanyID in (select id from Func_getCompany (@CompanyID, @isDependent))
                        AND ( @CurrencyID IS NULL
                              OR GL.CurrencyID = @CurrencyID
                            )
                        AND ( @BankAccountID IS NULL
                              OR ( (GL.BankAccountDetailID = @BankAccountID
                                   AND @BankAccountID <> @BankAccountOther )
                                 )

                              OR ( (GL.BankAccountDetailID IS  NULL
                                   AND @BankAccountID = @BankAccountOther )
                                 )
                            )
							END
	----================================ thêm các cột tài khoản ================================
        IF EXISTS ( SELECT TOP 1
                            *
                    FROM    #Result )
            BEGIN
                DECLARE @AccountNumberAdded NVARCHAR(50)
                DECLARE @AccountNumberList NVARCHAR(MAX) -- danh sách tài khoản ghi nợ và 4 tài khoản hiển thị trên tiêu đề cột
                DECLARE @Updatesql1 NVARCHAR(MAX)
                DECLARE @Updatesql2 NVARCHAR(MAX)
                DECLARE @tblName NVARCHAR(20)
                DECLARE @colNumber INT

                DECLARE @CoresAccountNumberList NVARCHAR(MAX) -- Danh sách 4 cột tài khoản đầu tiên

                SET @tblName = '#Result'
                SET @Updatesql1 = ''
                SET @Updatesql2 = ''
                SET @colNumber = 2
                SET @AccountNumberList = @AccountNumber + ','
                SET @CoresAccountNumberList = ''

                DECLARE curAcc CURSOR FAST_FORWARD READ_ONLY
                FOR
                    SELECT DISTINCT TOP 4
                            R.CorrespondingAccountNumber
                    FROM    #Result AS R
                    WHERE   ISNULL(R.CorrespondingAccountNumber, '') <> ''
                    ORDER BY R.CorrespondingAccountNumber
                OPEN curAcc

                FETCH NEXT FROM curAcc INTO @AccountNumberAdded

                WHILE @@FETCH_STATUS = 0
                    BEGIN
                                -- Cập nhật các cột col2 - col 5
                        IF @Updatesql1 <> ''
                            SET @Updatesql1 = @Updatesql1 + ', '
                        SET @Updatesql1 = @Updatesql1 + '[col'
                            + CONVERT(NVARCHAR(10), @colNumber) + ']'
                            + ' = (CASE WHEN ISNULL(CorrespondingAccountNumber,'''') = '''
                            + @AccountNumberAdded
                            + ''' THEN Amount Else 0 END)'
                -- Cập nhật cột col6
                        IF @Updatesql2 <> ''
                            SET @Updatesql2 = @Updatesql2 + ' AND '
                        SET @Updatesql2 = @Updatesql2
                            + 'ISNULL(CorrespondingAccountNumber,'''') <> '''
                            + @AccountNumberAdded + ''' '
                -- Lấy tài khoản để đưa lên tiêu đề cột
                        SET @AccountNumberList = @AccountNumberList
                            + @AccountNumberAdded + ','


                        SET @CoresAccountNumberList = @CoresAccountNumberList
                            + ',''' + @AccountNumberAdded + ''''
                -- Tăng biến đếm cột
                        SET @colNumber = @colNumber + 1
                        FETCH NEXT FROM curAcc INTO @AccountNumberAdded

                    END
                CLOSE curAcc
                DEALLOCATE curAcc

	           ------------- Kết thúc Add thêm cột -------------------

                SET @Updatesql1 = 'UPDATE #Result SET ' + @Updatesql1
                    + -- Update col6
                    ', [col6] = (CASE WHEN ' + @Updatesql2
                    + ' THEN Amount ELSE 0 END)'
                    + -- Update cột tài khoản khác
                    ', [ColOtherAccount] = (CASE WHEN ' + @Updatesql2
                    + ' THEN CorrespondingAccountNumber END)'


                EXEC (@Updatesql1)
                -- Update Danh sách các tài khoản để thực hiện gán tên cột TK trên mẫu
                UPDATE  #Result
                SET     AccountNumberList = @AccountNumberList
            END

        -- Nếu cộng gộp theo chứng từ
        IF @IsSimilarSum = 1
            BEGIN
                SELECT  BranchName ,
                        RefType , --RefType
                        RefID , --RefID
                        PostedDate , --PostedDate
                        No , --No
                        Date ,--Date
                        [Description] , --Description
                        AccountNumber ,
                        SUM(Amount) AS Amount , --Amount
                        SUM(Col2) AS Col2 ,
                        SUM(Col3) AS Col3 ,
                        SUM(Col4) AS Col4 ,
                        SUM(Col5) AS Col5 ,
                        SUM(Col6) AS Col6 ,
                        /*ISNULL(ColOtherAccount,
                               Temp1.CorrespondingAccountNumber) AS ColOtherAccount , comment by cuongpv*/
						ColOtherAccount, /*add by cuongpv*/
                        AccountNumberList
                FROM    #Result AS R
                        /*OUTER APPLY ( SELECT TOP 1
                                                CorrespondingAccountNumber
                                      FROM      #Result AS R1
                                      WHERE     R1.RefID = R.RefID
                                                AND ColOtherAccount NOT IN (
                                                STUFF(@CoresAccountNumberList,
                                                      1, 1, '') )
                                      ORDER BY  CorrespondingAccountNumber
                                    ) AS Temp1 comment by cuongpv*/
                GROUP BY BranchName ,
                        RefType , --RefType
                        RefID , --RefID
                        PostedDate , --PostedDate
                        No , --No
                        Date ,--Date
                        [Description] , --Description
                        AccountNumber ,
                        /*ISNULL(ColOtherAccount,
                               Temp1.CorrespondingAccountNumber) , comment by cuongpv*/
						ColOtherAccount, /*add by cuongpv*/
						SortOrder,
                        AccountNumberList
                  /*-Nhật ký thu tiền, chi tiền: Sắp xếp không đúng theo thứ tự nhập trên chứng từ */
                ORDER BY
						R.PostedDate ,
						R.SortOrder,--namnh
                        R.Date ,
                        R.No
            END
        ELSE -- Không cộng gộp theo chứng từ
            BEGIN

                SELECT  *
                FROM    #Result AS R
                ORDER BY R.PostedDate ,
                        R.Date ,
                        R.No ,
                          /*-Nhật ký thu tiền, chi tiền: Sắp xếp không đúng theo thứ tự nhập trên chứng từ */
                        R.SortOrder,
                        R.DetailPostOrder
            END

        DROP TABLE #Result
    END
go

