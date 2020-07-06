-- Author : Chuongnv
-- crate : 2020-04-20
-- Thêm col

create table MaterialGoodsSpecificationsLedger
(
    ID               uniqueidentifier not null primary key,
    CompanyID        uniqueidentifier,
    BranchID         UNIQUEIDENTIFIER,
    ReferenceID      UNIQUEIDENTIFIER,
    DetailID         UNIQUEIDENTIFIER,
    RefTypeID        INT,
    Date             DATETIME,
    PostedDate       DATETIME,
    TypeLedger       INT,
    NoFBook          NVARCHAR(25),
    NoMBook          NVARCHAR(25),
    MaterialGoodsID  uniqueidentifier,
    Specification1   NVARCHAR(50),
    Specification2   NVARCHAR(50),
    Specification3   NVARCHAR(50),
    Specification4   NVARCHAR(50),
    Specification5   NVARCHAR(50),
    IWRepositoryID   uniqueidentifier,
    OWRepositoryID   uniqueidentifier,
    IWQuantity       DECIMAL(25, 10),
    OWQuantity       DECIMAL(25, 10),
    ConfrontID       uniqueidentifier,
    ConfrontDetailID uniqueidentifier,
    ConfrontTypeID   uniqueidentifier,
    OrderPriority    INT identity
)
go

