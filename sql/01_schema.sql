CREATE DATABASE IF NOT EXISTS ecoviento;
USE ecoviento;

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasenia VARCHAR(100) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS centrales_eolicas (
    id_central INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(150) NOT NULL,
    provincia VARCHAR(80) NOT NULL
);

CREATE TABLE IF NOT EXISTS turbinas_eolicas (
    id_turbina INT AUTO_INCREMENT PRIMARY KEY,
    id_central INT NOT NULL,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    modelo VARCHAR(80) NOT NULL,
    potencia_maxima_kw DECIMAL(10,2) NOT NULL,
    estado VARCHAR(30) NOT NULL,
    CONSTRAINT fk_turbina_central
        FOREIGN KEY (id_central) REFERENCES centrales_eolicas(id_central)
);

CREATE TABLE IF NOT EXISTS registros_telemetria (
    id_registro INT AUTO_INCREMENT PRIMARY KEY,
    id_turbina INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    velocidad_viento_kmh DECIMAL(10,2) NOT NULL,
    direccion_viento VARCHAR(20) NOT NULL,
    energia_generada_mwh DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_registro_turbina
        FOREIGN KEY (id_turbina) REFERENCES turbinas_eolicas(id_turbina)
);

CREATE TABLE IF NOT EXISTS alertas (
    id_alerta INT AUTO_INCREMENT PRIMARY KEY,
    id_turbina INT NOT NULL,
    id_registro INT NULL,
    fecha_hora DATETIME NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    severidad VARCHAR(20) NOT NULL,
    atendida BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_alerta_turbina
        FOREIGN KEY (id_turbina) REFERENCES turbinas_eolicas(id_turbina),
    CONSTRAINT fk_alerta_registro
        FOREIGN KEY (id_registro) REFERENCES registros_telemetria(id_registro)
);
