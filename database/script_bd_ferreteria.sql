-- Borramos la bd si existe
DROP DATABASE IF EXISTS ferreteriaweb;

-- Creamos la bd
CREATE DATABASE ferreteriaweb;

-- Activamos la bd
USE ferreteriaweb;

-- ------------------------------------------------------------
-- 1. Tabla de Tipos de Usuario (Roles del Sistema)
-- ------------------------------------------------------------
CREATE TABLE tbl_tipo(
	id_tipo		INT PRIMARY KEY,
	descripcion VARCHAR(15) NOT NULL
);

INSERT INTO tbl_tipo VALUES (1, 'administrador');
INSERT INTO tbl_tipo VALUES (2, 'almacenero');
INSERT INTO tbl_tipo VALUES (3, 'vendedor');

-- ------------------------------------------------------------
-- 2. Tabla de Usuarios (Credenciales y acceso)
-- ------------------------------------------------------------
CREATE TABLE tbl_usuario (
	id_usuario INT AUTO_INCREMENT PRIMARY KEY,
	nombres VARCHAR(15) NOT NULL,
	apellidos VARCHAR(25) NOT NULL,
	cuenta VARCHAR(45) NOT NULL UNIQUE,
	clave VARCHAR(20) NOT NULL,
	fecha_nac DATE,
	activo BIT DEFAULT 1,
	id_tipo INT DEFAULT 2 CHECK(id_tipo IN (1, 2, 3)), 
	CONSTRAINT fk_usuario_tipo FOREIGN KEY (id_tipo) REFERENCES tbl_tipo(id_tipo)
);

-- Usuarios de prueba
INSERT INTO tbl_usuario VALUES (NULL, 'Admin', 'General', 'admin@ferreteria.com', 'admin', CURDATE(), DEFAULT, 1);
INSERT INTO tbl_usuario VALUES (NULL, 'Carlos', 'Carazas', 'almacen@ferreteria.com', '12345', CURDATE(), DEFAULT, 2);
INSERT INTO tbl_usuario VALUES (NULL, 'Manuel', 'Caparachin', 'ventas@ferreteria.com', '12345', CURDATE(), DEFAULT, 3);
INSERT INTO tbl_usuario VALUES (NULL, 'Yhojan', 'Morales', 'jramirez@ferreteria.com', '12345', CURDATE(), 0, 2);
INSERT INTO tbl_usuario VALUES (NULL, 'Jhordan', 'Ayala', 'storres@ferreteria.com', '12345', CURDATE(), DEFAULT, 3);

-- ------------------------------------------------------------
-- 3. Tabla de Categorías de Ferretería
-- ------------------------------------------------------------
CREATE TABLE tbl_categoria(
	id_categoria INT PRIMARY KEY,
	descripcion VARCHAR(45) NOT NULL
);

INSERT INTO tbl_categoria VALUES (1, 'Herramientas Manuales');
INSERT INTO tbl_categoria VALUES (2, 'Herramientas Eléctricas');
INSERT INTO tbl_categoria VALUES (3, 'Gasfitería y Plomería');
INSERT INTO tbl_categoria VALUES (4, 'Electricidad e Iluminación');
INSERT INTO tbl_categoria VALUES (5, 'Pinturas y Adhesivos');
INSERT INTO tbl_categoria VALUES (6, 'Fijaciones y Tornillería');
INSERT INTO tbl_categoria VALUES (7, 'Construcción y Albañilería');

-- ------------------------------------------------------------
-- 4. Tabla de Proveedores de Ferretería
-- ------------------------------------------------------------
CREATE TABLE tbl_proveedor(
	id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
	razon_social VARCHAR(45) NOT NULL,
	celular VARCHAR(10) NOT NULL,
	email VARCHAR(45) NOT NULL
);

INSERT INTO tbl_proveedor VALUES (NULL, 'Truper Perú S.A.C.', '987112233', 'ventas@truper.pe');
INSERT INTO tbl_proveedor VALUES (NULL, 'Stanley Black & Decker', '991234567', 'contacto@stanley.com');
INSERT INTO tbl_proveedor VALUES (NULL, 'Indeco Cables S.A.', '945678123', 'pedidos@indeco.pe');
INSERT INTO tbl_proveedor VALUES (NULL, 'Pavco Wavin Perú', '956789012', 'distribucion@pavco.com.pe');
INSERT INTO tbl_proveedor VALUES (NULL, 'Anypsa Corporation', '912345678', 'ventas@anypsa.com.pe');

