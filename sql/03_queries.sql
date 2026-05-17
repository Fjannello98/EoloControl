USE ecoviento;

SELECT
    c.nombre AS central,
    t.codigo AS turbina,
    t.modelo,
    r.fecha_hora,
    r.velocidad_viento_kmh,
    r.energia_generada_mwh
FROM registros_telemetria r
INNER JOIN turbinas_eolicas t ON r.id_turbina = t.id_turbina
INNER JOIN centrales_eolicas c ON t.id_central = c.id_central
ORDER BY r.fecha_hora DESC;

SELECT
    t.codigo AS turbina,
    c.nombre AS central,
    COALESCE(e.energia_total_mwh, 0) AS energia_total_mwh,
    COALESCE(e.viento_promedio_kmh, 0) AS viento_promedio_kmh,
    COALESCE(a.alertas, 0) AS alertas
FROM turbinas_eolicas t
INNER JOIN centrales_eolicas c ON t.id_central = c.id_central
LEFT JOIN (
    SELECT id_turbina,
           SUM(energia_generada_mwh) AS energia_total_mwh,
           AVG(velocidad_viento_kmh) AS viento_promedio_kmh
    FROM registros_telemetria
    GROUP BY id_turbina
) e ON t.id_turbina = e.id_turbina
LEFT JOIN (
    SELECT id_turbina, COUNT(*) AS alertas
    FROM alertas
    GROUP BY id_turbina
) a ON t.id_turbina = a.id_turbina
ORDER BY energia_total_mwh DESC;
