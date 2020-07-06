-- Author:		Hautv
-- Create date: <09/04/2020>
-- Update_Status_SAOrder '08171BA4-B89D-AF40-AA4B-045D0F7866C6,6FF0F895-9B77-2A49-99E5-06561275D5F5,39D7E76D-6FAD-434A-9C48-0689AA41C891,DC43112F-29A5-0843-8FCB-0896B01054D5'
-- select * from SAorder where id  = '6FF0F895-9B77-2A49-99E5-06561275D5F5'
-- =============================================
CREATE PROCEDURE [dbo].[Update_Status_SAOrder] @ListSAOrderID NVARCHAR(Max)
as
begin
    declare
        @TableID as table
                    (
                        ID uniqueidentifier
                    )
    declare
        @TableUpdate as table
                        (
                            ID      uniqueidentifier,
                            status  int,
                            row_num int identity
                        )
    insert into @TableID select * from Func_ConvertStringIntoTable_Nvarchar(@ListSAOrderID, ',')
    insert into @TableUpdate
    select SAOrderID                   as ID,
           case
               when sum(case
                            when QuantityDelivery is null then 0
                            else QuantityDelivery
                   end) = 0 then 0
               when sum(case
                            when QuantityDelivery is null and Quantity > 0 then -999
                            when QuantityDelivery is null and Quantity = 0 then 1
                            when QuantityDelivery < Quantity then -999
                            when QuantityDelivery >= Quantity then 1
                   end) < 0 then 1
               when sum(case
                            when QuantityDelivery is null and Quantity > 0 then -999
                            when QuantityDelivery is null and Quantity = 0 then 1
                            when QuantityDelivery < Quantity then -999
                            when QuantityDelivery >= Quantity then 1
                   end) > 0 then 2 end as status
    from SAOrderDetail
    where SAOrderID in (select ID from @TableID)
    group by SAOrderID;
--         select * from @TableUpdate
    UPDATE SAOrder set Status = 0 where ID in (select ID from @TableUpdate where status = 0)
    UPDATE SAOrder set Status = 1 where ID in (select ID from @TableUpdate where status = 1)
    UPDATE SAOrder set Status = 2 where ID in (select ID from @TableUpdate where status = 2)
end
go

