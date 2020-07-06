SET CONCAT_NULL_YIELDS_NULL, ANSI_NULLS, ANSI_PADDING, QUOTED_IDENTIFIER, ANSI_WARNINGS, ARITHABORT, XACT_ABORT ON
SET NUMERIC_ROUNDABORT, IMPLICIT_TRANSACTIONS OFF
GO

DECLARE @db_name SYSNAME
SET @db_name = N'EASYBOOKS'

DECLARE @bakpath NVARCHAR(4000)
SET @bakpath = N'E:\Backup_DB\' + @db_name

IF OBJECT_ID('xp_create_subdir') IS NOT NULL
  EXEC xp_create_subdir @bakpath
GO

DECLARE @db_name SYSNAME
SET @db_name = N'EASYBOOKS'

DECLARE @file_name NVARCHAR(250)
SET @file_name =  @db_name + '_' +
/*append date*/ REPLACE(CONVERT(NVARCHAR(10), GETDATE(), 102), '.', '_') + '_' +
/*append time*/ REPLACE(CONVERT(NVARCHAR(5), GETDATE(), 108), ':', '_') + '.bak'


DECLARE @filepath NVARCHAR(4000)
SET @filepath =
/*define base part*/ N'E:\Backup_DB\' + @db_name + '\' + @file_name

DECLARE @SQL NVARCHAR(MAX)
SET @SQL =
    N'BACKUP DATABASE ' + QUOTENAME(@db_name) + ' TO DISK = @filepath WITH INIT' +
      CASE WHEN EXISTS(
                SELECT value
                FROM sys.configurations
                WHERE name = 'backup compression default'
          )
        THEN ', COMPRESSION'
        ELSE ''
      END

EXEC sys.sp_executesql @SQL, N'@filepath NVARCHAR(4000)', @filepath = @filepath
