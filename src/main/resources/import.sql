INSERT INTO wsda_role (id, role) VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_MERCHANT');
INSERT INTO wsda_user (id, age, email, password, name, wsda_role_id) VALUES (1, 23, 'emanuelemuzio@hotmail.it', '123','Emanuele Muzio',1),(2, 22,'barracostefania@outlook.it','456','Stefania Barraco',2)
INSERT INTO wsda_credit_card(balance, wsda_user_id, number) VALUES (500, 1, 123), (50, 2, 234)