-- ------------------------------------------------------------
-- 5. Tabla de Productos del Inventario
-- ------------------------------------------------------------
CREATE TABLE tbl_producto(
	id_producto INT AUTO_INCREMENT PRIMARY KEY,
	descripcion VARCHAR(45) NOT NULL,
	stock INT NOT NULL CHECK(stock >= 0),
	precio DECIMAL(10,2) NOT NULL CHECK(precio > 0),
	activo BIT DEFAULT 1,
	id_categoria INT,
	id_proveedor INT,
	CONSTRAINT fk_producto_categoria FOREIGN KEY (id_categoria) REFERENCES tbl_categoria(id_categoria),
	CONSTRAINT fk_producto_proveedor FOREIGN KEY (id_proveedor) REFERENCES tbl_proveedor(id_proveedor)
);

-- Herramientas Manuales (id_categoria = 1)
INSERT INTO tbl_producto VALUES (NULL, 'Martillo de uña 16oz Stanley', 45, 28.50, 1, 1, 2);
INSERT INTO tbl_producto VALUES (NULL, 'Alicate universal 8 pulg Truper', 60, 22.00, 1, 1, 1);
INSERT INTO tbl_producto VALUES (NULL, 'Juego destornilladores 6 pzas', 30, 35.00, 1, 1, 2);
INSERT INTO tbl_producto VALUES (NULL, 'Serrucho profesional 18 pulg', 15, 26.80, 0, 1, 2);

-- Herramientas Eléctricas (id_categoria = 2)
INSERT INTO tbl_producto VALUES (NULL, 'Taladro percutor 1/2 650W', 20, 189.90, 1, 2, 2);
INSERT INTO tbl_producto VALUES (NULL, 'Amoladora angular 4-1/2 800W', 18, 165.00, 1, 2, 1);
INSERT INTO tbl_producto VALUES (NULL, 'Lijadora orbital 200W', 12, 130.00, 1, 2, 2);

-- Gasfitería y Plomería (id_categoria = 3)
INSERT INTO tbl_producto VALUES (NULL, 'Tubo PVC 1/2 pulg x 3m Pavco', 150, 9.50, 1, 3, 4);
INSERT INTO tbl_producto VALUES (NULL, 'Codo PVC 1/2 x 90 Pavco', 300, 1.20, 1, 3, 4);
INSERT INTO tbl_producto VALUES (NULL, 'Pegamento PVC 1/4 galón', 40, 18.50, 1, 3, 4);
INSERT INTO tbl_producto VALUES (NULL, 'Cinta teflón 3/4 x 10m', 200, 2.50, 1, 3, 4);

-- Electricidad e Iluminación (id_categoria = 4)
INSERT INTO tbl_producto VALUES (NULL, 'Cable unipolar 2.5mm rojo x 100m', 25, 145.00, 1, 4, 3);
INSERT INTO tbl_producto VALUES (NULL, 'Cinta aislante negra 3M 20m', 180, 4.50, 1, 4, 3);
INSERT INTO tbl_producto VALUES (NULL, 'Interruptor simple Ticino', 90, 8.90, 1, 4, 3);
INSERT INTO tbl_producto VALUES (NULL, 'Foco LED 12W luz blanca Philips', 120, 7.50, 1, 4, 3);

-- Pinturas y Adhesivos (id_categoria = 5)
INSERT INTO tbl_producto VALUES (NULL, 'Pintura Látex Blanca 1 Gal Anypsa', 50, 42.00, 1, 5, 5);
INSERT INTO tbl_producto VALUES (NULL, 'Brocha cerda rubia 2 pulg', 85, 5.50, 1, 5, 1);
INSERT INTO tbl_producto VALUES (NULL, 'Silicona multiuso transparente', 65, 12.00, 1, 5, 5);
INSERT INTO tbl_producto VALUES (NULL, 'Thinner estándar 1 litro', 40, 9.00, 0, 5, 5);

-- Fijaciones y Tornillería (id_categoria = 6)
INSERT INTO tbl_producto VALUES (NULL, 'Tarugos de plástico 3/8 cj x 100', 80, 8.50, 1, 6, 1);
INSERT INTO tbl_producto VALUES (NULL, 'Tornillos autorroscantes 1 pulg x 100', 110, 11.50, 1, 6, 1);


