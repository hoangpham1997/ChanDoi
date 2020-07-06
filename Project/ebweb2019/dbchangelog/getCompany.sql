-- [getCompany] '761D463F-7068-E848-AF53-56DB1D03A87C', 'TCKHAC_SDDMDoiTuong'

ALTER PROCEDURE [dbo].[getCompany] (@CompanyId UNIQUEIDENTIFIER,
                                             @Code nvarchar(25))
as
    begin
        set nocount on;
--         SELECT * into #tg1 from EbOrganizationUnit WHERE ID = @CompanyId
--         DECLARE @UnitType2 INT, @AccType INT
--         SET @UnitType2 = (SELECT TOP 1 UnitType FROM #tg1)
--         SET @AccType = (SELECT TOP 1 AccType FROM #tg1)
--         DECLARE @ParentID2 UNIQUEIDENTIFIER = (SELECT ID FROM EbOrganizationUnit WHERE ID = (SELECT TOP 1 ParentID FROM #tg1))
--         IF(@UnitType2 = 1 AND @AccType = 0 AND @ParentID2 IS NOT NULL)
--         BEGIN
--             SET @CompanyId = @ParentID2
--         END
--             drop table #tg1
        DECLARE @findAllCompany nvarchar(2) = (select so.Data from SystemOption so where CompanyID = @CompanyId and code = @Code);
        if @findAllCompany = '0'
            begin
                DECLARE @UnitType int = (select UnitType from EbOrganizationUnit ou where ou.ID =  @CompanyId);
                if @UnitType = 0
                    begin
                        select id as uuid from EbOrganizationUnit where id = @CompanyId or (ParentID = @CompanyId AND UnitType = 1)
                    end
                else
                    begin
                        DECLARE @ParentId uniqueidentifier = (select ParentID from EbOrganizationUnit ou where ou.ID =  @CompanyId);
                        select id as uuid from EbOrganizationUnit where id = @ParentId or (ParentID = @ParentId AND UnitType = 1)
                    end
            end
        else
            begin
                    select @CompanyId as uuid
            end
    end
go

