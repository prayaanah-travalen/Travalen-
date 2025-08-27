create table travalen.common_parameter(id BIGSERIAL NOT NULL,
parameter_id varchar(50),
parameter_group varchar(50),
parameter varchar(50),
description varchar(50),
available boolean
);


insert into travalen.common_parameter(parameter_id,parameter_group,parameter,description, available) values
('PCK0001','PACKAGE','accommodation-only','Accommodation Only', true),
('PCK0002','PACKAGE','breakfast-only','Breakfast Only', true),
('PCK0003','PACKAGE','breakfast-with-dinner','Breakfast With Dinner', true),
('PCK0003','PACKAGE','breakfast-with-lunch','Breakfast With Lunch', true),
('PCK0004','PACKAGE','all-meals','Breakfast, Lunch, Dinner', true)


;