-- ------------------------------------------------------------
-- 6. Tabla de Clientes
-- ------------------------------------------------------------
CREATE TABLE tbl_cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(45) NOT NULL,
    apellidos VARCHAR(45) NOT NULL,
    dni CHAR(8) NOT NULL UNIQUE CHECK (dni REGEXP '^[0-9]{8}$'),
    celular CHAR(9) NOT NULL CHECK (celular REGEXP '^9[0-9]{8}$'),
    activo BIT DEFAULT 1
);

INSERT INTO tbl_cliente VALUES (NULL, 'Carlos', 'Ramirez Torres', '74851236', '987654321', DEFAULT);
INSERT INTO tbl_cliente VALUES (NULL, 'Maria', 'Gonzales Perez', '62547891', '912345678', DEFAULT);
INSERT INTO tbl_cliente VALUES (NULL, 'Luis', 'Fernandez Rojas', '85123647', '965874123', DEFAULT);
INSERT INTO tbl_cliente VALUES (NULL, 'Ana', 'Torres Mendoza', '71234567', '945123789', DEFAULT);

-- ------------------------------------------------------------
-- 7. Tabla Cabecera de Boleta (Transacción de Ventas)
-- ------------------------------------------------------------
CREATE TABLE tbl_cab_boleta(
    num_boleta INT AUTO_INCREMENT PRIMARY KEY,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_usuario INT NOT NULL,
    id_cliente INT NOT NULL,
    CONSTRAINT fk_boleta_usuario FOREIGN KEY (id_usuario) REFERENCES tbl_usuario(id_usuario),
    CONSTRAINT fk_boleta_cliente FOREIGN KEY (id_cliente) REFERENCES tbl_cliente(id_cliente)
);

-- ------------------------------------------------------------
-- 8. Tabla Detalle de Boleta
-- ------------------------------------------------------------
CREATE TABLE tbl_det_boleta(
    num_boleta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_venta DECIMAL(8,2) NOT NULL,
    PRIMARY KEY (num_boleta, id_producto),
    CONSTRAINT fk_det_boleta_cab FOREIGN KEY (num_boleta) REFERENCES tbl_cab_boleta(num_boleta),
    CONSTRAINT fk_det_boleta_prod FOREIGN KEY (id_producto) REFERENCES tbl_producto(id_producto)
);

-- ------------------------------------------------------------
-- 9. Vistas para Reportes de Boleta (JasperReports)
-- ------------------------------------------------------------
DROP VIEW IF EXISTS v_header_boleta;
CREATE VIEW v_header_boleta AS
SELECT
    cab.num_boleta AS num_boleta,
    CONCAT('B001 - ', LPAD(cab.num_boleta, 8, '0')) AS numBoletaText,
    CONCAT(usu.nombres, ' ', usu.apellidos) AS usuario,
    CONCAT(cli.nombres, ' ', cli.apellidos) AS cliente,
    DATE_FORMAT(cab.fecha_registro, '%d/%m/%Y %h:%i:%s %p') AS fechaText
FROM tbl_cab_boleta AS cab
INNER JOIN tbl_usuario AS usu
    ON cab.id_usuario = usu.id_usuario
INNER JOIN tbl_cliente AS cli
    ON cab.id_cliente = cli.id_cliente;

DROP VIEW IF EXISTS v_detail_boleta;
CREATE VIEW v_detail_boleta AS
SELECT
    det.num_boleta AS num_boleta,
    det.id_producto AS id_producto,
    pro.descripcion AS descripcion,
    det.cantidad AS cantidad,
    det.precio_venta AS precio_venta,
    (det.cantidad * det.precio_venta) AS sub_total
FROM tbl_det_boleta AS det
INNER JOIN tbl_producto AS pro
    ON det.id_producto = pro.id_producto;

-- Datos iniciales de prueba para Boletas
INSERT INTO tbl_cab_boleta VALUES (1, NOW(), 1, 1);
INSERT INTO tbl_det_boleta VALUES (1, 1, 2, 28.50);
INSERT INTO tbl_det_boleta VALUES (1, 13, 1, 4.50);

INSERT INTO tbl_cab_boleta VALUES (2, NOW(), 2, 2);
INSERT INTO tbl_det_boleta VALUES (2, 5, 1, 189.90);
INSERT INTO tbl_det_boleta VALUES (2, 8, 3, 9.50);
