IF OBJECT_ID ( 'Func_getCompany' ) IS NOT NULL
    DROP FUNCTION Func_getCompany;
    -------------------------------------
CREATE FUNCTION [dbo].[Func_getCompany] (@CompanyID uniqueidentifier,
    @isDependent BIT)
RETURNS @Result TABLE
                    (
                        ID             UNIQUEIDENTIFIER)
    AS
    BEGIN
        if @isDependent = 1
        begin
            insert into @Result
            SELECT ID FROM EbOrganizationUnit WHERE (ParentID = @CompanyID And AccType = 0 And UnitType = 1) or id = @CompanyID
        end

        else
        begin
            insert into @Result
            select @CompanyID as id
        end
        RETURN
	end
go
