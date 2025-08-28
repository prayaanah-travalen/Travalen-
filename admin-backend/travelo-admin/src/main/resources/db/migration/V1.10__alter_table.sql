ALTER table finance
ADD column if not exists  pan varchar(50);

ALTER table hotel_room
ADD column if not exists extra_bed_cost varchar(50),
ADD column if not exists extra_child_bed_cost varchar(50);

