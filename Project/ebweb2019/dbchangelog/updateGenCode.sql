/*
*@Author Hautv
*
* Đối với bảng có cả 2 loại sổ quản trị và sổ tài chính:
*           + Code dữ nguyên và thay tên bảng và tên Trigger
* Đối với bảng có cả 2 loại sổ quản trị và sổ tài chính:
*           + Thay tên bảng và tên Trigger
*           + Sửa dòng 20 thành @TypeLedger int = 2
*           + Comment dòng 41, 42 vì nó không có 2 trường noFBook và noMBook
*           + Xóa comment dòng 214
*
*/
CREATE TRIGGER updateGenCode
    ON MCReceipt
    AFTER UPDATE, INSERT
    AS
BEGIN
    DECLARE
        @TypeLedger int = (select TypeLedger from INSERTED)
    DECLARE
        @companyID uniqueidentifier = (select CompanyID from INSERTED)
    DECLARE
        @noFBook nvarchar(MAX)
    DECLARE
        @noMBook nvarchar(MAX)
    DECLARE
        @typeID int = (select TypeID from INSERTED)
    DECLARE
        @TypeGroupID int = (select TypeGroupID from Type where ID = @typeID)
    if @TypeLedger = 2
        begin
            DECLARE
                @count int = (select count(1)
                              from GenCode
                              where DisplayOnBook = @TypeLedger
                                and TypeGroupID = @TypeGroupID
                                and CompanyID = @companyID
                )
            if @count = 0
                begin
                    set @noFBook = (select NoFBook from INSERTED)
                    set @noMBook = (select NoMBook from INSERTED)
--                     Sổ tài chính
                    DECLARE
                        @PrefixTC nvarchar(MAX) = (select Prefix
                                                   from GenCode
                                                   where DisplayOnBook = 0
                                                     and TypeGroupID = @TypeGroupID
                                                     and CompanyID = @companyID)
                    DECLARE
                        @CurrentValueTC int = (select CurrentValue
                                               from GenCode
                                               where DisplayOnBook = 0
                                                 and TypeGroupID = @TypeGroupID
                                                 and CompanyID = @companyID)
                    DECLARE
                        @SuffixTC nvarchar(MAX) = (select Suffix
                                                   from GenCode
                                                   where DisplayOnBook = 0
                                                     and TypeGroupID = @TypeGroupID
                                                     and CompanyID = @companyID)
                    DECLARE
                        @LengthTC int = (select Length
                                         from GenCode
                                         where DisplayOnBook = 0
                                           and TypeGroupID = @TypeGroupID
                                           and CompanyID = @companyID)


                    DECLARE
                        @PrefixTCNew nvarchar(MAX) = (select Prefix from GetNoVoucherToTable(@noFBook))
                    DECLARE
                        @CurrentValueTCNew int = (select CurrentValue from GetNoVoucherToTable(@noFBook))
                    DECLARE
                        @SuffixTCNew nvarchar(MAX) = (select Suffix from GetNoVoucherToTable(@noFBook))
                    DECLARE
                        @LengthTCNew int = LEN(@noFBook) - LEN(@PrefixTCNew) - LEN(@SuffixTCNew)

                    if @PrefixTC != @PrefixTCNew or @SuffixTC != @SuffixTCNew
                        begin
                            if @LengthTC != @LengthTCNew
                                begin
                                    update GenCode
                                    set Prefix       = @PrefixTCNew,
                                        CurrentValue = @CurrentValueTCNew,
                                        Length       = @LengthTCNew,
                                        Suffix       = @SuffixTCNew
                                    where DisplayOnBook = 0
                                      and TypeGroupID = @TypeGroupID
                                      and CompanyID = @companyID
                                end
                            else
                                begin
                                    update GenCode
                                    set Prefix       = @PrefixTCNew,
                                        CurrentValue = @CurrentValueTCNew,
                                        Suffix       = @SuffixTCNew
                                    where DisplayOnBook = 0
                                      and TypeGroupID = @TypeGroupID
                                      and CompanyID = @companyID
                                end
                        end
                    else
                        begin
                            if @CurrentValueTC < @CurrentValueTCNew or @LengthTC != @LengthTCNew
                                begin
                                    if @LengthTC != @LengthTCNew
                                        begin
                                            update GenCode
                                            set CurrentValue = @CurrentValueTCNew,
                                                Length       = @LengthTCNew
                                            where DisplayOnBook = 0
                                              and TypeGroupID = @TypeGroupID
                                              and CompanyID = @companyID
                                        end
                                    else
                                        begin
                                            update GenCode
                                            set CurrentValue = @CurrentValueTCNew
                                            where DisplayOnBook = 0
                                              and TypeGroupID = @TypeGroupID
                                              and CompanyID = @companyID
                                        end
                                end
                        end
                    --                     sổ tài chính
