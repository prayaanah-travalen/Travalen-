INSERT INTO role(
	 role_name, registration_time, registrant_by, last_updated_time, last_updated_by)
	VALUES  ('Super Admin', now(), 'SYSTEM', now(), 'SYSTEM'),
            ('Hotel Admin', now(), 'SYSTEM', now(), 'SYSTEM'),
            ('Booking Admin', now(), 'SYSTEM', now(), 'SYSTEM'),
            ('Admin Admin', now(), 'SYSTEM', now(), 'SYSTEM');

--password: admin_pass
INSERT INTO users(
     user_name, email_id, phone, password, registration_time, registrant_by, last_updated_time, last_updated_by)
    VALUES ( 'Super Admin', 'operations@prayaanah.com', '', '$2a$12$p6QYJTrjKu597ygzOZPhXuca7nt/MjJVW60.qDxH87fNLc89Ww48C', now(), 'SYSTEM', now(), 'SYSTEM');


INSERT INTO users_role(
     user_id, role_id)
    VALUES ((select user_id from users where email_id='operations@prayaanah.com'),(select role_id from role where role_name='Super Admin') );
