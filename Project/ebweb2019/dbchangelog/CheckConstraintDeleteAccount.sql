/*
-- =============================================
-- Author:		<namnh>
-- Create date: <03-2020>
-- Description:	<Xoá các danh mục khi xoá cơ cấu tổ chức>
-- =============================================
*/
-- [DELETEOrganizationUnit] '044643C6-EB38-8A4D-8A93-F2F4E78F32D4'
ALTER proc [dbo].[DELETEOrganizationUnit] @companyID UNIQUEIDENTIFIER
as
begin
    DELETE FROM SystemOption WHERE CompanyID = @companyID
    DELETE FROM AccountList WHERE CompanyID = @companyID
    DELETE FROM AccountTransfer WHERE CompanyID = @companyID
    DELETE FROM AccountDefault WHERE CompanyID = @companyID
    DELETE FROM AutoPrinciple WHERE CompanyID = @companyID
    DELETE FROM Bank WHERE CompanyID = @companyID
    DELETE FROM GenCode WHERE CompanyID = @companyID
    DELETE FROM Currency WHERE CompanyID = @companyID
    DELETE FROM Unit WHERE CompanyID = @companyID
    DELETE FROM PersonalSalaryTax WHERE CompanyID = @companyID
    DELETE FROM TimeSheetSymbols WHERE CompanyID = @companyID
    DELETE FROM FixedAssetCategory WHERE CompanyID = @companyID
    DELETE FROM MaterialGoodsSpecialTaxGroup WHERE CompanyID = @companyID
    DELETE FROM MaterialGoodsResourceTaxGroup WHERE CompanyID = @companyID
    DELETE FROM ContractState WHERE CompanyID = @companyID
    DELETE FROM Warranty WHERE CompanyID = @companyID
    DELETE FROM OrganizationUnitOptionReport WHERE organizationunitid = @companyID
end
go
