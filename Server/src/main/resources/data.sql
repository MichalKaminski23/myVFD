INSERT INTO Addresses (country, voivodeship, city, postal_code, street, apart_number, house_number)
VALUES ('Poland', 'Silesia', 'Strzyzowice', '00-001', 'Belna', '1A', '10'),
       ('Poland', 'Silesia', 'Dabie', '30-001', 'Pszczela', NULL, '5'),
       ('Poland', 'Silesia', 'Psary', '40-001', 'Kolejowa', '2', '20');

INSERT INTO Asset_Types (asset_type, name)
VALUES ('WaterPump', 'Heavy water pump'),
       ('FH52', 'Firehose'),
       ('RPA', 'Respiratory Protective Apparatus');

INSERT INTO Inspection_types (inspection_type, name)
VALUES ('SecCheck', 'Security check'),
       ('MainTen', 'Maintenance'),
       ('Insu', 'Insurance');

INSERT INTO Operation_Types (operation_type, name)
VALUES ('ResOpe', 'Rescue operation'),
       ('Exerc', 'Exercises'),
       ('MainWor', 'Maintenance work');

INSERT INTO Firefighter_Activity_Types (firefighter_activity_type, name)
VALUES ('TRAIN', 'Training'),
       ('CERT', 'Certification'),
       ('FAC', 'First aid course');

INSERT INTO Users (first_name, last_name, address_id, phone_number, email_address, created_at, logged_at, is_active,
                   password_hash)
