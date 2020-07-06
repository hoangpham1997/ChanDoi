alter table SAQuoteDetail drop column OrderPriority
ALTER TABLE SAQuoteDetail
    ADD OrderPriority integer identity not null
