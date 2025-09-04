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
VALUES ('Train', 'Training'),
       ('Cert', 'Certification'),
       ('FAC', 'First aid course');

INSERT INTO Users (first_name, last_name, address_id, phone_number, email_address, created_at, logged_at, is_active,
                   password_hash)
VALUES ('Jan', 'Kowalski', 1, '+48123456789', 'jan.kowalski@test.com', '2025-01-01 08:00:00',
        '2025-07-20 12:00:00', TRUE, 'JanKowalski!'),
       ('Anna', 'Nowak', 2, '+48987654321', 'anna.nowak@test.com', '2025-02-15 09:30:00', '2025-07-22 16:45:00',
        TRUE, 'AnnaNowak!'),
       ('Piotr', 'Wisniewski', 3, '+48111222333', 'piotr.wisniewski@test.com', '2025-03-10 10:15:00',
        '2025-07-23 09:20:00', FALSE, 'PiotrWisniewski!'),
       ('Katarzyna', 'Wojcik', 1, '+48444555666', 'katarzyna.wojscik@test.com;', '2025-04-05 11:45:00',
        '2025-07-24 14:30:00', TRUE, 'KatarzynaWojcik!');

INSERT INTO Firedepartments (name, address_id, is_NRFS)
VALUES ('OSP Strzyzowice', 1, TRUE),
       ('OSP Dabie', 2, FALSE);

INSERT INTO Firefighters (firefighter_id, firedepartment_id, role)
VALUES (1, 1, 'PRESIDENT'),
       (2, 1, 'MEMBER'),
       (3, 2, 'USER');

INSERT INTO Firefighter_Activities (firefighter_id, firefighter_activity_type, activity_date, expiration_date,
                                    description)
VALUES (1, 'Train', '2025-07-01 09:00:00', '2026-07-01 09:00:00', 'Basic firefighter training'),
       (2, 'FAC', '2025-06-20 10:00:00', '2026-06-20 10:00:00', 'First aid course'),
       (3, 'Cert', '2025-05-15 08:00:00', '2027-05-15 08:00:00', 'Advanced Certification');

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