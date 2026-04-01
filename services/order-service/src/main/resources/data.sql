-- Se replica la información inicial que contiene la tabla product perteneciente
-- a la bd de product-inventory.

INSERT INTO product_snapshot (id, name, price, last_updated_at) VALUES
    (1, 'Yerba Mate Premium 1kg', 4500.00, CURRENT_TIMESTAMP),
    (2, 'Arroz Integral 1kg', 2200.50, CURRENT_TIMESTAMP),
    (3, 'Heladera No Frost', 950000.00, CURRENT_TIMESTAMP),
    (4, 'Teclado Mecánico AL68', 85000.00, CURRENT_TIMESTAMP),
    (5, 'Monitor 27 Pulgadas', 320000.00, CURRENT_TIMESTAMP),
    (6, 'iPhone 17 Pro', 2100000.00, CURRENT_TIMESTAMP),
    (7, 'Desodorante Axe Excite', 3500.00, CURRENT_TIMESTAMP),
    (8, 'Lavandina Original 2L', 1800.00, CURRENT_TIMESTAMP),
    (9, 'Set de Herramientas 100pcs', 120000.00, CURRENT_TIMESTAMP),
    (10, 'Alimento para Perros 15kg', 45000.00, CURRENT_TIMESTAMP),
    (11, 'Vino Malbec Reserva', 12500.00, CURRENT_TIMESTAMP),
    (12, 'Silla de Escritorio Ergonómica', 180000.00, CURRENT_TIMESTAMP);