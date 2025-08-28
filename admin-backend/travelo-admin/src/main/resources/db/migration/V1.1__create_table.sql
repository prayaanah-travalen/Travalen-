create table location( location_id BIGSERIAL NOT NULL,
    location varchar(100),
    CONSTRAINT PK_LOCATION  PRIMARY KEY ( location_id )
);

create table hotel (
    hotel_code BIGSERIAL NOT NULL,
    hotel_name varchar(50),
    email varchar(50),
    address varchar(50),
    postal_code varchar(15),
    city varchar(50),
    state varchar(50),
    country varchar(50),
    location_id bigint,
    star_rating varchar(10),
    about varchar(500),
    property_rule varchar(500),
    website_link varchar(50),
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_HOTEL  PRIMARY KEY ( hotel_code ),
    CONSTRAINT FK_LOCATION FOREIGN KEY (location_id) REFERENCES travalen.location(location_id)
);

create table room(
    room_code BIGSERIAL NOT NULL,
    room_type varchar(50),
    bed_type varchar(50),
CONSTRAINT PK_ROOM_CODE PRIMARY KEY(room_code));

create table hotel_room(
    hotel_room_id BIGSERIAL NOT NULL,
    hotel_code bigint,
    room_name varchar(50),
    no_of_rooms integer,
    occupancy integer,
    room_description varchar(50),
    price varchar(50),
    bed_type varchar(50),
    status boolean,
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_HOTEL_ROOM PRIMARY KEY (hotel_room_id),
    CONSTRAINT FK_HOTEL_CODE FOREIGN KEY (hotel_code) REFERENCES travalen.hotel(hotel_code)
);

create table hotel_availabilty(
    id BIGSERIAL NOT NULL,
    hotel_code bigint,
    available_from TIMESTAMP,
    available_till TIMESTAMP,
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_HOTEL_AVAILABILITY PRIMARY KEY (id),
    CONSTRAINT FK_HOTEL_CODE FOREIGN KEY (hotel_code) REFERENCES travalen.hotel(hotel_code)
);

create table room_availabilty(
    id BIGSERIAL NOT NULL,
    hotel_room_id bigint,
    available_from TIMESTAMP,
    available_till TIMESTAMP,
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_HOTEL_ROOM_AVAILABILITY PRIMARY KEY (id),
    CONSTRAINT FK_HOTEL_CODE FOREIGN KEY (hotel_room_id) REFERENCES travalen.hotel_room(hotel_room_id)
);

create table hotel_api(
    hotel_ap_id BIGSERIAL NOT NULL,
    hotel_code bigint,
    api_name varchar(50),
    api_url varchar(50),
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_HOTEL_API PRIMARY KEY (hotel_ap_id),
    CONSTRAINT FK_HOTEL_CODE FOREIGN KEY (hotel_code) REFERENCES travalen.hotel(hotel_code)
);

create table hotel_image(
    image_id BIGSERIAL NOT NULL,
    hotel_code bigint,
    image bytea,
    name varchar(50),
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_HOTEL_IMAGE PRIMARY KEY (image_id),
    CONSTRAINT FK_HOTEL_CODE FOREIGN KEY (hotel_code) REFERENCES travalen.hotel(hotel_code)
);

create table room_image(
    image_id BIGSERIAL NOT NULL,
    hotel_room_id bigint,
    image bytea,
    name varchar(50),
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_HOTEL_ROOM_IMAGE PRIMARY KEY (image_id),
    CONSTRAINT FK_HOTEL_CODE FOREIGN KEY (hotel_room_id) REFERENCES travalen.hotel_room(hotel_room_id)
);

create table role(
    role_id BIGSERIAL NOT NULL,
    role_name varchar(50),
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_ROLE PRIMARY KEY (role_id)
);

create table users(
    user_id BIGSERIAL NOT NULL,
    user_type varchar(20),
    first_name varchar(50),
    last_name varchar(50),
    status varchar(10),
    user_name varchar(50),
    email_id varchar(50),
    phone varchar(50),
    country_code varchar(50),
    password varchar(100),
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_USER PRIMARY KEY (user_id)
);

create table users_role(
    id BIGSERIAL NOT NULL,
    role_id bigint,
    user_id bigint,
    CONSTRAINT PK_USER_ROLE_ID PRIMARY KEY (id)
);

create table guest_details(
    guest_id BIGSERIAL NOT NULL,
    first_name varchar(50),
    last_name varchar(50),
    email_id varchar(50),
    phone varchar(50),
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_GUEST_ID PRIMARY KEY (guest_id)
);

create table booking(
    booking_id BIGSERIAL NOT NULL,
    no_of_hotels varchar(50),
    guest_id bigint,
    check_in TIMESTAMP,
    check_out TIMESTAMP,
    no_of_children integer,
    no_of_adults integer,
    amount varchar(50),
    status varchar(50),
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_BOOKING PRIMARY KEY (booking_id),
    CONSTRAINT FK_GUEST FOREIGN KEY (guest_id) REFERENCES travalen.guest_details(guest_id)
);

