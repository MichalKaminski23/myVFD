INSERT INTO Addresses (country, voivodeship, city, postal_code, street, apart_number, house_number)
VALUES ('Polska', 'Slaskie', 'Strzyzowice', '00-001', 'Ulica Glowna', '1A', '10'),
       ('Polska', 'Slaskie', 'Dabie', '30-001', 'Rynek Glowny', NULL, '5'),
       ('Polska', 'Slaskie', 'Katowice', '40-001', 'Aleja Wolnosci', '2', '20');

INSERT INTO Asset_Types (asset_type, name)
VALUES ('ciezki', 'Woz strazacki'),
       ('w52', 'Waz strazacki'),
       ('aodo', 'Sprzet ochronny');

INSERT INTO inspection_types (inspection_type, name)
VALUES ('kon_bezp', 'Kontrola bezpieczenstwa'),
       ('kons', 'Konserwacja'),
       ('ubez', 'Ubezpieczenie');

INSERT INTO Operation_Types (operation_type, name)
VALUES ('akc_rat', 'Akcja ratunkowa'),
       ('cwi', 'Cwiczenia'),
       ('kons', 'Prace konserwacyjne');

INSERT INTO Firefighter_Activity_Types (firefighter_activity_type, name)
VALUES ('szkol', 'Szkolenie'),
       ('cert', 'Certyfikacja'),
       ('pppm', 'Kurs pierwszej pomocy');

INSERT INTO Users (first_name, last_name, address_id, phone_number, email_address, created_at, logged_at, is_active,
                   password_hash)
VALUES ('Jan', 'Kowalski', 1, '+48 123 456 789', 'jan.kowalski@example.com', '2025-01-01 08:00:00',
        '2025-07-20 12:00:00', TRUE, 'hash1'),
       ('Anna', 'Nowak', 2, '+48 987 654 321', 'anna.nowak@example.com', '2025-02-15 09:30:00', '2025-07-22 16:45:00',
        TRUE, 'hash2'),
       ('Piotr', 'Wisniewski', 3, '+48 111 222 333', 'piotr.wisniewski@example.com', '2025-03-10 10:15:00',
        '2025-07-23 09:20:00', FALSE, 'hash3');

INSERT INTO Firedepartments (name, address_id, is_NRFS)
VALUES ('OSP Strzyzowice', 1, FALSE),
       ('OSP Dabie', 2, FALSE);

INSERT INTO Firefighters (firefighter_id, firedepartment_id, role)
VALUES (1, 1, 'Prezes'),
       (2, 1, 'Wiceprezes'),
       (3, 2, 'Czlonek');

INSERT INTO Firefighter_Activities (firefighter_id, firefighter_activity_type, activity_date, expiration_date,
                                    description)
VALUES (1, 'szkol', '2025-07-01 09:00:00', '2026-07-01 09:00:00', 'Podstawowe szkolenie strazackie'),
       (2, 'pppm', '2025-06-20 10:00:00', '2026-06-20 10:00:00', 'Kurs pierwszej pomocy'),
       (3, 'cert', '2025-05-15 08:00:00', '2027-05-15 08:00:00', 'Zaawansowana certyfikacja');

INSERT INTO Assets (firedepartment_id, name, asset_type, description)
VALUES (1, 'Woz A', 'ciezki', 'MAN Stolarczyk'),
       (1, 'Waz A', 'w52', 'Waz w52 Kadimex'),
       (2, 'Zestaw ochronny', 'aodo', 'Ochrona drog oddechowych');

INSERT INTO Inspections (asset_id, inspection_type, inspection_date, expiration_date)
VALUES (1, 'kon_bezp', '2025-06-15 11:00:00', '2026-06-15 11:00:00'),
       (2, 'kons', '2025-07-05 14:00:00', '2026-07-05 14:00:00');

INSERT INTO Operations (firedepartment_id, operation_type, address_id, operation_date, description)
VALUES (1, 'akc_rat', 3, '2025-07-15 13:00:00', 'Akcja ratunkowa w Katowicach'),
       (2, 'cwi', 2, '2025-07-18 09:00:00', 'Miesieczne cwiczenia w Bedzinie');

INSERT INTO Participations (operation_id, firefighter_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (2, 3);

INSERT INTO Investment_Proposals (firedepartment_id, description, amount, submission_date, status)
VALUES (1, 'Nowy sprzet do remizy', 50000.00, '2025-07-10 12:00:00', 'oczekujace'),
       (2, 'Wymiana ubran ochronnych', 10000.00, '2025-07-12 15:30:00', 'zatwierdzone');

INSERT INTO Votes (proposal_id, firefighter_id, value, vote_date)
VALUES (1, 1, TRUE, '2025-07-11 10:00:00'),
       (1, 2, FALSE, '2025-07-11 10:05:00'),
       (2, 2, TRUE, '2025-07-13 09:00:00'),
       (2, 3, TRUE, '2025-07-13 09:15:00');
