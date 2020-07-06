/*
@Author Hautv
*/
CREATE TRIGGER [dbo].[updateGenCodeInMCPayment]
    ON [MCPayment]
    AFTER UPDATE, INSERT
    AS
BEGIN
    if ((select COUNT(*) from inserted) = 1)
        begin
            DECLARE
                @TypeLedger int = (select TypeLedger from INSERTED)
            DECLARE
                @companyID uniqueidentifier = (select CompanyID from INSERTED)
            DECLARE
                @noFBook nvarchar(MAX) =(select NoFBook from INSERTED)
            DECLARE
                @noMBook nvarchar(MAX) = (select NoMBook from INSERTED)
            DECLARE
                @typeID int = (select TypeID from INSERTED)

            DECLARE
                @NoFBookOld nvarchar(MAX) = (select NoFBook from deleted)

            DECLARE
                @NoMBookOld nvarchar(MAX) = (select NoMBook from deleted)

            if (@noFBook != @NoFBookOld or @noMBook != @NoMBookOld or (@NoFBookOld is NULL and @NoMBookOld is NULL))
                begin
                    EXEC updateGenCodeST @TypeLedger = @TypeLedger, @companyID = @companyID, @noFBook = @noFBook,
                         @noMBook = @noMBook,
                         @typeID = @typeID
                end
        end
END
go


