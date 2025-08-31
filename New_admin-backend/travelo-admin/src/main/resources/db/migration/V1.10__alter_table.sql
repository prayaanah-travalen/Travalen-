ALTER table travalen.finance
ADD column if not exists  pan varchar(50);

ALTER table travalen.hotel_room
ADD column if not exists extra_bed_cost varchar(50),
ADD column if not exists extra_child_bed_cost varchar(50);

