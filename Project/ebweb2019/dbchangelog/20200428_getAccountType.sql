CREATE proc [dbo].[GetAccountType] @TypeID int,
    @columnName nvarchar(100),
    @companyID UNIQUEIDENTIFIER,
    @ppType bit,
    @isDependent BIT = 0
    as
    begin
        declare @check nvarchar(50), @str nvarchar(250), @count int, @AccountingType int
        SET @AccountingType = (Select top 1 AccountingType From EbOrganizationUnit Where ID in (select id from Func_getCompany(@CompanyID, @isDependent)))
        set @check = (select top 1 Data from SystemOption where CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent)) and Code = 'TCKHAC_HanCheTK')
        set @count = (select count(FilterAccount)
                      from AccountDefault
                      where TypeID = @typeID
                        and ColumnName = @columnName
                        and CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                        and AccountingType = @AccountingType
                        and FilterAccount is not null
                        and FilterAccount != ''
                        and PPType = @ppType)

        if (@count > 0)
            begin
                set @str = (select TOP 1 FilterAccount
                            from AccountDefault
                            where TypeID = @typeID
                              and ColumnName = @columnName
                              and PPType = @ppType
                              and CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent)))
            end
        if (@check = '1' and @count > 0)
            begin
                Declare @tbAccount TABLE
                                   (
                                       account     nvarchar(10),
                                       accountLike nvarchar(20)
                                   )

                insert into @tbAccount(account)
                select *
                FROM [dbo].[SplitStringToTable](@str, ';')

                Update @tbAccount set accountLike = account + '%' where account = account
                select DISTINCT AccountList.*
                from @tbAccount
                         left join AccountList on AccountNumber like accountLike
                where CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                  and IsParentNode = 0
                  and IsActive = 1
                order by AccountNumber asc;
            end
        else
            begin
                select *
                from AccountList
                where CompanyID in (select id from Func_getCompany(@CompanyID, @isDependent))
                  and AccountingType = @AccountingType
                  and IsParentNode = 0
                  and IsActive = 1
                order by AccountNumber asc;
            end
    end;
go

