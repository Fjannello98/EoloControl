USE ecoviento;

INSERT INTO usuarios (nombre_usuario, contrasenia, rol, activo)
VALUES
('admin', 'admin123', 'ADMINISTRADOR', TRUE),
('operador1', 'operador123', 'OPERADOR', TRUE)
ON DUPLICATE KEY UPDATE rol = VALUES(rol), activo = VALUES(activo);

INSERT INTO centrales_eolicas (id_central, nombre, ubicacion, provincia)
VALUES
(1, 'Central Patagonia Norte', 'Comodoro Rivadavia', 'Chubut'),
(2, 'Central Vientos del Sur', 'Rio Gallegos', 'Santa Cruz')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), ubicacion = VALUES(ubicacion), provincia = VALUES(provincia);

INSERT INTO turbinas_eolicas (id_turbina, id_central, codigo, modelo, potencia_maxima_kw, estado)
VALUES
(1, 1, 'TUR-001', 'Nordex N100', 2500.00, 'OPERATIVA'),
(2, 1, 'TUR-002', 'Vestas V90', 2000.00, 'MANTENIMIENTO'),
(3, 2, 'TUR-003', 'Siemens SWT', 2300.00, 'OPERATIVA')
ON DUPLICATE KEY UPDATE modelo = VALUES(modelo), potencia_maxima_kw = VALUES(potencia_maxima_kw), estado = VALUES(estado);

INSERT INTO registros_telemetria
(id_registro, id_turbina, fecha_hora, velocidad_viento_kmh, direccion_viento, energia_generada_mwh)
VALUES
(1, 1, '2026-05-16 10:00:00', 42.50, 'SO', 12.80),
(2, 1, '2026-05-16 11:00:00', 38.00, 'SO', 10.90),
(3, 3, '2026-05-16 10:00:00', 12.00, 'O', 2.10)
ON DUPLICATE KEY UPDATE energia_generada_mwh = VALUES(energia_generada_mwh);

INSERT INTO alertas
(id_alerta, id_turbina, id_registro, fecha_hora, tipo, descripcion, severidad, atendida)
VALUES
(1, 3, 3, '2026-05-16 10:05:00', 'BAJA_GENERACION',
 'La energia generada es inferior a la esperada para la velocidad de viento registrada.',
 'MEDIA', FALSE)
ON DUPLICATE KEY UPDATE atendida = VALUES(atendida);
