alter table TIAllocation
    alter column NoFBook nvarchar(25) null
go
alter table TIAllocation
    alter column TemplateID uniqueidentifier null
go

alter table TIAllocation
    alter column NoMBook nvarchar(25) null
go
alter table TIIncrement
    alter column NoFBook nvarchar(25) null
go
alter table TIIncrement
    alter column TemplateID uniqueidentifier null
go
alter table TIIncrement
    alter column NoMBook nvarchar(25) null
go

alter table TIDecrement
    alter column TemplateID uniqueidentifier null
go
alter table TIDecrement
    alter column NoFBook nvarchar(25) null
go

alter table TIDecrement
    alter column NoMBook nvarchar(25) null
go

alter table TIAdjustment
    alter column TemplateID uniqueidentifier null
go
alter table TIAdjustment
    alter column NoFBook nvarchar(25) null
go

alter table TIAdjustment
    alter column NoMBook nvarchar(25) null
go


alter table TITransfer
    alter column TemplateID uniqueidentifier null
go
alter table TITransfer
    alter column NoFBook nvarchar(25) null
go

alter table TITransfer
    alter column NoMBook nvarchar(25) null
go

alter table TIAudit
    alter column TemplateID uniqueidentifier null
go

alter table TIAudit
    alter column NoFBook nvarchar(25) null
go

alter table TIAudit
    alter column NoMBook nvarchar(25) null
go

alter table TIAllocation
    alter column TemplateID uniqueidentifier null
go

alter table TIAllocation
    alter column NoFBook nvarchar(25) null
go

alter table TIAllocation
    alter column NoMBook nvarchar(25) null
go

alter table FAIncrement
    alter column TemplateID uniqueidentifier null
go
alter table FAIncrement
    alter column NoFBook nvarchar(25) null
go

alter table FAIncrement
    alter column NoMBook nvarchar(25) null
go

alter table FADecrement
    alter column TemplateID uniqueidentifier null
go
alter table FADecrement
    alter column NoFBook nvarchar(25) null
go

alter table FADecrement
    alter column NoMBook nvarchar(25) null
go

alter table FAAdjustment
    alter column TemplateID uniqueidentifier null
go
alter table FAAdjustment
    alter column NoFBook nvarchar(25) null
go

alter table FAAdjustment
    alter column NoMBook nvarchar(25) null
go

alter table FATransfer
    alter column TemplateID uniqueidentifier null
go
alter table FATransfer
    alter column NoFBook nvarchar(25) null
go

alter table FATransfer
    alter column NoMBook nvarchar(25) null
go
alter table FAAudit
    alter column TemplateID uniqueidentifier null
go

alter table FAAudit
    alter column NoFBook nvarchar(25) null
go

alter table FAAudit
    alter column NoMBook nvarchar(25) null
go

alter table FADepreciation
    alter column TemplateID uniqueidentifier null
go
alter table FADepreciation
    alter column NoFBook nvarchar(25) null
go

alter table FADepreciation
    alter column NoMBook nvarchar(25) null
go