VALUES ('Jan', 'Kowalski', 1, '+48123456789', 'jan.kowalski@test.com', '2025-01-01 08:00:00',
        '2025-07-20 12:00:00', TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Anna', 'Nowak', 2, '+48987654321', 'anna.nowak@test.com', '2025-02-15 09:30:00', '2025-07-22 16:45:00',
        TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Piotr', 'Wisniewski', 3, '+48111222333', 'piotr.wisniewski@test.com', '2025-03-10 10:15:00',
        '2025-07-23 09:20:00', FALSE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Katarzyna', 'Wojcik', 1, '+48444555666', 'katarzyna.wojcik@test.com;', '2025-04-05 11:45:00',
        '2025-07-24 14:30:00', TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Tomasz', 'Zielinski', 1, '+48500111222', 'tomasz.zielinski@test.com', '2025-01-12 08:30:00',
        '2025-07-22 10:05:00', TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Maria', 'Lewandowska', 2, '+48500333444', 'maria.lewandowska@test.com', '2025-02-03 09:10:00',
        '2025-07-23 11:20:00', TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Pawel', 'Kaczmarek', 3, '+48500555666', 'pawel.kaczmarek@test.com', '2025-02-18 12:00:00',
        '2025-07-24 15:45:00', TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Agnieszka', 'Dabrowska', 1, '+48500777888', 'agnieszka.dabrowska@test.com', '2025-03-05 07:50:00',
        '2025-07-25 09:10:00', TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Michal', 'Wisniewski', 2, '+48500999000', 'michal.wisniewski@test.com', '2025-03-21 14:40:00',
        '2025-07-26 18:25:00', TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Monika', 'Kaminska', 3, '+48501234567', 'monika.kaminska@test.com', '2025-03-29 10:15:00',
        '2025-07-27 13:35:00', TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Krzysztof', 'Grabowski', 1, '+48507654321', 'krzysztof.grabowski@test.com', '2025-04-04 16:05:00',
        '2025-07-28 19:05:00', TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Ewa', 'Krol', 2, '+48504152637', 'ewa.krol@test.com', '2025-04-12 08:05:00', '2025-07-29 12:12:00', TRUE,
        '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Damian', 'Pawlak', 3, '+48506802468', 'damian.pawlak@test.com', '2025-04-20 09:25:00', '2025-07-30 14:45:00',
        TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Natalia', 'Dudek', 1, '+48507773311', 'natalia.dudek@test.com', '2025-05-02 11:30:00', '2025-07-31 17:00:00',
        TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Szymon', 'Nowicki', 2, '+48506669988', 'szymon.nowicki@test.com', '2025-05-10 13:00:00', '2025-08-01 09:40:00',
        TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Olga', 'Piatek', 3, '+48505551234', 'olga.piatek@test.com', '2025-05-18 15:25:00', '2025-08-02 10:10:00', TRUE,
        '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Patryk', 'Sokolowski', 1, '+48504449876', 'patryk.sokolowski@test.com', '2025-05-26 08:55:00',
        '2025-08-03 13:20:00', TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Weronika', 'Maj', 2, '+48503334555', 'weronika.maj@test.com', '2025-06-03 09:45:00', '2025-08-04 16:35:00',
        TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Bartek', 'Czerwinski', 3, '+48502229876', 'bartek.czerwinski@test.com', '2025-06-11 10:20:00',
        '2025-08-05 18:10:00', TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Julia', 'Sawicka', 1, '+48501116777', 'julia.sawicka@test.com', '2025-06-19 12:35:00', '2025-08-06 08:50:00',
        TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Kamil', 'Lis', 2, '+48508889900', 'kamil.lis@test.com', '2025-06-27 14:05:00', '2025-08-07 11:45:00', TRUE,
        '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Zuzanna', 'Pawlowska', 3, '+48507774422', 'zuzanna.pawlowska@test.com', '2025-07-05 08:20:00',
        '2025-08-08 13:15:00', TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Lukasz', 'Nowakowski', 1, '+48506665544', 'lukasz.nowakowski@test.com', '2025-07-13 09:10:00',
        '2025-08-09 15:30:00', TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Alicja', 'Borkowska', 2, '+48505553311', 'alicja.borkowska@test.com', '2025-07-21 10:45:00',
        '2025-08-10 17:25:00', TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Alicja', 'Zyga', 2, '+48505753301', 'alicja.zyga@test.com', '2025-07-21 10:45:00',
        '2025-08-10 17:25:00', TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS'),
       ('Alicja', 'Terlin', 2, '+48505750090', 'alicja.terlin@test.com', '2025-07-21 10:45:00',
        '2025-08-10 17:25:00', TRUE, '$2a$10$J//IgZVeyG5bD/fOEwdXpOfUmddLn9gcsunMFyNdGTa1qoRiBzIpS');

INSERT INTO Firedepartments (name, address_id, is_nrfs)
VALUES ('OSP Strzyzowice', 1, TRUE),
       ('OSP Dabie', 2, FALSE);

INSERT INTO Firefighters (firefighter_id, firedepartment_id, role, status)
VALUES (1, 1, 'PRESIDENT', 'ACTIVE'),
       (2, 1, 'MEMBER', 'ACTIVE'),
       (3, 2, 'USER', 'PENDING'),
       (4, 2, 'MEMBER', 'ACTIVE'),
       (5, 1, 'USER', 'PENDING'),
       (6, 1, 'MEMBER', 'ACTIVE'),
       (7, 1, 'MEMBER', 'ACTIVE'),
       (8, 1, 'MEMBER', 'ACTIVE'),
       (9, 1, 'USER', 'PENDING'),
       (10, 1, 'MEMBER', 'ACTIVE'),
       (11, 1, 'USER', 'PENDING'),
       (12, 1, 'MEMBER', 'ACTIVE'),
       (13, 1, 'MEMBER', 'ACTIVE'),
       (14, 1, 'MEMBER', 'ACTIVE'),
       (15, 1, 'MEMBER', 'ACTIVE'),
       (16, 1, 'MEMBER', 'ACTIVE'),
       (17, 1, 'MEMBER', 'ACTIVE'),
       (18, 1, 'MEMBER', 'ACTIVE'),
       (19, 1, 'MEMBER', 'ACTIVE'),
       (20, 1, 'MEMBER', 'ACTIVE'),
       (21, 1, 'MEMBER', 'ACTIVE'),
       (22, 1, 'MEMBER', 'ACTIVE'),
       (23, 1, 'MEMBER', 'ACTIVE'),
       (24, 1, 'MEMBER', 'ACTIVE'),
       (25, 1, 'MEMBER', 'ACTIVE'),
       (26, 1, 'MEMBER', 'ACTIVE');

INSERT INTO Firefighter_Activities (firefighter_id, firefighter_activity_type, activity_date, expiration_date,
                                    description)
VALUES (1, 'TRAIN', '2025-07-01 09:00:00', '2026-07-01 09:00:00', 'Basic firefighter training'),
       (2, 'FAC', '2025-06-20 10:00:00', '2026-06-20 10:00:00', 'First aid course'),
       (3, 'CERT', '2025-05-15 08:00:00', '2027-05-15 08:00:00', 'Advanced Certification');

INSERT INTO Assets (firedepartment_id, name, asset_type, description)
VALUES (1, 'Mutant', 'WaterPump', 'Mega pump'),
       (1, 'Firehose green 52', 'FH52', 'Firehose FH52 Kadimex'),
       (2, 'Respiratory protection', 'RPA', 'Respiratory protection');

INSERT INTO Inspections (asset_id, inspection_type, inspection_date, expiration_date)
VALUES (1, 'SecCheck', '2025-06-15 11:00:00', '2026-06-15 11:00:00'),
       (2, 'Insu', '2025-07-05 14:00:00', '2026-07-05 14:00:00');

INSERT INTO Operations (firedepartment_id, operation_type, address_id, operation_date, description)
VALUES (1, 'ResOpe', 3, '2025-07-15 13:00:00', 'Rescue operation in Katowice'),
       (2, 'Exerc', 2, '2025-07-18 09:00:00', 'Monthly exercises in Bedzin');

INSERT INTO Participations (operation_id, firefighter_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (2, 3);

INSERT INTO Investment_Proposals (firedepartment_id, description, amount, submission_date, status)
VALUES (1, 'New equipment for the fire station', 50000.00, '2025-07-10 12:00:00', 'PENDING'),
       (2, 'Replacing protective clothing', 10000.00, '2025-07-12 15:30:00', 'APPROVED');

INSERT INTO Votes (proposal_id, firefighter_id, vote_value, vote_date)
VALUES (1, 1, TRUE, '2025-07-11 10:00:00'),
       (1, 2, FALSE, '2025-07-11 10:05:00'),
       (2, 2, TRUE, '2025-07-13 09:00:00'),
       (2, 3, TRUE, '2025-07-13 09:15:00');

INSERT INTO Events(firedepartment_id, header, description, event_date)
VALUES (1, 'Potato_s Day', 'The best dat in the world.', '2025-08-01 10:00:00'),
       (2, 'Community Fire Safety Day', 'Fire safety awareness event in Dabie', '2025-08-05 14:00:00');