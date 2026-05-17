# SQL del prototipo

Ejecutar en este orden:

```powershell
mysql -u root -p < sql/01_schema.sql
mysql -u root -p < sql/02_seed.sql
mysql -u root -p < sql/03_queries.sql
```

Para limpiar los datos de prueba:

```powershell
mysql -u root -p < sql/04_delete_test_data.sql
```
