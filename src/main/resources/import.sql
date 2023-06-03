INSERT INTO wsda_role (id, role) VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_MERCHANT');
INSERT INTO wsda_user (id, email, password, name, wsda_role_id) VALUES (0, 'emanuelemuzio@hotmail.it', '123','Emanuele Muzio',1)
INSERT INTO wsda_credit_card(balance, wsda_user_id, number) VALUES (500, 0, 123)