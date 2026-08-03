DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM rental_transactions;
DELETE FROM maintenance_logs;
DELETE FROM equipment;
DELETE FROM products;
DELETE FROM members;
DELETE FROM users;

INSERT INTO users (id, display_name, email, password, role, created_at, is_deleted) VALUES
(1, 'Gym Admin', 'admin@eblouder.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'ADMIN', CURRENT_TIMESTAMP, false),
(2, 'Paulo B', 'Paulo@eblouder.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'ADMIN', CURRENT_TIMESTAMP, false),
(3, 'Alex Climber', 'alex@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'USER', CURRENT_TIMESTAMP, false),
(4, 'Sarah Boulderer', 'sarah@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'USER', CURRENT_TIMESTAMP, false),
(5, 'Jan Kowalski', 'jan@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'USER', CURRENT_TIMESTAMP, false),
(6, 'Anna Send', 'anna@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'USER', CURRENT_TIMESTAMP, false);

INSERT INTO members (id, user_id, join_date, membership_type, created_at, is_deleted) VALUES
(1, 3, '2025-01-10', 'ANNUAL', CURRENT_TIMESTAMP, false),
(2, 4, '2026-03-15', 'MONTHLY', CURRENT_TIMESTAMP, false),
(3, 5, CURRENT_DATE, 'PUNCH_PASS', CURRENT_TIMESTAMP, false),
(4, 6, '2026-07-01', 'MONTHLY', CURRENT_TIMESTAMP, false);

INSERT INTO equipment (id, brand, model, size, sku, purchase_date, type, condition, created_at, is_deleted) VALUES
(1, 'Scarpa', 'Reflex V', '40', 'R-SH-SC40-01', '2026-04-01', 'RENTAL_SHOE', 'GOOD', CURRENT_TIMESTAMP, false),
(2, 'Scarpa', 'Reflex V', '40', 'R-SH-SC40-02', '2026-04-01', 'RENTAL_SHOE', 'NEEDS_REPAIR', CURRENT_TIMESTAMP, false),
(3, 'La Sportiva', 'Tarantulace', '42', 'R-SH-LS42-01', '2025-11-20', 'RENTAL_SHOE', 'GOOD', CURRENT_TIMESTAMP, false),
(4, 'La Sportiva', 'Tarantulace', '38', 'R-SH-LS38-01', '2025-11-20', 'RENTAL_SHOE', 'RETIRED', CURRENT_TIMESTAMP, false),
(5, 'Petzl', 'Corax', 'M', 'R-HA-PZ-M-01', '2026-01-10', 'RENTAL_HARNESS', 'NEW', CURRENT_TIMESTAMP, false),
(6, 'Black Diamond', 'Momentum', 'L', 'R-HA-BD-L-01', '2026-01-10', 'RENTAL_HARNESS', 'GOOD', CURRENT_TIMESTAMP, false),
(7, 'FrictionLabs', 'Gorilla Grip', '5oz', 'RET-CHALK-01', CURRENT_DATE, 'RETAIL_CHALK', 'NEW', CURRENT_TIMESTAMP, false);

INSERT INTO products (id, name, description, price, stock_quantity, created_at, is_deleted) VALUES
(1, 'Premium Bouldering Chalk', 'Extra friction loose chalk, 100g', 12.50, 40, CURRENT_TIMESTAMP, false),
(2, 'Athletic Finger Tape', 'Standard 1.5 inch white tape', 5.00, 100, CURRENT_TIMESTAMP, false),
(3, 'Lapis Boar Hair Brush', 'Wooden brush for cleaning holds', 15.00, 25, CURRENT_TIMESTAMP, false),
(4, 'Energy Bar - Chocolate', 'Mid-session fuel', 3.50, 50, CURRENT_TIMESTAMP, false);

INSERT INTO maintenance_logs (id, equipment_id, date_sent, date_returned, cost, description, created_at, is_deleted) VALUES
(1, 2, '2026-07-20', NULL, 25.00, 'Resoling required on right toe box', CURRENT_TIMESTAMP, false),
(2, 4, '2026-02-15', '2026-03-01', 0.00, 'Buckle inspected, deemed unsafe, retired', CURRENT_TIMESTAMP, false);

INSERT INTO rental_transactions (id, member_id, equipment_id, check_out_time, expected_return_time, actual_return_time, created_at, is_deleted) VALUES
(1, 1, 3, '2026-08-01 14:00:00', '2026-08-01 18:00:00', '2026-08-01 17:30:00', CURRENT_TIMESTAMP, false), -- Returned historically
(2, 2, 5, '2026-08-02 10:00:00', '2026-08-02 12:00:00', '2026-08-02 12:15:00', CURRENT_TIMESTAMP, false), -- Returned historically
(3, 4, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + interval '3 hours', NULL, CURRENT_TIMESTAMP, false); -- ACTIVE RENTAL RIGHT NOW

INSERT INTO orders (id, user_id, status, created_at, is_deleted) VALUES
(1, 3, 'PAID', '2026-08-01 14:05:00', false),
(2, 4, 'PAID', '2026-08-02 10:05:00', false);

INSERT INTO order_items (id, order_id, product_id, quantity, price, created_at, is_deleted) VALUES
(1, 1, 1, 1, 12.50, '2026-08-01 14:05:00', false),
(2, 1, 2, 2, 10.00, '2026-08-01 14:05:00', false),
(3, 2, 3, 1, 15.00, '2026-08-02 10:05:00', false);

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('members_id_seq', (SELECT MAX(id) FROM members));
SELECT setval('equipment_id_seq', (SELECT MAX(id) FROM equipment));
SELECT setval('products_id_seq', (SELECT MAX(id) FROM products));
SELECT setval('maintenance_logs_id_seq', (SELECT MAX(id) FROM maintenance_logs));
SELECT setval('rental_transactions_id_seq', (SELECT MAX(id) FROM rental_transactions));
SELECT setval('orders_id_seq', (SELECT MAX(id) FROM orders));
SELECT setval('order_items_id_seq', (SELECT MAX(id) FROM order_items));