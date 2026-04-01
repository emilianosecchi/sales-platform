INSERT INTO product (name, description, price, enabled, creation_date, category) VALUES
    ('Yerba Mate Premium 1kg', 'Yerba mate orgánica misionera', 4500.00, true, CURRENT_DATE, 'ALMACEN'),
    ('Arroz Integral 1kg', 'Grano largo fino calidad 0000', 2200.50, true, CURRENT_DATE, 'ALMACEN'),
    ('Heladera No Frost', 'Heladera inverter 400 litros acero inoxidable', 950000.00, true, CURRENT_DATE, 'ELECTRODOMESTICOS'),
    ('Teclado Mecánico AL68', 'Teclado custom 65% con switches lineares', 85000.00, true, CURRENT_DATE, 'INFORMATICA'),
    ('Monitor 27 Pulgadas', 'Panel IPS 144Hz 1ms respuesta', 320000.00, true, CURRENT_DATE, 'INFORMATICA'),
    ('iPhone 17 Pro', 'Aluminio color silver 256GB', 2100000.00, true, CURRENT_DATE, 'CELULARES'),
    ('Desodorante Axe Excite', 'Fragancia con notas de coco y avellana', 3500.00, true, CURRENT_DATE, 'PERFUMERIA'),
    ('Lavandina Original 2L', 'Máxima pureza para desinfección', 1800.00, true, CURRENT_DATE, 'LIMPIEZA'),
    ('Set de Herramientas 100pcs', 'Maletín reforzado con tubos y llaves', 120000.00, true, CURRENT_DATE, 'HERRAMIENTAS'),
    ('Alimento para Perros 15kg', 'Proteína premium para perros adultos', 45000.00, true, CURRENT_DATE, 'MASCOTAS'),
    ('Vino Malbec Reserva', 'Cosecha 2023 valle de uco', 12500.00, true, CURRENT_DATE, 'BEBIDAS'),
    ('Silla de Escritorio Ergonómica', 'Respaldo mesh y soporte lumbar', 180000.00, true, CURRENT_DATE, 'HOGAR');

INSERT INTO warehouse (name, province) VALUES
    ('Depósito Central La Plata', 'Buenos Aires'),
    ('Centro Logístico Córdoba', 'Córdoba'),
    ('Nodo Distribución Rosario', 'Santa Fe'),
    ('Almacén Regional Cuyo', 'Mendoza');

INSERT INTO stock (product_id, warehouse_id, quantity) VALUES
   -- Yerba Mate (ID 1): Disponible en dos puntos
   (1, 1, 5),
   (1, 2, 3),

   -- Arroz Integral (ID 2): Solo en el interior
   (2, 3, 8),

   -- Heladera No Frost (ID 3): Stock mínimo
   (3, 1, 1),

   -- Teclado Mecánico (ID 4): Stock elevado
   (4, 4, 10),

   -- Monitor (ID 5): Repartido
   (5, 1, 4),
   (5, 3, 3),

   -- iPhone 17 Pro (ID 6): Exclusivo y escaso
   (6, 1, 2),

   -- Desodorante Axe (ID 7): OMITIDO a propósito para testear stock 0

   -- Lavandina (ID 8): Solo en Córdoba
   (8, 2, 7),

   -- Set de Herramientas (ID 9):
   (9, 3, 6),

   -- Alimento para Perros (ID 10):
   (10, 4, 9),

   -- Vino Malbec (ID 11): Stock en origen (Cuyo)
   (11, 4, 10),

   -- Silla Ergonómica (ID 12):
   (12, 1, 5);