/*
-- =============================================
-- Author:		<chuongnv>
-- Create date: <18-02-2020>
-- Description:	<Lấy dữ liệu bảng cân đối kế toán>
-- Proc_Get_BANG_CAN_DOI_TAI_KHOAN '5A814271-A115-41D1-BA4D-C50BC0040482', 0, '01/01/2019 00:00:00', '12/31/2020 23:59:59', 1, 0, 0, 0
-- =============================================
*/
CREATE PROCEDURE [dbo].[Proc_BANG_CAN_DOI_TAI_KHOAN] @CompanyID UNIQUEIDENTIFIER, --Chi nhánh
                                                     @IncludeDependentBranch BIT, --Lấy số liệu chi nhánh phụ thuộc
                                                     @FromDate DATETIME, -- Đến ngày
                                                     @ToDate DATETIME, --Từ ngày
                                                     @MaxAccountGrade INT, --Bậc tài khoản
                                                     @IsBalanceBothSide BIT,
                                                     @IsSimilarBranch BIT,
                                                     @IsWorkingWithManagementBook BIT -- Lấy số liệu từ sổ quản trị
AS
BEGIN
    SELECT t.AccountID as ID,
           t.AccountID,
           t.AccountCategoryKind,
           t.AccountNumber,
           t.AccountName,
           sum(t.OpeningDebitAmount)  OpeningDebitAmount,
           sum(t.OpeningCreditAmount) OpeningCreditAmount,
           sum(t.DebitAmount)         DebitAmount,
           sum(t.CreditAmount)        CreditAmount,
           sum(t.DebitAmountAccum)    DebitAmountAccum,
           sum(t.CreditAmountAccum)   CreditAmountAccum,
           sum(t.ClosingDebitAmount)  ClosingDebitAmount,
           sum(t.ClosingCreditAmount) ClosingCreditAmount,
           A.Grade,
           A.ParentAccountID,
           A.IsParentNode
    FROM [dbo].[Func_Get_BANG_CAN_DOI_TAI_KHOAN](@CompanyID,
                 @FromDate
             , @ToDate
             , @MaxAccountGrade
             , @IsBalanceBothSide
        , @IsWorkingWithManagementBook, @IncludeDependentBranch) t
    LEFT JOIN AccountList A ON A.ID = t.AccountID
    WHERE A.CompanyID = (SELECT TOP (1) (CASE
                                                     WHEN ParentID IS NOT NULL THEN ParentID
                                                     ELSE ID END)
                                 FROM EbOrganizationUnit
                                 WHERE ID = @CompanyID)
    group by t.AccountID,
             t.AccountCategoryKind,
             t.AccountNumber,
             t.AccountName,
             A.Grade,
             A.ParentAccountID,
             A.IsParentNode
    order by t.AccountNumber
end
go