--                     sổ quản trị
                    DECLARE
                        @PrefixQT nvarchar(MAX) = (select Prefix
                                                   from GenCode
                                                   where DisplayOnBook = 1
                                                     and TypeGroupID = @TypeGroupID
                                                     and CompanyID = @companyID)
                    DECLARE
                        @CurrentValueQT int = (select CurrentValue
                                               from GenCode
                                               where DisplayOnBook = 1
                                                 and TypeGroupID = @TypeGroupID
                                                 and CompanyID = @companyID)
                    DECLARE
                        @SuffixQT nvarchar(MAX) = (select Suffix
                                                   from GenCode
                                                   where DisplayOnBook = 1
                                                     and TypeGroupID = @TypeGroupID
                                                     and CompanyID = @companyID)
                    DECLARE
                        @LengthQT int = (select Length
                                         from GenCode
                                         where DisplayOnBook = 1
                                           and TypeGroupID = @TypeGroupID
                                           and CompanyID = @companyID)

                    DECLARE
                        @PrefixQTNew nvarchar(MAX) = (select Prefix from GetNoVoucherToTable(@noMBook))
                    DECLARE
                        @CurrentValueQTNew int = (select CurrentValue from GetNoVoucherToTable(@noMBook))
                    DECLARE
                        @SuffixQTNew nvarchar(MAX) = (select Suffix from GetNoVoucherToTable(@noMBook))
                    DECLARE
                        @LengthQTNew int = LEN(@noMBook) - LEN(@PrefixQTNew) - LEN(@SuffixQTNew)

                    if @PrefixQT != @PrefixQTNew or @SuffixQT != @SuffixQTNew
                        begin
                            if @LengthQT != @LengthQTNew
                                begin
                                    update GenCode
                                    set Prefix       = @PrefixQTNew,
                                        CurrentValue = @CurrentValueQTNew,
                                        Length       = @LengthQTNew,
                                        Suffix       = @SuffixQTNew
                                    where DisplayOnBook = 1
                                      and TypeGroupID = @TypeGroupID
                                      and CompanyID = @companyID
                                end
                            else
                                begin
                                    update GenCode
                                    set Prefix       = @PrefixQTNew,
                                        CurrentValue = @CurrentValueQTNew,
                                        Suffix       = @SuffixQTNew
                                    where DisplayOnBook = 1
                                      and TypeGroupID = @TypeGroupID
                                      and CompanyID = @companyID
                                end
                        end
                    else
                        begin
                            if @CurrentValueQT < @CurrentValueQTNew or @LengthQT != @LengthQTNew
                                begin
                                    if @LengthQT != @LengthQTNew
                                        begin
                                            update GenCode
                                            set CurrentValue = @CurrentValueQTNew,
                                                Length       = @LengthQTNew
                                            where DisplayOnBook = 1
                                              and TypeGroupID = @TypeGroupID
                                              and CompanyID = @companyID
                                        end
                                    else
                                        begin
                                            update GenCode
                                            set CurrentValue = @CurrentValueQTNew
                                            where DisplayOnBook = 1
                                              and TypeGroupID = @TypeGroupID
                                              and CompanyID = @companyID
                                        end
                                end
                        end
--                     sổ quản trị
                end
            else
                begin
                    --                     Không chia sổ tài chính, sổ quản trị
