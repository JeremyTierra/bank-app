-- BaseDatos.sql
-- Script de creación de base de datos para la aplicación bancaria

-- Crear base de datos (ejecutar como administrador)
-- CREATE DATABASE bankdb;
-- \c bankdb;

-- Tabla Clientes (hereda de Persona)
CREATE TABLE IF NOT EXISTS clientes (
    cliente_id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    genero VARCHAR(50) NOT NULL,
    edad INTEGER NOT NULL,
    identificacion VARCHAR(50) NOT NULL UNIQUE,
    direccion VARCHAR(255) NOT NULL,
    telefono VARCHAR(50) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    estado BOOLEAN NOT NULL
);

-- Tabla Cuentas
CREATE TABLE IF NOT EXISTS cuentas (
    id BIGSERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(50) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(50) NOT NULL,
    saldo_inicial DECIMAL(10,2) NOT NULL,
    estado BOOLEAN NOT NULL,
    cliente_id BIGINT NOT NULL,
    CONSTRAINT fk_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id) ON DELETE CASCADE
);

-- Tabla Movimientos
CREATE TABLE IF NOT EXISTS movimientos (
    id BIGSERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL,
    tipo_movimiento VARCHAR(50) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    saldo DECIMAL(10,2) NOT NULL,
    cuenta_id BIGINT NOT NULL,
    CONSTRAINT fk_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuentas(id) ON DELETE CASCADE
);

-- Índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_clientes_identificacion ON clientes(identificacion);
CREATE INDEX IF NOT EXISTS idx_cuentas_numero ON cuentas(numero_cuenta);
CREATE INDEX IF NOT EXISTS idx_cuentas_cliente ON cuentas(cliente_id);
CREATE INDEX IF NOT EXISTS idx_movimientos_cuenta ON movimientos(cuenta_id);
CREATE INDEX IF NOT EXISTS idx_movimientos_fecha ON movimientos(fecha);

-- Datos de ejemplo (Caso de uso 1: Creación de Usuarios)
INSERT INTO clientes (nombre, genero, edad, identificacion, direccion, telefono, contrasena, estado)
VALUES 
    ('Jose Lema', 'Masculino', 30, '1234567890', 'Otavalo sn y principal', '098254785', '1234', true),
    ('Marianela Montalvo', 'Femenino', 28, '0987654321', 'Amazonas y NNUU', '097548965', '5678', true),
    ('Juan Osorio', 'Masculino', 35, '1122334455', '13 junio y Equinoccial', '098874587', '1245', true)
ON CONFLICT (identificacion) DO NOTHING;

-- Datos de ejemplo (Caso de uso 2: Creación de Cuentas de Usuario)
INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id)
VALUES 
    ('478758', 'Ahorro', 2000.00, true, (SELECT cliente_id FROM clientes WHERE identificacion = '1234567890')),
    ('225487', 'Corriente', 100.00, true, (SELECT cliente_id FROM clientes WHERE identificacion = '0987654321')),
    ('495878', 'Ahorros', 0.00, true, (SELECT cliente_id FROM clientes WHERE identificacion = '1122334455')),
    ('496825', 'Ahorros', 540.00, true, (SELECT cliente_id FROM clientes WHERE identificacion = '0987654321'))
ON CONFLICT (numero_cuenta) DO NOTHING;

-- Datos de ejemplo (Caso de uso 3: Nueva Cuenta Corriente para Jose Lema)
INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id)
VALUES 
    ('585545', 'Corriente', 1000.00, true, (SELECT cliente_id FROM clientes WHERE identificacion = '1234567890'))
ON CONFLICT (numero_cuenta) DO NOTHING;

-- Datos de ejemplo (Caso de uso 4: Realizar los siguientes movimientos)
-- Nota: En producción estos se crearían a través de la API, aquí solo para demostración
INSERT INTO movimientos (fecha, tipo_movimiento, valor, saldo, cuenta_id)
VALUES 
    -- Retiro de 575 de cuenta 478758 (Jose Lema - Ahorro)
    (TIMESTAMP '2022-02-10 10:00:00', 'Retiro', -575.00, 1425.00, (SELECT id FROM cuentas WHERE numero_cuenta = '478758')),
    -- Depósito de 600 en cuenta 225487 (Marianela Montalvo - Corriente)
    (TIMESTAMP '2022-02-10 11:00:00', 'Depósito', 600.00, 700.00, (SELECT id FROM cuentas WHERE numero_cuenta = '225487')),
    -- Depósito de 150 en cuenta 495878 (Juan Osorio - Ahorros)
    (TIMESTAMP '2022-02-10 12:00:00', 'Depósito', 150.00, 150.00, (SELECT id FROM cuentas WHERE numero_cuenta = '495878')),
    -- Retiro de 540 de cuenta 496825 (Marianela Montalvo - Ahorros)
    (TIMESTAMP '2022-02-08 09:00:00', 'Retiro', -540.00, 0.00, (SELECT id FROM cuentas WHERE numero_cuenta = '496825'));

-- Comentarios sobre las reglas de negocio implementadas:
-- 1. Los créditos tienen valores positivos, los débitos negativos
-- 2. El saldo se calcula y almacena en cada transacción
-- 3. Si el saldo < 0 al intentar un débito, se rechaza con "Saldo no disponible"
-- 4. Límite diario de retiro: $1000
-- 5. Si se excede el cupo diario: "Cupo diario Excedido"
-- 6. Los reportes se generan por rango de fechas y cliente
