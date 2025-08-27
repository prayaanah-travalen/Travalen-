ALTER table travalen.popular_destination
ADD dest_name varchar(50),
ADD description varchar(100);

ALTER table travalen.popular_destination
ALTER image TYPE varchar(500);