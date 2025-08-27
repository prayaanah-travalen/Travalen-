
ALTER table travalen.users
ADD column if not exists hotel_name varchar(50),
ADD column if not exists contact_person varchar(50),
ADD column if not exists whatsapp_number varchar(50),
ADD column if not exists contact_number varchar(50),
ADD column if not exists designation varchar(50)
;

ALTER table travalen.hotel
ADD column if not exists active BOOLEAN not null
;