--                     set @noFBook = (select No from INSERTED)
                    DECLARE
                        @PrefixKC nvarchar(MAX) = (select Prefix
                                                   from GenCode
                                                   where DisplayOnBook = 2
                                                     and TypeGroupID = @TypeGroupID
                                                     and CompanyID = @companyID)
                    DECLARE
                        @CurrentValueKC int = (select CurrentValue
                                               from GenCode
                                               where DisplayOnBook = 2
                                                 and TypeGroupID = @TypeGroupID
                                                 and CompanyID = @companyID)
                    DECLARE
                        @SuffixKC nvarchar(MAX) = (select Suffix
                                                   from GenCode
                                                   where DisplayOnBook = 2
                                                     and TypeGroupID = @TypeGroupID
                                                     and CompanyID = @companyID)
                    DECLARE
                        @LengthKC nvarchar(MAX) = (select Length
                                                   from GenCode
                                                   where DisplayOnBook = 2
                                                     and TypeGroupID = @TypeGroupID
                                                     and CompanyID = @companyID)

                    DECLARE
                        @PrefixKCNew nvarchar(MAX) = (select Prefix from GetNoVoucherToTable(@noFBook))
                    DECLARE
                        @CurrentValueKCNew int = (select CurrentValue from GetNoVoucherToTable(@noFBook))
                    DECLARE
                        @SuffixKCNew nvarchar(MAX) = (select Suffix from GetNoVoucherToTable(@noFBook))
                    DECLARE
                        @LengthKCNew int = LEN(@noFBook) - LEN(@PrefixKCNew) - LEN(@SuffixKCNew)

                    if @PrefixKC != @PrefixKCNew or @SuffixKC != @SuffixKCNew
                        begin
                            if @LengthKC != @LengthKCNew
                                begin
                                    update GenCode
                                    set Prefix       = @PrefixKCNew,
                                        CurrentValue = @CurrentValueKCNew,
                                        Length       = @LengthKCNew,
                                        Suffix       = @SuffixKCNew
                                    where DisplayOnBook = 2
                                      and TypeGroupID = @TypeGroupID
                                      and CompanyID = @companyID
                                end
                            else
                                begin
                                    update GenCode
                                    set Prefix       = @PrefixKCNew,
                                        CurrentValue = @CurrentValueKCNew,
                                        Suffix       = @SuffixKCNew
                                    where DisplayOnBook = 2
                                      and TypeGroupID = @TypeGroupID
                                      and CompanyID = @companyID
                                end
                        end
                    else
                        begin
                            if @CurrentValueKC < @CurrentValueKCNew or @LengthKC != @LengthKCNew
                                begin
                                    if @LengthKC != @LengthKCNew
                                        begin
                                            update GenCode
                                            set CurrentValue = @CurrentValueKCNew,
                                                Length       = @LengthKCNew
                                            where DisplayOnBook = 2
                                              and TypeGroupID = @TypeGroupID
                                              and CompanyID = @companyID
                                        end
                                    else
                                        begin
                                            update GenCode
                                            set CurrentValue = @CurrentValueKCNew
                                            where DisplayOnBook = 2
                                              and TypeGroupID = @TypeGroupID
                                              and CompanyID = @companyID
                                        end

                                end
                        end
--                     Không chia sổ tài chính, sổ quản trị
                end
        end
    else
        begin
            DECLARE
                @Prefix nvarchar(MAX) = (select Prefix
                                         from GenCode
                                         where DisplayOnBook = @TypeLedger
                                           and TypeGroupID = @TypeGroupID
                                           and CompanyID = @companyID)
            DECLARE
                @CurrentValue int = (select CurrentValue
                                     from GenCode
                                     where DisplayOnBook = @TypeLedger
                                       and TypeGroupID = @TypeGroupID
                                       and CompanyID = @companyID)
            DECLARE
                @Suffix nvarchar(MAX) = (select Suffix
                                         from GenCode
                                         where DisplayOnBook = @TypeLedger
                                           and TypeGroupID = @TypeGroupID
                                           and CompanyID = @companyID)
            DECLARE
                @Length nvarchar(MAX) = (select Length
                                         from GenCode
                                         where DisplayOnBook = @TypeLedger
                                           and TypeGroupID = @TypeGroupID
                                           and CompanyID = @companyID)
            DECLARE
                @no nvarchar(MAX)
            if @TypeLedger = 0
                set @no = @noFBook
            else
                set @no = @noMBook

            DECLARE
                @PrefixNew nvarchar(MAX) = (select Prefix from GetNoVoucherToTable(@no))
            DECLARE
                @CurrentValueNew int = (select CurrentValue from GetNoVoucherToTable(@no))
            DECLARE
                @SuffixNew nvarchar(MAX) = (select Suffix from GetNoVoucherToTable(@no))
            DECLARE
                @LengthNew int = LEN(@no) - LEN(@PrefixNew) - LEN(@SuffixNew)

            if (@Prefix != @PrefixNew or @Suffix != @SuffixNew)
                begin
                    if @Length != @LengthNew
                        begin
                            update GenCode
                            set Prefix       = @PrefixNew,
                                CurrentValue = @CurrentValueNew,
                                Length       = @LengthNew,
                                Suffix       = @SuffixNew
                            where DisplayOnBook = @TypeLedger
                              and TypeGroupID = @TypeGroupID
                              and CompanyID = @companyID
                        end
                    else
                        begin
                            update GenCode
                            set Prefix       = @PrefixNew,
                                CurrentValue = @CurrentValueNew,
                                Suffix       = @SuffixNew
                            where DisplayOnBook = @TypeLedger
                              and TypeGroupID = @TypeGroupID
                              and CompanyID = @companyID
                        end
                end
            else
                begin
                    if @CurrentValue < @CurrentValueNew or @Length != @LengthNew
                        begin
                            if @Length != @LengthNew
                                begin
                                    update GenCode
                                    set CurrentValue = @CurrentValueNew,
                                        Length       = @LengthNew
                                    where DisplayOnBook = @TypeLedger
                                      and TypeGroupID = @TypeGroupID
                                      and CompanyID = @companyID
                                end
                        end
                    else
                        begin
                            update GenCode
                            set CurrentValue = @CurrentValueNew
                            where DisplayOnBook = @TypeLedger
                              and TypeGroupID = @TypeGroupID
                              and CompanyID = @companyID
                        end

                end
        end
END
go