create table booking_details(
    booking_detail_id BIGSERIAL NOT NULL,
    booking_id bigint,
    hotel_room_id bigint,
    no_of_rooms integer,
    check_in TIMESTAMP,
    check_out TIMESTAMP,
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_BOOKING_DETAILS PRIMARY KEY (booking_detail_id),
    CONSTRAINT FK_HOTEL_CODE FOREIGN KEY (hotel_room_id) REFERENCES travalen.hotel_room(hotel_room_id),
    CONSTRAINT FK_BOOKING FOREIGN KEY (booking_id) REFERENCES travalen.booking(booking_id)
);


create table hotel_amenity(
    amenity_id BIGSERIAL NOT NULL,
    amenity varchar(50),
    hotel_code bigint,
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_AMENITY_ID PRIMARY KEY (amenity_id),
    CONSTRAINT FK_HOTEL_CODE FOREIGN KEY (hotel_code) REFERENCES travalen.hotel(hotel_code)
);

create table room_amenity(
    amenity_id BIGSERIAL NOT NULL,
    amenity varchar(50),
    hotel_room_id bigint,
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_ROOM_AMENITY_ID PRIMARY KEY (amenity_id),
    CONSTRAINT FK_HOTEL_CODE FOREIGN KEY (hotel_room_id) REFERENCES travalen.hotel_room(hotel_room_id)
);

create table room_tags(
    room_tag_id BIGSERIAL NOT NULL,
    room_tag varchar(50),
    hotel_room_id bigint,
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_ROOM_TAG_ID PRIMARY KEY (room_tag_id),
    CONSTRAINT FK_HOTEL_CODE FOREIGN KEY (hotel_room_id) REFERENCES travalen.hotel_room(hotel_room_id)
);

create table offers(
    offers_id BIGSERIAL NOT NULL,
    offer_details varchar(50),
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_OFFERS_ID PRIMARY KEY (offers_id)
);

create table price_slab(
    id BIGSERIAL NOT NULL,
    price_slab integer,
    max_allowed_guest integer,
    max_allowed_room integer,
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_ID PRIMARY KEY (id)
);

create table rooms_price_slab(
    id BIGSERIAL NOT NULL,
    price_slab_id bigint,
    hotel_code bigint,
    hotel_room_id bigint,
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_ROOMS_PRICE_SLAB PRIMARY KEY (id),
    CONSTRAINT FK_HOTEL_ROOM_CODE FOREIGN KEY (hotel_room_id) REFERENCES travalen.hotel_room(hotel_room_id),
    CONSTRAINT FK_PRICE_SLAB_ID FOREIGN KEY (price_slab_id) REFERENCES travalen.price_slab(id),
    CONSTRAINT FK_HOTEL_CODE FOREIGN KEY (hotel_code) REFERENCES travalen.hotel(hotel_code)
);

create table popular_destination(
    popular_dest_id BIGSERIAL NOT NULL,
    location varchar(50),
    image varchar(50),
    url varchar(50),
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_POPULAR_DEST_ID PRIMARY KEY (popular_dest_id)
);

create table payment(
    id BIGSERIAL NOT NULL,
    payment_id varchar(50) NOT NULL,
    parent_payment_id varchar(50),
    booking_id bigint,
    status varchar(50),
    transaction_no varchar(50),
    transaction_type varchar(50),
    payment_method varchar(20),
    payment_type varchar(20),
    guest_id bigint,
    bank_id varchar(50),
    auth_transaction_id varchar(50),
    pg_transaction_id varchar(50),
    pg_service_transaction_id varchar(50),
    amount varchar(50),
    merchant_transaction_id varchar(50),
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_PAYMENT_ID PRIMARY KEY (id),
    CONSTRAINT FK_BOOKING FOREIGN KEY (booking_id) REFERENCES travalen.booking(booking_id)
);

create table users_hotel(
    hotel_code bigint,
    user_id bigint
);

create table finance(
    id BIGSERIAL NOT NULL,
    country varchar(50) NOT NULL,
    bank_name varchar(50),
    swift_code varchar(50),
    ifsc varchar(50),
    account_number varchar(50),
    account_holder_name varchar(50),
    registered_for_gst varchar(20),
    trade_name varchar(50),
    gst_in varchar(50),
    hotel_code bigint,
    registration_time TIMESTAMP,
    registrant_by varchar(20),
    last_updated_time TIMESTAMP,
    last_updated_by varchar(20),
    CONSTRAINT PK_FINANCE_ID PRIMARY KEY (id),
    CONSTRAINT FK_HOTEL_CODE FOREIGN KEY (hotel_code) REFERENCES travalen.hotel(hotel_code)
);
